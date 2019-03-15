package com.breadwallet.crypto.api;

public final class Bitcoin {

    public static final Currency CURRENCY = new Currency ("BTC", "\u20BF", "Bitcoin", 8, "SAT", "sat");

    public static final Unit SATOSHI = CURRENCY.baseUnit;
    public static final Unit BITCOIN = CURRENCY.defaultUnit;

    public static final Network TESTNET = CryptoApi.provider().networkFactory().testnet();

    public static final Network MAINNET = CryptoApi.provider().networkFactory().mainnet();
}
