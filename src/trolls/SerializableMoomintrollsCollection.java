package trolls;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import utils.FileManager;
import utils.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.PriorityQueue;

public class SerializableMoomintrollsCollection extends MoomintrollsCollection implements Serializable{

    private void fromJson(String json) {
        try {
            // TODO: make it without PriorityQueue<Moomintroll> : SOLID
            this.moomintrolls = JsonParser.jsonToObject(
                    json,
                    new TypeToken<PriorityQueue<Moomintroll>>(){}.getType()
            );
        } catch (JsonSyntaxException e) {
            System.out.println("Can't convert: error when parsing JSON: bad format");
        } catch (Exception e) {
            System.out.println("Can't convert: undefined exception when parsing JSON");
        }
    }

    private String toJson() {
        return JsonParser.objectToJson(this.moomintrolls);
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
        String fileContent;
        if(path == null) {
            System.out.println("Can't load: file path is undefined");
            return;
        }
        try {
            fileContent = FileManager.readFromFile(path);
        } catch (FileNotFoundException e) {
            System.out.println("Can't load: \"" + path + "\" isn't exist or isn't a file");
            return;
        } catch (Exception e) {
            System.out.println("Can't load: undefined exception when reading file \"" + path + "\"");
            return;
        }
        if(fileContent.isEmpty()) {
            System.out.println("Can't load: unknown error when reading file \"" + path + "\"");
            return;
        }
        MoomintrollsCollection lastMoomintrollsCollection = this;
        fromJson(fileContent);
        if(lastMoomintrollsCollection != this) {
            System.out.println("Objects successfully loaded from \"" + path + "\"");
        }
    }

}
