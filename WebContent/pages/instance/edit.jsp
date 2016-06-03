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
			alert("请输入提交日期");
			return;
		}
		if (document.getElementById("create_man").value.trim() == "") {
			alert("请输入提交者");
			return;
		}
		if (document.getElementById("summary").value.trim() == "") {
			alert("请输入案例名称");
			return;
		}
		if (document.getElementById("description").value.trim() == "") {
			alert("请输入案例描述");
			return;
		}
		if (document.getElementById("category").value.trim() == "") {
			alert("请选择案例来源");
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

/*	function selectPeople(see, hidden) {
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
		action="<%=request.getContextPath()%>/pages/instance/instanceinfo!update.action"
		method="post">
		<input type="hidden" name="instance.id"
			value='<s:property value="instance.id"/>'>
		<table width="90%" align="center" cellPadding="0" cellSpacing="0" style="word-wrap: break-word; word-break: break-all;">
			<tr>
				<td>
						<table class="input_table">
						<tr>
							<td class="input_tablehead"> 爱心小鱼</td>
						</tr>

							<tr>
								<td class="input_label2" width="11%">
									<div align="center">
										提出日期 <font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="2" bgcolor="#FFFFFF">
								<s:if test="#request.isLogUser==true">
								<input
									name="instance.create_date" type="text" 
									id="create_date"
									value='<s:date name="instance.create_date" format="yyyy-MM-dd"/>'
									class="MyInput">
									</s:if>
									</td>
									<s:else>
								<input
									name="instance.create_date" type="text" readonly="readonly"
									id="create_date"
									value='<s:date name="instance.create_date" format="yyyy-MM-dd"/>'
									class="MyInput">
									</s:else>
									</td>
									
								<td width="10%" class="input_label2">
									<div align="center">
										提出者 <font color="#FF0000">*</font>
									</div>
								</td>
								<td width="29%" bgcolor="#FFFFFF">
								<input
									name="instance.create_man" type="text" readonly="readonly"
									id="create_man"
									value='<s:property value="instance.create_man"/>' size="13"
									maxlength="13" readonly class="input_readonly"> 
									<input name="create_mantmp"
									type="hidden" id="create_mantmp" maxlength="15" size="15"
									value='<s:property value="upMan"/>'> 
									<input type="checkbox"
									name="status_flag" id="status_flag" value="1" /> 公开发布 
								<!--  <input type='hidden'
									id="anon" name="instance.anon"
									value="<s:property value='instance.anon'/>" /> 
									 <input type='hidden' id="status" name="instance.status"
									value="<s:property value='instance.status'/>" /> <input
									type="checkbox" name="oaemail_flag" id="oaemail_flag" value="1" />
									OA以及邮件通知接受者  -->
									<script language="javascript">
									document.getElementById("status_flag").checked = true;
									//document.getElementById("oaemail_flag").checked = true;
									</script>
									</td>



							</tr>
							<tr>
								<td class="input_label2"><div align="center">
										接受者 <font color="#FF0000">*</font>
									</div></td>
								<td colspan="2" bgcolor="#FFFFFF">
								<s:if test="#request.isLogUser==true">
								
								<input
									type="hidden" name="instance.embracerids" id="embracerids"
									size="40" value="<s:property value='instance.embracerids'/>">
								<input type="text" readonly="true"
									name="instance.embracer_man" id="embracer_man" size="40"
									value="<s:property value='instance.embracer_man'/>"> 
									<input class="selectuser" onclick="selUser(this)" type="button" value="选择">
								<!--  <input type="button" value="选择" id="zhusong"
									onClick="selectPeople('embracer_man','embracerids')" /> -->	
								</s:if>	
								<s:else>
								<input
									type="hidden" name="instance.embracerids" id="embracerids"
									size="40" value="<s:property value='instance.embracerids'/>">
								<input type="text" readonly="true" readonly="readonly"
									name="instance.embracer_man" id="embracer_man" size="40"
									value="<s:property value='instance.embracer_man'/>"> 
									<input class="selectuser" onclick="selUser(this)" type="button" value="选择">
								<!--	<input type="button" value="选择" id="zhusong" readonly="readonly"
									onClick="selectPeople('embracer_man','embracerids')" /> -->	
								</s:else>
									</td>
								<td class="input_label2"><div align="center">
										类别<font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF">
								<s:if test="#request.isLogUser==true">
								<tm:tmSelect name="instance.category" 
										id="category" selType="dataDir"
										path="systemConfig.instancesource" style="width:88px" /> 
									</s:if>
									<s:else>
									<tm:tmSelect name="instance.category" 
										id="category" selType="dataDir"
										path="systemConfig.instancesource" style="width:88px" /> 
									</s:else>	
										<input
									type='hidden' id="st1" name="st1"
									value="<s:property value='instance.category'/>" /> <s:if
										test="instance.category!=null&&!instance.category.equals('')">
										<script language="javascript">
											var category = $("#st1").val();
											category = decodeURI(category);
											$("#category").val(category);
										</script>
									</s:if></td>

							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										概要<font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="5" bgcolor="#FFFFFF">
								<s:if test="#request.isLogUser==true">
								<input
									name="instance.summary" type="text" id="summary"
									maxlength="102" size="102"
									value='<s:property value="instance.summary"/>'>
									</s:if>
									<s:else>
									<input
									name="instance.summary" type="text" id="summary" readonly="readonly"
									maxlength="102" size="102"
									value='<s:property value="instance.summary"/>'>
									</s:else>
									
									</td>
							</tr>


							<tr>
								<td class="input_label2">
								        事件描述 <font color="#FF0000">*</font>
								</td>

								<td class="input_label2" colspan="5">
								<s:if test="#request.isLogUser==true">
										<textarea cols="76" rows="6" name="instance.description"
											id="description" style="width:100%"><s:property
												value="instance.description" />
										  </textarea>
										  </s:if>
										  <s:else>
										  <textarea cols="76" rows="6" name="instance.description" readonly="readonly"
											id="description" style="width:100%"><s:property
												value="instance.description" />
										  </textarea>
										  </s:else>
								</td>

							</tr>
							<tr>
								<td width="11%" class="input_label2">建议/备注</td>
								<td bgcolor="#FFFFFF" colspan="5">
								<s:if test="#request.isLogUser==true">
								<textarea name="instance.solution" cols="76" rows="6" id="solution" style="width:100%"><s:property value="instance.solution" /></textarea>
								</s:if>
								<s:else>
								<textarea name="instance.solution" cols="76" rows="6" readonly="readonly" id="solution" style="width:100%"><s:property value="instance.solution" /></textarea>
								</s:else>
								</td>
							    
							</tr>
						<tr>
		                  	 <td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">确认说明</font></div></td>
		                     	<td colspan="7" nowrap bgcolor="#FFFFFF" >
				                     <div align="left">
				                     
				                     <!-- 返回创建者ID以作判断是否为本人修改 -->
				                     <input name="instance.create_manId" type="hidden" id="instance.create_manId"
									value="<s:property value='#request.create_manId'/>">
				                     
				                     <input name="instance.confirmStatus" type="hidden" id="instance.confirmStatus"
									value="<s:property value='#request.confirmStatus'/>">
				                     <!-- 部门经理级别以上可以编辑 -->
					                  	 <s:if test="#request.hasComfirmRight==true">
							             	<textarea name="instance.confirmDesc" type="text"  rows="6" style="width:99%"><s:property value='instance.confirmDesc'/></textarea>
					                  	 </s:if>
					                  	 <s:else>
							             	<textarea style="background-color: #E3E3E3" readonly="readonly" name="instance.confirmDesc" type="text"  rows="6" style="width:99%"><s:property value='instance.confirmDesc'/></textarea>
					                  	 </s:else>
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
