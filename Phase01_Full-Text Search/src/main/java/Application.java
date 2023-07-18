import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    public void run() {
        Dictionary dictionary = new Dictionary();
        getInput(dictionary);
    }

    private void printQueryResult(ArrayList<String> result, long startTime) {
        System.out.println(MessageFormat.format("{0} records found in {1}ns!", result.size(), System.nanoTime() - startTime));
        System.out.println(result);
    }

    private void getInput(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            String query = scanner.nextLine();
            if (query.equals("!")) break;
            long startTime = System.nanoTime();//TODO: separate stopwatch
            var result = dictionary.Search(query);
            printQueryResult(result, startTime);
        }
        scanner.close();
    }
}
