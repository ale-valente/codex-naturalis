package org.codex.codex.View;

import org.codex.codex.CONST;
import org.codex.codex.Model.*;

import java.util.ArrayList;

public class ViewTUI {

    public static String strToEmoji(String s) {
        return switch (s) {
            //case "" -> CONST.TEXT_EMPTY_SPACE;
            case "blank" -> CONST.TEXT_EMOJI_BLANK;
            case "animals" -> CONST.TEXT_EMOJI_ANIMALS;
            case "vegetables" -> CONST.TEXT_EMOJI_VEGETABLES;
            case "mushrooms" -> CONST.TEXT_EMOJI_MUSHROOMS;
            case "insects" -> CONST.TEXT_EMOJI_INSECTS;
            case "feather" -> CONST.TEXT_EMOJI_FEATHER;
            case "ink" -> CONST.TEXT_EMOJI_INK;
            case "parchment" -> CONST.TEXT_EMOJI_PARCHMENT;
            default -> CONST.TEXT_EMPTY_SPACE;
        };
    }



    public static void starterCard(
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            ArrayList<String> middle) {

        System.out.println("Starter card FRONT");
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(ViewTUI.strToEmoji(frontTopLeft));
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(ViewTUI.strToEmoji(frontTopRight));
        System.out.println(CONST.TEXT_RESET);
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.println(CONST.TEXT_RESET);
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(ViewTUI.strToEmoji(frontBottomLeft));
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(ViewTUI.strToEmoji(frontBottomRight));
        System.out.println(CONST.TEXT_RESET);

        System.out.println("Starter card BACK");
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(ViewTUI.strToEmoji(backTopLeft));
        if(middle.size() > 1) System.out.print(ViewTUI.strToEmoji(middle.get(1))); else System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(ViewTUI.strToEmoji(backTopRight));
        System.out.println(CONST.TEXT_RESET);
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        if(middle.size() > 0) System.out.print(ViewTUI.strToEmoji(middle.get(0))); else System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.println(CONST.TEXT_RESET);
        System.out.print(CONST.TEXT_YELLOW_BACKGROUND);
        System.out.print(ViewTUI.strToEmoji(backBottomLeft));
        if(middle.size() > 2) System.out.print(ViewTUI.strToEmoji(middle.get(2))); else System.out.print(CONST.TEXT_EMPTY_SPACE);
        System.out.print(ViewTUI.strToEmoji(backBottomRight));
        System.out.println(CONST.TEXT_RESET);

    }


    public static void objective2Cards(
            int points1, ArrayList<String> res1, String card1_0_type, String card1_0_pos, String card1_1_type, String card1_1_pos, String card1_2_type, String card1_2_pos,
            int points2, ArrayList<String> res2, String card2_0_type, String card2_0_pos, String card2_1_type, String card2_1_pos, String card2_2_type, String card2_2_pos
    ) {
        System.out.println("Objective Card 1");
        System.out.print("Points: " + points1 + " | Resources: ");
        res1.forEach(r -> {System.out.print(r + " ");});
        System.out.println("\n0 - type: " + card1_0_type + " pos: " + card1_0_pos);
        System.out.println("1 - type: " + card1_1_type + " pos: " + card1_1_pos);
        System.out.println("2 - type: " + card1_2_type + " pos: " + card1_2_pos);

        System.out.println("\nObjective Card 2");
        System.out.print("Points: " + points2 + " | Resources: ");
        res2.forEach(r -> {System.out.print(r + " ");});
        System.out.println("\n0 - type: " + card2_0_type + " pos: " + card2_0_pos);
        System.out.println("1 - type: " + card2_1_type + " pos: " + card2_1_pos);
        System.out.println("2 - type: " + card2_2_type + " pos: " + card2_2_pos);
    }

