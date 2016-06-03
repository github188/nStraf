<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/dialog.inc"%>

<html>
<head>
<title>人日统计</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta-equiv="X-UA-patible" content="IE=EmulateIE"/>
</head>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/hightcharts/highcharts.js"></script>
<%@ include file="/inc/pagination.inc"%>
<style type="text/css">
	.top_img{
		width: 28px;
		height: 15px
	}
	.img_up{
		width: 13px;
		height: 10px;
		margin: 0 0 5px 5px;
	}
	.change_header{
		font-size: 12px;
		margin:0 0 0 13px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		line-height: 12px;
	}
	.change_rate{
		font-size: 10px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
	}
	.change_prj{
		font-size: 13px;
		font-family: Microsoft YaHei,Microsoft JhengHei;	
	}
	.change_left{
		float: left;
		margin: 9px 0 0 15px;
	}
	.change_right{
		float:right;
		margin:9 10px 0 0;
		line-height: 12px
	}
	.change_num{
		margin:5 0 0 0;
		font-size: 30px;
		font-weight:bold ;
		font-family: Microsoft YaHei,Microsoft JhengHei;
	}
	.change_table{
		width:300px;
		height:160px;
		margin:0 30px 0 0;
		background-image:url('../../images_new/chart/renri/change_bg.png');
		background-size:100% 100%
	}

	.total_tb_header{
		width:6%;
		font-size:12px;
		line-height: 12px;
		font-family: Microsoft YaHei,Microsoft JhengHei;
	}
	
	.total_tb_td{
		width: 6%;
		font-size:20px;
		color:#323E4E;
		font-family: Microsoft YaHei,Microsoft JhengHei;
		text-decoration:none;
	}
	.total_tb_prjName{
		width:11%;
		font-size:13px;
		color: #24A9E1;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		cursor:hand;
	}
	.total_tb_tr{
		line-height: 35px
	}
	.total_tb_tr:hover{
		background: #25AAE2
	}
	.total_tb td {
		border-top:#FFFFFF;
		border-bottom:1px solid #E0E0D1;
		border-right:1px solid #E0E0D1;
		border-collapse: collapse;
	}
	.area_title {
		font-size:16px;
		font-family:  Microsoft YaHei,Microsoft JhengHei;
		color: #323E4F
	}
</style>
<script type="text/javascript">
var globalData={
		chart:{
			categories:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月','总计'],
			tickInterval:100
		},
		personDayJson:<%=(String)request.getAttribute("personDayJson")%>
	}


//填充TOP3数据
function fillTop3Data(tableId,data,index){
	var upRate = data.upRate;
	var upNum = data.upNum;
	var totalNum = data.totalNum;
	if(isNaN(upRate)||typeof(upRate) =="undefined"){
		upRate = 0;
	}
	if(isNaN(upNum)||typeof(upNum) =="undefined"){
		upNum = 0;
	}
	if(isNaN(totalNum)||typeof(totalNum) =="undefined"){
		totalNum = 0;
	}
	var prjName = data.prjName;
	if(prjName.length>6){
		prjName = prjName.substr(0,10);
	}
	if(upRate>0){
		upRate = "+" + upRate;
	}
	var html = '<tr id="'+data.prjId+'"><td class="change_left"><img class="top_img" src="../../images_new/chart/renri/top'+index+'.png"/></td>';
	html += '<td class="change_left"><div class="change_prj" title='+data.prjName+'>' + prjName + '</div></td>'
	if(tableId=="change_uptable"){
		html += '<td class="change_right"><img class="img_up" src="../../images_new/chart/renri/change_up.png"/>';
		html += '<div class="change_rate">' + upRate + '%</div></td>';		
		html += '<td class="change_right"><div class="change_num">' + upNum + '</div>';
	}else if (tableId=="change_downtable"){
		html += '<td class="change_right"><img class="img_up" src="../../images_new/chart/renri/change_down.png"/>';
		html += '<div class="change_rate">' + upRate + '%</div></td>';		
		html += '<td class="change_right"><div class="change_num">' + upNum + '</div>';	}else if (tableId=="change_totaltable"){
		html += '<td class="change_right"><div class="change_num">' + totalNum + '</div></td>';
	}
	$("#"+tableId).append(html);
}



