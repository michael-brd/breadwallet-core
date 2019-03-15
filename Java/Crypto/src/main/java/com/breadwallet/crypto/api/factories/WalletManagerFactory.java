package com.breadwallet.crypto.api.factories;

import com.breadwallet.crypto.api.Account;
import com.breadwallet.crypto.api.Network;
import com.breadwallet.crypto.api.WalletManager;
import com.breadwallet.crypto.api.WalletManager.Mode;
import com.breadwallet.crypto.api.bitcoin.BitcoinBackendClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinPersistenceClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinWalletManagerListener;

import java.util.concurrent.Executor;

public interface WalletManagerFactory {

    WalletManager createBitcoinWalletManager(Account account,
                                             Network network,
                                             Mode mode,
                                             int earliestKeyTime,
                                             String storagePath,
                                             BitcoinPersistenceClient persistenceClient,
                                             BitcoinBackendClient backendClient,
                                             BitcoinWalletManagerListener listener,
                                             Executor listenerExecutor);
}
