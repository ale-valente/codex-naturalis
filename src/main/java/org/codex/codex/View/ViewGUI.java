package org.codex.codex.View;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.codex.codex.CONST;
import org.codex.codex.Client;
import org.codex.codex.ClientInterface;

import java.io.IOException;
import java.util.ArrayList;

public class ViewGUI {

    public static AnchorPane root = new AnchorPane();
    public static TextField text = new TextField();
    public static Button sendButton = new Button("Send");
    public static HBox bottom = new HBox();
    public static Label message = new Label();
    public static Label messageError = new Label();

    public static ClientInterface client;


    public static void setupHandlers() {





    }


    public static void anchorElement(javafx.scene.Node element, double x, double y) {
        ViewGUI.root.getChildren().remove(element);
        ViewGUI.root.getChildren().add(element);
        AnchorPane.setLeftAnchor(element, x);
        AnchorPane.setTopAnchor(element, y);
    }

    public static void removeAnchorElement(javafx.scene.Node element) {
        ViewGUI.root.getChildren().remove(element);
    }


    public static void table(ArrayList<String> historyPlacedCards) throws IOException {
        // ArrayList<String> historyPlacedCards = "assets/gold/5f.jpg 123 145"
        ViewGUI.root.getChildren().forEach(o -> {
            if(o instanceof ImageView) ViewGUI.root.getChildren().remove(o);
        });
        for(int i = 0; i < historyPlacedCards.size(); i++) {
            String[] h = historyPlacedCards.get(i).split(" ");
            ImageView img = new ImageView(new Image(Client.class.getResource(h[0]).openStream()));
            img.setFitHeight(CONST.guiCardHeight); img.setFitWidth(CONST.guiCardWidth);
            img.setSmooth(true); img.setPreserveRatio(false);
            ViewGUI.anchorElement(img, Double.parseDouble(h[1]), Double.parseDouble(h[2]));
        }

    }



}