//填充表格数据
function fillTableData(){
	var projectName = $("#projectName").val();//查询的项目名称
	var select=document.getElementById("isEnd");//是否完结
	var index = select.selectedIndex; // 选中索引
	var isEndValue = select.options[index].value; // 选中值
	$("#total_tb").html("");
	var personDayJson = globalData.personDayJson;
	var year = $("#yearSel").val();
	var prjDataArr = personDayJson[year];
	for(var i=0;i<prjDataArr.length;i++){
		//填充人日统计表数据
 		var monthCountsArr = prjDataArr[i].confirmCountsArr;
 		var prjName = prjDataArr[i].prjName;
 		
 		var isEnd =  prjDataArr[i].isEnd;//0：否 1：是
 		if(prjName.length>8){
 			prjName = prjName.substr(0,8);
 		}
 		
 		if(  projectName != ""){
 			
 			if(prjName.indexOf(projectName) >-1){
 				
 				html = '<tr id="'+prjDataArr[i].prjId+'" class="total_tb_tr" >';
 				html = html + '<td align="center" class="total_tb_prjName" title='+prjDataArr[i].prjName+'>' + prjName + '</td>';
 				for(var j=0;j < monthCountsArr.length;j++){
 					//html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="queryPersonDayDetail(\''+prjDataArr[i].prjId+'\',\''+(j+1)+'\',\''+year+'\');" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 					html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 				}
 				html = html + '<td class="total_tb_td" align="center">' + prjDataArr[i].curTotalNum + '</td></tr>';
 				$("#total_tb").append(html);
 				
 			}
 		}else if( isEndValue != ""){
 			if( isEndValue ==isEnd ){
 				html = '<tr id="'+prjDataArr[i].prjId+'" class="total_tb_tr" >';
 				html = html + '<td align="center" class="total_tb_prjName" title='+prjDataArr[i].prjName+'>' + prjName + '</td>';
 				for(var j=0;j < monthCountsArr.length;j++){
 					//html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="queryPersonDayDetail(\''+prjDataArr[i].prjId+'\',\''+(j+1)+'\',\''+year+'\');" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 					html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 				}
 				html = html + '<td class="total_tb_td" align="center">' + prjDataArr[i].curTotalNum + '</td></tr>';
 				$("#total_tb").append(html);
 			}
 			
 		}else{
 			
 			html = '<tr id="'+prjDataArr[i].prjId+'" class="total_tb_tr" >';
 			html = html + '<td align="center" class="total_tb_prjName" title='+prjDataArr[i].prjName+'>' + prjName + '</td>';
 			for(var j=0;j < monthCountsArr.length;j++){
 				//html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="queryPersonDayDetail(\''+prjDataArr[i].prjId+'\',\''+(j+1)+'\',\''+year+'\');" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 				html = html + '<td class="total_tb_td" align="center"><a href="#" onclick="" class="total_tb_td">' + monthCountsArr[j] + '</a></td>';
 			}
 			html = html + '<td class="total_tb_td" align="center">' + prjDataArr[i].curTotalNum + '</td></tr>';
 			$("#total_tb").append(html);
 			
 		}
 		
 		
		
	} 
	//为tr绑定监听器
	bindEventListener();
}

//弹出人日详情页
//projectId='+projectId+'&'+month='+month+'&'+year='+year+'&'+

var refreshProjectId = ''; //重载的折线图项目
var refreshFlag = false;  //判断是否重载
function queryPersonDayDetail(projectId,month,year){
	var resultvalue = OpenModal('/pages/personDay/listData!PersonDayDetailByMonth.action?projectId='+projectId+'&month='+month+'&year='+year+'&menuid=<%=request.getParameter("menuid")%>','800,550,tmlInfo.addTmlTitle,tmlInfo');
	refreshProjectId = projectId;
	refresh();
	//fillTableData();
}

function refresh(){
	//重新加载数据
	var actionUrl = "<%=request.getContextPath()%>/pages/personDay/listData!refreshPersonDayData.action";
	actionUrl=encodeURI(actionUrl);
	$.ajax({
		url : actionUrl,
		data : {},
		type : 'POST',
		dataType : 'json',
		timeout : 30000,
		success : function(data) {
			
			globalData.personDayJson=data;
			fillTableData();
			refreshFlag = true;
			fillHightChartData(null);
			//更新任务执行者
		},
		error : function(data, data1) {
			alert("更新数据失败!");
		}
	});
	//alert("123");
	
}

//绘制折线图
function fillHightChartData(ele){
	if(refreshFlag){
		var prjId = refreshProjectId;	
		refreshFlag = false;
	}
	else{
		var prjId = $(ele).attr("id");
	}
	var year = $("#yearSel").val();
	var personDayJson = globalData.personDayJson;
	var prjDataArr = personDayJson[year];
 	var prjData = prjDataArr[0];
	for(var i=0;i<prjDataArr.length;i++){
		if(prjId == prjDataArr[i].prjId){
			prjData = prjDataArr[i];
			break;
		}
	}
	if(prjData!=undefined){
		$('#hightChartContainer').highcharts({
	        chart: {
	            type: 'areaspline',
	            spacingLeft:-1,
	            backgroundColor:'#E5F4FB'
	        },
	        title: {
	            align:'right',
	            style:{
			        fontSize:'14px',
			        color:'#828584',
			        fontFamily:  'Microsoft YaHei,Microsoft JhengHei'
	            },
	            text: prjData.prjName
	        },
	        legend: {
	        	enabled: false  //设置图例不可见
	        },
	        xAxis: {
	            labels: {
	            	formatter: function() {
	            		return '<font style="font-size:9px;">'+globalData.chart.categories[this.value]+'</font>';
	            	}
	     		},
	     		tickInterval:1,
	            gridLineColor:"#C0C0C0",
	            gridLineWidth:1,
	            gridLineDashStyle: 'shortdash'
	        },
	        yAxis: {
	            title: {text: null},
	            gridLineColor:"#C0C0C0",
	            gridLineWidth:0,
	            lineWidth:0,
	            minorGridLineDashStyle: 'shortdash',
	            minorTickInterval:globalData.chart.tickInterval,
	            tickInterval:globalData.chart.tickInterval,
	            minorTickWidth: 0,
	            startOnTick:true
	        },
	        tooltip: {
	        	formatter: function() { //格式化提示信息 
	                return this.y+"人日"; 
	            } 
	        },
	        credits: {
	            enabled: false
	        },
	        plotOptions: {
	            areaspline: {
	            	//fillColor:'#24A7E0',
	                fillOpacity: 0.5
	            }
	        },
	        series: [{
	        	data: prjData.confirmCountsArr
	        }]
	    });
	}
}

/*
 *计算TOP3
 * field 需要排行的属性
 * order asc 递增  否则递减
 */
function calculateTop3(field,order){
	var personDayJson = globalData.personDayJson;
	//var year = $("#yearSel").val();
 	var date = new Date();
	var year = date.getFullYear();//当前年份
 	var month=date.getMonth();//当前月份，month从0开始
	var prjDataArr = personDayJson[year];//每年所有项目人日数据
 	var topArr = new Array();//排行数组
 	var regNum =/^[+-]?\d$/;//判断是否为数字
 	//获取排行统计,当前月份人日量一直在变不稳定，没有可比性。
	for(var i=0;i<prjDataArr.length;i++){
		var id =  prjDataArr[i].prjId;//项目ID
		var curMon =  prjDataArr[i].confirmCountsArr[month];//上月份
		var lastMon =  prjDataArr[i].confirmCountsArr[month-1];//上上个月份
		var upNum = curMon - lastMon;
		var upRate = 0;
		//上个月没有投入的不计入排行榜
		if(lastMon!=0){
			upRate = (upNum/lastMon).toFixed(1)*100;			
			topArr.push(eval("("+"{'prjId':'" + prjDataArr[i].prjId + "','prjName':'" + prjDataArr[i].prjName + "','upNum':" + upNum + ",'upRate':" + upRate + ",'totalNum':" + curMon +"}" +")") );
		}
	}
 	//排序
 	topArr.sort(getSortFun(field, order));
	return topArr;
}

//构造数组排序函数
function getSortFun(field, order) {
    var ordAlpah = (order == 'asc') ? '>' : '<';
    var sortFun = new Function('a', 'b', 'return a.' + field + ordAlpah + 'b.' + field + '?1:-1');
    return sortFun;
}

//鼠标移到TR上时改变字体颜色
function changeBgColor(ele,operate){
	if(operate==0){
		$(ele).attr("style","background-color:#25AAE2");//由于IE8一下不支持A元素以外的:hover伪类，故用JS控制背景色
		$(ele).find("td").each(function(){
			$(this).attr("style","color:#FFFFFF");
			$(this).find("a").attr("style","color:#FFFFFF");
		})
	}else if(operate==1){
		$(ele).attr("style","background-color:#FFFFFF");
		$(ele).find("td[class!=total_tb_prjName]").each(function(){
			$(this).attr("style","width: 6%;font-size:20px;font-family:  Microsoft YaHei,Microsoft JhengHei;");
			$(this).find("a").attr("style","width: 6%;font-size:20px;font-family:  Microsoft YaHei,Microsoft JhengHei;");
		})
		$(ele).find("td[class=total_tb_prjName]").attr("style","width:11%;font-size:13px;color: #24A9E1;font-family:  Microsoft YaHei,Microsoft JhengHei;");
	}
}
//点击设置颜色
var prevele;//定义点击对象全局变量以保存前一对象
function clickBgColor(ele){
	
	//点击的前一对象恢复mouseout状态
	 changeBgColor(prevele,1);
	//当前点击对象改变为mouseover状态
	changeBgColor(ele,0)
	
	//点击的前一对象重新绑定mouse事件
	$(prevele).on("mouseover",function(){
		changeBgColor(this,0);
	})
	$(prevele).on("mouseout",function(){
		changeBgColor(this,1)
	})
    //当前点击对象解除mouse点击事件
	$(ele).off("mouseout");
	$(ele).off("mouseover");
	prevele = ele;
}
//点击月份人日量改变颜色
/*
function clickBgtb(ele, asd){
	var id = ele;
	var month = asd;
	alert("id:"+id+",month:"+month);
	//alert(asd);
	//$(ele).attr("style","background-color:#323E4F;color:#FFFFFF;");
}
*/
//onclick="clickBgtb(this);"
//为元素绑定监听器
function bindEventListener(){
	$("tr[class=total_tb_tr]").each(function(){
		var ele = this;
		$(this).find("td[class=total_tb_prjName]").on("click",function(){
			fillHightChartData(ele);
			clickBgColor(ele);
		})
		$(this).on("mouseover",function(){
			changeBgColor(this,0);
		})
		$(this).on("mouseout",function(){
			changeBgColor(this,1)
		})
	})
}
//数据初始化
$(function () {
	//alert(JSON.stringify(globalData.personDayJson));
	var personDayJson = globalData.personDayJson;
 	var monthArr = globalData.chart.categories;
	var topArr = calculateTop3("upRate","desc");
	var len = topArr.length;
	var yearArr = new Array();
	//初始化年份下拉框
	for(var year in personDayJson){
		yearArr.push(year);
	}
	yearArr.sort().reverse();
	for(var i=0;i<yearArr.length;i++){
		$("#yearSel").append("<option value='" + yearArr[i] + "'>" + yearArr[i] + "</option>");		
	}
	//填充TOP3数据
	if(len>=3){
		for(var i=0;i<3;i++){
			fillTop3Data("change_uptable",topArr[i],i+1);
		}
		var j = 0;
		for(var i=len-1;i>len-4;i--){
			fillTop3Data("change_downtable",topArr[i],++j);
		}
	}
	topArr = calculateTop3("totalNum","desc");
	//alert(JSON.stringify(topArr))
	if(len>=3){
		for(var i=0;i<3;i++){
			fillTop3Data("change_totaltable",topArr[i],i+1);
		}
	}
	
 	//统计表表头
 	$("#total_tb_header").append('<td align="center" style="width:11%;"></td>');
	for(var i=0;i<monthArr.length;i++){
		$("#total_tb_header").append('<td class="total_tb_header" align="center">' + monthArr[i] + "</td>" );
	}
	//统计表表尾
	$("#total_tb_header").append('<td align="center" style="width:1%;"></td>');
	//填充当年统计表数据
	fillTableData();
	
	//为元素绑定监听器
	bindEventListener();
	
	//绘制第一个项目折线图
	fillHightChartData(null);
	
	//用JS改变折线图插件字体
/* 	var style = "font-size:14px;font-family:  Microsoft YaHei,Microsoft JhengHei;color: #828584";
	$(".highcharts-title").attr("style",style); */
});

</script>
<!-- <body id="bodyid" leftmargin="10px" topmargin="" style="margin-top:20px;">
 -->
 <body id="bodyid" leftmargin="0" topmargin="0">
 <%@include file="/inc/navigationBar.inc"%>
		<table width="100%" cellSpacing="0" cellPadding="0" margin-top="20px"
			margin-button="20px">
			<tr height="50px">
				<td width="8%" align="right" class="input_label" style="  text-align: right; font-weight: 600; white-space: nowrap;">项目名称:</td>
					<td width="10%">
						<input  name="projectName" style="width:124px;height:22px;margin-left:5px;" type="text"
							id="projectName" size="40" maxlength="40" >
					</td>
					<td width="8%" align="right" class="input_label" style="  text-align: right; font-weight: 600; white-space: nowrap;">是否完结:</td>
						<td width="10%">
							<select id="isEnd" >
							<option value="" selected="selected"></option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
						</td>
					<td>
						<div>
							<input style="margin-left:20px;" type="button" value="查询"  onclick="fillTableData()" >
						</div>
					</td>
			</tr>
		</table>
		<table width="100%" height="20" border="0" cellspacing="0"
			cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td class="orarowhead"><s:text name="grpInfo.table2title" /></td>
				<td align="right" width="50%"><%-- <tm:button site="2"></tm:button> --%>
				</td>
			</tr>
		</table>
		

<table style="width:80%" align="center">
	<tr>
	<td colspan="1">
		<div style="float: left;" class="area_title">年项目人日统计表</div>
		<div style="float: right; background:#D3D3D1;">
		<select style="width:100px;border:none;background:#D3D3D1; margin:3px 10px;" id="yearSel" onchange="fillTableData()" ></select>
		</div>
	</td>
	</tr>
	<tr height="200px">
		<td style="border-left: 3px solid rgb(0, 155, 218)">
			<div style="position: relative;margin: 0 0 0 0;background-color:  rgb(221, 237, 246)">
				<table style="width: 100%;height: 10px;">
				<tr id="total_tb_header">
				</tr>
				</table>
			</div>
			<div style="overflow-y:scroll;overflow-x: hidden;height:370px;margin: 8px 0 0 2px">
				<table style="width: 100%;height:100%;" cellpadding="0"  cellspacing="0"  id="total_tb" class="total_tb" border="0">
				</table>
			</div>
	    </td>
	</tr>
	<tr>
	<td colspan="1">
		<div style="float: left;margin-top:20px;" class="area_title">项目人日折线图</div>
	</td>
	</tr>
	<tr>
		<td>
			<div id="hightChartContainer" style="height: 300px;"></div>
		</td>
	</tr>
	<tr>
		<td>
			<table>
			<tr>
				<td><div style="float: left;margin-top:20px;" class="area_title">月项目人日变动排行榜</div></td>
			</tr>
				<tr>
					<td>
						<table id="change_uptable" class="change_table" >
							<tr>
								<td style="height: 12px;"><div  class="change_header">升比率</div></td>
							</tr>
						</table>
					</td>
					<td>
						<table id="change_downtable" class="change_table" >
							<tr >
								<td style="height: 12px;"><div  class="change_header">降比率</div></td>
							</tr>
						</table>
					</td>
					<td>
						<table id="change_totaltable" class="change_table" >
							<tr>
								<td style="height: 12px;"><div  class="change_header" style="font-size:12px;line-height: 12px;">投入量</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>