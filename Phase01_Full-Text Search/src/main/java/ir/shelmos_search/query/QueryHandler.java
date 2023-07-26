package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Builder
public class QueryHandler {
    private @Getter
    @Setter
    Normalizer normalizer;

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
        // check if query is blank (query is trimmed, so only first object needs to be checked)
        if (queries[0].isBlank()) return;
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

    public HashSet<String> runQueries(HashMap<String, ArrayList<String>> queries, InvertedIndex invertedIndex) {
        HashSet<String> result = getANDQueries(queries.get("AND"), invertedIndex);

        HashSet<String> unionPlusResult = getORQueries(queries.get("OR"), invertedIndex);
        if (queries.get("AND").isEmpty())
            result = unionPlusResult;
        else if (!queries.get("OR").isEmpty())
            result.retainAll(unionPlusResult);

        result.removeAll(getNOTQueries(queries.get("NOT"), invertedIndex));

        return result;
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
        HashSet<String> search = invertedIndex.getDictionary().get(q);
        if (search == null)
            return new HashSet<>();
        return new HashSet<>(search);
    }
}
