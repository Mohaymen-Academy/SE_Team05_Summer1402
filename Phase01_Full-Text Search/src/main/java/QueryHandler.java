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
        ArrayList<String> result = new ArrayList<>();
        boolean firstPart = true;
        for (String q : queries.get("AND")) {
            ArrayList<String> searchResult = find(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else result.retainAll(searchResult);
        }

        ArrayList<String> unionPlusResult = new ArrayList<>();
        for (String q : queries.get("OR")) {
            ArrayList<String> searchResult = find(dictionary, q);
            unionPlusResult.removeAll(searchResult);
            unionPlusResult.addAll(searchResult);
        }
        if (queries.get("OR").size() > 0) result.retainAll(unionPlusResult);


        for (String q : queries.get("NOT")) {
            ArrayList<String> searchResult = find(dictionary, q);
            result.removeAll(searchResult);
        }
        return result;
    }

    private ArrayList<String> find(HashMap<String, ArrayList<String>> dictionary, String q) {
        ArrayList<String> result = dictionary.get(q);
        if (result == null) return new ArrayList<>();
        return result;
    }
}
