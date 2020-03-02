package com.knoban.betterfriends.game;

import com.knoban.betterfriends.requests.RequestCode;
import com.knoban.betterfriends.server.BFServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class BFGame {

    public static final byte MAX_PLAYERS = 8;

    private BFServer server;
    protected BFPlayer host;
    protected RoomCode roomCode;

    protected HashMap<String, BFPlayer> players = new HashMap<String, BFPlayer>();
    protected ArrayList<HashSet<BFPlayer>> roles = new ArrayList<HashSet<BFPlayer>>();

    protected byte state;
    protected byte round;

    public BFGame(BFServer server, BFPlayer host) {
        this.server = server;
        this.host = host;
        this.state = GameState.LOBBY;
        this.round = 0;
        host.game = this;

        // Two roles
        roles.add(new HashSet<BFPlayer>());
        roles.add(new HashSet<BFPlayer>());

        // Obviously not the best way to do this, but I doubt this game will be popular enough to matter
        RoomCode roomCode;
        do {
            roomCode = new RoomCode();
        } while(server.getGame(roomCode) != null);
        this.roomCode = roomCode;

        System.out.println("Created game [" + roomCode + "] with host: " + host.getUUID());
    }

    public void open() {
        server.addGame(roomCode, this);
    }

    public void close() {
        players.values().stream().forEach((p) -> p.game = null);
        players.clear();
        server.removeGame(roomCode);

        System.out.println("Closed game [" + roomCode + "] with host: " + host.getUUID());
    }

    public void joinPlayer(BFPlayer player) {
        players.put(player.name, player);
        player.game = this;

        try {
            host.out.writeS16(RequestCode.HANDSHAKE);
            host.out.writeS8(RequestCode.HOST_ADD_PLAYER);
            host.out.writeS16((short) (player.name.length() + 1));
            host.out.writeString(player.name);
        } catch(IOException e) {
            System.out.println("Failed to alert host of joining player.");
        }
    }

    public void quitPlayer(BFPlayer player) {
        if(player.equals(host)) {
            players.values().stream().forEach((p) -> {
                try {
                    p.out.writeS16(RequestCode.HANDSHAKE);
                    p.out.writeS8(RequestCode.PLAYER_GAME_ENDED);
                    p.out.writeS16((short) 0);
                } catch(IOException e) {
                    System.out.println("Failed to alert a player of a quitting host!");
                }
            });
            close();
        } else {
            players.remove(player.getName());
            player.game = null;

            try {
                host.out.writeS16(RequestCode.HANDSHAKE);
                host.out.writeS8(RequestCode.HOST_REMOVE_PLAYER);
                host.out.writeS16((short) (player.name.length() + 1));
                host.out.writeString(player.name);
            } catch(IOException e) {
                System.out.println("Failed to alert host of quitting player.");
            }
        }
    }

    public BFPlayer getPlayer(String name) {
        return players.get(name);
    }

    public BFPlayer getHost() {
        return host;
    }

    public RoomCode getRoomCode() {
        return roomCode;
    }

    public byte getGameState() {
        return state;
    }

    public void advanceGameState() {
        switch(state) {
            case GameState.LOBBY:
                state = GameState.INSTRUCTIONS;
                players.values().stream().forEach((p) -> {
                    try {
                        p.out.writeS16(RequestCode.HANDSHAKE);
                        p.out.writeS8(RequestCode.PROGRESS_GAME);
                        p.out.writeS16((short) 1);
                        p.out.writeS8(state);
                    } catch(IOException e) {
                        System.out.println("Failed to alert a player in a LOBBY game state!");
                    }
                });
                break;

            case GameState.INSTRUCTIONS:
                state = GameState.ROLE_ASSIGNMENT;
                players.values().stream().forEach((p) -> {
                    try {
                        p.out.writeS16(RequestCode.HANDSHAKE);
                        p.out.writeS8(RequestCode.PROGRESS_GAME);
                        p.out.writeS16((short) 1);
                        p.out.writeS8(state);
                    } catch(IOException e) {
                        System.out.println("Failed to alert a player in an INSTRUCTIONS game state!");
                    }
                });

                ArrayList<BFPlayer> shuffledPlayers = new ArrayList<BFPlayer>(players.values());
                Collections.shuffle(shuffledPlayers);
                for(int i=0; i<shuffledPlayers.size(); i++) {
                    BFPlayer p = shuffledPlayers.get(i);

                    byte role = (i+1) % 4 != 0 ? BFRole.POSITIVE : BFRole.NEGATIVE;
                    roles.get(role).add(p);

                    try {
                        p.out.writeS16(RequestCode.HANDSHAKE);
                        p.out.writeS8(RequestCode.ASSIGN_ROLE);
                        p.out.writeS16((short) 1);
                        p.out.writeS8(role);

                    } catch(IOException e) {
                        System.out.println("Failed to assign a player a role!");
                    }

                    try {
                        host.out.writeS16(RequestCode.HANDSHAKE);
                        host.out.writeS8(RequestCode.ASSIGN_ROLE);
                        host.out.writeS16((short) (p.name.length() + 2));
                        host.out.writeString(p.name);
                        host.out.writeS8(role);
                    } catch(IOException e) {
                        System.out.println("Failed to tell the host a player's role!");
                    }
                }
                break;

            case GameState.ROLE_ASSIGNMENT:
                state = GameState.GAME;
                players.values().stream().forEach((p) -> {
                    try {
                        p.out.writeS16(RequestCode.HANDSHAKE);
                        p.out.writeS8(RequestCode.PROGRESS_GAME);
                        p.out.writeS16((short) 1);
                        p.out.writeS8(state);
                    } catch(IOException e) {
                        System.out.println("Failed to alert a player in a ROLE_ASSIGNMENT game state!");
                    }
                });
                break;

            case GameState.GAME:
                break;

            case GameState.VOTING:
                break;

            case GameState.SCORES:
                break;
        }
    }

    public byte getRound() {
        return round;
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
