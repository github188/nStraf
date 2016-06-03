<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page import="cn.grgbanking.feeltm.loglistener.domain.*" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" >
	function save(){
		if($("input[name='orgType']:checked").val()=='project' && $("#projectId").val()==""){
			alert("请先选择项目");
			return false;
		}else if($("input[name='orgType']:checked").val()=='dept' && $("#deptId").val()==""){
			alert("请先选择部门");
		}
		window.returnValue=true;
		holsForm0.submit();
	}
	
	function selectPeople(nameContainer,idContainer){
		var strUrl="/pages/common/common!selectOrSearchUser.action?userIdInList="+$("#"+idContainer).val()+"&see="+nameContainer+"&hidden="+idContainer;
		var feature="520,380,notidy.addTitle,notify";
	 	var returnValue=OpenModal(strUrl,feature);
	 	 
	 	if(returnValue!=null && returnValue!=""){
		 	var values = returnValue.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id +","+temp[0];
		 		name = name+"," + temp[1]+""; 
		 	} 
		 	name = name.substring(2);
		 	id =  id.substring(2);
		 	$("#"+nameContainer).val(name);
		 	$("#"+idContainer).val(id);
	 	}else{
	 		$("#"+nameContainer).val("");
		 	$("#"+idContainer).val("");
	 	}
	}
	
	function isInUserList(userid,userids){
		var userList=userids.split(",");
		for(var k=0;k<userList.length;k++){
			if(userid==userList[k]){
				return true;
			}
		}
		return false;
	}
	
	function addUser(orgType,role,nameContainer,idContainer){
		if("project"==orgType){//类型为项目
			
			if($("#projectId").val()==''){
				alert("请先选择项目，才能添加项目相关人员");
				return false;
			}
			$.post("<%=request.getContextPath()%>/pages/loglistener/loglistener!getRoleUser.action",{orgType:orgType,orgId:$('#projectId').val(),role:role},function(data){
				if(role=='projectPM' || role=='projectDM'){
					var beforeMonitorId=idContainer.val();
					var beforeMonitorName=nameContainer.val();
					for(var k=0;k<data.length;k++){
						var ele=data[k];
						var hasInUserList=isInUserList(ele.userid,beforeMonitorId);
						if(!hasInUserList){
							if(beforeMonitorId!=""){
								beforeMonitorId+=","+ele.userid;
								beforeMonitorName+=","+ele.username;
							}else{
								beforeMonitorId=ele.userid;
								beforeMonitorName=ele.username;
							}
						}
					}
					//设置到控件中
					idContainer.val(beforeMonitorId);
					nameContainer.val(beforeMonitorName);
					
				}else if(role='projectMB'){
					var beforeWatchedId=idContainer.val();
					var beforeWatchedName=nameContainer.val();
					for(var k=0;k<data.length;k++){
						var ele=data[k];
						var hasInUserList=isInUserList(ele.userid,beforeWatchedId);
						if(!hasInUserList){
							if(beforeWatchedId!=""){
								beforeWatchedId+=","+ele.userid;
								beforeWatchedName+=","+ele.username;
							}else{
								beforeWatchedId=ele.userid;
								beforeWatchedName=ele.username;
							}
						}
					}
					//设置到控件中
					idContainer.val(beforeWatchedId);
					nameContainer.val(beforeWatchedName);
				}
		  },"json");
			
		}else{
			if($("#deptId").val()==''){
				alert("请先选择部门，才能添加部门相关人员");
				return false;
			}
			$.post("<%=request.getContextPath()%>/pages/loglistener/loglistener!getRoleUser.action",{orgType:orgType,orgId:$('#deptId').val(),role:role},function(data){
				if(role=='deptDM'){
					var beforeMonitorId=idContainer.val();
					var beforeMonitorName=nameContainer.val();
					for(var k=0;k<data.length;k++){
						var ele=data[k];
						var hasInUserList=isInUserList(ele.userid,beforeMonitorId);
						if(!hasInUserList){
							if(beforeMonitorId!=""){
								beforeMonitorId+=","+ele.userid;
								beforeMonitorName+=","+ele.username;
							}else{
								beforeMonitorId=ele.userid;
								beforeMonitorName=ele.username;
							}
						}
					}
					//设置到控件中
					idContainer.val(beforeMonitorId);
					nameContainer.val(beforeMonitorName);
					
				}else if(role='deptMB'){
					var beforeWatchedId=idContainer.val();
					var beforeWatchedName=nameContainer.val();
					for(var k=0;k<data.length;k++){
						var ele=data[k];
						var hasInUserList=isInUserList(ele.userid,beforeWatchedId);
						if(!hasInUserList){
							if(beforeWatchedId!=""){
								beforeWatchedId+=","+ele.userid;
								beforeWatchedName+=","+ele.username;
							}else{
								beforeWatchedId=ele.userid;
								beforeWatchedName=ele.username;
							}
						}
					}
					//设置到控件中
					idContainer.val(beforeWatchedId);
					nameContainer.val(beforeWatchedName);
				}
		  },"json");
		}
	}
	
	
	function setSelectPeopleValue(returnValue,see,hidden){
		if(returnValue!=null && returnValue!=""){
		 	var values = returnValue.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id +","+temp[0];
		 		name = name+"," + temp[1]+""; 
		 	} 
		 	name = name.substring(2);
		 	id =  id.substring(2);
		 	$("#"+see).val(name);
		 	$("#"+hidden).val(id);
	 	}else{
	 		$("#"+see).val("");
		 	$("#"+hidden).val("");
	 	}
	}
	
	
	$(function(){
		listenOrgTypeChange();
		$("input[name='orgType']").change(function(){
			listenOrgTypeChange();
		});
	});
	
	function listenOrgTypeChange(){
		$("input[name='orgType']").each(function(){
			var checked=$(this).attr('checked')==true||$(this).prop('checked')==true;
			if(checked && $(this).val()=='project'){
				$("#projectList").show();//显示项目信息
				$("#projectMonitor").show();
				$("#projectWatched").show();
				$("#deptList").hide();//隐藏部门信息
				$("#deptMonitor").hide();
				$("#deptWatched").hide();
			}else if(checked && $(this).val()=='dept'){
				$("#deptList").show();//显示部门信息
				$("#deptMonitor").show();
				$("#deptWatched").show();
				$("#projectList").hide();//隐藏项目信息
				$("#projectMonitor").hide();
				$("#projectWatched").hide();
			}
		});
	}
	</script>
