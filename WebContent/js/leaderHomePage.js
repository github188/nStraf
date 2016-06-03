	var REQUEST_PATH = "/nStraf/leaderHomePageInfo!loadLeaderHomePageInfoForWeb.action";
	var gabolContractInfo = {};
	function loadData(){
		//重新加载数据
		var actionUrl = REQUEST_PATH;
		actionUrl=encodeURI(actionUrl);
		$.ajax({
			url : actionUrl,
			data : {},
			type : 'POST',
			dataType : 'json',
			timeout : 30000,
			success : function(data) {
				//console.info(data);
				//当浏览器窗口改变时改变同时改变合同速度计中的刻度值
				gabolContractInfo = data.contractInfo;
				loadMap(data.mapInfos);
				loadContract(data.contractInfo);
				loadPersonDay(data.pDayInfos);
				loadDeptMonthPersonDay(data.deptMonthPDayInfos);
				$("#loading").hide();
			},
			error : function(data, data1) {
			//	alert("加载数据失败!");
				document.getElementById('loading').innerHTML="" ;
				$("#loading").append('<div class="jiazai">数据加载失败</div>');
			}
		});
	}
	
	/**
	加载地图
	*/
	
	function loadMap(data) {
		//触发touchmove事件时，阻止其他所有事件触发
		/*var obj = document.getElementById('container'); 
		obj.addEventListener('touchmove', function(event) { 
			event.stopPropagation();
			event.preventDefault(); 
		}, false);*/
    $('#container').highcharts('Map', {
    	title : {
            text : false
        },
        subtitle : {
            text : false
        },
        credits:{
        	enabled:false
        },
        exporting:{
        	enabled:false
        },
        legend:{
        	enabled:false
        },
        tooltip: {
        	animation:false,
        	enabled:true,
//        	snap: 5,
        	hideDelay: 0,
        	followPointer:false,
			//shared:true,
           // followTouchMove:true,
            formatter: function(){
            	
            	var res = "";
    			if(this.point.projects!=undefined){
    				var projectArray = this.point.projects.split(',');
    				
    				if(projectArray.length>0)
    				{
    					//res = projectArray[0];
    					for (var i = 0, l = projectArray.length; i < l; i++) 
    					{
    						if(projectArray[i]!='')
    						{
    						res +=projectArray[i]+'<br/>' ;
    						}
    					}
    					res += this.series.name+':'+this.point.name+ '<br/>项目数: ';
    					var text = 0;
    					if(this.point.value!=0){
    						text +=this.point.value-10;
    					}else{
    						text +=this.point.value;
    						return false;
    					}
    					res +=text;
    				}
    			}
    			return res;
				}
		},
        mapNavigation: {
            enabled: true,
            enableMouseWheelZoom:false,
            buttonOptions: {
                verticalAlign: 'middle',
                align:'right',
                height: 20,
                width: 10}
        },
        colorAxis: {
        	min: 0,
            minColor: '#FFE76E',
            maxColor: '#E33331'
        },
        /*labels:{
        	items:{
        		html:'<img alt="战略红旗" src="./images/hongdian.png">'
        	}
        },*/
        series : [{
        	// color: '#FF0000',
            data : data,
            mapData: Highcharts.maps['countries/china'],
            joinBy: 'hckey',
            name: '项目分布',
           // allowPointSelect: true,
            //enableMouseTracking:false,
            selected:true,
//            events: {
//                click: function (e) {
//                    e.point.zoomTo();
//                }
//            },
            states: {
                hover: {
                    color: '#BADA55'
                }
            },
            dataLabels: {
                enabled: true,
                color: '#3A3C3B',
                format: '{point.name}',
                style:{
                	HcTextStroke:false
                }
            }
            /*dataLabels: {
            	 enabled: true,
                 color: 'white',
	            formatter: function() {
	                // display only if larger than 1
	                return  this.point.name +'</br> '+ '<img alt="战略红旗" src="./images/hongdian.png">';
	            }
	        }*/
        }]
    });
    $('#zoom-out').click(function () {
        $('#container').highcharts().mapZoom();
    });
}

	
	/*合同*/
	function loadContract(data) {
	    var colors = ['#FA0100','#FFFF00','#07FD04'];
	    //计划总额
	    var maxContract = data.contractPlan;
	    //目前为止合同金额
	    var sumContract = data.sumContract;
	    //如果合同总额大于计划总额，指针指向合同计划总额.以overContract变量存放。
	    var overContract = data.sumContract;
	    if(overContract>maxContract){
	    	overContract = maxContract;
	    }
	    //前部分比例
	    var fristPercent = data.fristPercent;
	    //前两部分比例
	    var secondPercent = data.secondPercent;
	    //刻度单位
	    var unit = data.unit;
	$('#contract').highcharts({
	        chart :{
	        type: 'gauge',
		        plotBackgroundColor: null,
		        plotBackgroundImage: null,
		        plotBorderWidth: 0,
		        plotShadow: false
	   },
	        title : {
	       text: false
	   },
	   credits:{
       	enabled:false
       },
	        pane : [{
	       startAngle: -90,
	       endAngle: 90,
	       background: null,
	       center: ['50%', '50%'],
	       size: '92%'
	   }],            
	   yAxis : [{
	        //      tickLength:0,
	       //         minorTickLength:0,
	       min: 0,
	       max: maxContract,
	       minorTickPosition: 'inside',
	       tickPosition: 'inside',
	       labels: {
	            //        enabled:false,
	        rotation: 'auto',
	        distance: 10
	       },
	       plotBands: [{//分区段
	        from: 0,
	        to: maxContract*data.fristPercent,
	        color: colors[0],
	        innerRadius: '100%',
	        outerRadius: '65%'
	       },{
	        from: maxContract*fristPercent,
	        to: maxContract*secondPercent,
	        color: colors[1],
	        innerRadius: '100%',
	        outerRadius: '65%'
	       },{
	        from: maxContract*secondPercent,
	        to: maxContract,
	        color: colors[2],
	        innerRadius: '100%',
	        outerRadius: '65%'
	       }],
	       title: {
	        text: unit,
	               style :{
	                   fontSize : '14px',
	                   marginTop : '10px'
	               },
	        y: 20
	       }
	   }],
	   tooltip: {
           valueSuffix: '(万元)',
           snap: 0,
           style:{
           	fontSize:'12px'
           },
           formatter: function(){
        	   var finish = (sumContract/10000).toFixed(2);
    		   var over =finish - (maxContract/10000).toFixed(2);
        	   if(sumContract>maxContract){
        		   return "合同已完成："+finish+"万元,超额完成:"+over.toFixed(2)+"万元";
        	   }
        	   else{
        		   return "合同已完成："+finish+"万元";
        	   }
        	  
           }
       },
	    exporting :{
	        enabled: false
	       },
	     plotOptions: {
	    gauge: {
	    dataLabels: {
	    enabled: true,
	     y : 10,
	        style: {
	                    fontSize: '16px'
	                    }
	    },
	    dial: {//仪表盘指针
	    radius: '80%',
	    rearLength: '0%',
	    backgroundColor: 'silver',
		borderColor: 'silver',
		borderWidth: 1,
		baseWidth: 10,
		topWidth: 1,
		baseLength: '30%'
	    }
	    }
	   },
	  series: [{
	    name : '合同完成度',
	       data: [overContract],
	       yAxis: 0
	   }]
	});
		/*var labe = $("#contract .highcharts-data-labels").innerHTM ="";
		var labe = $("#contract .highcharts-data-labels");*/
		//改变合同完成度刻度
	if(maxContract!=0){
		changeDegree();
	}
		/*document.getElementById('loading').innerHTML="" ;*/
		$("#contract .highcharts-container")
		$("#contract .highcharts-container").css("overflow","visible");
		$("#contract").height(210);
		$("#contract .highcharts-container").height(210);
		$("#contract .highcharts-container").children(0).height(210);
	}
	
	//改变合同完成度刻度
	function changeDegree(){
		var maxContract = gabolContractInfo.contractPlan;
	    //目前为止合同金额
	    var sumContract = gabolContractInfo.sumContract;
		//修改刻度值
		maxContract = (maxContract/10000).toFixed(2);//（以万元为单位）
		sumContract = (sumContract/10000).toFixed(2);//（以万元为单位）
		
		//根据控件生成刻度数对应刻度单位
		var textTagLength = $("#contract .highcharts-axis-labels").children().size();
		var part = (maxContract/(textTagLength-1));
		if(isNaN(part)){
			part = 0;
		}
		//兼容性判断
		try{
			var innerHtmlVar = $("#contract .highcharts-axis-labels").children(0)[1].innerHTML;
			for(var i=0;i<textTagLength;i++){
				if(typeof(innerHtmlVar)=="undefined"){
					$("#contract .highcharts-axis-labels").children(0)[i].textContent = i*part;
				}
				else{
					$("#contract .highcharts-axis-labels").children(0)[i].innerHTML = i*part;
				}
				
			}
		}catch(e){
			
		}
		/*if(sumContract>maxContract){
	    	maxContract = sumContract;
	    }*/
		
		
		/*if(isIE8){//IE8
		if($($("#contract .highcharts-data-labels").children(0).children(0)[1])!=undefined){
			$($("#contract .highcharts-data-labels").children(0).children(0)[1]).css("visibility","hidden");
		}
		if($("#contract .highcharts-data-labels").children(0).children(0)[0]!=undefined){
			$("#contract .highcharts-data-labels").children(0).children(0)[0].innerHTML = sumContract+"(万元)";
		}
	 }*/
		if(navigator.userAgent.indexOf("MSIE 8.0")>0)
		{
			if($($("#contract .highcharts-data-labels").children(0).children(0)[1])!=undefined){
				$($("#contract .highcharts-data-labels").children(0).children(0)[1]).css("visibility","hidden");
			}
			if($("#contract .highcharts-data-labels").children(0).children(0)[0]!=undefined){
				$("#contract .highcharts-data-labels").children(0).children(0)[0].innerHTML = sumContract+"(万元)";
			}
		}
		else{//非IE8
			//修改显示金额样式去边框
			if($($("#contract .highcharts-data-labels").children(0).children(0)[0])!=undefined){
				$($("#contract .highcharts-data-labels").children(0).children(0)[0]).removeAttr("stroke"); 
			}
			if($("#contract .highcharts-data-labels").children(0).children(0)[1]!=undefined){
				//兼容性
				if($("#contract .highcharts-data-labels").children(0).children(0)[1].innerHTML!=undefined){
					$("#contract .highcharts-data-labels").children(0).children(0)[1].innerHTML = sumContract+"(万元)";
				}
				else{
					$("#contract .highcharts-data-labels").children(0).children(0)[1].textContent = sumContract+"(万元)";
				}
				
			}
		}
	}
	
	/**
	 * 板块人日统计
	 */
	function loadPersonDay(data){
		$('#personDayMoudle').highcharts({
	        title: {
	            text: false,
	            x: -20 //center
	        },
	        credits:{
	           	enabled:false
	           },
	        xAxis: {
	            categories: ['1月', '2月', '3月', '4月', '5月', '6月','7月', '8月', '9月', '10月', '11月', '12月']
	        },
	        yAxis: {
	            title: {
	                text: false
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	        	animation:false,
	            valueSuffix: '人日',
	            snap: 0,
	            shared: true,
	            style:{
	            	fontSize:'12px'
	            }
	        },
	        legend: {
	            layout: 'horizontal',
	            align: 'center',
	            verticalAlign: 'top',
	            borderWidth: 0
	        },
	        series:data
	    });
	}
	
	/**
	 * 部门人日统计
	 */
	function loadDeptMonthPersonDay(data){
		
		var versionsData = [];
		var centerData = [];
		var sum = 0;
		//求所有部门人日总和
		for(var i = 0; i < data.length; i++){
			sum = sum + data[i].personDay;
		}
		//中心圆数据
		var cData = {};
		cData.name = sum;
		cData.y = 100;
		cData.color = '#CCCCCC';
		cData.labelDetail = '';
		cData.events =  {
	        	//控制图标的图例legend不允许切换
				legendItemClick: function (event) {                                   
					return false; //return  true 则表示允许切换
	        	}
	        }
		centerData.push(cData);
		for (var i = 0; i < data.length; i++) {
			var dpersonDay = data[i];
			var deptName = dpersonDay.deptName;
			var deptColor = '#'+dpersonDay.deptColor;
			var personDay = dpersonDay.personDay==undefined?0 :dpersonDay.personDay;
			var personDayRate = 0;
			if(sum!=0){
				var personDayRate = Math.round(((personDay/sum)*100));
			}
			var temp = {};
			
			temp.y = personDayRate;
			temp.color = deptColor;
			temp.name = deptName;
			temp.labelDetail = "　" + personDay + "人日,占: " + personDayRate + "%";
			temp.events =  {
		        	//控制图标的图例legend不允许切换
					legendItemClick: function (event) {                                   
						return false; //return  true 则表示允许切换
		        	}
		        }
			
			versionsData.push(temp);
		}
		$('#DeptMonthPersonDay').highcharts({
		    chart: {
		        type: 'pie'
		    },
		    title: {
		        text: false
		    },
		    credits:{
		       	enabled:false
		       },
		    legend:{
		    	align:'center',
		    	verticalAlign : "top",
		    	layout:'horizontal',
		    	symbolHeight:12,
		    	padding:0,
		    	itemStyle :  {
		    		"color": "#333333", "fontSize": "12px"
		    	},
		    	labelFormatter:function(){
		    		/*if(this.y==100){
		    			return "[ 人日总和  "+this.name+"人日 ]";
		    		}*/
		    		return this.name + this.labelDetail
		    	}
		    },
		    yAxis: {
		        title: {
		            text: false
		        }
		    },
		    plotOptions: {
		        pie: {
		            shadow: false,
		            center: ['53%', '45%'],
		            dataLabels: {
		                enabled: true
		            },
		            showInLegend: true
		        }
		    },
		    tooltip: {
		    	animation:false,
			    valueSuffix: '%'
		    },
		    series: [ 
		      //中心圆
		     /* {
	            name: '所占比例',
	            data: centerData,
	            size: '60%',
	            dataLabels: {
	            	formatter: function() {
	            		return "总人日："+this.point.name 
		            },
	                color: 'black',
	                distance: -60
	            }
	        },*/
	        {
		        name: '所占比例',
		        data: versionsData,
		        size: '80%',
		        innerSize: '60%',
		        dataLabels: {
		            formatter: function() {
		                // display only if larger than 1
		                return this.y > 1 ? '<b>'+ this.point.name +':</b> '+ this.y +'%'  : null;
		            }
		        }
		    }]
		});
	}
	
	