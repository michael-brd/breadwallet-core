package com.breadwallet.crypto.api.events.transfer;

public interface TransferEventVisitor<T> {

    T visit(CreatedTransferEvent event);

    T visit(DeletedTransferEvent event);
}
