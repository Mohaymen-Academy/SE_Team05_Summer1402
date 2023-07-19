import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLP {
    private static Normalizer normalizer = new DefaultNormalizer();
    private static Tokenizer tokenizer = new DefaultTokenizer();
    private static String[] stopWords = new FileReader().getStopWords(FolderPath.getInstance().getStopWordsPath());

    public static Normalizer getNormalizer() {
        return normalizer;
    }

    public static void setNormalizer(Normalizer normalizer) {
        NLP.normalizer = normalizer;
    }

    public static Tokenizer getTokenizer() {
        return tokenizer;
    }

    public static void setTokenizer(Tokenizer tokenizer) {
        NLP.tokenizer = tokenizer;
    }

    public static void setStopWords(String[] stopWords) {
        NLP.stopWords = stopWords;
    }

    public static ArrayList<String> filterTokens(String[] tokens) {
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
