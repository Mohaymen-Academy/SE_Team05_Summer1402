import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private final HashMap<String, ArrayList<String>> dictionary;



    public Dictionary() {
        InvertedIndex _invertedIndex = new InvertedIndex();
        dictionary = _invertedIndex.createDataStructure();
    }
    public ArrayList<String> Search(String query) {
        QueryHandler queryHandler = new QueryHandler();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueries(query);
        return queryHandler.runQueries(queries, getDictionary());
    }
    protected HashMap<String, ArrayList<String>> getDictionary() {
        return dictionary;
    }
}
