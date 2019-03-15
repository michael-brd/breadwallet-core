package com.breadwallet.crypto.core.bitcoin.jni;

import com.breadwallet.crypto.core.common.jni.JniReference;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class CoreBitcoinWalletManager extends JniReference {

    private static final Map<Long, WeakReference<CoreBitcoinWalletManager>> wmMap = new HashMap<>();

    private static native long createBitcoinWalletManager(CoreBitcoinMasterPubKey mpk,
                                                          CoreBitcoinChainParams params,
                                                          int earliestKeyTime,
                                                          String storagePath);

    private static native void initializeNative();

    private static CoreBitcoinWalletManager lookupWM (long wmid) {
        WeakReference<CoreBitcoinWalletManager> wm = wmMap.get(wmid);
        return (null== wm ? null : wm.get());
    }

    private static void handleTransactionAdded(long wmid, long wid, long tid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleTransactionAdded(wid, tid);
        }
    }

    private static void handleTransactionUpdated(long wmid, long wid, long tid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleTransactionUpdated(wid, tid);
        }
    }

    private static void handleTransactionDeleted(long wmid, long wid, long tid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleTransactionDeleted(wid, tid);
        }
    }

    private static void handleWalletCreated(long wmid, long wid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletCreated(wid);
        }
    }

    private static void handleWalletBalanceUpdated(long wmid, long wid, long satoshi) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletBalanceUpdated(wid, satoshi);
        }
    }

    private static void handleWalletDeleted(long wmid, long wid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletDeleted(wid);
        }
    }

    private static void handleWalletManagerConnected(long wmid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletManagerConnected();
        }
    }

    private static void handleWalletManagerDisconnected(long wmid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletManagerDisconnected();
        }
    }

    private static void handleWalletManagerSyncStarted(long wmid) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletManagerSyncStarted();
        }
    }

    private static void handleWalletManagerSyncStopped(long wmid, int errorCode) {
        CoreBitcoinWalletManager walletManager = lookupWM(wmid);
        if (null != walletManager) {
            walletManager.handleWalletManagerSyncStopped(errorCode);
        }
    }

    static { initializeNative(); }

    private final WeakReference<CoreBitcoinWalletManagerClient> client;

    public CoreBitcoinWalletManager(CoreBitcoinWalletManagerClient client,
                                    CoreBitcoinMasterPubKey mpk,
                                    CoreBitcoinChainParams params,
                                    int earliestKeyTime,
                                    String storagePath) {
        // ISSUE: we miss the first event because we are not registerd in the map yet, look into
        //        solutions
        super(createBitcoinWalletManager(mpk, params, earliestKeyTime, storagePath));

        wmMap.put(jniReferenceAddress, new WeakReference<> (this));

        this.client = new WeakReference<>(client);
    }

    public native void connect();

    public native void disconnect();

    @Override
    protected native void disposeNative();

    private void handleTransactionAdded(long wid, long tid) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            // TODO: Use tid to create transfer wrapper
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleTransactionAdded(wallet);
        }
    }

    private void handleTransactionUpdated(long wid, long tid) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            // TODO: Use tid to create transfer wrapper
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleTransactionUpdated(wallet);
        }
    }

    private void handleTransactionDeleted(long wid, long tid) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            // TODO: Use tid to create transfer wrapper
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleTransactionDeleted(wallet);
        }
    }

    private void handleWalletCreated(long wid) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleWalletCreated(wallet);
        }
    }

    private void handleWalletBalanceUpdated(long wid, long satoshi) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleWalletBalanceUpdated(wallet, satoshi);
        }
    }

    private void handleWalletDeleted(long wid) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            CoreBitcoinWallet wallet = new CoreBitcoinWallet(wid, this);
            wmc.handleWalletDeleted(wallet);
        }
    }

    private void handleWalletManagerConnected() {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            wmc.handleWalletManagerConnected();
        }
    }

    private void handleWalletManagerDisconnected() {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            wmc.handleWalletManagerDisconnected();
        }
    }

    private void handleWalletManagerSyncStarted() {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            wmc.handleWalletManagerSyncStarted();
        }
    }

    private void handleWalletManagerSyncStopped(int errorCode) {
        CoreBitcoinWalletManagerClient wmc = client.get();
        if (null != wmc) {
            // TODO: Convert error code to string properly
            wmc.handleWalletManagerSyncStopped(Integer.toString(errorCode));
        }
    }
}
