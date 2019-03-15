package com.breadwallet.crypto.api;

public interface Wallet {

    WalletManager manager();

    String name();

    Currency currency();

    Amount balance();

    Transfer[] transfers();
}
