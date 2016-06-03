<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>Edit</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@include file="/inc/pagination.inc"%>
<script type="text/javascript">
 /* function CloseWin(){
     window.close()
 } */

 function validateMenuInfoForm() {
	 var menuitem = document.getElementById("menuitem").value;
	 var actionto = document.getElementById("actionto").value;
	 if(menuitem == "") {
		 alert("菜单名称不能为空");
		 return false;
	 }
	 if(actionto == "") {
		 alert("响应命令不能为空");
		 return false;
	 }
	 return true;
	 
 }
 
 function  SubmitForm()
 {
   var  returnValue=validateMenuInfoForm();
   //var newAnOther = document.getElementById("newAnOther").checked;
   menuInfoForm.action="<%=request.getContextPath()%>/pages/menu/MenuInfo!save.action";
	   //?newAnOther="+newAnOther;
 
   if(returnValue)
   { 
      window.returnValue=true
	}  
   return returnValue
 }



</script>
<body id="bodyid"  leftmargin="10" topmargin="10" style="overflow:hidden" onLoad="">	
<s:iterator value="menuInfo">
<form name="menuInfoForm" id="menuInfoForm" action="<%=request.getContextPath()%>/pages/menu/MenuInfo!save.action" focus="menuitem" method="post" onSubmit="return SubmitForm()">
<input type="hidden" name="flag" value="<%=request.getParameter("flag") %>"/>
<table width="90%" align="center" cellspacing="0" cellpadding="0" class="popnewdialog1">
  <tr> 
    <td> 
	    <fieldset class="jui_fieldset" width="100%"><!--condition-->
	      <legend>
          <s:if test="menuid == null"> 
	           <s:text name="menuInfo.title.add"/>
	           <input type="hidden" name="control" value="add">
		  </s:if>
          <s:else> 
	           <s:text name="menuInfo.title.modify"/>
	           <input type="hidden" name="control" value="modify">
		  </s:else>
		  
		  </legend> 
      <table>
			
				<input type="hidden" name="menuPath" value='<s:property value="menuPath"/>'>
			  
          <tr> 
            <td width="100" align="right"><s:text name="menuInfoForm.menuname"/><s:text name="label.colon"/> 
            </td>
            <td colspan="3">
              <input name="menuitem" type="text" size="50"  value='<s:property value="menuitem"/>' maxlength="50">
            	<font color="#FF0000">*</font>
            </td>
          </tr>
          <tr> 
            <td width="100" align="right"><s:text name="menuInfoForm.setorder"/><s:text name="label.colon"/> 
            </td>
            <td colspan="3">
			        <input name="actionto" type="text" size="50"  value='<s:property value="actionto"/>' maxlength="100">
			        <font color="#FF0000">*</font>
			      </td>
          </tr>
          <tr> 
            <td width="100" align="right"><s:text name="menuInfoForm.jumpurl"/><s:text name="label.colon"/> 
            </td>
            <td colspan="3">
            	<tm:tmSelect name="target" id="target" style="width:80%" selType="dataDir" path="menuMgr.target" />
				<s:if  test="target != null"> 
            <script language="javascript">
                            menuInfoForm.target.value='<s:property value="target"/>';
                            </script>
                            </s:if>
				<font color="#FF0000">*</font>
			      </td>
          </tr>
          <tr> 
            <td width="100" align="right"><s:text name="menuInfoForm.pic"/><s:text name="label.colon"/> 
            </td>
            <td colspan="3">
            	<input name="pic" type="text" size="50"  value='<s:property value="pic"/>' maxlength="50">
			      </td>
          </tr>
          <tr> 
            <td width="100" align="right">&nbsp;</td>
            <td width="176">
			        <input type="hidden" name="menuid" value='<s:property value="menuid"/>'>
			        <input type="hidden" name="parentid" value='<s:property value="parentid"/>'>
			        <input type="hidden" name="floor" value='<s:property value="floor"/>'>
            </td>
            <td align="right" width="80">&nbsp;&nbsp;</td>
            <td width="124">&nbsp; &nbsp;</td>
          </tr>
        </table>
     </fieldset>
   </td>
  </tr>
</table>
<br>
<table width="90%" align="center" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
      <fieldset class="jui_fieldset" width="100%">
	      <!--tips -->
        <legend><s:text name="tips.title"/></legend>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="left"> 
        		  <s:text name="label.content"/>
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>
<br>
<table width="90%" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
     <!--   <fieldset class="jui_fieldset" width="100%">
      
        <legend><s:text name="label.operate"/></legend>-->
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="center"> 
              <input type="submit" name="btnSave" value='<s:text name="button.ok"/>' class="MyButton"  image="../../images/share/yes1.gif">
              <input type="button" name="btnClose" value='<s:text name="button.close"/>' class="MyButton"  image="../../images/share/f_closed.gif" onclick="closeModal()">
        		  <s:if test="menuid == null"> 
        		  	<input type="checkbox" name="newAnOther" id="newAnOther" value="1">
        		  	<label for=newAnOther><s:text name="menuInfo.label.addAnOther"/></label>
        		  </s:if>
            </td>
          </tr>
        </table>
      <!--  </fieldset>-->
    </td>
  </tr>
</table>
<br>
</form>
</s:iterator>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>