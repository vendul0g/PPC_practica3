package threadsCliente;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cliente.Cliente;
import cliente.HttpMethod;
import cliente.IndexHTML;
import httpMessages.*;
import messages.ControlMessage;
import messages.ControlMessageType;


public class HttpThreadManager extends Thread{
	//Variables globales
	private static boolean VERBOSE = false;
	private static final int BUFSIZE = 2048;
	public static final int TIMEOUT = 1000*10;
		
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	private boolean isOpen;
	private Cliente creator;
	private int port;
	
	//Constructor
	public HttpThreadManager(Cliente c, Socket s, int port) {
		this.port = port;
		if(VERBOSE)	System.out.println("\tConectado "+ s.getInetAddress()+":"+s.getPort());
		this.creator = c;
		this.s = s;
		//Establecemos un timeout
		try {
			this.s.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
			System.err.println("Error al establecer el timeout");
		}
		
		this.isOpen = true;
	}
	
	//Funcionalidad
	public void run() {
		//Declaracion de variables
		String message;
		String answer;
		
		//Inicializamos los buffers de lectura/escritura
		createBuffers();
		
		while(isOpen) {
			//Leemos la petición
			if( (message = getMessage()).equals(""))
				break;
			//Procesamos la petición para crear una respuesta		
			answer = proccesMessage(message);
			
			//Enviamos respuesta
			sendMessage(answer);
		}
		
		if(VERBOSE) System.out.println("\tCerramos conexión\n------------------------------------------");
		//Cerramos el socket
		closeSocket();
	}
	
	private String proccesMessage(String m) {
		boolean showLastEntry = false;
		
		//Comprobamos el formato de la petición
		if(!checkFormat(m)) {
			if(VERBOSE) System.out.println("\t\tBad Request");
			isOpen = false;
			return new HTTPResponse400().getMessage();
		}
		
		//Procesamos la petición
		String[] lines = m.split(HTTPMessage.RETCAR);
		String[] reqline = lines[0].split(" ");
		
		//Comprobamos el método
		if( HttpMethod.isMethod(reqline[0]) == null) {
			if(VERBOSE) System.out.println("\t\tMethod Not Allowed");
			isOpen = false;
			return new HTTPResponse405().getMessage();
		}
		
		//Comprobamos la version
		if(!HTTPMessage.isVersion(reqline[2])) {
			if(VERBOSE) System.out.println("\t\tVersion not supported");
			isOpen = false;
			return new HTTPResponse505().getMessage();
		}
		
		//Comprobamos el tipo de conexión
		this.isOpen = isConnectionOpen(lines);
		
		//Comprobamos URL
		String url = getURL(reqline[1]);
		if(VERBOSE) System.out.println("\t\tPetición correcta (200) --> "+url);
		
		//Procesamos según el comando 
		if(! url.equals("/index.html")) {
			ControlMessage c = ControlMessageType.getTypeURL(url);
			switch(c.getCommand()) {
			case SET_TIME_REFRESH:
				creator.sendSetRefreshMessage(c);
				break;
			
			case DISABLE:
			case ENABLE:
				creator.sendControlMessage(c);
				break;
				
			case CONTROL_MODE:
				creator.sendControlMessage(c);
				break;
				
			case BROADCAST_MODE:
				creator.sendSetModeMessage(c);
				break;
				
			case SHOW_LAST_ENTRY:
				showLastEntry = true;
				break;
			
			case INVALID:
			default:
				isOpen = false;
				return new HTTPResponse400().getMessage();
			}
		}
		
		//Siempre respondemos con el fichero index, sin importar el comando que ejecute
		HTTPResponse200 index = new HTTPResponse200();
		IndexHTML file = new IndexHTML();
		if(showLastEntry) {
			file.addEntry(creator.getLastEntry().toString());
			showLastEntry = false;
		}
		String html = file.getHTML(this.port);
		index.setContentLenght(html.length());
		index.setCuerpo(html);
		return index.getMessage();
	}
	
	private boolean isConnectionOpen(String[] lines) {
		//Recorremos las cabeceras buscando "Connection"
		for(int i=1; i<lines.length; i++) {
			if(lines[i].split(":")[0].equals("Connection")) {
				if(lines[i].split(":")[1].equals(" keep-alive")) {
					return true;
				}else {
					return false;
				}
			}
		}
		return false;
	}
	
	private void createBuffers() {
		try {
			istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(IOException e) {
			System.err.println("Fallo al intentar crear sockets de lectura y escritura");
			return;
		}
	}
	
	private String getMessage() {
		StringBuilder message = new StringBuilder();
		int bytesRead;
		char[] buff = new char[BUFSIZE];
		
		try {
			//Leemos del socket
			do {
				try { //Preparamos timeout
					bytesRead = istream.read(buff);
				} catch (SocketTimeoutException e) {
					if(VERBOSE) System.err.println("\t\tTimeout Excedido");
					this.isOpen = false;
					return "";
				}
				
				if(bytesRead > 0) {
					message.append(buff, 0, bytesRead);
				}
			}while(bytesRead == BUFSIZE);

			return message.toString();
		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("\t\tLectura del socket vacía");
			return "";
		}
	}
	
	private synchronized void sendMessage(String m) {
		try {
			ostream.write(m);
			ostream.newLine();
			ostream.flush();
		} catch (IOException e) {
			System.err.println("Error en la escritura del socket");	
		}
	}
	
	private void closeSocket(){
		try {
			this.s.close();
			istream.close();
			ostream.close();
		} catch (IOException e3) {
			System.err.println("Error cerrando los sockets I/O");
		}
	}
	
	private boolean checkFormat(String m) {
		Pattern pattern = Pattern.compile(HTTPRequestMessage.HTTP_REQ_REGEX);
		Matcher matcher = pattern.matcher(m);
		if(!matcher.find()) {
			return false;
		}
		return true;
	}
	
	private String getURL(String url) {
		if(url.equals("/")) {
			return "/index.html";
		}
		return url;
	}
}
