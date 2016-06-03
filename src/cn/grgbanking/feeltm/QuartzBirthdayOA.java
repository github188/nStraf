/**
 * Copyright (c) 2010 Changchun CBIT Co. Ltd.
 * All right reserved.
 * History
 */
/*
 * @(#)QuartzTaskLog.java 1.1 Mar 4, 2010
 */

package cn.grgbanking.feeltm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.grgbanking.feeltm.util.ChineseCalendar;
import cn.grgbanking.feeltm.util.OaOrEmail;

/**
 * app_name:send mail on time
 * @author songsd
 * @version 1.0.0
 */
public class QuartzBirthdayOA extends QuartzJobBean
{
	@Override
	protected void executeInternal( JobExecutionContext arg0 )
			throws JobExecutionException
	{
		//System.out.println(new java.util.Date()+"---------------send oa message by Birthday---------------");
		try {
			getOaData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	private  Map<String,String> getallname() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb=new StringBuffer();
		sb.append("select c_userid,c_username ")
		.append("from sys_user ")
		.append("where level1=0 or level1=1 or level1=2");
		
		
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		
		Map<String,String> es=new HashMap<String,String>();
		while(rs.next()){
			String userid=rs.getString(1);
			String username=rs.getString(2);
			es.put(userid, username);
		}
		conn.close();
		return es;
	}
	/**
	 * 此函数的主要作用是用来显示出状态为‘打开’，且计划完成时间小于系统时间15天的处理者发送oa短信进行提醒
	 * 促其解决问题
	 * @return  所需的uid，以及相应的问题编号列表
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	
	private  void getOaData() throws ClassNotFoundException, SQLException, ParseException{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb=new StringBuffer();
		sb.append("select t.c_userid userid,p.C_USERNAME username,p.C_BIRTHDATE birthdate,p.C_CALENDERTYPE calendertype,p.C_WORKINGDATE workingdate")
		.append(" from sys_info p,sys_user t ") 
		.append("where p.C_USERNAME = t.c_username");
		
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		
		Calendar NowDate= Calendar.getInstance();
		int NowYear = NowDate.get(Calendar.YEAR);
		int NowDay = NowDate.get(Calendar.DAY_OF_MONTH);
		int NowMonth = NowDate.get(Calendar.MONTH)+1;
		ChineseCalendar chineseCalendar = new ChineseCalendar(NowYear,NowMonth,NowDay);
		 SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		while(rs.next()){	
			String userid=rs.getString(1);
			final String Username = rs.getString(2);
			String BirthDay = rs.getString(3);
			String Calendertype = rs.getString(4);	
			String WorkingDay = rs.getString(5);
			Calendar BirthDate = Calendar.getInstance();
			BirthDate.setTime(fo.parse(BirthDay));
			final Calendar WorkingDate = Calendar.getInstance();
			WorkingDate.setTime(fo.parse(WorkingDay));
			int BirthdateMonth = BirthDate.get(Calendar.MONTH)+1;
			int BirthdateDay = BirthDate.get(Calendar.DAY_OF_MONTH);
			int WorkingdateMonth = WorkingDate.get(Calendar.MONTH)+1;
			int WorkingdateDay = WorkingDate.get(Calendar.DAY_OF_MONTH);
				
			if(Calendertype.equals("公历")&& BirthdateMonth== NowMonth && BirthdateDay == NowDay)
			{
				Map<String,String> mapuserid=getallname();  //获得想要的oa数据
				for(final String mapuid:mapuserid.keySet()){				
						
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
								OaOrEmail oe=new OaOrEmail();
								try {
									oe.oaReportB(mapuid, Username);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								};						
							}
						});
						aa.start();
				}
			}
			else if(Calendertype.equals("农历"))
			{
				if(chineseCalendar.getChineseMonth() == BirthdateMonth && chineseCalendar.getChineseDate() == BirthdateDay)
				{
					Map<String,String> mapuserid=getallname();  //获得想要的oa数据
					for(final String mapuid:mapuserid.keySet()){				
						
						Thread aa=new Thread(new Runnable() {
							@Override
							public void run() {
								OaOrEmail oe=new OaOrEmail();
								try {
									oe.oaReportB(mapuid, Username);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								};						
							}
						});
						aa.start();
				}
				}
			}
			if(WorkingdateMonth == NowMonth && WorkingdateDay == NowDay)
			{
				Map<String,String> mapuserid=getallname();  //获得想要的oa数据
				for(final String mapuid:mapuserid.keySet()){				
					
					Thread aa=new Thread(new Runnable() {
						@Override
						public void run() {
							OaOrEmail oe=new OaOrEmail();
							try {
								oe.oaReportC(mapuid, Username,WorkingDate);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							};						
						}
					});
					aa.start();
			}
			}
		
		}
		conn.close();
	}
}

