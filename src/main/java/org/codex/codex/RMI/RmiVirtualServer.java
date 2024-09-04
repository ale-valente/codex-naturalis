package org.codex.codex.RMI;

import org.codex.codex.Server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface RmiVirtualServer extends Remote {
    void connect(RmiVirtualClient client) throws RemoteException;

    RmiVirtualClient getClientNickname(String game, String nickname) throws IOException;
    boolean clientsContainNickname(String game, String nickname) throws IOException;

    /* ----- Functions that are called by the client to control the game ----------------------------- */
    // Prototypes: ClientInterface, RmiVirtualServer
    // Implementations: RmiClient, SocketClient

    void ping() throws IOException;
    ArrayList<String> getAvailableGameList() throws IOException;
    ArrayList<String> getPlayerList(String name) throws IOException;
    boolean gameAlreadyExists(String name) throws IOException;
    boolean playerAlreadyExists(String game, String player) throws IOException;
    boolean addGame(String name, int numPlayers) throws IOException;
    boolean addPlayer(String game, String player, String color, RmiVirtualClient client) throws IOException;
    boolean numPlayersReached(String game) throws IOException;
    boolean colorAlreadyExists(String game, String color) throws IOException;



    /* ----------------------------------------------------------------------------------------------- */

}
