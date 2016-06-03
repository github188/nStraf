/**
 * Copyright (c) 2010 Changchun CBIT Co. Ltd.
 * All right reserved.
 * History
 */
/*
 * @(#)SendMail.java 1.1 Feb 23, 2010
 */

package cn.grgbanking.feeltm.util;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import cn.grgbanking.feeltm.domain.testsys.WebInfoAttach;


/**
 * 发送邮件 类
 * 
 * @author songsd
 * @version 1.0.0
 */
public class SendMail
{
	/**
	 * 服务路径
	 */
	private String path;
	/**
	 * 取得邮件协议的Properties
	 * 
	 * @return Properties 属性实例
	 */
	public Properties getProperties()
	{
		Properties props=null;
		InputStream is=null;
		try
		{
			props=new Properties();
			is=this.getClass().getResourceAsStream( "/buyingCenter.mail.properties" );
			props.load( is );
			is.close();
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return props;
	}
	/**
	 * 发送邮件
	 * 
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件正文
	 * @param toAddress
	 *            收件人地址数组
	 * @return 发送成功返回true,失败返回false
	 */
	public boolean sendMail(String subject,String content,String []toAddress)
	{
		boolean flag=false;
		try
		{
			// Properties对象用来获取诸如邮件服务器，用户名，密码等信息以及其它可在整个应用程序共享的信息。
			Properties propsAll=getProperties();
			Properties props=new Properties();
			//存储发送邮件服务器的信息
			props.put("mail.smtp.host",propsAll.getProperty( "mail.smtp.host", "" ));
			//同时通过验证
			props.put("mail.smtp.auth","true");
			// session类定义全局或每个用户的与邮件相关的属性。在此根据属性创建一个邮件会话，null参数是一种Authenticator(验证程序)
			// 对象
			Session s=Session.getInstance( props ,null);
			// 由邮件会话新建一个消息对象
			Message message=new MimeMessage(s);
			// 取发件人邮件字付串
			String fromAdd=propsAll.getProperty( "mail.sender.mail", "" );
			// 发件人邮件地址
			Address from=new InternetAddress(fromAdd);
			// 设置发件人
			message.setFrom( from );
			if(toAddress.length==1)
			{
				// 设置收件人，设置接收类型为To
				message.setRecipient( Message.RecipientType.TO, new InternetAddress(toAddress[0]) );
			}
			else
			{
				// 设置第一个收件人，设置接收类型为To
				message.setRecipient( Message.RecipientType.TO, new InternetAddress(toAddress[0]) );
				Address toAdd[]=new InternetAddress[toAddress.length-1];
				for(int i=0;i<toAddress.length-1;i++)
				{
					toAdd[i]=new InternetAddress(toAddress[i+1]);
				}
				// 设置其它人的接收类型为CC
				message.setRecipients( Message.RecipientType.CC, toAdd );
			}
			// 设置主体
			message.setSubject( subject );
			// 设置信件内容
			//message.setText( content );
			message.setContent( content.toString(), "text/html;charset=GB2312" );
			// 设置发送日期
			// 存储邮件信息
			message.saveChanges();
			// transport是用来发送邮件的
			Transport tr=s.getTransport("smtp");
			// 以smtp方式登录邮箱
			tr.connect( propsAll.getProperty( "mail.smtp.host" ), propsAll.getProperty( "mail.smtp.user" ), propsAll.getProperty( "mail.smtp.password" ));
			// 发送邮件,其中第二个参数是所有已设好的收件人地址
			tr.sendMessage( message,message.getAllRecipients() );
			tr.close();
			flag=true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return flag;
		}
		return flag;
		
	}
	/**
	 * 发送带附件的邮件
	 * @param subject　主题
	 * @param content　正文
	 * @param toAddress　收件人地址数组
	 * @param attachList 信息附件集合
	 * @return
	 */
	public boolean sendMailOnAttachFile(String subject,String content,String []toAddress,List<WebInfoAttach> attachList)
	{
		
		boolean flag=false;
		try
		{
			// Properties对象用来获取诸如邮件服务器，用户名，密码等信息以及其它可在整个应用程序共享的信息。
			Properties propsAll=getProperties();
			Properties props=new Properties();
			//存储发送邮件服务器的信息
			props.put("mail.smtp.host",propsAll.getProperty( "mail.smtp.host", "" ));
			//同时通过验证
			props.put("mail.smtp.auth","true");
			// session类定义全局或每个用户的与邮件相关的属性。在此根据属性创建一个邮件会话，null参数是一种Authenticator(验证程序)
			// 对象
			Session s=Session.getInstance( props ,null);
			// 由邮件会话新建一个消息对象
			Message message=new MimeMessage(s);
			// 取发件人邮件字付串
			String fromAdd=propsAll.getProperty( "mail.sender.mail", "" );
			// 发件人邮件地址
			Address from=new InternetAddress(fromAdd);
			// 设置发件人
			message.setFrom( from );
			if(toAddress.length==1)
			{
				// 设置收件人，设置接收类型为To
				message.setRecipient( Message.RecipientType.TO, new InternetAddress(toAddress[0]) );
			}
			else
			{
				// 设置第一个收件人，设置接收类型为To
				message.setRecipient( Message.RecipientType.TO, new InternetAddress(toAddress[0]) );
				Address toAdd[]=new InternetAddress[toAddress.length-1];
				for(int i=0;i<toAddress.length-1;i++)
				{
					toAdd[i]=new InternetAddress(toAddress[i+1]);
				}
				// 设置其它人的接收类型为CC
				message.setRecipients( Message.RecipientType.CC, toAdd );
			}
			//后面的BodyPart 将加入到此处创建的Multipart中去
			Multipart mp=new MimeMultipart();
			MimeBodyPart mpb=new MimeBodyPart();
			//mpb.setDataHandler( new DataHandler( new StringDataSource( content, "text/html" ) ) );
			mpb.setContent( content.toString(), "text/html;charset=GB2312" );
			mp.addBodyPart( mpb );
			//如果名称数组不为空加入附件
			if(attachList!=null&&attachList.size()>0)
			{
				//文件路径
				String fullPath = "";
				HttpServletRequest request=ServletActionContext.getRequest();
				
				for(WebInfoAttach attach:attachList)
				{
					fullPath=request.getRealPath(attach.getAttachPath())+System.getProperty("file.separator")+attach.getAttachName();
					FileDataSource fd=new FileDataSource(new File(fullPath));
					MimeBodyPart mbp=new MimeBodyPart();
					//得到附件本身并加入到BodyPart中
					mbp.setDataHandler( new DataHandler( fd ) );
					//文件名加入到BodyPart中去
					mbp.setFileName(MimeUtility.encodeText(  attach.getAttachOldname() ));
					mp.addBodyPart( mbp );
				}
				
			}
			message.setContent( mp );
			// 设置主体
			message.setSubject( subject );
			// 设置信件内容
			//message.setText( content );
			// 设置发送日期
			message.setSentDate( new Date() );
			// 存储邮件信息
			message.saveChanges();
			// transport是用来发送邮件的
			Transport tr=s.getTransport("smtp");
			// 以smtp方式登录邮箱
			tr.connect( propsAll.getProperty( "mail.smtp.host" ), propsAll.getProperty( "mail.smtp.user" ), propsAll.getProperty( "mail.smtp.password" ));
			// 发送邮件,其中第二个参数是所有已设好的收件人地址
			tr.sendMessage( message,message.getAllRecipients() );
			tr.close();
			flag=true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return flag;
		}
		return flag;
	}
	/**
	 * 根据招标信息类型发送邮件
	 * @param key 招标信息关键字
	 * @param subject　邮件主题
	 * @param content　邮件内容
	 */
	public void sendMail(int type,String subject,String content)throws Exception
	{
		try
		{
			//邮箱地址
			String[] add=getMailList( type );
			if(add!=null&&add.length>0)
			{
				//发送邮箱
				sendMail( subject, content, add );
			}
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
	}
	/**
	 * 从xml文件中取邮箱地址
	 * @param type type为0时取xml文件中全部邮箱，否则取xml节点mail属性type=0的节点
	 * @return
	 * @throws Exception
	 */
	public String[] getMailList(int type)
	{
		String addr[]=null;
		List tempList=null;
		String path=this.path+System.getProperty( "file.separator" )+"buyingweb"+System.getProperty( "file.separator" )+"mailaddress"+"/mail.xml";
		//判断文件是否存在不存则返回null
		File file=new File( path );
		if(!file.exists())
		{
			return null;
		}
		try
		{
			SAXBuilder build=new SAXBuilder();
			Document domcunet=build.build( file );
			XPath xpath=null;
			//节点
			Element element=null;
			//type为0取xml文件中所有邮箱地址
			if(type==0)
			{
				xpath=XPath.newInstance( "//mail" );
			}
			else
			{
				xpath=XPath.newInstance( "//mail[@type='0']" );
			}
			//取xml文件中邮件集合
			tempList=xpath.selectNodes( domcunet );
			if(tempList!=null&&tempList.size()>0)
			{
				addr=new String[tempList.size()];
				for(int i=0;i<tempList.size();i++)
				{
					element=(Element)tempList.get( i );
					addr[i]=element.getAttributeValue( "address" );
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return addr;
		
	}
	public String getPath()
	{
		return path;
	}
	public void setPath( String path )
	{
		this.path = path;
	}
//	public static void main(String [] args)
//	{
//		SendMail ma=new SendMail();
//		ma.sendMail( "邮箱测试44", "测试444", new String[]{"zxyue@grgbanking.com","zxyue1985@163.com"});
//	}
}

