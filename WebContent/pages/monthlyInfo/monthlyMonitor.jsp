<%-- <%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%> --%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<!DOCTYPE html>
<html>
	<head>
	<title>运通信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport"
		content="width=device-width,height=device-height,initial-scale=1.0, maximum-scale=1,user-scalable=no">
	<meta content="telephone=yes" name="format-detection">

	<!-- <link rel="stylesheet" type="text/css" href="../../js/plugin/reveal/reveal.css"> -->

	<script type="text/javascript" src="../../js/plugin/fullcalendar/jquery/jquery-1.5.2.min.js"></script>
	
	<script type="text/javascript" src="monthlyMonitor.js"></script>
	
	<script type="text/javascript" src="../../plugin/hightcharts/highcharts.js"></script>
	
	<!-- fullcalendar -->
	<!-- 修改后的fullcalendar_modify.css，请勿随意更换 -->
	<link rel="stylesheet" type="text/css" href="../../js/plugin/fullcalendar/fullcalendar_modify.css">
	<script type="text/javascript" src="../../js/plugin/fullcalendar/fullcalendar.min.js"></script>
	
	<!-- <script type="text/javascript" src="../../js/plugin/reveal/jquery.reveal.js"></script> -->
	<!-- 弹出层-->
	<script type="text/javascript" src="../../js/plugin/custombox/jquery.custombox.js"></script>
	<link rel="stylesheet" href="../../js/plugin/custombox/jquery.custombox.css" />
	<link rel="stylesheet" href="../../js/plugin/custombox/goodEmp.css" />

	<style type="text/css">
	body{
		background: rgb(244, 244, 244);
		position: relative;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		margin-top: 5px;
	}
	.topArea1 {
		width: 100%;
		height:100px;
		float: left;
		position: relative;
		color: #FFFFFF;
	}
	.topArea2{
		width: 100%;
		float: left;
		position: relative;
		margin-top: 20px;
	}
	.midArea1{
		width: 100%;
		float: left;
		position: relative;
		margin-top: 20px;
	}
	.midArea2{
		width: 100%;
		float: left;
		position: relative;
		margin-top: 20px;
	}
	.footArea{
		width: 100%;
		float: left;
		position: relative;
		margin-top: 20px;
	}

	.total_innerArea {
		width: 20%;
		height: 100%;
		float:left;
		position:relative;
	}
	.total_contractNumArea {
		background-color: #FDB515;
	}
	.total_contractRateArea {
		background-color: #F27B51;
	}
	.total_staffArea {
		background-color: #72BA4A;
	}
	.total_projectArea {
		background-color: #20A68B;
	}
	.total_trainArea {
		width:19.9%;
		background-color: #827CBA;
	}
	.total_val{
		width:40%;
		height:30%;
		line-height:20px;
		text-align: center;
		font-size: 28px;
		float:left;
		margin-top:10%;
	}
	.total_val_contract{
		width:100%;
		height:30%;
		line-height:22px;
		text-align: center;
		font-size: 28px;
		float:left;
		position:absolute;
		top:14%;
		left:0px;
		text-overflow:ellipsis; 
		white-space:nowrap; 
		overflow:hidden;
	}
	.total_icon {
		width:30%;
		height:80%;
		float:left;
		background-position: center;
		background-size: 100% 80%;
		background-repeat: no-repeat;
		margin-left: 7%;
		margin-right: 3%;
		margin: 5%;
	}
	.total_icon_contract {
		width:30%;
		height:60%;
		float:left;
		background-position: center;
		background-size: 100% 80%;
		background-repeat: no-repeat;
		margin-left: 7%;
		margin-right: 3%;
		margin: 17% 5% 5% 5%
	}
	.contractTotal_icon {
		background-image: url(../../images_new/monthlyMonitor/total_contract.png);
	}
	.contractRate_icon {
		background-image: url(../../images_new/monthlyMonitor/total_contractRate.png);
	}
	.staffTotal_icon {
		background-image: url(../../images_new/monthlyMonitor/total_staff.png);
	}
	.projectTotal_icon {
		background-image: url(../../images_new/monthlyMonitor/total_projectNum.png);
	}
	.trainTotal_icon {
		background-image: url(../../images_new/monthlyMonitor/total_train.png);
	}
	.total_type {
		width:40%;
		height:25%;
		font-size: 16px;
		line-height :16px;
		text-align: center;
		float:left;
		margin-top: 3%;
		margin-bottom: 1%;
	}
	.total_type_contract {
		width:40%;
		height:25%;
		font-size: 16px;
		line-height :16px;
		text-align: center;
		float:left;
		margin-top: 24%;
		margin-bottom: 1%;
	}
	.highcharts-container{
		border-left: 1px solid #DCDCDC;
		border-right: 1px solid #DCDCDC;
		border-bottom: 1px solid #DCDCDC;
	}
	.pieChartsArea_staff{
		width: 30%;
		color: #FFFFFF;
		font-size:8px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		float: left;
		position: relative;
		margin-right: 1%;
	}
	.pieChartsArea_title_staff{
		background-color: #72B5E2;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
	}
	.pieCharts_staff{
		float: left;
		width: 100%;
		height: 300px;
	}
	.pieCharts_attence{
		float: left;
		width: 100%;
		height:300px;
	}
	.lineCharts_attence{
		float: left;
		width: 100%;
		height: 300px;
	}
	.pieChartsArea_attence{
		width: 35%;
		color: #FFFFFF;
		font-size:8px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		float: left;
		position: relative;
		margin-right: 1%;
	}
	
	.pieChartsArea_title_attence{
		background-color: #AE8567;
		height:28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
		
	}

	.lineChartsArea_attence{
		width: 33%;
		color: #FFFFFF;
		font-size:8px;
		float: left;
		position: relative;
	}
	
	.lineChartsArea_title_attence{
		background-color: #7A848D;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
	}



	.lineChartsArea_staffChange{
		width: 49%;
		color: #FFFFFF;
		font-size:8px;
		float: left;
		position: relative;
		margin-left: 1%;
	}

	.lineChartsArea_title_staffChange{
		background-color: #9E4B9D;
		color:#FFFFFF;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
	}

	.charts_staffChange {
		float: left;
		width: 100%;
		height: 203px;
	}

	.lineChartsArea_contract{
		width: 49%;
		color: #FFFFFF;
		font-size:8px;
		float: left;
		position: relative;
		margin-left: 1%;
		margin-top: 1%;
	}

	.lineChartsArea_title_contract{
		background-color: #9E4B9D;
		color:#FFFFFF;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
		
	}

	.charts_contract {
		float: left;
		width: 100%;
		height: 202px;
	}
	
	.projectTableArea{
		width: 100%;
		float: left;
		position: relative;
		background-color:#F6F6F6;
		color:#777677;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		text-align: center;
		white-space:nowrap;
		overflow:hidden;
		text-overflow:ellipsis; 
	}
	
	.projectTableTitle {
		background-color: #E2577E;
		color:#FFFFFF;
		height:28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height:28px;
		padding-left: 3px;
		text-align: left;
	}
	.projectTableHeadArea{
		width: 98%;
		float:left;
		font-weight: bold;
		position:relative;
		white-space:nowrap; overflow:hidden; text-overflow:ellipsis; 
		padding: 0 5px;
		border-bottom: 1px solid #DCDCDC
	}
	.projectTableDataArea {
		width:100%;
		float: left;
		overflow:scroll; 
		overflow-x:hidden;
		background-color:#F6F6F6;
		position: relative;
		text-align: center;
		
	}
	.projectTableHead {
		float:left;
		font-size: 15px;
		line-height:25px;
		border-bottom: 1px solid #CECECE;
		text-align: center;
	}

	.prjInfo_rowDiv{
		width:100%;
		float:left;
		position: relative;
	}
	.prjInfo_rowDiv:hover{
		background-color: #E77898;
		color:#FFFFFF;
	}

	.prjInfo_grid{
		line-height:40px;
		height:40px;
		position:relative;
		float:left;
		font-size: 13px;
		border-bottom: 1px solid #DADADA;
		white-space:nowrap; overflow:hidden; text-overflow:ellipsis; 
		text-align: center;
	}
	.prjInfo_grid1{
		width:5%;
	}
	.prjInfo_grid2{
		width:15%;
	}
	.prjInfo_grid3{
		width:25%;
	}
	.prjInfo_grid4{
		width:15%;
	}
	.prjInfo_grid5{
		width:8%;
	}
	.prjInfo_grid6{
		width:8%;
	}
	.prjInfo_grid7{
		width:8%;
	}
	.prjInfo_grid8{
		color:#777677;
		width:15%;
	}

	.finish_progressbar{
	   width: 80%;
	   height:14px;
	   float:left;
       border: 1px #669CB8 solid;  
       position: relative;
       box-shadow: 0px 2px 2px #D0D4D6;  
       -moz-box-shadow: 0px 2px 2px #D0D4D6;  
       border-radius: 10px;  
       -moz-border-radius: 10px;  
       background: -moz-linear-gradient(left, #E1E9EE, #FFFFFF);
       background: -webkit-gradient(linear, 0 0, 0 100%, from(#E1E9EE), to(#FFFFFF));
       margin: 10px 10% 10px 10%;
       behavior: url(../../htc/PIE.htc);
	}
	.progressNum {
	    float: left;
	    position: absolute;
	    top: -13px;
	    left: 45%;
	}
	.finish_progress{
		/* background: -webkit-linear-gradient(left, #7BC3FF, #42A9FF);
		background: -o-linear-gradient(left, #7BC3FF, #42A9FF);
		background: linear-gradient(to right, #7BC3FF, #42A9FF);  */
        width: 0;  
        height:14px;
        line-height:14px;
        border-radius: 10px 0 0 10px;  
        -moz-border-radius: 10px 0 0 10px;  
        float: left;
        text-align: center;
        color:#777677;
        font-size: 10px;
        behavior: url(../../htc/PIE.htc);
	}
	.goodEmployeeArea{
		width: 100%;
		float: left;
		position: relative;
	}
	.goodEmployee {
		width: 100%;
		height: 700px;
		float: left;
		position: relative;
		color:#777677;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		text-align: center;
		-webkit-text-size-adjust:none;
		white-space:nowrap;
		overflow:auto; 
		overflow-x: hidden;
		text-overflow:ellipsis;
		background-color: #F3F3F5;
		border-left: 1px solid #DCDCDC;
		border-right: 1px solid #DCDCDC;
		border-bottom: 1px solid #DCDCDC;
	}
	.goodEmployeeRow {
		float: left;
		position: relative;
		width: 48%;
		height: 80px;
		margin-top: 10px;
		margin-bottom: 10px;
		margin-left: 2%;
	}
	.headerImage {
		width:20%;
		height: 100%;
		background-size: 100% 100%;
		background-image: url(../../images_new/monthlyMonitor/goodEmp.jpg);
		background-repeat:no-repeat;
		float:left;
		display:block;
		margin-right: 5%;
		filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/nStraf/images_new/monthlyMonitor/goodEmp.jpg',sizingMethod='scale');
	}
	.goodEmployeeTitle{
		background-color: #468DC5;
		color:#FFFFFF;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 3px;
		
		text-align: left;
	}
	.goodEmpInfo {
		width:70%;
		height:70px;
		background-image: url(../../images_new/monthlyMonitor/goodEmpBg.png);
		background-repeat:no-repeat;
		background-size: 100% 100%;
		float:left;
		text-align: left;
	}
	.goodEmpInfo_name{
		width:100%;
		height: 40%;
		margin-left: 6%;
		float: left;
		margin-top: 3%;
		color:#FFFFFF;
	}
	.goodEmpInfo_dept_duty{
		width:100%;
		height: 60%;
		margin-left: 6%;
		float: left;
		color:#FFFDFD;
	}
	.lateCalendar_title{
		background-color: #FDB515;
		color:#FFFFFF;
		height: 28px;
		width:100%;
		float: left;
		font-size:12px;
		line-height: 28px;
		padding-left: 5px;
	}
	.lateCalendar{
		width: 50%;
		float: left;
		position: relative;
	}
	.lateCalendar_container{
		border-left: 1px solid #DCDCDC;
		border-right: 1px solid #DCDCDC;
		border-bottom: 1px solid #DCDCDC;
		float: left;
		width: 100%;
		height: 445px;
	}
	.attence_popWin{
		width:130px;
		float:left;
		display: none;
		position: absolute;
		background-color:#468DC5;
		z-index:99998;
		text-align:center;
		font-size:12px;
		color:#FFF;
	}
	.pop_title {
		width:33%;
		float: left;
		text-align: center;
		background-color: #3A87AD;
		color: #FFFFFF;
	}
	.pop_title_left {
		width:34%;
		float: left;
		text-align: center;
		background-color: #3A87AD;
		color: #FFFFFF;
	}
	.popDataDiv{
		width:33%;
		float: left;
		text-align: center;
		color:#777677;
		background-color: #FFFFFF;
		border-bottom:1px solid #DCDCDC;
	}
	.popDataRow{
		width:100%;

		float: left;
		margin: 2px;
	}
	.popWinArrow{
		width: 0;
		height:0;
		float:left;
		position:absolute;
		left:41%;
		top:-16px;
		border:solid 8px #000;
		border-color:transparent transparent #468DC5 transparent;
	}
	.popDataDiv_left {
		width:34%;
		float: left;
		text-align: center;
		color:#777677;
		background-color: #FFFFFF;
		border-bottom: 1px solid #DCDCDC;
	}

	</style>
	</head>
	<body>
		<!-- 总数统计 -->
		<div class="topArea1">
			<div class="total_contractNumArea total_innerArea">
				<div class="contractTotal_icon total_icon_contract"></div>
				<div class="total_val_contract" id="total_contractNum">0</div>
				<div class="total_type_contract">合同总额</div>
			</div>
			<div class="total_contractRateArea total_innerArea">
				<div class="contractRate_icon total_icon"></div>
				<div class="total_val" id="total_contractRate">0</div>
				<div class="total_type">合同完成率</div>
			</div>
			<div class="total_staffArea total_innerArea">
				<div class="staffTotal_icon total_icon"></div>
				<div class="total_val" id="total_staffNum">0</div>
				<div class="total_type">总人数</div>
			</div>
			<div class="total_projectArea total_innerArea">
				<div class="projectTotal_icon total_icon"></div>
				<div class="total_val" id="total_projectNum">0</div>
				<div class="total_type">项目数</div>
			</div>
			<div class="total_trainArea total_innerArea">
				<div class="trainTotal_icon total_icon"></div>
				<div class="total_val" id="total_trainNum">0</div>
				<div class="total_type">培训次数</div>
			</div>
		</div>
		<!-- 人员结构-->
		<div class="topArea2">
			<div class="pieChartsArea_staff">
				<div class="pieChartsArea_title_staff">人员结构</div>
				<div class="pieCharts_staff" id="staffDistributePie"></div>
			</div>
			<!-- 缺勤分布-->
			<div class="pieChartsArea_attence">
				<div class="pieChartsArea_title_attence">缺勤结构</div>
				<div class="pieCharts_attence" id="staffAttencePie"></div>
			</div>
			<!-- 缺勤人均-->
			<div class="lineChartsArea_attence">
				<div class="lineChartsArea_title_attence">缺勤统计</div>
				<div class="lineCharts_attence" id="staffAttenceLine"></div>
			</div>
		</div>
		<div class="midArea1">
			<!-- 考勤情况-->
			<div class="lateCalendar">
				<div class="lateCalendar_title">考勤异常</div>
				<div id="lateCalendar_container" class="lateCalendar_container"></div>
			</div>
			<!--人员增长-->
			<div class="lineChartsArea_staffChange">
				<div class="lineChartsArea_title_staffChange">人员增长</div>
				<div class="charts_staffChange" id="staffChangeLine"></div>
			</div>
			<!--合同统计-->
			<div class="lineChartsArea_contract">
				<div class="lineChartsArea_title_contract">合同统计</div>
				<div class="charts_contract" id="staffContractLine"></div>
			</div>
		</div>
		<!--项目情况-->
		<div class="midArea2">
			<div class="projectTableArea">
				<div class="projectTableTitle">项目情况</div>
				<div class="projectTableHeadArea">
					<div class="prjInfo_grid1 projectTableHead">序号</div>
					<div class="prjInfo_grid2 projectTableHead">客户</div>
					<div class="prjInfo_grid3 projectTableHead">项目</div>
					<div class="prjInfo_grid4 projectTableHead">项目预收</div>
					<div class="prjInfo_grid5 projectTableHead">投入人日</div>
					<div class="prjInfo_grid6 projectTableHead">进度</div>
					<div class="prjInfo_grid7 projectTableHead">风险数</div>
					<div class="prjInfo_grid8 projectTableHead">完成度</div>
				</div>
				<div class="projectTableDataArea" id="projectInfo"></div>
			</div>
		</div>
		<!--优秀员工-->
		<div class="footArea">
			<div class="goodEmployeeArea">
				<div class="goodEmployeeTitle">优秀员工</div>
				<div class="goodEmployee" id="goodEmployeeArea"></div>
			</div>
		</div>

		<!-- 考勤情况弹出框 -->
		<div id="popWin" style="display: none;" class="attence_popWin" >
		<!-- <div class="popWinArrow" id="popWinArrow"></div> -->
		</div>
	</body>
</html>

<script type="text/javascript">

//初始化数据
$(function() {
		//body宽高适应屏幕
	var bodyH = $(window).height() ;
	var bodyW = $(window).width()*1;
	$("body").attr("style","height:" + bodyH + "px;");

	//初始化数据
	globalData = <%=(String)request.getAttribute("globalData")%>;
	
	initData();
	//点击日历以外元素时隐藏弹窗
	$("body[class!=lateCalendar]").click(function(){
		$("#popWin").hide()
	})
});
	
</script>
