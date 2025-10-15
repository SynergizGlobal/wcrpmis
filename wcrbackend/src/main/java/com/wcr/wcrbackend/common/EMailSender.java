package com.wcr.wcrbackend.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.Alerts;
import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.RandRMain;
import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.DTO.Training;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.DTO.Mail;

public class EMailSender {    
	
	private static Logger logger = Logger.getLogger(EMailSender.class);
	
	/*private static String mailId = "syntrack@synergizglobal.com";
	private static String pass = "Synergiz@2018";*/
	
	/************** ZIMBRA Mail Server Credentials**************************************/
	private static String mailId = "syntrack@synergizglobal.com";
	private static String pass = "bxku goln hhil axze";
	
	public static Session getSession() {
		Properties props = new Properties();
		
		/************** ZIMBRA Server Starts**************************************/
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		/************** ZIMBRA Server ends*************************************/
		
		/************** GMAIL Server Starts**************************************/
		/*props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");*/
		/************** GMAIL Server ends*************************************/
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailId, pass);
			}
		  });
		return session;
	}
	
	public static boolean send(String toAddress, String subject, String body) {
		boolean isSend = false;		
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(mailId));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText(body);
			//Transport.send(message);
			logger.info("Email sent successfully");
			isSend = true;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("Exception occured while sending an email: "+e.getMessage());			
		}
		return isSend;
	}
	public static File convert(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
	
	
	public void sendEmailWithContractAlerts(Mail mail, Map<String, List<Alerts>> alerts, String today_date, String current_year, String alert_type) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alerts", alerts);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  velocityContext.put("alert_type", alert_type);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  if(!StringUtils.isEmpty(mail.getMailTo())) {
				  ArrayList<String> recipientsArray = new ArrayList<String>();
				  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
				 
				  while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				  }
				  int sizeTo = recipientsArray.size();
				  InternetAddress[] addressTo = new InternetAddress[sizeTo];
				  for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.TO, addressTo);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailCc())) {
				  ArrayList<String> ccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
				 
				  while (stringTokenizerCc.hasMoreTokens()) {
					  ccArray.add(stringTokenizerCc.nextToken());
				  }
				  int sizeCc = ccArray.size();
				  InternetAddress[] addressCc = new InternetAddress[sizeCc];
				  for (int i = 0; i < sizeCc; i++) {
					 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.CC, addressCc);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithIssueAlerts(Mail mail, Map<String, List<Alerts>> alerts, String today_date, String current_year, String alert_type) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alerts", alerts);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  velocityContext.put("alert_type", alert_type);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  if(!StringUtils.isEmpty(mail.getMailTo())) {
				  ArrayList<String> recipientsArray = new ArrayList<String>();
				  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
				 
				  while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				  }
				  int sizeTo = recipientsArray.size();
				  InternetAddress[] addressTo = new InternetAddress[sizeTo];
				  for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.TO, addressTo);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailCc())) {
				  ArrayList<String> ccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
				 
				  while (stringTokenizerCc.hasMoreTokens()) {
					  ccArray.add(stringTokenizerCc.nextToken());
				  }
				  int sizeCc = ccArray.size();
				  InternetAddress[] addressCc = new InternetAddress[sizeCc];
				  for (int i = 0; i < sizeCc; i++) {
					 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.CC, addressCc);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithRiskAlerts(Mail mail, List<Alerts> alerts, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alerts", alerts);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  velocityContext.put("title", mail.getMailSubject());
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  if(!StringUtils.isEmpty(mail.getMailTo())) {
				  ArrayList<String> recipientsArray = new ArrayList<String>();
				  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
				 
				  while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				  }
				  int sizeTo = recipientsArray.size();
				  InternetAddress[] addressTo = new InternetAddress[sizeTo];
				  for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.TO, addressTo);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailCc())) {
				  ArrayList<String> ccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
				 
				  while (stringTokenizerCc.hasMoreTokens()) {
					  ccArray.add(stringTokenizerCc.nextToken());
				  }
				  int sizeCc = ccArray.size();
				  InternetAddress[] addressCc = new InternetAddress[sizeCc];
				  for (int i = 0; i < sizeCc; i++) {
					 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.CC, addressCc);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithAlertsToITAdmins(Mail mail, Map<String, List<Alerts>> alerts, String alert_type, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alerts", alerts);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  velocityContext.put("alert_type", alert_type);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  if(!StringUtils.isEmpty(mail.getMailTo())) {
				  ArrayList<String> recipientsArray = new ArrayList<String>();
				  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
				 
				  while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				  }
				  int sizeTo = recipientsArray.size();
				  InternetAddress[] addressTo = new InternetAddress[sizeTo];
				  for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.TO, addressTo);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailCc())) {
				  ArrayList<String> ccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
				 
				  while (stringTokenizerCc.hasMoreTokens()) {
					  ccArray.add(stringTokenizerCc.nextToken());
				  }
				  int sizeCc = ccArray.size();
				  InternetAddress[] addressCc = new InternetAddress[sizeCc];
				  for (int i = 0; i < sizeCc; i++) {
					 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.CC, addressCc);
			  }
			  /*********************************************************************/
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithIssueStatusAlert(Mail mail, Issue iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithTrainingStatusAlert(Mail mail, Training iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void sendEmailWithRandRAlert(Mail mail, RandRMain iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}	

	public void sendEmailWithUtilityShiftingAlert(Mail mail, UtilityShifting iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	
	public void sendEmailWithLandAcquisitionAlert(Mail mail, LandAcquisition iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}	
	
	public void sendEmailWithSafetyStatusAlert(Mail mail, Safety iObj, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     
			 			  
			  Template template = velocityEngine.getTemplate("templates/"+ mail.getTemplateName());
				
			  VelocityContext velocityContext = new VelocityContext();
			  velocityContext.put("alert", iObj);
			  velocityContext.put("today_date", today_date);
			  velocityContext.put("current_year", current_year);
			  
			  StringWriter stringWriter = new StringWriter();
			  
			  template.merge(velocityContext, stringWriter);


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  //multipart.addBodyPart( htmlPart );

			  
			  //Multipart multiPart = new MimeMultipart();
			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer(mail.getMailTo(), ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
			  
			  /*********************************************************************/
			  
			  ArrayList<String> ccArray = new ArrayList<String>();
			  StringTokenizer stringTokenizerCc = new StringTokenizer(mail.getMailCc(), ",");
			 
			  while (stringTokenizerCc.hasMoreTokens()) {
				  ccArray.add(stringTokenizerCc.nextToken());
			  }
			  int sizeCc = ccArray.size();
			  InternetAddress[] addressCc = new InternetAddress[sizeCc];
			  for (int i = 0; i < sizeCc; i++) {
				 addressCc[i] = new InternetAddress(ccArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.CC, addressCc);
			  /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(mail.getMailBcc())) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(mail.getMailBcc(), ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
				 
			  //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMailTo()));
			  message.setSubject(mail.getMailSubject());
			  
			  //Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}	
	
	public void sendEmailWithAttachment(String recipients,String cc, String bcc, String subject, 
			String body,String attachment_file_name,String file_extention, byte[] byteArray,String attachment_type) {
		 
		 try {
			 // create a message
			 MimeMessage message = new MimeMessage( getSession() );
			 DataSource ds = null;
			 message.setFrom(new InternetAddress(mailId));
			 
			 if(!StringUtils.isEmpty(recipients)) {
				 ArrayList<String> recipientsArray = new ArrayList<String>();
				 StringTokenizer stringTokenizer = new StringTokenizer(recipients, ",");
				 
				 while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				 }
				 int sizeTo = recipientsArray.size();
				 InternetAddress[] addressTo = new InternetAddress[sizeTo];
				 for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				 }	 
				 message.setRecipients(Message.RecipientType.TO, addressTo);
			 }
			 /*********************************************************************/
			 
			 if(!StringUtils.isEmpty(cc)) {
				 ArrayList<String> ccRecipientsArray = new ArrayList<String>();
				 StringTokenizer ccStringTokenizer = new StringTokenizer(cc, ",");
				 
				 while (ccStringTokenizer.hasMoreTokens()) {
					 ccRecipientsArray.add(ccStringTokenizer.nextToken());
				 }
				 int sizeCC = ccRecipientsArray.size();
				 InternetAddress[] addressCC = new InternetAddress[sizeCC];
				 for (int i = 0; i < sizeCC; i++) {
					 addressCC[i] = new InternetAddress(ccRecipientsArray.get(i).toString());
				 }
				 message.setRecipients(Message.RecipientType.CC, addressCC);
			 }
			 
			 /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(bcc)) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(bcc, ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
			  /*********************************************************************/
			  
			 message.setSubject(subject);
			 // create and fill the first message part
			 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			 mimeBodyPart1.setText(body);
			 
			 // create the second message part
			 MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
			 try{
				 ds = new ByteArrayDataSource(byteArray, attachment_type);
			 }catch (Exception ioe ){			
				 logger.error("sendEmailWithAttachment >> "+ioe.getMessage());
			 }
			 DataHandler dh = new DataHandler(ds);
			 mimeBodyPart2.setHeader("Content-Disposition", "attachment;filename="+attachment_file_name+"."+file_extention);
			 mimeBodyPart2.setDataHandler(dh);
			 mimeBodyPart2.setFileName(attachment_file_name+"."+file_extention);
			 // create the Multipart and add its parts to it
			 Multipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(mimeBodyPart1);
			 multiPart.addBodyPart(mimeBodyPart2);
			 
			 // add the Multipart to the message
			 message.setContent(multiPart);
			 
			 // set the Date: header
			 message.setSentDate(new Date());
			 
			 // send the message
			 //Transport.send(message);
			 
		 }catch (MessagingException mex) {
			 mex.printStackTrace();
			 logger.error("sendEmailWithAttachment >> "+mex);
		 }
		
	}
	
	public void sendEmailWithIssuesReportsAttachment(String recipients,String cc, String bcc, String subject, 
			String body,String open_issues_file_name,String issues_summary_file_name, String file_extention, byte[] open_issues_byte_array, byte[] issues_summary_byte_array) {
		 
		 try {
			 // create a message
			 MimeMessage message = new MimeMessage( getSession() );
			 DataSource ds = null;
			 message.setFrom(new InternetAddress(mailId));
			 
			 if(!StringUtils.isEmpty(recipients)) {
				 ArrayList<String> recipientsArray = new ArrayList<String>();
				 StringTokenizer stringTokenizer = new StringTokenizer(recipients, ",");
				 
				 while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				 }
				 int sizeTo = recipientsArray.size();
				 if(sizeTo<=32){
					 InternetAddress[] addressTo = new InternetAddress[sizeTo];
					 for (int i = 0; i < sizeTo; i++) {
						 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
					 }	 
					 message.setRecipients(Message.RecipientType.TO, addressTo);
				 }else{
					 InternetAddress[] addressTo = new InternetAddress[32];

					 for (int i = 0; i < 32; i++) {
						 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
					 }	 
					 message.setRecipients(Message.RecipientType.TO, addressTo);					 
					 
					 int sizeCC = sizeTo-32;
					 InternetAddress[] addressCC = new InternetAddress[sizeCC];
					 int j=0;
					 for (int i = 32; i < sizeTo; i++) {
						 addressCC[j++] = new InternetAddress(recipientsArray.get(i).toString());
					 }
					 message.setRecipients(Message.RecipientType.CC, addressCC);
				 }
			 }
			 /*********************************************************************/
			 
			 if(!StringUtils.isEmpty(cc)) {
				 ArrayList<String> ccRecipientsArray = new ArrayList<String>();
				 StringTokenizer ccStringTokenizer = new StringTokenizer(cc, ",");
				 
				 while (ccStringTokenizer.hasMoreTokens()) {
					 ccRecipientsArray.add(ccStringTokenizer.nextToken());
				 }
				 int sizeCC = ccRecipientsArray.size();
				 InternetAddress[] addressCC = new InternetAddress[sizeCC];
				 for (int i = 0; i < sizeCC; i++) {
					 addressCC[i] = new InternetAddress(ccRecipientsArray.get(i).toString());
				 }
				 message.setRecipients(Message.RecipientType.CC, addressCC);
			 }
			 
			 /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(bcc)) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(bcc, ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
			  /*********************************************************************/
			  
			 message.setSubject(subject);
			 
			 // create and fill the first message part
			 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			 mimeBodyPart1.setText(body);
			 
			 // Open issues attachment part
			 MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
			 try{
				 ds = new ByteArrayDataSource(open_issues_byte_array, "application/msword");
			 }catch (Exception ioe ){			
				 logger.error("sendEmailWithAttachment >> "+ioe.getMessage());
			 }
			 DataHandler dh = new DataHandler(ds);
			 mimeBodyPart2.setHeader("Content-Disposition", "attachment;filename="+open_issues_file_name+"."+file_extention);
			 mimeBodyPart2.setDataHandler(dh);
			 mimeBodyPart2.setFileName(open_issues_file_name+"."+file_extention);
			 
			// Issues summary attachment part
			 MimeBodyPart mimeBodyPart3 = new MimeBodyPart();
			 try{
				 ds = new ByteArrayDataSource(issues_summary_byte_array, "application/msword");
			 }catch (Exception ioe ){			
				 logger.error("sendEmailWithAttachment >> "+ioe.getMessage());
			 }
			 DataHandler dh2 = new DataHandler(ds);
			 mimeBodyPart3.setHeader("Content-Disposition", "attachment;filename="+issues_summary_file_name+"."+file_extention);
			 mimeBodyPart3.setDataHandler(dh2);
			 mimeBodyPart3.setFileName(issues_summary_file_name+"."+file_extention);
			 
			 /*****************************************************************************************/
			 
			 // create the Multipart and add its parts to it
			 Multipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(mimeBodyPart1);
			 multiPart.addBodyPart(mimeBodyPart2);
			 multiPart.addBodyPart(mimeBodyPart3);
			 
			 // add the Multipart to the message
			 message.setContent(multiPart);
			 
			 // set the Date: header
			 message.setSentDate(new Date());
			 
			 // send the message
			 //Transport.send(message);
			 
		 }catch (MessagingException mex) {
			 mex.printStackTrace();
			 logger.error("sendEmailWithAttachment >> "+mex);
		 }
		
	}
	
	
	public void sendSupportEmail(String recipient, String UserName) {
		 try {
			 // create a message
			 MimeMessage message = new MimeMessage( getSession() );
			 message.setFrom(new InternetAddress(mailId));
			 recipient="swathi1670@gmail.com,swathi.sagi@synergizglobal.com";
			 
			 if(!StringUtils.isEmpty(recipient)) {
				 ArrayList<String> recipientsArray = new ArrayList<String>();
				 StringTokenizer stringTokenizer = new StringTokenizer(recipient, ",");
				 
				 while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				 }
				 int sizeTo = recipientsArray.size();
				 InternetAddress[] addressTo = new InternetAddress[sizeTo];
				 for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				 }	 
				 message.setRecipients(Message.RecipientType.TO, addressTo);
			 }

			 String subject = "PMIS error";
			 String content = "Dear PMIS Team \r\n" + 
			 		"\r\n" + 
			 		"The user is unable to access the PMIS due to an error.\r\n" + 
			 		"\r\n" + 
			 		"Do the needful.\r\n" + 
			 		"\r\n" + 
			 		""+UserName+"";
		 
			 
			 message.setSubject(subject);
			 
			 // create and fill the first message part
			 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			 mimeBodyPart1.setText(content);
			 
			 // Open issues attachment part

			 
			 
			 /*****************************************************************************************/
			 
			 Multipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(mimeBodyPart1);
			 
			 message.setContent(multiPart);
			 
			 // set the Date: header
			 message.setSentDate(new Date());
			 
			 // send the message
			 Transport.send(message);
			 
		 }catch (MessagingException mex) {
			 mex.printStackTrace();
			 logger.error("sendOTPEmail >> "+mex);
		 }
	    
	}
	
	
	
	public void sendOTPEmail(String recipient, int OTP,String UserName) {
		 try {
			 // create a message
			 MimeMessage message = new MimeMessage( getSession() );
			 message.setFrom(new InternetAddress(mailId));
			 
			 if(!StringUtils.isEmpty(recipient)) {
				 ArrayList<String> recipientsArray = new ArrayList<String>();
				 StringTokenizer stringTokenizer = new StringTokenizer(recipient, ",");
				 
				 while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				 }
				 int sizeTo = recipientsArray.size();
				 InternetAddress[] addressTo = new InternetAddress[sizeTo];
				 for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				 }	 
				 message.setRecipients(Message.RecipientType.TO, addressTo);
			 }

			 String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
			 String content = "Dear "+UserName+",\r\n" + 
			 		"\r\n" + 
			 		"We recieved a request to reset password to your PMIS Account.\r\n" + 
			 		"\r\n" + 
			 		"The OTP to reset your password is "+OTP+".\r\n" + 
			 		"\r\n" + 
			 		"Please enter the OTP details within 5 mins in the PMIS webpage before the OTP expires and enter your new password details.\r\n" + 
			 		"\r\n" + 
			 		"Cheers,\r\n" + 
			 		"\r\n" + 
			 		"PMIS Team";
		 
			 
			 message.setSubject(subject);
			 
			 // create and fill the first message part
			 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			 mimeBodyPart1.setText(content);
			 
			 // Open issues attachment part

			 
			 
			 /*****************************************************************************************/
			 
			 Multipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(mimeBodyPart1);
			 
			 message.setContent(multiPart);
			 
			 // set the Date: header
			 message.setSentDate(new Date());
			 
			 // send the message
			 Transport.send(message);
			 
		 }catch (MessagingException mex) {
			 mex.printStackTrace();
			 logger.error("sendOTPEmail >> "+mex);
		 }
	    
	}	

	
	public void sendEmailWithContractReportsAttachment(String recipients,String cc, String bcc, String subject, 
			String body,String contract_file_name, String file_extention, byte[] contract_byte_array) {
		 
		 try {
			 // create a message
			 MimeMessage message = new MimeMessage( getSession() );
			 DataSource ds = null;
			 message.setFrom(new InternetAddress(mailId));
			 
			 if(!StringUtils.isEmpty(recipients)) {
				 ArrayList<String> recipientsArray = new ArrayList<String>();
				 StringTokenizer stringTokenizer = new StringTokenizer(recipients, ",");
				 
				 while (stringTokenizer.hasMoreTokens()) {
					 recipientsArray.add(stringTokenizer.nextToken());
				 }
				 int sizeTo = recipientsArray.size();
				 InternetAddress[] addressTo = new InternetAddress[sizeTo];
				 for (int i = 0; i < sizeTo; i++) {
					 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
				 }	 
				 message.setRecipients(Message.RecipientType.TO, addressTo);
			 }
			 /*********************************************************************/
			 
			 if(!StringUtils.isEmpty(cc)) {
				 ArrayList<String> ccRecipientsArray = new ArrayList<String>();
				 StringTokenizer ccStringTokenizer = new StringTokenizer(cc, ",");
				 
				 while (ccStringTokenizer.hasMoreTokens()) {
					 ccRecipientsArray.add(ccStringTokenizer.nextToken());
				 }
				 int sizeCC = ccRecipientsArray.size();
				 InternetAddress[] addressCC = new InternetAddress[sizeCC];
				 for (int i = 0; i < sizeCC; i++) {
					 addressCC[i] = new InternetAddress(ccRecipientsArray.get(i).toString());
				 }
				 message.setRecipients(Message.RecipientType.CC, addressCC);
			 }
			 
			 /*********************************************************************/
			  
			  if(!StringUtils.isEmpty(bcc)) {
				  ArrayList<String> bccArray = new ArrayList<String>();
				  StringTokenizer stringTokenizerBcc = new StringTokenizer(bcc, ",");
				 
				  while (stringTokenizerBcc.hasMoreTokens()) {
					  bccArray.add(stringTokenizerBcc.nextToken());
				  }
				  int sizeBcc = bccArray.size();
				  InternetAddress[] addressBcc = new InternetAddress[sizeBcc];
				  for (int i = 0; i < sizeBcc; i++) {
					  addressBcc[i] = new InternetAddress(bccArray.get(i).toString());
				  }	 
				  message.setRecipients(Message.RecipientType.BCC, addressBcc);
			  }
			  /*********************************************************************/
			  
			 message.setSubject(subject);
			 
			 // create and fill the first message part
			 MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			 mimeBodyPart1.setText(body);
			 
			 // Open issues attachment part
			 MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
			 try{
				 ds = new ByteArrayDataSource(contract_byte_array, "application/msword");
				 
			 }catch (Exception ioe ){			
				 logger.error("sendEmailWithAttachment >> "+ioe.getMessage());
			 }
			 DataHandler dh = new DataHandler(ds);
			 mimeBodyPart2.setHeader("Content-Disposition", "attachment;filename="+contract_file_name+"."+file_extention);
			 mimeBodyPart2.setDataHandler(dh);
			 mimeBodyPart2.setFileName(contract_file_name+"."+file_extention);
			 
			 
			 /*****************************************************************************************/
			 
			 Multipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(mimeBodyPart1);
			 multiPart.addBodyPart(mimeBodyPart2);
			 
			 message.setContent(multiPart);
			 
			 // set the Date: header
			 message.setSentDate(new Date());
			 
			 // send the message
			 //Transport.send(message);
			 
		 }catch (MessagingException mex) {
			 mex.printStackTrace();
			 logger.error("sendEmailWithAttachment >> "+mex);
		 }
		
	}

	public void sendEmail(String to, String subject, String body, String cc) {
        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(getSession());

            // Set the "from" field
            message.setFrom(new InternetAddress(mailId));

            // Set the "to" field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set the "cc" field (if provided)
            if (cc != null && !cc.isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }

            // Set the subject
            message.setSubject(subject);

            // Set the body
            message.setText(body);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email to " + to);
        }
    }

	public void sendErrorEmailWithIssueStatusAlert(Mail mail, Issue issue, String today_date, String current_year) throws Exception {
		try {
			  MimeMessage message = new MimeMessage( getSession() );
			  Multipart multipart = new MimeMultipart( "alternative" );

			  VelocityEngine velocityEngine = new VelocityEngine();
			  Properties p = new Properties();
			  //p.setProperty("resource.loader", "class");
			  //p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("resource.loader", "class");
			  p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			  p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			  
			  p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			  
			  //p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,    NullLogChute.class.getName());
			  try {
				  velocityEngine.init( p );    
			  }catch (Exception e) {
				  throw new Exception(e);
			  }
			     

			  
			  StringWriter stringWriter = new StringWriter();
			  


			  MimeBodyPart htmlPart = new MimeBodyPart();
			  htmlPart.setContent( stringWriter.toString(), "text/html; charset=utf-8" );

			  multipart.addBodyPart(htmlPart);
			  
			  
			  message.setContent( multipart );
		    
			  message.setFrom(new InternetAddress(mailId));
			  
			  
			  ArrayList<String> recipientsArray = new ArrayList<String>();
			  StringTokenizer stringTokenizer = new StringTokenizer("kiranmayi@synergizglobal.com", ",");
			 
			  while (stringTokenizer.hasMoreTokens()) {
				 recipientsArray.add(stringTokenizer.nextToken());
			  }
			  int sizeTo = recipientsArray.size();
			  InternetAddress[] addressTo = new InternetAddress[sizeTo];
			  for (int i = 0; i < sizeTo; i++) {
				 addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
			  }	 
			  message.setRecipients(Message.RecipientType.TO, addressTo);
				 
			  message.setSubject(mail.getMailSubject());
			  
			  Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}

	
}
