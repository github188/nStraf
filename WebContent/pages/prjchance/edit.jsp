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
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	function save() {
		var prjNamevalue=document.getElementById("prjName").value;
		    prjNamevalue=prjNamevalue.replace(/(^\s*)|(\s*$)/g,'');
		if (prjNamevalue== "") {
			alert("请填写项目名称");
			document.getElementById("prjName").focus();
			return;
		}
		
		 var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
		 var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
		 var phoneormobile=document.getElementById("contactortel").value;
		 if(phoneormobile!=""){
			 if(!isPhone.test(phoneormobile)&&!isMob.test(phoneormobile)){
			    	alert("请填写正确的联系方式！");
					return;
			    }
		 }
		 
		 var otherInfoList_relMan= $("input[name='otherInfoList_relMan']");
			var relManlen=otherInfoList_relMan.size();
			for(var i=0;i<relManlen;i++){
				var relmanvalue=otherInfoList_relMan[i].value;
				relmanvalue=relmanvalue.replace(/(^\s*)|(\s*$)/g,'');
				if(relmanvalue==""){
					alert("请填写联系人");
					$("input[name='otherInfoList_relMan']").focus();
					return;
				}
			}
			
			var followList_followMan= $("input[name='followList_followMan']");
			var followManlen=followList_followMan.size();
			for(var i=0;i<followManlen;i++){
				var followManvalue=followList_followMan[i].value;
				followManvalue = followManvalue.replace(/(^\s*)|(\s*$)/g,'');
				if(followManvalue==""){
					alert("请填写跟进人");
					$("input[name='followList_followMan']").focus();
					return;
				}
			}
			
			
// 			var followList_followContent= $("textarea[name='followList_followContent']");
// 			var followContentlen=followList_followContent.size();
// 			alert(followContentlen);
// 			for(var i=0;i<followContentlen;i++){
// 				alert();
// 				if(followList_followContent[i].value==""){
// 					alert("请填写跟进内容");
// 					$("textarea[name='followList_followContent']").focus();
// 					return;
// 				}
// 			}
       var length = $("textarea[name='followList_followContent']").length;
       var flag = true;
       if(length == 0){
    	   document.getElementById("ok").disabled = true;
		   window.returnValue = true;
		   prjChanceInfoForm.submit();
       }else{
    	   
          $("textarea[name='followList_followContent']").each(function (){
        	  var text = $(this).val();
        	  text = text.replace(/(^\s*)|(\s*$)/g,'');
        	  if(text == ""){
        		  alert("跟进内容不能为空，请输入内容");
        		  $(this).focus();
        		  flag = false;
        		  
        	  }else{
			   }
		    });
          }
       
       if(flag){
    	   
	               document.getElementById("ok").disabled = true;
				   window.returnValue = true;
				   prjChanceInfoForm.submit();
       }

	}

	/**--------------------------------其他信息-------------------------------------------**/
	function addAgain() {
		if ($("#otherInfoContainer").children().size() <= 0) {
			$("#otherInfoContainer").append(getOtherInfoTable());
		} else {
			$("#otherInfoContainer").children("table:last").after(
					getOtherInfoTable());
		}
		//重新对checkbox的序号编号
		renameCheckboxSpan();
	}

	function delAgain() {
		//被选中的checkbox
		var checked = 0;
		$("div[name='otherInfoCheckBoxDiv']").find("input[type='checkbox']")
				.each(
						function() {
							if ($(this).attr('checked') == true
									|| $(this).prop('checked') == true) {
								checked++;
							}
						});
		if (checked == 0) {
			return;
		}
		$("div[name='otherInfoCheckBoxDiv']").find("input[type='checkbox']")
				.each(
						function() {
							if ($(this).attr('checked') == true
									|| $(this).prop('checked') == true) {
								$(this).parent().parent().parent().parent()
										.remove();
							}
						});
		//重新对checkbox的序号编号
		renameCheckboxSpan();
	}

	function getOtherInfoTable() {
		var html = '<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">';
		html += '	<tr>';
		html += '		<td bgcolor="#FFFFFF" width="5%"><div name="otherInfoCheckBoxDiv"><input type="checkbox" /><span id="indexvalue">1</span></div></td>';
		html += '		<td bgcolor="#E6E6E6" >';
		html += '			<table width="100%" align="center" cellPadding="1" cellSpacing="1">';
		html += '				<tr>';
		html += '					<td bgcolor="#FFFFFF" style="width:12%" align="right">联系人:</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:22%">';
		html += '						<input name="otherInfoList_relMan" type="text" style="width: 90%" value="">';
		html += '					</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:13%" align="right">联系人部门:</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:18%">';
		html += '						<input name="otherInfoList_relDept" type="text" style="width: 90%" value="">';
		html += '					</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:13%" align="right">联系人职位:</td>';
		html += '					<td bgcolor="#FFFFFF">';
		html += '						<input name="otherInfoList_relPosition" type="text" style="width: 90%" value="">';
		html += '					</td>';
		html += '				</tr>';
		html += '			</table>';
		html += '		</td>';
		html += '	</tr>';
		html += '</table>';
		return html;
	}
	function renameCheckboxSpan() {
		var i = 0;
		$("div[name='otherInfoCheckBoxDiv']").find("span").each(function() {
			i++;
			$(this).html(i);
		});
	}

	/**--------------------------------跟进信息-------------------------------------------**/
	function addAgain2() {
		if ($("#followContainer").children().size() <= 0) {
			$("#followContainer").append(getFollowTable());
		} else {
			$("#followContainer").children("table:last")
					.after(getFollowTable());
		}
		//重新对checkbox的序号编号
		renameCheckboxSpan2();
		resetLastFollowDate();
		listenCaldarInput($("#attendanceContainer2").children("table:last"));
	}

	function delAgain2() {
		//被选中的checkbox
		var checked = 0;
		$("div[name='followCheckBoxDiv2']").find("input[type='checkbox']")
				.each(
						function() {
							if ($(this).attr('checked') == true
									|| $(this).prop('checked') == true) {
								checked++;
							}
						});
		if (checked == 0) {
			return;
		}
		$("div[name='followCheckBoxDiv2']").find("input[type='checkbox']")
				.each(
						function() {
							if ($(this).attr('checked') == true
									|| $(this).prop('checked') == true) {
								$(this).parent().parent().parent().parent()
										.remove();
							}
						});
		//重新对checkbox的序号编号
		renameCheckboxSpan2();
		resetLastFollowDate();
	}

	function getFollowTable() {
		var html = '<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">';
		html += '	<tr>';
		html += '		<td bgcolor="#FFFFFF" width="5%"><div name="followCheckBoxDiv2"><input type="checkbox" /><span id="indexvalue">1</span></div></td>';
		html += '		<td bgcolor="#E6E6E6" >';
		html += '			<table width="100%" align="center" cellPadding="1" cellSpacing="1">';
		html += '				<tr>';
		html += '					<td bgcolor="#FFFFFF" style="width:10%" align="right">跟进人:</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:23%">';
		html += '						<input name="followList_followMan" type="text" style="width: 90%" value="">';
		html += '					</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:10%" align="right">跟进日期:</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:23%">';
		html += '						<input type="text" class="CalendarInput" name="followList_followDate" onchange="resetLastFollowDate()" value="'
				+ getNowTime() + '"  style="width:90%" value=""/>';
		html += '					</td>';
		html += '					<td bgcolor="#FFFFFF" style="width:10%" align="right">项目阶段:</td>';
		html += '					<td bgcolor="#FFFFFF">';
		html += '						<select name="followList_prjStage" style="width:90%">';
		<s:iterator value="#request.prjStage">
		html += '<option value="<s:property value="key"/>"><s:property value="value" /></option>';
		</s:iterator>
		html += '						</select>';
		html += '					</td>';
		html += '				</tr>';
		html += '				<tr>';
		html += '					<td bgcolor="#FFFFFF" align="right">跟进内容:</td>';
		html += '					<td bgcolor="#FFFFFF" colspan="5">';
		html += '						<textarea name="followList_followContent" onkeyup="limitLen(2000,this)" cols="84" rows="2"></textarea>';
		html += '					</td>';
		html += '				</tr>';
		html += '			</table>';
		html += '		</td>';
		html += '	</tr>';
		html += '</table>';
		return html;
	}
	function renameCheckboxSpan2() {
		var i = 0;
		$("div[name='followCheckBoxDiv2']").find("span").each(function() {
			i++;
			$(this).html(i);
		});
	}

	//当添加或修改跟进信息时，自动计算最近跟进时间，并放到基本信息中
	function resetLastFollowDate() {
		var lastFollowDate = "0000-00-00 00:00:00";
		$("#followContainer").find("input[name='followList_followDate']").each(
				function() {
					var thisDate = $(this).val();
					if (thisDate != ''
							&& compareCalendar(thisDate, lastFollowDate) == 1) {
						lastFollowDate = thisDate;
					}
				});
		if (lastFollowDate != "0000-00-00 00:00:00") {//相等表示没有跟进信息中的时间都是空的，则不修改
			$("#lastFollowDate").val(lastFollowDate);
		}
	}

	function getNowTime() {
		var currentTime = "";
		var myDate = new Date();
		var year = myDate.getFullYear();
		var month = parseInt(myDate.getMonth().toString()) + 1; //month是从0开始计数的，因此要 + 1
		if (month < 10) {
			month = "0" + month.toString();
		}
		var date = myDate.getDate();
		if (date < 10) {
			date = "0" + date.toString();
		}
		var hour = myDate.getHours();
		if (hour < 10) {
			hour = "0" + hour.toString();
		}
		var minute = myDate.getMinutes();
		if (minute < 10) {
			minute = "0" + minute.toString();
		}
		var second = myDate.getSeconds();
		if (second < 10) {
			second = "0" + second.toString();
		}

		currentTime = year.toString() + "-" + month.toString() + "-"
				+ date.toString() + " " + hour.toString() + ":"
				+ minute.toString() + ":" + second.toString(); //以时间格式返回
		return currentTime;
	}

	//比较日期，时间大小  
	function compareCalendar(startDate, endDate) {
		if (startDate.indexOf(" ") != -1 && endDate.indexOf(" ") != -1) {
			//包含时间，日期  
			return compareTime(startDate, endDate);
		} else {
			//不包含时间，只包含日期  
			return compareDate(startDate, endDate);
		}
	}

	//比较日前大小  
	function compareDate(checkStartDate, checkEndDate) {
		var arys1 = new Array();
		var arys2 = new Array();
		if (checkStartDate != null && checkEndDate != null) {
			arys1 = checkStartDate.split('-');
			var sdate = new Date(arys1[0], parseInt(arys1[1] - 1), arys1[2]);
			arys2 = checkEndDate.split('-');
			var edate = new Date(arys2[0], parseInt(arys2[1] - 1), arys2[2]);
			if (sdate > edate) {
				return 1;
			} else {
				return 2;
			}
		}
	}

	//判断日期，时间大小  
	function compareTime(startDate, endDate) {
		if (startDate.length > 0 && endDate.length > 0) {
			var startDateTemp = startDate.split(" ");
			var endDateTemp = endDate.split(" ");

			var arrStartDate = startDateTemp[0].split("-");
			var arrEndDate = endDateTemp[0].split("-");

			var arrStartTime = startDateTemp[1].split(":");
			var arrEndTime = endDateTemp[1].split(":");

			var allStartDate = new Date(arrStartDate[0], arrStartDate[1],
					arrStartDate[2], arrStartTime[0], arrStartTime[1],
					arrStartTime[2]);
			var allEndDate = new Date(arrEndDate[0], arrEndDate[1],
					arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);

			if (allStartDate.getTime() >= allEndDate.getTime()) {
				return 1;
			} else {
				return 2;
			}
		} else {
			return 2;
		}
	}

	//监听JqueryUI的Calendar控件
	function listenCaldarInput() {

		$(".CalendarInput").datetimepicker({
			dateFormat : 'yy-mm-dd', //更改时间显示模式  
			changeMonth : true, //是否显示月份的下拉菜单，默认为false  
			changeYear : true, //是否显示年份的下拉菜单，默认为false  
			showSecond : true, //显示秒
			timeFormat : 'HH:mm:ss',
			timeOnlyTitle : '时间选择',
			timeText : '时间',
			hourText : '时',
			minuteText : '钟',
			secondText : '秒',
			stepHour : 1,//设置步长
			stepMinute : 1,
			stepSecond : 1
		});
	}

	$(function() {
		listenCaldarInput();
	})
