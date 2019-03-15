package com.breadwallet.crypto.core.bitcoin;

import com.breadwallet.crypto.api.bitcoin.BitcoinMasterPubKey;

/* package */ final class BitcoinMasterPubKeyAdapter {

    public static com.breadwallet.crypto.core.bitcoin.BitcoinMasterPubKey from(BitcoinMasterPubKey mbk) {
        if (mbk instanceof com.breadwallet.crypto.core.bitcoin.BitcoinMasterPubKey) {
            return (com.breadwallet.crypto.core.bitcoin.BitcoinMasterPubKey) mbk;
        }
        throw new IllegalArgumentException("Unsupported BitcoinMasterPubKey implementation");
    }
}
