package ir.ShelmosSearch.Language;

import ir.ShelmosSearch.Util;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;

public class OpennlpSimpleTokenizer implements Tokenizer {
    SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

    public ArrayList<String> tokenize(String text) {
        return new ArrayList<>(Util.toArrayList(tokenizer.tokenize(text)));
    }
}
