<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="cn.grgbanking.feeltm.projectPlan.domain.*" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/tabletree/css/tabletree4j.css" type="text/css" />
<link rel="StyleSheet" href="../../plugin/contextmenu/css/myContextMenu.css" type="text/css" />
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../plugin/tabletree/TableTree4J.js"></script>
<script type="text/javascript" src="../../plugin/contextmenu/script/myContextMenu.js"></script>

<script type="text/javascript" src="../../plugin/util/floatDiv.js"></script>

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
		<!-- 右键菜单背景图片 -->
		.div_RightMenu ul li{background-image:url(../../plugin/contextmenu/images/t.gif);}
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
		//处于关闭状态的节点(点击节点时，会对这个全局变量进行操作，操作逻辑在TableTree4J.js中的clickNode方法中)
		var $closedGridTreeNodes="";
		
		function s4() {
		  return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
		};
		function randomInt(start, end){
		    return Math.floor(Math.random() * (end - start) + start);
		}
		
		function generateUUID(){
			return s4() + s4() + s4() +s4() + s4() + s4() + s4() + s4();
			//var timeStmap=new Date().getTime();
			//return (timeStmap+""+randomInt(1,9999))*1;
		}
	</script>
	
	<script type="text/javascript">
		var globalGridTreeData=new Array();
		var globalGridTreeDataExtend=new Array();
		var globalIdToIndex=new Array();//记录id和上面两个全局变量的下标关系
		var tmpTaskId;
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
				//tmpTaskId=generateUUID();
				tmpTaskId='<%=id%>';//这里不能用新生成的随机数，否则生成的表格树某个parentId记录的值找不到对应id的节点，出现只能看到第一级节点的现象
				
				globalGridTreeDataExtend[<%=i%>]={id:tmpTaskId,pid:'<%=parentId%>',showOrder:'<%=showOrder%>'};
				globalGridTreeData[<%=i%>]=new Array(
				"<span idGroup='taskName' ><input type='text' <%=levelHtml%> size='32' name='submitTaskName' maxlength='100' idGroup='taskName'  value='<%=taskName%>' /></span><input <%=levelHtml%> name='submitId' idGroup='id' type='hidden' value='"+tmpTaskId+"' /> <input <%=levelHtml%> name='submitParentId' idGroup='parentId' type='hidden' value='<%=parentId%>' /> <input <%=levelHtml%> name='submitShowOrder' idGroup='showOrder' type='hidden' value='<%=showOrder%>' />",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='13' name='submitPlanStartDate' idGroup='planStartDate'   value='<%=planStartDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='13' name='submitFactStartDate' idGroup='factStartDate'  value='<%=factStartDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='13' name='submitPlanEndDate' idGroup='planEndDate'  value='<%=planEndDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='13' name='submitFactEndDate' idGroup='factEndDate'  value='<%=factEndDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitPlanWorkDate' idGroup='planWorkDate' onBlur='checkPlanWorkDate(this)' value='<%=planWorkDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitFactWorkDate' idGroup='factWorkDate' onBlur='checkFactWorkDate(this)'  value='<%=factWorkDate%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitPlanWorkTime' idGroup='planWorkTime' onBlur='checkPlanWorkTime(this)'  value='<%=planWorkTime%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitFactWorkTime' idGroup='factWorkTime' onBlur='checkFactWorkTime(this)'  value='<%=factWorkTime%>' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitPremise' idGroup='premise' class='MyInput' value='<%=premise%>' maxlength='15' /></span>",
				"<span styleGroup='left'><input type='text' <%=levelHtml%> size='13' name='submitDutyMan' idGroup='dutyMan' class='MyInput' value='<%=dutyMan%>' maxlength='15' /></span>",
			    "<span styleGroup='left'><input type='text' <%=levelHtml%> size='7' name='submitFare' idGroup='fare' class='MyInput' value='<%=fare%>' maxlength='15' /></span>");
		<%
			}
		}
		%>
	</script>	
	
	<script type="text/javascript">
		var gridTree;
		function showGridTree(){
			gridTree=new TableTree4J("gridTree","../../plugin/tabletree/");	
			gridTree.tableDesc="<table border=\"1\" class=\"GridView\" width=\"100%\" id=\"table1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: collapse\"  bordercolordark=\"#C0C0C0\" bordercolorlight=\"#C0C0C0\" >";	
			var headerDataList=new Array("<span styleGroup='center'>任务名称</span>","<span styleGroup='center'>计划开始时间</span>","<span styleGroup='center'>实际开始时间</span>","<span styleGroup='center'>计划结束时间</span>","<span styleGroup='center'>实际结束时间</span>","<span styleGroup='center'>计划工期</span>","<span styleGroup='center'>实际工期</span>","<span styleGroup='center'>计划工时</span>","<span styleGroup='center'>实际工时</span>","<span styleGroup='center'>前置条件</span>","<span styleGroup='center'>负责人</span>","<span styleGroup='center'>进度</span>");
			var widthList=new Array("28%","8%","8%","8%","8%","5%","5%","5%","5%","5%","8%","5%");
	
			//参数: arrayHeader,id,headerWidthList,booleanOpen,classStyle,hrefTip,hrefStatusText,icon,iconOpen
			gridTree.setHeader(headerDataList,-1,widthList,true,"GridHead","This is a tipTitle of head href!","header status text","","");				
			//设置列样式
			gridTree.gridHeaderColStyleArray=new Array("","","","centerClo");
			gridTree.gridDataCloStyleArray=new Array("","","","centerClo");
			
			if(globalGridTreeData.length<=0){//表格树中没有数据，则新建一行供填写
				//插入新节点
				var taskId=generateUUID();
		 		globalGridTreeData[0]=generateNewGridTreeRow(false,null,taskId,-1,1);//taskId传入，用于初始化hidden类型的id	
		 		//插入的节点的pid为当前点击节点id
		 		globalGridTreeDataExtend[0]={id:taskId,pid:-1,showOrder:1};
			}
			
			for(var index=0;index<globalGridTreeData.length;index++){
				
				var open=!isNodeColsed(globalGridTreeDataExtend[index].id);
				//参数: dataList,id,pid,booleanOpen,order,url,target,hrefTip,hrefStatusText,classStyle,icon,iconOpen
				gridTree.addGirdNode(globalGridTreeData[index],globalGridTreeDataExtend[index].id,globalGridTreeDataExtend[index].pid,open,globalGridTreeDataExtend[index].showOrder);
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
			
			//计算div的大小，太大的话，则用滚动条
			if($("#gridTreeDiv").height()>400){
				$("#gridTreeDiv").height(400);
			}
		}
		
		//判断节点是否处于关闭状态
		function isNodeColsed(nodeId){
			var ids=$closedGridTreeNodes.split(",");
			for(var i=0;i<ids.length;i++){
				if(ids[i]==nodeId){
					return true;
				}
			}
			return false;
		}
	
		//右键时点击的任务
		var currentClickedTask;
		
		function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		
		function ajaxRequest(actionUrl){
			$.ajax({
				url: actionUrl,
				data:$("form[name='reportInfoForm']").serialize(),
				type:'POST',
				dataType:'json',
				cache: false,
				async:true,
				timeout: 600000,
				success:function(data){
					if(data+''=='true'){
						alert("保存成功，点击关闭页面!");
						window.close();
					}
				},
				error:function(data,data1){
					alert("保存出错了,错误原因:"+data1);
				}
			});
		} 
		
		function save(){
			
			if(!validateSubmit()){
				return false;
			}
			
			//构造一次html并showTree一次，使全局变量中的属性及时更新到要提交的表单html中
			reBuildGridTreeData();
			showGridTree();
			
			//统计到项目基础信息中
			statisticPlanInfo();
			ajaxRequest("/nStraf/pages/projectplan/projectplanInfo!update.action");
			//reportInfoForm.submit();
		}
		
		function validateSubmit(){
			if($("#basicProjectName").val().trim()=='' || $("#basicProjectName").val().indexOf('--')==0){
				alert('请选择项目名称!');
				return false;
			}
			if($("#basicGroupName").val().trim()=='' || $("#basicGroupName").val().indexOf('--')==0){
				alert('请选择项目组别!');
				return false;
			}
			if($("#basicProjectManager").val().trim()==''){
				alert('请输入项目经理姓名!');
				$("#basicProjectManager").focus();
				return false;
			}
			if($("#basicFare").val().trim()==''){
				alert('请输入项目整体进度!');
				$("#basicFare").focus();
				return false;
			}
			if($("#basicProjectDesc").html().length>1000){
				alert('项目描述过长，请限制在1000个字符之类!');
				$("#basicProjectDesc").focus();
				return false;
			}
			if(!confirm("提交过程可能有点慢，请稍等待，确认提交么?")){
				return false;
			}
			return true;
		}
		
	
		$(function(){
			//显示表格树并监听
			initAndShowGridTree();
			
			//设置默认选中值
			$("#prjName").find("option").each(function(){
				if($(this).text().trim()=='<%=((ProjectPlan)request.getAttribute("plan")).getProjectName()%>'){
					$(this).attr('selected','selected');
				}
			});
			$("#gpName").find("option").each(function(){
				if($(this).attr('value')=='<%=((ProjectPlan)request.getAttribute("plan")).getGroupName()%>'){
					$(this).attr('selected','selected');
				}
			});
			
			
			//监听基本信息中项目名称和项目组
			$("#prjName").change(function(){
				$("#basicProjectName").val($("#prjName").find("option:selected").text().trim());
			});
			
			$("#gpName").change(function(){
				$("#basicGroupName").val($("#gpName").find("option:selected").text().trim());
			});
			
			
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
			$("#gridTreeDiv").find("input[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){//只计算top节点的数据
				tmpPart=tmp.split("-");
				if($(this).val().trim()!=''){
					datePart=$(this).val().split("-");
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
						tmp=$(this).val();
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
			$("#gridTreeDiv").find("input[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){
				tmpPart=tmp.split("-");
				if($(this).val().trim()!=''){
					datePart=$(this).val().split("-");
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
						tmp=$(this).val();
					}
				}
			});
			if(tmp=="0000-00-00"){
				tmp="";
			}
			return tmp;
		}
		
		function checkPrjDesc(ele){
			if($(ele).html().length>1000){
				$(ele).html($(ele).html().substr(0,999));
				//$(ele).focus();
			}
		}
		
		function checkPlanWorkDate(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字");
				$(ele).focus();
			}			
			statisticPlanInfo();
		}
		
		function checkFactWorkDate(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字");
				$(ele).focus();
			}	
			statisticPlanInfo();
		}
		
		function checkPlanWorkTime(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字");
				$(ele).focus();
			}		
		}
		
		function checkFactWorkTime(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字");
				$(ele).focus();
			}	
			statisticPlanInfo();
		}
		
		function stripScriptChar(s,ch,replaceStr){
			var rs=s.replace(new RegExp(ch,"g"),replaceStr);
			return rs; 
		}
		
		//计算总和
		function findSumTotal(eleIdGroup,ignoreBlank){
			var tmp=0;
			var flag=false;
			$("#gridTreeDiv").find("input[idGroup='"+eleIdGroup+"'][levelGroup='-1']").each(function(){
				if($(this).val().trim()!=''){
					tmp+=$(this).val()*1;
				}else if(ignoreBlank==false){//不忽略空格，表示只要有没有填数字的框，就直接返回空
					flag=true;
					return;
				}
			});
			return flag?"":tmp;
		}
		
		//显示表格树并监听
		function initAndShowGridTree(){
			var test1=new Date().getTime();
			showGridTree();	
			var test2=new Date().getTime();
	 		formatAndListening();
	 		var test3=new Date().getTime();
	 		//alert((test2-test1)+","+(test3-test2));
	 		setInterval(function(){
				//统计工时日期等信息，并显示到基础信息里面
				statisticPlanInfo();
			},5*1000);
		}
		
		
		//格式化并监听
		function formatAndListening(){
			var t1=new Date().getTime();
			
			//对属性styleGroup为center的span的父节点(td)，设其文本内容居中（效果：标题居中）
			$("td").find("span[styleGroup='center']").each(function(){
				$(this).parent().css("text-align","center");
			});
			
			var p3=new Date().getTime();
			
			$("td").find("input[idGroup='taskName']").each(function(){
				var tdEle=$(this).parent().parent();
				tdEle.RightMenu('myMenu2',{
					   menuList:[{menuName:"插入任务(向后)",menuclass:"9",clickEvent: "onMenuClick('addAfter')"},
					             {menuName:"插入任务(向前)",menuclass:"1",clickEvent:"onMenuClick('addBefore')"},
					             {menuName:"添加子任务",menuclass:"16",clickEvent: "onMenuClick('addSon')"},
					             {menuName:"删除任务",menuclass:"8",clickEvent: "onMenuClick('del')"}
				       ]
				});
				tdEle.mousedown(function(event, a){
			        if(event.which == 3 || a == 'right'){
			        	reBuildGridTreeData();
			        	//对任务名点击右键时，用全局变量记录其td，然后从改td中找出id等相关信息
			        	currentClickedTask=$(this);
			            return true;
			        }
			    });
				
				//监听四个日期输入框
				tdEle.next().find("input").datepicker({ dateFormat: 'yy-mm-dd',changeMonth: true,changeYear: true});
				tdEle.next().next().find("input").datepicker({ dateFormat: 'yy-mm-dd',changeMonth: true,changeYear: true });
				tdEle.next().next().next().find("input").datepicker({ dateFormat: 'yy-mm-dd',changeMonth: true,changeYear: true });
				tdEle.next().next().next().next().find("input").datepicker({ dateFormat: 'yy-mm-dd',changeMonth: true,changeYear: true });
				
			});
			
			var whole=new Date().getTime();
			
			//alert("whole:"+(whole-p3)+",p3:"+(p3-t1));
		}
		
		//去掉特殊符号
		function stripScript(s){ 
			/* var tmp=stripScriptChar(s,'"','&quot;');
			tmp=stripScriptChar(tmp,"'","&#039;");
			tmp=stripScriptChar(tmp,"<","&#60;");
			tmp=stripScriptChar(tmp,">","&#62;"); */
			var tmp=stripScriptChar(s,'"','');
			tmp=stripScriptChar(tmp,"'","");
			tmp=stripScriptChar(tmp,"<","");
			tmp=stripScriptChar(tmp,">","");
			return tmp;
		}
		
		function stripScriptChar(s,ch,replaceStr){
			var rs=s.replace(new RegExp(ch,"g"),replaceStr);
			return rs; 
		}
		
		//子节点的数目
		function getSonNodeNum(nodeId){
			
			if(nodeId=='-1'){//-1表示根节点，所有的节点都是其子节点
				return globalGridTreeDataExtend.length;
			}
			var num=0;
			for(var i=0;i<globalGridTreeDataExtend.length;i++){
				if(globalGridTreeDataExtend[i].pid+''==nodeId+''){
					num++;
					num=num+getSonNodeNum(globalGridTreeDataExtend[i].id)*1;
					//alert('parentId:'+nodeId+",id:"+globalGridTreeDataExtend[i].id+",num:"+num);
				}else if(i>=globalGridTreeDataExtend.length-1){//找到最后一个节点都没找到pid与传入的nodeId一致的节点，说明此节点为叶子
					return num;
				}
			}
			return num;
		}
		
		//递归删除
		function recursiveDel(nodeId){
			//获取子节点的id连接字符串
			var ids=getRecursiveId(nodeId);
			ids=nodeId+ids;//自身也需要删除
			
			var deleteIds=ids.split(",");
			var newDatas=new Array();
			var newDataExtends=new Array();
			var index=0;			
			for (var i=0;i<globalGridTreeDataExtend.length;i++){
				var notInDeleteIds=true;//查看当前数据是否在待删除的数组中
				for(var j=0;j<deleteIds.length;j++){
					if(globalGridTreeDataExtend[i].id==deleteIds[j]){
						notInDeleteIds=false;
					}
				}
				if(notInDeleteIds){//在待删除的id数组中没找到,说明该数据是需要留下来的
					newDataExtends[index]=globalGridTreeDataExtend[i];
					newDatas[index]=globalGridTreeData[i];
					index++;
				}
			}
			globalGridTreeData=newDatas;
			globalGridTreeDataExtend=newDataExtends;
			
		}
		
		function getRecursiveId(nodeId){
			var ids="";
			for(var i=0;i<globalGridTreeDataExtend.length;i++){
				if(globalGridTreeDataExtend[i].pid+''==nodeId+''){
					ids+=","+globalGridTreeDataExtend[i].id;
					ids=ids+getRecursiveId(globalGridTreeDataExtend[i].id);
				}else if(i>=globalGridTreeDataExtend.length-1){//找到最后一个节点都没找到pid与传入的nodeId一致的节点，说明此节点为叶子
					return ids;
				}
			}
			return ids;
		}
		
		function resetIdToIndex(){
			var tmpIdToIndex=new Array();
			for(var i=0;i<globalGridTreeDataExtend.length;i++){
				var id=globalGridTreeDataExtend[i].id;
				tmpIdToIndex[id]=i;
			}
			globalIdToIndex=tmpIdToIndex;
		}
		
		function reBuildGridTreeData(){
			
			resetIdToIndex();
			
			$("td").find("input[idGroup='taskName']").each(function(){
				var TaskNameValue,idValue,levelGroup,showOrder,planStartDateValue,factStartDateValue,planEndDateValue,factEndDateValue,
				planWorkDateValue,factWorkDateValue,planWorkTimeValue,factWorkTimeValue,premiseValue,dutyManValue,fareValue;
				
				//首先获取顺序号
				TaskNameValue=stripScript($(this).val());
				idValue=$(this).parent().next().val();
				levelGroup=$(this).parent().next().next().val();
				
				//根据showOrder获取全局变量中的下标
				var globalIndex=globalIdToIndex[idValue];
				
				//新增一条后，原来的行的showOrder已经发生变化，最新的数值在extend变量中，不能根据html的旧数据取
				//showOrder=$(this).parent().next().next().next().val();
				showOrder=globalGridTreeDataExtend[globalIndex].showOrder;
				
				
				//获取其他文本框的值
				
				planStartDateValue=$(this).parent().parent().next().find("input[idGroup='planStartDate']").val();
				factStartDateValue=$(this).parent().parent().next().next().find("input[idGroup='factStartDate']").val();
				planEndDateValue=$(this).parent().parent().next().next().next().find("input[idGroup='planEndDate']").val();
				factEndDateValue=$(this).parent().parent().next().next().next().next().find("input[idGroup='factEndDate']").val();
				planWorkDateValue=$(this).parent().parent().next().next().next().next().next().find("input[idGroup='planWorkDate']").val();
				factWorkDateValue=$(this).parent().parent().next().next().next().next().next().next().find("input[idGroup='factWorkDate']").val();
				planWorkTimeValue=$(this).parent().parent().next().next().next().next().next().next().next().find("input[idGroup='planWorkTime']").val();
				factWorkTimeValue=$(this).parent().parent().next().next().next().next().next().next().next().next().find("input[idGroup='factWorkTime']").val();
				premiseValue=$(this).parent().parent().next().next().next().next().next().next().next().next().next().find("input[idGroup='premise']").val();
				dutyManValue=$(this).parent().parent().next().next().next().next().next().next().next().next().next().next().find("input[idGroup='dutyMan']").val();
				fareValue=$(this).parent().parent().next().next().next().next().next().next().next().next().next().next().next().find("input[idGroup='fare']").val();
			
				var newRow=generateNewGridTreeRow(true,TaskNameValue,idValue,levelGroup,showOrder,planStartDateValue,
						factStartDateValue,planEndDateValue,factEndDateValue,planWorkDateValue,
						factWorkDateValue,planWorkTimeValue,factWorkTimeValue,premiseValue,dutyManValue,fareValue);
				globalGridTreeData[globalIndex]=newRow;
			});
		}
		
		//在操作节点之前对原先的节点的显示顺序做调整，使得后续插入或者删除节点后，节点列表中的显示顺序能够连贯不重复
		 function updateShowOrder(action,newGridDataOrder,offset){
			 if(action=="addAfter" || action=="addBefore" || action=="addSon"){//比这个数字小的保持不变，大于等于这个数字的，统一加1; 向前：点击节点的showOrder为新节点的showOrder，点击节点以及后续的所有节点顺推1即可（+1）
				 for(var i=0;i<globalGridTreeDataExtend.length;i++){
					 var itemShowOrder=globalGridTreeDataExtend[i].showOrder;
					 if(itemShowOrder>=newGridDataOrder){
						 globalGridTreeDataExtend[i].showOrder=itemShowOrder*1+1;
					 }
				 }
			 }else if(action=="del"){
				 for(var i=0;i<globalGridTreeDataExtend.length;i++){
					 var itemShowOrder=globalGridTreeDataExtend[i].showOrder;
					 if(itemShowOrder>=newGridDataOrder){
						 globalGridTreeDataExtend[i].showOrder=itemShowOrder*1-offset;
					 }
				 }
			 }
		 }
		
		 //点击右键菜单的某一项
		 function onMenuClick(action){
		 	var id=currentClickedTask.find("input[idGroup='id']").val();
		 	//向当前记录后面插入一个空行
		 	if(action=='addAfter'){
		 		
		 		var taskId=generateUUID();
		 		//遍历查找出当前id的pid和showOrder
		 		var pid=0;
		 		var showOrder=0;
		 		for(var k=0;k<globalGridTreeDataExtend.length;k++){
		 			if(globalGridTreeDataExtend[k].id==id){
		 				pid=globalGridTreeDataExtend[k].pid;
		 				showOrder=globalGridTreeDataExtend[k].showOrder;
		 				break;
		 			}
		 		}
		 		//判断点击的节点有多少子节点
		 		var sonNode=getSonNodeNum(id);
		 		var offset=sonNode*1+1;//偏移量
		 		var newShowOrder=showOrder*1+offset;
		 		//alert("sonNode:"+sonNode+",offset:"+offset+",newOrder:"+newShowOrder);
		 		//处理js中的显示顺序，当前插入的showOrder为右击节点加便宜量，比这个数字小的保持不变，大于等于这个数字的，统一加1
		 		updateShowOrder(action,newShowOrder);
		 		//插入新节点
		 		globalGridTreeData[globalGridTreeData.length]=generateNewGridTreeRow(false,null,taskId,pid,newShowOrder);//taskId传入，用于初始化hidden类型的id	
		 		globalGridTreeDataExtend[globalGridTreeDataExtend.length]={id:taskId,pid:pid,showOrder:newShowOrder};
		 		
		 	}else if(action=='addBefore'){
		 		
		 		var taskId=generateUUID();
		 		//遍历查找出当前id的pid和showOrder
		 		var pid=0;
		 		var showOrder=0;
		 		for(var k=0;k<globalGridTreeDataExtend.length;k++){
		 			if(globalGridTreeDataExtend[k].id==id){
		 				pid=globalGridTreeDataExtend[k].pid;
		 				showOrder=globalGridTreeDataExtend[k].showOrder;
		 				break;
		 			}
		 		}
		 		//向前：点击节点的showOrder为新节点的showOrder，点击节点以及后续的所有节点顺推1即可（+1）
		 		var newShowOrder=showOrder*1;
		 		
		 		updateShowOrder(action,newShowOrder);
		 		//插入新节点
		 		globalGridTreeData[globalGridTreeData.length]=generateNewGridTreeRow(false,null,taskId,pid,newShowOrder);//taskId传入，用于初始化hidden类型的id	
		 		globalGridTreeDataExtend[globalGridTreeDataExtend.length]={id:taskId,pid:pid,showOrder:newShowOrder};
		 	}else if(action=='del'){
		 		var taskId=generateUUID();
		 		//遍历查找出当前id的pid和showOrder
		 		var pid=0;
		 		var showOrder=0;
		 		for(var k=0;k<globalGridTreeDataExtend.length;k++){
		 			if(globalGridTreeDataExtend[k].id==id){
		 				pid=globalGridTreeDataExtend[k].pid;
		 				showOrder=globalGridTreeDataExtend[k].showOrder;
		 				break;
		 			}
		 		}
		 		//判断点击的节点有多少子节点
		 		var sonNode=getSonNodeNum(id);
		 		if(sonNode>=1){
		 			if(!confirm("检查到有子任务，会一并删除子任务，确定删除么?")){
		 				return;
		 			}
		 		}
		 		//递归删除
		 		recursiveDel(id);
		 		//更新现有的显示顺序,如：2的下属有3，4，节点5和2平级，当删除2后，5的显示顺序需要变成2
		 		var offset=sonNode*1+1;//偏移量
		 		var updateOrders=showOrder*1+offset;
		 		updateShowOrder(action,updateOrders,offset);
		 	}else if(action=='addSon'){
		 		var taskId=generateUUID();
		 		var showOrder;
		 		for(var k=0;k<globalGridTreeDataExtend.length;k++){
		 			if(globalGridTreeDataExtend[k].id==id){
		 				showOrder=globalGridTreeDataExtend[k].showOrder;
		 				break;
		 			}
		 		}
		 		var pid=id;//子节点的pid为当前id
		 		//找到直接子节点最小的order
		 		var sonMinShowOrder=0;
		 		var k=0;
		 		for(k=0;k<globalGridTreeDataExtend.length;k++){
		 			if(globalGridTreeDataExtend[k].pid==id){
		 				sonMinShowOrder=(sonMinShowOrder<=globalGridTreeDataExtend[k].showOrder*1)?sonMinShowOrder:globalGridTreeDataExtend[k].showOrder*1;
		 			}	
		 		}
		 		if(k>=globalGridTreeDataExtend.length){//没有子节点，当前点击的节点已经是叶子
		 			sonMinShowOrder=showOrder*1+1;//如果是叶子，则设置其order为当前点击的节点+1
		 		}
		 		
		 		//更新现有的树的order
		 		updateShowOrder(action,sonMinShowOrder);
		 		//插入新节点
		 		globalGridTreeData[globalGridTreeData.length]=generateNewGridTreeRow(false,null,taskId,pid,sonMinShowOrder);//taskId传入，用于初始化hidden类型的id	
		 		//插入的节点的pid为当前点击节点id
		 		globalGridTreeDataExtend[globalGridTreeDataExtend.length]={id:taskId,pid:id,showOrder:sonMinShowOrder};
		 	}
		 	
		 	//显示表格树并监听 
	 		initAndShowGridTree();
		 }
		 
		 
		 
		 function generateNewGridTreeRow(reBuild,taskNameValue,idValue,levelGroup,showOrder,planStartDateValue,
					factStartDateValue,planEndDateValue,factEndDateValue,planWorkDateValue,
					factWorkDateValue,planWorkTimeValue,factWorkTimeValue,premiseValue,dutyManValue,fareValue){
			 var dataList;
			 if(reBuild!=true){
				 dataList=new Array(
							"<span idGroup='taskName'><input type='text' levelGroup='"+levelGroup+"' name='submitTaskName' idGroup='taskName' maxlength='100' size='32'  value='' /></span><input levelGroup='"+levelGroup+"' name='submitId' idGroup='id' type='hidden' value='"+idValue+"' /><input name='submitParentId' idGroup='parentId' type='hidden' value='"+levelGroup+"' /> <input name='submitShowOrder' idGroup='showOrder' type='hidden' value='"+showOrder+"' />",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanStartDate' idGroup='planStartDate' size='13'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactStartDate' idGroup='factStartDate' size='13'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanEndDate' idGroup='planEndDate' size='13'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactEndDate' idGroup='factEndDate' size='13'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanWorkDate' idGroup='planWorkDate'  onBlur='checkPlanWorkDate(this)' size='7'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactWorkDate' idGroup='factWorkDate' size='7' onBlur='checkFactWorkDate(this)' value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanWorkTime' idGroup='planWorkTime' size='7' onBlur='checkPlanWorkTime(this)' value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactWorkTime' idGroup='factWorkTime' size='7' onBlur='checkFactWorkTime(this)' value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPremise' idGroup='premise' size='7' maxlength='15' value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitDutyMan' idGroup='dutyMan' size='13' maxlength='15'  value='' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFare' idGroup='fare' size='7' maxlength='15' value='' /></span>");
			 }else{
				 dataList=new Array(
							"<span idGroup='taskName'><input type='text' levelGroup='"+levelGroup+"' name='submitTaskName' idGroup='taskName' maxlength='100' size='32'  value='"+taskNameValue+"' /></span><input name='submitId' idGroup='id' type='hidden' value='"+idValue+"' /><input name='submitParentId' idGroup='parentId' type='hidden' value='"+levelGroup+"' /> <input name='submitShowOrder' idGroup='showOrder' type='hidden' value='"+showOrder+"' />",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"'  name='submitPlanStartDate' idGroup='planStartDate' size='13'   value='"+planStartDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"'  name='submitFactStartDate' idGroup='factStartDate' size='13'  value='"+factStartDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanEndDate' idGroup='planEndDate' size='13'   value='"+planEndDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactEndDate' idGroup='factEndDate' size='13'   value='"+factEndDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanWorkDate' idGroup='planWorkDate' onBlur='checkPlanWorkDate(this)' size='7'  value='"+planWorkDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactWorkDate' idGroup='factWorkDate' onBlur='checkFactWorkDate(this)' size='7'  value='"+factWorkDateValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPlanWorkTime' idGroup='planWorkTime' onBlur='checkPlanWorkTime(this)' size='7'  value='"+planWorkTimeValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFactWorkTime' idGroup='factWorkTime' onBlur='checkFactWorkTime(this)' size='7'  value='"+factWorkTimeValue+"' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitPremise' idGroup='premise' size='7'  value='"+premiseValue+"' maxlength='15' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitDutyMan' idGroup='dutyMan' size='13'  value='"+dutyManValue+"' maxlength='15' /></span>",
							"<span styleGroup='left'><input type='text' levelGroup='"+levelGroup+"' name='submitFare' idGroup='fare' size='7'  value='"+fareValue+"' maxlength='15' /></span>");
			 }
			 //alert(dataList[0]);
			 return dataList;
		 }
		 
		 
		 function checkPlanWorkDate(ele){
				if(isNaN($(ele).val())){
					alert("请输入数字");
					$(ele).focus();
				}			
			}
			
			function checkFactWorkDate(ele){
				if(isNaN($(ele).val())){
					alert("请输入数字");
					$(ele).focus();
				}		
			}
			
			function checkPlanWorkTime(ele){
				if(isNaN($(ele).val())){
					alert("请输入数字");
					$(ele).focus();
				}		
			}
			
			function checkFactWorkTime(ele){
				if(isNaN($(ele).val())){
					alert("请输入数字");
					$(ele).focus();
				}		
			}
		
	</script>
	
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/projectplan/projectplanInfo!update.action" method="post">
<table width="98%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td bgcolor="#FFFFFF">
	<!-- ********************************************* 项目信息展示 ******************************************** -->
		<input type="hidden" id="basicProjectId" name='plan.id' value="<s:property  value='plan.id'/>">
		<input type="hidden" id="basicProjectCreator" name='plan.creatorUser' value="<s:property  value='plan.creatorUser'/>">
		<table width="100%" class="input_table">
		<tr>
		<td class="input_tablehead">修改项目信息</td>
		</tr>
			<tr >
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
				<td  bgcolor="#FFFFFF" colspan="5">
					<div align="left">
						<tm:tmSelect id="prjName" name='prjName' selType="projectName" path="systemConfig.projectname"  style="width:550px" />
						<input type="hidden" id="basicProjectName" name='plan.projectName' value="<s:property  value='plan.projectName'/>">
					</div>
				</td>
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">项目组别</font><font color="#FF0000">*</font></div></td>
				<td  bgcolor="#FFFFFF" colspan="5">
	      			<div align="left">
	      				<select id="gpName" style="width:350px">
	      					<option value='<s:property value="------ 请选择项目组别 -------"/>'>------ 请选择项目组别 -------</option>
	      					<s:iterator value="#request.groupList" id="tranInfo" status="row">
	      						<option value='<s:property value="name"/>'><s:property value="name"/></option>
	      					</s:iterator>
	      				</select>
	      				<input type="hidden" id="basicGroupName" name='plan.groupName'  value="<s:property value='plan.groupName'/>">
	      			</div>
	            </td>
			</tr>
			<tr >
				<td class="input_label2" width="8%"><div align="center"><font color="#000000">进度</font><font color="#FF0000">*</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" id="basicFare" name='plan.fare' size="20" maxlength="10" value="<s:property value='plan.fare'/>">
	      			</div>
	            </td>
	            <td class="input_label2" width="8%"><div align="center"><font color="#000000">项目经理</font></div></td>
				<td  bgcolor="#FFFFFF" width="12%">
	      			<div align="left">
	      				<input type="text" id="basicProjectManager" name='plan.projectManager' style="border-color:transparent;" class="input_readonly" readonly="readonly" size="20" maxlength="16" value="<s:property value='#request.usr.username'/>">
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
				<td colspan="9" bgcolor="#FFFFFF" >
					<div align="left">
						<textarea name='plan.projectDesc' id="basicProjectDesc" style="text-align:left;" onkeyup="checkPrjDesc(this)"  type="text"  rows="3" cols="135" maxlength="20"><s:property value='plan.projectDesc'/></textarea>
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
		<div>
			<a  href="javascript:gridTree.openAllNodes()">展开所有</a> | <a  href="javascript:gridTree.closeAllNodes()">关闭所有</a>
			&nbsp;<font style="color:#3366FF" >(温馨提示:右击"任务名称"节点可添加删除数据)</font>
		</div>
		
		<div id="gridTreeDiv" style="width:100%;overflow-y:auto;">
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

