package cn.grgbanking.feeltm.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import cn.grgbanking.feeltm.util.OaOrEmail;

/**
 * 
 * @author lhy
 * 2014-6-8
 * 发送邮件 oa 短信功能
 *
 */
public class SendEmailUtil {
	
	public static String OA_URL_ADDRESS = "http://oa.grgbanking.com/interface/sms.php";
	public static String USER_ID = "F9GH3JKL0U7T";//oa发送人
	public static String PASSWORD = "F9GH6JKLOU7T";//oa发送密码
	public static int SMS_TYPE = 0;//oa类型
	public static String HOST = "smtp.grgbanking.com";
	public static String EMAIL_USERAME = "ytxx@grgbanking.com";
	public static String EMAIL_PWD = "grg2014";
	private static boolean NEED_AUTH = true; //smtp是否需要认证
	
	/**
	 * 发送邮件
	 * @param mains
	 * @param copys
	 * @param n
	 */
	public static boolean sendEmail(String mains, String copys,String sender,String title,String content) {
		MultiMailInfo mailInfo = new MultiMailInfo(HOST);
		mailInfo.setNeedAuth(NEED_AUTH); //需要验证  
        mailInfo.setSubject(title);  
        mailInfo.setBody(content);  
        mailInfo.setTo(mains);  
        if(copys!=null){
        	mailInfo.setCopyTo(copys); 
        }
        mailInfo.setFrom(sender);  
        mailInfo.setNamePass(EMAIL_USERAME,EMAIL_PWD);  
        if(!mailInfo.sendOut()) return false;  
        return true;  
	}
	
	
	/**
	 *  
	 * @param mains
	 * @param copys
	 * @param n
	 */
	public static boolean sendEmailtoCopy(String mains, String copys,String sender,String title,String content) {
		MultiMailInfo mailInfo = new MultiMailInfo(HOST);
		mailInfo.setNeedAuth(NEED_AUTH); //需要验证  
        mailInfo.setSubject(title);  
        mailInfo.setBody(content);  
        mailInfo.setTo(mains);  
        if(copys!=null){
        	mailInfo.setCopyToEmail(copys); 
        }
        mailInfo.setFrom(sender);  
        mailInfo.setNamePass(EMAIL_USERAME,EMAIL_PWD);  
        if(!mailInfo.sendOut()) return false;  
        return true;  
	}
	
	public static void oaSendEmail(String sender,String touserids,String content) throws Exception{
		Date now = new Date();
		String urlStr = OA_URL_ADDRESS+"?USER_ID="+USER_ID+"&PASSWORD="+PASSWORD+"&SMS_TYPE="+SMS_TYPE;
        URL url = new URL(urlStr);   
        URLConnection connection = url.openConnection();   
        /**  
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。  
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：  
         */  
        connection.setDoOutput(true);   
        /**  
         * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...  
         */  
        OutputStreamWriter out = new OutputStreamWriter(connection   
                .getOutputStream(), "utf-8");   
        out.write("FROM_ID="+sender+"&TO_ID="+touserids+"&CONTENT="+content); //post的关键所在！   
        out.flush();   
        // remember to clean up   
        out.close();   
        /**  
         * 这样就可以发送一个看起来象这样的POST：   
         * POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:  
         * text/plain Content-type: application/x-www-form-urlencoded  
         * Content-length: 99 username=bob password=someword  
         */  
        // 一旦发送成功，用以下方法就可以得到服务器的回应：   
        String sCurrentLine;   
        String sTotalString;   
        sCurrentLine = "";   
        sTotalString = "";   
        InputStream l_urlStream;   
        l_urlStream = connection.getInputStream();   
        // 传说中的三层包装！   
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(   
                l_urlStream));   
        while ((sCurrentLine = l_reader.readLine()) != null) {   
            sTotalString += sCurrentLine + "\\r\\n";   
            System.out.println(sTotalString);
        }  
	}
	
	public static void main(String[] args) throws Exception{
		OaOrEmail oaOrEmail = new OaOrEmail();
		String aaaS = "lhyan3";
		SendEmailUtil.oaSendEmail("lhyan3",aaaS, "运通信息OA测试");
		//oaOrEmail.oaContent(aaaS, "多个发送");
	}

}
