<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/calendar.inc"%><%-- 日志控件 --%>
<%@ include file="/inc/pagination.inc"%>
<html>
<script type="text/javascript" src="../../js/plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script><!-- 日志控件-->

<script language="JavaScript">


$(function(){
	//jqueryui的日期控件 
	$(".MyInput").datepicker({  
        dateFormat:'yy-mm-dd',  //更改时间显示模式  
        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
        changeYear:true        //是否显示年份的下拉菜单，默认为false  
     }); 
	$("#projectPersonDayMonth").focus(function(){  
		WdatePicker({ dateFmt: 'yyyy-MM',isShowClear:false,readOnly:true });
	 }); 
	
	var nowDate = new Date();
	//$("#operateTime").value = nowDate.toLocaleDateString();
	//nowDate = nowDate.toLocaleDateString();
	//nowDate = (nowDate.split('/')).join('-');//"yyyy/MM/dd"转"yyyy-MM-dd"格式
	
	  /* nowDate = nowDate.toJSON();
	 document.getElementById("operateTime").value = nowDate.substr(0,10); */
	 
	 document.getElementById("operateTime").value = getNowFormatDate()
});

//当前时间格式化为yyyy-MM-dd
function getNowFormatDate() 
{ 
	var day = new Date(); 
	var Year = 0; 
	var Month = 0; 
	var Day = 0; 
	var CurrentDate = ""; 
	//初始化时间 
	Year= day.getFullYear();//ie火狐下都可以 
	Month= day.getMonth()+1; 
	Day = day.getDate(); 
	CurrentDate += Year + "-"; 
	if (Month >= 10 ) 
	{ 
	CurrentDate += Month + "-"; 
	} 
	else 
	{ 
	CurrentDate += "0" + Month + "-"; 
	} 
	if (Day >= 10 ) 
	{ 
	CurrentDate += Day ; 
	} 
	else 
	{ 
	CurrentDate += "0" + Day ; 
	} 
	return CurrentDate; 
} 


//签到数据
function exeSign(){
	var url="<%=request.getContextPath()%>/pages/common/tool!exeSign.action";
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="签到数据  ";
			if(data=="true" || data==true){
				html+="验证成功";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="验证失败";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

//人日数据 人日统计模块
function exeProjectPersonday(){
	var operateTime = $("#projectPersonDayMonth").val();
	if(operateTime==''){
		alert("请在右侧选择选择要统计的月份!");
		return false;
	}
	var url="<%=request.getContextPath()%>/pages/common/tool!exePersonday.action?operateTime="+operateTime;
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="人日统计模块数据  ";
			if(data=="true" || data==true){
				html+="同步成功";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="同步失败";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

//人日数据 领导首页  按部门
function exePersondayByLeader(){
	var operateTime = $("#operateTime").val();
	if(operateTime==''){
		alert("请选择日期!");
		return false;
	}
	var url="<%=request.getContextPath()%>/pages/common/tool!exePersondayByLeader.action?operateTime="+operateTime;
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="领导首页人日数据  ";
			if(data=="true" || data==true){
				html+="同步成功";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="同步失败";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

//月度管理报告
function exeMonthly(){
	var operateTime = $("#operateTime").val();
	if(operateTime==''){
		alert("请选择日期!");
		return false;
	}
	operateTime = (operateTime.split('-')).join('');//"yyyy-MM-dd"转"yyyyMMdd"格式
	operateTime = operateTime.substr(0,6);//截取字符串
	var url="<%=request.getContextPath()%>/pages/common/tool!exeMonthly.action?operateTime="+operateTime;
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="月度管理报告数据  ";
			if(data=="true" || data==true){
				html+="同步成功";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="同步失败";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

//修复当月考勤数据
function exeRepairAttendance(){
	var url="<%=request.getContextPath()%>/pages/common/tool!repairAttendance.action";
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="当月考勤数据  ";
			if(data=="true" || data==true){
				html+="修复成功";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="修复失败";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

//统计部门人日成本
function costControl(){
	var begin = $("#costControlBegin").val();
	var end =$("#costControlEnd").val();
	if(begin=='' || end==''){
		alert("请在按钮右侧选择起始和截至时间!");
		return false;
	}
	var url="<%=request.getContextPath()%>/pages/common/tool!costControl.action?begin="+begin+"&end="+end;
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="指定时间段部门人日成本  ";
			if(data=="true" || data==true){
				html+="统计完成";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="统计出错";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});   
}

function costConfirmHour(){
	var begin = $("#confirmStartDate").val();
	var end =$("#confirmEndDate").val();
	if(begin=='' || end==''){
		alert("请在按钮右侧选择起始和截至时间!");
		return false;
	}
	var url="<%=request.getContextPath()%>/pages/common/tool!costConfirmHour.action?begin="+begin+"&end="+end;
	$.ajax({
		type:"post",
		url:url,
		dataType:"json",
		cache: false,
		async:true,
		success:function(data){
			var html="指定时间段日志确认工时  ";
			if(data=="true" || data==true){
				html+="统计完成";
				$("#showInfo").css("color","green");
			}else if(data=="false" || data==false){
				html+="统计出错";
				$("#showInfo").css("color","red");
			}
			$("#showInfo").html(html);
		},
		error:function(data){
			alert("请求错误:"+data);
		}
	});  
}

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var actionUrl = "<%=request.getContextPath()%>/pages/project/project!refresh.action?form=refresh&pageNum="+pageNum;
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	
	<%
	int j = 0;//记录的索引
	int k = 1;
	%>
	
}


</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
		<br>
	   <span style="font-size:13px;">选择执行时间:</span> <input class="MyInput" readonly="true" id="operateTime" type="text"  size="11" value="">
		<br>
		<span style="font-size:13px;">处理状态:</span><span id="showInfo" style="width:100%;font-size:13px;" ></span>
		<br>
		<input type="button" value="同步人日数据(领导首页部门人日)" onclick="exePersondayByLeader()">
		<br>
		 <input type="button" value="签到地址及考勤状态分析" onclick="exeSign()"> 
		 <br>
		 <input type="button" value="执行月度管理报告分析定时器"  onclick="exeMonthly()"> 
		 <br>
		 <input type="button" value="修复当月考勤数据" onclick="exeRepairAttendance()"> 
		 <br>
		 <hr>
		  <br>
		 <input type="button" value="指定月份项目人日统计(人日统计模块)"  onclick="exeProjectPersonday()"> <input readonly="true" id="projectPersonDayMonth" type="text"  size="11" value="">
		 <br>
		 <input type="button" value="指定时间段日志确认工时计算" onclick="costConfirmHour()"> <input class="MyInput" readonly="true" id="confirmStartDate" type="text"  size="11" value=""> 至 <input class="MyInput" readonly="true" id="confirmEndDate" type="text"  size="11" value="">
		 <br>
		 <input type="button" value="指定时间段部门成本总体情况和部门人员详情统计" onclick="costControl()">  <input class="MyInput" readonly="true" id="costControlBegin" type="text"  size="11" value=""> 至 <input class="MyInput" readonly="true" id="costControlEnd" type="text"  size="11" value="">
	</body>
</html>
