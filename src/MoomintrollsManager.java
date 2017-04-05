import gui.GraphicalMoomintrollsManager;

public class MoomintrollsManager {
    public static void main(String[] args) {

        // cmd-mode
        if(args.length > 0 && args[0].equals("--cmd")) {
            ConsoleMoomintrollsManager consoleMoomintrollsManager = new ConsoleMoomintrollsManager();
            if(args.length > 1) {
                String envName = args[1];
                consoleMoomintrollsManager.setPathVariableName(envName);
            }
            consoleMoomintrollsManager.start();
        }

        GraphicalMoomintrollsManager graphicalMoomintrollsManager = new GraphicalMoomintrollsManager();

    }
}
