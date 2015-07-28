/**
 * @title MailSenderImpl.java
 * @package test
 * @description 
 * @author YuezhouLi
 * @update 2012-7-2 下午3:57:35
 * @version V1.0
 */

package com.hisoft.hscloud.mail.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-7-2 下午3:57:35
 */
public class MailSenderUtil {

	/**
	 * 发送邮件的props文件
	 */
	private final transient Properties props = System.getProperties();
	/**
	 * 邮件服务器登录验证
	 */
//	private transient MailAuthenticator authenticator;

	/**
	 * 邮箱session
	 */
//	private transient Session session;

	private String senderAddr;
	
	private String senderDisplayName =  null;
	
	private String subjectPrefix = "";

	private String templete = "default";
	private String blindCarbonCopy = null;
	private String username;
	private String password;

	/**
	 * 初始化邮件发送器
	 * 
	 * @param smtpHostName
	 *            SMTP邮件服务器地址
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            发送邮件的密码
	 */
	public MailSenderUtil(final String smtpHostName, final String username,
			final String password, String senderAddr, String templete) {
		init(username, password, smtpHostName);
		this.senderAddr = senderAddr;
		this.templete = templete;
	}

	/**
	 * 初始化邮件发送器
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)，并以此解析SMTP服务器地址
	 * @param password
	 *            发送邮件的密码
	 */
	public MailSenderUtil(final String username, final String password,
			String senderAddr, String templete) {
		// 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);
		this.senderAddr = senderAddr;
		this.templete = templete;
	}

	/**
	 * 初始化
	 * 
	 * @param username
	 *            发送邮件的用户名(地址)
	 * @param password
	 *            密码
	 * @param smtpHostName
	 *            SMTP主机地址
	 */
	private void init(String username, String password, String smtpHostName) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		props.put("mail.smtp.port", 587);
		
		this.username = username;
		this.password = password;
		// 验证
//		authenticator = new MailAuthenticator(username, password);
		// 创建session
//		session = Session.getDefaultInstance(props, authenticator);
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void send(String recipient, String subject, Object content)
			throws AddressException, MessagingException,
			UnsupportedEncodingException {
		//System.out.println("recipient:" + recipient);
		MailAuthenticator mailAuthenticator = new MailAuthenticator(username, password);
        // 创建session
        Session session = Session.getInstance(props, mailAuthenticator);
		// 创建mime类型邮件
		MimeMessage message = new MimeMessage(session);
		// 设置发信人
		if(senderDisplayName == null || senderDisplayName.equals("")){
			senderDisplayName = this.senderAddr;
		}
		message.setFrom(new InternetAddress(this.senderAddr, senderDisplayName));
		// 设置收件人
		Address[] addrs = { new InternetAddress(recipient) };
		message.setRecipients(RecipientType.TO, addrs);
		// 设置主题
		message.setSubject(subjectPrefix+subject);
		
		message.setSentDate(new Date());
		//设置密件抄送者地址
		if(blindCarbonCopy != null){
			message.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(this.blindCarbonCopy));
		} else {
		    message.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(this.senderAddr));
		}
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();

		// 设置邮件内容
		System.out.println(content.getClass());
		if (content.getClass().equals(HashMap.class)) {
			System.out.println("mapper");
			Map map = (Map) content;
			System.out.println(generateContent(map));
			html.setContent(generateContent(map), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// message.setContent(generateContent(map),
			// "text/html;charset=utf-8");
			// message.setText(generateContent(map));
		} else {
			html.setContent(content.toString(), "text/html;charset=utf-8");
			mainPart.addBodyPart(html);
			// message.setText(content.toString());
		}

		message.setContent(mainPart);
		Transport.send(message);
		session = null;
	}

	/**
	 * 群发邮件
	 * 
	 * @param recipients
	 *            收件人们
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void send(List<String> recipients, String subject, Object content)
			throws AddressException, MessagingException,
			UnsupportedEncodingException {
	    MailAuthenticator mailAuthenticator = new MailAuthenticator(username, password);
        // 创建session
        Session session = Session.getInstance(props, mailAuthenticator);
	    // 创建mime类型邮件
		final MimeMessage message = new MimeMessage(session);
		// 设置发信人
		if(senderDisplayName == null || senderDisplayName.equals("")){
			senderDisplayName = this.senderAddr;
		}
		message.setFrom(new InternetAddress(this.senderAddr, senderDisplayName));
		// 设置收件人们
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		}
		message.setRecipients(RecipientType.TO, addresses);
		// 设置主题
		message.setSubject(subjectPrefix+subject);
		message.setSentDate(new Date());

		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();

		// 设置邮件内容
		System.out.println(content.getClass());
		if (content.getClass().equals(HashMap.class)) {
			System.out.println("mapper");
			Map map = (Map) content;
			System.out.println(generateContent(map));
			html.setContent(generateContent(map), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// message.setContent(generateContent(map),
			// "text/html;charset=utf-8");
			// message.setText(generateContent(map));
		} else {
			html.setContent(content.toString(), "text/html;charset=utf-8");
			mainPart.addBodyPart(html);
			// message.setText(content.toString());
		}

		message.setContent(mainPart);
		Transport.send(message);
	}

	private String generateContent(Map<String, String> map) {
		BufferedReader reader;
		StringBuffer templete = new StringBuffer();
		String temp = null;
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/");
		System.out.println("====>>> "+url.getPath());
		
		try {
			reader = new BufferedReader(new FileReader(url.getPath()+"default.templete"));
			while ((temp = reader.readLine()) != null) {
				templete.append(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String templeteStr = templete.toString();
		for (String key : map.keySet()) {
			if (map.get(key) != null) {
				templeteStr = templeteStr.replaceAll(
						"\\{params:" + key + "\\}", map.get(key));
			}
		}
		return templeteStr;
	}

	/**
	 * @return senderDisplayName : return the property senderDisplayName.
	 */
	public String getSenderDisplayName() {
		return senderDisplayName;
	}

	/**
	 * @param senderDisplayName : set the property senderDisplayName.
	 */
	public void setSenderDisplayName(String senderDisplayName) {
		this.senderDisplayName = senderDisplayName;
	}

	/**
	 * @return subjectPrefix : return the property subjectPrefix.
	 */
	public String getSubjectPrefix() {
		return subjectPrefix;
	}

	/**
	 * @param subjectPrefix : set the property subjectPrefix.
	 */
	public void setSubjectPrefix(String subjectPrefix) {
		if(subjectPrefix != null){
			this.subjectPrefix = subjectPrefix;
		}
	}

	public String getBlindCarbonCopy() {
		return blindCarbonCopy;
	}

	public void setBlindCarbonCopy(String blindCarbonCopy) {
		this.blindCarbonCopy = blindCarbonCopy;
	}
	
	
}
