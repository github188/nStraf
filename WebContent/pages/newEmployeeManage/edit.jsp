<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {

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
		/*
		var blackScore=document.getElementById("blackScore").value.trim();
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
		}
		*/
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
		action="<%=request.getContextPath()%>/pages/newEmployeeManage/newEmployeeManageinfo!update.action"
		method="post">
		<input name="em.id" type="hidden" id="id"
			value='<s:property value="em.id"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table width="95%" class="input_table">
							<tr>
								<td class="input_tablehead"><s:text name="新员工学习信息" /></td>
							</tr>
							<tr>
								<td width="9%" class="input_label2"><div
										align="center">
										姓名<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF" nowrap="nowrap" width="21%"><input
									name="em.uname" type="text" id="uname" size="20" maxlength="20" readonly
									disable class="input_readonly" value='<s:property value="em.uname"/>'></td>
								<td width="9%" class="input_label2"><div
										align="center">
										部门<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF" nowrap="nowrap" width="21%"><input
									name="em.detName" type="text" id="detName" size="20" readonly
									maxlength="20" disable class="input_readonly"
									value='<s:property value="em.detName"/>'></td>
								<td  width="9%" class="input_label2"><div
										align="center">
										项目组<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF"><input name="em.groupName" readonly
									type="text" id="groupName" size="20" maxlength="20" disable
									class="input_readonly" value='<s:property value="em.groupName"/>'>
								</td>

							</tr>
							<tr>
								<td  width="10%" class="input_label2"><div
										align="center">
										入职日期<font color="#FF0000">*</font>
									</div></td>
								<td width="24%" bgcolor="#FFFFFF"><input
									name="em.entryDate" type="text" id="entryDate" size="20"
									maxlength="20" class="MyInput" 
									value='<s:date name="em.entryDate" format="yyyy-MM-dd"/>' />
								</td>
								<td width="13%" class="input_label2"><div
										align="center">
										转正日期<font color="#FF0000">*</font>
									</div></td>
								<td width="23%" bgcolor="#FFFFFF" colspan="3"><input
									name="em.changeDate" type="text" id="changeDate" size="20"
									maxlength="20" class="MyInput" 
									value='<s:date name="em.changeDate" format="yyyy-MM-dd"/>' /></td>

							</tr>
							<tr>
								<td  width="10%" class="input_label2"><div
										align="center">
										岗位<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF">
									<div align="left">
										<s:select list="{'初级工程师','中级工程师','高级工程师'}" name="em.position"
											value="em.position" theme="simple" cssStyle="width:162px;"
											headerKey="" headerValue="----" id="position"></s:select>
									</div>
								</td>
								<td class="input_label2">
									<div align="center">
										督导师<font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF">
									<input name="em.teacher" type="text" id="teacher" size="12" maxlength="20" value='<s:property value="em.teacher"/>'>
									<input type="button" value="选择" id="zhusong" onClick="selectPeople('teacher','')" />
								</td>
								<td class="input_label2">学习状态<font
									color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF">
									<!-- 
                    	<select  name="em.studyStatus"  style="width:141px;" id="studyStatus" >
                				<option  value="待学习">待学习</option>
                				<option value="学习中">学习中</option>
                				<option  value="完成学习">完成学习</option>
              			</select > 
              			 --> <s:select list="{'待学习','学习中','完成学习'}"
										name="em.studyStatus" value="em.studyStatus" theme="simple"
										cssStyle="width:141px;"></s:select>


								</td>
							</tr>
							<!-- 
							<tr>
								<td class="input_label2"><div align="center">笔试成绩</div></td>
								<td bgcolor="#FFFFFF"><input name="em.blackScore" onkeyup="this.value=this.value.replace(/[^\d\.]/g,'')" onKeyDown="this.value=this.value.replace(/[^\d\.]/g,'')"
									type="text" id="blackScore" size="24" maxlength="24"
									value='<s:property value="em.blackScore==0.0?'':em.blackScore"/>'></td>
								<td class="input_label2"><div align="center">实操成绩</div></td>
								<td bgcolor="#FFFFFF" colspan="3"><input
									name="em.whiteScore" type="text" id="whiteScore" size="22"
									maxlength="20" onkeyup="this.value=this.value.replace(/[^\d\.]/g,'')" onKeyDown="this.value=this.value.replace(/[^\d\.]/g,'')"
									value='<s:property value="em.whiteScore==0.0?'':em.whiteScore"/>'></td>
							</tr> -->
						</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok"
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
