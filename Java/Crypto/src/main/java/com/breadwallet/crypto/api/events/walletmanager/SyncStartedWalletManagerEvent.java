package com.breadwallet.crypto.api.events.walletmanager;

public class SyncStartedWalletManagerEvent implements WalletManagerEvent {

    @Override
    public <T> T accept(WalletManagerEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SyncStartedWalletManagerEvent";
    }
}
