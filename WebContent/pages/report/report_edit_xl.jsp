<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head>report edit</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
    <script language="javascript" src="../../js/Validator.js"></script>
    <script language="javascript" src="../../js/DateValidator.js"></script>
    <script language="javascript">
		function closeModal(){
		 	window.close();
		}
		function update(){
			tmlInfoForm.submit();
		}
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.name, "Number"))
			{
				document.getElementsByName(itemName.name)[0].value = 0;
				alert("����������");
			}
			else{
				tmp = parseInt(itemName.value);
				if(tmp>20){
					itemName.value = 0;
					alert("������20���ڵ�����");
				}			
			}
			ret += (isNaN(parseInt(document.getElementsByName("management")[0].value))?0:(parseInt(document.getElementsByName("management")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("requirement")[0].value))?0:(parseInt(document.getElementsByName("requirement")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("design")[0].value))?0:(parseInt(document.getElementsByName("design")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("code")[0].value))?0:(parseInt(document.getElementsByName("code")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("test")[0].value))?0:(parseInt(document.getElementsByName("test")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("other")[0].value))?0:(parseInt(document.getElementsByName("other")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("project")[0].value))?0:(parseInt(document.getElementsByName("project")[0].value)));
			if(isNaN(ret)){
				ret = 0;
			}
			document.getElementsByName("subsum")[0].value = ret;
		}
	</script>
	<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name=tmlInfoForm  action="<%=request.getContextPath()%>/pages/tmlInfo/tmlInfo!update.action"   method="post"    >
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head>report edit</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
    <script language="javascript" src="../../js/Validator.js"></script>
    <script language="javascript" src="../../js/DateValidator.js"></script>
    <script language="javascript">
		function closeModal(){
		 	window.close();
		}
		function update(){
			tmlInfoForm.submit();
		}
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.name, "Number"))
			{
				document.getElementsByName(itemName.name)[0].value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseInt(itemName.value);
				if(tmp>20){
					itemName.value = 0;
					alert("请输入20以内的数字");
				}			
			}
			ret += (isNaN(parseInt(document.getElementsByName("management")[0].value))?0:(parseInt(document.getElementsByName("management")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("requirement")[0].value))?0:(parseInt(document.getElementsByName("requirement")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("design")[0].value))?0:(parseInt(document.getElementsByName("design")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("code")[0].value))?0:(parseInt(document.getElementsByName("code")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("test")[0].value))?0:(parseInt(document.getElementsByName("test")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("other")[0].value))?0:(parseInt(document.getElementsByName("other")[0].value)));
			ret += (isNaN(parseInt(document.getElementsByName("project")[0].value))?0:(parseInt(document.getElementsByName("project")[0].value)));
			if(isNaN(ret)){
				ret = 0;
			}
			document.getElementsByName("subsum")[0].value = ret;
		}
	</script>
	<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name=tmlInfoForm  action="<%=request.getContextPath()%>/pages/tmlInfo/tmlInfo!update.action"   method="post"    >
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
					<legend><s:text name="label.newTermInfo" /></legend>
		 			<s:iterator value="tmlInfo" id="tmlInfo">
		 			<input type="hidden" name="id" value="<s:property value='id' />" /> 
					<table width="100%" align="center">
						<tr>
                          <td align="center" width="6%"><div align="center">项目名称</div></td>  
                          <td colspan="4"><input name="prjName" type="text" id="prjName"  size="50" value='<s:property value="prjName"/>' ></td> 
                          <td align="center" width="6%"><div align="center">交付件</div></td>  
                          <td colspan="4"><textarea name="attachment" type="text" id="attachment" rows="2" cols="42"><s:property value="attachment"/></textarea>  </td> 
   				      </tr> 
                        <tr>
                            <td align="center" width="6%"><div align="center">任务描述</div></td>
                            <td colspan="4"><textarea name="prjDesc" type="text" id="prjDesc" rows="3" cols="42" value="" onChange="checkHdl(this)" ><s:property value="prjDesc"/></textarea><font color="#FF0000">*</font>  </td> 
                            <td align="center" width="6%"><div align="center">任务偏差原因</div></td> 
                          <td colspan="4"><textarea name="reason" type="text" id="reason"  rows="3" cols="42" value=""><s:property value="prjDesc"/></textarea> </td> 
                      </tr>
                        <tr>
                            <td align="center" width="6%"><div align="center">完成%</div></td>
                            <td colspan="4" align="center"><div align="left">
                            <select name="finishRate"  id="finishRate" style="width: 315px" >
                            <option value="100">100%</option>
                            <option value="95">95%</option>
                            <option value="90">90%</option>
                            <option value="85">85%</option>
                            <option value="80">80%</option>
                            <option value="75">75%</option>
                            <option value="70">70%</option>
                            <option value="65">65%</option>
                            <option value="60">60%</option>
                            <option value="55">55%</option>
                            <option value="50">50%</option>
                            <option value="45">45%</option>
                            <option value="40">40%</option>
                            <option value="35">35%</option>
                            <option value="30">30%</option>
                            <option value="25">25%</option>
                            <option value="20">20%</option>
                            <option value="15">15%</option>
                            <option value="10">10%</option>
                            <option value="5">5%</option>
                            <option value="0" selected="true">0%</option>
                            </select><font color="#FF0000">*</font>
                            </div></td> 
                            <td align="center" width="6%"><div align="center">状态</div></td> 
                          <td colspan="4" align="center"><div align="left">
                            <select name="status"  id="status" style="width: 315px">
                            <option value="5" selected="true">按时</option>
                            <option value="4">延迟</option>
                            <option value="3">新增</option>
                            <option value="2">取消</option> 
                            <option value="1">暂停</option>         
                            </select><font color="#FF0000">*</font>
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
                            <td><div align="center"><input name="management" type="text" id="management"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="management"/>'></div></td> 
                          <td><div align="center"><input name="requirement" type="text" id="requirement"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="requirement"/>'></div></td> 
                          <td><div align="center"><input name="design" type="text" id="design"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="design"/>'></div></td>
                          <td><div align="center"><input name="code" type="text" id="code"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="code"/>'></div></td>
                          <td><div align="center"><input name="test" type="text" id="test"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="test"/>'></div></td>  
                          <td><div align="center"><input name="other" type="text" id="other"  size="8" maxlength="2" onChange="numVali(this)" value='<s:property value="other"/>'></div></td>
                          <td><div align="center"><input name="project" type="text" id="project"  size="8"  maxlength="2" onChange="numVali(this)" value='<s:property value="project"/>'></div></td>
                          <td><div align="center"><input name="subSum" type="text" id="subSum"  size="8" maxlength="2" readonly="true" disabled="true" value='<s:property value="subSum"/>'></div></td>
					      <td>&nbsp;</td>
                      </tr>
					</table>
                    <script type="text/javascript">
						var szFinishRate = '<s:property value="finishRate"/>';
						var szStatus = '<s:property value="status"/>';
						document.getElementById("finishRate").value = (szFinishRate.trim() == "" ? 0 : szFinishRate.trim());
						document.getElementById("status").value = (szStatus.trim() == "" ? 0 : szStatus.trim());
					</script>
		   			</s:iterator>
					</fieldset>
				</td> 
			</tr> 
			<tr>
				<td align="center"> 
		 		<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
		  	</tr>  
		 </table> 
	 </form>
	  
	</body>  
</html> 
