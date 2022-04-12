import java.util.ArrayList;
import java.util.Collections;

public class KataFour
{
    public static long nextSmaller(long n)
    {
        long best = -1;
        StringBuilder digits = new StringBuilder(Long.toString(n));
        ArrayList<Character> rest = new ArrayList<>();
        int index = digits.length()-1;
        while (best == -1 && index > 0){
            rest.add(digits.charAt(index));
            rest.sort(Collections.reverseOrder());
            digits.deleteCharAt(index);
            index--;
            Character last = digits.charAt(index);
            Character swap = ' ';
            for (Character c: rest) {
                if (c < last){ // biggest less than last
                    swap = c;
                    break;
                }
            }
            if (swap != ' '){
                // found a swap
                rest.remove(swap);
                rest.add(last);
                rest.sort(Collections.reverseOrder());
                digits.deleteCharAt(index);
                digits.append(swap);
                while (rest.size()> 0)
                    digits.append(rest.remove(0));
                if (digits.charAt(0) != '0')
                    best = Long.parseLong(digits.toString());
            }
        }
        return best;
    }
}