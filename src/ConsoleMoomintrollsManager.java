import com.google.gson.JsonSyntaxException;
import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;
import utils.FileManager;
import utils.JsonParser;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;


public class ConsoleMoomintrollsManager {
    private SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection();
    private String path;
    private String pathVariableName;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (path == null) {
                System.out.println("File path is undefined");
                path = FileManager.generateFileName("trolls", ".json");
                System.out.println("File path was automatically set to \"" + path + "\"");
            }
            try {
                moomintrollsCollection.saveToFile(path);
            } catch (IOException e) {
                System.out.println("Failed to save: error when writing to \"" + path + "\"");
            }
        }));
        System.out.println(" ----------------------------------------------\n" +
                "| Welcome to Moomintrolls Command Line Manager |\n" +
                " ----------------------------------------------\n");
        if(pathVariableName != null) {
            path = loadPathVariable(pathVariableName);
            moomintrollsCollection.loadFromFile(path);
        }

        System.out.println("\nType commands below to manage moomintrolls collection.\n" +
                "To see all available commands type \"help\"");

        while (true) {
            System.out.print("=> ");
            executeCommand(CommandLineParser.getNewCommand());
        }

    }

    public void setPathVariableName(String pathVariableName) {
        this.pathVariableName = pathVariableName;
    }

    private static String loadPathVariable(String pathVariableName) {
        String path = System.getenv(pathVariableName);
        if (path == null || path.isEmpty()) {
            System.out.println("Can't open path variable: \"" + pathVariableName + "\" is undefined or empty");
            path = FileManager.findSimilarFileName("trolls", ".json");
            if(path != null) {
                System.out.println("Path automatically set to " + path);
            }
        }
        return path;
    }

    private void executeCommand(String command) {
        if (!command.contains("{")) {
            command = command.trim();
        } else {
            command = command.substring(0, command.indexOf("{")).replaceAll(" ", "").replaceAll("\t", "")
                    + command.substring(command.indexOf("{"));
        }
        if (command.isEmpty()) {
            return;
        }
        if (command.equals("help")) {
            System.out.print(
                    "===================\n" +
                            "Commands\n" +
                            "===================\n" +
                            "save_and_exit\n" +
                            "remove_first\n" +
                            "open\n" +
                            "add_if_max {element}\n" +
                            "remove_greater {element}\n" +
                            "===================\n"
            );
        }
        else if (command.equals("save_and_exit")) {
            // unnecessary: shutdownhook automatically activates file saving
            // moomintrollsCollection.saveToFile(path);
            System.exit(0);
        }
        else if (command.equals("remove_first")) {
            if (moomintrollsCollection.isEmpty()) {
                System.out.println("Nothing to remove: collection is already empty");
            } else {
                moomintrollsCollection.poll();
                System.out.println("First element was successfully deleted");
            }
        }
        else if (command.equals("open")) {
            moomintrollsCollection.loadFromFile(path);
        }
        else if (command.equals("print")) {
            System.out.println(moomintrollsCollection);
        }
        else if (command.startsWith("use_file")) {
            path = command.substring("use_file".length());
        }

        // commands with JSON trolls.Moomintroll object argument
        else if ( command.contains("{") && command.contains("}")) {
            String element = command.substring(command.indexOf('{'));
            command = command.substring(0, command.indexOf('{'));
            Moomintroll moomintroll;
            try {
                moomintroll = JsonParser.jsonToObject(element, Moomintroll.class);
            } catch (JsonSyntaxException e) {
                System.out.println("Error when converting JSON: bad format");
                return;
            } catch (Exception e) {
                System.out.println("Undefined exception when converting JSON");
                return;
            }


            switch (command) {
                case "add_if_max":
                    moomintrollsCollection.add_if_max(moomintroll);
                    break;
                case "remove_greater":
                    moomintrollsCollection.remove_greater(moomintroll);
                    break;
                case "add":
                    moomintrollsCollection.add(moomintroll);
                    break;
                default:

                    System.out.println("Undefined command.\n" +
                            "Type \"help\" to see all available commands");
                    break;
            }
        } else {
            System.out.println("Undefined command or bad arguments format.\n" +
                    "Type \"help\" to see all available commands");
        }
    }

}


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
