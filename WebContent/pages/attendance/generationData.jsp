<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String type = request.getParameter("type");
%>
<html>
<head>
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript">

function closeModal(){
		if(!confirm('您确认关闭此页面吗？')){
		return;				
	}
 	window.close();
}
function toExport(){
	var type = "<%=type%>";
	var signTime = document.getElementById("signTime").value;
	var signEndTime = document.getElementById("signEndTime").value;
	if(signTime==""){
		alert("请选择开始日期");
		return;
	}
	if(signEndTime==""){
		alert("请选择结束日期");
		return;
	}
	var actionUrl = "";
	if(type=="0"){//导出广发数据
		actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!countLeaveToAttendance.action?startdate="+signTime;
		actionUrl += "&enddate="+signEndTime;
	}else{
		actionUrl = "<%=request.getContextPath()%>/pages/attendance/attendanceAction!countAllAttendanceData.action?startdate="+signTime;
		actionUrl += "&enddate="+signEndTime;
	}
	actionUrl=encodeURI(actionUrl);
	window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
	//window.location.href=strReturn;
	window.close();
	
}
</script>
<body>
	<s:form action="/pages/signrecord/signRecord!exportData.action" method="post" enctype="multipart/form-data">
	<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">导出考勤数据 </td>
						</tr>
              			<tr>
              			 	<td width="14%" align="right">开始日期:</td> 
                			<td width="14%" >
                				<input name="signTime" type="text" id="signTime"  
                				class="MyInput"  />
                			</td>
                			<td width="14%" >结束日期:</td>
                			<td>
                				<input name="signEndTime" type="text" id="signEndTime"  
                				class="MyInput" />
                			</td>
              			</tr>
					</table>
      <br>
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr height=50><td colspan=4 ></td></tr>
	    <tr height=2><td colspan=4 ></td></tr>
	    <tr height=10><td colspan=4 ></td></tr>
        <tr>
          <td align="center" colspan=4>
          	  <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="toExport();" image="../../images/share/yes1.gif"> 
       		 <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
       </td>
        </tr>
      </table>
    </s:form>
</body>
</html>