package threadsServidor;

import java.io.*;
import java.net.*;

import servidores.Servidor;

public class SenderThread extends Thread{
	//Constantes
	
	//Atributos
	private Servidor creator;
	private DatagramSocket s;
	InetAddress dstAddress;
	
	//Constructor
	public SenderThread(Servidor serverCreator, DatagramSocket s) {
		this.creator = serverCreator;
		this.s = s;
		this.dstAddress = creator.getSendAddr();
	}
	
	//Funcionalidad
	public void run() {
		while(true) {
			if(!this.creator.isDeshabilitado()) {
				//Cogemos el mensaje del servidor para enviarlo a la red
				byte[] buff = createMessage();
				
				//Enviamos el mensaje
				sendMessage(buff);
			}
			
			//Esperamos el tiempo de refresco hasta mandar el siguiente mensaje
			sleep();
		}
	}	
	
	public byte[] createMessage() {
		return creator.getParameters().getBytes();
	}
	
	public void sendMessage(byte[] buff) {
		DatagramPacket packet = new DatagramPacket(buff, buff.length, dstAddress, Servidor.BROADCAST_PORT);
		try {
			s.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("["+this.creator.getID()+"]: Error en el envío del paquete" );
			return;
		}
	}
	
	public void sleep() {
		try {
			Thread.sleep(creator.getTimeRefresh());
		} catch (InterruptedException e) {
			System.err.println("["+this.creator.getID()+"]: Error mientras el hilo dormía");
		}
	}
}
