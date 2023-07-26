package ir.shelmos_search.language;

import ir.shelmos_search.model.Document;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedIndex {
    private @Getter final LanguageProcessor languageProcessor;
    private @Getter final HashMap<String, HashMap<String, Integer>> dictionary;

    public InvertedIndex() {
        languageProcessor = new LanguageProcessor();
        dictionary = new HashMap<>();
    }

    public void addDoc(Document document) {
        String filteredContent = languageProcessor.filterText(document.content());
        ArrayList<String> tokenizedWords = languageProcessor.tokenize(filteredContent);
        ArrayList<String> normalizedWords = languageProcessor.normalize(tokenizedWords);
        insertProcessedWords(normalizedWords, document.title());
    }

    private void insertProcessedWords(ArrayList<String> processedWords, String title) {
        for (String word : processedWords) {
            if (!dictionary.containsKey(word)) {
                HashMap<String, Integer> docList = new HashMap<>();
                docList.put(title, 1);
                dictionary.put(word, docList);
            } else {
                HashMap<String, Integer> docList = dictionary.get(word);
                if (docList.containsKey(title)){
                    docList.put(title, docList.get(title) + 1);
                }
                else {
                    docList.put(title, 1);
                }
            }
        }

    }
}
