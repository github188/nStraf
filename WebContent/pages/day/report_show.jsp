<!--本页面为show新版页面，修改日期2010-1-5，后台字段未改，加入struts迭代器-->
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
		
		function crlfLine(str, trunNum, cr)
		{
			var testStr = str;
			var allStr = "";
			var start = 0;
			for(start = 0; start<= testStr.length; start+=trunNum)
			{
				allStr = allStr + testStr.substr(start, trunNum) + cr;
			}
			return allStr;
		}
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/report/reportinfo!show.action"   method="post">
		<table width="100%" align="center" cellPadding="1" cellSpacing="1">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="工作报告信息" /></legend>
				<table width="700" align="center">
                <tr>
                    <td align="center" width="10%"><div align="center" style="padding-top:4">日期：</div></td>  
                    <td><div align="left">
						<!--<input name="createDate"  type="text" id="begin" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="createDate" format="yyyy-MM-dd"/>'> -->
                        <font color="#336699"><span><s:date name="createDate" format="yyyy-MM-dd"/></span></font>
					</div></td>
                    <td>
                        <div align="right">工时总计 <font color="#FF0000"><span id="sumShow"><s:property value="subsum"/></span></font> 小时</div>
                    </td>
                </tr>
            </table>
		<s:iterator value="dayReports" id="report" var="report" status="st"> <!--修改为迭代器的id和value-->
			<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" id="task1">
        	<tr>
                <td>
        		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                		<tr>
                          <td nowrap bgcolor="#A5A5A5" colspan="2" width="180"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">完成%</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">状态</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">管理</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">需求</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">设计</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">编码</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">测试</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">其他</font></div></td>
                          <td nowrap width="50" bgcolor="#A5A5A5"><div align="center"><font color="#000000">工程</font></div></td>
                          <td nowrap bgcolor="#A5A5A5" width="50"><div align="center"><font color="#000000">小计</font></div></td>
                      </tr>
                        <tr>
                          <td nowrap bgcolor="#FFFFFF" colspan="2"><div align="center"><s:property value="prjName"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="finishRate"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="status"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="managerment"/>&nbsp;</div></td> 
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="requirement"/>&nbsp;</div></td> 
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="design"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="code"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="test"/>&nbsp;</div></td>  
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="other"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF"><div align="center"><s:property value="project"/>&nbsp;</div></td>
                          <td nowrap bgcolor="#FFFFFF" ><div align="center"><font color="#FF0000"><s:property value="subtotal"/>&nbsp;</font></div></td>
                      </tr>
                     </table>
                     <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                     <tr>
                          <td bgcolor="#A5A5A5" ><div align="center"><font color="#000000">任务描述</font><font color="#FF0000">*</font></div></td>
                          <td colspan="3" bgcolor="#FFFFFF"><div align="left" id='<s:property value="#st.index+1"/>'><textarea  cols= "78" rows="4" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "> <s:property value="taskDesc"/></textarea></div></td>
                      </tr>
                      <tr>
                          <td nowrap bgcolor="#A5A5A5" width="100"><div align="center"><font color="#000000">交付件</font></div></td>
                        <!--<td bgcolor="#FFFFFF" width="340"><div align="left" id='attr<s:property value="#st.index+1"/>'><s:property value="attachment"/></div></td>-->
                        <td bgcolor="#FFFFFF" ><div align="left" id='attr<s:property value="#st.index+1"/>'><textarea  cols= "32" rows="4" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden "><s:property value="attachment"/></textarea></div></td>
                          <td bgcolor="#A5A5A5" bgcolor="#FFFFFF" nowrap width="100" align="center"><div align="center" style="width: 70px"><font color="#000000">偏差原因</font></div></td>
                          <td bgcolor="#FFFFFF" width="340"><div align="left" id='rson<s:property value="#st.index+1"/>'><textarea  cols= "32" rows="4" readonly style= "BORDER-BOTTOM:0px solid; BORDER-LEFT:0px solid; BORDER-RIGHT:0px solid;BORDER-TOP:0px solid;scrollbar-shadow-color:white; overflow-x:hidden;overflow-y:hidden; color:#FF0000"><s:property value="taskReason"/></textarea></div></td>
                      </tr>
                  </table>
             <hr color="#0000FF" size="3"/>
             <script type="text/javascript">
			 	var idStr = <s:property value="#st.index+1"/>;
				var idAttrStr = "attr" + <s:property value="#st.index+1"/>;
				var idRsonStr = "rson" + <s:property value="#st.index+1"/>;
			 	//document.getElementById(idStr).innerHTML = crlfLine(document.getElementById(idStr).innerHTML, 70, "<br/>");
				//document.getElementById(idAttrStr).innerHTML = crlfLine(document.getElementById(idAttrStr).innerHTML, 30, "<br/>");
				//document.getElementById(idRsonStr).innerHTML = '<font color="#FF0000">' + crlfLine(document.getElementById(idRsonStr).innerHTML, 30, "<br/>") + '</font>';
			 </script>
             </td>
             </tr>
			</table>
		</s:iterator>
		</fieldset>
		<script type="text/javascript">
			var szSumShow = '<s:property value="subsum"/>';
			document.getElementById("sumShow").innerHTML = (szSumShow.trim() == "" ? "0" : szSumShow.trim());
		</script>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
                <br/>
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
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
			document.getElementsByTagName("textarea")[i].style.overflowY = "auto";
			calcRows = 4;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			document.getElementsByTagName("textarea")[i].rows = calcRows;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].rows = calcRows + tmpLfCount ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script>
</body>  
</html> 