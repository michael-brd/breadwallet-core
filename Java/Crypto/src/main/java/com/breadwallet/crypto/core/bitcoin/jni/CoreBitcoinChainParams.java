package com.breadwallet.crypto.core.bitcoin.jni;

import com.breadwallet.crypto.core.common.jni.JniReference;

public final class CoreBitcoinChainParams extends JniReference {

    public static CoreBitcoinChainParams TESTNET = new CoreBitcoinChainParams(createTestnetChainParams());

    private static native long createTestnetChainParams();

    private CoreBitcoinChainParams(long jniReferenceAddress) {
        super(jniReferenceAddress);
    }
}
