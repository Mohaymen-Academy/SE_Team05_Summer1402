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
