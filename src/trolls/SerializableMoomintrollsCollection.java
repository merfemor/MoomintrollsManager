package trolls;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import utils.FileManager;
import utils.JsonParser;

import java.io.IOException;
import java.io.Serializable;

public class SerializableMoomintrollsCollection extends MoomintrollsCollection implements Serializable{

    public SerializableMoomintrollsCollection(String json) throws Exception {
        super();
        fromJson(json);
    }

    public SerializableMoomintrollsCollection() {
        super();
    }

    public void fromJson(String json) {
        MoomintrollsCollection moomintrollsCollection = JsonParser.jsonToObject(
                json,
                new TypeToken<MoomintrollsCollection>(){}.getType()
        );
        clear();
        this.addAll(moomintrollsCollection);
    }

    private String toJson() {
        return JsonParser.objectToJson(this);
    }

    public void saveToFile(String path) throws IOException {
        System.out.println("Saving collection...");
        FileManager.writeToFile(path, toJson());
        System.out.println("Successfully saved to \"" + path + "\"");
    }


    /**
     * Loads moomintrolls collection from path
     */
    public void loadFromFile(String path) {
        String fileContent = FileManager.readFromFile(path, true);
        try {
            fromJson(fileContent);
            System.out.println("Objects successfully loaded from \"" + path + "\"");
        } catch (JsonSyntaxException e) {
        System.out.println("Can't convert: error when parsing JSON: bad format");
        } catch (Exception e) {
        System.out.println("Can't convert: undefined exception when parsing JSON");
        }
    }
}
