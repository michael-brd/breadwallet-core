package com.breadwallet.crypto.api.events.wallet;

public class CreatedWalletEvent implements WalletEvent {

    @Override
    public <T> T accept(WalletEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "CreatedWalletEvent";
    }
}
