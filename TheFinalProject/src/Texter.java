import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;

public class Texter {

	//stuff for Texter's email
	private static String smptHostName = "smtp.gmail.com";
    private static String UserName = "tester84l33@gmail.com";
    private static String SecretPassword = "fakeemailaccount";
    private Session TheSession;
    private Map<String, String> PhoneCarriers;
    private ExecutorService ThreadPool = Executors.newFixedThreadPool(2);
    
    /**
     * initializes what is needed to send text messages
     */
    public Texter()
    {
    	AddPhoneCarriers();
        // create a session with an Authenticator
        TheSession = Session.getInstance(SetProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(UserName, SecretPassword);
            }
        });
    }
    
    /**
     * sets the properties for connection to send text messages
     * @return
     */
    private Properties SetProperties()
    {
    	// Get system properties
        Properties TheProperties = System.getProperties();
    	// Setup mail server
        TheProperties.put("mail.smtp.host", smptHostName); 
        TheProperties.put("mail.smtp.USERNAME",UserName);
        TheProperties.put("mail.smtp.ssl.enable", "true");
        TheProperties.put("mail.debug", "false");
        TheProperties.put("mail.smtp.auth", "true");
        TheProperties.put("mail.smtp.starttls.enable","true");
        TheProperties.put("mail.smtp.EnableSSL.enable","true");
        TheProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        TheProperties.setProperty("mail.smtp.socketFactory.fallback", "false");  
        TheProperties.setProperty("mail.smtp.port", "465");  
        TheProperties.setProperty("mail.smtp.socketFactory.port", "465");
        return TheProperties;
    }
    
    /**
     * Sends a text message using a thread pool
     * @param CurrentUser - user who will be receiving the message
     * @param TheMessage - string that contains message
     */
    public void ThreadSendMessage(User CurrentUser, String TheMessage)
    {
    	ThreadPool.execute(new MessageSender(CurrentUser, TheMessage));
    }
    
    private class MessageSender implements Runnable
    {
    	User CurrentUser;
    	String TheMessage;
    	//gets user and message values
    	public MessageSender(User CurrentUser, String TheMessage)
    	{
    		this.CurrentUser = CurrentUser;
    		this.TheMessage = TheMessage;
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SendTextMessage(CurrentUser, TheMessage);
		}
    	
    }
    
    /**
     * Sends text message
     * @param CurrentUser - user who will be receiving message
     * @param TheMessage - string of message that will be sent
     */
    public void SendTextMessage(User CurrentUser, String TheMessage)
    {
    	String Carrier = PhoneCarriers.get(CurrentUser.GetCarrier());
    	if (Carrier != null && PhoneNumberValid(CurrentUser.GetPhoneNumber()))
    	{
	    	try
	    	{
	            //Creates a default MimeMessage object
	            MimeMessage message = new MimeMessage(TheSession);
	
	            //Sent From: header field of the header.
	            message.setFrom(new InternetAddress(UserName));
	
	            //Set To: header field of the header.
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(CurrentUser.GetPhoneNumber() + Carrier));
	
	            //puts string of message into message MimeMessage object
	            message.setText(TheMessage);
	           // Sends the message
            	Transport.send(message);
	            System.out.println("Sent message successfully....");
	        }
	    	//could fail due to no access to Internet or message setfrom is not same as username
	    	catch (MessagingException mex) 
	    	{
	    		System.out.println("Text message failed to send, check Internet connection....");
	        }
    	}
    	// if user data not valid, will reach here
    	else
    	{
    		System.out.println("Message not sent successfully due to invalid User data....");
    	}
    }
    
    /**
     * checks that phone number is valid
     * @param PhoneNumber - string of user's phone number
     * @return - returns boolean that represents if phone number is valid or not
     */
    private boolean PhoneNumberValid(String PhoneNumber)
    {
    	//checks if phone number correct length
    	if (PhoneNumber == null || PhoneNumber.length() != 10)
    		return false;
    	//check if there are any non-digit characters in string
    	for(int i = 0; i < PhoneNumber.length(); i++)
    	{
    		if(!Character.isDigit(PhoneNumber.charAt(i)))
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * adds phone numbers to a hashmap
     */
    private void AddPhoneCarriers()
    {
    	PhoneCarriers = new HashMap<String, String>();
    	PhoneCarriers.put("AllTel", "@text.wireless.alltel.com");
    	PhoneCarriers.put("AT&T", "@txt.att.net");
    	PhoneCarriers.put("Boost Mobile", "@myboostmobile.com");
    	PhoneCarriers.put("Cricket", "@sms.mycricket.com");
    	PhoneCarriers.put("Sprint", "@messaging.sprintpcs.com");
    	PhoneCarriers.put("T-Mobile", "@tmomail.net");
    	PhoneCarriers.put("US Cellular", "@email.uscc.net");
    	PhoneCarriers.put("Verizon", "@vtext.com");
    	PhoneCarriers.put("Virgin Mobile", "@vmobl.com");
    	
    }
}
