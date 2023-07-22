import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLP {
    private Normalizer normalizer = new DefaultNormalizer();
    private Tokenizer tokenizer = new DefaultTokenizer();
    private String[] stopWords;

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void setStopWords(String[] stopWords) {
        this.stopWords = stopWords;
    }

    public ArrayList<String> filterTokens(String[] tokens) {
        ArrayList<String> filteredTokens = new ArrayList<>();
        String stopWordsPattern = "^[" + String.join("", stopWords) + "]$";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        for (String token : tokens) {
            Matcher matcher = pattern.matcher(token);
            String filtered = matcher.replaceAll("");
            if (!filtered.isBlank())
                filteredTokens.add(filtered);
        }
        return filteredTokens;
    }
}
