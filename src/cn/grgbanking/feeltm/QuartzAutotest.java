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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;



/**
 * app_name:send mail on time
 * @author songsd
 * @version 1.0.0
 */
public class QuartzAutotest extends QuartzJobBean
{
	
	@Override
	protected void executeInternal( JobExecutionContext arg0 )
			throws JobExecutionException
	{
		//System.out.println(new java.util.Date()+": check the queue of autotest...");
		try {
			checkAutotest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkAutotest() throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn =  DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:testsysd","system","system");
		
		StringBuffer sb2=new StringBuffer();
		String runningIP = "";
		sb2.append("select net_ip ")
		.append(" from AUTO_TEST ")
		.append(" where 1=1 ")
		.append(" and status='正在运行' ");
		PreparedStatement stat2=conn.prepareStatement(sb2.toString());
		ResultSet rs2=stat2.executeQuery();
		while(rs2.next())
		{
			runningIP = runningIP + "'" + rs2.getString(1) + "',";
		}
		rs2.close();
		stat2.close();
		if(runningIP!="")
		{
			runningIP = runningIP.substring(0, runningIP.length()-1);
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append("select net_ip, net_port, version_no, id ")
		.append(" from AUTO_TEST ")
		.append(" where start_time in ")
		.append(" (select min(start_time) from AUTO_TEST ")
		.append(" where (status='排队等待' or status='重试执行中') ")
		.append(" group by prj_name) ");
		if(runningIP!="")
		{
			sb.append(" and net_ip not in (" + runningIP + ")");
		}
		
		Runtime rt = Runtime.getRuntime();
		
		PreparedStatement stat=conn.prepareStatement(sb.toString());
		ResultSet rs=stat.executeQuery();
		while(rs.next())
		{
			String szExecParam = "E:\\jdk\\AutoClt.exe  ";
			String uid = rs.getString(4);
			szExecParam = szExecParam + rs.getString(1) + " ";
			szExecParam = szExecParam + rs.getString(2) + " ";
			szExecParam = szExecParam + rs.getString(3) + " "; 
			szExecParam = szExecParam + uid;
			Process proc = rt.exec(szExecParam);
			System.out.println(szExecParam);
			
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null)
			{
				System.out.println(line);
			}
			int iData = proc.waitFor();
			System.out.println("Process exitValue: " + iData);
			
			// update status
			StringBuffer sb1=new StringBuffer();
			sb1.append("update AUTO_TEST set ")
			.append(" status = ? ,")
			.append(" exec_time = ? ")
			.append(" where 1=1")
			.append(" and id='" + uid + "'");
			PreparedStatement stmt1 = conn.prepareStatement(sb1.toString());
			if(iData == 0)
			{
				stmt1.setString(1, "正在运行");
			}
			else
			{
				stmt1.setString(1, "重试执行中");
			}
			stmt1.setTimestamp(2, new java.sql.Timestamp((new java.util.Date()).getTime()));
			stmt1.executeUpdate();
			stmt1.close();
			br.close();
		}
		stat.close();
		rs.close();
		conn.close();
	}
}

