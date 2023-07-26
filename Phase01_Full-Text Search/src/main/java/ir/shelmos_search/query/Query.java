package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Query {
    private final ArrayList<String> queries;

    public Query() {
        this.queries = new ArrayList<>();
    }

    public ArrayList<String> getQueries() {
        return queries;
    }

    public void addQuery(String query){
        queries.add(query);
    }

    public abstract HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex);

    protected abstract HashSet<String> getQueryResult(InvertedIndex invertedIndex);
}
