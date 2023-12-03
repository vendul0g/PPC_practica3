package messages;

public abstract class Message {
	//Constantes
	public static final int MODE_XML = 0;
	public static final int MODE_JSON = 1;
	public static final int MODE_CELSIUS = 2;
	public static final int MODE_FARENHEIT = 3;
	
	public static final char XML_HEADER = 'X';
	public static final char JSON_HEADER = 'J';
}
