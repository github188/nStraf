﻿<!--本页面为list新版页面，修改日期2010-1-5，后台字段未改动，但字段名需根据新需求改动-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="cn.grgbanking.feeltm.staff.service.StaffInfoService"%>
<%@page import="cn.grgbanking.feeltm.project.service.ProjectService"%>
<%@page import="cn.grgbanking.feeltm.context.BaseApplicationContext" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%
	UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
	String username = userModel.getUserid();
	StaffInfoService staffInfoService =  (StaffInfoService) BaseApplicationContext.getAppContext().getBean("staffInfoService");
	ProjectService projectService =  (ProjectService) BaseApplicationContext.getAppContext().getBean("projectService");
	String groupName=projectService.getProjectNameByUserid(userModel.getUserid());
	String deptname=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
	groupName=groupName.indexOf(',')>0?groupName.substring(0,groupName.indexOf(',')):groupName;
%>
<html>
<head><title></title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript"> 
	var firstLogin = true;
	var returnIt = false;
	function init(){
		var myDate= new Date();
		var month=myDate.getMonth();
		month=parseInt(month)+1;
		var day=0;
		var year=parseInt(myDate.getFullYear());
		if(month!=2){
			if(month==1 || month ==3 || month ==5 || month ==7 || month ==8 || month ==10 || month ==12){ 
				day="31";
			}else{
				day="30";
			}
		}else{
			if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
				day="28";
			}else{
				day="29"
			}
		}
		var strMonthTemp = "0" + month;
		var strMonthL = strMonthTemp.length;
		var strMonth = strMonthTemp.substring(strMonthL-2, 2);
		var s1 = myDate.getFullYear() + "-" + strMonth + "-01";//第一天
		var s2 = myDate.getFullYear() + "-" + strMonth + "-" + day;//最后一天
		document.getElementById("start").value=s1;
		document.getElementById("end").value=s2;
		var username="<%=username%>";
		if(username=="" || username==null || username=="null"){
			username="";
		}else{
			document.getElementById("username").value="<%=username%>";
		}
		//document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].text="<%=groupName%>";
		//document.getElementById("deptName").options[document.getElementById("deptName").selectedIndex].text="<%=deptname%>";
		query();
	}
	function query(){
		if(!validateInputInfo()){
			return ;
		}
		 var username =  document.getElementById("idusername").options[document.getElementById("idusername").selectedIndex].text;
		var start =  document.getElementById("start").value;
		var end =  document.getElementById("end").value;
		var groupName=document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].text;
		
		if(document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].title){
			groupName = document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].title;
		}
		
		
//		var prjName =  document.getElementById("prjName").value;
		var searchtype = document.getElementById("searchtype").value;
		
		var deptName = document.getElementById("deptName").options[document.getElementById("deptName").selectedIndex].text;
		if($("#idusername").is(":visible")==true&&username=="----请选择人员----"){
			username="";
		}else if($("#idusername").is(":visible")==true){
			//username=username.substring(0, username.indexOf("(", 0));
		}else{
			username=document.getElementById("username").value;
		}
		if(groupName=="---请选择项目名称---")
			groupName="";
		if(deptName=="---请选择部门---")
			deptName="";
		var pageNum = document.getElementById("pageNum").value;
