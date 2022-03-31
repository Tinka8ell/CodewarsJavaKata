import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatmanQuotes{

    public static String getQuote(String[] quotes, String hero){
        String returnString = "";
        String [] heros =  new String[]{"Batman", "Robin", "Joker"};
        Matcher lookUp = Pattern.compile("(\\d)").matcher(hero);
        if (lookUp.find()){
            int index = Integer.parseInt(lookUp.group(1));
            returnString = heros["BRJ".indexOf(hero.charAt(0))] + ": " + quotes[index];
        }
        return returnString;
    }

}

