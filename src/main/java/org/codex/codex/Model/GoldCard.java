package org.codex.codex.Model;

import java.util.ArrayList;

public class GoldCard extends Card {
    private final int points; // "points":1
    private final String pointsRes; // "ink"
    private final ArrayList<String> requiredRes; // ["vegetables", "vegetables", "vegetables", "insects"]


    public GoldCard(
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            String kingdom, int points,
            String pointsRes, ArrayList<String> requiredRes,
            String asset
    ) {
        super(
                "gold",
                frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                null,
                asset
        );
        ArrayList<String> backMiddleArray = new ArrayList<String>(); backMiddleArray.addFirst(kingdom); this.setBackMiddle(backMiddleArray);
        this.points = points;
        this.pointsRes = pointsRes;
        this.requiredRes = requiredRes;
    }

    // kingdom = middle.getFirst() is always defined
    public String getKingdom() {ArrayList<String> middle = this.getBackMiddle(); return middle == null ? null : middle.getFirst();}
    @Override
    public int getPoints() {return this.points;}
    @Override
    public String getPointsRes() {return this.pointsRes;}
    public ArrayList<String> getRequiredRes() {return this.requiredRes;}
}
