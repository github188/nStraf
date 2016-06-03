<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<html>
<head><title>DayLog query</title></head>
	<script type="text/javascript">
		function query(){
			
			var userDept= $("#queryUserDept").find("option:selected").text();
			var userGroup= $("#queryUserGroup").find("option:selected").text(); 
			
			if($("#queryUserGroup").find("option:selected").attr("title")){
				userGroup = $("#queryUserGroup").find("option:selected").attr("title");
			}
			
			var userName= $("#idqueryUserName").find("option:selected").text(); 
			var queryStartTime=  document.getElementById("queryStartTime").value;
			var queryEndTime=  document.getElementById("queryEndTime").value;
			var pageNum= document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/weeklog/logInfo!query.action?from=refresh&pageNum="+pageNum;
			if(userDept!='---请选择部门---'){
				actionUrl += "&personWeekLog.detName="+userDept;
			}
			if(userGroup!='---请选择项目名称---'){
				actionUrl +="&personWeekLog.groupName="+userGroup;
			}
			if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
				actionUrl+="&personWeekLog.userName="+userName;//.substring(0,userName.indexOf('('));
	        }else if($("#idqueryUserName").is(":visible")==false){
				actionUrl += "&personWeekLog.userName="+$("#queryUserName").val();
			}
			
			if(queryStartTime!=''){
				actionUrl+="&queryStartTime="+queryStartTime;
			}
			if(queryEndTime!=''){
				actionUrl+="&queryEndTime="+queryEndTime;
			}
			//调试测试连接
			actionUrl = "/nStraf/pages/weeklog/logInfo!query.action?from=refresh&pageNum=1&personWeekLog.userName=";
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
	function setHTML(entry,entryInfo){
		var html = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		var groupInfo=entryInfo["groupName"];
		/* if(groupInfo.length>25){
			var shortShow=groupInfo.substring(0,25)+'...';
			shortShow='<a style="font-weight:bold;" title="'+groupInfo+'">'+shortShow+'</a>';
			groupInfo=shortShow;
		} */
		
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td width="2%">';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td width="15%"><a href='+str+'><font style="color: #3366FF">' +entryInfo["startDate"].substring(0,10) + '</font></a></td>';
		html += '<td width="12%"><a href='+str+'><font style="color: #3366FF">' +entryInfo["endDate"].substring(0,10) + '</font></a></td>';
		html += '<td width="12%" align="left">';
		html += entryInfo["userName"];
		html += '</td>';
		html += '<td width="12%" align="left">' +entryInfo["detName"] + '</td>';
		html += '<td width="32%" align="left">' +groupInfo+ '</td>';
		html += '<td widht="15%"><a href='+str+'>' + entryInfo["updateTime"].substring(0,10) + '</a></td>';
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}
	
	function  GetSelIds(){
		var idList="";
		var  em=  document.getElementsByTagName("input");
		for(var  i=0;i<em.length;i++){
		  if(em[i].type=="checkbox"){
		      if(em[i].checked){
		        idList+=","+em[i].value.split(",")[0];
		  		}
		  } 
	 	} 
		if(idList=="") 
		   return "";
		return idList.substring(1);
	}
	
	function  SelAll(chkAll){
		var  em= document.getElementsByTagName("input");
		for(var  i=0;i<em.length;i++){
			if(em[i].type=="checkbox")
		    	em[i].checked=chkAll.checked
		}
	}
	
	function del() {
		var aa=document.getElementsByName("chkList");
		var itemId;
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
		}

		if (j==0)
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次删除一条记录');
		else{
			if(confirm('您确认删除该记录吗？')) {
		  		var strUrl="/pages/weeklog/logInfo!delete.action?ids="+itemId;
				var resultvalue = OpenModal(strUrl,"600,380,operInfo.delete,um");
				document.getElementById("queryStartTime").value = "";
				document.getElementById("queryEndTime").value = "";
				refreshList();
			}
		}
	}

	function modify(){
		var aa=document.getElementsByName("chkList");
		var itemId;
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
		}
		if (j==0)
		 	alert('<s:text name="operator.update" />')
		 else if (j>1)
		 	alert('<s:text name="operator.updateone" />')
		else{
		  	var strUrl="/pages/weeklog/logInfo!edit.action?ids="+itemId;
		  	var features="900,650,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
			//location="/nStraf/pages/daylog/logInfo!edit.action?ids="+itemId;
		}
	}
		
	function add(){
		var resultvalue = OpenModal('/pages/weeklog/logInfo!add.action','900,650,tmlInfo.addTmlTitle,tmlInfo');
		document.getElementById("queryStartTime").value = "";
		document.getElementById("queryEndTime").value = "";
		refreshList();

		//location='/nStraf/pages/daylog/logInfo!add.action';
	}
		
				
	function show(actionto) {
		/* var strUrl="/pages/weeklog/logInfo!show.action?ids="+id;
		var features="900,650,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features); */
		 self.location="<%=request.getContextPath()%>"+actionto;
	}
	
	
	$(function(){
		//query();
	});
	function resetInfo(){
		$("#queryUserDept").val('全选');
		$("#queryUserGroup").val('全选');
		$("#queryStartTime").val('');
		$("#queryEndTime").val('');
		var userName = $("#idqueryUserName").find("option:selected").text();
		if($("#idqueryUserName").is(":visible")==true){
			$("#idqueryUserName").val('全选');
		}else if($("#idqueryUserName").is(":visible")==false){
			$("#queryUserName").val('');
		}
	}
