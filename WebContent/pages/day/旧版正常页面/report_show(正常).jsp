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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/report/reportinfo!show.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="工作报告信息" /></legend>
				<table width="100%" align="center">
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
		<s:iterator value="dayReports" id="report" var="report"> <!--修改为迭代器的id和value-->
			<table width="100%" align="center" border="1"  id="task1" cellspacing="0">
        	<tr>
                <td>
        		<table width="100%" align="center" border="1" cellspacing="0">
                		<tr>
                          <td colspan="2" rowspan="2" bgcolor="#336699"><div align="center"><font color="#FFFFFF">项目名称</font><font color="#FF0000">*</font></div></td>
                          <td rowspan="2" width="10%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">完成%</font></div></td>
                          <td rowspan="2" width="10%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">状态</font></div></td>
                          <td colspan="7" bgcolor="#336699"><div align="center"><font color="#FFFFFF">工时(小计: </font><font color="#FF0000"><span name="subtotal" id="subtotal"><s:property value="subtotal"/></span></font><font color="#FFFFFF"> 小时)</font></div></td>
                          <!--<td><div align="center">&nbsp;</div></td>-->
                        </tr>
                        <tr>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">管理</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">需求</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">设计</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">编码</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">测试</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">其他</font></div></td>
                          <td width="8%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">工程</font></div></td>
                          <!--<td><div align="center">小计</div></td>-->
                      </tr>
                        <tr>
                          <td colspan="2"><div align="center"><s:property value="prjName"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="finishRate"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="status"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="managerment"/>&nbsp;</div></td> 
                          <td><div align="center"><s:property value="requirement"/>&nbsp;</div></td> 
                          <td><div align="center"><s:property value="design"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="code"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="test"/>&nbsp;</div></td>  
                          <td><div align="center"><s:property value="other"/>&nbsp;</div></td>
                          <td><div align="center"><s:property value="project"/>&nbsp;</div></td>
                          <!--<td><div align="center"><input name="subtotal" type="text" id="subtotal"  size="2" maxlength="2" readonly value='0'></div></td>-->
                      </tr>
                      <tr>
                          <td width="25%" bgcolor="#336699"><div align="center"><font color="#FFFFFF">交付件</font></div></td>
                        <td colspan="3"><div align="left"><s:property value="attachment"/>&nbsp;</div></td>
                          <td colspan="2" bgcolor="#336699"><div align="center"><font color="#FFFFFF">偏差原因</font></div></td>
                          <td colspan="5"><div align="left"><s:property value="taskReason"/>&nbsp;</div></td>
                      </tr>
                      <tr>
                          <td bgcolor="#336699"><div align="center"><font color="#FFFFFF">任务描述</font><font color="#FF0000">*</font></div></td>
                          <td colspan="11"><div align="left"><s:property value="taskDesc"/>&nbsp;</div></td>
                      </tr>
                  </table>
              </td>
             </tr>
             <hr color="#0000FF" />
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