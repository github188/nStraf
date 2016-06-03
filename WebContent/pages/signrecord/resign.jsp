<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet"
	href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css"
	type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript"
	src="../../plugin/jqueryui/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head></head>
<script type="text/javascript">
		var buttonVal = "";//0:新增地址 1:选择已有地址
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}

		function save(){
			var areaName=$("#areaName").val();
			areaName=areaName==null?"":areaName.trim();
			var areaNameSelect=$("#areaNameSelect").val();
			areaNameSelect=areaNameSelect==null?"":areaNameSelect.trim();
			if(buttonVal=="0"){//areaname
				if(areaName==""){
					alert("请填写补签地址");
					document.getElementById("areaName").focus();
					return;
				}else{
					//document.getElementById("areaName").value=areaNameSelect;
					document.getElementById("areaName").value=areaName;
				}
			}else{
				if(areaNameSelect==""){
					alert("请填写补签地址");
					document.getElementById("areaName").focus();
					return;
				}else{
					document.getElementById("areaName").value=areaNameSelect;
				}
			}
			
			if(document.getElementById("approvePerson").value.trim()=="")
			{
				alert("请选择审核人员");
				document.getElementById("approvePerson").focus();
				return;
			}
			if(document.getElementById("signNote").value.trim()=="")
			{
				alert("请填写补签说明");
				document.getElementById("signNote").focus();
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		
		
		$(function(){
			var areaname = "<%=request.getAttribute("areanameV")%>";
			var isDeptManager = "<%=request.getAttribute("isDeptManager")%>";
			areaname=areaname=="null"?"":areaname;
			if(areaname!=""){
				document.getElementById("areaNameSelect").value=areaname;
				document.getElementById("areaName").value=areaname;
			}
			if(isDeptManager=="true"){
				$("#approveTr").hide();
			}
			//jqueryui的日期控件 
			$("#signTime").datetimepicker({  
				timeFormat: 'HH:mm:ss',
				stepHour: 1,
				stepMinute: 1,
				stepSecond: 5,
				dateFormat:'yy-mm-dd',  //更改时间显示模式  
				//showAnim:"slide",       //显示日历的效果slide、fadeIn、show等  
				changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
				changeYear:true,        //是否显示年份的下拉菜单，默认为false  
				//showWeek:true,          //是否显示星期,默认为false  
				//showButtonPanel:true,   //是否显示取消按钮，并含有current,close按钮，datePicker默认为false,datetimepicker默认为：ture  
				//closeText:'close'      //设置关闭按钮的值  
				//yearRange:'2010:2012',  //显示可供选择的年份  
				//defaultDate:+7          //表示默认日期是在当前日期加上7天  
				timeText: '时间',
				hourText: '小时',
				minuteText: '分钟',
				secondText: '秒',
				//showButtonPanel:false,
				//controlType:'select',
				closeText: 'Close'
		      });
		
		});
		//切换签到地址选择模式
		function chgAreaNameMode(){
			if($("#areaName").is(":hidden")){
				buttonVal="0";
		 		$("#chgAreaNameBtn").val("选择已有地址");
		 		$("#areaNameSelect").attr("disabled",true);
		 		$("#areaNameSelect").hide();
		 		$("#areaName").attr("disabled",false);
				$("#areaName").show(); 				
			}else {
				buttonVal="1";
		 		$("#chgAreaNameBtn").val("新增地址");
		 		$("#areaNameSelect").attr("disabled",false);
		 		$("#areaNameSelect").show();
		 		$("#areaName").attr("disabled",true);
				$("#areaName").hide(); 	
			}
		}
	</script>
<body id="bodyid" leftmargin="0" topmargin="20">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/signrecord/signRecord!saveResign.action"
		method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
					<table class="user_info_banner" height="10%">
						<tr height="20%" class="user_info_banner">
							<s:hidden name="id" id="id" />
							<td align="left" width="13%" nowrap="nowrap"><b><div
										align="left" style="padding-top: 4">
										姓名：
										<s:property value='username' />
									</div> </b>
							</td>
							<td align="left" width="7%" nowrap="nowrap"></td>
							<td align="left" width="13%" nowrap="nowrap"><b><div
										align="left" style="padding-top: 4">
										部门：
										<s:property value='deptName' />
									</div> </b>
							</td>
							<td align="left" width="7%" nowrap="nowrap"></td>
							<td align="left" width="13%" nowrap="nowrap"><b><s:if
										test="grpName.length()>0">
										<div align="right'" style="padding-top: 4">
											项目名称：
											<s:property value='grpName' />
										</div>
									</s:if> </b>
							</td>
							<td align="left" width="7%" nowrap="nowrap"></td>
							<td align="left" width="40%" nowrap="nowrap"></td>
						</tr>
					</table>
					<table class="input_table"
						style="word-break: break-all; word-wrap: break-word;">
						<tr>
							<td class="input_label2" colspan="2"><div align="right">
									<font color="#000000">补签到时间：</font>
								</div>
							</td>
							<td colspan="5" nowrap bgcolor="#FFFFFF" width="100%"><div
									align="left">

									<input name="signTime" id="signTime" size="22"
										style="text-align: left;"
										value='<s:date name="signTime" format="yyyy-MM-dd HH:mm:ss"/>'
										readonly="true" class="input_readonly" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="input_label2" colspan="2"><div align="right">
									<font color="#000000">补签到地址：</font>
								</div>
							</td>
							<td colspan="5" nowrap bgcolor="#FFFFFF" width="100%"><div
									align="left">
									<s:select list="#request.arealist"
										style="width:80%;overflow:hidden;" name="areaName"
										id="areaNameSelect">
									</s:select>
									<s:textfield style="display:none;width:80%" name="areaName"
										id="areaName" />
									<input id="chgAreaNameBtn" type="button"
										onclick="chgAreaNameMode()" value="新增地址" />
								</div>
							</td>
						</tr>
						<tr id="approveTr">
							<td class="input_label2" colspan="2"><div align="right">
									<font color="#000000">审核人员：</font>
								</div>
							</td>
							<td colspan="5" nowrap bgcolor="#FFFFFF" width="100%"><div
									align="left">
									<s:select name="approvePerson" id="approvePerson"
										list="#request.approvePerson" listKey="userid"
										listValue="username"></s:select>
								</div>
							</td>
						</tr>
						<tr>
							<td class="input_label2" colspan="2"><div align="right">
									<font color="#000000">备注说明：</font>
								</div>
							</td>
							<td colspan="5" nowrap bgcolor="#FFFFFF" width="100%"><div
									align="left">
									<s:textarea name="signNote" id="signNote" style="width:100%;"
										rows="4" cols="80" onblur="limitLen(250,this)"></s:textarea>
								</div>
							</td>
						</tr>
						<s:if test="#request.approveRecord.length()>0">
							<tr height="40%">

								<td class="input_label2" colspan="2"><div align="right">
										<font color="#000000">审核记录：</font>
									</div>
								</td>
								<td colspan="5" nowrap bgcolor="#FFFFFF" width="100%"><div
										align="left">
										<!-- <s:property value="#request.arealist" /> id="approveRecord" style="width:100%;"  rows="4" cols="80" onblur="limitLen(250,this)"/>-->
										${approveRecord }
									</div>
								</td>
							</tr>
						</s:if>
					</table></td>
			</tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>

