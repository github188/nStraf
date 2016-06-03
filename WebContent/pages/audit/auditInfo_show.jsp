<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head>   </head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript">
function closeModal(){
 window.close();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form action="/pages/audit/auditInfo!show.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	<tr>
	<td>
		<fieldset class="jui_fieldset" width="100%">
		<legend><s:text name="audit.info" /></legend>
		  <s:iterator value="auditInfo" id="auditInfo">
		  	<table width="100%" align="center">
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="audit.info.applyId" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> <s:property value="applyId"/></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="audit.info.creater" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left">
          				<s:property value="applyName" />
          			</td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"><s:text name="audit.info.applydate" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left">
          				<s:property value="applayDate" />
          			</td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="audit.info.applyType" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left">
          				<tm:dataDir beanName="auditInfo" property="applytyp" path="ruleMgr.applyType" />
					</td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="audit.info.orgid" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> 
          				<s:property value="orgId" />
          			</td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="audit.info.auditStatus" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left">
          				<tm:dataDir beanName="auditInfo" property="auditStatus" path="auditMgr.auditStatus" />
          			</td>
          		</tr> 
			</table> 
		  </s:iterator>
		</fieldset>
	</td> 
	</tr> 
	<tr>
		<td align="center"> 
			<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		</td> 
	</tr>  
</table> 
 </form>
  
</body>  
</html> 

