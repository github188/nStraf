<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>update user password</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript">
function CheckForm()
{  

     if (pwdSaveForm.password.value=="")
    {
        alert('<s:text name="pwdsetting.password"/>');
        return false;
    }
    if (pwdSaveForm.newPassword.value=="")
    {
        alert('<s:text name="pwdsetting.newPassword"/>');
        return false;
    }
    
    
    if (pwdSaveForm.newPassword.value!=pwdSaveForm.newpwdagain.value)
    {
        alert('<s:text name="pwdsetting.differ"/>');
        return false;
    }
    pwdSaveForm.submit();
   
    
}
</script>

<body id="bodyid"  leftmargin="0" topmargin="0">
<%@include file="/inc/navigationBar.inc"%>

<form name="pwdSaveForm" action="<%=request.getContextPath()%>/pages/um/pwdSave!pwdSave">
<table border="0" cellpadding="1" cellspacing="1" class="tablebg" width="80%" align="center">
          <tr>
              <td colspan="2" align="center" class="bgbuttonselect" height="24"><s:text name="pwdsetting.title"/></td>
          </tr>
          <tr> 
            <td height=24 width="30%" class="bgbuttonselect">
			<div align="right"><s:property value="%{getText('pwdsetting.oldpwd')}"/></div></td>
            <td width="70%" class="bgbuttonselect">          
                <input name="password" type="password" id="password" size="40" class="MyInput" value="">
				<font color="red">*</font>
				
			</td>
          </tr>
		  <tr> 
            <td height=24 width="30%" class="bgbuttonselect"><div align="right"><s:property value="%{getText('pwdsetting.newpwd')}"/></div></td>
            <td width="70%" class="bgbuttonselect">         
                <input name="newPassword" type="password" id="newPassword" size="40" class="MyInput" value="">
				<font color="red">*</font>
				
			</td>
          </tr>
		  <tr> 
            <td height=24 width="30%" class="bgbuttonselect"><div align="right"><s:property value="%{getText('pwdsetting.newpwdagain')}"/></div></td>
            <td width="70%" class="bgbuttonselect">          
                <input name="newpwdagain" type="password" id="newpwdagain" size="40" class="MyInput" value="">
				<font color="red">*</font>
				
			</td>
          </tr>
		  <tr> 
              <td colspan="2" bgcolor="#FFFFFF" height="24"> 
              <div align="center"> 
		<input type="button" name="Submit1" value='<s:property value="%{getText('roleInfo.ok')}"/>' class="MyButton"  onClick="CheckForm()" image="../../images/share/yes1.gif">&nbsp;
		<input type="reset" name="reset" value='<s:property value="%{getText('button.reset')}"/>' class="MyButton"  image="../../images/share/refu.gif">
		      </div>
          </tr>
          <tr> 
            <td width="30%" bgcolor="#F5F5F5"> 
              <div align="center"> 
                <table width="93%" border="0" cellspacing="0" cellpadding="0" >
                  <tr> 
                    <td> 
                      <div align="center"><img src="../../images/share/lamp.gif" ></div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
            <td width="70%" bgcolor="#FFFFFF" valign="top"><font color="#0033FF">&nbsp;</font> 
              <div align="left"> 
                <table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
                  <tr> 
                    <td valign="bottom"><font color="#FF0000"><s:property value="%{getText('tips.title')}"/></font></td>
                  </tr>
                  <tr> 
                    <td valign="top"><s:text name="label.admin.content"/>  
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
	</form>
</body>
</html>
