package messages;

import serializacion.JSONParser;
import serializacion.XMLBroadcastMessageParser;

public class BroadcastMessage extends Message{
	//Atributos
	private int id;
	private String nameParam1;
	private String param1; 
	private String medida1;
	private String nameParam2;
	private String param2;
	private String medida2;
	private String nameParam3;
	private String param3;
	private String medida3;
	
	//Constructor
	public BroadcastMessage() {
	}
	
	public BroadcastMessage(int id, String nameParam1, String param1, String medida1, String nameParam2, String param2,
			String medida2, String nameParam3, String param3, String medida3) {
		super();
		this.id = id;
		this.nameParam1 = nameParam1;
		this.param1 = param1;
		this.medida1 = medida1;
		this.nameParam2 = nameParam2;
		this.param2 = param2;
		this.medida2 = medida2;
		this.nameParam3 = nameParam3;
		this.param3 = param3;
		this.medida3 = medida3;
	}

	//Getters & Setters
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getNameParam1() {
		return nameParam1;
	}

	public String getParam1() {
		return param1;
	}

	public String getNameParam2() {
		return nameParam2;
	}

	public String getParam2() {
		return param2;
	}

	public String getNameParam3() {
		return nameParam3;
	}

	public String getParam3() {
		return param3;
	}
	
	public String getMedida1() {
		return medida1;
	}

	public String getMedida2() {
		return medida2;
	}

	public String getMedida3() {
		return medida3;
	}

	public void setParam1(String p1) {
		this.param1 = p1;
	}
	
	public void setParam2(String p2) {
		this.param2 = p2;
	}
	
	public void setParam3(String p3) {
		this.param3 = p3;
	}
	
	//Funcionalidad
	public BroadcastMessage deserialize(String msg) {
		if(msg.startsWith("J")) return deserializeJSON(msg);
		if(msg.startsWith("X")) return deserializeXML(msg);
		return null;
	}
	
	public String serializeXML() {
		return XML_HEADER + new XMLBroadcastMessageParser().serialize(this);
	}
	
	private BroadcastMessage deserializeXML(String xml) {
		return new XMLBroadcastMessageParser().deserialize(xml);
	}
	
	public String serializeJSON() {
		return JSON_HEADER+JSONParser.serialize(this);
	}
	
	private BroadcastMessage deserializeJSON(String json) {
		return JSONParser.deserialize(json, BroadcastMessage.class);
	}
	
	public String toString() {
		return  id+": "
				+nameParam1+"="+param1+" "+medida1+", "
				+ nameParam2+"="+param2+" "+medida2+", "
				+ nameParam3+"="+param3+" "+medida3;
	}	
}
