﻿<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="cn.grgbanking.feeltm.dayLog.domain.*" %>

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
<%String defaultSelectText="--------------- 请选择 ---------------";%>
<html> 
	<head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	</head>
	<script type="text/javascript">
		var globalTaskInProject;
		var globalOldDate='<%=(String)request.getAttribute("logDate")%>';
		var iTaskIndex = <%=((List)request.getAttribute("daylogList")).size()%>;			// the number of the task tables
		var selAllFlag = true;
		var iTaskTotalpd = 20;		
		
		var initialTable;
		
		/* function closeModal(){
			if(window==window.parent){
		       window.close();
			}else{
			   parent.close();  
			}
		} */
		
		function checkSubTotal(ele){
			if(isNaN($(ele).val())){
				alert("请输入数字!");
				$(ele).focus();
			}
			calculateSumShow();
		}
		
		function save(){
			
			/* if(!validateInputInfo())
			{
				return ;
			} */
			var sumWorkhours = $("#sumShow").html()*1
			if (sumWorkhours > 24) {
				alert("您当天日志总时长超过24小时,请重新确认工时");
				return ;
			}
			if(sumWorkhours>8){
				if(!confirm("当天日志总时长超过8小时，确定提交么?")){
					return ;	
				}
			}
			
			 var submitDate=$("input[name='submitCreateDate']").val();
			//验证日期是否改变，如果改变，应该提示用户
			var submitOldDate=$("input[name='submitOldDate']").val();
			if(submitDate!=submitOldDate){
				if(!confirm('日期已经从'+submitOldDate+'变为'+submitDate+'，这将导致'+submitOldDate+'的日志被删除，确认继续吗？'))
				{
					return;				
				}
			} 
				
			//验证各项任务是否合法
			if(!validateTask()){
				return false;
			}
			
			window.returnValue=true;
			reportInfoForm.submit();
		}
		function Validate(itemName,pattern){
		  var Require= /.+/;
		  var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		  var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
		  var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
		  var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		  var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		  var Currency = /^\d+(\.\d+)?$/;
		  var Number= /^\d+$/;
		  var Zip = /^[1-9]\d{5}$/;
		  var QQ = /^[1-9]\d{4,8}$/;
		  var Integer = /^[-\+]?\d+$/;
		  var integer = /^[+]?\d+$/;
		  var Double= /^[-\+]?\d+(\.\d+)?$/;
		  var double = /^[+]?\d+(\.\d+)?$/;
		  var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		  var Chinese = /^[\u0391-\uFFE5]+$/;
		  var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		
			//var itemNameValue=document.getElementsByName(itemName)[0].value
			var itemNameValue=itemName;
			
				var flag;
			switch(pattern){ 
			 case "Require":
				 flag = Require.test(itemNameValue);
				  break;
			 case "Email":
				 flag = Email.test(itemNameValue);
				  break;
			 case "Phone":
				 flag = Phone.test(itemNameValue);
				  break;
			 case "Mobile":
				 flag = Mobile.test(itemNameValue);
				  break;
			 case "Url":
				 flag = Url.test(itemNameValue);
				  break;
			 case "IdCard":
				 flag = IdCard.test(itemNameValue);
				  break;
			 case "Currency":
				 flag = Currency.test(itemNameValue);
				  break;
			 case "Number":
				 flag = Number.test(itemNameValue);
					  break;
			 case "Zip":
				 flag = Zip.test(itemNameValue);
				  break;
			 case "QQ":
				 flag = QQ.test(itemNameValue);
				  break;
			 case "integer":
				 flag = integer.test(itemNameValue);
				  break;		  
			 case "Integer":
			// if (itemNameValue.length>0)
				 flag = Integer.test(itemNameValue);
			//else 
			//	flag=true
				  break;
			 case "Double":
				 flag = Double.test(itemNameValue);
				  break;
			 case "double":
					 flag = double.test(itemNameValue);
					  break;
			 case "English":
				 flag = English.test(itemNameValue);
				  break;
			 case "Chinese":
				 flag = Chinese.test(itemNameValue);
				  break;
			 case "BankCard":
				 flag = BankCard.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numClear(itemName){
			itemName.select();
		}
		
		function addAgain(){
			//验证各项任务是否合法
			if(!validateTask()){
				return;
			}
			
			if(document.getElementsByName("subtotal").length >= iTaskTotalpd)
			{
				alert("哇，一天这么多任务，可惜本页面限制最多新增" + iTaskTotalpd + "项任务");
				return;
			}
			
			//获取初始化table
			var taskTable2 = initialTable.clone(true);  //复制初始化表格
			
			//匹配替换表格中的name，便于表单提交
			replaceTableTagName(taskTable2,iTaskIndex);

			//对序号进行重新赋值
			taskTable2.find("div[name='taskIdDiv']").html(iTaskIndex+1);
			
			
			//编辑页面的初始化表格是有数据的，这里需要对里面的数据进行清空
			taskTable2.find("select[name='daylogList_type']").val(0);
			taskTable2.find("select[name='daylogList_statu']").val(0);
			taskTable2.find("select[name='daylogList_finishRate']").val(0);
			taskTable2.find("select[name='daylogList_planOrAdd']").val(0);
			taskTable2.find("input[name='daylogList_subTotal']").val('');
			taskTable2.find("input[name='daylogList_confirmHour']").val('');
			taskTable2.find("textarea[name='daylogList_reason']").html('');

			//$("#taskTableDiv").append(taskTable2);
			var html='<table name="taskTable" width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">';
			html+=taskTable2.html();
			html+='</table>';
			$("#taskTableDiv").append(html);
			iTaskIndex++;    //将记录的序号数加1	
			listenProjectChange();
			updateTaskInProject($("#taskTableDiv").find("select[name='daylogListPrjName']:last"));
		}
		
		//验证各项任务是否合法
		function validateTask(){
			var curCount = 1;
			var result=true;
			$("table[name='taskTable']").each(function(){
				var taskDivPtrS = $(this).find("select[name='daylogList_desc']").get(0);
				var taskDivPtrJ = $(this).find("select[name='daylogList_prjName']").get(0);
				var taskDivPtrPgr = $(this).find("select[name='daylogList_finishRate']").get(0);
				var taskDivPtrST = $(this).find("input[name='daylogList_subTotal']").val();
				var taskDivptrStatu=$(this).find("select[name='daylogList_statu']").val();
				var taskDivStatu=$(this).find("select[name='daylogList_statu']").get(0).value.trim();
				var taskDivDelayReason=$(this).find("textarea[name='daylogList_delayReason']").val();
				if(taskDivPtrJ!=null)
				{
					if(taskDivPtrJ.value.trim().indexOf("--")==0)
					{
						alert("第"+ curCount +"项任务的项目名称请确认是否正确");
						taskDivPtrJ.value = "";
						result= false;
						return;
					}
				}
				if(taskDivPtrPgr!=null)
				{
					if(taskDivPtrPgr.value.trim() == "0%")
					{
						alert("请选择第"+curCount+"项任务的完成情况");
						result= false;
						return;
					}
				}
				if(taskDivPtrS!=null)
				{
					if(taskDivPtrS.value.trim() == "")
					{
						alert("请选择第"+curCount+"项任务的任务描述");
						result= false;
						return;
					}
				}
				if(Math.round(taskDivPtrST*100)/100==0)
				{
					alert("请输入第"+curCount+"项任务的任务工时");
					result= false;
					return;
				}
				if(taskDivPtrST.trim().match(/^\d+(\.\d+)?$/) == null){
			        alert("请确认第"+curCount+"项任务的任务工时是整数或小数!");
			        result= false;
			        return false;
			    }
				if(taskDivStatu=="延迟" && taskDivDelayReason==""){
					alert("请输入第"+curCount+"项的延迟原因");
					result=false;
					return false;
				}
				curCount++;
			});
			return result;
		}
		
		
		function delAgain(){
			var selectNum = 0;
			//获取有多少任务被选中
			$("table[name='taskTable']").each(function(){
				if($(this).find('input[type=checkbox]').attr('checked')||$(this).find('input[type=checkbox]').prop('checked')){
					selectNum++;
					return;
				} 
			}); 
			
			//验证是否至少剩下一条任务
			if(selectNum>0 && iTaskIndex-selectNum>0) {
				if(!confirm('您确认删除记录吗？'))
				{
					return;				
				}
			}else if(selectNum>0 && iTaskIndex-selectNum==0){
				alert('请至少保留一条记录!');
				return;
			}
			//执行删除操作
			$("table[name='taskTable']").each(function(){
				if($(this).find('input[type=checkbox]').attr('checked')||$(this).find('input[type=checkbox]').prop('checked')){
					$(this).remove();
					iTaskIndex--;//记录的序号数减1
				} 
			});
			
			//遍历剩下的任务，对任务的序号重新编号，并重新命名表格控件的name属性，
			//如：原来3条记录，删除前两条，剩下最后一条，这时需要将原来的daylogList[2].desc重命名成daylogList[0].desc
			var nowTaskTableIndex=1;
			$("table[name='taskTable']").each(function(){
				//重新定义序号
				$(this).find("div[name='taskIdDiv']").html(nowTaskTableIndex);
				//修改name属性
				replaceTableTagName($(this),nowTaskTableIndex-1);
				//下标加1
				nowTaskTableIndex++;
			}); 
			calculateSumShow();
			
			listenProjectChange(); 
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
		
		/* function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = reportInfoForm.submitCreateDate.value.trim();
		if(thisDate.length>0){
		var a = re1.test(thisDate);
		 if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 } 
		  return true;
	 } */
	 
	 function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementsByName(beginDate)[0].value
    var end=document.getElementsByName(endDate)[0].value

	var flag=true;

    if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;
   return flag;

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
			
			//页面加载后，就获取初始化table
			$('table[name="taskTable"]').each(function(){
				initialTable=$(this.cloneNode(true));  //复制第一个表格
				return;
			});
			
			//初始化按日期查询的开始日期
			$("input[typeGroup='sdateCon']").each(function(){
				var role=$(this).prev().val();
				if(role.length>0)
					role="20"+role.substring(0,8);
				$(this).val(role);
			});
			//初始化按日期查询的结束日期
			$("input[typeGroup='edateCon']").each(function(){
				var role=$(this).prev().val();
				if(role.length>0)
					role="20"+role.substring(0,8);
				$(this).val(role);
			});
			//初始化查询方式的值
			$("div[id='tasktypeDiv']").each(function(){
				var type = $(this).find("input[id='s_tasktype']").val();
				if(type=="")
					type="0";
				$(this).find('input[typeGroup="tasktypeRadio"]').each(function(){
					if($(this).val()==type){
						$(this).attr("checked","checked");
					}
				});
				if(type=="2"){
					$(this).next().show();
				}else{
					$(this).next().hide();
				}
			});
			//初始化状态
			$("select[typeGroup='statuSelect']").each(function(){
				if($(this).val()=="延迟"){
					$(this).parent().parent().next().next().next().next().show();
					changeShowDivSize();
				}else{
					$(this).parent().parent().next().next().next().next().hide();
					changeShowDivSize();
				}
			});
			
			//通过js判断哪个项目被选中
       	 	$("div[name='divPrjName']").each(function(){
       	 		var divPrj=$(this);
       	 		$(this).find("option").each(function(){
	       	 		if($(this).val()==divPrj.find("input").val()){
	       	 			$(this).attr("selected","selected");
	       	 		}else{
	       	 			$(this).removeAttr("selected");
	       	 		}       	 			
       	 		});
	   	 		updateTaskInProject(divPrj.find("select[name='daylogListPrjName']")); 
       	 	});
			
			//计算总工时
			calculateSumShow();
			listenProjectChange();
			
			
		});
		
		function changeShowDivSize(){
			var tableNum=$("table[name='taskTable']").size();
			var tableHight=$("table[name='taskTable']:first").height()*tableNum+15;
			if(tableHight>600){
				$(".scrollFeild").css("height","600");
			}else{
				$(".scrollFeild").css("height",tableHight);
			}
		}
		
		function listenProjectChange(){
			//监听选中的项目，更新项目中人员列表
			$("select[name='daylogListPrjName']").change(function(){
				updateTaskInProject($(this));		
			});
			
			//监听日期的变化，如果发生变化，应该修改所有任务列表，修改为对应日期的
			$("#submitCreateDate").change(function(){
				if(isAllowedChange($(this).val())){
					globalOldDate=$(this).val();
					$("select[name='daylogListPrjName']").each(function(){
						updateTaskInProject($(this));		
					});
				}else{
					alert("您只能补最近一周的日志!");
					$(this).val(globalOldDate);
				}
			});
			//状态修改时，显示延迟原因
			$("select[typeGroup='statuSelect']").change(function(){
				if($(this).val()=="延迟"){
					$(this).parent().parent().next().next().next().next().show();
					changeShowDivSize();
				}else{
					$(this).parent().parent().next().next().next().next().hide();
					changeShowDivSize();
				}
			});
			
			changeShowDivSize();
			
			//jqueryui的日期控件 
			$("#submitCreateDate").datepicker({  
	            dateFormat:'yy-mm-dd',  //更改时间显示模式  
	            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
	            changeYear:true        //是否显示年份的下拉菜单，默认为false  
	        });
			
			//铲鲟计划任务变化是，修改任务列表中的数据
			var index = iTaskIndex-1;
			/*$('input:radio[name="daylogList['+index+'].tasktype"]').change(function(){
				getTaskByType($(this));		
			});*/
			$("input[typeGroup='tasktypeRadio']").change(function(){
				/*var type = $(this).val();
				alert(type);
				var num = $(this).parent().find('input[name="radioRowid"]').val();
				//getTaskByType($('input:radio[name="daylogList['+num+'].tasktype"]'));
				$('input:radio[name="daylogList['+num+'].tasktype"]').change(function(){
					if($(this).val()==type){
						alert(type);
						getTaskByType($(this));	
					}
				});*/
				getTaskByType($(this));	
			});
			
			//为开始日期及结束日期添加日期控件事件
			for(var i=0;i<iTaskIndex;i++){
				$('input[name="daylogList['+i+'].tasksdate"]').datepicker({  
		            dateFormat:'yy-mm-dd',  //更改时间显示模式  
		            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
		            changeYear:true        //是否显示年份的下拉菜单，默认为false  
		         });
				$('input[name="daylogList['+i+'].taskedate"]').datepicker({  
		            dateFormat:'yy-mm-dd',  //更改时间显示模式  
		            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
		            changeYear:true        //是否显示年份的下拉菜单，默认为false  
		         });
			}
		}
		
		//判断是否允许改变日期
		function isAllowedChange(newDate){
			var oldDate='<%=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())%>';
			//计算时间差
			var oldTime = new Date(oldDate.replace("-","/")); //当前时间
			var newTime = new Date(newDate.replace("-","/")); //用户填写的日志时间
			var diff = parseInt((newTime - oldTime) / (1000*60*60*24)); 
			if(diff<-15 ){ //修改成过去的日志 （不能修改成15天前的日志  页不能修改成未来时间的日志）
				return false;
			}else{
				return true;
			}
		}
		
		//获取用户选择项目中本周的任务
		function updateTaskInProject(projectSelect){
			var projectId=projectSelect.val();
			var type="";
			var index = iTaskIndex-1;
			projectSelect.parent().parent().parent().next().find('input[typeGroup="tasktypeRadio"]').each(function(){
				if($(this).val()=="0" && $(this).is(':checked'))
					type="0";
				if($(this).val()=="1" && $(this).is(':checked'))
					type="1";
				if($(this).val()=="2" && $(this).is(':checked'))
					type="2";
			});
			
			var userid=$("#logUserIdStr").val();
			var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!getTaskInProject.action?projectId="+projectId+"&userid="+userid;
			//加上查询某个日期的任务
			actionUrl+="&queryDate="+$("#submitCreateDate").val()+"&type="+type;
			var dateUrl="";
			if(type=="2"){
				projectSelect.parent().parent().parent().next().find('input[typeGroup="sdateCon"]').each(function(){
					dateUrl+="&sdate="+$(this).val();
				});
				projectSelect.parent().parent().parent().next().find('input[typeGroup="edateCon"]').each(function(){
					dateUrl+="&edate="+$(this).val();
				});
			}
			actionUrl+=dateUrl;
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
					
					//更新任务执行者
					//alert(projectSelect.prev().attr("name")+":"+projectSelect.prev().val());
					var i=0;
					projectSelect.parent().parent().parent().next().next().find("select[typeGroup='taskSelect']").each(function(){
						var html="";
						$.each(data,function(key,ele){
							var taskStartdate = "";
							//if(type!="0"){
								taskStartdate = "&nbsp;&nbsp;&nbsp;&nbsp;开始时间:"+ele.desc;
							//}
							if(ele.priority!=""){
								html+='<option value="'+ele.id+'">'+ele.taskContent+'(优先级:'+ele.priority+')'+taskStartdate+'</option>';
							}else{
								html+='<option value="'+ele.id+'">'+ele.taskContent+taskStartdate+'</option>';
							}
						});
						html+='<option value="其他任务">其他任务</option>';
						$(this).html(html);
					});
					
					updateCurSelectTask();
				},
				error : function(data, data1) {
					alert("获取任务错误!");
				}
			});	
		}
		
		//更新每行默认选中的任务select
		function updateCurSelectTask(){
			$("input[name='curSelectedTaskInput']").each(function(){
				var hiddenValue=$(this).val();
				$(this).parent().find("select").find("option").each(function(){
					var val = $(this).html();
					if(val=="其他任务")
						val = "";
					if(val==hiddenValue){
						$(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
			});
		}
	
		//计算总工时
		function calculateSumShow(){
			var sum=0;
			$("input[name='daylogList_subTotal']").each(function(){
				sum+=$(this).val()*1;
			});
			$("#sumShow").html(sum);
		}
		
		//项目select控件发生变化，自动更新其后面的隐藏控件的值，隐藏控件的值用于表单提交
		function changeProject(select){
			$(select).parent().find("input[name='daylogList_prjName'][type='hidden']").val($(select).find("option:selected").val());
			//alert($(select).parent().find("input[name='daylogList_prjName'][type='hidden']").val());
		}
		
		//由于js的table对象的name属性不能直接替换，这里变相的通过替换table中的html字符串中对应的name来实现
		function replaceTableTagName(taskTable,tableIndex){
			var html=taskTable.html();
			//查询方式
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasktype","g"),"daylogList["+tableIndex+"].tasktype");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasksdate","g"),"daylogList["+tableIndex+"].tasksdate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.taskedate","g"),"daylogList["+tableIndex+"].taskedate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.searchTaskBut","g"),"daylogList["+tableIndex+"].searchTaskBut");
			html=html.replace(new RegExp("sdate\\[[0-9]+\\]","g"),"sdate["+tableIndex+"]");
			html=html.replace(new RegExp("edate\\[[0-9]+\\]","g"),"edate["+tableIndex+"]");
			//最后赋值给table
			taskTable.html(html);
		}
		
		//按周或按月查询计划任务列出计划任务
		function getTaskByType(taskType){
			var type = taskType.val();
			if(type=="2"){
				taskType.parent().next().parent().find("div[id='dateTask']").each(function(){
					$(this).show();
				});
				return;
			}else{
				taskType.parent().next().parent().find("div[id='dateTask']").each(function(){
					$(this).hide();
				});
			}
			var projectId="";
			taskType.parent().parent().parent().prev().find("select[typeGroup='prjNameSelect']").each(function(){
				projectId = $(this).val();
			});
			var userid=$("#logUserIdStr").val();
			var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!getTaskInProject.action?projectId="+projectId+"&type="+type+"&userid="+userid;	
			//加上查询某个日期的任务
			actionUrl+="&queryDate="+$("#submitCreateDate").val();
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
					
					globalTaskInProject=data;
					//更新任务执行者
					taskType.parent().parent().parent().next().find("select[typeGroup='taskSelect']").each(function(){
						var html="";
						$.each(data,function(key,ele){
							var taskStartdate = "";
							//if(type!="0"){
								taskStartdate = "&nbsp;&nbsp;&nbsp;&nbsp;开始时间:"+ele.desc;
							//}
							if(ele.priority!=""){
								html+='<option value="'+ele.id+'">'+ele.taskContent+'(优先级:'+ele.priority+')'+taskStartdate+'</option>';
							}else{
								html+='<option value="'+ele.id+'">'+ele.taskContent+taskStartdate+'</option>';
							}
						});
						html+='<option value="其他任务">其他任务</option>';
						$(this).html(html);
					});
				},
				error : function(data, data1) {
					alert("获取任务错误!");
				}
			});	
		}
		
		function searchTaskBut(_this){
			var taskBut = $(_this);
			var index = iTaskIndex-1;
			var sdate = "";
			var edate = "";
			taskBut.parent().find("input[typeGroup='sdateCon']").each(function(){
				sdate = $(this).val();
			})
			taskBut.parent().find("input[typeGroup='edateCon']").each(function(){
				edate = $(this).val();
			})
			var projectId="";
			taskBut.parent().parent().parent().prev().find("select[typeGroup='prjNameSelect']").each(function(){
				projectId = $(this).val();
			});
			if(sdate==""){
				alert("请填写开始日期");
				return;
			}			
			if(edate==""){
				alert("请填写结束日期");
				return;
			}
			var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!getTaskInProject.action?projectId="+projectId+"&type=2";
			//加上查询某个日期的任务
			actionUrl+="&sdate="+sdate+"&edate="+edate;
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
					
					globalTaskInProject=data;
					//更新任务执行者
					taskBut.parent().parent().parent().next().find("select[typeGroup='taskSelect']").each(function(){
						var html="";
						$.each(data,function(key,ele){
							var taskStartdate = "";
							var type="2";
							//if(type!="0"){
								taskStartdate = "&nbsp;&nbsp;&nbsp;&nbsp;开始时间:"+ele.desc;
							//}
							if(ele.priority!=""){
								html+='<option value="'+ele.id+'">'+ele.taskContent+'(优先级:'+ele.priority+')'+taskStartdate+'</option>';
							}else{
								html+='<option value="'+ele.id+'">'+ele.taskContent+taskStartdate+'</option>';
							}
						});
						html+='<option value="其他任务">其他任务</option>';
						$(this).html(html);
					});
				},
				error : function(data, data1) {
					alert("获取任务错误!");
				}
			});	
		}
		
		function timeValidate(){
			var dateId = $("#submitCreateDate");
			var createDate= dateId.val();
			var now=new Date();
			var year=now.getFullYear();
			var month=now.getMonth() + 1;
			var day=now.getDate();
			var currentTime = year+"-"+month+"-"+day;
			var selectDate = new Date(createDate.replace(/\-/g, "\/")); 
			var currentDate = new Date(currentTime.replace(/\-/g, "\/"));  
			if(selectDate != "" && currentDate != "" && selectDate > currentDate) 
			{ 
			  alert("修改失败：当前只能填写今天或者过去的日志,请修改日志日期。"); 
			  dateId.val(currentTime);
			  //$("#ok").attr("disabled",true);
			}else if(selectDate != "" && currentDate != "" && selectDate <= currentDate){
				 $("#ok").attr("disabled",false);
			}
		}
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/daylog/logInfo!update.action" method="post">
<input type="hidden" name="logUserIdStr" id="logUserIdStr" value="<s:property value='#request.logUserIdStr'/>">
<table width="100%" align="center" cellPadding="1" cellSpacing="1">
<tr>
<td>
        <table width="100%" class="input_table">
        	<tr>
		<td class="input_tablehead"><s:text name="label.newTermInfo" /></td>
        	</tr>
        	<tr>
        		<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">姓名：<s:property value='#request.logUserNameStr'/></div></b></td> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
        	 	<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">部门：</b><s:property value='#request.logDeptNameStr'/></div></b></td> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
            	<td align="right" nowrap="nowrap"></td>  
            	<td align="right" width="10%" nowrap="nowrap"><b><div align="right'" style="padding-top:4">日期：</div></b></td>   
                <td align="left" width="20%"><div align="left">
                	<input name="submitCreateDate" id="submitCreateDate"   style="text-align:left;" type="text" size="12"   onchange="timeValidate();" value='<s:property value='#request.logDate'/>' > 
                	<input name="submitOldDate" type='hidden' value='<s:property value='#request.logDate'/>'  >
                </div></td>
            	<td align="right" width="10%" nowrap="nowrap">
	            	<div align="right">工时总计 <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
            <s:if test="#request.showAudit">
            <tr>
				<td colspan="8" style="text-align: left" colspan="8" style="text-align: left"><b>审核状态:<s:if test="'#request.auditResult'=='审核通过'"><font style="color:blue"><s:property value='#request.auditResult'/></font></s:if><s:else><font style="color:red"><s:property value='#request.auditResult'/></font></s:else></b></td>
            </tr>
            <s:if test="#request.showAuditContent">
			<tr>
            	<td colspan="8" style="text-align: left">
            		<b><s:property value='#request.auditMan'/>(<s:property value='#request.auditTime'/>)</b>:<s:property value='#request.auditLog'/>
            	</td>
            </tr>        
            </s:if>    
            </s:if>
        </table>
        <div style="height:500px;overflow:auto;" >
        <div id="taskTableDiv" class="scrollFeild">
        	<s:iterator value="daylogList" id="daylog" status="row">
			<table name='taskTable' width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
	        	<tr>
					<td bgcolor="#FFFFFF"><input type="checkbox" name="taskCkd"/> <div align="center" name='taskIdDiv' ><s:property value='#row.index+1'/></div></td>
	                <td bgcolor="#FFFFFF">
	        		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
	   		    		<tr >
	                          <td colspan="2" class="column_label"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
	                          <td class="column_label" width="10%"><div align="center"><font color="#000000">类别</font></div></td>
	                          <td class="column_label" width="10%"><div align="center"><font color="#000000">状态</font></div></td>
	                          <td class="column_label" width="10%"><div align="center"><font color="#000000">完成%</font><font color="#FF0000">*</font></div></td>
	                          <td class="column_label" width="20%"><div align="center"><font color="#000000">计划/新增</font></div></td>
	                          <td class="column_label" width="10%"><div align="center"><font color="#000000">工时</font><font color="#FF0000">*</font></div></td>
	                          <td class="column_label" width="10%"><div align="center"><font color="#000000">确认工时</font>	</div></td>
	                    </tr>
	                    <tr>
	                       	<td colspan="2" nowrap bgcolor="#FFFFFF">
	                       	 <div name="divPrjName">
	                       	 	<input type="hidden" name="daylogList_prjName" value="<s:property value='prjName'/>">
		                       	 	<select onchange='changeProject(this)' name="daylogListPrjName" id="prjName" style="width:320px" typeGroup="prjNameSelect">
			                       	 	<!-- 没有确认工时权限时显示自己的项目，有权限显示所有项目 -->
			                       	 	<s:if test="#request.hasComfirmRight==false">
											<s:iterator  value="#request.projects" id="ele" status="row">
												<option value='<s:property value="id" />'><s:property value="name" /></option>
											</s:iterator>
										</s:if>
										<s:else>
											<s:iterator  value="#request.allProject" id="ele" status="row">
												<option value='<s:property value="id" />'><s:property value="name" /></option>
											</s:iterator>										
										</s:else>
									</select>
	                       	 </div>
	                       	</td>
					        <td nowrap bgcolor="#FFFFFF">
	                            <select name="daylogList_type"  style='width:80px'>
	                            	<option value="日常工作" <s:if test="'日常工作'==type">selected</s:if>>日常工作</option>
		                            <option value="需求" <s:if test="'需求'==type">selected</s:if>>需求</option>
		                            <option value="设计" <s:if test="'设计'==type">selected</s:if>>设计</option>
		                            <option value="编码" <s:if test="'编码'==type">selected</s:if>>编码</option>
		                            <option value="测试" <s:if test="'测试'==type">selected</s:if>>测试</option> 
		                            <option value="管理" <s:if test="'管理'==type">selected</s:if>>管理</option>
		                            <option value="文档" <s:if test="'文档'==type">selected</s:if>>文档</option>
		                            <option value="会议" <s:if test="'会议'==type">selected</s:if>>会议</option>
		                            <option value="培训" <s:if test="'培训'==type">selected</s:if>>培训</option>     
		                            <option value="其他" <s:if test="'其他'==type">selected</s:if>>其他</option>    
	                           	</select>
	                           </td>
	                           <td nowrap bgcolor="#FFFFFF">
	                            <select name="daylogList_statu"  style='width:50px' typeGroup='statuSelect'>
		                            <option value="按时" <s:if test="'按时'==statu">selected</s:if>>按时</option>
		                            <option value="延迟" <s:if test="'延迟'==statu">selected</s:if>>延迟</option>
		                            <option value="提前" <s:if test="'提前'==statu">selected</s:if>>提前</option>
		                            <option value="取消" <s:if test="'取消'==statu">selected</s:if>>取消</option> 
	                           	</select>
	                           </td>
	                           <td nowrap bgcolor="#FFFFFF">
	                            <select name="daylogList_finishRate"  style='width:80px'>
		                            <option value="100%" <s:if test="'100%'==finishRate">selected</s:if>>100%</option>
		                            <option value="95%" <s:if test="'95%'==finishRate">selected</s:if>>95%</option>
		                            <option value="90%" <s:if test="'90%'==finishRate">selected</s:if>>90%</option>
		                            <option value="85%" <s:if test="'85%'==finishRate">selected</s:if>>85%</option>
		                            <option value="80%" <s:if test="'80%'==finishRate">selected</s:if>>80%</option>
		                            <option value="75%" <s:if test="'75%'==finishRate">selected</s:if>>75%</option>
		                            <option value="70%" <s:if test="'70%'==finishRate">selected</s:if>>70%</option>
		                            <option value="65%" <s:if test="'65%'==finishRate">selected</s:if>>65%</option>
		                            <option value="60%" <s:if test="'60%'==finishRate">selected</s:if>>60%</option>
		                            <option value="55%" <s:if test="'55%'==finishRate">selected</s:if>>55%</option>
		                            <option value="50%" <s:if test="'50%'==finishRate">selected</s:if>>50%</option>
		                            <option value="45%" <s:if test="'45%'==finishRate">selected</s:if>>45%</option>
		                            <option value="40%" <s:if test="'40%'==finishRate">selected</s:if>>40%</option>
		                            <option value="35%" <s:if test="'35%'==finishRate">selected</s:if>>35%</option>
		                            <option value="30%" <s:if test="'30%'==finishRate">selected</s:if>>30%</option>
		                            <option value="25%" <s:if test="'25%'==finishRate">selected</s:if>>25%</option>
		                            <option value="20%" <s:if test="'20%'==finishRate">selected</s:if>>20%</option>
		                            <option value="15%" <s:if test="'15%'==finishRate">selected</s:if>>15%</option>
		                            <option value="10%" <s:if test="'10%'==finishRate">selected</s:if>>10%</option>
		                            <option value="5%" <s:if test="'5%'==finishRate">selected</s:if>>5%</option>
		                            <option value="0%" <s:if test="'0%'==finishRate">selected</s:if>>0%</option>
	                            </select>
	                      </td>
	                      <td nowrap bgcolor="#FFFFFF">
	                         <div align="left">
	                         <select name="daylogList_planOrAdd"  style='width:80px'>
	                            <option value="计划" <s:if test="'计划'==planOrAdd">selected</s:if>>计划</option>
	                            <option value="新增" <s:if test="'新增'==planOrAdd">selected</s:if>>新增</option>
	                         </select>
	                         </div>
	                      </td>
	                      <td nowrap bgcolor="#FFFFFF" align="center">
	                      <div align="center">
	                      	<s:if test="#request.isLogUser==true">
		                     	 <input maxlength="4" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'');calculateSumShow()" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'');" name="daylogList_subTotal" type="text" size="6" value="<s:property value='subTotal'/>" style="color:#FF0000"  text-align:center>
	                      	</s:if>
	                      	<s:else>	                      	
		                     	 <input style="background-color: #E3E3E3" readonly="readonly" maxlength="4" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'');calculateSumShow()" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'');" name="daylogList_subTotal" type="text" size="6" value="<s:property value='subTotal'/>" style="color:#FF0000"  text-align:center>
	                      	</s:else>
	                      </div></td>
	                     <!-- 项目经理级别以上可以编辑确认工时 -->
	                      <s:if test="#request.hasComfirmRight==true">
	                      	<td nowrap bgcolor="#FFFFFF" align="center"><div align="center"><input maxlength="4" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'');calculateSumShow()" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'');" name="daylogList_confirmHour" type="text" size="6" value="<s:property value='confirmHour'/>" style="color:#FF0000"  text-align:center></div></td>
	                  	  </s:if>
	                  	  <s:elseif test="#request.hasComfirmRight==false">	                  	  	
	                      	<td nowrap  style="background-color: #E3E3E3"  align="center"><div align="center"><input style="background-color: #E3E3E3" readonly="readonly" maxlength="4");" name="daylogList_confirmHour" type="text" size="6" value="<s:property value='confirmHour'/>"  text-align:center></div></td>
	                  	  </s:elseif>
	                  </tr>
	                  <tr>
	                      <td class="input_label2"><div align="center"><font color="#000000">查询方式</font></div></td>
	                      <td colspan="7" nowrap bgcolor="#FFFFFF" >
	                      	<div align="left" style="float:left;" id="tasktypeDiv">
	                      		<input type="hidden" id="radioRowid" name="radioRowid" value="<s:property value='#row.index'/>"/>
	                      		<input type="hidden" id="s_tasktype" name="s_tasktype" value="<s:property value='tasktype'/>"/>
		                      	<input name="daylogList[<s:property value='#row.index'/>].tasktype" type="radio" value="0" checked typeGroup="tasktypeRadio">本日
		                      	<input name="daylogList[<s:property value='#row.index'/>].tasktype" type="radio" value="1" typeGroup="tasktypeRadio">本周
		                      	<input name="daylogList[<s:property value='#row.index'/>].tasktype" type="radio" value="2" typeGroup="tasktypeRadio">指定日期
	                      	</div>
	                      	<div id="dateTask" style="text-align:left;display:none;"><!-- class="MyInput" -->
	                      		<input type="hidden" id="s_tasksdate" name="s_tasksdate" value="<s:property value='tasksdate'/>"/>
	                     		<input name="daylogList[<s:property value='#row.index'/>].tasksdate" type="text" id="sdate[0]" size="15" value="" typeGroup="sdateCon">至
	                     		<input type="hidden" id="s_taskedate" name="s_taskedate" value="<s:property value='taskedate'/>"/>
	                     		<input name="daylogList[<s:property value='#row.index'/>].taskedate" type="text" id="edate[0]"  size="15"  value="" typeGroup="edateCon">
	                     		<input type="button" id="testTask" name="daylogList[<s:property value='#row.index'/>].searchTaskBut" value="查询" onclick="searchTaskBut(this)" style="height:20px;">
	                     		<font color="red">请填写日期进行查询</font>
                     		</div>
                     	</td>
	                  </tr>
	                  <tr>
	                      <td class="input_label2"><div align="center"><font color="#000000">计划任务</font><font color="#FF0000">*</font></div></td>
	                      <td colspan="7" nowrap bgcolor="#FFFFFF" ><div align="left" id="taskSelectDiv">
	                      	<input type="hidden" name="curSelectedTaskInput" value="<s:property value='desc'/>">
	                      	<select typeGroup='taskSelect' name="daylogList_desc" id="taskDesc"  style="width:100%">
	                      		<option value="其他任务">其他任务</option>
	                      	</select>
	                      </div></td>
	                  </tr>
	                  <tr>
	                  	 <td class="input_label2">
	                  	 <div align="center" style="width:80px;">
	                  	 <font color="#000000">任务描述 </font> 
	                  	 
	                  	<%--  <input type='checkbox'  name = 'isLeave' id = 'leave' class = 'check_'  >
                  	 		<span>
                  	 			<font color='#f00806'>请假</font>
                  	 		</span> --%>
	                  	 </div>
	                  	 
	                  	 </td>
	                  	 
	                      <td colspan="7" nowrap bgcolor="#FFFFFF" >
	                      	<div align="left">
								<s:if test="#request.isLogUser==true">
		                      		<textarea name="daylogList_reason" type="text"  rows="6" style="width:99%"><s:property value='reason'/></textarea>
		                  	  	</s:if>
								<s:else>	                  	  	
		                      		<textarea name="daylogList_reason" style="background-color: #E3E3E3"  readonly='readonly' type="text"  rows="6" style="width:99%"><s:property value='reason'/></textarea>
								</s:else>
	                      	</div>
	                      </td>
	                  </tr>
					<tr id="delayReasonTr" style="display:none;">
                  	 	<td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">延迟原因</font></div></td>
                      	<td colspan="7" nowrap bgcolor="#FFFFFF" >
	                      	<div align="left">
								<s:if test="#request.isLogUser==true">
		                      		<textarea name="daylogList_delayReason" type="text"  rows="6" style="width:99%"><s:property value='delay_reason'/></textarea>
		                  	  	</s:if>
								<s:else>	                  	  	
		                      		<textarea name="daylogList_delayReason" style="background-color: #E3E3E3"  readonly='readonly' type="text"  rows="6" style="width:99%"><s:property value='delay_reason'/></textarea>
								</s:else>
	                      	</div>
                      	</td>
					</tr>
	                  <tr>
	                  	 <td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">确认工时说明</font></div></td>
	                     	<td colspan="7" nowrap bgcolor="#FFFFFF" >
			                     <div align="left">
			                     <!-- 项目经理级别以上可以编辑 -->
				                  	 <s:if test="#request.hasComfirmRight==true">
						             	<textarea name="daylogList_confirmReason" type="text"  rows="6" style="width:99%"><s:property value='confirmDesc'/></textarea>
				                  	 </s:if>
				                  	 <s:else>
						             	<textarea style="background-color: #E3E3E3" readonly="readonly" name="daylogList_confirmReason" type="text"  rows="6" style="width:99%"><s:property value='confirmDesc'/></textarea>
				                  	 </s:else>
			                     </div>
	                       </td>
	                  </tr>
	              </table>
	              </td>
	              </tr>
				</table>
		</s:iterator>
        </div>
        </div>
	</fieldset>
</td> 
</tr>
</table>
<br/>




<table width="100%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
 <tr>  
       <td>
             <fieldset width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%" >
                      <tr>
                        <td><s:text name="label.admin.content"/></td>
                        <td width="6%"><input type="button" value="新增" onClick="addAgain()"/></td>
                        <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
                        <!--<td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>-->
                      </tr>
                  </table>
          </td>  
  </tr>
	<td align="center"> 
	<br/>
 		<input type="button" name="ok" id ="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

