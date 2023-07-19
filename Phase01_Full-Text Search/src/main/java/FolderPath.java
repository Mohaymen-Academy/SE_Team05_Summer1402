public class FolderPath {
    private static FolderPath folderPath = null;
    private String dataPath;
    private final String stopwordsPath;

    private FolderPath() {
        dataPath = "./src/main/resources/Software Books Dataset/";
        stopwordsPath = "./src/main/resources/stopwords.txt";
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

    public String getStopwordsPath() {
        return stopwordsPath;
    }
}