//		if(prjName.trim() == "--------黑盒测试组项目列表--------" || prjName.trim() == "------自动化测试组项目列表------" || prjName.trim() == "--------白盒测试组项目列表--------" || prjName.trim() == "--------质量管理组项目列表--------" || prjName.trim() == "------------其他事项列表------------"){
//			alert("项目名称请确认是否正确");
//			document.getElementById("prjName").value = "";
//			return;
//		}
		var actionUrl = "<%=request.getContextPath()%>/pages/workhour/workhouraction!querySummary.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&username="+username+"&start="+start+"&end="+end+"&groupName="+groupName+"&searchtype="+searchtype;
		actionUrl += "&deptName="+deptName;
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0 ; %>
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	function setHTML(entry,entryInfo){
		for(var i=1;i<=5;i++){
			document.getElementById("line"+i).style.display="";
		}
		var html = '';
		//if(entryInfo["prjName"] == "" || entryInfo["prjName"] == "汇总"){
		if(entryInfo["username"] == "汇总" 
				|| entryInfo["deptname"] == "汇总"
				|| entryInfo["groupname"] == "汇总"
				|| entryInfo["prjName"] == "汇总"){
			html += '<tr bgcolor="#6699FF" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		}else{
			html += '<tr class="trClass<%=k%2 %>" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		}
		var typevalue = document.getElementById("searchtype").value;
		if(typevalue==1){
			html += '<td >' +entryInfo["username"] + '</td>';
			html += '<td >' +entryInfo["deptname"] + '</td>';
			var groupname=entryInfo["groupname"];
			html += '<td title='+groupname+'>' +((groupname.length>15)?(groupname.substring(0,15)+'...'): groupname)+'</td>';
			//html += '<td >' +entryInfo["groupname"] + '</td>';
			for(var i=1;i<=5;i++){
				if(i==4 || i==5){
					document.getElementById("line"+i).style.display="none";
				}else{
					document.getElementById("line"+i).style.display="";
				}
			}
		}else if(typevalue==2){
			html += '<td >' +entryInfo["deptname"] + '</td>';
			for(var i=1;i<=5;i++){
				if(i==1|| i==4){
					document.getElementById("line"+i).style.display="none";
				}else{
					document.getElementById("line"+i).style.display="";
				}
			}
		}else if(typevalue==3){
			var groupname=entryInfo["groupname"];
			html += '<td title='+groupname+'>' +((groupname.length>15)?(groupname.substring(0,15)+'...'): groupname)+'</td>';
			//html += '<td >' +entryInfo["groupname"] + '</td>';
			for(var i=1;i<=5;i++){
				if(i==1 || i==2){
					document.getElementById("line"+i).style.display="none";
				}else{
					document.getElementById("line"+i).style.display="";
				}
			}
		}else if(typevalue==4){
			for(var i=1;i<=5;i++){
				if(i==4 || i==5){
					document.getElementById("line"+i).style.display="";
				}else{
					document.getElementById("line"+i).style.display="none";
				}
			}
		}
//		html += '<td >' +entryInfo["prjName"] + '</td>';
		html += '<td >' +entryInfo["daywork"] + '</td>';
		html += '<td>'+ entryInfo["requirement"]+ '</td>';
		html += '<td>' +entryInfo["design"] + '</td>';
		html += '<td>' + entryInfo["code"] + '</td>';
		html += '<td>' + entryInfo["test"] + '</td>';
		html += '<td>' + entryInfo["managerment"] + '</td>';
		html += '<td>' + entryInfo["document"] + '</td>';
		html += '<td>' + entryInfo["meet"] + '</td>';
		html += '<td>' + entryInfo["train"] + '</td>';
		html += '<td>' + entryInfo["other"] + '</td>';
		html += '<td>' + entryInfo["sumtotal"] + '</td>';
		//html += '<td bgcolor="#6699FF">' + entryInfo["sumtotal"] + '</td>';
		if(typevalue==2 || typevalue==3 || typevalue==4){
			html += '<td >' +entryInfo["people"] + '</td>';
			html += '<td >' +entryInfo["userlevel"] + '</td>';
		}
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}

	function validateInputInfo() {
		var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
		var thisDate = document.getElementById("start").value;
		var endDate = document.getElementById("end").value;
		if (thisDate.length > 0 && endDate.length > 0) {
			var v = re.test(endDate);
			var a = re1.test(thisDate);
			if (!v || !a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			if (!DateValidate('start', 'end')) {
				alert('开始日期大于结束日期，请重新输入！');
				return false;
			}
		} else if (thisDate.length > 0 && endDate.length == 0) {
			var a = re1.test(thisDate);
			if (!a) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		} else if (thisDate.length == 0 && endDate.length > 0) {
			var v = re.test(endDate);
			if (!v) {
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
		}
		return true;
	}

	function DateValidate(beginDate, endDate) {
		var Require = /.+/;
		var begin = document.getElementsByName(beginDate)[0].value.trim();
		var end = document.getElementsByName(endDate)[0].value.trim();
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
		if (keycode == 13 || keycode == 108) {
			document.getElementById("bodyid").focus();
			query();
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

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	listenKey();
</script>
 <body id="bodyid" style="margin: 0" onload="init();">
 	<s:form name="workhourForm"  namespace="/pages/workhour" action="workhouraction!querySummary.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <input name="levelHidden" type="hidden" id="levelHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel() %>'/>
 	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 		<td >
			<table width="100%" class="select_area">
			  <tr>
				<td width="10%" align="center" class="input_label">开始日期：</td>
				<td width="15%"><input name="start"  type="text" id="start" size="15" class="MyInput"   /> </td>
				<td width="10%" align="center" class="input_label">结束日期：</td>
				<td width="15%"> <input name="end"  type="text" id="end" size="15" class="MyInput"  /> </td>
				<td width="10%" align="center" class="input_label">查询类别：</td>
                <td width="15%">
                	<select id="searchtype" name="searchtype" style="width:100%;">
                		<option value="1">个人</option>
                		<option value="2">部门</option>
                		<option value="3">项目</option>
                	</select>
                </td>
     <!--            <td width="10%" align="center" class="input_label">项目名称：</td>
                <td width="15%"><tm:tmSelect name="prjName" id="prjName" selType="dataDir" path="systemConfig.projectname"  style="width:100%;" />
                	<script type="text/javascript">
						var html=$("#prjName").html();
						$("#prjName").html("");
						html="<option value='' selected></option>"+html;
						$("#prjName").html(html);
						$("#prjName").val("");
   					</script>
   				</td>-->
			  </tr>
              <tr>
            	<tm:deptSelect 
					deptId="deptName" 
					groupId="groupName"
					userId="username" 
					isloadName="false" 
					deptHeadKey="---请选择部门---" 
					deptHeadValue="全选" 
					userHeadKey="----请选择人员----" 
					userHeadValue="全选"  
					groupHeadKey="---请选择项目名称---"
					groupHeadValue="全选"
					labelDept="部门 ：" 
					labelGroup="项目名称：" 
					labelUser="姓名 ：" 
					deptLabelClass="align:center; width:10%;class:input_label"
					deptClass="align:left;width:15%;" 
					groupLabelClass="align:center; width:10%;class:input_label"
					groupClass="align:left;width:15%;" 
					userLabelClass="align:center; width:10%;class:input_label"
					userClass="align:left;width:15%;">
				</tm:deptSelect>
				<td align="right" colspan="2"> <tm:button site="1"/></td>
				<!-- <td align="right" colspan="2"><input type="button" id="searchv" name="searchv" value="查询" onclick="query()"></td>-->
              </tr>
			</table> 
		</td> 
		</tr>
	</table>
	<br/>
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0"  class="bgbuttonselect">
		<tr>
		 <td width="25"  height="23" valign="middle">&nbsp;</td>
		 <td class="orarowhead"><s:text name="operInfo.title" /></td>
		 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>

	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
		<tr> 
			<td nowrap width="8%" class="oracolumncenterheader" id="line1"><div align="center">姓名</div></td>
			<td nowrap width="8%" class="oracolumncenterheader" id="line2"><div align="center">部门</div></td>
			<td nowrap width="10%" class="oracolumncenterheader" id="line3"><div align="center">项目名称</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">日常工作</div></td>
			<td nowrap width="4%" class="oracolumncenterheader"><div align="center">需求</div></td>
			<td nowrap width="4%" class="oracolumncenterheader"><div align="center">设计</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">编码</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">测试</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">管理</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">文档</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">会议</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">培训</div></td>
            <td nowrap width="4%" class="oracolumncenterheader"><div align="center">其他</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">小计</div></td>
            <td nowrap width="8%" class="oracolumncenterheader" id="line4"><div align="center">参与人员</div></td>
            <td nowrap width="8%" class="oracolumncenterheader" id="line5"><div align="center">人力成本</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  			 
 		</tbody>
 	</table>
 		<table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1" cellspacing="1" >  
		<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
					<td width="83%" align="right">
					<div id="pagetag"><tm:pagetag pageName="currPage" formName="reportInfoForm" /></div>
				</td>
			</tr>
			</table>
		</td>
		</tr>
	</table>
 </s:form>
</body>
</html>

