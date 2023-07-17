import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    private static HashMap<String, ArrayList<String>> _dictionary;
    private static HashMap<String, String> _books;

    public InvertedIndex(String folderPath) {
        populateBooks(folderPath);
        createDictionary();
    }

    private String[] tokenize(String text) {
        return text.split("[ \\t\\n\\r]+");
    }

    public static String normalize(String token) {
        return token.toLowerCase();
    }

    private void addToDictionary(HashMap<String, HashSet<String>> dict, String title, String word) {
        if (!dict.containsKey(word)) {
            HashSet<String> bookList = new HashSet<>();
            bookList.add(title);
            dict.put(word, bookList);
        } else {
            HashSet<String> bookList = dict.get(word);
            bookList.add(title);
        }
    }

    private void populateBooks(String folderPath) {
        _books = FileReader.getDataset(folderPath);
    }

    private void createDictionary() {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : _books.keySet()) {
            String content = _books.get(title);
            String[] tokens = tokenize(content);

            for (String token : tokens) {
                String normalized = normalize(token);
                addToDictionary(dict, title, normalized);
            }
        }
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        for (String key : dict.keySet())
            dictionary.put(key, toArrayList(dict.get(key)));
        _dictionary = dictionary;
    }

    private static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }

    public HashMap<String, ArrayList<String>> getDictionary() {
        return _dictionary;
    }
}
