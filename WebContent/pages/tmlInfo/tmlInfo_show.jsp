<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
		function closeModal(){
	 		window.close();
		}
		
		function modify(){
		    var id = document.getElementById("id").value;
		  	var strUrl="/pages/tmlInfo/tmlInfo!modify.action?id="+id;
			var features="600,500,operInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			window.close();
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/tmlInfo/tmlInfo!show.action"   method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="label.newTermInfo" /></legend>
  				<s:iterator value="tmlInfo" id="tmlInfo"> 
				<table width="100%" align="center">
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="page.infoTermid" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> <s:property value="termid"/></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="page.CTermtype" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"><tm:dataDir beanName="tmlInfo" property="termtype" path="tmlMgr.termType" /></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="page.brand" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"><tm:dataDir beanName="tmlInfo" property="brand" path="tmlMgr.brand" /></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="page.netaddr" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> <s:property value="netaddr"/></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="model.equipmentAddress" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> <s:property value="termaddress"/></td>
          		</tr> 
				<tr>
					<td width="30%" align="right"> <div align="right"> <s:text name="page.infoActiveTime" /></div></td>
          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
          			<td width="65%" height="20"  align="left"> <s:property value="activedate"/></td>
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

