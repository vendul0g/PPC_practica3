package cliente;

public class IndexHTML {
	// TODO ver como traducir peticiones
	//Constructor
	public IndexHTML() {
		
	}
	
	//Funcionalidad
	public String getHTML() {
		return "<!DOCTYPE html>\n"
				+ "<html lang=\"es\">\n"
				+ "<head>\n"
				+ "  <meta charset=\"UTF-8\">\n"
				+ "  <title>Practica 3 PPC - Álvaro</title>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "  <h1>Control Servidores online</h1>\n"
				+ "  <div>\n"
				+ "    <p>Funcionalidad de control de servidores</p>\n"
				+ "    <a href=\"http://localhost:8080/execute?command=disableServer1\">Deshabilitar Servidor 1</a><br>"
				+ "    "
				+ "  </div>\n"
				+ "</body>\n"
				+ "</html>\n";
	}
}
