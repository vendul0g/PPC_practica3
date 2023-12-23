package httpMessages;

import serializacion.JSONParser;

public class HTTPResponseAPI400 extends HTTPResponseError{
	//Variables globales
	private static final int CODE = 400;
	private static final String PHRASE = "Bad Request";
	
	//Constructor
	public HTTPResponseAPI400() {
		super(CODE, PHRASE);
	}
	
	//Getters & Setters
	public String getMessage() {
		return VERSION+SPACE+code+SPACE+phrase+RETCAR
			+ "Server: serverppc.com"+RETCAR
			+ "Content-Type: application/json"+RETCAR
			+ "Content-Length: "+JSONParser.badRequest().length()+RETCAR
			+ RETCAR
			+ JSONParser.badRequest()
			+ RETCAR;
	}
}
