package cn.grgbanking.feeltm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;

@SuppressWarnings("unchecked")
public class MsgBox {

	public static final String KEY = "MSGBOX.KEY";
	// ------------------------------------------------------------------------------

	public static final String TARGET_PARENT = "PARENT";
	public static final String TARGET_SELF = "SELF";
	public static final String TARGET_TOP = "TOP";

	public static final String BUTTON_OK = "OK";

	public static final String BUTTON_RETURN = "RETURN";

	public static final String BUTTON_CLOSE = "CLOSE";

	public static final String BUTTON_NONE = "NONE";

	// ------------------------------------------------------------------------------
	private String target = TARGET_SELF;
	private String backUrl = "/";
	private Map urlParameters;
	private String buttonType = BUTTON_RETURN;
	private Exception errorObj;
	private String id;
	private String msgInfo;// ------------------------------------------------------------------------------

	public MsgBox(HttpServletRequest request, ActionMessages messages) {
		saveMessages(request, messages);
	}

	// ------------------------------------------------------------------------------
	public MsgBox(HttpServletRequest request, String msgKey) {
		// msgInfo=MsgResources.message(request,msgKey);
		msgInfo = msgKey;
		save(request);
	}

	// ------------------------------------------------------------------------------
	public MsgBox(HttpServletRequest request, String msgKey, String bundle) {
		// msgInfo=MsgResources.message(request,msgKey,bundle);
		msgInfo = msgKey;
		save(request);
	}

	// ------------------------------------------------------------------------------

	public MsgBox(HttpServletRequest request, String msgKey, Object[] msgArgs) {
		msgInfo = MsgResources.message(request, msgKey, msgArgs);
		save(request);
	}

	// ------------------------------------------------------------------------------

	public MsgBox(HttpServletRequest request, String msgKey, String bundle,
			Object[] msgArgs) {
		msgInfo = MessageFormat.format(msgKey, msgArgs);
		// System.out.println(msgInfo);
		// msgInfo=MsgResources.message(request,msgKey,bundle,msgArgs);
		// msgInfo=msgKey;
		save(request);
	}

	// ------------------------------------------------------------------------------
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target.toUpperCase();
	}

	// ------------------------------------------------------------------------------
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getBackUrl() {
		try{
		if (urlParameters != null) {
			StringBuffer sbf = new StringBuffer();
			if (backUrl.indexOf("?") > 0)
				sbf.append(backUrl);
			else
				sbf.append(backUrl);

			String key;
			Iterator itr = urlParameters.keySet().iterator();
			String url=null;
			while (itr.hasNext()) 
			{
				url=sbf.toString();
				key = itr.next().toString();
				if(urlParameters.get( key )!=null)
				{
					if(url.indexOf( "?" )>0)
					{
						sbf.append("&").append(key).append("=").append(urlParameters.get(key).toString().trim());
					}
					else
					{
						sbf.append("?").append(key).append("=").append(urlParameters.get(key).toString());
					}
				}
			}
			return sbf.toString();
		}
		}catch(Exception e){e.printStackTrace();}

		return backUrl;
	}

	// ------------------------------------------------------------------------------
	private void saveMessages(HttpServletRequest request,
			ActionMessages messages) {
		// Remove any messages attribute if none are required
		if ((messages == null) || messages.isEmpty()) {
			request.removeAttribute(Globals.MESSAGE_KEY);
			return;
		}

		// Save the messages we need
		request.setAttribute(Globals.MESSAGE_KEY, messages);
		save(request);
	}

	private void save(HttpServletRequest request) {
		request.setAttribute(KEY, this);
	}

	// ------------------------------------------------------------------------------
	public Map getUrlParameters() {
		return urlParameters;
	}

	public void setUrlParameters(Map urlParameters) {
		this.urlParameters = urlParameters;
	}

	// ------------------------------------------------------------------------------

	public void setUrlParameters(HttpServletRequest request) {
		Enumeration em = request.getParameterNames();
		HashMap hm = new HashMap();
		String key;
		while (em.hasMoreElements()) {
			key = em.nextElement().toString();
			if("id".equals( key )&&StringUtils.isBlank( request.getParameter(key) ))
			{
				hm.put( key, getId() );
			}
			else
			{
				hm.put(key, request.getParameter(key).replace("\r\n", "@@"));
//				if(key.equals("atm.space")){
//					hm.put(key, request.getParameter(key).replace("\r\n", "@@"));
//				}else
//				hm.put(key, request.getParameter(key));
			}
		}
		urlParameters = hm;
	}

	// ------------------------------------------------------------------------------
	public Exception getErrorObj() {
		return errorObj;
	}

	public void setErrorObj(Exception errorObj) {
		this.errorObj = errorObj;
	}

	// ------------------------------------------------------------------------------
	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType.toUpperCase();
	}

	// ------------------------------------------------------------------------------
	public String getMsgInfo() {
		return msgInfo;
	}

	public String getErrorStack() {

		if (errorObj == null)
			return null;

		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			errorObj.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "(bad stack2string)".concat(errorObj.getMessage());
		}
	}
	// -------------------------------------------------------------------------------

	public String getId()
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
	}
}
