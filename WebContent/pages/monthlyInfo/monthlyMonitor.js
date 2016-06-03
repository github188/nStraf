var basePath = "/nStraf";
var app = {};

app.urlGetMonthlyInfo = basePath + "/monthlyInfo!getMonthlyInfo.action";

app.msg = {};
app.operate = {};
app.duration = {};
app.data = {};

app.data.month = [ "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月",
		"11月", "12月" ];
app.data.month2 = [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
                   "十一月", "十二月" ];
app.data.week= [ "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"];

app.msg.dataloading = "正在加载数据，请稍候";

app.msg.loadMonthlyTotalInfoError = "加载总数统计失败，请稍后再试";
app.msg.loadMonthlyDeptInfoError = "加载部门人员信息失败，请稍后再试";
app.msg.loadMonthlyChangeInfoError = "加载人员变动信息失败，请稍后再试";
app.msg.loadMonthlyContractInfoError = "加载合同信息失败，请稍后再试";
app.msg.loadMonthlyProjectInfoError = "加载项目信息失败，请稍后再试";
app.msg.loadMonthlyGoodEmployeeInfoError = "加载优秀员工信息失败，请稍后再试";
app.msg.loadMonthlyAttenceInfoError = "加载考勤信息失败，请稍后再试";

app.msg.submitJsonError = "很抱歉,后台解析数据异常,请稍后再试";
app.msg.linkError = "连接异常，请检查您的网络设置或稍后再试";

app.defaultCountdownTime = 60;
app.overTime = 20000;
app.dowmTime = 120;

// toast持续时间
app.duration.validateToast = 2000;
app.duration.ajaxErrorToast = 15000;
app.duration.ajaxSucceedToast = 2000;
app.duration.closePage = 2000;

var companyStaffTotal = 0;//公司员工总数
var globalData2 = {};
var globalData={
		 curYear:'2014',
		curProjectId:'01',
		curProjectName:'运通信息作战系统名字很长很长很长会怎么显示',
		dateSelectData:[2014,2013,2012,2011,2010,2009,2008,2007,2006,2005],
		chart:{
			categories:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
			seriesData:[200, 280, 250, 360, 180, 220, 250,320,330,0,0,0],
			tickInterval:100
		},
		projectInfo:[
			{id:"01",name:"运通信息作战系统名字再长一点是什么情况",total:"1024"},
			{id:"02",name:"招聘官网",total:"1200"},
			{id:"03",name:"广发VTM项目",total:"4620"},
			{id:"04",name:"广电运通官网升级项目",total:"2200"},
			{id:"05",name:"广电冠字号平台",total:"980"},
			{id:"06",name:"上海银行项目",total:"800"},
			{id:"07",name:"晋中银行人力外包",total:"3256"},
			{id:"08",name:"中银香港",total:"1800"}
		] 
	};
var globalData1 = {
	"monthlyDeptInfo" : [// 部门月度信息
	{
		"deptId" : "dept1",
		"deptName" : "dept1",
		"staffTotal" : 40,// 部门总人数
		"absentTotal" : 2,// 缺席总人数
		"deptColor" : "#FF6100",
		"lateTotal" : 1,// 迟到总人数
		"trainTotal" : 2
	// 参加培训人数
	}, {
		"deptId" : "dept2",
		"deptName" : "dept2",
		"staffTotal" : 50,
		"deptColor" : "#802A2A",
		"absentTotal" : 2,// 缺席总人数
		"lateTotal" : 1,// 迟到总人数
		"trainTotal" : 2
	// 参加培训人数
	} ],
	"staffChangeInfo" : [// 人员变动信息
	{
		"month" : 1,
		"totalNum" : 180,// 上月月底总人数
		"add" : 5,// 上月新增人数
		"leave" : 3
	// 上月离职人数
	}, {
		"month" : 2,
		"totalNum" : 180,
		"add" : 5,
		"leave" : 3
	} ],
	"contractInfo" : [// 合同信息
	{
		"month" : 1,
		"amount" : 1800,// 合同金额
		"status" : 0
	// 合同状态
	}, {
		"month" : 2,
		"amount" : 2800,
		"status" : 0
	// 合同状态
	} ],
	"projectInfo" : [// 项目信息
	{
		"prjId" : "zzxt",
		"prjName" : "作战系统",
		"custName" : "运通信息",
		"personDay" : 56,// 上月人日投入
		"riskNum" : 0.2
	// 风险率
	}, {
		"prjId" : "cash",
		"prjName" : "钞票图像采集系统",
		"custName" : "运通信息",
		"personDay" : 56,
		"riskNum" : 0.2
	} ],
	"goodEmployee" : [// 优秀员工
	{
		"userId" : "tmr1",
		"userName" : "唐马儒1",
		"deptId" : "kaifatwo",
		"deptName" : "开发二部",
		"duty" : "软件工程师"
	}, {
		"userId" : "tmr2",
		"userName" : "唐马儒2",
		"deptId" : "kaifatwo",
		"deptName" : "开发二部",
		"duty" : "软件工程师"
	} ],
	"attenceInfo" : [// 考勤数据
	{
		"year" : 2014,// 年
		"month" : 10,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 1,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			}, {
				"deptId" : "kaifatwo",
				"deptName" : "开发一部",
				"absentNum" : 2,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 10,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 12,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 10,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 3,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 10,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 28,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 11,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 3,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 10,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 19,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	}, {
		"year" : 2014,// 年
		"month" : 5,// 月
		"detail" : [// 当月每日详情，只记录不为0的日期
		{
			"day" : 1,
			"deptDetail" : [ {
				"deptId" : "kaifatwo",
				"deptName" : "开发二部",
				"absentNum" : 1,
				"lateNum" : 2
			} ]
		} ]
	} ],
	"totalInfo" : {
		"contractTotal" : 2423324234,
		"contractRate" : 0.99,
		"staffTotal" : 168,
		"projectTotal" : 24,
		"trainTotal" : 223
	}
	
}

