package iris.playharmony.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Json {

    public static String toJson(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

    public static <T> T fromJson(Class<T> clazz, String json) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> T fromJsonFile(Class<T> clazz, String jsonFile) {
        try {
            return fromJson(clazz, new String(Files.readAllBytes(Paths.get(jsonFile))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
