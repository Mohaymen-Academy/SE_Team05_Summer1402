package ir.shelmos_search.query;

import java.util.ArrayList;
import java.util.HashSet;

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
}
