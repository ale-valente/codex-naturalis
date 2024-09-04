package org.codex.codex;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface {

    void run() throws RemoteException;

    /* ----- Functions that are called by the client to control the game ----------------------------- */
    // Prototypes: ClientInterface, RmiVirtualServer
    // Implementations: RmiClient, SocketClient
    void ping() throws IOException;
    ArrayList<String> getAvailableGameList() throws IOException;
    ArrayList<String> getPlayerList(String name) throws IOException;
    boolean gameAlreadyExists(String name) throws IOException;
    boolean playerAlreadyExists(String game, String player) throws IOException;
    boolean addGame(String name, int numPlayers) throws IOException;
    boolean addPlayer(String game, String player, String color) throws IOException;
    boolean numPlayersReached(String game) throws IOException;

    boolean colorAlreadyExists(String game, String color) throws IOException;

    /* ----------------------------------------------------------------------------------------------- */

}
