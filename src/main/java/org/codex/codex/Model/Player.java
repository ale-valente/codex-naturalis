package org.codex.codex.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.codex.codex.CONST;

public class Player {
    /*
    Table = 323 x 323
    0,0     ...     322,0
    ...             ...
    0,322   ...     322,322

    Starter card -> 160,160 = CONST.starterCardX,CONST.starterCardY
    160,160 - 161,160 - 162,160
    160,161 - 161,161 - 162,161
    160,162 - 161,162 - 162,162
     */
    private ArrayList<ArrayList<Box>> table;

    private String color;

    //private ResourceCardDeck resourceCardDeck;
    //private GoldCardDeck goldCardDeck;
    private ArrayList<ResourceCard> resourceHand;
    private ArrayList<GoldCard> goldHand;
    private StarterCard starterCard;
    private ObjectiveCard objectiveCard;
    private ArrayList<int[]> handCoordinates;
    private HashMap<GoldCard, ArrayList<int[]>> goldHandCoordinates;
    private int points;
    private HashMap<String, Integer> numResOnTable; // "ink" -> 3, "mushrooms" -> 0 , ...

    private HashMap<String, ArrayList<int[]>> coordResOnTable; // "ink" -> {[123,125], ...} , ... the coordinates are of the CENTER of the card that's always in visibleCard

    private int viewStartX;
    private int viewEndX;
    private int viewStartY;
    private int viewEndY;

    private ArrayList<String> historyPlacedCards; // for GUI porpouse

    public Player() {
        this.color = "";
        this.resourceHand = new ArrayList<>();
        this.goldHand = new ArrayList<>();
        this.handCoordinates = new ArrayList<>();
        this.goldHandCoordinates = new HashMap<>();
        this.points = 0;
        this.numResOnTable = new HashMap<String, Integer>();
        this.numResOnTable.put("animals", 0);
        this.numResOnTable.put("mushrooms", 0);
        this.numResOnTable.put("insects", 0);
        this.numResOnTable.put("vegetables", 0);
        this.numResOnTable.put("ink", 0);
        this.numResOnTable.put("parchment", 0);
        this.numResOnTable.put("feather", 0);
        //resourceCardDeck = new ResourceCardDeck();
        //goldCardDeck = new GoldCardDeck();
        this.viewStartX = Math.max((CONST.starterCardX - CONST.tableViewPadding), 0);
        this.viewStartY = Math.max((CONST.starterCardY - CONST.tableViewPadding), 0);
        this.viewEndX = Math.min((CONST.starterCardX + 2 + CONST.tableViewPadding), CONST.tableWidth - 1);
        this.viewEndY = Math.min((CONST.starterCardY + 2 + CONST.tableViewPadding), CONST.tableHeight - 1);
        table = new ArrayList<ArrayList<Box>>();
        for(int x = 0; x < CONST.tableWidth; x++) {
            ArrayList<Box> b = new ArrayList<Box>();
            for(int y = 0; y < CONST.tableHeight; y++) b.add(new Box());
            table.add(b);
        }
        this.coordResOnTable = new HashMap<>();
        this.coordResOnTable.put("animals", new ArrayList<>());
        this.coordResOnTable.put("mushrooms", new ArrayList<>());
        this.coordResOnTable.put("insects", new ArrayList<>());
        this.coordResOnTable.put("vegetables", new ArrayList<>());
        this.coordResOnTable.put("ink", new ArrayList<>());
        this.coordResOnTable.put("parchment", new ArrayList<>());
        this.coordResOnTable.put("feather", new ArrayList<>());
        historyPlacedCards = new ArrayList<>();
    }

    /*
    standard get-set methods
     */
    public String getColor() {return this.color;}
    public void setColor(String color) {this.color = color;}

    /*
    it returns an arraylist of strings. each string contains the path of each placed card and its coordinates.
     */
    public ArrayList<String> getHistoryPlacedCards() {return this.historyPlacedCards;}
    public void addHistoryPlacedCards(String s) {this.historyPlacedCards.add(s);}

    public ArrayList<ArrayList<Box>> getTable() {return this.table;}

    public int getPoints() {return this.points;}

