package cn.grgbanking.feeltm.util;

import java.util.ArrayList;   
import java.util.List;   
  
public class MailBean {   

    // 发送邮件服务器   
    private String hostName = "smtp.163.com";   
  
    // 发送邮件服务器端口   
    private int port = 25;   
  
    // 发送者邮箱用户名   
    private String userName = "iss0587";   
  
    // 发送者邮箱密码   
    private String password = "101314";   
    
    // 邮件发送者   
    private String fromAddress = "iss0587@163.com";   
  
    // 收件人地址.多个地址必须以分号（;）隔开   
    private String toAddress = "iss0587@163.com;lzyong1@grgbanking.com"; 
    
    //抄件人地址
    private String cc = "wling1@grgbanking.com";
  
    // 邮件主题   
    private String subject = "JavaMial 测试";   
  
    // 邮件内容   
    private String content = "这是一封JavaMail测试邮件";   
       
    //附件路径列表   
    private List<String> fileList=new ArrayList<String>();   
 
    
    public MailBean(){}
    
    public MailBean(String hostName,int port,String username,String password,String toAddress,String cc,String subject,
    		String content,String fromAddress,List<String>fileList)
    {
    	this.hostName = hostName;
    	this.port = port;
    	this.userName = username;
    	this.password = password;
    	this.toAddress = toAddress;
    	this.cc = cc;
    	this.subject = subject;
    	this.content = content;
    	this.fromAddress = fromAddress;
    	this.fileList = fileList;
    }
       
    public List<String> getFileList() {   
        return fileList;   
    }   
  
    public void setFileList(List<String> fileList) {   
        this.fileList = fileList;   
    }   
  
    public String getFromAddress() {   
        return fromAddress;   
    }   
  
    public void setFromAddress(String fromAddress) {   
        this.fromAddress = fromAddress;   
    }   
  
    public int getPort() {   
        return port;   
    }   
  
    public void setPort(int port) {   
        this.port = port;   
    }   
  
    public String getHostName() {   
        return hostName;   
    }   
  
    public void setHostName(String hostName) {   
        this.hostName = hostName;   
    }   
  
    public String getUserName() {   
        return userName;   
    }   
  
    public void setUserName(String userName) {   
        this.userName = userName;   
    }   
  
    public String getPassword() {   
        return password;   
    }   
  
    public void setPassword(String password) {   
        this.password = password;   
    }   
  
    public String getToAddress() {   
        return toAddress;   
    }   
  
    public void setToAddress(String toAddress) {   
        this.toAddress = toAddress;   
    }   
    
    public String getCC() {   
        return cc;   
    }   
  
    public void setCC(String cc) {   
        this.cc = cc;   
    }   
  
    public String getSubject() {   
        return subject;   
    }   
  
    public void setSubject(String subject) {   
        this.subject = subject;   
    }   
  
    public String getContent() {   
        return content;   
    }   
  
    public void setContent(String content) {   
        this.content = content;   
    }   
  
}  

