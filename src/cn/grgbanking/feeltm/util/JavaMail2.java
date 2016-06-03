package cn.grgbanking.feeltm.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.grgbanking.feeltm.log.SysLog;

/**
 * @author weiling 2010-7-26 This class is for the purpose of sending email.
 */
public class JavaMail2 {

	private EmailAuthenticator auth;
	private Properties props;
	private Session mailSession;
	private MimeMessage msg;
	private MailBean mailBean;
	private Transport tran;

	public JavaMail2(MailBean mailBean) {
		this.mailBean = mailBean;
		this.props = setProperties(mailBean);

		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
		this.auth = new EmailAuthenticator(mailBean.getUserName(), mailBean
				.getPassword());

		// 根据已配置的JavaMail属性创建Session实例
		this.mailSession = Session.getInstance(props, (Authenticator) auth);
		// 设置是否在控制台上显示发送邮件的过程
		mailSession.setDebug(true);

		this.msg = new MimeMessage(mailSession);

		try {
			this.tran = this.mailSession.getTransport("smtp");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	// 设置JavaMail属性
	public Properties setProperties(MailBean mailBean) {
		Properties props = new Properties();
		// 设置邮件服务器端口
		props.put("mail.smtp.port", mailBean.getPort());
		// SMTP邮件服务器IP地址或主机名
		props.put("mail.smtp.host", mailBean.getHostName());
		props.put("mail.smtp.auth", "true");

		return props;
	}

	public void sendMail() {
		try {
			// 设置收件人地址
			msg.setRecipients(Message.RecipientType.TO,
					getInternetAddress(mailBean.getToAddress()));

			// 设置抄件人地址
			msg.setRecipients(Message.RecipientType.CC,
					getInternetAddress(mailBean.getCC()));

			// 设置邮件主题
			msg.setSubject(mailBean.getSubject());

			// 设置邮件时间
			msg.setSentDate(new Date());

			// 设置邮件发送者
			msg.setFrom(new InternetAddress(mailBean.getFromAddress()));

			// 设置邮件主体,包括邮件文本内容和附件
			msg.setContent(setMultipart(mailBean.getContent(), mailBean
					.getFileList()));

			tran.connect(mailBean.getHostName(), mailBean.getUserName(),
					mailBean.getPassword());
			Transport.send(msg);
			tran.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// 设置邮件接收者以及抄送接收者地址
	public InternetAddress[] getInternetAddress(String address) {
		InternetAddress[] ia;
		if (address != null && address.trim().length() > 0) {
			String[] addressArr = address.split(";");

			if (addressArr.length > 0)// 不止一个收件人
			{
				ia = new InternetAddress[addressArr.length];
				for (int i = 0; i < addressArr.length; i++) {
					try {
						ia[i] = new InternetAddress(addressArr[i]);
					} catch (AddressException e) {
						e.printStackTrace();
						SysLog.error(e);
					}
				}
				return ia;
			} else// 只有一个收件人
			{
				ia = new InternetAddress[1];
				try {
					ia[0] = new InternetAddress(address);
					return ia;
				} catch (AddressException e) {
					e.printStackTrace();
					return null;
				}
			}
		}// end outter if
		else
			return null;
	}

	public Multipart setMultipart(String text, List<String> fileList) {
		Multipart multipart = new MimeMultipart();

		List<MimeBodyPart> bodyPartList = getBodyPartList(text, fileList);

		try {
			for (int i = 0; i < bodyPartList.size(); i++) {
				multipart.addBodyPart(bodyPartList.get(i));
			}
			return multipart;
		} catch (MessagingException e) {
			e.printStackTrace();
			SysLog.error(e);
			return null;
		}
	}

	public List<MimeBodyPart> getBodyPartList(String text, List<String> fileList) {
		List<MimeBodyPart> bodyPartList = new ArrayList<MimeBodyPart>();

		bodyPartList.add(getText(text));

		bodyPartList.addAll(getAttachment(fileList));

		return bodyPartList;
	}

	// 设置邮件文本内容
	public MimeBodyPart getText(String text) {
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
	public List<MimeBodyPart> getAttachment(List<String> fileList) {
		List<MimeBodyPart> bodyPartList = new ArrayList<MimeBodyPart>();
		try {
			// 加入附件
			for (int i = 0; i < bodyPartList.size(); i++) {
				MimeBodyPart bodyPart = new MimeBodyPart();
				// 得到数据源
				FileDataSource fileDataSource = new FileDataSource(fileList
						.get(i));
				bodyPart.setDataHandler(new DataHandler(fileDataSource));
				bodyPart.setDisposition(Part.ATTACHMENT);
				// 设置文件名
				bodyPart.setFileName(fileDataSource.getName());
				bodyPartList.add(bodyPart);
			}
			return bodyPartList;
		} catch (MessagingException e) {
			e.printStackTrace();
			SysLog.error(e);
			return null;
		}
	}

	// 邮件用户身份验证类
	class EmailAuthenticator extends Authenticator {

		private String username;

		private String userpass;

		EmailAuthenticator(String un, String up) {
			super();
			username = un;
			userpass = up;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, userpass);
		}
	}

	// 测试
	public static void main(String[] args) {
		MailBean mailBean = new MailBean();
		JavaMail2 javaMail = new JavaMail2(mailBean);
		List<String> fileList = new ArrayList<String>();
		fileList.add("D:\\temp\\1.txt");
		fileList.add("D:\\temp\\2.txt");
		mailBean.setFileList(fileList);
		javaMail.sendMail();

	}

}
