package ir.ShelmosSearch;

import ir.ShelmosSearch.File.FileReader;
import ir.ShelmosSearch.File.TXTFileReader;
import ir.ShelmosSearch.Language.InvertedIndex;
import ir.ShelmosSearch.Language.Normalizer;
import ir.ShelmosSearch.Language.Tokenizer;
import ir.ShelmosSearch.Models.Document;
import ir.ShelmosSearch.Query.QueryHandler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Application {
    private final InvertedIndex invertedIndex;
    private final QueryHandler queryHandler;
    private final FileReader fileReader;

    public Application() {
        invertedIndex = new InvertedIndex();
        queryHandler = QueryHandler
                .builder()
                .normalizer(invertedIndex.getLanguageProcessor().getNormalizer())
                .build();
        fileReader = new TXTFileReader();
    }

    public ArrayList<String> search(String query) {
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueriesByType(query);
        HashSet<String> result = queryHandler.runQueries(queries, invertedIndex);
        return Util.toArrayList(result);
    }

    public Application addDoc(String title, String content) {
        this.invertedIndex.addDoc(new Document(title, content));
        return this;
    }

    public Application addDocsByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = fileReader.getFiles(newDataPathFolder);
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

    public Application setStopWordsByFile(String stopWordsFolder) {
        String stopWords = fileReader.getFileContent(Paths.get(stopWordsFolder));
        setStopWords(stopWords.split("\\n+"));
        return this;
    }
}
