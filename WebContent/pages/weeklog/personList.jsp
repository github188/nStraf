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
		
				
	function show(id) {
		var strUrl="/pages/weeklog/logInfo!show.action?ids="+id;
		var features="900,650,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features);
	}
	
	
	$(function(){
		$("#queryUserDept").find("option").each(function(){
			if('<%=((String)request.getAttribute("queryDeptName")).trim()%>'==$(this).text()){
				$(this).attr('selected','selected');
			}
		});
		$("#queryUserGroup").find("option").each(function(){
			if('<%=((String)request.getAttribute("queryGroupName")).trim()%>'==$(this).text()){
				$(this).attr('selected','selected');
			}
		});
		$("#queryUserName").val('<%=((String) request.getAttribute("queryUserName")).trim()%>');
		
		$("#grpCode").attr("style","width:100%");
	});
	
	function toQueryExport(){ 
		var userDept= $("#queryUserDept").find("option:selected").text();
		var userGroup= $("#queryUserGroup").find("option:selected").text(); 
		
		if($("#queryUserGroup").find("option:selected").attr("title")){
			userGroup = $("#queryUserGroup").find("option:selected").attr("title");
		}
		
		var userName= $("#idqueryUserName").find("option:selected").text(); 
		var queryStartTime=  document.getElementById("queryStartTime").value;
		var queryEndTime=  document.getElementById("queryEndTime").value;
		var pageNum= document.getElementById("pageNum").value;
		
		var actionUrl = "<%=request.getContextPath()%>/pages/weeklog/logInfo!exportData.action?from=refresh&pageNum="+pageNum;
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
		actionUrl=encodeURI(actionUrl);
		//actionUrl = encodeURI(actionUrl,"UTF-8");
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		window.close();
	}
	
	function toExport(){ 
		if(window.navigator.userAgent.indexOf("Chrome") != -1){
			var strUrl="<%=request.getContextPath() %>/pages/weeklog/logInfo!toExport.action";
			var iHeight = "300";
			var iWidth = "400";
			var iTop = (window.screen.availHeight - 20 - iHeight) / 2;
			var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
			var winOption = 'height=300px,width=400px,top='+iTop+'px,left='+iLeft+'px,toolbar=no,menubar=no,scrollbars=no, resizable=yes,center=yes,location=no, status=no';
			window.open(strUrl,window, winOption);
		}else{
			var strUrl="/pages/weeklog/logInfo!toExport.action";
			var features="400,300,transmgr.traninfo,watch";
			OpenModal(strUrl,features);
		}
	}
	
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
					<tr>
						<tm:deptSelect 
							deptId="queryUserDept" 
							groupId="queryUserGroup"
							userId="queryUserName" 
							isloadName="false" 
							deptHeadKey="---请选择部门---" 
							deptHeadValue="全选" 
							userHeadKey="----请选择人员----" 
							userHeadValue="全选"  
							groupHeadKey="---请选择项目名称---"
							groupHeadValue="全选"
							labelDept="部门 :" 
							labelGroup="项目名称:" 
							labelUser="姓名 :" 
							deptLabelClass="align:right; width:5%;class:input_label;"
							deptClass="align:left;width:12%;" 
							groupLabelClass="align:right; width:5%;class:input_label;"
							groupClass="align:left;width:12%;" 
							userLabelClass="align:right; width:5%;class:input_label;"
							userClass="align:left;width:15%;" 
							>
						</tm:deptSelect>
						</tr>
					
					</tr>
				</table>  
			</td> 
				<td class="input_label">周报时间：</td>
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
			<td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">起始时间 </div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">截至时间</div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">用户姓名</div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">所属部门</div></td>
			<td nowrap width="32%" class="oracolumncenterheader"><div align="center">所在项目名称</div></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">更新时间</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="personWeekLogList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td nowrap width="15%">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'>
						<font style="color: #3366FF"><s:date name="startDate" format="yyyy-MM-dd" /></font>
			  		</a>
			  	</div>
		  	</td>
		  	<td nowrap width="15%">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'>
						<font style="color: #3366FF"><s:date name="endDate" format="yyyy-MM-dd" /></font>
			  		</a>
			  	</div>
		  	</td>
		 	<td nowrap width="12%"><s:property value="userName"/></td>
		 	<td nowrap width="12%"><s:property value="detName"/></td>
		    <td nowrap width="29%" name="userGroup"><s:property value="groupName"/></td>
		    <td nowrap width="15%">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'>
						<s:date name="updateTime" format="yyyy-MM-dd" />
			  		</a>
			  	</div>
		  	</td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
		<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
					<td width="83%" align="right">
					<div id="pagetag"><tm:pagetag pageName="currPage" formName="certificationInfoForm" /></div>
				</td>
			</tr>
			</table>
		</td>
		</tr>
	</table>
 </s:form>
</body>
</html>

