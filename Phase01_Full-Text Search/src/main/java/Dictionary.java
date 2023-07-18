import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private HashMap<String, ArrayList<String>> _dictionary;
    public ArrayList<String> Search(String query){
        QueryHandler queryHandler=new QueryHandler();
        HashMap<String, ArrayList<String>> queries = queryHandler.parseQueries(query);
        return queryHandler.runQueries(queries, _dictionary);
    }
    public Dictionary(FolderPath folderPath){
        InvertedIndex _invertedIndex = new InvertedIndex(folderPath);
        _dictionary=_invertedIndex.createDataStructure();
    }


}
