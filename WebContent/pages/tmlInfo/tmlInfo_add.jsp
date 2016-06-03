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
		function save(){
			window.returnValue=true
			tmlInfoForm.submit();
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="tmlInfoForm" action="<%=request.getContextPath() %>/pages/tmlInfo/tmlInfo!save.action" method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
<tr>
<td>
	<fieldset class="jui_fieldset" width="100%">
		<legend><s:text name="label.newTermInfo" /></legend>
		<table width="100%" align="center">
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="page.infoTermid"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"> <input name="termid" type="text" id="termid" size="30" maxlength="15"> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="page.CTermtype"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"><tm:tmSelect name="termtype" id="termtype" selType="dataDir" path="tmlMgr.termType" /> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="page.brand"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"> <tm:tmSelect name="brand" id="brand" selType="dataDir" path="tmlMgr.brand" /> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
        <tr>
        	<td width="30%" align="right">
        		<div align="right"><s:text name="tmlInfo.orgOwner"/></div>
        	</td>
        	<td width="5%" align="left"><s:text name="label.colon"/></td>
        	<td width="65%" align="left">
        	<tm:tmSelectNext name="orgid" id="orgid" style="width:50%" selType="orgid" path="0000000000"/><font color="#FF0000">*</font>
        	</td>
        </tr>
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="page.netaddr"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"> <input name="netaddr" type="text" id="netaddr" size="30" maxlength="15" value=""> 
             	<font color="#FF0000">*</font> 
            </td>
        </tr> 
		<tr>
			<td width="30%" align="right"> 
           		<div align="right"><s:text name="model.equipmentAddress"/></div>
           	</td>
           	<td width="5%"  align="left"><s:text name="label.colon"/></td>
           	<td width="65%"  align="left"> <input name="termaddress" type="text" id="termaddress" size="30" maxlength="15" value=""> 
            </td>
        </tr> 
		</table>
	</fieldset>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
 <tr>  
       <td>
             <fieldset  width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%" >
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>  
  </tr>
	<td align="center"> 
	<br/>
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onclick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

