<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@page import="cn.grgbanking.feeltm.domain.*"%>
<html>
<head>
	<title>add  advances and responses</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link href="js/jquery.autocomplete.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="js/jquery.autocomplete.js"></script>
<script language="javascript">
var prePath="<%=request.getRequestURL().toString().substring(0,request.getRequestURL().toString().indexOf(request.getRequestURI()))%>";

$(function () {
	var url="<%=request.getContextPath()%>/pages/adviceAndResponse/myAdviceandResponse!searchAllUser.action";
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			$('#adviceMan').devbridgeAutocomplete({
			    lookup: data,
				minChars: 1,
				onSelect: function (suggestion) {
					$('#adviceMan').val(suggestion.username);
					$('#tel').val(suggestion.tel);
					$('#email').val(suggestion.email);
					$('#content').focus();
				},
				showNoSuggestionNotice: true,
				noSuggestionNotice: "<font color='#FF0000'>该用户不存在</font>",
				groupBy: 'category'
			});
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
});

$(function (){	
	var islogin=$("#longin").val();
	if(islogin=="yes"){
	 $("#adviceMan").css({ color: "#000000", background: "#D3D3D3"});
	 $("#adviceMan").attr("readonly","readonly");
	}
});



//确认保存
function save(){
	if(document.getElementById("adviceMan").value==""){
		alert("请填写建议提出人");
		document.getElementById("adviceMan").focus();
		return ;
	}
	if(document.getElementById("content").value==""){
		alert("请填写意见内容");
		document.getElementById("content").focus();
		return ;
	}
	
	if(document.getElementById("tel").value==""){
		alert("请填写联系电话");
		document.getElementById("tel").focus();
		return ;
	}
		var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	  	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	    var phoneormobile=document.getElementById("tel").value;
	    var email=document.getElementById("email").value;
    if(!isPhone.test(phoneormobile)&&!isMob.test(phoneormobile)){
    	alert("请填写正确的联系方式！");
		return;
    }
    if(!Email.test(email)){
    	alert("请填写正确的邮箱！");
		return;
    }	

    document.getElementById("ok").disabled=true;
	window.returnValue=true;
	addAdvanceAndResponseForm.submit();
}	



</script>

<body id="bodyid"  leftmargin="0" topmargin="0">
<%-- <%@include file="/inc/navigationBar.inc"%> --%>
<debug></debug>
<form name="addAdvanceAndResponseForm" action="myAdviceandResponse!save.action";
		namespace="/pages/adviceAndResponse" method="post">
		<input type="hidden" id="longin" name="longin"  value="<%=(String)request.getAttribute("longin")%>"><!-- 登录者的状态 -->
		
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
					<table class="input_table">
			      		<tr>
							<td class="input_tablehead">
								意见反馈
							</td>
			      		</tr>
			      		<tr>
							<td class="input_label2" width="13%">
		  						<div align="center">
									意见提出人：<font color="#FF0000">*</font>										
								</div>									
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input  name="adviceandresponse.adviceMan" maxlength="16" size="40" id="adviceMan"  onblur='' value="<%=(String)request.getAttribute("curUser")%>"  >
						</tr>
						
						<tr>
							<td class="input_label2">
								<div align="center">
									联系电话：<font color="#FF0000"></font>					
								</div>							
							</td>
							<td  bgcolor="#FFFFFF" >
								<input name="adviceandresponse.tel" maxlength="20" size="40" id="tel" value="<%=(String)request.getAttribute("usrtel")%>">
							</td>

						</tr>
						<tr>
							
							<td class="input_label2">
								<div align="center">
									邮箱：<font color="#FF0000"></font>					
								</div>							
							</td>
							<td  bgcolor="#FFFFFF" >
								<input name="adviceandresponse.email" maxlength="20" size="40" id="email" value="<%=(String)request.getAttribute("usremail")%>">
							</td>
							
						</tr>
						<tr>
						
             				
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									意见内容<font color="#FF0000"></font>		
								</div>					
							</td>
							<td  colspan="5">
								<div >
									<textarea name="adviceandresponse.content" cols="84"  rows="8" id="content"></textarea>
								</div>									
							</td>
						</tr>
					</table>
				</td>
				</tr>
				</table>
	<br />
	<br />
	<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
				<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	
   
	</form>
</body>
</html>
