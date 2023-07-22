import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Util {
    public static <T> ArrayList<T> toArrayList(HashSet<T> set) {
        return new ArrayList<>(set);
    }

    public static <T> List<T> toArrayList(T[] array) {
        return Arrays.asList(array);
    }
}
