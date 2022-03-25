import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KataOne {

    public static List filterList(final List list) {
        return (List) list.stream().filter(Predicate.not((Object a) -> a instanceof String)).collect(Collectors.toList());
    }
}
