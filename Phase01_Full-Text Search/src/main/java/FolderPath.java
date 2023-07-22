public class FolderPath {
    private static FolderPath folderPath = null;
    private final String stopWordsPath;

    private FolderPath() {
        stopWordsPath = "./src/main/resources/stopWords.txt";
    }

    public static FolderPath getInstance() {
        if (folderPath == null) folderPath = new FolderPath();
        return folderPath;
    }


    public String getStopWordsPath() {
        return stopWordsPath;
    }
}
