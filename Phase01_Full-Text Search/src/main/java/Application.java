import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    private final Dictionary dictionary = new Dictionary();

    public ArrayList<String> search(String query) {
        return dictionary.search(query);
    }

    public Application add(String title, String content) {
        this.dictionary.add(new Doc(title, content));
        return this;
    }

    public Application setTokenizer(Tokenizer newTokenizer) {
        NLP.setTokenizer(newTokenizer);
        return this;
    }

    public Application setNormalizer(Normalizer newNormalizer) {
        NLP.setNormalizer(newNormalizer);
        return this;
    }

    public Application setStopWords(String[] newStopWords) {
        NLP.setStopWords(newStopWords);
        return this;
    }

    public Application addByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = new FileReader().getDataset(newDataPathFolder);
        for (var title : docs.keySet())
            this.add(title, docs.get(title));
        return this;
    }
}
