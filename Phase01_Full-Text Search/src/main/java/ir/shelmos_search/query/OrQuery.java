package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.HashSet;

public class OrQuery extends Query{
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex) {
        HashSet<String> unionPlusResult = getQueryResult(invertedIndex);
        if (priorResult.isEmpty())
            priorResult = unionPlusResult;
        else if (!getQueries().isEmpty())
            priorResult.retainAll(unionPlusResult);

        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(InvertedIndex invertedIndex) {
        HashSet<String> unionPlusResult = new HashSet<>();
        for (String q : getQueries()) {
            HashSet<String> searchResult = QueryHandler.find(invertedIndex, q);
            unionPlusResult.addAll(searchResult);
        }
        return unionPlusResult;
    }
}
