<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title><s:text name="menuOperate.title.add"/></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript">
 function  SubmitForm(formInfo)
 {
   var  returnValue=validateMenuOperateForm(formInfo)
   var newAnOther = document.getElementById("newAnOther").checked;
   menuOperateForm.action="<%=request.getContextPath()%>/pages/menu/MenuOperate!save.action?newAnOther="+newAnOther;
   
   if(returnValue)
   { 
      window.returnValue=true
	 }  
   return returnValue
 }
 
function setFlag()
{
	var flag = menuOperateForm.flag.value;
	if( flag == "add")
		menuOperateForm.newAnOther.checked = true;
}

</script>
<html:errors />
<body id="bodyid"  leftmargin="10" topmargin="10" style="overflow:hidden" onLoad="">

<form name="menuOperateForm" action="<%=request.getContextPath()%>/pages/menu/MenuOperate!save.action" method="post">
  <table width="90%" cellSpacing="0" cellPadding="0" class="popnewdialog1" align="center">
      <tr>
          <td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="menuOperateForm.title" /></legend>
                  <table align="center">
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.id" /><s:text name="label.colon"/></td>
                          <td>
                              <input name="operid" type="text" id="operid" value="" size="30"  maxlength="32">
                              <font color="#FF0000">*</font>
                          </td>
                      </tr>
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateForm.opername" /></td>
                          <td>
                              <input name="opername" type="text" id="opername" value="" size="30"  maxlength="50">
                              <font color="#FF0000">*</font>
                          </td>
                      </tr>
                     <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.clickname" /><s:text name="label.colon"/></td>
                          <td>
                              <input name="clickname" type="text" id="clickname" value="" size="30"  maxlength="200">
                              <font color="#FF0000">*</font>
                          </td>
                      </tr>
                     <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.picpath" /><s:text name="label.colon"/></td>
                          <td>
                              <input name="picpath" type="text" id="picpath" size="30"  maxlength="200" value="">
                             
                          </td>
                      </tr>
                     <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.types" /><s:text name="label.colon"/></td>
                          <td>
            		<tm:tmSelect name="types" id="types"  selType="dataDir" path="menuMgr.buttonTypes" />
				<s:iterator value="menuOperateForm">
				<s:if test="types != null"> 
            <script language="javascript">
                            menuOperateForm.types.value='<s:property value="types"/>';
                            </script>
                            </s:if>
				                       </td>
                      </tr>					  					  
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuOperateInfoForm.keys" /><s:text name="label.colon"/></td>
                          <td>
                              <input name="keys" type="text" id="keys" size="30"  maxlength="2" value="">
                              <input type="hidden" name="flag" value="" />
                          </td>
                      </tr>	
                      <tr>
                          <td width="110" align="right">
                              <s:text name="menuInfo.site" /><s:text name="label.colon"/></td>
                          <td>
                              <tm:tmSelect name="site" id="site"  selType="dataDir" path="menuMgr.buttonSite"/>
				<s:if test="site != null"> 
            <script language="javascript">
                            menuOperateForm.site.value='<s:property value="site"/>';
                            </script>
                            </s:if>
                            </s:iterator>
                          </td>
                      </tr>				  					  					  
                  </table>
              </fieldset>
          </td>
      </tr>
  </table>
<br>
  <table width="90%" cellSpacing="0" cellPadding="0" align="center">
      <tr>
      	<td>
              <fieldset class="jui_fieldset" width="100%">
                  <legend><s:text name="label.title"/></legend>
                  <table width="100%" >
                      <tr>
                          <td><s:text name="label.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>
      </tr>
      <tr>
      	<td align="center">
      		<br>
      		<input type="submit" name="Submit1" value="<s:text name="button.ok"/>" class="MyButton"  image="../../images/share/yes1.gif">
          	<input type="button" name="Submit2" value="<s:text name="button.close"/>" class="MyButton"  onclick="window.close();" image="../../images/share/f_closed.gif">
        	<input type="checkbox" name="newAnOther" id="newAnOther" value="1" >
        	<label for=newAnOther><s:text name="label.addAnOther"/></label>      	
      	</td></tr>
  </table>
</form>

<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>