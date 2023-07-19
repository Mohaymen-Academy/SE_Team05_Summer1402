import org.apache.commons.lang3.time.StopWatch;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Application {
    private final Dictionary dictionary = new Dictionary();

    public ArrayList<String> Search(String query) {
        return dictionary.Search(query);
    }

    public Application add(String title, String content) {
        this.dictionary.add(new Doc(title, content));
        return this;
    }

    public Application setTokenizer(Tokenizer newTokenizer) {
        NLP.setTokenizer(newTokenizer);
        return this;
    }

    public Application setNormalizer(Normalizer newNormalizer) {
        NLP.setNormalizer(newNormalizer);
        return this;
    }

    public Application setStopWords(String[] newStopWords) {
        NLP.setStopWords(newStopWords);
        return this;
    }

    public Application addByFolder(String newDataPathFolder) {
        HashMap<String, String> docs = new FileReader().getDataset(newDataPathFolder);
        for (var title : docs.keySet())
            this.add(title, docs.get(title));
        return this;
    }

    private void printQueryResult(ArrayList<String> result, long duration) {
        System.out.println(
                MessageFormat.format("{0} records found in {1}ns!", result.size(), duration));
        System.out.println(result);
    }

    public void runInConsole() {
        Scanner scanner = new Scanner(System.in);
        StopWatch watch = new StopWatch();
        while (true) {
            System.out.println("Type \"!\" if you want to exit the program.");
            System.out.print("Search: ");
            String query = scanner.nextLine();
            if (query.equals("!")) break;
            watch.start();
            ArrayList<String> result = Search(query);
            watch.stop();
            printQueryResult(result, watch.getNanoTime());
            watch.reset();
        }
        scanner.close();
    }
}
