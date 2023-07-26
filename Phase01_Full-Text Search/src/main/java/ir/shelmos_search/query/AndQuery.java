package ir.shelmos_search.query;

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
            if (firstPart) {
                result = search;
                firstPart = false;
            } else
                result.retainAll(search);
        }

        return result;
    }
}
