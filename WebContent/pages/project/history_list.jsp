<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript"> 


function query()
{
	 var pageNum = document.getElementById("pageNum").value;
	var deptcode =  document.getElementById("queryDept").value;
	var projectId =  document.getElementById("queryGroup").value;
	var userid =  document.getElementById("queryUserName").value;
	var entryTime =  document.getElementById("entryTime").value;
	var exitTime =  document.getElementById("exitTime").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/project/project!historyrefresh.action?form=refresh&pageNum="+pageNum+"&deptcode="+deptcode+"&projectId="+projectId+"&userid="+userid+"&entryTime="+entryTime+"&exitTime="+exitTime;
	actionUrl = encodeURI(actionUrl,"UTF-8");
	var method="setHTML";
	<%int k = 0 ; %>
	<%int j = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true); 
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
        html+=' <tr id="tr<%=++k%>" class="trClass<%=j%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
        html+='<td align="center" width="25%">'+entryInfo['username']+'</td>';
        html+='<td align="center" width="10%">'+entryInfo['projectname'].substring(0,10)+'</td>';
        html+='<td align="center" width="10%">'+entryInfo['entryTime'].substring(0,10)+'</td>';
        html+='<td align="center" width="10%">'+entryInfo['exitTime'].substring(0,10)+'</td>';
        html+='</tr>';
        <%j++;%>//每调用一次该方法，索引值加1
	return html;
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="historyForm" action="project!historylist.action" namespace="/pages/project" method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />

		<%@include file="/inc/navigationBar.inc"%>
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<tm:deptSelect deptId="queryDept" deptName="deptcode"
								groupId="queryGroup" groupName="projectId" userId="queryUserName"
								userName="userid" isloadName="false" deptHeadKey="---请选择部门---"
								deptHeadValue="" userHeadKey="----请选择人员----" userHeadValue=""
								groupHeadKey="---请选择项名称---" groupHeadValue="" labelDept="部门 :"
								labelGroup="项目名称:" labelUser="姓名 :"
								deptLabelClass="align:right; width:5%;class:input_label"
								deptClass="align:left;width:12%;"
								groupLabelClass="align:right; width:5%;class:input_label"
								groupClass="align:left;width:12%;"
								userLabelClass="align:right; width:5%;class:input_label"
								userClass="align:left;width:15%;">
							</tm:deptSelect>
							<td align="right"><tm:button site="1" /></td>
						</tr>
						<tr>
							<td width="10%" align="right" class="input_label">进入时间 <s:text name="label.colon" /></td>
							<td width="20%"><input name="entryTime" type="text"
								id="entryTime" size="12" maxlength="12" class="MyInput"
								value='<s:date name="startTime" format="yyyy-MM-dd"/>'>
							</td>
							<td width="10%" align="right" class="input_label">退出时间<s:text name="label.colon" /></td>
							<td width="20%"><input name="exitTime" type="text"
								id="exitTime" size="12" maxlength="12" class="MyInput"
								value='<s:date name="endTime" format="yyyy-MM-dd"/>'></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td width="25%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">姓名</div>
				</td>
				<td width="10%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">项目</div>
				</td>
				<td width="10%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">进入时间</div>
				</td>
				<td width="10%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">退出时间</div>
				</td>
			</tr>
			<tbody id="formlist">
				<%
					int i = 1;
						int index = 0;
				%>

				<s:iterator id="grp" value="#request.history" status="status">
					<tr id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						<td align="center" width="25%"><s:property value="username" />
						</td>
						<td align="center" width="10%"><s:property
								value="projectname" /></td>
						<td align="center" width="10%"><s:date name="entryTime"
								format="yyyy-MM-dd" /></td>
						<td align="center" width="10%"><s:date name="exitTime"
								format="yyyy-MM-dd" /></td>
					</tr>
					<%
						index++;
					%>
				</s:iterator>
			</tbody>
		</table>
		<table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1"
			cellspacing="1" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="historyForm" />
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