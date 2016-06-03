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
	/* function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */
	function save() {
		if (document.getElementById("createdate").value.trim() == "") {
			alert("请输入提出日期");
			return;
		}
		if (document.getElementById("createman").value.trim() == "") {
			alert("请输入提出者");
			return;
		}
		if (document.getElementById("summary").value.trim() == "") {
			alert("请输入概要");
			return;
		}
		if (document.getElementById("pond").value.trim() == "") {
			alert("请选择严重性");
			return;
		}
		if (document.getElementById("urgent").value.trim() == "") {
			alert("请选择紧急程度");
			return;
		}
		if (document.getElementById("riskdesc").value.trim() == "") {
			alert("请输入风险描述");
			return;
		}
		if (document.getElementById("suggest").value.trim() == "") {
			alert("请输入建议方案");
			return;
		}
		if (document.getElementById("type").value.trim() == "") {
			alert("请选择类别");
			return;
		}
		var status=document.getElementById("status").value.trim();
		
		if (status == "新建") {
			alert("此环节，状态不能为新建");
			return
		}
		if(status=="处理中"){
			if(document.getElementById("handleman").value.trim()==""){
				alert("请输入处理者");
				return;
			}
			if(document.getElementById("plan").value.trim()==""){
				alert("请输入行动计划");
				return;
			}
		}
		if(status=="已解决"){
			if(document.getElementById("factdate").value.trim()==""){
				alert("请输入实际完成日期");
				return;
			}
			if(document.getElementById("fruit").value.trim()==""){
				alert("请输入解决情况");
				return;
			}
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

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function checkHdl(val) {
		if (val.value.trim() == "" || val.value == null) {
			alert("必填项不能为空");
		}
	}

	function startMonthHdl() {
		var pond = document.getElementById("pond").value;
		var urgent = document.getElementById("urgent").value;
		var dt1 = new Date();

		if (pond.trim() != "" && urgent.trim() != "") {
			if (pond.trim() == "高") {
				if (urgent.trim() == "非常紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000);//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 2;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "一般") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 3;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "不紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 4;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				}
			} else if (pond.trim() == "中") {
				if (urgent.trim() == "非常紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 2;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 3;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "一般") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 4;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "不紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 5;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				}
			} else if (pond.trim() == "低") {
				if (urgent.trim() == "非常紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 3;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 4;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "一般") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 5;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				} else if (urgent.trim() == "不紧急") {
					var distancetmp = dt1.getTime() + (24 * 60 * 60 * 1000) * 6;//算出天数 
					dt1.setTime(distancetmp);
					var seperator = "-";
					var month = dt1.getMonth() + 1;
					var strDate = dt1.getDate();
					if (month >= 1 && month <= 9) {
						month = "0" + month;
					}
					if (strDate >= 0 && strDate <= 9) {
						strDate = "0" + strDate;
					}

					var tmp = dt1.getFullYear() + seperator + month + seperator
							+ strDate;
					document.getElementById("handleterm").value = tmp;
				}
			}

		}
		return true;
	}
	function c_status(){
		var status=document.getElementById("status").value;
		if(status=="处理中"){
			document.getElementById("color1").innerHTML="<font color='red'>*</font>";
			document.getElementById("color3").innerHTML="<font color='red'>*</font>";
			document.getElementById("color2").innerHTML="";
			document.getElementById("color4").innerHTML="";
			document.getElementById("fruit").readOnly=true;
			document.getElementById("factdate").readOnly=true;
			document.getElementById("factdate").disabled=true;
		}else if(status=="已解决"){
			document.getElementById("color2").innerHTML="<font color='red'>*</font>";
			document.getElementById("color4").innerHTML="<font color='red'>*</font>";
			document.getElementById("fruit").readOnly=false;
			document.getElementById("factdate").readOnly=false;
			document.getElementById("factdate").disabled=false;
			document.getElementById("color1").innerHTML="";
			document.getElementById("color3").innerHTML="";
		}else{
			document.getElementById("color1").innerHTML="";
			document.getElementById("color2").innerHTML="";
			document.getElementById("color3").innerHTML="";
			document.getElementById("color4").innerHTML="";
			document.getElementById("fruit").readOnly=false;
			document.getElementById("factdate").readOnly=false;
			document.getElementById("factdate").disabled=false;
		}
	}
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 600) {
					alert("你输入的字数超过600个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 600);
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
	function selectMainPeople(see,hidden){
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
		 	document.getElementById(hidden).value = id.substring(0, id.indexOf(",", 0));
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
		 	document.getElementById(hidden).value = id.substring(0, id.indexOf(",", 0));
	 	}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!update.action"
		method="post">
		<input type="hidden" name="prjRisk.id"
			value='<s:property value="prjRisk.id"/>'> <input
			name="usernameHidden" type="hidden" id="usernameHidden"
			value="<%=((cn.grgbanking.feeltm.login.domain.UserModel) session
					.getAttribute("tm.loginUser")).getUsername()%>" />
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table class="input_table">
							<tr >
								<td class="input_tablehead"> 项目风险333 </td>
							</tr>
							<tr>
								<td class="input_label2" width="13%">
									<div align="center">
										提出日期 
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="27%" colspan="3"><input
									name="prjRisk.createdate" type="text" 
									id="createdate"
									value='<s:date name="prjRisk.createdate" format="yyyy-MM-dd"/>'
									readonly class="input_readonly"></td>
								<td width="9%" class="input_label2">
									<div align="center">
										提出者 
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="26%"><input
									name="prjRisk.createman" type="text" id="createman"
									maxlength="20" size="20"
									value='<s:property value="prjRisk.createman"/>' readonly class="input_readonly"></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										风险编号<font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="3" nowrap bgcolor="#FFFFFF">
									<input name="riskno" type="text" id="riskno"
									value='<s:property value="prjRisk.rno"/>' class="input_readonly" readonly="readonly">
								</td>
								<td class="input_label2" width="11%"><div align="center">
										状态 <font color="#FF0000">*</font>
									</div></td>
								<td width="14%" bgcolor="#FFFFFF"><select
									name="prjRisk.status" id="status" align="left"
									style="width: 77px" onchange="c_status()">
										<option value='新建' style="width: 105px">新建</option>
										<option value='处理中'>处理中</option>
										<option value='已解决'>已解决</option>
										<option value="不解决">不解决</option>
								</select></td>
								<input type='hidden' id="st1" name="st1"
									value="<s:property value='prjRisk.status'/>" />
								<s:if test="prjRisk.status!=null&&!prjRisk.status.equals('')">
									<script language="javascript">
										var status = $("#st1").val();
										status = decodeURI(status);
										$("#status").val(status);
									</script>
								</s:if>
							</tr>
							

							<tr>
								<td class="input_label2">
									<div align="center">
										项目名称 
									</div>
								</td>
								<td colspan="3" bgcolor="#FFFFFF">
									<input name="prjRisk.prjname" type="text" id="prjname" style="width:100%;" value='<s:property value="prjRisk.prjname"/>' readonly class="input_readonly"/>
								</td>
								<td class="input_label2"><div align="center">
										类别
									</div></td>
								<td bgcolor="#FFFFFF">
									<input name="prjRisk.type" type="text" id="type" style="width:100%;" value='<s:property value="prjRisk.type"/>' readonly class="input_readonly"/>
								</td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										严重性
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="3" onClick="startMonthHdl()">
									<input name="prjRisk.pond" type="text" id="pond"  style="width:100%;" value='<s:property value="prjRisk.pond"/>' readonly class="input_readonly"/>
								</td>
								<td width="11%" class="input_label2"><div align="center">
										紧急程度
									</div></td>
								<td bgcolor="#FFFFFF" onClick="startMonthHdl()">
									<input name="prjRisk.urgent" type="text" id="urgent" style="width:100%;" value='<s:property value="prjRisk.urgent"/>' readonly class="input_readonly"/>
								</td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										概要
									</div>
								</td>
								<td colspan="5" bgcolor="#FFFFFF"><input
									name="prjRisk.summary" type="text" id="summary" maxlength="70"
									size="74" value='<s:property value="prjRisk.summary"/>' readonly class="input_readonly" style="width:100%">
								</td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										风险描述
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea cols="74" rows="3" name="prjRisk.riskdesc"
											id="riskdesc" readonly style="width:100%"><s:property value="prjRisk.riskdesc" /></textarea>
									</div>
								</td>
							</tr>

							<tr>
								<td class="input_label2">建议方案 
								</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea cols="74"
										rows="3" name="prjRisk.suggest" id="suggest" readonly style="width:100%"><s:property
											value="prjRisk.suggest" /></textarea></td>
							</tr>
							<tr>
								<td width="13%" class="input_label2">行动计划<span id="color1"></span></td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea
										name="prjRisk.plan" cols="74" rows="3" id="plan" style="width:100%"><s:property
											value="prjRisk.plan" /></textarea></td>
							</tr>


							<tr>
								<td width="13%" class="input_label2">
									<div align="center">计划完成日期</div>
								</td>
								<td width="27%" class="input_label2"><input
									name="prjRisk.handleterm" type="text" id="handleterm"
									 value='<s:date name="prjRisk.handleterm" format="yyyy-MM-dd"/>' readonly class="input_readonly"></td>
								<td width="13%" class="input_label2">
									<div align="center">实际完成日期<span id="color2"></span></div>
								</td>
								<td bgcolor="#FFFFFF"><input name="prjRisk.factdate"
									type="text" id="factdate" class="MyInput" 
									value='<s:date name="prjRisk.factdate" format="yyyy-MM-dd"/>'>
								</td>
								<td class="input_label2" width="9%">
									<div align="center">处理者<span id="color3"></span></div>
								</td>
								<td width="26%" bgcolor="#FFFFFF">
									<input name="prjRisk.handleman" type="text" id="handleman" style="width: 60%;" readonly="readonly" value='<s:property value="prjRisk.handleman"/>'/>
									<input name="handlemanid" type="hidden" id="handlemanid" />
									<input type="button" value="选择" id="fixadmin" onclick="selectMainPeople('handleman','handlemanid')"/>
								</td>
							</tr>

							<tr>
								<td align="center" class="input_label2">解决情况<span id="color4"></span></td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea
										name="prjRisk.fruit" cols="74" rows="3" id="fruit"><s:property
											value="prjRisk.fruit" /></textarea></td>
							</tr>
						<input type="hidden" name="prjRisk.userid" id="userid" value="<s:property value='prjRisk.userid'/>"/>
						<input type="hidden" name="prjRisk.deptname" id="deptname" value="<s:property value='prjRisk.deptname'/>"/>
						<input type="hidden" name="prjRisk.groupname" id="groupname" value="<s:property value='prjRisk.groupname'/>"/>

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
