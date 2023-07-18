public class FolderPath {
    private static FolderPath folderPath = null;

    private String dataPath;
    private String stopwordsPath;

    private FolderPath() {
        dataPath ="./src/main/resources/Software Books Dataset/";
        stopwordsPath="./src/main/resources/stopwords.txt";
    }

    public static FolderPath getInstance() {
        return (folderPath == null) ? new FolderPath() : folderPath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getStopwordsPath() {
        return stopwordsPath;
    }
}
