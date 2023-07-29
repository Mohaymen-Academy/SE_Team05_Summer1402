package ir.shelmos_search.query;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
public abstract class Query {

    private final ArrayList<String> queries;

    public Query() {
        this.queries = new ArrayList<>();
    }

    public void addQuery(String query) {
        queries.add(query);
    }

    public abstract HashSet<String> processQueryResult(HashSet<String> priorResult, ArrayList<HashSet<String>> searchResult);

    protected HashSet<String> getQueryResult(ArrayList<HashSet<String>> searchResult) {
        return searchResult.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
