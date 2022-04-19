/**
 * Morse Code Decoder
 *
 * The Morse code encodes every character as a sequence of "dots" and "dashes".
 * For example, the letter A is coded as ·−, letter Q is coded as −−·−, and digit 1 is coded as ·−−−−.
 * The Morse code is case-insensitive, traditionally capital letters are used.
 * When the message is written in Morse code,
 * a single space is used to separate the character codes and 3 spaces are used to separate words.
 * For example, the message HEY JUDE in Morse code is ···· · −·−−   ·−−− ··− −·· ·.
 *
 * NOTE: Extra spaces before or after the code have no meaning and should be ignored.
 *
 * In addition to letters, digits and some punctuation, there are some special service codes,
 * the most notorious of those is the international distress signal SOS (that was first issued by Titanic),
 * that is coded as ···−−−···. These special codes are treated as single special characters,
 * and usually are transmitted as separate words.
 *
 *
 * MorseCodeDecoder.decode(".... . -.--   .--- ..- -.. .")
 * //should return "HEY JUDE"
 * NOTE: For coding purposes you have to use ASCII characters . and -, not Unicode characters.
 *
 * The Morse code table is preloaded for you as a dictionary, feel free to use it:
 * MorseCode.get(".--")
 */
public class MorseCodeDecoderVerbose {

    private final char[] code;
    private int next;


    public MorseCodeDecoderVerbose(String morseCode){
        code = morseCode.trim().toCharArray();
        next = 0;
    }

    public boolean more(){
        return next < code.length;
    }

    public char getNext(){
        if (!more())
            return 0;
        next++;
        return code[next - 1];
    }

    private String getCode() {
        StringBuilder code = new StringBuilder();
        char symbol = getNext();
        while (symbol > 0 && symbol != ' '){
            code.append(symbol);
            symbol = getNext();
        }
        if (symbol > 0)
            next--; // unread space
        return code.toString();
    }

    public int skipSpace(){
        int spaces = 0;
        char symbol = getNext();
        while (symbol == ' '){
            spaces++;
            symbol = getNext();
        }
        if (symbol > 0)
            next--; // unread space
        return spaces;
    }

    /**
     * Decode a String of Morse Code into uppercase readable String.
     *
     * @param morseCode a String of '.', '-' and ' ' characters
     * @return readable String
     */
    public static String decode(String morseCode) {
        StringBuilder message = new StringBuilder();
        MorseCodeDecoderVerbose decoder = new MorseCodeDecoderVerbose(morseCode);
        while (decoder.more()){
            String code = decoder.getCode();
            message.append(MorseCode.get(code));
            int gap = decoder.skipSpace();
            if (gap > 1)
                message.append(" ");
        }
        return message.toString();
    }

}