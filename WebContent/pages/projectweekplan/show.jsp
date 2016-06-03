
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.projectweekplan.domain.*"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%String defaultSelectText="------ 请选择 ------";%>
<html> 
	<head></head>
	<style>
		<!-- 项目名过长时，鼠标移上去进行提示 -->
		#livetip {
			position: absolute;
			background-color: #cfc;
			padding: 4px;
			border: 2px solid #9c9;
			border-radius: 4px;
			-webkit-border-radius: 4px;
			-moz-border-radius: 4px;
		}
	</style>
	<script type="text/javascript">
		var globalUserInProject;
	
		
		
		/* function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		} */

		
		
		function showShedualStateBackgroud(){
			var select=$("#shedualStateSelect").html();
			if(select=='项目进度正常'){
				$("#shedualStateSelect").parent().css('background-color','#9BBB59');
			}else if(select=='项目进度超前'){
				$("#shedualStateSelect").parent().css('background-color','#00CCFF');
			}else if(select=='项目进度延迟'){
				$("#shedualStateSelect").parent().css('background-color','#FF6600');
			}
		}
		$(function(){
			showShedualStateBackgroud();
			
			//监听选中的项目，更新项目中人员列表
			$("#submitProjectSelect").change(function(){
				updateUserInProject($(this).val());		
			});
			
			//监听周选择列表，更新周时间段文本框
			$("#timePeriodSelect").change(function(){
				updateWeekPeriodInput($(this).find("option:selected").attr("timePeriod"));		
			});
			
			
			$("body").find("textarea[typeGroup='tipShowContainer']").parent().mouseover(function(){
				var ele=$(this).find("textarea[typeGroup='tipShowContainer']");
				showTip(ele);
			}); 
			$("body").find("input[typeGroup='tipShowContainer']").parent().mouseover(function(){
				var ele=$(this).find("input[typeGroup='tipShowContainer']");
				showTip(ele);
			}); 
			
			
		});
		
		
		//更新选择周所在的时间段
		function updateWeekPeriodInput(text,key){
			$("#timePeriodInput").val(text);
			$("#keyTimePeriodInput").val(key);
		}
		
		//监听选中的项目，更新项目中人员列表
		function updateUserInProject(projectId){
			var actionUrl = "<%=request.getContextPath()%>/pages/projectweekplan/projectweekplanInfo!getUserInProject.action?projectId="+projectId;
			actionUrl=encodeURI(actionUrl);
			$.ajax({
				url : actionUrl,
				data : {},
				type : 'POST',
				dataType : 'json',
				cache : false,
				async : true,
				timeout : 30000,
				success : function(data) {
					
					globalUserInProject=data;
					//更新任务执行者
					$("select[name='submitTaskDoManList']").each(function(){
						var html='<option value=""><%=defaultSelectText%></option>';
						$.each(data,function(key,ele){
							html+='<option value="'+ele.userid+'">'+ele.username+'('+ele.userid+')</option>';
						});
						$(this).html(html);
					});
					
					updateCurSelectTaskDoMan();
				},
				error : function(data, data1) {
					alert("获取项目组成员错误:" + data1);
				}
			});	
		}
		
		//更新每行执行者的select
		function updateCurSelectTaskDoMan(){
			$("input[name='taskDoManKeyHiddenInput']").each(function(){
				var userKey=$(this).val();//获取该行的执行者的userkey
				$(this).parent().find("select").find("option").each(function(){
					if($(this).val()==userKey){
						$(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
			});
		}
		
		
		function showTip(ele){
			var $liveTip = $('<div id="livetip"></div>').hide().appendTo('body');
			var tipTitle = '';
			ele.parent().bind('mouseover mouseout mousemove', function(event) {
				if (!ele.attr('tipTitle')) { return; }
				if (event.type == 'mouseover' || event.type == 'mousemove') {
					$liveTip.css({
						top: event.pageY + 12,
						left: event.pageX + 12
					});
				};
				if (event.type == 'mouseover') {
					tipTitle = ele.attr('tipTitle');
					$liveTip.html('<div>' + tipTitle + '</div>').show();
				};
				if (event.type == 'mouseout') {
					$liveTip.hide();
					if (tipTitle) {
						ele.attr('tipTitle',tipTitle);
					};
				};
			});
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/projectweekplan/projectweekplanInfo!update.action" method="post">
<input type="hidden" name="plan.id" value='<s:property value="plan.id" />'>
<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td>
	
	<table width="100%" class="input_table" >
		<tr style="height:50px;text-align: center;background-color:#FFFFFF;padding-top:5px;padding-bottom:5px;" align="center">
			<td width="13%" >
				<span id="shedualStateSelect"><%=((ProjectWeekPlan)request.getAttribute("plan")).getScheduleState()%></span>
			</td>
			<td colspan="5"><font style="font-size:22px;font-weight:bold;">周计划工作任务单</font></td>
			<td width="13%">logo位置</td>
		</tr>
		<tr style="background-color:#5286B8;">
			<td width="13%" style="text-align: center;">
				<input style="width:170px;" value='<%=((ProjectWeekPlan)request.getAttribute("plan")).getCustomerName()%>' style="border-color:transparent;" readonly="readonly"/>
			</td>
			<td width="7%" align="right"><font style="font-weight: bold;color:#FFFFFF;">项目名称:</font></td>
			<td width="24%">
				<input style="width:297px" value='<%=((ProjectWeekPlan)request.getAttribute("plan")).getProjectName()%>' style="border-color:transparent;" readonly="readonly"/>
			</td>
			<td width="13%">
				<input size="23" value='<s:property value="plan.weekPeriod" />' style="border-color:transparent;" readonly="readonly"/>
			</td>
			<td width="13%">
				<input size="23" value='<s:property value="plan.weekDesc" />' style="border-color:transparent;" readonly="readonly"/>
			</td>
			<td width="7%" align="right"><font style="font-weight: bold;color:#FFFFFF;">标准时长:</font></td>
			<td width="13%" style="backgroud-color:#FFFFFF;"><input size="23"  value='<s:property value="plan.standardDuration" />' style="border-color:transparent;" readonly="readonly"/></td>
		</tr>
		<tr style="background-color:#E6E6E6;">
			<td colspan="7" style="height:25px;text-align:center;">
				<font style="font-weight:bold;font-size: 17px;">本周项目目标（项目经理填写）</font>
			</td>
		</tr>
		<tr >
			<td colspan="7">
				<table width="100%" border="0" cellPadding="1" cellSpacing="1" id="targetTable">
					<s:iterator  value="plan.targetList" id="info" status="row">
					<tr style="background-color:#FFFFFF;">
						<td width="2%;"><s:property value="#row.index+1" /></td>
						<td>
							<textarea style="border-color:transparent;width:100%;" readonly="readonly" type="text" rows="2" cols="130" ><s:property value="#info.target" /></textarea>
						</td>
					</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr style="background-color:#E6E6E6;">
			<td colspan="7" style="height:25px;text-align:center;">
				<font style="font-weight:bold;font-size: 17px;">本周项目风险（项目经理填写）</font>
			</td>
		</tr>
		<tr >
			<td colspan="7">
				<table width="100%" border="0" cellPadding="1" cellSpacing="1" id="targetTable">
					<tr style="background-color:#FFFFFF;">
						<td>
							<textarea name="plan.projectrish" type="text" rows="4" cols="134" style="width:100%;"><s:property value="plan.projectrish" /></textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr style="background-color:#E6E6E6;">
			<td colspan="7" style="height:25px;text-align:center;">
				<font style="font-weight:bold;font-size: 17px;">项目任务分解（项目经理下达）</font>
			</td>
		</tr>
		<tr>
			<td colspan="7">
				<table style="margin: -2px;" width="100%" border="0" cellPadding="0" cellSpacing="1" id="taskTable">
					<tr style="background-color:#5286B8;">
						<td width="2%" align="center" style="text-align: center" ></td>
						<td width="5%" align="center" style="text-align: center" ><font style="font-weight: bold;color:#FFFFFF;">执行人员</font></td>
						<td width="8%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">开始日期</font></td>
						<td width="8%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">结束日期</font></td>
						<td width="26%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">计划任务</font></td>
						<td width="8%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">任务完成度</font></td>
						<td width="12%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">计划花费工时(H)</font></td>
						<td width="12%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">实际花费工时(H)</font></td>
						<td width="5%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">偏差度</font></td>
						<td width="4%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">优先级</font></td>
						<td width="10%" align="center" style="text-align: center;"><font style="font-weight: bold;color:#FFFFFF;">备注</font></td>
					</tr>
					<s:iterator  value="plan.taskList" id="tranInfo" status="row">
					<tr style="background-color:#FFFFFF;">
						<td><s:property value="#row.index+1" /></td>
						<td>
							<s:property value="userName" />
						</td>
						<td><input readonly="readonly" style="border-color:transparent;" value='<s:date name="#tranInfo.startDate" format="yyyy-MM-dd"/>' style="width:100%;"/></td>
						<td><input readonly="readonly" style="border-color:transparent;" value='<s:date name="#tranInfo.endDate"  format="yyyy-MM-dd" />' style="width:100%;" /></td>
						<td><textarea readonly="readonly" typeGroup="tipShowContainer" tipTitle='<s:property value="taskContent" />' style="border-color:transparent;" rows="1" style="width:100%;"><s:property value="taskContent" /></textarea></td>
						<td><s:property value="finish" /></td>
						<td><s:property value="planWorkTime" /></td>
						<td><s:property value="factWorkTime" /></td>
						<td>
							<s:set name="deviation" value="#tranInfo.deviation" />
							<s:if test="%{#deviation.substring(0,#deviation.indexOf('%')) < 0}">
					          <s:property value="deviation" />
					        </s:if>
					        <s:elseif test="%{#deviation.substring(0,#deviation.indexOf('%')) > 50}"> 
					          <s:property value="deviation" />
					        </s:elseif>
					        <s:else>
					        	<s:property value="deviation" />
					        </s:else>
						</td>
						<td><s:property value="priority" /></td>
						<td><input readonly="readonly" typeGroup="tipShowContainer" tipTitle='<s:property value="desc" />' style="border-color:transparent;" style="width:100%;" value='<s:property value="desc" />'/></td>
					</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
	<td align="center"> 
	<br/>
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 