package com.breadwallet.crypto.api.events.wallet;

// TODO: Add transferAdded, transferChanged, transferDeleted & feeBasisUpdated events
public interface WalletEvent {

    <T> T accept(WalletEventVisitor<T> visitor);
}
