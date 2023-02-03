package extractor;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSON {
	
	private static ObjectMapper objectMapper = getDefaultObjectMapper();
	
	
	private static ObjectMapper getDefaultObjectMapper() {
		ObjectMapper defaultobjectmapper = new ObjectMapper();
		defaultobjectmapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		return defaultobjectmapper;
	}
	
	public static JsonNode parse(String stringtoparse) throws IOException {
		
		return objectMapper.readTree(stringtoparse);
	}
}
