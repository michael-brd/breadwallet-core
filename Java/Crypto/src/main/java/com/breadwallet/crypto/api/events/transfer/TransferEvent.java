package com.breadwallet.crypto.api.events.transfer;

// TODO: Add changed event
public interface TransferEvent {

    <T> T accept(TransferEventVisitor<T> visitor);
}
