public class BraceChecker {

    public boolean isValid(String braces) {
        //ArrayList<String> stack = new ArrayList<String>();
        return braces.chars()
                .mapToObj(Character::toString)
                .reduce("", this::addBrace).length() == 0;
    }

    private String addBrace(String stack, String brace) {
        int index = "([{".indexOf(brace);
        if (index < 0){
            int lastBrace = stack.length() - 1;
            index = ")]}".indexOf(brace);
            if ((lastBrace >= 0) && (index == "([{".indexOf(stack.substring(lastBrace))))
                stack = stack.substring(0, lastBrace);
            else
                stack = "*"; // matches nothing
        } else { // add new start brace
            stack += brace;
        }
        return stack;
    }

}