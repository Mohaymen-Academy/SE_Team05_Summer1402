public class FolderPath {
    private String dataPath;
    private String stopwordsPath;

    public FolderPath(String dataPath, String stopwordsPath) {
        this.dataPath = dataPath;
        this.stopwordsPath = stopwordsPath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getStopwordsPath() {
        return stopwordsPath;
    }
}
