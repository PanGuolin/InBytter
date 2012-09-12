package com.byttersoft.patchbuild.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件工具类
 * @author pangl
 *
 */
public class MailUtil {
	public static final String CONFIG_NAME = "/com/byttersoft/patchbuild/utils/mail.properties";
	
	private static String serverHost = null;
	
	private static String loginUser = null;
	
	private static String loginPassword = null;
	
	private static String fromAddr = null;
	
	private static String sender = null;
	
	static {
		init();
	}
	
	private static void init() {
		Properties props = new Properties();
		try {
			props.load(MailUtil.class.getResourceAsStream(CONFIG_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverHost = props.getProperty("host");
		loginUser = props.getProperty("user");
		loginPassword = props.getProperty("password");
		fromAddr = props.getProperty("addr");
		sender = props.getProperty("sender");
	}
	
	/**
	 * 发送邮件
	 * @param info
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMail(MailInfo info) throws MessagingException, UnsupportedEncodingException{
		Properties props = new Properties();
        Authenticator auth = new EmailAutherticator(loginUser, loginPassword); 
        props.put("mail.smtp.host", serverHost);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage message = new MimeMessage(session);
		message.setSubject(info.getSubject());
		message.setText(info.getContent(), "GBK", "html");
		message.setSentDate(new Date());
		Address address;
		address = new InternetAddress(fromAddr, sender);
		message.setFrom(address); 
		Address toAddress = new InternetAddress(info.getToAddrs()); 
		message.addRecipient(Message.RecipientType.TO, toAddress);
		Transport.send(message); 
	}
}

class EmailAutherticator extends Authenticator {
	private String username;
	private String password;
	
    public EmailAutherticator() {
        super();
    }

    public EmailAutherticator(String user, String pwd)
    {
        super();
        username = user;
        password = pwd;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(username, password);
    }
}
