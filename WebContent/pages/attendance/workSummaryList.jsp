<!--本页面为list新版页面，修改日期2010-1-5，后台字段未改动，但字段名需根据新需求改动-->
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
	ProjectService projectService =  (ProjectService) BaseApplicationContext.getAppContext().getBean("projectGroupService");
	String groupName=projectService.getProjectNameByUserid(userModel.getUserid());
	StaffInfoService staffInfoService =  (StaffInfoService) BaseApplicationContext.getAppContext().getBean("staffInfoService");
	String deptname=staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
	groupName=groupName.indexOf(',')>0?groupName.substring(0,groupName.indexOf(',')):groupName;
%>
<html>
<head><title></title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript"> 
	var firstLogin = true;
	var returnIt = false;
	function query(){
		var startdate = document.getElementById("startdate").value;
		var enddate = document.getElementById("enddate").value;
		var pageNum = document.getElementById("pageNum").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!queryWorkSummary.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&&startdate="+startdate+"&enddate="+enddate;
		//alert(actionUrl);
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0 ; %>
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	function setHTML(entry,entryInfo){
		var html = '<tr class="trClass<%=k%2 %>" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td >' +entryInfo["employeename"] + '</td>';
		html += '<td>' +entryInfo["dept"] + '</td>';
		html += '<td>' +entryInfo["begindate"].substr(0,10) + '</td>';
		html += '<td>' +entryInfo["enddate"].substr(0,10) + '</td>';
		html += '<td>' +entryInfo["shouldattendhours"] + '</td>';
		html += '<td>' + entryInfo["reallyattendhours"] + '</td>';
		
		html += '<td >' +entryInfo["monthleavedays"] + '</td>';
		html += '<td>' +entryInfo["normalovertimehours"] + '</td>';
		html += '<td>' +entryInfo["weekendovertimehours"] + '</td>';
		html += '<td>' +entryInfo["businesstrip"] + '</td>';
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}
	
	function toExport(){
		var username = document.getElementById("username").value.trim();
		var deptname = document.getElementById("deptname").value.trim();
		var groupname = document.getElementById("groupname").value.trim();
	    var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!exportData.action";
		actionUrl += "?username="+username;
        actionUrl += "&deptname="+deptname;
        actionUrl += "&groupname="+groupname;
		actionUrl=encodeURI(actionUrl);
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
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
	
	function init(){
		query();
	}
</script>
 <body id="bodyid" style="margin: 0" onload="init();">
 	<s:form name="workhourForm"  namespace="/pages/attendance" action="attendanceAction!queryWorkSummary.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <input name="levelHidden" type="hidden" id="levelHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel() %>'/>
 	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 		<td >
			<table width="100%" class="select_area">
              <tr>
            	<td width="10%" align="center" class="input_label">开始日期：</td>
				<td width="20%"><input name="startdate" type="text"
					id="overtimeDay" size="13" class="MyInput" 
					value='<s:date name="startdate" format="yyyy-MM-dd"/>' />
				</td>
				<td width="10%" align="center" class="input_label">结束日期：</td>
				<td width="20%"><input name="enddate" type="text"
					id="overtimeDayEnd" size="13" class="MyInput" 
					value='<s:date name="enddate" format="yyyy-MM-dd"/>' />
				</td>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">姓名</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">部门</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">开始时间</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">结束时间</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">应出勤日</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">实出勤日</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">月请假时数</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">平时加班时数</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">周末加班时数 </div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">出差时数</div></td>
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

