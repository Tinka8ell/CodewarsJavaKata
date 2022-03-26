import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Permutations {

    private static HashMap<String, String> permute(HashMap<String, String> source){
        HashMap<String, String> soFar = new HashMap<>();
        for (String key: source.keySet()) {
            String word = source.get(key);
            for (int i = 0; i < word.length(); i++) {
                char selected = word.charAt(i);
                soFar.put(key + selected, word.substring(0, i) + word.substring(i + 1));
            }
        }
        return soFar;
    }

    public static List<String> singlePermutations(String s) {
        HashMap<String, String> source = new HashMap<>();
        source.put("", s);
        for (int i = 0; i < s.length(); i++) {
            source = permute(source);
        }
        return new ArrayList<>(source.keySet());
    }
}