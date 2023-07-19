import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {

    private HashMap<String, String> files;

    public InvertedIndex() {
        populateBooks();
    }

    private void populateBooks() {
        String folderPath = FolderPath.getInstance().getDataPath();
        files = new FileReader().getDataset(folderPath);
    }

    private void add(HashMap<String, HashSet<String>> dict, String title, String word) {
        if (!dict.containsKey(word)) {
            HashSet<String> bookList = new HashSet<>();
            bookList.add(title);
            dict.put(word, bookList);
        } else {
            HashSet<String> bookList = dict.get(word);
            bookList.add(title);
        }
    }

    public HashMap<String, ArrayList<String>> create() {
        HashMap<String, String[]> tokenizedWords = tokenizeBookWords();
        HashMap<String, ArrayList<String>> filteredWords = filterBookWords(tokenizedWords);
        HashMap<String, HashSet<String>> dictHashSet = normalizeBookWords(filteredWords);
        return Util.convertHashMapSetToHashMapList(dictHashSet);
    }

    private HashMap<String, String[]> tokenizeBookWords(){
        HashMap<String, String[]> words = new HashMap<>();
        String[] tokens;
        for (String title : getFiles().keySet()) {
            String content = getFiles().get(title);
            tokens = NLP.getTokenizer().tokenize(content);
            words.put(title, tokens);
        }
        return words;
    }

    private HashMap<String, ArrayList<String>> filterBookWords(HashMap<String, String[]> words){
        HashMap<String, ArrayList<String>> filteredWords = new HashMap<>();
        for (String title : getFiles().keySet()) {
            ArrayList<String> filteredTokens = NLP.filterTokens(words.get(title));
            filteredWords.put(title, filteredTokens);
        }
        return filteredWords;
    }

    private HashMap<String, HashSet<String>> normalizeBookWords(HashMap<String, ArrayList<String>> filteredWords){
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : getFiles().keySet()) {
            for (String filteredToken : filteredWords.get(title)) {
                String normalized = NLP.getNormalizer().normalize(filteredToken);
                add(dict, title, normalized);
            }
        }
        return dict;
    }

    protected HashMap<String, String> getFiles() {
        return files;
    }
}
