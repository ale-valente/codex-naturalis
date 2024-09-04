package org.codex.codex.Model;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.codex.codex.CONST;

public class ObjectiveCardDeck extends Deck<ObjectiveCard> {

    public ObjectiveCardDeck() throws IOException {
        super();
        int i; JSONObject o = new JSONObject(new String(Files.readAllBytes(Paths.get(CONST.objectiveAssets))));
        for(i = 0; i < CONST.objectiveCards; i++){
            String i_str = Integer.toString(i);
            int points = o.getJSONObject(i_str).getInt("points");
            ArrayList<String> res = new ArrayList<String>();
            o.getJSONObject(i_str).getJSONArray("res").forEach(r -> {res.add(r.toString());});
            JSONObject cardJSON = o.getJSONObject(i_str).getJSONObject("card");
            ArrayList<HashMap<String, String>> card = new ArrayList<>();
            HashMap<String, String> c = new HashMap<>();
            c.put("type", cardJSON.getJSONObject("0").getString("type"));
            c.put("pos", cardJSON.getJSONObject("0").getString("pos"));
            card.add(c);
            c = new HashMap<>();
            c.put("type", cardJSON.getJSONObject("1").getString("type"));
            c.put("pos", cardJSON.getJSONObject("1").getString("pos"));
            card.add(c);
            c = new HashMap<>();
            c.put("type", cardJSON.getJSONObject("2").getString("type"));
            c.put("pos", cardJSON.getJSONObject("2").getString("pos"));
            card.add(c);
            this.addCard(/*i, */new ObjectiveCard(points, card, res, i_str));
        }
        this.shuffle(); // Shuffle the deck
    }
}
