import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndex {
    private final FolderPath folderPath;
    private HashMap<String, String> books;

    public InvertedIndex(FolderPath folderPath) {
        this.folderPath =folderPath;
        populateBooks(folderPath.getDataPath());
//        createDataStructure();
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
        books = new FileReader().getDataset(folderPath);
    }

    private ArrayList<String> filterTokens(String[] tokens) {
        ArrayList<String> filteredTokens = new ArrayList<>();
        ArrayList<String> stopWords = new FileReader().getStopWords(folderPath.getStopwordsPath());
        String stopWordsPattern = "[" + String.join("", stopWords) + "]";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        for (String token : tokens) {
            Matcher matcher = pattern.matcher(token);
            String filtered = matcher.replaceAll("");
            if (!filtered.isBlank()) filteredTokens.add(filtered);
        }
        return filteredTokens;
    }

    public HashMap<String, ArrayList<String>> createDataStructure() {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : books.keySet()) {
            String content = books.get(title);
            String[] tokens = NLP.tokenize(content);
            ArrayList<String> filteredTokens = filterTokens(tokens);
            for (String filteredToken : filteredTokens) {
                String normalized = NLP.normalize(filteredToken);
                addToDictionary(dict, title, normalized);
            }
        }

        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();
        for (String key : dict.keySet())
            dictionary.put(key, Util.toArrayList(dict.get(key)));
        return dictionary;
    }
}
