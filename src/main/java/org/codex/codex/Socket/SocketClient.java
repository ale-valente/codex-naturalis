package org.codex.codex.Socket;

import org.codex.codex.CONST;
import org.codex.codex.ClientInterface;
import org.codex.codex.View.ViewTUI;
import org.json.JSONArray;

import java.io.*;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class SocketClient implements ClientInterface {
    final BufferedReader input;
    final PrintWriter output;

    //final BufferedReader input_ping;
    //final PrintWriter output_ping;

    private final long maxTimeout = 120000; // (120 sec) Max Timeout for a server response in millisecs

    /* Server Result Buffer */ private HashMap<String, String> results; // {"method" -> "getAvailableGameList", "result" -> "...JSON..."}

    public SocketClient() throws IOException {
        this.results = new HashMap<>(); this.resetServerResult();
        Socket serverSocket = new Socket(CONST.serverLocalHost, CONST.serverLocalSocketPort);
        this.input = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())));
        //this.input_ping = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        //this.output_ping = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())));
    }

    public SocketClient(String ip, int port) throws IOException {
        this.results = new HashMap<>(); this.resetServerResult();
        Socket serverSocket = new Socket(ip, port);
        this.input = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())));
        //this.input_ping = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        //this.output_ping = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())));
    }
    /* ----------------------------------------------------------------------------------------------- */

    private void resetServerResult() { // Reset the server result buffer
        this.results.put("result", ""); this.results.put("method", "");
    }
    private void setServerResult(String methodName) throws IOException { // Set the server result buffer
        this.results.put("result", this.input.readLine()); this.results.put("method", methodName);
    }

    private boolean waitServerResult(String methodName) { // wait for the server result into the buffer
        long startTime = System.currentTimeMillis(); long elapsedTime = 0L;
        while(!this.results.get("method").equals(methodName) && elapsedTime < this.maxTimeout) {elapsedTime = (new Date()).getTime() - startTime;}
        if(!this.results.get("method").equals(methodName)) {
            this.resetServerResult();
            return false;}
        return true;
    }


    /* ----- Functions that are called by the client to control the game ----------------------------- */
    // Prototypes: ClientInterface, RmiVirtualServer
    // Implementations: RmiClient, SocketClient



    public void ping() throws IOException {
        synchronized (this.output) {this.output.println("ping"); this.output.flush();}
    }

    public ArrayList<String> getAvailableGameList() throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("getAvailableGameList");
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("getAvailableGameList")) throw new IOException();
            /* Get the server result */
            JSONArray o = new JSONArray(this.results.get("result"));
            /* Reset the result buffer */
            this.resetServerResult();
            o.forEach(s -> {
                result.add(s.toString());
            });
        }
        return result;
    }

    public ArrayList<String> getPlayerList(String name) throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("getPlayerList");
            this.output.println(name);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("getPlayerList")) throw new IOException();
            /* Get the server result */
            JSONArray o = new JSONArray(this.results.get("result"));
            /* Reset the result buffer */
            this.resetServerResult();
            o.forEach(s -> {
                result.add(s.toString());
            });
        }
        return result;
    }

    public boolean gameAlreadyExists(String name) throws IOException {
        boolean result = true;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("gameAlreadyExists");
            this.output.println(name);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("gameAlreadyExists")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result").equals("1");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }

    public boolean playerAlreadyExists(String game, String player) throws IOException {
        boolean result = true;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("playerAlreadyExists");
            this.output.println(game);
            this.output.println(player);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("playerAlreadyExists")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result").equals("1");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }

    public boolean addGame(String name, int numPlayers) throws IOException {
        String result = "0";
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("addGame");
            this.output.println(name);
            this.output.println(numPlayers);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("addGame")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        if(result.equals("exception")) throw new IOException();
        else return result.equals("1");
    }

    public boolean addPlayer(String game, String player, String color) throws IOException {
        String result = "0";
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("addPlayer");
            this.output.println(game);
            this.output.println(player);
            this.output.println(color);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("addPlayer")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result.equals("1");
    }

    public boolean numPlayersReached(String game) throws IOException {
        String result = "1";
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("numPlayersReached");
            this.output.println(game);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("numPlayersReached")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result.equals("1");
    }

    public boolean colorAlreadyExists(String game, String color) throws IOException {
        String result = "1";
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("colorAlreadyExists");
            this.output.println(game);
            this.output.println(color);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("colorAlreadyExists")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result.equals("1");
    }

    /* ----------------------------------------------------------------------------------------------- */

    private boolean handCoordinatesContains(ArrayList<int[]> handCoordinates, int x, int y) {
        for (int[] handCoo : handCoordinates) {if (handCoo[0] == x && handCoo[1] == y) return true;}
        return false;}

    @Override
    public void run() {

        /*new Thread(() -> {
            try {
                String line;
                while((line = this.input.readLine()) != null) {
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
                String line;
                while((line = this.input.readLine()) != null) { // Read message type
                    switch(line) {

                        case "ping" -> {}

                        /* ----- Functions that are called by the server to send actions (or messages) to the client ----- */
                        // Prototypes: RmiVirtualClient
                        // Implementations: RmiClient, SocketClient

                        // ----- Results from server methods to client methods ----------------------------------------
                        case "getAvailableGameList_result" -> this.setServerResult("getAvailableGameList");
                        case "getPlayerList_result" -> this.setServerResult("getPlayerList");
                        case "gameAlreadyExists_result" -> this.setServerResult("gameAlreadyExists");
                        case "playerAlreadyExists_result" -> this.setServerResult("playerAlreadyExists");
                        case "addGame_result" -> this.setServerResult("addGame");
                        case "addPlayer_result" -> this.setServerResult("addPlayer");
                        case "numPlayersReached_result" -> this.setServerResult("numPlayersReached");
                        case "colorAlreadyExists_result" -> this.setServerResult("colorAlreadyExists");
                        // --------------------------------------------------------------------------------------------

                        case "startGame" -> {
                            System.out.println("THE GAME STARTS NOW.");
                        }
                        case "updateTable" -> {
                            System.out.println(this.input.readLine().replace('§', '\n'));
                        }
                        case "endGame" -> {
                            System.out.println("\n* THE GAME IS FINISHED! *\nWINNER(S) IS(ARE): " + this.input.readLine());
                        }

                        case "chooseStarterCardSide" -> {
                            String frontTopLeft = this.input.readLine();
                            String frontTopRight = this.input.readLine();
                            String frontBottomLeft = this.input.readLine();
                            String frontBottomRight = this.input.readLine();
                            String backTopLeft = this.input.readLine();
                            String backTopRight = this.input.readLine();
                            String backBottomLeft = this.input.readLine();
                            String backBottomRight = this.input.readLine();
                            String middleString = this.input.readLine();
                            ArrayList<String> middleArr = new ArrayList<>();
                            (new JSONArray(middleString)).forEach(o -> {middleArr.add(o.toString());});
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
                            this.output.println("chooseStarterCardSide_result");
                            this.output.println(inputString);
                            this.output.flush();
                        }

                        case "chooseObjectiveCard" -> {
                            int points1 = Integer.parseInt(this.input.readLine());
                            ArrayList<String> res1 = new ArrayList<>(); (new JSONArray(this.input.readLine())).forEach(o -> {res1.add(o.toString());});
                            String card1_0_type = this.input.readLine();
                            String card1_0_pos = this.input.readLine();
                            String card1_1_type = this.input.readLine();
                            String card1_1_pos = this.input.readLine();
                            String card1_2_type = this.input.readLine();
                            String card1_2_pos = this.input.readLine();
                            int points2 = Integer.parseInt(this.input.readLine());
                            ArrayList<String> res2 = new ArrayList<>(); (new JSONArray(this.input.readLine())).forEach(o -> {res2.add(o.toString());});
                            String card2_0_type = this.input.readLine();
                            String card2_0_pos = this.input.readLine();
                            String card2_1_type = this.input.readLine();
                            String card2_1_pos = this.input.readLine();
                            String card2_2_type = this.input.readLine();
                            String card2_2_pos = this.input.readLine();
                            ViewTUI.objective2Cards(
                                    points1, res1, card1_0_type, card1_0_pos, card1_1_type, card1_1_pos, card1_2_type, card1_2_pos,
                                    points2, res2, card2_0_type, card2_0_pos, card2_1_type, card2_1_pos, card2_2_type, card2_2_pos
                            );
                            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose 1 of the 2 objective cards. 1 or 2: " + CONST.TEXT_RESET);
                            Scanner scan = new Scanner(System.in);
                            String inputString = "";
                            inputString = scan.nextLine().toLowerCase();
                            while(!inputString.equals("1") && !inputString.equals("2")) {
                                System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert '1' or '2'!" + CONST.TEXT_RESET);
                                System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose 1 of the 2 objective cards. 1 or 2: " + CONST.TEXT_RESET);
                                inputString = scan.nextLine().toLowerCase();
                            }
                            this.output.println("chooseObjectiveCard_result");
                            this.output.println(inputString);
                            this.output.flush();
                        }

                        case "chooseToPlaceCard" -> {
                            String view = this.input.readLine().replace('§', '\n');
                            String handCoordinatesString = this.input.readLine();
                            String goldHandCoordinatesString = this.input.readLine();
                            int resourceHandSize = Integer.parseInt(this.input.readLine());
                            int goldHandSize = Integer.parseInt(this.input.readLine());
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
                                String[] goldHC = goldHandCoordinatesString.split(";"); // goldHC contiene le stringhe di ogni gold card: "x-y x-y x-y", "..", ...
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
                            this.output.println("chooseToPlaceCard_result");
                            this.output.println(inputString);
                            this.output.flush();
                        }

                        case "chooseToDrawCard" -> {
                            String view = this.input.readLine().replace('§', '\n');
                            boolean isResourceCardDeckFinished = this.input.readLine().equals("1");
                            boolean isGoldCardDeckFinished = this.input.readLine().equals("1");

                            System.out.println("DRAW A CARD");
                            System.out.print(view);
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
                            this.output.println("chooseToDrawCard_result");
                            this.output.println(inputString);
                            this.output.flush();
                        }

                        default -> {/*System.err.println("[INVALID MESSAGE]");*/ /* Reset the result buffer */ this.resetServerResult();}

                        /* ----------------------------------------------------------------------------------------------- */

                    }
                }
            } catch (IOException e) {System.err.print("IO Exception!");}
        }).start();
    }



}
