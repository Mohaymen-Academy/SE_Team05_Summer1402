import java.util.ArrayList;
import java.util.HashSet;

public class Util {
    public static  <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }
}
