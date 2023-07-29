package ir.shelmos_search.language;

import lombok.Getter;
import ir.shelmos_search.model.Document;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class InvertedIndex {

    private final LanguageProcessor languageProcessor;
    private final HashMap<String, HashMap<String, Double>> mapWordToDocs;

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
        if (processedWords.isEmpty()) return;

        double incrementFraction = 1d / processedWords.size();

        ArrayList<String> tokenizedTitle = languageProcessor.tokenize(title);
        ArrayList<String> normalizedTitle = languageProcessor.normalize(tokenizedTitle);

        for (String word : processedWords) {
            if (!mapWordToDocs.containsKey(word)) {
                HashMap<String, Double> docList = new HashMap<>();
                docList.put(title, incrementFraction);
                mapWordToDocs.put(word, docList);
            } else {
                HashMap<String, Double> docList = mapWordToDocs.get(word);
                if (docList.containsKey(title)) docList.put(title, docList.get(title) + incrementFraction);
                else docList.put(title, incrementFraction);
            }
            // big score for when document title includes the word
            if (normalizedTitle.contains(word))
                mapWordToDocs.get(word).put(title, 1 + mapWordToDocs.get(word).get(title));
        }
    }
}
