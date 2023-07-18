public class Main {
    public static void main(String[] args) {
        FolderPath folderPath = new FolderPath("./src/main/resources/Software Books Dataset/",
                "./src/main/resources/stopwords.txt");
        new Application(folderPath).run();
    }
}
