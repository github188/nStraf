<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %><html> 
 <head>   </head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script language="javascript">
function closeModal(){
 window.close();
}
function save(){
window.returnValue=true
auditLogForm.submit();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="auditLogForm" action="<%=request.getContextPath() %>/pages/audit/auditLog!save.action"   method="post"   >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add AuditLog </legend>
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value="" >  </td> 
<td>c_apply_id</td> 
  <td><input name="applyId" type="text" id="applyId"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_userid</td> 
  <td><input name="userid" type="text" id="userid"  size="30" maxlength="20" value="" >  </td> 
<td>c_username</td> 
  <td><input name="username" type="text" id="username"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>d_applay_date</td> 
  <td><input name="applayDate" type="text" id="applayDate"  size="30" maxlength="20" value="" >  </td> 
<td>c_orgid</td> 
  <td><input name="orgid" type="text" id="orgid"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_apply_type</td> 
  <td><input name="applyType" type="text" id="applyType"  size="30" maxlength="20" value="" >  </td> 
<td>c_apply_status</td> 
  <td><input name="applyStatus" type="text" id="applyStatus"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_apply_result</td> 
  <td><input name="applyResult" type="text" id="applyResult"  size="30" maxlength="20" value="" >  </td> 
<td>c_apply_note</td> 
  <td><input name="applyNote" type="text" id="applyNote"  size="30" maxlength="20" value="" >  </td> 
 </tr></table>
</fieldset></td> </tr> <tr><td align="center"> 
 <input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onclick="save()"  image="../../images/share/yes1.gif"> 
<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

