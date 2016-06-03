package cn.grgbanking.feeltm.util;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.AutoOa;
import cn.grgbanking.feeltm.domain.testsys.Instance;
import cn.grgbanking.feeltm.domain.testsys.Meeting;
import cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion;

public class OaOrEmail {
	/**
	 *获得所有的邮件地址
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public   Map<String,SysUser> getEmails() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		//Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@10.1.89.31:1521:testsysdb","feelstraf","feelstraf");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		PreparedStatement stat=conn.prepareStatement("SELECT c_userid,c_email,level1,c_username,group_name FROM sys_user where group_name<>'项目管理组' and level1 != 3 order by level1");
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
	
	//该为新建状态时,即发短信也发邮件，发给除王总外的部门所有人
	public void  sendMailOaByNew(ProblemOrSuggestion suggestion){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			
			sb.append("测试管理平台编号为[").append(suggestion.getPno()).append("]的问题建议（");
			sb.append("状态：").append(suggestion.getStatus()).append("）");
			
			
			sb1.append("提出日期：").append(fo.format(suggestion.getRaise_date())).append("\r\n");
			sb1.append("提出者：").append(suggestion.getRaise_man()).append("\r\n");
			sb1.append("状态：").append(suggestion.getStatus()).append("\r\n");
			sb1.append("概要：").append(suggestion.getSummary()).append("\r\n");
			sb1.append("类别：").append(suggestion.getCategory()).append("\r\n");
			sb1.append("描述：").append(suggestion.getDescription()).append("\r\n");
			sb1.append("建议措施：").append(suggestion.getGiving_solution()).append("\r\n");
			
			//主题   也为oa的内容
			String subject=sb.toString();
			
			//抄送给经理
			String cc="";
			
			//发送给所有普通组员
//			String to="";
			
			for(String userid:map.keySet()){
				SysUser user=map.get(userid);
				//先发oa短信
				if(user.getLevel()==0&&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
					
					cc+=user.getEmail()+",";
				}else if(user.getLevel()==1){ //组长

						sb2.append(user.getEmail()).append(",");
				}
			}
			
			//oaReport("xlei", suggestion);
		//	JavaMail.sendMail("hjfeng@grgbanking.com", "xlei@grgbanking.com", subject, sb1.toString());
			JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void  sendOaByNewAutoOa(AutoOa AutoOas){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
		    String p_dateStr = "";
		    SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
		    p_dateStr =  f.format(new Date());
		    Date p_date;
		    Date a_date;
		    p_date = f.parse(p_dateStr);
		    a_date = f.parse(AutoOas.getSendDate());
		    if(p_date.compareTo(a_date) >= 0)
		    {
		    	for(String userid:map.keySet()){
					SysUser user=map.get(userid);
					
					if((AutoOas.getEmbracerMan().indexOf(user.getUsername())!= -1))
					{ 
						oaReportAutoOa(userid,AutoOas.getDescription());
					}
				}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//该为新建-->打开状态时,即发短信也发邮件，发给部门所有人
	public void  sendMailOaByNewToOpen(ProblemOrSuggestion suggestion){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			
			sb.append("测试管理平台编号为[").append(suggestion.getPno()).append("]的问题建议（");
			sb.append("状态：").append(suggestion.getStatus()).append("）");
			
			sb1.append("提出日期：").append(fo.format(suggestion.getRaise_date())).append("\r\n");
			sb1.append("提出者：").append(suggestion.getRaise_man()).append("\r\n");
			sb1.append("状态：").append(suggestion.getStatus()).append("\r\n");
			sb1.append("概要：").append(suggestion.getSummary()).append("\r\n");
			sb1.append("类别：").append(suggestion.getCategory()).append("\r\n");
			sb1.append("描述：").append(suggestion.getDescription()).append("\r\n");
			sb1.append("建议措施：").append(suggestion.getGiving_solution()).append("\r\n");
			String finishDate="";
			try{
				 finishDate=fo.format(suggestion.getFinishing_date());
			}catch(Exception e){
				finishDate="";
				e.printStackTrace();
			}
			sb1.append("评估意见：").append(suggestion.getMamager_sugggestion()==null?"":suggestion.getMamager_sugggestion()).append("\r\n");
			sb1.append("计划完成日期：").append(finishDate).append("\r\n");
			sb1.append("处理者：").append(suggestion.getResolve_man()==null?"":suggestion.getResolve_man()).append("\r\n");
			sb1.append("价值分：").append(suggestion.getPrice_score()==null?"":suggestion.getPrice_score()).append("\r\n");
			sb1.append("解决措施：").append(suggestion.getSolution()==null?"":suggestion.getSolution()).append("\r\n");
			//主题   也为oa的内容
			String subject=sb.toString();
			
			//抄送给经理
			String cc="";
			
			//发送给所有普通组员
		//	String to="";
			
			for(String userid:map.keySet()){
				SysUser user=map.get(userid);
				//先发oa短信
				if(user.getLevel()==0 &&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
					cc+=user.getEmail()+",";
				}
				else if((!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng")))
				 {
					//to+=user.getEmail()+",";
					sb2.append(user.getEmail()).append(",");
				}
			}		
			JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	//状态变为不解决，或者为已解决,即发短信也发邮件，发给部门所有人

	/**
	 * 状态变为不解决，或者为已解决，发oa短信给所有人
	 * @param suggestion
	 */
	public void sendOaOnly(ProblemOrSuggestion suggestion){
		try {
			Map<String,SysUser> map=getEmails();
			for(String userid:map.keySet()){
				//先发oa短信
				try{
					oaReport(userid, suggestion);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public  void oaReportAutoOa(String userid,String Description) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append(Description);
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
	
	public  void oaReport(String userid,ProblemOrSuggestion suggestion) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append("编号为[").append(suggestion.getPno()).append("]的问题建议（");
		sb.append("状态：").append(suggestion.getStatus()).append("）").append("，详情请查看邮件或测试管理平台的问题建议管理");
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
	
	
	public void oaContent(String userid,String content) throws Exception{
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
        out.write("username=B9E3B5E7&password=D4CBCDA8&FROM_ID=tfei&TO_ID="+userid+"&CONTENT="+content+"&SMS_TYPE=0"); //post的关键所在！   
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
	
	
	public void oaContent(String[] userid,String content) throws Exception{
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
        for(String uid:userid){
        	out.write("username=B9E3B5E7&password=D4CBCDA8&FROM_ID=tfei&TO_ID="+uid+"&CONTENT="+content+"&SMS_TYPE=0"); //post的关键所在！   
        // remember to clean up   
        	out.flush();   
        }
        out.close();   
       
	}
	
	public void  sendMailByMeeting(String main,String copy,Meeting meeting){
		try {
			StringBuffer sb=new StringBuffer();    //邮件主题
			StringBuffer sb1=new StringBuffer();    //接收template.jsp的具体内容
			String content="";    //邮件发送的实际内容
			
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fo1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//邮件主师：   ***会议纪要_2012-06-12
			sb.append(meeting.getSubject()).append("会议纪要_").append(fo.format(meeting.getCurrentDateTime()));
			
			InputStream is=this.getClass().getResourceAsStream("template.jsp");
			BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			while((content=reader.readLine())!=null){
				sb1.append(content);
			}
			content=sb1.toString();
			reader.close();
			
			content=content.replace("${subject}",meeting.getSubject() );
			content=content.replace("${currentDateTime}",fo1.format(meeting.getCurrentDateTime()) );
			content=content.replace("${hour}",meeting.getHour() );
			content=content.replace("${addr}",meeting.getAddr() );
			content=content.replace("${compere}",meeting.getCompere() );
			content=content.replace("${attendPersons}",meeting.getAttendPersons() );
			content=content.replace("${absentPersons}",meeting.getAbsentPersons() );
			content=content.replace("${main}",meeting.getMain() );
			content=content.replace("${copy}",meeting.getCopy());
			content=content.replace("${writer}",meeting.getWriter());
			content=content.replace("${reAudit}",meeting.getReAudit());
			content=content.replace("${sign}",meeting.getSign() );
			content=content.replace("${content}",meeting.getContent() );
		//	content=content.replace("${auditStatus}",meeting.getAuditStatus() );
			
			
			//主题   也为oa的内容
			String subject=sb.toString();
			List<String> fileList=new ArrayList<String>();
			fileList.add("c:\\test.html");

			JavaMail.sendMailByHtml(main,copy, subject, content,fileList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void  sendMailOaByInstanceOpen(Instance instance){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			
			sb.append("测试管理平台新增了编号为[").append(instance.getIno()).append("]的互评管理记录");
			
			sb1.append("提出者：").append(instance.getCreate_man()).append("\r\n");
			sb1.append("概要：").append(instance.getSummary()).append("\r\n");
			sb1.append("类别：").append(instance.getCategory()).append("\r\n");
			sb1.append("接受者：").append(instance.getEmbracer_man()).append("\r\n");
			sb1.append("事件描述：").append("\r\n").append(instance.getDescription()).append("\r\n");
			sb1.append("建议/备注：").append("\r\n").append(instance.getSolution()).append("\r\n");
			
			//主题   也为oa的内容
			String subject=sb.toString();
			
			//抄送给经理
			String cc="";
			
			//发送给所有普通组员
//			String to="";
			
			for(String userid:map.keySet()){
				SysUser user=map.get(userid);
				//先发oa短信
				if(user.getLevel()==0 &&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
					try{
						oaReportByInstanceOpen(userid, instance);
					}catch(Exception e){
						e.printStackTrace();
					}
					cc+=user.getEmail()+",";
				}
				else 
				{ 
					try{
						oaReportByInstanceOpen(userid, instance);
						}catch(Exception e){
							e.printStackTrace();
						}
						sb2.append(user.getEmail()).append(",");
				}
			}
			
			//oaReport("xlei", suggestion);
		//	JavaMail.sendMail("hjfeng@grgbanking.com", "xlei@grgbanking.com", subject, sb1.toString());
			JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void  sendMailOaByInstanceModiOpen(Instance instance){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			
			sb.append("测试管理平台编号为[").append(instance.getIno()).append("]的互评管理记录被更新了");
			
			sb1.append("提出者：").append(instance.getCreate_man()).append("\r\n");
			sb1.append("概要：").append(instance.getSummary()).append("\r\n");
			sb1.append("类别：").append(instance.getCategory()).append("\r\n");
			sb1.append("接受者：").append(instance.getEmbracer_man()).append("\r\n");
			sb1.append("事件描述：").append("\r\n").append(instance.getDescription()).append("\r\n");
			sb1.append("建议/备注：").append("\r\n").append(instance.getSolution()).append("\r\n");
			
			//主题   也为oa的内容
			String subject=sb.toString();
			
			//抄送给经理
			String cc="";
			
			//发送给所有普通组员
//			String to="";
			
			for(String userid:map.keySet()){
				SysUser user=map.get(userid);
				//先发oa短信
				if(user.getLevel()==0 &&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
					try{
						oaReportByInstanceModiOpen(userid, instance);
					}catch(Exception e){
						e.printStackTrace();
					}
					cc+=user.getEmail()+",";
				}
				else 
				{ 
					try{
						oaReportByInstanceModiOpen(userid, instance);
						}catch(Exception e){
							e.printStackTrace();
						}
						sb2.append(user.getEmail()).append(",");
				}
			}
			
			//oaReport("xlei", suggestion);
		//	JavaMail.sendMail("hjfeng@grgbanking.com", "xlei@grgbanking.com", subject, sb1.toString());
			JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	
	public void  sendMailOaByInstanceUnopen(Instance instance){
		try {
			Map<String,SysUser> map=getEmails();//key为userid,value为email
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			
			sb.append("测试管理平台新增了编号为[").append(instance.getIno()).append("]的互评管理记录");
			
			sb1.append("提出者：").append(instance.getCreate_man()).append("\r\n");
			sb1.append("概要：").append(instance.getSummary()).append("\r\n");
			sb1.append("类别：").append(instance.getCategory()).append("\r\n");
			sb1.append("接受者：").append(instance.getEmbracer_man()).append("\r\n");
			sb1.append("事件描述：").append("\r\n").append(instance.getDescription()).append("\r\n");
			sb1.append("建议/备注：").append("\r\n").append(instance.getSolution()).append("\r\n");
			
			//主题   也为oa的内容
			String subject=sb.toString();
			
			//抄送给经理
			String cc="";
			
			//发送给所有普通组员
//			String to="";
			
			for(String userid:map.keySet()){
				SysUser user=map.get(userid);
				//先发oa短信
				if(user.getLevel()==0 &&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
					try{
						oaReportByInstanceOpen(userid, instance);
					}catch(Exception e){
						e.printStackTrace();
					}
					cc+=user.getEmail()+",";
				}
				else if((instance.getEmbracer_man().indexOf(user.getUsername())!= -1))
				{ 
					try{
						oaReportByInstanceOpen(userid, instance);
						}catch(Exception e){
							e.printStackTrace();
						}
						sb2.append(user.getEmail()).append(",");
				}
			}
			
			//oaReport("xlei", suggestion);
		//	JavaMail.sendMail("hjfeng@grgbanking.com", "xlei@grgbanking.com", subject, sb1.toString());
			JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
		public void  sendMailOaByInstanceModiUnopen(Instance instance){
			try {
				Map<String,SysUser> map=getEmails();//key为userid,value为email
				StringBuffer sb=new StringBuffer();
				StringBuffer sb1=new StringBuffer();
				StringBuffer sb2=new StringBuffer();
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
				
				sb.append("测试管理平台编号为[").append(instance.getIno()).append("]的互评管理记录被更新了");
				
				sb1.append("提出者：").append(instance.getCreate_man()).append("\r\n");
				sb1.append("概要：").append(instance.getSummary()).append("\r\n");
				sb1.append("类别：").append(instance.getCategory()).append("\r\n");
				sb1.append("接受者：").append(instance.getEmbracer_man()).append("\r\n");
				sb1.append("事件描述：").append("\r\n").append(instance.getDescription()).append("\r\n");
				sb1.append("建议/备注：").append("\r\n").append(instance.getSolution()).append("\r\n");
				
				//主题   也为oa的内容
				String subject=sb.toString();
				
				//抄送给经理
				String cc="";
				
				//发送给所有普通组员
//				String to="";
				
				for(String userid:map.keySet()){
					SysUser user=map.get(userid);
					//先发oa短信
					if(user.getLevel()==0 &&(!user.getUserid().equals("wqsheng")) && (!user.getUserid().equals("dgfeng"))){  //经理级别
						try{
							oaReportByInstanceModiOpen(userid, instance);
						}catch(Exception e){
							e.printStackTrace();
						}
						cc+=user.getEmail()+",";
					}
					else if((instance.getEmbracer_man().indexOf(user.getUsername())!= -1))
					{ 
						try{
							oaReportByInstanceModiOpen(userid, instance);
							}catch(Exception e){
								e.printStackTrace();
							}
							sb2.append(user.getEmail()).append(",");
					}
				}
				
				//oaReport("xlei", suggestion);
			//	JavaMail.sendMail("hjfeng@grgbanking.com", "xlei@grgbanking.com", subject, sb1.toString());
				JavaMail.sendMail(sb2.toString(), cc, subject, sb1.toString());
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
	}
	
	public  void oaReportByInstanceOpen(String userid,Instance instance) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append("新增了编号为[").append(instance.getIno()).append("]的互评管理记录，详情请查看邮件或测试管理平台的互评管理");
	
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
	
	public  void oaReportByInstanceModiOpen(String userid,Instance instance) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append("编号为[").append(instance.getIno()).append("]的互评管理记录被更新了，详情请查看邮件或测试管理平台的互评管理");
	
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
	
	
	public  void oaReportB(String userid,String pno_list) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
		StringBuffer sb=new StringBuffer();
		sb.append(pno_list).append("童鞋，今天是个很特别的日子，因为是你的生日!在如此值得纪念的日子应该让它更有纪念意义,请我们吃饭为你庆祝吧!祝你每月工资额外多18%,年会上台与叶总握手留影！什么?佳人有约,下次再请?好吧,只能祝愿兽星你福如东海老王八,寿与南山大石头!:)生日快乐!");
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
	
	public  void oaReportC(String userid,String pno_list,Calendar WorkingDate) throws IOException {   
		  
        /**  oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using  
         *  java.net.URL and //java.net.URLConnection  
         */  
	//	SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		Calendar NowDate= Calendar.getInstance();
		int NowYear = NowDate.get(Calendar.YEAR);
		int Yearsub = NowYear - (WorkingDate.get(Calendar.YEAR));
		StringBuffer sb=new StringBuffer();
		if(Yearsub>=1 && Yearsub <5)
		{
			sb.append(Yearsub).append("年前的今天,").append(pno_list).append("来到了广电运通与我们一起努力奋进,不管之前TA来自于哪里，在这").append(Yearsub).append("年时间里，TA为公司、部门创造的价值与贡献，都是不可估量的。软件测试 部全体员工在此衷心感谢TA这些年来的辛勤付出，愿往后能够再接再历，与大家共创佳绩！");
		}
		else if(Yearsub>=5)
		{
			sb.append(Yearsub).append("年前的今天，广电运通来了一个人，TA的名字叫做").append(pno_list).append("，从此他成为了公司品质的代言人、软件BUG的克星 ，TA业务知识专业众人皆知，TA技术能力过硬无人不晓，质量意识差的开发人员想起TA总是愁眉苦脸，资历不深的测试人员总是以TA为终极超越的目标！衷心感谢TA这么些年来神一样的存在，让软件测试部能够与众不同！");
		}
		
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