    public static String tableString(Player player) {
        String result = "";
        int startX = player.getTableViewStartX();
        int startY = player.getTableViewStartY();
        int endX = player.getTableViewEndX();
        int endY = player.getTableViewEndY();
        result += CONST.TEXT_CYAN + "(" + startX + "," + startY + ")" + CONST.TEXT_RESET + "\n";
        for(int y = startY; y <= endY; y++) {
            for(int x = startX; x <= endX; x++) {
                Box b = player.getTableBox(x, y);
                Card c = b.getVisibleCard();
                if(c != null) {
                    if(player.handCoordinatesContains(x, y)) result += CONST.TEXT_CYAN_BACKGROUND;
                    else if(c.getType().equals("starter")) result += CONST.TEXT_YELLOW_BACKGROUND;
                    else {
                        String kingdom = c.getBackMiddle().getFirst();
                        switch (kingdom) {
                            case "animals" -> result += CONST.TEXT_BLUE_BACKGROUND;
                            case "vegetables" -> result += CONST.TEXT_GREEN_BACKGROUND;
                            case "mushrooms" -> result += CONST.TEXT_RED_BACKGROUND;
                            case "insects" -> result += CONST.TEXT_PURPLE_BACKGROUND;
                            default -> result += CONST.TEXT_BLACK_BACKGROUND;
                        }
                    }
                    String cardPos = b.getVisibleCardPos();
                    switch (cardPos) {
                        case "frontTopLeft" -> result += ViewTUI.strToEmoji(c.getFrontTopLeft());
                        case "frontTopRight" -> result += ViewTUI.strToEmoji(c.getFrontTopRight());
                        case "frontBottomLeft" -> result += ViewTUI.strToEmoji(c.getFrontBottomLeft());
                        case "frontBottomRight" -> result += ViewTUI.strToEmoji(c.getFrontBottomRight());
                        case "backTopLeft" -> result += ViewTUI.strToEmoji(c.getBackTopLeft());
                        case "backTopRight" -> result += ViewTUI.strToEmoji(c.getBackTopRight());
                        case "backBottomLeft" -> result += ViewTUI.strToEmoji(c.getBackBottomLeft());
                        case "backBottomRight" -> result += ViewTUI.strToEmoji(c.getBackBottomRight());
                        case "backMiddle" -> result += ViewTUI.strToEmoji(c.getBackMiddle().getFirst());
                        case "backTop" -> {
                            if (c.getBackMiddle().size() > 1)
                                result += ViewTUI.strToEmoji(c.getBackMiddle().get(1));
                            else result += CONST.TEXT_EMPTY_SPACE;
                        }
                        case "backBottom" -> {
                            if (c.getBackMiddle().size() > 2)
                                result += ViewTUI.strToEmoji(c.getBackMiddle().get(2));
                            else result += CONST.TEXT_EMPTY_SPACE;
                        }
                        default -> result += CONST.TEXT_EMPTY_SPACE; // case "frontMiddle", "frontTop", "frontBottom", "frontLeft", "frontRight", "backLeft", "backRight"
                    }
                    result += CONST.TEXT_RESET;
                } else {
                    if(player.handCoordinatesContains(x, y)) result += CONST.TEXT_CYAN_BACKGROUND + CONST.TEXT_EMPTY_SPACE_TABLE + CONST.TEXT_RESET;
                    else result += CONST.TEXT_BLACK_BACKGROUND + CONST.TEXT_EMPTY_SPACE_TABLE + CONST.TEXT_RESET;}
            }
            result += "\n";
        }
        for(int x = startX; x <= endX; x++) result += CONST.TEXT_EMPTY_SPACE;
        result += CONST.TEXT_CYAN + "(" + endX + "," + endY + ")" + CONST.TEXT_RESET + "\n\n";
        return result;
    }

    public static void table(Player player) {
        System.out.println(ViewTUI.tableString(player));
    }


