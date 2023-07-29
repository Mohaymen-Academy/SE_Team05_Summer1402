package ir.ShelmosSearch.Language;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageProcessor {
    private Normalizer normalizer;
    private Tokenizer tokenizer;
    private String[] stopWords;

    public LanguageProcessor() {
        normalizer = new PorterStemmerNormalizer();
        tokenizer = new opennlpSimpleTokenizer();
        stopWords = new String[]{",", "."};
    }

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

    public String[] getStopWords() {
        return stopWords;
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
