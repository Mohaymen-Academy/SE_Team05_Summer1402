import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    private static String[] getFileNameAndContent(Path path) {
        File file = new File(path.toUri());
        String fileName = null, fileContent = null;
        try {
            Scanner scanner = new Scanner(file);
            fileName = path.getFileName().toString().split("\\.")[0];
            fileContent = scanner.useDelimiter("\\Z").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new String[]{fileName, fileContent};
    }

    private static String[] tokenize(String text) {
        return text.split("[ \\t\\n\\r]+");
    }

    private static String normalize(String token) {
        return token.toLowerCase();
    }

    private static HashMap<String, String> getDataset(String path) {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files
                .walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                String[] file = getFileNameAndContent(p);
                int indexFilename = 0, indexContent = 1;
                fileText.put(file[indexFilename], file[indexContent]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

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

    private static void addToDictionary(HashMap<String, HashSet<String>> dict, String title, String word) {
        if (!dict.containsKey(word)) {
            HashSet<String> bookList = new HashSet<>();
            bookList.add(title);
            dict.put(word, bookList);
        } else {
            var bookList = dict.get(word);
            bookList.add(title);
            // dictionary.replace(norm, bookList);
        }
    }

    private static HashMap<String, ArrayList<String>> createDictionary(HashMap<String, String> books) {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : books.keySet()) {
            String content = books.get(title);
            String[] tokens = tokenize(content);

            // HashMap<String, Integer> counts=getTokensCounts(tokens);
            for (String token : tokens) {
                String normalized = normalize(token);
                addToDictionary(dict, title, normalized);
            }

        }
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        for (String key : dict.keySet()) {
            dictionary.put(key, toArrayList(dict.get(key)));
        }
        return dictionary;
    }

    private static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }

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
        HashMap<String, String> books = getDataset(
                "./src/main/resources/Software Books Dataset/");

        HashMap<String, ArrayList<String>> dictionary = createDictionary(books);
        String query = "goal -compiler +java +design";

        long startTime = System.nanoTime();
        HashMap<String, ArrayList<String>> queries = separateQueries(query);
        ArrayList<String> result =runQueries(queries,dictionary);
        printQueryResult(result,startTime);
    }
}
