package com.breadwallet.crypto.core;

import com.breadwallet.crypto.api.bitcoin.BitcoinMasterPubKey;
import com.breadwallet.crypto.core.jni.Bip39;

// TODO: Review visibility (for class, methods, fields, etc.)
public class Account implements com.breadwallet.crypto.api.Account {

    protected final BitcoinMasterPubKey masterPublicKey;

    public Account(String phrase) {
        this(Bip39.deriveKey(phrase));
    }

    public Account(byte[] seed) {
        this.masterPublicKey = new com.breadwallet.crypto.core.bitcoin.BitcoinMasterPubKey(seed);
    }

    @Override
    public BitcoinMasterPubKey masterPublicKey() {
        return masterPublicKey;
    }
}
