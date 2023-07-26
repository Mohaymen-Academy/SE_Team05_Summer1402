import ir.shelmos_search.ShelmosSearch;
import ir.shelmos_search.language.EdgeGramTokenizer;
import lombok.Cleanup;
import org.apache.commons.lang3.time.StopWatch;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ShelmosSearch shelmosSearch = new ShelmosSearch()
                .setStopWords(new String[] { ",", "." })
                .setStopWordsByFile("./src/main/resources/stopWords.txt")
                .setTokenizer(new EdgeGramTokenizer(2,5))
                .addDocsByFolder("./src/main/resources/Software Books Dataset/");
        runInConsole(shelmosSearch);
    }

    private static void runInConsole(ShelmosSearch shelmosSearch) {

        @Cleanup
        Scanner scanner = new Scanner(System.in);
        StopWatch watch = new StopWatch();
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            String query = scanner.nextLine();
            if (query.equals("!"))
                break;
            watch.start();
            ArrayList<String> result = shelmosSearch.search(query);
            watch.stop();
            printQueryResult(result, watch.getNanoTime());
            watch.reset();
        }
    }

    private static void printQueryResult(ArrayList<String> result, long duration) {
        System.out.println(
                MessageFormat.format("{0} records found in {1}ns!", result.size(), duration));
        System.out.println(result);
    }
}
