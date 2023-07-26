package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.HashSet;

public class AndQuery extends Query{
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex) {
        priorResult = getQueryResult(invertedIndex);
        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(InvertedIndex invertedIndex) {
        boolean firstPart = true;
        HashSet<String> result = new HashSet<>();
        HashSet<String> searchResult;
        for (String q : getQueries()) {
            searchResult = QueryHandler.find(invertedIndex, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else
                result.retainAll(searchResult);
        }

        return result;
    }
}
