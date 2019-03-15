package com.breadwallet.crypto.api.events.transfer;

public class DeletedTransferEvent implements TransferEvent {

    @Override
    public <T> T accept(TransferEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "DeletedTransferEvent";
    }
}
