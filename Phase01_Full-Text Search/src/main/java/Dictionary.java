import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private HashMap<String, ArrayList<String>> dictionary;
    public ArrayList<String> Search(String query){
        QueryHandler queryHandler=new QueryHandler();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueries(query);
        return queryHandler.runQueries(queries, dictionary);
    }
    public Dictionary(FolderPath folderPath){
        InvertedIndex _invertedIndex = new InvertedIndex(folderPath);
        dictionary =_invertedIndex.createDataStructure();
    }


}