    /*
    given the string of the resource, it returns its number on the table
     */
    public int getNumResOnTable(String s) {
        if(!s.equals("animals") && !s.equals("mushrooms") && !s.equals("insects") && !s.equals("vegetables") && !s.equals("ink") && !s.equals("parchment") && !s.equals("feather")) return 0;
        return this.numResOnTable.get(s);
    }

    /*
    it returns all the coordinates of all the card on the table that has the kingdom = resource
     */
    public ArrayList<int[]> getCoordResOnTable(String resource) {return this.coordResOnTable.get(resource);}
    public void addCoordResOnTable(String resource, int x, int y) { // ! CENTRAL COORDINATES OF THE CARD AND NOT TOP-LEFT
        if(!resource.equals("animals") && !resource.equals("mushrooms") && !resource.equals("insects") && !resource.equals("vegetables") && !resource.equals("ink") && !resource.equals("parchment") && !resource.equals("feather")) return;
        int[] coo = new int[2]; coo[0] = x; coo[1] = y;
        this.coordResOnTable.get(resource).add(coo);
    }

    /*
    it returns a single box on the table
     */
    public Box getTableBox(int x, int y) {return this.table.get(x).get(y);}
    public Card getTableBoxVisibleCard(int x, int y) {return this.table.get(x).get(y).getVisibleCard();}
    public String getTableBoxVisibleCardPos(int x, int y) {return this.table.get(x).get(y).getVisibleCardPos();}
    public Card getTableBoxHiddenCard(int x, int y) {return this.table.get(x).get(y).getHiddenCard();}
    public String getTableBoxHiddenCardPos(int x, int y) {return this.table.get(x).get(y).getHiddenCardPos();}

    /*
    it returns the hidden card resource in the box in coordinates x, y
     */
    public String getTableBoxHiddenCardRes(int x, int y) { // used just for Card corners
        String pos = this.table.get(x).get(y).getHiddenCardPos();
        Card c = this.table.get(x).get(y).getHiddenCard();
        switch (pos) {
            case "frontTopLeft" -> {return c.getFrontTopLeft();}
            case "frontTopRight" -> {return c.getFrontTopRight();}
            case "frontBottomLeft" -> {return c.getFrontBottomLeft();}
            case "frontBottomRight" -> {return c.getFrontBottomRight();}
            case "backTopLeft" -> {return c.getBackTopLeft();}
            case "backTopRight" -> {return c.getBackTopRight();}
            case "backBottomLeft" -> {return c.getBackBottomLeft();}
            case "backBottomRight" -> {return c.getBackBottomRight();}
            default -> {return "";}
        }
    }

    /*
    it returns the visible card resource in the box in coordinates x, y
     */
    public String getTableBoxVisibleCardRes(int x, int y) { // used just for Card corners
        switch (this.table.get(x).get(y).getVisibleCardPos()) {
            case "frontTopLeft" -> {return this.table.get(x).get(y).getVisibleCard().getFrontTopLeft();}
            case "frontTopRight" -> {return this.table.get(x).get(y).getVisibleCard().getFrontTopRight();}
            case "frontBottomLeft" -> {return this.table.get(x).get(y).getVisibleCard().getFrontBottomLeft();}
            case "frontBottomRight" -> {return this.table.get(x).get(y).getVisibleCard().getFrontBottomRight();}
            case "backTopLeft" -> {return this.table.get(x).get(y).getVisibleCard().getBackTopLeft();}
            case "backTopRight" -> {return this.table.get(x).get(y).getVisibleCard().getBackTopRight();}
            case "backBottomLeft" -> {return this.table.get(x).get(y).getVisibleCard().getBackBottomLeft();}
            case "backBottomRight" -> {return this.table.get(x).get(y).getVisibleCard().getBackBottomRight();}
            default -> {return "";}
        }
    }

    public void setPoints(int p) {this.points = p;}
    public void setNumResOnTable(String s, int n) {
        if(s.equals("animals") || s.equals("mushrooms") || s.equals("insects") || s.equals("vegetables") || s.equals("ink") || s.equals("parchment") || s.equals("feather")) {
            this.numResOnTable.put(s, n);
        }
    }

