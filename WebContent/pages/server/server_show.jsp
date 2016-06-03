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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/server/serverinfo!show.action"   method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="测试工具信息" /></legend>
				<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                <br/>
                    <tr>
                      <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">编号</div></td>  
                      <td bgcolor="#FFFFFF"><input type="text" readonly size="20" style="border-style:none; background-color:#FFFFFF" value='<s:property value="server.deviceNo"/>' />&nbsp;</td>
                      <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">名称</div></td>
                      <td bgcolor="#FFFFFF"><input type="text" readonly size="20" style="border-style:none; background-color:#FFFFFF" value='<s:property value="server.deviceName"/>' />&nbsp;</td> 
                      <td align="center" width="20%" bgcolor="#999999"><div align="center" id="ipNameLable" style="width:63px">IP地址</div></td>
                      <td bgcolor="#FFFFFF"><input type="text" readonly size="20" style="border-style:none; background-color:#FFFFFF" value='<s:property value="server.netIP"/>' />&nbsp;</td>  
                  </tr> 
                    <tr>
                    	<td align="center" bgcolor="#999999"><div align="center">负责人</div></td>
                        <td bgcolor="#FFFFFF"><input type="text" readonly size="20" style="border-style:none; background-color:#FFFFFF" value='<s:property value="server.responsor"/>' />&nbsp;</td> 
                        <td align="center" bgcolor="#999999"><div align="center">用途</div></td>
                        <td bgcolor="#FFFFFF" colspan="3"><s:property value="server.deviceDesc"/>&nbsp;</td> 
                  </tr>
                  <tr>
                        <td align="center" bgcolor="#999999"><div align="center">访问方式</div></td>
                        <td id="visitURL" bgcolor="#FFFFFF"><input type="text" readonly size="20" style="border-style:none; background-color:#FFFFFF" value='<s:property value="server.visitURL"/>' />&nbsp;</td> 
                        <td align="center" bgcolor="#999999"><div align="center" id="ipNameLable">链接地址</div></td>
                        <td colspan="3" bgcolor="#FFFFFF"><textarea id="pageURL" cols="45" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "><s:property value="server.pageURL"/></textarea></td> 
                  </tr>
                </table>
			</fieldset>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
				<br/><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 		</table> 
 	</form>
    <script type="text/javascript">
	for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
	{
		var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
		var tmpStrlen = tmpStr.length;
		var tmpLfCount = tmpStr.split("\n").length;
		var tmpCols = parseInt(document.getElementsByTagName("textarea")[i].cols, 10);
		var calcRows = Math.ceil(tmpStrlen/tmpCols);
		var nosetFlag = false;
		if(calcRows == 0)
		{
			calcRows = 1;
		}
		if(calcRows>=4)
		{
			document.getElementsByTagName("textarea")[i].style.overflowY = "hidden";
			calcRows = 1;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			document.getElementsByTagName("textarea")[i].rows = calcRows + 1;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].rows = calcRows ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script>
</body>  
</html> 