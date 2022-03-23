import java.util.Optional;
import java.util.stream.Stream;

public class ProdFib {

    public static long[] productFib(long prod) {
        // If prod is the product of two sequential numbers,
        // then upper one is < Math.sqrt(prod)!
        long most = (long) Math.floor(Math.sqrt(prod));
        long[] defaultResult = {0, 1, 0};
        Optional<long[]> result = Stream
                .iterate(defaultResult, t -> new long[]{t[1], t[0] + t[1], t[1] * (t[0] + t[1])})
                .limit(most)
                .filter(t -> t[2] >= prod)
                .limit(1)
                .map(t -> new long[]{t[0], t[1], (t[2] == prod) ? 1 : 0})
                .findFirst();
        return result.orElse(defaultResult);
    }
}