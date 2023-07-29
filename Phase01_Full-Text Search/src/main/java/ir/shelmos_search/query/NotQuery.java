package ir.shelmos_search.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class NotQuery extends Query {
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult) {
        priorResult.removeAll(getQueryResult(searchResult));
        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(ArrayList<HashSet<String>> searchResult) {
        return searchResult.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
