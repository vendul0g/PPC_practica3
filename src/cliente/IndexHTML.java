package cliente;

public class IndexHTML {
	//Atributos
	private String lastEntry;
	
	//Constructor
	public IndexHTML() {
		
	}
	
	//Funcionalidad
	public String getHTML(int port) {
		String protocol = port == 4433 ? "https" : "http";
		String s= "<!DOCTYPE html>\n"
				+ "<html lang=\"es\">\n"
				+ "<head>\n"
				+ "  <meta charset=\"UTF-8\">\n"
				+ "  <title>Practica 3 PPC - Álvaro</title>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "  <h1>Control Servidores online</h1>\n"
				+ "  <div>\n"
				+ "    <p>Funcionalidad de control de servidores</p>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2001?disable\">Deshabilitar Servidor 1</a><br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2001?enable\">Habilitar Servidor 1</a><br>\n"
				+ "<br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2002?setrefresh=1\">Tiempo de refresco Servidor 2: 1 segundo</a><br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2002?setrefresh=5\">Tiempo de refresco Servidor 2: 5 segundos</a><br>\n"
				+ "<br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2003?broadcastmode=json\">Modo de envío broadcast Servidor 3: JSON</a><br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/2003?broadcastmode=xml\">Modo de envío broadcast Servidor 3: XML</a><br>\n"
				+ "<br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/0?controlmode=json\">Modo de envío mensajes de control: JSON</a><br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/0?controlmode=xml\">Modo de envío mensajes de control: XML</a><br>\n"
				+ "<br>\n"
				+ "    <a href=\""+protocol+"://serverppc.com:"+port+"/0?showlastentry\">Mostar el últiimo parámetro obtenido</a></br>\n"
				+ "  </div>\n\n";
		if (lastEntry != null) {
			s +=  "  <p>"+lastEntry+"</p>\n";
			lastEntry = null;
		}
				s+= "</body>\n"
				 +  "</html>  \n\n"
				 + "\n\n  ";
		return s;
	}
	
	public void addEntry(String s) {
		this.lastEntry = s;
	}
}
