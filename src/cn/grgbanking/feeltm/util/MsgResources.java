package cn.grgbanking.feeltm.util;


import org.apache.struts.Globals;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

public class MsgResources {

//	------------------------------------------------------------------------------
	 public static String message(HttpServletRequest request,String key)
	  {
		  MessageResources resources = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
		  return resources.getMessage(key);
	  }
//		------------------------------------------------------------------------------
	 public static String message(HttpServletRequest request,String key,String bundle)
	  {
		  MessageResources resources = (MessageResources)request.getAttribute(bundle);
		  return resources.getMessage(key);
	  }
//------------------------------------------------------------------------------
	  public static String message(HttpServletRequest request,String key,Object args[])
	  {
		  MessageResources resources = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
		  return resources.getMessage(key,args);
	  }

//------------------------------------------------------------------------------
	 public static String message(HttpServletRequest request,String key,String bundle,Object args[])
	  {
		  MessageResources resources = (MessageResources)request.getAttribute(bundle);
		  return resources.getMessage(key,args);
	  }

//------------------------------------------------------------------------------
}
