package com.breadwallet.crypto.api.factories;

import com.breadwallet.crypto.api.Network;

public interface NetworkFactory {

    Network testnet();

    Network mainnet();
}
