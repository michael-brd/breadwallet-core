package com.breadwallet.crypto.api;

import com.breadwallet.crypto.api.factories.AccountFactory;
import com.breadwallet.crypto.api.factories.NetworkFactory;
import com.breadwallet.crypto.api.factories.WalletManagerFactory;

public class CryptoApi {

    public interface Provider {

        AccountFactory accountFactory();

        WalletManagerFactory walletManagerFactory();

        NetworkFactory networkFactory();
    }

    private static Provider provider;

    public static synchronized void init(Provider provider) {
        if (null != CryptoApi.provider) throw new IllegalStateException("Provider already set");
        CryptoApi.provider = provider;
    }

    /* package */ static Provider provider() {
        return provider;
    }
}
