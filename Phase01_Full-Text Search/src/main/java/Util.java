import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Util {
    public static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }
    //TODO: delete unused methods
    public static <T, U> HashMap<T, ArrayList<U>> convertHashMapSetToHashMapList(HashMap<T, HashSet<U>> setHashMap) {
        HashMap<T, ArrayList<U>> listHashMap = new HashMap<>();
        for (T key : setHashMap.keySet())
            listHashMap.put(key, toArrayList(setHashMap.get(key)));
        return listHashMap;
    }
    public static HashSet<String> minus(HashSet<String> A,HashSet<String> B){
        HashSet<String> result=new HashSet<>();
        for (String s : A) {
            if(!B.contains(s))
                result.add(s);
        }
        return result;
    }
    public static HashSet<String> intersect(HashSet<String> A,HashSet<String> B){
        HashSet<String> result=new HashSet<>();
        for (String s : A) {
            if (B.contains(s))result.add(s);
        }
        return result;
    }
    public static HashSet<String> union(HashSet<String> A,HashSet<String> B){
        HashSet<String> result=new HashSet<>();
        result.addAll(A);
        result.addAll(B);
        return result;
    }
}
