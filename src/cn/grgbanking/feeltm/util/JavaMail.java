package cn.grgbanking.feeltm.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.log.SysLog;

/**
 * 发送邮件测试通过
 * 
 * @author 
 */
public class JavaMail {

	public static boolean sendMail(String to, String cc, String subject, String content)
			throws MessagingException {
		boolean  sendFlag=false;
		String host = Configure.getProperty("host");
		String username = Configure.getProperty("username");
		String password = Configure.getProperty("password");
		String from = Configure.getProperty("frommail");
		//host="smtp.qq.com";
		//from="nicholas_hjf@qq.com";
		//username="nicholas_hjf@qq.com";
		//password="IFCA1user";
		host="smtp.grgbanking.com";
		from="testing@grgbanking.com";
		username="testing@grgbanking.com";
		password="panji725";
		//String host ="smtp.sohu.com";
	   // String username = "bankmanager";
		//String password ="123456";
		//String from = "bankmanager@sohu.com";
		boolean authentication = false;
//		if (Configure.getProperty("authentication") != null
//				&& Configure.getProperty("authentication").equals("true"))
			authentication = true;

		// Get system properties
		Properties props = System.getProperties();
		// Setup mail server
		props.put("mail.smtp.host", host);
		if (authentication) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
		// Get session
		Session session = Session.getDefaultInstance(props, null);
		// Define message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// 发送邮件地址
		if (to != null && to.trim().length() > 0) {
			String[] arrto = to.split(",");
			// int ReceiverCount=1;
			int receiverCountto = arrto.length;
			if (receiverCountto > 0) {
				InternetAddress[] addressto = new InternetAddress[receiverCountto];
				for (int i = 0; i < receiverCountto; i++) {
					addressto[i] = new InternetAddress(arrto[i]);
				}
				message.addRecipients(Message.RecipientType.TO, addressto);

			}
          
			
			// 抄送邮件地址
			if (cc != null && cc.trim().length() > 0) {
				String[] arrcc = cc.split(",");
				// int ReceiverCount=1;
				int receiverCountcc = arrcc.length;
				if (receiverCountcc > 0) {
					InternetAddress[] addresscc = new InternetAddress[receiverCountcc];
					for (int i = 0; i < receiverCountcc; i++) {
						addresscc[i] = new InternetAddress(arrcc[i]);
					}

					message.addRecipients(Message.RecipientType.CC, addresscc);
				}

			}
			message.setSubject(subject);
			message.setContent(content, "text/plain;charset=gb2312");
			// Send message
			if (authentication) {
				Transport smtp = null;
				try {
					smtp = session.getTransport("smtp");
					smtp.connect(host, username, password);
					smtp.sendMessage(message, message.getAllRecipients());
					sendFlag=true;
				} catch (AddressException e) {
					e.printStackTrace();
					sendFlag=false;
					System.out.println("send email  faile    addressException ");
				} catch (MessagingException e) {
					e.printStackTrace();
					sendFlag=false;
					System.out.println("send email faile  messagingException");
				} finally {
					smtp.close();
				}
			}
		}
		
    return sendFlag;
		
		
	}
	
	
	public static boolean sendMailByHtml(String to, String cc, String subject, String content,List<String> fileList)
	throws MessagingException {
		boolean  sendFlag=false;
		String host = Configure.getProperty("host");
		String username = Configure.getProperty("username");
		String password = Configure.getProperty("password");
		String from = Configure.getProperty("frommail");
		host="smtp.grgbanking.com";
		//from="hjfeng@grgbanking.com";
		//username="hjfeng@grgbanking.com";
		//password="IFCA1user";
		//host="smtp.grgbanking.com";
		from="testing@grgbanking.com";
		username="testing@grgbanking.com";
		password="panji725";
		//String host ="smtp.sohu.com";
		// String username = "bankmanager";
		//String password ="123456";
		//String from = "bankmanager@sohu.com";
		boolean authentication = false;
		//if (Configure.getProperty("authentication") != null
		//		&& Configure.getProperty("authentication").equals("true"))
			authentication = true;
		
		// Get system properties
		Properties props = System.getProperties();
		// Setup mail server
		props.put("mail.smtp.host", host);
		if (authentication) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
		// Get session
		Session session = Session.getDefaultInstance(props, null);
		// Define message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// 发送邮件地址
		if (to != null && to.trim().length() > 0) {
			String[] arrto = to.split(",");
			// int ReceiverCount=1;
			int receiverCountto = arrto.length;
			if (receiverCountto > 0) {
				InternetAddress[] addressto = new InternetAddress[receiverCountto];
				for (int i = 0; i < receiverCountto; i++) {
					addressto[i] = new InternetAddress(arrto[i]);
				}
				message.addRecipients(Message.RecipientType.TO, addressto);
		
			}
		  
			
			// 抄送邮件地址
			if (cc != null && cc.trim().length() > 0) {
				String[] arrcc = cc.split(",");
				// int ReceiverCount=1;
				int receiverCountcc = arrcc.length;
				if (receiverCountcc > 0) {
					InternetAddress[] addresscc = new InternetAddress[receiverCountcc];
					for (int i = 0; i < receiverCountcc; i++) {
						addresscc[i] = new InternetAddress(arrcc[i]);
					}
		
					message.addRecipients(Message.RecipientType.CC, addresscc);
				}
		
			}
			message.setSubject(subject);
		//	message.setContent(content, "text/html;charset=gb2312");
			
			//添加附
			
			
			message.setContent(setMultipart(content,fileList,subject) );
			

			// Send message
			if (authentication) {
				Transport smtp = null;
				try {
					smtp = session.getTransport("smtp");
					smtp.connect(host, username, password);
					smtp.sendMessage(message, message.getAllRecipients());
					sendFlag=true;
				} catch (AddressException e) {
					e.printStackTrace();
					sendFlag=false;
					System.out.println("send email  faile    addressException ");
				} catch (MessagingException e) {
					e.printStackTrace();
					sendFlag=false;
					System.out.println("send email faile  messagingException");
				} finally {
					smtp.close();
				}
			}
		}
		
		return sendFlag;
	
	
	}
	
