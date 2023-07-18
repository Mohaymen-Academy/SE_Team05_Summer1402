import java.util.Scanner;

public class Application {
    private FolderPath _folderPath;
    public Application(FolderPath folderPath) {
        _folderPath=folderPath;
    }

    private void getInput(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        String query;
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            query = scanner.nextLine();
            if (query.equals("!")) break;
            dictionary.Search(query);
        }
        scanner.close();
    }
    public void run(){
        Dictionary dictionary=new Dictionary(_folderPath);
        getInput(dictionary);
    }
}
