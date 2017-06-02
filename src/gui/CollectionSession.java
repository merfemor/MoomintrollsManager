package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import trolls.MoomintrollsCollection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CollectionSession {
    private boolean isSaved = true;
    private MoomintrollsCollection moomintrollsCollection;
    private File file;
    private Component owner;
    private ResourceBundle bundle;


    public CollectionSession(MoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }

    public void setResourceBundle(ResourceBundle bundle) {
        this.bundle = bundle;
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
                    bundle.getString("collectionCloseDialogMessage"),
                    bundle.getString("collectionCloseDialogTitle"),
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
        moomintrollsCollection = new MoomintrollsCollection();
        isSaved = true;
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
            if (newFile.exists() && file == null) {
                int reply = JOptionPane.showConfirmDialog(
                        owner,
                        bundle.getString("fileExistsWarningMessage"),
                        bundle.getString("fileExistsWarningTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (reply != JOptionPane.YES_OPTION) {
                    break;
                }
            }
            try {
                saveToFile(newFile);
                JOptionPane.showMessageDialog(
                        owner,
                        MessageFormat.format(bundle.getString("successfullySavedDialogMessage"), newFile.getPath()),
                        bundle.getString("successfullySavedDialogTitle"),
                        JOptionPane.INFORMATION_MESSAGE
                );
                file = newFile;
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        owner,
                        MessageFormat.format(bundle.getString("failedToSaveErrorMessage"), newFile.getPath()),
                        bundle.getString("failedToSaveErrorTitle"),
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
                        MessageFormat.format(bundle.getString("failedToOpenErrorMessage"), newFile.getPath()),
                        bundle.getString("failedToOpenErrorTitle"),
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        owner,
                        MessageFormat.format(bundle.getString("wrongFileFormatErrorMessage"), newFile.getPath()),
                        bundle.getString("wrongFileFormatErrorTitle"),
                        JOptionPane.ERROR_MESSAGE
                );
            }
            newFile = chooseOpenFile(file);
        }
        return false;
    }

    public void saveToFile(File file) throws IOException {
        String jsonCollection;
        jsonCollection = new GsonBuilder().setPrettyPrinting().create().toJson(moomintrollsCollection);
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
        moomintrollsCollection.clear();
        moomintrollsCollection.addAll(new Gson().fromJson(fileContent.toString(), MoomintrollsCollection.class));
        isSaved = true;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void reportChange() {
        isSaved = false;
    }

    public MoomintrollsCollection getMoomintrollsCollection() {
        return moomintrollsCollection;
    }

    public synchronized void setMoomintrollsCollection(MoomintrollsCollection moomintrollsCollection) {
        this.moomintrollsCollection = moomintrollsCollection;
    }

    public boolean isSaved() {
        return isSaved;
    }
}