    public void setTableBoxVisibleCard(Card c, int x, int y) {this.table.get(x).get(y).setVisibleCard(c);}
    public void setTableBoxVisibleCardPos(String pos, int x, int y) {this.table.get(x).get(y).setVisibleCardPos(pos);}
    public void setTableBoxHiddenCard(Card c, int x, int y) {this.table.get(x).get(y).setHiddenCard(c);}
    public void setTableBoxHiddenCardPos(String pos, int x, int y) {this.table.get(x).get(y).setHiddenCardPos(pos);}



    public int getTableViewStartX() {return this.viewStartX;}
    public int getTableViewStartY() {return this.viewStartY;}
    public int getTableViewEndX() {return this.viewEndX;}
    public int getTableViewEndY() {return this.viewEndY;}

    public void setTableViewStartX(int i) {this.viewStartX = i;}
    public void setTableViewStartY(int i) {this.viewStartY = i;}
    public void setTableViewEndX(int i) {this.viewEndX = i;}
    public void setTableViewEndY(int i) {this.viewEndY = i;}


    public StarterCard getStarterCard() {return this.starterCard;}
    public void setStarterCard(StarterCard c) {this.starterCard = c;}

    public ObjectiveCard getObjectiveCard() {return this.objectiveCard;}
    public void setObjectiveCard(ObjectiveCard c) {this.objectiveCard = c;}

    public ArrayList<ResourceCard> getResourceHand() {return this.resourceHand;}
    public ArrayList<GoldCard> getGoldHand() {return this.goldHand;}
    public ResourceCard getResourceHandCard(int i) {return this.resourceHand.get(i);}
    public GoldCard getGoldHandCard(int i) {return this.goldHand.get(i);}

    public void addResourceHandCard(ResourceCard c) {this.resourceHand.add(c);}
    public void addGoldHandCard(GoldCard c) {this.goldHand.add(c); this.goldHandCoordinates.put(c, new ArrayList<>());}
    public void removeResourceHandCard(int i) {this.resourceHand.remove(i);}
    public void removeGoldHandCard(int i) {this.goldHandCoordinates.remove(this.goldHand.get(i)); this.goldHand.remove(i);}

    public ArrayList<int[]> getHandCoordinates() {return this.handCoordinates;}
    public HashMap<GoldCard, ArrayList<int[]>> getGoldHandCoordinates() {return this.goldHandCoordinates;}
    public ArrayList<int[]> getGoldHandCoordinates(int i) {return this.goldHandCoordinates.get(this.goldHand.get(i));}
    public ArrayList<int[]> getGoldHandCoordinates(GoldCard c) {return this.goldHandCoordinates.get(c);}

    public boolean handCoordinatesContains(int x, int y) {
        for (int[] handCoordinate : this.handCoordinates) {if (handCoordinate[0] == x && handCoordinate[1] == y) return true;}
        return false;}
    public boolean goldHandCoordinatesContains(GoldCard c, int x, int y) {
        for(int i = 0; i < this.goldHandCoordinates.get(c).size(); i++) {
            if(this.goldHandCoordinates.get(c).get(i)[0] == x && this.goldHandCoordinates.get(c).get(i)[1] == y) return true;}
        return false;}
    public boolean goldHandCoordinatesContains(int i, int x, int y) {
        for(int ii = 0; ii < this.goldHandCoordinates.get(this.goldHand.get(i)).size(); ii++) {
            if(this.goldHandCoordinates.get(this.goldHand.get(i)).get(ii)[0] == x && this.goldHandCoordinates.get(this.goldHand.get(i)).get(ii)[1] == y) return true;}
        return false;}

