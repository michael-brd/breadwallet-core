package com.breadwallet.crypto.api.events.walletmanager;

import com.breadwallet.crypto.api.WalletManager.State;

public class ChangedWalletManagerEvent implements WalletManagerEvent {

    private final State oldState;
    private final State newState;

    public ChangedWalletManagerEvent(State oldState, State newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    public State oldState() {
        return oldState;
    }

    public State newState() {
        return newState;
    }

    @Override
    public <T> T accept(WalletManagerEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("ChangedWalletManagerEvent(old: %s, new: %s)", oldState, newState);
    }
}
