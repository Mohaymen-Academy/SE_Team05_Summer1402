package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;

import java.util.*;

public class QueryHandler {
    public ArrayList<Query> parseQueriesByType(String query, Normalizer normalizer) {
        ArrayList<Query> queries = new ArrayList<>(List.of(new AndQuery(), new OrQuery(), new NotQuery()));

        String[] parts = query.trim().split("\\s+");
        normalizeQueries(parts, queries, normalizer);
        return queries;
    }

    private void normalizeQueries(String[] queries, ArrayList<Query> queryList, Normalizer normalizer) {
        // check if query is blank (query is trimmed, so only first object needs to be checked)
        if (queries[0].isBlank()) return;
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> queryList.get(1).addQuery(normalizer.normalize(query.substring(1)));
                case '-' -> queryList.get(2).addQuery(normalizer.normalize(query.substring(1)));
                default -> queryList.get(0).addQuery(normalizer.normalize(query));
            }
        }
    }

    public List<String> runQueries(ArrayList<Query> queries, InvertedIndex invertedIndex) {
        if (isSingleQuery(queries))
            return runSingleQuery(queries.get(0).queries.get(0), invertedIndex);

        HashSet<String> result = new HashSet<>();
        for (Query query : queries)
            result = query.processQueryResult(result, invertedIndex);
        return List.of(result.toArray(String[]::new));
    }

    static HashSet<String> find(InvertedIndex invertedIndex, String q) {
        Set<String> search = invertedIndex.getDictionary().get(q).keySet();
        return new HashSet<>(search);
    }

    private HashMap<String, Integer> findWithCount(InvertedIndex invertedIndex, String q) {
        Map<String, Integer> search = invertedIndex.getDictionary().get(q);
        return new HashMap<>(search);
    }

    private boolean isSingleQuery(ArrayList<Query> queries) {
        return queries.get(0).queries.size() == 1 &&
                queries.get(1).queries.isEmpty() &&
                queries.get(2).queries.isEmpty();
    }

    private List<String> runSingleQuery(String query, InvertedIndex invertedIndex) {
        var queryResult = findWithCount(invertedIndex, query);
        return queryResult
                .entrySet()
                .stream()
                .sorted((d1, d2) -> d2.getValue().compareTo(d1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }
}
