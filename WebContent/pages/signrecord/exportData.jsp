<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript">
function toExport(){
	var signTime = document.getElementById("signTime").value;
	var signEndTime = document.getElementById("signEndTime").value;
	var prjname = document.getElementById("prjname").value;
	if(signTime==""){
		alert("开始日期不能为空");
		return;
	}
	if(signEndTime==""){
		alert("结束日期不能为空");
		return;
	}
	if(prjname==""){
		alert("项目名称不能为空");
		return;
	}
    var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!exportData.action?signTime="+signTime;
	actionUrl += "&signEndTime="+signEndTime+"&prjname="+prjname;
	actionUrl=encodeURI(actionUrl);
	window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
	//window.location.href=strReturn;
	setTimeout(function(){
		if(window==window.parent){
	        window.close();
		}else{
		   parent.close();  
		}
	},500);
	
}
</script>
<body>
	<s:form action="/pages/signrecord/signRecord!exportData.action" method="post" enctype="multipart/form-data">
	<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead" colspan=6>导出签到信息 </td>
						</tr>
              			<tr>
              			 	<td align="right" width="14%">开始日期:</td> 
                			<td width="18%">
                				<input name="signTime" type="text" id="signTime"  
                				class="MyInput" />
                			</td>
                			<td align="right" width="14%">结束日期:</td>
                			<td>
                				<input name="signEndTime" type="text" id="signEndTime"  
                				class="MyInput" />
                			</td>
                			<td align="right" width="14%">项目名称:</td>
                			<td style="width:30%;">
                				<select name="prjname" id="prjname" style="width:100%">
									<s:iterator value="#request.projects" id="project">
										   <option value="<s:property value='name'/>"><s:property value="name"/></option>
									</s:iterator>
								</select>
                			</td>
              			</tr>
					</table>
      <br>
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr height=50><td colspan=4 ></td></tr>
	    <tr height=2><td colspan=4 ></td></tr>
	    <tr height=10><td colspan=4 ></td></tr>
        <tr>
          <td align="center" colspan=4>
          	  <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="toExport();" image="../../images/share/yes1.gif"> 
       		 <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
       </td>
        </tr>
      </table>
    </s:form>
</body>
</html>