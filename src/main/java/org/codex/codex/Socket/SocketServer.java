package org.codex.codex.Socket;

import org.codex.codex.CONST;
import org.codex.codex.Controller.ServerController;
import org.codex.codex.RMI.RmiVirtualClient;
import org.json.JSONArray;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class SocketServer {
    ServerController controller;
    ArrayList<SocketClientHandler> clients;
    HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}
    HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}

    ServerSocket listenSocket;
    public SocketServer(ServerController controller, ArrayList<SocketClientHandler> clients, HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname, HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname) throws IOException {
        this.listenSocket = new ServerSocket(CONST.serverLocalSocketPort); this.controller = controller; this.clients = clients; this.socketClientsNickname = socketClientsNickname; this.rmiClientsNickname = rmiClientsNickname;}

    public SocketServer(String ip, int port, ServerController controller, ArrayList<SocketClientHandler> clients, HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname, HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname) throws IOException {
        this.listenSocket = new ServerSocket(); this.listenSocket.bind(new InetSocketAddress(ip, port)); this.controller = controller; this.clients = clients; this.socketClientsNickname = socketClientsNickname; this.rmiClientsNickname = rmiClientsNickname;}


    public SocketClientHandler getClientNickname(String game, String nickname) {
        return this.socketClientsNickname.get(game).get(nickname);
    }
    public boolean clientsContainNickname(String game, String nickname) {
        return this.socketClientsNickname.get(game).containsKey(nickname) /*|| this.rmiClientsNickname.get(game).containsKey(nickname)*/;
    }

    public void run() throws IOException {
        Socket clientSocket = null;
        while((clientSocket = this.listenSocket.accept()) != null) {
            SocketClientHandler newClient = new SocketClientHandler(
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
                    new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())),
                    new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
            );
            synchronized (this.clients) {this.clients.add(newClient);}

            /*new Thread(() -> {
                try {
                    String line;
                    while((line = newClient.input.readLine()) != null) {
                        switch(line) {
                            case "ping" -> {}
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();*/

            new Thread(() -> {
                try {
                    /* ----- Functions that are called by the client to control the game ----------------------------- */
                    String line;
                    while((line = newClient.input.readLine()) != null) {
                        switch(line) {

                            case "ping" -> {}

                            // ----- Results from server methods to client methods ----------------------------------------
                            case "chooseStarterCardSide_result" -> newClient.setServerResult("chooseStarterCardSide");
                            case "chooseObjectiveCard_result" -> newClient.setServerResult("chooseObjectiveCard");
                            case "chooseToPlaceCard_result" -> newClient.setServerResult("chooseToPlaceCard");
                            case "chooseToDrawCard_result" -> newClient.setServerResult("chooseToDrawCard");
                        // --------------------------------------------------------------------------------------------


                            case "getAvailableGameList" -> {
                                JSONArray o = new JSONArray(this.controller.getAvailableGameList());
                                newClient.output.println("getAvailableGameList_result"); newClient.output.println(o.toString());
                                newClient.output.flush();
                            }
                            case "getPlayerList" -> {
                                JSONArray o = new JSONArray(this.controller.getPlayerList(newClient.input.readLine()));
                                newClient.output.println("getPlayerList_result"); newClient.output.println(o.toString());
                                newClient.output.flush();
                            }
                            case "gameAlreadyExists" -> {
                                newClient.output.println("gameAlreadyExists_result");
                                if(this.controller.gameAlreadyExists(newClient.input.readLine())) newClient.output.println("1");
                                else newClient.output.println("0");
                                newClient.output.flush();
                            }
                            case "playerAlreadyExists" -> {
                                newClient.output.println("playerAlreadyExists_result");
                                if(this.controller.playerAlreadyExists(newClient.input.readLine(), newClient.input.readLine())) newClient.output.println("1");
                                else newClient.output.println("0");
                                newClient.output.flush();
                            }
                            case "addGame" -> {
                                boolean exception = false;
                                boolean result = false;
                                String gameName = newClient.input.readLine();
                                try{result = this.controller.addGame(gameName, Integer.parseInt(newClient.input.readLine()));}
                                catch (IOException e) {exception = true;}
                                if(exception) {newClient.output.println("addGame_result"); newClient.output.println("exception"); newClient.output.flush();}
                                else {
                                    newClient.output.println("addGame_result");
                                    if(result) {
                                        socketClientsNickname.put(gameName, new HashMap<>()); rmiClientsNickname.put(gameName, new HashMap<>());
                                        newClient.output.println("1");
                                    } else newClient.output.println("0");
                                    newClient.output.flush();
                                }
                            }
                            case "addPlayer" -> {
                                String gameName = newClient.input.readLine();
                                String playerName = newClient.input.readLine();
                                boolean result = this.controller.addPlayer(gameName, playerName, newClient.input.readLine());
                                newClient.output.println("addPlayer_result");
                                if(result) {
                                    this.socketClientsNickname.get(gameName).put(playerName, newClient);
                                    newClient.output.println("1");
                                } else newClient.output.println("0");
                                newClient.output.flush();
                            }
                            case "numPlayersReached" -> {
                                boolean result = this.controller.maxNumPlayersReached(newClient.input.readLine());
                                newClient.output.println("numPlayersReached_result");
                                if(result) newClient.output.println("1");
                                else newClient.output.println("0");
                                newClient.output.flush();
                            }

                            case "colorAlreadyExists" -> {
                                boolean result = this.controller.colorAlreadyExists(newClient.input.readLine(), newClient.input.readLine());
                                newClient.output.println("colorAlreadyExists_result");
                                if(result) newClient.output.println("1");
                                else newClient.output.println("0");
                                newClient.output.flush();
                            }
                            default -> System.err.println("[INVALID MESSAGE]");
                        }
                    }
                    /* ----------------------------------------------------------------------------------------------- */
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

}
