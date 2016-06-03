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
		if (document.getElementById("courseName").value.trim() == "") {
			alert("请输入课程名称");
			return;
		}
		if (document.getElementById("category").value.trim() == "") {
			alert("请输入类别");
			return;
		}
		if (document.getElementById("teacher").value.trim() == "") {
			alert("请输入讲师");
			return;
		}
		if (document.getElementById("resourceName").value.trim() == "") {
			alert("请输入资料名称");
			return;
		}
		if (document.getElementById("path").value.trim() == "") {
			alert("请输入链接地址");
			return;
		}
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		reportInfoForm.submit();

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
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 1000) {
					alert("你输入的字数超过1000个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 1000);
				}
			}
		}
	}
	function listenKey() {
		if (document.addEventListener) {
			document.addEventListener("keyup", getKey, false);
		} else if (document.attachEvent) {
			document.attachEvent("onkeyup", getKey);
		} else {
			document.onkeyup = getKey;
		}
	}
	listenKey();
	
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
		action="<%=request.getContextPath()%>/pages/course/courseinfo!save.action"
		method="post">
		<table width="80%" align="center">
			<tr>
				<td>
					<table width="95%" class="input_table">
						<tr>
							<td class="input_tablehead"><s:text name="课程信息" /></td>
						</tr>
						<tr>
							<td width="13%" class="input_label2"><div align="center">
									课程名称<font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF" nowrap="nowrap" width="25%"><input
								name="course.courseName" type="text" id="courseName" size="24"
								maxlength="50" value=''></td>
							<td width="13%" class="input_label2"><div align="center">
									分类<font color="#FF0000">*</font>
								</div></td>
							<td width="16%" bgcolor="#FFFFFF"><s:select
									list="{'理论','方法','工具','业务','标准','技术','制度','流程'}"
									name="course.category" theme="simple" cssStyle="width:90px;"
									headerKey="" headerValue="----" id="category"></s:select> <!-- 
                     	<input name="course.category" type="text" id="category" size="24" maxlength="24" value=''>
                      	--></td>
							<td width="10%" class="input_label2">
								<div align="center"> 讲师<font color="#FF0000">*</font></div>
							</td>
							<td width="23%" bgcolor="#FFFFFF">
								<input name="course.teacher" type="text" id="teacher" size="15" maxlength="10" value='<s:property value="course.teacher"/>'>
								<input type="button" value="选择" id="zhusong" onClick="selectPeople('teacher','')" />
							</td>
						</tr>
						<tr>
							<td width="13%" class="input_label2"><div align="center">
									资料名称<font color="#FF0000"></font><font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><input name="course.resourceName"
								type="text" id="resourceName" size="24" maxlength="35" value=''></td>
							<td width="13%" class="input_label2"><div align="center">
									链接地址<font color="#FF0000">*</font>
								</div></td>
							<td colspan="3" bgcolor="#FFFFFF"><input name="course.path"
								type="text" id="path" size="57" maxlength="200" value=''>
								<div align="center"></div></td>
						</tr>
						<tr>
							<td width="13%" class="input_label2"><div align="center">
									课程简介<font color="#FF0000"></font>
								</div></td>
							<td colspan="5" bgcolor="#FFFFFF"><textarea
									name="course.summary" cols="100" rows="5"></textarea></td>

						</tr>
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
