package cn.grgbanking.feeltm.staff.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtil {

	private static Map<String, Object> config = new HashMap<String, Object>();
	private static Properties properties=new Properties();
	static {
		ResourceBundle rb = ResourceBundle.getBundle("viewConfig");
		Enumeration<String> cfgs = rb.getKeys();
		while (cfgs.hasMoreElements()) {
			String key = cfgs.nextElement();
			String val=rb.getString(key);
			config.put(key,val);
		}
		 properties.putAll(config);
	}
	
	public static String get(String key){
		return (String) properties.get(key);
	}
	
}
