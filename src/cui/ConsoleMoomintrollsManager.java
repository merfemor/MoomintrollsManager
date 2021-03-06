package cui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import java.io.IOException;


public class ConsoleMoomintrollsManager {
    private MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
    private String path;
    private String pathVariableName;

    /**
     * Loads moomintrolls collection from path
     */
    @Deprecated
    public static void loadFromFile(MoomintrollsCollection moomintrolls, String path) {
        String fileContent = FileUtils.readFromFile(path, true);
        try {
            moomintrolls.clear();
            moomintrolls.addAll(new Gson().fromJson(fileContent, MoomintrollsCollection.class));
            System.out.println("Objects successfully loaded from \"" + path + "\"");
        } catch (JsonSyntaxException e) {
        System.out.println("Can't convert: error when parsing JSON: bad format");
        } catch (Exception e) {
        System.out.println("Can't convert: undefined exception when parsing JSON");
        }
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (path == null) {
                System.out.println("File path is undefined");
                path = FileUtils.generateFileName("trolls", ".json");
                System.out.println("File path was automatically set to \"" + path + "\"");
            }
            try {
                System.out.println("Saving collection...");
                FileUtils.writeToFile(path, new GsonBuilder().setPrettyPrinting().create().toJson(moomintrollsCollection));
                System.out.println("Successfully saved to \"" + path + "\"");
            } catch (IOException e) {
                System.out.println("Failed to save: error when writing to \"" + path + "\"");
            }
        }));
        System.out.println(" ----------------------------------------------\n" +
                "| Welcome to Moomintrolls Command Line Manager |\n" +
                " ----------------------------------------------\n");
        if(pathVariableName != null) {
            path = loadPathVariable(pathVariableName);
            loadFromFile(moomintrollsCollection, path);
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
            path = FileUtils.findSimilarFileName("trolls", ".json");
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
            loadFromFile(moomintrollsCollection, path);
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
                moomintroll = new Gson().fromJson(element, Moomintroll.class);
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


