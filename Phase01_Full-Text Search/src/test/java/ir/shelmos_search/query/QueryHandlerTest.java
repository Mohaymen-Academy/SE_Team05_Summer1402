package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.LanguageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

class QueryHandlerTest {
    private QueryHandler queryHandler;
    @Mock
    private InvertedIndex invertedIndex;

    @BeforeEach
    void setUp() {
        queryHandler = new QueryHandler();
        invertedIndex = Mockito.mock(InvertedIndex.class);
        HashMap<String, HashMap<String, Double>> mockDictionary = createFakeData();
        Mockito.when(invertedIndex.getDictionary()).thenReturn(mockDictionary);
        Mockito.when(invertedIndex.getLanguageProcessor()).thenReturn(new LanguageProcessor());
    }

    @Test
    void parseQueriesByType_justAnd_resultShouldContainsOnlyAndQueriesSeperated() {
        String text = "Ali Hassan Hossein";
        HashMap<QueryTypes, Query> actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        String[] expected = new String[]{"ali", "hassan", "hossein"};
        Assertions.assertTrue(actual.get(QueryTypes.OR).getQueries().isEmpty());
        Assertions.assertTrue(actual.get(QueryTypes.NOT).getQueries().isEmpty());
        Assertions.assertArrayEquals(actual.get(QueryTypes.AND).getQueries().toArray(), expected);
    }

    @Test
    void parseQueriesByType_justOR_resultShouldContainsOnlyORQueriesSeperated() {
        String text = "+Ali +Hassan +Hossein";
        HashMap<QueryTypes, Query> actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        String[] expected = new String[]{"ali", "hassan", "hossein"};
        Assertions.assertTrue(actual.get(QueryTypes.AND).getQueries().isEmpty());
        Assertions.assertTrue(actual.get(QueryTypes.NOT).getQueries().isEmpty());
        Assertions.assertArrayEquals(actual.get(QueryTypes.OR).getQueries().toArray(), expected);
    }

    @Test
    void parseQueriesByType_justNOT_resultShouldContainsOnlyNOTQueriesSeperated() {
        String text = "-Ali -Hassan -Hossein";
        HashMap<QueryTypes, Query> actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        String[] expected = new String[]{"ali", "hassan", "hossein"};
        Assertions.assertTrue(actual.get(QueryTypes.AND).getQueries().isEmpty());
        Assertions.assertTrue(actual.get(QueryTypes.OR).getQueries().isEmpty());
        Assertions.assertArrayEquals(actual.get(QueryTypes.NOT).getQueries().toArray(), expected);
    }

    @Test
    void parseQueriesByType_multipleQueries_resultShouldContainEachType() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        HashMap<QueryTypes, Query> actual = queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer());
        String[] expectedAND = new String[]{"ali", "hassan"};
        String[] expectedOR = new String[]{"mohammad", "hossein"};
        String[] expectedNOT = new String[]{"abba", "sadegh"};
        Assertions.assertArrayEquals(actual.get(QueryTypes.AND).getQueries().toArray(), expectedAND);
        Assertions.assertArrayEquals(actual.get(QueryTypes.OR).getQueries().toArray(), expectedOR);
        Assertions.assertArrayEquals(actual.get(QueryTypes.NOT).getQueries().toArray(), expectedNOT);
    }

    @Test
    void runQueries_justAND_resultShouldIntersectEachResult() {
        String text = "Ali Hassan Hossein";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        String[] expected = new String[]{"C", "E"};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_justOR_resultShouldUnionEachResult() {
        String text = "+Ali +Hassan +Hossein";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        String[] expected = new String[]{"A", "B", "C", "D", "E"};
        Assertions.assertEquals(expected.length, actual.length);
    }

    @Test
    void runQueries_justNOT_resultShouldBeEmpty() {
        String text = "-Ali -Hassan -Hossein";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        Assertions.assertEquals(0, actual.length);
    }

    @Test
    void runQueries_multipleQueries_resultShouldIntersectANDandORandDeleteNOT() {
        String text = "+Mohammad -Abbas Ali Hassan +Hossein -Sadegh";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        String[] expected = new String[]{"E"};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_singleAND_resultShouldBeSortedByCount() {
        String text = "Ali";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        String[] expected = new String[]{"E", "B", "C", "A"};
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void runQueries_singleANDWhichIsPartOfTitle_resultShouldBeSortedByCount() {
        String text = "clean";
        Object[] actual = queryHandler
                .runQueries(queryHandler.parseQueriesByType(text, invertedIndex.getLanguageProcessor().getNormalizer()),
                        invertedIndex)
                .toArray();
        String[] expected = new String[]{"Clean Code", "Clean Architecture", "Daisy Diary"};
        Assertions.assertArrayEquals(expected, actual);
    }

    private HashMap<String, HashMap<String, Double>> createFakeData() {

        HashMap<String, HashMap<String, Double>> mockDictionary = new HashMap<>();
        mockDictionary.put("ali", new HashMap<>() {
            {
                put("A", 0.1);
                put("B", 0.2);
                put("C", 0.2);
                put("E", 0.5);
            }
        });
        mockDictionary.put("hassan", new HashMap<>() {
            {
                put("B", 0.1);
                put("C", 0.3);
                put("E", 0.6);
            }
        });
        mockDictionary.put("hossein", new HashMap<>() {
            {
                put("C", 0.6);
                put("D", 0.1);
                put("E", 0.3);
            }
        });
        mockDictionary.put("mohammad", new HashMap<>() {
            {
                put("A", 0.5);
                put("B", 0.1);
                put("C", 0.4);
            }
        });
        mockDictionary.put("abba", new HashMap<>() {
            {
                put("C", 1.0);
            }
        });
        mockDictionary.put("sadegh", new HashMap<>() {
            {
                put("A", 0.2);
                put("B", 0.5);
                put("C", 0.3);
            }
        });
        mockDictionary.put("clean", new HashMap<>() {
            {
                put("Clean Architecture", 0.5);
                put("Clean Code", 1.2);
                put("Daisy Diary", 0.3);
            }
        });
        return mockDictionary;
    }
}
