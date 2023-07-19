import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    public void addDoc(Doc doc, HashMap<String, HashSet<String>> dict) {
        String[] tokenizedWords = tokenizeDocWords(doc);
        ArrayList<String> filteredWords = NLP.filterTokens(tokenizedWords);
        var normalizedWords = normalizeDocWords(filteredWords);
        insertNormalizedWords(normalizedWords, dict, doc.title());
    }

    private String[] tokenizeDocWords(Doc doc) {
        String content = doc.content();
        return NLP.getTokenizer().tokenize(content);
    }

    private ArrayList<String> normalizeDocWords(ArrayList<String> filteredWords) {
        ArrayList<String> normalizedWords = new ArrayList<>();
        for (String filteredToken : filteredWords) {
            String normalized = NLP.getNormalizer().normalize(filteredToken);
            normalizedWords.add(normalized);
        }
        return normalizedWords;
    }

    private void insertNormalizedWords(ArrayList<String> normalizedWords, HashMap<String, HashSet<String>> dict, String title) {
        for (String normalized : normalizedWords) {
            if (!dict.containsKey(normalized)) {
                HashSet<String> bookList = new HashSet<>();
                bookList.add(title);
                dict.put(normalized, bookList);
            } else {
                HashSet<String> bookList = dict.get(normalized);
                bookList.add(title);
            }
        }

    }
}
