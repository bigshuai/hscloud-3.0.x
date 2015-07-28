/**
 * @title MailFactory.java
 * @package com.hisoft.hscloud.util.mail
 * @description 
 * @author YuezhouLi
 * @update 2012-7-2 下午4:49:14
 * @version V1.0
 */
package com.hisoft.hscloud.mail.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.hisoft.hscloud.mail.entity.MailSender;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-7-2 下午4:49:14
 */
public class MailFactory {

//	private MailSenderUtil sender = null;
	
	public MailFactory(){
		
	}
	
	private static String senderAddr = null;
	
	private static String senderUsername = null;
	
	private static String senderPassword = null ;
	
	private static String senderSmtp = null;
	
	private static String contextType = null;
	
	private static String contextTemplete = null ;
	
	private static String senderDisplayName = null;
	
	private static String subjectPrefix = null;
	
	private static String blindCarbonCopy = null;
	
/*	private static void init(){
		Properties properties = new Properties();
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/");
		if(url == null){
			url = Thread.currentThread().getContextClassLoader().getResource(".");
		}
		System.out.println("====>>> "+url.getPath());
		
		try{
			properties.load(new FileInputStream(url.getPath()+"mail.properties"));
			System.out.println(properties.isEmpty());
			senderAddr = properties.getProperty("sender.address");
			senderUsername = properties.getProperty("sender.username");
			senderPassword = properties.getProperty("sender.password");
			senderSmtp = properties.getProperty("sender.smtp");
			contextType = properties.getProperty("mail.context.type");
			contextTemplete = properties.getProperty("mail.context.templete");
			senderDisplayName = properties.getProperty("sender.displayname");
			subjectPrefix = properties.getProperty("mail.subject.prefix");
			blindCarbonCopy = properties.getProperty("mail.recipient.bcc");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(String config){
		Properties properties = new Properties();
		
		try{
			properties.load(new FileInputStream(config));
			senderAddr = properties.getProperty("sender.address");
			senderUsername = properties.getProperty("sender.username");
			senderPassword = properties.getProperty("sender.password");
			senderSmtp = properties.getProperty("sender.smtp");
			contextType = properties.getProperty("mail.context.type");
			contextTemplete = properties.getProperty("mail.context.templete");
			senderDisplayName = properties.getProperty("sender.displayname");
			subjectPrefix = properties.getProperty("mail.subject.prefix");
			blindCarbonCopy = properties.getProperty("mail.recipient.bcc");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MailSender getSender() throws EmailAccountException{
		if(sender == null){
			init();
			System.out.println(senderSmtp+" "+senderAddr+ " "+senderUsername+' '+senderPassword );
			if(senderSmtp != null && senderAddr != null && senderUsername != null && senderPassword != null){
				sender = new MailSender(senderSmtp,senderUsername,senderPassword,senderAddr,contextTemplete);
				sender.setSenderDisplayName(senderDisplayName);
				sender.setSubjectPrefix(subjectPrefix);
				sender.setBlindCarbonCopy(blindCarbonCopy);
				return sender;
			}else{
				throw new EmailAccountException();
			}
		}
		return sender;
	}
	*/
	public MailSenderUtil getSender(MailSender mailSender) throws EmailAccountException{
     //   if(sender == null){
        //    System.out.println(senderSmtp+" "+senderAddr+ " "+senderUsername+' '+senderPassword );
            senderSmtp = mailSender.getSmtp();
            senderAddr = mailSender.getAddress();
            senderUsername = mailSender.getUsername();
            senderPassword = mailSender.getPassword();
            blindCarbonCopy = mailSender.getAddress();
            if(senderSmtp != null && senderAddr != null && senderUsername != null && senderPassword != null){
                MailSenderUtil sender = new MailSenderUtil(senderSmtp,senderUsername,senderPassword,senderAddr,contextTemplete);
                sender.setSenderDisplayName(senderDisplayName);
                sender.setSubjectPrefix(subjectPrefix);
                sender.setBlindCarbonCopy(blindCarbonCopy);
                return sender;
            }else{
                throw new EmailAccountException();
            }
    //    }
    //    return sender;
    }

	public static String getSenderUsername() {
		return senderUsername;
	}

	public static void setSenderUsername(String senderUsername) {
		MailFactory.senderUsername = senderUsername;
	}
	
}
