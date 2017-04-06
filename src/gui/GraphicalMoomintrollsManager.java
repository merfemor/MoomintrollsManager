package gui;

import trolls.SerializableMoomintrollsCollection;

public class GraphicalMoomintrollsManager {
    private SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection();
    private MainWindow mainWindow = new MainWindow();

    public GraphicalMoomintrollsManager() {
        mainWindow.setVisible(true);
    }
}
