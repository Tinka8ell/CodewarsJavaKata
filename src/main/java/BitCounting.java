public class BitCounting {

    public static int countBits(int n){
        return (int) Integer.toString(n, 2)
                .chars()
                .filter((a) -> a == '1')
                .count();
    }

}