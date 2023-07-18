public class FolderPath {
    private static FolderPath folderPath = null;

    private String dataPath;
    private String stopwordsPath;

    private FolderPath() {
        // TODO: 7/18/2023 save paths in enum or file
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

    public String getStopwordsPath() {
        return stopwordsPath;
    }
}
