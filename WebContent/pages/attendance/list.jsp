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
	function query(){
		var startdate = document.getElementById("overtimeDay").value;
		var enddate = document.getElementById("overtimeDayEnd").value;
		var pageNum = document.getElementById("pageNum").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!querySummary.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&startdate="+startdate+"&enddate="+enddate;
		//alert(actionUrl);
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0 ; %>
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	function setHTML(entry,entryInfo){
		var html = '<tr class="trClass<%=k%2 %>" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td >' +entryInfo["username"] + '</td>';
		html += '<td>' +entryInfo["workday"] + '</td>';
		var morning = entryInfo["mornlast"];
		var str1 = "0";
		var str2 = "0";
		if(morning.split(".").length>1){
			str1 = morning.split(".")[0];
			str2=morning.split(".")[1];
			morning = str1 + "." + str2;
			morning = parseFloat(morning);
		}
		html += '<td>' + morning + '</td>';
		html += '<td>' + entryInfo["afterlast"] + '</td>';
		var desc1 = entryInfo["desc1"];
		desc1 = desc1.replace(/@/g,"<br/>");
		html += '<td>' + desc1 + '</td>';
		html += '<td>' + entryInfo["leave"] + '</td>';
		html += '<td>' + entryInfo["desc2"] + '</td>';
		html += '<td>' + entryInfo["nowork"] + '</td>';
		html += '<td>' + entryInfo["desc3"] + '</td>';
		html += '<td>' + entryInfo["catchnum"] + '</td>';
		var sjia = entryInfo["sjia"];
		sjia = sjia.replace(/\|/g,"@").replace(/@/g,"<br/>");
		html += '<td>' + sjia + '</td>';
		var sjiadesc = entryInfo["sjiadesc"];
		sjiadesc = sjiadesc.replace(/@/g,"<br/>");
		html += '<td>' + sjiadesc + '</td>';
		var bjia = entryInfo["bjia"];
		bjia = bjia.replace(/@/g,"<br/>").replace(/\|/g,"<br/>");
		html += '<td>' + bjia + '</td>';
		var bjiadesc = entryInfo["bjiadesc"];
		bjiadesc = bjiadesc.replace(/@/g,"<br/>");
		html += '<td>' + bjiadesc + '</td>';
		var njia = entryInfo["njia"];
		njia = njia.replace(/@/g,"<br/>").replace(/\|/g,"<br/>");
		html += '<td>' + njia + '</td>';
		var njiadesc = entryInfo["njiadesc"];
		njiadesc = njiadesc.replace(/@/g,"<br/>");
		html += '<td>' + njiadesc + '</td>';
		var otherjia = entryInfo["otherjia"];
		otherjia = otherjia.replace(/@/g,"<br/>").replace(/\|/g,"<br/>");
		html += '<td>' + otherjia + '</td>';
		var otherdesc = entryInfo["otherdesc"];
		otherdesc = otherdesc.replace(/@/g,"<br/>");
		html += '<td>' + otherdesc + '</td>';
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}
	
	function toExport(){
		var resultvalue = OpenModal('/pages/attendance/exportData.jsp?type=0','500,300,page.export.title,contactInfo');
		/*var username = document.getElementById("username").value.trim();
		var deptname = document.getElementById("deptname").value.trim();
		var groupname = document.getElementById("groupname").value.trim();
	    var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!exportAttendanceData.action";
		actionUrl += "?username="+username;
        actionUrl += "&deptname="+deptname;
        actionUrl += "&groupname="+groupname;
		actionUrl=encodeURI(actionUrl);
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");
		*/
		window.close();
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
	
	function countData(){
		var resultvalue = OpenModal('/pages/attendance/generationData.jsp?type=0','500,300,page.export.title,contactInfo');
		/*var actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!countLeaveToAttendance.action";
		actionUrl=encodeURI(actionUrl);
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");
		*/
		query();
	}
</script>
 <body id="bodyid" style="margin: 0" onload="init();">
 	<s:form name="workhourForm"  namespace="/pages/attendance" action="attendanceAction!querySummary.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <input name="levelHidden" type="hidden" id="levelHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel() %>'/>
 	<table width="100%" cellSpacing="0" cellPadding="0" > 
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
			<td width="5%" class="oracolumncenterheader"><div align="center">姓名</div></td>
			<td width="4%" class="oracolumncenterheader"><div align="center">出勤(天)</div></td>
			<td width="4%" class="oracolumncenterheader"><div align="center">早上迟到(分)</div></td>
			<td width="4%" class="oracolumncenterheader"><div align="center">中午迟到(分)</div></td>
			<td width="8%" class="oracolumncenterheader"><div align="center">说明(打卡时间)</div></td>
			<td width="4%" class="oracolumncenterheader"><div align="center">早退(小时)</div></td>
            <td width="8%" class="oracolumncenterheader"><div align="center">说明(打卡时间)</div></td>
			<td width="3%" class="oracolumncenterheader"><div align="center">缺勤(天)</div></td>
			<td width="8%" class="oracolumncenterheader"><div align="center">说明(打卡时间)</div></td>
            <td width="3%" class="oracolumncenterheader"><div align="center">出勤异常次数</div></td>
            <td width="8%" class="oracolumncenterheader"><div align="center">事假(天)</div></td>
            <td width="4%" class="oracolumncenterheader"><div align="center">说明</div></td>
            <td width="8%" class="oracolumncenterheader"><div align="center">病假(天)</div></td>
            <td width="4%" class="oracolumncenterheader"><div align="center">说明</div></td>
            <td width="8%" class="oracolumncenterheader"><div align="center">年假(天)</div></td>
            <td width="4%" class="oracolumncenterheader"><div align="center">说明</div></td>
            <td width="8%" class="oracolumncenterheader"><div align="center">其它假(天)</div></td>
            <td width="4%" class="oracolumncenterheader"><div align="center">说明</div></td>
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

