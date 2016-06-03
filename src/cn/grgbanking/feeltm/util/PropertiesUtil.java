package cn.grgbanking.feeltm.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtil {
	private static Properties pro;
	static {
		pro = new Properties();
		try {
			InputStream in=PropertiesUtil.class.getClassLoader().getResourceAsStream("db.properties");
			pro.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Properties getPro() {
		return pro;
	}
	public static void setPro(Properties pro) {
		PropertiesUtil.pro = pro;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getPro().getProperty("USER"));
	}
	
}
