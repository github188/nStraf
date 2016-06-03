var AJAX_REQUEST_URL = "/commonPlatform/personalHome/personalHomeInfo!getPersonalHomeInfo4Web";
var CONTENT_HEIGHT = 360;
var MONTH_NAMES = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
var DAYLOG_NONE_COLOR = "#FFFFFF";
var DAYLOG_NORMAL_COLOR = "#00FF00";
var DAYLOG_DELAY_COLOR = "#FF0000";
var PROGRESS_BAR_W = 100;
var PROGRESS_BAR_H = 20;

var yyyyMM = "";
$(function(){
	loadPersonalHomeInfo();
	$("#tips-shade").click(function(){
		$("#tips-shade").hide();
		$("#tips").hide();
	});
});

/**
 * 进行rgb与16位色值之间的转换 
 * @param rgb rgb(255,0,0)
 * @returns {String} #ff0000
 */
function rgbToHex(rgb){ 
   var regexp = /[0-9]{0,3}/g;  
   var re = rgb.match(regexp);//利用正则表达式去掉多余的部分，将rgb中的数字提取
   var hexColor = "#"; 
   var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];  
   for (var i = 0; i < re.length; i++) {
        var r = null, c = re[i], l = c; 
        var hexAr = [];
        while (c > 16){  
              r = c % 16;  
              c = (c / 16) >> 0; 
              hexAr.push(hex[r]);  
         } hexAr.push(hex[c]);
         if(l < 16&&l != ""){        
             hexAr.push(0);
         }
       hexColor += hexAr.reverse().join(''); 
    }  
   return hexColor;  
}

/**
 * 加载数据
 */
function loadPersonalHomeInfo(){
	doAjaxRequest(AJAX_REQUEST_URL);
}

/**
 * 请求后台的数据
 * @param ajaxRequestURL
 */
function doAjaxRequest(ajaxRequestURL){
	//重新加载数据
	var actionUrl = "<%=request.getContextPath()%>/pages/" + ajaxRequestURL;
	actionUrl=encodeURI(actionUrl);
	$.ajax({
		url : actionUrl,
		data : {},
		type : 'POST',
		dataType : 'json',
		timeout : 30000,
		success:function(data){
			var daylog = data.daylogList;
			setDaylogInfoHTML(daylog);
			var attendance = data.attendanceList;
			setAttendanceInfoHTML(attendance);
			var pointsList = data.integralList;
			setPointListHTML(pointsList);
			var workhoursList = data.workhoursList;
			setWorkHoursInfoHTML(workhoursList);
			var taskProgressList = data.taskProgressList;
			setTaskProgressHTML(taskProgressList);
		},
		error : function(xhr,textStatus) {
			alert("请求数据失败!" + textStatus);
		}
	});
}

/**
 * 得到年月字符串
 * @param date
 * @returns {String}
 */
function getYearMonth(date){
	var	month = parseInt(date.substring(5,7),10);
	var	year = parseInt(date.substring(0,4),10);
	var yyyyMM = year + "年" + month + "月";
	return yyyyMM;
}

/**
 * 以日历形式显示个人日志填写情况
 * @param data
 */
function setDaylogInfoHTML(data) {
	var info = data;
	//日历中整月的日志填写情况
	var daylogArray = new Array();
	//取出月份
	yyyyMM = getYearMonth(info[0].date);
	for (var i = 0; i < info.length; i++) {
		var dateInDay = {};		
		dateInDay.start = info[i].date;
		dateInDay.end = info[i].date;
		dateInDay.overlap = false;
		var status = info[i].status;
		dateInDay.rendering = 'background';
		if (status==-1) {
			//延迟填写
			dateInDay.color = DAYLOG_DELAY_COLOR;
		} else if (status==0){
			//正常填写
			dateInDay.color = DAYLOG_NORMAL_COLOR;
		}else {
			//未填写
			dateInDay.color = DAYLOG_NONE_COLOR;
		}
		daylogArray.push(dateInDay);
	}
	//显示日历中的数据
	var ele = $("#daylogInfo");
	ele.fullCalendar({
		theme : false,
		header: {
			left : 'prev',
			center : 'title',
			right : 'next'
		},
		height: CONTENT_HEIGHT,
		monthNames : MONTH_NAMES,
		dayClick: function(date, allDay, jsEvent, view) {
		},
		eventClick:function(calEvent) {
		},
		events: daylogArray
	});
	//如果是月初则显示上月的数据的日历
	showCalendarByDate(ele);
	resetCalendarStyle();
}

