import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Dictionary {
    private final HashMap<String, HashSet<String>> dictionary;
    private final InvertedIndex invertedIndex;

    public Dictionary() {
        dictionary = new HashMap<>();
        invertedIndex = new InvertedIndex();
    }

    public void add(Doc doc) {
        invertedIndex.addDoc(doc, dictionary);
    }

    public ArrayList<String> Search(String query) {
        if (query.isBlank()) return new ArrayList<>();
        QueryHandler queryHandler = new QueryHandler();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueriesByType(query);
        HashSet<String> result = queryHandler.runQueries(queries, dictionary);
        return Util.toArrayList(result);
    }

}
