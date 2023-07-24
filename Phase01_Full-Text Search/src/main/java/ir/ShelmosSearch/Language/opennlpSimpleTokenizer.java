package ir.ShelmosSearch.Language;

import ir.ShelmosSearch.Util;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;

public class OpennlpSimpleTokenizer implements Tokenizer {
    public ArrayList<String> tokenize(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        return new ArrayList<>(Util.toArrayList(tokenizer.tokenize(text)));
    }
}
