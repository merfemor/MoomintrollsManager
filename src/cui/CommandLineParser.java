package cui;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

class CommandLineParser {
    /**
     * Cuts all the pairs of double quotes and the text between them in string
     * @param s string to cut
     * @return cutted string
     */
    private static String cutQuotes(String s) {
        s = s.replace("\\\"", ""); // cut escape characters
        String[] quotes = s.split("\"");
        String result = "";
        for(int i = 0; i < quotes.length; i += 2) {
            result += quotes[i];
        }

        // if there is odd number of parentheses in string
        if((s.replace("\"", "").length() - s.length()) % 2 != 0) {
            result += "\""; // add the last bracket and string after it
            if(quotes.length %2 == 0 && quotes.length > 0) {
                result += quotes[quotes.length - 1];
            }
        }
        return result;
    }

    /**
     * Checks if the string is part of correct bracket sequence.
     * It means that you can get a correct bracket sequence from this string by adding >=1 brackets.
     * <p>
     *     Ignores all symbols in string, except brackets []{}().
     *     Also checks correct double quotes "" sequence.
     *     Ignores all symbols between quotes.
     * </p>
     * @param s string to check
     * @return true if so, false if not
     */
    private static boolean isValid(String s) {
        s = cutQuotes(s);
        // if have unclosed double quote
        if(s.contains("\"")) {
            return false;
        }

        char[] str = s.toCharArray();
        // string without any brackets is not valid
        // so we need to check that there is at least one element added to stack
        boolean stackHasChanged = false;

        Stack<Character> stack = new Stack<>();

        for (char symbol: str) {
            if (symbol == '{' || symbol == '[' || symbol == '(') {
                stack.push(symbol);
                stackHasChanged = true;
            } else if (symbol == '}' || symbol == ']' || symbol == ')')  {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    char topSymbol = stack.pop();
                    if(     (symbol == '}' && topSymbol != '{') ||
                            (symbol == ']' && topSymbol != '[') ||
                            (symbol == ')' && topSymbol != '(')) {
                        return false;
                    }
                }
            }
        }
        return stackHasChanged && !stack.isEmpty();
    }

    public static String getNewCommand() {
        Scanner reader = new Scanner(System.in);
        String command = "";
        do {
            try {
                command += reader.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println();
                break;
            }
        } while (isValid(command)); // ends when there is nothing to add
        return command;
    }

}
