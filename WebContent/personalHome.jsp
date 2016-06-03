<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>个人首页</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta-equiv ="X-UA-patible" content="IE=EmulateIE" />
<link rel="stylesheet" href="js/plugin/fullcalendar2.2.1/fullcalendar.css" /> 
<link rel="stylesheet" href="js/plugin/fullcalendar2.2.1/fullcalendar.print.css" media="print" /> 
<link rel="stylesheet" href="css/personalHome.css" />
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="plugin/hightcharts/highcharts.js"></script>
<!-- 日历插件 V2.2.1-->
<script type='text/javascript' src="js/plugin/fullcalendar2.2.1/moment.min.js"></script>
<script type='text/javascript' src="js/plugin/fullcalendar2.2.1/fullcalendar.min.js"></script>
<script type="text/javascript" src="js/personalHome.js"></script>
 </head>
<body id="bodyid">
	<!-- 个人数据 -->
	<!-- <div class="personal-data">
		<div class="personal-data-item">
			<div class="personal-data-item-img">
				<img src="images/home/total(1).png" />
			</div><div class="personal-data-item-num">0,000<br>需求待定</div>
		</div>
		<div class="personal-data-item">
			<div class="personal-data-item-img">
				<img src="images/home/total(2).png" />
			</div><div class="personal-data-item-num">0,000<br>需求待定</div>
		</div>
		<div class="personal-data-item">
			<div class="personal-data-item-img">
				<img src="images/home/total(3).png" />
			</div><div class="personal-data-item-num">0,000<br>需求待定</div>
		</div>
		<div class="personal-data-item">
			<div class="personal-data-item-img">
				<img src="images/home/total(4).png" />
			</div><div class="personal-data-item-num">0,000<br>需求待定</div>
		</div>
	</div> -->
	<div class="clear"></div>
	<div class="main-data">
		<ul class="main-data-ul">
			<li class="main-data-ul-li">
				<!-- 工时统计 -->
				<div class="workhours-title">工时一览</div>
				<div class="workhours-data">
					<div id="workhours-chart" class="workhours-chart">
					</div>
				</div>
			</li>
			<li class="main-data-ul-li">
				<!-- 日志填写情况-->
				<div class="daylog-title">日志填写一览</div>
				<div class="daylog-data">
					<div class="daylog-cld">
						<div class="daylogInfo" id="daylogInfo">							
						</div>
					</div>
				</div>				
			</li>
			<li class="main-data-ul-li">
				<!-- 部门积分排行 -->
				<div class="points-title">部门内积分排行</div>
				<div class="points-data">
					<div>
					</div>
				</div>
				<div class="clear"></div>
			</li>
			<li class="main-data-ul-li">
				<!-- 考勤情况 -->
				<div class="attendance-title">考勤一览</div>
				<div class="attendance-data">
					<div class="attendance-cld">
						<div class="attendanceInfo" id="attendanceInfo"></div>
					</div>
				</div>
			</li>			
		</ul>
		<div id="weekplan-data-block">
			<!-- 周计划任务情况 -->
			<div class="weekplan-title">
				<div class="weekplan-title-img">
					<img src="images/home/planList.png" height=40 />
				</div>
				<div class="weekplan-title-word">本周任务完成一览</div>
			</div>
			<div class="weekplan-data">
				<div class="weekplan-data-header">
					<div class="project-name">项目名称</div>
                    <div class="project-task">计划任务</div>
                    <div class="progress">进度</div>
                    <div class="time-number">计划工时</div>
                    <div class="time-number">实际工时</div>
                    <div class="time-number">偏差度</div>
				</div>
                <div class="weekplan-data-list">
				</div>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	<div id="tips-shade"></div>
	<div id="tips">tips</div>
</body>
</html>