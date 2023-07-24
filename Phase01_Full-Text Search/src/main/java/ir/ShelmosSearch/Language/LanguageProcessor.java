package ir.ShelmosSearch.Language;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

public class LanguageProcessor {
    private @Getter @Setter Normalizer normalizer;
    private @Getter @Setter Tokenizer tokenizer;
    private @Getter @Setter String[] stopWords;

    public LanguageProcessor() {
        normalizer = new PorterStemmerNormalizer();
        tokenizer = new OpennlpSimpleTokenizer();
        stopWords = new String[] { ",", "." };
    }

    public ArrayList<String> filterTokens(ArrayList<String> tokens) {
        ArrayList<String> filteredTokens = new ArrayList<>();
        String stopWordsPattern = "^[" + String.join("", getStopWords()) + "]$";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        for (String token : tokens) {
            Matcher matcher = pattern.matcher(token);
            String filtered = matcher.replaceAll("");
            if (!filtered.isBlank())
                filteredTokens.add(filtered);
        }
        return filteredTokens;
    }

    public ArrayList<String> tokenize(String content) {
        return getTokenizer().tokenize(content);
    }

    public ArrayList<String> normalize(ArrayList<String> words) {
        ArrayList<String> normalizedWords = new ArrayList<>();
        for (String word : words) {
            String normalized = normalizer.normalize(word);
            normalizedWords.add(normalized);
        }
        return normalizedWords;
    }
}
