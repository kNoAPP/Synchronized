package com.knoban.betterfriends.utils;

import java.net.Socket;
import java.util.Random;

public class Tools {

    public static int randomNumber(int min, int max) {
        Random rand = new Random();
        int val = rand.nextInt(max - min + 1) + min;
        return val;
    }

    public static String formatSocket(Socket socket) {
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}
