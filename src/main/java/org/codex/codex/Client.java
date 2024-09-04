package org.codex.codex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.codex.codex.RMI.RmiClient;
import org.codex.codex.Socket.SocketClient;
import org.codex.codex.View.ViewGUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.application.Platform.exit;

public class Client extends Application {

    static String game;
    static String nickname;




    /* ----- Main ------------------------------------------------------------------------------------ */
    public static void main(String[] args) {
        String typeOfNetwork; String typeOfUI;

        final int maxNameLen = CONST.maxNameLen;
        final int maxParticipants = CONST.maxParticipants;

        Scanner scan = new Scanner(System.in);
        String inputString = "";
        int inputInt = 0;
        boolean serverError = false;
        ArrayList<String> gamesList = null;
        ArrayList<String> participantsList = null;
        AtomicInteger numOfGame = new AtomicInteger();
        AtomicBoolean notValid = new AtomicBoolean(false);
        String gameName;
        String nickname;
        int gameNumParticipants;
        boolean gameAlreadyExists;
        boolean nicknameAlreadyExistsOrMaxNumPlayersReached;
        boolean createGame;
        boolean numPlayersReached;
        boolean numPlayersReachedException;

        System.out.println(CONST.TEXT_TITLE);

        // RMI or TCP Socket -------------------------------------------------------------------------------------------
        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "NETWORK: Choose RMI [r] or TCP Socket [s]: " + CONST.TEXT_RESET);
        inputString = scan.nextLine().toLowerCase();
        while(!inputString.equals("r") && !inputString.equals("s")) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert 'r' or 's'!" + CONST.TEXT_RESET);
            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "NETWORK: Choose RMI [r] or TCP Socket [s]: " + CONST.TEXT_RESET);
            inputString = scan.nextLine().toLowerCase();
        }
        // "r" = RMI - "s" = TCP Socket - ... other letters for other types of network
        typeOfNetwork = inputString;

        // GUI or TUI --------------------------------------------------------------------------------------------------
        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "VIEW: Choose GUI [g] or TUI [t]: " + CONST.TEXT_RESET);
        inputString = scan.nextLine().toLowerCase();
        while(!inputString.equals("g") && !inputString.equals("t")) {
            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert 'g' or 't'!" + CONST.TEXT_RESET);
            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "VIEW: Choose GUI [g] or TUI [t]: " + CONST.TEXT_RESET);
            inputString = scan.nextLine().toLowerCase();
        }
        // "g" = GUI - "t" = TUI - ... other letters for other types of UI
        typeOfUI = inputString;

        System.out.println("Connecting to the server ...");
        ClientInterface client = null;
        if(typeOfNetwork.equals("r")) { // Run the RMI Client
            try{
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter server ip (leave empty and press enter if you want to use the local server): ");
                String serverIp = scanner.nextLine().toLowerCase();
                if(!serverIp.isEmpty()) client = new RmiClient(serverIp);
                else client = new RmiClient();
            } catch(IOException | NotBoundException e) {System.out.println(CONST.TEXT_RED + "RMI Client - IO/NotBound Exception!" + CONST.TEXT_RESET);}}
        else { // Run the Socket Client
            try {
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
                }
                if(!serverIp.isEmpty()) client = new SocketClient(serverIp, serverSocketPort);
                else client = new SocketClient();
            } catch(IOException e) {System.out.println(CONST.TEXT_RED + "Socket Client - IO Exception!" + CONST.TEXT_RESET);}}
        try{assert client != null; client.run();
        } catch(RemoteException e) {/*System.out.println(e);*/ System.out.println(CONST.TEXT_RED + "Client - Remote Exception!" + CONST.TEXT_RESET);}
        System.out.println(CONST.TEXT_GREEN_BOLD + "Connected." + CONST.TEXT_RESET);



        // Ping to check the network of the server #########################################
        ClientInterface finalClient = client;
        new Thread(() -> {
            boolean pingEnd = false;
            while(!pingEnd) {
                try{finalClient.ping();}
                catch(IOException e) {System.out.println(CONST.TEXT_RED + "Server has lost the network!" + CONST.TEXT_RESET); pingEnd = true;}
            }
        }).start();
        // ###################################################################################################




        // Launch GUI or TUI
        if(typeOfUI.equals("g")) { // GUI
            System.out.println("Running GUI ...");
            ViewGUI.client = client;
            launch(/*args*/); // Launch GUI
        }
        else { // ***** TUI ********************************************************************************************
            System.out.println("Running TUI ...");
            do {
                System.out.println("Getting the list of online games ...");
                try{gamesList = client.getAvailableGameList();}
                catch(IOException e) {System.out.println(CONST.TEXT_RED + "Client - IO Exception!" + CONST.TEXT_RESET);}
                if(gamesList == null) gamesList = new ArrayList<>();
                numOfGame.set(0);
                System.out.println("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Select one game or create one inserting its number: " + CONST.TEXT_RESET);
                System.out.println(0 + " - " + "Create a new game...");
                gamesList.forEach((g) -> {numOfGame.set(numOfGame.get() + 1); System.out.println(numOfGame + " - " + g);});
                System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Game: " + CONST.TEXT_RESET);
                inputInt = Integer.parseInt(scan.nextLine());
                while (inputInt < 0 || inputInt > numOfGame.get()) {
                    System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid number!" + CONST.TEXT_RESET);
                    numOfGame.set(0);
                    System.out.println("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Select one game or create one inserting its number: " + CONST.TEXT_RESET);
                    System.out.println(0 + " - " + "Create a new game...");
                    gamesList.forEach((g) -> {numOfGame.set(numOfGame.get() + 1); System.out.println(numOfGame + " - " + g);});
                    System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Game: " + CONST.TEXT_RESET);
                    inputInt = Integer.parseInt(scan.nextLine());
                }
                if (inputInt == 0) { // Create game
                    createGame = true;
                    do {
                        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a game name (max " + maxNameLen + " characters): " + CONST.TEXT_RESET);
                        inputString = scan.nextLine().toLowerCase();
                        try {notValid.set(client.gameAlreadyExists(inputString));
                        } catch (IOException e) {
                            notValid.set(true);
                            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the game name!" + CONST.TEXT_RESET);}
                        while (inputString.isEmpty() || inputString.length() > maxNameLen || notValid.get()) {
                            notValid.set(false);
                            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid game name!" + CONST.TEXT_RESET);
                            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a game name (max " + maxNameLen + " characters): " + CONST.TEXT_RESET);
                            inputString = scan.nextLine().toLowerCase();
                            try {notValid.set(client.gameAlreadyExists(inputString));
                            } catch (IOException e) {
                                notValid.set(true);
                                System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the game name!" + CONST.TEXT_RESET);}}
                        gameName = inputString;
                        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose the number of participants (min 2, max " + maxParticipants + " participants): " + CONST.TEXT_RESET);
                        inputInt = Integer.parseInt(scan.nextLine());
                        while (inputInt <= 1 || inputInt > maxParticipants) {
                            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid number of participants!" + CONST.TEXT_RESET);
                            System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose the number of participants (min 2, max " + maxParticipants + " participants): " + CONST.TEXT_RESET);
                            inputInt = Integer.parseInt(scan.nextLine());}
                        gameNumParticipants = inputInt;
                        gameAlreadyExists = true;
                        try {gameAlreadyExists = !client.addGame(gameName, gameNumParticipants); serverError = false;} catch (IOException e) {serverError = true;}
                        if(serverError) System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Error. Try Again." + CONST.TEXT_RESET);
                        else if(gameAlreadyExists) System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Game name already exists!" + CONST.TEXT_RESET);
                    } while(serverError || gameAlreadyExists);
                } else {
                    createGame = false;
                    gameName = gamesList.get(inputInt-1);

                    System.out.println("Getting the list of participants ...");
                    try{participantsList = client.getPlayerList(gameName);}
                    catch(IOException e) {System.out.println(CONST.TEXT_RED + "Client - IO Exception!" + CONST.TEXT_RESET);}
                    if(participantsList == null) participantsList = new ArrayList<>();
                    participantsList.forEach(System.out::println);
                }

                do {
                    System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a nickname (max " + maxNameLen + " characters): " + CONST.TEXT_RESET);
                    inputString = scan.nextLine().toLowerCase();
                    try {notValid.set(client.playerAlreadyExists(gameName, inputString));
                    } catch (IOException e) {
                        notValid.set(true);
                        System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the nickname!" + CONST.TEXT_RESET);}
                    while (inputString.isEmpty() || inputString.length() > maxNameLen || notValid.get()) {
                        notValid.set(false);
                        System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid nickname!" + CONST.TEXT_RESET);
                        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a nickname (max " + maxNameLen + " characters): " + CONST.TEXT_RESET);
                        inputString = scan.nextLine().toLowerCase();
                        try {notValid.set(client.playerAlreadyExists(gameName, inputString));
                        } catch (IOException e) {
                            notValid.set(true);
                            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the nickname!" + CONST.TEXT_RESET);}
                    }
                    nickname = inputString;

                    // Choose a color
                    String color = "";
                    System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a color ('black', 'blue', 'green', 'red', 'yellow'): " + CONST.TEXT_RESET);
                    inputString = scan.nextLine().toLowerCase();
                    if (!inputString.equals("black") && !inputString.equals("blue") && !inputString.equals("green") && !inputString.equals("red") && !inputString.equals("yellow")) {
                        notValid.set(true);
                    } else {
                        try {
                            notValid.set(client.colorAlreadyExists(gameName, inputString));
                        } catch (IOException e) {
                            notValid.set(true);
                            System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the color!" + CONST.TEXT_RESET);
                        }
                    }
                    while (notValid.get()) {
                        notValid.set(false);
                        System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Insert a valid color or maybe the color is already taken!" + CONST.TEXT_RESET);
                        System.out.print("\uD83D\uDD39 " + CONST.TEXT_BLUE_BOLD + "Choose a color ('black', 'blue', 'green', 'red', 'yellow'): " + CONST.TEXT_RESET);
                        inputString = scan.nextLine().toLowerCase();
                        if (!inputString.equals("black") && !inputString.equals("blue") && !inputString.equals("green") && !inputString.equals("red") && !inputString.equals("yellow")) {
                            notValid.set(true);
                        } else {
                            try {
                                notValid.set(client.colorAlreadyExists(gameName, inputString));
                            } catch (IOException e) {
                                notValid.set(true);
                                System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the color!" + CONST.TEXT_RESET);
                            }
                        }
                    }
                    color = inputString;

                    nicknameAlreadyExistsOrMaxNumPlayersReached = true;
                    numPlayersReached = false;
                    try {nicknameAlreadyExistsOrMaxNumPlayersReached = !client.addPlayer(gameName, nickname, color); serverError = false;} catch (IOException e) {serverError = true;}
                    if(serverError) System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Error. Try Again." + CONST.TEXT_RESET);
                    else if(nicknameAlreadyExistsOrMaxNumPlayersReached) {
                        numPlayersReached = true; numPlayersReachedException = false;
                        try {numPlayersReached = client.numPlayersReached(gameName);}
                        catch (IOException e) {numPlayersReachedException = true; numPlayersReached = false; System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Can't verify from the server the number of players!" + CONST.TEXT_RESET);}
                        if(!numPlayersReachedException) {
                            if(numPlayersReached) System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Number of players reached" + CONST.TEXT_RESET);
                            else System.out.println("\n" + "\uD83D\uDEA8 " + CONST.TEXT_RED_BOLD + "Nickname or color already exists!" + CONST.TEXT_RESET);
                        }
                    }
                    if(numPlayersReached) {serverError = false; nicknameAlreadyExistsOrMaxNumPlayersReached = false;} // to exit from this internal while-loop
                } while(serverError || nicknameAlreadyExistsOrMaxNumPlayersReached);
            } while (numPlayersReached);

            System.out.println(CONST.TEXT_GREEN_BOLD + "The game will start soon..." + CONST.TEXT_RESET);


            exit();

        } // ***********************************************************************************************************




    }
    /* ----------------------------------------------------------------------------------------------- */




    @Override
    public void start(Stage stage) throws IOException {

        //VBox vbox = new VBox();
        //HBox top = new HBox(); // top.setAlignment(Pos.CENTER);
        /*ArrayList<String> historyPlacedCards = new ArrayList<>();
        historyPlacedCards.add("assets/gold/5f.jpg 80 80");
        historyPlacedCards.add("assets/resource/23f.jpg 10 30");
        historyPlacedCards.add("assets/resource/12b.jpg 120 50");
        ViewGUI.table(historyPlacedCards);*/

        ViewGUI.text.setPrefWidth(CONST.guiWidth - 100d);
        ViewGUI.bottom.getChildren().addAll(ViewGUI.text, ViewGUI.sendButton); // bottom.setMaxSize(1024, 1024);
        ViewGUI.anchorElement(ViewGUI.bottom, 20, 720);
        ViewGUI.anchorElement(ViewGUI.message, 20, 680);
        ViewGUI.anchorElement(ViewGUI.messageError, 20, 750);



        ViewGUI.message.setText("Ciao questa è una prova");






        ViewGUI.sendButton.setOnAction(e -> { //label.setText(label.getText() + ".");

            /*
            try{ViewGUI.client.guiText(ViewGUI.text.getText());}
            catch(IOException ee) {ViewGUI.messageError.setText("Client - IO Exception!");}
            */

        });


        // Each card 90x90 , al max 20 cards -> 30x20 = 600 (approximately)
        Scene scene = new Scene(ViewGUI.root, CONST.guiWidth, CONST.guiHeight);

        ViewGUI.setupHandlers();

        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        stage.show();

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        */
        /*
        Image img = new Image("assets/gold/1b.jpg", 360.0d, 360.0d, true, true);
        ImageView imgView = new ImageView(img);
        imgView.setPreserveRatio(true);
        imgView.setFitHeight(360);
        imgView.setFitWidth(360);

        Scene scene = new Scene(new StackPane(imgView OR new Text("Hello")), 600, 600);
        stage.setTitle("Codex Game");
        stage.setScene(scene);
        stage.show();

        VBox vbox = new VBox();
        Button btn = new Button("Refresh");
        Label label = new Label("Test");

        HBox topControls = new HBox();
        topControls.getChildren().addAll(btn, label); // hbox costruisce da sinistra verso destra
        TableView<String> table = new TableView<>();

        vbox.getChildren().addAll(topControls, table); // vbox costruisce dall'alto verso il basso
        */
        // StackPane : elementi sovrapposti
        // GridPane : elementi a griglia
        // TilePane : griglia con tutte le caselle della stessa dimensione




    }



}
