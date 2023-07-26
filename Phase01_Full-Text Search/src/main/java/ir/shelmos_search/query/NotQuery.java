package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.HashSet;

public class NotQuery extends Query{

    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex) {
        priorResult.removeAll(getQueryResult(invertedIndex));
        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(InvertedIndex invertedIndex) {
        HashSet<String> searchResult = new HashSet<>();
        for (String q : getQueries())
            searchResult.addAll(QueryHandler.find(invertedIndex, q));
        return searchResult;
    }
}
