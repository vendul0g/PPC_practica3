package cliente;

import java.io.*;
import java.net.*;

import threadsCliente.HttpThreadManager;

public class HttpServer extends Thread{
	//Constantes
	public static final int HTTP_PORT = 8080;

	//Atributos
	private ServerSocket s;
	private Socket cliente; 
	private Cliente creator;
	private int port;
	
	//Constructor
	public HttpServer(Cliente c, int port) {
		this.port = port;
		this.creator = c;
		try {
			s = new ServerSocket(port);
			System.out.println("Listening on port "+port);
		} catch (IOException e) {
			System.err.println("Error al asociar el servidor con el puerto "+port);
		}
	}
	
	//Funcionalidad
	public void run() {
		if(s == null) {
			System.err.println("No se pueden tener 2 clientes con el servicio de control HTTP abierto");
			return;
		}
		System.out.println("Abrimos el servidor HTTP");
		while(true) {
			try {
				cliente = s.accept();
				new HttpThreadManager(creator, cliente, port).start();
			}catch(IOException e) {
				System.err.println("Error en la creación del hilo");
			}
		}
	}
}

