package ir.shelmos_search.query;

import java.util.ArrayList;
import java.util.HashSet;

public class NotQuery extends Query {

    @Override
    public HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult) {
        priorResult.removeAll(getQueryResult(searchResult));
        return priorResult;
    }
}
