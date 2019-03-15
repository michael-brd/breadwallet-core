package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.api.Account;
import com.breadwallet.crypto.api.Amount;
import com.breadwallet.crypto.api.Network;
import com.breadwallet.crypto.api.Wallet;
import com.breadwallet.crypto.api.WalletManager;
import com.breadwallet.crypto.api.Bitcoin;
import com.breadwallet.crypto.api.bitcoin.BitcoinBackendClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinPersistenceClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinWalletManagerListener;
import com.breadwallet.crypto.api.events.wallet.BalanceUpdatedWalletEvent;
import com.breadwallet.crypto.api.events.wallet.CreatedWalletEvent;
import com.breadwallet.crypto.api.events.wallet.DeletedWalletEvent;
import com.breadwallet.crypto.api.events.walletmanager.ChangedWalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.SyncEndedWalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.SyncStartedWalletManagerEvent;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinChainParams;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinMasterPubKey;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinWallet;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinWalletManager;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinWalletManagerClient;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

// TODO: Verify assumption that CoreBitcoinWalletManagerClient callbacks are on single native thread
public final class BitcoinWalletManager extends WalletManager implements CoreBitcoinWalletManagerClient {

    private final WeakReference<BitcoinWalletManagerListener> listener;
    private final CoreBitcoinWalletManager coreWalletManager;

    private final BitcoinPersistenceClient persistenceClient;
    private final BitcoinBackendClient backendClient;

    private final Account account;
    private final Network network;
    private final Mode mode;
    private final long earliestKeyTime;
    private final String storagePath;
    private final Executor listenerExecutor;

    private State state;

    public BitcoinWalletManager(Account account,
                                Network network,
                                Mode mode,
                                int earliestKeyTime,
                                String storagePath,
                                BitcoinPersistenceClient persistenceClient,
                                BitcoinBackendClient backendClient,
                                BitcoinWalletManagerListener listener,
                                Executor listenerExecutor) {
        if (null == account) throw new IllegalArgumentException("Invalid seed");
        if (null == network) throw new IllegalArgumentException("Invalid network");
        if (null == mode) throw new IllegalArgumentException("Invalid mode");
        if (earliestKeyTime < 0) throw new IllegalArgumentException("Invalid earliest key time");
        if (null == storagePath) throw new IllegalArgumentException("Invalid storage path");
        if (null == persistenceClient) throw new IllegalArgumentException("Invalid persistence client");
        if (null == backendClient) throw new IllegalArgumentException("Invalid backend client");
        if (null == listener) throw new IllegalArgumentException("Invalid listener");
        if (null == listenerExecutor) throw new IllegalArgumentException("Invalid listener executor");

        CoreBitcoinChainParams chainParams = BitcoinChainParamsAdapter.from(network.chainParams()).chainParams();
        CoreBitcoinMasterPubKey masterPubKey = BitcoinMasterPubKeyAdapter.from(account.masterPublicKey()).masterPubKey();

        this.listenerExecutor = listenerExecutor;
        this.account = account;
        this.network = network;
        this.mode = mode;
        this.earliestKeyTime = earliestKeyTime;
        this.storagePath = storagePath;
        this.state = State.CREATED;

        this.persistenceClient = persistenceClient;
        this.backendClient = backendClient;

        this.listener = new WeakReference<>(listener);
        this.coreWalletManager = new CoreBitcoinWalletManager(this, masterPubKey, chainParams, earliestKeyTime, storagePath);
    }

    // WalletManager

    @Override
    public void connect() {
        coreWalletManager.connect();
    }

    @Override
    public void disconnect() {
        coreWalletManager.disconnect();
    }

    // CoreWalletManagerClient

    @Override
    public void handleTransactionAdded(CoreBitcoinWallet coreWallet) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                // TODO: Implement this
            }
        });
    }

    @Override
    public void handleTransactionUpdated(CoreBitcoinWallet coreWallet) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                // TODO: Implement this
            }
        });
    }

    @Override
    public void handleTransactionDeleted(CoreBitcoinWallet coreWallet) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                // TODO: Implement this
            }
        });
    }

    @Override
    public void handleWalletCreated(CoreBitcoinWallet coreWallet) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                Wallet wallet = new BitcoinWallet(coreWallet, this);
                l.handleWalletEvent(this, wallet, new CreatedWalletEvent());
            }
        });
    }

    @Override
    public void handleWalletBalanceUpdated(CoreBitcoinWallet coreWallet, long satoshi) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                Wallet wallet = new BitcoinWallet(coreWallet, this);
                Amount amount = new Amount(satoshi, Bitcoin.SATOSHI);
                l.handleWalletEvent(this, wallet, new BalanceUpdatedWalletEvent(amount));
            }
        });
    }

    @Override
    public void handleWalletDeleted(CoreBitcoinWallet coreWallet) {
        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                Wallet wallet = new BitcoinWallet(coreWallet, this);
                l.handleWalletEvent(this, wallet, new DeletedWalletEvent());
            }
        });
    }

    @Override
    public void handleWalletManagerConnected() {
        State oldState = state;
        state = State.CONNECTED;
        State newState = state;

        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                l.handleManagerEvent(this, new ChangedWalletManagerEvent(oldState, newState));
            }
        });
    }

    @Override
    public void handleWalletManagerDisconnected() {
        State oldState = state;
        state = State.DISCONNECTED;
        State newState = state;

        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                l.handleManagerEvent(this, new ChangedWalletManagerEvent(oldState, newState));
            }
        });
    }

    @Override
    public void handleWalletManagerSyncStarted() {
        State oldState = state;
        state = State.SYNCING;
        State newState = state;

        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                l.handleManagerEvent(this, new ChangedWalletManagerEvent(oldState, newState));
                l.handleManagerEvent(this, new SyncStartedWalletManagerEvent());
            }
        });
    }

    @Override
    public void handleWalletManagerSyncStopped(String error) {
        State oldState = state;
        state = State.CONNECTED;
        State newState = state;

        listenerExecutor.execute(() -> {
            BitcoinWalletManagerListener l = listener.get();
            if (l != null) {
                l.handleManagerEvent(this, new ChangedWalletManagerEvent(oldState, newState));
                l.handleManagerEvent(this, new SyncEndedWalletManagerEvent(error));
            }
        });
    }
}
