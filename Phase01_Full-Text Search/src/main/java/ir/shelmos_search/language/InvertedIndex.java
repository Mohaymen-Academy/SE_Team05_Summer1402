package ir.shelmos_search.language;

import ir.shelmos_search.model.Document;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class InvertedIndex {
    private @Getter final LanguageProcessor languageProcessor;
    private @Getter final HashMap<String, HashMap<String, Double>> dictionary;

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
        if (processedWords.isEmpty())
            return;
        double incrementFraction = 1d / processedWords.size();
        var tokenizedTitle = languageProcessor.tokenize(title);
        var normalizedTitle = languageProcessor.normalize(tokenizedTitle);
        for (String word : processedWords) {
            if (!dictionary.containsKey(word)) {
                HashMap<String, Double> docList = new HashMap<>();
                if (normalizedTitle.contains(word)) {
                    // big score for when document title includes the word
                    docList.put(title, 1 + incrementFraction);
                } else {
                    docList.put(title, incrementFraction);
                }
                dictionary.put(word, docList);
            } else {
                HashMap<String, Double> docList = dictionary.get(word);
                if (docList.containsKey(title)) {
                    docList.put(title, docList.get(title) + incrementFraction);
                } else {
                    docList.put(title, incrementFraction);
                }
                if (normalizedTitle.contains(word)) {
                    // big score for when document title includes the word
                    docList.put(title, 1 + docList.get(title));
                }
            }
        }

    }
}
