<!--20100107 14:00-->
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
	<form action="/pages/tool/toolinfo!show.action"  method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="测试工具信息" /></legend>
				<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                <br/>
                    <tr>
                      <td align="center" width="20%" bgcolor="#999999"><div align="center">名称：</div></td> 
                      <td bgcolor="#FFFFFF"><input name="tool.toolName" type="text" id="toolName" size="30" maxlength="30" value='<s:property value="tool.toolName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                      <td align="center" width="20%" bgcolor="#999999"><div align="center">版本：</div></td>  
                      <td bgcolor="#FFFFFF"><input name="tool.versionNO" type="text" id="versionNO" size="30" maxlength="30" value='<s:property value="tool.versionNO"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>  
                  </tr> 
                    <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">分类：</div></td>  
                       <td bgcolor="#FFFFFF"><input name="tool.assort" type="text" id="assort" size="30" maxlength="30" value='<s:property value="tool.assort"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">属性：&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="tool.property" type="text" id="property" size="30" maxlength="30" value='<s:property value="tool.property"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                  </tr>
                  <tr>
                            <td align="center" bgcolor="#999999"><div align="center">来源：&nbsp;&nbsp;</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="tool.source" type="text" id="provider" readonly size="82" maxlength="80" value='<s:property value="tool.source"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td> 
                      </tr>
                    <tr>
                        <td align="center" bgcolor="#999999"><div align="center">访问地址<font color="#FF0000">*</font></div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="tool.visitURL" type="text" id="visitURL" size="82" value='<s:property value="tool.visitURL"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td> 
                  </tr>
                  <tr>
                            <td align="center" bgcolor="#999999"><div align="center">工具用途</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><textarea name="tool.toolDesc" type="text" id="toolDesc" cols="70" rows="4" readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; overflow:hidden"><s:property value="tool.toolDesc"/></textarea></td> 
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
	var assort = document.getElementById("assort").value;
	var prop = document.getElementById("property").value;
	var aptmp = "";
	switch(parseInt(assort,10))
	{
		case 0:
			aptmp = "硬件检测";
			break;
		case 1:
			aptmp = "硬件检测(XFS)";
			break;
		case 2:
			aptmp = "介质升级及硬件检测";
			break;
		case 3:
			aptmp = "介质升级";
			break;
		case 4:
			aptmp = "联网测试";
			break;
		case 5:
			aptmp = "自动化测试";
			break;
		case 6:
			aptmp = "白盒测试";
			break;
		case 7:
			aptmp = "辅助测试";
			break;
	}
	document.getElementById("assort").value = aptmp;
	aptmp = "";
	switch(parseInt(prop,10))
	{
		case 0:
			aptmp = "自研";
			break;
		case 1:
			aptmp = "共享";
			break;
		case 2:
			aptmp = "破解";
			break;
		case 3:
			aptmp = "正版";
			break;
		case 4:
			aptmp = "免费";
			break;
	}
	document.getElementById("property").value = aptmp;
	for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
	{
		var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
		var tmpStrlen = tmpStr.length;
		var tmpLfCount = tmpStr.split("\n").length;
		var tmpCols = parseInt(document.getElementsByTagName("textarea")[i].cols);
		var calcRows = Math.ceil(tmpStrlen/tmpCols);
		var nosetFlag = false;
		if(calcRows == 0)
		{
			calcRows = 1;
		}
		if(calcRows>=4)
		{
			document.getElementsByTagName("textarea")[i].style.overflowY = "visible";
			//calcRows = 3;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			//document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			//document.getElementsByTagName("textarea")[i].style.overflowY = "visible";
			document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1 ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
</script>
</body>  
</html> 