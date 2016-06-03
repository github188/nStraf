<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
	<head>
		<title>certification query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<%-- <script type="text/javascript" src="../../js/jquery.js"></script> --%>
	<script type="text/javascript" src="../../js/common.js"></script>
	<script type="text/javascript">
	/* function closeModal(){
 		if(!confirm('您确认关闭此页面吗？')){
			return;				
		}
	 	window.close();
	} */
	function save(){
		if(document.getElementById("client").value==""){
			alert("请填写客户名称");
			document.getElementById("client").focus();
			return ;
		}
		if(document.getElementById("mouthPiece").value==""){
			alert("请填写联系人");
			document.getElementById("mouthPiece").focus();
			return ;
		}
		if(document.getElementById("address").value==""){
			alert("请填写联系地址");
			document.getElementById("address").focus();
			return ;
		}
		if(document.getElementById("creatDate").value==""){
			alert("请填写创建日期");
			return ;
		}
		if(document.getElementById("tel").value==""){
			alert("请填写联系电话");
			document.getElementById("tel").focus();
			return ;
		}
		if(document.getElementById("mail").value==""){
			alert("请填写联系邮箱");
			document.getElementById("mail").focus();
			return ;
		}
		var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		var mail = document.getElementById("mail").value;
		if(!Email.test(mail)){
			alert("请填写正确的邮箱格式!");
			return;
		}
		document.getElementById("ok").disabled=true;
		window.returnValue=true;
		customInfoForm.submit();
	}	
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
<div style="height:450px;overflow-y:auto;">
<form name="customInfoForm" action="<%=request.getContextPath()%>/pages/custom/customInfo!update.action" method="post">
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
					<table class="input_table">
			      		<tr>
					<td class="input_tablehead">
						修改客户信息
					</td>
			      		</tr>
			      		<input name="custom.id" type="hidden"  value='<s:property value="custom.id"/>' >
			      		<tr>
							<td class="input_label2" width="13%">
		  						<div align="center">
									客户名称<font color="#FF0000">*</font>										
								</div>									
							</td>
							<td bgcolor="#FFFFFF" width="25%">
							<s:textfield name="custom.client" maxlength="16" size="40" id="client"/>
							</td>
							<td width="13%" class="input_label2">
								<div align="center">
									联系人<font color="#FF0000">*</font>					
								</div>							
							</td>
							<td  bgcolor="#FFFFFF" width="25%">
								<s:textfield name="custom.mouthPiece" maxlength="16" size="40" id="mouthPiece"/>
							</td>
						</tr>
						
						<tr>
							<td  class="input_label2">
								<div align="center">
									联系地址<font color="#FF0000">*</font>					
								</div>							
							</td>
							<td  bgcolor="#FFFFFF" >
								<s:textfield name="custom.address" maxlength="20" size="40" id="address"/>
							</td>
							<td class="input_label2">
								<div align="center"> 
								  	创建日期 <font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<input name="custom.creatDate" type="text" id="creatDate" class="MyInput" 
								readonly="readonly"  
								value='<s:date name="custom.creatDate" format="yyyy-MM-dd"/>'>
                           	</td>
						</tr>
						<tr>
						<td class="input_label2" width="13%">
								<div align="center"> 
								  	联系电话 <font color="#FF0000">*</font>
								</div>
							</td>
							<td width="14%" bgcolor="#FFFFFF">
								<s:textfield name="custom.tel" maxlength="15" size="40" id="tel" onkeyup="this.value=this.value.replace(/[^0-9\-]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\-]+/,'')"/>
                           	</td>
                           	<td class="input_label2">
								<div align="center"> 
								  	联系邮箱<font color="#FF0000">*</font>
								</div>
							</td>
							<td width="14%" bgcolor="#FFFFFF">
								<s:textfield name="custom.mail" maxlength="25" size="40" id="mail" onkeyup="this.value=this.value.replace(/[^0-9a-zA-Z@_.\-]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9a-zA-Z@_.\-]+/,'')"/>
                           	</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									项目列表<font color="#FF0000"></font>		
								</div>					
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div >
									<s:textarea name="custom.prjList" onkeyup="limitLen(500,this)" cols="84" rows="3" id="prjList"/>
								</div>									
							</td>
						</tr>
						
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>		
								</div>					
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div >
									<s:textarea name="custom.note" onkeyup="limitLen(500,this)" cols="84" rows="3" id="note"/>
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
</div>
</body>
</html>
