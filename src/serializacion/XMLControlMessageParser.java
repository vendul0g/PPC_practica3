package serializacion;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import messages.ControlMessage;
import messages.ControlMessageType;

public class XMLControlMessageParser {
	//Atributos
	protected ControlMessage cm;
	protected Document doc;
	protected Element rootElement;
	
	//Constructor
	public XMLControlMessageParser() {
	
	}
	
	public XMLControlMessageParser(ControlMessage cm) {
		this.cm = cm;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error inicializando la serializacion XML");
			return;
		}
		this.doc = dBuilder.newDocument();
		
		//Elemento raíz
		this.rootElement = doc.createElement("ControlMessage");
		this.doc.appendChild(rootElement);
		
		//Añadimos el comando
		Element command = doc.createElement("Command");
		command.appendChild(doc.createTextNode(cm.getCommand().toString()));
		this.rootElement.appendChild(command);
		
		//Añadimos el id del servidor
		Element dstPort = doc.createElement("serverID");
		dstPort.appendChild(doc.createTextNode(String.valueOf(cm.getIdServer())));
		this.rootElement.appendChild(dstPort);
	}
	
	//Funcionalidad
	public String serialize() {
		// Convert to string
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error serializando XML");
			return null;
		}
	}
	
	public ControlMessage deserialize(String xml) {
		xml = xml.substring(1, xml.length());
		if(!validateDocument(xml))
			return null;
		
		// Leemos el documento XML
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error extrayendo XML");
			return null;
		}
		
		Document doc;
		try {
			doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error parseando XML");
			return null;
		}
		doc.getDocumentElement().normalize();

		// Extract root
		Element root = doc.getDocumentElement();
		
		//Sacamos los demás parámetros
		String command = root.getElementsByTagName("Command").item(0).getTextContent();
		int idServer = Integer.valueOf(root.getElementsByTagName("serverID").item(0).getTextContent());
		return new ControlMessage(ControlMessageType.getByName(command), idServer);
	}
	
	private boolean validateDocument(String xml) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/ControlMessage.xsd"))); // Load your XSD file
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			return false;
		}
		// El documento es válido
		return true;
	}
}
