import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static HashMap<String, ArrayList<String>> parseQueries(String query) {
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

    private static ArrayList<String> runQueries(HashMap<String, ArrayList<String>> queries, HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> result = new ArrayList<>();
        boolean firstPart = true;
        for (String q : queries.get("AND")) {
            ArrayList<String> searchResult = search(dictionary, q);
            if (firstPart) {
                result = searchResult;
                firstPart = false;
            } else result.retainAll(searchResult);
        }

        ArrayList<String> unionPlusResult = new ArrayList<>();
        for (String q : queries.get("OR")) {
            ArrayList<String> searchResult = search(dictionary, q);
            unionPlusResult.removeAll(searchResult);
            unionPlusResult.addAll(searchResult);
        }
        if (queries.get("OR").size() > 0) result.retainAll(unionPlusResult);


        for (String q : queries.get("NOT")) {
            ArrayList<String> searchResult = search(dictionary, q);
            result.removeAll(searchResult);
        }
        return result;
    }

    private static ArrayList<String> search(HashMap<String, ArrayList<String>> dictionary, String q) {
        ArrayList<String> result = dictionary.get(q);
        if (result == null) return new ArrayList<>();
        return result;
    }

    private static void printQueryResult(ArrayList<String> result, long startTime) {
        System.out.println(MessageFormat.format("{0} records found in {1}ns!", result.size(), System.nanoTime() - startTime));
        System.out.println(result);
    }

    private static void getInput(HashMap<String, ArrayList<String>> dictionary) {
        Scanner scanner = new Scanner(System.in);
        String query;
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            query = scanner.nextLine();
            if (query.equals("!")) break;
            long startTime = System.nanoTime();
            HashMap<String, ArrayList<String>> queries = parseQueries(query);
            ArrayList<String> result = runQueries(queries, dictionary);
            printQueryResult(result, startTime);
        }
        scanner.close();
    }

    public static void main(String[] args) {
        InvertedIndex invertedIndex = new InvertedIndex("./src/main/resources/Software Books Dataset/");
        HashMap<String, ArrayList<String>> dictionary = invertedIndex.getDictionary();

        getInput(dictionary);
    }
}
