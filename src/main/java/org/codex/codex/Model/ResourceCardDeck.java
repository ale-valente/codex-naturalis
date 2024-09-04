package org.codex.codex.Model;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.codex.codex.CONST;

public class ResourceCardDeck extends Deck<ResourceCard> {

    public ResourceCardDeck() throws IOException {
        super();
        int i; JSONObject o = new JSONObject(new String(Files.readAllBytes(Paths.get(CONST.resourceAssets))));
        for(i = 0; i < CONST.resourceCards; i++){
            String i_str = Integer.toString(i);
            JSONObject front = o.getJSONObject(i_str).getJSONObject("front");
            JSONObject back = o.getJSONObject(i_str).getJSONObject("back");
            String kingdom = o.getJSONObject(i_str).getString("kingdom");
            int points = o.getJSONObject(i_str).getInt("points");
            this.addCard(/*i, */new ResourceCard(
                    front.getString("topLeft"), front.getString("topRight"), front.getString("bottomLeft"), front.getString("bottomRight"),
                    back.getString("topLeft"), back.getString("topRight"), back.getString("bottomLeft"), back.getString("bottomRight"),
                    kingdom, points,
                    i_str
            ));
        }
        this.shuffle(); // Shuffle the deck
    }

}
