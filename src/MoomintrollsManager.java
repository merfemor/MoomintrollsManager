public class MoomintrollsManager {
    public static void main(String[] args) {
        ConsoleMoomintrollsManager consoleMoomintrollsManager = new ConsoleMoomintrollsManager();
        if(args.length > 0) {
            String envName = args[0];
            consoleMoomintrollsManager.setPathVariableName(envName);
        }
        consoleMoomintrollsManager.start();
    }
}
