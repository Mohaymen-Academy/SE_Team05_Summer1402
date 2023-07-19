import java.util.ArrayList;
import java.util.HashMap;

public class QueryHandler {
    public HashMap<String, ArrayList<String>> parseQueriesByType(String query) {

        HashMap<String, ArrayList<String>> queries = new HashMap<>() {{
            put("AND", new ArrayList<>());
            put("OR", new ArrayList<>());
            put("NOT", new ArrayList<>());
        }};

        String[] parts = query.split(" +");
        normalizeQueries(parts, queries);
        return queries;
    }

    private void normalizeQueries(String[] queries, HashMap<String, ArrayList<String>> queryList) {
        for (String query : queries) {
            switch (query.charAt(0)) {
                case '+' -> {
                    ArrayList<String> or = queryList.get("OR");
                    or.add(NLP.getNormalizer().normalize(query.substring(1)));
                }
                case '-' -> {
                    ArrayList<String> not = queryList.get("NOT");
                    not.add(NLP.getNormalizer().normalize(query.substring(1)));
                }
                default -> {
                    ArrayList<String> and = queryList.get("AND");
                    and.add(NLP.getNormalizer().normalize(query));
                }
            }
        }
    }

    public ArrayList<String> runQueries(HashMap<String, ArrayList<String>> queries,
                                        HashMap<String, ArrayList<String>> dictionary) {

        ArrayList<String> result = getANDQueries(queries.get("AND"), dictionary);;

        ArrayList<String> unionPlusResult = getORQueries(queries.get("OR"), dictionary);
        if (queries.get("AND").isEmpty()) result = unionPlusResult;
        else if (!queries.get("OR").isEmpty()) result.retainAll(unionPlusResult);

        getNOTQueries(queries.get("NOT"), dictionary, result);

        return result;
    }

    private void getNOTQueries(ArrayList<String> queries,
                               HashMap<String, ArrayList<String>> dictionary,
                               ArrayList<String> result) {
        ArrayList<String> searchResult;
        for (String q : queries) {
            searchResult = find(dictionary, q);
            result.removeAll(searchResult);
        }
    }

    private ArrayList<String> getORQueries(ArrayList<String> queries,
                                           HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> searchResult;
        ArrayList<String> unionPlusResult = new ArrayList<>();
        for (String q : queries) {
            searchResult = find(dictionary, q);
            unionPlusResult.removeAll(searchResult);
            unionPlusResult.addAll(searchResult);
        }
        return unionPlusResult;
    }

    private ArrayList<String> getANDQueries(ArrayList<String> queries,
                                            HashMap<String, ArrayList<String>> dictionary) {
        boolean firstPart = true;
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> searchResult;
        for (String q : queries) {
            searchResult = find(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else result.retainAll(searchResult);
        }
        return result;
    }


    private ArrayList<String> find(HashMap<String, ArrayList<String>> dictionary, String q) {
        ArrayList<String> result = dictionary.get(q);
        if (result == null) return new ArrayList<>();
        return result;
    }
}