	public MimeBodyPart createAttachment(String fileName) throws Exception 
	{  
			MimeBodyPart attachmentPart = new MimeBodyPart();
             FileDataSource fds = new FileDataSource(fileName);
            attachmentPart.setDataHandler(new DataHandler(fds));
            attachmentPart.setFileName(fds.getName()); 
            return attachmentPart;
    }  
	
	public static Multipart setMultipart(String text, List<String> fileList,String subject) {
		Multipart multipart = new MimeMultipart();
		

		List<MimeBodyPart> bodyPartList = getBodyPartList(text, fileList,subject);

		try {
			for (int i = 0; i < bodyPartList.size(); i++) {
				bodyPartList.get(i).setDataHandler(new DataHandler(text,"text/html;charset=utf-8"));
				multipart.addBodyPart(bodyPartList.get(i));
				
				MimeBodyPart jpgBody = new MimeBodyPart();
				FileDataSource fds = new FileDataSource("D:\\nStraf\\nStraf\\images\\main\\GrgBack.jpg");
				jpgBody.setDataHandler(new DataHandler(fds));
				jpgBody.setContentID("IMG1");
				jpgBody.setFileName("1"+ ".jpg"); 
				jpgBody.setHeader("Content-ID", "IMG" + "1");
				multipart.addBodyPart(jpgBody);
			}
			return multipart;
		} catch (MessagingException e) {
			e.printStackTrace();
			SysLog.error(e);
			return null;
		}
	}

	public static List<MimeBodyPart> getBodyPartList(String text, List<String> fileList,String subject) {
		List<MimeBodyPart> bodyPartList = new ArrayList<MimeBodyPart>();

		bodyPartList.add(getText(text));

		bodyPartList.addAll(getAttachment(fileList,subject));

		return bodyPartList;
	}
	
	
	// 设置邮件文本内容
	public static MimeBodyPart getText(String text) {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		try {
			mimeBodyPart.setText(text);
			return mimeBodyPart;
		} catch (MessagingException e) {
			e.printStackTrace();
			SysLog.error(e);
			return null;
		}
	}

	// 设置邮件附件
	public static List<MimeBodyPart> getAttachment(List<String> fileList,String subject) {
		List<MimeBodyPart> bodyPartList = new ArrayList<MimeBodyPart>();
		try {
			// 加入附件
			for (int i = 0; i < fileList.size(); i++) {

					MimeBodyPart bodyPart = new MimeBodyPart();
					// 得到数据源
					FileDataSource fileDataSource = new FileDataSource(fileList
							.get(i));
					bodyPart.setDataHandler(new DataHandler(fileDataSource));
					bodyPart.setDisposition(Part.ATTACHMENT);
					// 设置文件名
					//bodyPart.setFileName(fileDataSource.getName());
					subject=MimeUtility.encodeText(subject+".html");
					bodyPart.setFileName(subject);
					bodyPartList.add(bodyPart);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			SysLog.error(e);
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bodyPartList;
	}
}
