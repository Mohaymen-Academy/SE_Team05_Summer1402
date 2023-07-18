import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Dictionary {
    private HashMap<String, ArrayList<String>> _dictionary;
    public void Search(String query){
        long startTime = System.nanoTime();
        HashMap<String, ArrayList<String>> queries = parseQueries(query);
        ArrayList<String> result = runQueries(queries, _dictionary);
        printQueryResult(result, startTime);
    }
    public Dictionary(FolderPath folderPath){
        InvertedIndex _invertedIndex = new InvertedIndex(folderPath);
        _dictionary=_invertedIndex.createDataStructure();
    }
    private HashMap<String, ArrayList<String>> parseQueries(String query) {
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
                    or.add(InvertedIndex.normalize(part.substring(1)));
                    break;
                case '-':
                    ArrayList<String> not = queries.get("NOT");
                    not.add(InvertedIndex.normalize(part.substring(1)));
                    break;
                default:
                    ArrayList<String> and = queries.get("AND");
                    and.add(InvertedIndex.normalize(part));
                    break;
            }
        }
        return queries;
    }

    private ArrayList<String> runQueries(HashMap<String, ArrayList<String>> queries, HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> result = new ArrayList<>();
        boolean firstPart = true;
        for (String q : queries.get("AND")) {
            ArrayList<String> searchResult = searchDictionary(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else result.retainAll(searchResult);
        }

        ArrayList<String> unionPlusResult = new ArrayList<>();
        for (String q : queries.get("OR")) {
            ArrayList<String> searchResult = searchDictionary(dictionary, q);
            unionPlusResult.removeAll(searchResult);
            unionPlusResult.addAll(searchResult);
        }
        if (queries.get("OR").size() > 0) result.retainAll(unionPlusResult);


        for (String q : queries.get("NOT")) {
            ArrayList<String> searchResult = searchDictionary(dictionary, q);
            result.removeAll(searchResult);
        }
        return result;
    }

    private ArrayList<String> searchDictionary(HashMap<String, ArrayList<String>> dictionary, String q) {
        ArrayList<String> result = dictionary.get(q);
        if (result == null) return new ArrayList<>();
        return result;
    }

    private void printQueryResult(ArrayList<String> result, long startTime) {
        System.out.println(MessageFormat.format("{0} records found in {1}ns!", result.size(), System.nanoTime() - startTime));
        System.out.println(result);
    }

}
