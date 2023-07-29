package ir.shelmos_search.language;

import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;
import java.util.List;

public class OpennlpSimpleTokenizer implements Tokenizer {
    private final SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

    public ArrayList<String> tokenize(String text) {
        return new ArrayList<>(List.of(tokenizer.tokenize(text)));
    }
}
