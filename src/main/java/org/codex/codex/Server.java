package org.codex.codex;

import org.codex.codex.Model.*;
import org.codex.codex.Controller.ServerController;
import org.codex.codex.RMI.RmiServer;
import org.codex.codex.RMI.RmiVirtualClient;
import org.codex.codex.RMI.RmiVirtualServer;
import org.codex.codex.Socket.SocketClientHandler;
import org.codex.codex.Socket.SocketServer;
import org.codex.codex.View.ViewTUI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    static ServerController controller = new ServerController();
    static ArrayList<SocketClientHandler> socketClients = new ArrayList<>();
    static ArrayList<RmiVirtualClient> rmiClients = new ArrayList<>();
    static HashMap<String, Thread> gameThreads = new HashMap<>(); // "gameName" -> Thread()
    static HashMap<String, HashMap<String, SocketClientHandler>> socketClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}
    static HashMap<String, HashMap<String, RmiVirtualClient>> rmiClientsNickname = new HashMap<>(); // "gamename" -> {"nickname1" -> Client(), ...}

    static SocketServer socketServer;
    static RmiVirtualServer rmiServer;


    /* ----- Main ------------------------------------------------------------------------------------ */
    public static void main(String[] args) {

        System.out.println(CONST.TEXT_BLUE_BACKGROUND + "--- WELCOME TO CODEX SERVER ---\n" + CONST.TEXT_RESET);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter server ip (leave empty and press enter if you want to use the local server): ");
        String serverIp = scanner.nextLine().toLowerCase();
        int serverSocketPort = 0;
        if(!serverIp.isEmpty()) {
            do {
                System.out.print("Enter server socket port (don't use RMI Port " + CONST.serverLocalRMIPort + "): ");
                serverSocketPort = scanner.nextInt();
                if (serverSocketPort == CONST.serverLocalRMIPort)
                    System.out.println(CONST.TEXT_RED + "Don't use RMI Port " + CONST.serverLocalRMIPort + "!" + CONST.TEXT_RESET);
            } while (serverSocketPort == CONST.serverLocalRMIPort);
            // This is optional because rmi has the 1099 standard port
            // System.out.print("Enter server rmi port: ");
        }
        int serverSocketPortFinal = serverSocketPort;
        /* Run the Socket Server */ new Thread(() -> {try {
            System.out.println("Opening the Socket Server ...");
            if(!serverIp.isEmpty()) Server.socketServer = new SocketServer(serverIp, serverSocketPortFinal, Server.controller, Server.socketClients, Server.socketClientsNickname, Server.rmiClientsNickname);
            else Server.socketServer = new SocketServer(Server.controller, Server.socketClients, Server.socketClientsNickname, Server.rmiClientsNickname);
            Server.socketServer.run();
        } catch(IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}}).start();

        /* Run the RMI Server */ new Thread(() -> {try {
            System.out.println("Opening the RMI Server ...");
            Server.rmiServer = new RmiServer(Server.controller, Server.rmiClients, Server.socketClientsNickname, Server.rmiClientsNickname);
            Registry registry = LocateRegistry.createRegistry(CONST.serverLocalRMIPort);
            registry.rebind(CONST.serverName, (RmiVirtualServer) UnicastRemoteObject.exportObject(Server.rmiServer, 0));
        } catch(RemoteException e) {System.out.println(CONST.TEXT_RED + "RMI Server - Remote Exception!" + CONST.TEXT_RESET);}}).start();

        /* Control all the game to start */ new Thread(() -> {
            ServerController s = Server.controller;
            boolean start = false;
            while(!start) {
                for(String gameName : s.getAllGameList()) {
                    //Game g = s.getGame(gs);
                    synchronized (Server.controller) {
                        if(s.maxNumPlayersReached(gameName) && !s.isStarted(gameName)) {s.setIsStarted(gameName, true); start = true;}
                    }
                    if(start) {
                        Thread t = new Thread(() -> {
                            // ----- Server main (when the game starts) ------------------------------------------------

                            // Ping to check the network of all clients #########################################
                            new Thread(() -> {
                                boolean pingEnd = false;
                                while(!pingEnd) {
                                    AtomicInteger availableClients = new AtomicInteger();
                                    s.getPlayerList(gameName).forEach(nickname -> {
                                        if (Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                            try {
                                                Server.socketServer.getClientNickname(gameName, nickname).ping();
                                                availableClients.addAndGet(1);
                                            } catch (IOException e) {
                                                System.out.println(CONST.TEXT_RED + "Ping Exception! Closing the game..." + CONST.TEXT_RESET);
                                            }
                                        } else {
                                            try {
                                                Server.rmiServer.getClientNickname(gameName, nickname).ping();
                                                availableClients.addAndGet(1);
                                            } catch (IOException e) {
                                                System.out.println(CONST.TEXT_RED + "Ping Exception! Closing the game..." + CONST.TEXT_RESET);
                                            }
                                        }
                                    });
                                    if(availableClients.get() <= 1) {
                                        // Say to all clients that the game ends
                                        s.getPlayerList(gameName).forEach(nickname -> {
                                            if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                                try {
                                                    Server.socketServer.getClientNickname(gameName, nickname).endGame(
                                                            "NOBOBY_WIN"
                                                    );
                                                } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                            } else {
                                                try {
                                                    if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                                        Server.rmiServer.getClientNickname(gameName, nickname).endGame(
                                                                "NOBOBY_WIN"
                                                        );
                                                } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                            }
                                        });
                                        // Remove all clients from the game
                                        s.removeAllPlayers(gameName);
                                        pingEnd = true;
                                    }
                                }
                            }).start();
                            // ###################################################################################################


                            // Alert all clients that the game is starting now
                            System.out.println("\nSTARTING THE GAME: " + gameName);
                            s.getPlayerList(gameName).forEach(nickname -> {
                                System.out.println(nickname);
                                if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                    try {
                                        Server.socketServer.getClientNickname(gameName, nickname).startGame();
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                } else {
                                    try {
                                        if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                            Server.rmiServer.getClientNickname(gameName, nickname).startGame();
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                }
                            });

                            // Place 2 Resource cards and 2 Gold cards on the "public" table
                            s.setPublicResourceCard1(gameName, s.getNewResourceCard(gameName));
                            s.setPublicResourceCard2(gameName, s.getNewResourceCard(gameName));
                            s.setPublicGoldCard1(gameName, s.getNewGoldCard(gameName));
                            s.setPublicGoldCard2(gameName, s.getNewGoldCard(gameName));

                            // Set the public objective cards
                            s.setPublicObjectiveCard1(gameName, s.getNewObjectiveCard(gameName));
                            s.setPublicObjectiveCard2(gameName, s.getNewObjectiveCard(gameName));

                            // Each player receives a starter card and choose on which side placing it
                            s.getPlayerList(gameName).forEach(nickname -> {
                                StarterCard c = s.getNewStarterCard(gameName);
                                s.getPlayer(gameName, nickname).setStarterCard(c);
                                String side = "";
                                String frontTopLeft = c.getFrontTopLeft();
                                String frontTopRight = c.getFrontTopRight();
                                String frontBottomLeft = c.getFrontBottomLeft();
                                String frontBottomRight = c.getFrontBottomRight();
                                String backTopLeft = c.getBackTopLeft();
                                String backTopRight = c.getBackTopRight();
                                String backBottomLeft = c.getBackBottomLeft();
                                String backBottomRight = c.getBackBottomRight();
                                ArrayList<String> middle = c.getBackMiddle();
                                if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                    try {
                                        side = Server.socketServer.getClientNickname(gameName, nickname).chooseStarterCardSide(
                                                frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                                                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                                                middle
                                        );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                } else {
                                    try {
                                        if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                            side = Server.rmiServer.getClientNickname(gameName, nickname).chooseStarterCardSide(
                                                    frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                                                    backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                                                    middle
                                            );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                }
                                side = side.equals("b") ? "back" : "front";
                                s.placeCard(gameName, nickname, c, CONST.starterCardX, CONST.starterCardY, side);

                                // Each player get 2 resource cards and 1 gold cards
                                s.getPlayer(gameName, nickname).addResourceHandCard(s.getNewResourceCard(gameName));
                                s.getPlayer(gameName, nickname).addResourceHandCard(s.getNewResourceCard(gameName));
                                s.getPlayer(gameName, nickname).addGoldHandCard(s.getNewGoldCard(gameName));

                                // Each player receives 2 objective cards and choose 1 of them as a private objective
                                /*
                                "card":{
                                      "0":{"type":"mushrooms", "pos":""},
                                      "1":{"type":"mushrooms", "pos":"angleBottomLeft"},
                                      "2":{"type":"mushrooms", "pos":"angleBottomLeft"}}
                                 */
                                ObjectiveCard o1 = s.getNewObjectiveCard(gameName);
                                ObjectiveCard o2 = s.getNewObjectiveCard(gameName);
                                int points1 = o1.getPoints();
                                ArrayList<String> res1 = o1.getRes();
                                ArrayList<HashMap<String, String>> card1 = o1.getCard();
                                String card1_0_type = card1.get(0).get("type");
                                String card1_0_pos = card1.get(0).get("pos");
                                String card1_1_type = card1.get(1).get("type");
                                String card1_1_pos = card1.get(1).get("pos");
                                String card1_2_type = card1.get(2).get("type");
                                String card1_2_pos = card1.get(2).get("pos");
                                int points2 = o2.getPoints();
                                ArrayList<String> res2 = o2.getRes();
                                ArrayList<HashMap<String, String>> card2 = o2.getCard();
                                String card2_0_type = card2.get(0).get("type");
                                String card2_0_pos = card2.get(0).get("pos");
                                String card2_1_type = card2.get(1).get("type");
                                String card2_1_pos = card2.get(1).get("pos");
                                String card2_2_type = card2.get(2).get("type");
                                String card2_2_pos = card2.get(2).get("pos");
                                if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                    try {
                                        side = Server.socketServer.getClientNickname(gameName, nickname).chooseObjectiveCard(
                                                points1, res1, card1_0_type, card1_0_pos, card1_1_type, card1_1_pos, card1_2_type, card1_2_pos,
                                                points2, res2, card2_0_type, card2_0_pos, card2_1_type, card2_1_pos, card2_2_type, card2_2_pos
                                        );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                } else {
                                    try {
                                        if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                            side = Server.rmiServer.getClientNickname(gameName, nickname).chooseObjectiveCard(
                                                    points1, res1, card1_0_type, card1_0_pos, card1_1_type, card1_1_pos, card1_2_type, card1_2_pos,
                                                    points2, res2, card2_0_type, card2_0_pos, card2_1_type, card2_1_pos, card2_2_type, card2_2_pos
                                            );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                }
                                if(side.equals("1")) s.getPlayer(gameName, nickname).setObjectiveCard(o1);
                                else s.getPlayer(gameName, nickname).setObjectiveCard(o2);
                            });

                            boolean goToLastRound = false; boolean end = false;
                            // Start round
                            while(!end) {
                                AtomicBoolean points20 = new AtomicBoolean(false);
                                s.getPlayerList(gameName).forEach(nickname -> {

                                    Player p = s.getPlayer(gameName, nickname);

                                    // Play a card from the hand
                                    String command = "";
                                    ArrayList<int[]> handCoordinates = p.getHandCoordinates();
                                    HashMap<GoldCard, ArrayList<int[]>> goldHandCoordinates = p.getGoldHandCoordinates();
                                    ArrayList<GoldCard> goldHand = p.getGoldHand();
                                    // Serialize handCoordinates -> handCoordinatesString
                                    String handCoordinatesString = "";
                                    for(int i = 0; i < handCoordinates.size(); i++) {
                                        int[] coo = handCoordinates.get(i); handCoordinatesString += coo[0] + "-" + coo[1]; if(i < handCoordinates.size() - 1) handCoordinatesString += " ";}
                                    // Serialize goldHandCoordinates -> goldHandCoordinatesString
                                    String goldHandCoordinatesString = "";
                                    for(int ii = 0; ii < goldHand.size(); ii++) {
                                        GoldCard c = goldHand.get(ii);
                                        ArrayList<int[]> handCoo = goldHandCoordinates.get(c);
                                        for(int j = 0; j < handCoo.size(); j++) {
                                            int[] coo = handCoo.get(j); goldHandCoordinatesString += coo[0] + "-" + coo[1]; if(j < handCoo.size() - 1) goldHandCoordinatesString += " ";}
                                        if(ii < goldHand.size() - 1) goldHandCoordinatesString += ";";}
                                    if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                        try {
                                            command = Server.socketServer.getClientNickname(gameName, nickname).chooseToPlaceCard(
                                                    ViewTUI.tableString(p) + ViewTUI.handString(p),
                                                    handCoordinatesString, goldHandCoordinatesString,
                                                    p.getResourceHand().size(), p.getGoldHand().size()
                                            );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                    } else {
                                        try {
                                            if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                                command = Server.rmiServer.getClientNickname(gameName, nickname).chooseToPlaceCard(
                                                        ViewTUI.tableString(p) + ViewTUI.handString(p),
                                                        handCoordinatesString, goldHandCoordinatesString,
                                                        p.getResourceHand().size(), p.getGoldHand().size()
                                                );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                    }
                                    String[] commandArray = command.split(" "); // "r 4 b 127 149" <--> "<type> <card_number> <side> <X> <Y>"
                                    String typeCommand = commandArray[0];
                                    int cardNumberCommand = Integer.parseInt(commandArray[1]);
                                    String sideCommand = commandArray[2].equals("b") ? "back" : "front";
                                    int xCommand = Integer.parseInt(commandArray[3]);
                                    int yCommand = Integer.parseInt(commandArray[4]);
                                    Card c;
                                    if(typeCommand.equals("r")) c = p.getResourceHandCard(cardNumberCommand);
                                    else c = p.getGoldHandCard(cardNumberCommand);
                                    s.placeCard(gameName, nickname, c, xCommand, yCommand, sideCommand);
                                    if(typeCommand.equals("r")) p.removeResourceHandCard(cardNumberCommand);
                                    else p.removeGoldHandCard(cardNumberCommand);

                                    // Draw a card
                                    if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                        try {

                                            command = Server.socketServer.getClientNickname(gameName, nickname).chooseToDrawCard(
                                                    ViewTUI.publicCardsString(s.getGame(gameName)),
                                                    s.isResourceCardDeckFinished(gameName),
                                                    s.isGoldCardDeckFinished(gameName)
                                            );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                    } else {
                                        try {
                                            if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                                command = Server.rmiServer.getClientNickname(gameName, nickname).chooseToDrawCard(
                                                        ViewTUI.publicCardsString(s.getGame(gameName)),
                                                        s.isResourceCardDeckFinished(gameName),
                                                        s.isGoldCardDeckFinished(gameName)
                                                );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                    }

                                    switch(command) {
                                        case "r" -> {p.addResourceHandCard(s.getNewResourceCard(gameName));}
                                        case "g" -> {p.addGoldHandCard(s.getNewGoldCard(gameName));}
                                        case "r1" -> {
                                            p.addResourceHandCard(s.getPublicResourceCard1(gameName));
                                            s.setPublicResourceCard1(gameName, s.getNewResourceCard(gameName));
                                        }
                                        case "r2" -> {
                                            p.addResourceHandCard(s.getPublicResourceCard2(gameName));
                                            s.setPublicResourceCard2(gameName, s.getNewResourceCard(gameName));
                                        }
                                        case "g1" -> {
                                            p.addGoldHandCard(s.getPublicGoldCard1(gameName));
                                            s.setPublicGoldCard1(gameName, s.getNewGoldCard(gameName));
                                        }
                                        case "g2" -> {
                                            p.addGoldHandCard(s.getPublicGoldCard2(gameName));
                                            s.setPublicGoldCard2(gameName, s.getNewGoldCard(gameName));
                                        }
                                    }

                                    // Update all clients with the new table + new hand + points
                                    if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                        try {
                                            Server.socketServer.getClientNickname(gameName, nickname).updateTable(
                                                    ViewTUI.tableString(p) + ViewTUI.handString(p) + ViewTUI.pointsAndResourcesString(p)
                                            );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                    } else {
                                        try {
                                            if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                                Server.rmiServer.getClientNickname(gameName, nickname).updateTable(
                                                        ViewTUI.tableString(p) + ViewTUI.handString(p) + ViewTUI.pointsAndResourcesString(p)
                                                );
                                        } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                    }
                                    if(p.getPoints() >= 20) points20.set(true); // Check if a player has reached 20 points
                                });

                                if(goToLastRound) end = true;
                                if(points20.get() || (s.isResourceCardDeckFinished(gameName) && s.isGoldCardDeckFinished(gameName))) goToLastRound = true;
                            }

                            // Update all clients with the points and the winner
                            AtomicInteger maxPoints = new AtomicInteger(-1);
                            ArrayList<String> winners = new ArrayList<>(); /*int maxPoints = -1;*/ AtomicInteger maxNumOfObjectives = new AtomicInteger(-1);
                            s.getPlayerList(gameName).forEach(nickname -> {
                                Player p = s.getPlayer(gameName, nickname);
                                int pointsFromPublicObjectiveCard1 = p.getPointsFromObjectiveCardIfRespected(s.getPublicObjectiveCard1(gameName));
                                int pointsFromPublicObjectiveCard2 = p.getPointsFromObjectiveCardIfRespected(s.getPublicObjectiveCard2(gameName));
                                int pointsFromPrivateObjectiveCard = p.getPointsFromObjectiveCardIfRespected(p.getObjectiveCard());
                                int pointsPlayer = p.getPoints() + pointsFromPublicObjectiveCard1 + pointsFromPublicObjectiveCard2 + pointsFromPrivateObjectiveCard;
                                int numOfObjectives = (pointsFromPublicObjectiveCard1 > 0 ? 1 : 0) + (pointsFromPublicObjectiveCard2 > 0 ? 1 : 0) + (pointsFromPrivateObjectiveCard > 0 ? 1 : 0);
                                if(pointsPlayer > maxPoints.get()) {
                                    winners.clear(); winners.add(nickname); maxPoints.set(pointsPlayer); maxNumOfObjectives.set(numOfObjectives);
                                } else if(pointsPlayer == maxPoints.get()) {
                                    if(numOfObjectives > maxNumOfObjectives.get()) {
                                        winners.clear(); winners.add(nickname); maxNumOfObjectives.set(numOfObjectives);
                                    } else if(numOfObjectives == maxNumOfObjectives.get()) {
                                        winners.add(nickname);
                                    }
                                }
                            });
                            String winnersString = "";
                            for(int i = 0; i < winners.size(); i++) {winnersString += winners.get(i); if(i < winners.size() - 1) winnersString += ", ";}
                            String finalWinnersString = winnersString;
                            s.getPlayerList(gameName).forEach(nickname -> {
                                if(Server.socketServer.clientsContainNickname(gameName, nickname)) {
                                    try {
                                        Server.socketServer.getClientNickname(gameName, nickname).endGame(
                                                finalWinnersString
                                        );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "Socket Server - IO Exception!" + CONST.TEXT_RESET);}
                                } else {
                                    try {
                                        if(Server.rmiServer.clientsContainNickname(gameName, nickname))
                                            Server.rmiServer.getClientNickname(gameName, nickname).endGame(
                                                    finalWinnersString
                                            );
                                    } catch (IOException e) {System.out.println(CONST.TEXT_RED + "RMI Server - IO Exception!" + CONST.TEXT_RESET);}
                                }
                            });

                            s.removeGame(gameName);

                            // -----------------------------------------------------------------------------------------
                        });
                        gameThreads.put(gameName, t);
                        t.start();
                    }
                }
            }
        }).start();

    }
    /* ----------------------------------------------------------------------------------------------- */


}
