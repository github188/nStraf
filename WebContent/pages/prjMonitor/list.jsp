<!-- 项目成本统计页面 -->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet"
	href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css"
	type="text/css" />
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/redirect.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ page isELIgnored="false" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目成本统计</title>
</head>
	<style type="text/css">
.check_area {
	font-size: 12px;
	margin-left: 5%;
}

.select_text {
	margin-left: 8px;
	margin-top: 5px;
}

.td_date {
	margin-left: 5px;
}
</style>

<script type="text/javascript">

	// 查询某段时间内的统计信息
	function query() {
		
		var userDept= $("#queryUserDept").find("option:selected").text();
		var userGroup= $("#queryUserGroup").find("option:selected").text(); 
		if($("#queryUserGroup").find("option:selected").attr("title")){
			userGroup = $("#queryUserGroup").find("option:selected").attr("title");
		}
		var userName= $("#idqueryUserName").find("option:selected").text(); 
		var startDate = $('#startDate').val(); 
		var endDate = $('#raiseEndDate').val();
		var pageNum= document.getElementById("pageNum").value;
		
		var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!calculateList.action?from=refresh&pageNum="+pageNum;

		if(startDate!=''){
			actionUrl+="&startDate="+startDate;
		}
		if(endDate!=''){
			actionUrl+="&endDate="+endDate;
		}
		if(userDept!='---请选择部门---'){
			actionUrl += "&dateDayLog.userDept="+userDept;
		}
		if(userGroup!='---请选择项目名称---'){
			actionUrl +="&dateDayLog.userGroup="+userGroup;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
			actionUrl+="&dateDayLog.userName="+userName;//.substring(0,userName.indexOf('('));
			
		}else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&dateDayLog.userName="+$("#queryUserName").val();
		}
	
		actionUrl = encodeURI(actionUrl);
		 
		var method="setHTML";
		<%int k = 0;%>
		//alert('sendAjaxRequest');
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		 
		var html = "";
		html += '<tr class="trClass'+<%=k%2%>+'" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '';

		html +='<td nowrap align="center">'+entryInfo['cuserid'] +'</td>';	

		html +='<td nowrap align="center">'+entryInfo['cusername'] +'</td>';
		html +='<td nowrap align="center">'+entryInfo['cdetname'] +'</td>';
		html +='<td nowrap align="left">'+entryInfo['cgroupname'] +'</td>';
		html +='<td nowrap align="right">'+entryInfo['cprjdays'] +'</td>';
		
		html +='<td nowrap align="right">'+entryInfo['totaldays'] +'</td>';
		html +='<td nowrap align="right">'+entryInfo['leavedays'] +'</td>';
		if (entryInfo['cstatus'] == "study") {
			html +='<td nowrap align="center">是</td>';
		}else{
			html +='<td nowrap align="center"></td>';

		}
		html+='</tr>'
		<%k++;%>;
		return html;
	}

	function toExport(){
		console.log("aaa");
	    
		var userDept= $("#queryUserDept").find("option:selected").text();
		var userGroup= $("#queryUserGroup").find("option:selected").text(); 
		if($("#queryUserGroup").find("option:selected").attr("title")){
			userGroup = $("#queryUserGroup").find("option:selected").attr("title");
		}
		var userName= $("#idqueryUserName").find("option:selected").text(); 
		var startDate = $('#startDate').val(); 
		var endDate = $('#raiseEndDate').val();
		  
		 var actionUrl = "";
			actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!exportList.action?from=refresh";	
		
		if(startDate!=''){
				actionUrl+="&startDate="+startDate;
		}
		if(endDate!=''){
				actionUrl+="&endDate="+endDate;
		}
		if(userDept!='---请选择部门---'){
				actionUrl += "&dateDayLog.userDept="+userDept;
		}
		if(userGroup!='---请选择项目名称---'){
				actionUrl +="&dateDayLog.userGroup="+userGroup;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
				actionUrl+="&dateDayLog.userName="+userName;//.substring(0,userName.indexOf('('));
				
		}else if($("#idqueryUserName").is(":visible")==false){
				actionUrl += "&dateDayLog.userName="+$("#queryUserName").val();
		}
		
		 actionUrl=encodeURI(actionUrl); 
	 	 window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		 window.close();
		
	}





</script>
	
<body leftmargin="0" topmargin="0">
<s:form name="calcInfoForm" namespace="/pages/logInfo"
		action="logInfo!calculatePrjInfo.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" /> 
  		
		<%@include file="/inc/navigationBar.inc"%>
			<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<tm:deptSelect deptId="queryUserDept"
								deptName="dateDayLog.userDept" groupId="queryUserGroup"
								groupName="dateDayLog.userGroup" userId="queryUserName"
								userName="dateDayLog.userName" isloadName="false"
								deptHeadKey="---请选择部门---" deptHeadValue="全选"
								userHeadKey="----请选择人员----" userHeadValue="全选"
								groupHeadKey="---请选择项目名称---" groupHeadValue="全选"
								labelDept="部门 :" labelGroup="项目名称:" labelUser="姓名 :"
								deptLabelClass="align:right; width:11%;class:input_label;"
								deptClass="align:left;width:30%;"
								groupLabelClass="align:right; width:11%;class:input_label;"
								groupClass="align:left;width:30%;"
								userLabelClass="align:right; width:11%;class:input_label;"
								userClass="align:left;width:30%;">
							</tm:deptSelect>
						</tr>
							
						<tr>
								<td width="8%" align="center" class="input_label">开始日期:</td>
								<td width="20%" align="left"><input name="startDate" size="20"
									type="text" id="startDate"  class="MyInput" />
								</td>
	
								<td width="8%" align="center" class="input_label">结束日期:</td>
								<td width="20%" align="left"><input name="raiseEndDate" size="20"
									type="text" id="raiseEndDate" class="MyInput" />
								<td  colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <tm:button
										site="1"></tm:button></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2" ></tm:button>
				</td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<!-- <td nowrap class="oracolumncenterheader" width="2%"></td> -->
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">编号</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">姓名</div></td>
				<td nowrap width="15%" class="oracolumncenterheader"><div
						align="center">部门</div></td>
				<td nowrap class="oracolumncenterheader" style="word-wrap: break-word; word-break: break-all;"><div
						align="center">项目名称</div></td>
				<!-- <td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">开始时间</div></td>
				<td nowrap class="oracolumncenterheader"><div
						align="center">结束时间</div></td> -->
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">项目人日(天)</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">投入人日(天)</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div
						align="center">请假人日</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div
						align="center">实习生标识</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="censusPrjInfos" status="row" var="prj">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					
					
					<td nowrap align="center"><s:property value="cuserid"/></td>
					
					<td nowrap align="center"><s:property value="cusername"/></td>
					<td nowrap align="center"><s:property value="cdetname"/></td>
					<td nowrap align="left"><s:property value="cgroupname"/></td>
					<td nowrap align="right"><s:property value="cprjdays"/></td>
					<td nowrap align="right"><s:property value="totaldays"/></td>
					<td nowrap align="center"><font color='#ff0000'></font><s:property value="leavedays"/></font></td>
					<s:if test="cstatus == 'study'">
						<td nowrap align="center">是</td>
					</s:if>
					<s:else>
						<td nowrap align="center"></td>
					</s:else>
					
					<%-- <td nowrap align="center"><s:property value="cstatus"/> </td> --%>
					</tr>
				
				</s:iterator>
			</tbody>
		</table>
		<!-- 分页按钮组 -->
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage"
										formName="certificationInfoForm" />
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
		
	</s:form>

</body>
</html>