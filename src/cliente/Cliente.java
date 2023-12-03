package cliente;

import java.net.*;
import java.util.Map;
import java.util.TreeMap;

import estadistico.Estadistico;
import messages.BroadcastMessage;
import messages.ControlMessage;
import messages.SetModeMessage;
import messages.SetRefreshMessage;
import servidores.Servidor;
import threadsCliente.ControlThreadClient;
import threadsCliente.ListenerThread;

public class Cliente {	
	//Atributos
	private DatagramSocket socketListener;
	private Map<Integer, InetAddress> portServerMapper;
	private boolean verbose;
	private Estadistico e;
	private ListenerThread l;
	private ControlThreadClient c;
	private HttpServer s;
	
	//Constructor
	public Cliente() {
		//Inicializamos valores
		crearSocketListener();
		this.portServerMapper = new TreeMap<Integer, InetAddress>(); 
		this.verbose = false;
		this.e = new Estadistico();
		
		//Inicializamos los hilos
		this.l = new ListenerThread(this, socketListener);
		this.c= new ControlThreadClient(this);
		this.s = new HttpServer(this, HttpServer.HTTP_PORT);
	}
	
	//Getters & Setters
	public void addServer(int port, InetAddress addr) {
		if(portServerMapper.get(port) != null)
			return;
		portServerMapper.put(port, addr);
	}
	
	public InetAddress getAddress(int port) {
	    return this.portServerMapper.get(port);
	}

	
	public String getServersRunning() {
	    if(portServerMapper.isEmpty()) 
	    	return null;
	    String s = "";
	    for(int i : portServerMapper.keySet()) {
	    	s += "\n - [id]="+i+" - "+portServerMapper.get(i);
	    }
	    return s;
	}
	
	public void setVerbose(boolean v) {
		this.verbose = v;
	}

	public boolean isVerbose() {
		return this.verbose;
	}
	
	public String getStatistic() {
		return this.e.getStatistic();
	}
	
	public void addEntry(BroadcastMessage bm) {
		this.e.addEntry(bm);
	}
	
	//Funcionalidad
	public void run() {
		//Invocamos el hilo de recepción de mensajes broadcast
		l.start();
		
		//Invocamos al servidor HTTP
		s.start();
		
		//Invocamos al hilo de mensajes de control
		c.start();
		
//		closeSockets(); No se cierran 
	}
	
	public void sendSetRefreshMessage(ControlMessage c) {
		this.c.sendSetRefreshMessage(c);
	}
	
	public void sendSetModeMessage(ControlMessage c) {
		this.c.sendSetModeMessage(c);
	}
	
	public void sendChangeUnitMessage(ControlMessage c) {
		this.c.sendChangeUnitMessage(c);
	}
	
	public void sendControlMessage(ControlMessage c) {
		this.c.sendControlMessage(c);
	}
	
	public void crearSocketListener() {
	    try {
	        this.socketListener = new DatagramSocket(null); 
	        this.socketListener.setReuseAddress(true); // Habilitamos SO_REUSEADDR
	        this.socketListener.bind(new InetSocketAddress(Servidor.BROADCAST_PORT)); // Bind the socket to the port
	    } catch (SocketException e) {
	        e.printStackTrace();
	        System.err.println("Error creando el socket de escucha del cliente");
	    }
	}

	
	public void closeSockets() {
		this.socketListener.close();
	}
	
	//Main
	public static void main(String args[]) {
		Cliente c = new Cliente();
		c.run();
	}
}