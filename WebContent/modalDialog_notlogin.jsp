<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.opensymphony.xwork2.ActionSupport"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
 String urlStr=request.getParameter("urlStr");
 String title=request.getParameter("title");
 String bundle=request.getParameter("bundle");
 java.util.Map paramsMap = request.getParameterMap();
	 
	if( !paramsMap.isEmpty()&&StringUtils.isNotBlank(urlStr) )
	{
		StringBuffer paramsStr = new StringBuffer(urlStr);
		java.util.Set keySet = paramsMap.keySet();		
		java.util.Iterator it = keySet.iterator();
		String key = null;
		while( it.hasNext() )
		{
			key = ( String )it.next();
			if("urlStr".equals(key)||"title".equals(key)||"bundle".equals(key))
			{
				continue;
			}
			if( paramsStr.toString().indexOf("?")!=-1)
			{
				paramsStr.append("&").append(key).append("=").append(((String[])paramsMap.get( key ))[0]);
			}
			else
			{
				paramsStr.append("?").append(key).append("=").append(((String[])paramsMap.get( key ))[0]);
			}
			
		}
		urlStr=paramsStr.toString();
	}
 if(urlStr==null)
   urlStr="";
 else
   urlStr=java.net.URLDecoder.decode(urlStr);

 if(title==null||title.equals(""))
   title="modalDialog.defTitle";
 
 title = new ActionSupport().getText(title);
%>
<html>
<head>
<%
  if(bundle!=null){
%>
<title><%=title %></title>
<%
 }else{
%>
<title><%=title %></title>
<%
 }
%>
 <script language="JavaScript">
	//非IE版本刷新列表
	function refreshParent(){
		window.opener.query();
	}
	//非IE版本选择人员时，回填值
	function selectPeople(idList,see,hidden){
		window.parent.opener.setSelectPeopleValue(idList,see,hidden);
		window.close();
	}
	//非IE版本选择人员时，点击取消按钮
	function closeSelectPeople(){
		window.close();
	}
	//非ie版本，项目信息管理模块选择人员时，回填值
	function projectSelectPeople(idList,indexvalue){
		window.parent.opener.setPrjSelectPeopleValue(idList,indexvalue);
		window.close();
	}
</script>
</head>
<body style="text-align:center">
	<iframe src="<%=urlStr%>" width="100%" height="100%" frameborder=0></iframe>
</body>
</html>
