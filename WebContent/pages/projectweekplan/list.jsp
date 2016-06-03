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
<%String defaultSelectText="------ 请选择 ------";%>
<head><title>DayLog query</title></head>
	<script type="text/javascript"> 
		function query(){
			
			var pageNum= document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/projectweekplan/projectweekplanInfo!query.action?from=refresh&pageNum="+pageNum;
			/* if($("#scheduleStateSelect").find("option:selected").val()!=''){
				actionUrl +="&plan.scheduleState="+$("#scheduleStateSelect").find("option:selected").val();
			} */
			var scheduleState = $("#scheduleStateSelect").find("option:selected").val();
			var projectId = $("#projectSelect").find("option:selected").val();
			var customerKey = $("#customerKeySelect").find("option:selected").val();
			var timePeriod = $("#timePeriodSelect").find("option:selected").val();
			if(typeof(scheduleState) == "undefined"){
			}else{
				actionUrl +="&plan.scheduleState="+ scheduleState;
			}
			if(typeof(projectId) == "undefined"){
			}else{
				actionUrl+="&plan.projectId="+ projectId;
			}
			if(typeof(customerKey) == "undefined"){
			}else{
				actionUrl+="&plan.customerKey="+ customerKey;
			}
			if(typeof(timePeriod) == "undefined"){
			}else{
				actionUrl+="&timePeriod="+ timePeriod;
			}
			/* if($("#customerKeySelect").find("option:selected").val()!=''){
				actionUrl+="&plan.customerKey="+$("#customerKeySelect").find("option:selected").val();
			}
			if($("#timePeriodSelect").find("option:selected").val()!=''){
				actionUrl+="&timePeriod="+$("#timePeriodSelect").find("option:selected").val();
			} */
			var startTime=$("#startTime").val();
			var endTime=$("#endTime").val();
			if(startTime!=''){
				actionUrl+="&startTime="+startTime;
			}
			if(endTime!=''){
				actionUrl+="&endTime="+endTime;
			}
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
	function setHTML(entry,entryInfo){
		var html = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		var style="background-color:";
		if("项目进度正常"==entryInfo["scheduleState"]){
			style+="#9BBB59;";
		}
		if("项目进度延迟"==entryInfo["scheduleState"]){
			style+="#FF6600;";
		}
		if("项目进度超前"==entryInfo["scheduleState"]){
			style+="#00CCFF;";
		}
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td width="12%" align="left"><a href='+str+'><font style="color: #3366FF">' +entryInfo["weekPeriod"] + '</font></a></td>';
		html += '<td width="12%" align="left">'+entryInfo["weekDesc"]+'</td>';
		html += '<td width="14%" align="left" style="'+style+'">' +entryInfo["scheduleState"] + '</td>';
		html += '<td width="10%" align="left">' +entryInfo["standardDuration"]+ '</td>';
		html += '<td width="30%" align="left">' +entryInfo["projectName"]+ '</td>';
		html += '<td width="20%" align="left">' +entryInfo["customerName"]+ '</td>';
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
		var itemId="";
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId+=","+aa[i].value;
				j=j+1;
			}
		}

		if (j==0)
		 	alert('请选择一条记录');
		 /* else if (j>1)
		 	alert('你只能一次删除一条记录'); */
		else{
			if(confirm('您确认删除该记录吗？')) {
		  		var strUrl="/pages/projectweekplan/projectweekplanInfo!delete.action?ids="+itemId;
				var resultvalue = OpenModal(strUrl,"600,380,operInfo.delete,um");
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
			var strUrl="/pages/projectweekplan/projectweekplanInfo!edit.action?ids="+itemId;
			var features="1152,720,tmlInfo.updateTitle,tmlInfo";
			OpenModal(strUrl,features);
			refreshList();
			//location="/nStraf/pages/projectweekplan/projectweekplanInfo!edit.action?ids="+itemId;
		}
	}
		
	function add(){
	    var resultvalue = OpenModal('/pages/projectweekplan/projectweekplanInfo!add.action','1152,720,tmlInfo.addTmlTitle,tmlInfo');
	    refreshList();
		//location='/nStraf/pages/projectweekplan/projectweekplanInfo!add.action';
	}
		
				
	function show(id) {
		var strUrl="/pages/projectweekplan/projectweekplanInfo!show.action?ids="+id;
		var features="1152,720,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features);
		//location="/nStraf"+strUrl;
	}
	
	$(function(){
		$("#projectSelect").find("option").each(function(){
			if($(this).val()=='<%=(String)request.getAttribute("curProject")%>'){
				$(this).attr('selected','selected');
			}else{
				$(this).removeAttr('selected');
			}
		});
	});
	
	function resetInfo(){
		$("#projectSelect").val('');
		$("#customerKeySelect").val('');
		$("#scheduleStateSelect").val('');
		$("#timePeriodSelect").val('');
		$("#startTime").val('');
		$("#endTime").val('');
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
	 		<td >
				<table width="100%" class="select_area">
					<tr>
						<td width="8%" height="27" align="right" class="input_label">整体状态:</td>
						<td width="17%" align="left">
							<select id="scheduleStateSelect" name="plan.scheduleState" style="width:150px">
								<option value=""><%=defaultSelectText%></option>
								<option value="项目进度正常">项目进度正常</option>
								<option value="项目进度超前">项目进度超前</option>
								<option value="项目进度延迟">项目进度延迟</option>
							</select>
						</td>
						<td width="8%" height="27" align="right" class="input_label">项目名称:</td>
						<td width="17%" align="left">
							<select id="projectSelect" name="plan.projectName" style="width:150px">
								<option value=""><%=defaultSelectText%></option>
								<s:iterator  value="#request.projectList" id="ele" status="row">
									<s:if test="name.length()>10">
										<option value='<s:property value="id" />' title='<s:property value="name" />'><s:property value="name.substring(0,8)" />...</option>
									</s:if>
									<s:else>
										<option value='<s:property value="id" />'><s:property value="name" /></option>
									</s:else>
								</s:iterator>
							</select>
						</td>
						<td width="8%" height="27" align="right" class="input_label">客户名称:</td>
						<td width="17%" align="left">
							<select id="customerKeySelect" name="plan.customerKey" style="width:150px">
								<option value=""><%=defaultSelectText%></option>
								<s:iterator  value="#request.customerList" id="ele" status="row">
								<option value='<s:property value="key" />'><s:property value="val" /></option>
								</s:iterator>
							</select>
						</td>
						<td width="8%" height="27" align="right" class="input_label">时间段:</td>
						<td width="17%" align="left">
							<select id="timePeriodSelect">
								<option value=""><%=defaultSelectText%></option>
								<s:iterator  value="#request.weekList" id="ele" status="row">
								<option value='<s:property value="key" />'><s:property value="info" /></option>
								</s:iterator>
							</select>
						</td>
					</tr>
					<tr>
						<td height="27" align="right" class="input_label">查询时间:</td>
						<td><input name="" id="startTime" type="text" id="start" size="20"  class="MyInput" /></td>
						<td height="27" style="text-align: center" align="center" class="input_label">至 </td>
						<td><input name="" id="endTime" type="text" id="end"  size="20" class="MyInput" /></td>
						<td align="right" colspan="4"> 
							<input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">
							<tm:button site="1"/>
						</td>
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
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">时间段</div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">所属周</div></td>
			<td nowrap width="14%" class="oracolumncenterheader"><div align="center">项目整体状态</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">标准时长</div></td>
			<td nowrap width="30%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="20%" class="oracolumncenterheader"><div align="center">客户名称</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="projectWeekPlanList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) >
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td nowrap width="12%">
				<div align="left">
					<a href='javascript:show("<s:property value="id"/>")'>
						<font style="color: #3366FF"><s:property value="weekPeriod" /></font>
			  		</a>
			  	</div>
		  	</td>
		  	<td nowrap width="12%"><s:property value="weekDesc" /></td>
		 	<td nowrap width="14%" style="background-color:
		 		<s:if test="scheduleState == '项目进度延迟'">
		            #FF6600;
		        </s:if>
		        <s:elseif test="scheduleState == '项目进度超前'">
		            #00CCFF;
		        </s:elseif>
		 		<s:else>
		            #9BBB59;
		        </s:else>
		 	
		 	;">
		 		<s:property value="scheduleState"/>
		 	</td>
		 	<td nowrap width="10%"><s:property value="standardDuration"/></td>
		  	<td nowrap width="30%"><s:property value="projectName" /></td>
		 	<td nowrap width="20%"><s:property value="customerName"/></td>
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

