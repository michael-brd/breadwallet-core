package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.core.bitcoin.jni.CoreBitcoinChainParams;

public final class BitcoinChainParams implements com.breadwallet.crypto.api.bitcoin.BitcoinChainParams {

    // TODO: Add MAINNET
    public static BitcoinChainParams TESTNET = new BitcoinChainParams(CoreBitcoinChainParams.TESTNET);

    private final CoreBitcoinChainParams chainParams;

    private BitcoinChainParams(CoreBitcoinChainParams chainParams) {
        this.chainParams = chainParams;
    }

    /* package */ CoreBitcoinChainParams chainParams() {
        return chainParams;
    }
}
