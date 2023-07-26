package ir.shelmos_search.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdgeNGramTokenizerTest {
    private EdgeNGramTokenizer edgeNGramTokenizer;

    @BeforeEach
    void setUp() {
        edgeNGramTokenizer = new EdgeNGramTokenizer(2,4);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldContainAPartOfRange() {
        String text = "Design";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.contains("sig");
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldContainTheWholdWord() {
        String text = "Design";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.contains("Design");
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldNotContainPartSmallerThanMin() {
        String text = "Design";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.contains("e");
        Assertions.assertFalse(assertion);
    }

    @Test
    void tokenize_parameterSizeMoreThanMax_shouldNotContainPartBiggerThanMax() {
        String text = "Design";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.contains("esign");
        Assertions.assertFalse(assertion);
    }

    @Test
    void tokenize_parameterSizeLessThanMin_shouldHaveOneItem() {
        String text = "D";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.size() == 1;
        Assertions.assertTrue(assertion);
    }

    @Test
    void tokenize_parameterSizeBetweenMinAndMax_shouldContainAPartInRange() {
        String text = "Des";

        var actual = edgeNGramTokenizer.tokenize(text);

        boolean assertion = actual.contains("es");
        Assertions.assertTrue(assertion);
    }
}