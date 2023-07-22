import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    public NLP getNlp() {
        return nlp;
    }

    private final NLP nlp=new NLP();

    public HashMap<String, HashSet<String>> getDictionary() {
        return dictionary;
    }

    private final HashMap<String, HashSet<String>> dictionary=new HashMap<>();

    public void addDoc(Doc doc) {
        String[] tokenizedWords = tokenizeDoc(doc);
        ArrayList<String> filteredWords = nlp.filterTokens(tokenizedWords);
        var normalizedWords = normalizeDocWords(filteredWords);
        insertNormalizedWords(normalizedWords, doc.title());
    }

    private String[] tokenizeDoc(Doc doc) {
        String content = doc.content();
        return nlp.getTokenizer().tokenize(content);
    }

    private ArrayList<String> normalizeDocWords(ArrayList<String> filteredWords) {
        ArrayList<String> normalizedWords = new ArrayList<>();
        for (String filteredToken : filteredWords) {
            String normalized = nlp.getNormalizer().normalize(filteredToken);
            normalizedWords.add(normalized);
        }
        return normalizedWords;
    }

    private void insertNormalizedWords(ArrayList<String> normalizedWords, String title) {
        for (String normalized : normalizedWords) {
            if (!dictionary.containsKey(normalized)) {
                HashSet<String> bookList = new HashSet<>();
                bookList.add(title);
                dictionary.put(normalized, bookList);
            } else {
                HashSet<String> bookList = dictionary.get(normalized);
                bookList.add(title);
            }
        }

    }
}
