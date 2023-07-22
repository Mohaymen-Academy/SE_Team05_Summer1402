import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Application {
    private final InvertedIndex invertedIndex;
    private final QueryHandler queryHandler;
    public Application(){
        invertedIndex=new InvertedIndex();
        queryHandler=new QueryHandler(invertedIndex.getLanguageProcessor().getNormalizer());
    }

    public ArrayList<String> search(String query) {
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueriesByType(query);
        HashSet<String> result = queryHandler.runQueries(queries, invertedIndex.getDictionary());
        return Util.toArrayList(result);
    }

    // TODO: 7/22/2023 remove document?
    public Application addDoc(String title, String content) {
//        this.invertedIndex.addDoc(new Document(title, content));
        this.invertedIndex.addDoc(new Document(title, content));
        return this;
    }

    public Application addDocsByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = new FileReader().getFiles(newDataPathFolder);
        for (String title : docs.keySet())
            this.addDoc(title, docs.get(title));
        return this;
    }

    public Application setTokenizer(Tokenizer newTokenizer) {
        invertedIndex.getLanguageProcessor().setTokenizer(newTokenizer);
        return this;
    }

    public Application setNormalizer(Normalizer newNormalizer) {
        invertedIndex.getLanguageProcessor().setNormalizer(newNormalizer);
        // TODO: 7/22/2023  
        queryHandler.setNormalizer(newNormalizer);
        return this;
    }

    public Application setStopWords(String[] newStopWords) {
        invertedIndex.getLanguageProcessor().setStopWords(newStopWords);
        return this;
    }
    public Application setStopWordsByFile(String stopwordFolder) {
        String[] stopWords = new FileReader().getStopWords(stopwordFolder);
        setStopWords(stopWords);
        return this;
    }
}
