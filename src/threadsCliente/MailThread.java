package threadsCliente;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cliente.Cliente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class MailThread extends Thread{
	// Constantes
	private static final String HOST_POP = "pop.gmail.com";
	private static final String HOST_SMTP = "smtp.gmail.com";
    private static final String POP_TYPE = "pop3s";
    private static final String CREDENTIALS_FILE = "/resources/MailThreadResources.txt";
    
    // Atributos
    private Cliente creator;
    private Timer timer = new Timer();
    private Properties popProps;
    private Properties smtpProps;
    private Store store;
    private String user;
    private String pass;
    
    // Constructor
    public MailThread() {
        loadCredentials();
    }
    
    // Funcionalidad
    public void sendEmail() {
    	System.out.println("Envío mensaje de vuelta");
//    	smtpProps = new Properties();
//        
//        // Añadimos las propiedades de SMTP
//        smtpProps.put("mail.smtp.host", HOST_SMTP);
//        smtpProps.put("mail.smtp.port", "587");
//        smtpProps.put("mail.smtp.auth", "true");
//        smtpProps.put("mail.smtp.starttls.enable", "true");
//        
//        Session session = Session.getInstance(smtpProps, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(USERNAME, pass);
//            }
//        });
//        
//        MimeMessage message = new MimeMessage(session);
//        message.setFrom(new InternetAddress(from));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//        message.setSubject(subject);
//        message.setText(body);
//
//        Transport.send(message);
    }
    
    public void checkINBOX() throws MessagingException{
    	Folder emailFolder = null;
        Store store = null;
        try {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", Integer.toString(995));
            properties.put("mail.pop3.starttls.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server

            store = emailSession.getStore("pop3s");

            store.connect("pop.gmail.com", "ruserchat@gmail.com", "!Ch@tGPT");

            //create the folder object and open it

            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message foundMessage = null;
            Message[] messages = emailFolder.getMessages();
            for (final Message msg : messages) {
                final String subject = msg.getSubject();
            }
        }
        finally {
            if (emailFolder != null) {
                emailFolder.close(false);
            }
            if (store != null) {
                store.close();
            }
        }
/* Prueba 2 fallida
    	popProps = new Properties();
    	
    	//Añadimos las propiedades de POP3
    	popProps.put("mail.pop3.host", HOST_POP);
        popProps.put("mail.pop3.port", "995");
//        popProps.put("mail.pop3.ssl.trust", "*");
        popProps.put("mail.pop3.ssl.enable", "true");
*/
//    	Session emailSession = Session.getDefaultInstance(popProps);
/* Prueba número 2
        Session session = Session.getInstance(popProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        
        try {
            Store store = session.getStore("pop3s");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int messageCount = inbox.getMessageCount();
            System.out.println("Total number of emails in the inbox: " + messageCount);

            inbox.close();
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/        
//        try {
//        	this.store = emailSession.getStore(POP_TYPE);
//			store.connect(HOST_POP, this.user, this.pass);
//			
//			Folder emailFolder = store.getFolder("INBOX");
//			emailFolder.open(Folder.READ_ONLY); // TODO cambiar para eliminar cosas de la bandeja
//			
//			Message[] messages = emailFolder.getMessages();
//			
//			for (Message m : messages) {
//				// Comprobamos si el mensaje debe ser respondido
//				//TODO comprobación
//				// Respondemos
//			}
//			
//			emailFolder.close();
//			store.close();
//			
//		} catch (MessagingException e) {
//			e.printStackTrace();
//			System.err.println("Error conectando con el servidor POP3");
//		}
    }
    
    public void run() {
    	// Repasamos la bandeja de entrada para responder los correos necesarios
    	try {
			checkINBOX();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// Esperamos 30 segundos
    	try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			
		}
    }
    
    private void loadCredentials() {
    	String file = System.getProperty("user.dir")+CREDENTIALS_FILE;
    	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    	    this.user = reader.readLine();
    	    this.pass = reader.readLine();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

    }
}
