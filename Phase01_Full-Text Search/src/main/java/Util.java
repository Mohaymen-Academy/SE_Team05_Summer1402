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

    public static HashSet<String> minus(HashSet<String> A, HashSet<String> B) {
        HashSet<String> result = new HashSet<>();
        for (String s : A)
            if (!B.contains(s)) result.add(s);
        return result;
    }

    public static HashSet<String> union(HashSet<String> A, HashSet<String> B) {
        HashSet<String> result = new HashSet<>();
        result.addAll(A);
        result.addAll(B);
        return result;
    }
}
