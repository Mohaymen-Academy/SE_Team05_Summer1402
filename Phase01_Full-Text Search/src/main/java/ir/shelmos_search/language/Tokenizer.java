package ir.shelmos_search.language;

import java.util.ArrayList;

public interface Tokenizer {
    ArrayList<String> tokenize(String text);
}
