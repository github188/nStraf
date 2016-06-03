<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>

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
//			var queryProjectName=$("#queryProjectName").val();
			var pageNum= document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/projectplan/projectplanInfo!query.action?from=refresh&pageNum="+pageNum;
			
			if(userGroup.indexOf('--')!=0){
				actionUrl +="&plan.groupName="+userGroup;
			}
			if(userName.indexOf('--')!=0){
				actionUrl+="&plan.projectManager="+userName;//.substring(0,userName.indexOf('('));
			}
//			if(queryProjectName.indexOf('--')!=0){
//				actionUrl+="&queryProjectName="+queryProjectName;
//			}
			var planStartTime1=$("#planStartTime1").val();
			var planStartTime2=$("#planStartTime2").val();
			var planEndTime1=$("#planEndTime1").val();
			var planEndTime2=$("#planEndTime2").val();
			if(planStartTime1!=''){
				actionUrl+="&planStartTime1="+planStartTime1;
			}
			if(planStartTime2!=''){
				actionUrl+="&planStartTime2="+planStartTime2;
			}
			if(planEndTime1!=''){
				actionUrl+="&planEndTime1="+planEndTime1;
			}
			if(planEndTime2!=''){
				actionUrl+="&planEndTime2="+planEndTime2;
			}
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
	function setHTML(entry,entryInfo){
		var html = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td width="24%" align="left"><a href='+str+'><font style="color: #3366FF">' +entryInfo["groupName"] + '</font></a></td>';
//		html += '<td width="18%" align="left">' +entryInfo["groupName"]+ '</td>';
		html += '<td width="10%" align="left">'+entryInfo["projectManager"]+'</td>';
		html += '<td width="6%" align="left">' +entryInfo["fare"] + '</td>';
		html += '<td width="10%" align="left">' +entryInfo["planStartDate"]+ '</td>';
		html += '<td width="10%" align="left">' +entryInfo["factStartDate"]+ '</td>';
		html += '<td width="10%" align="left">' +entryInfo["planEndDate"]+ '</td>';
		html += '<td width="10%" align="left">' +entryInfo["factEndDate"]+ '</td>';
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}
	
	function  GetSelIds(){
		var idList="";
		var  em= document.getElementsByTagName("input");
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
		  		var strUrl="/pages/projectplan/projectplanInfo!delete.action?ids="+itemId;
				var resultvalue = OpenModal(strUrl,"600,380,operInfo.delete,um");
		        query();
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
			var strUrl="/pages/projectplan/projectplanInfo!edit.action?ids="+itemId;
			var features="1280,725,tmlInfo.updateTitle,tmlInfo";
			OpenModal(strUrl,features);
			query();
			//location="/nStraf/pages/projectplan/projectplanInfo!edit.action?ids="+itemId;
		}
	}
		
	function add(){
	     var resultvalue = OpenModal('/pages/projectplan/projectplanInfo!add.action','1280,725,tmlInfo.addTmlTitle,tmlInfo');
	    query();  
		//location='/nStraf/pages/projectplan/projectplanInfo!add.action';
	}
		
				
	function show(id) {
		var strUrl="/pages/projectplan/projectplanInfo!show.action?ids="+id;
		var features="1280,725,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features);
		//location="/nStraf"+strUrl;
	}
	
	
	$(function(){
		//监听基本信息中项目名称
	//	$("#prjName").change(function(){
		//	$("#queryProjectName").val($("#prjName").find("option:selected").text());
		//});
		//隐藏部门查询
		$("#queryUserDept").parent().prev().hide();
		$("#queryUserDept").parent().hide();
	});
	
</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="dayInfoForm"  namespace="/pages/dayinfo" action="dayInfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%"   cellSpacing="0" cellPadding="0"> 
 		<tr>
	 		<td >
				<table width="100%" class="select_area">
					<tr>
						<tm:deptSelect 
							deptId="queryUserDept" 
							deptName="dateDayLog.userDept"
							groupId="queryUserGroup"
							groupName="dateDayLog.userGroup"
							userId="queryUserName" 
							userName="dateDayLog.userName"
							isloadName="false" 
							deptHeadKey="---请选择部门---" 
							deptHeadValue="全选" 
							userHeadKey="--------请选择人员--------" 
							userHeadValue="全选"  
							groupHeadKey="---请选择项目名称---"
							groupHeadValue="全选"
							labelDept="所属部门 :" 
							labelGroup="所在项目名称:" 
							labelUser="项目经理 :" 
							deptLabelClass="align:right; width:0%;class:input_label;"
							deptClass="align:left;width:0%;" 
							groupLabelClass="align:right; width:8%;class:input_label;width:115px;"
							groupClass="align:left;width:26%;" 
							userLabelClass="align:right; width:8%;class:input_label;width:115px"
							userClass="align:left;width:26%;" 
							>
						</tm:deptSelect>
				<!-- 	  	<td width="8%" height="27" align="right" class="input_label">项目名称:</td>
						<td align="left" >
							<tm:tmSelect id="prjName" name='prjName' selType="dataDir" path="systemConfig.projectname"  style="width:220px" />
							<input type="hidden" id="queryProjectName" value="">
						</td> -->
					</tr>
					<tr>
						<td width="8%" height="27" align="right" class="input_label">计划开始:</td>
						<td width="26%" align="left">
							<table width="100%"   cellSpacing="0" cellPadding="0" >
								<tr>
									<td><input name="" id="planStartTime1" type="text" id="start" size="14"  class="MyInput" /></td>
									<td>至 </td>
									<td><input name="" id="planStartTime2" type="text" id="end"  size="14" class="MyInput" /></td>
								</tr>
							</table>
						</td>
						<td width="8%" height="27" align="right" class="input_label">计划结束:</td>
						<td width="26%" align="left">
							<table width="100%"   cellSpacing="0" cellPadding="0" >
								<tr>
									<td><input name="" id="planEndTime1" type="text" id="start" size="14"  class="MyInput" /></td>
									<td>至 </td>
									<td><input name="" id="planEndTime2" type="text" id="end"  size="14" class="MyInput" /></td>
								</tr>
							</table>	
						</td>
						<td align="right" colspan="2"> <tm:button site="1"/></td>
					</tr>
				</table>  
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
			<td nowrap width="24%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
	<!--  		<td nowrap width="18%" class="oracolumncenterheader"><div align="center">项目组</div></td>-->
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目经理</div></td>
			<td nowrap width="6%" class="oracolumncenterheader"><div align="center">进度</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">计划开始日期</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">实际开始日期</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">计划结束日期</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">实际结束日期</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="projectPlanList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td nowrap width="24%">
				<div align="left">
					<a href='javascript:show("<s:property value="id"/>")'>
						<font style="color: #3366FF"><s:property value="groupName" /><!--<s:property value="projectName" />--></font>
			  		</a>
			  	</div>
		  	</td>
	<!--  	  	<td nowrap width="18%"><s:property value="groupName" /></td> -->
		  	<td nowrap width="10%"><s:property value="projectManager" /></td>
		 	<td nowrap width="6%"><s:property value="fare"/></td>
		 	<td nowrap width="10%"><s:date name="planStartDate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="10%"><s:date name="factStartDate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="10%"><s:date name="planEndDate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="10%"><s:date name="factEndDate" format="yyyy-MM-dd" /></td>
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

