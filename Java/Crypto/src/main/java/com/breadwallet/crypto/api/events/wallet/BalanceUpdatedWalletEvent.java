package com.breadwallet.crypto.api.events.wallet;

import com.breadwallet.crypto.api.Amount;

public class BalanceUpdatedWalletEvent implements WalletEvent {

    private final Amount amount;

    public BalanceUpdatedWalletEvent(Amount amount) {
        this.amount = amount;
    }

    public Amount amount() {
        return amount;
    }

    @Override
    public <T> T accept(WalletEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("BalanceUpdatedWalletEvent(amount = %s)", amount);
    }
}
