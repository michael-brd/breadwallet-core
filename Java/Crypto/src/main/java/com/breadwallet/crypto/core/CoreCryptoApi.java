package com.breadwallet.crypto.core;

import com.breadwallet.crypto.api.*;
import com.breadwallet.crypto.api.Account;
import com.breadwallet.crypto.api.WalletManager.Mode;
import com.breadwallet.crypto.api.bitcoin.BitcoinBackendClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinPersistenceClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinWalletManagerListener;
import com.breadwallet.crypto.api.factories.AccountFactory;
import com.breadwallet.crypto.api.factories.NetworkFactory;
import com.breadwallet.crypto.api.factories.WalletManagerFactory;
import com.breadwallet.crypto.core.bitcoin.BitcoinChainParams;
import com.breadwallet.crypto.core.bitcoin.BitcoinWalletManager;

import java.util.concurrent.Executor;

public class CoreCryptoApi implements CryptoApi.Provider {

    static {
        System.loadLibrary("crypto");
    }

    private final AccountFactory accountFactory = new AccountFactory() {
        @Override
        public Account create(String phrase) {
            return new com.breadwallet.crypto.core.Account(phrase);
        }

        @Override
        public Account create(byte[] seed) {
            return new com.breadwallet.crypto.core.Account(seed);
        }
    };

    private final WalletManagerFactory walletManagerFactory = new WalletManagerFactory() {
        @Override
        public WalletManager createBitcoinWalletManager(Account account,
                                                        Network network,
                                                        Mode mode,
                                                        int earliestKeyTime,
                                                        String storagePath,
                                                        BitcoinPersistenceClient persistenceClient,
                                                        BitcoinBackendClient backendClient,
                                                        BitcoinWalletManagerListener listener,
                                                        Executor listenerExecutor) {
            return new BitcoinWalletManager(account,
                    network,
                    mode,
                    earliestKeyTime,
                    storagePath,
                    persistenceClient,
                    backendClient,
                    listener,
                    listenerExecutor);
        }
    };

    private final NetworkFactory networkFactory = new NetworkFactory() {
        @Override
        public Network testnet() {
            return new Network(new Network.Bitcoin("BTC Testnet", 0x40, BitcoinChainParams.TESTNET));
        }

        @Override
        public Network mainnet() {
            // TODO: Implement this
            return null;
        }
    };

    @Override
    public AccountFactory accountFactory() {
        return accountFactory;
    }

    @Override
    public WalletManagerFactory walletManagerFactory() {
        return walletManagerFactory;
    }

    @Override
    public NetworkFactory networkFactory() {
        return networkFactory;
    }
}
