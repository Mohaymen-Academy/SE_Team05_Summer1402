import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Dictionary {
    private final HashMap<String, HashSet<String>> dictionary;

    public Dictionary() {
        dictionary = new InvertedIndex().create();
    }

    public ArrayList<String> Search(String query) {
        QueryHandler queryHandler = new QueryHandler();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueriesByType(query);
        HashSet<String> result=queryHandler.runQueries(queries, getDictionary());
        return Util.toArrayList(result);
    }

    protected HashMap<String, HashSet<String>> getDictionary() {
        return dictionary;
    }
}
