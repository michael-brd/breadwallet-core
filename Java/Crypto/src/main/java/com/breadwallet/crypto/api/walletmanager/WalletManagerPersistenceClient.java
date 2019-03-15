package com.breadwallet.crypto.api.walletmanager;

import com.breadwallet.crypto.api.WalletManager;

import java.util.Map;

public interface WalletManagerPersistenceClient {

    enum ChangeType { ADDED, UPDATED, DELETED }

    void savePeers(WalletManager walletManager, Map<String, String> data);

    void saveBlocks(WalletManager walletManager, Map<String, String> data);

    void changeTransaction(WalletManager walletManager, ChangeType type, String hash, String data);
}
