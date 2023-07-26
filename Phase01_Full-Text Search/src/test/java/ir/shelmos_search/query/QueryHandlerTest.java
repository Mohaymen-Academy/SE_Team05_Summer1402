package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.LanguageProcessor;
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
//        queryHandler = QueryHandler.builder().normalizer(new PorterStemmerNormalizer()).build();
//        queryHandler = Mockito.mock(QueryHandler.class);
        queryHandler = new QueryHandler();
        invertedIndex = Mockito.mock(InvertedIndex.class);



//        invertedIndex.getLanguageProcessor().se
        HashMap<String, HashMap<String, Integer>> mockDictionary = new HashMap<>();
        mockDictionary.put("ali", new HashMap<String, Integer>() {
            {
                put("A", 2);
                put("B", 3);
                put("C", 3);
                put("E", 10);
            }
        });
        mockDictionary.put("hassan", new HashMap<String, Integer>() {
            {
                put("B", 1);
                put("C", 3);
                put("E", 10);
            }
        });
        mockDictionary.put("hossein", new HashMap<String, Integer>() {
            {
                put("C", 20);
                put("D", 4);
                put("E", 10);
            }
        });
        mockDictionary.put("mohammad", new HashMap<String, Integer>() {
            {
                put("A", 5);
                put("B", 2);
                put("C", 4);
            }
        });
        mockDictionary.put("abba", new HashMap<String, Integer>() {
            {
                put("C", 30);
            }
        });
        mockDictionary.put("sadegh", new HashMap<String, Integer>() {
            {
                put("A", 2);
                put("B", 20);
                put("C", 3);
            }
        });
        Mockito.when(invertedIndex.getDictionary()).thenReturn(mockDictionary);
        Mockito.when(invertedIndex.getLanguageProcessor()).thenReturn(new LanguageProcessor());
//        System.out.println(invertedIndex.getLanguageProcessor());
//        System.out.println(invertedIndex.getLanguageProcessor().getNormalizer());
//        Mockito.when(invertedIndex.getLanguageProcessor().getNormalizer()).thenReturn(new PorterStemmerNormalizer());
//        Mockito.doReturn(new PorterStemmerNormalizer()).when(invertedIndex.getLanguageProcessor().getNormalizer());
//        System.out.println(invertedIndex.getLanguageProcessor().getNormalizer());
    }

    @Test
    void parseQueriesByType_justAnd_resultShouldContainsOnlyAndQueriesSeperated() {
        String text = "Ali Hassan Hossein";
        var actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get(1).queries.isEmpty());
        Assertions.assertTrue(actual.get(2).queries.isEmpty());
        Assertions.assertArrayEquals(actual.get(0).queries.toArray(), expected);
    }

    @Test
    void parseQueriesByType_justOR_resultShouldContainsOnlyORQueriesSeperated() {
        String text = "+Ali +Hassan +Hossein";
        var actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get(0).queries.isEmpty());
        Assertions.assertTrue(actual.get(2).queries.isEmpty());
        Assertions.assertArrayEquals(actual.get(1).queries.toArray(), expected);
    }

    @Test
    void parseQueriesByType_justNOT_resultShouldContainsOnlyNOTQueriesSeperated() {
        String text = "-Ali -Hassan -Hossein";
        var actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        var expected = new String[] { "ali", "hassan", "hossein" };
        Assertions.assertTrue(actual.get(0).queries.isEmpty());
        Assertions.assertTrue(actual.get(1).queries.isEmpty());
        Assertions.assertArrayEquals(actual.get(2).queries.toArray(), expected);
    }

    @Test
    void parseQueriesByType_multipleQueries_resultShouldContainEachType() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        var actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        var expectedAND = new String[] { "ali", "hassan" };
        var expectedOR = new String[] { "mohammad", "hossein" };
        var expectedNOT = new String[] { "abba", "sadegh" };
        Assertions.assertArrayEquals(actual.get(0).queries.toArray(), expectedAND);
        Assertions.assertArrayEquals(actual.get(1).queries.toArray(), expectedOR);
        Assertions.assertArrayEquals(actual.get(2).queries.toArray(), expectedNOT);
    }

    @Test
    void runQueries_justAND_resultShouldIntersectEachResult() {
        String text = "Ali Hassan Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()), invertedIndex).toArray();
        var expected = new String[] { "C", "E" };
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_justOR_resultShouldUnionEachResult() {
        String text = "+Ali +Hassan +Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()), invertedIndex).toArray();
        var expected = new String[] { "A", "B", "C", "D", "E" };
        Assertions.assertEquals(expected.length, actual.length);
    }

    @Test
    void runQueries_justNOT_resultShouldBeEmpty() {
        String text = "-Ali -Hassan -Hossein";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()), invertedIndex).toArray();
        Assertions.assertTrue(actual.length == 0);
    }

    @Test
    void runQueries_multipleQueries_resultShouldIntersectANDandORandDeleteNOT() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()), invertedIndex).toArray();
        var expected = new String[] { "E" };
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_singleAND_resultShouldBeSortedByCount() {
        String text = "Ali";
        var actual = queryHandler.runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()), invertedIndex).toArray();
        var expected = new String[] { "E", "B", "C", "A" };
        Assertions.assertArrayEquals(expected, actual);
    }

//    @Test
//    void setNormalizer_textContainsComma_resultShouldNotContainComma() {
//        var token = "Goals";
//        queryHandler.setNormalizer(String::toLowerCase);
//        var actual = queryHandler.getNormalizer().normalize(token);
//        var expected = "goals";
//        Assertions.assertEquals(expected, actual);
//    }

    @Test
    void getNormalizer_defaultNormalizer_resultShouldBeInstanceOfNormalizer() {
        var actual = invertedIndex.getLanguageProcessor().getNormalizer();
        Assertions.assertInstanceOf(Normalizer.class, actual);
    }
}