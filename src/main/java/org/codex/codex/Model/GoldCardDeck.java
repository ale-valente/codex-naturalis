package org.codex.codex.Model;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.codex.codex.CONST;

public class GoldCardDeck extends Deck<GoldCard> {

    public GoldCardDeck() throws IOException {
        super();
        int i; JSONObject o = new JSONObject(new String(Files.readAllBytes(Paths.get(CONST.goldAssets))));
        for(i = 0; i < CONST.goldCards; i++){
            String i_str = Integer.toString(i);
            JSONObject front = o.getJSONObject(i_str).getJSONObject("front");
            JSONObject back = o.getJSONObject(i_str).getJSONObject("back");
            String kingdom = o.getJSONObject(i_str).getString("kingdom");
            int points = o.getJSONObject(i_str).getInt("points");
            String pointsRes = o.getJSONObject(i_str).getString("pointsRes");
            ArrayList<String> requiredRes = new ArrayList<String>();
            o.getJSONObject(i_str).getJSONArray("requiredRes").forEach(r -> {requiredRes.add(r.toString());});
            this.addCard(/*i, */new GoldCard(
                    front.getString("topLeft"), front.getString("topRight"), front.getString("bottomLeft"), front.getString("bottomRight"),
                    back.getString("topLeft"), back.getString("topRight"), back.getString("bottomLeft"), back.getString("bottomRight"),
                    kingdom, points, pointsRes, requiredRes,
                    i_str
            ));
        }
        this.shuffle(); // Shuffle the deck
    }

}
