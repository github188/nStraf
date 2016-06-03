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
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript">
function closeModal(){
 window.close();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form action="/pages/transmgr/transBanknoteSeq!show.action"   method="post"    >
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
					<legend><s:text name="transmgr.check"/></legend>
		  			<s:iterator value="transBanknoteSeq" id="transBanknoteSeq"> 
					<table width="100%" align="center">
						<tr style="padding-top: 10px;">
							<td width="30%" align="right"> <div align="right"><s:text name="watch.termID"/></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="termid"/></td>
		          		</tr>
		          		 
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.journal_no" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="journalNo"/></td>
		          		</tr> 
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.currency" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="currency"/></td>
		          		</tr> 
		          		
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.sequence" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="sequence"/></td>
		          		</tr>
		          		
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.denomination" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="denomination"/></td>
		          		</tr>
		          		
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.seriaNo" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="seriaNo"/></td>
		          		</tr>
		          		
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.trans_date" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="transDate"/></td>
		          		</tr>
						<tr>
							<td width="30%" align="right"> <div align="right"> <s:text name="watch.trans_time" /></div></td>
		          			<td width="5%"  align="left"> <s:text name="label.colon" /></td>
		          			<td width="65%" height="20"  align="left"> <s:property value="tranTime"/></td>
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

