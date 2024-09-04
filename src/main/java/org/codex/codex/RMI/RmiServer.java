package org.codex.codex.RMI;

import org.codex.codex.Controller.ServerController;
import org.codex.codex.Model.Player;
import org.codex.codex.Model.Card;
import org.codex.codex.Model.StarterCard;
import org.codex.codex.Server;
import org.codex.codex.Socket.SocketClientHandler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class RmiServer implements RmiVirtualServer {
    ServerController controller;
    ArrayList<RmiVirtualClient> clients;
    HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}
    HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}



    public RmiServer(ServerController controller, ArrayList<RmiVirtualClient> clients, HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname, HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname) {
        this.controller = controller; this.clients = clients; this.socketClientsNickname = socketClientsNickname; this.rmiClientsNickname = rmiClientsNickname;}

    /*public void run() throws RemoteException {
        //RmiVirtualServer stub = (RmiVirtualServer)UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(CONST.serverRMIPort);
        registry.rebind(CONST.serverName, (RmiVirtualServer)UnicastRemoteObject.exportObject(this, 0));
    }*/
    public void connect(RmiVirtualClient client) throws RemoteException {synchronized (this.clients) {this.clients.add(client);}}
    /* ----------------------------------------------------------------------------------------------- */

    public RmiVirtualClient getClientNickname(String game, String nickname) throws IOException {
        return this.rmiClientsNickname.get(game).get(nickname);
    }
    public boolean clientsContainNickname(String game, String nickname) throws IOException {
        return /*this.socketClientsNickname.get(game).containsKey(nickname) ||*/ this.rmiClientsNickname.get(game).containsKey(nickname);
    }


    /* ----- Functions that are called by the client to control the game ----------------------------- */

    public void ping() throws IOException {return;}
    public ArrayList<String> getAvailableGameList() throws IOException  {return this.controller.getAvailableGameList();}
    public ArrayList<String> getPlayerList(String name) throws IOException {return this.controller.getPlayerList(name);}
    public boolean gameAlreadyExists(String name) throws IOException {return this.controller.gameAlreadyExists(name);}
    public boolean playerAlreadyExists(String game, String player) throws IOException {return this.controller.playerAlreadyExists(game, player);}
    public boolean addGame(String name, int numPlayers) throws IOException {
        boolean result = this.controller.addGame(name, numPlayers);
        if(result) {socketClientsNickname.put(name, new HashMap<>()); rmiClientsNickname.put(name, new HashMap<>());}
        return result;
    }
    public boolean addPlayer(String game, String player, String color, RmiVirtualClient client) throws IOException {
        boolean result = this.controller.addPlayer(game, player, color);
        if(result) rmiClientsNickname.get(game).put(player, client);
        return result;
    }
    public boolean numPlayersReached(String game) throws IOException {return this.controller.maxNumPlayersReached(game);}
    public boolean colorAlreadyExists(String game, String color) throws IOException {return this.controller.colorAlreadyExists(game, color);}



    /* ----------------------------------------------------------------------------------------------- */



}
