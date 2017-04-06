package trolls;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import utils.FileManager;
import utils.JsonParser;

import javax.swing.text.html.HTMLDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.PriorityQueue;

public class SerializableMoomintrollsCollection extends MoomintrollsCollection implements Serializable{

    private boolean fromJson(String json) {
        try {
            MoomintrollsCollection moomintrollsCollection = JsonParser.jsonToObject(
                    json,
                    new TypeToken<MoomintrollsCollection>(){}.getType()
            );
            clear();
            this.addAll(moomintrollsCollection);
            return true;
        } catch (JsonSyntaxException e) {
            System.out.println("Can't convert: error when parsing JSON: bad format");
        } catch (Exception e) {
            System.out.println("Can't convert: undefined exception when parsing JSON");
        }
        return false;
    }

    private String toJson() {
        return JsonParser.objectToJson(this);
    }

    public void saveToFile(String path) {
        System.out.println("Saving collection...");
        if (path == null) {
            System.out.println("File path is undefined");
            path = FileManager.generateFileName("trolls", ".json");
            System.out.println("File path was automatically set to \"" + path + "\"");
        }
        try {
            FileManager.writeToFile(path, toJson());
            System.out.println("Successfully saved to \"" + path + "\"");
        } catch (IOException e) {
            System.out.println("Failed to save: error when writing to \"" + path + "\"");
        }
    }


    /**
     * Loads moomintrolls collection from path
     */
    public void loadFromFile(String path) {
        String fileContent = FileManager.readFromFile(path, true);
        if(fromJson(fileContent)) {
            System.out.println("Objects successfully loaded from \"" + path + "\"");
        }
    }

}
