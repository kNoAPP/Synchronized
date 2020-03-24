package com.knoban.betterfriends.game;

import com.knoban.betterfriends.requests.*;
import com.knoban.betterfriends.server.BFServer;
import com.knoban.betterfriends.utils.Pair;
import com.knoban.gml.streams.GMLInputStream;
import com.knoban.gml.streams.GMLOutputStream;
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
    protected GMLOutputStream out;
    private volatile Boolean isClosed;

    private Queue<Pair<Byte, RequestFulfillment>> queuedRequests = new LinkedList<Pair<Byte, RequestFulfillment>>();
    private ReadWriteLock queuedRequestsLock = new ReentrantReadWriteLock();

    protected UUID uuid;
    protected String name;
    protected BFGame game;

    private byte controlState;

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

    public BFGame getGame() {
        return game;
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
                                case RequestCode.CREATE_GAME:
                                case RequestCode.PROGRESS_GAME:
                                    queuedRequestsLock.writeLock().lock();
                                    queuedRequests.offer(new Pair(request, null));
                                    queuedRequestsLock.writeLock().unlock();
                                    break;

                                case RequestCode.UPDATE_NAME:
                                    UpdateNameRequest updateNameRequest = new UpdateNameRequest(in.readString());
                                    queuedRequestsLock.writeLock().lock();
                                    queuedRequests.offer(new Pair(request, updateNameRequest));
                                    queuedRequestsLock.writeLock().unlock();
                                    break;

                                case RequestCode.JOIN_GAME:
                                    JoinGameRequest joinGameRequest = new JoinGameRequest(in.readString());
                                    queuedRequestsLock.writeLock().lock();
                                    queuedRequests.offer(new Pair(request, joinGameRequest));
                                    queuedRequestsLock.writeLock().unlock();
                                    break;

                                case RequestCode.CONTROL_STATE:
                                    ControlStateRequest controlStateRequest = new ControlStateRequest(in.readS8());
                                    queuedRequestsLock.writeLock().lock();
                                    queuedRequests.offer(new Pair(request, controlStateRequest));
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
            Pair<Byte, RequestFulfillment> request = queuedRequests.poll();
            queuedRequestsLock.writeLock().unlock();

            Byte requestCode = request.getKey();
            RequestFulfillment data = request.getValue();

            try {
                BFGame game = null;
                String code = null;
                switch(requestCode) {
                    case RequestCode.USER_ID:
                        System.out.println(Tools.formatSocket(connection) + ": USER_ID");
                        out.writeS16(RequestCode.HANDSHAKE);
                        out.writeS8(requestCode);
                        out.writeS16((short) (uuid.toString().length() + 1)); // size + null terminator
                        out.writeString(uuid.toString());
                        break;

                    case RequestCode.UPDATE_NAME:
                        UpdateNameRequest updateNameRequest = (UpdateNameRequest) data;
                        name = updateNameRequest.getName().trim();
                        if(name.length() > 16)
                            name = name.substring(0, 16);

                        System.out.println(Tools.formatSocket(connection) + ": UPDATE_NAME -> " + name);

                        out.writeS16(RequestCode.HANDSHAKE);
                        out.writeS8(requestCode);
                        out.writeS16((short) (name.length() + 1));
                        out.writeString(name);
                        break;

                    case RequestCode.CREATE_GAME:
                        System.out.println(Tools.formatSocket(connection) + ": CREATE_GAME");
                        if(this.game == null) {
                            game = new BFGame(server, this);
                            game.open();

                            code = game.getRoomCode().toString();
                            out.writeS16(RequestCode.HANDSHAKE);
                            out.writeS8(requestCode);
                            out.writeS16((short) (code.length() + 2));
                            out.writeS8(BFGame.MAX_PLAYERS);
                            out.writeString(code);
                        }
                        break;

                    case RequestCode.JOIN_GAME:
                        System.out.println(Tools.formatSocket(connection) + ": JOIN_GAME");
                        JoinGameRequest joinGameRequest = (JoinGameRequest) data;
                        byte statusCode = 0;
                        code = joinGameRequest.getCode();

                        if(this.game == null) {
                            if(code.length() == 4) {
                                game = server.getGame(new RoomCode(code));
                                if(game != null) {
                                    if(game.players.size() < BFGame.MAX_PLAYERS || game.state != GameState.LOBBY) {
                                        if(game.players.get(name) == null) {
                                            statusCode = 3;
                                            game.joinPlayer(this);
                                        } else
                                            statusCode = 2;
                                    } else
                                        statusCode = 1;
                                }
                            }

                            out.writeS16(RequestCode.HANDSHAKE);
                            out.writeS8(requestCode);
                            out.writeS16((short) 1);
                            out.writeS8(statusCode);
                        }
                        break;

                    case RequestCode.PROGRESS_GAME:
                        System.out.println(Tools.formatSocket(connection) + ": PROGRESS_GAME");
                        if(this.game != null && this.game.host == this) {
                            this.game.advanceGameState();
                        }
                        break;

                    case RequestCode.CONTROL_STATE:
                        ControlStateRequest controlStateRequest = (ControlStateRequest) data;
                        controlState = controlStateRequest.getState();

                        System.out.println(Tools.formatSocket(connection) + ": CONTROL_STATE: " + controlState);

                        if(this.game != null && this.game.host != this && this.game.state == GameState.GAME) {
                            this.game.host.out.writeS16(RequestCode.HANDSHAKE);
                            this.game.host.out.writeS8(RequestCode.CONTROL_STATE);
                            this.game.host.out.writeS16((short) (name.length() + 2));
                            this.game.host.out.writeString(name);
                            this.game.host.out.writeS8(controlState);
                        }
                        break;
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

        if(game != null)
            game.quitPlayer(this);
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
