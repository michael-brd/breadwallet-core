package com.breadwallet.crypto.api;

import com.breadwallet.crypto.api.bitcoin.BitcoinChainParams;

// TODO: Revisit this class and consider moving to interfaces along with the Visitor pattern
public class Network {

    enum Type { BITCOIN, BITCASH, ETHEREUM}

    public static final class Bitcoin {
        final String name;
        final int forkId;
        final BitcoinChainParams chainParams;

        public Bitcoin(String name, int forkId, BitcoinChainParams chainParams) {
            this.name = name;
            this.forkId = forkId;
            this.chainParams = chainParams;
        }
    }

    public static final class Bitcash {
        final String name;
        final int forkId;
        final BitcoinChainParams chainParams;

        public Bitcash(String name, int forkId, BitcoinChainParams chainParams) {
            this.name = name;
            this.forkId = forkId;
            this.chainParams = chainParams;
        }
    }

    public static final class Ethereum {
        final String name;
        final int chainId;

        public Ethereum(String name, int chainId) {
            this.name = name;
            this.chainId = chainId;
        }
    }

    private final Type type;
    private final Bitcoin bitcoin;
    private final Bitcash bitcash;
    private final Ethereum ethereum;

    private Network(Type type, Bitcoin bitcoin, Bitcash bitcash, Ethereum ethereum) {
        this.type = type;
        this.bitcoin = bitcoin;
        this.bitcash = bitcash;
        this.ethereum = ethereum;
    }

    public Network (Bitcoin bitcoin) {
        this (Type.BITCOIN, bitcoin, null, null);
    }

    public Network (Bitcash bitcash) {
        this (Type.BITCASH, null, bitcash, null);
    }

    public Network (Ethereum ethereum) {
        this (Type.ETHEREUM, null, null, ethereum);
    }

    public String name() {
        switch (type) {
            case BITCOIN: return bitcoin.name;
            case BITCASH: return bitcash.name;
            case ETHEREUM: return ethereum.name;
            default: throw new IllegalStateException();
        }
    }

    public int forkId() {
        switch (type) {
            case BITCOIN: return bitcoin.forkId;
            case BITCASH: return bitcash.forkId;
            default: throw new IllegalStateException();
        }
    }

    public BitcoinChainParams chainParams() {
        switch (type) {
            case BITCOIN: return bitcoin.chainParams;
            case BITCASH: return bitcash.chainParams;
            default: throw new IllegalStateException();
        }
    }
}