</script>
<body id="bodyid">
<form name="prjChanceInfoForm" action="<%=request.getContextPath()%>/pages/prjchance/prjChanceInfo!update.action" method="post">
	<input type="hidden" name="pageNum" id="pageNum" value="1" /> 
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
				<table class="input_table">
				
					<tr>
						<td class="input_tablehead">商机基本信息</td>
					</tr>
					<tr>
						<td class="input_label2">
							<input type="hidden" name="prjChance.id" value="<s:property value="prjChance.id"/>"/>
							<input type="hidden" name="prjChance.lastSendEmailTime" id="lastSendEmailTime" value="<s:property value="prjChance.lastSendEmailTime"/>" />
							
							<div align="center">
								项目名称<font color="#FF0000">*</font>
							</div>
						</td>
						<td align="left" bgcolor="#FFFFFF" width="95%" colspan="3">
							<input name="prjChance.prjName" id="prjName" maxlength="80" size="80" id="client" value='<s:property value="prjChance.prjName"/>'>
						</td>
						<td width="13%" class="input_label2">
							<div align="center">客户名称</div>
						</td>
						<td bgcolor="#FFFFFF" width="25%">
							<input name="prjChance.client" maxlength="22" size="22" id="client" value='<s:property value="prjChance.client"/>'>
						</td>
					</tr>
					<tr>
						<td class="input_label2"  width="11%">
							<div align="center">客户联系人</div>
						</td>
						<td bgcolor="#FFFFFF" width="23%">
							<input name="prjChance.clientLinkMan" maxlength="22" size="22" id="clientLinkMan" value='<s:property value="prjChance.clientLinkMan"/>'>
						</td>
						<td class="input_label2" width="10%">
							<div align="center">联系人部门</div>
						</td>
						<td bgcolor="#FFFFFF" width="23%">
							<input name="prjChance.linkManDept" maxlength="22" size="22" id="linkManDept" value='<s:property value="prjChance.linkManDept"/>'></td>
						<td class="input_label2" width="10%">
							<div align="center">联系人职位</div>
						</td>
						<td bgcolor="#FFFFFF" width="23%">
							<input name="prjChance.linkManPosition" maxlength="22" size="22" id="linkManPosition" value='<s:property value="prjChance.linkManPosition"/>'>
						</td>
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">所属区域</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.area"
							maxlength="22" size="22" id="area"
							value='<s:property value="prjChance.area"/>'>
						</td>
						<td class="input_label2">
							<div align="center">省份</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.province"
							maxlength="22" size="22" id="province"
							value='<s:property value="prjChance.province"/>'></td>
						<td class="input_label2">
							<div align="center">客户经理</div>
						</td>
						<td bgcolor="#FFFFFF"><input
							name="prjChance.clientManager" maxlength="22" size="22"
							id="clientManager"
							value='<s:property value="prjChance.clientManager"/>'>
						</td>
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">客户类别</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.clientType"
							maxlength="22" size="22" id="clientType"
							value='<s:property value="prjChance.clientType"/>'></td>
						<td class="input_label2">
							<div align="center">跟进人</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.followMan"
							maxlength="22" size="22" id="followMan"
							value='<s:property value="prjChance.followMan"/>'></td>
						<td class="input_label2">
							<div align="center">最后跟进日期</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="3">
							<input name="prjChance.lastFollowDate"  size="22" type="text" id="lastFollowDate" class="CalendarInput"  value='<s:date name="prjChance.lastFollowDate" format="yyyy-MM-dd HH:mm:ss"/>'>
						</td>
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">项目阶段</div>
						</td>
						<td bgcolor="#FFFFFF">
							<s:select style="width:90%" list="#request.prjStage" id="prjStage" name="prjChance.prjStage" listKey="key" listValue="value"></s:select>
						</td>
						<td class="input_label2">
							<div align="center">项目预算</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.budget"
							maxlength="22" size="22" id="budget"
							value='<s:property value="prjChance.budget"/>'></td>
						<td class="input_label2">
							<div align="center">登记日期</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="3">
							<input name="prjChance.creatDate" type="text"  size="22" id="creatDate" class="CalendarInput" value='<s:date name="prjChance.creatDate" format="yyyy-MM-dd HH:mm:ss"/>'>
						</td>
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">项目结果</div>
						</td>
						<td bgcolor="#FFFFFF">
							<s:select style="width:90%" list="#request.prjResult" id="prjResult" name="prjChance.prjResult" listKey="key" listValue="value"></s:select>
						</td>
						<td class="input_label2">
							<div align="center">邮件抄送(用;分开)</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="3">
							<input name="prjChance.copyEmail" size="72" type="text" id="copyEmail"  value='<s:property value="prjChance.copyEmail"/>'>
						</td>
					</tr>
					<tr>
						
						<td class="input_label2">
							<div align="center">联系人电话</div>
						</td>
						<td bgcolor="#FFFFFF"><input name="prjChance.contactortel"
							maxlength="22" size="22" id="contactortel"
							value='<s:property value="prjChance.contactortel"/>'></td>
						
						
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">商机简述</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="5">
							<div><textarea name="prjChance.description" onkeyup="limitLen(500,this)" cols="90" rows="3" id="description"><s:property value="prjChance.description" /></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td class="input_label2">
							<div align="center">
								备注<font color="#FF0000"></font>
							</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="5">
							<div><textarea name="prjChance.note" onkeyup="limitLen(500,this)" cols="90" rows="3" id="note"><s:property value="prjChance.note" /></textarea></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><br /></td>
		</tr>
		<tr>
			<td>
				<table class="input_table">
					<tr>
						<td class="input_tablehead">其他信息</td>
					</tr>
					<tr>
						<td id="otherInfoContainer">
							<s:iterator value="otherInfoList" status="row">
								<table bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%">
											<div name="otherInfoCheckBoxDiv">
												<input type="checkbox" />
												<span id="indexvalue">
													<s:property value="#row.index+1" />
												</span>
											</div></td>
										<td bgcolor="#E6E6E6">
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width: 12%" align="right">联系人:</td>
													<td bgcolor="#FFFFFF" style="width: 22%">
														<input name="otherInfoList_relMan" type="text" style="width: 90%" value='<s:property value="relMan" />'>
													</td>
													<td bgcolor="#FFFFFF" style="width: 13%" align="right">联系人部门:</td>
													<td bgcolor="#FFFFFF" style="width: 18%">
														<input name="otherInfoList_relDept" type="text" style="width: 90%" value='<s:property value="relDept" />'>
													<td bgcolor="#FFFFFF" style="width: 13%" align="right">联系人职位:</td>
													<td bgcolor="#FFFFFF">
														<input name="otherInfoList_relPosition" type="text" style="width: 90%" value='<s:property value="relPosition" />'>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</s:iterator>
						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" align="center" cellPadding="1"
								cellSpacing="1">
								<tr>
									<td></td>
									<td width="6%"><input type="button" value="添加"
										onClick="addAgain()" /></td>
									<td width="6%"><input type="button" value="删除"
										onClick="delAgain()" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><br /></td>
		</tr>
		<tr>
			<td>
				<table class="input_table">
					<tr>
						<td class="input_tablehead">跟进信息</td>
					</tr>
					<tr>
						<td id="followContainer">
							<s:iterator value="followList" status="row" id="follow">
								<table bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%">
											<div name="followCheckBoxDiv2">
												<input type="checkbox" />
												<span id="indexvalue">
													<s:property value="#row.index+1" />
												</span>
											</div>
										</td>
										<td bgcolor="#E6E6E6">
											<table width="100%" align="center" cellPadding="1"
												cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width: 10%" align="right">跟进人:</td>
													<td bgcolor="#FFFFFF" style="width: 23%">
														<input name="followList_followMan" type="text"   style="width: 90%" value='<s:property value="followMan" />'>
													</td>
													<td bgcolor="#FFFFFF" style="width: 10%" align="right">跟进日期:</td>
													<td bgcolor="#FFFFFF" style="width: 23%">
														<input name="followList_followDate" class="CalendarInput" type="text" style="width: 90%" onchange="resetLastFollowDate()" value='<s:date name="followDate" format="yyyy-MM-dd HH:mm:ss"/>'>
													<td bgcolor="#FFFFFF" style="width: 10%" align="right">项目阶段:</td>
													<td bgcolor="#FFFFFF">
														<s:select list="#request.prjStage" style="width:90%" name="followList_prjStage" listKey="key" listValue="value"  value="#follow.prjStage"></s:select>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" align="right">
														跟进内容:
													</td>
													<td bgcolor="#FFFFFF" colspan="5"><textarea name="followList_followContent" onkeyup="limitLen(2000,this)" cols="84" rows="2" ><s:property value="followContent" /> </textarea></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</s:iterator>
						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" align="center" cellPadding="1"
								cellSpacing="1">
								<tr>
									<td></td>
									<td width="6%"><input type="button" value="添加"
										onClick="addAgain2()" /></td>
									<td width="6%"><input type="button" value="删除"
										onClick="delAgain2()" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" align="center">
					<tr>
						<td align="center"><input type="button" name="ok" id="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onClick="save()" image="../../images/share/yes1.gif"> <input
							type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal(true);"
							image="../../images/share/f_closed.gif">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
