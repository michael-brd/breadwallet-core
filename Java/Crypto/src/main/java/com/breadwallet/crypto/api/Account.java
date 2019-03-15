package com.breadwallet.crypto.api;

import com.breadwallet.crypto.api.bitcoin.BitcoinMasterPubKey;
import com.breadwallet.crypto.api.factories.AccountFactory;

public abstract class Account {

    private static AccountFactory FACTORY = CryptoApi.provider().accountFactory();

    public static Account create(String phrase) {
        return FACTORY.create(phrase);
    }

    public static Account create(byte[] seed) {
        return FACTORY.create(seed);
    }

    public abstract BitcoinMasterPubKey masterPublicKey();
}