/**
 * 日历样式的再调整
 */
function resetCalendarStyle(){
	var bgevent = $(".fc-bgevent");
	for (var index = 0; index < bgevent.length; index++) {
		var item = bgevent.get(index);
		var bgColor = item.style.backgroundColor;
		var hexColor = DAYLOG_NONE_COLOR;
		if(!bgColor.match("#")){
			hexColor = rgbToHex(bgColor);
		}else{
			hexColor = bgColor;
		}
		if (hexColor.toUpperCase() == DAYLOG_DELAY_COLOR) {
			item.style.backgroundImage = "url('images/home/daylog-delay.png')";
		} else if (hexColor.toUpperCase() == DAYLOG_NORMAL_COLOR) {
			item.style.backgroundImage = "url('images/home/daylog-normal.png')";
		}
		item.style.backgroundColor = DAYLOG_NONE_COLOR;
		item.style.backgroundRepeat = "no-repeat";
		item.style.backgroundPosition = "center";
	}
}

/**
 * 以日历形式显示考勤
 * @param data
 */
function setAttendanceInfoHTML(data) {
	var info = data;
	//日历中整月的样式的设定值
	var dateInMonth = new Array();
	//日历中整月的考勤时间
	var attendanceArray = new Array();
	for(var i = 0; i < info.length; i++){
		var dateInDay = {};		
		//取每日的样式		
		dateInDay.start = info[i].signDay;
		dateInDay.end = info[i].signDay;
		dateInDay.overlap = false;
		dateInDay.rendering = 'background';
		var statusEntry = info[i].attendanceStatusEntry;//迟到状态判断
		var statusExit = info[i].attendanceStatusExit;//早退状态判断
		if ((statusEntry == 3) || (statusEntry == 4)||(statusExit == 3) || (statusExit == 4)) {
			dateInDay.color = DAYLOG_DELAY_COLOR;
		} else if((statusEntry == 1) || (statusEntry == 2)||(statusExit == 1) || (statusExit == 2)){
			dateInDay.color = DAYLOG_NORMAL_COLOR;
		} else {
			dateInDay.color = DAYLOG_NONE_COLOR;
		}
		dateInMonth.push(dateInDay);
		//取考勤时间
		var attendanceDetail = {};
		attendanceDetail.time = info[i].entryTime + "-" + info[i].exitTime;
		attendanceDetail.msg = "";
		if(statusEntry == "3" && statusExit == "4"){
			attendanceDetail.msg = "迟到且早退";
		}
		else if (statusEntry == "3") {
			attendanceDetail.msg = "迟到";
		} else if (statusExit == "4"){
			attendanceDetail.msg = "早退";
		}
		attendanceArray.push(attendanceDetail);
	}
	//显示日历中的数据
	var ele = $("#attendanceInfo");
	ele.fullCalendar({
		theme : false,
		header: {
			left : 'prev',
			center : 'title',
			right : 'next'
		},
		height: CONTENT_HEIGHT,
		monthNames : MONTH_NAMES,
		dayClick: function(date, allDay, jsEvent, view) {
			var selDate = date.format();
			//使用parseInt转换时记得指明为十进制
			var dayIndex = parseInt(selDate.substring(8),10);
			var content = selDate;
			try {
				var eventObj;
				eventObj = attendanceArray[dayIndex - 1];
				var time = eventObj.time;
				content += "<p>" + time + "</p>";
				if ( undefined == time || $.trim(time) == "-") {
					return;
				}
				if (eventObj.msg != "") {
						content += "<p>" + eventObj.msg + "</p>";;
				}
				showAttedanceTips(content,allDay.pageX,allDay.pageY);
			} catch (e) {
				return ;
			}
		},
		events: dateInMonth
	});
	//如果是月初则显示上月的数据的日历
	showCalendarByDate(ele);
}

/**
 * 显示考勤的详情
 */
function showAttedanceTips(content,papeX,papeY){
	$("#tips-shade").height($("body").height());
	$("#tips-shade").width($("body").width());
	$("#tips-shade").show();
	$("#tips").html(content);
	$("#tips").css("left",papeX);
	$("#tips").css("top",papeY);
	$("#tips").css("background-color","#FCF8E3");
	$("#tips").show();
}

/**
 * 如果是月初则显示上月的数据的日历
 */
