<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/prjdb/prjdbinfo!show.action"  method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="数据库配置信息" /></legend>
				<table width="60%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                <br/>
                    <tr>
                      <td align="center" width="30%" bgcolor="#999999"><div align="center">项目名称：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td> 
                      <td bgcolor="#FFFFFF" width="20%"><input name="prjdb.prjName" type="text" id="prjName" size="30" maxlength="30" value='<s:property value="prjdb.prjName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                    </tr>
                    <tr>
                      <td align="center" width="20%" bgcolor="#999999"><div align="center">数据库路径：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>  
                      <td bgcolor="#FFFFFF"><input name="prjdb.dbIp" type="text" id="dbIp" size="30" maxlength="30" value='<s:property value="prjdb.dbIp"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>  
                  </tr> 
                    <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">数据库名称：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>  
                       <td bgcolor="#FFFFFF"><input name="prjdb.dbName" type="text" id="dbName" size="30" maxlength="30" value='<s:property value="prjdb.dbName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                     </tr>
                     <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">数据库用户名：&nbsp;&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjdb.dbUsername" type="text" id="dbUsername" size="30" maxlength="30" value='<s:property value="prjdb.dbUsername"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                  </tr>
                </table>
			</fieldset>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
				<br/><input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 		</table> 
 	</form>
</body>  
</html> 