<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
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

<html>
<head>
<title>DayLog query</title>
<style type="text/css">
  .check_area{
     font-size:12px;
     margin-left:5%;
  }
  .select_text{
    margin-left:8px;
    margin-top:5px;
  }
  .td_date{
    margin-left:5px;
  }
</style>
</head>
<script type="text/javascript"> 
	//加载时限制下拉框宽度,主要是针对名称过长时出现的问题,设置100%是为了让它适应外部td
	$(function(){
		$("#grpCode").attr("style","width:100%");
		//jqueryui的日期控件 
		$("#queryStartTime,#queryEndTime").datepicker({  
            dateFormat:'yy-mm-dd',  //更改时间显示模式  
            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
            changeYear:true        //是否显示年份的下拉菜单，默认为false  
         });  
		
		$("input[name='Submit1'][type='reset']").click(function(){
			setTimeout(function(){
				$("#queryStartTime").val('');
				$("#queryEndTime").val('');
			},10);
		});
		
	});
	
	function query(){
		//checkbox选中状态
		var isCheck=document.getElementById("checkArea").checked;
		var userDept= $("#queryUserDept").find("option:selected").text();
		var userGroup= $("#queryUserGroup").find("option:selected").text(); 
		if($("#queryUserGroup").find("option:selected").attr("title")){
			userGroup = $("#queryUserGroup").find("option:selected").attr("title");
		}
		var userName= $("#idqueryUserName").find("option:selected").text(); 
		var queryStartTime=  document.getElementById("queryStartTime").value;
		var queryEndTime=  document.getElementById("queryEndTime").value;
		var pageNum= document.getElementById("pageNum").value;
		var confirmStatus = $("select[name=confirmStatus]").val();
		var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!refresh.action?from=refresh&pageNum="+pageNum;
        actionUrl+= "&isCheck="+isCheck;
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
		if(queryStartTime!=''){
			actionUrl+="&queryStartTime="+queryStartTime;
		}
		if(queryEndTime!=''){
			actionUrl+="&queryEndTime="+queryEndTime;
		}
		if(confirmStatus!=''){
			actionUrl += "&confirmStatus=" + confirmStatus;
		}
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0;%>
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
		
	function setHTML(entry,entryInfo){
		var html = '';
		//alert(entryInfo["id"]);
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		var groupInfo=entryInfo["userGroup"];
		if(groupInfo.length>25){
			var shortShow=groupInfo.substring(0,25)+'...';
			shortShow='<a style="font-weight:bold;" title="'+groupInfo+'">'+shortShow+'</a>';
			groupInfo=shortShow;
		}
		
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td><a href='+str+'><font style="color: #3366FF">' +entryInfo["logDate"] + '</font></a></td>';
		html += '<td align="center">'+entryInfo["userName"]+'</td>';
		html += '<td align="center">' +entryInfo["userDept"] + '</td>';
		html += '<td>' + entryInfo["subTotal"] + '</td>';
/* 		html += '<td align="center">' +entryInfo["auditStatus"]+ '</td>';
		html += '<td align="center">' +entryInfo["auditMan"]+ '</td>';
		html += '<td align="center">' +entryInfo["auditTime"]+ '</td>'; */
		html += '<td align="center">' +entryInfo["confirmHour"]+ '</td>';
		html += '<td align="center">' +entryInfo["confirmStatus"]+ '</td>';
		html += '<td align="center">'+ entryInfo["updateMan"] + '</td>';
		html += '<td align="center">'+ entryInfo["updateTime"] + '</td>';
	  	html += '</tr>';
	 	<%k++;%>;
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
	
	function auditing(){
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
		 	alert('请选择记录');
		 else if (j>1)
		 	alert('一次只能处理一条记录')
		else{
		  	var strUrl="/pages/daylog/logInfo!toAuditing.action?ids="+itemId;
		  	var features="840,800,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
	}
	
	function UpAuditing(){
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
		 	alert('请选择记录')
		 else if (j>1)
		 	alert('一次只能处理一条记录')
		else{
			if(confirm("提交审核后，将不能修改当前日志，确定提交审核么?")){
				var strUrl="/pages/daylog/logInfo!upauditing.action?ids="+itemId;
			  	var features="830,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();				
			}
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
		 	alert('请选择一天的记录');
		 else if (j>1)
		 	alert('你只能一次删除一天的记录');
		else{
			if(confirm('您确认删除该天的工作记录吗？')) {
		  		var strUrl="/pages/daylog/logInfo!delete.action?ids="+itemId;
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
		  	var strUrl="/pages/daylog/logInfo!edit.action?ids="+itemId;
		  	var features="830,800,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
			//location="/nStraf/pages/daylog/logInfo!edit.action?ids="+itemId;
		}
	}
	function remark(){
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
			var strUrl="/pages/daylog/logInfo!show.action?ids="+itemId+"&operate=remark";
			var features="840,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
			//location="/nStraf/pages/daylog/logInfo!edit.action?ids="+itemId;
		}
	}
	
	
	function add(){
		var dosewritedaylog=document.getElementById("dosewritedaylog").value;
		if(dosewritedaylog=="yes"){
			//将新增按钮设置为不可用  若当前的用户已经添加了日志 则将爱那个新增的按钮设为不可用
			alert("您今天已经填写了日志，不需要再次填写哦");
			$("[value='新增']").attr("disabled", true);
		}else{
			$("[value='新增']").attr("disabled", false);
			var resultvalue = OpenModal('/pages/daylog/logInfo!add.action','840,800,tmlInfo.addTmlTitle,tmlInfo');
			refreshList();
			//location='/nStraf/pages/daylog/logInfo!add.action';

		}
	}
		
				
	function show(id) {
		var strUrl="/pages/daylog/logInfo!show.action?ids="+id+"&operate=show";
		var features="840,800,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features);
		refreshList();
	}
	
	function toExport(){ 
		var strUrl="/pages/daylog/logInfo!toExport.action";
		var features="400,300,transmgr.traninfo,watch";
		OpenModal(strUrl,features);
	}
	function toQueryExport(){ 
		var userDept= $("#queryUserDept").find("option:selected").text();
		var userGroup= $("#queryUserGroup").find("option:selected").text(); 
		var userName= $("#idqueryUserName").find("option:selected").text(); 
		var queryStartTime=  document.getElementById("queryStartTime").value;
		var queryEndTime=  document.getElementById("queryEndTime").value;
		var confirmStatus = $("select[name=confirmStatus]").val();

		
		var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!exportData.action?1=1";
		if(userDept!='---请选择部门---'){
			actionUrl += "&dateDayLog.userDept="+userDept;
		}
		if(userGroup!='---请选择项目名称---'){
			actionUrl +="&dateDayLog.userGroup="+userGroup;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
			if(userName.indexOf('(')>=0){
				actionUrl+="&dateDayLog.userName="+userName.substring(0,userName.indexOf('('));
			}else{
				actionUrl+="&dateDayLog.userName="+userName;
			}
		}else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&dateDayLog.userName="+$("#queryUserName").val();
		}
		if(queryStartTime!=''){
			actionUrl+="&queryStartTime="+queryStartTime;
		}
		if(queryEndTime!=''){
			actionUrl+="&queryEndTime="+queryEndTime;
		}
		// 确认状态 这里要加实体前缀，否则无法获得该属性值，利用了struts2的反射
		// 而在查询时，java代码中并没有用反射，所以 request.pa。。。（"confirmStatus"）这样获得；不统一！！！
		// 添加2016年3月16日8:23:57
		if (confirmStatus != '') {  // 为1时 已确认
			actionUrl+="&dateDayLog.confirmStatus="+confirmStatus;
			
		}


		//actionUrl = encodeURI(actionUrl,"UTF-8");
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		window.close();
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
	});
	//加载时限制下拉列表宽度,主要是针对名称过长时出现的问题
	$(function(){
		$("#grpCode").attr("style","width:100%");
	})
	
	function resetInfo(){
		$("#queryUserDept").val('全选');
		$("#queryUserGroup").val('全选');
		$("#confirmStatus").val('');
		$("#queryStartTime").val('');
		$("#queryEndTime").val('');
		var userName = $("#idqueryUserName").find("option:selected").text();
		if($("#idqueryUserName").is(":visible")==true){
			$("#idqueryUserName").val('全选');
		}else if($("#idqueryUserName").is(":visible")==false){
			$("#queryUserName").val('');
		}
	}
	
	function workEnterProject(){ 
		var strUrl="/pages/daylog/logInfo!projectSelect.action?role=groupManager";
		var features="450,250,transmgr.traninfo,watch";
		OpenModal(strUrl,features);
	}
	
	function workEnterDept(){ 
		var strUrl="/pages/daylog/logInfo!projectSelect.action?role=deptManager";
		var features="450,250,transmgr.traninfo,watch";
		OpenModal(strUrl,features);
	}
	//菜单测试
	/* function test(){
		   var actionto = "/pages/weeklog/logInfo!query.action";//个人周报
		 redirect(actionto); 
		}
	function test2(){
		   var actionto = "/pages/prjchance/prjChanceInfo!listAll.action";//个人周报
		 redirect(actionto); 
		} */
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="dayInfoForm" namespace="/pages/dayinfo"
		action="dayInfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
  		<input type="hidden" id="dosewritedaylog" name="dosewritedaylog"  value="<%=request.getAttribute("dosewritedaylog")%>"><!-- 数据的状态 -->
  		
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr width="100%">
				<td width="70%" colspan="6">
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
					</table>
				</td>
				<td width="30%" colspan="3" align="center"><input type="checkbox" value="按项目查" class=".check_area" id="checkArea"><b>查询项目人员日志</b> </td>
			</tr>
			<tr width="100%">
			    <td width="70%" colspan="6">
			        <table class="select_text" cellPadding="1px">
			            <tr>
			                <td align="right" width="7%" class="input_label" nowrap="nowrap">确认状态 :</td>
							<td align="left" width="28%"><s:select list="#{1:'已确认',0:'未确认' }" name="confirmStatus" id="confirmStatus" headerKey="" headerValue="---请选择状态---"></s:select></td>
							<td nowrap="nowrap" align="left" width="10%" class="td_date"><b>日志时间 :</b></td>
							<td width="15%" align="left"><input name="" readonly="true" id="queryStartTime" type="text"  size="11" value='<s:property value="#request.queryStartTime"/>'></td>
							<td nowrap="nowrap" align="left" width="5%">至</td>
							<td width="30%"><input id="queryEndTime" readonly="true" ty	pe="text"  size="11" value='<s:property value="#request.queryEndTime"/>'></td>
			            </tr>
			        </table>
			    </td>
				<td align="center" colspan="3" nowrap="nowrap" width="30%" border="1px solid">
				<!-- <input type="button" name="testBut" id="testBut" value="个人周报" class="MyButton" image="../../images/share/Modify_icon.gif" onclick="test()">
				<input type="button" name="testBut" id="testBut" value="商机管理" class="MyButton" image="../../images/share/Modify_icon.gif" onclick="test2()">
				 -->	
				 <input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">			
				    <tm:button site="1" />
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">日志时间 </div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">员工姓名</div></td>
			<td nowrap width="14%" class="oracolumncenterheader"><div align="center">所属部门</div></td>
			<td nowrap width="7%" class="oracolumncenterheader"><div align="center">工时小计</div></td>
<!-- 			<td nowrap width="9%" class="oracolumncenterheader"><div align="center">审核状态</div></td>
			<td nowrap width="9%" class="oracolumncenterheader"><div align="center">审核人</div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">审核时间</div></td> -->
			<td nowrap width="9%" class="oracolumncenterheader"><div align="center">确认工时</div></td>
 			<td nowrap width="9%" class="oracolumncenterheader"><div align="center">确认状态</div></td>
			<td nowrap width="9%" class="oracolumncenterheader"><div align="center">更新人</div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">更新时间</div></td>
			</tr>
			<tbody name="formlist" id="formlist">
				<s:iterator value="dateDayLogList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this) >
					</s:else>
					<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
					<td nowrap>
						<div align="center">
							<a href='javascript:show("<s:property value="id"/>")'>
								<font style="color: #3366FF"><s:property value="logDate"/></font>
					  		</a>
					  	</div>
				  	</td>
				 	<td nowrap align="center"><s:property value="userName"/></td>
				 	<td nowrap align="center"><s:property value="userDept"/></td>
				 	<td nowrap>
				    	<div align="center"><s:property value="subTotal"/></div>
				    </td>
					    <td >
						    <div align="center">
						    	<s:property value="confirmHour"/>
						    </div>
					    </td>
				    <td nowrap><div align="center"><s:property value="confirmStatus"/></div></td>
<%-- 				    <td nowrap><s:property value="auditStatus"/></td>
				    <td nowrap><s:property value="auditMan"/></td>
				    <td nowrap><s:property value="auditTime"/></td> --%>
				    <td nowrap><div align="center"><s:property value="updateMan"/></div></td>
				    <td nowrap>
						<div align="center">
							<s:property value="updateTime"/>
					  	</div>
				  	</td>
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

