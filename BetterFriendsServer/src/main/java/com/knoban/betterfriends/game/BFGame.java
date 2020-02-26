package com.knoban.betterfriends.game;

import com.knoban.betterfriends.server.BFServer;

import java.util.HashMap;

public class BFGame {

    private BFServer server;
    private BFPlayer host;
    private RoomCode roomCode;
    private HashMap<String, BFPlayer> players = new HashMap<String, BFPlayer>();

    public BFGame(BFServer server, BFPlayer host) {
        this.server = server;
        this.host = host;

        // Obviously not the best way to do this, but I doubt this game will be popular enough to matter
        RoomCode roomCode;
        do {
            roomCode = new RoomCode();
        } while(server.getGame(roomCode) != null);
        this.roomCode = roomCode;
    }

    public void open() {
        server.addGame(roomCode, this);
    }

    public void close() {
        server.removeGame(roomCode);
    }

    public void joinPlayer(BFPlayer player) {
        players.put(player.getName(), player);
    }

    public void quitPlayer(BFPlayer player) {
        players.remove(player.getName());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BFGame))
            return false;

        BFGame game = (BFGame) o;
        return game.roomCode.equals(roomCode);
    }

    @Override
    public int hashCode() {
        return roomCode.hashCode();
    }
}
