import java.util.*;

public class QueryHandler {
    private Normalizer normalizer;

    public QueryHandler(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    public HashMap<String, ArrayList<String>> parseQueriesByType(String query) {
        HashMap<String, ArrayList<String>> queries = new HashMap<>() {
            {
                put("AND", new ArrayList<>());
                put("OR", new ArrayList<>());
                put("NOT", new ArrayList<>());
            }
        };

        String[] parts = query.trim().split("\\s+");
        normalizeQueries(parts, queries);
        return queries;
    }

    public void setNormalizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    private void normalizeQueries(String[] queries, HashMap<String, ArrayList<String>> queryList) {
        // check if query is blank (query is trimmed, so only first object needs to be checked)
        if (queries[0].length() == 0) return;
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> {
                    ArrayList<String> or = queryList.get("OR");
                    or.add(normalizer.normalize(query.substring(1)));
                }
                case '-' -> {
                    ArrayList<String> not = queryList.get("NOT");
                    not.add(normalizer.normalize(query.substring(1)));
                }
                default -> {
                    ArrayList<String> and = queryList.get("AND");
                    and.add(normalizer.normalize(query));
                }
            }
        }
    }

    public HashSet<String> runQueries(HashMap<String, ArrayList<String>> queries, InvertedIndex invertedIndex) {

        HashMap<String, HashSet<String>> dictionary = invertedIndex.getDictionary();

        HashSet<String> result = getANDQueries(queries.get("AND"), dictionary);

        HashSet<String> unionPlusResult = getORQueries(queries.get("OR"), dictionary);
        if (queries.get("AND").isEmpty())
            result = unionPlusResult;
        else if (!queries.get("OR").isEmpty())
            result.retainAll(unionPlusResult);

        return getNOTQueries(queries.get("NOT"), dictionary, result);
    }

    private HashSet<String> getNOTQueries(ArrayList<String> queries,
                                          HashMap<String, HashSet<String>> dictionary,
                                          HashSet<String> result) {
        for (String q : queries) {
            HashSet<String> searchResult = find(dictionary, q);
            result = Util.minus(result, searchResult);
        }
        return result;
    }

    private HashSet<String> getORQueries(ArrayList<String> queries,
                                         HashMap<String, HashSet<String>> dictionary) {
        HashSet<String> unionPlusResult = new HashSet<>();
        for (String q : queries) {
            HashSet<String> searchResult = find(dictionary, q);
            unionPlusResult = Util.union(unionPlusResult, searchResult);
        }
        return unionPlusResult;
    }

    private HashSet<String> getANDQueries(ArrayList<String> queries,
                                          HashMap<String, HashSet<String>> dictionary) {
        boolean firstPart = true;
        HashSet<String> result = new HashSet<>();
        HashSet<String> searchResult;
        for (String q : queries) {
            searchResult = find(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else
                result.retainAll(searchResult);
        }
        return result;
    }

    private HashSet<String> find(HashMap<String, HashSet<String>> dictionary, String q) {
        HashSet<String> search = dictionary.get(q);
        if (search == null)
            return new HashSet<>();
        HashSet<String> result = (HashSet<String>) search.clone();
        if (result == null)
            return new HashSet<>();
        return result;
    }
}
