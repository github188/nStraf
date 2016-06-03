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
	<form action="/pages/prjdetail/prjdetailinfo!show.action"  method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td width="70%">
		<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="项目附加信息" /></legend>
				<table width="90%" align="center" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#583F70">
                <br/>
                    <tr>
                      <td width="10%" rowspan="2" align="center" bgcolor="#CCCCCC">需求数</td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC" colspan="3">新增</td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC" colspan="3">修改</td>
                      <td align="center" width="10%" bgcolor="#CCCCCC" colspan="3">删除</td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC" colspan="3"><font color="#0033FF">总数</font></td>
                  </tr> 
                    <tr>
                       <td align="center" bgcolor="#FFFFFF" colspan="3"><input name="prjdetail.reqAdd" type="text" id="reqAdd" size="10" value='<s:property value="prjdetail.reqAdd"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                       <td align="center" bgcolor="#FFFFFF" colspan="3"><input name="prjdetail.reqModify" type="text" id="reqModify" size="10" value='<s:property value="prjdetail.reqModify"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                       <td align="center" bgcolor="#FFFFFF" colspan="3"><input name="prjdetail.reqDelete" type="text" id="reqDelete" size="10" value='<s:property value="prjdetail.reqDelete"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                          <td align="center" bgcolor="#FFFFFF" colspan="3"><input name="prjdetail.reqSubtotal" type="text" id="reqSubtotal" size="10" value='<s:property value="prjdetail.reqSubtotal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center; color:#0033FF"></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">用例数</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="4">通过</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="4">失败</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="4"><font color="#0033FF">总数</font></td>
                  </tr> 
                  <tr>
                      <td align="center" bgcolor="#FFFFFF" colspan="4"><input name="prjdetail.casePass" type="text" id="casePass" size="10" value='<s:property value="prjdetail.casePass"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="4"><input name="prjdetail.caseFail" type="text" id="caseFail" size="10" value='<s:property value="prjdetail.caseFail"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="4"><input name="prjdetail.caseSubtotal" type="text" id="caseSubtotal" size="10" value='<s:property value="prjdetail.caseSubtotal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center; color:#0033FF"></td> 
                  </tr> 
                  <tr>
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">缺陷数</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">致命</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">严重</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">一般</td>  
                      <td align="center" bgcolor="#CCCCCC" colspan="2">警告</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">建议</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2"><font color="#0033FF">缺陷总数</font></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><div><input name="prjdetail.bugFatal" type="text" id="bugzm" size="10" value='<s:property value="prjdetail.bugFatal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdetail.bugSerious" type="text" id="bugyz" size="10" value='<s:property value="prjdetail.bugSerious"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdetail.bugGeneral" type="text" id="bugyb" size="10" value='<s:property value="prjdetail.bugGeneral"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdetail.bugWarn" type="text" id="bugjg" size="10" value='<s:property value="prjdetail.bugWarn"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdetail.bugSuguest" type="text" id="bugjy" size="10" value='<s:property value="prjdetail.bugSuguest"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdetail.bugSubtotal" type="text" id="bugSubtotal" size="10" value='<s:property value="prjdetail.bugSubtotal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center; color:#0033FF"></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC">缺陷总值</td> 
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdetail.bugSubvalue" type="text" id="bugzz" size="10" value='<s:property value="prjdetail.bugSubvalue"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td colspan="2" align="center" bgcolor="#CCCCCC">代码行数</td>
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdetail.codeLine" type="text" id="codeLine" size="10" value='<s:property value="prjdetail.codeLine"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC">被拒缺陷数</td> 
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdetail.bugReject" type="text" id="bugRefuse" size="10" value='<s:property value="prjdetail.bugReject"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td colspan="2" align="center" bgcolor="#CCCCCC">关闭缺陷数</td>
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdetail.bugClosed" type="text" id="bugClose" size="10" value='<s:property value="prjdetail.bugClosed"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                  </tr>
                  <tr>
                  	<td align="center" bgcolor="#CCCCCC">备注信息</td>
                   	<td colspan="12" bgcolor="#FFFFFF"><textarea name="prjdetail.note" rows="4" cols="61" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "><s:property value="prjdetail.note"/></textarea></td>
          </tr>
                  <!--<tr>
                      <td width="20%" align="center" bgcolor="#CCCCCC">需求数</td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                  </tr> -->
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