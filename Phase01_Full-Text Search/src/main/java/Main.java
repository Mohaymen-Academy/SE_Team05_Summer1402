import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

public class Main {






    // private static HashMap<String, Integer> getTokensCounts(String[] tokens) {
    // HashMap<String, Integer> counter = new HashMap<>();
    // for (String token : tokens) {
    // String normalized = stemString(token);
    // if (!counter.containsKey(normalized)) {
    // counter.put(normalized, 0);
    // }
    // counter.replace(normalized, counter.get(normalized) + 1);
    // }
    // return counter;
    // }





    private static <T> void removeDuplicates(ArrayList<T> list) {
        Set<T> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }

    private static void printQueryResult(ArrayList<String> result,long startTime) {
        System.out.println(MessageFormat.format("{0} records found in {1}ns!", result.size(), System.nanoTime() - startTime));
        System.out.println(result);
    }
    private static ArrayList<String> runQueries(HashMap<String, ArrayList<String>> queries,HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> result = new ArrayList<>();
        boolean firstPart = true;
        for (String q : queries.get("AND")) {
            var searchResult = search(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else {
                result.retainAll(searchResult);
            }
        }
        ArrayList<String> unionPlusResult = new ArrayList<>();
        for (String q : queries.get("OR")) {
            var searchResult = search(dictionary, q);
            unionPlusResult.removeAll(searchResult);
            unionPlusResult.addAll(searchResult);
        }
        if (queries.get("OR").size() > 0)
            result.retainAll(unionPlusResult);
        for (String q : queries.get("NOT")) {
            var searchResult = search(dictionary, q);
            result.removeAll(searchResult);
        }
        return result;
    }

    private static HashMap<String, ArrayList<String>> separateQueries(String query) {
        HashMap<String, ArrayList<String>> queries = new HashMap<>() {{
            put("AND", new ArrayList<String>());
            put("OR", new ArrayList<String>());
            put("NOT", new ArrayList<String>());
        }};
        String[] parts = query.split(" +");
        for (String part : parts) {
            switch (part.charAt(0)) {
                case '+':
                    var or = queries.get("OR");
                    or.add(part.substring(1));
                    break;
                case '-':
                    var not = queries.get("NOT");
                    not.add(part.substring(1));
                    break;
                default:
                    var and = queries.get("AND");
                    and.add(part);
                    break;
            }
        }
        return queries;
    }

    private static ArrayList<String> search(HashMap<String, ArrayList<String>> dictionary, String q) {
        var result = dictionary.get(q);
        if (result == null)
            return new ArrayList<String>();
        return result;
    }

    public static void main(String[] args) {
        InvertedIndex.populateBooks("./src/main/resources/Software Books Dataset/");
        InvertedIndex.createDictionary();
        String query = "goal -compiler +java +design";
        long startTime = System.nanoTime();
        HashMap<String, ArrayList<String>> queries = separateQueries(query);
        ArrayList<String> result =runQueries(queries,InvertedIndex.getDictionary());
        printQueryResult(result,startTime);
    }
}
