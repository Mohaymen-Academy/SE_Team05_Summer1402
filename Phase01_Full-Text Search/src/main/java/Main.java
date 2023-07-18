import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static void getInput(Dictionary dictionary) {
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

    public static void main(String[] args) {
        FolderPath folderPath=new FolderPath("./src/main/resources/Software Books Dataset/","./src/main/resources/stopwords.txt");
        Dictionary dictionary=new Dictionary(folderPath);
        getInput(dictionary);
    }
}
