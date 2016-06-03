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
auditInfoForm.submit();
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="auditInfoForm" action="<%=request.getContextPath() %>/pages/audit/auditInfo!save.action"   method="post"   >
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add AuditInfo </legend>
<table width="100%" align="center">
<tr><td>c_audit_status</td> 
  <td><input name="auditStatus" type="text" id="auditStatus"  size="30" maxlength="20" value="" >  </td> 
<td>c_org_id</td> 
  <td><input name="orgId" type="text" id="orgId"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_apply_name</td> 
  <td><input name="applyName" type="text" id="applyName"  size="30" maxlength="20" value="" >  </td> 
<td>c_applay_date</td> 
  <td><input name="applayDate" type="text" id="applayDate"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_applytyp</td> 
  <td><input name="applytyp" type="text" id="applytyp"  size="30" maxlength="20" value="" >  </td> 
<td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value="" >  </td> 
 </tr><tr><td>c_apply_id</td> 
  <td><input name="applyId" type="text" id="applyId"  size="30" maxlength="20" value="" >  </td> 
</table>
</fieldset></td> </tr> <tr><td align="center"> 
 <input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onclick="save()"  image="../../images/share/yes1.gif"> 
<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

