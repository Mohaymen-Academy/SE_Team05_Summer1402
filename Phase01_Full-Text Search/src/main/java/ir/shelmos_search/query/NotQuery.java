package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.ArrayList;
import java.util.HashSet;

public class NotQuery extends Query {
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult) {
        priorResult.removeAll(getQueryResult(searchResult));
        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(ArrayList<HashSet<String>> searchResult) {
        HashSet<String> result = new HashSet<>();
        for (HashSet<String> search : searchResult) {
            result.addAll(search);
        }
        return result;
    }
}
