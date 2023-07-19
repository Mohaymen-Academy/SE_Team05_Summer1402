import opennlp.tools.tokenize.SimpleTokenizer;

public class DefaultTokenizer implements Tokenizer {
    public String[] tokenize(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        return tokenizer.tokenize(text);
    }
}
