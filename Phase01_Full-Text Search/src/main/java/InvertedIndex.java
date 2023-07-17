import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndex {
    private static HashMap<String, ArrayList<String>> _dictionary;
    private static HashMap<String, String> _books;

    public InvertedIndex(String folderPath) {
        populateBooks(folderPath);
        createDictionary();
    }

    private String[] tokenize(String text) {
        SimpleTokenizer tokenizer=SimpleTokenizer.INSTANCE;
        return tokenizer.tokenize(text);
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
    private ArrayList<String> filterTokens(String[] tokens){
        ArrayList<String> filteredTokens=new ArrayList<>();
        ArrayList<String> stopWords=FileReader.getStopWords("./src/main/resources/stopwords.txt");
        String stopWordsPattern = "[" + String.join("", stopWords) + "]";
        Pattern pattern = Pattern.compile(stopWordsPattern, Pattern.CASE_INSENSITIVE);
        for (String token : tokens) {
            Matcher matcher = pattern.matcher(token);
            String filtered = matcher.replaceAll("");
            if (!filtered.isBlank())
                filteredTokens.add(filtered);
        }
        return filteredTokens;
    }

    private void createDictionary() {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : _books.keySet()) {
            String content = _books.get(title);
            String[] tokens = tokenize(content);
            ArrayList<String> filteredTokens=filterTokens(tokens);
            for (String filteredToken : filteredTokens) {
                String normalized = normalize(filteredToken);
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
