package estadistico;

import messages.BroadcastMessage;
import servidores.*;

public class Estadistico {
	//Atributos
	private int n1;
	private int n2;
	private int n3;
	
	private int tempMedia;
	private int humedadMedia;
	private double presAtmosMedia;
	private int conCO2Media;
	private double conO3Media;
	private double conPartSuspMedia;
	private double lluviaMedia;
	private double velVientoMedia;
	private int radiacionMedia;
	
	private String mT;
	private String mH;
	private String mPA;
	private String mCO2;
	private String mO3;
	private String mCPS;
	private String mL;
	private String mV;
	private String mR;	
	
	//Constructor
	public Estadistico() {
		this.n1 = 1;
		this.n2 = 1;
		this.n3 = 1;
		
		this.tempMedia = 0;
		this.humedadMedia = 0;
		this.presAtmosMedia = 0;
		this.conCO2Media = 0;
		this.conO3Media = 0;
		this.conPartSuspMedia = 0;
		this.lluviaMedia = 0;
		this.velVientoMedia = 0;
		this.radiacionMedia = 0;
	}
	
	//Getters & Setters
	public int getTempMedia() {
		return tempMedia/n1;
	}

	private void setTempMedia(String tempMedia) {
		this.tempMedia += Integer.parseInt(tempMedia);
	}

	public int getHumedadMedia() {
		return humedadMedia/n1;
	}

	private void setHumedadMedia(String humedadMedia) {
		this.humedadMedia += Integer.parseInt(humedadMedia);
	}

	public double getPresAtmosMedia() {
		return presAtmosMedia/n1;
	}

	private void setPresAtmosMedia(String presAtmosMedia) {
		this.presAtmosMedia += Double.parseDouble(presAtmosMedia);
	}

	public int getConCO2Media() {
		return conCO2Media/n2;
	}

	private void setConCO2Media(String conCO2Media) {
		this.conCO2Media += Integer.parseInt(conCO2Media);
	}

	public double getConO3Media() {
		return conO3Media/n2;
	}

	private void setConO3Media(String conO3Media) {
		this.conO3Media += Double.parseDouble(conO3Media);
	}

	public double getConPartSuspMedia() {
		return conPartSuspMedia/n2;
	}

	private void setConPartSuspMedia(String conPartSuspMedia) {
		this.conPartSuspMedia += Double.parseDouble(conPartSuspMedia);
	}

	public double getLluviaMedia() {
		return lluviaMedia/n3;
	}

	private void setLluviaMedia(String lluviaMedia) {
		if( Boolean.parseBoolean(lluviaMedia) == true)
			this.lluviaMedia++;
	}

	public double getVelVientoMedia() {
		return velVientoMedia/n3;
	}

	private void setVelVientoMedia(String velVientoMedia) {
		this.velVientoMedia += Double.parseDouble(velVientoMedia);
	}

	public int getRadiacionMedia() {
		return radiacionMedia/n3;
	}

	private void setRadiacionMedia(String radiacionMedia) {
		this.radiacionMedia += Integer.parseInt(radiacionMedia);
	}
	
	//Funcionalidad
	public void addEntry(BroadcastMessage msg) {
		addValue(msg.getNameParam1(), msg.getParam1());
		addValue(msg.getNameParam2(), msg.getParam2());
		addValue(msg.getNameParam3(), msg.getParam3());
		
		switch(msg.getId()) {
		case 2001:
			n1++;
			this.mPA = msg.getMedida1();
			this.mT = msg.getMedida2();
			this.mH = msg.getMedida3();
			break;
			
		case 2002:
			n2++;
			this.mCO2 = msg.getMedida1();
			this.mO3 = msg.getMedida2();
			this.mCPS = msg.getMedida3();
			break;
		
		case 2003:
			n3++;
			this.mL = msg.getMedida1();
			this.mV = msg.getMedida2();
			this.mR = msg.getMedida3();
			break;
		}
	}

	private void addValue(String np, String v) {
		switch(np) {
		case ServidorClima.TEMP:
			setTempMedia(v);
			return;
		case ServidorClima.PRES_ATMOS:
			setPresAtmosMedia(v);
			return;
		case ServidorClima.HUMEDAD:
			setHumedadMedia(v);
			return;
		case ServidorCalidadAire.CON_CO2:
			setConCO2Media(v);
			return;
		case ServidorCalidadAire.CON_O3:
			setConO3Media(v);
			return;
		case ServidorCalidadAire.CON_PART_SUSP:
			setConPartSuspMedia(v);
			return;
		case ServidorMeteorologia.LLOVIENDO:
			setLluviaMedia(v);
			return;
		case ServidorMeteorologia.RADIACION:
			setRadiacionMedia(v);
			return;
		case ServidorMeteorologia.VEL_VIENTO:
			setVelVientoMedia(v);
			return;
		}
	}
	
	public String getStatistic() {
		return ServidorClima.TEMP					+": "+getTempMedia()		+" "+mT		+"\n"
				+ ServidorClima.PRES_ATMOS			+": "+getPresAtmosMedia()	+" "+mPA	+"\n"
				+ ServidorClima.HUMEDAD				+": "+getHumedadMedia()		+" "+mH		+"\n"
				+ ServidorCalidadAire.CON_CO2		+": "+getConCO2Media()		+" "+mCO2	+"\n"
				+ ServidorCalidadAire.CON_O3		+": "+getConO3Media()		+" "+mO3	+"\n"
				+ ServidorCalidadAire.CON_PART_SUSP	+": "+getConPartSuspMedia()	+" "+mCPS	+"\n"
				+ ServidorMeteorologia.LLOVIENDO	+": "+getLluviaMedia()		+" "+mL		+"\n"
				+ ServidorMeteorologia.RADIACION	+": "+getRadiacionMedia()	+" "+mR		+"\n"
				+ ServidorMeteorologia.VEL_VIENTO	+": "+getVelVientoMedia()	+" "+mV		+"\n";
	}
}
