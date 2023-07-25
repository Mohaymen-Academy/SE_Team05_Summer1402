package ir.shelmos_search;

import ir.shelmos_search.file.FileReader;
import ir.shelmos_search.file.TXTFileReader;
import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import ir.shelmos_search.language.Tokenizer;
import ir.shelmos_search.model.Document;
import ir.shelmos_search.query.QueryHandler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ShelmosSearch {
    private final InvertedIndex invertedIndex;
    private final QueryHandler queryHandler;
    private final FileReader fileReader;

    public ShelmosSearch() {
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
        return new ArrayList<>(result);
    }

    public ShelmosSearch addDoc(String title, String content) {
        this.invertedIndex.addDoc(new Document(title, content));
        return this;
    }

    public ShelmosSearch addDocsByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = fileReader.getFiles(newDataPathFolder);
        for (String title : docs.keySet())
            this.addDoc(title, docs.get(title));
        return this;
    }

    public ShelmosSearch setTokenizer(Tokenizer newTokenizer) {
        invertedIndex.getLanguageProcessor().setTokenizer(newTokenizer);
        return this;
    }

    public ShelmosSearch setNormalizer(Normalizer newNormalizer) {
        invertedIndex.getLanguageProcessor().setNormalizer(newNormalizer);
        // TODO: 7/22/2023
        queryHandler.setNormalizer(newNormalizer);
        return this;
    }

    public ShelmosSearch setStopWords(String[] newStopWords) {
        invertedIndex.getLanguageProcessor().setStopWords(newStopWords);
        return this;
    }

    public ShelmosSearch setStopWordsByFile(String stopWordsFolder) {
        String stopWords = fileReader.getFileContent(Paths.get(stopWordsFolder));
        setStopWords(stopWords.split("\\n+"));
        return this;
    }
}
