package cn.grgbanking.feeltm.login.webapp;

import java.util.HashMap;

public class UserOnline {
	
	public static HashMap<String, String> userMap = new HashMap<String, String>();
	
	public static HashMap<String, String> sessionidMap = new HashMap<String, String>();
   
	public static HashMap<String, String> sessionidAndIPMap = new HashMap<String, String>();
	
	public static HashMap<String, String> userAndIPMap = new HashMap<String, String>();
	
	public static void logonOffUser(String sessionid){
		String userid = (String)sessionidMap.get(sessionid);
		userMap.remove(userid);
		sessionidMap.remove(sessionid);
		
		userAndIPMap.remove(userid);
		sessionidAndIPMap.remove(sessionid);
	}
	
	public static void loginUser(String userid, String sessionid, String ip){
		userMap.put(userid, sessionid);
		sessionidMap.put(sessionid, userid);
		
		userAndIPMap.put(userid, ip);
		sessionidAndIPMap.put(sessionid, ip);
	}
	
	//
	public static boolean checkUser(String userid){
		if(userMap.get(userid)!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean checkUserAndIp(String userid, String ip){
		String userip = userAndIPMap.get(userid);
		if(userip!=null && !userip.equals(ip)){
			return true;
		}else{
			return false;
		}
	}
}
