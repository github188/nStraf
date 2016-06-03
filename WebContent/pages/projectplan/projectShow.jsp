<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="cn.grgbanking.feeltm.projectPlan.domain.*" %>

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/tabletree/css/tabletree4j.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../plugin/tabletree/TableTree4J.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<html> 
	<head></head>
	<style>
		#taskDetailField{
			font-size:12px;	
		}
		.btnDiv a{
		color:#0000FF;	
		text-decoration: none;	
		}
		.btnDiv a:hover{
		color:#CC3300;	
		text-decoration: underline;	
		}
		.items{
		color:#669999;
		font-size:14px;		
		}
		.title{
		font-size:16px;		
		font-weight:bold;
		}
		.copyrightdiv{
		font-size:12px;	
		font-family:"Arial";
		color:#C0C0C0;	
		}
		.centerClo {
			text-align:center;
		}
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
		var t1=new Date().getTime();
		var $closedGridTreeNodes="";	
		var globalGridTreeData=new Array();
		var globalGridTreeDataExtend=new Array();
		var gridTree;
		<%
		//从后台读取数据，组装成js
		List<ProjectPlanTask> taskList=(List<ProjectPlanTask>)request.getAttribute("taskList");
		if(taskList!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			for(int i=0;i<taskList.size();i++){
				ProjectPlanTask task=taskList.get(i);
				String taskName=task.getTaskName()==null?"":task.getTaskName();//任务名
				String planStartDate=task.getPlanStartDate()==null?"":sdf.format(task.getPlanStartDate());//计划开始时间
				String factStartDate=task.getFactStartDate()==null?"":sdf.format(task.getFactStartDate());//实际开始时间
				String planEndDate=task.getPlanEndDate()==null?"":sdf.format(task.getPlanEndDate());//计划结束时间
				String factEndDate=task.getFactEndDate()==null?"":sdf.format(task.getFactEndDate());//实际结束时间
				String planWorkDate=task.getPlanWorkDate()==null?"":task.getPlanWorkDate()+"";//计划工期
				String factWorkDate=task.getFactWorkDate()==null?"":task.getFactWorkDate()+"";//实际工期
				String planWorkTime=task.getPlanWorkTime()==null?"":task.getPlanWorkTime()+"";//计划工时
				String factWorkTime=task.getFactWorkTime()==null?"":task.getFactWorkTime()+"";//实际工时
				String dutyMan=task.getDutyMan()==null?"":task.getDutyMan();//负责人
				String fare=task.getFare()==null?"":task.getFare();//进度
				String premise=task.getPremise()==null?"":task.getPremise();//前置条件
				String taskDesc=task.getTaskDesc()==null?"":task.getTaskDesc();//任务描述
				String taskResource=task.getTaskResource()==null?"":task.getTaskResource();//资源
				String showOrder=task.getShowOrder()==null?"0":task.getShowOrder()+"";//显示顺序
				String id=task.getId();//id
				String parentId=task.getParentId();//父节点id
				String levelHtml="levelGroup='"+parentId+"'";
		%>
				
				globalGridTreeDataExtend[<%=i%>]={id:'<%=id%>',pid:'<%=parentId%>',showOrder:'<%=showOrder%>'};
				globalGridTreeData[<%=i%>]=new Array(
				"<span <%=levelHtml%> idGroup='taskName' tipTitle='<%=taskName%>'>"+shortShow('<%=taskName%>')+"</span>",
				"<span <%=levelHtml%> idGroup='planStartDate' styleGroup='left'><%=planStartDate%></span>",
				"<span <%=levelHtml%> idGroup='factStartDate' styleGroup='left'><%=factStartDate%></span>",
				"<span <%=levelHtml%> idGroup='planEndDate' styleGroup='left'><%=planEndDate%></span>",
				"<span <%=levelHtml%> idGroup='factEndDate' styleGroup='left'><%=factEndDate%></span>",
				"<span <%=levelHtml%> idGroup='planWorkDate' styleGroup='left'><%=planWorkDate%></span>",
				"<span <%=levelHtml%> idGroup='factWorkDate' styleGroup='left'><%=factWorkDate%></span>",
				"<span <%=levelHtml%> idGroup='planWorkTime' styleGroup='left'><%=planWorkTime%></span>",
				"<span <%=levelHtml%> idGroup='factWorkTime' styleGroup='left'><%=factWorkTime%></span>",
				"<span <%=levelHtml%> idGroup='premise' styleGroup='left'><%=premise%></span>",
				"<span <%=levelHtml%> idGroup='dutyMan' styleGroup='left'><%=dutyMan%></span>",
			    "<span <%=levelHtml%> idGroup='fare' styleGroup='left'><%=fare%></span>");
		<%
			}
		}
		%>
		
		function shortShow(str){
			if(str.trim().length>10){
				str=str.substring(0,9)+"..."
			}
			return str;
		}
		
		function showGridTree(){
			gridTree=new TableTree4J("gridTree","../../plugin/tabletree/");	
			gridTree.tableDesc="<table border=\"1\" class=\"GridView\" width=\"100%\" id=\"table1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse\"  bordercolordark=\"#C0C0C0\" bordercolorlight=\"#C0C0C0\" >";	
			var headerDataList=new Array("<span styleGroup='center'>任务名称</span>","<span styleGroup='center'>计划开始时间</span>","<span styleGroup='center'>实际开始时间</span>","<span styleGroup='center'>计划结束时间</span>","<span styleGroup='center'>实际结束时间</span>","<span styleGroup='center'>计划工期</span>","<span styleGroup='center'>实际工期</span>","<span styleGroup='center'>计划工时</span>","<span styleGroup='center'>实际工时</span>","<span styleGroup='center'>前置条件</span>","<span styleGroup='center'>负责人</span>","<span styleGroup='center'>进度</span>");
			var widthList=new Array("22%","10%","10%","10%","10%","5%","5%","5%","5%","5%","6%","5%");
	
			//参数: arrayHeader,id,headerWidthList,booleanOpen,classStyle,hrefTip,hrefStatusText,icon,iconOpen
			gridTree.setHeader(headerDataList,-1,widthList,true,"GridHead","This is a tipTitle of head href!","header status text","","");				
			//设置列样式
			gridTree.gridHeaderColStyleArray=new Array("","","","centerClo");
			gridTree.gridDataCloStyleArray=new Array("","","","centerClo");
			
			for(var index=0;index<globalGridTreeData.length;index++){
				//参数: dataList,id,pid,booleanOpen,order,url,target,hrefTip,hrefStatusText,classStyle,icon,iconOpen
				gridTree.addGirdNode(globalGridTreeData[index],globalGridTreeDataExtend[index].id,globalGridTreeDataExtend[index].pid,true,globalGridTreeDataExtend[index].showOrder);
			}
			
			
			//print	
			gridTree.printTableTreeToElement("gridTreeDiv");	
			
			//输出后，动态在table的左侧再加上一个序号列（table控件封装逻辑不太清晰，不方便直接在其内部加上td）
			var trIndex=0;
			$("#gridTreeDiv tr").each(function(){
				if(trIndex==0){
					$(this).find("td:first").before("<td style='text-algin:center;'></td>");
				}else{
					$(this).find("td:first").before("<td style='text-algin:center;'>"+trIndex+"</td>");
				}
				trIndex++;
			});
		}
	
		function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		

		$(function(){
			//显示表格树并监听
			initAndShowGridTree();
			var t2=new Date().getTime();
			//alert(t2*1-t1*1);
		});
		
		
		//统计工时日期等信息，并显示到基础信息里面
		function statisticPlanInfo(){
			calcPlanStartDate();
			calcFactStartDate();
			calcPlanEndDate();
			calcFactEndDate();
			calcPlanWorkDate();
			calcFactWorkDate();
			calcPlanWorkTime();
			calcFactWorkTime();
		}
		
		//计划开始时间 
		function calcPlanStartDate(){
			var date=findEarlistDate("planStartDate");
			//设置到基础信息中
			$("#basicPlanStartDate").val(date);
		}
		
		//实际开始时间
		function calcFactStartDate(){
			var date=findEarlistDate("factStartDate");
			//设置到基础信息中
			$("#basicFactStartDate").val(date);
		}
		
		//计划结束时间
		function calcPlanEndDate(){
			var date=findLatestDate("planEndDate");
			//设置到基础信息中
			$("#basicPlanEndDate").val(date);
		}
		
		//实际结束时间
		function calcFactEndDate(){
			var date=findLatestDate("factEndDate");
			//设置到基础信息中
			$("#basicFactEndDate").val(date);
		}
		
		//计划工期 
		function calcPlanWorkDate(){
			var date=findSumTotal("planWorkDate",true);
			//设置到基础信息中
			$("#basicPlanWorkDate").val(date);
		}
		
		//实际工期 
		function calcFactWorkDate(){
			var date=findSumTotal("factWorkDate",true);
			//设置到基础信息中
			$("#basicFactWorkDate").val(date);
		}
		
		//计划工时 
		function calcPlanWorkTime(){
			var time=findSumTotal("planWorkTime",true);
			//设置到基础信息中
			$("#basicPlanWorkTime").val(time);
		}
		
		//实际工时
		function calcFactWorkTime(){
			var time=findSumTotal("factWorkTime",true);
			//设置到基础信息中
			$("#basicFactWorkTime").val(time);
		}
		
		//最早的时间 
		function findEarlistDate(eleIdGroup){
			var tmp="9999-99-99";
			$("#gridTreeDiv").find("span[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){//只计算top节点的数据
				tmpPart=tmp.split("-");
				if($(this).html().trim()!=''){
					datePart=$(this).html().split("-");
					var change=false;
					for(var k=0;k<tmpPart.length;k++){//tmp始终取最小的日期
						if(tmpPart[k]*1>datePart[k]*1){
							change=true;
							break;
						}else if(tmpPart[k]*1<datePart[k]*1){
							change=false;
							break;
						}					
					}
					if(change){
						tmp=$(this).html();
					}
				}
			});
			if(tmp=="9999-99-99"){
				tmp="";
			}
			return tmp;
		}
		
		//最晚的时间
		function findLatestDate(eleIdGroup){
			var tmp="0000-00-00";
			$("#gridTreeDiv").find("span[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){
				tmpPart=tmp.split("-");
				if($(this).html().trim()!=''){
					datePart=$(this).html().split("-");
					var change=false;
					for(var k=0;k<tmpPart.length;k++){//tmp始终取最大的日期
						if(tmpPart[k]*1<datePart[k]*1){
							change=true;
							break;
						}else if(tmpPart[k]*1>datePart[k]*1){
							change=false;
							break;
						}					
					}
					if(change){
						tmp=$(this).html();
					}
				}
			});
			if(tmp=="0000-00-00"){
				tmp="";
			}
			return tmp;
		}
		
		//计算总和
		function findSumTotal(eleIdGroup,ignoreBlank){
			var tmp=0;
			var flag=false;
			$("#gridTreeDiv").find("span[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){
				if($(this).html().trim()!=''){
					tmp+=$(this).html()*1;
				}else if(ignoreBlank==false){//不忽略空格，表示只要有没有填数字的框，就直接返回空
					flag=true;
					return;
				}
			});
			return flag?"":tmp;
		}
		
		//显示表格树并监听
		function initAndShowGridTree(){
			showGridTree();	
	 		formatAndListening();
	 		//统计工时日期等信息，并显示到基础信息里面
			statisticPlanInfo();
		}
		
		
		//格式化并监听
		function formatAndListening(){
			//对属性styleGroup为center的span的父节点(td)，设其文本内容居中（效果：标题居中）
			$("td").find("span[styleGroup='center']").each(function(){
				$(this).parent().css("text-align","center");
			});
			
			//同理，设置表格内容居左显示
			$("td").find("span[styleGroup='left']").each(function(){
				$(this).parent().css("text-align","left");
			});
			
		
			//项目名称过长时，鼠标移上去时显示整个项目名
			$("td").find("span[idGroup='taskName']").parent().parent().mouseover(function(){
				
				var ele=$(this).find("span[idGroup='taskName']");
				
				if(ele.attr('tipTitle').length<=10){
					ele.attr('tipTitle','');
				}
				showTip(ele);
			}); 
		}
		
		function showTip(ele){
			var $liveTip = $('<div id="livetip"></div>').hide().appendTo('body');
			var tipTitle = '';
			ele.parent().parent().bind('mouseover mouseout mousemove', function(event) {
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
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/projectplan/projectplanInfo!save.action" method="post">
<table width="98%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td bgcolor="#FFFFFF">
	<!-- ********************************************* 项目信息展示 ******************************************** -->
		<table width="100%" class="input_table">
		<tr>
			<td class="input_tablehead">项目基本信息</td>
		</tr>
			<tr >
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">项目名称</font></div></td>
				<td  bgcolor="#FFFFFF" colspan="5">
					<div align="left">
						<input type="text" id="basicProjectName" name='plan.projectName' style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="100" maxlength="100" value="<s:property  value='plan.projectName'/>">
					</div>
				</td>
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">项目组别</font><font color="#FF0000">*</font></div></td>
				<td  bgcolor="#FFFFFF" colspan="5">
	      			<div align="left">
	      				<input type="text" id="basicGroupName" style="border-color:transparent;" class="input_readonly" readonly="readonly" name='plan.groupName'  value="<s:property value='plan.groupName'/>">
	      			</div>
	            </td>
			</tr>
			<tr >
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">进度</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" id="basicFare" name='plan.fare' style="border-color:transparent;" class="input_readonly" readonly="readonly" size="20" maxlength="16" value="<s:property value='plan.fare'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">项目经理</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" id="basicProjectManager" name='plan.projectManager' style="border-color:transparent;" class="input_readonly" readonly="readonly" size="20" maxlength="16" value="<s:property value='plan.projectManager'/>">
	      			</div>
	            </td>
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">计划工期</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.planWorkDate' id="basicPlanWorkDate" style="border-color:transparent;" class="input_readonly" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.planWorkDate'/>">
	      			</div>
	            </td>
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">实际工期</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.factWorkDate' id="basicFactWorkDate" style="border-color:transparent;" class="input_readonly" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.factWorkDate'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">计划工时</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.planWorkTime' id="basicPlanWorkTime"  style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='plan.planWorkTime'/>">
	      			</div>
	            </td>
			</tr>
			<tr>
	        	<td class="input_label2" width="8%"><div align="center"><font color="#000000">计划开始日期</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input id="basicPlanStartDate" name='plan.planStartDate' type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.planStartDate'/>">
	      			</div>
	            </td>
	      		<td class="input_label2" width="8%"><div align="center"><font color="#000000">实际开始日期</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.factStartDate' id="basicFactStartDate" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factStartDate'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">计划完成日期</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.planEndDate' id="basicPlanEndDate" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.planEndDate'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">实际完成日期</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.factEndDate' id="basicFactEndDate" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factEndDate'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">实际工时</font></div></td>
	            <td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" name='plan.factWorkTime' id="basicFactWorkTime" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factWorkTime'/>">
	      			</div>
	            </td>
			</tr>
	        <tr>
				<td width="8%" class="input_label2"><div align="center"><font color="#000000">项目描述</font></div></td>
				<td colspan="9"  bgcolor="#FFFFFF" >
					<div align="left">
						<textarea name='plan.projectDesc' style="text-align:left;" id="basicProjectDesc"  style="border-color:transparent;" readonly="readonly" type="text"  rows="3" cols="135" ><s:property value='plan.projectDesc'/></textarea>
					</div>
				</td>
			</tr>
        </table>
	<br>
    <!-- ********************************************* 任务细节展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%" id="taskDetailField">
		<legend><a style="font-weight:bold;">项目任务列表</a></legend>
		<br>
		<div>
			<a  href="javascript:gridTree.openAllNodes()">展开所有</a> | <a  href="javascript:gridTree.closeAllNodes()">关闭所有</a>
		</div>
		
		<div id="gridTreeDiv">
		</div>
	</fieldset>
	<br>
	
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
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

