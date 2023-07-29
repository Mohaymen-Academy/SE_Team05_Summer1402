package ir.shelmos_search.query;

import java.util.ArrayList;
import ir.shelmos_search.language.InvertedIndex;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class OrQuery extends Query {
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult) {
        HashSet<String> unionPlusResult = getQueryResult(searchResult);
        if (priorResult.isEmpty())
            priorResult = unionPlusResult;
        else if (!getQueries().isEmpty())
            priorResult.retainAll(unionPlusResult);

        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(ArrayList<HashSet<String>> searchResult) {
        return searchResult.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
