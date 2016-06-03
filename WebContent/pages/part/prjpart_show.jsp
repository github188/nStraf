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
	<form action="/pages/prjparams/prjparamsinfo!show.action"  method="post">
		<table width="70%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="项目附加信息" /></legend>
				<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                <br/>
                	<tr>
                      	<td align="center" bgcolor="#999999" width="35%">项目类别：</td>
                      	<td bgcolor="#FFFFFF"><input name="prjparams.prjType1" type="text" id="prjType1" size="57" maxlength="30" value='<s:property value="prjparams.prjType1"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                      </tr>
                    <tr>
                      <td align="center"bgcolor="#999999"><div align="center">项目名称：</div></td> 
                      <td bgcolor="#FFFFFF"><input name="prjparams.prjName" type="text" id="prjName" size="57" maxlength="30" value='<s:property value="prjparams.prjName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                    </tr>
                    <tr>
                      <td align="center"bgcolor="#999999"><div align="center">项目版本：</div></td>  
                      <td bgcolor="#FFFFFF"><input name="prjparams.versionNO" type="text" id="versionNO" size="57" maxlength="30" value='<s:property value='prjparams.versionNO=="fqst"?"":prjparams.versionNO'/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>  
                  </tr> 
                    <tr>
                       <td align="center" bgcolor="#999999"><div align="center">工程反馈缺陷数：</div></td>  
                       <td bgcolor="#FFFFFF"><input name="prjparams.prjDefect" type="text" id="prjDefect" size="57" maxlength="30" value='<s:property value="prjparams.prjDefect"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                     </tr>
                     <tr>
                       <td align="center" bgcolor="#999999"><div align="center">代码行数：</div></td>  
                          <td bgcolor="#FFFFFF"><input name="prjparams.codeLine" type="text" id="codeLine" size="57" maxlength="30" value='<s:property value="prjparams.codeLine"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none"></td>
                  </tr>
                  <tr>
                      	<td align="center" bgcolor="#999999">备注信息：</td>
                      	<td bgcolor="#FFFFFF"><textarea name="prjparams.note" rows="4" cols="42" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "><s:property value="prjparams.note"/></textarea></td>
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
    <script type="text/javascript">
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
			//calcRows = 4;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			//document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1 ;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].style.overflowY = "visible";
			//document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount + 1 ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script>
</body>  
</html> 