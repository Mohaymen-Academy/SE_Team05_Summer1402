import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
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
        return new String[]{fileName, fileContent};
    }


    private static HashMap<String, String> getDataset() {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/Software Books Dataset/"))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String[] file = getFileNameAndContent(path);
                fileText.put(file[0], file[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    public static void main(String[] args) {
        HashMap<String, String> data = getDataset();
    }
}
