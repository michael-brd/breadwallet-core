package com.breadwallet.crypto.api.events.transfer;

public class CreatedTransferEvent implements TransferEvent {

    @Override
    public <T> T accept(TransferEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "CreatedTransferEvent";
    }
}
