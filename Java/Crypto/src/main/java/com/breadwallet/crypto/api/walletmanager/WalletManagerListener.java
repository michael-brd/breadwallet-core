package com.breadwallet.crypto.api.walletmanager;

import com.breadwallet.crypto.api.Transfer;
import com.breadwallet.crypto.api.Wallet;
import com.breadwallet.crypto.api.WalletManager;
import com.breadwallet.crypto.api.events.transfer.TransferEvent;
import com.breadwallet.crypto.api.events.wallet.WalletEvent;
import com.breadwallet.crypto.api.events.walletmanager.WalletManagerEvent;

public interface WalletManagerListener {

    void handleManagerEvent(WalletManager manager, WalletManagerEvent event);

    void handleTransferEvent(WalletManager manager, Wallet wallet, Transfer transfer, TransferEvent event);

    void handleWalletEvent(WalletManager manager, Wallet wallet, WalletEvent event);
}
