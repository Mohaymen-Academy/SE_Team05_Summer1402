package ir.ShelmosSearch.File;

import java.nio.file.Path;
import java.util.HashMap;

public interface FileReader {
    HashMap<String, String> getFiles(String path);
    String getFileContent(Path path);
}
