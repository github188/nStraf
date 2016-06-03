package cn.grgbanking.feeltm.config;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.grgbanking.feeltm.util.DateUtil;

/**
 * 
 * @author lhy
 * 2014-5-9
 * 发送邮件封装
 */
public class MultiMailInfo {
	
	private MimeMessage mimeMsg; //MIME邮件对象   
    private Session session; //邮件会话对象   
    private Properties props; //系统属性   
    //smtp认证用户名和密码   
    private String username;   
    private String password;   
    private Multipart multipart; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    /** 
     * Constructor 
     * @param smtp 邮件发送服务器 
     */  
    public MultiMailInfo(String smtp){   
        setSmtpHost(smtp);   
        createMimeMessage();   
    }

    /** 
     * 设置邮件发送服务器 
     * @param hostName String  
     */  
    public void setSmtpHost(String hostName) {   
        if(props == null)  
            props = System.getProperties(); //获得系统属性对象    
        props.put("smtp.grgbanking.com",hostName); //设置SMTP主机   
    }     
    
    /** 
     * 创建MIME邮件对象   
     * @return 
     */  
    public boolean createMimeMessage()   
    {   
        try {   
            session = Session.getDefaultInstance(props,null); //获得邮件会话对象   
        }   
        catch(Exception e){   
            System.err.println("获取邮件会话对象时发生错误！"+e);   
            return false;   
        }   
        try {   
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象   
            multipart = new MimeMultipart();   
            return true;   
        } catch(Exception e){   
            System.err.println("创建MIME邮件对象失败！"+e);   
            return false;   
        }   
    }

    /**  
     * 设置邮件正文 
     * @param mailBody String  
     */   
    public void setBody(String mailBody) {   
        try{   
            BodyPart bp = new MimeBodyPart();   
            bp.setContent(""+mailBody,"text/html;charset=GBK");   
            multipart.addBodyPart(bp);   
        } catch(Exception e){   
        	System.err.println("设置邮件正文时发生错误！"+e);   
        }   
    }   
    
    /** 
     * 设置邮件主题 
     * @param mailSubject 
     * @return 
     */  
    public void setSubject(String mailSubject) {   
        try{   
            mimeMsg.setSubject(mailSubject);   
        }   
        catch(Exception e) {   
            System.err.println("设置邮件主题发生错误！"); 
            e.printStackTrace();
        }   
    }  
    
    /**  
     * 设置收信人 
     * @param to String  
     */   
    public void setTo(String to){   
        try{   
            mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));   
        } catch(Exception e) {   
        }     
    }   
    
    /**  
     * 设置抄送人 
     * @param copyto String   
     */   
    public void setCopyTo(String copyto)   
    {   
        try{   
        	mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto));   
        }   
        catch(Exception e)   
        { 
        	e.printStackTrace();
        }   
    }   
    
    /**  
     * 设置抄送人   
     * （字符串数组转换为Address[]）
     * @param copyto String   
     */   
    public void setCopyToEmail(String copyto)   
    {   
        try{
        	String[] copys = copyto.split(";");
        	Address[] address = new Address[copys.length];
        	for (String copy : copys) {
        		for (int i=0;i<copys.length;i++){
        			address[i]=new InternetAddress(copys[i]);
        			}
			}
        	mimeMsg.setRecipients(Message.RecipientType.CC,address);
        }   
        catch(Exception e)   
        { 
        	e.printStackTrace();
        }   
    }   
    
    /**  
     * 设置发信人 
     * @param from String  
     */   
    public void setFrom(String from) {   
        try{   
            mimeMsg.setFrom(new InternetAddress(from)); //设置发信人   
        } catch(Exception e) {   
            e.printStackTrace();  
        }   
    }   
    
    /** 
     * 设置SMTP是否需要验证 
     * @param need 
     */  
    public void setNeedAuth(boolean need) {   
        if(props == null) props = System.getProperties();   
        if(need){   
            props.put("mail.smtp.auth","true");   
        }else{   
            props.put("mail.smtp.auth","false");   
        }   
    }

	 /**  
	     * 发送邮件 
	     */   
	    public boolean sendOut()   
	    {   
	        try{   
	            mimeMsg.setContent(multipart);   
	            mimeMsg.saveChanges();   
	            Session mailSession = Session.getInstance(props,null);   
	            Transport transport = mailSession.getTransport("smtp");   
	            transport.connect((String)props.get("smtp.grgbanking.com"),username,password);  
	            if(mimeMsg.getRecipients(Message.RecipientType.TO)!=null){
	            	transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));   
	            }
	            if(mimeMsg.getRecipients(Message.RecipientType.CC)!=null){
	            	transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.CC));   
	            }
	            DateUtil.showLog("发送邮件成功，接收人:"+mimeMsg.getRecipients(Message.RecipientType.TO));
	            transport.close();   
	            return true;   
	        } catch(Exception e) {   
	            System.err.println("邮件发送失败！"+e);   
	            return false;   
	        }   
	    }   
      
	    /** 
	     * 设置用户名和密码 
	     * @param name 
	     * @param pass 
	     */  
	    public void setNamePass(String name,String pass) {   
	        username = name;   
	        password = pass;   
	    }   
	  
}
