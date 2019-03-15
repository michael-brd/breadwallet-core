package com.breadwallet.crypto.api.events.walletmanager;

public interface WalletManagerEventVisitor<T> {

    T visit(ChangedWalletManagerEvent event);

    T visit(SyncEndedWalletManagerEvent event);

    T visit(SyncStartedWalletManagerEvent event);
}
