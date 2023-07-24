package ir.ShelmosSearch.Language;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Builder
public class EdgeGramTokenizer implements Tokenizer {
    private int min, max;

    public ArrayList<String> tokenize(String text) {
        var spaceSeprated = text.split("\\s+");
        ArrayList<String> result = new ArrayList<>(List.of(spaceSeprated));
        Stream.of(spaceSeprated)
                .forEach(t -> {
                    result.addAll(makeSlugs(t));
                });
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
