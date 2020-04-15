package com.knoban.betterfriends.server;

import com.knoban.betterfriends.game.BFGame;
import com.knoban.betterfriends.game.BFPlayer;
import com.knoban.betterfriends.game.RoomCode;
import com.knoban.betterfriends.utils.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BFServer {

    private HashMap<RoomCode, BFGame> onGoingGames = new HashMap<RoomCode, BFGame>();
    private HashSet<BFPlayer> playerConnections = new HashSet<BFPlayer>();
    private ReadWriteLock rwPlayerConnections = new ReentrantReadWriteLock();

    private ServerSocket serverSocket;
    private int port;
    private Thread connectionListener;
    private volatile boolean isListening;

    private Thread requestProcessor;
    private volatile boolean isProcessingRequests;

    public BFServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public BFGame addGame(RoomCode code, BFGame game) {
        return onGoingGames.put(code, game);
    }

    public BFGame removeGame(RoomCode code) {
        return onGoingGames.remove(code);
    }

    public BFGame getGame(RoomCode code) {
        return onGoingGames.get(code);
    }

    public boolean addPlayer(BFPlayer player) {
        rwPlayerConnections.writeLock().lock();
        boolean toRet = playerConnections.add(player);
        rwPlayerConnections.writeLock().unlock();
        return toRet;
    }

    public boolean removePlayer(BFPlayer player) {
        rwPlayerConnections.writeLock().lock();
        boolean toRet = playerConnections.remove(player);
        rwPlayerConnections.writeLock().unlock();
        return toRet;
    }

    public boolean hasPlayer(BFPlayer player) {
        rwPlayerConnections.readLock().lock();
        boolean toRet = playerConnections.contains(player);
        rwPlayerConnections.readLock().unlock();
        return toRet;
    }

    public int getPort() {
        return port;
    }

    public void open() {
        isListening = true;
        connectionListener = new Thread(() -> {
            while(isListening) {
                try {
                    Socket connection = serverSocket.accept();
                    System.out.println("Connected [CLIENT/SERVER]: " + Tools.formatSocket(connection));
                    BFPlayer player = new BFPlayer(BFServer.this, connection);
                    rwPlayerConnections.writeLock().lock();
                    playerConnections.add(player);
                    rwPlayerConnections.writeLock().unlock();
                    player.open();
                } catch(IOException e) {
                    System.out.println("Failed to accept connection: " + e.getMessage());
                }
            }
        });
        connectionListener.start();
    }

    public void startProcessingRequests() {
        isProcessingRequests = true;
        requestProcessor = new Thread(() -> {
            while(isProcessingRequests) {
                rwPlayerConnections.readLock().lock();
                List<BFPlayer> players = new ArrayList<BFPlayer>(playerConnections);
                rwPlayerConnections.readLock().unlock();
                for(BFPlayer player : players) {
                    player.processRequests();
                }
            }
        });
        requestProcessor.start();
    }

    public void stopProcessingRequests() {
        isProcessingRequests = false;
        try {
            requestProcessor.join();
        } catch(InterruptedException e) {
            System.out.println("Got interrupted while stopping request processing: " + e.getMessage());
        }
    }

    public void close() {
        isListening = false;

        stopProcessingRequests();

        new ArrayList<BFGame>(onGoingGames.values()).stream().forEach((g) -> g.close());
        new ArrayList<BFPlayer>(playerConnections).stream().forEach((p) -> p.close());

        try {
            serverSocket.close();
            connectionListener.join();
            connectionListener = null;
        } catch(InterruptedException | IOException e) {
            System.out.println("Got interrupted while stopping incoming connections: " + e.getMessage());
        }
    }
}
