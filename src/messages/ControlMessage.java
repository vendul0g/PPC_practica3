package messages;

import serializacion.JSONParser;
import serializacion.XMLControlMessageParser;

public class ControlMessage extends Message{
	//Atributos
	private ControlMessageType command;
	private int idServer;
	
	//Constructor
	public ControlMessage(ControlMessageType c) {
		this.command = c;
	}
	
	public ControlMessage(ControlMessageType c, int port) {
		this.command = c;
		this.idServer = port;
	}
	
	//Getters & Setters
	public ControlMessageType getCommand() {
		return this.command;
	}
	
	protected String getCommandString() {
		return command.toString();
	}
	
	public int getIdServer() {
		return idServer;
	}
	
	//Funcionalidad
	public byte[] serialize(int mode){
		String s = "";
		if(mode == Message.MODE_JSON) {
			s += JSON_HEADER + JSONParser.serialize(this);
			return s.getBytes();
		}
		s += XML_HEADER + new XMLControlMessageParser(this).serialize();
		return s.getBytes();
	}
}
