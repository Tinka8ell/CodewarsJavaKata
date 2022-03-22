import java.util.Arrays;

public class FindOutlier{
    static int find(int[] integers){
        // mostly odd if more than 1 odd numbers
        boolean mostlyOdd = 1 < Arrays.stream(integers, 0, 3)
                .reduce(0, (a, b) -> a += Math.abs(b % 2)); // count odd numbers
        int i = 0;
        if (mostlyOdd){ // look for first even
            while ((integers[i] % 2) != 0) i ++;
        } else { // look for first odd
            while ((integers[i] % 2) == 0) i ++;
        }
        return integers[i];
    }}