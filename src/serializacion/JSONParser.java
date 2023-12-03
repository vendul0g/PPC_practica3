package serializacion;

import com.google.gson.Gson;

public class JSONParser {
    private static final Gson gson = new Gson();

    public static String serialize(Object object) {
        return gson.toJson(object);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json.substring(1, json.length()), clazz);
    }
    
}