    public static String handString(Player player) {

        String result = "";

        ArrayList<ResourceCard> resourceHand = player.getResourceHand();
        ArrayList<GoldCard> goldHand = player.getGoldHand();
        ArrayList<int[]> handCoordinates = player.getHandCoordinates();

        result += "----- [r] Resource Card Hand (FRONT - BACK) [f, b] -----\n";
        for(int i = 0; i < resourceHand.size(); i++) {

            ResourceCard c = resourceHand.get(i);

            String kingdom = c.getBackMiddle().getFirst();
            String frontTopLeft = c.getFrontTopLeft();
            String frontTopRight = c.getFrontTopRight();
            String frontBottomLeft = c.getFrontBottomLeft();
            String frontBottomRight = c.getFrontBottomRight();
            String backTopLeft = c.getBackTopLeft();
            String backTopRight = c.getBackTopRight();
            String backBottomLeft = c.getBackBottomLeft();
            String backBottomRight = c.getBackBottomRight();

            String backgroundColor;
            switch (kingdom) {
                case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
                case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
                case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
                case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
                default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
            }

            result += backgroundColor;
            result += ViewTUI.strToEmoji(frontTopLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(frontTopRight);
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += ViewTUI.strToEmoji(backTopLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(backTopRight);
            result += CONST.TEXT_RESET;
            result += "\n";

            result += backgroundColor;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(kingdom);
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_RESET;
            result += "\n";

            result += backgroundColor;
            result += ViewTUI.strToEmoji(frontBottomLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(frontBottomRight);
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += ViewTUI.strToEmoji(backBottomLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(backBottomRight);
            result += CONST.TEXT_RESET;
            result += "\n";

            result += "Number: [" + Integer.toString(i) + "] - Coordinates: {";
            for(int j = 0; j < handCoordinates.size(); j++) {
                int[] coo = handCoordinates.get(j);
                result += "[" + coo[0] + " " + coo[1] + "]";
                if(j < handCoordinates.size() - 1) result += " ";
            }
            result += "}\n\n";
        }

        result += "----- [r] Gold Card Hand (FRONT - BACK) [f, b] -----\n";

        for(int i = 0; i < goldHand.size(); i++) {

            GoldCard c = goldHand.get(i);

            String kingdom = c.getBackMiddle().getFirst();
            String frontTopLeft = c.getFrontTopLeft();
            String frontTopRight = c.getFrontTopRight();
            String frontBottomLeft = c.getFrontBottomLeft();
            String frontBottomRight = c.getFrontBottomRight();
            String backTopLeft = c.getBackTopLeft();
            String backTopRight = c.getBackTopRight();
            String backBottomLeft = c.getBackBottomLeft();
            String backBottomRight = c.getBackBottomRight();

            String backgroundColor;
            switch (kingdom) {
                case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
                case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
                case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
                case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
                default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
            }

            result += backgroundColor;
            result += ViewTUI.strToEmoji(frontTopLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(frontTopRight);
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += ViewTUI.strToEmoji(backTopLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(backTopRight);
            result += CONST.TEXT_RESET;
            result += "\n";

            result += backgroundColor;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(kingdom);
            result += CONST.TEXT_EMPTY_SPACE;
            result += CONST.TEXT_RESET;
            result += "\n";

            result += backgroundColor;
            result += ViewTUI.strToEmoji(frontBottomLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(frontBottomRight);
            result += CONST.TEXT_RESET;
            result += CONST.TEXT_EMPTY_SPACE;
            result += backgroundColor;
            result += ViewTUI.strToEmoji(backBottomLeft);
            result += CONST.TEXT_EMPTY_SPACE;
            result += ViewTUI.strToEmoji(backBottomRight);
            result += CONST.TEXT_RESET;
            result += "\n";

            result += "Resources you need: ";
            for(int j = 0; j < c.getRequiredRes().size(); j++) {
                result += c.getRequiredRes().get(j) + " ";
            }
            result += "\n";

            result += "Number: [" + Integer.toString(i) + "] - Coordinates: {";
            ArrayList<int[]> goldHandCoordinates = player.getGoldHandCoordinates(c);
            for(int j = 0; j < goldHandCoordinates.size(); j++) {
                int[] coo = goldHandCoordinates.get(j);
                result += "[" + coo[0] + " " + coo[1] + "]";
                if(j < goldHandCoordinates.size() - 1) result += " ";
            }
            result += "}\n\n";
        }
        result += "----------------------------------------------------\n";

        return result;
    }

    public static String publicCardsString(Game game) {
        String result = "";
        ResourceCard r1 = game.getPublicResourceCard1();
        ResourceCard r2 = game.getPublicResourceCard2();
        GoldCard g1 = game.getPublicGoldCard1();
        GoldCard g2 = game.getPublicGoldCard2();

        result += "Resourse Card 1 [r1]\n";
        String kingdom = r1.getBackMiddle().getFirst();
        String frontTopLeft = r1.getFrontTopLeft();
        String frontTopRight = r1.getFrontTopRight();
        String frontBottomLeft = r1.getFrontBottomLeft();
        String frontBottomRight = r1.getFrontBottomRight();
        String backTopLeft = r1.getBackTopLeft();
        String backTopRight = r1.getBackTopRight();
        String backBottomLeft = r1.getBackBottomLeft();
        String backBottomRight = r1.getBackBottomRight();
        String backgroundColor;
        switch (kingdom) {
            case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
            case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
            case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
            case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
            default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
        }
        result += backgroundColor + ViewTUI.strToEmoji(frontTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontTopRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backTopRight) + CONST.TEXT_RESET + "\n";
        result += backgroundColor + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(kingdom) + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + "\n";
        result += backgroundColor + ViewTUI.strToEmoji(frontBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontBottomRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backBottomRight) + CONST.TEXT_RESET + "\n";

        result += "\nResourse Card 2 [r2]\n";
        kingdom = r2.getBackMiddle().getFirst();
        frontTopLeft = r2.getFrontTopLeft();
        frontTopRight = r2.getFrontTopRight();
        frontBottomLeft = r2.getFrontBottomLeft();
        frontBottomRight = r2.getFrontBottomRight();
        backTopLeft = r2.getBackTopLeft();
        backTopRight = r2.getBackTopRight();
        backBottomLeft = r2.getBackBottomLeft();
        backBottomRight = r2.getBackBottomRight();
        switch (kingdom) {
            case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
            case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
            case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
            case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
            default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
        }
        result += backgroundColor + ViewTUI.strToEmoji(frontTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontTopRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backTopRight) + CONST.TEXT_RESET + "\n";
        result += backgroundColor + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(kingdom) + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + "\n";
        result += backgroundColor + ViewTUI.strToEmoji(frontBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontBottomRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backBottomRight) + CONST.TEXT_RESET + "\n";

        result += "\nGold Card 1 [g1]\n";
        kingdom = g1.getBackMiddle().getFirst();
        frontTopLeft = g1.getFrontTopLeft();
        frontTopRight = g1.getFrontTopRight();
        frontBottomLeft = g1.getFrontBottomLeft();
        frontBottomRight = g1.getFrontBottomRight();
        backTopLeft = g1.getBackTopLeft();
        backTopRight = g1.getBackTopRight();
        backBottomLeft = g1.getBackBottomLeft();
        backBottomRight = g1.getBackBottomRight();
        switch (kingdom) {
            case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
            case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
            case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
            case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
            default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
        }
        result += backgroundColor + ViewTUI.strToEmoji(frontTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontTopRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backTopRight) + CONST.TEXT_RESET + "\n";
        result += backgroundColor + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(kingdom) + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + "\n";
        result += backgroundColor + ViewTUI.strToEmoji(frontBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontBottomRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backBottomRight) + CONST.TEXT_RESET + "\n";

        result += "\nGold Card 2 [g2]\n";
        kingdom = g2.getBackMiddle().getFirst();
        frontTopLeft = g2.getFrontTopLeft();
        frontTopRight = g2.getFrontTopRight();
        frontBottomLeft = g2.getFrontBottomLeft();
        frontBottomRight = g2.getFrontBottomRight();
        backTopLeft = g2.getBackTopLeft();
        backTopRight = g2.getBackTopRight();
        backBottomLeft = g2.getBackBottomLeft();
        backBottomRight = g2.getBackBottomRight();
        switch (kingdom) {
            case "animals" -> backgroundColor = CONST.TEXT_BLUE_BACKGROUND;
            case "vegetables" -> backgroundColor = CONST.TEXT_GREEN_BACKGROUND;
            case "mushrooms" -> backgroundColor = CONST.TEXT_RED_BACKGROUND;
            case "insects" -> backgroundColor = CONST.TEXT_PURPLE_BACKGROUND;
            default -> backgroundColor = CONST.TEXT_BLACK_BACKGROUND;
        }
        result += backgroundColor + ViewTUI.strToEmoji(frontTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontTopRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backTopLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backTopRight) + CONST.TEXT_RESET + "\n";
        result += backgroundColor + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(kingdom) + CONST.TEXT_EMPTY_SPACE + CONST.TEXT_RESET + "\n";
        result += backgroundColor + ViewTUI.strToEmoji(frontBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(frontBottomRight) + CONST.TEXT_RESET + CONST.TEXT_EMPTY_SPACE
                + backgroundColor + ViewTUI.strToEmoji(backBottomLeft) + CONST.TEXT_EMPTY_SPACE + ViewTUI.strToEmoji(backBottomRight) + CONST.TEXT_RESET + "\n";

        return result;
    }


    public static String pointsAndResourcesString(Player player) {
        String result = "";
        result += "# TOTAL POINTS: " + player.getPoints() + "\n";
        result += "- ANIMALS: " + player.getNumResOnTable("animals") + "\n";
        result += "- MUSHROOMS: " + player.getNumResOnTable("mushrooms") + "\n";
        result += "- INSECTS: " + player.getNumResOnTable("insects") + "\n";
        result += "- VEGETABLES: " + player.getNumResOnTable("vegetables") + "\n";
        result += "- INK: " + player.getNumResOnTable("ink") + "\n";
        result += "- PARCHMENT: " + player.getNumResOnTable("parchment") + "\n";
        result += "- FEATHER: " + player.getNumResOnTable("feather") + "\n";
        result += "\n";
        return result;
    }

}





















