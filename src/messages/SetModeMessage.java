package messages;

import serializacion.JSONParser;
import serializacion.XMLSetModeMessageParser;

public class SetModeMessage extends ControlMessage{
	//Atributos
	int mode;
	
	//Constructor
	public SetModeMessage(ControlMessageType t, String m) {
		super(t);
		setMode(m);
	}
	
	public SetModeMessage(ControlMessageType t, int idServer, int m) {
		super(t, idServer);
		setMode(m);
	}
	
	public SetModeMessage(ControlMessageType t, int idServer, String m) {
		super(t, idServer);
		setMode(m);
	}
	
	//Getters & Setters
	public int getMode() {
		return mode;
	}
	
	public void setMode(int m) {
		if(m == Message.MODE_JSON || m == Message.MODE_XML || m == Message.MODE_FARENHEIT || m == Message.MODE_CELSIUS)
			this.mode = m;
	}
	
	//Funcionalidad
	private void setMode(String m) {
		switch(m) {
		case "xml":
			this.mode = Message.MODE_XML;
			break;
		case "json":
			this.mode = Message.MODE_JSON;
			break;
		case "celsius":
			this.mode = Message.MODE_CELSIUS;
			break;
		case "farenheit":
			this.mode = Message.MODE_FARENHEIT;
			break;		
		default:
			this.mode = -1;
		}
	}
	
	public byte[] serialize(int mode){
		String s = "";
		if(mode == Message.MODE_JSON) {
			s += JSON_HEADER + JSONParser.serialize(this);
			return s.getBytes();
		}
		s += XML_HEADER + new XMLSetModeMessageParser(this).serialize();
		return s.getBytes();
	}
	
}
