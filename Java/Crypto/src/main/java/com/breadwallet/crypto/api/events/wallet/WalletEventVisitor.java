package com.breadwallet.crypto.api.events.wallet;

public interface WalletEventVisitor<T> {

    T visit(BalanceUpdatedWalletEvent event);

    T visit(CreatedWalletEvent event);

    T visit(DeletedWalletEvent event);
}
