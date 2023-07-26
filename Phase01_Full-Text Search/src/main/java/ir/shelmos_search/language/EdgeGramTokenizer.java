package ir.shelmos_search.language;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class EdgeGramTokenizer implements Tokenizer {
    private int min, max;

    public ArrayList<String> tokenize(String text) {
        String[] spaceSeparated = text.split("\\s+");
        ArrayList<String> result = new ArrayList<>(List.of(spaceSeparated));
        for (String t : spaceSeparated)
            result.addAll(makeChunk(t));

        return result;
    }

    private ArrayList<String> makeChunk(String token) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < token.length(); i++) {
            for (int j = min; j <= max; j++) {
                if (i + j > token.length()) break;
                String chunk = token.substring(i, i + j);
                result.add(chunk);
            }
        }
        return new ArrayList<>(result);
    }
}
