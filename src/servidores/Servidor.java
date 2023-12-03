package servidores;

import java.io.IOException;
import java.net.*;
import java.util.Random;

import addressCalculator.AddressCalculator;
import messages.Message;
import threadsServidor.*;

public abstract class Servidor {
	//Constantes
	public static final int BROADCAST_PORT = 2048;
	private static final int DEFAULT_REFRESH = 1*1000; // 1 seg
	
	//Atributos
	protected String id;
	private int timeRefresh;
	private DatagramSocket serverSocket;
	protected Random r;
	private SenderThread senderT;
	private ControlThreadServer controller;
	private InetAddress broadcastAddr;
	private int modeBroadcast;
	private boolean deshabilitado;
	
	//Constructor
	public Servidor(int id, int port) {
		createSocket(port);
		broadcastAddr = AddressCalculator.getBroadcastAddress();
		
		this.senderT = new SenderThread(this, getSocket());
		this.controller = new ControlThreadServer(this, getSocket());
		
		this.timeRefresh = DEFAULT_REFRESH;
		this.r = new Random();
		
		this.modeBroadcast = Message.MODE_XML;
		this.deshabilitado = false;
	}
	
	//Getters & Setters
	protected DatagramSocket getSocket() {
		return this.serverSocket;
	}
	
	protected Random getRandom() {
		return this.r;
	}
	
	public String getID() {
		return id;
	}
	
	public int getTimeRefresh() {
		return timeRefresh;
	}
	
	public void setTimeRefresh(int n) {
		this.timeRefresh = n;
	}
	
	public InetAddress getSendAddr() {
		return this.broadcastAddr;
	}
	
	public int getMode() {
		return this.modeBroadcast;
	}
	
	public boolean setMode(int m) {
		if(m == Message.MODE_XML || m == Message.MODE_JSON) {
			this.modeBroadcast = m;
			return true;
		}
		return false;
	}
	
	public boolean isDeshabilitado() {
		return this.deshabilitado;
	}
	
	public void setDeshabilitado(boolean d) {
		this.deshabilitado = d;
	}
	
	public abstract String getParameters();
	
	//Funcionalidad
	public void run() {
		senderT.start();
		controller.start();
	}
	
	public void createSocket(int port) {
		try {
			serverSocket = new DatagramSocket(port);
		}catch(IOException e) {
			e.printStackTrace();
			System.err.println("["+id+"]: Error con binding de servidor");
		}
	}
}
