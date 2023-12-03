package httpMessages;

import java.util.Set;
import java.util.TreeSet;

public class HTTPGetMessage extends HTTPRequestMessage{
	//Variables globales
	private static final String METHOD = "GET";
	
	//Atributos
	
	//Constructor
	public HTTPGetMessage(String url, String servername) {
		super(url,servername);
	}
	
	//Getters 
	@Override
	public String getMessage() {
		String m = METHOD+" "+this.url+" "+VERSION+RETCAR
				+"Host: "+this.host+RETCAR
				+this.USER_AGENT+RETCAR
				+this.CONNECTION+RETCAR
				+ RETCAR;
		return m;
	}
	
	//Funcionalidad
}
