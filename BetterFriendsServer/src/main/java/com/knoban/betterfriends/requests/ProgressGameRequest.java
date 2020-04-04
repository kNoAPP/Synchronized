package com.knoban.betterfriends.requests;

public class ProgressGameRequest implements RequestFulfillment {

    private byte to;

    public ProgressGameRequest(byte to) {
        this.to = to;
    }

    public byte getTo() {
        return to;
    }
}
