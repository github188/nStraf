<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head>report show</head>
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
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="工作报告信息" /></legend>
				<table width="100%" align="center">
                    <tr>
                      <td align="center" width="6%"><div align="center">项目名称</div></td>  
                      <td colspan="4"><textarea name="prjName" type="text" id="prjName" rows="2" cols="42" readonly><s:property value="report.prjName"/></textarea> </td> 
                      <td align="center" width="6%"><div align="center">交付件</div></td>  
                      <td colspan="4"><textarea name="attachment" type="text" id="attachment" rows="2" cols="42" readonly><s:property value="report.attachment"/></textarea>  </td> 
                  </tr> 
                    <tr>
                        <td align="center" width="6%"><div align="center">任务描述</div></td>
                        <td colspan="4"><textarea name="prjDesc" type="text" id="prjDesc" rows="3" cols="42" readonly onChange="checkHdl(this)" ><s:property value="report.taskDesc"/></textarea><font color="#FF0000">*</font>  </td> 
                        <td align="center" width="6%"><div align="center">任务偏差原因</div></td> 
                      <td colspan="4"><textarea name="reason" type="text" id="reason"  rows="3" cols="42" readonly><s:property value="report.taskReason"/></textarea> </td> 
                  </tr>
                    <tr>
                        <td align="center" width="6%"><div align="center">完成%</div></td>
                        <td colspan="4" align="center"><div align="left">
						<input name ="finishRate" type="text" id="finishRate" style="width: 315px" value='<s:property value="report.finishRate"/>' readOnly />
                        </div></td> 
                        <td align="center" width="6%"><div align="center">状态</div></td> 
                      <td colspan="4" align="center"><div align="left">
                        <input type="text" id="status" name="status" style="width: 315px" value='<s:property value="report.status"/>' readOnly />
                        </div></td> 
                  </tr>
                    <tr><td colspan="15">&nbsp;</td></tr>
                    <tr>
                      <td width="6%">&nbsp;</td>
                      <td width="6%"><div align="center">管理</div></td>
                      <td width="6%"><div align="center">需求</div></td>
                      <td width="6%"><div align="center">设计</div></td>
                      <td width="6%"><div align="center">编码</div></td>
                      <td width="6%"><div align="center">测试</div></td>
                      <td width="6%"><div align="center">其他</div></td>
                      <td width="6%"><div align="center">工程</div></td>
                      <td width="6%"><div align="center">小计</div></td>
                      <td width="6%">&nbsp;</td>
                  </tr>
                    <tr>
                        <td><div align="center">&nbsp;</div></td>
                        <td><div align="center"><input name="management" type="text" id="management"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.managerment"/>'></div></td> 
                      <td><div align="center"><input name="requirement" type="text" id="requirement"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.requirement"/>'></div></td> 
                      <td><div align="center"><input name="design" type="text" id="design"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.design"/>'></div></td>
                      <td><div align="center"><input name="code" type="text" id="code"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.code"/>'></div></td>
                      <td><div align="center"><input name="test" type="text" id="test"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.test"/>'></div></td>  
                      <td><div align="center"><input name="other" type="text" id="other"  size="8" maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.other"/>'></div></td>
                      <td><div align="center"><input name="project" type="text" id="project"  size="8"  maxlength="2" onChange="numVali(this)" readonly value='<s:property value="report.project"/>'></div></td>
                      <td><div align="center"><input name="subtotal" type="text" id="subtotal"  size="8" maxlength="2" readonly value='<s:property value="report.subtotal"/>'></div></td>
                      <td>&nbsp;</td>
                  </tr>
                </table>
			</fieldset>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 		</table> 
 	</form>
</body>  
</html> 