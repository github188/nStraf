<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%><%-- 日志控件 --%>
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
<script type="text/javascript" src="../../calendar/fixDate.js"></script><!-- 日志控件-->
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
<style type="text/css">
.content{
    margin:30px 50px 30px 50px;
    overflow:hidden;
}
.headTitle{
color:#000000;/* font-family:'宋体'; */
font-size:16px;font-weight:bold;
}
.wordClass{
color:#000000;/* font-family:'宋体'; */
font-size:14px;
}
.huiSe{
color:#4C4C4C;/*深灰色  */
}
/*圆角边框开始*/
.b1,.b2,.b3,.b4,.b1b,.b2b,.b3b,.b4b,.b{display:block;overflow:hidden;}
.b1,.b2,.b3,.b1b,.b2b,.b3b{height:1px;}
.b2,.b3,.b4,.b2b,.b3b,.b4b,.b{border-left:1px solid #77cce7;border-right:1px solid #77cce7;}
.b1,.b1b{margin:0 5px;background:#77cce7;}
.b2,.b2b{margin:0 3px;border-width:2px;}
.b3,.b3b{margin:0 2px;}
.b4,.b4b{height:2px;margin:0 1px;}
.d1{background:#F7F8F9;}
/*圆角边框结束*/
</style>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<div class ="content" height="650px;">
	 <b class="b1"></b><b class="b2 d1"></b><b class="b3 d1"></b><b class="b4 d1"></b>
	 <div class="b d1 k" style="font-family: Microsoft YaHei,Microsoft JhengHei;">
		 <div class ="content">
			<div class = "headTitle">
			报名设置
			</div>
			<div style = "margin: 30px 0px 30px 0px;overflow:hidden">
				<label class = "wordClass">培训课程介绍：</label>
				<label id = "introduction" class = "wordClass huiSe">Html5开发框架的讨论与二次开发沟通</label>
			</div>
			<div style = "margin: 20px 0px 20px 0px;overflow:hidden" class = "wordClass">
				<label >报名时间：从&nbsp;</label> 
				<input name="queryStartTime" id="queryStartTime" type="text"  size="11"  class="dateTimeInput" />
					&nbsp;至&nbsp;
				<input name="queryEndTime" id="queryEndTime" type="text"  size="11" class="dateTimeInput" />
			</div>
			<div style = "margin: 20px 0px 20px 0px;overflow:hidden" class = "wordClass">
				<label >报名人数达&nbsp;</label> 
				<input name="limit" id="limit" type="text"  size="2"  />
				<label >&nbsp人时，报名停止。</label>
			</div>
			<div style = "margin: 20px 0px 20px 0px;overflow:hidden" class = "wordClass">
				<label >报名结束后的提示信息：</label> 
				<div style = "margin: 20px 0px 00px 0px;" >
					<!-- onblur="if(this.value=='') {alert('不说点什么吗？')}" -->
				<textarea rows="10" cols="100" class = "wordClass" style = "color:#CECED1;"
				onfocus="this.style.color='#000000';" 
                >如：请保持手机畅通，我们会随时您联系……</textarea>
				</div>
			</div>
			<div style = "margin: 20px 0px 0px 0px; overflow:hidden" class = "wordClass">
			<input name="" id="" type="checkbox" />
				<label >显示已报名人员</label> 
			</div>
			 <div style = "margin-left: 300px;overflow:hidden" >
			<input name="" id="" type="button" value = "保存设置" class = "wordClass" style ="height:35px; width:100px;" />
			</div> 
			 <div style = "margin: 20px 0px 0px 0px;overflow:hidden" class = "wordClass">
					<label >报名网址：</label> 
			</div> 
			 <div>
				<div style = "overflow:hidden" >
					<input name="" id="" type="button" value = "上一步" class = "wordClass" style ="height:35px; width:100px;" />
					<input name="" id="" type="button" value = "下一步" class = "wordClass" style ="height:35px; width:100px;" />
				</div>
			</div> 
		</div>
	</div>
	<b class="b4b d1"></b><b class="b3b d1"></b><b class="b2b d1"></b><b class="b1b"></b>
</div>
<div class ="content">
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
</div>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
