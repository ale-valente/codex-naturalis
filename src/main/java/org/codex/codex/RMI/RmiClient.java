package org.codex.codex.RMI;

import org.codex.codex.CONST;
import org.codex.codex.ClientInterface;
import org.codex.codex.View.ViewTUI;
import org.json.JSONArray;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class RmiClient extends UnicastRemoteObject implements RmiVirtualClient, ClientInterface {
    final RmiVirtualServer server; // Server object client-side
    public RmiClient() throws IOException, NotBoundException {
        this.server = (RmiVirtualServer) LocateRegistry.getRegistry(CONST.serverLocalHost, CONST.serverLocalRMIPort).lookup(CONST.serverName);
    }
    public RmiClient(String ip) throws IOException, NotBoundException {
        this.server = (RmiVirtualServer) LocateRegistry.getRegistry(ip, CONST.serverLocalRMIPort).lookup(CONST.serverName);
    }
    /* ----------------------------------------------------------------------------------------------- */

    private boolean handCoordinatesContains(ArrayList<int[]> handCoordinates, int x, int y) {
        for (int[] handCoo : handCoordinates) {if (handCoo[0] == x && handCoo[1] == y) return true;}
        return false;}

    /* ----- Functions that are called by the server to send actions (or messages) to the client ----- */
    // Prototypes: RmiVirtualClient
    // Implementations: RmiClient, SocketClient

    public void startGame() throws IOException {
        System.out.println("THE GAME STARTS NOW.");
    }

    public void ping() throws IOException {
        return;
    }

    public void endGame(String s) throws IOException {
        System.out.println("\n* THE GAME IS FINISHED! *\nWINNER(S) IS(ARE): " + s);
    }

    public void updateTable(String view) throws IOException {
        System.out.println(view);
    }

    public String chooseStarterCardSide(String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
                                        String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
                                        ArrayList<String> middle) throws IOException {
        ArrayList<String> middleArr = new ArrayList<>();
        (new JSONArray(middle)).forEach(o -> {middleArr.add(o.toString());});
        ViewTUI.starterCard(frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                middleArr);
        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose the side of the starter card. Front [f] or Back [b]: " + CONST.TEXT_RESET);
        Scanner scan = new Scanner(System.in);
        String inputString = "";
        inputString = scan.nextLine().toLowerCase();
        while(!inputString.equals("f") && !inputString.equals("b")) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert 'f' or 'b'!" + CONST.TEXT_RESET);
            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose the side of the starter card. Front [f] or Back [b]: " + CONST.TEXT_RESET);
            inputString = scan.nextLine().toLowerCase();
        }
        return inputString;
    }

    public String chooseObjectiveCard(
            int points1, ArrayList<String> res1, String card1_0_type, String card1_0_pos, String card1_1_type, String card1_1_pos, String card1_2_type, String card1_2_pos,
            int points2, ArrayList<String> res2, String card2_0_type, String card2_0_pos, String card2_1_type, String card2_1_pos, String card2_2_type, String card2_2_pos
    ) throws IOException {
        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose 1 of the 2 objective cards. 1 or 2: " + CONST.TEXT_RESET);
        ViewTUI.objective2Cards(
                points1, res1, card1_0_type, card1_0_pos, card1_1_type, card1_1_pos, card1_2_type, card1_2_pos,
                points2, res2, card2_0_type, card2_0_pos, card2_1_type, card2_1_pos, card2_2_type, card2_2_pos
        );
        Scanner scan = new Scanner(System.in);
        String inputString = "";
        inputString = scan.nextLine().toLowerCase();
        while(!inputString.equals("1") && !inputString.equals("2")) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert '1' or '2'!" + CONST.TEXT_RESET);
            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose 1 of the 2 objective cards. 1 or 2: " + CONST.TEXT_RESET);
            inputString = scan.nextLine().toLowerCase();
        }
        return inputString;
    }

    public String chooseToPlaceCard(
            String view,
            String handCoordinatesString, String goldHandCoordinatesString,
            int resourceHandSize, int goldHandSize
    ) throws IOException {
        // Deserialize handCoordinatesString -> handCoordinates
        ArrayList<int[]> handCoordinates = new ArrayList<>();
        if(!handCoordinatesString.equals("empty")) {
            String[] hcs = handCoordinatesString.split(" ");
            for(int i = 0; i < hcs.length; i++) {
                String[] hcs_ = hcs[i].split("-");
                int[] coord = new int[2]; coord[0] = Integer.parseInt(hcs_[0]); coord[1] = Integer.parseInt(hcs_[1]);
                handCoordinates.add(coord);
            }
        }
        // Deserialize goldHandCoordinatesString -> goldHandCoordinates
        ArrayList<ArrayList<int[]>> goldHandCoordinates = new ArrayList<>();
        if(goldHandCoordinatesString.equals("empty"))
            for(int a = 0; a < goldHandSize; a++) goldHandCoordinates.add(new ArrayList<>());
        else {
            String[] goldHC = goldHandCoordinatesString.split(";");
            for (int k = 0; k < goldHC.length; k++) {
                ArrayList<int[]> goldHC_temp = new ArrayList<>();
                if(!goldHC[k].isEmpty()) {
                    String[] ghcs = goldHC[k].split(" ");
                    for (int i = 0; i < ghcs.length; i++) {
                        String[] ghcs_ = ghcs[i].split("-");
                        int[] coord = new int[2];
                        coord[0] = Integer.parseInt(ghcs_[0]);
                        coord[1] = Integer.parseInt(ghcs_[1]);
                        goldHC_temp.add(coord);
                    }
                }
                goldHandCoordinates.add(goldHC_temp);
            }
        }
        System.out.println("IT'S YOUR ROUND!");
        System.out.print(view);
        System.out.print("Select the card to place and the available coordinates in the format: <type> <card_number> <side> <X> <Y>\n"
                + "<type> : 'r' Resource Card | 'g' Gold Card\n"
                + "<card_number> : '0' or '1' or '2' ...\n"
                + "<side> : 'f' Front | 'b' Back\n"
                + "<X> : '0' or '1' or '2' ...\n"
                +"<Y> : '0' or '1' or '2' ...\n"
                + "PLACE CARD ===> ");
        Scanner scan = new Scanner(System.in);
        String inputString = "";
        String type = ""; int card_number = -1; String side = ""; int x = -1; int y = -1;
        inputString = scan.nextLine().toLowerCase();
        String[] commands = inputString.split(" "); // "r 4 b 127 149" <--> "<type> <card_number> <side> <X> <Y>"
        if(commands.length == 5) {
            type = commands[0];
            card_number = Integer.parseInt(commands[1]);
            side = commands[2];
            x = Integer.parseInt(commands[3]);
            y = Integer.parseInt(commands[4]);
        }
        while(
                commands.length != 5 ||
                        card_number < 0 || x < 0 || y < 0 ||
                        ( !type.equals("r") && !type.equals("g") ) || ( !side.equals("f") && !side.equals("b") ) ||
                        (type.equals("r") && card_number >= resourceHandSize) ||
                        (type.equals("g") && card_number >= goldHandSize) ||
                        ( type.equals("r") && !this.handCoordinatesContains(handCoordinates, x, y) ) ||
                        ( type.equals("g") && !this.handCoordinatesContains(goldHandCoordinates.get(card_number), x, y) )
        ) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid command!" + CONST.TEXT_RESET);
            System.out.print("PLACE CARD ===> ");
            inputString = scan.nextLine().toLowerCase();
            commands = inputString.split(" "); // "r 4 b 127 149" <--> "<type> <card_number> <side> <X> <Y>"
            if(commands.length == 5) {
                type = commands[0];
                card_number = Integer.parseInt(commands[1]);
                side = commands[2];
                x = Integer.parseInt(commands[3]);
                y = Integer.parseInt(commands[4]);
            }
        }
        System.out.println("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Placed card!" + CONST.TEXT_RESET);
        return inputString;
    }


    public String chooseToDrawCard(String publicCardsString, boolean isResourceCardDeckFinished, boolean isGoldCardDeckFinished) throws IOException {
        System.out.println("DRAW A CARD");
        System.out.print(publicCardsString);
        if(isResourceCardDeckFinished) System.out.println(CONST.TEXT_RED + "Resource Deck is finished!" + CONST.TEXT_RESET);
        if(isGoldCardDeckFinished) System.out.println(CONST.TEXT_RED + "Gold Deck is finished!" + CONST.TEXT_RESET);
        System.out.print("Type where you want to draw a card ('r' Resource Deck, 'g' Gold Deck, 'r1', 'r2', 'g1', 'g2'): ");
        Scanner scan = new Scanner(System.in);
        String inputString = "";
        inputString = scan.nextLine().toLowerCase();
        while(
                ( !inputString.equals("r1") && !inputString.equals("r2") && !inputString.equals("g1") && !inputString.equals("g2") && !inputString.equals("r") && !inputString.equals("g") )
                        || ( inputString.equals("r") && isResourceCardDeckFinished )
                        || ( inputString.equals("g") && isGoldCardDeckFinished )
        ) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid command! (Maybe you have chosen a finished deck)" + CONST.TEXT_RESET);
            System.out.print("Type where you want to draw a card ('r' Resource Deck, 'g' Gold Deck, 'r1', 'r2', 'g1', 'g2'): ");
            inputString = scan.nextLine().toLowerCase();
        }
        System.out.println("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Card Drawn!" + CONST.TEXT_RESET);
        return inputString;
    }

    /* ----------------------------------------------------------------------------------------------- */


    /* ----- Functions that are called by the client to control the game ----------------------------- */
    // Prototypes: ClientInterface, RmiVirtualServer
    // Implementations: RmiClient, SocketClient

    public ArrayList<String> getAvailableGameList() throws IOException {return this.server.getAvailableGameList();}
    public ArrayList<String> getPlayerList(String name) throws IOException {return this.server.getPlayerList(name);}
    public boolean gameAlreadyExists(String name) throws IOException {return this.server.gameAlreadyExists(name);}
    public boolean playerAlreadyExists(String game, String player) throws IOException {return this.server.playerAlreadyExists(game, player);}
    public boolean addGame(String name, int numPlayers) throws IOException {return this.server.addGame(name, numPlayers);}
    public boolean addPlayer(String game, String player, String color) throws IOException {return this.server.addPlayer(game, player, color, this);}
    public boolean numPlayersReached(String game) throws IOException {return this.server.numPlayersReached(game);}
    public boolean colorAlreadyExists(String game, String color) throws IOException {return this.server.colorAlreadyExists(game, color);}

    /* ----------------------------------------------------------------------------------------------- */

    @Override
    public void run() throws RemoteException {this.server.connect(this);}







}
