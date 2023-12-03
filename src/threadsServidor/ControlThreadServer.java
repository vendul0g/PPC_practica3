package threadsServidor;

import java.net.*;

import estadistico.Logger;
import messages.ControlMessage;
import messages.ControlMessageType;
import messages.Message;
import messages.SetModeMessage;
import messages.SetRefreshMessage;
import serializacion.JSONParser;
import serializacion.XMLControlMessageParser;
import serializacion.XMLSetModeMessageParser;
import serializacion.XMLSetRefreshParser;
import servidores.Servidor;
import servidores.ServidorClima;

import java.io.*;

public class ControlThreadServer extends Thread{
	//Constantes
	private static final String LOGFILE = "/home/alvaro/Documents/PPC/Practica2_PPC/src/estadistico/controlLog.txt";
	
	//Atributos
	private Servidor creator;
	private DatagramSocket socket;
	private Logger logger;
	
	//Constructor
	public ControlThreadServer(Servidor server, DatagramSocket s) {
		this.creator = server;
		this.socket = s;
		this.logger = new Logger(LOGFILE);
	}
	
	//Funcionalidad
	public void run() {
		byte[] buf = new byte[256];
		DatagramPacket packet;
		String msg;
		
		while(true) {
			ControlMessage cm;

			//Recibimos mensajes de control
			packet = new DatagramPacket(buf, buf.length);
			msg = receiveMessage(packet);
			logger.log(msg);
			
			//parseamos el mensaje y procesamos en consecuencia
			if(msg.startsWith("J")) { //JSON
				cm = JSONParser.deserialize(msg, ControlMessage.class);
				if(cm.getCommand() == ControlMessageType.DISABLE 
						|| cm.getCommand() == ControlMessageType.ENABLE)
					proccesMessage(cm);
				if(cm.getCommand() == ControlMessageType.SET_TIME_REFRESH) {
					cm = JSONParser.deserialize(msg, SetRefreshMessage.class); 
					proccesMessage(cm);
				}
				else if(cm.getCommand() == ControlMessageType.BROADCAST_MODE
						|| cm.getCommand() == ControlMessageType.CHANGE_UNIT) {
					cm = JSONParser.deserialize(msg, SetModeMessage.class);
					proccesMessage(cm);
				}
			} 
			else { //XML
				if( (cm = new XMLControlMessageParser().deserialize(msg)) != null) {
					//Disable or Enable
					proccesMessage(cm);
				}
				else if( (cm = new XMLSetRefreshParser().deserialize(msg)) != null) {
					//SetRefresh
					proccesMessage(cm);
				}
				else if( (cm = new XMLSetModeMessageParser().deserialize(msg)) != null) {
					//Set broadcast mode or Change unit
					proccesMessage(cm);
				}
			}
		}
	}
	
	public String receiveMessage(DatagramPacket packet) {
		try {
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Servidor: Error al leer mensaje de control");
			return "";
		}
	}
	
	public void sendMessage(DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Servidor: error al responder mensaje de control");
		}
	}
	
	public void proccesMessage(ControlMessage c) {
		switch(c.getCommand()) {
		case SET_TIME_REFRESH:
			this.creator.setTimeRefresh( ((SetRefreshMessage) c).getTime()*1000 );
			break;
		
		case BROADCAST_MODE:
			this.creator.setMode( ((SetModeMessage) c).getMode() );
			break;
			
		case CHANGE_UNIT:
			boolean f = ((SetModeMessage) c).getMode() == Message.MODE_FARENHEIT ? true : false;
			if (this.creator instanceof ServidorClima) {
				if(f) ((ServidorClima) this.creator).setMedidaT(ServidorClima.FARENHEIT);
				else  ((ServidorClima) this.creator).setMedidaT(ServidorClima.CELSIUS);
			}
			break;
			
		case DISABLE:
			this.creator.setDeshabilitado(true);
			break;
		
		case ENABLE:
			this.creator.setDeshabilitado(false);
			break;
			
		default:
			System.err.println("["+this.creator.getID()+"]: Mensaje de control fallido:\n"+c.toString());
			break;
		}
	}
}
