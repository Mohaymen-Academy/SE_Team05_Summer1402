import java.util.ArrayList;
import java.util.HashMap;

public class QueryHandler {
    public HashMap<String, ArrayList<String>> parseQueries(String query) {

        HashMap<String, ArrayList<String>> queries = new HashMap<>() {{
            put("AND", new ArrayList<>());
            put("OR", new ArrayList<>());
            put("NOT", new ArrayList<>());
        }};

        String[] parts = query.split(" +");
        for (String part : parts) {
            switch (part.charAt(0)) {
                case '+':
                    ArrayList<String> or = queries.get("OR");
                    or.add(NLP.normalize(part.substring(1)));
                    break;
                case '-':
                    ArrayList<String> not = queries.get("NOT");
                    not.add(NLP.normalize(part.substring(1)));
                    break;
                default:
                    ArrayList<String> and = queries.get("AND");
                    and.add(NLP.normalize(part));
                    break;
            }
        }
        return queries;
    }

    public ArrayList<String> runQueries(HashMap<String, ArrayList<String>> queries, HashMap<String, ArrayList<String>> dictionary) {
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
