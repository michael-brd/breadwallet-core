package com.breadwallet.crypto.api;

public interface Transfer {

    Wallet wallet();

    Address source();

    Address target();

    Amount amount();

    Amount fee();
}
