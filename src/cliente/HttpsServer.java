package cliente;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import threadsCliente.HttpThreadManager;

import java.security.*;

public class HttpsServer extends Thread {
    // Constants
    private static final String SERVERCERT = "src/certs/serverppc.p12";
    private static final String PASSPHRASE = "alvaro";
    private static final String CACERT = "src/certs/cappc.p12";
    public static final int HTTPS_PORT = 4433;

    // Attributes
    private int port;
    private KeyStore serverKeystore;
    private KeyManagerFactory keyManagerFactory;
    private TrustManagerFactory trustManagerFactory;
    private SSLContext sslContext;
    private SSLServerSocket sss;
    private Cliente creator;  // Assuming Cliente is a class you have defined

    // Constructor
    public HttpsServer(Cliente creator, int port) {
        this.creator = creator;
        this.port = port;
        System.out.println("Listening on port " + port);

        // Load server and CA certificates
        loadServerCert();
        loadCACert();
        loadContext();
    }

    // Run method
    public void run() {
        if (sss == null) {
            System.err.println("SSLServerSocket not initialized.");
            return;
        }

        System.out.println("Abrimos el servidor HTTPS.");
        while (true) {
            try {
                Socket client = sss.accept();
                new HttpThreadManager(creator, client, port).start();
            } catch (IOException e) {
                System.err.println("Error in creating thread for client connection.");
            }
        }
    }

    // Load server certificate
    private void loadServerCert() {
        try {
            serverKeystore = KeyStore.getInstance("PKCS12");
            serverKeystore.load(new FileInputStream(SERVERCERT), PASSPHRASE.toCharArray());

            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(serverKeystore, PASSPHRASE.toCharArray());
        } catch (Exception e) {
            System.err.println("Error loading server certificate: " + e.getMessage());
        }
    }

    // Load CA certificate
    private void loadCACert() {
        try {
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(new FileInputStream(CACERT), PASSPHRASE.toCharArray());

            trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);
        } catch (Exception e) {
            System.err.println("Error loading CA certificate: " + e.getMessage());
        }
    }

    // Load SSL context
    private void loadContext() {
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
            sss = (SSLServerSocket) factory.createServerSocket(port);
            sss.setNeedClientAuth(true);
        } catch (Exception e) {
            System.err.println("Error creating SSL server socket: " + e.getMessage());
        }
    }
}
