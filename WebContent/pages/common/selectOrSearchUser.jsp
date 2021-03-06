﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%
	String seename = (String)request.getAttribute("seename");
	String seeid = (String)request.getAttribute("seeid");
%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
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

function closeRoleModal(){
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie浏览器
		window.close();
	}else{
		parent.closeSelectPeople();
	}
}
	
function formsubmit(){
	var userInListSelectOption = $("#userInListSelect").find("option");
    var usergroups = "";
    for(var i=0;i<userInListSelectOption.length;i++){
 	   var ele=userInListSelectOption[i];
 	   usergroups+=$(ele).val().replace(",",":");
 	   if(i!=userInListSelectOption.length-1){
 		  usergroups+=",";
 	   }
    }
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie浏览器
		window.returnValue=usergroups;
		window.close();
	}else{
		var seename = "<%=seename%>";
		var seeid = "<%=seeid%>";
		parent.selectPeople(usergroups,seename,seeid);
	}
}

function getInUserIds(){
	var userInListSelectOption = $("#userInListSelect").find("option");
    var users = "";
    for(var i=0;i<userInListSelectOption.length;i++){
 	   var ele=userInListSelectOption[i];
 	   users+=$(ele).val().split(",")[0];
 	   if(i!=userInListSelectOption.length-1){
 		  users+=",";
 	   }
    }
    return users;
}

$(function(){
	$("#deptname").change(function(){
		var deptname = $("#deptname").val();
		var username = $("#username").val();
		var url = "<%=request.getContextPath()%>/pages/common/common!getSearchUser.action";
		var userIdInList=getInUserIds();
		$.ajax({
			url:url,
			datatype:"json",
			type:"post",
			data:{
				userIdInList:userIdInList,
				deptname:deptname,
				username:username
			},
			success:function(data){
				//data = eval('(' + data + ')');
				var option1 = "";
				var option2 = "";
				$("#outgroup option").remove();
				$.each(data[0].notinidname,function(key,value){
					option1 = option1 +"<option value="+value[0]+","+value[1]+" >"+value[1]+"("+value[0]+")"+"</option>";
				});
				$("#outgroup").append(option1);
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
		var deptname = $("#deptname").val();
		var username = $("#username").val();
		var url = "<%=request.getContextPath()%>/pages/common/common!getSearchUser.action";
		var userIdInList=getInUserIds();
		$.ajax({
			url:url,
			datatype:"json",
			type:"post",
			data:{
				userIdInList:userIdInList,
				deptname:deptname,
				username:username
			},
			success:function(data){
				//data = eval('(' + data + ')');
				var option1 = "";
				var option2 = "";
				$("#outgroup option").remove();
				$.each(data[0].notinidname,function(key,value){
					option1 = option1 +"<option value="+value[0]+","+value[1]+" >"+value[1]+"("+value[0]+")"+"</option>";
				});
				$("#outgroup").append(option1);
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

<form name="frm" action="" method="post">
<div class="settingtable">
<table width="96%" border="0" cellspacing="0" cellpadding="3" align="center">
   <tr>
  	<td colspan="3">
  		<label>部门查询:</label>
  		<select id="deptname" name="deptName" style="width:160px;">
  			<option value="">请选择...</option>
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
   <select id="outgroup" name="OutGroup" size="15" style="width:180px;height:250px" multiple ondblclick="MoveSelection(document.frm.OutGroup,document.frm.userInListSelect)">
        <s:iterator value="#request.userlist" id="user">
	         <option value="<s:property value='#request.user.userid'/>,<s:property value="#request.user.username"/>"><s:property value="#request.user.username"/>(<s:property value='#request.user.userid'/>)</option>
        </s:iterator>
   </select>  
  </td>
  <td align="left" width="60">
   <input type="button" class="wd2 btn btn_true" onclick="MoveSelection(document.frm.OutGroup,document.frm.userInListSelect)" value="  &#8250; " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onClick="MoveSelection(document.frm.userInListSelect,document.frm.OutGroup)" value=" &#8249;  " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onclick="MoveAll(document.frm.OutGroup,document.frm.userInListSelect)"  value=" &#187; " /><br /><br />
   <input type="button" class="wd2 btn btn_true" onclick="MoveAll(document.frm.userInListSelect,document.frm.OutGroup)" value=" &#171; " /><br />
  </td>
  <td align="left" style="padding:2px 10px 2px 20px;line-height:20px;">
   已有人员<br>
   <select id="userInListSelect" name="userInListSelect" size="15" style="width:180px;height:250px" multiple ondblclick="MoveSelection(document.frm.userInListSelect,document.frm.OutGroup)">
    	<s:iterator value="#request.userInList" id="user2">
	   		 <option value="<s:property value='#request.user2.userid'/>,<s:property value="#request.user2.username"/>"><s:property value="#request.user2.username"/>(<s:property value='#request.user2.userid'/>)</option>
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
   <input type="button" value=" 保存 " class="wd2 btn btn_true" onclick="formsubmit();"/>
   <input type="button" value=" 取消 " class="wd1 btn btn_true" onclick="closeRoleModal();"/>
  </td>
 </tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
