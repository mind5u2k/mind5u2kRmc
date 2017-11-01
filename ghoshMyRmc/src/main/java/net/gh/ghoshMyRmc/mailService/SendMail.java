package net.gh.ghoshMyRmc.mailService;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private static String username = "armtooladmin@vedhacon.com";
	private static String password = "yahoo@123";

	public static boolean SendMail(String to, String cc, String subject,
			String mailText) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.zoho.com");
		props.put("mail.smtp.port", "587");

		Session session1 = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session1);

			message.setFrom(new InternetAddress(username));

			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(cc));

			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject(subject);

			// message.setText(mailText);

			message.setContent(mailText, "text/html; charset=ISO-8859-1");

			Transport.send(message);
			System.out.println("Done");
			return true;

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return false;

		}
	}

	public static void main(String[] args) {
		System.out.println("hahahaha");
		SendMail("anurag.ghosh.1014@gmail.com",
				"anuraghosh.1992@rediffmail.com", "zzxcvzxcvzx",
				"asdfasdf<br> hsdfhdsa");
		System.out.println("qwerty");
	}

}
