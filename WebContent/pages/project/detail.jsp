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
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
		/* function closeModal(){
		 	window.close();
		} */
		function toViewResourcePlan(){
			var strUrl='/pages/project/project!viewDistribution.action?projectId=<s:property value="#request.project.id"/>';
	 		var returnValue=OpenModal(strUrl,"1000,800,back.title.del,org"); 
		}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="projectForm"
		action="<%=request.getContextPath()%>/pages/project/project!add.action"
		method="post">
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">项目基本信息</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目名称<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<input type="text" style="border-color:transparent;" readonly="readonly" name="" value='<s:property value="#request.project.name"/>' maxlength="100" size="100" />
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目所在地<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="#request.project.province"/> <s:property value="#request.project.city"/>'/>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									是否完结
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%" >
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="#request.project.isEnd"/>'/>
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目类型<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="#request.project.projectType"/>'/>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									项目负责人<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="#request.project.proManager"/>'/>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									是否虚拟项目
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%" colspan="3">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:if test="#request.project.isVisual==0">否</s:if><s:else>是</s:else>'/>
							</td>
						</tr>
						<tr>
							<td width="8%" class="input_label2">
								<div align="center">
									计划开始时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.planStartTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									计划结束时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.planEndTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									项目编号
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name=""  value='<s:property value="#request.project.projectNo"/>'/>
							</td>
						</tr>
						<tr>
							
							<td width="8%" class="input_label2">
								<div align="center">
									实际开始时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.factStartTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									实际结束时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  name="" value='<s:date name="#request.project.factEndTime" format="yyyy-MM-dd"/>'/>
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									客户名称
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name=""  value='<s:property value="#request.project.customer"/>'/>
							</td>
						</tr>
						<tr>
							<td width="13%" class="input_label2">
								<div align="center">
									<br/>项目组成员<br/>
									<b><a title="查看计划人员分配" href="javascript:toViewResourcePlan();">(查看详情)</a></b><br/><br/>
								</div>
							</td>
							<td colspan="5" bgcolor="#FFFFFF" style="vertical-align: top;">
								<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" >
									<tr style="background-color:#E3E3E3;text-align:center" >
										<td width="13%"><b>姓名</b></td>
										<td width="13%"><b>项目角色</b></td>
										<td width="13%"><b>项目职责</b></td>
										<td width="13%"><b>所在部门</b></td>
										<td width="13%"><b>进入日期</b></td>
										<td width="13%"><b>退出日期</b></td>
										<td width="13%"><b>联系电话</b></td>
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
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<textarea style="border-color:transparent;width: 98%" readonly="readonly" name="project.description" id="description"  rows="3" ><s:property value="#request.project.description"/></textarea>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="input_table" cellPadding="0" cellSpacing="0" >
						<tr>
							<td class="input_tablehead">考勤地点</td>
						</tr>
						<tr bgcolor="#E6E6E6">
							<td id="attendanceContainer">
								<s:iterator value="#request.project.attendances" status="row">
								<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%" align="center"><s:property value="#row.index+1" /></td>
										<td bgcolor="#E6E6E6" >
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(进入):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  value='<s:date name="entryTime" format="HH:mm:ss"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(离开):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" style="border-color:transparent;width:90%;" readonly="readonly"  value='<s:date name="exitTime" format="HH:mm:ss"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:8%">考勤类型:</td>
													<td bgcolor="#FFFFFF" bgcolor="#FFFFFF" style="width:23%">
														<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="attendanceType"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF">考勤地点:</td>
													<td bgcolor="#FFFFFF" colspan="5">
														<input type="text" style="border-color:transparent;width:90%;" readonly="readonly" name="" value='<s:property value="signPlace"/>'/>
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
				<td align="center"><input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal();"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
