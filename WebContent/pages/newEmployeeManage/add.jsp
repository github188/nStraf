<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%
	String ctxPath = "http://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<title>certification query</title>
</head>
<script type="text/javascript">
	function save() {
		if ($("#entryInfoDetName").find("option:selected").val() == '全选') {
			alert('请选择部门!');
			return;
		}
		if ($("#identryInfoUserName").find("option:selected").val() == '全选') {
			alert('请选择用户!');
			return;
		}
			
		if (document.getElementById("entryDate").value.trim() == "") {
			alert("请输入入职日期");
			return;
		}
		if (document.getElementById("changeDate").value.trim() == "") {
			alert("请输入转正日期");
			return;
		}
		if (document.getElementById("position").value.trim() == "") {
			alert("请输入岗位信息");
			return;
		}
		if (document.getElementById("teacher").value.trim() == "") {
			alert("请输入督导师");
			return;
		}
		/*var blackScore=document.getElementById("blackScore").value.trim();
		var whiteScore=document.getElementById("whiteScore").value.trim();
		var flag=false;
		if(blackScore!=""){
			if(parseFloat(blackScore)<0 || parseFloat(blackScore)>100){
				alert("笔试成绩，请输入0-100间的数值");
				flag=true;
			}
			if(flag)
				return;
		}
		if(whiteScore!=""){
			if(parseFloat(whiteScore)<0 || parseFloat(whiteScore)>100){
				alert("实操成绩，请输入0-100间的数值");
				flag=true;
			}
			if(flag)
				return;
		}*/
		//确定按钮按下，将项目名称的值放到form里面
		$("#entryInfoGroupNameHidden").val($("#entryInfoGroupName").val());
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		reportInfoForm.submit();

	}
	
	function ismonth(str) {
		for (var ilen = 0; ilen < str.length; ilen++) {
			if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9') {
				if ((str.charAt(ilen) != '.'))
					return false;
			}
		}
		return true;
	}

	function check() {
		if (document.getElementById("certificationNo").value.trim() == "") {
			alert("请输入认定编号");
			$("#certificationNo").focus();
			return;
		}
		var url = "cerinfo!check.action";
		var params = {
			deviceNo : $("#certificationNo").val()
		};
		jQuery.post(url, params, $(document).callbackFun1, 'json');
	}
	$.fn.callbackFun1 = function(json) {

		if (json != null && json == false) {
			//	$("#deviceNo").focus();
			document.getElementById("ok").disabled = true;
			alert("认定编号与之前输入的相同，请重新输入");
			return;
		} else {
			document.getElementById("ok").disabled = false;
			return;
		}
	}

	function selDate(id) {
		var obj = document.getElementById(id);
		ShowDate(obj);
	}

	//页面加载完毕执行
	$(function() {
		//插入一空行
		//insertLastRow();
		//加载并解析模版中的行，然后插入到空行前面
		//var encodeCheckListStr='<s:property value="#encodeCheckListStr"/>';
		//analysisCheckCondtion(UrlDecode(encodeCheckListStr));

		//用户选择改变后，自动赋值给隐藏控件
		$("#identryInfoUserName").change(
			function() {
				var selectedOption = $(this).find("option:selected");
				if (selectedOption.val() != '全选') {
					$("#entryInfoUserIdHidden").val(selectedOption.val());
					$("#entryInfoUserNameHidden").val(selectedOption.text());
					//alert("grp:" + $("#entryInfoGroupName").val())
					//$("#entryInfoGroupNameHidden").val($("#entryInfoGroupName").val());
				} else {
					$("#entryInfoUserIdHidden").val('');
					$("#entryInfoUserNameHidden").val('');
					//$("#entryInfoGroupNameHidden").val('');

				}
			});
		//部门选择改变后，自动复制给隐藏控件
		$("#entryInfoDetName").change(function() {
			var selectedOption = $(this).find("option:selected");
			if (selectedOption.val() != '全选') {
				$("#entryInfoDetNameHidden").val(selectedOption.text());
			} else {
				$("#entryInfoDetNameHidden").val('');

			}
		});
		//隐藏姓名的输入框
		$("#entryInfoUserName").hide();
		$("#identryInfoUserName").show();
	});

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function checkHdl(val) {
		if (val.value.trim() == "" || val.value == null) {
			alert("必填项不能为空");
		}
	}
	
	function selectPeople(see, hidden) {
		var strUrl="/pages/fixasset/fixassetinfo!select.action?see="+see+"&hidden="+hidden;
		var feature="520,380,notidy.addTitle,notify";
	 	var returnValue=OpenModal(strUrl,feature);
	 	if(returnValue!=null && returnValue!=""){
		 	var values = returnValue.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name.substring(0, name.indexOf(",", 0));
		 	//document.getElementById(hidden).value = id; 
	 	}
	}
	function setSelectPeopleValue(idList,see,hidden){
		if(idList!=null && idList!=""){
		 	var values = idList.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name.substring(0, name.indexOf(",", 0));
	 	}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/newEmployeeManage/newEmployeeManageinfo!save.action"
		method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<input type="hidden" name="em.userId" id="entryInfoUserIdHidden" value='' />
						<input type="hidden" name="em.uname" id="entryInfoUserNameHidden" value='' />
						<input type="hidden" name="em.detName" id="entryInfoDetNameHidden" value='' />
						<input type="hidden" name="em.groupName" id="entryInfoGroupNameHidden" value='' />
						<table width="95%" class="input_table">
							<tr>
								<td colspan=6 class="input_tablehead"><s:text name="新员工学习信息" /></td>
							</tr>							
							<tr>
								<tm:deptSelect deptId="entryInfoDetName" deptName="detName"
									groupId="entryInfoGroupName" groupName="groupName"
									userId="entryInfoUserName" userName="uname" deptHeadKey="---请选择部门---"
									deptHeadValue="全选" userHeadKey="---请选择人员---" userHeadValue="全选"
									groupHeadKey="--请选择项目组--" groupHeadValue="全选" type="add"
									labelDept="选择部门 <font color='#FF0000\'>*</font>"
									labelGroup="项目组" labelUser="姓名 <font color='#FF0000\'>*</font>"
									tableClass="width:100%;align:center;border:0;"
									deptLabelClass=" class:input_label2"
									deptClass="bgcolor:#FFFFFF;"
									groupLabelClass=" class:input_label2"
									groupClass="bgcolor:#FFFFFF;"
									userLabelClass="class:input_label2"
									userClass="bgcolor:#FFFFFF;">
								</tm:deptSelect>
							</tr>
							<tr>
								<td class="input_label2"><div
										align="center">
										入职日期<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF"><input
									name="em.entryDate" type="text" id="entryDate" size="22"
									maxlength="20" class="MyInput" /></td>
								<td class="input_label2"><div
										align="center">
										转正日期<font color="#FF0000">*</font>
									</div></td>
								<td colspan=3 bgcolor="#FFFFFF" colspan="3"><input
									name="em.changeDate" type="text" id="changeDate" size="20"
									maxlength="20" class="MyInput" /></td>
							</tr>
							<tr>

								<td  class="input_label2"><div
										align="center">
										岗位<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF">
									<div align="left">
										<s:select list="{'初级工程师','中级工程师','高级工程师'}" name="em.position"
											theme="simple" cssStyle="width:162px;" headerKey=""
											headerValue="----" id="position"></s:select>

									</div>
								</td>
								<td class="input_label2">
									<span align="center">
										督导师<font color="#FF0000">*</font>
									</span>
								</td>
								<td  width="20%" bgcolor="#FFFFFF">
									<input name="em.teacher" type="text" id="teacher" size="11" maxlength="20" value='<s:property value="em.teacher"/>'>
									<input type="button" value="选择" id="zhusong" onClick="selectPeople('teacher','')" />
								</td>
								<td class="input_label2">学习状态<font
									color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF"><select name="em.studyStatus"
									style="width: 141px;" id="studyStatus">
										<option value="待学习">待学习</option>
										<option value="学习中">学习中</option>
										<option value="完成学习">完成学习</option>
								</select></td>
							</tr>
							<!-- 
							<tr>
								<td class="input_label2"><div align="center">笔试成绩</div></td>
								<td bgcolor="#FFFFFF"><input name="em.blackScore"  onkeyup="this.value=this.value.replace(/[^\d\.]/g,'')" onKeyDown="this.value=this.value.replace(/[^\d\.]/g,'')"
									type="text" id="blackScore" size="24" maxlength="24"></td>
								<td class="input_label2"><div align="center">实操成绩</div></td>
								<td bgcolor="#FFFFFF" colspan="3"><input onkeyup="this.value=this.value.replace(/[^\d\.]/g,'')" onKeyDown="this.value=this.value.replace(/[^\d\.]/g,'')"
									name="em.whiteScore" type="text" id="whiteScore" size="22"
									maxlength="20"></td>
							</tr> -->
						</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onclick="save();" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>

	</form>
</body>
</html>
