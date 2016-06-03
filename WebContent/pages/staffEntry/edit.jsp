<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%String ctxPath= "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function save() {
		if(validateForm()){
			document.getElementById("ok").disabled = true;
			window.returnValue = true;
			entryInfoForm.submit();
		}
	}
	
	
	//提交时验证数据是否填写完整
	function validateForm(){
		/* if($("#note").html().length>500){
			var str = $("#note").html();
			str = str.substr(0,499);
			$("#note").html(str);
			alert('备注限制在500字之内!');
			return false;
		} */
		return true;
	}

	//页面加载完毕执行
	$(function(){
		//插入一空行
		insertLastRow();
	});
	

	//增加一行
	function addAgain(){
		if(checkHasInput()){//检查是否有输入框没有填入值
			insertLastRow();
		}
	}
	//删除行
	function delAgain(){
		var selectCount=0;
		//找到table中所有选中的checkBox
		$("#extendStatusTable").find("input[type='checkbox']").each(function(){
			//通过判断<input type='checkbox'>这个控件中是否包含CHECKED字段来判定其是否被选中
			if($(this).parent().html().indexOf('CHECKED')>0){
				selectCount++;
				//获取该checkBox对应的行
				var trObj=$(this).parent().parent().parent();
				//删除这行
				trObj.remove();
			}
		});
		if(selectCount==0){
			alert("请至少选择一行!");
		}
	}
	
	//在最后新建一空行
	function insertLastRow(){
		//获取最后一行
		var rowsCount=document.getElementById("extendStatusTable").rows.length;
		var lastRow=$(document.getElementById ("extendStatusTable").rows [rowsCount-1]);
		//在最后一行后插入一行(插入后新行为最后一行)
		var lastRowHtml=generateLastRowHtml();	
		lastRow.after(lastRowHtml);
	}
	
	//先检查当前页面是否都已经填入值
	function checkHasInput(){
		var rowsCount=document.getElementById("extendStatusTable").rows.length;
		for(var i=0;i<rowsCount;i++){
			var lastRow=document.getElementById ("extendStatusTable").rows[i];
			var typeCol=$(lastRow.cells[1]);//类型
			var contentCol=$(lastRow.cells[2]);//项目
			var jointDeptCol=$(lastRow.cells[3]);//相关部门
			var noteCol=$(lastRow.cells[4]);//状态
			
			//说明：由于每行的name都不一样，这里是查找name属性以checkConditionList开头，以.type(或.content或.jointDept或.note)结尾的input
			if(typeCol.find("input[name^='checkConditionList'][name$='.type']").val()==''){
				alert("请输入类型!");
				return false; 
			}
			if(contentCol.find("input[name^='checkConditionList'][name$='.content']").val()==''){
				alert("请输入项目!");
				return false;
			}
			if(jointDeptCol.find("input[name^='checkConditionList'][name$='.jointDept']").val()==''){
				alert("请输入相关部门!");
				return false;
			}
			if(noteCol.find("input[name^='checkConditionList'][name$='.note']").val()==''){
				alert("请输入状态!");
				return false;
			}
		}
		return true;
	}

	
	//在最后一行产生一新行
	function generateLastRowHtml(){
		var id=guidGenerator();
		var checkConditionListIndex=document.getElementById("extendStatusTable").rows.length-1;
		var html="<tr>";
		html+="<td width='5%' bgcolor='#FFFFFF'><div align='center'><input type='checkBox' name='checkCondition_"+id+"' value='checkCondition_"+id+"'/></div></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].type' type='text' id='' size='21' value=''/></td>";
		html+="<td width='25%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].content' type='text' id='' size='27' value=''/></td>";
		html+="<td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].jointDept' type='text' id='' size='21' value=''/></td>";
		html+="<td width='30%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList["+checkConditionListIndex+"].note' type='text' id='' size='33' value=''/></td>";
		html+="</tr>";
		return html;
	}
	

	//产生GUID
	function guidGenerator() {
	    var S4 = function() {
	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	    };
	    return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
	}

</script>

