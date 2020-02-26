package com.knoban.betterfriends.game;

import com.knoban.betterfriends.requests.RequestCode;
import com.knoban.betterfriends.requests.RequestFulfillment;
import com.knoban.betterfriends.requests.UserIdFulfillment;
import com.knoban.betterfriends.server.BFServer;
import com.knoban.betterfriends.streams.GMLInputStream;
import com.knoban.betterfriends.streams.GMLOutputStream;
import com.knoban.betterfriends.utils.Tools;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BFPlayer {

    private BFServer server;
    private Socket connection;
    private Thread listener;
    private GMLInputStream in;
    private GMLOutputStream out;
    private volatile Boolean isClosed;

    private Queue<RequestFulfillment> queuedRequests = new LinkedList<RequestFulfillment>();
    private ReadWriteLock queuedRequestsLock = new ReentrantReadWriteLock();

    private String name;
    private UUID uuid;
    private BFGame currentGame;

    public BFPlayer(BFServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public BFGame getCurrentGame() {
        return currentGame;
    }

    /**
     * @return true, if connection is open. false, if connection has been closed, null, if the connection can be opened.
     */
    public Boolean isClosed() {
        return isClosed;
    }

    public void open() {
        if(isClosed != null)
            return;

        isClosed = false;
        listener = new Thread(() -> {
            while(!isClosed) {
                try {
                    in = new GMLInputStream(connection.getInputStream());
                    out = new GMLOutputStream(connection.getOutputStream());

                    while(!isClosed) {
                        in.readS8();
                        in.skipPassHeader();
                        if(in.readS16() == RequestCode.HANDSHAKE) {
                            byte request = in.readS8();
                            switch(request) {
                                case RequestCode.USER_ID:
                                    UserIdFulfillment uid = new UserIdFulfillment();
                                    queuedRequestsLock.writeLock().lock();
                                    queuedRequests.offer(uid);
                                    queuedRequestsLock.writeLock().unlock();
                                    break;
                            }
                        }
                    }
                } catch(SocketException | EOFException e) {
                    System.out.println("Disconnect [Client]: " + Tools.formatSocket(connection));
                    close();
                } catch(IOException e) {
                    System.out.println("Unable to read input stream: " + e.getMessage());
                }
            }
        });
        listener.start();
    }

    public void processRequests() {
        while(!queuedRequests.isEmpty()) {
            queuedRequestsLock.writeLock().lock();
            RequestFulfillment request = queuedRequests.poll();
            queuedRequestsLock.writeLock().unlock();

            try {
                if(request instanceof UserIdFulfillment) {
                    System.out.println(Tools.formatSocket(connection) + ": USER_ID");
                    for(int i=0; i<10000; i++) {
                        out.writeS16(RequestCode.HANDSHAKE);
                        out.writeS16(RequestCode.USER_ID);
                        out.writeString(uuid.toString());
                    }
                }
            } catch(IOException e) {
                System.out.println("Unable to write output stream: " + e.getMessage());
            }
        }
    }

    public void close() {
        if(isClosed == null || isClosed == true)
            return;
        isClosed = true;

        System.out.println("Disconnect [Server]: " + Tools.formatSocket(connection));

        if(currentGame != null)
            currentGame.quitPlayer(this);
        server.removePlayer(this);

        try {
            connection.close();
            listener.join();
        } catch(IOException | InterruptedException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BFPlayer))
            return false;

        BFPlayer player = (BFPlayer) o;
        return player.uuid.equals(uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
