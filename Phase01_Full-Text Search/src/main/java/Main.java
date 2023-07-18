public class Main {
    public static void main(String[] args) {
        final String datasetFolderPath ="./src/main/resources/Software Books Dataset/";
        final String stopWordsFilePath="./src/main/resources/stopwords.txt";
        FolderPath folderPath = new FolderPath(datasetFolderPath,stopWordsFilePath);
        new Application(folderPath).run();
    }
}
