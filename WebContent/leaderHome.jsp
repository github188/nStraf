<%@ page contentType="text/html; charset=UTF-8" %>
<%--以下两个*.inc影响highchart加载和布局
 <%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/pagination.inc"%> --%>
<!DOCTYPE HTML>
<html>
<head>
  <title><s:property value="%{getText('index.mainpanel')}"/></title>
  <link href="css/css_v2.css" type=text/css rel=stylesheet>
  
   <!-- phonegap 基本样式 需要调用的包-->
   <!-- <script type="text/javascript" charset="utf-8" src="./js/serverpath.js"></script> -->
<script type="text/javascript" charset="utf-8" src="./plugin/hightcharts/jquery-1.8.2.min.js"></script>
<script type="text/javascript" charset="utf-8" src="./plugin/hightcharts/highcharts.js"></script>
<script type="text/javascript" charset="utf-8" src="./plugin/hightcharts/highcharts-more.js"></script>
<script type="text/javascript" charset="utf-8" src="./plugin/hightcharts/map.js"></script>
<script type="text/javascript" charset="utf-8" src="./plugin/hightcharts/china-data.js"></script>
<!-- 领导首页调用的JS -->
<script type="text/javascript" charset="utf-8" src="./js/leaderHomePage.js"></script>
 <style type="text/css">
 body{
 height:100%;
 width:100%;
 }
	  #midArea{
	   		margin:0 auto;
			height:100%;
			width: 100%;
		}
	#mapArea{
		width: 65%;
		/* min-width: 600px; width:e-xpression            (document.body.clientWidth < 600? "600px": "auto" ); */
		color: #FFFFFF;
		font-size:8px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		float: left;
		position: relative;
		margin-right: 1%; 
	}
	#rightArea{
		width: 34%;
		color: #FFFFFF;
		font-size:8px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		float: left;
		position: relative;
	}
	.contractArea{
		width: 100%;
		color: #FFFFFF;
		float: left;
		position: relative;
	}
	.moduleArea{
		width: 100%;
		color: #FFFFFF;
		float: left;
		position: relative;
	}
	.depetArea{
		width: 100%;
		color: #FFFFFF;
		float: left;
		position: relative;
	}
	#container {
    width: 100%;
    height: 800px; 
    margin: 0 auto; 
    float: left;
	position: relative;
}
	#contract {
	    width: 100%; 
	    height:300px;
	    margin: 0 auto; 
	    float: left;
	    padding-top:20px;
		position: relative;
	}
	 /* #contract .highcharts-container{
	overflow:visible;
	}  */
	#personDayMoudle{
	    width: 100%;
	    height:300px;
	    margin: 0 auto; 
	    float: left;
		position: relative;
	}
	#DeptMonthPersonDay{
	    width: 100%;
	    height:300px;
	    margin: 0 auto; 
	    float: left;
		position: relative;
	}
.loading {
    margin-top: 10em;
    text-align: center;
    color: gray;
}
.spanTitle{
	margin-left:5px;
	}
.lateMapTitle{
		width: 100%;
		float: left;
		font-size:17px;
		fill:#333333;
		color:white;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		background-color: #72B5E2;
		padding:5px 0px 5px 0px; 
	}
.lateContractTitle{
		width: 100%;
		float: left;
		font-size:17px;
		fill:#333333;
		color:white;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		background-color: #FCB414;
		padding:5px 0px 5px 0px; 
	}
	.latePersonDayTitle{
		width: 100%;
		float: left;
		font-size:17px;
		fill:#333333;
		color:white;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		background-color: #9D4A9C;
		padding:5px 0px 5px 0px; 
	}
	.lateDeptMonthPersonDayTitle{
		width: 100%;
		float: left;
		font-size:17px;
		fill:#333333;
		color:white;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		background-color: #E1567D;
		padding:5px 0px 5px 0px; 
	}
.lateDiv{
		width: 100%;
	}
	#loading{
	    z-index:100;
		width: 100%;
		height:100%;
	    color:#000000;
	    font-size:20px;
	    font-family: Microsoft YaHei,Microsoft JhengHei;
	    background-color:gainsboro;
	    position:absolute;
	  	top:0px; 
	  	text-align: center;
	}
	.jiazai{
	margin-top:260px;
	}
	/* 地图重置按钮 */
	#zoom-out{
	font-size:12px;
	position:fixed;
	display:block;
	font-family: Microsoft YaHei,Microsoft JhengHei;
	left:30%;
	top:95%;
	cursor:default;
	text-align:center;
	background:#FFFFFF;
	border-color:#D9D9D9;
	color:black;fill:black;font-weight:bold;
	/* 按钮圆边 */
	 border-radius:5px;-webkit-border-radius:5px;-moz-border-radius:5px; 
}
.clearFloat{
clear:both;
}
  </style>
</head>
<body topmargin="0" leftmargin="0">
<script language="javaScript" src="js/overlib_mini.js"></script>
<script language="javaScript">
</script>
<div id="loading"><div class="jiazai">领导首页数据正在加载……</div></div>
<div id = "midArea">
	   <div id = "mapArea">
			<div class="lateDiv">
			<div class="lateMapTitle"><span class="spanTitle">运通信息项目分布</span></div>
			</div>
			  <div id="container"></div> 
			  <!-- <button id="zoom-out">重置</button> -->
	   </div>
	   <div id = "rightArea">
		   <div class = "contractArea">
			   <div class="lateDiv">
				<div class="lateContractTitle"><span class="spanTitle">合同完成度</span></div>
				</div>
				<div id="contract" ></div>
		   </div>
		   <div class="clearFloat"></div>
		   <div class = "moduleArea">
				<div class="lateDiv">
				<div class="latePersonDayTitle"><span class="spanTitle">板块项目确认人日统计</span></div>
				</div>
				<div id="personDayMoudle"></div>
		   </div>
		   <div class="clearFloat"></div>
		   <div class = "depetArea">
				<div class="lateDiv">
				<div class="lateDeptMonthPersonDayTitle"><span class="spanTitle">部门确认人日统计</span></div>
				</div>
				<div id="DeptMonthPersonDay"></div>
		   </div>
		   <div class="clearFloat"></div>
	   </div>
</div>
</body>
<script type="text/javascript">
$(function(){
	//初始化各个容器最小宽度
/*	var midAreaW = $(window).width()*1;
 	$("#midArea").attr("style","min-width:" + midAreaW + "px;");
	$("#mapArea").attr("style","min-width:" + midAreaW*0.65 + "px;");
	$("#rightArea").attr("style","min-width:" + midAreaW*0.34 + "px;"); */
	//初始化加载
	loadData();
});
//浏览器窗口大小改变触发事件
var k = 1;
window.onresize = function(){
	//由于完成合同度图表模块会根据已改变的高度改变大小
	//所有先恢复适合加载图表大小300px,再延迟改变容器大小使容器适合的高度210px
	if(k==1){
	 $("#contract").height(300);
	}
	k++;
    setTimeout("$('#contract').height(210)",10); 
	//重新刷新合同完成度刻度
	setTimeout("changeDegree()",500); 
} 
//借助div[id=personDayMoudle]容器只改变一次而且与需要改变的div[id = contract]
</script>

</html>