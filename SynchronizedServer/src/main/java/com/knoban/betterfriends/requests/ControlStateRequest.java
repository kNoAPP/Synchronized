package com.knoban.betterfriends.requests;

public class ControlStateRequest implements RequestFulfillment {

    private byte state;

    public ControlStateRequest(byte state) {
        this.state = state;
    }

    public byte getState() {
        return state;
    }
}
