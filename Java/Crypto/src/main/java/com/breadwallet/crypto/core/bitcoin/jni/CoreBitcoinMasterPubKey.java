package com.breadwallet.crypto.core.bitcoin.jni;

import com.breadwallet.crypto.api.bitcoin.BitcoinMasterPubKey;
import com.breadwallet.crypto.core.common.jni.JniReference;

public final class CoreBitcoinMasterPubKey extends JniReference implements BitcoinMasterPubKey {

    private static native long createBitcoinMasterPubKey (byte[] seed);

    public CoreBitcoinMasterPubKey(byte[] seed) {
        super(createBitcoinMasterPubKey(seed));
    }
}
