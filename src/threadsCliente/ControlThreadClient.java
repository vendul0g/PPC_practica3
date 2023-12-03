package threadsCliente;

import java.net.*;
import java.util.Scanner;
import cliente.Cliente;
import messages.*;
import java.io.*;

public class ControlThreadClient extends Thread{
	//Atributos
	private DatagramSocket socket;
	private Cliente creator;
	private Scanner scanner;
	private int serializationMode;
	
	//Constructor
	public ControlThreadClient(Cliente c) {
		this.creator = c;
		scanner = new Scanner(System.in);
		this.socket = createSocket();
		
		this.serializationMode = Message.MODE_XML; //Por defecto en XML
	}
	
	//Getters & Setters
	public int getSerializationMode() {
		return this.serializationMode;
	}
	
	public void setSerializationMode(int m) {
		if(m == Message.MODE_XML || m == Message.MODE_JSON) {
			this.serializationMode = m;
		}
	}
	
	//Funcionalidad
	public void run() {
		String inst;
		
		while(true) {
			//Preparamos la interfaz
			printPrompt();
			//Leemos la entrada del usuario
			inst = scanner.nextLine();
			
			//Procesamos lo que quiere el usuario
			ControlMessage c = ControlMessageType.getType(inst);
			
			//Comprobamos si el comando es inválido
			if(c.getCommand() == ControlMessageType.INVALID) {
				System.out.println("Comando incorrecto");
				printHelp();
				continue;
			}
			
			//Controlamos si es un comando de ayuda
			if(c.getCommand() == ControlMessageType.HELP) {
				printHelp();
				continue;
			}
			
			//Controlamos si se quiere cambiar el modo verbose
			if(c.getCommand() == ControlMessageType.VERBOSE) {
				this.creator.setVerbose(true);
				continue;
			}
			
			//Controlamos si se quiere cambiar el modo verbose
			if(c.getCommand() == ControlMessageType.NOT_VERBOSE) {
				this.creator.setVerbose(false);
				continue;
			}
			
			//Comprobamos si se quiere cambiar el modo de serialización de los mensajes de control
			if(c.getCommand() == ControlMessageType.CONTROL_MODE) {
				this.serializationMode = ((SetModeMessage) c).getMode();	
				continue;
			}
			
			//Si se quiere ver la estadística
			if(c.getCommand() == ControlMessageType.GET_STATISTICS) {
				System.out.println(this.creator.getStatistic());
				continue;
			}
				
			//Caso de mensajes con envío 
			if(!checkAddress(c)) {
				continue;
			}
			//Se quiere cambiar el tiempo de refresco
			if( c.getCommand() == ControlMessageType.SET_TIME_REFRESH) {
				sendSetRefreshMessage(c);
				continue;
			}
			
			//Comprobamos si se quiere cambiar el modo de serialización de los mensajes broadcast
			else if( c.getCommand() == ControlMessageType.BROADCAST_MODE) {
				sendSetModeMessage(c);		
				continue;
			}
			
			//Comprobamos si se quiere cambiar la unidad de medida
			else if( c.getCommand() == ControlMessageType.CHANGE_UNIT) {
				sendChangeUnitMessage(c);
				continue;
			}
			
			//Comprobamos si se quiere habilitar/deshabilitar el envio de datos de un servidor
			else if( c.getCommand() == ControlMessageType.DISABLE
					|| c.getCommand() == ControlMessageType.ENABLE) {
				sendControlMessage(c);
				continue;
			}
			
			System.out.println("Error: comando inválido");
			printHelp();			
		}
		//Cerramos el socket
//		closeSocket(socket);
	}
	
	public void sendSetRefreshMessage(ControlMessage c) {
		byte[] buf = ((SetRefreshMessage)c).serialize(getSerializationMode());
		//Manadmos el paquete
		sendMessage(c.getIdServer(), buf);
	}
	
	public void sendSetModeMessage(ControlMessage c) {
		byte[] buf = ((SetModeMessage) c).serialize(getSerializationMode());
		//Manadmos el paquete
		sendMessage(c.getIdServer(), buf);
	}
	
	public void sendChangeUnitMessage(ControlMessage c) {
		byte[] buf = ((SetModeMessage) c).serialize(getSerializationMode());
		//Manadmos el paquete
		sendMessage(c.getIdServer(), buf);
	}
	
	public void sendControlMessage(ControlMessage c) {
		byte[] buf = c.serialize(getSerializationMode());
		//Mandamos el paquete
		sendMessage(c.getIdServer(), buf);
	}
	
	private boolean checkAddress(ControlMessage cm) {
		if(creator.getAddress(cm.getIdServer()) == null) {
			System.out.println("Servidor inexistente");
			printServersRunning();
			return false;
		}
		return true;
	}
	
	public void printPrompt() {
		System.out.print("\nCliente:$ ");
	}
	
	public void printHelp() {
		System.out.println("USAGE: <command> [id_server] [options] \n"
				+ " setrefresh <id_server> <time> : Establece el tiempo de refresco de un servidor\n"
				+ " controlmode <xml/json> : Establece el tipo de serialización para los mensajes de control\n"
				+ " broadcastmode <id_server> <xml/json> : Establece el tipo de serialización para los mensajes broadcast\n"
				+ " changeunit <id_server> <celsius/farenheit> : Cambia la unidad de temperatura\n"
				+ " disable <id_server> : Deshabilita el envío de datos del servidor con id_server\n"
				+ " enable <id_server> : Habilita el envío de datos del servidor con id_server\n"
				+ " statistic : Muestra las estadísticas de valores ofrecidas por los servidores\n"
				+ " verbose : Muestra los mensajes que van enviando los servidores\n"
				+ " notverbose : Deja de mostrar los mensajes que van enviando los servidores\n"
				+ " help : Muestra este mensaje");
		printServersRunning();
	}
	
	public void printServersRunning() { 
		System.out.println("Servers running: "+creator.getServersRunning());
	}
	
	public void sendMessage(int port, byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length, creator.getAddress(port), port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en el envío de mensaje mensaje de control");
		}
	}
	
	public String receiveMessage(DatagramPacket packet) {
		try {
			this.socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en la lectura de mensaje de control");
			return "";
		}
	}
	
	public DatagramSocket createSocket() {
		try {
			DatagramSocket socket = new DatagramSocket();
			return socket;
		} catch (SocketException e) {
			e.printStackTrace();
			System.err.println("Error creando el socket del hilo control del cliente");
			return null;
		}
	}
	
	public void closeSocket() {
		this.socket.close();
	}
}
