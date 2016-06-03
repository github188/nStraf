<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 	<head>
 		<title><s:text name="rule.title.info" /></title>
 		<script>
	function showTerm(id){
		var strUrl="/pages/rule/repeatRegulation!show.action?ids="+id+"&menuid=<%=request.getParameter("menuid")%>";
		var features="600,500,rule.title.info,rule";
		var resultvalue = OpenModal(strUrl,features);
  		if(resultvalue!=null)
			query();
	}
 		</script>
 	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	
<body>
<form action="/pages/rule/repeatRegulation!info.action"   method="post">
	<s:iterator value="repeatRegulation" id="repeatRegulation">
	<table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" >
		<tr class="trClass0">
 		 <td nowrap  width="16%" align="right"><s:text name="rule.dealwithMode" /><s:text name="label.colon"/></td>
 		 <td nowrap  width="18%"><tm:dataDir beanName="repeatRegulation" property="dealwithMode" path="ruleMgr.processModel"/></td>
 		 
 		 <td nowrap  width="16%" align="right"><s:text name="rule.isLogMode" /><s:text name="label.colon"/></td>
		 <td nowrap  width="17%">
		  	<tm:dataDir beanName="repeatRegulation" property="logMode" path="ruleMgr.logModel"/>
		 </td>
		 
		 <td nowrap  width="16%" align="right"><s:text name="rule.creenMode" /><s:text name="label.colon"/></td>
		 <td nowrap  width="*">
		  <tm:dataDir beanName="repeatRegulation" property="creenMode" path="ruleMgr.greenModel" />
		 </td>
 		</tr>
 		
 		<tr class="trClass1">
 			<td nowrap  width="16%" align="right"><s:text name="rule.enterAccountMode" /><s:text name="label.colon"/></td>
			<td nowrap  width="18%"><tm:dataDir beanName="repeatRegulation" property="enterAccountMode" path="ruleMgr.accountModel" /></td>
			<td nowrap  width="16%" align="right"><s:text name="rule.repeatnum" /><s:text name="label.colon"/></td>
			<td nowrap  width="17%"><s:property value="repeatnum"/></td>
			<td nowrap  width="16%" align="right"><s:text name="rule.termNum"/><s:text name="label.colon"/></td>
			<td nowrap  width="*" ><a href=javascript:showTerm("<s:property value='id' />")> <s:property value="termNum"/> </a></td>
 		</tr>
 		<tr class="trClass0">
 			<!-- <td nowrap  width="16%" align="right"><s:text name="rule.reguStatus" /><s:text name="label.colon"/></td> 
			<td nowrap  width="18%"><tm:dataDir beanName="repeatRegulation" property="regulationStatus" path="ruleMgr.reguDeliverStatus" /></td>-->
			<td nowrap  width="16%" align="right"><s:text name="rule.createTime" /><s:text name="label.colon"/></td>
			<td nowrap  width="18%"><s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
			<td nowrap  width="16%" align="right"><s:text name="rule.createName"/><s:text name="label.colon"/></td>
			<td nowrap  width="17%" > <s:property value="createName"/></td>
			<td nowrap width="16%">&nbsp;</td>
			<td nowrap width="*%">&nbsp;</td>
 		</tr>
 	</table>
 	</s:iterator>
 </form>
  
</body>  
</html> 