<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="entryInfoForm" action="<%=request.getContextPath()%>/pages/staffEntry/entryInfo!update.action" method="post">
		<!-- 加入隐藏的id ,表单提交后告知后台更新哪一条数据 -->
		<input name="entryInfo.id" type="hidden"  value='<s:property value="entryInfo.id"/>' />
		<input name="entryInfo.serialNumber" type="hidden"  value='<s:property value="entryInfo.serialNumber"/>' />
		<input name="entryInfo.userId" type="hidden" value='<s:property value="entryInfo.userId"/>' />
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table width="90%" class="input_table">
							<tr>
								<td class="input_tablehead">编辑入职信息</td>
							</tr>
							<tr>
								<td class="input_label2" width="13%" >
									<div align="center">
										所属部门
									</div>
								</td>
								<td class="input_label2" width="20%">
									<input 
									name="entryInfo.detName"  type="text" id="detName" value='<s:property value="entryInfo.detName"/>'
									maxlength="20" size="20" readonly='readonly' class="input_readonly">
								</td>
								<td width="10%" class="input_label2">
									<div align="center">
										姓名 
									</div>
								</td>
								<td class="input_label2" width="20%">
									<input
										name="entryInfo.userName" readonly='readonly' class="input_readonly" type="text" id="userName" value='<s:property value="entryInfo.userName"/>'
										maxlength="20" size="20" >
								</td>
								<td class="input_label2" width="13%"><div align="center">
										所属组别
									</div></td>
								<td width="20%" class="input_label2">
									<input
										name="entryInfo.groupName" readonly='readonly' class="input_readonly" type="text" id="groupName" value='<s:property value="entryInfo.groupName"/>'
										maxlength="20" size="20" >
								</td>
							</tr>
							<tr>
								<td align="center" class="input_label2">
									<div align="center">
										入职日期
									</div>
								</td>
								<td bgcolor="#FFFFFF" >
									<input name="entryInfo.grgBeginDate" type="text" id="grgBeginDate" class="MyInput" value='<s:date name="entryInfo.grgBeginDate" format="yyyy-MM-dd"/>' maxlength="20" size="20"> 
								</td>
								<td  class="input_label2">
									<div align="center">
										转正日期 
									</div>
								</td>
								<td colspan="3" bgcolor="#FFFFFF">
									<input name="entryInfo.regularDate" width="15%" type="text" id="regularDate" class="MyInput"  value='<s:date name="entryInfo.regularDate" format="yyyy-MM-dd"/>' maxlength="20" size="20">
								</td>
								
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										备注 
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea cols="91" onkeyup="limitLen(500,this)" rows="3" name="entryInfo.note" id="note"><s:property value="entryInfo.note"/></textarea>
									</div>
								</td>
							</tr>
							<tr>
								<td  height="67" class="input_label2"	>
									<div align="center">其他检查字段</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5" style="vertical-align: top;">
									<table id="extendStatusTable" width="100%" class="input_table">
										<tr>
											<td width="5%" class="column_label"><div align="center">序号</div></td>
											<td width="20%" class="column_label">
												<div align="center">
													<font color="#000000">类型</font><font color="#FF0000">*</font>
												</div>
											</td>
											<td width="25%" class="column_label">
												<div align="center">
													<font color="#000000">项目</font><font color="#FF0000">*</font>
												</div>
											</td>
											<td width="20%" class="column_label">
												<div align="center">
													<font color="#000000">关联部门</font><font color="#FF0000">*</font>
												</div>
											</td>
											<td width="30%" class="column_label">
												<div align="center">
													<font color="#000000">状态</font><font color="#FF0000">*</font>
												</div>
											</td>
										</tr>
										<s:iterator value="checkConditionList" status="row">
						 				<tr>
						 					 <td width='5%' bgcolor='#FFFFFF'><div align='center'><input type='checkBox' name='<s:property value="id"/>' value='<s:property value="id"/>'/></div></td>
						 					 <td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList[<s:property value="#row.index"/>].type' type='text' id='' size='21' value='<s:property value="type"/>'/></td>
						 					 <td width='25%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList[<s:property value="#row.index"/>].content' type='text' id='' size='27' value='<s:property value="content"/>'/></td>
						 					 <td width='20%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList[<s:property value="#row.index"/>].jointDept' type='text' id='' size='21' value='<s:property value="jointDept"/>'/></td>
						 					 <td width='30%' nowrap='nowrap' bgcolor='#FFFFFF'><input name='checkConditionList[<s:property value="#row.index"/>].note' type='text' id='' size='33' value='<s:property value="note"/>'/></td>
						        		</tr>
										</s:iterator> 
									</table>
									<hr style="line-height:3px;">
									<table width="100%">
										<tr style="vertical-align: top;">
											<td><s:text name="label.admin.content" /></td>
											<td width="6%"><input type="button" value="新增" onClick="addAgain()" /></td>
											<td width="6%"><input type="button" value="删除" onClick="delAgain()" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br /> 
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
				<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
