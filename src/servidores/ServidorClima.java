package servidores;

import messages.BroadcastMessage;
import messages.Message;

public class ServidorClima extends Servidor{
	//Constantes
	public static final int CONTROL_PORT = 2001;
	public static final int ID = CONTROL_PORT;
	public static final String PRES_ATMOS = "PresionAtmosferica";
	public static final String TEMP = "Temperatura";
	public static final String HUMEDAD = "Humedad";
	public static final String CELSIUS = "ºC";
	public static final String FARENHEIT = "ºF";
	
	//Atributos
	private double presionAtmosferica; //[950, 1020] hPa
	private String medidaPA;
	private int temperatura; // [-20, 40] ºC or F
	private String medidaT;
	private int humedad; // [0-100] %
	private String medidaH;
	private boolean farenheit;
	
	//Constructor
	public ServidorClima() {
		super(ID, CONTROL_PORT);
		farenheit = false;
		
		this.medidaPA = "hPa";
		this.medidaT = CELSIUS;
		this.medidaH = "%";
	}

	//Getters & Setters
	public double getPresionAtmosferica() {
		//generamos un double con 2 decimales
		this.presionAtmosferica = Math.round(this.r.nextDouble(950,1021)* 100.0) / 100.0;
		return presionAtmosferica;
	}

	public int getTemperatura(boolean farenheit) {
		this.temperatura = this.r.nextInt(15,35);
		return farenheit ? (temperatura* 9/5) + 32 : temperatura;
	}

	public int getHumedad() {
		this.humedad = this.r.nextInt(101);
		return humedad;
	}
	
	public boolean getFarenheit() {
		return this.farenheit;
	}
	
	public String getMedidaPA() {
		return medidaPA;
	}
	
	public String getMedidaH() {
		return medidaH;
	}

	public String getMedidaT() {
		return medidaT;
	}

	public void setMedidaT(String medidaT) { //Se puede cambiar desde el cliente
		this.medidaT = medidaT;
		this.farenheit = medidaT == FARENHEIT ? true : false;
	}

	public String getParameters() { 
		BroadcastMessage bm = new BroadcastMessage(ID, 
				PRES_ATMOS, String.valueOf(getPresionAtmosferica()), getMedidaPA(),
				TEMP, String.valueOf(getTemperatura(getFarenheit())), getMedidaT(),
				HUMEDAD, String.valueOf(getHumedad()), getMedidaH());
		return getMode() == Message.MODE_XML ? bm.serializeXML() : bm.serializeJSON();
	}
	
	public String getID() {
		return id;
	}

	//Funcionalidad
	
	//Main
	public static void main(String args[]) {
		//Creamos los servidores
		Servidor sClima = new ServidorClima();
		//Ejecutamos
		sClima.run();
	}
}
