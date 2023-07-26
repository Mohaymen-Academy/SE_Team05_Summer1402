package ir.shelmos_search.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpennlpSimpleTokenizerTest {
    private OpennlpSimpleTokenizer opennlpSimpleTokenizer;

    @BeforeEach
    void setUp() {
        opennlpSimpleTokenizer = new OpennlpSimpleTokenizer();
    }

    @Test
    void tokenize_textWithWhiteSpaceAndNewLineAndTab_resultShouldBeTokenized() {
        String text = "Ali   \n said hi \t to Jimmy.";
        Object[] actual = opennlpSimpleTokenizer.tokenize(text).toArray();
        String[] expected = new String[]{"Ali", "said", "hi", "to", "Jimmy", "."};
        Assertions.assertArrayEquals(expected, actual);
    }
}
