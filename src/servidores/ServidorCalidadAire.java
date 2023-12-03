package servidores;

import messages.BroadcastMessage;
import messages.Message;

public class ServidorCalidadAire extends Servidor{
	//Constantes
	public static final int CONTROL_PORT = 2002;
	public static final int ID = CONTROL_PORT;
	public static final String CON_CO2 = "ConcentracionCO2";
	public static final String CON_O3 = "ConcentracionO3";
	public static final String CON_PART_SUSP = "ConcentracionParticulasSuspension";
	
	//Atributos
	private int conCO2; //[350,450] ppm
	private String medidaCO;
	private double conO3; //[0,1] ppm
	private String medidaO3;
	private double conPartSusp; //[0,40] microgramos/m3
	private String medidaCP;
	
	
	//Constructor
	public ServidorCalidadAire() {
		super(ID, CONTROL_PORT);
		
		this.medidaCO = "ppm";
		this.medidaO3 = "ppm";
		this.medidaCP = "microgramos/m3";
	}
	
	//Getters & Setters
	public int getConCO2() {
		this.conCO2 = this.r.nextInt(250,451);
		return conCO2;
	}

	public double getConO3() {
		this.conO3 = Math.round(this.r.nextDouble()* 100.0) / 100.0;
		return conO3;
	}

	public double getConPartSusp() {
		this.conPartSusp = Math.round(this.r.nextDouble(41)* 100.0) / 100.0;
		return conPartSusp;
	}
	
	public String getMedidaCO() {
		return medidaCO;
	}

	public String getMedidaO3() {
		return medidaO3;
	}

	public String getMedidaCP() {
		return medidaCP;
	}
	
	public String getParameters() {
		BroadcastMessage bm = new BroadcastMessage(ID,
				CON_CO2, String.valueOf(getConCO2()), getMedidaCO(),
				CON_O3, String.valueOf(getConO3()), getMedidaO3(),
				CON_PART_SUSP, String.valueOf(getConPartSusp()), getMedidaCP());
		return getMode() == Message.MODE_XML ? bm.serializeXML() : bm.serializeJSON();
	}
	
	public String getID() {
		return id;
	}
	
	//Funcionalidad
	
	//Main
	public static void main(String args[]) {
		//Creamos los servidores
		Servidor sAire = new ServidorCalidadAire();
		//Ejecutamos
		sAire.run();
	}
}
