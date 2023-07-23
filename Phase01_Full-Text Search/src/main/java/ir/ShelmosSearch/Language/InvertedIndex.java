package ir.ShelmosSearch.Language;

import ir.ShelmosSearch.Models.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InvertedIndex {
    private final LanguageProcessor languageProcessor;
    private final HashMap<String, HashSet<String>> dictionary;

    public InvertedIndex() {
        languageProcessor = new LanguageProcessor();
        dictionary = new HashMap<>();
    }

    public void addDoc(Document document) {
        ArrayList<String> tokenizedWords = languageProcessor.tokenize(document.content());
        ArrayList<String> filteredWords = languageProcessor.filterTokens(tokenizedWords);
        ArrayList<String> normalizedWords = languageProcessor.normalize(filteredWords);
        insertProcessedWords(normalizedWords, document.title());
    }

    public LanguageProcessor getLanguageProcessor() {
        return languageProcessor;
    }

    public HashMap<String, HashSet<String>> getDictionary() {
        return dictionary;
    }

    private void insertProcessedWords(ArrayList<String> processedWords, String title) {
        for (String word : processedWords) {
            if (!dictionary.containsKey(word)) {
                HashSet<String> fileList = new HashSet<>();
                fileList.add(title);
                dictionary.put(word, fileList);
            } else {
                HashSet<String> bookList = dictionary.get(word);
                bookList.add(title);
            }
        }

    }
}
