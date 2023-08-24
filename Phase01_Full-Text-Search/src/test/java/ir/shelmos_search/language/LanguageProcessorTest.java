package language;

import ir.shelmos_search.language.LanguageProcessor;
import ir.shelmos_search.language.Normalizer;
import ir.shelmos_search.language.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class LanguageProcessorTest {

    private LanguageProcessor languageProcessor;

    @BeforeEach
    void setUp() {
        languageProcessor = new LanguageProcessor();
    }

    @Test
    void filterText_textContainsDot_resultShouldNotContainDot() {
        String text = "Ali said hi to Jimmy.";
        String actual = languageProcessor.filterText(text);
        Assertions.assertEquals("Ali said hi to Jimmy ", actual);
    }

    @Test
    void filterText_textContainsComma_resultShouldNotContainComma() {
        String text = "Ali said hi to Jimmy,";
        String actual = languageProcessor.filterText(text);
        Assertions.assertEquals("Ali said hi to Jimmy ", actual);
    }

    @Test
    void normalize_stemmerNormalizer_resultShouldBeStemmedAndLowered() {
        ArrayList<String> tokens = new ArrayList<>(List.of(new String[]{"Goals", "like", "AWESOME", "twItTer"}));
        Object[] actual = languageProcessor.normalize(tokens).toArray();
        String[] expected = new String[]{"goal", "like", "awesom", "twitter"};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void tokenize_openNlpTokenizer_resultShouldBeTokenized() {
        String text = "Ali   \n said hi \t to Jimmy.";
        Object[] actual = languageProcessor.tokenize(text).toArray();
        String[] expected = new String[]{"Ali", "said", "hi", "to", "Jimmy", "."};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void setStopWords_textContainsI_resultShouldNotContainI() {
        String text = "Ali said hi to Jimmy.";
        languageProcessor.setStopWords(new String[]{"i"});
        String actual = languageProcessor.filterText(text);
        Assertions.assertEquals("Al  sa d h  to J mmy.", actual);
    }

    @Test
    void setTokenizer_textContainsWhiteSpace_resultShouldBeSplittedByWhiteSpace() {
        String text = "Ali    said hi  to Jimmy.";
        languageProcessor.setTokenizer(t -> new ArrayList<>(List.of(t.split("\\s+"))));
        Object[] actual = languageProcessor.tokenize(text).toArray();
        String[] expected = new String[]{"Ali", "said", "hi", "to", "Jimmy."};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void setNormalizer_textContainsComma_resultShouldNotContainComma() {
        ArrayList<String> tokens = new ArrayList<>(List.of(new String[]{"Goals", "like", "AWESOME", "twItTer"}));
        languageProcessor.setNormalizer(String::toLowerCase);
        Object[] actual = languageProcessor.normalize(tokens).toArray();
        String[] expected = new String[]{"goals", "like", "awesome", "twitter"};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void getNormalizer_defaultNormalizer_resultShouldBeInstanceOfNormalizer() {
        Normalizer actual = languageProcessor.getNormalizer();
        Assertions.assertInstanceOf(Normalizer.class, actual);
    }

    @Test
    void getTokenizer_defaultTokenizer_resultShouldBeInstanceOfTokenizer() {
        Tokenizer actual = languageProcessor.getTokenizer();
        Assertions.assertInstanceOf(Tokenizer.class, actual);
    }
}
