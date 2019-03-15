//  Copyright (c) 2019 breadwallet LLC.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.

#include <stdlib.h>
#include <assert.h>

#include "BRWalletManager.h"

#include "BRCryptoJni.h"
#include "com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager.h"

static jclass trampolineClass = NULL;
static jmethodID trampolineHandleTransactionAdded = NULL;
static jmethodID trampolineHandleTransactionUpdated = NULL;
static jmethodID trampolineHandleTransactionDeleted = NULL;

static jmethodID trampolineHandleWalletCreated = NULL;
static jmethodID trampolineHandleWalletBalanceUpdated = NULL;
static jmethodID trampolineHandleWalletDeleted = NULL;

static jmethodID trampolineHandleWalletManagerConnected = NULL;
static jmethodID trampolineHandleWalletManagerDisconnected = NULL;
static jmethodID trampolineHandleWalletManagerSyncStarted = NULL;
static jmethodID trampolineHandleWalletManagerSyncStopped = NULL;

static void
transactionEventCallback (BRWalletManager manager,
                          BRWallet *wallet,
                          BRTransaction *transaction,
                          BRTransactionEvent event);

static void
walletEventCallback (BRWalletManager manager,
                     BRWallet *wallet,
                     BRWalletEvent event);

static void
walletManagerEventCallback (BRWalletManager manager,
                            BRWalletManagerEvent event);

static jmethodID
trampolineOrFatal (JNIEnv *env, const char *name, const char *signature);

JNIEXPORT void JNICALL Java_com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager_initializeNative (
        JNIEnv * env,
        jclass thisClass)
{
    trampolineClass = (*env)->NewGlobalRef(env, thisClass);

    trampolineHandleTransactionAdded       = trampolineOrFatal (env, "handleTransactionAdded",             "(JJJ)V");
    trampolineHandleTransactionUpdated     = trampolineOrFatal (env, "handleTransactionUpdated",           "(JJJ)V");
    trampolineHandleTransactionDeleted     = trampolineOrFatal (env, "handleTransactionDeleted",           "(JJJ)V");

    trampolineHandleWalletCreated          = trampolineOrFatal (env, "handleWalletCreated",                "(JJ)V");
    trampolineHandleWalletBalanceUpdated   = trampolineOrFatal (env, "handleWalletBalanceUpdated",         "(JJJ)V");
    trampolineHandleWalletDeleted          = trampolineOrFatal (env, "handleWalletDeleted",                "(JJ)V");

    trampolineHandleWalletManagerConnected    = trampolineOrFatal (env, "handleWalletManagerConnected",    "(J)V");
    trampolineHandleWalletManagerDisconnected = trampolineOrFatal (env, "handleWalletManagerDisconnected", "(J)V");
    trampolineHandleWalletManagerSyncStarted  = trampolineOrFatal (env, "handleWalletManagerSyncStarted",  "(J)V");
    trampolineHandleWalletManagerSyncStopped  = trampolineOrFatal (env, "handleWalletManagerSyncStopped",  "(JI)V");
}

JNIEXPORT jlong JNICALL Java_com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager_createBitcoinWalletManager (
        JNIEnv * env,
        jclass thisClass,
        jobject masterPublicKeyObject,
        jobject chainParamsObject,
        jint earliestKeyTime,
        jstring storagePathString)
{
    assert (!(*env)->IsSameObject (env, masterPublicKeyObject, NULL));
    assert (!(*env)->IsSameObject (env, chainParamsObject, NULL));
    assert (!(*env)->IsSameObject (env, storagePathString, NULL));
    assert (earliestKeyTime >= 0);

    BRMasterPubKey *masterPubKey = (BRMasterPubKey *) getJNIReference(env, masterPublicKeyObject);
    BRChainParams *chainParams   = (BRChainParams *) getJNIReference(env, chainParamsObject);
    const char *storagePath = (*env)->GetStringUTFChars (env, storagePathString, 0);

    BRWalletManagerClient client = { transactionEventCallback, walletEventCallback, walletManagerEventCallback };
    BRWalletManager *walletManager = BRWalletManagerNew(client, *masterPubKey, chainParams, earliestKeyTime, storagePath);

    return (jlong) walletManager;
}

JNIEXPORT void JNICALL Java_com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager_connect (
        JNIEnv * env,
        jobject thisObject)
{
    BRWalletManager *walletManager = (BRWalletManager *) getJNIReference(env, thisObject);
    BRWalletManagerConnect(walletManager);
}

JNIEXPORT void JNICALL Java_com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager_disconnect (
        JNIEnv * env,
        jobject thisObject)
{
    BRWalletManager *walletManager = (BRWalletManager *) getJNIReference(env, thisObject);
    BRWalletManagerDisconnect(walletManager);
}

JNIEXPORT void JNICALL Java_com_breadwallet_crypto_core_bitcoin_jni_CoreBitcoinWalletManager_disposeNative (
        JNIEnv * env,
        jobject thisObject)
{
    BRWalletManager *walletManager = (BRWalletManager *) getJNIReference(env, thisObject);
    if (NULL != walletManager) BRWalletManagerFree(walletManager);
}


static jmethodID
trampolineOrFatal (JNIEnv *env, const char *name, const char *signature) {
    jmethodID method = (*env)->GetStaticMethodID (env, trampolineClass, name, signature);
    assert (NULL != method);
    return method;
}


static void
transactionEventCallback (BRWalletManager wmid,
                          BRWallet *wid,
                          BRTransaction *tid,
                          BRTransactionEvent e) {
    JNIEnv *env = getEnv();
    if (NULL == env) return;

    switch (e.type) {
        case BITCOIN_TRANSACTION_ADDED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleTransactionAdded, (jlong) wmid, (jlong) wid, (jlong) tid);
            break;
        case BITCOIN_TRANSACTION_UPDATED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleTransactionUpdated, (jlong) wmid, (jlong) wid, (jlong) tid);
            break;
        case BITCOIN_TRANSACTION_DELETED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleTransactionDeleted, (jlong) wmid, (jlong) wid, (jlong) tid);
            break;
    }

}

static void
walletEventCallback (BRWalletManager wmid,
                     BRWallet *wid,
                     BRWalletEvent e) {
    JNIEnv *env = getEnv();
    if (NULL == env) return;

    switch (e.type) {
        case BITCOIN_WALLET_CREATED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletCreated, (jlong) wmid, (jlong) wid);
            break;
        case BITCOIN_WALLET_BALANCE_UPDATED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletBalanceUpdated, (jlong) wmid, (jlong) wid, (jlong) e.u.balance.satoshi);
            break;
        case BITCOIN_WALLET_DELETED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletDeleted, (jlong) wmid, (jlong) wid);
            break;
    }
}

static void
walletManagerEventCallback (BRWalletManager wmid,
                            BRWalletManagerEvent e) {
    JNIEnv *env = getEnv();
    if (NULL == env) return;

    switch (e.type) {
        case BITCOIN_WALLET_MANAGER_CONNECTED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletManagerConnected, (jlong) wmid);
            break;
        case BITCOIN_WALLET_MANAGER_DISCONNECTED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletManagerDisconnected, (jlong) wmid);
            break;
        case BITCOIN_WALLET_MANAGER_SYNC_STARTED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletManagerSyncStarted, (jlong) wmid);
            break;
        case BITCOIN_WALLET_MANAGER_SYNC_STOPPED:
            (*env)->CallStaticVoidMethod(env, trampolineClass, trampolineHandleWalletManagerSyncStopped, (jlong) wmid, (jint) e.u.syncStopped.error);
            break;
    }
}
