package utils;

import java.io.*;
import java.util.Scanner;

public class FileManager {
    public static String generateFileName(String fileName, String fileExtension) {
        String path = fileName + fileExtension;
        int num = 2;
        for (int i = 1; pathExists(path); i++) {
            path = fileName + "(" + i + ")" + fileExtension;
        }
        return path;
    }

    public static String findSimilarFileName(String fileName, String fileExtension) {
        File[] directory = new File(".").listFiles(
                (file, s) -> s.matches(fileName + "[\\d]*" + fileExtension)
        );
        if (directory.length != 0) {
            String biggest = directory[0].getName();
            for (File file : directory) {
                String thisName = file.getName();
                if (thisName.length() > biggest.length() ||
                        (thisName.length() == biggest.length() &&
                                thisName.compareTo(biggest) > 0)
                        ) {
                    biggest = thisName;
                }
            }
            return biggest;
        }
        return null;
    }

    public static String readFromFile(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        String fileContent = "";
        while (sc.hasNextLine()) {
            fileContent += (sc.nextLine() + "\n");
        }
        return fileContent;
    }


    public static String readFromFile(String path, boolean printText) {
        try {
            return readFromFile(path);
        } catch (FileNotFoundException|NullPointerException e) {
            if(printText) {
                System.out.println("Can't open: \"" + path + "\" isn't exist or isn't a file");
            }

        } catch (Exception e) {
            if(printText) {
                System.out.println("Can't open: undefined exception when reading file \"" + path + "\"");
            }
        }
        return null;
    }

    public static void writeToFile(String path, String s) throws IOException, NullPointerException {
        File file = new File(path);
        file.createNewFile();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
        outputStreamWriter.write(s);
        outputStreamWriter.close();

    }

    public static boolean pathExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}
