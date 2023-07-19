import org.apache.commons.lang3.time.StopWatch;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private Dictionary dictionary = null;

    public void runInConsole() {
        if (dictionary == null) init();
        getInput(dictionary);
    }

    public Application init() {
        this.dictionary = new Dictionary();
        return this;
    }

    public ArrayList<String> Search(String query) {
        if (dictionary == null) init();
        return dictionary.Search(query);
    }

    public Application setTokenizer(ITokenizer newTokenizer) {
        NLP.setTokenizer(newTokenizer);
        return this;
    }

    public Application setNormalizer(INormalizer newNormalizer) {
        NLP.setNormalizer(newNormalizer);
        return this;
    }

    public Application setStopWords(String[] newStopWords) {
        NLP.setStopWords(newStopWords);
        return this;
    }

    public Application setDataPathFolder(String newDataPathFolder) {
        FolderPath.getInstance().setDataPath(newDataPathFolder);
        return this;
    }

    private void printQueryResult(ArrayList<String> result, long duration) {
        System.out.println(
                MessageFormat.format("{0} records found in {1}ns!", result.size(), duration));
        System.out.println(result);
    }

    private void getInput(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        StopWatch watch = new StopWatch();
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            String query = scanner.nextLine();
            if (query.equals("!")) break;
            watch.start();
            ArrayList<String> result = dictionary.Search(query);
            watch.stop();
            printQueryResult(result, watch.getNanoTime());
            watch.reset();
        }
        scanner.close();
    }
}
