package cn.grgbanking.feeltm.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.SessionMap;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;

public class DbUtil {
//	private static final String USER="root";
//	private static final String PWD="1234";
//	private static final String URL="jdbc:mysql://localhost:3306/dangdang";
//	private static final String DRIVER="com.mysql.jdbc.Driver";
	
	private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
	private static Logger log = Logger.getLogger(DbUtil.class);
	private static Properties pro=PropertiesUtil.getPro();
	static{
		try {
			//加载mysql.properties文件中的NAME、PWD、URL、DRIVER信息
			//TODO
			Class.forName(pro.getProperty("DRIVER"));
			log.info("注册驱动");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public synchronized static Connection getConnection(SessionMap session){
		log.info("执行getConnection方法获取connection对象");
		Connection conn=null;
		//Connection conn = connLocal.get();
		try {
			//if(conn == null){
				//conn =  DriverManager.getConnection(pro.getProperty("URL"),pro.getProperty("USER"),pro.getProperty("PWD"));
				ProjectDB prjDB=(ProjectDB)session.get("globalDB");
				String url="jdbc:sqlserver://"+prjDB.getDbIp()+":1433;databaseName="+prjDB.getDbName()+";";
				conn =  DriverManager.getConnection(url,prjDB.getDbUsername(),prjDB.getDbPassword());
//				connLocal.set(conn);
			log.debug(conn);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
	}
	

	
	public synchronized static void closeConnection(Connection  conn){
		log.info("执行closeConnection方法关闭connection对象");
	//	Connection conn = connLocal.get();
		log.debug(conn);
	//	connLocal.set(null);
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}
	
}
