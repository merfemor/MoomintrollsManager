package gui;

import trolls.SerializableMoomintrollsCollection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class CollectionSession {
    private boolean isSaved = true;
    private SerializableMoomintrollsCollection moomintrollsCollection;
    private File file;
    private Component owner;

    public CollectionSession(SerializableMoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }

    public void setOwner(Component owner) {
        this.owner = owner;
    }

    private File chooseSaveFile(File currentDirectory) {
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION ?
                fileChooser.getSelectedFile() : null;
    }

    private File chooseOpenFile(File currentDirectory) {
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION ?
                fileChooser.getSelectedFile() : null;
    }

    public boolean close() {
        if (!isSaved) {
            int reply = JOptionPane.showConfirmDialog(
                    owner,
                    "Current collection is not saved.\nDo you want to save it before closing?",
                    "Warning: unsaved collection",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (reply == JOptionPane.YES_OPTION) {
                save();
            }
            if (reply == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        file = null;
        moomintrollsCollection = new SerializableMoomintrollsCollection();
        return true;
    }

    public boolean saveAs() {
        File lastFile = file;
        file = null;
        boolean saved = save();
        if(!saved) {
            file = lastFile;
        }
        return saved;
    }

    public boolean save() {
        File newFile = file;
        if(newFile == null) {
            newFile = chooseSaveFile(new File("."));
            if(newFile == null) {
                return false;
            }
        }
        while (newFile != null) {
            if (newFile.exists()) {
                int reply = JOptionPane.showConfirmDialog(
                        owner,
                        "File is already exists.\nOverwrite it?",
                        "Warning: overwriting file",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (reply != JOptionPane.YES_OPTION) {
                    break;
                }
            }
            try {
                saveToFile(newFile);
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(
                        owner,
                        "Successfully saved into\n" + newFile.getPath(),
                        "Successfully saved",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                file = newFile;
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        owner,
                        "Failed to save into \n" + newFile.getPath() + "\nSelect file again.",
                        "Error: failed to save",
                        JOptionPane.ERROR_MESSAGE
                );
                newFile = chooseSaveFile(newFile);
            }
        }
        return false;
    }

    public boolean open() {
        File newFile = chooseOpenFile(file == null? new File(".") : file);
        while (newFile != null) {
            try {
                loadFromFile(newFile);
                file = newFile;
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        owner,
                        "Failed to open " + newFile.getPath() + "\nSelect file again.",
                        "Error: failed to open",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        owner,
                        "Failed to read " + newFile.getPath() + "\nFile is in the wrong format.\nSelect file again.",
                        "Error: failed to read",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            newFile = chooseOpenFile(file);
        }
        return false;
    }

    public void saveToFile(File file) throws IOException {
        String jsonCollection;
        jsonCollection = moomintrollsCollection.toJson();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
        outputStreamWriter.write(jsonCollection);
        outputStreamWriter.close();
        isSaved = true;
    }

    public void loadFromFile(File sourceFile) throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner sc = new Scanner(sourceFile);
        while (sc.hasNextLine()) {
            fileContent.append(sc.nextLine()).append("\n");
        }
        sc.close();
        moomintrollsCollection.fromJson(fileContent.toString());
        isSaved = true;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void reportChange() {
        isSaved = false;
    }

    public synchronized void setMoomintrollsCollection(SerializableMoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }
    public SerializableMoomintrollsCollection getMoomintrollsCollection() {
        return moomintrollsCollection;
    }

    public boolean isSaved() {
        return isSaved;
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