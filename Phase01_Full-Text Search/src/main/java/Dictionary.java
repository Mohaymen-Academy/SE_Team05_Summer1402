import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private final HashMap<String, ArrayList<String>> dictionary;

    public Dictionary() {
        dictionary = new InvertedIndex().createDataStructure();
    }

    Dictionary setTokenizer(ITokenizer newTokenizer) {
        NLP.setTokenizer(newTokenizer);
        return this;
    }

    Dictionary setNormalizer(INormalizer newNormalizer) {
        NLP.setNormalizer(newNormalizer);
        return this;
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
