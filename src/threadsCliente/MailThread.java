//package threadsCliente;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import cliente.Cliente;
//
//import java.util.Properties;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MailThread extends Thread{
//	// Constantes
//	private static final String HOST_POP = "pop.gmail.com";
//	private static final String HOST_SMTP = "smtp.gamail.com";
//    private static final String POP_TYPE = "pop3s";
//    private static final String USERNAME = "ruserchat@gmail.com";
//    
//    // Atributos
//    private Cliente creator;
//    private String pass;
//    private Timer timer = new Timer();
//    private Properties popProps;
//    private Properties smtpProps;
//    private Store store;
//    
//    // Constructor
//    public MailThread(String pass) {
//        this.pass = pass;
//    }
//    
//    // Funcionalidad
//    public void sendEmail() {
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
//    }
//    
//    public void checkINBOX() {
//    	popProps = new Properties();
//    	
//    	//Añadimos las propiedades de POP3
//    	popProps.put("mail.pop3.host", HOST_POP);
//        popProps.put("mail.pop3.port", "995");
//        popProps.put("mail.pop3.ssl.trust", "*");
//        popProps.put("mail.pop3.ssl.enable", "true");
//    	
//    	Session emailSession = Session.getDefaultInstance(popProps);
//        try {
//        	this.store = emailSession.getStore(POP_TYPE);
//			store.connect(HOST_POP, USERNAME, this.pass);
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
//				
//			}
//			
//			emailFolder.close();
//			store.close();
//			
//		} catch (MessagingException e) {
//			e.printStackTrace();
//			System.err.println("Error conectando con el servidor POP3");
//		}
//    }
//    
//    public void run() {
//    	// Repasamos la bandeja de entrada para responder los correos necesarios
//    	checkINBOX();
//    	
//    	// Esperamos 30 segundos
//    }
//}
