package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;

import java.util.*;

public class QueryHandler {
    public HashMap<QueryTypes, Query> parseQueriesByType(String query, Normalizer normalizer) {
        HashMap<QueryTypes, Query> queries = new HashMap<>() {{
            put(QueryTypes.AND, new AndQuery());
            put(QueryTypes.OR, new OrQuery());
            put(QueryTypes.NOT, new NotQuery());
        }};

        String[] parts = query.trim().split("\\s+");
        normalizeQueries(parts, queries, normalizer);
        return queries;
    }

    private void normalizeQueries(String[] queries, HashMap<QueryTypes, Query> queryList, Normalizer normalizer) {
        // check if query is blank (query is trimmed, so only first object needs to be checked)
        if (queries[0].isBlank()) return;
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> queryList.get(QueryTypes.OR).addQuery(normalizer.normalize(query.substring(1)));
                case '-' -> queryList.get(QueryTypes.NOT).addQuery(normalizer.normalize(query.substring(1)));
                default -> queryList.get(QueryTypes.AND).addQuery(normalizer.normalize(query));
            }
        }
    }

    public List<String> runQueries(HashMap<QueryTypes, Query> queries, InvertedIndex invertedIndex) {
        if (isSingleQuery(queries))
            return runSingleQuery(queries.get(QueryTypes.AND).getQueries().get(0), invertedIndex);

        HashSet<String> result = new HashSet<>();
        var orderQueries = new QueryTypes[]{QueryTypes.AND, QueryTypes.OR, QueryTypes.NOT};
        for (QueryTypes queryType : orderQueries)
            result = queries.get(queryType).processQueryResult(result, find(invertedIndex, queries.get(queryType).getQueries()));


        return List.of(result.toArray(String[]::new));
    }

    private HashSet<String> find(InvertedIndex invertedIndex, String q) {
        Set<String> search = invertedIndex.getDictionary().get(q).keySet();
        return new HashSet<>(search);
    }

    private ArrayList<HashSet<String>> find(InvertedIndex invertedIndex, ArrayList<String> queries) {
        ArrayList<HashSet<String>> result = new ArrayList<>();
        for (String query : queries) {
            HashSet<String> search = new HashSet<>(invertedIndex.getDictionary().get(query).keySet());
            result.add(search);
        }
        return result;
    }

    private HashMap<String, Double> findWithCount(InvertedIndex invertedIndex, String q) {
        HashMap<String, Double> search = invertedIndex.getDictionary().get(q);
        if (search == null) {
            return new HashMap<>();
        }
        return search;
    }

    private boolean isSingleQuery(HashMap<QueryTypes, Query> queries) {
        return queries.get(QueryTypes.AND).getQueries().size() == 1 &&
                queries.get(QueryTypes.OR).getQueries().isEmpty() &&
                queries.get(QueryTypes.NOT).getQueries().isEmpty();
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
