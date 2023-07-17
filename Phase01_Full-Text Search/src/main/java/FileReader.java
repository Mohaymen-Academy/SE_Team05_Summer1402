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
    public static HashMap<String, String> getDataset(String path) {
        HashMap<String, String> fileText = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
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

    public static ArrayList<String> getStopWords(String path) {
        ArrayList<String> stopWords=new ArrayList<>();
        try {
            FileInputStream fis=new FileInputStream(path);
            Scanner sc=new Scanner(fis);
            while(sc.hasNextLine())
            {
                stopWords.add(sc.nextLine());
            }
            sc.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return stopWords;
    }
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
}
