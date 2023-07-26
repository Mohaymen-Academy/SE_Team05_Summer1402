package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Builder
public class QueryHandler {
    private @Getter @Setter Normalizer normalizer;

    public HashMap<String, ArrayList<String>> parseQueriesByType(String query) {
        HashMap<String, ArrayList<String>> queries = new HashMap<>() {
            {
                put("AND", new ArrayList<>());
                put("OR", new ArrayList<>());
                put("NOT", new ArrayList<>());
            }
        };

        String[] parts = query.trim().split("\\s+");
        normalizeQueries(parts, queries);
        return queries;
    }

    private void normalizeQueries(String[] queries, HashMap<String, ArrayList<String>> queryList) {
        // check if query is blank (query is trimmed, so only first object needs to be
        // checked)
        if (queries[0].isBlank())
            return;
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> {
                    ArrayList<String> or = queryList.get("OR");
                    or.add(getNormalizer().normalize(query.substring(1)));
                }
                case '-' -> {
                    ArrayList<String> not = queryList.get("NOT");
                    not.add(getNormalizer().normalize(query.substring(1)));
                }
                default -> {
                    ArrayList<String> and = queryList.get("AND");
                    and.add(getNormalizer().normalize(query));
                }
            }
        }
    }

    public List<String> runQueries(HashMap<String, ArrayList<String>> queries, InvertedIndex invertedIndex) {
        if (isSingleQuery(queries))
            return runSignleQuery(queries.get("AND").get(0), invertedIndex);
        HashSet<String> result = getANDQueries(queries.get("AND"), invertedIndex);

        HashSet<String> unionPlusResult = getORQueries(queries.get("OR"), invertedIndex);
        if (queries.get("AND").isEmpty())
            result = unionPlusResult;
        else if (!queries.get("OR").isEmpty())
            result.retainAll(unionPlusResult);

        result.removeAll(getNOTQueries(queries.get("NOT"), invertedIndex));

        return List.of(result.toArray(String[]::new));
    }

    private HashSet<String> getANDQueries(ArrayList<String> queries, InvertedIndex invertedIndex) {
        boolean firstPart = true;
        HashSet<String> result = new HashSet<>();
        HashSet<String> searchResult;
        for (String q : queries) {
            searchResult = find(invertedIndex, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else
                result.retainAll(searchResult);
        }
        return result;
    }

    private HashSet<String> getNOTQueries(ArrayList<String> queries, InvertedIndex invertedIndex) {
        HashSet<String> searchResult = new HashSet<>();
        for (String q : queries)
            searchResult.addAll(find(invertedIndex, q));
        return searchResult;
    }

    private HashSet<String> getORQueries(ArrayList<String> queries, InvertedIndex invertedIndex) {
        HashSet<String> unionPlusResult = new HashSet<>();
        for (String q : queries) {
            HashSet<String> searchResult = find(invertedIndex, q);
            unionPlusResult.addAll(searchResult);
        }
        return unionPlusResult;
    }

    private HashSet<String> find(InvertedIndex invertedIndex, String q) {
        Set<String> search = invertedIndex.getDictionary().get(q).keySet();
        return new HashSet<>(search);
    }

    private HashMap<String, Double> findWithCount(InvertedIndex invertedIndex, String q) {
        HashMap<String, Double> search = invertedIndex.getDictionary().get(q);
        if (search == null) {
            return new HashMap<>();
        }
        return search;
    }

    private boolean isSingleQuery(HashMap<String, ArrayList<String>> queries) {
        return queries.get("AND").size() == 1 && queries.get("OR").isEmpty() && queries.get("NOT").isEmpty();
    }

    private List<String> runSignleQuery(String query, InvertedIndex invertedIndex) {
        var queryResult = findWithCount(invertedIndex, query);
        return queryResult
                .entrySet()
                .stream()
                .sorted((d1, d2) -> d2.getValue().compareTo(d1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }
}
