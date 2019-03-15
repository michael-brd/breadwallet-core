package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.api.Amount;
import com.breadwallet.crypto.api.Currency;
import com.breadwallet.crypto.api.Transfer;
import com.breadwallet.crypto.api.Wallet;
import com.breadwallet.crypto.api.WalletManager;
import com.breadwallet.crypto.api.Bitcoin;
import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinWallet;

public final class BitcoinWallet implements Wallet {

    private final CoreBitcoinWallet wallet;
    private final BitcoinWalletManager walletManager;

    /* packate */ BitcoinWallet(CoreBitcoinWallet wallet, BitcoinWalletManager walletManager) {
        this.wallet = wallet;
        this.walletManager = walletManager;
    }

    @Override
    public WalletManager manager() {
        return walletManager;
    }

    @Override
    public String name() {
        return Bitcoin.CURRENCY.name;
    }

    @Override
    public Currency currency() {
        return Bitcoin.CURRENCY;
    }

    @Override
    public Amount balance() {
        // TODO: Implement this
        return null;
    }

    @Override
    public Transfer[] transfers() {
        // TODO: Implement this
        return new Transfer[0];
    }
}
