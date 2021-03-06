﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script language="javascript">
function MoveAll(FromList,ToList)
{
 for(var i=0;i<FromList.length;i++)
 {
   ToList[ToList.length]=new Option(FromList.options[i].text,FromList.options[i].value);
 }
 FromList.length=0;
}
function MoveSelection(FromList,ToList)
{
 for(var i=0;i<FromList.length;i++)
 {
  if(FromList.options[i].selected)
  {
   ToList[ToList.length]=new Option(FromList.options[i].text,FromList.options[i].value);
  }
 }
 for(i=FromList.length-1;i>=0;i--)
 {
  if(FromList.options[i].selected)FromList.remove(i);
 }
}

/* function closeRoleModal(){
	  window.close();
} */
	
function formsubmit(){
       var usergroupsOptions = document.getElementById("usergroups").options;
       var usergroups = "";
       if(usergroupsOptions.length>0){
	       for(var i=0;i<usergroupsOptions.length-1;i++){
	    	   usergroups = usergroups + usergroupsOptions[i].value +",";
	       }
	       usergroups = usergroups + usergroupsOptions[usergroupsOptions.length-1].value;
       }
       frm.action="<%=request.getContextPath() %>/pages/um/sysUserGroup!updateusrGroup.action?usergroups="+usergroups;
       frm.submit();
}

$(function(){
	$("#deptname").change(function(){
		var grpcode = $("#grpcode").val();
		var deptname = $("#deptname").val();
		var username = $("#username").val();
		var url = "<%=request.getContextPath()%>/pages/um/sysUserGroup!getStaffByDeptName.action";
		$.ajax({
			url:url,
			datatype:"json",
			type:"post",
			data:{
				grpcode:grpcode,
				deptname:deptname,
				username:username
			},
			success:function(data){
				//data = eval('(' + data + ')');
				var option1 = "";
				var option2 = "";
				$("#outgroup option").remove();
				$.each(data[0].notgrp,function(key,value){
					option1 = option1 +"<option value="+value[0]+" >"+value[1]+"</option>";
				});
				$("#outgroup").append(option1);
				/* $("#usergroups option").remove();
				$.each(data[0].grplist,function(key,value){
					option2 = option2 +"<option value="+value[0]+" >"+value[1]+"</option>";
				});
				$("#usergroups").append(option2); */
				$("#deptname option").remove();
				var optionEm3 = "<option value=''>请选择...</option>";
				$.each(data[0].dept,function(key,value){
					var flag = key==deptname;
					if(flag){
						optionEm3 = optionEm3+"<option value="+key+" selected>"+value+"</option>";
					}else{
						optionEm3 = optionEm3+"<option value="+key+">"+value+"</option>";
					}
				});
				$("#deptname").append(optionEm3);
			},
			error:function(e){
				alert("error:"+e);
			}
		});
	});
	
	$("#searchbtn").click(function(){
		var grpcode = $("#grpcode").val();
		var deptname = $("#deptname").val();
		var username = $("#username").val();
		var url = "<%=request.getContextPath()%>/pages/um/sysUserGroup!getStaffByDeptName.action";
		$.ajax({
			url:url,
			datatype:"json",
			type:"post",
			data:{
				grpcode:grpcode,
				deptname:deptname,
				username:username
			},
			success:function(data){
				//data = eval('(' + data + ')');
				var option1 = "";
				var option2 = "";
				$("#outgroup option").remove();
				$.each(data[0].notgrp,function(key,value){
					option1 = option1 +"<option value="+value[0]+" >"+value[1]+"</option>";
				});
				$("#outgroup").append(option1);
				/* $("#usergroups option").remove();
				$.each(data[0].grplist,function(key,value){
					option2 = option2 +"<option value="+value[0]+" >"+value[1]+"</option>";
				});
				$("#usergroups").append(option2); */
				$("#deptname option").remove();
				var optionEm3 = "<option value=''>请选择...</option>";
				$.each(data[0].dept,function(key,value){
					var flag = key==deptname;
					if(flag){
						optionEm3 = optionEm3+"<option value="+key+" selected>"+value+"</option>";
					}else{
						optionEm3 = optionEm3+"<option value="+key+">"+value+"</option>";
					}
				});
				$("#deptname").append(optionEm3);
			},
			error:function(e){
				alert("error:"+e);
			}
		});
	});
	
});

</script>

<body id="bodyid"  leftmargin="0" topmargin="10" >

<form name="frm" action="/pages/um/sysUserGroup!updateusrGroup.action" method="post">
<input type="hidden" id="grpcode" name="grpcode" value="<s:property value='#request.grpcode'/>"/>
<div class="settingtable">
<table width="96%" border="0" cellspacing="0" cellpadding="3" align="center">
   <tr>
  	<td colspan="3">
  		<label>部门查询:</label>
  		<select id="deptname" name="deptName" style="width:180px;">
  			<option value="">全部</option>
	        <s:iterator value="#request.deptMap">
		         <option value="<s:property value='key'/>"><s:property value="value"/></option>
	        </s:iterator>
   		</select>  
   		姓名查询：<input id="username" type="text" name="username"><input type="button" id="searchbtn" value="搜索">
  	</td>
  </tr>
 <tr>
  <td align="left" width="270" style="padding:2px 5px 2px 8px;line-height:20px;">
   可选人员<br>
   <select id="outgroup" name="OutGroup" size="15" style="width:180px;height:250px" multiple ondblclick="MoveSelection(document.frm.OutGroup,document.frm.usergroups)">
        <s:iterator value="#request.userList" id="user">
	         <option value="<s:property value='#request.user.userid'/>"><s:property value="#request.user.username"/></option>
        </s:iterator>
   </select>  
  </td>
  <td align="left" width="60">
   <input type="button" class="wd2 btn btn_true" onclick="MoveSelection(document.frm.OutGroup,document.frm.usergroups)" value="  &#8250; " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onClick="MoveSelection(document.frm.usergroups,document.frm.OutGroup)" value=" &#8249;  " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onclick="MoveAll(document.frm.OutGroup,document.frm.usergroups)"  value=" &#187; " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onclick="MoveAll(document.frm.usergroups,document.frm.OutGroup)" value=" &#171; " /><br />
  </td>
  <td align="left" style="padding:2px 10px 2px 20px;line-height:20px;">
   已有人员<br>
   <select id="usergroups" name="usergroups" size="15" style="width:180px;height:250px" multiple ondblclick="MoveSelection(document.frm.usergroups,document.frm.OutGroup)">
   	<s:iterator value="#request.usergrpList" id="user2">
   		 <option value="<s:property value='#request.user2.userid'/>"><s:property value="#request.user2.username"/></option>
   	</s:iterator>
            </select>
  </td>
 </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="2" class="toolbg">
 <input type="hidden" name="AddrID" value="">
 <input type="hidden" name="t">
 <tr>
  <td align="center" nowrap class="barspace toolbgline f_family">
   <input type="button" value=" 保存 " class="wd2 btn btn_true" onclick="formsubmit();"/><input type="button" value=" 取消 " class="wd1 btn btn_true" onclick="closeModal();"/>
  </td>
 </tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
