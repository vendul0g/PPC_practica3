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
    
    public static String badRequest() {
		return "{\n"
				+ "  \"status\": 400,\n"
				+ "  \"error\": \"Bad Request\"\n"
				+ "}\n";
	}
	
	public static String ok200() {
		return "{\n"
				+ "  \"status\": 200,\n"
				+ "  \"message\": \"OK.\""
				+ "}\n";
	}
    
}
