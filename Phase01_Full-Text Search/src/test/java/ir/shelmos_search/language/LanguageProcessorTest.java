package ir.shelmos_search.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LanguageProcessorTest {
    private LanguageProcessor languageProcessor;
    @BeforeEach
    void setUp() {
        languageProcessor=new LanguageProcessor();
    }

    @Test
    void filterText_textContainsDot_resultShouldNotContainDot() {
        String text="Ali said hi to Jimmy.";

        var actual=languageProcessor.filterText(text);

        Assertions.assertTrue("Ali said hi to Jimmy ".equals(actual));

    }
    @Test
    void filterText_textContainsComma_resultShouldNotContainComma() {
        String text="Ali said hi to Jimmy,";

        var actual=languageProcessor.filterText(text);

        Assertions.assertTrue("Ali said hi to Jimmy ".equals(actual));
    }
    @Test
    void normalize_stemmerNormalizer_resultShouldBeStemmedAndLowered() {
        var tokens= new ArrayList<>(List.of(new String[]{"Goals", "like", "AWESOME", "twItTer"}));
        var actual=languageProcessor.normalize(tokens).toArray();
        var expected=new String[]{"goal","like","awesom","twitter"};
        Assertions.assertArrayEquals(expected,actual);
    }
    @Test
    void tokenize_openNlpTokenizer_resultShouldBeTokenized() {
        String text="Ali   \n said hi \t to Jimmy.";
        var actual=languageProcessor.tokenize(text).toArray();
        var expected=new String[]{"Ali", "said", "hi", "to", "Jimmy","."};
        Assertions.assertArrayEquals(expected,actual);
    }
    @Test
    void setStopwords_textContainsI_resultShouldNotContainI() {
        String text="Ali said hi to Jimmy.";
        languageProcessor.setStopWords(new String[]{"i"});
        var actual=languageProcessor.filterText(text);

        Assertions.assertTrue("Al  sa d h  to J mmy.".equals(actual));
    }

    @Test
    void setTokenizer_textContainsWhiteSpace_resultShouldBeSplittedByWhiteSpace() {
        String text="Ali    said hi  to Jimmy.";
        languageProcessor.setTokenizer(t -> new ArrayList<>(List.of(t.split("\\s+"))));
        var actual=languageProcessor.tokenize(text).toArray();
        var expected=new String[]{"Ali", "said", "hi", "to", "Jimmy."};
        Assertions.assertArrayEquals(expected,actual);
    }
    @Test
    void setNormalizer_textContainsComma_resultShouldNotContainComma() {
        var tokens= new ArrayList<>(List.of(new String[]{"Goals", "like", "AWESOME", "twItTer"}));
        languageProcessor.setNormalizer(String::toLowerCase);
        var actual=languageProcessor.normalize(tokens).toArray();
        var expected=new String[]{"goals","like","awesome","twitter"};
        Assertions.assertArrayEquals(expected,actual);
    }
    @Test
    void getNormalizer_defaultNormalizer_resultShouldBeInstanceOfNormalizer() {
        var actual=languageProcessor.getNormalizer();
        Assertions.assertInstanceOf(Normalizer.class,actual);
    }
    @Test
    void getTokenizer_defaultTokenizer_resultShouldBeInstanceOfTokenizer() {
        var actual=languageProcessor.getTokenizer();
        Assertions.assertInstanceOf(Tokenizer.class,actual);
    }
}