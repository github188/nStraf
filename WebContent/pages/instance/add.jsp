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

		if (document.getElementById("create_date").value.trim() == "") {
			alert("请输入提出日期");
			return;
		}
		if (document.getElementById("create_man").value.trim() == "") {
			alert("请输入提出者");
			return;
		}
		if (document.getElementById("summary").value.trim() == "") {
			alert("请输入概要");
			return;
		}
		if (document.getElementById("description").value.trim() == "") {
			alert("请输入详细描述");
			return;
		}
		if (document.getElementById("category").value.trim() == "") {
			alert("请选择互评类别");
			return;
		}
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		reportInfoForm.submit();

	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function selDate(id) {
		var obj = document.getElementById(id);
		ShowDate(obj);
	}

	function chickanon() {
		if (document.getElementById("anon_flag").checked == true) {
			document.getElementById("create_man").value = "***";
		} else {
			document.getElementById("create_man").value = document
					.getElementById("create_mantmp").value;
		}
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function checkHdl(val) {
		if (val.value.trim() == "" || val.value == null) {
			alert("必填项不能为空");
		}
	}

	/**	function selectPeople(see, hidden) {
		var strUrl = "/pages/instance/instanceinfo!select.action";
		var feature = "520,380,notidy.addTitle,notify";
		var returnValue = OpenModal(strUrl, feature);
		if (returnValue != null && returnValue != "") {
			var values = returnValue.split(",");
			var name = ",";
			var id = ",";
			for (var i = 0; i < values.length; i++) {
				var temp = values[i].split(":");
				id = id + temp[0] + ",";
				name = name + temp[1] + ",";
			}
			name = name.substring(1);
			id = id.substring(1);
			document.getElementById(see).value = name;
			document.getElementById(hidden).value = id;
		}
	}*/
	
	function selUser(ele) {
		var userids=encodeURI($(ele).prev().prev().val());
		var usernames=encodeURI($(ele).prev().val());
		//alert(userids+"." +usernames +"." + projectId );
		var strUrl = "/pages/instance/instanceinfo!getAllUserByUserIds.action?userids="+userids+"&usernames="+usernames;
		var feature = "520,380,grpInfo.updateTitle,um";
	 	var returnValue = OpenModal(strUrl, feature);
		if (returnValue != null && returnValue != "") {
			var values = returnValue.split(":");
			userids="";
			usernames="";
			if(values.length>0){
				for(var j=0;j<values.length;j++){
					var idname = values[j].split(",");
					userids += ","+idname[0];
					usernames += ","+idname[1];
				}
			}
			userids= userids.substring(1);
			usernames = usernames.substring(1);
			$(ele).prev().val(usernames);
			$(ele).prev().prev().val(userids);
		}else if(returnValue==""){
			$(ele).prev().val("");
			$(ele).prev().prev().val("");
		}
	}
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 500) {
					alert("你输入的字数超过500个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 500);
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
	
	function setSelectPeopleValue(returnValue,see,hidden){
		if (returnValue != null && returnValue != "") {
			var values = returnValue.split(":");
			userids="";
			usernames="";
			if(values.length>0){
				for(var j=0;j<values.length;j++){
					var idname = values[j].split(",");
					userids += ","+idname[0];
					usernames += ","+idname[1];
				}
			}
			userids= userids.substring(1);
			usernames = usernames.substring(1);
			$("#embracer_man").val(usernames);
		 	$("#embracerids").val(userids);
		}else if(returnValue==""){
			$("#embracer_man").val("");
		 	$("#embracerids").val("");
		}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/instance/instanceinfo!save.action"
		method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">爱心小鱼</td>
						</tr>

						<tr>
							<td class="input_label2" width="15%">
								<div align="center">
									提出日期 <font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="2" bgcolor="#FFFFFF"><input
								name="instance.create_date" type="text" id="create_date"
								class="MyInput" 
								value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'>
							</td>
							<td class="input_label2" width="11%">
								<div align="center">
									提出者 <font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="0" bgcolor="#FFFFFF" width="36%"><input
								name="instance.create_man" type="text" id="create_man" readonly="readonly"
								maxlength="15" size="15" value='<s:property value="createMan"/>'>
								<input name="create_mantmp" type="hidden" id="create_mantmp"
								maxlength="15" size="15" value='<s:property value="createMan"/>'>
								<input type="checkbox"
									name="status_flag" id="status_flag" value="1" /> 公开发布 
								<script language="javascript">
									document.getElementById("status_flag").checked = true;
									//document.getElementById("oaemail_flag").checked = true;
								</script>
								</td>
						</tr>
						<tr>
							<td class="input_label2"><div align="center">
									接受者<font color="#FF0000">*</font>
								</div></td>
								<td colspan="2" bgcolor="#FFFFFF">
								<input
								type="hidden" name="instance.embracerids" id="embracerids"
								size="40" value="<s:property value='instance.embracerids'/>">
							<input type="text" readonly="true"
								name="instance.embracer_man" id="embracer_man" size="40"
								value="<s:property value='instance.embracer_man'/>"> 
								<input class="selectuser" onclick="selUser(this)" type="button" value="选择"></td>
								<!--  <input type="button" value="选择" id="zhusong"
								onClick="selectPeople('embracer_man','embracerids')" /></td>-->

							<td class="input_label2"><div align="center">
									类别 <font color="#FF0000">*</font>
								</div></td>
							<td bgcolor="#FFFFFF"><tm:tmSelect name="instance.category"
									id="category" selType="dataDir"
									path="systemConfig.instancesource" style="width:105px" /></td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									概要<font color="#FF0000">*</font>
								</div>
							</td>
							<td colspan="5" bgcolor="#FFFFFF"><input
								name="instance.summary" type="text" id="summary" maxlength="100"
								size="102" value=''></td>
						</tr>


						<tr>
							<td class="input_label2">
								<div align="center">
									事件描述 <font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<textarea cols="76" rows="6" name="instance.description"
										id="description" style="width: 100%"></textarea>
								</div>
							</td>
						</tr>

						<tr>
							<td class="input_label2" width="15%">建议/备注</td>
							<td bgcolor="#FFFFFF" colspan="5"><textarea
									name="instance.solution" cols="76" rows="6" id="solution"
									style="width: 100%"></textarea></td>
						</tr>
						<tr>
		                  	 <td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">确认说明</font></div></td>
		                     	<td colspan="7" nowrap bgcolor="#FFFFFF" >
				                     <div align="left">
				                     <!-- 部门经理级别以上可以编辑 -->
							             	<textarea style="background-color: #E3E3E3" readonly="readonly" name="instance.confirmDesc" type="text"  rows="6" style="width:99%"></textarea>
				                     </div>
		                       </td>
	                  </tr>
						
					</table>
				</td>
			</tr>

		</table>
		<br /> <br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
