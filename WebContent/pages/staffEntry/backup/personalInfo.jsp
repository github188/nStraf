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
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="10">
	<s:if test='#request.noPersonalInfo==true'>
		<!-- 没有个人信息的，直接输出提示 -->
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td style="text-align: center;"><font style="color: red;" size="5">没有找到您的入职信息!</font></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<!-- 加入隐藏的id ,表单提交后告知后台更新哪一条数据 -->
		<input name="entryInfo.id" type="hidden"  value='<s:property value="entryInfo.id"/>' >
		<table width="100%" align="center" border="0" cellspacing="1"
			cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			<tr>
				<td align="center" width="13%" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.add_jsp.detName" />
					</div>
				</td>
				<td bgcolor="#FFFFFF" width="20%">
					<s:property value="entryInfo.detName"/>
				</td>
				<td width="13%" align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.add_jsp.userName"/> 
					</div>
				</td>
				<td bgcolor="#FFFFFF" width="20%">
					<s:property value="entryInfo.userName"/>
				</td>
				<td bgcolor="#999999" width="13%"><div align="center">
						<s:text name="staffEntry.add_jsp.groupName"/>
					</div></td>
				<td width="20%" bgcolor="#FFFFFF">
					<s:property value="entryInfo.groupName"/>
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.add_jsp.grgBeginDate"/> 
					</div>
				</td>
				<td bgcolor="#FFFFFF">
					<s:date name="entryInfo.grgBeginDate" format="yyyy-MM-dd"/>
				</td>
				<td  bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.add_jsp.regularDate"/>
					</div>
				</td>
				<td colspan="3" bgcolor="#FFFFFF">
					<s:date name="entryInfo.regularDate" format="yyyy-MM-dd"/>
				</td>
				
			</tr>
			<tr>
				<td align="center" bgcolor="#999999">
					<div align="center">
						<s:text name="staffEntry.add_jsp.note"/> <font color="#FF0000">*</font>
					</div>
				</td>
				<td bgcolor="#FFFFFF" colspan="5">
					<div>
						<s:property value="entryInfo.note"/>
					</div>
				</td>
			</tr>
			<tr>
				<td  height="67" align="center" bgcolor="#999999">
					<div align="center"><s:text name="staffEntry.add_jsp.extendStatus"/></div>
				</td>
				<td bgcolor="#FFFFFF" colspan="5" style="vertical-align: top;">
					<table id="extendStatusTable" width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
						<tr>
							<td width="22%" nowrap="nowrap" bgcolor="#A5A5A5">
								<div align="center">
									<font color="#000000">类型</font>
								</div>
							</td>
							<td width="25%" nowrap="nowrap" bgcolor="#A5A5A5">
								<div align="center">
									<font color="#000000">项目</font>
								</div>
							</td>
							<td width="23%" nowrap bgcolor="#A5A5A5">
								<div align="center">
									<font color="#000000">关联部门</font>
								</div>
							</td>
							<td width="30%" nowrap bgcolor="#A5A5A5">
								<div align="center">
									<font color="#000000">状态</font>
								</div>
							</td>
						</tr>
						<s:iterator value="checkConditionList" status="row">
		 				<tr>
		 					 <td width='22%' nowrap='nowrap' bgcolor='#FFFFFF'><s:property value="type"/></td>
		 					 <td width='25%' nowrap='nowrap' bgcolor='#FFFFFF'><s:property value="content"/></td>
		 					 <td width='23%' nowrap='nowrap' bgcolor='#FFFFFF'><s:property value="jointDept"/></td>
		 					 <td width='30%' nowrap='nowrap' bgcolor='#FFFFFF'><s:property value="note"/></td>
		        		</tr>
						</s:iterator> 
					</table>
				</td>
			</tr>
		</table>
	</s:else>
</body>
</html>
