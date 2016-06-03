<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>add group</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
	if(document.getElementById("grpname").value == "")
	{
		alert('<s:text name="roleInfo.check.grpname"/>');
		return false;
	}
	/* if(document.getElementById("grpLevel").value == "")
	{
		alert('请选择数据级别!');
		return false;
	} */
	return true;
}	 

<%-- function closeGrpModal(){
	var theUrl='<%=request.getContextPath()%>/pages/um/userGroup.do';
	window.returnValue=theUrl
	window.close();
} --%>

function closeGrpModal(){
	window.close();
}

function formsubmit(){
	if (validateInfo()){
		userGroupForm.submit();
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<s:form action="/pages/um/userGroup!update.action" method="post" name="userGroupForm" id="userGroupForm" >
<table width="100%" border="0" align="center">
  <tr >
  <td>
	  <fieldset class="jui_fieldset" width="100%">
	        <legend> 修改组别 </legend>
	        <table align="center">
		        <s:iterator value="groupInfo">
				 <tr> 
		            <td width="30%" align="right"><s:text name="grpInfoForm.grpcode"/><s:text name="label.colon"/></td>        
		            <td width="65%">
		            	<s:property value="grpcode"/><input type="hidden" name="grpcode" id="grpcode" value='<s:property value="grpcode"/>'/>
					</td> 
		         </tr>
		          <tr> 
		            <td width="30%" align="right"><s:text name="grpInfoForm.grpname"/><s:text name="label.colon"/></td>        
		            <td width="70%">
					<input type="text" value='<s:property value="grpname"/>' name="grpname" id="grpname" maxlength="50"/><font color="#FF0000">*</font>			
					</td>
		          </tr>
	          	 <%--  <tr> 
		            <td width="30%" align="right">数据级别：</td>        
		            <td width="70%">
		            	<select name="grpLevel" id="grpLevel">
		            		<option value="">请选择...</option>
		            		<option value="0" <s:if test='grpLevel=="0"'>selected</s:if>>1级(查看所有数据)</option>
		            		<option value="1" <s:if test='grpLevel=="1"'>selected</s:if>>2级(查看部门数据)</option>
		            		<option value="2" <s:if test='grpLevel=="2"'>selected</s:if>>3级(查看项目内数据)</option>
		            		<option value="3" <s:if test='grpLevel=="3"'>selected</s:if>>4级(查看个人数据)</option>
		            	</select><font color="#FF0000">*</font>
					</td>
		          </tr>  --%>
	 		   </s:iterator>
	        </table>
	        </fieldset>
        </td>   
  </tr>
  <tr>  
  <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%">
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
    </td>
  </tr>
  
  <tr>
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="button.ok"/>' class="MyButton"  onclick="formsubmit()" image="../../images/share/yes1.gif"> 
        <input type="button" name="cancel" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal()" image="../../images/share/f_closed.gif"> 
      </td>
  </tr>
</table>
</s:form>

</body>
</html>