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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import messages.BroadcastMessage;
import servidores.ServidorCalidadAire;
import servidores.ServidorClima;
import servidores.ServidorMeteorologia;

public class XMLBroadcastMessageParser{

	
	public String serialize(BroadcastMessage bm) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error inicializando la serializacion XML");
			return null;
		}
		Document doc = dBuilder.newDocument();
		
		//Elemento raíz
		Element rootElement = doc.createElement("BroadcastMessage");
		doc.appendChild(rootElement);
		
		// Declaramos XSI
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		
		//Añadimos atributos: XSD
		Attr attr = doc.createAttribute("xsi:noNamespaceSchemaLocation");
		attr.setValue("BroadcastMessage.xsd");
		rootElement.setAttributeNode(attr);
		
		// Elemento id
		Element idE = doc.createElement("ID");
		idE.appendChild(doc.createTextNode(String.valueOf(bm.getId())));
		rootElement.appendChild(idE);
		
		//Parámetro 1
		Element param1 = doc.createElement(bm.getNameParam1());
		rootElement.appendChild(param1);
		
		//Valor parámetro 1
		Element inner11 = doc.createElement("Valor");
		inner11.appendChild(doc.createTextNode(bm.getParam1()));
		param1.appendChild(inner11);
		
		//Medida parámetro 1
		Element inner12 = doc.createElement("Medida");
		inner12.appendChild(doc.createTextNode(bm.getMedida1()));
		param1.appendChild(inner12);
	
		//Parámetro 2
		Element param2 = doc.createElement(bm.getNameParam2());
		rootElement.appendChild(param2);		

		//Valor parámetro 2
		Element inner21 = doc.createElement("Valor");
		inner21.appendChild(doc.createTextNode(bm.getParam2()));
		param2.appendChild(inner21);
		
		//Medida parámetro 2
		Element inner22 = doc.createElement("Medida");
		inner22.appendChild(doc.createTextNode(bm.getMedida2()));
		param2.appendChild(inner22);
		
		//Parámetro 3
		Element param3 = doc.createElement(bm.getNameParam3());
		rootElement.appendChild(param3);

		//Valor parámetro 3
		Element inner31 = doc.createElement("Valor");
		inner31.appendChild(doc.createTextNode(bm.getParam3()));
		param3.appendChild(inner31);
		
		//Medida parámetro 3
		Element inner32 = doc.createElement("Medida");
		inner32.appendChild(doc.createTextNode(bm.getMedida3()));
		param3.appendChild(inner32);

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
	
	
	public BroadcastMessage deserialize(String xml) {
		xml = xml.substring(1, xml.length());
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
		
		//Comprobamos el tipo de datos que estamos recibiendo y deserializamos en consecuencia
		if(root.getElementsByTagName(ServidorClima.PRES_ATMOS).item(0) != null) {
			return deserializeServidorClima(xml, root);
		}else if(root.getElementsByTagName(ServidorMeteorologia.LLOVIENDO).item(0) != null) {
			return deserializeServidorMeteorologia(xml, root);
		}else {
			return deserializeServidorCalidadAire(xml, root);
		}
	}
	
	private BroadcastMessage deserializeServidorClima(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorClima.xsd"))); 
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		// El documento es válido
		
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		
		Element aux = (Element) root.getElementsByTagName(ServidorClima.PRES_ATMOS).item(0);
		String presAtmos = aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mPA = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorClima.TEMP).item(0);
		String temp = aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mT = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorClima.HUMEDAD).item(0);
		String hum = aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mH = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		return new BroadcastMessage(id, 
				ServidorClima.PRES_ATMOS, presAtmos, mPA,
				ServidorClima.TEMP, temp, mT,
				ServidorClima.HUMEDAD, hum, mH);
	}
	
	private BroadcastMessage deserializeServidorMeteorologia(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorMeteorologia.xsd"))); 
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		
		Element aux = (Element) root.getElementsByTagName(ServidorMeteorologia.LLOVIENDO).item(0);
		String lloviendo= aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mL = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorMeteorologia.VEL_VIENTO).item(0);
		String velViento = aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mV = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorMeteorologia.RADIACION).item(0);
		String radiacion= aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mR = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		return new BroadcastMessage(id, 
				ServidorMeteorologia.LLOVIENDO, lloviendo, mL,
				ServidorMeteorologia.VEL_VIENTO, velViento, mV,
				ServidorMeteorologia.RADIACION, radiacion, mR);
	}
	
	private BroadcastMessage deserializeServidorCalidadAire(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorCalidadAire.xsd"))); 
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		
		Element aux = (Element) root.getElementsByTagName(ServidorCalidadAire.CON_CO2).item(0);
		String conCO2= aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mCO = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorCalidadAire.CON_O3).item(0);
		String conO3 = aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mO3 = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		aux = (Element) root.getElementsByTagName(ServidorCalidadAire.CON_PART_SUSP).item(0);
		String conPartSusp= aux.getElementsByTagName("Valor").item(0).getTextContent();
		String mCP = aux.getElementsByTagName("Medida").item(0).getTextContent();
		
		return new BroadcastMessage(id, 
				ServidorCalidadAire.CON_CO2, conCO2, mCO,
				ServidorCalidadAire.CON_O3, conO3, mO3,
				ServidorCalidadAire.CON_PART_SUSP, conPartSusp, mCP);
	}
}
