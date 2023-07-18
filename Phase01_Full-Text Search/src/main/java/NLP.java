import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLP {
    public static String[] tokenize(String text) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        return tokenizer.tokenize(text);
    }

    public static String normalize(String token) {
        PorterStemmer stemmer = new PorterStemmer();
        token = token.toLowerCase();
        return stemmer.stem(token);
    }

    public static ArrayList<String> filterTokens(String[] tokens) {
        ArrayList<String> filteredTokens = new ArrayList<>();
        ArrayList<String> stopWords = new FileReader().getStopWords(FolderPath.getInstance().getStopwordsPath());
        String stopWordsPattern = "[" + String.join("", stopWords) + "]";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        for (String token : tokens) {
            Matcher matcher = pattern.matcher(token);
            String filtered = matcher.replaceAll("");
            if (!filtered.isBlank()) filteredTokens.add(filtered);
        }
        return filteredTokens;
    }
}
