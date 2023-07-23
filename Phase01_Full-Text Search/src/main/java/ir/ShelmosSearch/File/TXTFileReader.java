package ir.ShelmosSearch.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class TXTFileReader implements FileReader {
    @Override
    public HashMap<String, String> getFiles(String path) {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).forEach(p -> fileText.put(getFileName(p), getFileContent(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    @Override
    public String getFileContent(Path path) {
        File file = new File(path.toUri());
        String fileContent = null;
        try {
            Scanner scanner = new Scanner(file);
            fileContent = scanner.useDelimiter("\\Z").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    private String getFileName(Path path) {
        return path.getFileName().toString().split("\\.")[0];
    }
}
