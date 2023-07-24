package ir.ShelmosSearch.Language;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class EdgeGramTokenizer implements Tokenizer {
    private int min, max;

    public ArrayList<String> tokenize(String text) {
        var spaceSeprated = text.split("\\s+");
        ArrayList<String> result = new ArrayList<>(List.of(spaceSeprated));
        for (String t : spaceSeprated) {
            result.addAll(makeSlugs(t));
        }

        return result;
    }

    private ArrayList<String> makeSlugs(String token) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < token.length(); i++) {
            for (int j = min; j <= max; j++) {
                if (i + j >= token.length()) {
                    break;
                }
                String slug = token.substring(i, i + j + 1);
                result.add(slug);
            }
        }
        return result;
    }
}
