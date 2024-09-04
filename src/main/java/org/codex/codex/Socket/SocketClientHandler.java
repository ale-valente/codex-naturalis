package org.codex.codex.Socket;

import org.codex.codex.Model.GoldCard;
import org.codex.codex.Model.ResourceCard;
import org.codex.codex.Server;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SocketClientHandler {
    final BufferedReader input;
    //final BufferedReader input_ping;
    final PrintWriter output;

    //final PrintWriter output_ping;

    private final long maxTimeout = 120000; // (120 sec) Max Timeout for a server response in millisecs
    /* Server Result Buffer */ public HashMap<String, String> results; // {"method" -> "getAvailableGameList", "result" -> "...JSON..."}

    public SocketClientHandler(BufferedReader input, BufferedReader input_ping, PrintWriter output_ping, PrintWriter output) {
        this.input = input; //this.input_ping = input_ping;
        this.output = output; //this.output_ping = output_ping;
        this.results = new HashMap<>(); this.resetServerResult();}

    private void resetServerResult() { // Reset the server result buffer
        this.results.put("result", ""); this.results.put("method", "");
    }

    public void setServerResult(String methodName) throws IOException { // Set the server result buffer
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

    /* ----- Functions that are called by the server to send actions (or messages) to the client ----- */

    public void startGame() throws IOException {
        synchronized (this.output) {
            this.output.println("startGame");
            this.output.flush();
        }
    }

    public void ping() throws IOException {
        synchronized (this.output) {
            this.output.println("ping");
            this.output.flush();
        }
    }

    public void updateTable(String view) throws IOException {
        synchronized (this.output) {
            this.output.println("updateTable");
            this.output.println(view.replace('\n', '§')); // '§' in never used in the view string
            this.output.flush();
        }
    }

    public void endGame(String s) throws IOException {
        synchronized (this.output) {
            this.output.println("endGame");
            this.output.println(s);
            this.output.flush();
        }
    }

    public String chooseStarterCardSide(
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            ArrayList<String> middle) throws IOException {
        String result;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("chooseStarterCardSide");
            this.output.println(frontTopLeft);
            this.output.println(frontTopRight);
            this.output.println(frontBottomLeft);
            this.output.println(frontBottomRight);
            this.output.println(backTopLeft);
            this.output.println(backTopRight);
            this.output.println(backBottomLeft);
            this.output.println(backBottomRight);
            this.output.println((new JSONArray(middle)).toString());
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("chooseStarterCardSide")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }

    public String chooseObjectiveCard(
            int points1, ArrayList<String> res1, String card1_0_type, String card1_0_pos, String card1_1_type, String card1_1_pos, String card1_2_type, String card1_2_pos,
            int points2, ArrayList<String> res2, String card2_0_type, String card2_0_pos, String card2_1_type, String card2_1_pos, String card2_2_type, String card2_2_pos
    ) throws IOException {
        String result;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("chooseObjectiveCard");
            this.output.println(points1);
            this.output.println((new JSONArray(res1)).toString());
            this.output.println(card1_0_type);
            this.output.println(card1_0_pos);
            this.output.println(card1_1_type);
            this.output.println(card1_1_pos);
            this.output.println(card1_2_type);
            this.output.println(card1_2_pos);
            this.output.println(points2);
            this.output.println((new JSONArray(res2)).toString());
            this.output.println(card2_0_type);
            this.output.println(card2_0_pos);
            this.output.println(card2_1_type);
            this.output.println(card2_1_pos);
            this.output.println(card2_2_type);
            this.output.println(card2_2_pos);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("chooseObjectiveCard")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }

    public String chooseToPlaceCard(
            String view,
            String handCoordinatesString, String goldHandCoordinatesString,
            int resourceHandSize, int goldHandSize
    ) throws IOException {
        String result;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("chooseToPlaceCard");
            this.output.println(view.replace('\n', '§')); // '§' in never used in the view string
            this.output.println(handCoordinatesString.isEmpty() ? "empty" : handCoordinatesString);
            this.output.println(goldHandCoordinatesString.isEmpty() ? "empty" : goldHandCoordinatesString);
            this.output.println(resourceHandSize);
            this.output.println(goldHandSize);
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("chooseToPlaceCard")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }


    public String chooseToDrawCard(String publicCardsString, boolean isResourceCardDeckFinished, boolean isGoldCardDeckFinished) throws IOException {
        String result;
        synchronized (this.output) {
            /* Invoke the method on the server */
            this.output.println("chooseToDrawCard");
            this.output.println(publicCardsString.replace('\n', '§')); // '§' in never used in the view string
            this.output.println(isResourceCardDeckFinished ? "1" : "0");
            this.output.println(isGoldCardDeckFinished ? "1" : "0");
            this.output.flush();
            /* Wait the thread that is reading the server result */
            if (!this.waitServerResult("chooseToDrawCard")) throw new IOException();
            /* Get the server result */
            result = this.results.get("result");
            /* Reset the result buffer */
            this.resetServerResult();
        }
        return result;
    }

    /* ----------------------------------------------------------------------------------------------- */


}
