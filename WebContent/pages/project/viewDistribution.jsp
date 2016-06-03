<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.lang.Integer"%>
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
<script type="text/javascript">
	
	/* function closeModal() {
		window.close();
	} */
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="projectForm"
		action="<%=request.getContextPath()%>/pages/project/project!saveUserProject.action"
		method="post">
		<input type="hidden" id="projectId" name="projectId" value="<s:property value='#request.project.id'/>">
		<table width="98%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">项目基本信息</td>
						</tr>
					</table>
					<table class="input_table">
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">
									项目名称
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="20%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name=""  value='<s:property value='#request.project.name' />'/>
							</td>
							<td width="10%" class="input_label2">
								<div align="center">
									计划开始时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="20%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.planStartTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td class="input_label2" width="10%">
								<div align="center">
									计划结束时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" >
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.planEndTime" format="yyyy-MM-dd"/>'/>
							</td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">
									负责人
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="20%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name=""  value='<s:property value='#request.project.proManager' />'/>
							</td>
							<td width="10%" class="input_label2">
								<div align="center">
									实际开始时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="20%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.factStartTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td width="10%" class="input_label2">
								<div align="center">
									实际结束时间
								</div>
							</td>
							<td bgcolor="#FFFFFF">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.factEndTime" format="yyyy-MM-dd"/>'/>
							</td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">
									<br/>项目组成员<br/>
								</div>
							</td>
							<td colspan="5" bgcolor="#FFFFFF" style="vertical-align: top;">
								<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" >
									<tr style="background-color:#E3E3E3;text-align:center" >
										<td width="13%">姓名</td>
										<td width="13%">项目角色</td>
										<td width="13%">项目职责</td>
										<td width="13%">所在部门</td>
										<td width="13%">进入日期</td>
										<td width="13%">退出日期</td>
										<td width="13%">联系电话</td>
									</tr>
									<s:iterator value="#request.project.userProjects" status="row">
										<tr>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="username"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="projectRole"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="projectDuty"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="deptName"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:date name="entryTime" format="yyyy-MM-dd"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:date name="exitTime" format="yyyy-MM-dd"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="phone"/>'/></td>
									</tr>
									</s:iterator>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>	
					<table class="input_table">
						<tr>
							<td class="input_tablehead">人员计划分配</td>
						</tr>
					</table>
					<table class="input_table">
						<tr>
							<td id="attendanceContainer">
								<s:iterator value="#request.project.projectResourcePlan" status="row">
								<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv"><input type="checkbox" /><span><s:property value="#row.index+1" /></span></div></td>
										<td bgcolor="#E6E6E6" >
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目角色:</td>
													<td bgcolor="#FFFFFF" style="width:13%">
														<input style="border-color:transparent;width:90%;" readonly="readonly"  type="text"value='<s:property value="projectRole"/>'>
													</td>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目职责:</td>
													<td bgcolor="#FFFFFF" style="width:13%">
														<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  value='<s:property value="projectDuty"/>'>
													</td>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">计划进入时间:</td>
													<td bgcolor="#FFFFFF" style="width:13%">
														<input style="border-color:transparent;width:90%;" readonly="readonly"  type="text"  value='<s:date name="planStartTime" format="yyyy-MM-dd"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">计划退出时间:</td>
													<td bgcolor="#FFFFFF">
														<input style="border-color:transparent;width:90%;" readonly="readonly"  type="text"  value='<s:date name="planEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" align="right">人员姓名:</td>
													<td bgcolor="#FFFFFF" colspan="3">
														<input type="text" style="border-color:transparent;width:40%;" readonly="readonly" value='<s:property value="username"/>'/>
													</td>
													<td bgcolor="#FFFFFF" align="right">实际进入时间:</td>
													<td bgcolor="#FFFFFF">
														<input style="border-color:transparent;width:90%;" readonly="readonly" type="text" value='<s:date name="factStartTime" format="yyyy-MM-dd"/>' />
													</td>
													<td bgcolor="#FFFFFF" align="right">实际退出时间:</td>
													<td bgcolor="#FFFFFF">
														<input style="border-color:transparent;width:74%;" readonly="readonly" type="text"  value='<s:date name="factEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
								</s:iterator>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br /> <br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
