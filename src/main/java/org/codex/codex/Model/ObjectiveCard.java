package org.codex.codex.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectiveCard {

    private final String asset; // "13" -> reference to: "assets/.../13f" & "assets/.../13b"

    private final int points;
    /*"card":{
        "0":{"type":"mushrooms", "pos":""},
        "1":{"type":"animals", "pos":"angleTopRight"},
        "2":{"type":"animals", "pos":"top"}}*/
    // "pos" indicates where the card before is in respect to the current card
    // EXAMPLE: "1":{"type":"animals", "pos":"angleTopRight"} indicates that the card 0 touch the top right angle of the card 1
    // EXAMPLE: "2":{"type":"animals", "pos":"top"} indicates that the card 1 is on top the card 2
    private final ArrayList<HashMap<String, String>> card;
    private final ArrayList<String> res; // ["feather", "ink", "parchment"]

    public ObjectiveCard(int points, ArrayList<HashMap<String, String>> card, ArrayList<String> res, String asset) {
        this.points = points; this.card = card; this.res = res; this.asset = asset;
    }

    public int getPoints() {return this.points;}
    public ArrayList<HashMap<String, String>> getCard() {return this.card;}
    public ArrayList<String> getRes() {return this.res;}
    public String getAsset() {return this.asset;}
}
