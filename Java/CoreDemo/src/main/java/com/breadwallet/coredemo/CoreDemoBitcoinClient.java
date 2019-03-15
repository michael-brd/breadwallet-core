package com.breadwallet.coredemo;

import android.util.Log;

import com.breadwallet.crypto.api.Account;
import com.breadwallet.crypto.api.Network;
import com.breadwallet.crypto.api.Transfer;
import com.breadwallet.crypto.api.Wallet;
import com.breadwallet.crypto.api.WalletManager;
import com.breadwallet.crypto.api.WalletManager.Mode;
import com.breadwallet.crypto.api.bitcoin.BitcoinBackendClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinPersistenceClient;
import com.breadwallet.crypto.api.bitcoin.BitcoinWalletManagerListener;
import com.breadwallet.crypto.api.events.transfer.CreatedTransferEvent;
import com.breadwallet.crypto.api.events.transfer.DeletedTransferEvent;
import com.breadwallet.crypto.api.events.transfer.TransferEvent;
import com.breadwallet.crypto.api.events.transfer.TransferEventVisitor;
import com.breadwallet.crypto.api.events.wallet.BalanceUpdatedWalletEvent;
import com.breadwallet.crypto.api.events.wallet.CreatedWalletEvent;
import com.breadwallet.crypto.api.events.wallet.DeletedWalletEvent;
import com.breadwallet.crypto.api.events.wallet.WalletEvent;
import com.breadwallet.crypto.api.events.wallet.WalletEventVisitor;
import com.breadwallet.crypto.api.events.walletmanager.ChangedWalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.SyncEndedWalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.SyncStartedWalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.WalletManagerEvent;
import com.breadwallet.crypto.api.events.walletmanager.WalletManagerEventVisitor;

import java.util.Map;
import java.util.concurrent.Executors;

public class CoreDemoBitcoinClient
        implements BitcoinWalletManagerListener, BitcoinBackendClient, BitcoinPersistenceClient {

    private static final String TAG = "CoreDemoBitcoinClient";

    private final WalletManager walletManager;

    public CoreDemoBitcoinClient(Network network, String storagePath, String paperKey) {
        this.walletManager = WalletManager.createBitcoinWalletManager(
                Account.create(paperKey),
                network,
                Mode.API_WITH_P2P_SUBMIT,
                1543190400,
                storagePath,
                this,
                this,
                this,
                Executors.newSingleThreadExecutor());
    }

    public void connect() {
        walletManager.connect();
    }

    public void disconnect() {
        walletManager.disconnect();
    }

    // BitcoinWalletManagerListener

    @Override
    public void handleManagerEvent(WalletManager manager, WalletManagerEvent event) {
        // toy example of the visitor pattern; generic can be used to extract information
        event.accept(new WalletManagerEventVisitor<Void>() {
            @Override
            public Void visit(ChangedWalletManagerEvent event) {
                Log.d(TAG, "handleManagerEvent: " + event.toString());
                return null;
            }

            @Override
            public Void visit(SyncEndedWalletManagerEvent event) {
                Log.d(TAG, "handleManagerEvent: " + event.toString());
                return null;
            }

            @Override
            public Void visit(SyncStartedWalletManagerEvent event) {
                Log.d(TAG, "handleManagerEvent: " + event.toString());
                return null;
            }
        });
    }

    @Override
    public void handleTransferEvent(WalletManager manager, Wallet wallet, Transfer transfer, TransferEvent event) {
        // toy example of the visitor pattern; generic can be used to extract information
        event.accept(new TransferEventVisitor<Void>() {
            @Override
            public Void visit(CreatedTransferEvent event) {
                Log.d(TAG, "handleTransferEvent: " + event.toString());
                return null;
            }

            @Override
            public Void visit(DeletedTransferEvent event) {
                Log.d(TAG, "handleTransferEvent: " + event.toString());
                return null;
            }
        });
    }

    @Override
    public void handleWalletEvent(WalletManager manager, Wallet wallet, WalletEvent event) {
        // toy example of the visitor pattern; generic can be used to extract information
        event.accept(new WalletEventVisitor<Void>() {
            @Override
            public Void visit(BalanceUpdatedWalletEvent event) {
                Log.d(TAG, "handleWalletEvent: " + event.toString());
                return null;
            }

            @Override
            public Void visit(CreatedWalletEvent event) {
                Log.d(TAG, "handleWalletEvent: " + event.toString());
                return null;
            }

            @Override
            public Void visit(DeletedWalletEvent event) {
                Log.d(TAG, "handleWalletEvent: " + event.toString());
                return null;
            }
        });
    }

    // BitcoinBackendClient

    @Override
    public boolean isReachable() {
        return true;
    }

    // BitcoinPersistenceClient

    @Override
    public void savePeers(WalletManager walletManager, Map<String, String> data) {

    }

    @Override
    public void saveBlocks(WalletManager walletManager, Map<String, String> data) {

    }

    @Override
    public void changeTransaction(WalletManager walletManager, ChangeType type, String hash, String data) {

    }
}
