package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinMasterPubKey;

public final class BitcoinMasterPubKey implements com.breadwallet.crypto.api.bitcoin.BitcoinMasterPubKey {

    private final CoreBitcoinMasterPubKey masterPubKey;

    public BitcoinMasterPubKey(byte[] seed) {
        // TODO: Add additional seed validation, if necessary
        if (null == seed) throw new IllegalArgumentException("Invalid seed");
        this.masterPubKey = new CoreBitcoinMasterPubKey(seed);
    }

    /* package */ CoreBitcoinMasterPubKey masterPubKey() {
        return masterPubKey;
    }
}
