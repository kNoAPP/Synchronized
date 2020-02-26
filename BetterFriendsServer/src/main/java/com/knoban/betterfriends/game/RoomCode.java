package com.knoban.betterfriends.game;

import java.util.Arrays;
import java.util.Random;

public class RoomCode {

    private char[] code;

    public RoomCode() {
        code = new char[4];

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for(int i=0; i<4; i++) {
            code[i] = (char) (rand.nextInt(26) + 65);
        }
    }

    public RoomCode(String s) {
        if(s.length() != 4)
            throw new IllegalArgumentException("Constructed room code does not have 4 characters!");

        s = s.toUpperCase();
        code = new char[4];
        for(int i=0; i<4; i++) {
            code[i] = s.charAt(i);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof RoomCode))
            return false;

        RoomCode rc = (RoomCode) o;
        for(int i=0; i<4; i++) {
            if(rc.code[i] != code[i])
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(code);
    }
}