<title>修改日志监控</title>
</head>

<body id="bodyid">
<form name="holsForm0" action="<%=request.getContextPath() %>/pages/loglistener/loglistener!update.action?listener=0"   method="post">
<div id="groupnameDiv">
<table width="400"  class="input_table">
<tr>
	<td class="input_tablehead">修改日志监控</td>
</tr>
<tr>
    <td class="input_label2"><div align="right">修改类型
     <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff" style="font-size:15px;padding:3px;">
    <input type="hidden" name="id" value="<s:property value='#request.listener.id'/>">
   	<input type="radio" name="orgType" value="project" checked/>修改为项目 &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="orgType" value="dept" />修改为部门
    <script>
		for(var i=0;i<$("input[name='orgType']").length;i++){
			var ele=$("input[name='orgType']")[i];
			if($(ele).val()=='<s:property value="#request.listener.orgType"/>'){
				$(ele).attr('checked','checked');
			}
		}
	</script>
    </td>
</tr>
<tr id="projectList">
    <td class="input_label2"><div align="right">项目选择
    <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    <select name="projectId" id="projectId" style="width:50%">
			<option value=""><s:text name="staff.add.option"/></option>
				<s:iterator value="#request.projects" id="project">
					   <option value="<s:property value='id'/>,<s:property value='name'/>"><s:property value="name"/></option>
				</s:iterator>
	</select>
	<script>
		if('project'=='<s:property value="#request.listener.orgType"/>'){
			for(var i=0;i<$("#projectId").find("option").length;i++){
				var ele=$("#projectId").find("option")[i];
				var val=$(ele).val();
				var id=val.split(",")[0];
				if(id=='<s:property value="#request.listener.orgId"/>'){
					$(ele).attr('selected','selected');
				}
			}
		}
	</script>
    </td>
