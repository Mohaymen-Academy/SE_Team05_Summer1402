package ir.shelmos_search.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PorterStemmerNormalizerTest {

    private PorterStemmerNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new PorterStemmerNormalizer();
    }

    @Test
    void normalize_Plural_shouldReturnSingular() {
        String text = "goals";
        var actual = normalizer.normalize(text);

        Assertions.assertEquals("goal", actual);
    }

    @Test
    void normalize_upperCase_shouldReturnLowerCase() {
        String text = "Goal";
        var actual = normalizer.normalize(text);

        Assertions.assertEquals("goal", actual);
    }

    @Test
    void normalize_thirdPersonSingularVerb_shouldReturnRootOfVerb() {
        String text = "takes";
        var actual = normalizer.normalize(text);

        Assertions.assertEquals("take", actual);
    }

    @Test
    void normalize_rootOfVerb_shouldReturnRootOfVerb() {
        String text = "go";
        var actual = normalizer.normalize(text);

        Assertions.assertEquals("go", actual);
    }
}