<!--20100107 14:00-->
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
<%String ctxPath= "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="entryInfoForm" action="<%=request.getContextPath()%>/pages/staffEntry/entryInfo!save.action" method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>

						<table id="extendStatusTable" width="100%" class="input_table">
							<tr>
								<td class="input_tablehead">其他检查字段</td>
							</tr>
							<tr>
								<td width="22%" class="column_label">
									<div align="center">
										<font color="#000000">类型</font><font color="#FF0000">*</font>
									</div>
								</td>
								<td width="25%" class="column_label">
									<div align="center">
										<font color="#000000">项目</font><font color="#FF0000">*</font>
									</div>
								</td>
								<td width="23%" class="column_label">
									<div align="center">
										<font color="#000000">关联部门</font><font color="#FF0000">*</font>
									</div>
								</td>
								<td width="30%" class="column_label">
									<div align="center">
										<font color="#000000">状态</font><font color="#FF0000">*</font>
									</div>
								</td>
							</tr>
							<s:iterator value="#request.conditionList" status="row">
			 				<tr>
			 					 <td width='22%'  bgcolor='#FFFFFF'><s:property value="type"/></td>
			 					 <td width='25%'  bgcolor='#FFFFFF'><s:property value="content"/></td>
			 					 <td width='23%' bgcolor='#FFFFFF'><s:property value="jointDept"/></td>
			 					 <td width='30%' bgcolor='#FFFFFF'><s:property value="note"/></td>
			        		</tr>
							</s:iterator> 
						</table>
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
