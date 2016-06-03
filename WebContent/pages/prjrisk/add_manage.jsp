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
		if(document.getElementById("plan").value.trim()==""){
			alert("请输入行动计划");
			return;
		}
		if(document.getElementById("handleman").value.trim()==""){
			alert("请输入处理者");
			return;
		}
		var sdate = document.getElementById("createdate").value.trim();
		var edate = document.getElementById("handleterm").value.trim();
		if(!compareDate(sdate,edate)){
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
	function compareDate(startDate,endDate){
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		if (startDate.length > 0 && endDate.length > 0) {
			var v = re.test(endDate);
			var a = re1.test(startDate);
			if (!v || !a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			if (!DateValidate(startDate,endDate)) {
				alert('计划完成日期不能小于提出日期，请重新输入！');
				return false;
			}
		} else if (startDate.length > 0 && endDate.length == 0) {
			var a = re1.test(startDate);
			if (!a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		} else if (startDate.length == 0 && endDate.length > 0) {
			var v = re.test(endDate);
			if (!v) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		}
		return true;
	}

	function DateValidate(begin, end) {
		var Require = /.+/;
		var flag = true;
		if (Require.test(begin) && Require.test(end)) {
			var beginStr = begin.split("-");
			var endStr = end.split("-");
			if (parseInt(beginStr[0], 10) > parseInt(endStr[0], 10)) {
				flag = false;
			} else if (parseInt(beginStr[0], 10) == parseInt(endStr[0], 10)) {
				if (parseInt(beginStr[1], 10) > parseInt(endStr[1], 10)) {
					flag = false;
				} else if (parseInt(beginStr[1], 10) == parseInt(endStr[1], 10)) {
					if (parseInt(beginStr[2], 10) > parseInt(endStr[2], 10)) {
						flag = false;
					}
				}
			}
		}
		return flag;
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
	
	function init(){
		var actionUrl = "<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!getNextNo.action";
		$.ajax({
		     type:"post",
		     url:actionUrl,
			 dataType:"json",
			 cache: false,
			 async:true,
			 success:function(data){
				 document.getElementById("riskno").value=data.no;
			 },
			 error:function(e){
				 alert(e);
			 }
		   }); 
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
<body id="bodyid" leftmargin="0" topmargin="10" onload="init();">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!save.action"
		method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
						<table class="input_table".>
							<tr>
								<td class="input_tablehead"> 项目风险 </td>
							</tr>
							<tr>
								<td class="input_label2" width="13%">
									<div align="center">
										提出日期 <font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="27%" colspan="3"><input
									name="prjRisk.createdate" type="text" id="createdate"
									value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'
									readonly="readonly" class="input_readonly"></td>
								<td width="9%" class="input_label2">
									<div align="center">
										提出者 <font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" width="26%"><input
									name="prjRisk.createman" type="text" id="createman" 
									maxlength="20" size="20" value='<s:property value="raiseMan"/>'
									readonly="readonly" class="input_readonly"></td>
							</tr>

							<tr>
								<td class="input_label2">
									<div align="center">
										风险编号<font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="3" nowrap bgcolor="#FFFFFF">
									<input name="riskno" type="text" id="riskno"
									value="" class="input_readonly" readonly="readonly">
								</td>
								<td class="input_label2" width="11%"><div align="center">
										状态 <font color="#FF0000">*</font>
									</div></td>
								<td width="14%" bgcolor="#FFFFFF">
									<input type="hidden" name="prjRisk.status" id="status" readonly="readonly" class="input_readonly" value="处理中"/>
									<input type="text" id="status_text" readonly="readonly" class="input_readonly" value="新建"/>
								</td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										项目名称 <font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="3" nowrap bgcolor="#FFFFFF"><tm:tmSelect
										name="prjRisk.prjname" id="prjname" selType="projectName" style="width:240px" /></td>

								<td class="input_label2"><div align="center">
										类别 <font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF"><select name="prjRisk.type" id="type"
									align="left" style="width: 77px">
										<option value='' selected="true"></option>
										<option value='资源'>资源</option>
										<option value='人力'>人力</option>
										<option value='进度'>进度</option>
										<option value='质量'>质量</option>
										<option value='设计'>设计</option>
										<option value='制度'>制度</option>
										<option value='生产问题'>生产问题</option>
										<option value='项目风险'>项目风险</option>
										<option value='其他'>其他</option>
								</select></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										严重性 <font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="3" onClick="startMonthHdl()">
									<select name="prjRisk.pond" id="pond" style="width: 78px"
									align="left">
										<option value='' selected="true"></option>
										<option value='高'>高</option>
										<option value='中'>中</option>
										<option value='低'>低</option>
								</select>
								</td>

								<td width="11%" class="input_label2"><div align="center">
										紧急程度 <font color="#FF0000">*</font>
									</div></td>
								<td bgcolor="#FFFFFF" onClick="startMonthHdl()"><select
									name="prjRisk.urgent" id="urgent" style="width: 78px"
									align="left">
										<option value='' selected="true"></option>
										<option value='非常紧急'>非常紧急</option>
										<option value='紧急'>紧急</option>
										<option value='一般'>一般</option>
										<option value='不紧急'>不紧急</option>
								</select></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										概要 <font color="#FF0000">*</font>
									</div>
								</td>
								<td colspan="5" bgcolor="#FFFFFF"><input
									name="prjRisk.summary" type="text" id="summary" maxlength="70"
									size="74" value=''></td>
							</tr>
							<tr>
								<td class="input_label2">
									<div align="center">
										风险描述 <font color="#FF0000">*</font>
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea cols="74" rows="3" name="prjRisk.riskdesc"
											id="riskdesc" style="width:100%"></textarea>
									</div>
								</td>
							</tr>

							<tr>
								<td class="input_label2">建议方案 <font
									color="#FF0000">*</font>
								</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea cols="74"
										rows="3" name="prjRisk.suggest" id="suggest" style="width:100%"></textarea></td>
							</tr>
							<tr>
								<td width="13%" class="input_label2">行动计划<font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea
										name="prjRisk.plan" cols="74" rows="3" id="plan" style="width:100%"></textarea>
								</td>
							</tr>


							<tr>
								<td width="13%" class="input_label2">
									<div align="center">计划完成日期<font color="#ff0000">*</font></div>
								</td>
								<td width="27%" bgcolor="#FFFFFF"><input
									name="prjRisk.handleterm" type="text" id="handleterm"
									class="MyInput" 
									value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'></td>
								<!-- 
								<td width="13%" class="input_label2">
									<div align="center">实际完成日期</div>
								</td>
								<td bgcolor="#FFFFFF"><input name="prjRisk.factdate"
									type="text" disabled="disabled" class="input_readonly" id="factdate"></td>
								 -->
								<td class="input_label2" width="9%" >
									<div align="center">处理者<font color="#FF0000">*</font></div>
								</td>
								<td width="26%" bgcolor="#FFFFFF" colspan="3">
									<input name="prjRisk.handleman" type="text" id="handleman" style="width: 60%;" readonly="readonly"/>
									<input name="handlemanid" type="hidden" id="handlemanid" />
									<input type="button" value="选择" id="fixadmin" onclick="selectMainPeople('handleman','handlemanid')"/>
								</td>

							</tr>
							<!-- 
							<tr>
								<td class="input_label2">解决情况</td>
								<td colspan="5"><textarea
										name="prjRisk.fruit" cols="74" rows="3" disabled class="input_readonly"
										id="fruit"></textarea></td>
							</tr> -->
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
