package ir.shelmos_search.language;

import ir.shelmos_search.model.Document;
import lombok.Getter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    @Getter
    private final LanguageProcessor languageProcessor;
    @Getter
    private final HashMap<String, HashSet<String>> mapWordToDocs;

    public InvertedIndex() {
        languageProcessor = new LanguageProcessor();
        mapWordToDocs = new HashMap<>();
    }

    public void addDoc(Document document) {
        String filteredContent = languageProcessor.filterText(document.content());
        ArrayList<String> tokenizedWords = languageProcessor.tokenize(filteredContent);
        ArrayList<String> normalizedWords = languageProcessor.normalize(tokenizedWords);
        insertProcessedWords(normalizedWords, document.title());
    }

    private void insertProcessedWords(ArrayList<String> processedWords, String title) {
        for (String word : processedWords) {
            if (!mapWordToDocs.containsKey(word)) {
                HashSet<String> fileList = new HashSet<>();
                fileList.add(title);
                mapWordToDocs.put(word, fileList);
            } else {
                HashSet<String> bookList = mapWordToDocs.get(word);
                bookList.add(title);
            }
        }

    }
}
