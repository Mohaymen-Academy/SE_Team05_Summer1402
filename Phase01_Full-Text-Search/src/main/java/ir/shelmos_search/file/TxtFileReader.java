package ir.shelmos_search.file;

import lombok.Cleanup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class TxtFileReader implements FileReader {

    @Override
    public HashMap<String, String> getFiles(String path) {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile)
                    .forEach(p -> fileText.put(getFileName(p), getFileContent(p)));
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
            @Cleanup Scanner scanner = new Scanner(file);
            fileContent = scanner.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    @Override
    public String getFileName(Path path) {
        int extensionIndex = path.getFileName().toString().lastIndexOf('.');
        return path.getFileName().toString().substring(0, extensionIndex);
    }
}
