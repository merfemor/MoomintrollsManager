package gui;

import trolls.SerializableMoomintrollsCollection;

import java.io.*;
import java.util.Scanner;

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

    public synchronized void load() throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        StringBuilder fileContent = new StringBuilder();
        while (sc.hasNextLine()) {
            fileContent.append(sc.nextLine()).append("\n");
        }
        this.moomintrollsCollection.fromJson(fileContent.toString());
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