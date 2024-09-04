package org.codex.codex.Controller;

import org.codex.codex.CONST;
import org.codex.codex.Model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerController {

    /*
    game contains, per each game name, the Game() object
     */
    private HashMap<String, Game> game; // "nomegame" -> Game()

    public ServerController() {
        this.game = new HashMap<>();
    }


    /*
    return the Game() object given the game name
     */
    public Game getGame(String s) {
        Game g;
        synchronized (this.game) {g = this.game.get(s);}
        return g;
    }

    /*
    if checks if a game name already exists
     */
    public boolean gameAlreadyExists(String name) {
        boolean result = true;
        synchronized (this.game) {result = this.game.containsKey(name);}
        return result;
    }

    /*
    add a new game with its name and number of players
     */
    public boolean addGame(String name, int numPlayers) throws IOException {
        boolean result = false;
        synchronized (this.game) {
            result = !this.game.containsKey(name);
            if(result) this.game.put(name, new Game(numPlayers));
        }
        return result;
    }

    public void removeGame(String name) {
        synchronized (this.game) {
            this.game.remove(name);
        }
    }

    /*
    it verifies if a game is started (when the number of participants is reached)
     */
    public boolean isStarted(String game) {
        boolean result = false;
        synchronized (this.game) {result = this.game.get(game).isStarted();}
        return result;
    }

    /*
    set the game as started
     */
    public void setIsStarted(String game, boolean b) {
        synchronized (this.game) {this.game.get(game).setIsStarted(b);}
    }

    /*
    given a game name, it checks if the nickname already exists
     */
    public boolean playerAlreadyExists(String game, String player) {
        boolean result = true;
        synchronized (this.game) {result = this.game.get(game).playerAlreadyExists(player);}
        return result;
    }

    /*
    given a game name, it checks if the color is already taken
     */
    public boolean colorAlreadyExists(String game, String color) {
        AtomicBoolean result = new AtomicBoolean(false);
        synchronized (this.game) {
            this.game.get(game).getPlayerList().forEach(nickname -> {
                if(this.game.get(game).getPlayer(nickname).getColor().equals(color)) result.set(true);
            });
        }
        return result.get();
    }

    /*
    check if the game, given by its name, has reached the number of participants to start
     */
    public boolean maxNumPlayersReached(String game) {
        boolean result = false;
        synchronized (this.game) {result = this.game.get(game).maxNumPlayersReached();}
        return result;
    }
    /*
    add a player to a game, given the game name, the nickname of the player and his color
     */
    public boolean addPlayer(String game, String player, String color) {
        boolean result = false;
        synchronized (this.game) {
            Game g = this.game.get(game);
            AtomicBoolean resultColor = new AtomicBoolean(false);
            g.getPlayerList().forEach(nickname -> {
                if(g.getPlayer(nickname).getColor().equals(color)) resultColor.set(true);
            });
            result = !g.playerAlreadyExists(player) && !g.maxNumPlayersReached() && !resultColor.get();
            if(result) g.addPlayer(player, color);
        }
        return result;
    }

    public void removeAllPlayers(String game) {
        synchronized (this.game) {
            this.game.get(game).removeAllPlayers();
        }
    }

    /*
    return the Player() associated with the game name and nickname
     */
    public Player getPlayer(String game, String nickname) {
        Player p;
        synchronized (this.game) {p = this.game.get(game).getPlayer(nickname);}
        return p;
    }

    /*
    return an array of string as a list of all games
     */
    public ArrayList<String> getAllGameList() {
        ArrayList<String> list = new ArrayList<>();
        synchronized (this.game) {
            this.game.forEach((s,g) -> {list.add(s);});
        }
        return list;
    }

    /*
    return an array of string as a list of all games that haven't reached the maximum number of participants yet.
    (and the creator of the game is logged in the game <-> g.getNumPlayers() > 0)
     */
    public ArrayList<String> getAvailableGameList() {
        ArrayList<String> list = new ArrayList<>();
        synchronized (this.game) {
            this.game.forEach((s,g) -> {
                if(!g.maxNumPlayersReached() && g.getNumPlayers() > 0 /* at least the creator of the game has to sign in */) list.add(s);
            });
        }
        return list;
    }

    /*
    given a game name, it returns the list of all the players
     */
    public ArrayList<String> getPlayerList(String game) {
        ArrayList<String> a;
        synchronized (this.game) {a = this.game.get(game).getPlayerList();}
        return a;
    }

    /*
    return a new card from the resource card deck
     */
    public ResourceCard getNewResourceCard(String game) {
        ResourceCard c;
        synchronized (this.game) {c = this.game.get(game).getNewResourceCard();}
        return c;
    }

    /*
    return a new card from the gold card deck
     */
    public GoldCard getNewGoldCard(String game) {
        GoldCard c;
        synchronized (this.game) {c = this.game.get(game).getNewGoldCard();}
        return c;
    }

    /*
    return a new card from the starter card deck
     */
    public StarterCard getNewStarterCard(String game) {
        StarterCard c;
        synchronized (this.game) {c = this.game.get(game).getNewStarterCard();}
        return c;
    }

    /*
    return a new card from the objective card deck
     */
    public ObjectiveCard getNewObjectiveCard(String game) {
        ObjectiveCard c;
        synchronized (this.game) {c = this.game.get(game).getNewObjectiveCard();}
        return c;
    }

    /*
    return the public resource card 1
     */
    public ResourceCard getPublicResourceCard1(String game) {
        ResourceCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicResourceCard1();}
        return c;
    }

    /*
    return the public resource card 2
     */
    public ResourceCard getPublicResourceCard2(String game) {
        ResourceCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicResourceCard2();}
        return c;
    }

    /*
    return the public gold card 1
     */
    public GoldCard getPublicGoldCard1(String game) {
        GoldCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicGoldCard1();}
        return c;
    }

    /*
    return the public gold card 2
     */
    public GoldCard getPublicGoldCard2(String game) {
        GoldCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicGoldCard2();}
        return c;
    }

    /*
    return the public objective card 1
     */
    public ObjectiveCard getPublicObjectiveCard1(String game) {
        ObjectiveCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicObjectiveCard1();}
        return c;
    }

    /*
    return the public objective card 2
     */
    public ObjectiveCard getPublicObjectiveCard2(String game) {
        ObjectiveCard c;
        synchronized (this.game) {c = this.game.get(game).getPublicObjectiveCard2();}
        return c;
    }

    /*
    return the public resource card 1
     */
    public void setPublicResourceCard1(String game, ResourceCard c) {
        synchronized (this.game) {this.game.get(game).setPublicResourceCard1(c);}}

    /*
    return the public resource card 2
     */
    public void setPublicResourceCard2(String game, ResourceCard c) {
        synchronized (this.game) {this.game.get(game).setPublicResourceCard2(c);}}

    /*
    return the public gold card 1
     */
    public void setPublicGoldCard1(String game, GoldCard c) {
        synchronized (this.game) {this.game.get(game).setPublicGoldCard1(c);}}

    /*
    return the public gold card 2
     */
    public void setPublicGoldCard2(String game, GoldCard c) {
        synchronized (this.game) {this.game.get(game).setPublicGoldCard2(c);}}

    /*
    return the public objective card 1
     */
    public void setPublicObjectiveCard1(String game, ObjectiveCard c) {
        synchronized (this.game) {this.game.get(game).setPublicObjectiveCard1(c);}}

    /*
    return the public objective card 2
     */
    public void setPublicObjectiveCard2(String game, ObjectiveCard c) {
        synchronized (this.game) {this.game.get(game).setPublicObjectiveCard2(c);}}

    /*
    check if the resource card deck is finished
     */
    public boolean isResourceCardDeckFinished(String game) {
        boolean result;
        synchronized (this.game) {result = this.game.get(game).isResourceCardDeckFinished();}
        return result;
    }

    /*
    check if the gold card deck is finished
     */
    public boolean isGoldCardDeckFinished(String game) {
        boolean result;
        synchronized (this.game) {result = this.game.get(game).isGoldCardDeckFinished();}
        return result;
    }

    /*
    given the game name, player nickname, card object of the card to place, coordinates and the side of the card, this method places the card
     */
    public void placeCard(String game, String nickname, Card card, int x, int y, String side) { // side = "front" or "back"
        synchronized (this.game) {
            Player player = this.getPlayer(game, nickname);
            // Check params' validity
            side = side.toLowerCase();
            if (     //player == null
                // x,y già verificate in handCoordinates
                //|| x < 0 || x >= CONST.tableWidth
                //|| y < 0 || y >= CONST.tableHeight
                /*||*/ (!side.equals("front") && !side.equals("back"))) return;
            // Update all Table Boxes with the new card (new card in visibleCard and old card in hiddenCard)
            this.updateTableBoxCard(player, card, x, y, side);
            // Update the table area to be viewed
            this.updateTableView(player, x, y);
            // Update Resources & Points on the table
            this.updateTableResourcesPoints(player, card, x, y, side);
            // Update Hand coordinates.
            // IMPORTANT: call this function AFTER this.updateTableResourcesPoints(...)
            // in way to correctly evaluate the gold cards with the updated resources (after placed the new card) on the table
            this.updateHandCoordinates(player, x, y);
            // Update Resources coordinates (for objective cards porpouse)
            // ! CENTRAL COORDINATES OF THE CARD AND NOT TOP-LEFT
            player.addCoordResOnTable(card.getBackMiddle().getFirst(), x+1, y+1);
            // Player historical placed card for GUI porpouse (example: "assets/gold/5f.jpg 123 145", "...", ...)
            String s = "";
            if(card.getType().equals("resource")) s += "assets/resource/";
            else if(card.getType().equals("gold")) s += "assets/gold/";
            else if(card.getType().equals("starter")) s += "assets/starter/";
            s += card.getAsset() + (side.equals("front") ? "f" : "b") + ".jpg ";
            s += Integer.toString( (x * (int)(CONST.guiCardWidth/3)) - CONST.differenceGuiTuiX)
                + " " + Integer.toString((y * (int)(CONST.guiCardHeight/3)) - CONST.differenceGuiTuiY);
            player.addHistoryPlacedCards(s);
        }
    }



    // ----- Auxiliar Private Functions ----------------------------------------------------------------------------


    private void updateTableBoxCard(Player player, Card card, int x, int y, String side) {
        int[][] c = new int[9][2];
        c[0] = new int[2]; c[0][0] = x;     c[0][1] = y;    // TopLeft
        c[1] = new int[2]; c[1][0] = x+1;   c[1][1] = y;    // Top
        c[2] = new int[2]; c[2][0] = x+2;   c[2][1] = y;    // TopRight
        c[3] = new int[2]; c[3][0] = x;     c[3][1] = y+1;  // Left
        c[4] = new int[2]; c[4][0] = x+1;   c[4][1] = y+1;  // Middle
        c[5] = new int[2]; c[5][0] = x+2;   c[5][1] = y+1;  // Right
        c[6] = new int[2]; c[6][0] = x;     c[6][1] = y+2;  // BottomLeft
        c[7] = new int[2]; c[7][0] = x+1;   c[7][1] = y+2;  // Bottom
        c[8] = new int[2]; c[8][0] = x+2;   c[8][1] = y+2;  // BottomRight
        String[] p = new String[9];
        p[0] = side + "TopLeft";        p[1] = side + "Top";        p[2] = side + "TopRight";
        p[3] = side + "Left";           p[4] = side + "Middle";     p[5] = side + "Right";
        p[6] = side + "BottomLeft";     p[7] = side + "Bottom";     p[8] = side + "BottomRight";
        for(int i=0; i<9; i++) {
            int xx = c[i][0]; int yy = c[i][1];
            // Put the old visible card in the hidden card and set the new visible card
            player.setTableBoxHiddenCard(player.getTableBoxVisibleCard(xx, yy), xx, yy);
            player.setTableBoxHiddenCardPos(player.getTableBoxVisibleCardPos(xx, yy), xx, yy);
            player.setTableBoxVisibleCard(card, xx, yy); player.setTableBoxVisibleCardPos(p[i], xx, yy);}
    }

    private void updateTableView(Player player, int x, int y) {
        // in way to respect the minimum of (0,0) and the maximum of (CONST.tableWidth - 1, CONST.tableHeight - 1)
        player.setTableViewStartX(
                Math.max(Math.min(x - CONST.tableViewPadding, player.getTableViewStartX()), 0));
        player.setTableViewStartY(
                Math.max(Math.min(y - CONST.tableViewPadding, player.getTableViewStartY()), 0));
        player.setTableViewEndX(
                Math.min(Math.max(x + 2 + CONST.tableViewPadding, player.getTableViewEndX()), CONST.tableWidth - 1));
        player.setTableViewEndY(
                Math.min(Math.max(y + 2 + CONST.tableViewPadding, player.getTableViewEndY()), CONST.tableHeight - 1));
    }

    private void updateTableResourcesPoints(Player player, Card card, int x, int y, String side) {
        String res;
        // Decrease the number of resources that are covered by the new placed card
        res = player.getTableBoxHiddenCardRes(x, y); player.setNumResOnTable(res, player.getNumResOnTable(res) - 1);
        res = player.getTableBoxHiddenCardRes(x+2, y); player.setNumResOnTable(res, player.getNumResOnTable(res) - 1);
        res = player.getTableBoxHiddenCardRes(x, y+2); player.setNumResOnTable(res, player.getNumResOnTable(res) - 1);
        res = player.getTableBoxHiddenCardRes(x+2, y+2); player.setNumResOnTable(res, player.getNumResOnTable(res) - 1);
        // Increase the number of resources that are visibled on the new placed card
        if(side.equals("front")) {
            res = card.getFrontTopLeft(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getFrontTopRight(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getFrontBottomLeft(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getFrontBottomRight(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
        } else {
            res = card.getBackTopLeft(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getBackTopRight(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getBackBottomLeft(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            res = card.getBackBottomRight(); if(!res.equals("blank") && !res.isEmpty()) player.setNumResOnTable(res, player.getNumResOnTable(res) + 1);
            ArrayList<String> resArr = card.getBackMiddle();
            resArr.forEach(r -> {if(!r.equals("blank") && !r.isEmpty()) player.setNumResOnTable(r, player.getNumResOnTable(r) + 1);});
        }
        // Update points (if the placed card is resource or gold)
        if(card.getType().equals("resource")) player.setPoints(player.getPoints() + card.getPoints());
        else if(card.getType().equals("gold")) {
            if(card.getPointsRes().isEmpty()) player.setPoints(player.getPoints() + card.getPoints());
            else if(card.getPointsRes().equals("blank")) { // card.getPoints() * coveredCorners
                int coveredCorners = 0;
                if(player.getTableBoxHiddenCard(x, y) != null) coveredCorners += 1;
                if(player.getTableBoxHiddenCard(x+2, y) != null) coveredCorners += 1;
                if(player.getTableBoxHiddenCard(x, y+2) != null) coveredCorners += 1;
                if(player.getTableBoxHiddenCard(x+2, y+2) != null) coveredCorners += 1;
                player.setPoints(player.getPoints() + (card.getPoints() * coveredCorners));
            } else { // card.getPoints() * numResourcesOnTable (including this card)
                player.setPoints(player.getPoints() + (card.getPoints() * player.getNumResOnTable(card.getPointsRes())));}
        }
    }

    private void updateHandCoordinates(Player player, int x, int y) {
        // 4 Corners of the placed card that are candidated to be of some cards in the hand
        int[][] cc = new int[4][2];
        cc[0] = new int[2]; cc[0][0] = x-2; cc[0][1] = y-2; // topLeft
        cc[1] = new int[2]; cc[1][0] = x+2; cc[1][1] = y-2; // topRight
        cc[2] = new int[2]; cc[2][0] = x-2; cc[2][1] = y+2; // bottomLeft
        cc[3] = new int[2]; cc[3][0] = x+2; cc[3][1] = y+2; // bottomRight
        // Update the list of coordinates to use to place a new card
        for(int i=0; i<4; i++) {
            int xTopLeft = cc[i][0]; int yTopLeft = cc[i][1];
            if(
                    ( // all corners has to be with at least one card already placed
                            player.getTableBoxHiddenCard(xTopLeft, yTopLeft) == null &&
                                    player.getTableBoxHiddenCard(xTopLeft+2, yTopLeft) == null &&
                                    player.getTableBoxHiddenCard(xTopLeft, yTopLeft+2) == null &&
                                    player.getTableBoxHiddenCard(xTopLeft+2, yTopLeft+2) == null
                    ) && ( // if there's a card already placed, it mustn't have a "" corner (for the rules, we can't place an other card on an empty corner)
                            (player.getTableBoxVisibleCard(xTopLeft, yTopLeft) == null || !player.getTableBoxVisibleCardRes(xTopLeft, yTopLeft).isEmpty()) &&
                                    (player.getTableBoxVisibleCard(xTopLeft+2, yTopLeft) == null || !player.getTableBoxVisibleCardRes(xTopLeft+2, yTopLeft).isEmpty()) &&
                                    (player.getTableBoxVisibleCard(xTopLeft, yTopLeft+2) == null || !player.getTableBoxVisibleCardRes(xTopLeft, yTopLeft+2).isEmpty()) &&
                                    (player.getTableBoxVisibleCard(xTopLeft+2, yTopLeft+2) == null || !player.getTableBoxVisibleCardRes(xTopLeft+2, yTopLeft+2).isEmpty())
                    )
                // evaluating the 4 corners around each placed card, we are sure that the candidated coordinates always have at least 1 placed card corner for a new card to place
            ) {
                // Resource cards
                player.addHandCoordinates(xTopLeft, yTopLeft); // Add coordinates
                // Gold cards (check also required resources for all the candidated coordinates)
                player.getHandCoordinates().forEach(hc -> {
                    int xx = hc[0]; int yy = hc[1];
                    player.getGoldHand().forEach(c -> {
                        AtomicInteger resAnimals = new AtomicInteger(player.getNumResOnTable("animals"));
                        AtomicInteger resMushrooms = new AtomicInteger(player.getNumResOnTable("mushrooms"));
                        AtomicInteger resInsects = new AtomicInteger(player.getNumResOnTable("insects"));
                        AtomicInteger resVegetables = new AtomicInteger(player.getNumResOnTable("vegetables"));
                        AtomicInteger resInk = new AtomicInteger(player.getNumResOnTable("ink"));
                        AtomicInteger resParchment = new AtomicInteger(player.getNumResOnTable("parchment"));
                        AtomicInteger resFeather = new AtomicInteger(player.getNumResOnTable("feather"));
                        c.getRequiredRes().forEach(rr -> {
                            switch(rr) {
                                case "animals" -> {/*resAnimals.set(resAnimals.get() - 1);*/ resAnimals.getAndDecrement();}
                                case "mushrooms" -> {/*resMushrooms.set(resMushrooms.get() - 1);*/ resMushrooms.getAndDecrement();}
                                case "insects" -> {/*resInsects.set(resInsects.get() - 1);*/ resInsects.getAndDecrement();}
                                case "vegetables" -> {/*resVegetables.set(resVegetables.get() - 1);*/ resVegetables.getAndDecrement();}
                                case "ink" -> {/*resInk.set(resInk.get() - 1);*/ resInk.getAndDecrement();}
                                case "parchment" -> {/*resParchment.set(resParchment.get() - 1);*/ resParchment.getAndDecrement();}
                                case "feather" -> {/*resFeather.set(resFeather.get() - 1);*/ resFeather.getAndDecrement();}
                            }});
                        // if true -> all required resources are respected
                        if(     resAnimals.get() >= 0 && resMushrooms.get() >= 0 &&
                                resInsects.get() >= 0 && resVegetables.get() >= 0 &&
                                resInk.get() >= 0 && resParchment.get() >= 0 &&
                                resFeather.get() >= 0) {
                            player.addGoldHandCoordinates(c, xx, yy);} // Add coordinates
                        else player.removeGoldHandCoordinates(c, xx, yy); // Remove coordinates that are no more candidated
                    });
                });
            } else { // Remove coordinates that are no more candidated
                /* Resource cards */ player.removeHandCoordinates(xTopLeft, yTopLeft);
                /* Gold cards */ player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, xTopLeft, yTopLeft);});
            }
        }
        // Remove coordinates that are no more candidated
        // Next placed cards won't be able to be positioned in way to hide 2 corner of the same card
        player.removeHandCoordinates(x, y); player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, x, y);});
        player.removeHandCoordinates(x+2, y); player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, x+2, y);});
        player.removeHandCoordinates(x, y+2); player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, x, y+2);});
        player.removeHandCoordinates(x-2, y); player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, x-2, y);});
        player.removeHandCoordinates(x, y-2); player.getGoldHand().forEach(c -> {player.removeGoldHandCoordinates(c, x, y-2);});
    }


}
