package com.knoban.betterfriends.requests;

public class JoinGameRequest implements RequestFulfillment {

    private String code;

    public JoinGameRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
