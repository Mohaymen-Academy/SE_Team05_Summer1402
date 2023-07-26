package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import ir.shelmos_search.language.PorterStemmerNormalizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

class QueryHandlerTest {
    private QueryHandler queryHandler;
    @Mock
    private InvertedIndex invertedIndex;

    @BeforeEach
    void setUp() {
        queryHandler = QueryHandler.builder().normalizer(new PorterStemmerNormalizer()).build();
        invertedIndex = Mockito.mock(InvertedIndex.class);
        HashMap<String, HashMap<String, Double>> mockDictionary = new HashMap<>();
        mockDictionary.put("ali", new HashMap<String, Double>() {
            {
                put("A", 0.1);
                put("B", 0.2);
                put("C", 0.2);
                put("E", 0.5);
            }
        });
        mockDictionary.put("hassan", new HashMap<String, Double>() {
            {
                put("B", 0.1);
                put("C", 0.3);
                put("E", 0.6);
            }
        });
        mockDictionary.put("hossein", new HashMap<String, Double>() {
            {
                put("C", 0.6);
                put("D", 0.1);
                put("E", 0.3);
            }
        });
        mockDictionary.put("mohammad", new HashMap<String, Double>() {
            {
                put("A", 0.5);
                put("B", 0.1);
                put("C", 0.4);
            }
        });
        mockDictionary.put("abba", new HashMap<String, Double>() {
            {
                put("C", 1.0);
            }
        });
        mockDictionary.put("sadegh", new HashMap<String, Double>() {
            {
                put("A", 0.2);
                put("B", 0.5);
                put("C", 0.3);
            }
        });
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
    void runQueries_singleAND_resultShouldBeSortedByCount() {
        String text = "Ali";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text), invertedIndex).toArray();
        var expected = new String[] { "E", "B", "C", "A" };
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