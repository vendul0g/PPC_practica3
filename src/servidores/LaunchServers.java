package servidores;

public class LaunchServers {
	public static void main(String args[]) {
		//Inicializamos los hilos con servidores
		
		//Lanzamos el servidor Clima
		Thread sClima = new Thread() {
			public void run() {
				ServidorClima.main(args);
			}
		};
		
		//Lanzamos el servidor de control del aire
		Thread sAire = new Thread() {
			public void run() {
				ServidorCalidadAire.main(args);
			}
		};
		
		//Lanzamos el servidor meteo
		Thread sMeteo = new Thread() {
			public void run() {
				ServidorMeteorologia.main(args);
			}
		};
		
		//Lanzamos
		sClima.start();
		sMeteo.start();
		sAire.start();
	}
}
