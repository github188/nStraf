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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;



/**
 * app_name:send mail on time
 * @author songsd
 * @version 1.0.0
 */
public class QuartzPerformanceOA extends QuartzJobBean
{

	@Override
	protected void executeInternal( JobExecutionContext arg0 )
			throws JobExecutionException
	{
			System.out.println(new java.util.Date()+"---------------send oa message by no reslove problem---------------");
			try {
				Map<String,String> map=getOaData();  //获得想要的oa数据
				for(String uid:map.keySet()){
					String pno_list=map.get(uid);
					try {
						oaReport(uid, pno_list);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
		

	}
	
	/**
	 * 此函数的主要作用是用来显示出状态为‘打开’，且计划完成时间小于系统时间15天的处理者发送oa短信进行提醒
	 * 促其解决问题
	 * @return  所需的uid，以及相应的问题编号列表
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private  Map<String,String> getOaData() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb=new StringBuffer();
		sb.append("select c_userid,c_username ")
		.append("from sys_user ")
		.append("where level1=1 ");
		
		
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		
		Map<String,String> es=new HashMap<String,String>();
		while(rs.next()){
			String userid=rs.getString(1);
			String username=rs.getString(2);
			es.put(userid, username);
		}
		es.put("hjfeng", "何建烽");
		es.put("tfei", "汤飞");
		conn.close();
		return es;
	}
	
	
	public  void oaReport(String userid,String username) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		Date now = new Date();
		SimpleDateFormat d5 = new SimpleDateFormat("yyyy-MM"); //显示日期，周，时间（精确到秒）
    	String str5 = d5.format(now);
		sb.append(username).append(",你好!").append("请确认是否已经完成测试管理平台").append(str5).append("的月度绩效评定。谢谢！");
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
        out.write("username=B9E3B5E7&password=D4CBCDA8&FROM_ID=tfei&TO_ID="+userid+"&CONTENT="+sb.toString()+"&SMS_TYPE=0"); //post的关键所在！   
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
            sTotalString += sCurrentLine + "\\r\\n";   
  
        }   
        System.out.println(userid+":"+sTotalString);    
    }  

}

