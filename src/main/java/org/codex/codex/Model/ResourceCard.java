package org.codex.codex.Model;

import java.util.ArrayList;

public class ResourceCard extends Card {
    private final int points; // "points":1

    public ResourceCard(
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            String kingdom, int points,
            String asset
    ) {
        super(
                "resource",
                frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                null,
                asset
        );
        ArrayList<String> backMiddleArray = new ArrayList<String>(); backMiddleArray.addFirst(kingdom); this.setBackMiddle(backMiddleArray);
        this.points = points;
    }

    // kingdom = middle.getFirst() is always defined
    public String getKingdom() {ArrayList<String> middle = this.getBackMiddle(); return middle == null ? null : middle.getFirst();}
    @Override
    public int getPoints() {return this.points;}
}
