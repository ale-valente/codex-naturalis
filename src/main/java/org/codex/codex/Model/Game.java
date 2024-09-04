package org.codex.codex.Model;

import org.codex.codex.CONST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private boolean isStarted = false; // true if the game is started
    private int maxNumPlayers; // number of players to start the game
    private ResourceCardDeck resourceCardDeck; // resource card deck of the game
    private GoldCardDeck goldCardDeck; // gold card deck of the game
    private StarterCardDeck starterCardDeck; // starter card deck of the game
    private ObjectiveCardDeck objectiveCardDeck; // objective card deck of the game

    private int resourceCardDeckIndex = 0; // resource card deck index of the next card to draw
    private int goldCardDeckIndex = 0; // gold card deck index of the next card to draw
    private int starterCardDeckIndex = 0; // starter card deck index of the next card to draw
    private int objectiveCardDeckIndex = 0; // objective card deck index of the next card to draw

    private ResourceCard publicResourceCard1; // public resource card 1
    private ResourceCard publicResourceCard2; // public resource card 2
    private GoldCard publicGoldCard1; // public gold card 1
    private GoldCard publicGoldCard2; // public gold card 2

    private ObjectiveCard publicObjectiveCard1; // public objective card 1
    private ObjectiveCard publicObjectiveCard2; // public objective card 2

    /*
    player maps each player nickname with the Player() object
     */
    private HashMap<String, Player> player; // {"nickname123" -> Player() , ...}



    public Game(int numPlayers) throws IOException {
        this.maxNumPlayers = numPlayers;
        this.resourceCardDeck = new ResourceCardDeck();
        this.goldCardDeck = new GoldCardDeck();
        this.starterCardDeck = new StarterCardDeck();
        this.objectiveCardDeck = new ObjectiveCardDeck();
        this.player = new HashMap<>();
    }

    /*
    it verifies if the game is started (when the number of participants is reached)
     */
    public boolean isStarted() {return this.isStarted;}

    /*
    set the game as started
     */
    public void setIsStarted(boolean b) {this.isStarted = b;}

    /*
    get maxNumPlayers
     */
    public int getMaxNumPlayers() {return this.maxNumPlayers;}

    /*
    return the current number of players in the game
     */
    public int getNumPlayers() {return this.player.size();}

    /*
    returns the resource card deck
     */
    public ResourceCardDeck getResourceCardDeck() {return this.resourceCardDeck;}

    /*
    returns the gold card deck
     */
    public GoldCardDeck getGoldCardDeck() {return this.goldCardDeck;}

    /*
    given a nickname, it returns the Player() object
     */
    public Player getPlayer(String nickname) {return this.player.get(nickname);}
    public ArrayList<String> getPlayerList() {
        ArrayList<String> a = new ArrayList<>();
        this.player.forEach((k,v) -> {a.add(k);});
        return a;
    }

    /*
    it checks if a player nickname already exists
     */
    public boolean playerAlreadyExists(String name) {return this.player.containsKey(name);}

    /*
    check if the number of players to start the game is reached
     */
    public boolean maxNumPlayersReached() {return (this.player.size() == this.maxNumPlayers);}

    /*
    add a player to the game (given his nickname and color)
     */
    public void addPlayer(String name, String color) {
        Player p = new Player(); p.setColor(color);
        this.player.put(name, p);}

    public void removeAllPlayers() {
        this.player.clear();
    }

    /*
    check if the resource card deck is finished
     */
    public boolean isResourceCardDeckFinished() {return this.resourceCardDeckIndex >= CONST.resourceCards;}

    /*
    check if the gold card deck is finished
     */
    public boolean isGoldCardDeckFinished() {return this.goldCardDeckIndex >= CONST.goldCards;}

    /*
    get a new resource card from the resource card deck
     */
    public ResourceCard getNewResourceCard() {
        this.resourceCardDeckIndex += 1;
        if(this.resourceCardDeckIndex > CONST.resourceCards) {this.resourceCardDeckIndex -= 1; return null;}
        return this.resourceCardDeck.getCard(this.resourceCardDeckIndex-1);
    }

    /*
    get a new gold card from the gold card deck
     */
    public GoldCard getNewGoldCard() {
        this.goldCardDeckIndex += 1;
        if(this.goldCardDeckIndex > CONST.goldCards) {this.goldCardDeckIndex -= 1; return null;}
        return this.goldCardDeck.getCard(this.goldCardDeckIndex-1);
    }

    /*
    get a new starter card from the starter card deck
     */
    public StarterCard getNewStarterCard() {
        this.starterCardDeckIndex += 1;
        if(this.starterCardDeckIndex > CONST.starterCards) {this.starterCardDeckIndex -= 1; return null;}
        return this.starterCardDeck.getCard(this.starterCardDeckIndex-1);
    }

    /*
    get a new objective card from the objective card deck
     */
    public ObjectiveCard getNewObjectiveCard() {
        this.objectiveCardDeckIndex += 1;
        if(this.objectiveCardDeckIndex > CONST.objectiveCards) {this.objectiveCardDeckIndex -= 1; return null;}
        return this.objectiveCardDeck.getCard(this.objectiveCardDeckIndex-1);
    }

    /* --- standard get and set methods for public cards --- */
    public ResourceCard getPublicResourceCard1() {return this.publicResourceCard1;}
    public ResourceCard getPublicResourceCard2() {return this.publicResourceCard2;}
    public GoldCard getPublicGoldCard1() {return this.publicGoldCard1;}
    public GoldCard getPublicGoldCard2() {return this.publicGoldCard2;}

    public ObjectiveCard getPublicObjectiveCard1() {return this.publicObjectiveCard1;}
    public ObjectiveCard getPublicObjectiveCard2() {return this.publicObjectiveCard2;}

    public void setPublicResourceCard1(ResourceCard c) {this.publicResourceCard1 = c;}
    public void setPublicResourceCard2(ResourceCard c) {this.publicResourceCard2 = c;}
    public void setPublicGoldCard1(GoldCard c) {this.publicGoldCard1 = c;}
    public void setPublicGoldCard2(GoldCard c) {this.publicGoldCard2 = c;}

    public void setPublicObjectiveCard1(ObjectiveCard c) {this.publicObjectiveCard1 = c;}
    public void setPublicObjectiveCard2(ObjectiveCard c) {this.publicObjectiveCard2 = c;}
    /* ----------------------------------------------------- */

}
