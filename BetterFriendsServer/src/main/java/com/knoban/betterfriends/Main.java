package com.knoban.betterfriends;

import com.knoban.betterfriends.server.BFServer;

import java.io.IOException;

public class Main {

    public static void main(String args[]) {
        try {
            BFServer server = new BFServer(25500);
            server.open();
            server.startProcessingRequests();
        } catch(IOException e) {
            System.out.println("Unable to create the BFServer: " + e.getMessage());
        }
    }
}
