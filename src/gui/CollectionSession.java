package gui;

import trolls.SerializableMoomintrollsCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CollectionSession {
    private SerializableMoomintrollsCollection moomintrollsCollection;
    private File file;

    public CollectionSession(SerializableMoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }

    public synchronized void save() throws IOException {
        String jsonCollection = moomintrollsCollection.toJson();
        sleep(8);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
        outputStreamWriter.write(jsonCollection);
        outputStreamWriter.close();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setMoomintrollsCollection(SerializableMoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }

    // method for testing
    public static void sleep(int seconds) {
        for(int i = seconds; i > 0; i--) {
            System.out.print(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}