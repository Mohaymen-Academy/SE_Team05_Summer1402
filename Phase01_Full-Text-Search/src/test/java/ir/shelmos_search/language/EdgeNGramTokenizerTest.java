package language;

import ir.shelmos_search.language.EdgeNGramTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class EdgeNGramTokenizerTest {

    private EdgeNGramTokenizer edgeNGramTokenizer;

    @BeforeEach
    void setUp() {
        edgeNGramTokenizer = new EdgeNGramTokenizer(2, 4);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldContainAPartOfRange() {
        String text = "Design";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.contains("sig");
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldContainTheWholdWord() {
        String text = "Design";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.contains("Design");
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldNotContainPartSmallerThanMin() {
        String text = "Design";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.contains("e");
        Assertions.assertFalse(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldNotContainPartBiggerThanMax() {
        String text = "Design";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.contains("esign");
        Assertions.assertFalse(assertion);
    }

    @Test
    void tokenize_parameterSizeLessThanMin_shouldHaveOneItem() {
        String text = "D";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.size() == 1;
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeBetweenMinAndMax_shouldContainAPartInRange() {
        String text = "Des";
        ArrayList<String> actual = edgeNGramTokenizer.tokenize(text);
        boolean assertion = actual.contains("es");
        Assertions.assertTrue(assertion);
    }
}
