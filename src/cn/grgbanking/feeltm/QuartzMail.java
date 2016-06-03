/**
 * Copyright (c) 2010 Changchun CBIT Co. Ltd.
 * All right reserved.
 * History
 */
/*
 * @(#)QuartzTaskLog.java 1.1 Mar 4, 2010
 */

package cn.grgbanking.feeltm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * app_name:send mail on time
 * @author songsd
 * @version 1.0.0
 */
public class QuartzMail extends QuartzJobBean
{

	@Override
	protected void executeInternal( JobExecutionContext arg0 )
			throws JobExecutionException
	{
			System.out.println(new java.util.Date()+"---------------send oa message-------------gaga ");
			
////			ma.sendMail( "测试管理平台温馨提示：您记得写周报了吗？", "又到周五了，请大家记得按时填写本周的工作总结和下周的工作计划；如果是月底，请同时填写本月的工作总结和下月的工作计划！",new String[]{"xlei@grgbanking.com"});
//			ma.sendMail( "【测试管理平台】温馨提示：您写周报了吗？", "又到周五了，请大家记得按时填写本周的工作总结和下周的工作计划；如果是月底，请同时填写本月的工作总结和下月的工作计划！", getEmails());
			//
			String[] headmans=null;
			try {
				headmans = getHeadmans();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for(String userid:headmans){
				try {
					oaReport(userid);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
		

	}
	
	private  String[] getEmails() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		PreparedStatement stat=conn.prepareStatement("SELECT c_email FROM sys_user where group_name<>'项目管理组'");
		ResultSet rs=stat.executeQuery();
		List<String> es=new ArrayList<String>();
		while(rs.next()){
			String email=rs.getString(1);
			if(email!=null&&!"".equals(email.trim())){
				es.add(email);
			}
		}
		conn.close();
		String[] ss=new String[es.size()];
		es.toArray(ss);
		return ss;
	}
	
	private String[]  getHeadmans()throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		PreparedStatement stat=conn.prepareStatement("SELECT c_userid FROM sys_user where group_name<>'项目管理组' and level1=1");
		ResultSet rs=stat.executeQuery();
		List<String> es=new ArrayList<String>();
		while(rs.next()){
			String email=rs.getString(1);
			if(email!=null&&!"".equals(email.trim())){
				es.add(email);
			}
		}
		conn.close();
		String[] ss=new String[es.size()];
		es.toArray(ss);
		return ss;
	}
	
	public  void oaReport(String userid) throws IOException {   
		  
        /**  
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
        URL url = new URL("http://10.1.1.2/interface/sms.php?USER_ID=B9E3B5E7&PASSWORD=D4CBCDA8&FROM_ID=tfei&TO_ID=xlei&CONTENT=熊磊&SMS_TYPE=0");   
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
        out.write("username=B9E3B5E7&password=D4CBCDA8&FROM_ID=tfei&TO_ID="+userid+"&CONTENT=请各组长填写周报&SMS_TYPE=0"); //post的关键所在！   
        // remember to clean up   
        out.flush();   
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
        // 传说中的三层包装阿！   
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(   
                l_urlStream));   
        while ((sCurrentLine = l_reader.readLine()) != null) {   
            sTotalString += sCurrentLine + "\r\n";   
  
        }   
        System.out.println(userid+":"+sTotalString);    
    }   

}

