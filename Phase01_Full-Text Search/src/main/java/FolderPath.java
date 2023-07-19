public class FolderPath {
    private static FolderPath folderPath = null;
    private String dataPath;
    private final String stopWordsPath;

    private FolderPath() {
        dataPath = "./src/main/resources/Software Books Dataset/";
        stopWordsPath = "./src/main/resources/stopWords.txt";
    }

    public static FolderPath getInstance() {
        if (folderPath == null) folderPath = new FolderPath();
        return folderPath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getStopWordsPath() {
        return stopWordsPath;
    }
}
