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
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
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

	function save() {
		var selcount = 0;
		var total = document.getElementsByName("planFinishDate").length;
		for (var ind = 0; ind < total; ind++) {
			if (document.getElementsByName("taskCkd1")[ind].checked) {
				var prjName = document.getElementsByName("courseName")[ind];
				var taskDesc = document.getElementsByName("category")[ind];   
				var responsible = document.getElementsByName("planFinishDate")[ind];
				var finishRate = document.getElementsByName("graspStandard")[ind];
				var delayReason = document.getElementsByName("prioryLevel")[ind];  
				if(prjName!=null){
					if(prjName.value.trim() == ""){
						alert("请输入课程名称");
						return;
					}
				}
				if(taskDesc!=null){
					if(taskDesc.value.trim() == ""){
						alert("请选择类别");
						return;
					}
				}
				if(responsible!=null){
					if(responsible.value.trim() == ""){
						alert("请输入计划完成时间");
						return;
					}
				}	
				if(finishRate!=null){
					if(finishRate.value.trim() == ""){
						alert("请选择掌握标准");
						return;
					}
				}	
				if(delayReason!=null){
					if(delayReason.value.trim() == ""){
						alert("请选择优先级");
						return;
					}
				}
				selcount++;
			}
		}
		if(selcount==0){
			alert("请选择一门课程");
			return;
		}
		var em = $("input[type='checkbox']"); 
		for(var  i=1;i<em.length;i++){
			if(em[i].type=="checkbox"){
				if(em[i].checked){
		        	$("#task"+(i-1)).find("[name='ck']").val(1);
		  		}else{
		  			$("#task"+(i-1)).find("[name='ck']").val(0);
		  		}
		  	} 
	 	} 
		/*$("[type='checkbox']").each(function(i, o) {
			if ($(o).attr('checked')) {
				var p = $(o).parent();
				var a = p.find("[name='ck']");
				a.val(1);
			} else {
				var p = $(o).parent();
				var a = p.find("[name='ck']");
				a.val(0);
			}
		});*/
		/*$("[name='ck']").each(
			function(i ,o){alert($(o).val());}
		);*/
		
		window.returnValue = true;
		reportInfoForm.submit();
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	
	$(function() {
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

	function addAgain() {
		var iFuskTotal = document.getElementsByName("planFinishDate").length; //获取当前的新增的记录数
		for (var ind = 0; ind < iFuskTotal; ind++) {
			var courseName = document.getElementsByName("courseName")[ind];
			var category = document.getElementsByName("category")[ind];
			var planFinishDate = document.getElementsByName("planFinishDate")[ind];
			var graspStandard = document.getElementsByName("graspStandard")[ind];
			var prioryLevel = document.getElementsByName("prioryLevel")[ind];
			if (courseName != null) {
				if (courseName.value.trim() == "") {
					alert("请输入课程名称");
					return;
				}
			}
			if (category != null) {
				if (category.value.trim() == "") {
					alert("请选择类别");
					return;
				}
			}
			if (planFinishDate != null) {
				if (planFinishDate.value.trim() == "") {
					alert("请输入计划完成时间");
					return;
				}
			}
			if (graspStandard != null) {
				if (graspStandard.value.trim() == "") {
					alert("请选择掌握标准");
					return;
				}
			}
			if (prioryLevel != null) {
				if (prioryLevel.value.trim() == "") {
					alert("请选择优先级");
					return;
				}
			}
		}

		var taskDivPPtr = document.getElementsByTagName("fieldset")[0]; //获取页面的fieldset的元素
		var taskDivPtr = taskDivPPtr.getElementsByTagName("table")[0]; //获取填写记录的表格，包括标题头以及具体的填写内容
		var taskTr = taskDivPtr.getElementsByTagName("tr")[iTaskIndex];
		//	var taskTr=taskDivPtr.childNodes[tableTaskIndex];  //获取填写的当前行
		var taskTr1 = taskTr.cloneNode(true); //复制当前的记录行
		taskTr1.id = "task" + iTaskIndex; //给复制的行的id赋值，'task1','task2'等，其中的1,2为任务的序列数
		iTaskIndex++; //将记录的序号数加1
		//给复制的表格中的每个元素赋值

		taskTr1.getElementsByTagName("input")[0].checked = true;
		taskTr1.getElementsByTagName("input")[1].value = 1;
		for (var j = 0; j < 6; j++) {
			taskTr1.getElementsByTagName("select")[j].value = ""; //给项目名称置为初始值
		}
		for (var k = 2; k < 5; k++) {
			taskTr1.getElementsByTagName("input")[k].value = "";
		}

		taskTr.parentElement.appendChild(taskTr1); //将复制的表格追加到后面
		// 将其序号数加1
		for (var ind = 0; ind < document.getElementsByName("planFinishDate").length; ind++) {
			document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind + 1);
			taskDivPtr.getElementsByTagName("tr")[ind + 1].id = "task" + ind;
		}
	}

	function delAgain() {
		var tipFlag = false;
		var ss = 0;
		var taskDivPPtr = document.getElementById("record"); //获取table元素
		var iTaskIndex = document.getElementsByName("planFinishDate").length;
		for (var ind = 0; ind < iTaskIndex; ind++) { //遍历任务序列号
			//var taskDivPtr = document.getElementById("task"+ind);  //获得每个数据输入行
			var taskDivPtr = taskDivPPtr.getElementsByTagName("tr")[ind + 1];
			if (taskDivPtr != null
					&& taskDivPtr.getElementsByTagName("input")[0].checked) {
				tipFlag = true;
				break;
			}
		}
		if (tipFlag) {
			if (!confirm('您确认删除记录吗？')) {
				return;
			}
		}

		var recordNum = iTaskIndex;
		//alert("recordNum:"+iTaskIndex);
		//var selectedRows=0;
		for (var ind1 = 0; ind1 < recordNum; ind1++) { ////遍历任务序列号
			//var taskDivPtr = document.getElementById("task"+ind1);  //获得任务序列号
			var taskDivPtr = taskDivPPtr.getElementsByTagName("tr")[ind1 + 1];

			if (taskDivPtr == null) {
				taskDivPtr = taskDivPPtr.getElementsByTagName("tr")[ind1 + 1
						- ss];
			}
			//alert("delAgain checked="+taskDivPtr.getElementsByTagName("input")[0].checked);
			//alert("delAgain iTaskIndex2="+taskDivPtr);
			if (taskDivPtr != null
					&& taskDivPtr.getElementsByTagName("input")[0].checked) { //如果该行被选定
				//alert("selectRow:"+ind)

				if (iTaskIndex == 1) {
					taskDivPtr.getElementsByTagName("input")[0].checked = false;
					taskDivPtr.getElementsByTagName("select")[0].value = ""; //给项目名称置为初始值
					taskDivPtr.getElementsByTagName("select")[1].value = ""; //给项目类型
					taskDivPtr.getElementsByTagName("select")[2].value = ""; //完成情况
					taskDivPtr.getElementsByTagName("select")[3].value = ""; //是否归档
					taskDivPtr.getElementsByTagName("select")[4].value = "0%"; //主任审核
					taskDivPtr.getElementsByTagName("select")[5].value = "";
					for (var ind2 = 0; ind2 < taskDivPtr
							.getElementsByTagName("input").length; ind2++) {
						if (taskDivPtr.getElementsByTagName("input")[ind2].type == "text") {
							taskDivPtr.getElementsByTagName("input")[ind2].value = "";
						}
					}
					document.getElementsByName("taskCkd1")[0].nextSibling.nodeValue = 1;
					return;
				} else {
					taskDivPtr.parentElement.removeChild(taskDivPtr);
					iTaskIndex--;
					ss++;
				}
				//}
			}
		}

		// add index
		for (var ind = 0; ind < document.getElementsByName("planFinishDate").length; ind++) {
			document.getElementsByName("taskCkd1")[ind].nextSibling.nodeValue = (ind + 1);
		}
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

	function res(chkAll) {
		/*if ($("#resid").attr("checked")) {
			$("[type='checkbox']").attr("checked", "true");
		} else {
			$("[type='checkbox']").removeAttr("checked");
		}*/
		var em = document.getElementsByTagName("input");
		for (var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox")
				em[i].checked = chkAll.checked
		}
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
			ckElement.value = 1;
		} else {
			ckElement.value = 0;
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
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/newEmployeeManage/newEmployeeManageinfo!updateDistribute.action"
		method="post">

		<table width="80%" align="center" cellPadding="1" cellSpacing="1"
			class="popnewdialog1">
			<tr>
				<td>
						
						<input type="hidden" name="uname" id="uname"
							value="<s:property value='uname'/>" /> <input type="hidden"
							name="uid" id="uid" value="<s:property value='uid'/>" />
						<input type="hidden" name="userid" id="userid"
							value="<s:property value='userid'/>" />
						<table width="100%"class="input_table" id="record">
							<tr><td class="input_tablehead">
							<s:property value="uname" />
							的课程分配
						</td></tr>
							<tr>
								<td width="6%" class="column_label"><div align="center">
										<input type="checkbox" id="resid" onclick="res(this)" checked />序号
									</div></td>
								<td width="20%" class="column_label"><div
										align="center">
										<font color="#000000">课程名称</font><font color="#FF0000">*</font>
									</div></td>
								<td width="4%" class="column_label"><div
										align="center">
										<font color="#000000">类别</font><font color="#FF0000">*</font>
									</div></td>
								<td width="11%" class="column_label"><div
										align="center">
										<font color="#000000">计划完成日期</font><font color="#FF0000">*</font>
									</div></td>
								<td width="11%" class="column_label"><div
										align="center">
										<font color="#000000">掌握标准</font><font color="#FF0000">*</font>
									</div></td>
								<td width="9%" class="column_label"><div align="center">
										<font color="#000000">优先级</font><font color="#FF0000">*</font>
									</div></td>
								<td width="10%" class="column_label"><div
										align="center">
										<font color="#000000">完成百分比</font>
									</div>
								<td width="17%" class="column_label"><div
										align="center">
										<font color="#000000">实际完成日期</font>
									</div></td>
								<td width="12%" class="column_label"><div
										align="center">
										<font color="#000000">完成效果</font>
									</div></td>
							</tr>

							<s:iterator value="ecList" id="records" status="st">
								<tr id='task<s:property value="#st.index"/>' name="taskDesc">
									<td bgcolor="#FFFFFF" align="left"><input type="checkbox"
										name="taskCkd1" onclick="change(this);" checked /><input
										type="hidden" name="ck" value="1" />
									<s:property value="#st.index+1" /></td>

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

									<td nowrap bgcolor="#FFFFFF">
										<div align="center">

											<input name="planFinishDate" type="text" id="planFinishDate<s:property value='#st.index'/>"
												size="15" class="MyInput" maxlength="18" 
												value="<s:date name='planFinishDate'  format='yyyy-MM-dd'/>" />
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

									<td nowrap bgcolor="#FFFFFF" align="left"><s:select
											list="percents" name="finishPercent" value="finishPercent"
											theme="simple" id="finishPercent"></s:select></td>

									<td nowrap bgcolor="#FFFFFF" align="left"><input
										name="actualFinishDate" type="text" id="actualFinishDate<s:property value='#st.index'/>"
										size="18" class="MyInput" maxlength="18" 
										value="<s:date name='actualFinishDate' format='yyyy-MM-dd'/>" />
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
				<td>
					<fieldset width="100%">
						<legend>
							<s:text name="label.tips.title" />
						</legend>
						<table width="100%">
							<tr>
								<td><s:text name="label.admin.content" />，选中代表该行记录被提交，未选中将不提交</td>
							</tr>
						</table>
					</fieldset>
				</td>
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
</body>
</html>

