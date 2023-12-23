package threadsCliente;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cliente.Cliente;
import messages.BroadcastMessage;
import serializacion.JSONParser;

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
    private static final String SMTP_TYPE = "smtp";
    private static final String CREDENTIALS_FILE = "/resources/MailThreadResources.txt";
    
    // Atributos
    private Cliente creator;
    private Properties popProps;
    private Properties smtpProps;
//    private Store store;
    private String user;
    private String pass;
    
    // Constructor
    public MailThread(Cliente c) {
    	this.creator = c;
    	
    	//Cargamos los credenciales de acceso al correo
        loadCredentials();
        
        //Cargamos las propiedades de POP
        this.popProps = new Properties();
    	popProps.put("mail.pop3.host", HOST_POP);
    	popProps.put("mail.pop3.port", "995");
    	popProps.put("mail.pop3.starttls.enable", "true");
    	
    	//Cargamos las propiedades de SMTP
    	this.smtpProps = new Properties();
    	smtpProps.put("mail.smtp.auth", "true");
    	smtpProps.put("mail.smtp.starttls.enable", "true");
    	smtpProps.put("mail.smtp.host", HOST_SMTP);
    	smtpProps.put("mail.smtp.port", "587");
    }
    
    // Funcionalidad
    public void sendEmail(Message m, String serializado, String deserializado) throws MessagingException {
        Session session = Session.getInstance(this.smtpProps, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        MimeMessage replyMessage = new MimeMessage(session);
        replyMessage = (MimeMessage) m.reply(false);
        replyMessage.setFrom(new InternetAddress(this.user));

        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        // Create the first part with p1
        MimeBodyPart part1 = new MimeBodyPart();
        part1.setText("Serializado:\n"+serializado);
        multipart.addBodyPart(part1);

        // Create the second part with p2
        MimeBodyPart part2 = new MimeBodyPart();
        part2.setText("\nDeserializado:\n"+deserializado);
        multipart.addBodyPart(part2);

        // Set the complete multipart as the message content
        replyMessage.setContent(multipart);
        
        replyMessage.setReplyTo(m.getReplyTo());

        Transport transport = session.getTransport(SMTP_TYPE);
        transport.connect(HOST_SMTP, this.user, this.pass);
        transport.sendMessage(replyMessage, replyMessage.getAllRecipients());
        transport.close();
    }
    
    private void processInbox(Message[] messages) throws MessagingException{
    	//Procesamos los mensajes que tengamos
    	for(Message m : messages) {
    		//Vemos quien lo envía
    		Address[] fromAddresses = m.getFrom();
    		String senderEmail = fromAddresses.length > 0 ? ((InternetAddress) fromAddresses[0]).getAddress() : null;
    		System.out.println("- "+senderEmail+": "+m.getSubject());
    		
    		//Vemos cual es la petición del mensaje
    		switch(m.getSubject()) {
    		case "/lastentry":
    			//Procesamos petición
    			BroadcastMessage lastEntry = creator.getLastEntry();
    			//Respondemos
    			sendEmail(m, JSONParser.serialize(lastEntry), lastEntry.toString());
    			break;
    		}

    		//Eliminamos el mensaje
    		m.setFlag(Flags.Flag.DELETED, true);
    	}
    	
    	//Volvemos a mostrar el prompt 
    	creator.printPrompt();
    }
    
    public void checkInbox() {
    	//Abrimos sesión
    	Session emailSession = Session.getDefaultInstance(popProps);
    	
    	try {
    		Store store = emailSession.getStore(POP_TYPE);
    		
    		//Conectamos
    		store.connect(HOST_POP, this.user, this.pass);
    		
    		//Descargamos los correos
    		Folder emailFolder = store.getFolder("INBOX");
    		emailFolder.open(Folder.READ_WRITE);
    		
    		//Procesamos los mensajes si existen
    		if (emailFolder.getUnreadMessageCount() > 0) {
    			System.out.println("\r[¡Alerta!]: Mensajes nuevos");
    			processInbox(emailFolder.getMessages());
    		}
    		
    		//Cerramos todo 
    		emailFolder.close(true);
    		store.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}

    public void run() {
    	while(true) {
	    	// Repasamos la bandeja de entrada para responder los correos necesarios
	    	checkInbox();
	    	
	    	// Esperamos 30 segundos
	    	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				
			}
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
