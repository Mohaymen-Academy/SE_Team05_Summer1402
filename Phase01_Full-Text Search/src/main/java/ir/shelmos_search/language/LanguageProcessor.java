package ir.shelmos_search.language;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class LanguageProcessor {
    private Normalizer normalizer;
    private Tokenizer tokenizer;
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
        return (ArrayList<String>) words.stream().map(word -> normalizer.normalize(word)).toList();
    }
}
