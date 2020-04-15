package com.knoban.betterfriends.requests;

public class UpdateNameRequest implements RequestFulfillment {

    private String name;

    public UpdateNameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
