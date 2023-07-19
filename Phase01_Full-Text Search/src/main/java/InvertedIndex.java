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

        HashMap<String, String[]> tokenizedWords = tokenizedBookWords();
        HashMap<String, ArrayList<String>> filteredWords = filteredBookWords(tokenizedWords);
        HashMap<String, HashSet<String>> dictHashSet = normalizedBookWords(filteredWords);
        return Util.convertHashMapSetToHashMapList(dictHashSet);
    }

    private HashMap<String, String[]> tokenizedBookWords(){
        HashMap<String, String[]> words = new HashMap<>();
        String[] tokens;
        for (String title : getBooks().keySet()) {
            String content = getBooks().get(title);
            tokens = NLP.getTokenizer().tokenize(content);
            words.put(title, tokens);
        }
        return words;
    }

    private HashMap<String, ArrayList<String>> filteredBookWords(HashMap<String, String[]> words){
        HashMap<String, ArrayList<String>> filteredWords = new HashMap<>();
        for (String title : getBooks().keySet()) {
            ArrayList<String> filteredTokens = NLP.filterTokens(words.get(title));
            filteredWords.put(title, filteredTokens);
        }
        return filteredWords;
    }

    private HashMap<String, HashSet<String>> normalizedBookWords(HashMap<String, ArrayList<String>> filteredWords){
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : getBooks().keySet()) {
            for (String filteredToken : filteredWords.get(title)) {
                String normalized = NLP.getNormalizer().normalize(filteredToken);
                addToDataStructure(dict, title, normalized);
            }
        }
        return dict;
    }

    protected HashMap<String, String> getBooks() {
        return books;
    }
}
