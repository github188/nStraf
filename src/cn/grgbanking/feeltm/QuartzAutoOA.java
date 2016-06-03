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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.grgbanking.feeltm.domain.SysUser;



/**
 * app_name:send mail on time
 * @author songsd
 * @version 1.0.0
 */
public class QuartzAutoOA extends QuartzJobBean
{

	@Override
	protected void executeInternal( JobExecutionContext arg0 )
			throws JobExecutionException
	{
			System.out.println(new java.util.Date()+"---------------send oa message by AutoOa message---------------");
			try {
				getOaData();  //获得想要的oa数据
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
/*	private  Map<String,String> getOaData() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb=new StringBuffer();
		sb.append("select embracer_man,wm_concat(p.description) pdescription_list ")
		.append("from auto_oa")
		.append("where sysdate>=p.effect_start and sysdate<=p.effect_end ")
		.append("group by t.c_userid");
		
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		
		Map<String,String> es=new HashMap<String,String>();
		while(rs.next()){
			String userid=rs.getString(1);
			String pno_list=rs.getString(2);
			es.put(userid, pno_list);
		}
		conn.close();
		return es;
	}
*/	
	private  void getOaData() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb=new StringBuffer();
		sb.append("select embracer_man,send_cycle,send_month,send_week,send_day,send_date,description ")
		.append("from auto_oa ")
		.append("where (to_char(sysdate,'yyyy-MM-dd')>=to_char(effect_start,'yyyy-MM-dd') and to_char(sysdate,'yyyy-MM-dd')<=to_char(effect_end,'yyyy-MM-dd')) or to_char(sysdate,'yyyy-MM-dd')>=to_char(effect_start,'yyyy-MM-dd') and effect_end is null");
		//.append("group by t.c_userid");
		//System.out.println(sb.toString());	
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		
	/*	Map<String,String> es=new HashMap<String,String>();
		while(rs.next()){
			String userid=rs.getString(1);
			String pno_list=rs.getString(2);
			es.put(userid, pno_list);
		}*/
		Map<String,SysUser> map=getEmails();
		while(rs.next()){
			String embracerMan = rs.getString(1);
			String sendCycle = rs.getString(2);	
			String sendMonth = rs.getString(3);
			String sendWeek = rs.getString(4);
			String sendDay = rs.getString(5);
			String sendDate = rs.getString(6);
			String description = rs.getString(7);
			Calendar c = Calendar.getInstance(); 
			c.setTime(new Date());             
		    int p_monthNum = c.get(Calendar.MONTH)+1;
		    int p_dayNum = c.get(Calendar.DAY_OF_MONTH);
		    int p_hourNum=c.get(Calendar.HOUR_OF_DAY);         
		    int p_minuteNum=c.get(Calendar.MINUTE);
		    String p_monthStr = "";
		    String p_dayStr = "";
		    String p_dateStr = "";
		    String p_weekStr = "";
		    p_weekStr = getWeekOfDate(new Date());
		    SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
		    p_dateStr =  f.format(new Date());
		    String[] p_dateAre = p_dateStr.split(":");
		    String[] m_dateAre = sendDate.split(":");
		    if(p_dayNum == 1){
		    	p_dayStr = "1号";
			}
		    else if(p_dayNum == 2){
		    	p_dayStr = "2号";
			}
		    else if(p_dayNum == 3){
		    	p_dayStr = "3号";
			}
		    else if(p_dayNum == 4){
		    	p_dayStr = "4号";
			}
		    else if(p_dayNum == 5){
		    	p_dayStr = "5号";
			}
		    else if(p_dayNum == 6){
		    	p_dayStr = "6号";
			}
		    else if(p_dayNum == 7){
		    	p_dayStr = "7号";
			}
		    else if(p_dayNum == 8){
		    	p_dayStr = "8号";
			}
		    else if(p_dayNum == 9){
		    	p_dayStr = "9号";
			}
		    else if(p_dayNum == 10){
		    	p_dayStr = "10号";
			}
		    else if(p_dayNum == 11){
		    	p_dayStr = "11号";
			}
		    else if(p_dayNum == 12){
		    	p_dayStr = "12号";
			}
		    else if(p_dayNum == 13){
		    	p_dayStr = "13号";
			}
		    else if(p_dayNum == 14){
		    	p_dayStr = "14号";
			}
		    else if(p_dayNum == 15){
		    	p_dayStr = "15号";
			}
		    else if(p_dayNum == 16){
		    	p_dayStr = "16号";
			}
		    else if(p_dayNum == 17){
		    	p_dayStr = "17号";
			}
		    else if(p_dayNum == 18){
		    	p_dayStr = "18号";
			}
		    else if(p_dayNum == 19){
		    	p_dayStr = "19号";
			}
		    else if(p_dayNum == 20){
		    	p_dayStr = "20号";
			}
		    else if(p_dayNum == 21){
		    	p_dayStr = "21号";
			}
		    else if(p_dayNum == 22){
		    	p_dayStr = "22号";
			}
		    else if(p_dayNum == 23){
		    	p_dayStr = "23号";
			}
		    else if(p_dayNum == 24){
		    	p_dayStr = "24号";
			}
		    else if(p_dayNum == 25){
		    	p_dayStr = "25号";
			}
		    else if(p_dayNum == 26){
		    	p_dayStr = "26号";
			}
		    else if(p_dayNum == 27){
		    	p_dayStr = "27号";
			}
		    else if(p_dayNum == 28){
		    	p_dayStr = "28号";
			}
		    else if(p_dayNum == 29){
		    	p_dayStr = "29号";
			}
		    else if(p_dayNum == 30){
		    	p_dayStr = "30号";
			}
		    else if(p_dayNum == 31){
		    	p_dayStr = "31号";
			}
		    
		    
			if(p_monthNum == 1){
				p_monthStr = "1月";
			}
			else if(p_monthNum == 2){
				p_monthStr = "2月";
			}
			else if(p_monthNum == 3){
				p_monthStr = "3月";
			}
			else if(p_monthNum == 4){
				p_monthStr = "4月";
			}
			else if(p_monthNum == 5){
				p_monthStr = "5月";
			}
			else if(p_monthNum == 6){
				p_monthStr = "6月";
			}
			else if(p_monthNum == 7){
				p_monthStr = "7月";
			}
			else if(p_monthNum == 8){
				p_monthStr = "8月";
			}
			else if(p_monthNum == 9){
				p_monthStr = "9月";
			}
			else if(p_monthNum == 10){
				p_monthStr = "10月";
			}
			else if(p_monthNum == 11){
				p_monthStr = "11月";
			}
			else if(p_monthNum == 12){
				p_monthStr = "12月";
			}
		
			try {
				if(sendCycle.equals("按日"))
				{
					 if(p_dateAre[0].equals(m_dateAre[0]) && p_dateAre[1].equals(m_dateAre[1]))
					    {
					    	for(String userid:map.keySet()){
								SysUser user=map.get(userid);
								
								if((embracerMan.indexOf(user.getUsername())!= -1))
								{ 
									oaReport(userid, sendCycle,sendMonth,sendWeek,sendDay,sendDate,description);
								}
							}
					    }
				}
				else if(sendCycle.equals("按周"))
				{
					 if(p_weekStr.equals(sendWeek) && p_dateAre[0].equals(m_dateAre[0]) && p_dateAre[1].equals(m_dateAre[1]))
					    {
					    	for(String userid:map.keySet()){
								SysUser user=map.get(userid);
								
								if((embracerMan.indexOf(user.getUsername())!= -1))
								{ 
									oaReport(userid, sendCycle,sendMonth,sendWeek,sendDay,sendDate,description);
								}
							}
					    }
				}
				else if(sendCycle.equals("按月"))
				{
					 if(p_dayStr.equals(sendDay) && p_dateAre[0].equals(m_dateAre[0]) && p_dateAre[1].equals(m_dateAre[1]))
					    {
					    	for(String userid:map.keySet()){
								SysUser user=map.get(userid);
								
								if((embracerMan.indexOf(user.getUsername())!= -1))
								{ 
									oaReport(userid, sendCycle,sendMonth,sendWeek,sendDay,sendDate,description);
								}
							}
					    }
				}
				else if(sendCycle.equals("按年")){
			
				    if(p_dayStr.equals(sendDay) && p_monthStr.equals(sendMonth) && p_dateAre[0].equals(m_dateAre[0]) && p_dateAre[1].equals(m_dateAre[1]))
				    {
				    	for(String userid:map.keySet()){
							SysUser user=map.get(userid);
							
							if((embracerMan.indexOf(user.getUsername())!= -1))
							{ 
								oaReport(userid, sendCycle,sendMonth,sendWeek,sendDay,sendDate,description);
							}
						}
				    }
				}
				
				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		conn.close();
		
	}
	
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);	 
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	public  void oaReport(String userid,String sendCycle,String sendMonth,String sendWeek,String sendDay,String sendDate,String description) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append(description);
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
        //System.out.println(userid+":"+sTotalString);    
    }  

	public   Map<String,SysUser> getEmails() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		//Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@10.1.89.31:1521:testsysdb","feelstraf","feelstraf");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		PreparedStatement stat=conn.prepareStatement("SELECT c_userid,c_email,level1,c_username,group_name FROM sys_user where group_name<>'项目管理组' order by level1");
		ResultSet rs=stat.executeQuery();
		Map<String,SysUser> es=new HashMap<String,SysUser>();
		while(rs.next()){
			String email=rs.getString(2);
			String userid=rs.getString(1);
			int level=rs.getInt(3);
			String username=rs.getString(4);
			String groupname=rs.getString(5);
			
			SysUser user=new SysUser();
			user.setEmail(email);
			user.setUserid(userid);
			user.setLevel(level);
			user.setUsername(username);
			user.setGroupName(groupname);
			if(email!=null&&!"".equals(email.trim())){
				es.put(userid,user);
			}
		}
		conn.close();
		return es;
	}
}

