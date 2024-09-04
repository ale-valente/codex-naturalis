package org.codex.codex.Model;

public class Box {
    /*
    cardPos =
    ""
    "frontTop", "frontBottom", "frontLeft", "frontRight"
    "backTop", "backBottom", "backLeft", "backRight"
    "frontTopLeft", "frontTopRight", "frontBottomLeft", "frontBottomRight", "frontMiddle"
    "backTopLeft", "backTopRight", "backBottomLeft", "backBottomRight", "backMiddle"
    */
    private Card visibleCard; // reference to the card
    private String visibleCardPos; // card position on the box
    private Card hiddenCard; // reference to the card
    private String hiddenCardPos; // card position on the box

    public Box() {
        this.visibleCard = null;
        this.visibleCardPos = "";
        this.hiddenCard = null;
        this.hiddenCardPos = "";
    }

    /*
    standard methods of get and set
     */
    public Card getVisibleCard() {return this.visibleCard;}
    public String getVisibleCardPos() {return this.visibleCardPos;}
    public Card getHiddenCard() {return this.hiddenCard;}
    public String getHiddenCardPos() {return this.hiddenCardPos;}

    public void setVisibleCard(Card c) {this.visibleCard = c;}
    public void setVisibleCardPos(String s) {this.visibleCardPos = s;}
    public void setHiddenCard(Card c) {this.hiddenCard = c;}
    public void setHiddenCardPos(String s) {this.hiddenCardPos = s;}


}
