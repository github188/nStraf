<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
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
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script language="javascript">
	
	//确认修改
	function save(){
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var  dateIncome=$("#dateIncome").val();
		var  prjGroup=$("#prjGroup").val();
		if(startTime==""){
			alert("请填写项目起始时间！");
			$("#startTime").focus();
			return;
		}
		if(endTime==""){
			alert("请填写项目结束时间！");
			$("#endTime").focus();
			return;
		}
		if(startTime>endTime){
			alert("起始时间不能大于结束时间");
			return;
		}
		if(dateIncome==""){
			alert("请填写人日收入！");
			$("#dateIncome").focus();
			return;
		}
		if(prjGroup==""){
			alert("请填写项目名称！");
			$("#prjGroup").focus();
			return;
		}
	    document.getElementById("ok").disabled=true;
		window.returnValue=true;
		adddateincomeForm.submit();
	}	
	
</script>

<body id="bodyid"  leftmargin="0" topmargin="0">
<form name="adddateincomeForm" action="dateincome!saveModify.action";
		namespace="/pages/costControl" method="post">		
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
					<table class="input_table">
			      		<tr>
							<td class="input_tablehead">
								人日收入确认管理
							</td>
			      		</tr>
			      		
			      		 <s:iterator id="grp" value="#request.income" status="status">
			      		 <input type="hidden" id="dateIncomeId" name="dateincomeManage.dateIncomeId"  value="<s:property value='dateIncomeId'/>" >
			      		<tr >
			      			<td colspan="2"  bgcolor="#FFFFFF"  width="15%"><div>起始时间：</div></td>
			      			<td colspan="3" bgcolor="#FFFFFF">
			      				<input name="dateincomeManage.startTime"  id="startTime" type="text" class="MyInput"  value="<s:date name="startTime" format="yyyy-MM-dd" />">
			      				至
			      				
			      				<input name="dateincomeManage.endTime"  id="endTime"  type="text" class="MyInput" value="<s:date name="endTime" format="yyyy-MM-dd" />"> 
			      			</td>
			      		</tr>
			      		<tr  id="tr1" class="checkbox">
				      		<td bgcolor="#FFFFFF"  width="2%" nowrap align="center"><input type="checkbox" name="chkList"  />
							</td>
			      			<td bgcolor="#FFFFFF"  width="10%">项目组名称：</td>
			      			<td bgcolor="#FFFFFF" width="25%">
			      				
			      				<select name="dateincomeManage.prjGroup"	id="prjGroup"  style="width: 90%">
										<option value="<s:property value='prjGroup'/>" selected="selected"><s:property value='prjGroup'/></option>
										<s:iterator value="#request.pro" id="ele">
											<option value="<s:property value='name'/>"><s:property value='name'/></option>
											</s:iterator>
					</select>
							</td>
						
			      			<td bgcolor="#FFFFFF"  width="10%">人日收入：</td>
			      			<td bgcolor="#FFFFFF"  width="25%"><input id="dateIncome" name="dateincomeManage.dateIncome"  value="<s:property value="dateIncome"/>">人日</td>
			      		</tr>
			      		
			      		</s:iterator>
					</table>
				</td>
				</tr>
					
				</table>
			<br>
			<br>
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
