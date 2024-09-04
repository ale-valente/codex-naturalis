package org.codex.codex.Model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.codex.codex.CONST;

public class StarterCardDeck extends Deck<StarterCard> {

    public StarterCardDeck() throws IOException {
        super();
        int i; JSONObject o = new JSONObject(new String(Files.readAllBytes(Paths.get(CONST.starterAssets))));
        for(i = 0; i < CONST.starterCards; i++){
            String i_str = Integer.toString(i);
            JSONObject front = o.getJSONObject(i_str).getJSONObject("front");
            JSONObject back = o.getJSONObject(i_str).getJSONObject("back");
            JSONArray middleJSON = back.getJSONArray("middle");
            ArrayList<String> middle = new ArrayList<String>();
            middleJSON.forEach(m -> {middle.add(m.toString());});
            this.addCard(/*i, */new StarterCard(
                    front.getString("topLeft"), front.getString("topRight"), front.getString("bottomLeft"), front.getString("bottomRight"),
                    back.getString("topLeft"), back.getString("topRight"), back.getString("bottomLeft"), back.getString("bottomRight"),
                    middle,
                    i_str
            ));
        }
        this.shuffle(); // Shuffle the deck
    }

}
