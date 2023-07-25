package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import ir.shelmos_search.language.PorterStemmerNormalizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

class QueryHandlerTest {
    private QueryHandler queryHandler;
    private InvertedIndex invertedIndex;

    @BeforeEach
    void setUp() {
        queryHandler = QueryHandler.builder().normalizer(new PorterStemmerNormalizer()).build();
        invertedIndex = Mockito.mock(InvertedIndex.class);
        HashMap<String, HashSet<String>> mockDictionary = new HashMap<>();
        mockDictionary.put("ali", new HashSet<>(List.of(new String[] { "A", "B", "C", "E" })));
        mockDictionary.put("hassan", new HashSet<>(List.of(new String[] { "B", "C", "E" })));
        mockDictionary.put("hossein", new HashSet<>(List.of(new String[] { "C", "D", "E" })));
        mockDictionary.put("mohammad", new HashSet<>(List.of(new String[] { "A", "B", "C" })));
        mockDictionary.put("abba", new HashSet<>(List.of(new String[] { "C" })));
        mockDictionary.put("sadegh", new HashSet<>(List.of(new String[] { "A", "B", "C" })));
        Mockito.when(invertedIndex.getDictionary()).thenReturn(mockDictionary);
    }

    @Test
    void parseQueriesByType_justAnd_resultShouldContainsOnlyAndQueriesSeperated() {
        String text = "Ali Hassan Hossein";
        var actual = queryHandler.parseQueriesByType(text);
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get("OR").isEmpty());
        Assertions.assertTrue(actual.get("NOT").isEmpty());
        Assertions.assertArrayEquals(actual.get("AND").toArray(), expected);
    }

    @Test
    void parseQueriesByType_justOR_resultShouldContainsOnlyORQueriesSeperated() {
        String text = "+Ali +Hassan +Hossein";
        var actual = queryHandler.parseQueriesByType(text);
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get("AND").isEmpty());
        Assertions.assertTrue(actual.get("NOT").isEmpty());
        Assertions.assertArrayEquals(actual.get("OR").toArray(), expected);
    }

    @Test
    void parseQueriesByType_justNOT_resultShouldContainsOnlyNOTQueriesSeperated() {
        String text = "-Ali -Hassan -Hossein";
        var actual = queryHandler.parseQueriesByType(text);
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get("AND").isEmpty());
        Assertions.assertTrue(actual.get("OR").isEmpty());
        Assertions.assertArrayEquals(actual.get("NOT").toArray(), expected);
    }

    @Test
    void parseQueriesByType_multipleQueries_resultShouldContainEachType() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        var actual = queryHandler.parseQueriesByType(text);
        var expectedAND = new String[] { "ali", "hassan" };
        var expectedOR = new String[] { "mohammad", "hossein" };
        var expectedNOT = new String[] { "abba", "sadegh" };
        Assertions.assertArrayEquals(actual.get("AND").toArray(), expectedAND);
        Assertions.assertArrayEquals(actual.get("OR").toArray(), expectedOR);
        Assertions.assertArrayEquals(actual.get("NOT").toArray(), expectedNOT);
    }

    @Test
    void runQueries_justAND_resultShouldIntersectEachResult() {
        String text = "Ali Hassan Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text), invertedIndex).toArray();
        var expected = new String[] { "C", "E" };
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_justOR_resultShouldUnionEachResult() {
        String text = "+Ali +Hassan +Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text), invertedIndex).toArray();
        var expected = new String[] { "A", "B", "C", "D", "E" };
        Assertions.assertEquals(expected.length, actual.length);
    }

    @Test
    void runQueries_justNOT_resultShouldBeEmpty() {
        String text = "-Ali -Hassan -Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text), invertedIndex).toArray();
        Assertions.assertTrue(actual.length == 0);
    }

    @Test
    void runQueries_multipleQueries_resultShouldIntersectANDandORandDeleteNOT() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text), invertedIndex).toArray();
        var expected = new String[] { "E" };
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void setNormalizer_textContainsComma_resultShouldNotContainComma() {
        var token = "Goals";
        queryHandler.setNormalizer(String::toLowerCase);
        var actual = queryHandler.getNormalizer().normalize(token);
        var expected = "goals";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getNormalizer_defaultNormalizer_resultShouldBeInstanceOfNormalizer() {
        var actual = queryHandler.getNormalizer();
        Assertions.assertInstanceOf(Normalizer.class, actual);
    }
}