    public void addHandCoordinates(int x, int y) { // NO DUPLICATES!
        int[] coordinates = new int[2]; coordinates[0] = x; coordinates[1] = y;
        if(!this.handCoordinatesContains(x, y)) this.handCoordinates.add(coordinates);}
    public void removeHandCoordinates(int x, int y) {
        int[] coordinates = new int[2];
        for (int[] handCoordinate : this.handCoordinates) {
            if (handCoordinate[0] == x && handCoordinate[1] == y) {coordinates = handCoordinate; break;}}
        this.handCoordinates.remove(coordinates);}
    public void addGoldHandCoordinates(GoldCard c, int x, int y) { // NO DUPLICATES!
        int[] coordinates = new int[2]; coordinates[0] = x; coordinates[1] = y;
        if(!this.goldHandCoordinatesContains(c, x, y)) this.goldHandCoordinates.get(c).add(coordinates);}
    public void addGoldHandCoordinates(int i, int x, int y) { // NO DUPLICATES!
        int[] coordinates = new int[2]; coordinates[0] = x; coordinates[1] = y;
        if(!this.goldHandCoordinatesContains(i, x, y)) this.goldHandCoordinates.get(this.goldHand.get(i)).add(coordinates);}
    public void removeGoldHandCoordinates(GoldCard c, int x, int y) {
        int[] coordinates = new int[2]; ArrayList<int[]> a = this.goldHandCoordinates.get(c);
        for (int[] goldHandCoordinate : a) {
            if (goldHandCoordinate[0] == x && goldHandCoordinate[1] == y) {coordinates = goldHandCoordinate; break;}}
        a.remove(coordinates);}
    public void removeGoldHandCoordinates(int i, int x, int y) {
        int[] coordinates = new int[2]; ArrayList<int[]> a = this.goldHandCoordinates.get(this.goldHand.get(i));
        for (int[] goldHandCoordinate : a) {
            if (goldHandCoordinate[0] == x && goldHandCoordinate[1] == y) {coordinates = goldHandCoordinate; break;}}
        a.remove(coordinates);}


    private boolean arrayListContainsCoords(ArrayList<int[]> a, int x, int y) {
        for (int[] coo : a) {if (coo[0] == x && coo[1] == y) return true;}
        return false;}


