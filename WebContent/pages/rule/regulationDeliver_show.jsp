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
<form action="/pages/regulationDeliver/regulationDeliver!show.action"   method="post"    >
<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
<tr>
<td>
<fieldset class="jui_fieldset" width="100%">
<legend> add RegulationDeliver </legend>
  <s:iterator value="regulationDeliver" id="regulationDeliver"> 
<table width="100%" align="center">
<tr><td>c_id</td> 
  <td><input name="id" type="text" id="id"  size="30" maxlength="20" value='<s:property value="id"/>' >  </td> 
<td>c_termid</td> 
  <td><input name="termid" type="text" id="termid"  size="30" maxlength="20" value='<s:property value="termid"/>' >  </td> 
 </tr><tr><td>c_role</td> 
  <td><input name="role" type="text" id="role"  size="30" maxlength="20" value='<s:property value="role"/>' >  </td> 
<td>c_version</td> 
  <td><input name="version" type="text" id="version"  size="30" maxlength="20" value='<s:property value="version"/>' >  </td> 
 </tr><tr><td>c_date</td> 
  <td><input name="date" type="text" id="date"  size="30" maxlength="20" value='<s:property value="date"/>' >  </td> 
<td>c_regu_status</td> 
  <td><input name="reguStatus" type="text" id="reguStatus"  size="30" maxlength="20" value='<s:property value="reguStatus"/>' >  </td> 
 </tr><tr><td>c_apply_id</td> 
  <td><input name="applyId" type="text" id="applyId"  size="30" maxlength="20" value='<s:property value="applyId"/>' >  </td> 
<td>c_reg_type</td> 
  <td><input name="regType" type="text" id="regType"  size="30" maxlength="20" value='<s:property value="regType"/>' >  </td> 
 </tr><tr><td>c_deliver_date</td> 
  <td><input name="deliverDate" type="text" id="deliverDate"  size="30" maxlength="20" value='<s:property value="deliverDate"/>' >  </td> 
</table>
</s:iterator>
</fieldset></td> </tr> 
 </table> <br/> <table width="90%" align="center" cellPadding="0" cellSpacing="0">  <tr><td align="center"> 
<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
</td> 
  </tr>  
 </table> 
 </form>
  
</body>  
</html> 

