<%@page import="cn.grgbanking.feeltm.domain.SysOperLog"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet"
	href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css"
	type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String noUsername = (String) request.getAttribute("nousername");
	String noUserCard = (String) request.getAttribute("nousercard");
%>

<html>
<head></head>
<script type="text/javascript">
var allCount = 0;
function init(){
	var nousername = "<%=noUsername%>";
	var nousercard = "<%=noUserCard%>";
	var tempHtml="";
	for(var i=0;i<nousername.split(",").length;i++){
		tempHtml+='<tr><td>姓名</td><td><input readonly type="text" id="username'+i+'" name="username'+i+'" value="'+nousername.split(",")[i]+'"></td>';
		tempHtml+='<td>外派工号</td><td><input readonly type="text" id="usercard'+i+'" name="usercard'+i+'" value="'+nousercard.split(",")[i]+'"></td>';
		tempHtml+='<td>oa账号</td><td><input type="text" id="userid'+i+'" name="userid'+i+'" value=""></td><tr>';
	}
	$("#contentTable").html(tempHtml);
	allCount=nousername.split(",").length;
}
function save(){
	var count = 0;
	var user_id = "";
	for(var i=0;i<allCount;i++){
		var userid=document.getElementById("userid"+i).value;
		user_id+=","+userid;
		if(userid==""){
			count++
		}
	}
	if(count>0){
		alert("请确认OA账号是否全部填写完整");
		return;
	}
	if(user_id!=""){
		user_id=user_id.substring(1);
	}
	//一个oa账号只能对应一个外派工号
	var return_ = flagHasMoreOaUserId(user_id);
	if(return_!=""){
		alert("一个外派工号只能对应一个oa账号，重复的oa账号有："+return_);
		return;
	}
	flagHasUserid(user_id);
}

//判断一个外派工号对应一个oa账号
function flagHasMoreOaUserId(user_id){
	var moreUserid="";
	var userids = user_id.split(",");
	for(var i=0;i<userids.length;i++) {
		if(user_id.replace(userids[i]+",","").indexOf(userids[i]+",")>-1) {
			if(moreUserid.indexOf(userids[i])==-1){
				moreUserid+=","+userids[i];
			}
		}
	}
	if(moreUserid!=""){
		moreUserid=moreUserid.substring(1);
	}
	return moreUserid;
}

//判断oa账号是否有对应的外派工号
function flagHasUserid(user_id){
	var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!flagHasUserid.action?user_id="+user_id;
	$.ajax({
	     type:"post",
	     url:actionUrl,
		 dataType:"json",
		 cache: false,
		 async:true,
		 success:function(data){
			if(data.hasUserid!=""){
				if(confirm("oa账号"+data.hasUserid+"已有对应的外派工号，是否进行更新操作.'确定'将进行更新操作,'取消'将退出导入操作")){
					reportInfoForm.submit();
				}else{
					closeModal();
				}
			}else{
				reportInfoForm.submit();
			}
		 },
		 error:function(e){
			 alert("请求异常");
		 }
	}); 
}
</script>
<body id="bodyid" leftmargin="0" topmargin="20" onload="init();">
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/signrecord/signRecord!saveUserId.action" method="post">
		<input type="hidden" id="nousercard" name="nousercard" value="<%=noUserCard %>"/>
		<input type="hidden" id="nousername" name="nousername" value="<%=noUsername %>"/>
		<font color="red" >提示：请填写对应用户的OA账号，保存后，页面将跳转到导入页面，请重新选择刚验证的文件，进行数据导入。</font>
		<br/><br/>
		<table width="90%" class="input_table" id="contentTable">
			
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="okButton"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onclick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>

