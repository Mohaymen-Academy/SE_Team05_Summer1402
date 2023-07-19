import org.apache.commons.lang3.time.StopWatch;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Application application = new Application()
                .addByFolder("./src/main/resources/Software Books Dataset/");
        runInConsole(application);
    }


    private static void runInConsole(Application application) {
        Scanner scanner = new Scanner(System.in);
        StopWatch watch = new StopWatch();
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            String query = scanner.nextLine();
            if (query.equals("!")) break;
            watch.start();
            ArrayList<String> result = application.search(query);
            watch.stop();
            printQueryResult(result, watch.getNanoTime());
            watch.reset();
        }
        scanner.close();
    }

    private static void printQueryResult(ArrayList<String> result, long duration) {
        System.out.println(
                MessageFormat.format("{0} records found in {1}ns!", result.size(), duration));
        System.out.println(result);
    }
}
