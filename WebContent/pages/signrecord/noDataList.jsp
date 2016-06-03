<%@page import="cn.grgbanking.feeltm.domain.SysOperLog"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date currentDate = new java.util.Date();
	String str = sf.format(currentDate);
	String year = str.split("-")[0];
	String month = str.split("-")[1];
%>

<html>
<head>
<title>未导入考勤数据的日期</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
	function query() {
		var pageNum = document.getElementById("pageNum").value;
		var year =  document.getElementById("year").value;
		var month =  document.getElementById("month").value;
		var prjname =  document.getElementById("prjname").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!noExportExcelDataPage.action?form=refresh&pageNum="+pageNum+
				"&year="+year+"&month="+month+"&prjname="+prjname;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		html += '<tr class="trClass<%=k%2 %> " oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td align="center" width="10%">' + entryInfo["prjname"] + '</td>';
		html += '<td align="center" width="20%">' + entryInfo["workdate"].substring(0, 11) + '</td>';
		html += '</tr>';
		<%k++;%>
		return html;
	}

	//获取所选复选框
	function GetSelIds() {
		var idList = "";
		var em = document.all.tags("input");
		for (var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox") {
				if (em[i].checked) {
					idList += "," + em[i].value.split(",")[0];
				}
			}
		}

		if (idList == "")
			return "";
		return idList.substring(1);
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="signbindForm" action="signRecord!noExportExcelDataPage.action" namespace="/pages/signrecord" method="post">
		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td>查看日期&nbsp;&nbsp;
								<select id="year" name="year">
									<%for(int i=10;i>=0;i--){ 
										int curYear = Integer.parseInt(year);
										int valYear = curYear-i;
										String selectOption = "";
										if(valYear==curYear){
											selectOption="selected";
										}
									%>
										<option value="<%=valYear %>" <%=selectOption %>><%=valYear %></option>
									<%} %>
									<%for(int j=1;j<=10;j++){ 
										int curYear = Integer.parseInt(year);
										int valYear = curYear+j;
										String selectOption = "";
										if(valYear==curYear){
											selectOption="selected";
										}
									%>
										<option value="<%=valYear %>" <%=selectOption %>><%=valYear %></option>
									<%} %>
								</select>年
								<select id="month" name="month">
									<option value="">全部</option>
									<%for(int t=1;t<=12;t++){ int curMonth = Integer.parseInt(month);%>
										<option value="<%=t %>" <%if(t==curMonth){ %>select="selected"<%} %>><%=t %></option>
									<%} %>
								</select>月
							</td>
							<td>项目组名称</td>
							<td>
								<select name="prjname" id="prjname" style="width: 100px">
										<option value="">全部</option>
										<s:iterator value="#request.attendancePrjname" id="attendancePrjname">
											<option value="<s:property value='value'/>"><s:property
													value="value" /></option>
										</s:iterator>
								</select>
							</td>
							<td width="15%" align="right"><tm:button site="1" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>

		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>

		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr> 
			  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">未导入日期</div></td>
		  	</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="noDataList" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td align="center" width="10%"><s:property value="prjname" /></td>
					<td align="center" width="10%"><s:date name="workdate" format="yyyy-MM-dd" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="signbindForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>