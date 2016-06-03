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
	function toViewResourcePlan(){
		var strUrl='/pages/project/project!viewDistribution.action?projectId=<s:property value="#request.project.id"/>';
			var returnValue=OpenModal(strUrl,"1000,800,back.title.del,org"); 
	}
	$(function(){
		$("#attendanceContainer").children("table").each(function(){
			listenCaldarInput($(this));
		});
		
		$("select[name='projectRoleList']").each(function(){
			var role=$(this).prev().val();
			$(this).find("option").each(function(){
				if($(this).val()==role){
					$(this).attr("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
		});
		
		$("select[name='projectDutyList']").each(function(){
			var role=$(this).prev().val();
			$(this).find("option").each(function(){
				if($(this).val()==role){
					$(this).attr("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
		});
	});
	
	//监听JqueryUI的Calendar控件
	function listenCaldarInput(listenTable){
		listenTable.find(".CalendarInput").datepicker({  
	        dateFormat:'yy-mm-dd',  //更改时间显示模式  
	        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
	        changeYear:true        //是否显示年份的下拉菜单，默认为false  
	     });
		
		listenTable.find(".selectuser").click(function() {
			var userids=encodeURI($(this).prev().prev().val());
			var usernames=encodeURI($(this).prev().val());
			var projectId = $("#projectId").val();
			var strUrl = "/pages/project/project!getAllUserByProject.action?projectId="+ projectId+"&userids="+userids+"&usernames="+usernames;
			var feature = "520,380,grpInfo.updateTitle,um";
		 	var returnValue = OpenModal(strUrl, feature);
			if (returnValue != null && returnValue != "") {
				var values = returnValue.split(":");
				userids="";
				usernames="";
				if(values.length>0){
					for(var j=0;j<values.length;j++){
						var idname = values[j].split(",");
						userids += ","+idname[0];
						usernames += ","+idname[1];
					}
				}
				userids= userids.substring(1);
				usernames = usernames.substring(1);
				$(this).prev().val(usernames);
				$(this).prev().prev().val(userids);
			}else if(returnValue==""){
				$(this).prev().val("");
				$(this).prev().prev().val("");
			}

		});
	}
	
	function addAgain(){
		if($("#attendanceContainer").children("table").size()==0){
			$("#attendanceContainer").children("table:last").after(getPlanResourceTable());
		}else{
			$("#attendanceContainer").children("table:last").after(getPlanResourceTable());
		}
		
		//重新对checkbox的序号编号
		renameCheckboxSpan();
		listenCaldarInput($("#attendanceContainer").children("table:last"));
	}
	function delAgain(){
		//被选中的checkbox
		var checked=0;
		$("div[name='attendanceCheckBoxDiv']").find("input[type='checkbox']").each(function(){
			 if($(this).attr('checked')==true||$(this).prop('checked')==true){
			 	checked++;
			 }
		});
		if(checked==0){
			return;
		}
		$("div[name='attendanceCheckBoxDiv']").find("input[type='checkbox']").each(function(){
			 if($(this).attr('checked')==true||$(this).prop('checked')==true){
				 $(this).parent().parent().parent().parent().remove();
			 }
		});
		//重新对checkbox的序号编号
		renameCheckboxSpan();
	}
	
	function getPlanResourceTable(){
		var html='<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">';
		html+='	<tr>';
		html+='		<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv"><input type="checkbox" /><span>1</span></div></td>';
		html+='		<td bgcolor="#E6E6E6" >';
		html+='			<table width="100%" align="center" cellPadding="1" cellSpacing="1">';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" style="width:12%" align="right">项目角色<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:22%">';
		html+='						<select name="projectRoleList" style="width:90%">';
		<s:iterator value="#request.projectRole">
		html+='<option value="<s:property value='key'/>"><s:property value="value" /></option>';
		</s:iterator>
		html+='						</select>';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">计划进入时间<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:18%">';
		html+='						<input type="text"  class="CalendarInput" name="planStartTimeList" style="width:90%" value="<s:date name="#request.project.projectResourcePlan[0].planStartTime" format="yyyy-MM-dd"/>" />';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">计划退出时间<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF">';
		html+='						<input type="text" class="CalendarInput" name="planEndTimeList" style="width:74%" value="<s:date name="#request.project.projectResourcePlan[0].planEndTime" format="yyyy-MM-dd"/>"/>';
		html+='					</td>';
		html+='				</tr>';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" style="width:12%" align="right">项目职责<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:22%">';
		html+='						<select name="projectDutyList" style="width:90%">';
		<s:iterator value="#request.projectDuty" id="ele">
		html+='<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>';
		</s:iterator>
		html+='						</select>';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">实际进入时间:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:18%">';
		html+='						<input type="text" class="CalendarInput" name="factStartTimeList" style="width:90%" value="<s:date name="#request.project.projectResourcePlan[0].factStartTime" format="yyyy-MM-dd"/>" />';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">实际退出时间:</td>';
		html+='					<td bgcolor="#FFFFFF">';
		html+='						<input type="text" class="CalendarInput" name="factEndTimeList" style="width:74%"  value="<s:date name="#request.project.projectResourcePlan[0].factEndTime" format="yyyy-MM-dd"/>"/>';
		html+='					</td>';
		html+='				</tr>';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" align="right">选择人员<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" colspan="5">';
		html+='						<input type="hidden" name="useridList" value=""/>';
		html+='						<input type="text" readonly="readonly" value="" name="usernameList" style="width:40%"/>';
		html+='						<input class="selectuser" type="button" value="选择">';
		html+='					</td>';
		html+='				</tr>';
		html+='			</table>';
		html+='		</td>';
		html+='	</tr>';
		html+='</table>';
		return html;
	}
	function renameCheckboxSpan(){
		var i=0;
		$("div[name='attendanceCheckBoxDiv']").find("span").each(function(){
			i++;
			$(this).html(i);
		});
	}
	

	/* function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	} */
	function save() {
		var names = document.getElementsByName("usernameList");
		if(names.length>0){
			if (names[names.length-1].value == "") {
				alert("人员必须选择必填");
				document.getElementById("usernamess").focus();
				return false;
			}
		}
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		projectForm.submit();
		
	}
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
									<b><a href="javascript:toViewResourcePlan();">(查看详情)</a></b><br/><br/>
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
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目角色<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:22%">
														<input type="hidden" name="projectRole" value='<s:property value="projectRole"/>'>
														<select name="projectRoleList" style="width:90%">
															<s:iterator value="#request.projectRole" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">计划进入时间<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:18%">
														<input type="text"  class="CalendarInput" name="planStartTimeList" style="width:90%" value='<s:date name="planStartTime" format="yyyy-MM-dd"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">计划退出时间<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF">
														<input type="text" class="CalendarInput" name="planEndTimeList" style="width:74%" value='<s:date name="planEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目职责<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:22%">
														<input type="hidden" name="projectDuty" value='<s:property value="projectDuty"/>'>
														<select name="projectDutyList" style="width:90%">
															<s:iterator value="#request.projectDuty" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">实际进入时间:</td>
													<td bgcolor="#FFFFFF" style="width:18%">
														<input type="text" class="CalendarInput" name="factStartTimeList" style="width:90%" value='<s:date name="factStartTime" format="yyyy-MM-dd"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">实际退出时间:</td>
													<td bgcolor="#FFFFFF">
														<input type="text" class="CalendarInput" name="factEndTimeList" style="width:74%"  value='<s:date name="factEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" align="right">选择人员<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" colspan="5">
														<input type="hidden" name="useridList" value='<s:property value="userid"/>'/>
														<input type="text" readonly="readonly" value='<s:property value="username"/>' name="usernameList" style="width:40%"/>
														<input class="selectuser" type="button" value="选择">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
								</s:iterator>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" align="center" cellPadding="1" cellSpacing="1">
									<tr>
				                        <td></td>
				                        <td width="6%"><input type="button" value="添加" onClick="addAgain()"/></td>
				                        <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br /> <br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
