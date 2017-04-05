package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonParser {
    public static String objectToJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    public static <T> T jsonToObject(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
}
