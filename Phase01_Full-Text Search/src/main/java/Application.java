import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Application {
    private final InvertedIndex invertedIndex =new InvertedIndex();
    private final QueryHandler queryHandler = new QueryHandler(invertedIndex.getNlp().getNormalizer());

    public ArrayList<String> search(String query) {
        if (query.isBlank()) return new ArrayList<>();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueriesByType(query);
        HashSet<String> result = queryHandler.runQueries(queries, invertedIndex.getDictionary());
        return Util.toArrayList(result);
    }

    public Application addDoc(String title, String content) {
        this.invertedIndex.addDoc(new Doc(title, content));
        return this;
    }

    public Application addDocsByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = new FileReader().getDataset(newDataPathFolder);
        for (String title : docs.keySet())
            this.addDoc(title, docs.get(title));
        return this;
    }

    public Application setTokenizer(Tokenizer newTokenizer) {
        invertedIndex.getNlp().setTokenizer(newTokenizer);
        return this;
    }

    public Application setNormalizer(Normalizer newNormalizer) {
        invertedIndex.getNlp().setNormalizer(newNormalizer);
        // TODO: 7/22/2023  
        queryHandler.setNormalizer(newNormalizer);
        return this;
    }

    public Application setStopWords(String[] newStopWords) {
        invertedIndex.getNlp().setStopWords(newStopWords);
        return this;
    }
    public Application setStopWordsByFile(String stopwordFolder) {
        String[] stopWords = new FileReader().getStopWords(stopwordFolder);
        setStopWords(stopWords);
        return this;
    }
}
