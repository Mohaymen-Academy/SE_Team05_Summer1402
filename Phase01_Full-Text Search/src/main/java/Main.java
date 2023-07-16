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

    private static HashMap<String, ArrayList<String>> createDictionay(HashMap<String, String> books) {
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
        ArrayList<T> list = new ArrayList<>();
        list.addAll(set);
        return list;
    }

    private static void removeDuplicates(ArrayList<String> list) {

    }

    public static void main(String[] args) {
        HashMap<String, String> books = getDataset(
                "./src/main/resources/Software Books Dataset/");

        HashMap<String, ArrayList<String>> dictionary = createDictionay(books);
        String query = "goal +get";
        String[] parts = query.split(" +");
        ArrayList<String> result = new ArrayList<>();
        for (String part : parts) {
            ArrayList<String> searchResult;
            switch (part.charAt(0)) {
                case '+':
                    searchResult = dictionary.get(part.substring(1));

                    result.removeAll(searchResult);
                    result.addAll(searchResult);
                    break;
                case '-':
                    searchResult = dictionary.get(part.substring(1));
                    result.removeAll(searchResult);
                    break;

                default:
                    searchResult = dictionary.get(part);
                    result.retainAll(searchResult);
                    break;
            }
        }
        System.out.println(result);

    }
}
