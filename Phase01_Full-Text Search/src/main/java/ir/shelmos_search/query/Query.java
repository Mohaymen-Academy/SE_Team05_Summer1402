package ir.shelmos_search.query;

import ir.shelmos_search.language.InvertedIndex;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Query {
    ArrayList<String> queries;

    public Query() {
        this.queries = new ArrayList<>();
    }

    public void addQuery(String query){
        queries.add(query);
    }

    public abstract HashSet<String> processQueryResult(HashSet<String> priorResult, InvertedIndex invertedIndex);

    protected abstract HashSet<String> getQueryResult(InvertedIndex invertedIndex);
}
