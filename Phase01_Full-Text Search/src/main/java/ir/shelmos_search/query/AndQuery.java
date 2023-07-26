package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.ArrayList;
import java.util.HashSet;

public class AndQuery extends Query {
    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult) {
        priorResult = getQueryResult(searchResult);
        return priorResult;
    }

    @Override
    protected HashSet<String> getQueryResult(ArrayList<HashSet<String>> searchResult) {
        boolean firstPart = true;
        HashSet<String> result = new HashSet<>();
        for (HashSet<String> search : searchResult) {
            for (String q : getQueries()) {
                if (firstPart) {
                    result = search;
                    firstPart = false;
                } else
                    result.retainAll(search);
            }
        }

        return result;
    }
}
