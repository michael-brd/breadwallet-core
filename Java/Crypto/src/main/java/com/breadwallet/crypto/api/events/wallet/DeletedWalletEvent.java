package com.breadwallet.crypto.api.events.wallet;

public class DeletedWalletEvent implements WalletEvent {

    @Override
    public <T> T accept(WalletEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "DeletedWalletEvent";
    }
}
