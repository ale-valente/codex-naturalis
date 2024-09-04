package org.codex.codex.Model;

import java.util.ArrayList;

public class Card {

    private final String type; // "resource", "gold", "starter"
    private final String asset; // "13" -> reference to: "assets/.../13f" & "assets/.../13b"

    private final String frontTopLeft; // = "insects"
    private final String frontTopRight; // = "animals"
    private final String frontBottomLeft; // = "mushrooms"
    private final String frontBottomRight; // = "vegetables"

    private final String backTopLeft; // = "insects"
    private final String backTopRight; // = "animals"
    private final String backBottomLeft; // = "mushrooms"
    private final String backBottomRight; // = "vegetables"

    private ArrayList<String> backMiddle; // = ["animals", "insects", "vegetables"] <-- generic case (starter card)

    public Card(
            String type,
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            ArrayList<String> backMiddle,
            String asset
    ) {
        this.type = type;
        this.frontTopLeft = frontTopLeft;
        this.frontTopRight = frontTopRight;
        this.frontBottomLeft = frontBottomLeft;
        this.frontBottomRight = frontBottomRight;
        this.backTopLeft = backTopLeft;
        this.backTopRight = backTopRight;
        this.backBottomLeft = backBottomLeft;
        this.backBottomRight = backBottomRight;
        this.backMiddle = backMiddle;
        this.asset = asset;
    }

    /*
    standard methods of get and set
     */
    public String getType() {return this.type;}

    public String getFrontTopLeft() {return this.frontTopLeft;}
    public String getFrontTopRight() {return this.frontTopRight;}
    public String getFrontBottomLeft() {return this.frontBottomLeft;}
    public String getFrontBottomRight() {return this.frontBottomRight;}
    //public ArrayList<String> getFrontMiddle() {return this.frontMiddle;}

    public String getBackTopLeft() {return this.backTopLeft;}
    public String getBackTopRight() {return this.backTopRight;}
    public String getBackBottomLeft() {return this.backBottomLeft;}
    public String getBackBottomRight() {return this.backBottomRight;}
    public ArrayList<String> getBackMiddle() {return this.backMiddle;}

    public String getAsset() {return this.asset;}

    public int getPoints() {return 0;}
    public String getPointsRes() {return "";}

    public void setBackMiddle(ArrayList<String> middle) {this.backMiddle = middle;}


}
