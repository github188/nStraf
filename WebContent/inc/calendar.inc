<%@ page contentType="text/html; charset=UTF-8" %>
<script language='JavaScript' src="../../js/jquery-1.11.0.js"></script>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script language='JavaScript' src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script language='JavaScript' src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script language='JavaScript' src="../../plugin/jqueryui/js/jquery-ui-timepicker-addon.js"></script>
<script language="javascript">
$(function(){
	$(".MyInput,.dateTimeInput,.timeInput").css("border","1px solid");
	$(".MyInput,.dateTimeInput,.timeInput").css("height","22px");
	$(".MyInput,.dateTimeInput,.timeInput").attr("readonly","readonly")
	$(".MyInput").css("width","80px");
	//jqueryui的日期控件 
	$(".MyInput").datepicker({  
        dateFormat:'yy-mm-dd',  //更改时间显示模式  
        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
        changeYear:true        //是否显示年份的下拉菜单，默认为false  
     });
    //jqueryui的日期时间控件 
    $(".dateTimeInput").css("width","150px");
	$(".dateTimeInput").datetimepicker({
		timeFormat: 'HH:mm:ss',
		hour:8,
		minute: 0,
		stepHour: 1,
		stepMinute: 1,
		stepSecond: 5,  
        dateFormat:'yy-mm-dd',  //更改时间显示模式  
        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
        changeYear:true,        //是否显示年份的下拉菜单，默认为false  
        timeText: '时间',
		hourText: '小时',
		minuteText: '分钟',
		secondText: '秒',
		currentText: '今天', 
		closeText: '关闭'
     });
     //jqueryui的时间控件 
    $(".timeInput").css("width","50px");
	$(".timeInput").timepicker({
		currentText: '现在',
		closeText: '关闭',
		hour: 8,
		minute: 0,
		stepMinute: 10,
		timeFormat: 'HH:mm',
		timeOnlyTitle: '时间选择',
		timeText: '时间',
		hourText: '小时',
		minuteText: '分钟'
     });
})
function ShowDateTime(ctrlobj)
{ 
  ShowCalendar(ctrlobj,1)	
}
function ShowDate(ctrlobj)
{
  ShowCalendar(ctrlobj,2)	
}
function ShowTime(ctrlobj)
{ 
  ShowCalendar(ctrlobj,3)	
}     
function ShowCalendar(ctrlobj,ptype)
{
	//由于页面的日期控件部分浏览器不兼容，如下代码屏蔽掉
  /*var RaletiveJsurl="<%=request.getContextPath()%>/calendar"	
  var showx = event.screenX;
  var showy = event.screenY;
  if(ptype == 1){
	  url=RaletiveJsurl+"/datetime.jsp";
	  width = 310;
	  height = 240;
	}else if(ptype == 2){
      url=RaletiveJsurl+"/date.jsp";
	  width = 310;
	  height = 220;
  }else if(ptype == 3){
      url=RaletiveJsurl+"/time.jsp";
	  width = 310;
	  height = 60;
  }
	if(showx + width >screen.width){
		showx = showx - width;
	}
	if(showy + height + 10 >screen.height){
		showy = showy -height -10;
	}else{
		showy = showy + 10;
	}
   var feather = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scroll:no;Resizable:no;help:0;"  
   window.showModalDialog(url,ctrlobj,feather );*/
}
</script>