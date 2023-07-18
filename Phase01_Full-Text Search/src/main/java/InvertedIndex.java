import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {

    private HashMap<String, String> books;

    public InvertedIndex() {
        populateBooks();
    }

    private void populateBooks() {
        String folderPath = FolderPath.getInstance().getDataPath();
        books = new FileReader().getDataset(folderPath);
    }

    private void addToDataStructure(HashMap<String, HashSet<String>> dict, String title, String word) {
        if (!dict.containsKey(word)) {
            HashSet<String> bookList = new HashSet<>();
            bookList.add(title);
            dict.put(word, bookList);
        } else {
            HashSet<String> bookList = dict.get(word);
            bookList.add(title);
        }
    }

    public HashMap<String, ArrayList<String>> createDataStructure() {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : getBooks().keySet()) {
            String content = getBooks().get(title);
            String[] tokens = NLP.getTokenizer().tokenize(content);
            ArrayList<String> filteredTokens = NLP.filterTokens(tokens);// TODO: inject stopwords,normalizer
            for (String filteredToken : filteredTokens) {
                String normalized = NLP.getNormalizer().normalize(filteredToken);
                addToDataStructure(dict, title, normalized);
            }
        }
        // TODO: HashSet to ArrayList
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();
        for (String key : dict.keySet())
            dictionary.put(key, Util.toArrayList(dict.get(key)));
        return dictionary;
    }

    protected HashMap<String, String> getBooks() {
        return books;
    }
}