function showCalendarByDate(calendar){
	var tempDate = new Date().getDate();
	if (tempDate == "1") {
		calendar.fullCalendar('prev');// 默认显示上一个月
	}
}

/**
 * 显示积分排行
 * @param data
 */
function setPointListHTML(data) {
	var info = data;
	var html="";
	for(var i = 0; i < info.length; i++){
		html += "	<div class=\"point-list-row\">";
		html += "		<div style=\"width:25%;font-size:1.1em;padding-left:16px;font-weight:bold;\">TOP" + (i+1) + "</div>";
		html += "		<div style=\"width:30%\">" + info[i].integral + "</div>";
		html += "		<div style=\"width:40%\">" + info[i].userName + "</div>";
		html += "	</div>";
	}
	$(".points-data div").append(html);
}

/**
 * 工时展示
 * @param data
 */
function setWorkHoursInfoHTML(data){
	var info = data;
	var dataInDay = {};
	var dataInMonth = new Array();
	var categories = new Array();
	for(var i = 0; i < info.length; i++){
		dataInDay = {};
		categories.push(info[i].day);
		dataInDay.y = info[i].workhours;
		if (dataInDay.y == 8) {
			dataInDay.color = "#9e4b9d";
		} else {
			dataInDay.color = "#f00";
		}
		
		dataInMonth.push(dataInDay);
	}
	$("#workhours-chart").highcharts({
        chart: {
            type: 'column'
        },
        credits:{enabled:false},
        title: {
            text: "<h2>" + yyyyMM + "</h2>"
        },
        tooltip: { 
            headerFormat: '<span style="font-size:10px">{point.key}日</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f}小时</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        xAxis: {
            categories: categories,
            title: {
                text: '日期'
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: ' '
            }
        },
        series: [{
            data: dataInMonth,
            name: " ",
            color: 'white'
        }]     
    });
	//遮住标题使他不能被点击跳转
	var tmpHeight = $("#highcharts-0").height();
	$("#highcharts-0").height(tmpHeight - 60);
}

/**
 * 显示任务完成进度情况
 * @param data
 */
function setTaskProgressHTML(data){
	
	var info = data;
	var html="";
	$(".weekplan-data-list").html("");
	for(var i = 0; i < info.length; i++){
		html += "<div>";
		html += "<div class=\"project-name\">" + info[i].projectName + "</div>";
		html += "<div class=\"project-task\">" + info[i].taskContent + "</div>";
		var widthTemp = getProgressbarWidth(info[i].finish);
		html += "<div class=\"progress\">" +
				"	<div class=\"progress-bar\" style=\"height:20px;width:" + PROGRESS_BAR_W + "px\">" +
				"		<div class=\"progress-subbar\" style=\"height:20px;width:" + widthTemp + "px\">" + info[i].finish + "</div>" +
				"   </div>" +
				"</div>";
		html += "<div class=\"time-number\">" + info[i].planWorkTime + "</div>";
		html += "<div class=\"time-number\">" + info[i].factWorkTime + "</div>";
		html += "<div class=\"time-number\">" + info[i].deviation + "</div>";
		html += "</div>";
		/*html += "<tr>";
		html += "    <td class='project-name'>" + info[i].projectName + "</td>";
		html += "    <td class='project-task'>" + info[i].taskContent + "</td>";
		var widthTemp = getProgressbarWidth(info[i].finish);
		html += "    <td class='progress'>" +
				"	     <div class=\"progress-bar\">" +
				"		    <div class=\"progress-subbar\" style=\"width:" + widthTemp + "px\"></div>" +
				"        </div>" + info[i].finish;
				"    </td>";
		html += "    <td class='time-number'>" + info[i].planWorkTime + "</td>";
		html += "    <td class='time-number'>" + info[i].factWorkTime + "</td>";
		html += "    <td class='time-number'>" + info[i].deviation + "</td>";
	    html += "</tr>";*/
	}
	
	$(".weekplan-data-list").append(html);
}

/**
 * 计算进度条的宽度
 * @param finish 任务完成度
 * @returns {Number} 进度条的宽度
 */
function getProgressbarWidth(finish){
	var progressbarW = PROGRESS_BAR_W;
	var width;
	if (finish == 'null' || finish == undefined || $.trim(finish) == "") {
		width = 0;
	}else{
		width = finish.substring(0,finish.length-1);
	}
	width = ((width == '') ? 0 : width);
	return (width * progressbarW) / 100;
}
