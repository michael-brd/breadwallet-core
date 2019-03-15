package com.breadwallet.crypto.api.events.walletmanager;

// TODO: Add walletAdded, walletChanged, walletDeleted, syncStarted, syncProgress & syncEnded events
public interface WalletManagerEvent {

    <T> T accept(WalletManagerEventVisitor<T> visitor);
}
