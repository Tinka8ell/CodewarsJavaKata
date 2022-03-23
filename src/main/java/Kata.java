public class Kata
{
    public static char findMissingLetter(char[] array)
    {
        char missing = (char) (array[0] - 1);
        for (var c: array) {
            missing++;
            if (c != missing)
                return missing;
        }
        return missing;
    }
}