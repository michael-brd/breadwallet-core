package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.api.bitcoin.BitcoinChainParams;

/* package */ final class BitcoinChainParamsAdapter {

    public static com.breadwallet.crypto.core.bitcoin.BitcoinChainParams from(BitcoinChainParams bcp) {
        if (bcp instanceof com.breadwallet.crypto.core.bitcoin.BitcoinChainParams) {
            return (com.breadwallet.crypto.core.bitcoin.BitcoinChainParams) bcp;
        }
        throw new IllegalArgumentException("Unsupported BitcoinChainParams implementation");
    }
}
