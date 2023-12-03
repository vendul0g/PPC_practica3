package threadsCliente;

import java.net.*;

import cliente.Cliente;
import estadistico.Logger;
import messages.BroadcastMessage;

import java.io.*;

public class ListenerThread extends Thread{
	//Constantes
	private static final String LOGFILE = "/home/alvaro/Documents/PPC/Practica2_PPC/src/estadistico/broadcastLog.txt";
	
	//Atributos
	private Cliente creator;
	private DatagramSocket s;
	private Logger logger;
	
	//Constructor
	public ListenerThread(Cliente c, DatagramSocket s) {
		this.creator = c;
		this.s = s;
		this.logger = new Logger(LOGFILE);
	}
	
	//Funcionalidad
	public void run() {
		byte[] buf = new byte[1024];
		DatagramPacket packet;
		String msg;
		
		while(true) { //Vamos escuchando los mensajes del servidor
			packet= new DatagramPacket(buf, buf.length);
			
			//Recibimos el mensaje y guardamos en fichero
			msg = recieveMessage(packet, buf);
			logger.log(msg);
			
			//Anotamos el puerto de envío
			creator.addServer(packet.getPort(), packet.getAddress());
			
			//Mandamos a procesar el mensaje 
			BroadcastMessage bm = new BroadcastMessage().deserialize(msg);
			if(bm != null) {
				if(creator.isVerbose()) System.out.println(bm.toString());
				this.creator.addEntry(bm);
			}
		}
	}
	
	public String recieveMessage(DatagramPacket packet, byte[] buf) {
		String msg;
		try {
			//Recibimos el mensaje
			s.receive(packet);
			msg = new String(packet.getData(), 0, packet.getLength());
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en el procesamiento del mensaje del servidor");
			return "";
		}
	}
	
	public void close() {
		s.close();
	}
}
