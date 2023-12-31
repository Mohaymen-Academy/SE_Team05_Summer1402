package ir.shelmos_search.file;

import java.nio.file.Path;
import java.util.HashMap;

public interface FileReader {

    HashMap<String, String> getFiles(String path);

    String getFileContent(Path path);

    String getFileName(Path path);
}
