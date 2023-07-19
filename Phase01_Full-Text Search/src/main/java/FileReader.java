import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileReader {
    private Doc getFileNameAndContent(Path path) {
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
        return new Doc(fileName, fileContent);
    }

    public HashMap<String, String> getDataset(String path) {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> {
                Doc file = getFileNameAndContent(p);
                fileText.put(file.title(), file.content());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    public String[] getStopWords(String path) {
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNextLine())
                stopWords.add(scanner.nextLine());
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords.toArray(String[]::new);
    }
}
