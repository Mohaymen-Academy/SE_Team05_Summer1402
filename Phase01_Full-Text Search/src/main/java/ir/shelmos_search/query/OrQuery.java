package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class OrQuery extends Query{
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex) {
        HashSet<String> unionPlusResult = getQueryResult(invertedIndex);
        if (priorResult.isEmpty())
            priorResult = unionPlusResult;
        else if (!queries.isEmpty())
            priorResult.retainAll(unionPlusResult);

        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(InvertedIndex invertedIndex) {
        return queries.stream()
                .map(query -> QueryHandler.find(invertedIndex, query))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
