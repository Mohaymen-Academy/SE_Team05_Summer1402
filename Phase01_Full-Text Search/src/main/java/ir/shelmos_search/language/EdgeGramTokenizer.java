package ir.shelmos_search.language;

import lombok.Builder;

import java.util.ArrayList;
import java.util.HashSet;
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
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < token.length(); i++) {
            for (int j = min; j <= max; j++) {
                if (i + j > token.length()) {
                    break;
                }
                String slug = token.substring(i, i + j);
                result.add(slug);
            }
        }
        return new ArrayList<>(result);
    }
}