    /*
    check if the objectives of the card c are respected and returns the gained points
     */
    public int getPointsFromObjectiveCardIfRespected(ObjectiveCard c) {
        ArrayList<String> res = c.getRes();
        ArrayList<HashMap<String, String>> card = c.getCard();
        String card_0_type = card.get(0).get("type");
        //String card_0_pos = card.get(0).get("pos");
        String card_1_type = card.get(1).get("type");
        String card_1_pos = card.get(1).get("pos");
        String card_2_type = card.get(2).get("type");
        String card_2_pos = card.get(2).get("pos");
        AtomicInteger totalPoints = new AtomicInteger(); totalPoints.set(0);
        ArrayList<int[]> coordsAlreadyUsed = new ArrayList<>();
        if(res.isEmpty()) { // evaluate "card" positions
            this.getCoordResOnTable(card_1_type).forEach(cc -> {
                boolean proceed = false;
                int ccX = cc[0];
                int ccY = cc[1];
                if (!this.arrayListContainsCoords(coordsAlreadyUsed, ccX, ccY)) { // a card can be in max 1 combo
                    int ca0x_temp = -1; int ca0y_temp = -1;
                    switch (card_1_pos) {
                        case "angleTopLeft" -> {if (this.getTableBoxVisibleCard(ccX - 2, ccY - 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX - 2; ca0y_temp = ccY - 2;}}
                        case "angleTopRight" -> {if (this.getTableBoxVisibleCard(ccX + 2, ccY - 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX + 2; ca0y_temp = ccY - 2;}}
                        case "angleBottomLeft" -> {if (this.getTableBoxVisibleCard(ccX - 2, ccY + 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX - 2; ca0y_temp = ccY + 2;}}
                        case "angleBottomRight" -> {if (this.getTableBoxVisibleCard(ccX + 2, ccY + 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX + 2; ca0y_temp = ccY + 2;}}
                        case "top" -> {if (this.getTableBoxVisibleCard(ccX, ccY - 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX; ca0y_temp = ccY - 2;}}
                        case "bottom" -> {if (this.getTableBoxVisibleCard(ccX, ccY + 2).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX; ca0y_temp = ccY + 2;}}
                        case "left" -> {if (this.getTableBoxVisibleCard(ccX - 2, ccY).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX - 2; ca0y_temp = ccY;}}
                        case "right" -> {if (this.getTableBoxVisibleCard(ccX + 2, ccY).getBackMiddle().getFirst().equals(card_0_type)) {proceed = true; ca0x_temp = ccX + 2; ca0y_temp = ccY;}}
                    }
                    if (proceed) {
                        switch (card_2_pos) {
                            case "angleTopLeft" -> {
                                if (this.getTableBoxVisibleCard(ccX + 2, ccY + 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX + 2; ca2[1] = ccY + 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "angleTopRight" -> {
                                if (this.getTableBoxVisibleCard(ccX - 2, ccY + 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX - 2; ca2[1] = ccY + 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "angleBottomLeft" -> {
                                if (this.getTableBoxVisibleCard(ccX + 2, ccY - 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX + 2; ca2[1] = ccY - 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "angleBottomRight" -> {
                                if (this.getTableBoxVisibleCard(ccX - 2, ccY - 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX - 2; ca2[1] = ccY - 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "top" -> {
                                if (this.getTableBoxVisibleCard(ccX, ccY + 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX; ca2[1] = ccY + 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "bottom" -> {
                                if (this.getTableBoxVisibleCard(ccX, ccY - 2).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX; ca2[1] = ccY - 2;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "left" -> {
                                if (this.getTableBoxVisibleCard(ccX + 2, ccY).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX + 2; ca2[1] = ccY;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                            case "right" -> {
                                if (this.getTableBoxVisibleCard(ccX - 2, ccY).getBackMiddle().getFirst().equals(card_2_type)) {
                                    totalPoints.addAndGet(c.getPoints());
                                    int[] ca0 = new int[2]; ca0[0] = ca0x_temp; ca0[1] = ca0y_temp;
                                    int[] ca1 = new int[2]; ca1[0] = ccX; ca1[1] = ccY;
                                    int[] ca2 = new int[2]; ca2[0] = ccX - 2; ca2[1] = ccY;
                                    coordsAlreadyUsed.add(ca0); coordsAlreadyUsed.add(ca1); coordsAlreadyUsed.add(ca2);}}
                        }
                    }
                }
            });
        } else { // evaluate resources
            AtomicInteger resAnimals = new AtomicInteger(this.getNumResOnTable("animals"));
            AtomicInteger resMushrooms = new AtomicInteger(this.getNumResOnTable("mushrooms"));
            AtomicInteger resInsects = new AtomicInteger(this.getNumResOnTable("insects"));
            AtomicInteger resVegetables = new AtomicInteger(this.getNumResOnTable("vegetables"));
            AtomicInteger resInk = new AtomicInteger(this.getNumResOnTable("ink"));
            AtomicInteger resParchment = new AtomicInteger(this.getNumResOnTable("parchment"));
            AtomicInteger resFeather = new AtomicInteger(this.getNumResOnTable("feather"));
            boolean _continue_ = true;
            while(_continue_) {
                res.forEach(rr -> {
                    switch(rr) {
                        case "animals" -> {/*resAnimals.set(resAnimals.get() - 1);*/ resAnimals.getAndDecrement();}
                        case "mushrooms" -> {/*resMushrooms.set(resMushrooms.get() - 1);*/ resMushrooms.getAndDecrement();}
                        case "insects" -> {/*resInsects.set(resInsects.get() - 1);*/ resInsects.getAndDecrement();}
                        case "vegetables" -> {/*resVegetables.set(resVegetables.get() - 1);*/ resVegetables.getAndDecrement();}
                        case "ink" -> {/*resInk.set(resInk.get() - 1);*/ resInk.getAndDecrement();}
                        case "parchment" -> {/*resParchment.set(resParchment.get() - 1);*/ resParchment.getAndDecrement();}
                        case "feather" -> {/*resFeather.set(resFeather.get() - 1);*/ resFeather.getAndDecrement();}
                    }});

                if(     resAnimals.get() >= 0 && resMushrooms.get() >= 0 &&
                        resInsects.get() >= 0 && resVegetables.get() >= 0 &&
                        resInk.get() >= 0 && resParchment.get() >= 0 &&
                        resFeather.get() >= 0) {totalPoints.addAndGet(c.getPoints());}
                else _continue_ = false;
            }
        }
        return totalPoints.get();
    }


}
