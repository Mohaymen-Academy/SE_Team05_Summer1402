package language;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.LanguageProcessor;
import ir.shelmos_search.model.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;

class InvertedIndexTest {

    private InvertedIndex invertedIndex;

    @BeforeEach
    void setUp() {
        invertedIndex = new InvertedIndex();
    }

    @Test
    void addDoc_addOneDocument_shouldAppearInDictionary() {
        String title = "Design by MSP";
        String content = "Design is Good!\nIt's all about engineering";
        invertedIndex.addDoc(new Document(title, content));
        boolean assertion = invertedIndex.getMapWordToDocs().get("about").containsKey(title);
        Assertions.assertTrue(assertion);
    }

    @Test
    void addDoc_addTwoDocument_shouldAppearInDictionary() {
        String title1 = "Design by MSP";
        String content1 = "Design is Good!\nIt's all about coding.";
        String title2 = "Clean Code";
        String content2 = "Clean code is code that is easy to read, understand, and maintain. It is code that is well-organized, concise, and follows best practices. Clean code is important because it makes it easier for other developers to work with your code, reduces the likelihood of bugs and errors, and makes it easier to add new features and functionality.";
        invertedIndex.addDoc(new Document(title1, content1));
        invertedIndex.addDoc(new Document(title2, content2));
        HashMap<String, Double> search = invertedIndex.getMapWordToDocs().get("code");
        Assertions.assertTrue(search.keySet().containsAll(List.of(new String[]{title1, title2})));
    }

    @Test
    void getLanguageProcessor_languageProcessorInstance_resultShouldBeInstanceOfLanguageProcessor() {
        LanguageProcessor actual = invertedIndex.getLanguageProcessor();
        Assertions.assertInstanceOf(LanguageProcessor.class, actual);
    }

    @Test
    void getDictionary_languageProcessorInstance_resultShouldBeInstanceOfHashMap() {
        HashMap<String, HashMap<String, Double>> actual = invertedIndex.getMapWordToDocs();
        Assertions.assertInstanceOf(HashMap.class, actual);
    }
}
