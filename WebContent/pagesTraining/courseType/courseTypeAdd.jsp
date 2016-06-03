<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>addOrModify</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@include file="/inc/pagination.inc"%>
<script type="text/javascript">
function validateForm(){
 	var courseType = dataDirForm.courseType.value;
   	var courseDesc = dataDirForm.courseDesc.value;
   	if(courseType==""){
   		alert("课程类别不能为空")
   		return false
   	}
   	window.returnValue=true;
   	return true;
}
 
function SubmitForm(){
	if(validateForm()){
		var action="<%=session.getAttribute("action")%>";
		var parentid=dataDirForm.parentid.value;
		var id=dataDirForm.id.value;
		var strUrl = "";
	   	if(dataDirForm.newAnOther==null){//修改
	   	 	strUrl="<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!save.action?action="+action+"&parentid="+parentid+"&id="+id;
	 	}else{//新增
			var newAnOther=dataDirForm.newAnOther.checked;
	   		strUrl="<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!save.action?newAnOther=" + newAnOther;
		}
		dataDirForm.action = strUrl;
		dataDirForm.submit();
	}
}

function setFlag() {
	var flag = dataDirForm.flag.value;
	if (flag == "add")
		dataDirForm.newAnOther.checked = true;
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0" onLoad="setFlag()">
	<s:iterator value="sysDatadir">
		<form name="dataDirForm" focus="note" method="post">
			<input type="hidden" name="flag" value="<%=request.getParameter("flag")%>" /> 
			<input type="hidden" name="parentid" value='<s:property value="parentid" />' /> 
			<input type="hidden" name="id" value="<%=request.getParameter("id")%>" />
			<table width="90%" cellspacing="0" cellpadding="0" align="center" class="popnewdialog1">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:if test="id==null">
									新增课程类别<input type="hidden" name="control" value="add">
								</s:if>
								<s:else>
									修改课程类别<input type="hidden" name="control" value="modify">
								</s:else>
							</legend>
							<table align="center">
								<tr>
									<td width="100" align="right">
										课程类别:
									</td>
									<td colspan="3" height="17">
										<input name="courseType" type="courseType" size="30" value='<s:property value="courseType"/>' maxlength="50">
									</td>
								</tr>
								<tr>
									<td width="100" align="right">
										类别描述:
									</td>
									<td colspan="12" nowrap bgcolor="#FFFFFF">
										<div align="left">
											<textarea name="courseDesc" type="text" size="50" maxlength="500" id="courseDesc" rows="8" style="width:100%;"><s:property value="courseDesc" /></textarea>
										</div>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<br>
			<table width="90%" cellSpacing="0" cellPadding="0">
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<input type="button" name="btnSave" value='<s:text name="button.ok"/>' class="MyButton" image="../../images/share/yes1.gif" onClick="SubmitForm()">
									<input type="button" name="btnClose" value='<s:text name="button.close"/>' class="MyButton" image="../../images/share/f_closed.gif" onClick="closeModal()">
									<s:if test="id == null"> 
										<input type="checkbox" name="newAnOther" id="newAnOther" value="1">
										<label>提交后继续新增</label>
							 		</s:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</s:iterator>
	<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>