</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="dayInfoForm"  namespace="/pages/dayinfo" action="dayInfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%"   cellSpacing="0" cellPadding="0"> 
 		<tr>
	 		<td width="60%">
				<table width="100%" class="select_area">
					<tbody>
						<tr>
							<td align="right" width="5%" class="input_label">培训名称:</td>
							<td align="left" width="12%"><input style="width:124px;height:22px" id="queryUserName" name="queryUserName"></td>
							<td align="right" width="5%" class="input_label">培训讲师:</td>
							<td align="left" width="12%"><input style="width:124px;height:22px" id="queryUserName" name="queryUserName"></td>
							<td align="right" width="5%" class="input_label">报名设置:</td>
							<td align="left" width="15%">
							<select id="queryUserGroup" name="dateDayLog.userGroup">
							<option value="待设置">待设置</option>
							</select>
							</td>
								
						</tr>
					</tbody>
				</table>  
			</td> 
				<td class="input_label">培训时间：</td>
						<td  nowrap="nowrap" width="12%" align="left"><input name="queryStartTime" id="queryStartTime" type="text"  size="11"  class="MyInput" />
						至
						<input name="queryEndTime" id="queryEndTime" type="text"  size="11" class="MyInput" /></td>
		                <td align="right"> 
		                	<input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">
		                	<tm:button site="1"/>
		                </td>
		</tr> 
	</table><br />
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			 <td width="25"  height="23" valign="middle"></td>
			 <td class="orarowhead"><s:text name="operInfo.title" /></td>
			 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="13%" class="oracolumncenterheader"><div align="center">培训名称 </div></td>
			<td nowrap width="13%" class="oracolumncenterheader"><div align="center">报名设置 </div></td>
			<td nowrap width="13%" class="oracolumncenterheader"><div align="center">发起报名</div></td>
			<td nowrap width="20%" class="oracolumncenterheader"><div align="center">培训时间</div></td>
			<td nowrap width="13%" class="oracolumncenterheader"><div align="center">讲师</div></td>
			<td nowrap width="13%" class="oracolumncenterheader"><div align="center">学员名单</div></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">操作</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
		   <tr class="trClass0" oriclass="" onmouseout="TrMove(this)" onmouseover="TrMove(this)" align="center">
				<td width="13%"><a href='javascript:show("#")'><font style="color: #3366FF">贸易</font></a></td>
				<td width="13%"><a href='javascript:show("/pagesTraining/enroll/enroll!enrollRequirement.action")'><font style="color: #3366FF">待设置</font></a></td>
				<td width="13%"><a href='javascript:show("#")'><font style="color: #3366FF">待发放</font></a></td>
				<td width="20%" align="center">2014-12-31(周三)</td>
				<td width="13%" align="center">汪腾蛟</td>
				<td width="13%" ><a href='javascript:show("#")'><font style="color: #3366FF">已有9人</font></a></td>
				<td widht="15%">
				<table>
					<tr>
					<td widht="33.33%" >
					<a href='javascript:show("#")'><font style="color: #3366FF">通知</font>
					<td>
					<td widht="33.33%">
					<a href='javascript:show("#")'><font style="color: #3366FF">签到</font>
					<td>
					<td widht="33.33%">
					<a href='javascript:show("#")'><font style="color: #3366FF">评估</font>
					<td>
					<tr>
				</table>
				</td>
			</tr>
 		</tbody> 
 	</table> 
 	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
		<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
					<td width="83%" align="right">
					<div id="pagetag"> <%-- <tm:pagetag pageName="currPage" formName="certificationInfoForm" /> --%> </div>
				</td>
			</tr>
			</table>
		</td>
		</tr>
	</table>
 </s:form>
</body>
</html>