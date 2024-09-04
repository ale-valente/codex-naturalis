package org.codex.codex.Model;

import java.util.ArrayList;

public class StarterCard extends Card {
    public StarterCard(
            String frontTopLeft, String frontTopRight, String frontBottomLeft, String frontBottomRight,
            String backTopLeft, String backTopRight, String backBottomLeft, String backBottomRight,
            ArrayList<String> backMiddle,
            String asset
    ) {
        super(
                "starter",
                frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                backMiddle,
                asset
        );
    }
}
