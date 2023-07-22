import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    private final LanguageProcessor languageProcessor;
    private final HashMap<String, HashSet<String>> dictionary;

    public InvertedIndex() {
        languageProcessor = new LanguageProcessor();
        dictionary = new HashMap<>();
    }

    public void addDoc(Document document) {
        String[] tokenizedWords = languageProcessor.tokenize(document.content());
        ArrayList<String> filteredWords = languageProcessor.filterTokens(tokenizedWords);
        ArrayList<String> normalizedWords = languageProcessor.normalize(filteredWords);
        insertWords(normalizedWords, document.title());
    }

    public LanguageProcessor getLanguageProcessor() {
        return languageProcessor;
    }

    public HashMap<String, HashSet<String>> getDictionary() {
        return dictionary;
    }

    private void insertWords(ArrayList<String> words, String title) {
        for (String word : words) {
            if (!dictionary.containsKey(word)) {
                HashSet<String> bookList = new HashSet<>();
                bookList.add(title);
                dictionary.put(word, bookList);
            } else {
                HashSet<String> bookList = dictionary.get(word);
                bookList.add(title);
            }
        }

    }
}
