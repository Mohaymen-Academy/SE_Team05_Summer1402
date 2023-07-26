package ir.shelmos_search.language;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LanguageProcessor {
    @Getter
    @Setter
    private Normalizer normalizer;
    @Getter
    @Setter
    private Tokenizer tokenizer;
    @Getter
    @Setter
    private String[] stopWords;

    public LanguageProcessor() {
        normalizer = new PorterStemmerNormalizer();
        tokenizer = new OpennlpSimpleTokenizer();
        stopWords = new String[]{",", "."};
    }

    public String filterText(String text) {
        String stopWordsPattern = "[" + String.join("", getStopWords()) + "]";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(" ");
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
