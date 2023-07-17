import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    private static String[] tokenize(String text) {
        return text.split("[ \\t\\n\\r]+");
    }

    private static String normalize(String token) {
        return token.toLowerCase();
    }

    private static void addToDictionary(HashMap<String, HashSet<String>> dict, String title, String word) {
        if (!dict.containsKey(word)) {
            HashSet<String> bookList = new HashSet<>();
            bookList.add(title);
            dict.put(word, bookList);
        } else {
            var bookList = dict.get(word);
            bookList.add(title);
            // dictionary.replace(norm, bookList);
        }
    }

    private static HashMap<String, ArrayList<String>> _dictionary;
    private static HashMap<String, String> _books;
    public static void populateBooks(String folderPath){
        _books=FileReader.getDataset(folderPath);
    }

    public static void createDictionary() {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : _books.keySet()) {
            String content = _books.get(title);
            String[] tokens = tokenize(content);

            // HashMap<String, Integer> counts=getTokensCounts(tokens);
            for (String token : tokens) {
                String normalized = normalize(token);
                addToDictionary(dict, title, normalized);
            }

        }
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        for (String key : dict.keySet()) {
            dictionary.put(key, toArrayList(dict.get(key)));
        }
        _dictionary = dictionary;
    }

    private static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }

    public static HashMap<String, ArrayList<String>> getDictionary() {
        return _dictionary;
    }
}
