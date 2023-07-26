package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;
import ir.shelmos_search.language.Normalizer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Builder
public class QueryHandler {
    @Getter
    @Setter
    private Normalizer normalizer;

    public ArrayList<Query> parseQueriesByType(String query) {
        ArrayList<Query> queries = new ArrayList<>(List.of(new AndQuery(), new OrQuery(), new NotQuery()));

        String[] parts = query.trim().split("\\s+");
        normalizeQueries(parts, queries);
        return queries;
    }

    private void normalizeQueries(String[] queries, ArrayList<Query> queryList) {
        // check if query is blank (query is trimmed, so only first object needs to be checked)
        if (queries[0].isBlank()) return;
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> queryList.get(1).addQuery(getNormalizer().normalize(query.substring(1)));
                case '-' -> queryList.get(2).addQuery(getNormalizer().normalize(query.substring(1)));
                default -> queryList.get(0).addQuery(getNormalizer().normalize(query));
            }
        }
    }

    public HashSet<String> runQueries(ArrayList<Query> queries, InvertedIndex invertedIndex) {
        HashSet<String> result = new HashSet<>();
        for (Query query : queries)
            result = query.processQueryResult(result, invertedIndex);

        return result;
    }

    public static HashSet<String> find(InvertedIndex invertedIndex, String q) {
        HashSet<String> search = invertedIndex.getDictionary().get(q);
        if (search == null)
            return new HashSet<>();
        return new HashSet<>(search);
    }
}
