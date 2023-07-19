import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Util {
    public static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }

    public static <T, U> HashMap<T, ArrayList<U>> convertHashMapSetToHashMapList(HashMap<T, HashSet<U>> setHashMap) {
        HashMap<T, ArrayList<U>> listHashMap = new HashMap<>();
        for (T key : setHashMap.keySet())
            listHashMap.put(key, toArrayList(setHashMap.get(key)));
        return listHashMap;
    }
}