function initData() {
	fillMonthlyTotalInfo();
	drawAttenceLineCharts();
	drawStaffPieCharts();
	drawAttencePieCharts();
	drawContractLineCharts() ;
	drawGoodEmployeeTable();
	drawProjectTable();
	drawAttenceCalendars();
	drawStaffChangeLineCharts();
	//alert(basePath)
	/*getMonthlyInfo("getMonthlyChangeInfo");
	getMonthlyInfo("getMonthlyTotalInfo");
	getMonthlyInfo("getMonthlyDeptInfo");
	getMonthlyInfo("getMonthlyProjectInfo");
	getMonthlyInfo("getMonthlyContractInfo");
	getMonthlyInfo("getMonthlyGoodEmployeeInfo");
	getMonthlyInfo("getMonthlyAttenceInfo");*/
}


//填充总数统计信息
function fillMonthlyTotalInfo(){
	var totalInfo = globalData.totalInfo;
	var contractTotal = totalInfo.contractTotal/10000;
	var contractRate = totalInfo.contractRate*100;
	var staffTotal = totalInfo.staffTotal;
	var projectTotal = totalInfo.projectTotal;
	var trainTotal = totalInfo.trainTotal;
	if(totalInfo!=""){
		$("#total_contractNum").text(contractTotal.toFixed(2)+"W");
		$("#total_contractRate").text(contractRate + "%");
		$("#total_staffNum").text(staffTotal);
		$("#total_projectNum").text(projectTotal);
		$("#total_trainNum").text(trainTotal);
	}else{
		$("#total_contractNum").text("0W");
		$("#total_contractRate").text("0%");
		$("#total_staffNum").text(0);
		$("#total_projectNum").text(0);
		$("#total_trainNum").text(0);
	}
}
// 绘制人员结构圈图
function drawStaffPieCharts() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var lastMon = month - 1;
	if (lastMon == 0) {
		lastMon = 12;
	}
	// 人员结构数据
	var staffDistPieData = new Array();
	var staffDistPieLegendData = new Array();
	var legendTotalRates = {};
	var colors = new Array();
	for ( var i = 0; i < globalData.monthlyDeptInfo.length; i++) {
		// 组装人员结构数据
		var deptInfo = globalData.monthlyDeptInfo[i];
		var staffTotal = parseInt(deptInfo.staffTotal);
		companyStaffTotal += staffTotal;
	}
	for ( var i = 0; i < globalData.monthlyDeptInfo.length; i++) {
		// 组装人员结构数据
		var deptInfo = globalData.monthlyDeptInfo[i];
		var deptName = deptInfo.deptName;
		var deptColor = deptInfo.deptColor;
		var staffTotal = parseInt(deptInfo.staffTotal);
		var staffTotalRate = ((staffTotal/companyStaffTotal)*100).toFixed(1);
		//alert(JSON.stringify(globalData.monthlyDeptInfo))
		
		var temp = {};
		temp.y = staffTotal;
		temp.name = deptName;
		temp.color = deptColor;
		temp.labelDetail = "　" + ((staffTotal/companyStaffTotal)*100).toFixed(1) + "%";
		temp.events =  {
        	//控制图标的图例legend不允许切换
			legendItemClick: function (event) {                                   
				return false; //return  true 则表示允许切换
        	}
        }
		legendTotalRates[deptName] = staffTotalRate;
		colors.push(deptColor);
		staffDistPieData.push(temp);
		
		var legend = {};
		legend.name = deptName;
		legend.textStyle = {fontWeight:'bold', color:deptColor};
		legend.icon = "";
		staffDistPieLegendData.push(legend);

	}

	// Create the chart
	$('#staffDistributePie').highcharts({
		credits: {
		     enabled: false
		},
	    chart: {
	        type: 'pie',
	        backgroundColor: '#F6F6F6'
	    },
	    title:false,
	   /* title: {
	        text: '人员结构图',
	        align:'left',
	        style:{
	        	"color":"#333333",
	        	"fontWeight" : "bold"
	        }
	    },*/
	    yAxis: {
	
	    },
	    plotOptions: {
	        pie: {
	            shadow: false,
	            center: ['45%', '45%'],
	            dataLabels: {
	                enabled: false
	            },
	            showInLegend: true
	        }
	    },
	    legend:{
	    	align:'right',
	    	verticalAlign : "middle",
	    	layout:'vertical',
	    	symbolHeight:12,
	    	padding:0,
	    	itemStyle :  {
	    		"color": "#333333", "fontSize": "12px"
	    	},
	    	//labelFormat:"{name}" + "　" + staffTotalRate*100 + "%"
	    	labelFormatter:function(){
	    		return this.name + this.labelDetail
	    	}	
	    },
	    series: [{
	        name: '人数',
	        data: staffDistPieData,
	        size: '90%',
	        innerSize: '45%',
	        dataLabels: {
	            formatter: function() {
	                // display only if larger than 1
	                return this.y > 1 ? + this.point.name + this.y +'%'  : null;
	            }
	        }
	    }]
	});

}
// 绘制缺勤圈图
function drawAttencePieCharts() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var lastMon = month - 1;
	if (lastMon == 0) {
		lastMon = 12;
	}
	// 缺勤分布数据
	var staffAttencePieData = new Array();
	var staffAttencePieLegendData = new Array();
	var attenceLegendRates = {};//每个部门缺勤比率
	var attenceLegendNums = {};//每个部门缺勤人数
	var colors = new Array();
	//alert(JSON.stringify(globalData.monthlyDeptInfo));
	//求全公司缺勤总人次
	var absentSum = 0;
	for ( var i = 0; i < globalData.monthlyDeptInfo.length; i++) {
		var deptInfo = globalData.monthlyDeptInfo[i];
		absentSum +=(deptInfo.absentTotal + deptInfo.lateTotal);
	}
	for ( var i = 0; i < globalData.monthlyDeptInfo.length; i++) {
		var deptInfo = globalData.monthlyDeptInfo[i];
		var deptName = deptInfo.deptName;
		var deptColor = deptInfo.deptColor;
		var staffTotal = deptInfo.staffTotal;
		var absentTotal = deptInfo.absentTotal==undefined?0 :deptInfo.absentTotal;
		var lateTotal = deptInfo.lateTotal==undefined?0 :deptInfo.lateTotal;
		absentTotal += lateTotal;
		var absentTotalRate = 0;
		if(absentSum>0){
			absentTotalRate = ((absentTotal/absentSum)*100).toFixed(1);
		}
		var temp = {};
		
		temp.y = absentTotal;
		temp.color = deptColor;
		temp.name = deptName;
		temp.labelDetail = "　" + absentTotal + "人次　" + absentTotalRate + "%";
		temp.events =  {
	        	//控制图标的图例legend不允许切换
				legendItemClick: function (event) {                                   
					return false; //return  true 则表示允许切换
	        	}
	        }
		
		staffAttencePieData.push(temp);

		attenceLegendRates[deptName] = absentTotalRate;
		attenceLegendNums[deptName] = absentTotal;

		colors.push(deptColor);
		
		var legend = {};
		legend.name = deptName;
		legend.textStyle = {fontWeight:'bold', color:deptColor};
		legend.icon = "";
		staffAttencePieLegendData.push(legend);
	}
	//alert(JSON.stringify(staffAttencePieData))
	// 缺勤
	
	// Create the chart
	$('#staffAttencePie').highcharts({
		credits: {
		     enabled: false
		},
	    chart: {
	        type: 'pie',
	        backgroundColor: '#F6F6F6'
	    },
	    title:false,
	   /* title: {
	        text: '缺勤结构图',
	        align:'left',
	        style:{
	        	"color":"#333333",
	        	"fontWeight" : "bold"
	        }
	    },*/
	    yAxis: {
	
	    },
	    plotOptions: {
	        pie: {
	            shadow: false,
	            center: ['53%', '45%'],
	            dataLabels: {
	                enabled: false
	            },
	            showInLegend: true
	        }
	    },
	    legend:{
	    	align:'right',
	    	verticalAlign : "middle",
	    	layout:'vertical',
	    	symbolHeight:12,
	    	padding:0,
	    	itemStyle :  {
	    		"color": "#333333", "fontSize": "12px"
	    	},
	    	//labelFormat:"{name}" + "　" + staffTotalRate*100 + "%"
	    	labelFormatter:function(){
	    		return this.name + this.labelDetail
	    	}	
	    },
	    series: [{
	        name: '人数',
	        data: staffAttencePieData,
	        size: '90%',
	        innerSize: '45%',
	        dataLabels: {
	            formatter: function() {
	                // display only if larger than 1
	                return this.y > 1 ? + this.point.name + this.y +'%'  : null;
	            }
	        }
	
	        
	    }]
	});
}
// 绘制人员增长折线图
function drawStaffChangeLineCharts() {
	// 组装折线图数据
	// 人员增长
	var staffChangeInfo = globalData.staffChangeInfo;
	var addNumArr = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
	var leaveNumArr = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
	for ( var i = 0; i < staffChangeInfo.length; i++) {
		var month = staffChangeInfo[i].month - 1;
		var addNum = staffChangeInfo[i].add;
		var leaveNum = staffChangeInfo[i].leave;
		if(month==undefined){
			continue;
		}
		addNumArr[month] = addNum==undefined? 0 : addNum;
		leaveNumArr[month] = leaveNum==undefined? 0 : leaveNum;
	}
	/*var staffChangeOption = {
		title : {
			text : '人员增长'
		},
		legend : {
			data : [ '入职', '离职' ],
			x : 'center'
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : true,
			data : app.data.month
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			name : '入职',
			type : 'line',
			stack : '总量',
			symbolSize : 2,
			smooth : false,
			large : false,
			data : addNumArr
		}, {
			name : '离职',
			type : 'line',
			stack : '总量',
			data : leaveNumArr
		} ]
	};*/

	// 开始绘图
/*	require([ 'echarts', 'echarts/chart/line' ], function(ec) {
		var staffChangeLine = ec.init(document
				.getElementById('staffChangeLine'));
		staffChangeLine.setOption(staffChangeOption);
	});*/
	//绘制折线图
	 $('#staffChangeLine').highcharts({
	        chart: {
	            type: 'line',
	            spacingLeft:-1,
	            backgroundColor: '#F6F6F6'
	        },
	        title:false,
	        /*title: {
	            text: '人员增长',
	            align:'left',
	            style:{'fontWeight':'bold'}
	        },*/
	        legend: {
	        	enabled: true,  //设置图例不可见 
	        	align:"center",
	        	verticalAlign: 'top'
	        },
	        xAxis: {
	            labels: {
	            	formatter: function() {
	            		return '<font style="font-size:9px;">'+app.data.month[this.value]+'</font>';
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
	            minorTickWidth: 0,
	            min: 0,
                startOnTick :false
	        },
	        tooltip: {
	        	formatter: function() { //格式化提示信息 
	                return this.y+"人"; 
	            },
	            shared:false
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
	        series: [
	            {
		        	name:"入职",
		        	data: addNumArr,
		        	color : "#39AFE2"
		        },
		        {	
		        	name : "离职",
					data : leaveNumArr,
					color : "#ED3133"
				} 
	        ]
	    });
}
// 绘制 缺勤折线图
function drawAttenceLineCharts() {
	// 组装折线图数据
	// 缺勤统计
	var attenceInfo = globalData.attenceInfo;
	for ( var k = 0; k < attenceInfo.length; k++) {
		var month = attenceInfo[k].month - 1;
		var detail = attenceInfo[k].detail;
		var attenceLineData = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
		var absentTotal = 0;
		for ( var i = 0; i < detail.length; i++) {
			var deptDetail = detail[i].deptDetail;
			//alert(JSON.stringify(deptDetail))
			for ( var j = 0; j < deptDetail.length; j++) {
				var absentNum = deptDetail[j].absentNum==undefined? 0 : deptDetail[j].absentNum;
				var lateNum = deptDetail[j].lateNum==undefined? 0 : deptDetail[j].lateNum ;
				absentTotal = absentTotal + absentNum + lateNum;
			}
			attenceLineData[month] = absentTotal;
		}
	}
	/*var staffAttenceLineOption = {
		title : {
			text : '缺勤统计'
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : true,
			data : app.data.month,
			splitNumber : 3
		} ],
		yAxis : [ {
			type : 'value',
			splitNumber : 3
		} ],
		series : [ {
			name : '总数',
			type : 'line',
			smooth : false,
			itemStyle : {
				normal : {
					areaStyle : {
						type : 'default'
					}
				}
			},
			data : attenceLineData
		} ]
	};

	// 开始绘图
	require([ 'echarts', 'echarts/chart/line' ], function(ec) {
		var staffAttenceLine = ec.init(document
				.getElementById('staffAttenceLine'));
		staffAttenceLine.setOption(staffAttenceLineOption);
	});*/
	
	//绘制折线图
	 $('#staffAttenceLine').highcharts({
	        chart: {
	            type: 'areaspline',
	            spacingLeft:-1,
	            backgroundColor: '#F6F6F6'
	        },
	        title:false,
	        /*title: {
	            text: '缺勤统计',
	            align:'left',
	            style:{'fontWeight':'bold'}
	        },*/
	        legend: {
	        	enabled: false  //设置图例不可见 
	        },
	        xAxis: {
	            labels: {
	            	formatter: function() {
	            		return '<font style="font-size:9px;">'+app.data.month[this.value]+'</font>';
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
	            minorTickWidth: 0,
	            min: 0,
                startOnTick :false
	        },
	        tooltip: {
	        	formatter: function() { //格式化提示信息 
	                return this.y+"人"; 
	            },
	            shared:false
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
	        	data: attenceLineData
	        }
	        ]
	    });
}

// 绘制合同统计折线图
function drawContractLineCharts() {
	// 组装折线图数据
	// 合同统计
	var contractInfo = globalData.contractInfo;
	//alert(JSON.stringify(contractInfo))
	var amountArr = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];

	for ( var i = 0; i < contractInfo.length; i++) {
		var month = contractInfo[i].month - 1;
		var amount = contractInfo[i].amount;
		amountArr[month] = amount;
	}

	/*var contractInfoOption = {
		title : {
			text : '合同统计'
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : true,
			data : app.data.month
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			type : 'line',
			data : amountArr
		} ]
	};

	// 开始绘图
	require([ 'echarts', 'echarts/chart/line' ], function(ec) {
		var staffContractLine = ec.init(document
				.getElementById('staffContractLine'));
		staffContractLine.setOption(contractInfoOption);
	});*/

	//绘制折线图
	 $('#staffContractLine').highcharts({
	        chart: {
	            type: 'line',
	            spacingLeft:-1,
	            backgroundColor: '#F6F6F6'
	        },
	        title:false,
	        /*title: {
	            text: '合同统计',
	            align:'left',
	            style:{'fontWeight':'bold'}
	        },*/
	        legend: {
	        	enabled: false  //设置图例不可见 
	        },
	        xAxis: {
	            labels: {
	            	formatter: function() {
	            		return '<font style="font-size:9px;">'+app.data.month[this.value]+'</font>';
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
	            minorTickWidth: 0,
                min: 0,
                startOnTick :false
	        },
	        tooltip: {
	        	formatter: function() { //格式化提示信息 
	                return this.y+"元"; 
	            },
	            shared:true
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
	        	data: amountArr
	        }
	        ]
	    });
}
// 绘画项目信息表格
function drawProjectTable() {
	var projectInfo = globalData.projectInfo;
	//alert(projectInfo.length)
	for ( var i = 0; i < projectInfo.length; i++) {
		var prjId = projectInfo[i].prjId;
		var custName = projectInfo[i].custName;
		var prjName = projectInfo[i].prjName;
		var personDay = projectInfo[i].personDay;
		var riskNum = projectInfo[i].riskNum;
		var expNum = projectInfo[i].expNum==undefined?0:expNum;
		var process = projectInfo[i].process==undefined?0:process;
		var finished = projectInfo[i].finished==undefined?0:finished;
		//finished = 0.6;
		var grid1 = "<div class='prjInfo_grid1 prjInfo_grid' id='" + prjId + "'>" + (i+1)
				+ ".</div>";
		var grid2 = "<div class='prjInfo_grid2 prjInfo_grid'>" + custName + "</div>";
		var grid3 = "<div class='prjInfo_grid3 prjInfo_grid'>" + prjName + "</div>";
		var grid4 = "<div class='prjInfo_grid4 prjInfo_grid'>" + expNum + "</div>";
		var grid5 = "<div class='prjInfo_grid5 prjInfo_grid'>" + personDay + "</div>";
		var grid6 = "<div class='prjInfo_grid6 prjInfo_grid'>" + process + "</div>";
		var grid7 = "<div class='prjInfo_grid7 prjInfo_grid'>" + riskNum + "</div>";
		
		var progressbar = "<div class='finish_progressbar'><div class='progressNum'>"+ finished*100 +"%</div><div class='finish_progress'  progress=" + finished + ">" + "</div></div>"
		var grid8 = "<div class='prjInfo_grid8 prjInfo_grid'>" + progressbar + "</div>";
		
		var rowDiv = "<div class='prjInfo_rowDiv'>" + grid1 + grid2 + grid3 + grid4 + grid5 + grid6 + grid7 + grid8 + "</div>";
		$("#projectInfo").append(rowDiv);
	};
	//根据进度改变颜色
	$(".finish_progress").each(function(){
		var progress = $(this).attr("progress")*100;
		var width = progress;
		var color = "#42A9FF";
		if(progress < 30){
			color = "#F37066";//0-30红色
		}else if(progress < 70){
			color = "#22B9F2";//30-70蓝色
		}else {
			color = "#62C169";//>70绿色
		}
		//alert(width)
		$(this).css("width", width + "%");
		$(this).css("background-color", color);
	})

}

// 绘画优秀员工表格
function drawGoodEmployeeTable() {
	var goodEmployee = globalData.goodEmployee;
	for ( var i = 0; i < goodEmployee.length; i++) {
		var userId = goodEmployee[i].userId;
		var userName = goodEmployee[i].userName;
		var deptId = goodEmployee[i].deptId;
		var deptName = goodEmployee[i].deptName;
		var duty = goodEmployee[i].duty;
		var headerDiv = "<div class='headerImage' id='" + userId + "'></div>";
		var nameDiv = "<div class='goodEmpInfo_name'>" + userName + "</div>";
		var dutyDiv = "<div class='goodEmpInfo_dept_duty'>" + deptName + "--"
				+ duty + "</div>";
		var empInfoDiv = "<div class='goodEmpInfo'>" + nameDiv + dutyDiv
				+ "</div>";
		var goodEmployeeRow = "<div class='goodEmployeeRow'>" + headerDiv
				+ empInfoDiv + "</div>";

		$("#goodEmployeeArea").append(goodEmployeeRow);
	}
	;

}
// 生成日历日程events数组，即每天早退和迟到总人数
function generateAttenceEvents() {
	// 遍历考勤统计数据
	var attenceInfo = globalData.attenceInfo;

	// alert(JSON.stringify(attenceInfo))
	var events = [];
	for ( var i = 0; i < attenceInfo.length; i++) {
		var year = attenceInfo[i].year;
		var month = attenceInfo[i].month;
		var detail = attenceInfo[i].detail;
		for ( var j = 0; j < detail.length; j++) {
			var day = detail[j].day;
			//一位数日期必须以0开头
			if((day.toString()).length==1){
				day = "0" + day;
			}
			var date = year + "/" + month + "/" + day;
			var deptDetail = detail[j].deptDetail;
			var absentTotal = 0;
			var lateTotal = 0;
			//alert(JSON.stringify(deptDetail))
			for ( var k = 0; k < deptDetail.length; k++) {
				var absentNum =  parseInt(deptDetail[k].absentNum==undefined? 0 : deptDetail[k].absentNum);
				var lateNum = parseInt(deptDetail[k].lateNum==undefined? 0 : deptDetail[k].lateNum);
				absentTotal += absentNum;
				lateTotal += lateNum;

			}

			var title = (absentTotal + lateTotal).toString();
			events.push({
				title : title,
				start : date,
				end : date
			});
		}
	}
	//alert(JSON.stringify(events))
	return events;
}

// 绘画日历
function drawAttenceCalendars() {
	var ele = $("#lateCalendar_container");
	var events = generateAttenceEvents();
	// $("#test").html(JSON.stringify(events.join("")))
	// alert(JSON.stringify(events))
	ele.fullCalendar({
		height:400,
		handleWindowResize:false,
		theme : false,
		weekends : true,//显示周六日
		aspectRatio:1.5,
		header : {
			left : 'prev',
			center : 'title',
			right : 'next'
		},
		titleFormat:{
		    month: 'yyyy年 M月'                           // September 2009
		},
		/*buttonText:{
			prev:     '上一月',
			next:     '下一月',
			prevYear: '上一年',
			nextYear: '下一年',
			today:    '今天',
			month:    '月',
			week:     '周',
			day:      '日'
			},*/
		weekMode : 'liquid',
		weekNumbers : false,
		events : events,
		dayClick : function(date, allDay, jsEvent, view) {
			var selDate = $.fullCalendar.formatDate(date, 'yyyy-MM-dd');// 格式化日期

			drawLateDeptsInPopWin(selDate,jsEvent.pageX,jsEvent.pageY);
		},
		eventClick : function(calEvent, jsEvent, view) {
			var selDate = $.fullCalendar.formatDate(calEvent.start,
					'yyyy-MM-dd');// 格式化日期;
			drawLateDeptsInPopWin(selDate,jsEvent.pageX,jsEvent.pageY);

		},
		eventMouseover : function(calEvent, jsEvent, view) {
			//console.info(this)
			var selDate = $.fullCalendar.formatDate(calEvent.start,
			'yyyy-MM-dd');// 格式化日期;
			drawLateDeptsInPopWin(selDate,jsEvent.pageX,jsEvent.pageY);
		},
		eventMouseout : function(calEvent, jsEvent, view) {
			//$("#popWin").hide()				
		},
		eventAfterRender:function( event, element, view ) {
			//customFullcalendar()//每次渲染日程事件后，JS修改日历UI
		}
	})
	//ele.fullCalendar('prev');// 默认显示上一个月
	/*var date = "2011-01";
	var arr = date.split("-") 
	var d = new Date();
	d.setFullYear(arr[0],arr[1],arr[2]);*/
	if(globalData.year!=undefined||globalData.month!=undefined){
		ele.fullCalendar( 'gotoDate',globalData.year,globalData.month-1);//指定某年某月
	}
	else{
		ele.fullCalendar('prev');// 默认显示上一个月
	}
}

//通过JS修改日历样式(由于此方法在IE8运行太慢，使用修改后的fullcalendar_modify.css替代)
function customFullcalendar(){
	$(".fc-event, .fc-agenda .fc-event-time, .fc-event a").each(function(){
		$(this).css("text-align","center");
		$(this).css("background-color","transparent");
		$(this).css("border-color","transparent");
		$(this).css("color","#4A8BBE");
		$(this).css("font-size","25px");
	})
	$(".fc-header").each(function(){
		$(this).css("margin","5px auto");
	})
	$(".fc-corner-left,.fc-corner-right").each(function(){
		$(this).css("margin","5px");
	})
	$(".fc-header-title").each(function(){
		$(this).css("margin","10px auto");
		$(this).css("font-size","16px");
	})
}

// 弹窗窗口函数
function popUpAttenceWin(selDate,ele) {
	
	// 在弹出窗口绘制表格
	drawLateDeptsInPopWin(selDate);

}

// 绘画弹出窗口考勤情况表格
function drawLateDeptsInPopWin(date,x,y) {
	clearDivByClass("popDataRow");// 清空弹窗内数据

	//$("#popWin").attr("style","width:500px;" + "position:relative");
	var dateArr = date.split("-");
	var year = dateArr[0];
	var month = dateArr[1];
	var day = dateArr[2];

	// 把01修改为1
	if (month.split("")[0] == "0") {
		month = month.split("")[1];
	}
	if (day.split("")[0] == "0") {
		day = day.split("")[1];
	}
	// 遍历考勤统计数据生存表格
	var attenceInfo = globalData.attenceInfo;
	//console.info(attenceInfo);
	var isExist = false;
	for ( var i = 0; i < attenceInfo.length; i++) {
		if (year == attenceInfo[i].year && month == attenceInfo[i].month) {
			var detail = attenceInfo[i].detail;
			for ( var j = 0; j < detail.length; j++) {
				if (day == detail[j].day) {
					isExist = true;
					var deptDetail = detail[j].deptDetail;
					for ( var k = 0; k < deptDetail.length; k++) {
						var deptName = deptDetail[k].deptName;
						var absentNum =  parseInt(deptDetail[k].absentNum==undefined? 0 : deptDetail[k].absentNum);
						var lateNum = parseInt(deptDetail[k].lateNum==undefined? 0 : deptDetail[k].lateNum);
						absentNum+=lateNum;
						/*var deptNameDiv = "<div class='popDataDiv_left'>" + deptName
								+ "</div>";
						var absentNumDiv = "<div class='popDataDiv'>"
								+ absentNum + "</div>";
						var lateNumDiv = "<div class='popDataDiv'>" + lateNum
								+ "</div>";
						var dataRow = deptNameDiv + lateNumDiv + absentNumDiv;*/
						var dataRow = "<div class='popDataRow'>" + deptName + "：" + absentNum + "</div>";
						var arrow =  "<div class='popWinArrow' id='popWinArrow'><div>";
						$("#popWin").append(dataRow);
						$("#popWin").append(arrow);
						var width = $("#popWin").width();
						var arroWidth = $("#popWinArrow").css("border-left-width").split("px")[0];
						//$("#popWin").css("height",arroWidth * deptDetail.length + "px");
						$("#popWin").css("left",x-width/2-arroWidth/2 + "px");
						$("#popWin").css("top",y + "px");
						$("#popWin").css("display","block");
					}
				}
			}
		}
	}
	if(!isExist){
		$("#popWin").css("display","none");
	}
}

// 移除DIV
function clearDivByClass(dClass) {
	var classes = dClass.split(",");
	for(var i=0;i<classes.length;i++){
		$("div." + classes[i]).each(function() {
			$(this).remove();
		})		
	}
}


// 获取月度数据
function getMonthlyInfo(action) {
	//var params = $("#params").val();
	var dataType = "";
	var drawMethod = "";
	var ajaxErrorToast = "";
	switch (action) {
	case "getMonthlyTotalInfo":
		drawMethod = "fillMonthlyTotalInfo";
		dataType = "totalInfo";
		ajaxErrorToast = app.msg.loadMonthlyTotalInfoError;
		break;
	case "getMonthlyDeptInfo":
		drawMethod = "drawStaffPieCharts,drawAttencePieCharts";
		dataType = "monthlyDeptInfo";
		ajaxErrorToast = app.msg.loadMonthlyDeptInfoError;
		break;
	case "getMonthlyChangeInfo":
		drawMethod = "drawStaffChangeLineCharts";
		dataType = "staffChangeInfo";
		ajaxErrorToast = app.msg.loadMonthlyChangeInfoError;
		break;
	case "getMonthlyContractInfo":
		drawMethod = "drawContractLineCharts";
		dataType = "contractInfo";
		ajaxErrorToast = app.msg.loadMonthlyContractInfoError;
		break;
	case "getMonthlyProjectInfo":
		drawMethod = "drawProjectTable";
		dataType = "projectInfo";
		ajaxErrorToast = app.msg.loadMonthlyProjectInfoError;
		break;
	case "getMonthlyGoodEmployeeInfo":
		drawMethod = "drawGoodEmployeeTable";
		dataType = "goodEmployee";
		ajaxErrorToast = app.msg.loadMonthlyGoodEmployeeInfoError;
		break;
	case "getMonthlyAttenceInfo":
		drawMethod = "drawAttenceCalendars,drawAttenceLineCharts";
		dataType = "attenceInfo";
		//ajaxErrorToast = app.msg.loadMonthlyAttenceInfoError;
		break;
	}
	//alert(drawMethod + "--" + dataType+ "--" +  url + "--" + ajaxErrorToast)
	//lalert(action)
	//alert(app.urlGetMonthlyInfo + action + dataType)
	app.urlGetMonthlyInfo = app.urlGetMonthlyInfo + "?action=" + action;
	$.ajax({
		cache : false,
		timeout : app.overTime,
		url : app.urlGetMonthlyInfo,
		type : "post",
		async:true,
		success : function(d) {
			if (d.retcode == 0) {
				//初始化数据
				data = $.parseJSON(d.data);
				var dataTypes = dataType.split(",");
				for(var i=0;i<dataTypes.length;i++){
					globalData[dataTypes[i]] = data[dataTypes[i]];
				}
				//alert(JSON.stringify(globalData))
				//初始化员工总数
				if(companyStaffTotal==0){
					if(globalData.monthlyDeptInfo!=undefined){
						for ( var i = 0; i < globalData.monthlyDeptInfo.length; i++) {
							// 组装人员结构数据
							var deptInfo = globalData.monthlyDeptInfo[i];
							companyStaffTotal += parseInt(deptInfo.staffTotal);
						}

					}
				}
				//alert(JSON.stringify(globalData))
				//利用反射调用对应方法绘图
				var drawMethods = drawMethod.split(",");
				for(var i=0;i<drawMethods.length;i++){
					//alert(drawMethods[i])
					var func = eval(drawMethods[i]);
					new func();
				}
			} else if (d.retcode == -1) {
				toast(ajaxErrorToast,
						app.duration.ajaxErrorToast);
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			toast(app.msg.linkError, app.duration.ajaxErrorToast);
			return false;
		}
	});
}