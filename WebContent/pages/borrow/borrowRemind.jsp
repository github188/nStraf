<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {
		/*var type="";
		var em= document.all.tags("input");
		for(var  i=0;i<em.length;i++){
			if(em[i].type=="checkbox"){
				if(em[i].checked){
					if(em[i].value=="0"){
						$("#email").val("0");
					}else{
						$("#oa").val("1");
					}
				}
			} 
		}*/
		var idList="";
		var em = $("input[type='checkbox']"); 
		for(var  i=0;i<em.length;i++){
			if(em[i].type=="checkbox"){
				if(em[i].checked){
		        	idList+=","+em[i].value.split(",")[0];
		  		}
		  	} 
	 	} 
		if(idList=="") 
			idList = idList.substring(1);
		for(var i=0;i<idList.split(",").length;i++){
			if(idList.split(",")[i]=="0"){
				$("#email").val("0");
			}
			if(idList.split(",")[i]=="1"){
				$("#oa").val("1");
			}
		}
		if($("#email").val()=="" && $("#oa").val()==""){
			alert("请选择还款提示方式");
			return;
		}
		window.returnValue = true;
		reportInfoForm.submit();
	}
	
	$(function(){
		var period = "<%=request.getAttribute("period")%>";
		var email = "<%=request.getAttribute("email")%>";
		var oa = "<%=request.getAttribute("oa")%>";
		var remindid = "<%=request.getAttribute("id")%>";
		if(!(remindid==null || remindid=="null")){
			$("#remindid").val(remindid);
		}
		$("#period option[value='"+period+"']").attr("selected","selected");
		var arrChk = $("input[type='checkbox']");  
        $(arrChk).each(function() {  
        	if($(this).val()==email){
        		$(this).attr("checked","checked");
        	}
        	if($(this).val()==oa){
        		$(this).attr("checked","checked");
        	}
         }); 
	});
	
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/borrow/borrowInfo!saveRemind.action" method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>	
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">还款提示设置</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_label2">还款提示方式:</td>
							<td bgcolor="#FFFFFF">
								<input type="hidden" name="remindid" id="remindid" value="" />
								<input type="hidden" name="email" id="email" value="" />
								<input type="hidden" name="oa" id="oa" value="" />
								<input type="checkbox" name="type" id="type" value="0">邮件
								<input type="checkbox" name="type" id="type" value="1">OA消息提醒
							</td>
						</tr>
						<tr>
			                <td class="input_label2">还款周期设置:</td>
							<td bgcolor="#FFFFFF">
								每
								<select id="period" name="period">
								<%for(int i=1;i<=31;i++){ %>
			                		<option value="<%=i%>"><%=i %></option>
			                	<%} %>
			                	</select>天一次
							</td>
						</tr>
					</table> 
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="return"
					value='<s:text  name="grpInfo.ok"/>' class="MyButton"
					onclick="save();" image="../../images/share/f_closed.gif">
					<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
