<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时小计span改为input-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript">
	var iTaskIndex = 1; // the number of the task tables
	var selAllFlag = true;
	var tableTaskIndex = 2; // table in table's index
	var iTaskTotalpd = 20; // the total number of tasks
	//总课程数
	var classItems = 1;
	function save() {

		var total = document.getElementsByName("planFinishDate").length;

		for (var ind = 0; ind < total; ind++) {
			var prjName = document.getElementsByName("courseName")[ind];
			var taskDesc = document.getElementsByName("category")[ind];
			var responsible = document.getElementsByName("planFinishDate")[ind];
			var finishRate = document.getElementsByName("graspStandard")[ind];
			var delayReason = document.getElementsByName("prioryLevel")[ind];
			if (prjName != null) {
				if (prjName.value.trim() == "") {
					alert("请输入课程名称");
					return;
				}
			}
			if (taskDesc != null) {
				if (taskDesc.value.trim() == "") {
					alert("请选择类别");
					return;
				}
			}
			if (responsible != null) {
				if (responsible.value.trim() == "") {
					alert("请输入计划完成时间");
					return;
				}
			}
			if (finishRate != null) {
				if (finishRate.value.trim() == "") {
					alert("请选择掌握标准");
					return;
				}
			}
			if (delayReason != null) {
				if (delayReason.value.trim() == "") {
					alert("请选择优先级");
					return;
				}
			}

		}

		var objs = document.getElementsByName("courseName");
		//首先定义个数组用来文本框的输入,本文7个
		var c = new Array(objs.length);
		var k = 0;
		for (i = 0; i < objs.length; i++) {
			c[k] = objs[i].value;
			if (k <= objs.length) {
				k = k + 1;
			}
		}
		//循环判断里面是否有相同输入
		var b;
		for (i = 0; i < c.length; i++) {
			b = c[i];
			for (j = i + 1; j < c.length; j++) {
				if (b == c[j]) {
					alert(b + "的课程名称重复，请重新进行选择");
					return false;
				}
			}
		}
		window.returnValue = true;
		reportInfoForm.submit();
	}
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	//新增课程
	function addTrainClass() {
		//classItems = $(":checkbox").length;//若2条，最后一条id=‘task1’
		//取到最后一条记录
		var lastTrObj = $("#record tbody tr:last");
		var recordClone = lastTrObj.clone(true);
		
		$("#task" + (classItems - 1)).after(recordClone);
		//recordClone.children().children()[0].nextSibling.nodeValue = classItems + 1;
		recordClone.attr("id", "task" + (classItems));
		
		recordClone.children().children()[3].innerHTML='<div align="center"><input name="planFinishDate" type="text" id="planFinishDate'+classItems+'" size="15"  maxlength="18" /></div>';
		recordClone.children().children()[7].innerHTML='<input name="actualFinishDate" type="text" id="actualFinishDate'+classItems+'" size="15"  maxlength="18" />';
		
		$("#actualFinishDate"+classItems).datepicker({
			dateFormat : 'yy-mm-dd', //更改时间显示模式  
			changeMonth : true, //是否显示月份的下拉菜单，默认为false  
			changeYear : true
		//是否显示年份的下拉菜单，默认为false  
		});
		$("#planFinishDate"+classItems).datepicker({
			dateFormat : 'yy-mm-dd', //更改时间显示模式  
			changeMonth : true, //是否显示月份的下拉菜单，默认为false  
			changeYear : true
		//是否显示年份的下拉菜单，默认为false  
		});
		classItems = classItems + 1;

		//去掉多余的日历控件
		//$(recordClone.children()[4].children(0)).children(1).nextAll().remove();
		//$(recordClone.children()[8].children(0)).nextAll().remove();
	}

	//删除课程
	function delTrainClass() {
		var nocheck = 1;
		$(":checkbox").each(
			function(i) {
				//if (i != 0) {
					if (this.checked) {
						$(this).parent().parent().remove();
						//classItems = classItems - 1;
					} else {
						nocheck = nocheck + 1;
						//this.nextSibling.nodeValue = nocheck;
						$(this).parent().parent().attr("id",
								"task" + (nocheck - 1));
					}
				//}
			});
	}

	function selAgain() {
		var taskDivPPtr = document.getElementsByTagName("table")[1];
		for (var ind = 0; ind < taskDivPPtr.getElementsByTagName("input").length; ind++) {
			if (taskDivPPtr.getElementsByTagName("input")[ind].type
					.toLowerCase() == "checkbox") {
				taskDivPPtr.getElementsByTagName("input")[ind].checked = selAllFlag;
			}
		}
		selAllFlag = !selAllFlag;
	}

	function validateInputInfo() {
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var thisDate = reportInfoForm.begin.value.trim();
		if (thisDate.length > 0) {
			var a = re1.test(thisDate);
			if (!a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		}
		return true;
	}

	function DateValidate(beginDate, endDate) {

		var Require = /.+/;

		var begin = document.getElementsByName(beginDate)[0].value
		var end = document.getElementsByName(endDate)[0].value

		var flag = true;

		if (Require.test(begin) && Require.test(end))
			if (begin > end)
				flag = false;
		return flag;

	}

	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 250) {
					alert("你输入的字数超过250个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 250);
				}
			}
		}
	}

	function change(checkEle) {
		var ckElement = checkEle.parentElement.parentElement
				.getElementsByTagName("input")[1];
		if (ckElement.value == 1) {
			ckElement.value = 0;
		} else {
			ckElement.value = 1;
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
	
	$(function() {
		classItems = $(":checkbox").length;//若2条，最后一条id=‘task1’
		var iTaskIndex = document.getElementsByName("planFinishDate").length;
		for (var i = 0; i < iTaskIndex; i++) {
			$('input[id="planFinishDate' + i + '"]').datepicker({
				dateFormat : 'yy-mm-dd', //更改时间显示模式  
				changeMonth : true, //是否显示月份的下拉菜单，默认为false  
				changeYear : true
			//是否显示年份的下拉菜单，默认为false  
			});
			$('input[id="actualFinishDate' + i + '"]').datepicker({
				dateFormat : 'yy-mm-dd', //更改时间显示模式  
				changeMonth : true, //是否显示月份的下拉菜单，默认为false  
				changeYear : true
			//是否显示年份的下拉菜单，默认为false  
			});
		}
	});
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/newEmployeeManage/newEmployeeManageinfo!updateDistribute.action"
		method="post">

		<table width="80%" align="center" cellPadding="1" cellSpacing="1"
			class="popnewdialog1">
			<tr>
				<td><input type="hidden" name="uname" id="uname"
					value="<s:property value='uname'/>" /> <input type="hidden"
					name="uid" id="uid" value="<s:property value='uid'/>" />
					<table width="100%" class="input_table" id="record">
						<tr>
							<td class="input_tablehead"><s:property value="uname" />的课程修改</td>
						</tr>
						<tr>
							<td width="6%" bgcolor="#FFFFFF"><div align="center">选择</div></td>
							<td width="20%" class="column_label"><div align="center">
									<font color="#000000">课程名称</font><font color="#FF0000">*</font>
								</div></td>
							<td width="4%" class="column_label"><div align="center">
									<font color="#000000">类别</font><font color="#FF0000">*</font>
								</div></td>
							<td width="11%" class="column_label"><div align="center">
									<font color="#000000">计划完成日期</font><font color="#FF0000">*</font>
								</div></td>
							<td width="11%" class="column_label"><div align="center">
									<font color="#000000">掌握标准</font><font color="#FF0000">*</font>
								</div></td>
							<td width="9%" class="column_label"><div align="center">
									<font color="#000000">优先级</font><font color="#FF0000">*</font>
								</div></td>
							<td width="10%" class="column_label"><div align="center">
									<font color="#000000">完成百分比</font>
								</div>
							<td width="17%" class="column_label"><div align="center">
									<font color="#000000">实际完成日期</font>
								</div></td>
							<td width="12%" class="column_label"><div align="center">
									<font color="#000000">完成效果</font>
								</div></td>
						</tr>

						<s:iterator value="ecList" id="records" status="st">
							<tr id='task<s:property value="#st.index"/>' name="taskDesc">
								<td bgcolor="#FFFFFF" align="left"><input type="checkbox"
									name="taskCkd1" />
								<!--<s:property value="#st.index+1" />-->
								</td>
								<input type="hidden" name="ck" value="1">
								<td colspan="" nowrap bgcolor="#FFFFFF">
									<div align="center">
										<s:select listKey="cid" listValue="courseName" list="courses"
											name="courseName" value="cid" theme="simple"
											cssStyle="width:141px;" headerKey="" headerValue="----"
											id="courses"></s:select>
									</div>
								</td>

								<td nowrap bgcolor="#FFFFFF" align="left"><s:select
										list="{'必修','选修'}" name="category" value="category"
										theme="simple" headerKey="" headerValue="----" id="category"></s:select>
								</td>

								<td nowrap bgcolor="#FFFFFF" id="planFinishDateTd">
									<div align="center">
										<input name="planFinishDate" type="text" id="planFinishDate<s:property value="#st.index"/>"
											size="15"  maxlength="18"
											value="<s:date name='planFinishDate' format='yyyy-MM-dd'/>" />
									</div>
								</td>

								<td nowrap bgcolor="#FFFFFF">
									<div align="center">
										<s:select list="{'了解','掌握','精通'}" name="graspStandard"
											value="graspStandard" theme="simple" headerKey=""
											headerValue="----" id="graspStandard"></s:select>
									</div>
								</td>

								<td nowrap bgcolor="#FFFFFF">
									<div align="center">
										<s:select list="{'高','中','低'}" name="prioryLevel"
											value="prioryLevel" theme="simple" headerKey=""
											headerValue="----" id="prioryLevel"></s:select>
									</div>
								</td>

								<td nowrap bgcolor="#FFFFFF" align="left">
									<div align="center">
										<s:select
											list="percents" name="finishPercent" value="finishPercent"
											theme="simple" id="finishPercent">
										</s:select>
									</div>
								</td>

								<td nowrap bgcolor="#FFFFFF" align="left" id="actualFinishDateTd">
									<div align="center">
										<input name="actualFinishDate" type="text" id="actualFinishDate<s:property value="#st.index"/>"
										size="15"  maxlength="18"
										value="<s:date name='actualFinishDate' format='yyyy-MM-dd'/>" />
									</div>
								</td>


								<td bgcolor="#FFFFFF" align="left"><s:select
										list="{'熟练掌握','基本掌握','有待提高'}" name="finishEffect"
										value="finishEffect" theme="simple" cssStyle="width:90px;"
										headerKey="" headerValue="----" id="finishEffect"></s:select>
								</td>

								<input type="hidden" name="ecID" value="" id="ecID">

							</tr>
						</s:iterator>

					</table>
		</table>
		<br />

		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
			<tr>
				<td><legend>
						<s:text name="label.tips.title" />
					</legend>
					<table width="100%">
						<tr>
							<td><s:text name="label.admin.content" />，该处的勾选按钮只用于删除操作，对提交操作无影响，第一条不能删除</td>
							<td width="6%"><input type="button" value="新增"
								onClick="addTrainClass()" /></td>
							<td width="6%"><input type="button" value="删除"
								onClick="delTrainClass()" /></td>
							<!--<td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>-->
						</tr>
					</table></td>
			</tr>
			<td align="center"><br /> <input type="button" name="ok"
				value='<s:text name="grpInfo.ok" />' class="MyButton"
				onClick="save()" image="../../images/share/yes1.gif"> <input
				type="button" name="return" value='<s:text name="button.close"/>'
				class="MyButton" onclick="closeModal(true);"
				image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
	<div id="test"></div>
</body>
</html>

