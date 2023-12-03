package cliente;

import java.io.*;
import java.net.*;

import threadsCliente.HttpThreadManager;

public class HttpServer extends Thread{
	//Constantes
	public static final int HTTP_PORT = 8080;

	//Atributos
	ServerSocket s;
	Socket cliente; 
	Cliente creator;
	
	//Constructor
	public HttpServer(Cliente c, int port) {
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
		System.out.println("Abrimos el servidor HTTP");
		while(true) {
			try {
				cliente = s.accept();
				new HttpThreadManager(creator, cliente).start();
			}catch(IOException e) {
				System.err.println("Error en la creación del hilo");
			}
		}
	}
	
	//Main
//	public static void main(String[] args) {
//        //Servidor HTTP
//        HttpServer httpServer = new HttpServer(HttpServer.HTTP_PORT);
//		httpServer.run();
//    }
}

