import opennlp.tools.tokenize.SimpleTokenizer;

public class Tokenizer implements ITokenizer {
    public String[] tokenize(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        return tokenizer.tokenize(text);
    }
}
