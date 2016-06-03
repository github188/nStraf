<!--本页面为add新版页面，修改日期2010-1-5，后台字段未改动1111-->
<!--本页面为add新版页面，修改日期2010-1-6，后台字段未改动，将工时工时span改为input-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<%@ include file="/inc/taglib.inc"%>
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
	<head></head>
	<script type="text/javascript">
		var globalTaskInProject;
		var globalOldDate='<%=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())%>';
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var iTaskTotalpd = 20;		// the total number of tasks
		
		var initialTable;

		
		
		/* function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
			if(window==window.parent){
		       window.close();
			}else{
			   parent.close();  
			}
		} */
		
		function checkMonth(){
			
		}
			
			
		
		
		
			
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
			/* var subtotal = $("#subtotal").val();
			if(subtotal <= 0){
				alert("工时要大于0哦");
				return;
			} */
			/* var subtotalId = $(".input_subtotal");
			var len = subtotalId.size();

			for(var i=0;i<len;i++){
				if (subtotalId[i] <= 0) {
					alert("工时不能比0还小哦");
					return;
				} 

			} */
			
			
			
			var sumWorkhours = $("#sumShow").html()*1
			if (sumWorkhours > 24) {
				alert("您当天日志总时长超过24小时,请重新确认工时");
				return ;
			}
			if(sumWorkhours > 8){
				if(!confirm("当天日志总时长超过8小时，确定提交么?")){
					return ;	
				}
			}
			
			var submitDate=$("input[name='submitCreateDate']").val();
			var taskdesc=$("#Taskdescription").val();
			taskdesc = taskdesc.replace(/(^\s*)|(\s*$)/g,'');
			if(taskdesc==""){
				alert("请填写任务描述");
				$("#Taskdescription").focus();
				return;
			}
			/* //验证是否创建本月日志
			var curMonth=true;
			try{
				if(submitDate.trim()!=''){
					var year=submitDate.split('-')[0];
					var clientYear=new Date().getYear();
					var date=submitDate.split('-')[1];
					var clientDate=new Date().getMonth() + 1;
					if(date*1!=clientDate*1 || year*1!=clientYear*1){
						curMonth=false;
					}
				}else{
					curMonth=false;
				}
			}catch(e){
				curMonth=false;
			}
			if(!curMonth){
				alert('只能添加本月日报!')
				return ;	
			} */
				
			//验证各项任务是否合法
			if(!validateTask()){
				return false;
			}
			
			window.returnValue=true;
			reportInfoForm.submit();
			//window.opener.location=window.opener.location;
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
				return false;
			}
			
			if(document.getElementsByName("subtotal").length >= iTaskTotalpd)
			{
				alert("哇，一天这么多任务，可惜本页面限制最多新增" + iTaskTotalpd + "项任务");
				return;
			}
			
			//获取初始化table
			var taskTable2 = initialTable.clone(true);  //复制初始化表格
			
			//给复制的表格中的控件name重命名，以便后表单提交
			//reNameTableTag($(taskTable2),iTaskIndex);//name貌似是只读的，改不了
			
			//匹配替换表格中的name，便于表单提交
			replaceTableTagName(taskTable2,iTaskIndex);
			
			//对序号进行重新赋值
			taskTable2.find("div[name^='taskIdDiv']").html(iTaskIndex+1);
			
			//遍历任务table，找到最后一个table，然后将克隆的table放到它后面
			var nowTableIndex=1;
			$("table[name='taskTable']").each(function(){
				if(nowTableIndex==$("table[name='taskTable']").length){
					$(this).after(taskTable2);//将复制的表格追加到后面
					return;
				}else{
					nowTableIndex++;			
				}
			});
			
			iTaskIndex++;    //将记录的序号数加1	
			
			listenProjectChange();
		}
		
		function changeShowDivSize(){
			var tableNum=$("table[name='taskTable']").size();
			var tableHight=$("table[name='taskTable']:first").height()*tableNum+15;
			if(tableHight>600){
				$(".scrollFeild").css("height","600");
			}else{
				$(".scrollFeild").css("height",tableHight);
			}
		}
		
		//验证各项任务是否合法
		function validateTask(){
			var curCount = 1;
			var iFuskTotal = $("table[name='taskTable']").length;       //获取当前的新增的记录数
			for(var ind=0; ind<iFuskTotal; ind++){
				var taskDivPtrS = document.getElementsByName("daylogList["+ind+"].desc")[0];
				var taskDivPtrJ = document.getElementsByName("daylogList["+ind+"].prjName")[0];
				var taskDivPtrPgr = document.getElementsByName("daylogList["+ind+"].finishRate")[0];
				var taskDivPtrST = document.getElementsByName("daylogList["+ind+"].subTotal")[0].value.trim();
				var taskDivptrStatu=document.getElementsByName("daylogList["+ind+"].statu")[0].value.trim();
				var taskDivDelayReason=document.getElementsByName("daylogList["+ind+"].delay_reason")[0].value.trim();
				var taskDivStatu=document.getElementsByName("daylogList["+ind+"].statu")[0].value.trim();
				//var taskDivptrReason=document.getElementsByName("daylogList["+ind+"].reason")[0].value.trim();
				if(taskDivPtrJ!=null)
				{
					if(taskDivPtrJ.value == "")
					{
						alert("请输入项目名称");
						return false;
					}
					if(taskDivPtrJ.value.trim().indexOf('--')==0)
					{
						alert("第"+ curCount +"项任务的项目名称请确认是否正确");
						taskDivPtrJ.value = "";
						return false;
					}
				}
				if(taskDivPtrPgr!=null)
				{
					if(taskDivPtrPgr.value.trim() == "0%")
					{
						alert("请选择第"+curCount+"项任务的完成情况");
						return false;
					}
				}
				if(taskDivPtrS!=null)
				{
					if(taskDivPtrS.value.trim() == "")
					{
						alert("请选择第"+curCount+"项任务的计划任务");
						return false;
					}
				}
				if(Math.round(taskDivPtrST*100)/100==0)
				{
					alert("请输入第"+curCount+"项任务的任务工时");
					return false;
				}
				if(taskDivPtrST.trim().match(/^\d+(\.\d+)?$/) == null){
			        alert("请确认第"+curCount+"项任务的任务工时是整数或小数!");
			        return false;
			    }
				if(taskDivStatu=="延迟" && taskDivDelayReason==""){
					alert("请输入第"+curCount+"项的延迟原因");
					return false;
				}
				curCount++;
			}
			return true;
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
				$(this).find("div[name^='taskIdDiv']").html(nowTaskTableIndex);
				//修改name属性
				DeletereplaceTableTagName($(this),nowTaskTableIndex-1);
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
		
	/* 	function validateInputInfo(){
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
			
			listenProjectChange();
			
		});
		
		// 添加对新增的元素事件绑定 
		$(document).on('click','.check_',function(ele){
			
			var ids = $(this);
			var ck = ids.prop('checked');
			console.log(ck);
			var textarea = ids.parent('div').parent('.input_label2').parent('.tr_tag').children('.child_td').children('div').children('textarea')
			console.log(textarea.length);
			if (ck) {

				textarea.val('请假状态哦');
				textarea.attr('readonly',true);
				//textarea.attr('disabled',true);
				/*console.log(ids.parent('div').parent('.input_label2').parent('.tr_tag').children('.child_td').children('div').children('textarea').val());
				*/
			}else{
				textarea.val('');
				textarea.attr('readonly',false);
			}				
			
		});
		
		
		
		
		
		
		
		function listenProjectChange(){
			//监听选中的项目，更新项目中人员列表
			$("select[name^='daylogList'][name$='prjName']").change(function(){
				updateTaskInProject($(this));		
			});
			
			//监听日期的变化，如果发生变化，应该修改所有任务列表，修改为对应日期的
			$("#submitCreateDate").change(function(){
				if(isAllowedChange($(this).val())){
					globalOldDate=$(this).val();
					$("select[name^='daylogList'][name$='prjName']").each(function(){
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
	            //showAnim:"slide",       //显示日历的效果slide、fadeIn、show等  
	            changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
	            changeYear:true        //是否显示年份的下拉菜单，默认为false  
	            //showWeek:true,          //是否显示星期,默认为false  
	            //showButtonPanel:true,   //是否显示取消按钮，并含有today按钮，默认为false  
	            //closeText:'close'      //设置关闭按钮的值  
	            //yearRange:'2010:2012',  //显示可供选择的年份  
	            //defaultDate:+7          //表示默认日期是在当前日期加上7天  
	         });
			
			//铲鲟计划任务变化是，修改任务列表中的数据
			var index = iTaskIndex-1;
			/*$('input:radio[name="daylogList['+index+'].tasktype"]').change(function(){
				getTaskByType($(this));		
			});*/
			$("input[typeGroup='tasktypeRadio']").change(function(){
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
			var oldTime = new Date(oldDate.replace("-","/")); 
			var newTime = new Date(newDate.replace("-","/")); 
			var diff = parseInt((newTime - oldTime) / (1000*60*60*24)); 
			if(diff<-7){
				return false;
			}else{
				return true;
			}
		}
		
		//获取用户选择项目中指定日期的任务
		function updateTaskInProject(projectSelect){
			var type="0";
			var index = iTaskIndex-1;
			projectSelect.parent().parent().next().find('input:radio[name="daylogList['+index+'].tasktype"]').each(function(){
				if($(this).val()=="0" && $(this).is(':checked'))
					type="0";
				if($(this).val()=="1" && $(this).is(':checked'))
					type="1";
				if($(this).val()=="2" && $(this).is(':checked'))
					type="2";
			});
			var projectId=projectSelect.val();
			var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!getTaskInProject.action?projectId="+projectId;
			//加上查询某个日期的任务
			actionUrl+="&queryDate="+$("#submitCreateDate").val()+"&type="+type;
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
					projectSelect.parent().parent().next().next().find("select[typeGroup='taskSelect']").each(function(){
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
						//$(this).width($(this).parent().width());
						$(this).html(html);
					});
				},
				error : function(data, data1) {
					alert("获取任务错误!");
				}
			});	
		}
		
		//计算总工时
		function calculateSumShow(){
			var sum=0;
			$("input[name^='daylogList'][name$='subTotal']").each(function(){
				sum+=$(this).val()*1;
			});
			$("#sumShow").html(sum);
		}
		
		//由于js的table对象的name属性不能直接替换，这里变相的通过替换table中的html字符串中对应的name来实现
		function replaceTableTagName(taskTable,tableIndex){
			var html=taskTable.html();
			//替换checkbox的name值,作用：把taskCkd[0]替换为taskCkd[1]或taskCkd[2]...
			html=html.replace(new RegExp("taskCkd\\[[0-9]+\\]","g"),"taskCkd["+tableIndex+"]");
			//序号DIV
			html=html.replace(new RegExp("taskIdDiv\\[[0-9]+\\]","g"),"taskIdDiv["+tableIndex+"]");
			//项目名称
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.prjName","g"),"daylogList["+tableIndex+"].prjName");
			//类别
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.type","g"),"daylogList["+tableIndex+"].type");
			//状态
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.statu","g"),"daylogList["+tableIndex+"].statu");
			//完成百分比
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.finishRate","g"),"daylogList["+tableIndex+"].finishRate");
			//计划新增
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.planOrAdd","g"),"daylogList["+tableIndex+"].planOrAdd");
			//工时
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.subTotal","g"),"daylogList["+tableIndex+"].subTotal");
			//确认工时
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.confirmHour","g"),"daylogList["+tableIndex+"].confirmHour");
			//描述
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.desc","g"),"daylogList["+tableIndex+"].desc");
			//交付件
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.fileName","g"),"daylogList["+tableIndex+"].fileName");
			//延迟原因
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.reason","g"),"daylogList["+tableIndex+"].reason");
			//查询方式
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasktype","g"),"daylogList["+tableIndex+"].tasktype");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasksdate","g"),"daylogList["+tableIndex+"].tasksdate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.taskedate","g"),"daylogList["+tableIndex+"].taskedate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.searchTaskBut","g"),"daylogList["+tableIndex+"].searchTaskBut");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.delay_reason","g"),"daylogList["+tableIndex+"].delay_reason");
			html=html.replace(new RegExp("sdate\\[[0-9]+\\]","g"),"sdate["+tableIndex+"]");
			html=html.replace(new RegExp("edate\\[[0-9]+\\]","g"),"edate["+tableIndex+"]");
			
			//最后赋值给table
			taskTable.html(html);
		}
		
		/* *
		**
		//由于js的table对象的name属性不能直接替换，这里变相的通过替换table中的html字符串中对应的name来实现
		delAgain()时,兼容fireFox和chrome,删除一个任务时,对taskTable赋值到HTML 
		*/ 
		function DeletereplaceTableTagName(taskTable,tableIndex){
			/* **********
			兼容fireFox和chrome,删除一个任务时,对taskTable赋值到HTML 
			*/
			//项目名称
			var prjNameValue = $("select[name^='daylogList['][name$='.prjName']")[tableIndex].value;
			//类别
			var typeValue = $("select[name^='daylogList['][name$='.type']")[tableIndex].value;
			//状态
			var statuValue = $("select[name^='daylogList['][name$='.statu']")[tableIndex].value;
			//完成%
			var finishRateValue = $("select[name^='daylogList['][name$='.finishRate']")[tableIndex].value;
			//计划/新增
			var planOrAddValue = $("select[name^='daylogList['][name$='.planOrAdd']")[tableIndex].value;
			//工时
			var subTotalValue = $("input[name^='daylogList['][name$='.subTotal']")[tableIndex].value;
			//查询方式
			var dayTasktypeValue = $("input[name^='daylogList['][name$='.tasktype']")[0].checked;//本日
			var weekTasktypeValue = $("input[name^='daylogList['][name$='.tasktype']")[1].checked;//本周
			var dateTasktypeValue = $("input[name^='daylogList['][name$='.tasktype']")[2].checked;//指定日期
			var tasksdateValue = $("input[name^='daylogList['][name$='.tasksdate']")[tableIndex].value;//开始日期
			var taskedateValue = $("input[name^='daylogList['][name$='.taskedate']")[tableIndex].value;//结束日期
			//计划任务
			var descValue = $("select[name^='daylogList['][name$='.desc']")[tableIndex].value;
			//任务描述
		 	var reasonValue = $("textarea[name^='daylogList['][name$='.reason']")[tableIndex].value;
			//延迟原因
		 	var delay_reasonValue = $("textarea[name^='daylogList['][name$='.delay_reason']")[tableIndex].value;
			var html=taskTable.html();
			//替换checkbox的name值,作用：把taskCkd[0]替换为taskCkd[1]或taskCkd[2]...
			html=html.replace(new RegExp("taskCkd\\[[0-9]+\\]","g"),"taskCkd["+tableIndex+"]");
			//序号DIV
			html=html.replace(new RegExp("taskIdDiv\\[[0-9]+\\]","g"),"taskIdDiv["+tableIndex+"]");
			//项目名称
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.prjName","g"),"daylogList["+tableIndex+"].prjName");
			//类别
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.type","g"),"daylogList["+tableIndex+"].type");
			//状态
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.statu","g"),"daylogList["+tableIndex+"].statu");
			//完成百分比
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.finishRate","g"),"daylogList["+tableIndex+"].finishRate");
			//计划新增
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.planOrAdd","g"),"daylogList["+tableIndex+"].planOrAdd");
			//工时
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.subTotal","g"),"daylogList["+tableIndex+"].subTotal");
			//描述
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.desc","g"),"daylogList["+tableIndex+"].desc");
			//交付件
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.fileName","g"),"daylogList["+tableIndex+"].fileName");
			//延迟原因
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.reason","g"),"daylogList["+tableIndex+"].reason");
			//查询方式
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasktype","g"),"daylogList["+tableIndex+"].tasktype");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.tasksdate","g"),"daylogList["+tableIndex+"].tasksdate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.taskedate","g"),"daylogList["+tableIndex+"].taskedate");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.searchTaskBut","g"),"daylogList["+tableIndex+"].searchTaskBut");
			html=html.replace(new RegExp("daylogList\\[[0-9]+\\]\\.delay_reason","g"),"daylogList["+tableIndex+"].delay_reason");
			html=html.replace(new RegExp("sdate\\[[0-9]+\\]","g"),"sdate["+tableIndex+"]");
			html=html.replace(new RegExp("edate\\[[0-9]+\\]","g"),"edate["+tableIndex+"]");
			
			//最后赋值给table
			taskTable.html(html);
			
			//项目名称
			$("select[name^='daylogList['][name$='.prjName']")[tableIndex].value = prjNameValue;
			//类别
			$("select[name^='daylogList['][name$='.type']")[tableIndex].value = typeValue;
			//状态
			$("select[name^='daylogList['][name$='.statu']")[tableIndex].value = statuValue;
			//完成%
			$("select[name^='daylogList['][name$='.finishRate']")[tableIndex].value = finishRateValue;
			//计划/新增
			$("select[name^='daylogList['][name$='.planOrAdd']")[tableIndex].value = planOrAddValue;
			//工时
			$("input[name^='daylogList['][name$='.subTotal']")[tableIndex].value = subTotalValue;
			//查询方式
			$("input[name^='daylogList['][name$='.tasktype']")[0].checked = dayTasktypeValue;//本日
			$("input[name^='daylogList['][name$='.tasktype']")[1].checked = weekTasktypeValue;//本周
			$("input[name^='daylogList['][name$='.tasktype']")[2].checked = dateTasktypeValue;//指定日期
			$("input[name^='daylogList['][name$='.tasksdate']")[tableIndex].value = tasksdateValue;//开始日期
			$("input[name^='daylogList['][name$='.taskedate']")[tableIndex].value = taskedateValue;//结束日期
			//计划任务
			$("select[name^='daylogList['][name$='.desc']")[tableIndex].value = descValue;
			//任务描述
		 	$("textarea[name^='daylogList['][name$='.reason']")[tableIndex].value = reasonValue;
			//延迟原因
		 	$("textarea[name^='daylogList['][name$='.delay_reason']")[tableIndex].value = delay_reasonValue;
			
		}
		
		
		
		
		
		//按周或按月查询计划任务列出计划任务
		function getTaskByType(taskType){
			var type = taskType.val();
			if(type=="2"){
				var obj = taskType.parent().next();
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
			var actionUrl = "<%=request.getContextPath()%>/pages/daylog/logInfo!getTaskInProject.action?projectId="+projectId+"&type="+type;
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
		
		function checkPrj(object){
			var  prjNameid=$("#prjName").find("option:selected").val();
			var  prjlength=$("#prjName").find("option").length;
			if(prjlength==1){
				$("#Taskdescription").focus();
				return;
			}
			if(prjlength>1&& prjNameid=="ff8080814876bd3c0148792d75fb01d7"){
				if(confirm('归纳为其他项目的工作量是不纳入项目投入成本统计，请根据实际项目情况慎重选择')){
					$("#Taskdescription").focus();
					return;
				}
			}
		}
		
		function timeValidate(){
			//var createDate=$("#submitCreateDate").val();
			var dateId = $("#submitCreateDate");
			var createDate=dateId.val();
			var now=new Date();
			var year=now.getFullYear();
			var month=now.getMonth() + 1;
			var day=now.getDate();
			var currentTime = year+"-"+month+"-"+day;
			var selectDate = new Date(createDate.replace(/\-/g, "\/")); 
			var currentDate = new Date(currentTime.replace(/\-/g, "\/"));  
			
			var array = new Array();
			array = createDate.split("-");
			
			if(selectDate != "" && currentDate != "" && selectDate > currentDate) 
			{ 
				/**
				   *优化日志日期问题：防止全部信息填写完后再去修改成之后的日期（之前这样可提交，但任务描述会被禁用且为""）
				   *@author zzwen6 
				   *@date 2016年3月24日9:48:28
				   */
				  alert("新增失败：当前只能填写今天或者过去的日志,请修改日志日期。"); 
				  dateId.val(currentTime);
			
			}else if((year == array[0]) && (month > array[1])){
				/**
				   *优化日志日期问题：当月日志当月写
				   *@author zzwen6 
				   *@date 2016年3月24日9:48:28
				   */
				 alert("新增失败，当前只能填写本月的日志，不可新增上月日志");
				 dateId.val(currentTime);
			}else if(year > array[0]){
				 alert("不可跨年新增日志");
				 dateId.val(currentTime);
			}else if(selectDate != "" && currentDate != "" && selectDate <= currentDate){
				 $("#Taskdescription").attr("disabled",false);
			}
		}
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10">
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/daylog/logInfo!save.action" method="post">
<table width="100%" align="center"  cellPadding="1" cellSpacing="1" >
<tr>
<td>
        <table class="input_table">
        <tr>
		<td class="input_tablehead"><s:text name="label.newTermInfo" /></td>
        </tr>
        	<tr>
        		<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">姓名：<s:property value='#request.usr.username'/></div></b></td> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
        	 	<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">部门：<s:property value='#request.deptNameStr'/></div></b></td> 
        	 	<!-- <td align="left" width="3%" nowrap="nowrap"></td>  --> 
        	 	<!-- <td align="left" width="10%" nowrap="nowrap"><div align="left" style="padding-top:4">级别：<s:property value='#request.userLevel'/></div></td> --> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
            	<td align="right" nowrap="nowrap"></td>  
            	<td align="right" width="10%" nowrap="nowrap"><b><div align="right'" style="padding-top:4">日期：</div></b></td>  
                <td align="left" width="20%"><div align="left">
                	<input   name="submitCreateDate" type="text" id="submitCreateDate" onchange="timeValidate();" size="12" style="text-align: left;" value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>' readonly="true" class="input_readonly"> 
                </div></td>
            	<td align="right" width="10%" nowrap="nowrap">
	            	 <div align="right">工时总计 <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
        </table>
        <div style="height:500px;overflow:auto;" >
        <div class="scrollFeild" style="height:200px;">
		<table name='taskTable' width="100%" class="input_table">
        	<tr>
				<td bgcolor="#FFFFFF"><input type="checkbox" name="taskCkd[0]"/> <div align="center" name='taskIdDiv[0]'>1</div></td>
                <td bgcolor="#FFFFFF">
        		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
   		    		<tr >
                          <td colspan="2" class="column_label" width="30%"><div align="center"><font color="#000000">项目名称</font><font color="#FF0000">*</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">类别</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">状态</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">完成%</font><font color="#FF0000">*</font></div></td>
                          <td class="column_label" width="20%"><div align="center"><font color="#000000">计划/新增</font></div></td>
                          <td class="column_label" width="10%"><div align="center"><font color="#000000">工时</font><font color="#FF0000">*</font></div></td>
                    </tr>
                    <tr>
                       	<td colspan="2" nowrap bgcolor="#FFFFFF" >
                       		<select style="font-size: 12px;width:100%" id="prjName" name="daylogList[0].prjName" typeGroup="prjNameSelect"  onchange=checkPrj(this)>
								<s:iterator  value="#request.projects" id="ele" status="row">
								<option value='<s:property value="id" />'><s:property value="name" /></option>
								</s:iterator>
							</select>
                       	</td>
				        <td nowrap bgcolor="#FFFFFF">
                            <select name="daylogList[0].type"  >
	                            <option value="日常工作" selected="true">日常工作</option>
	                            <option value="需求">需求</option>
	                            <option value="设计">设计</option>
	                            <option value="编码">编码</option>
	                            <option value="测试">测试</option>
	                            <option value="管理">管理</option>  
	                            <option value="文档">文档</option>
	                            <option value="会议">会议</option>
	                            <option value="培训">培训</option>      
	                            <option value="其他">其他</option>    
                           	</select>
                           </td>
                           <td nowrap bgcolor="#FFFFFF">
                            <select name="daylogList[0].statu"  typeGroup="statuSelect">
	                            <option value="按时" selected="true">按时</option>
	                            <option value="延迟">延迟</option>
	                            <option value="提前">提前</option>
	                            <option value="取消">取消</option>        
                           	</select>
                           </td>
                           <td nowrap bgcolor="#FFFFFF">
                            <select name="daylogList[0].finishRate" >
	                            <option value="100%" selected="true">100%</option>
	                            <option value="95%">95%</option>
	                            <option value="90%">90%</option>
	                            <option value="85%">85%</option>
	                            <option value="80%">80%</option>
	                            <option value="75%">75%</option>
	                            <option value="70%">70%</option>
	                            <option value="65%">65%</option>
	                            <option value="60%">60%</option>
	                            <option value="55%">55%</option>
	                            <option value="50%">50%</option>
	                            <option value="45%">45%</option>
	                            <option value="40%">40%</option>
	                            <option value="35%">35%</option>
	                            <option value="30%">30%</option>
	                            <option value="25%">25%</option>
	                            <option value="20%">20%</option>
	                            <option value="15%">15%</option>
	                            <option value="10%">10%</option>
	                            <option value="5%">5%</option>
	                            <option value="0%">0%</option>
                            </select>
                      </td>
                      <td nowrap bgcolor="#FFFFFF">
                         <div align="left">
                         <select name="daylogList[0].planOrAdd"  >
                            <option value="计划" selected="true">计划</option>
                            <option value="新增">新增</option>
                         </select>
                         </div>
                      </td>
                      <td nowrap bgcolor="#FFFFFF" align="center" class = "">
                      	<div align="center" class = "">
                      		<input class = "input_subtotal" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'');calculateSumShow()" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')" 
                      		name="daylogList[0].subTotal" type="text" size="6" maxlength="4" value="" style="color:#FF0000"  text-align:center>
                      		</div>
                      	</td>
                  </tr>
                  <tr>
                      <td class="input_label2"><div align="center"><font color="#000000">查询方式</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" >
                      	<div align="left" style="float:left;">
	                      	<input name="daylogList[0].tasktype" type="radio" value="0" checked typeGroup="tasktypeRadio">本日
	                      	<input name="daylogList[0].tasktype" type="radio" value="1" typeGroup="tasktypeRadio">本周
	                      	<input name="daylogList[0].tasktype" type="radio" value="2" typeGroup="tasktypeRadio">指定日期
                      	</div>
                      	<div id="dateTask" style="text-align:left;display:none;"><!-- class="MyInput" -->
                      		<input name="daylogList[0].tasksdate" type="text" id="sdate[0]" size="15" value="" typeGroup="sdateCon">至<input name="daylogList[0].taskedate" type="text" id="edate[0]"  size="15"  value="" typeGroup="edateCon">
                      		<input type="button" id="testTask" name="daylogList[0].searchTaskBut" value="查询" onclick="searchTaskBut(this)" style="height:20px;">
                      		<font color="red">请填写日期进行查询</font>
                      	</div>
                      </td>
                  </tr>
                  <tr>
                      <td class="input_label2"><div align="center"><font color="#000000">计划任务</font><font color="#FF0000">*</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" ><div align="left">
                      	<select typeGroup="taskSelect" style="width:100%;" name="daylogList[0].desc" id="taskDesc" >
                      		<s:iterator  value="#request.tasks" id="ele" status="row">
							<option value='<s:property value="id" />'><s:property value="taskContent" /><s:if test='#ele.priority!=null'>(优先级:<s:property value="priority" />)&nbsp;&nbsp;&nbsp;&nbsp;开始时间:<s:property value="desc" /></s:if></option>
							</s:iterator>
                      	</select>
                      </div></td>
                  </tr>
                  <tr class = 'tr_tag'>
                  	 <td class="input_label2">
                  	 	<div align="center" >
                  	 		<font color="#000000">任务描述</font><font color="#FF0000">*</font></div>
                  	 	  <%-- --%> <div align="center" >
                  	 		<input type='checkbox'  name = 'daylogList[0].isLeave' id = 'leave' class = 'check_'  >
                  	 		<span><font color='#f00806'>请假</font></span>
                  	 	</div>  
                  	 
                  	 </td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" width="100%" class = 'child_td'><div align="left" >
                      	<textarea name="daylogList[0].reason"  style="width:100%;" type="text"  rows="4" style="width:99%" id="Taskdescription" ></textarea>
                      </div></td>
                  </tr>
                  <tr id="delayReasonTr" style="display:none;">
                  	 <td class="input_label2"><div align="center" ><font color="#000000">延迟原因</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<textarea name="daylogList[0].delay_reason"  style="width:100%;" type="text"  rows="4" style="width:99%" ></textarea>
                      </div></td>
                  </tr>
              </table>
              </td>
              </tr>
			</table>
		</div>
		</div>
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
              </fieldset>
          </td>  
  </tr>
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

