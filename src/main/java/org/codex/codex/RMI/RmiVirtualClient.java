package org.codex.codex.RMI;

import org.codex.codex.Model.GoldCard;
import org.codex.codex.Model.ResourceCard;
import org.codex.codex.Model.StarterCard;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface RmiVirtualClient extends Remote {

    /* ----- Functions that are called by the server to send actions (or messages) to the client ----- */
    // Prototypes: RmiVirtualClient
    // Implementations: RmiClient, SocketClient

    void startGame() throws IOException;

    void ping() throws IOException;

    void updateTable(String view) throws IOException;

    void endGame(String s) throws IOException;

    String chooseStarterCardSide(String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
                                 String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
                                 ArrayList<String> middle) throws IOException;

    String chooseObjectiveCard(
            int points1, ArrayList<String> res1, String card1_0_type, String card1_0_pos, String card1_1_type, String card1_1_pos, String card1_2_type, String card1_2_pos,
            int points2, ArrayList<String> res2, String card2_0_type, String card2_0_pos, String card2_1_type, String card2_1_pos, String card2_2_type, String card2_2_pos
    ) throws IOException;

    String chooseToPlaceCard(
            String view,
            String handCoordinatesString, String goldHandCoordinatesString,
            int resourceHandSize, int goldHandSize
    ) throws IOException;

    String chooseToDrawCard(String publicCardsString, boolean isResourceCardDeckFinished, boolean isGoldCardDeckFinished) throws IOException;

    /* ----------------------------------------------------------------------------------------------- */

}
