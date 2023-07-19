import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLP {
    private static INormalizer normalizer = new Normalizer();
    private static ITokenizer tokenizer = new Tokenizer();
    private static String[] stopWords = new FileReader().getStopWords(FolderPath.getInstance().getStopwordsPath());

    public static INormalizer getNormalizer() {
        return normalizer;
    }

    public static void setNormalizer(INormalizer normalizer) {
        NLP.normalizer = normalizer;
    }

    public static ITokenizer getTokenizer() {
        return tokenizer;
    }

    public static void setTokenizer(ITokenizer tokenizer) {
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
