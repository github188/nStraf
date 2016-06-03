<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>addOrModify</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript">
/*  function CloseWin(){
     window.close()
 } */
 function  SubmitForm(formInfo)
 {
   window.returnValue=true
   return true
 }
</script>
<body id="bodyid">	
<% 
String menuid=request.getAttribute("temp_menuid").toString();
%>
<form action="<%=request.getContextPath() %>/pages/menu/MenuInfo!saveOperate.action?menuid=<%=menuid%>" method="post" onsubmit="return SubmitForm(this)">
	
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
	<tr>
		<td width="25" valign="middle">
			&nbsp;
			<img src="../../images/share/list.gif" width="14" height="16">
		</td>
		<td class="orarowhead">
			<s:text name="menuInfo.title.setting" />
		</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
	<%int i = 1;%>
	<s:iterator id="menuOper" value="menuOperLst">
	
	 <input  type="hidden" name="menuid" value='<%=request.getAttribute("temp_menuid") %>'/>
		<tr id="<%="tr"+ ++i%>" class="oracletdone" onclick="pass(downloadList,<%="tr"+ i%>)">
			<td width="10%">
				<div align="center">
					<input type="checkbox" <s:property value="checked"/>  name="checkbox" value='<s:property value="operid"/>' />
				</div>
			</td>
			<td width="20%">
				<s:property value="opername"/>
			</td>						
		</tr>
	</s:iterator>
</table>
<br>
<table width="100%" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
      <fieldset class="jui_fieldset" width="100%">
        <!--operate -->
        <legend><s:text name="label.operate"/></legend>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="center"> 
              <input type="submit" name="btnSave" value='<s:text name="button.submit"/>' class="MyButton"  image="<%=request.getContextPath()%>/images/share/save.gif">
              <input type="button" name="btnClose" value='<s:text name="button.close"/>' class="MyButton"  image="<%=request.getContextPath()%>/images/share/f_closed.gif" onclick="closeModal()">
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>
</form>
</body>
</html>