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
		<table width="95%" align="center" cellPadding="1" cellSpacing="1">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="工作报告信息" /></legend>
				<table width="95%" align="center">
                <tr>
                    <td align="center" width="10%"><div align="center">日期</div></td>  
                    <td><div align="left">
						<!--<input name="createDate"  type="text" id="begin" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="createDate" format="yyyy-MM-dd"/>'> -->
                        <font color="#FF0000"><span><s:date name="createDate" format="yyyy-MM-dd"/></span></font>
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
                          <td nowrap bgcolor="#336699" colspan="2" width="1%"><div align="center"><font color="#FFFFFF">项目名称</font><font color="#FF0000">*</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">完成%</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">状态</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">管理</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">需求</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">设计</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">编码</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">测试</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">其他</font></div></td>
                          <td nowrap width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">工程</font></div></td>
                          <td nowrap bgcolor="#336699" width="8%"><div align="center"><font color="#FFFFFF">小计</font></div></td>
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
                          <td nowrap bgcolor="#CCCCCC" ><div align="center"><font color="#FF0000"><s:property value="subtotal"/>&nbsp;</font></div></td>
                      </tr>
                      <tr>
                          <td nowrap bgcolor="#336699" width="20%"><div align="center"><font color="#FFFFFF">交付件</font></div></td>
                        <td colspan="4" bgcolor="#FFFFFF"><div align="left" id='attr<s:property value="#st.index+1"/>' ><s:property value="attachment"/></div></td>
                          <td colspan="2" bgcolor="#336699" nowrap bgcolor="#FFFFFF"><div align="center" style="width: 70px"><font color="#FFFFFF">&nbsp;偏差原因</font></div></td>
                          <td colspan="6" bgcolor="#FFFFFF"><div align="left" id='rson<s:property value="#st.index+1"/>'><font color="#FF0000"><s:property value="taskReason"/></font></div></td>
                      </tr>
                      <tr>
                          <td bgcolor="#336699" nowrap><div align="center"><font color="#FFFFFF">任务描述</font><font color="#FF0000">*</font></div></td>
                          <td colspan="12" bgcolor="#FFFFFF"><div align="left" id='<s:property value="#st.index+1"/>'><s:property value="taskDesc"/></div></td>
                      </tr>
                  </table>
              </td>
             </tr>
             <hr color="#0000FF" size="4"/>
             <script type="text/javascript">
			 	//var idStr = <s:property value="#st.index+1"/>;
				//var idAttrStr = "attr" + <s:property value="#st.index+1"/>;
				//var idRsonStr = "rson" + <s:property value="#st.index+1"/>;
			 	//document.getElementById(idStr).innerHTML = crlfLine(document.getElementById(idStr).innerHTML, 70, "<br/>");
				//document.getElementById(idAttrStr).innerHTML = crlfLine(document.getElementById(idAttrStr).innerHTML, 20, "<br/>");
				//document.getElementById(idRsonStr).innerHTML = '<font color="#FF0000">' + crlfLine(document.getElementById(idRsonStr).innerHTML, 20, "<br/>") + '</font>';
			 </script>
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
</body>  
</html> 