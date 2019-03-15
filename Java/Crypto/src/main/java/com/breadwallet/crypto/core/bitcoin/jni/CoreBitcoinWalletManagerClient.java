package com.breadwallet.crypto.core.bitcoin.jni;

public interface CoreBitcoinWalletManagerClient {

    void handleTransactionAdded(CoreBitcoinWallet wallet);
    void handleTransactionUpdated(CoreBitcoinWallet wallet);
    void handleTransactionDeleted(CoreBitcoinWallet wallet);

    void handleWalletCreated(CoreBitcoinWallet wallet);
    void handleWalletBalanceUpdated(CoreBitcoinWallet wallet, long satoshi);
    void handleWalletDeleted(CoreBitcoinWallet wallet);

    void handleWalletManagerConnected();
    void handleWalletManagerDisconnected();
    void handleWalletManagerSyncStarted();
    void handleWalletManagerSyncStopped(String error);
}
