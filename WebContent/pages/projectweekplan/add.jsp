<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@ page contentType="text/html; charset=UTF-8" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
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
	<script type="text/javascript">
		var globalUserInProject;
		
		
	
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var iTaskTotalpd = 20;		// the total number of tasks
		
		var initialTable;
		
		
		var globalCommonContainerPointer;//指向哪个控件
		var globalCommonContainerPointerType;//指向控件的类型
		//控件的值放到公共控件中显示
		function showInCommonContainer(type,ele){
			globalCommonContainerPointer=ele;
			globalCommonContainerPointerType=type;
			var str="";
			if('html'==type){
				str=$(ele).html();
			}else if('value'==type){
				str=$(ele).val();
			}
			$("#commonContainer").html(str);
		}
		//把值回填进入对应控件
		function backShowCommonContainer(){
			if(globalCommonContainerPointerType!=undefined && globalCommonContainerPointer!=undefined){
				var str=$("#commonContainer").html();
				if('html'==globalCommonContainerPointerType){
					$(globalCommonContainerPointer).html(str);
				}else if('value'==globalCommonContainerPointerType){
					$(globalCommonContainerPointer).val(str);
				}
			}
		}
		
		function changeCommonContainerCss(){
			$("#commonContainer").css("border","#f00 1px solid");
		}
		function backChangeCommonContainerCss(){
			$("#commonContainer").css("border","#7F9DB9 1px solid");
		}
		
		
		/* function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		} */
		
		function checkSubTotal(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字!");
				$(ele).focus();
			}
			calculateSumShow();
		}
		
		function save(){
			if($("#timePeriodSelect").val()==''){
				alert("请选择周别!");
				$("#timePeriodSelect").focus();
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numClear(itemName){
			itemName.select();
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Currency"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>20){
					itemName.value = 0;
					alert("请输入20以内的数字");
				}			
			}
			var itemPPtr = itemName.parentNode.parentNode.parentNode;
			for(var ind=0; ind<itemPPtr.getElementsByTagName("input").length; ind++){
				if(itemPPtr.getElementsByTagName("input")[ind].name != "subtotal"){
					var tmpInt = parseFloat(itemPPtr.getElementsByTagName("input")[ind].value);
					if(isNaN(tmpInt)){
						tmpInt = 0;
					}
					ret += tmpInt;
				}
			}
			for(var ind=0; ind<itemPPtr.parentNode.getElementsByTagName("input").length; ind++){
				if(itemPPtr.parentNode.getElementsByTagName("input")[ind].name == "subtotal"){
					itemPPtr.parentNode.getElementsByTagName("input")[ind].value = Math.round(ret*100)/100;
				}
			}
			ret = 0;
			var tmpRet = 0;
			for(var ind=0; ind<document.getElementsByName("subtotal").length; ind++){
				tmpRet = parseFloat(document.getElementsByName("subtotal")[ind].value);
				if(isNaN(tmpRet))
				{
					tmpRet = 0;
				}
				ret += tmpRet;
			}
			document.getElementById("sumShow").innerHTML = Math.round(ret*100)/100;
		}
		
		//重新给targetdiv编号
		function reBuildTargetNumDiv(){
			var i=1;
			$("span[name='targetNumDiv']").each(function(){
				$(this).html(i++);
			});
		}
		
		//重新给taskSpan编号
		function reBuildTaskNumSpan(){
			var i=1;
			$("span[name='taskNumSpan']").each(function(){
				$(this).html(i++);
			});
		}
		
		function addAgain(){
			var html='<tr style="background-color:#FFFFFF;">';
			html+='<td width="5%"><input type="checkbox" name="cbTarget" typeGroup="cbTarget"/><span name="targetNumDiv"></span></td>';
			html+='<td><textarea name="submitTargetList" type="text" rows="2" cols="134" ></textarea>';
			html+='</td></tr>';
			if($("#targetTable").find("tr").length==0){
				$("#targetTable").html(html);
			}else{
				$("#targetTable").find("tr:last").after(html);
			}
			reBuildTargetNumDiv();
		}
		
		function delAgain(){
			var checkedItems = $('input[type="checkbox"][typeGroup="cbTarget"]:checked').length;
			if(checkedItems == 0){
				alert("请选择一条记录")
				return;
			}
			if(confirm("确认选择的删除吗?")){
				$('input[type="checkbox"][typeGroup="cbTarget"]').each(function(){
					if($(this).attr('checked')==true||$(this).prop('checked')==true){
						$(this).parent().parent().remove();
					}
				});
			}
			reBuildTargetNumDiv();
		}
		
		function addTaskAgain(){
			var selectHtml='<option value=""><%=defaultSelectText%></option>';
			if(globalUserInProject!=undefined){
				$.each(globalUserInProject,function(key,ele){
					selectHtml+='<option value="'+ele.userid+'">'+ele.username+'('+ele.userid+')</option>';
				});
				$(this).html(html);
			}
			
			var html='<tr style="background-color:#FFFFFF;">';
			html+='<td width="5%"><input type="checkbox" name="cbTask" typeGroup="cbTask"/><span name="taskNumSpan"></span></td>';
			html+='<td width="9%">';
			html+='<select typeGroup="taskDoMan" name="submitTaskDoManList">';
			html+=selectHtml;
			html+='</select></td>';
			html+='<td width="9%"><input typeGroup="dateInput" size="11" style="width:100%;" name="submitTaskStartDateList"/></td>';
			html+='<td width="9%"><input typeGroup="dateInput" size="11" style="width:100%;" name="submitTaskEndDateList" /></td>';
			html+='<td width="33%"><textarea onblur="backChangeCommonContainerCss()" onfocus="showInCommonContainer(\'html\',this);changeCommonContainerCss()" onchange="showInCommonContainer(\'html\',this)" onkeyup="showInCommonContainer(\'html\',this)" cols="45" rows="1" name="submitTaskContentList"></textarea></td>';
			html+='<td width="10%"><input type="text" maxLength=5 onKeyUp="workhourCheck(this)" size="10" style="width:100%;" name="submitTaskPlanWorkTimeList" /></td>';
			html+='<td width="10%"><input type="text" maxLength=5 onKeyUp="workhourCheck(this)" size="10" style="width:100%;" name="submitTaskFactWorkTimeList" /></td>';
			html+='<td width="5%">';
			html+='<select typeGroup="prioritySelect" name="submitPrioritySelectList">';
			html+='<option value="高">高</option><option value="中">中</option><option value="低">低</option>';
			html+='</select></td>';
			html+='<td width="10%"><input onblur="backChangeCommonContainerCss()" onfocus="showInCommonContainer(\'value\',this);changeCommonContainerCss()" onchange="showInCommonContainer(\'value\',this)" onkeyup="showInCommonContainer(\'value\',this)" size="25" name="submitTaskDescList" /></td>';
			html+='</tr>';
			if($("#taskTable").find("tr").length==0){
				$("#taskTable").html(html);
			}else{
				$("#taskTable").find("tr:last").before(html);
			}
			reBuildTaskNumSpan();
			dateInputListener();
		}
		
		function delTaskAgain(){
			var checkedItems = $('input[type="checkbox"][typeGroup="cbTask"]:checked').length;
			if(checkedItems == 0){
				alert("请选择一条记录")
				return;
			}
			if(confirm("确认选择的删除吗?")){
				$('input[type="checkbox"][typeGroup="cbTask"]').each(function(){
					if($(this).attr('checked')==true||$(this).prop('checked')==true){
						$(this).parent().parent().remove();
					}
				});
			}
			reBuildTaskNumSpan();
			dateInputListener();
		}
		
		function selAgain(){
			var taskDivPPtr = document.getElementsByTagName("fieldset")[0];
			for(var ind=0; ind<taskDivPPtr.getElementsByTagName("input").length; ind++){
				if(taskDivPPtr.getElementsByTagName("input")[ind].type.toLowerCase() =="checkbox"){
					taskDivPPtr.getElementsByTagName("input")[ind].checked=selAllFlag;
				}
			}
			selAllFlag = !selAllFlag;
		}
		
		
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				{
					var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
					if(tmpStr.length > 250)
					{
						alert("你输入的字数超过250个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,250);
					}
				}
			} 
		} 
		
		function listenKey(){ 
			if (document.addEventListener) { 
				document.addEventListener("keyup",getKey,false); 
			} else if (document.attachEvent) { 
				document.attachEvent("onkeyup",getKey); 
			} else { 
				document.onkeyup = getKey; 
			} 
		} 
		listenKey();
		
		$(function(){
			$("#shedualStateSelect").change(function(){
				var select=$(this).find("option:selected").val();
				if(select=='项目进度正常'){
					$(this).parent().css('background-color','#9BBB59');
				}else if(select=='项目进度超前'){
					$(this).parent().css('background-color','#00CCFF');
				}else if(select=='项目进度延迟'){
					$(this).parent().css('background-color','#FF6600');
				}
			});
			
			//监听选中的项目，更新项目中人员列表
			updateUserInProject($("#submitProjectSelect").find("option:selected").val());	
			$("#submitProjectSelect").change(function(){
				updateUserInProject($(this).val());		
			});
			
			//监听周选择列表，更新周时间段文本框
			$("#timePeriodSelect").change(function(){
				updateWeekPeriodInput($(this).find("option:selected").attr("timePeriod"),$(this).find("option:selected").attr("keyTimePeriod"));		
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
				},
				error : function(data, data1) {
					alert("获取项目组成员错误:" + data1);
				}
			});	
		}
		
		
		$(function(){
			dateInputListener();
		})
		
		
		function dateInputListener(){
			$("input[typeGroup='dateInput']").datepicker({  
	            dateFormat:'yy-mm-dd',  //更改时间显示模式  
	            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
	            changeYear:true        //是否显示年份的下拉菜单，默认为false  
	        });
			$("input[typeGroup='dateInput']").change(function(){
				calculatePlanTime($(this));
			});
		}
		
		//计算计划工时
		function calculatePlanTime(ele){
			//$("input[name='submitTaskStartDateList']").each(function(){
			$(ele).parent().parent().find("input[name='submitTaskStartDateList']").each(function(){
				var startTime=$(this).val();
				var endTime=$(this).parent().parent().find("input[name='submitTaskEndDateList']").val();
				var planTimeEle=$(this).parent().parent().find("input[name='submitTaskPlanWorkTimeList']");
				
				//计算时间差
				var start = new Date(startTime.replace("-","/")); 
				var end = new Date(endTime.replace("-","/")); 
				var diff = parseInt((end - start) / (1000*60*60*24)); 
				
				if(!isNaN(diff)){
					planTimeEle.val((diff+1)*8);
				}
			});
		}
		/* 工时的输入校验 */
		function workhourCheck(obj){
			obj.value=obj.value.replace(/[^0-9\.]+/,'');
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/projectweekplan/projectweekplanInfo!save.action" method="post">
<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td>

	<table width="100%" class="input_table" >
		<tr style="height:50px;text-align: center;background-color:#FFFFFF;padding-top:5px;padding-bottom:5px;" align="center">
			<td width="13%" style='background-color:#9BBB59;vertical-align:top;'>
				<select name='plan.scheduleState' id="shedualStateSelect" style="width:160px;font-size:15px;">
					<option value='项目进度正常'>项目进度正常</option>
					<option value='项目进度超前'>项目进度超前</option>
					<option value='项目进度延迟'>项目进度延迟</option>
				</select>
			</td>
			<td colspan="5"><font style="font-size:22px;font-weight:bold;">周计划工作任务单</font></td>
			<td width="13%">logo位置</td>
		</tr>
		<tr style="background-color:#5286B8;">
			<td width="13%" style="">
				<select id="customerKeySelect" name="plan.customerKey" style="width:160px" >
					<s:iterator  value="#request.customerList" id="ele" status="row">
					<option value='<s:property value="key" />'><s:property value="val" /></option>
					</s:iterator>
				</select>
			</td>
			<td width="7%" align="right"><font style="font-weight: bold;color:#FFFFFF;">项目名称:</font></td>
			<td width="24%">
				<select id="submitProjectSelect" name="plan.projectId" style="width:297px">
					<s:iterator value="#request.projectList" id="tranInfo" status="row">
    					<option value='<s:property value="id"/>'><s:property value="name"/></option>
    				</s:iterator>
				</select>
			</td>
			<td width="13%">
				<select id="timePeriodSelect" name="plan.weekDesc" style="width:170px">
					<s:iterator  value="#request.weekList" id="ele" status="row">
					<option <s:if test="selected==true">selected</s:if> keyTimePeriod='<s:property value="key" />' timePeriod='<s:property value="timePeriod" />' value='<s:property value="desc" />'><s:property value="desc" /></option>
					</s:iterator>
				</select>
			</td>
			<td width="13%" style="text-align: left;">
				<input name="plan.weekPeriod" id="timePeriodInput" style="border-color:transparent;height:20px;" readonly="readonly" size="23"/>
				<input name="submitKeyTimePeriod" id="keyTimePeriodInput" type="hidden" />
				<script type="text/javascript">
				updateWeekPeriodInput($("#timePeriodSelect").find("option:selected").attr("timePeriod"),$("#timePeriodSelect").find("option:selected").attr("keyTimePeriod"));		
				</script>
			</td>
			<td width="7%" align="right"><font style="font-weight: bold;color:#FFFFFF;">标准时长(H):</font></td>
			<td width="13%" style="backgroud-color:#FFFFFF;"><input type="text" maxLength=5 onKeyUp="workhourCheck(this)" name="plan.standardDuration" id="standardTime" size="23" value="40" /></td>
		</tr>
		<tr style="background-color:#E6E6E6;">
			<td colspan="7" style="height:25px;text-align:center;">
				<font style="font-weight:bold;font-size: 17px;">本周项目目标（项目经理填写）</font>
			</td>
		</tr>
		<tr >
			<td colspan="7">
				<table width="100%" border="0" cellPadding="1" cellSpacing="1" id="targetTable">
					<tr style="background-color:#FFFFFF;">
						<td width="5%"><input type="checkbox" name="cbTarget" typeGroup="cbTarget"/><span name="targetNumDiv" style="text-align:center">1</span></td>
						<td>
							<textarea name="submitTargetList" type="text" rows="2" cols="134" style="width:100%;"></textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr style="background-color:#FFFFFF;">
			<td colspan="7" style="text-align:right; text-align: right;">
            	<input type="button" value="新增" onClick="addAgain()"/>
           		<input type="button" value="删除" onClick="delAgain()"/>
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
							<textarea name="plan.projectrish" type="text" rows="4" cols="134" style="width:100%;"></textarea>
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
			<td colspan="7" style="height:30px;text-align:center;">
				<textarea id="commonContainer" onblur="backShowCommonContainer()" onkeyup="backShowCommonContainer()" onchange="backShowCommonContainer()" rows="2" cols="139" style="width:100%;"></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="7">
				<table style="margin: -2px;" width="100%" border="0" cellPadding="0" cellSpacing="1" id="taskTable">
					<tr style="background-color:#5286B8;">
						<td width="5%" align="center" style="text-align: center" ></td>
						<td width="9%" align="center" style="text-align: center" ><font style="font-weight: bold;color:#FFFFFF;">执行人员</font></td>
						<td width="9%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">开始日期</font></td>
						<td width="9%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">结束日期</font></td>
						<td width="33%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">工作内容</font></td>
						<td width="10%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">计划工时(H)</font></td>
						<td width="10%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">确认工时(H)</font></td>
						<td width="5%" align="center" style="text-align: center"><font style="font-weight: bold;color:#FFFFFF;">优先级</font></td>
						<td width="10%" align="center" style="text-align: center;"><font style="font-weight: bold;color:#FFFFFF;">备注</font></td>
					</tr>
					<tr style="background-color:#FFFFFF;">
						<td  width="5%" ><input type="checkbox" name="cbTask" typeGroup="cbTask"/><span name="taskNumSpan">1</span></td>
						<td  width="9%" align="center" style="text-align: center">
							<select typeGroup="taskDoMan" name="submitTaskDoManList">
								<option><%=defaultSelectText%></option>
							</select>
						</td>
						<td width="9%"><input typeGroup='dateInput' size="11" name="submitTaskStartDateList" style="width:100%;"/></td>
						<td width="9%"><input typeGroup='dateInput' size="11" name="submitTaskEndDateList"  style="width:100%;"/></td>
						<td width="33%"><textarea onblur="backChangeCommonContainerCss()" onfocus="showInCommonContainer('html',this);changeCommonContainerCss()" onchange="showInCommonContainer('html',this)" onkeyup="showInCommonContainer('html',this)" cols="45" rows="1" name="submitTaskContentList"></textarea></td>
						<td width="10%"><input type="text" maxLength=5 onKeyUp="workhourCheck(this)" size="10" name="submitTaskPlanWorkTimeList" style="width:100%;"/></td>
						<td width="10%"><input type="text" maxLength=5 onKeyUp="workhourCheck(this)" size="10" name="submitTaskFactWorkTimeList" style="width:100%;"/></td>
						<td width="5%">
							<select typeGroup="prioritySelect" name="submitPrioritySelectList">
								<option value="高">高</option>
								<option value="中">中</option>
								<option value="低">低</option>
							</select>
						</td>
						<td width="10%"><input onblur="backChangeCommonContainerCss()" onfocus="showInCommonContainer('value',this);changeCommonContainerCss()" onchange="showInCommonContainer('value',this)" onkeyup="showInCommonContainer('value',this)" size="25" name="submitTaskDescList" /></td>
					</tr>
					<tr style="background-color:#FFFFFF;">
						<td colspan="9" style="text-align: right;">
                        	<input type="button" value="新增" onClick="addTaskAgain()"/>
                        	<input type="button" value="删除" onClick="delTaskAgain()"/>
						</td>
					</tr>
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
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 