import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private Dictionary dictionary;

    // TODO: 7/18/2023 Dictionary must be newed after setting new attributes
    public Application() {
        this.dictionary = new Dictionary();
    }

    public void runInConsole() {
        getInput(dictionary);
    }
    public ArrayList<String> Search(String query) {
        return dictionary.Search(query);
    }
    Application setTokenizer(ITokenizer newTokenizer) {
        NLP.setTokenizer(newTokenizer);
        return this;
    }

    Application setNormalizer(INormalizer newNormalizer) {
        NLP.setNormalizer(newNormalizer);
        return this;
    }
    Application setStopWords(ArrayList<String> newStopWords) {
        NLP.setStopWords(newStopWords);
        return this;
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
