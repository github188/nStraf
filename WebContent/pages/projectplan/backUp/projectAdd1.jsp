<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="cn.grgbanking.feeltm.projectPlan.domain.*" %>

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="stylesheet" href="../../plugin/contextmenu/css/jquery.contextmenu.css">
<link rel="StyleSheet" href="../../plugin/tabletree/css/tabletree4j.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/contextmenu/js/jquery.contextmenu.js"></script>
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
	</style>
	
	<script type="text/javascript">

		function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
			
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
	
		$(function(){
			//显示表格树
			showGridTree();
			
			//对属性styleGroup为center的span的父节点(td)，设其文本内容居中（效果：标题居中）
			$("td").find("span[styleGroup='center']").each(function(){
				$(this).parent().css("text-align","center");
			});
			
			//同理，设置表格内容居左显示
			$("td").find("span[styleGroup='left']").each(function(){
				$(this).parent().css("text-align","left");
			});
			
			
			$("td").find("span[idGroup='taskName']").parent().each(function(){
				$(this).contextPopup({
		          items: [
		            {label:'删除任务',action:function() { 
						alert($(this).find("input[idGroup='id']").val());				            	
		            }},
		            {label:'插入任务(向前)',action:function() { alert('clicked 2') } },
		            {label:'插入任务(向后)',action:function() { alert('clicked 3') } },
		            {label:'创建子任务', action:function() { alert('clicked 4') } },
		          ]
		        });				
			});
			
		});
		
	</script>
	
	
	<script language="JavaScript">
		var gridTree;	
		function showGridTree(){
		    //init
			gridTree=new TableTree4J("gridTree","../../plugin/tabletree/");	
			gridTree.tableDesc="<table border=\"1\" class=\"GridView\" width=\"100%\" id=\"table1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse\"  bordercolordark=\"#C0C0C0\" bordercolorlight=\"#C0C0C0\" >";	
			var headerDataList=new Array("<span styleGroup='center'>任务名称</span>","<span styleGroup='center'>计划开始时间</span>","<span styleGroup='center'>实际开始时间</span>","<span styleGroup='center'>计划结束时间</span>","<span styleGroup='center'>实际结束时间</span>","<span styleGroup='center'>计划工期</span>","<span styleGroup='center'>实际工期</span>","<span styleGroup='center'>计划工时</span>","<span styleGroup='center'>实际工时</span>","<span styleGroup='center'>前置条件</span>","<span styleGroup='center'>负责人</span>","<span styleGroup='center'>操作</span");
			var widthList=new Array("20%","10%","10%","10%","10%","5%","5%","5%","5%","5%","9%","5%");
	
			//参数: arrayHeader,id,headerWidthList,booleanOpen,classStyle,hrefTip,hrefStatusText,icon,iconOpen
			gridTree.setHeader(headerDataList,-1,widthList,true,"GridHead","This is a tipTitle of head href!","header status text","","");				
			//设置列样式
			gridTree.gridHeaderColStyleArray=new Array("","","","centerClo");
			gridTree.gridDataCloStyleArray=new Array("","","","centerClo");	
		   
			var dataList;
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
						String id=task.getId();//id
						String parentId=task.getParentId();//父节点id
			%>
			dataList=new Array(
			"<span idGroup='taskName'><%=taskName%></span><input idGroup='id' type='hidden' value='<%=id%>' />",
			"<span styleGroup='left'><input type='text' size='12' class='MyInput' isSel='true' isDate='true' onFocus='ShowDate(this)' dofun='ShowDate(this)' value='<%=planStartDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='12' class='MyInput' isSel='true' isDate='true' onFocus='ShowDate(this)' dofun='ShowDate(this)' value='<%=factStartDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='12' class='MyInput' isSel='true' isDate='true' onFocus='ShowDate(this)' dofun='ShowDate(this)' value='<%=planEndDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='12' class='MyInput' isSel='true' isDate='true' onFocus='ShowDate(this)' dofun='ShowDate(this)' value='<%=factEndDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='7' class='MyInput' value='<%=planWorkDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='7' class='MyInput' value='<%=factWorkDate%>' /></span>",
			"<span styleGroup='left'><input type='text' size='7' class='MyInput' value='<%=planWorkTime%>' /></span>",
			"<span styleGroup='left'><input type='text' size='7' class='MyInput' value='<%=factWorkTime%>' /></span>",
			"<span styleGroup='left'><input type='text' size='7' class='MyInput' value='<%=premise%>' /></span>",
			"<span styleGroup='left'><input type='text' size='14' class='MyInput' value='<%=dutyMan%>' /></span>",
			"<span styleGroup='center'><a href=\"#\">删除</a></span>");
			//参数: dataList,id,pid,booleanOpen,order,url,target,hrefTip,hrefStatusText,classStyle,icon,iconOpen
			gridTree.addGirdNode(dataList,<%=id%>,<%=parentId%>);
			<%
					}
				}
			%>
		
			//print	
			gridTree.printTableTreeToElement("gridTreeDiv");		
		
		}
	
	</script>	
	
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/weeklog/logInfo!save.action" method="post">
<table width="98%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td bgcolor="#FFFFFF">
	<!-- ********************************************* 项目信息展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">新增项目信息</a></legend>
		<br>
		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
			<tr >
				<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
				<td nowrap bgcolor="#FFFFFF" colspan="9">
					<div align="left">
						<input type="text"  size="140" maxlength="140" value="<s:property value='plan.projectName'/>">
					</div>
				</td>
			</tr>
			<tr >
				<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">项目经理</font><font color="#FF0000">*</font></div></td>
				<td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text"  size="15" maxlength="15" value="<s:property value='plan.projectManager'/>">
	      			</div>
	            </td>
				<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">进度</font><font color="#FF0000">*</font></div></td>
				<td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text"  size="15" maxlength="15" value="<s:property value='plan.fare'/>">
	      			</div>
	            </td>
				<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">计划工期</font></div></td>
				<td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.planWorkTime'/>">
	      			</div>
	            </td>
				<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">实际工期</font></div></td>
				<td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.factWorkTime'/>">
	      			</div>
	            </td>
	            <td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">计划工时</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text"  style="border-color:transparent;" readonly="readonly"  size="10" maxlength="10" value="<s:property value='plan.planWorkTime'/>">
	      			</div>
	            </td>
			</tr>
			<tr>
	        	<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">计划开始日期</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly" size="15" maxlength="15" value="<s:property value='plan.planStartDate'/>">
	      			</div>
	            </td>
	      		<td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">实际开始日期</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factStartDate'/>">
	      			</div>
	            </td>
	            <td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">计划完成日期</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.planEndDate'/>">
	      			</div>
	            </td>
	            <td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">实际完成日期</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factEndDate'/>">
	      			</div>
	            </td>
	            <td bgcolor="#A5A5A5" width="8%"><div align="center"><font color="#000000">实际工时</font></div></td>
	            <td nowrap bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" style="border-color:transparent;" readonly="readonly"  size="15" maxlength="15" value="<s:property value='plan.factWorkTime'/>">
	      			</div>
	            </td>
			</tr>
	        <tr>
				<td width="8%" bgcolor="#A5A5A5"><div align="center"><font color="#000000">项目描述</font></div></td>
				<td colspan="9" nowrap bgcolor="#FFFFFF" >
					<div align="left">
						<textarea  type="text"  rows="3" cols="105" ><s:property value='plan.projectDesc'/></textarea>
					</div>
				</td>
			</tr>
        </table>
	</fieldset>
	<br>
    <!-- ********************************************* 任务细节展示 ******************************************** -->
	<fieldset class="jui_fieldset" width="100%" id="taskDetailField">
		<legend><a style="font-weight:bold;">项目任务</a></legend>
		<br>
		<div id="gridTreeBtn" class="btnDiv" >
			<a  href="javascript:gridTree.openAllNodes()">展开所有</a> | 
			<a  href="javascript:gridTree.closeAllNodes()">关闭所有</a>
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
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

