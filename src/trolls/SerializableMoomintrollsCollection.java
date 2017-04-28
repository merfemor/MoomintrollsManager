package trolls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import cui.FileUtils;

import java.io.IOException;
import java.io.Serializable;

public class SerializableMoomintrollsCollection extends MoomintrollsCollection implements Serializable{

    public void fromJson(String json) {
        clear();
        this.addAll(new Gson().fromJson(json, MoomintrollsCollection.class));
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public void saveToFile(String path) throws IOException {
        System.out.println("Saving collection...");
        FileUtils.writeToFile(path, toJson());
        System.out.println("Successfully saved to \"" + path + "\"");
    }

    /**
     * Loads moomintrolls collection from path
     */
    public void loadFromFile(String path) {
        String fileContent = FileUtils.readFromFile(path, true);
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
