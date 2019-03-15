package com.breadwallet.crypto.api.factories;

import com.breadwallet.crypto.api.Account;

public interface AccountFactory {

    Account create(String phrase);

    Account create(byte[] seed);
}
