package com.breadwallet.crypto.core.jni;

// TODO: Expose a public static native method that wraps a private static native impl to
//       facilitate checks
public class Bip39 {

    public static native byte[] deriveKey(String phrase);
}
