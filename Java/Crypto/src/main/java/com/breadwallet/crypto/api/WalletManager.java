package com.breadwallet.crypto.api;

import com.breadwallet.crypto.api.bitcoin.BitcoinBackendClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinPersistenceClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinWalletManagerListener;
import com.breadwallet.crypto.api.factories.WalletManagerFactory;

import java.util.concurrent.Executor;

public abstract class WalletManager {

    private static final WalletManagerFactory FACTORY = CryptoApi.provider().walletManagerFactory();

    public static WalletManager createBitcoinWalletManager(Account account,
                                                           Network network,
                                                           Mode mode,
                                                           int earliestKeyTime,
                                                           String storagePath,
                                                           BitcoinPersistenceClient persistenceClient,
                                                           BitcoinBackendClient backendClient,
                                                           BitcoinWalletManagerListener listener,
                                                           Executor listenerExecutor) {
        return FACTORY.createBitcoinWalletManager(account,
                network,
                mode,
                earliestKeyTime,
                storagePath,
                persistenceClient,
                backendClient,
                listener,
                listenerExecutor);
    }

    public enum Mode { API_ONLY, API_WITH_P2P_SUBMIT, P2P_WITH_API_SYNC, P2P_ONLY, }

    public enum State { CREATED, DISCONNECTED, CONNECTED, SYNCING, DELETED }

    public abstract void connect();

    public abstract void disconnect();
}
