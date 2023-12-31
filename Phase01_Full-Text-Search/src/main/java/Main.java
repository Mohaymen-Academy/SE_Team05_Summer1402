import lombok.Cleanup;
import org.apache.commons.lang3.time.StopWatch;
import ir.shelmos_search.ShelmosSearch;
import ir.shelmos_search.language.EdgeNGramTokenizer;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ShelmosSearch shelmosSearch = new ShelmosSearch()
                .setStopWords(new String[]{",", "."})
                .setStopWordsByFile("./src/main/resources/Stop Words.txt")
                .setTokenizer(new EdgeNGramTokenizer(2, 10))
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
            if (query.equals("!")) break;
            watch.start();
            List<String> result = shelmosSearch.search(query);
            watch.stop();
            printQueryResult(result, watch.getNanoTime());
            watch.reset();
        }
    }

    private static void printQueryResult(List<String> result, long duration) {
        System.out.println(
                MessageFormat.format("{0} records found in {1}ns!", result.size(), duration));
        System.out.println(result);
    }
}