</tr>
<tr id="projectMonitor">
    <td class="input_label2"><div align="right">监控人
     <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
	    <textarea rows="1" cols="50" name="projectMonitorName" id="projectMonitorName" readonly="readonly"></textarea>
	    <input type="hidden" name="projectMonitorId" id="projectMonitorId">
	    <input type="button" value="选择"  onclick="selectPeople('projectMonitorName','projectMonitorId')"/>
	    <input type="button" value="项目经理"  onclick="addUser('project','projectPM',$('#projectMonitorName'),$('#projectMonitorId'))"/>
	    <input type="button" value="部门经理"  onclick="addUser('project','projectDM',$('#projectMonitorName'),$('#projectMonitorId'))"/>
    </td>
    <script>
		if('project'=='<s:property value="#request.listener.orgType"/>'){
			$("#projectMonitorName").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getMonitorName()%>');
			$("#projectMonitorId").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getMonitorId()%>');
		}
	</script>
</tr>
<tr id="projectWatched">
    <td class="input_label2"><div align="right">被监控人
    <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
	    <textarea rows="3" cols="50" name="projectWatchedName" id="projectWatchedName" readonly="readonly"></textarea>
	    <input type="hidden" name="projectWatchedId" id="projectWatchedId">
	    <input type="button" value="选择"  onclick="selectPeople('projectWatchedName','projectWatchedId')"/>
	    <input type="button" value="添加项目组成员"  onclick="addUser('project','projectMB',$('#projectWatchedName'),$('#projectWatchedId'))"/>
    </td>
    <script>
		if('project'=='<s:property value="#request.listener.orgType"/>'){
			$("#projectWatchedName").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getWatchedName()%>');
			$("#projectWatchedId").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getWatchedId()%>');
		}
	</script>
</tr>

<tr id="deptList">
    <td class="input_label2"><div align="right">部门选择
    <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    <select name="deptId" id="deptId" style="width:50%">
			<option value=""><s:text name="staff.add.option"/></option>
				<s:iterator value="#request.depts" id="dept">
					   <option value="<s:property value='key'/>,<s:property value='value'/>"><s:property value="value"/></option>
				</s:iterator>
	</select>
	<script>
		if('dept'=='<s:property value="#request.listener.orgType"/>'){
			for(var i=0;i<$("#deptId").find("option").length;i++){
				var ele=$("#deptId").find("option")[i];
				var val=$(ele).val();
				var id=val.split(",")[0];
				if(id=='<s:property value="#request.listener.orgId"/>'){
					$(ele).attr('selected','selected');
				}
			}
		}
	</script>
    </td>
</tr>
<tr id="deptMonitor">
    <td class="input_label2"><div align="right">监控人
     <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    	<textarea rows="1" cols="50" name="deptMonitorName" id="deptMonitorName" readonly="readonly"></textarea>
	    <input type="hidden" name="deptMonitorId" id="deptMonitorId">
	    <input type="button" value="选择"  onclick="selectPeople('deptMonitorName','deptMonitorId')"/>
	    <input type="button" value="部门经理"  onclick="addUser('dept','deptDM',$('#deptMonitorName'),$('#deptMonitorId'))"/>
    </td>
    <script>
		if('dept'=='<s:property value="#request.listener.orgType"/>'){
			$("#deptMonitorName").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getMonitorName()%>');
			$("#deptMonitorId").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getMonitorId()%>');
		}
	</script>
</tr>
<tr id="deptWatched">
    <td class="input_label2"><div align="right">被监控人
    <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    	<textarea rows="3" cols="50" name="deptWatchedName" id="deptWatchedName" readonly="readonly"></textarea>
	    <input type="hidden" name="deptWatchedId" id="deptWatchedId">
	    <input type="button" value="选择"  onclick="selectPeople('deptWatchedName','deptWatchedId')"/>
	    <input type="button" value="添加部门成员"  onclick="addUser('dept','deptMB',$('#deptWatchedName'),$('#deptWatchedId'))"/>
    </td>
    <script>
		if('dept'=='<s:property value="#request.listener.orgType"/>'){
			$("#deptWatchedName").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getWatchedName()%>');
			$("#deptWatchedId").val('<%=((LogListenerMonitor)request.getAttribute("listener")).getWatchedId()%>');
		}
	</script>
</tr>
</table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
</table> 
    </div>
    </form>
</body>
</html>
