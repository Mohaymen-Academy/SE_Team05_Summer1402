import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
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
        return new String[] { fileName, fileContent };
    }

    private static String[] tokenize(String text) {
        return text.split("[ \\t\\n\\r]+");
    }

    private static String stemString(String token) {
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

    private static HashMap<String, ArrayList<String>> createDictionay(HashMap<String, String> books) {
        HashMap<String, HashSet<String>> dict = new HashMap<>();

        for (String title : books.keySet()) {
            String content = books.get(title);
            String[] tokens = tokenize(content);

            // HashMap<String, Integer> counts=getTokensCounts(tokens);
            for (String token : tokens) {
                String normalized = stemString(token); // stemming occurs here.
                if (!dict.containsKey(normalized)) {
                    HashSet<String> bookList = new HashSet<>();
                    bookList.add(title);
                    dict.put(normalized, bookList);
                } else {
                    var bookList = dict.get(normalized);
                    bookList.add(title);
                    // dictionary.replace(norm, bookList);
                }
            }

        }
        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        for (String key : dict.keySet()) {
            ArrayList<String> bookList = new ArrayList<>();
            bookList.addAll(dict.get(key));
            dictionary.put(key, bookList);
        }
        return dictionary;
    }

    public static void main(String[] args) {
        HashMap<String, String> books = getDataset(
                "./src/main/resources/Software Books Dataset/");

        HashMap<String, ArrayList<String>> dictionary = createDictionay(books);
        System.out.println(dictionary.get("goal"));
        // HashMap<String, HashMap<String, Integer>> booksWithTokens;

    }
}
