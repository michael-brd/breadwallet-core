package com.breadwallet.crypto.core.bitcoin.jni;

import com.breadwallet.crypto.core.common.jni.JniReference;

public class CoreBitcoinWallet extends JniReference {

    // Maintain a reference to the owning wallet manager to avoid a user after free
    private final CoreBitcoinWalletManager owner;

    /* package */ CoreBitcoinWallet(long jniReferenceAddress, CoreBitcoinWalletManager owner) {
        super(jniReferenceAddress);
        this.owner = owner;
    }

    @Override
    protected void dispose() {
        // Intentionally omit call to disposeNative() as the jniReferenceAddress is borrowed
        // from the owning wallet manager
    }
}
