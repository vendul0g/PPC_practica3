package estadistico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	//Atributos
    private PrintWriter out;

    //Constructor
    public Logger(String filename) {
        try {
            // Set up the PrintWriter to append to the file
            out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        } catch (IOException e) {
            System.err.println("Error abriendo el fichero "+filename+" ¿Existe?s");
        }
    }

    //Funcionalidad
    public void log(String message) {
        if (out != null) {
            out.println(message+"\n---------------------------------------------------------");
            out.flush(); // Ensure data is written to the file
        }
    }

    public void close() {
        if (out != null) {
            out.close();
        }
    }
}
