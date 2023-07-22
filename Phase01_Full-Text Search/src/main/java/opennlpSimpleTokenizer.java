import opennlp.tools.tokenize.SimpleTokenizer;

public class opennlpSimpleTokenizer implements Tokenizer {
    public String[] tokenize(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        return tokenizer.tokenize(text);
    }
}
