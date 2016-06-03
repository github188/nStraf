var mapControl;
var overviewControl;
var layerControl;
var navigationControl;
var magnifierControl;
var sliderBarControl;
var params = new Object();

//指定多少毫秒无动作将跳转    
var timeout = 2000;      
//记录当前时间    
var occurtime = new Date().getTime();
var id;
var deleteDivId ;
var mouse_x ;
var mouse_y ;
//地图可视范围发生变化的变量


var tmapScale ;
var tmapRect ;

//========================静态页面初始化========================
function onPageLoad(){
	// 把SuperMap.Committer.handler和params.contextPath都改成本机应用的相应路径
	SuperMap.Resource.ins = new SuperMap.Resource();
	SuperMap.Resource.ins.setLanguage("Chinese");
	var contextPath = getContextPath();
	SuperMap.Committer.handler = contextPath+ "commonhandler";
    
    params.contextPath = contextPath;
    params.mapHandler = contextPath + "maphandler";

    params.mapName = "grgbankingGis";
    params.mapServicesAddress = "10.1.90.12";
    params.mapServicesPort = 8600;
    params.imageFormat = "png";
    params.buffer=1;

	mapControl = new SuperMap.UI.MapControl($get("mapcontrol1Div"));
    mapControl.set_params(params);
	//地图尺寸重置
	mapControl.add_startZoom(onMapResize);
    //当地图的可视范围发生变化
    mapControl.add_viewBoundsChanged(viewBoundsChanged);
    mapControl.initialize();
};

function onPageLoaded(){
	setinitMenu();
	mapControl.add_init(initOverview);
	mapControl.add_init(initNavigationControl);
	mapControl.add_init(initLayerControl);
	mapControl.add_init(initSliderBarControl);
	mapControl.add_init(loadMarker);
	if(mapControl.get_mapName() == "grgbankingGis"){
		mapControl._mapScales = [1/1200000 ,1/300000, 1/75000 ,1/35000, 1/12000, 1/5000, 1/2500];
		mapControl._params.mapScales = [1/1200000 ,1/300000, 1/75000 ,1/35000, , 1/12000, 1/5000, 1/2500];
	}
    mapControl.add_init(function(){
   	document.body.onresize = rerangePage;
	    }
    );
};

function loadMarker(){
	var mapScale=mapControl.get_mapScale() ;
	//现在的比例尺大于图层最小可见比例尺
	if(mapScale>=getMinScale(page_res["bankLayerName"])){
		var doubleRect=getDoubleRect();
		queryRect(doubleRect);
	}
}

function initLayerControl(){
	var layerControlDiv = document.getElementById("layer");
	if(layerControlDiv){
        var tempMap = mapControl.getMap();
	    if(!layerControl){
			layerControl = new SuperMap.UI.LayerControl(layerControlDiv);
			layerControl.set_map(tempMap);
			layerControl.set_params(null);
			layerControl.initialize();
	    }
	    if(layerControl && !(layerControl.get_layers().length > 0)){
	        layerControl.dispose();
            layerControl = null;
			layerControl = new SuperMap.UI.LayerControl(layerControlDiv);
			layerControl.set_map(tempMap);
			layerControl.set_params(null);
			layerControl.initialize();
	    }
		layerControl.set_mapControl(mapControl);
    }
};

function initNavigationControl(){
    var nContainer = document.getElementById("navigation");
    if(nContainer){
        if(!navigationControl){
			navigationControl = new SuperMap.UI.NavigationControl(nContainer);
			navigationControl.set_mapControl(mapControl);
			navigationControl.set_params(null);
			navigationControl.initialize();
        }
    }
};

function initSliderBarControl(){
    var mContainer = document.getElementById("sliderBar");
    if(mContainer){
        if(!sliderBarControl){
			sliderBarControl = new SuperMap.UI.SliderBarControl(mContainer);
			sliderBarControl.set_mapControl(mapControl);
			sliderBarControl.set_params(null);
			sliderBarControl.initialize();
        }
    }
};

function initMagnifierControl(){
    var mContainer = document.getElementById("magnifier");
    if(mContainer){
        if(!magnifierControl){
			magnifierControl = new SuperMap.UI.MagnifierControl(mContainer);
			magnifierControl.set_mapControl(mapControl);
			magnifierControl.set_params(null);
			magnifierControl.initialize();
        }
    }
};

function setZoomIn(){
    var zoomInAction = new SuperMap.UI.ZoomInAction();
    mapControl.set_action(zoomInAction);
};

function setZoomOut(){
    var zoomOutAction = new SuperMap.UI.ZoomOutAction();
    mapControl.set_action(zoomOutAction);
};

function setPan(){
    var panAction = new SuperMap.UI.PanAction();
    mapControl.set_action(panAction);
};
  
function clearHighlight(){
    mapControl.clearHighlight();
};

function clearCustomLayer(){
    mapControl.customLayer.removeMarker("insertTag");
    mapControl.customLayer.clearLines();
    mapControl.customLayer.clearPolygons();
};

function viewEntire(){
    mapControl.viewEntire();
};

function switchOverview(){
     if(overviewControl.get_isHidden()){
        overviewControl.show();
     }else{
       overviewControl.hide();
     }
};

function setMeasureDistanceAction() {
    var measureDistanceAction = new SuperMap.UI.MeasureDistanceAction(onMeasureDistanceComplete, onError);
	mapControl.set_action(measureDistanceAction);
};

function setMeasureAreaAction() {
    var measureAreaAction = new SuperMap.UI.MeasureAreaAction(onMeasureAreaComplete, onError);
	mapControl.set_action(measureAreaAction);
};
function onMapComplete(resultSet){

	var recordSetCount;
	if(resultSet && resultSet.recordSets){
		recordSetCount = resultSet.recordSets.length;
	
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		strTableHTML = "";
		for(i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			strTR = "<tr>";
			if(recordSet.returnFieldCaptions){
			    for(j = 0; j < recordSet.returnFieldCaptions.length; j++) {
				    strTR += "<td class='title_2'>" + recordSet.returnFieldCaptions[j] + "</td>";
			    }
			}else{
			    for(j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
				    strTR += "<td class='title_2'>" + fieldValue + "</td>";
			    }
			}
			strTR += "</tr>";
			var recordCount;
			if(recordSet.records != null){
				recordCount = recordSet.records.length;
			}else{
				recordCount = 0;
			}
			strTableHTML += recordSet.layerName + " (" + recordCount + ")";
			strTableHTML += "<table cellspacing='1' cellpadding='3'>" + strTR;
			for(j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				strTR = "<tr>";
				for(k = 0; k < record.fieldValues.length; k++) {
					strTR += "<td>";
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
					    if(_GetBrowser() != "ie"){
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }else{
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }
					}
					var fieldValue = record.fieldValues[k];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = parseInt(fieldValue * 100 + "") / 100;
					}
					// strTR += record.fieldValues[k];
					strTR += fieldValue;
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
						strTR += "</u></font>";
					}
					strTR += "</td>";
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			strTableHTML += "</table><br>";
		}
		if(resultSet.totalCount > resultSet.currentCount) {
				var tempStr = "";
				tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
				var pageCount = 0;
				var pageNo = 0;
				if(resultSet.customResponse) {
					try {
						eval(resultSet.customResponse);
					} catch(e) {
						alert(e);
					}
				}
				if(mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
					pageCount = 100;
				} else {
					pageCount = 10;
				}
				var totalPage = parseInt((resultSet.totalCount-1) / pageCount + 1);
				
				if((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction")||(mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				} else {
					// queryBySQL
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						tempStr += "<br>";
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(0);'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				}
				strTableHTML += tempStr;
			}
		if(strTableHTML != ""){
			strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + ""; 
			var aWin;
			aWin = window.open("", page_res["queryResult"], "");
			aWin.document.write(strTableHTML);
			aWin.document.close();
			aWin.focus();
			if (mapControl.get_action()._type=="RectQueryAction"){
				mapControl._queryAction._mapRect = mapControl.get_action()._mapRect;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="CircleQueryAction"){
				mapControl._queryAction._firstMapCoord = mapControl.get_action()._firstMapCoord;
				mapControl._queryAction.radius = mapControl.get_action().radius;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="LineQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="PolygonQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			}
		}else{
			alert(page_res["queryResultNull"]);
		}
	}else{
		alert(page_res["queryResultNull"]);
	}
	
   // var rect = new SuperMap.Rect2D();
   // SuperMap.Decoder.fromJson(rect, json);
  //  mapControl.viewByBounds(rect);
};
//========================静态页面地图操作=======================
//========================地图操作========================
function getMapNames(){
	function ongetMapNamesComplete(jsonNames){
	    var mapNames = eval(jsonNames);
		var tname = "";
		for(var i = 0; i < mapNames.length; i++){
			if (mapNames[i] != null && mapNames[i] != ""){
				tname = mapNames[i];
				for (var j = i+1; j < mapNames.length; j++)
				{
					if (mapNames[j] == tname)
					{
						delete mapNames[j];
					}
				}
			}
		}
	    var mapNamesDiv = document.getElementById("mapNames");
	    if(mapNamesDiv){
    		mapNamesDiv.innerHTML = ""; 
	    	for(var i = 0; i < mapNames.length; i++){
				if (mapNames[i] != null && mapNames[i] != "")
				{
	    			mapNamesDiv.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='" + mapNames[i] + "' onclick='switchMap(\"" + mapNames[i] + "\")' href='#'>" + mapNames[i] + "</a><br>";
				}
	    	}
	    }
	}
	var ongetMapNamesCompleteDelegate = Function.createDelegate(this, ongetMapNamesComplete);
	SuperMap.Committer.commitAjax("", SuperMap.Committer.handler, "getmapnames", [], [], true, ongetMapNamesCompleteDelegate);
};
function switchMap(mapName){
    try{
        if(layerControl){
            layerControl.dispose();
            layerControl = null;
        }
        if(navigationControl){
            navigationControl.dispose();
            navigationControl = null;
        }
        if(sliderBarControl){
            sliderBarControl.dispose();
            sliderBarControl = null;
        }
        if(magnifierControl){
            magnifierControl.dispose();
            magnifierControl = null;
        }
        if(overviewControl){
            overviewControl.dispose();
            overviewControl = null;
        }
    }catch(e){
        alert(e);
    }
    mapControl.get_params().mapScale = null;
    if(mapName == "Changchun"){
		var mapScales = [1/80000, 1/50000, 1/20000, 1/8000, 1/5000, 1/2000, 1/800, 1/500, 1/200];
		mapControl.set_mapScales(mapScales);
		mapControl.get_params().mapScales = mapScales;
    }else{
		mapControl.set_mapScales(null);
		mapControl.get_params().mapScales = null;
    }
    mapControl.switchMap(mapName);
};

function getUnits(unitValue){
	var units = new SuperMap.Units();
	if(unitValue == units.meter) {
		return page_res["meter"];
	} else if(unitValue == units.kilometer){
		return page_res["kilometer"];
	} else if(unitValue == units.mile){
		return page_res["mile"];
	} else if(unitValue == units.kilometer){
		return page_res["kilometer"];
	} else if(unitValue == units.yard){
		return page_res["yard"];
	} else if(unitValue == units.degree){
		return page_res["degree"];
	} else if(unitValue == units.millimeter){
		return page_res["millimeter"];
	} else if(unitValue == units.centimeter){
		return page_res["centimeter"];
	} else if(unitValue == units.inch){
		return page_res["inch"];
	} else if(unitValue == units.decimeter){
		return page_res["decimeter"];
	} else if(unitValue == units.foot){
		return page_res["foot"];
	} 
	
	return page_res["meter"];
};

function onMeasureDistanceComplete(result){
	if(result && result.distance){
		var unitValue = mapControl.getMap().get_coordsSys().units;
	    var temp = result.distance + getUnits(unitValue);
	    alert(temp);
    }
};

function onMeasureAreaComplete(result){
	if(result && result.area){
		var unitValue = mapControl.getMap().get_coordsSys().units;
	    var temp = result.area + page_res["square"] + getUnits(unitValue);
	    alert(temp);
    }
};

function onError(responseText) {
	alert(responseText);
};

/**
 * 保存地图
 */
function saveMap(){
 	var mapName = mapControl.get_mapName();
 	var size = mapControl.getSize();
 	var imageFormat = "png";
 	var mapScale = mapControl.get_mapScale();
	var url = mapControl.get_params().mapHandler;
	var mapCenterX = mapControl.getMapCenter().x;
	var mapCenterY = mapControl.getMapCenter().y;
	
	url += "?";
	url += "method=getimage" ;
	url += "&mapName=" + mapName;
	url += "&width=" + size.width();
	url += "&height=" + size.height();
	url += "&mapCenterX=" + mapCenterX;
	url += "&mapCenterY=" + mapCenterY;
	url += "&imageFormat=" + imageFormat;
	url += "&mapScale=" + mapScale;
	url += "&layersKey=" + mapControl.getMap()._params.layersKey;
	url += "&t=" + new Date().getTime();
	var aWin;
	aWin = window.open("", page_res["saveMap"], "status=no,toolbar=no,menubar=no");
	aWin.document.write("<IMG src='" + url + "'/>");
	aWin.document.close();
	aWin.focus();
};

/**
 * 打印地图
 */
function printMap(){
	var mapName = mapControl.get_mapName();
 	var size = mapControl.getSize();
 	var imageFormat = "png";
 	var mapScale = mapControl.get_mapScale();
	var url = mapControl.get_params().mapHandler;
	var mapCenterX = mapControl.getMapCenter().x;
	var mapCenterY = mapControl.getMapCenter().y;
	
	url += "?";
	url += "method=getimage" ;
	url += "&mapName=" + mapName;
	url += "&width=" + size.width();
	url += "&height=" + size.height();
	url += "&mapCenterX=" + mapCenterX;
	url += "&mapCenterY=" + mapCenterY;
	url += "&imageFormat=" + imageFormat;
	url += "&mapScale=" + mapScale;
	url += "&layersKey=" + mapControl.getMap()._params.layersKey;
	url += "&t=" + new Date().getTime();
	var aWin;
	aWin = window.open("", page_res["printMap"], "status=no,toolbar=no,menubar=no");
	aWin.document.write("<IMG src='" + url + "'/>");
	aWin.document.close();
	aWin.print();
};

/**
 * 鼠标移动事件
 */
function onMouseMoving(e){
	var pixelX = SuperMap.Utility.getMouseX(e);
	var pixelY = SuperMap.Utility.getMouseY(e);
	var offsetTop = mapControl.get_container().offsetTop;
	var offsetLeft = mapControl.get_container().offsetLeft;
	var pixelCoord = new SuperMap.Point(pixelX - offsetLeft, pixelY - offsetTop); 
    var mapCoord = mapControl.getMap().pixelToMapCoord(pixelCoord, mapControl.getMap().get_mapParam().mapScale);
    
	var positionInputX = document.getElementById('mapPositionX');
	var positionInputY = document.getElementById('mapPositionY');
	if(positionInputX){
		positionInputX.value = mapCoord.x;
	}
	if(positionInputY){
		positionInputY.value = mapCoord.y;
	}
	var mapScaleInput = document.getElementById('mapScale');
	if(mapScaleInput){
		mapScaleInput.value = Math.floor(1.0 / mapControl.get_mapScale());
	}
};

function rePosition(){
	var centerX = document.getElementById('mapPositionX').value;
	var centerY = document.getElementById('mapPositionY').value;
	var mapScale = document.getElementById('mapScale').value;
	var center = new SuperMap.Point2D(eval(centerX), eval(centerY));
	
	if(!mapScale){
		alert(page_res["scaleCannotNull"]);
	}
	if((mapScale > 0) && !isNaN(mapScale)){
		var newScale = 1.0 / eval(mapScale);
		mapControl.setCenterAndZoom(center, eval(newScale));
	}
};
//========================地图操作========================
//========================空间查询========================
var queryLayerNames = new Array();
function setPointQuery(){
    var queryAction = getQueryAction("PointQueryAction");
	if (queryAction){
		mapControl.set_action(queryAction);
		queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
	}
};

function setCircleQuery(){
    var queryAction = getQueryAction("CircleQueryAction");
	mapControl.set_action(queryAction);
	queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
};

function setLineQuery(){
    var queryAction = getQueryAction("LineQueryAction");
	mapControl.set_action(queryAction);
	queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
};

function setRectQuery(){
    var queryAction = getQueryAction("RectQueryAction");
	mapControl.set_action(queryAction);
	queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
};

function setPolygonQuery(){
    var queryAction = getQueryAction("PolygonQueryAction");
	mapControl.set_action(queryAction);
	queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
};

function getQueryAction(queryType){
	var queryAction = null;

	queryLayerNames = getQueryLayerNames();

	var sqlParam = new SuperMap.SqlParam();
	var selectLayer= new Array();
	var layerName=document.getElementById("findTotalQuery").value.split("::")[0] ;
	selectLayer[0]=layerName ;
	var orgfloor=document.getElementById("orgfloor").value ;
	sqlParam.whereClause = "C_ORGFLOOR like '%"+orgfloor+"%'";
	
    switch(queryType){
        case "PointQueryAction":
        	var toleranceM = document.getElementById("pointQueryTolerance").value;
        	//将米的单位换算成经纬制,1秒约为33M
        	var tolerance=parseFloat(toleranceM/(33*3600));
			var scale = mapControl.get_mapScale();
			var patrn=/^[+]?\d+(\.\d+)?$/; 
			if (!patrn.exec(tolerance))
			{
				alert("容限必须是数值");
				return false 
			}
			//查询前，先清除高亮等
			try{mapControl.clearHighlight();}catch(e){alert(e.Message);}
			clearCustomLayer();
			
			
	    	queryAction = new SuperMap.UI.PointQueryAction(selectLayer, sqlParam, tolerance, true, onSelectTotalQueryComplete2, onError);
               break;
        case "CircleQueryAction":
		    queryAction = new SuperMap.UI.CircleQueryAction(selectLayer, sqlParam, true, onSelectTotalQueryComplete2, onError);
               break;
        case "LineQueryAction":
		    queryAction = new SuperMap.UI.LineQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
        case "RectQueryAction":
           
	    	queryAction = new SuperMap.UI.RectQueryAction(selectLayer, sqlParam, true, onSelectTotalQueryComplete2, onError);
               break;
        case "PolygonQueryAction":
		    queryAction = new SuperMap.UI.PolygonQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
	}

	return queryAction;
};



/**
	modify by lyang 2009-11-20
	点选，框选，圈选查询验证后的回调方法,增加了到业务数据库表里统计这一功能
*/
function onSelectTotalQueryComplete(resultSet)
{
 var recordSetCount;
	//var point2D = new Object();
	//判断地图比例尺，如果为网点不可见，则设置比例尺
	var scale = mapControl.get_mapScale();
	var layerName=document.getElementById("findTotalQuery").value.split("::")[0] ;
	if (scale < getMinScale(layerName)) {
		mapControl.set_mapScale(getMinScale(layerName));
	}
	
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		//网点编号字符串
		var bankIds="" ;
		strTableHTML = "";
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			strTR = "<tr>";
			if (recordSet.returnFieldCaptions) {
				for (var j = 0; j < recordSet.returnFieldCaptions.length; j++) {
					//var fieldValue = recordSet.returnFields[j];
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'>" + recordSet.returnFieldCaptions[j] + "</td>";
					}
				}
			} else {
				for (var j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if (isNumber(fieldValue)) {
						fieldValue = parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'>" + fieldValue + "</td>";
					}
				}
			}
			strTR += "</tr>";
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			strTableHTML += "<table width='100%'><tr><td align='left'><b>查询结果: (" + recordCount + ")<b></td>";
			strTableHTML +="<td align='right'><img src='../../images/supGis/del.gif' onclick='divClose3()'/></td></tr></table>" ;
			strTableHTML += "<table cellspacing='1' cellpadding='3'>" + strTR;
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				//平移到查询出来的最后一个网点位置
				if(i==recordCount-1){
					pointMove(record.center.x,record.center.y);
				}
				strTR = "<tr>";
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"||nameFiledValue[k]=="C_BANK_NAME"){
						strTR += "<td>";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							if (_GetBrowser() != "ie") {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							} else {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							}
							//把c_bank_id组合成字符串
							bankIds += ","+record.fieldValues[k];
						}
						var fieldValue = record.fieldValues[k];
						if (isNumber(fieldValue)) {
							fieldValue = parseFloat(fieldValue);
							fieldValue = parseInt(fieldValue * 100 + "") / 100;
						}
						strTR += fieldValue;
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							strTR += "</u></font>";
						}
						strTR += "</td>";
					}
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			bankIds=bankIds.substring(1);
			
			strTableHTML +="<tr><td colspan='2' align='center'><input type='button' value='统计数据' size='3' onClick='statOperationData(\""+bankIds+"\")'>&nbsp;"
			strTableHTML += "<input type='button' value='关闭' onclick='divClose3()'></td></tr></table><br>";
		}
		if (resultSet.totalCount > resultSet.currentCount) {
			var tempStr = "";
			tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
			var pageCount = 0;
			var pageNo = 0;
			if (resultSet.customResponse) {
				try {
					eval(resultSet.customResponse);
				}
				catch (e) {
					alert(e);
				}
			}
			if (mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
				pageCount = 100;
			} else {
				pageCount = 10;
			}
			var totalPage = parseInt((resultSet.totalCount - 1) / pageCount + 1);
			if ((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction") || (mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			} else {
					// queryBySQL
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					tempStr += "<br>";
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(0);'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			}
			strTableHTML += tempStr;
		}
		if (strTableHTML != "") {
			strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML ;
			//var aWin;
			//aWin = window.open("", page_res["queryResult"], "");
			//aWin.document.write(strTableHTML);
			//aWin.document.close();
			//aWin.focus();
			var myDiv = document.createElement("div");//创建div
			myDiv.id="newDivId3" ;
			myDiv.innerHTML ="<div  class='superMapDiv'>"+strTableHTML+"</div>" ;
			document.body.appendChild(myDiv);
			
			if (mapControl.get_action()._type == "RectQueryAction") {
				mapControl._queryAction._mapRect = mapControl.get_action()._mapRect;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else {
				if (mapControl.get_action()._type == "CircleQueryAction") {
					mapControl._queryAction._firstMapCoord = mapControl.get_action()._firstMapCoord;
					mapControl._queryAction.radius = mapControl.get_action().radius;
					mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
				} else {
					if (mapControl.get_action()._type == "LineQueryAction") {
						mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
						mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
						mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
					} else {
						if (mapControl.get_action()._type == "PolygonQueryAction") {
							mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
							mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
							mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
						}
					}
				}
			}
		} else {
			alert(page_res["queryResultNull"]);
		}
	} else {
		alert(page_res["queryResultNull"]);
	}
};

function isInteger(s) {
  return /^-?\d+$/.test(s);
};

function isNumber(s) {
	return /^-?\d+$/.test(s) || /^-?\d+.\d+$/.test(s) || /^-?\d+.\d+E-?\d+$/.test(s);
};
/*
最近网点查找
*/
function onQueryNearComplete(resultSet)
{
var recordSetCount;
	if(resultSet && resultSet.recordSets){
		recordSetCount = resultSet.recordSets.length;
	
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		strTableHTML = "";
		for(i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			strTR = "<tr>";
			if(recordSet.returnFieldCaptions){
			    for(j = 0; j < recordSet.returnFieldCaptions.length; j++) {
			    if(recordSet.returnFields[j]=="C_BANK_ID"||recordSet.returnFields[j]=="C_BANK_NAME"||recordSet.returnFields[j]=="C_BANK_ADDRESS")
				    strTR += "<td class='title_2'>" + recordSet.returnFieldCaptions[j] + "</td>";
			    }
			}else{
			    for(j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
				    strTR += "<td class='title_2'>" + fieldValue + "</td>";
			    }
			}
			strTR += "</tr>";

			var recordCount;
			if(recordSet.records != null){
				recordCount = recordSet.records.length;
			}else{
				recordCount = 0;
			}
			strTableHTML +=  " 网点数量：" + recordCount + "";
			strTableHTML += "<table cellspacing='1' cellpadding='3'>" + strTR;
			
			for(j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				strTR = "<tr>";
				for(k = 0; k < record.fieldValues.length; k++) {
				
				
				
				 if(recordSet.returnFields[k]=="C_BANK_ID"||recordSet.returnFields[k]=="C_BANK_NAME"||recordSet.returnFields[k]=="C_BANK_ADDRESS")
				 {
				  strTR += "<td>";
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
					    if(_GetBrowser() != "ie"){
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }else{
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }
					}
					var fieldValue = record.fieldValues[k];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = parseInt(fieldValue * 100 + "") / 100;
					}
					// strTR += record.fieldValues[k];
					strTR += fieldValue;
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
						strTR += "</u></font>";
					}
					strTR += "</td>";
				 }
					
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			strTableHTML += "</table><br>";
		}
		if(resultSet.totalCount > resultSet.currentCount) {
				var tempStr = "";
				tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
				var pageCount = 0;
				var pageNo = 0;
				if(resultSet.customResponse) {
					try {
						eval(resultSet.customResponse);
					} catch(e) {
						alert(e);
					}
				}
				if(mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
					pageCount = 100;
				} else {
					pageCount = 10;
				}
				var totalPage = parseInt((resultSet.totalCount-1) / pageCount + 1);
				
				if((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction")||(mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				} else {
					// queryBySQL
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						tempStr += "<br>";
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(0);'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				}
				strTableHTML += tempStr;
			}
		if(strTableHTML != ""){
			strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='../../css/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + ""; 
			
	
	
			var aWin;
			aWin = window.open("", page_res["queryResult"], "height=600,width=800");
			aWin.document.write(strTableHTML);
			aWin.document.close();
			aWin.focus();

			if (mapControl.get_action()._type=="RectQueryAction"){
				mapControl._queryAction._mapRect = mapControl.get_action()._mapRect;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="CircleQueryAction"){
				mapControl._queryAction._firstMapCoord = mapControl.get_action()._firstMapCoord;
				mapControl._queryAction.radius = mapControl.get_action().radius;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="LineQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="PolygonQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			}
		}else{
			alert(page_res["queryResultNull"]);
		}
	}else{
		alert(page_res["queryResultNull"]);
	}
};



function onQueryComplete(resultSet){
	var recordSetCount;
	if(resultSet!=null && resultSet.recordSets){
		recordSetCount = resultSet.recordSets.length;
	
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		strTableHTML = "";
		for(i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			strTR = "<tr>";
			if(recordSet.returnFieldCaptions){
			    for(j = 0; j < recordSet.returnFieldCaptions.length; j++) {
				    strTR += "<td class='title_2'>" + recordSet.returnFieldCaptions[j] + "</td>";
			    }
			}else{
			    for(j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
				    strTR += "<td class='title_2'>" + fieldValue + "</td>";
			    }
			}
			strTR += "</tr>";
			var recordCount;
			if(recordSet.records != null){
				recordCount = recordSet.records.length;
			}else{
				recordCount = 0;
			}
			strTableHTML += recordSet.layerName + " (" + recordCount + ")";
			strTableHTML += "<table cellspacing='1' cellpadding='3'>" + strTR;
			for(j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				strTR = "<tr>";
				for(k = 0; k < record.fieldValues.length; k++) {
					strTR += "<td>";
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
					    if(_GetBrowser() != "ie"){
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }else{
    						strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
					    }
					}
					var fieldValue = record.fieldValues[k];
					if(isNumber(fieldValue)) {
						fieldValue =  parseFloat(fieldValue);
						fieldValue = parseInt(fieldValue * 100 + "") / 100;
					}
					// strTR += record.fieldValues[k];
					strTR += fieldValue;
					if(recordSet.returnFields[k].toLowerCase() == "smid" && record.center != null){
						strTR += "</u></font>";
					}
					strTR += "</td>";
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			strTableHTML += "</table><br>";
		}
		if(resultSet.totalCount > resultSet.currentCount) {
				var tempStr = "";
				tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
				var pageCount = 0;
				var pageNo = 0;
				if(resultSet.customResponse) {
					try {
						eval(resultSet.customResponse);
					} catch(e) {
						alert(e);
					}
				}
				if(mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
					pageCount = 100;
				} else {
					pageCount = 10;
				}
				var totalPage = parseInt((resultSet.totalCount-1) / pageCount + 1);
				
				if((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction")||(mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				} else {
					// queryBySQL
					if(totalPage > 0) {
						tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
						tempStr += "<br>";
						if(pageNo > 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(0);'><u>首页</u></font>";
						}
						if(pageNo != 0) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo - 1) + ");'><u>上一页</u></font>";
						}
						if(pageNo < totalPage - 2) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (pageNo + 1) + ");'><u>下一页</u></font>";
						}
						if(pageNo < totalPage - 1) {
							tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.sqlQuery(" + (totalPage - 1) + ");'><u>末页</u></font>";
						}
					}
				}
				strTableHTML += tempStr;
			}
		if(strTableHTML != ""){
			strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + ""; 
			var aWin;
			aWin = window.open("", page_res["queryResult"], "");
			aWin.document.write(strTableHTML);
			aWin.document.close();
			aWin.focus();
			if (mapControl.get_action()._type=="RectQueryAction"){
				mapControl._queryAction._mapRect = mapControl.get_action()._mapRect;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="CircleQueryAction"){
				mapControl._queryAction._firstMapCoord = mapControl.get_action()._firstMapCoord;
				mapControl._queryAction.radius = mapControl.get_action().radius;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="LineQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			} else if (mapControl.get_action()._type=="PolygonQueryAction"){
				mapControl._queryAction._keyPoints = mapControl.get_action()._keyPoints;
				mapControl._queryAction._onComplete = mapControl.get_action()._onComplete;
				mapControl._queryAction.queryParam = mapControl.get_action().queryParam;
			}
		}else{
			alert(page_res["queryResultNull"]);
		}
	}else{
		alert(page_res["queryResultNull"]);
	}
};

function insertTag(point2D) {
	/// 增加中心点显示
		var id = "insertTag" ;
		var icon = new Object();
		icon.src = "../../images/supGis/marker_blue.gif";
		icon.width = 16;
		icon.height = 16;
        mapControl.customLayer.insertMarker(id, point2D.x, point2D.y, 16, 16, null, null, 70, icon);	
}
//========================空间查询========================
//========================缓冲查询========================
function setPointBufferQuery(){
	var bufferAction = getBufferAction("PointBufferQueryAction");
	mapControl.set_action(bufferAction);
};

function setRectBufferQuery(){
	var bufferAction = getBufferAction("RectBufferQueryAction");
	mapControl.set_action(bufferAction);
};

function setLineBufferQuery(){
	var bufferAction = getBufferAction("LineBufferQueryAction");
	mapControl.set_action(bufferAction);
};

function setPolygonBufferQuery(){
	var bufferAction = getBufferAction("PolygonBufferQueryAction");
	mapControl.set_action(bufferAction);
};

function getBufferAction(bufferType){
	var bufferAction = null;
	
	queryLayerNames = getQueryLayerNames();

	var bufferAnalystParam = new SuperMap.BufferAnalystParam();
	bufferAnalystParam.leftDistance = document.getElementById("leftDistance").value;
	// bufferAnalystParam.rightDistance 
	bufferAnalystParam.semicircleLineSegment = document.getElementById("semicircleLineSegment").value;
	var queryMode = document.getElementById("spatialQueryMode").value;

    switch(bufferType){
        case "PointBufferQueryAction":
			document.getElementById("bufferEndType").disabled = "disabled";
			bufferAnalystParam.bufferEndType = 0;
		    bufferAction = new SuperMap.UI.PointBufferQueryAction(queryLayerNames, null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
               break;
		case "MultiPointBufferQueryAction":
			document.getElementById("bufferEndType").disabled = "disabled";
			bufferAnalystParam.bufferEndType = 0;
		    bufferAction = new SuperMap.UI.MultiPointBufferQueryAction(queryLayerNames, null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
               break;
        case "RectBufferQueryAction":
			document.getElementById("bufferEndType").disabled = "disabled";
			bufferAnalystParam.bufferEndType = 0;
		    bufferAction = new SuperMap.UI.RectBufferQueryAction(queryLayerNames, null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
              break;
        case "LineBufferQueryAction":
          	var bufferEndTypeSelect = document.getElementById("bufferEndType");
			bufferEndTypeSelect.disabled = "";
			bufferAnalystParam.bufferEndType = bufferEndTypeSelect.value;
		    bufferAction = new SuperMap.UI.LineBufferQueryAction(queryLayerNames, null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
              break;
        case "PolygonBufferQueryAction":
			document.getElementById("bufferEndType").disabled = "disabled";
			bufferAnalystParam.bufferEndType = 0;
		    bufferAction = new SuperMap.UI.PolygonBufferQueryAction(queryLayerNames, null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
              break;
    }

	return bufferAction;
};

function setPointEntityBufferQuery(){
	var entityBufferAction = getEntityBufferAction("PointEntityBufferQueryAction");
	mapControl.set_action(entityBufferAction);
};

function setRectEntityBufferQuery(){
	var entityBufferAction = getEntityBufferAction("RectEntityBufferQueryAction");
	mapControl.set_action(entityBufferAction);
};

function getEntityBufferAction(entityBufferType){
	var entityBufferAction = null;

	var fromLayer = document.getElementById("entityLayersSelect").value;
	var layerName = document.getElementById("targetLayersSelect").value;
	var bufferAnalystParam = new SuperMap.BufferAnalystParam();
	bufferAnalystParam.leftDistance = document.getElementById("leftDistance").value;
	// bufferAnalystParam.rightDistance 
	bufferAnalystParam.semicircleLineSegment = document.getElementById("semicircleLineSegment").value;
	document.getElementById("bufferEndType").disabled = "disabled";
	bufferAnalystParam.bufferEndType = 0;
	var queryMode = document.getElementById("spatialQueryMode").value;
	
    switch(entityBufferType){
        case "PointEntityBufferQueryAction":
			entityBufferAction = new SuperMap.UI.PointEntityBufferQueryAction(fromLayer, [layerName], null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
               break;
        case "RectEntityBufferQueryAction":
			entityBufferAction = new SuperMap.UI.RectEntityBufferQueryAction(fromLayer, [layerName], null, bufferAnalystParam, queryMode, true, onQueryComplete, onError);
               break;
	}

	return entityBufferAction;
};
//========================缓冲查询========================
//========================获取网络分析设置========================
function getNetworkSetting(){
	var networkSetting = new SuperMap.NetworkModelSetting();
	networkSetting.weightFieldInfos = new Array();
	var weightFieldInfo = new SuperMap.WeightFieldInfo();
	weightFieldInfo.name = "length";
	weightFieldInfo.fTWeightField = document.getElementById("txtNetworkSettingFTWeight").value;
	weightFieldInfo.tFWeightField = document.getElementById("txtNetworkSettingTFWeight").value;
	networkSetting.weightFieldInfos.push(weightFieldInfo);
	networkSetting.nodeIDField = document.getElementById("txtNetworkSettingNodeID").value;
	networkSetting.edgeIDField = document.getElementById("txtNetworkSettingEdgeID").value;
	networkSetting.tolerance = document.getElementById("txtNetworkSettingTolerance").value;
	var dtAndDs = document.getElementById("selNetworkSettingLayerName").value;
	var strArr = dtAndDs.split('@');
	if(strArr.length == 2){
		networkSetting.networkDatasetName = strArr[0];
		networkSetting.networkDataSourceName = strArr[1];
	}
	return networkSetting;
};

function getNetworkAnalystParam(){
	var param = new SuperMap.NetworkAnalystParam();
	param.isNodesReturn = document.getElementById("ckbIsNodeReturn").checked;
	param.isEdgesReturn = document.getElementById("ckbIsEdgeReturn").checked;
	param.isPathGuidesReturn = false;
	param.isPathsReturn = false;
	param.isStopsReturn = false;
	return param;
};

function getNeedHighlight(){
	return document.getElementById("ckbHighLightAnalystResult").checked;
};
function createOneRow(leftText,rightText){
	var strTR = "";
	strTR = "<tr>";
	strTR += "<td class='title_2' align='right' width='60px'>" + leftText + "</td>";
	strTR += "<td class='title_2' align='left'>" + rightText + "</td>";
	strTR += "</tr>";
	return strTR;
};
function showNetworkAnalystResult(result,msg){
	//呈现网络分析结果
	if(result == null){
		return;
	}
	
	var strTableHTML = "";
	var strTR = "";
	if(result.nodes){
		var left = page_res["networkInfo"];
		var right = "";
		for(var i=0;i<result.nodes.length;i++){
			if(i>0){
				right += "<br>";
			}
			var aPath = "";
			for(var j=0;j<result.nodes[i].length;j++){
				if(j>0){
					aPath += ",";
				}
				aPath += result.nodes[i][j];
			}
			right += aPath;
		}
		strTR += createOneRow(left,right);
	}
	
	if(result.edges){
		var left = page_res["arcInfo"];
		var right = "";
		for(var i=0;i<result.edges.length;i++){
			if(i>0){
				right += "<br>";
			}
			var aEdge = "";
			for(var j=0;j<result.edges[i].length;j++){
				if(j>0){
					aEdge += ",";
				}
				aEdge += result.edges[i][j];
			}
			right += aEdge;
		}
		strTR += createOneRow(left,right);
	}
	
	if(strTR == ""){
		return;
	}
	strTableHTML = "";
	strTableHTML += page_res["networkAnalystResult"];
	strTableHTML += "<table cellspacing='1' cellpadding='3' border='1'>" + strTR;
	strTableHTML += "</table><br>";
	strTableHTML += msg;
	if(strTableHTML != ""){
		 
		strTableHTML = "<html><head><title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'></head><body>" + strTableHTML + "</body></html>"; 
		var aWin;
		aWin = window.open("", page_res["networkAnalystResult"], "");
		if(aWin){
			aWin.document.write(strTableHTML);
			aWin.document.close();
			aWin.focus();
		}
	}
};
//========================获取网络分析设置========================
//========================路径分析========================
function setFindPath(){
	var sel = document.getElementById("selNetworkSettingLayerName");
	if( !sel.options || sel.options.length == 0){
		alert(page_res["noNetworkLayer"]);
		sel.focus();
		return;
	}
	var networkSetting = getNetworkSetting();
	var networkAnalystParam = getNetworkAnalystParam();
	var needHighlight = getNeedHighlight();
	var pathParam = new SuperMap.PathParam();
	pathParam.networkAnalystParam = networkAnalystParam;
	pathParam.hasLeastEdgeCount = document.getElementById("ckbHasLeastEdgeCount").checked;
	var findPathAction = new SuperMap.UI.FindPathAction(networkSetting, pathParam,needHighlight,onFindPathComplete, onError);
	mapControl.set_action(findPathAction);
};

function setFindTSPPath(){
	var sel = document.getElementById("selNetworkSettingLayerName");
	if( !sel.options || sel.options.length == 0){
		alert(page_res["noNetworkLayer"]);
		sel.focus();
		return;
	}
	var networkSetting = getNetworkSetting();
	var networkAnalystParam = getNetworkAnalystParam();
	var needHighlight = getNeedHighlight();
	var tspPathParam = new SuperMap.TSPPathParam();
	tspPathParam.networkAnalystParam = networkAnalystParam;
	tspPathParam.isEndNodeAssigned = document.getElementById("ckbIsEndNodeAssigned").checked;
	var findTSPPathAction = new SuperMap.UI.FindTSPPathAction(networkSetting,tspPathParam, needHighlight,onFindTSPPathComplete, onError);
	mapControl.set_action(findTSPPathAction);
};


function onFindPathComplete(pathResult,userContext){
	if(pathResult && pathResult.weights && pathResult.weights.length>0){
		var unitValue = mapControl.getMap().get_coordsSys().units;
	    var temp = getUnits(unitValue);
		if(pathResult.nodes || pathResult.edges){
			showNetworkAnalystResult(pathResult, page_res["roadLength"]+pathResult.weights[0] + temp);
		}else{
			alert(page_res["roadLength"]+pathResult.weights[0] + temp);
		}
	}else{
		alert(page_res["findPathFail"]);
	}
};

function onFindTSPPathComplete(pathResult,userContext){
	if(pathResult && pathResult.weights && pathResult.weights.length>0){
		var unitValue = mapControl.getMap().get_coordsSys().units;
	    var temp = getUnits(unitValue);
		if(pathResult.nodes || pathResult.edges){
			showNetworkAnalystResult(pathResult,page_res["roadLength"]+pathResult.weights[0] + temp);
		}else{
			alert(page_res["roadLength"]+pathResult.weights[0] + temp);
		}
	}else{
		alert(page_res["findTSPPathFail"]);
	}
};

//========================路径分析========================
//========================最近地物查找=======================
function setFindNearest(){
	var layerName = document.getElementById("findNearestLayersSelect").value;
	var sqlParam = new SuperMap.SqlParam();
   	var maxDistance = document.getElementById("maxDistance").value;
	//var highlightFindNearest = document.getElementById("highlightFindNearest").checked;
	var highlightFindNearest=true;//高亮显示
    var findNearestAction = new SuperMap.UI.FindNearestAction([layerName], sqlParam, maxDistance, highlightFindNearest, onQueryNearComplete, onError);
	mapControl.set_action(findNearestAction);
	
};


//查找半径，目标图层，是否高亮变化时触发这个事件
function onMaxDistanceChange() {
	var currentAction = mapControl.get_action();
	if(currentAction.type == "FindNearestAction") {
		var layerName = document.getElementById("findNearestLayersSelect").value;
		var sqlParam = new SuperMap.SqlParam();
		var maxDistance = document.getElementById("maxDistance").value;
		var highlightFindNearest = document.getElementById("highlightFindNearest").checked;
		var findNearestAction = new SuperMap.UI.FindNearestAction([layerName], sqlParam, maxDistance, highlightFindNearest, onQueryComplete, onError);
		mapControl.set_action(findNearestAction);
	}
}

//========================最近地物查找=======================
//========================最近设施分析=======================
function setClosestFacility(){
	var sel = document.getElementById("selNetworkSettingLayerName");
	if( !sel.options || sel.options.length == 0){
		alert(page_res["noNetworkLayer"]);
		sel.focus();
		return;
	}
	var networkSetting = getNetworkSetting();
	var networkAnalystParam = getNetworkAnalystParam();
	var needHighlight = getNeedHighlight();
	var proximityParam = new SuperMap.ProximityParam();
	proximityParam.networkAnalystParam = networkAnalystParam;
	proximityParam.isFromEvent = document.getElementById("rdEventToFaclities").checked;
	proximityParam.maxImpedance = parseFloat(document.getElementById("txtClosestFacilityMaxImpedance").value);
	proximityParam.facilityCount = parseInt(document.getElementById("txtClosestFacilityCount").value);
	var closestFacilityAnalystAction= new SuperMap.UI.ClosestFacilityAnalystAction(networkSetting,proximityParam, needHighlight,document.getElementById("rdSelectEventPoint").checked,onClosestFacilityComplete, onError);
	mapControl.set_action(closestFacilityAnalystAction);
};

function onClosestFacilityComplete(pathResult,userContext){
	if(pathResult){
		showNetworkAnalystResult(pathResult,"");
	}else{
		alert(page_res["closestFacilityFail"]);
	}
};

function startClosestFacility(){
	var action = 	mapControl.get_action();
	if(action.type == "ClosestFacilityAnalystAction" && action.isReady()){
		action.startAnalyst(selectEventPoint);
	}else{
		alert(page_res["choosePoint"]);
	}
};

function changeClosestFacilityAnalystActionState(selectEventPoint){
	var action = 	mapControl.get_action();
	if(action.type == "ClosestFacilityAnalystAction"){
		action.setIsSelectingEventPoint(selectEventPoint);
	}
};
//========================最近设施分析=======================
//=========================服务区分析========================
function setServiceArea(){
	var sel = document.getElementById("selNetworkSettingLayerName");
	if( !sel.options || sel.options.length == 0){
		alert(page_res["noNetworkLayer"]);
		sel.focus();
		return;
	}
	var networkSetting = getNetworkSetting();
	var networkAnalystParam = getNetworkAnalystParam();
	var needHighlight = getNeedHighlight();
	var serviceAreaParam = new SuperMap.ServiceAreaParam();
	serviceAreaParam.networkAnalystParam = networkAnalystParam;
	serviceAreaParam.isFromCenter = document.getElementById("ckbFromCenter").checked;
	if(document.getElementById("ckbCenterExclusive")) {
		serviceAreaParam.isCenterMutuallyExclusive = document.getElementById("ckbCenterExclusive").checked;
	}
	var action = new SuperMap.UI.ServiceAreaAction(networkSetting,serviceAreaParam, needHighlight,onServiceCenterAdded,onServiceCenterRemoved,onServiceAreaComplete, onError, onServiceAreaAnalystStart);
	mapControl.set_action(action);
};

function onServiceAreaComplete(pathResult,userContext){
	if(pathResult){
		showNetworkAnalystResult(pathResult,"");
	}else{
		alert(page_res["serviceAnalystFail"]);
	}
};

function onServiceCenterAdded(index,point,weight){
	var table = document.getElementById("tblServiceWeights");
	if(table){
		var row = table.insertRow(-1);
		var cellIndex = row.insertCell(-1);
		var cellWeight = row.insertCell(-1);
		cellIndex.align = "center";
		cellIndex.innerHTML = index +1;
		cellWeight.align = "center";
		var txtWeight = document.createElement("input");
		txtWeight.id = "txtWeights_"+index;
		txtWeight.type = "TEXT";
		txtWeight.style.width = "30px";
		txtWeight.value = weight;
		txtWeight.onchange = onWeightChanged;
		cellWeight.appendChild(txtWeight);
	}
	table.style.display = "block";
};

function onServiceAreaAnalystStart() {
	// 服务区分析之前处理事件
	var action = mapControl.get_action();
	if(action.type != "ServiceAreaAction"){
		return;
	}
	var table = document.getElementById("tblServiceWeights");
	if(table){
		// 假定分析的长度不超过999
		for(var i = 0; i < 999; i++) {
			var weightText = document.getElementById("txtWeights_" + i);
			if(weightText) {
				var weight = parseFloat(weightText.value);
				action.setWeight(i, weight);
			} else {
				break;
			}
		}
	}
};

function onWeightChanged(){
	var action = 	mapControl.get_action();
	if(action.type != "ServiceAreaAction"){
		return;
	}
	var pos = this.id.indexOf("_");
	if(pos<0){
		return;
	}
	var index = parseInt(this.id.substring(pos+1,this.id.length));
	var weight = parseFloat(this.value);
	if(isNaN(weight) || weight<=0){
		alert(page_res["invalidValue"]+this.value);
		weight = 200.0;
		this.value = weight;
		action.setWeight(index,weight);
		this.focus();
	}else{
		action.setWeight(index,weight);
	}
};

function onServiceCenterRemoved(index){
	var table = document.getElementById("tblServiceWeights");
	if(table){
		table.deleteRow(index+1);
		if(table.rows.length == 1){
			table.style.display = "none";
		}
	}
};
//=========================服务区分析========================
//=========================叠加分析========================
function overlayAnalyst(){
    var sourceLayerName = document.getElementById("sourceLayerName").value;
    var operateLayertName = document.getElementById("operateLayertName").value;
    var sourceDatasetName;
    var sourceDataSourceName;
	if(sourceLayerName.indexOf("@") != -1){
		sourceDatasetName = sourceLayerName.substring(0, sourceLayerName.indexOf("@"));
		sourceDataSourceName = sourceLayerName.substring(sourceLayerName.indexOf("@") + 1, sourceLayerName.length);
	}
    var operateDatasetName;
    var operateDataSourceName;
	if(operateLayertName.indexOf("@") != -1){
		operateDatasetName = operateLayertName.substring(0, operateLayertName.indexOf("@"));
		operateDataSourceName = operateLayertName.substring(operateLayertName.indexOf("@") + 1, operateLayertName.length);
	}
    var overlayParam = new SuperMap.OverlayParam();
    overlayParam.sourceDatasetName = sourceDatasetName;
    overlayParam.operateDatasetName = operateDatasetName;
    overlayParam.operation = eval(document.getElementById("overlayType").value);
    overlayParam.sourceDataSourceName = sourceDataSourceName;
    overlayParam.operateDataSourceName = operateDataSourceName;
    overlayParam.resultDatasetName = document.getElementById("overlayResultName").value;
    overlayParam.deleteResultIfExists = document.getElementById("deleteResultIfExists").checked;
    overlayParam.resultDataSourceName = sourceDataSourceName;
    
    var showOverlayResult = document.getElementById("showOverlayResult").checked;
    mapControl.getMap().overlayAnalyst(overlayParam, showOverlayResult, onOverlayAnalystComplete);
};

function fillOverlayAnalystLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var datasetType = new SuperMap.DatasetType()
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的矢量图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType != datasetType.text){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};

function fillSpatialOperateLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var datasetType = new SuperMap.DatasetType()
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的矢量图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.region){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};

function setSourceQueryForSpatialOperate(){
    var spatialOperateAction = mapControl.get_action();
    if(spatialOperateAction.type != "SpatialOperateAction"){
        spatialOperateAction = new SuperMap.UI.SpatialOperateAction(document.getElementById("sourceLayer").value, document.getElementById("operateLayer").value, document.getElementById("toleranceSpatailOperate").value, document.getElementById("showSpatialOperateResult").checked, onspatialOperateComplete);
        mapControl.set_action(spatialOperateAction);
    }
    spatialOperateAction.selectSourceGeometry = true;
    spatialOperateAction.strokeColor = "red";
    spatialOperateAction.sourceLayer = document.getElementById("sourceLayer").value;
    spatialOperateAction.sourceQueryParam.queryLayerParams[0].name = document.getElementById("sourceLayer").value;
};

//叠加分析-->地物与地物叠加-->选择被操作地物-->选择图层下的下拉框改变的事件。
function setSourceQueryForSpatialOperateChange() {
	var spatialOperateAction = mapControl.get_action();
	//alert(spatialOperateAction.type == "SpatialOperateAction");
	if(spatialOperateAction.type == "SpatialOperateAction") {
		spatialOperateAction.selectSourceGeometry = true;
		spatialOperateAction.strokeColor = "red";
		spatialOperateAction.sourceLayer = document.getElementById("sourceLayer").value;
		spatialOperateAction.sourceQueryParam.queryLayerParams[0].name = document.getElementById("sourceLayer").value;
	}
};
function setOperateQueryForSpatialOperate(){
    var spatialOperateAction = mapControl.get_action();
    if(spatialOperateAction.type != "SpatialOperateAction"){
        spatialOperateAction = new SuperMap.UI.SpatialOperateAction(document.getElementById("sourceLayer").value, document.getElementById("operateLayer").value, document.getElementById("toleranceSpatailOperate").value, document.getElementById("showSpatialOperateResult").checked, onspatialOperateComplete);
        mapControl.set_action(spatialOperateAction);
    }
    spatialOperateAction.selectSourceGeometry = false;
    spatialOperateAction.strokeColor = "blue";
    spatialOperateAction.operateLayer = document.getElementById("operateLayer").value;
    spatialOperateAction.operateQueryParam.queryLayerParams[0].name = document.getElementById("operateLayer").value;
};
//叠加分析-->地物与地物叠加-->选择操作地物：-->选择图层下的下拉框改变的事件。
function setOperateQueryForSpatialOperateChange() {
	var spatialOperateAction = mapControl.get_action();
	//alert(spatialOperateAction.type == "SpatialOperateAction");
	if(spatialOperateAction.type == "SpatialOperateAction") {
		spatialOperateAction.selectSourceGeometry = false;
	    spatialOperateAction.strokeColor = "blue";
	    spatialOperateAction.operateLayer = document.getElementById("operateLayer").value;
	    spatialOperateAction.operateQueryParam.queryLayerParams[0].name = document.getElementById("operateLayer").value;
	}
};
function onspatialOperateComplete(result){
//    alert(result);
};

function spatialOperate(){
    var spatialOperateAction = mapControl.get_action();
    if(spatialOperateAction){
        spatialOperateAction.spatialOperationType = document.getElementById("spatialOperationType").value;
        spatialOperateAction.spatialOperate();
    }
};

function onOverlayAnalystComplete(result){
    var tempParams = mapControl.get_params();
    tempParams.layersKey = result.message;
    mapControl.set_params(tempParams);
    mapControl.refreshMapControl();
};

function setOverlayWithGeometryAction(){
    var overlayParam = new SuperMap.OverlayParam();
    var showOverlayResult = document.getElementById("showOverlayResultWithGeometry").checked;
    var operateLayerName = document.getElementById("operatorGeometryLayer").value;
    
    var overlayWithGeometryAction = mapControl.get_action();
    if(overlayWithGeometryAction.type != "OverlayWithGeometryAction"){
        overlayWithGeometryAction = new SuperMap.UI.OverlayWithGeometryAction(overlayParam, operateLayerName, document.getElementById("toleranceOverlayWithGeometry").value, showOverlayResult, onOverlayAnalystComplete);
        mapControl.set_action(overlayWithGeometryAction);
    }
};
//叠加分析--地物与图层叠加 -->选择操作图层和被操作图层改变时出发的事件。
function setOverlayWithGeometryActionChange() {
	var overlayParam = new SuperMap.OverlayParam();
    var showOverlayResult = document.getElementById("showOverlayResultWithGeometry").checked;
    var operateLayerName = document.getElementById("operatorGeometryLayer").value;
	var overlayWithGeometryAction = mapControl.get_action();
	if(overlayWithGeometryAction.type == "OverlayWithGeometryAction") {
		overlayWithGeometryAction = new SuperMap.UI.OverlayWithGeometryAction(overlayParam, operateLayerName, document.getElementById("toleranceOverlayWithGeometry").value, showOverlayResult, onOverlayAnalystComplete);
        mapControl.set_action(overlayWithGeometryAction);
	}
};
function overlayWithGeometry(){
    var overlayWithGeometryAction = mapControl.get_action();
        if(overlayWithGeometryAction){
        var sourceLayerName = document.getElementById("sourceLayerNameWithGeometry").value;
        var sourceDatasetName;
        var sourceDataSourceName;
	    if(sourceLayerName.indexOf("@") != -1){
		    sourceDatasetName = sourceLayerName.substring(0, sourceLayerName.indexOf("@"));
		    sourceDataSourceName = sourceLayerName.substring(sourceLayerName.indexOf("@") + 1, sourceLayerName.length);
	    }
        overlayWithGeometryAction.overlayParam.sourceDatasetName = sourceDatasetName;
        overlayWithGeometryAction.overlayParam.operation = eval(document.getElementById("overlayWithGeometryType").value);
        overlayWithGeometryAction.overlayParam.sourceDataSourceName = sourceDataSourceName;
        overlayWithGeometryAction.overlayParam.resultDatasetName = document.getElementById("overlayResultWithGeometry").value;
        overlayWithGeometryAction.overlayParam.deleteResultIfExists = document.getElementById("deleteResultIfExistsWithGeometry").checked;
        overlayWithGeometryAction.overlayParam.resultDataSourceName = sourceDataSourceName;
        overlayWithGeometryAction.overlayAnalyst();
    }
};
//=========================叠加分析========================
//=========================Demo页面行为操作=========================
function setMenu(menuID){
	var oMenu = document.getElementById(menuID);
	var oMenuTD = eval("document.all." + oMenu.id + "TD");
	if(oMenu){
		if(oMenu.style.display == ""){
			oMenu.style.display = "none";
			oMenuTD._expand = false;
			onMenuMouseOver(oMenuTD);
		}else{
			setinitMenu();
			// 计算当前显示的菜单的高度
			var totalHeight = 677;
			var barHeight = 26;
			var iMenuCount = 0;
			var menuHeight = totalHeight - barHeight * iMenuCount;		

			oMenu.style.display = "";
			oMenuTD._expand = true;
			onMenuMouseOver(oMenuTD);
		}
	}
	//borderline();//对齐边界
};

function setinitMenu(){
	// 总高度
	var totalHeight = 677;
	// 菜单栏高度
	var barHeight = 26;
	// 菜单个数
	var iMenuCount = 0;
	var oMenus = document.getElementsByTagName("tr");
	var i;
	for(i = 0; i < oMenus.length; i++){
		if(oMenus[i].id.lastIndexOf("Menu") != -1){
			oMenus[i].style.display = "none";
			++iMenuCount;
		}
	}

	var oMenuTDs = document.getElementsByTagName("td");
	var j;
	for(j = 0; j < oMenuTDs.length; j++){
		if(oMenuTDs[j].id.lastIndexOf("MenuTD") != -1){
			oMenuTDs[j]._expand = false;
			oMenuTDs[j].background = "../../images/supGis/leftbutton_close_s.jpg";
		}
	}	
};

function onMenuMouseOver(oTD){
	if(eval(oTD._expand)){
		oTD.background = "../../images/supGis/leftbutton_close_s.jpg";
	}else{
		oTD.background = "../../images/supGis/leftbutton_close_s.jpg";
	}
	oTD.style.color = "244E88";
};

function onMenuMouseOut(oTD){
	if(eval(oTD._expand)){
		oTD.background = "../../images/supGis/leftbutton_close_s.jpg";
	}else{
		oTD.background = "../../images/supGis/leftbutton_close_s.jpg";
	}
	oTD.style.color = "244E88";
};

/**
 * 填充查询图层下拉框
 */
function fillQueryLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的矢量图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};

/**
 *
 * 填充可以做缓冲分析的图层
 */
function fillBufferEnableLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var superMapDatasetType = new SuperMap.DatasetType();
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的矢量图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								(objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == superMapDatasetType.point ||
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == superMapDatasetType.line ||
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == superMapDatasetType.region)){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};
/**
 * 填充网络图层下拉框
 */
function fillNetworkLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var datasetType = new SuperMap.DatasetType();
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的矢量图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.network){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								//oOption.value = objLayers[i].subLayers[j].name;
								oOption.value = objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetName+ "@"+objLayers[i].subLayers[j].layerSetting.datasetInfo.datasourceName;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};

/**
 * 获取所有可查图层
 */
function getQueryLayerNames(){
	var queryLayerNames = new Array();
	try{
		var objLayers = mapControl.getMap().get_layers();
		if(objLayers){
			var layerSettingType = new SuperMap.LayerSettingType();
			for(var i = 0; i < objLayers.length; i++){
				if(objLayers[i] && 
					objLayers[i].layerSetting && 
					objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
					objLayers[i].subLayers){
					var superMapLayerType = new SuperMap.SuperMapLayerType();
					for(var j = 0; j < objLayers[i].subLayers.length; j++){
						// SuperMap的矢量图层
						if(objLayers[i].subLayers[j] && 
							objLayers[i].subLayers[j].layerSetting && 
							objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
							objLayers[i].subLayers[j].queryable){
							var layerName = objLayers[i].subLayers[j].name;
							queryLayerNames.push(layerName);
						}
					}			
				}
			}
		}
	}catch(e){
		alert(e);
	}
	return queryLayerNames;
};

/**
 * 填充编辑图层下拉框
 */
/**
function fillEditLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var datasetType = new SuperMap.DatasetType();
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的点、线、面
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo &&
								(objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.point ||
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.line ||
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.region)){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name + "::" + objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};
*/

function fillPointQueryLayers(name){
	try{
		var objSelect = document.getElementById(name);
		if(objSelect){
			var objLayers = mapControl.getMap().get_layers();
			while(objSelect.options.length > 0){
				objSelect.remove(0);
			}
			if(objLayers){
				var layerSettingType = new SuperMap.LayerSettingType();
				for(var i = 0; i < objLayers.length; i++){
					if(objLayers[i] && objLayers[i].layerSetting && 
						objLayers[i].layerSetting.layerSettingType == layerSettingType.supermapcollection && 
						objLayers[i].subLayers){
						var superMapLayerType = new SuperMap.SuperMapLayerType();
						var datasetType = new SuperMap.DatasetType();
						for(var j = 0; j < objLayers[i].subLayers.length; j++){
							// SuperMap的点图层
							if(objLayers[i].subLayers[j] && 
								objLayers[i].subLayers[j].layerSetting && 
								objLayers[i].subLayers[j].layerSetting.superMapLayerType == superMapLayerType.vector &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo &&
								objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType == datasetType.point){
								var oOption = document.createElement("OPTION");
								var layerName = objLayers[i].subLayers[j].caption ? objLayers[i].subLayers[j].caption : objLayers[i].subLayers[j].name;
								if(layerName.indexOf("@") != -1){
									layerName = layerName.substring(0, layerName.indexOf("@"));
								}
								oOption.text = layerName;
								oOption.value = objLayers[i].subLayers[j].name + "::" + objLayers[i].subLayers[j].layerSetting.datasetInfo.datasetType;
								objSelect.options.add(oOption);
							}
						}			
					}
				}
			}
		}
	}catch(e){
		alert(e);
	}
};

// 比例尺回车事件
function inputTextKeyup(e) {
	var event = e ? e : window.event;
	if (event.keyCode == 13){
		updateScale();
	}
};

// 设置比例尺
function updateScale(){
	var scale = document.getElementById("mapScaleInput").value;
	var x = document.getElementById("mapXInput").value;
	var y = document.getElementById("mapYInput").value;
	if(isNaN(x) || isNaN(y) || x == "" || y == ""){
		alert(page_res["coordinateIllegal"]);
		return;
	}
	x = eval(x);
	y = eval(y);
	// 判断输入的比例尺是否合法
	try{
		if((scale > 0) && !isNaN(scale)){
		  var newScale = 1.0 / eval(scale);
		  var center = new SuperMap.Point2D(x, y);
		  mapControl.setCenterAndZoom(center, newScale);
		}else{
		  alert(page_res["scaleIllegal"]);
		}
	}catch(e){
		alert(page_res["setScaleIllegal"]);
	}
};

// 根据坐标进行地图定位
function mapPosition(mapControl, x, y){
	try{
		var center = new SuperMap.Point2D(x, y);
		mapControl.viewByPoint(center);
	}catch(e){
		alert(page_res["mapPositionException"]);
	}
};
// 改变坐标点输入框的状态
function checkBoxClicked(checked){
	var objInputX = document.getElementById("mapXInput");
	var objInputY = document.getElementById("mapYInput");
	if(checked){
		objInputX.disabled = false;
		objInputY.disabled = false;
		objInputX.value = Math.floor(mapControl.getMapCenter().x*10)/10;
		objInputY.value = Math.floor(mapControl.getMapCenter().y*10)/10;
	}else{
		objInputX.disabled = true;
		objInputY.disabled = true;
	}
};

function onDemoMouseMoving(e){
	e = SuperMap.Utility.getEvent(e);
	try{
		if (!document.getElementById("positionInputChecker").checked){
			var pixelX = SuperMap.Utility.getMouseX(e);
			var pixelY = SuperMap.Utility.getMouseY(e);
			var offsetTop = mapControl.get_container().offsetTop;
			var offsetLeft = mapControl.get_container().offsetLeft;
			var pixelPoint = new SuperMap.Point(pixelX - offsetLeft, pixelY - offsetTop); 
			var mapPoint = mapControl.pixelToMapCoord(pixelPoint);
			var mapX, mapY;
			// 取小数点后一位
			mapX = Math.floor(mapPoint.x * 10) / 10;
			mapY = Math.floor(mapPoint.y * 10) / 10;
			var objMapXInput = document.getElementById("mapXInput");
			var objMapYInput = document.getElementById("mapYInput");
			if(objMapXInput){
				objMapXInput.value = mapX;
			}
			if(objMapYInput){
				objMapYInput.value = mapY;
			}
		}
	}catch(e){
		alert(page_res["mouseMoveException"] + e);
	}
};

function updateMapScaleInput(){
	document.getElementById("mapScaleInput").value = Math.floor(1.0 / mapControl.get_mapScale());
};

var menuHidden = false;
var positionBarMinimized = false;

// 布置页面
function rerangePage(){
	// 获取客户端窗口的大小
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight - 5;
	// 在1024*768分辨率下，地图窗口的大小
	var minClientWidth = 1004;
	var minClientHeight = 595;
	// 地图最小宽度
	var leastMapWidth = 802;
	// 地图最小高度
	var leastMapHeight = 429;
	// 客户端窗口最小宽度
	var leastWindowWidth = leastMapWidth + 220;
	// 窗口大小变化后，跟最初相比的偏移量
	var deltaWidth = (clientWidth - minClientWidth);
	var deltaHeight = (clientHeight - minClientHeight);
	// 如果页面比初始化还小，就按照初始化大小处理
	if(deltaWidth < 0){
		deltaWidth = 0;
	}
	if(deltaHeight < 0){
		deltaHeight = 0;
	}
	var titleDIV = document.getElementById("title_div");
	titleDIV.style.width = clientWidth;
	var showMenuImg = document.getElementById("showMenu");
	var menuDIV = document.getElementById("menuDiv");
	var mapLeftTopDIV = document.getElementById("maplefttop");
	var mapTopDIV = document.getElementById("maptop");
	var mapLeftDIV = document.getElementById("mapleft");
	var mapBottomDIV = document.getElementById("mapbottom");
	var mapLeftBottomDIV = document.getElementById("mapleftbottom");
	var mapRightTopDIV = document.getElementById("maprighttop");
	var mapRightDIV = document.getElementById("mapright");
	var mapRightBottomDIV = document.getElementById("maprightbottom");
	var toolbarDIV = document.getElementById("toolbar");
	var positionBarDIV = document.getElementById("positionbar");
	var positionBarMiddleDIV = document.getElementById("positionbar_middle");
	var positionBarRightDIV = document.getElementById("positionbar_right");
	var positionBarMinimum = document.getElementById("positionbar_minimum");
	var positionBarResume = document.getElementById("positionbar_resume");
	var footerDiv = document.getElementById("footer_div");
	var positionbarFunctionDiv = document.getElementById("positionbar_function_div");
	var naviContainer = document.getElementById("navigation");
	// 当前地图页面宽度
	var mapClientWidth = 0;
	// 当前地图页面高度
	var mapClientHeight = 0;
	// 参照点像素坐标
	var reliantPointX = 0;
	var reliantPointY = 0;
	
	var tempDiv = document.createElement("DIV");
	tempDiv.style.display = "none";
	tempDiv.style.width = 0;
	tempDiv.style.height = 0;
	tempDiv.style.top = 0;
	tempDiv.style.left = 0;
	tempDiv.style.bottom = 0;
	tempDiv.style.right = 0;
	toolbarDIV = toolbarDIV?toolbarDIV:tempDiv;
	positionBarDIV = positionBarDIV?positionBarDIV:tempDiv;
	positionBarMiddleDIV = positionBarMiddleDIV?positionBarMiddleDIV:tempDiv;
	positionBarRightDIV = positionBarRightDIV?positionBarRightDIV:tempDiv;
	positionBarMinimum = positionBarMinimum?positionBarMinimum:tempDiv;
	positionBarResume = positionBarResume?positionBarResume:tempDiv;
	positionbarFunctionDiv = positionbarFunctionDiv?positionbarFunctionDiv:tempDiv;
	footerDiv = footerDiv?footerDiv:tempDiv;
	naviContainer = naviContainer?naviContainer:tempDiv;
	
	if(menuHidden){
		menuDIV.style.display = "none";
		mapClientWidth = (clientWidth - 28);
		mapClientHeight = (clientHeight - 135);
		if(mapClientWidth < leastMapWidth){
			mapClientWidth = leastMapWidth;
		}
		if(mapClientHeight < leastMapHeight){
			mapClientHeight = leastMapHeight
		}
		
		if(mapControl){
			mapControl.resize(mapClientWidth, mapClientHeight);
			mapControl.get_container().style.left = 19;
			mapControl.set_containerX(19);
			reliantPointX = parseInt(mapControl.get_container().style.left);
			reliantPointY = parseInt(mapControl.get_container().style.top);
		}else{
			reliantPointX = 19;
			reliantPointY = 109;
		}
		// 183长度为左边工具栏宽度
		titleDIV.style.width = ((clientWidth<(leastWindowWidth - 195))?(leastWindowWidth - 195):clientWidth) ;
		footerDiv.style.width = titleDIV.style.width;
		showMenuImg.style.left = 0;
		mapLeftTopDIV.style.left = 0;
		mapLeftDIV.style.left = 0;
		mapLeftBottomDIV.style.left = 0;
		toolbarDIV.style.left = reliantPointX + 16;
		toolbarDIV.style.width = mapClientWidth - 14;
	}else{
		menuDIV.style.display = "";
		mapClientWidth = (clientWidth - 225);
		mapClientHeight = (clientHeight - 135);
		if(mapClientWidth < leastMapWidth){
			mapClientWidth = leastMapWidth;
		}
		if(mapClientHeight < leastMapHeight){
			mapClientHeight = leastMapHeight
		}
		if(mapControl){
			mapControl.resize(mapClientWidth, mapClientHeight);
			mapControl.get_container().style.left = 216;
			mapControl.set_containerX(216);
			reliantPointX = parseInt(mapControl.get_container().style.left);
			reliantPointY = parseInt(mapControl.get_container().style.top);
		}else{
			reliantPointX = 216;
			reliantPointY = 109;
		}
		titleDIV.style.width = ((clientWidth<leastWindowWidth)?leastWindowWidth:clientWidth) ;
		footerDiv.style.width = titleDIV.style.width ;
		showMenuImg.style.left = reliantPointX - 19;
		mapLeftTopDIV.style.left = reliantPointX - 19;
		mapLeftDIV.style.left = reliantPointX - 19;
		mapLeftBottomDIV.style.left = reliantPointX - 19;
		toolbarDIV.style.left = reliantPointX + 16;
		toolbarDIV.style.width = mapClientWidth - 14;
	}

	mapTopDIV.style.left = reliantPointX;
	mapTopDIV.style.width = mapClientWidth;
	mapRightTopDIV.style.left = mapClientWidth + reliantPointX;
	mapRightDIV.style.left = mapClientWidth + reliantPointX;
	mapRightDIV.style.height = mapClientHeight;
	mapRightBottomDIV.style.left = mapClientWidth + reliantPointX;
	mapRightBottomDIV.style.top = mapClientHeight + reliantPointY;
	mapBottomDIV.style.left = reliantPointX;
	mapBottomDIV.style.width = mapClientWidth;
	positionBarDIV.style.left = reliantPointX;
	if(positionBarMinimized){
		positionBarDIV.style.width = 58;
		positionBarMiddleDIV.style.width = 50;
		positionBarRightDIV.style.left = 54;
		positionBarResume.style.left = 26;
		positionBarMinimum.style.left = 4;
	}else{
		positionBarDIV.style.width = mapClientWidth;
		positionBarMiddleDIV.style.width = mapClientWidth - 7;
		positionBarRightDIV.style.left = parseInt(positionBarMiddleDIV.style.width) + parseInt(positionBarMiddleDIV.style.left);
		positionBarMinimum.style.left = parseInt(positionBarRightDIV.style.left) - 47;
		positionBarResume.style.left = parseInt(positionBarMinimum.style.left) + 22;
	}

	mapLeftDIV.style.height = mapClientHeight;
	mapLeftBottomDIV.style.top = mapClientHeight + reliantPointY;
	mapBottomDIV.style.top = mapClientHeight + reliantPointY;
	positionBarDIV.style.top = mapClientHeight + reliantPointY - parseInt(positionBarDIV.style.height);
	positionBarDIV.style.zIndex = 20;
	menuDIV.style.height = mapClientHeight + 45;
	positionbarFunctionDiv.noWrap = -1;
	footerDiv.style.top = parseInt(positionBarDIV.style.top) + 38;
    naviContainer.style.top = parseInt(mapRightTopDIV.style.top) + 12;
    naviContainer.style.left = parseInt(mapRightTopDIV.style.left) - 131;
    showDate();
    tempDiv = null;
};


function showMenu(){
	menuHidden = false;
	var menuDiv = document.getElementById("menuDiv");
	if(menuDiv){
		menuDiv.style.visibility = "";
	}
	var showMenuImg = document.getElementById("showMenu");
	showMenuImg.src = "../../images/mid/closeleft.gif";
	showMenuImg.style.left = 0;
	showMenuImg.onclick = hideMenu;
	showMenuImg.alt=page_res["hideMenu"];
	showMenuImg.title=page_res["hideMenu"];
	showMenuImg.onmouseover = function(){
	    this.src='../../images/mid/closeleft.gif';
	}
	showMenuImg.onmousedown = function(){
	    this.src='../../images/mid/closeleft.gif';
	}
	showMenuImg.onmouseout = function(){
	    this.src='../../images/mid/closeleft.gif';
	}
	var sliderBar = document.getElementById("sliderBar");
	if(sliderBar){
		sliderBar.style.left = 178;
	}
	if(overviewControl){
		overviewControl.show();
	}
	rerangePage();
};

function onMinimizeButtonClick(e){
	var positionBar = document.getElementById("positionbar");
	if(positionBarMinimized){
		return;	
	}else{
		positionBarMinimized = true;
		var positionBarF = document.getElementById("positionbar_function_div");
		positionBarF.style.display = "none";
		var positionBarMiddle = document.getElementById("positionbar_middle");
		positionBarMiddle.style.width = 50;
		var minimumButton = document.getElementById("positionbar_minimum");
		minimumButton.style.left = 4;
		var resumeButton = document.getElementById("positionbar_resume");
		resumeButton.style.left = 26;
		var positionBarRight = document.getElementById("positionbar_right");
		positionBarRight.style.left = 54;
	}
};

function onResumeButtonClick(e){
	var positionBar = document.getElementById("positionbar");
	if(positionBarMinimized){
		positionBarMinimized = false;
		var positionBarF = document.getElementById("positionbar_function_div");
		positionBarF.style.display = "";
		rerangePage();
	}
};

function showDate(){
	var d = new Date();
	var year = d.getYear();
	var ua=navigator.userAgent.toLowerCase();
	if(ua.indexOf('gecko')!=-1){
	year += 1900;
	}
	var month = d.getMonth() + 1;
	var date = d.getDate();
	var day = d.getDay();
	switch(day){
		case 1:
			day = page_res["one"];
			break;
		case 2:
			day = page_res["two"];
			break;
		case 3:
			day = page_res["three"];
			break;
		case 4:
			day = page_res["four"];
			break;
		case 5:
			day = page_res["five"];
			break;
		case 6:
			day = page_res["six"];
			break;
		case 0:
			day = page_res["sun"];
			break;
	}
	var titleDIV = document.getElementById("title_div");
	var str = year + page_res["year"] + month + page_res["month"] + date + page_res["day"] + " " + page_res["week"] + day;
	var dateDIV = document.getElementById("date_div");
	dateDIV.innerHTML = "<font color=white>" + str + "</font>";
	dateDIV.style.left = parseInt(titleDIV.style.width) - 130;
};

// 获取ContextPath
function getContextPath() {
	var location = document.location.toString();
	var contextPath = "";
	if(location.indexOf("://") != -1) {
		contextPath += location.substring(0, location.indexOf("//") + 2);
		location = location.substring(location.indexOf("//") + 2, location.length);
	}
	var index = location.indexOf("/");
	contextPath += location.substring(0, index+1);
	location = location.substring(index+1);
	index = location.indexOf("/");
	contextPath += location.substring(0, index+1);
	
	return contextPath;
}
//=========================Demo页面行为操作=========================

//获取当前的专题图类型信息
function getCurrentThemeType(){
	var value = -1;
	var radios = document.getElementsByName("radThemeType");
	for( var i = 0 ; i < radios.length ; i++){
		if( radios[i] && radios[i].checked){
			value = radios[i].value;
		}
	}
	return eval(value);
}

function changThemePanel(){
	var themeType = getCurrentThemeType();
	var panelNames = ["divThemeUnique","divThemeRange","divThemeGraph"];
	for(var i = 0 ; i < panelNames.length ; i ++){
		var panelName = panelNames[i];
		if(panelName && panelName != ""){
			var div = document.getElementById(panelName);
			if(div){
				div.style.display = (i == themeType-1) ? 'block' : 'none';
			}
		}
	}
	bindThemePanelData();
}

//重新绑定专题图面板的数据信息
function bindThemePanelData(){
	var themeType = getCurrentThemeType();
	var themeValue = new SuperMap.ThemeType();
	var selection = getCurrentThemeDatasetListControl();
	switch(themeType){
		case themeValue.unique:
			fillDatasetvectorList(selection.id,onGetThemeDatasetsComplete);
			break;
		case themeValue.range:
			fillDatasetvectorList(selection.id,onGetThemeDatasetsComplete);
			break;
		case themeValue.graph:
			fillDatasetvectorList(selection.id,onGetThemeDatasetsComplete);
			onThemeGraphItemCountChanged();
			break;
		default:
			break;
	}
}

//获取数据源信息。根据地图图层的LayerSetting信息，获取第一个有效的数据源信息
function getDatasourceName(){
	var defaultName = "world";
	var map = mapControl.getMap();
	if(map){
		var layers = map.get_layers();
		if(layers && layers.length > 0){
			for( var i = 0 ; i < layers.length ; i++ ){
				var subLayers = layers[i].subLayers;
				if(subLayers && subLayers.length > 0){
					for( var j = 0 ; j < subLayers.length ; j++ ){
						if(subLayers[j].layerSetting && subLayers[j].layerSetting.datasetInfo 
						 &&  subLayers[j].layerSetting.datasetInfo.datasourceName !=""){
						 	defaultName = subLayers[j].layerSetting.datasetInfo.datasourceName;
						 	return defaultName;
						 }
					}
				}
			}
		}
	}
	return defaultName;
}

//获取当前的数据集列表控件
function getCurrentThemeDatasetListControl(){
	var themeType = getCurrentThemeType();
	var controlNames = ["selThemeUniqueDatasetList","selThemeRangeDatasetList","selThemeGraphDatasetList"];
	return document.getElementById(controlNames[themeType-1]);
}

//填充矢量数据集列表
function fillDatasetvectorList(selectionID,onComplete){	
	var control = document.getElementById(selectionID);
	if(control){
		var dataHandler = getContextPath() + "datahandler";
		var datamanager = new SuperMap.DataManager(dataHandler);
		var dataSourceName = getDatasourceName();
		datamanager.getDatasetInfos(dataSourceName, onComplete, null,null);
	}
}

//获取数据集信息的回调函数，返回所有数据集信息，过滤栅格、Grid以及Tabular类型的数据集
function onGetThemeDatasetsComplete(result) {	
	if(result){
		//清空数据集列表
		var objSelect = getCurrentThemeDatasetListControl();
		if(objSelect){
			while(objSelect.options.length > 0){
				objSelect.options.remove(0);
			}
			var i;
			var datasetType = new SuperMap.DatasetType();
			for(i=0;i<result.length;i++){
				if(result[i].datasetType == datasetType.grid ||
					result[i].datasetType == datasetType.image ||
					result[i].datasetType == datasetType.tabular ||
					result[i].datasetType == datasetType.text){
					continue;
					}
				var oOption = document.createElement("OPTION");
				var name = result[i].datasetName;
				oOption.value = name;
				oOption.text = name;
				objSelect.options.add(oOption);
			}
		}
		if(objSelect.options.length > 0 && objSelect.onchange){
			objSelect.onchange();
		}
	}
}

function onGetStatisticDatasetsComplete(result){
	if(result){
		//清空数据集列表
		var objSelect = document.getElementById("selStatisticDatasetList");
		if(objSelect && objSelect.options.length > 0){
			if(_GetBrowser() == "ie"){
				while( objSelect.options.length > 0 ){
					objSelect.options.remove(0);
				}
			} else {
				var length = objSelect.options.length;
				for(var i = 0; i < length;i++) {
					objSelect.remove(i);
				}
			}
		}
		if(objSelect && objSelect.options.length == 0){
			var i;
			var datasetType = new SuperMap.DatasetType();
			for(i=0;i<result.length;i++){
				if(result[i].datasetType == datasetType.grid ||
					result[i].datasetType == datasetType.image){
					continue;
					}
				var oOption = document.createElement("OPTION");
				var name = result[i].datasetName;
				oOption.value = name;
				oOption.text = name;
				objSelect.options.add(oOption);
			}
		}
		if(objSelect.options.length > 0 && objSelect.onchange){
			objSelect.onchange();
		}
	}
}
//重新填充字段列表信息
function rebindFieldList(fieldListName,datasetName){
	var selection = document.getElementById(fieldListName);
	if(selection && datasetName){
		var dataHandler = getContextPath() + "datahandler";
		var datamanager = new SuperMap.DataManager(dataHandler);
		var dataSourceName = getDatasourceName();
		var queryParam = new SuperMap.QueryParam();
		var sqlParam = new SuperMap.SqlParam();
		sqlParam.whereClause = "1<0";
		queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = datasetName;
		queryLayer.sqlParam = sqlParam;
		queryParam.queryLayerParams.push(queryLayer);
		datamanager.queryBase("query", ["dataSourceName", "queryParam","customParam"], [dataSourceName, queryParam,fieldListName], onGetFieldListComplete);
	}
}

//获取字段信息的回调函数，返回结果为查询的结果集
function onGetFieldListComplete(result){
	if(result && result.customResponse && result.recordSets && result.recordSets.length > 0){
		var selection = document.getElementById(result.customResponse);
		if(selection){
			if(_GetBrowser() == "ie"){
				while( selection.options.length > 0 ){
					selection.options.remove(0);
				}
			} else {
				var length = selection.options.length;
				for(var i = 0; i < length;i++) {
					selection.remove(i);
				}
			}
			var recordSet = result.recordSets[0];
			if(recordSet.returnFields && recordSet.returnFields.length > 0){
				for( var  i = 0 ; i < recordSet.returnFields.length ; i++){
					var option = document.createElement("OPTION");
					option.value = recordSet.returnFields[i];
					option.text = recordSet.returnFields[i];
					selection.options.add(option);
				}
			}  
			if(selection.onchange){
				selection.onchange();
			}
		}
	}
}

//设置文本框的值
function setTextboxValue(textBoxName,value){
	var textBox = document.getElementById(textBoxName);
	if(textBox){
		if(value){
			textBox.value = value;
		}else{
			textBox.value = "";
		}
	}
}


function showThemeGraphOption(count){
	var fieldCount = eval(count);
	var graghDiv = document.getElementById("divThemeGraghFields");
	var innerHTML = "";
	innerHTML += "<table width='100%'   style='font-size:12px;'>";
	for (var i = 0; i < fieldCount; i++){
		innerHTML += "<tr height='20'><td align='right' width='90px'><b>";
		innerHTML += page_res["field"] + (i + 1)+"：";
		innerHTML += "</b></td><td width='110px'></td></tr>";
		//字段名
		innerHTML += "<tr height='20'><td align='right' width='90px'>";
		innerHTML += page_res["name"];
		innerHTML += "</td><td align='left'>";
		innerHTML += "<SELECT id='selFields" + i + "' name='selFields" + i + "'  style='width:100px;' onchange=onSelectedChanged('" + i + "') />";
		innerHTML += "</td></tr>";
		
		//表达式
		innerHTML += "<tr height='20'><td align='right' width='90px'>";
		innerHTML += page_res["experssion"];
		innerHTML += "</td><td align='left'>";
		innerHTML += "<input id='txtExpression" + i + "' name='txtExpression" + i + "' style='text-align:center;width:100px;' type='text'/>";
		innerHTML += "</td></tr>";
		
		//相应的颜色值
		innerHTML += "<tr height='20'><td align='right'>";
		innerHTML += page_res["color"];
		innerHTML += "</td><td align='left'>";
		innerHTML += "<input  onmouseover='this.focus()' onclick='caculateColor(this)' onblur='setColor(this)' id='graghfieldcolor" + i + "' style='text-align: center;width: 100px;color: #FF0000;' type='text' value='FF0000' />";		
		innerHTML += "</td></tr>";
	}
	graghDiv.innerHTML = innerHTML;
}

 function showThemeRangeOption(count){
 	var rangeCount = eval(count);
 	var rangeDiv = document.getElementById("divRangeOption");
 	var innerHTML = "";
 	for (var i = 0;i < (rangeCount - 1); i++){
 		//需要根据字段的统计信息设置相应的值
		innerHTML += page_res["subsection"] + (i + 1) + ":&nbsp;";
		innerHTML += "<input id='txtRangeColor" + i + "' style='cursor:hand;text-align: center;width: 50px;color: #FF0000;' type='text' onclick='caculateColor(this)' onblur='setColor(this)' value='FF0000'/><br>";
	}
	rangeDiv.innerHTML = innerHTML;
 }

function addTheme(){
	var themeTypeValue = new SuperMap.ThemeType();
	var themeType = getCurrentThemeType();
	var theme;
	var datasourceName = getDatasourceName();
	var layerName = "";
	//单值专题图
	if(themeType == themeTypeValue.unique){
		var datasetName = document.getElementById("selThemeUniqueDatasetList").value;
		layerName = datasetName + "@" + datasourceName;
		theme = new SuperMap.ThemeUnique();
		theme.uniqueExpression = document.getElementById("txtThemeUniqueExpression").value;
		theme.makeDefaultParam = new SuperMap.ThemeUniqueParam();
		theme.makeDefaultParam.layerName = layerName;
		theme.makeDefaultParam.colorGradientType = eval(document.getElementById("selThemeUniqueColorGradientType").value);
	//范围专题图
	}else if(themeType == themeTypeValue.range){
		theme = new SuperMap.ThemeRange();
		var datasetName = document.getElementById("selThemeRangeDatasetList").value;
		layerName = datasetName + "@" + datasourceName;
		theme.rangeExpression = document.getElementById("txtThemeRangeExpression").value;
		//使用自动分段的方式
		if(document.getElementById("radRangeByDefault").checked){
			theme.makeDefaultParam = new SuperMap.ThemeRangeParam();
			theme.makeDefaultParam.layerName = layerName; 
			theme.makeDefaultParam.colorGradientType = eval(document.getElementById("selThemeRangeColorGradientType").value);
			theme.makeDefaultParam.rangeMode = eval(document.getElementById("selRangeMode").value);
			theme.makeDefaultParam.rangeParameter = eval(document.getElementById("txtRangeParameter").value);
		//使用自定义分段的方式
		}else{
			theme.items = getThemeRangeItems();
		}		
	}else if(themeType == themeTypeValue.graph){
		var datasetName = document.getElementById("selThemeGraphDatasetList").value;
		layerName = datasetName + "@" + datasourceName;
		theme = new SuperMap.ThemeGraph();
		theme.items = getThemeGraphItems();
		theme.offsetX = "0.0";
		theme.offsetY = "0.0";
		theme.graphType = eval(document.getElementById("txtGraphType").value);
		theme.barWidth = 0.0;
		theme.startAngle = 0.0;
		theme.roseAngle = 0.0;
		theme.isFlowEnabled = document.getElementById("ckbIsFlowEnabled").checked;
		theme.leaderLineStyle = null;
		theme.isLeaderLineDisplayed = false;
		theme.isNegativeDisplayed = false;
		theme.axesColor = new SuperMap.Color(0,0,0);
		theme.isAxesDisplayed = document.getElementById("ckbIsAxesDisplayed").checked;
		theme.axesTextStyle = null;
		theme.isAxesTextDisplayed = document.getElementById("ckbIsAxesTextDisplayed").checked;
		theme.isAxesGridDisplayed = document.getElementById("ckbIsAxesGridDisplayed").checked;
		theme.graphTextStyle = null;
		theme.isGraphTextDisplayed = document.getElementById("ckbIsGraphTextDisplayed").checked;
		if(theme.isGraphTextDisplayed){
			theme.graphTextStyle = new SuperMap.TextStyle();
			theme.graphTextStyle.align = 1;
			theme.graphTextStyle.fontName = "宋体";
			theme.graphTextStyle.transparent  = false;
			
			theme.graphTextStyle.fixedSize = document.getElementById("ckbIsGraphTextSizeFixed").checked;
			theme.graphTextStyle.fontHeight = eval(document.getElementById("txtGraphTextFontHeight").value);
			theme.graphTextStyle.fontWidth = 0.0;
			var td = document.getElementById("tdGraphTextColor");
			var colorString = td.bgColor;
			var red = parseInt("0x"+colorString.substring(1,3));
			var green = parseInt("0x"+colorString.substring(3,5));
			var blue = parseInt("0x"+colorString.substring(5));
			theme.graphTextStyle.color = new SuperMap.Color(red,green,blue);
		}
		theme.graphTextFormat = eval(document.getElementById("selGraphTextFormat").value);
		theme.isGraphTextDisplayed = document.getElementById("ckbIsGraphTextDisplayed").checked;
		theme.minGraphSize = eval(document.getElementById("txtThemeGraphMinValue").value);
		theme.maxGraphSize = eval(document.getElementById("txtThemeGraphMaxValue").value);
		theme.isGraphSizeFixed = document.getElementById("ckbIsGraphSizeFixed").checked;
		theme.graduatedMode = eval(document.getElementById("selGraduatedMode").value);
		theme.themeType = new SuperMap.ThemeType().graph;
	}
	if(theme){
		mapControl.getMap().addTheme(layerName,themeType,theme,onAddThemeComplete);
	} 
}

function onAddThemeComplete(result){
	if(result){
		var strs = result.split(",");
		if(strs.length>1){
			var layerName = strs[0];
			var selection = document.getElementById("selDynamicThemes");
			var option = document.createElement("OPTION");
			option.text  = layerName;
			option.value  = layerName;
			selection.options.add(option);
			selection.selectedIndex = selection.options.length - 1;
			mapControl.getMap()._params.layersKey = eval(strs[1]);
			mapControl.refreshMapControl();
		}
	}
}

function changeRangeDiv(){
	var radios = document.getElementsByName("radIsRangeByDefault");
	for( var i = 0 ; i < radios.length ; i++){
		var div = document.getElementById(radios[i].value);
		if(div){ 
			if(radios[i].checked){
				div.style.display = 'block';
			}else{
				div.style.display = 'none';
			}
		}
	}
	//如果是自定义分段，则显示分段值
	if(document.getElementById("radRangeByManul").checked && getCurrentRangeItemCount()!= eval(document.getElementById("txtThemeRangeItemsCount").value)){
		onThemeRangeItemCountChanged();
	}
}

function onRangeModeChanged(value){
	value = eval(value);
	var rangeModeValue = new SuperMap.RangeMode();
	var label = document.getElementById("labRangeInfo");
	var textBox = document.getElementById("txtRangeParameter");
	var divInfo = document.getElementById("divRangeSetting");
	switch(value){
		case rangeModeValue.equalInterval:
		case rangeModeValue.squareRoot:
		case rangeModeValue.logarithm:
		case rangeModeValue.quantile:
			divInfo.style.display = 'block';
			label.innerHTML = page_res["segmentNumber"];
			textBox.value = "3";
			break;
		case rangeModeValue.stdDeviation:
			divInfo.style.display = 'none';
			break;
		case rangeModeValue.customInterval:
			divInfo.style.display = 'block';
			label.innerHTML = page_res["distance"];
			textBox.value = "1.0";
			break;		
	}
}

//计算实际显示的RangeItem的个数
function getCurrentRangeItemCount(){
	var i = 0;
	var obj = document.getElementById("txtRangeItemMin"+i);
	while(obj){
		i++;
		obj = document.getElementById("txtRangeItemMin"+i);
	}
	return i;
}


function onThemeRangeItemCountChanged(){
	//检查段数在2-32之间，而且必须是数字
	var textBox = document.getElementById("txtThemeRangeItemsCount");
	var str = textBox.value;	 
	if(str == null || str.length == 0){
		alert(page_res["segmentNumberNull"]);
		textBox.focus();
		return;
	}
	if (!(/^\d+$/.test(str))){
	     alert(page_res["segmentNumberInteger"]);
	     textBox.focus();
	     return;
   	}
	var count = eval(str);
	if(count <2 || count > 32){
		alert("段数必须介于 2-32");
		textBox.focus();
		return;
	}
	//根据段数设置RangeItem
	var div = document.getElementById("divThemeRangeItems");
	div.innerHTML = "";
	var innerHTML = "";
	innerHTML += "<table cellpadding='0' cellspacing='0' style='font-size:12px;'>";
	var colorMax = Math.round(Math.pow(2,24)-1);
	for (var i = 0; i < count; i++){
		innerHTML += "<tr height='20'>";
		innerHTML += "<td align='right'width='40px'>";
		innerHTML += "<input id='txtRangeItemMin"+i+"' type='text' style='width:40px;align:center' onchange='onRangeItemChanged(this);'/>";
		innerHTML += "</td>";
		innerHTML += "<td align='left'>";
		innerHTML += "--<input id='txtRangeItemMax"+i+"' type='text' style='width:40px;align:center' onchange='onRangeItemChanged(this);'/>";
		innerHTML += "</td>";
		var randomColor = (Math.round(Math.random()*(colorMax-1))+1).toString(16);
		var zcount = 6-randomColor.length;
		for( var j = 0 ; j < zcount ; j++){
		    randomColor = "0" + randomColor; 
		}
		innerHTML += "<td id='tdRangeItemColor"+i+"' align='left' width='60px' onclick='caculateColor(this)' bgcolor='#"+randomColor+"'>";
		innerHTML += "</td>";
		innerHTML += "</tr>";
	}
	innerHTML += "</table>";
	div.innerHTML = innerHTML;
}

function isNumber(str){
	if(str == null || str.length == 0){
		return false;
	}
	if( str.indexOf(':') != -1 || str.indexOf('-') > 0 ) {
		return false;
	}
	return /^-?\d+\.?\d?/.test(str);
}

function onRangeItemChanged(target){
	if(!isNumber(target.value)){
		target.focus();
		alert(page_res["inputEffectData"]);
		return;
	}
	var value = eval(target.value);
	//如果是有效值，则保证之前的所有值小于该值，并且相邻的值是相等的
	var index = eval(target.id.substring("txtRangeItemMax".length));
	var isMin = (target.id.indexOf("txtRangeItemMin")!=-1);
	var curIndex = index;
	var curPrefix = "txtRangeItemMin";
	//设置之前的文本框
	if(isMin){
		curPrefix = "txtRangeItemMax";
		curIndex = index-1;
	} 
	var curTextbox = document.getElementById(curPrefix + curIndex);
	while(curTextbox){
		//相邻的设置为同样的值
		if(curIndex == index-1 && curPrefix == "txtRangeItemMax" && isMin){
			curTextbox.value = value;
		}
		else if(curTextbox.value != null && curTextbox.value != ""){
			var curValue = parseFloat(curTextbox.value);
			if(curValue == NaN){
				curTextbox.value = "";
				continue;
			}
			if(curIndex == index && curValue>value){
				alert(page_res["mustMoreThanStartValue"]);
				target.focus();
				return;
			}else if(curIndex < index && curValue>value){
				curTextbox.value = ""; 
			} 
		}
		curIndex = (curPrefix == "txtRangeItemMax") ? curIndex:curIndex-1;
		curPrefix = (curPrefix == "txtRangeItemMax") ? "txtRangeItemMin":"txtRangeItemMax";
		curTextbox = document.getElementById(curPrefix + curIndex);
	}
	//设置之后的文本框
	if(isMin){
		curPrefix = "txtRangeItemMax";
		curIndex = index;
	}else{
		curPrefix = "txtRangeItemMin";
		curIndex = index+1;
	}
	curTextbox = document.getElementById(curPrefix + curIndex);
	while(curTextbox){
		//相邻的设置为同样的值
		if(curIndex == index+1 && curPrefix == "txtRangeItemMin" && !isMin){
			curTextbox.value = value;
		}
		else if(curTextbox.value != null && curTextbox.value != ""){
			var curValue = parseFloat(curTextbox.value);
			if(curValue == NaN){
				curTextbox.value = "";
				continue;
			}
			if(curIndex == index && curValue<value){
				alert(page_res["mustLessThanEndValue"]);
				target.focus();
				return;
			}else if(curIndex > index && curValue<value){
				curTextbox.value = ""; 
			} 
		}
		curIndex = (curPrefix == "txtRangeItemMax") ? curIndex:curIndex+1;
		curPrefix = (curPrefix == "txtRangeItemMax") ? "txtRangeItemMin":"txtRangeItemMax";
		curTextbox = document.getElementById(curPrefix + curIndex);
	}
}

function getThemeRangeItems(){
	var items = [];
	var i = 0;
	var txtMin = document.getElementById("txtRangeItemMin"+i);
	var txtMax = document.getElementById("txtRangeItemMax"+i);
	while(txtMin && txtMax){
		var item = new SuperMap.ThemeRangeItem();
		item.start = eval(txtMin.value);
		item.end = eval(txtMax.value);
		item.style = new SuperMap.Style();
		var td = document.getElementById("tdRangeItemColor"+i);
		var colorString = td.bgColor;
		var red = parseInt("0x"+colorString.substring(1,3));
		var green = parseInt("0x"+colorString.substring(3,5));
		var blue = parseInt("0x"+colorString.substring(5));
		var color = new SuperMap.Color(red,green,blue);
		item.style.fillBackColor = color;
		item.style.fillForeColor = color;
		item.style.lineColor = color;
		items.push(item);
		i++;
		txtMin = document.getElementById("txtRangeItemMin"+i);
		txtMax = document.getElementById("txtRangeItemMax"+i);
	}
	return items;
}
//子项文本是否可见发生改变
function onGraphTextDisplayedChanged(){
	var checked = document.getElementById("ckbIsGraphTextDisplayed").checked;
	var div = document.getElementById("divGraphTextSetting");
	if(checked){
		div.style.display = 'block';
	}else{
		div.style.display = 'none';
	}
}

//用于制作统计专题图的数据集发生改变，重新绑定所有的Item的可供选择的字段列表
function onSelThemeGraphDatasetListChanged(){
	var datasetName = document.getElementById("selThemeGraphDatasetList").value;
	var dataHandler = getContextPath() + "datahandler";
	var datamanager = new SuperMap.DataManager(dataHandler);
	var dataSourceName = getDatasourceName();
	var queryParam = new SuperMap.QueryParam();
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = "1<0";
	queryParam.queryLayerParams = new Array();
	var queryLayer = new SuperMap.QueryLayerParam();
	queryLayer.name = datasetName;
	queryLayer.sqlParam = sqlParam;
	queryParam.queryLayerParams.push(queryLayer);
	datamanager.queryBase("query", ["dataSourceName", "queryParam"], [dataSourceName, queryParam], onSelThemeGraphDatasetListChangedComplete);
}

function onSelThemeGraphDatasetListChangedComplete(result){
	if(result && result.recordSets && result.recordSets.length > 0){
		var prefix = "selThemeGraphItemField";
		var curIndex = 0;
		
		var selection = document.getElementById(prefix+curIndex);
		while(selection){
			if(_GetBrowser() == "ie"){
				while( selection.options.length > 0 ){
					selection.options.remove(0);
				}
			} else {
				var length = selection.options.length;
				for(var i = 0; i < length;i++) {
					selection.remove(i);
				}
			}
			var recordSet = result.recordSets[0];
			if(recordSet.returnFields && recordSet.returnFields.length > 0){
				for( var  i = 0 ; i < recordSet.returnFields.length ; i++){
					var option = document.createElement("OPTION");
					option.value = recordSet.returnFields[i];
					option.text = recordSet.returnFields[i];
					selection.options.add(option);
				}
			}  
			if(selection.onchange){
				selection.onchange();
			}
			curIndex++;
			selection = document.getElementById(prefix+curIndex);
		}
	}
}

function getThemeGraphItems(){
	var items = [];
	var txtPrefix = "txtThemeGraphItemField";
	var tdPrefix = "tdThemeGraphItemColor";
	var curIndex = 0;
	
	var textbox = document.getElementById(txtPrefix+curIndex);
	while(textbox){
		var item = new SuperMap.ThemeGraphItem();
		item.graphExpression = textbox.value;
		item.uniformStyle =  new SuperMap.Style();
		var td = document.getElementById(tdPrefix+curIndex);
		var colorString = td.bgColor;
		var red = parseInt("0x"+colorString.substring(1,3));
		var green = parseInt("0x"+colorString.substring(3,5));
		var blue = parseInt("0x"+colorString.substring(5));
		var color = new SuperMap.Color(red,green,blue);
		item.uniformStyle.fillBackColor = color;
		item.uniformStyle.fillForeColor = color;
		item.uniformStyle.lineColor = color;
		items.push(item);
		curIndex++;
		textbox = document.getElementById(txtPrefix+curIndex);
	}
	return items;
}
//统计专题图字段个数改变，验证字段数目的有效性并重新设置字段的列表面板
function onThemeGraphItemCountChanged(){
	var textBox = document.getElementById("txtThemeGraphItemCount");
	var str = textBox.value;	 
	if(str == null || str.length == 0){
		alert(page_res["fieldCanNotNull"]);
		textBox.focus();
		return;
	}
	if (!(/^\d+$/.test(str))){
	     alert(page_res["fieldMustInteger"]);
	     textBox.focus();
	     return;
   	}
	var count = eval(str);
	if(count < 0){
		alert(page_res["fieldMustMoreThanZero"]);
		textBox.focus();
		return;
	}
	//设置DIV
	var div = document.getElementById("divThemeGraghItems");
	div.innerHTML = "";
	var innerHTML = "";
	innerHTML += "<table cellpadding='0' cellspacing='0' style='font-size:12px;'>";
	var colorMax = Math.round(Math.pow(2,24)-1);
	for (var i = 0; i < count; i++){
		innerHTML += "<tr height='20'>";
		innerHTML += "<td align='right'>";
		innerHTML += "<select id='selThemeGraphItemField"+i+"' onchange='onSelThemeGraphItemFieldChanged(this)' style='width:70px'></select>";
		innerHTML += "</td>";
		innerHTML += "<td align='left'>";
		innerHTML += "<input id='txtThemeGraphItemField"+i+"' type='text' style='width:60px;align:center'/>";
		innerHTML += "</td>";
		var randomColor = (Math.round(Math.random()*(colorMax-1))+1).toString(16);
		var zcount = 6-randomColor.length;
		for( var j = 0 ; j < zcount ; j++){
		    randomColor = "0" + randomColor; 
		}
		innerHTML += "<td id='tdThemeGraphItemColor"+i+"' align='left' width='25px' onclick='caculateColor(this)' bgcolor='#"+randomColor+"'>";
		innerHTML += "</td>";
		innerHTML += "</tr>";
	}
	innerHTML += "</table>";
	div.innerHTML = innerHTML;
	onSelThemeGraphDatasetListChanged();
}

//用户选择的字段发生改变
function onSelThemeGraphItemFieldChanged(selection){
	var prefix = "selThemeGraphItemField";
	var index = selection.id.substring(prefix.length);
	var textbox = document.getElementById("txtThemeGraphItemField"+index);
	if(textbox){
		textbox.value = selection.value;
	}
}

//子项文本是否可见发生改变
function onGraphTextDisplayedChanged(){
	var checked = document.getElementById("ckbIsGraphTextDisplayed").checked;
	var div = document.getElementById("divGraphTextSetting");
	if(checked){
		div.style.display = 'block';
	}else{
		div.style.display = 'none';
	}
}

//检查文本框中的文本是否为有效的数值
function checkNumber(textbox){
	if(textbox){
		var str = textbox.value;
		if(str.length == ""){
			textbox.value = "0.0";
			alert(page_res["numberCanNotNull"]);
			textbox.focus();
			return;
		}
		if(!isNumber(str)){
			textbox.value = 0.0;
			alert(page_res["inputEffectData"]);
			textbox.focus();
			return; 
		}
	}
}
//删除专题图
function deleteTheme(){
	var oSelect = document.getElementById("selDynamicThemes");
	if(oSelect.options.length > 0){
		var layerName = oSelect.value;
		var index = -1;
		if(layerName == ""){
			layerName = oSelect.options[0].value;
			index =  0;
		}else{
			index = oSelect.selectedIndex;
		}
		oSelect.remove(index);
		if(oSelect.options.length > 0){
			oSelect.selectedIndex = oSelect.options.length -1; 
		}
		mapControl.getMap().removeSuperMapLayer(layerName,onDeleteThemeComplete);
	}
}
	
function deleteAllTheme(){
	var oSelect = document.getElementById("selDynamicThemes");
	var layerNames = [];
	var count = oSelect.options.length; 
	if( count >= 0){
		for(var i= count - 1;i >= 0;i--){
			layerNames.push(oSelect.options[i].value);
			oSelect.remove(i);
		}
		mapControl.getMap().removeSuperMapLayers(layerNames,onDeleteThemeComplete);
	}
}

function onDeleteThemeComplete(hashCode){
	if(hashCode){
		mapControl.getMap()._params.layersKey = eval(hashCode);
		mapControl.refreshMapControl();
	}
}

//计算颜色值,并将对象的背景颜色值设为选择的值
function caculateColor(target){
  if(typeof(dlgHelper) != "undefined"){
    var clr;

    try{
        clr=dlgHelper.ChooseColorDlg(target.bgColor);		
    }catch (e){}

    if (clr || clr == 0) {
      var cl=clr.toString(16);
      var z="0";
      var i;
      for (i = cl.length; i < 6; i++) {
        cl = z.toString() + cl;
      }
      target.bgColor = "#"+cl;
    }
  }
}

function statistic(){
	var dataHandler = getContextPath() + "datahandler";
	var datamanager = new SuperMap.DataManager(dataHandler);
	var datasourceName = getDatasourceName();
	var datasetName = document.getElementById("selStatisticDatasetList").value;
	var fieldName = document.getElementById("selStatisticFieldList").value;
	var statisticMode = eval(document.getElementById("selStatisticMode").value);
	datamanager.statistic(datasourceName,datasetName,fieldName,statisticMode,onStatisticComplete);
}

function onStatisticComplete(result){
	alert(result);
}


//add by lyang 09-11-13

//鹰眼控件
function initOverview() {
	var overviewDiv = document.getElementById("view");
	if (overviewDiv) {
		if (!overviewControl) {
			//Rect2D rect2D=new SuperMap.Rect2D() ;
			overviewControl = new SuperMap.UI.OverviewControl(overviewDiv);
			overviewControl.set_map(mapControl);
			overviewControl.set_param(null);
			//overviewControl.setViewBounds(rect2D);
			overviewControl.initialize();
		}
	}
}

function pointMove(pointX,pointY) {
	var point2D = new Object()
	point2D.x=pointX ;
	point2D.y=pointY ;
	mapControl.panToMapCoord(point2D);
	//clearCustomLayer();
	insertTag(point2D);
}

//=================网点属性查询 start==========================
function bankLayoutQuery(pageNo) {
	//sql语句
	var bankId = document.getElementById("bankId").value;
	var bankName = document.getElementById("bankName").value;
	var orgfloor=document.getElementById("orgfloor").value ;
	var sqlClause = "1=1 and C_ORGFLOOR like '%"+orgfloor+"%' ";
	//如果输入条件为空，则不查询
	if((bankId==null || bankId=="")&& (bankName==null||bankName=="")){
		sqlClause="1=2" ;
	}
	if (bankId != null && bankId.length > 0) {
		sqlClause = sqlClause + " and C_BANK_ID like '%" + bankId + "%' ";
	}
	if (bankName != null && bankName.length > 0) {
		sqlClause = sqlClause + " and C_BANK_NAME like '%" + bankName + "%' ";
	}
	//图层名
	var queryLayerName = document.getElementById("bankLayoutSelect").value.split("::")[0];
	//是否高清晰显示
	var highlightSQL = document.getElementById("bankLayouthighlightSQL").checked;
	
	//进行地图查询设置条件的类
	var queryParam = new SuperMap.QueryParam();
	//设置查询属性条件的类
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = sqlClause;
	if (!queryLayerName) {
		queryParam.queryLayerParams = null;
	} else {
		//待查询的图层。QueryLayerParam 数组。
		queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = queryLayerName;
		queryLayer.sqlParam = sqlParam;
		queryParam.queryLayerParams.push(queryLayer);
	}
	if (pageNo) {
		queryParam.startRecord = pageNo * 10;
	} else {
		pageNo = 0;
	}
	queryParam.expectCount = 10;
	var customParam = "[pageNo]" + pageNo + "[/pageNo]";
	//属性查询前，先清除高亮
	try{mapControl.clearHighlight();}catch(e){alert(e.Message);}
	clearCustomLayer();
	mapControl.getMap().queryBySql(queryParam, highlightSQL, onBankQueryComplete2, onError, customParam);
}

//网点属性查询回调函数
function onBankQueryComplete(resultSet) {
	var recordSetCount;
	//var point2D = new Object();
	//判断地图比例尺，如果为网点不可见，则设置比例尺
	var scale = mapControl.get_mapScale();
	var layerName=document.getElementById("bankLayoutSelect").value.split("::")[0] ;
	if (scale < getMinScale(layerName)) {
		mapControl.set_mapScale(getMinScale(layerName));
	}
	
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		strTableHTML = "";
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			strTR = "<tr>";
			if (recordSet.returnFieldCaptions) {
				for (var j = 0; j < recordSet.returnFieldCaptions.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'>" + recordSet.returnFieldCaptions[j] + "</td>";
					}
				}
			} else {
				for (var j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if (isNumber(fieldValue)) {
						fieldValue = parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'>" + fieldValue + "</td>";
					}
				}
			}
			strTR += "</tr>";
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			strTableHTML += "<table width='100%'><tr><td align='left'><b>查询结果: (" + recordCount + ")<b></td>";
			strTableHTML +="<td align='right'><img src='../../images/supGis/del.gif' onclick='divClose2()'/></td></tr></table>" ;
			strTableHTML += "<table cellspacing='1' cellpadding='3'>" + strTR;
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				//获取查询的第一个网点，平移到查询点的坐标位置去
				if(j==recordCount-1){
					pointMove(record.center.x,record.center.y);
				}
				strTR = "<tr>";
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"||nameFiledValue[k]=="C_BANK_NAME"){
						strTR += "<td>";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							if (_GetBrowser() != "ie") {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							} else {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							}
						}
						var fieldValue = record.fieldValues[k];
						if (isNumber(fieldValue)) {
							fieldValue = parseFloat(fieldValue);
							fieldValue = parseInt(fieldValue * 100 + "") / 100;
						}
						strTR += fieldValue;
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							strTR += "</u></font>";
						}
						strTR += "</td>";
					}
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			strTableHTML += "</table><br>";
		}
		if (resultSet.totalCount > resultSet.currentCount) {
			var tempStr = "";
			tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
			var pageCount = 0;
			var pageNo = 0;
			if (resultSet.customResponse) {
				try {
					eval(resultSet.customResponse);
				}
				catch (e) {
					alert(e);
				}
			}
			if (mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
				pageCount = 10;
			} else {
				pageCount = 10;
			}
			var totalPage = parseInt((resultSet.totalCount - 1) / pageCount + 1);
			if ((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction") || (mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			} else {
					// queryBySQL
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					tempStr += "<br>";
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(0);'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			}
			strTableHTML += tempStr;
		}
		if (strTableHTML != "") {
			strCloseHTML = "<body onunload = 'clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + "";
			//var aWin;
			//aWin = window.open("", page_res["queryResult"], "");
			//aWin.document.write(strTableHTML);
			//aWin.document.close();
			//aWin.focus();
			var myDiv = document.createElement("div");//创建div
			myDiv.id="newDivId2" ;
			myDiv.style.opacity = "0.5";
			myDiv.style.filter = "alpha(opacity=70)";
			
			myDiv.innerHTML ="<div  class='superMapDiv'>"+strTableHTML+"</div>" ;
			document.body.appendChild(myDiv);
			
		} else {
			alert(page_res["queryResultNull"]);
		}
	} else {
		alert(page_res["queryResultNull"]);
	}
}



//===================网点属性查询 end====================

//=========================移动显示网点信息功能 start=====================================
//判断是否鼠标停留2秒的函数,该函数已经取消
function judgeStop(e) {
	var point2D = new Object();
	var a = parseInt(new Date().getTime() - occurtime);
	var orgfloor=document.getElementById("orgfloor").value ;
	if (a >= timeout && e != null) {
		var scale = mapControl.get_mapScale();
  	//当鼠标在地图停留超过2秒，开始获得当前鼠标的坐标，并获取离该坐标最近网点位置，求出于该距离的位置。此处比列尺数据根据测试得来
		if (scale >= getMinScale(page_res["bankLayerName"])) {
			mouse_x = e.clientX + document.body.scrollLeft;
			mouse_y = e.clientY + document.body.scrollTop;
			point2D.x = e.mapCoord.x;
			point2D.y = e.mapCoord.y;
		
		//最近地物查找
			var layerName = page_res["bankLayerName"];
			var queryParam = new SuperMap.QueryParam();
			var sqlParam = new SuperMap.SqlParam();
			sqlParam.whereClause =" C_ORGFLOOR like '%"+orgfloor+"%'" ;
   		//var maxDistance = document.getElementById("maxDistance").value;
			var tolerance = 0.0002;
			var highlightGetNearest = true;//高亮显示
			if (!layerName) {
				queryParam.queryLayerParams = null;
			} else {
			//待查询的图层。QueryLayerParam 数组。
				queryParam.queryLayerParams = new Array();
				var queryLayer = new SuperMap.QueryLayerParam();
				queryLayer.name = layerName;
				queryLayer.sqlParam = sqlParam;
				queryParam.queryLayerParams.push(queryLayer);
			}
			queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
			queryParam.expectCount = 10;
			
			//如果停留2秒，则清除高亮
			try{mapControl.clearHighlight();}catch(e){alert(e.Message);}
			clearCustomLayer();
			
			mapControl.getMap().findNearest(point2D, tolerance, queryParam, highlightGetNearest, getQueryNearCompete, onError, null, null);
		}
		occurtime = new Date().getTime();
	}
}

//当鼠标进入地图层
function onMapMouseOver(oTD) {
	//id = window.setInterval("judgeStop(e1)", 2000);
	//deleteDivId=window.setInterval("deleteDiv(e1)", 1000);
	//alert("进入地图层:"+id);
}

//删除DIV的方法,该函数已经取消
function deleteDiv(e){
	 //先删除之前的弹出的DIV
	if (document.getElementById("newDivId") != null) {
		var a = parseInt(new Date().getTime() - occurtime);
		if (a > 1000) {
			document.body.removeChild(document.getElementById("newDivId"));
		}
	}
}

//当鼠标移出地图层,取消自动执行函数,该函数取消
function onMapMouseOut(oTD) {
	//window.clearInterval(id);
	//window.clearInterval(deleteDivId);
}

 //鼠标在地图层移动触发的事件,该函数已取消
function onMapMoving(e) {
	//occurtime = new Date().getTime();
	//e1 = e;
}

//该函数已经取消,该函数已经取消
function getQueryNearCompete(resultSet) {
	var recordSetCount;
	var point2D = new Object();
	if (resultSet && resultSet.recordSets) {
		var newDiv = document.createElement("div");//创建div
		newDiv.id = "newDivId";
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		//显示网点属性
		var bankId = "C_BANK_ID";
		var bankName = "C_BANK_NAME";
		var bankaddress = "C_BANK_ADDRESS";
		var bankIdValue;
		var bankNameValue;
		var bankaddressValue;
				
		//newDiv.style.visibility ="visible";//hidden
		newDiv.style.opacity = "0.5";
		newDiv.style.filter = "alpha(opacity=70)";
		for (i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			
			//首先判断网点的三个字段有无取别名，如果又别名就取别名，否则就默认取字段名
			if (recordSet.returnFieldCaptions) {
				for (j = 0; j < recordSet.returnFieldCaptions.length; j++) {
					if (recordSet.returnFields[j] == "C_BANK_ID") {
						bankId = recordSet.returnFieldCaptions[j];
					}
					if (recordSet.returnFields[j] == "C_BANK_NAME") {
						bankName = recordSet.returnFieldCaptions[j];
					}
					if (recordSet.returnFields[j] == "C_BANK_ADDRESS") {
						bankaddress = recordSet.returnFieldCaptions[j];
					}
				}
			}
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			for (j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				newDiv.style.top=record.center.x ;
				newDiv.style.left=record.center.y ;
				for (k = 0; k < record.fieldValues.length; k++) {
					if (recordSet.returnFields[k] == "C_BANK_ID") {
						bankIdValue = record.fieldValues[k];
					}
					if (recordSet.returnFields[k] == "C_BANK_NAME") {
						bankNameValue = record.fieldValues[k];
					}
					if (recordSet.returnFields[k] == "C_BANK_ADDRESS") {
						bankaddressValue = record.fieldValues[k];
					}
				}
			}
			
			//给newDiv的Id赋值
			/*
			if(bankIdValue!=null && bankIdValue.length>0){
				newDiv.id=bankIdValue ;
				this.divObj=bankIdValue ;
			}else{
				//newDiv.id="bankId" ;
				this.divObj="bankId"  ;
			}#CCCCCC
			*/
			newDiv.innerHTML =
			"<div  class='superShowDiv'  style='position:absolute;left:"+mouse_x+" ;top:"+mouse_y+"; height:120px;width:250px;border:gray 2px solid;fontSize:12px;lineHeight:25px;padding:5px;zIndex:100;opacity:0.5;filter:alpha(opacity=70)'>"+
			"<table cellspacing='1'  cellpadding='3'>" + "<tr><td><font color='red'>" + bankId +
			 "</font></td><td>" + bankIdValue + "</td><td><img src='../../images/supGis/del.gif' onclick='divClose("+newDiv.id+")'></td></tr>" + "<tr><td><font color='red'>" 
			 + bankName + "</font></td><td colspan='2'>" + bankNameValue + "</td></tr>" + 
			 "<tr><td><font color='red'>" + bankaddress + "</font></td><td colspan='2'>" + bankaddressValue +
			  "</td></tr>" + 
			  "<tr><td><input type='button' value='查看详细信息' size='3' onClick='showDetail("+bankIdValue+")'></td><td><input type='button' value='查看业务信息' size='3'></td><tr>"
			  "</table></div>";
			newDiv.style.display = "block";
			document.body.appendChild(newDiv);
		}
	}
}

//=========================移动显示网点信息功能 end=====================================


//当地图的缩放时候，控制地图上的高亮和marker add by lyang 2009-11-24
function onMapResize(){
	var mapscale=mapControl.mapScale ;
	var objLayers=mapControl.getMap().get_layers()  ;
	var layer=null ;
	//遍历图层，获得Bank图层
	if(objLayers!=null && objLayers[0]!=null &&objLayers[0].subLayers!=null && objLayers[0].subLayers.length!=null){
		for(i = 0; i < objLayers[0].subLayers.length; i++){
				strName = objLayers[0].subLayers[i].name;
				if(strName.indexOf(page_res["bankLayerName"]) >=0){
					layer=objLayers[0].subLayers[i] ;
	             }
		}
	}
	if(layer!=null){
		var layerMinScale=layer.minScale  ;
		var mapScale=mapControl.get_mapScale();
		if(mapScale<layerMinScale){
			mapControl.customLayer.clearMarkers();
			mapControl.clearHighlight();
		}
		if(mapScale>=layerMinScale){
			var orgfloor=document.getElementById("orgfloor").value ;
			if(orgfloor!=null && orgfloor!=""){
				//layerFilter(orgfloor);
			}
		}
	}
}

//求图层的最小比例尺
function getMinScale(layerName){
	var mapscale=mapControl.mapScale ;
	var objLayers=mapControl.getMap().get_layers()  ;
	var layer=null ;
	//遍历图层，获得Bank图层
	if(objLayers!=null && objLayers[0]!=null &&objLayers[0].subLayers!=null && objLayers[0].subLayers.length!=null){
		for(i = 0; i < objLayers[0].subLayers.length; i++){
				strName = objLayers[0].subLayers[i].name;
				if(strName.indexOf(layerName) >=0){
					layer=objLayers[0].subLayers[i] ;
	             }
		}
	}
	return layer.minScale ;
}

//==========================最新方法=================================

//当地图可视范围发生变化 触发的事件
function viewBoundsChanged(){
	if(tmapScale==null){
		tmapScale=mapControl.get_mapScale();
	}
	if(tmapRect==null){
			tmapRect=mapControl.getViewBounds();
	}
	var mapScale=mapControl.get_mapScale() ;
	var mapRect=mapControl.getViewBounds();
	//现在的比例尺大于图层最小可见比例尺
	if(mapScale>=getMinScale(page_res["bankLayerName"])){
		//如果上一次的比例尺也大于最小比例尺
		if(tmapScale>=getMinScale(page_res["bankLayerName"])){
			if(tmapScale==mapScale){
				//假如两者比例尺相等，则先判断平移坐标位置是否，重新添加MARKER	
				if(judgeFresh()){
					var doubleRect=getDoubleRect();
					queryRect(doubleRect);
					tmapRect=mapRect ;
				}	
			}else{
				//否则都需要重新添加MARKER
				var doubleRect=getDoubleRect();
				queryRect(doubleRect);
				tmapRect=mapRect ;
				tmapScale=mapScale ;
				tmapRect=mapRect ;
			}
		}else{
			var doubleRect=getDoubleRect();
			queryRect(doubleRect);
			tmapRect=mapRect ;
			tmapScale=mapScale ;
			//如果上一次的比列尺不大于最小比例尺.
		}
	}else{
		if(tmapScale>=getMinScale(page_res["bankLayerName"])){
			tmapRect=mapRect ;
			tmapScale=mapScale ;
		}
	}
}

function judgeFresh(){
	var isfresh=false ;
	var mapRect=mapControl.getViewBounds();
	//在比例尺不变的情况下，只有中心点位置变了才刷新marker
	if(mapRect.center()!=tmapRect.center()){
		var rowCoord=mapRect.center().x ;
		var colCoord=mapRect.center().y ;
		
		var maxRow=tmapRect.center().x+parseFloat(tmapRect.width()) ;
		var minRow=tmapRect.center().x-parseFloat(tmapRect.width()) ;
		
		var maxCol=tmapRect.center().y+parseFloat(tmapRect.height())
		var minCol=tmapRect.center().y-parseFloat(tmapRect.height())
		
		if(rowCoord>maxRow || rowCoord<minRow || colCoord>maxCol || colCoord<minCol ){
			isfresh=true ;
		}
	}
	return isfresh ;
}


function getDoubleRect(){
	var mapRect=mapControl.getViewBounds();
	var mapCenter=mapRect.center();
	var mapHeight=mapRect.height();
	var mapWidth=mapRect.width();
		
	var orgfloor=document.getElementById("orgfloor").value ;
	var newRect=new SuperMap.Rect2D() ;
	newRect.copy(mapRect);
	
	var leftbottom=newRect.leftBottom ;
	leftbottom.x=mapRect.leftBottom.x-(mapWidth);
	leftbottom.y=mapRect.leftBottom.y-(mapHeight);
	
	var rightTop=newRect.rightTop ;
	rightTop.x=mapRect.rightTop.x+(mapWidth);
	rightTop.y=mapRect.rightTop.y+(mapHeight);
	
	return newRect ;	
}


//利用矩形查询的方法
function queryRect(doubleRect){

	var orgfloor=document.getElementById("orgfloor").value ;
	var sqlClause="C_ORGFLOOR like '%"+orgfloor+"%'" ;
	var queryLayerName = page_res["bankLayerName"] ;
	var highlightSQL = false ;
	var queryParam = new SuperMap.QueryParam();
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = sqlClause;
	var customParam = "[pageNo]" + 0 + "[/pageNo]";
	
	if(!queryLayerName){
		queryParam.queryLayerParams = null;
	}else{
		queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = queryLayerName;
		queryLayer.sqlParam = sqlParam;
		queryParam.queryLayerParams.push(queryLayer);
	}
	//查询前先清除高亮清除MARKER
	mapControl.clearHighlight();
	mapControl.customLayer.clearMarkers();
	mapControl.getMap().queryByRect(doubleRect,queryParam,highlightSQL,queryRectCompete,onError,customParam);
}


//利用矩形查询的回调方法，此处扩展MAP
function queryRectCompete(resultSet) {
	var bankids="";
	var smx="" ;
	var smy="" ;
	var recordSetCount;
	//判断地图比例尺，如果为网点不可见，则设置比例尺
	var scale = mapControl.get_mapScale();
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"){
						var fieldValue = record.fieldValues[k];
						bankids=bankids+","+fieldValue ;
					}
					if(nameFiledValue[k]=="SMX"){
						var fieldValue = record.fieldValues[k];
						smx=smx+","+fieldValue ;			
					}
					if(nameFiledValue[k]=="SMY"){
						var fieldValue = record.fieldValues[k];
						smy=smy+","+fieldValue ;			
					}
				}
			}
		}
	}
	var orgfloor=document.getElementById("orgfloor").value ;
	//把通过矩形查出的网点id,通过bankid传给后台扩展MAP
	SuperMap.Committer.commitMapCmd('grgbankingGis','showstyle', ["bankids","orgfloor","smx","smy"], [bankids.substring(1),orgfloor,smx.substring(1),smy.substring(1)],showStyleCompete);
}


function showStyleCompete(message){
//message格式x,y,bankid,bankName,bankAddress,status,type|,x,y,bankid,status.....
if(message!=null)
  {
    var messages=message.split("|");
	for(var i=0 ;i<messages.length ;i++){
		var desc=messages[i].split(",");
		//假如网点状态不为空
		if(desc[6]!=null && desc[6]=="0"){
			if(desc[5]!=null&&desc[5]!=""&&desc[5]!="undefined"){
				if(desc[5]=="0"){
					var markerContent = new SuperMap.MarkerContent();
					markerContent.imageSrc = '../../images/supGis/status_disfrock.bmp' ;//网点撤销
					markerContent.imageHeight=25 ;
					markerContent.imageWidth=25 ;
					var innerHTML="<table ><tr><td>网点行号:</td><td  colspan='3'>"+desc[2]+"</td><tr>"
					+"<tr><td>网点名称:</td><td colspan='3'>"+desc[3]+"</td></tr><tr><td>网点地址:</td><td colspan='3'>"+desc[4]+"</td></tr>"
					+"<tr><td><input type='button' value='基本信息' size='3' onClick='showDetail("+desc[2]+")'></td><td><input type='button' value='业务信息' size='3' onClick='showFundevDetail("+desc[2]+")'></td>"
					+"<td><input type='button' value='人员情况' size='3' onClick='showPersonDetail("+desc[2]+")'></td><td><input type='button' value='功能分区' size='3' onClick='showFunareaDetail("+desc[2]+")'></td>"
					+"</tr></table>";
					mapControl.customLayer.addMarker("bank_"+desc[2],desc[0],desc[1],0,0,innerHTML,"",0,markerContent);
				}
				if(desc[5]=="1"){
					var markerContent = new SuperMap.MarkerContent();
					markerContent.imageSrc = '../../images/supGis/status_natural.bmp' ;//正常
					markerContent.imageHeight=25 ;
					markerContent.imageWidth=25 ;
					var innerHTML="<table ><tr><td>网点行号:</td><td width='50%' colspan='3'>"+desc[2]+"</td><tr>"
					+"<tr><td>网点名称:</td><td colspan='3'>"+desc[3]+"</td></tr><tr><td>网点地址:</td><td colspan='3'>"+desc[4]+"</td></tr>"
					+"<tr><td><input type='button' value='基本信息' size='3' onClick='showDetail("+desc[2]+")'></td><td><input type='button' value='业务信息' size='3' onClick='showFundevDetail("+desc[2]+")'></td>"
					+"<td><input type='button' value='人员情况' size='3' onClick='showPersonDetail("+desc[2]+")'></td><td><input type='button' value='功能分区' size='3' onClick='showFunareaDetail("+desc[2]+")'></td>"
					+"</tr></table>";
					mapControl.customLayer.addMarker("bank_"+desc[2],desc[0],desc[1],0,0,innerHTML,"",0,markerContent);
				}
				if(desc[5]=="2"){
					var markerContent = new SuperMap.MarkerContent();
					markerContent.imageSrc = '../../images/supGis/status_cause.bmp' ;//临时停业
					markerContent.imageHeight=25 ;
					markerContent.imageWidth=25 ;
					var innerHTML="<table ><tr><td'>网点行号:</td><td colspan='3'>"+desc[2]+"</td><tr>"
					+"<tr><td>网点名称:</td><td colspan='3'>"+desc[3]+"</td></tr><tr><td>网点地址:</td><td colspan='3'>"+desc[4]+"</td></tr>"
					+"<tr><td><input type='button' value='基本信息' size='3' onClick='showDetail("+desc[2]+")'></td><td><input type='button' value='业务信息' size='3' onClick='showFundevDetail("+desc[2]+")'></td>"
					+"<td><input type='button' value='人员情况' size='3' onClick='showPersonDetail("+desc[2]+")'></td><td><input type='button' value='功能分区' size='3' onClick='showFunareaDetail("+desc[2]+")'></td>";
					+"</tr></table>";
					mapControl.customLayer.addMarker("bank_"+desc[2],desc[0],desc[1],0,0,innerHTML,"",0,markerContent);
				}
			}
		}
		if(desc[6]!=null && desc[6]=="1"){
			var markerContent = new SuperMap.MarkerContent();
			markerContent.imageSrc = '../../images/supGis/status_4.bmp' ;//网点选址
			markerContent.imageHeight=25 ;
			markerContent.imageWidth=25 ;
			var innerHTML="<table><tr><td>需求点序号:</td><td colspan='2'>"+desc[2]+"</td><tr>"
			+"<tr><td>需求点名称:</td><td colspan='2'>"+desc[3]+"</td></tr><tr><td>选址地址:</td><td>"+desc[4]+"</td></tr>"+
			"<tr><td><input type='button' value='基本信息' onclick='showSeladdInfoDetail(\""+desc[2]+"\")'></td>"
			+"<td><input type='button' value='竞争对手' onclick='showSeladdAdversaryDetail(\""+desc[2]+"\")'></td><td><input type='button' value='周边环境' onclick='showSeladdEnvironmentDetail(\""+desc[2]+"\")'></td></table>";
			mapControl.customLayer.addMarker("bank_"+desc[2],desc[0],desc[1],0,0,innerHTML,"",0,markerContent);
		}
		if(desc[6]!=null && desc[6]=="2"){
			var markerContent = new SuperMap.MarkerContent();
			markerContent.imageSrc = '../../images/supGis/status_black.bmp' ;//不存在
			markerContent.imageHeight=25 ;
			markerContent.imageWidth=25 ;
			var innerHTML="<table ><tr><td width='50%'>编号:</td><td width='50%'>"+desc[2]+"</td><tr>"
			+"<tr><td colspan='2' align='center'><font color='red'>此网点不存在,请核对</font></td></tr>"
			+"</table>";
			mapControl.customLayer.addMarker("bank_"+desc[2],desc[0],desc[1],0,0,innerHTML,"",0,markerContent);
		}
	}
	mapControl.refreshMapControl();
  }
}

function insertTable(status){
	alert(status);
}


function zhongaddpointMaker() {
  //  var point2D = new SuperMap.Point2D(0,0);
	var markerContent = new SuperMap.MarkerContent();
	var innerHtml = "<div><a>SuperMap</a>到底<input id='userTitle' name='testname' type='text' size='21' value='text'/></div>";
	markerContent.imageSrc="../../images/supGis/status_3.bmp";
	mapControl.customLayer.addMarker("ent", 113.3338503382,23.133127164, 10, 10, innerHtml, "",99,markerContent);

}
//=======================2009-01-07=====================
//初始化查询，查询出所有的网点
function initQuery(){
	var orgfloor=document.getElementById("orgfloor").value ;
	var sqlClause = "1=1 and C_ORGFLOOR like '%"+orgfloor+"%' ";
	
	//图层名
	var queryLayerName =page_res["bankLayerName"];
	//是否高清晰显示
	var highlightSQL = false ;
	//进行地图查询设置条件的类
	var queryParam = new SuperMap.QueryParam();
	//设置查询属性条件的类
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = sqlClause;
	if (!queryLayerName) {
		queryParam.queryLayerParams = null;
	} else {
		//待查询的图层。QueryLayerParam 数组。
		queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = queryLayerName;
		queryLayer.sqlParam = sqlParam;
		queryParam.queryLayerParams.push(queryLayer);
	}
	var pageNo=0 ;
	queryParam.expectCount = 100;
	var customParam = "[pageNo]" + pageNo + "[/pageNo]";
	//属性查询前，先清除高亮
	try{mapControl.clearHighlight();}catch(e){alert(e.Message);}
	clearCustomLayer();
	mapControl.getMap().queryBySql(queryParam, highlightSQL, initQueryCompete, onError, customParam);
}

//初始化查询回调函数
function initQueryCompete(resultSet){
	var recordSetCount;
	
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		strTableHTML = "";
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			strTR = "<tr>";
			if (recordSet.returnFieldCaptions) {
				for (var j = 0; j < recordSet.returnFieldCaptions.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'><b>" + recordSet.returnFieldCaptions[j] + "</b></td>";
					}
				}
			} else {
				for (var j = 0; j < recordSet.returnFields.length; j++) {
					var fieldValue = recordSet.returnFields[j];
					if (isNumber(fieldValue)) {
						fieldValue = parseFloat(fieldValue);
						fieldValue = Math.floor(fieldValue * 100) / 100;
					}
					if(nameFiledValue[j]=="C_BANK_ID"||nameFiledValue[j]=="C_BANK_NAME"){
						strTR += "<td class='title_2' align='center'>" + fieldValue + "</td>";
					}
				}
			}
			strTR += "</tr>";
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			strTableHTML += "<table cellspacing='1' cellpadding='3' border='0' width='100%' >" + strTR;
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				strTR = "<tr>";
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"||nameFiledValue[k]=="C_BANK_NAME"){
						strTR += "<td>";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							if (_GetBrowser() != "ie") {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							} else {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							}
						}
						var fieldValue = record.fieldValues[k];
						if (isNumber(fieldValue)) {
							fieldValue = parseFloat(fieldValue);
							fieldValue = parseInt(fieldValue * 100 + "") / 100;
						}
						strTR += fieldValue+"&nbsp;";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							strTR += "</u></font>";
						}
						strTR += "</td>";
					}
				}
				strTR += "</tr>";
				strTableHTML += strTR;
			}
			strTableHTML += "</table><br>";
		}
		if (resultSet.totalCount > resultSet.currentCount) {
			var tempStr = "";
			tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
			var pageCount = 0;
			var pageNo = 0;
			if (resultSet.customResponse) {
				try {
					eval(resultSet.customResponse);
				}
				catch (e) {
					alert(e);
				}
			}
			if (mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
				pageCount = 100;
			} else {
				pageCount = 10;
			}
			var totalPage = parseInt((resultSet.totalCount - 1) / pageCount + 1);
			if ((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction") || (mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl._queryAction.type=\"using\";window.opener.mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			} else {
					// queryBySQL
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					tempStr += "<br>";
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.bankLayoutQuery(0);'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.bankLayoutQuery(" + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.bankLayoutQuery(" + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='window.opener.mapControl.get_action()._needHighlight = false;window.opener.bankLayoutQuery(" + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			}
			strTableHTML += tempStr;
		}
		if (strTableHTML != "") {
			strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + "";
			
			var myDiv=document.getElementById("resultDiv");
			myDiv.innerHTML =strTableHTML ;
		} else {
			alert(page_res["queryResultNull"]);
		}
	} else {
		alert(page_res["queryResultNull"]);
	}	
}


//=======================2009-01-08=====================
//对齐边框
function borderline(){
	var menuDivHeight=document.getElementById("menuDiv").offsetHeight;//获得查询按钮栏的高度
	var mapleftDiv=document.getElementById("mapleft");
	if(mapleftDiv!=null){
		mapleftDiv.style.height=menuDivHeight ;//重新设置查询菜单左边边框的高度
	}
}


//网点属性查询回调函数2
function onBankQueryComplete2(resultSet) {
	var recordSetCount;
	//var point2D = new Object();
	//判断地图比例尺，如果为网点不可见，则设置比例尺
	var scale = mapControl.get_mapScale();
	var layerName=document.getElementById("bankLayoutSelect").value.split("::")[0] ;
	if (scale < getMinScale(layerName)) {
		mapControl.set_mapScale(getMinScale(layerName));
	}
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		var strTR2 ;
		strTableHTML = "";
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				//获取查询的第一个网点，平移到查询点的坐标位置去
				if(j==recordCount-1){
					//pointMove(record.center.x,record.center.y);
				}
				strTR = "<tr id='"+j+"' class='trClass"+(j%2)+"' oriClass='' onMouseOut='TrMove(this)' onMouseOver='TrMove(this)'>";
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"||nameFiledValue[k]=="C_BANK_NAME"){
						strTR += "<td align='center'>";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							if (_GetBrowser() != "ie") {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							} else {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							}
						}
						var fieldValue = record.fieldValues[k];
						if (isNumber(fieldValue)) {
							fieldValue = parseFloat(fieldValue);
							fieldValue = parseInt(fieldValue * 100 + "") / 100;
						}
						strTR += fieldValue;
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							strTR += "</u></font>";
						}
						strTR += "</td>";
					}
				}
				strTR += "</tr>";
				
				strTableHTML += strTR;
			}
			
			//如果当前返回的记录条数少于10条
			if(recordCount<10){
				for(var k=0 ;k<10-recordCount ;k++){
					strTR2 = "<tr  class='trClass"+(k%2)+"' oriClass='' onMouseOut=TrMove(this) onMouseOver=TrMove(this)><td>&nbsp;</td><td>&nbsp;</td></tr>";
					strTableHTML +=strTR2 ;
				}
			}
		}
		if (resultSet.totalCount > resultSet.currentCount) {
			var tempStr="" ;
			tempStr += "<tr class='trClass1' oriClass='' ><td colspan='2' align='right'>" ;
			//tempStr = page_res["tempStr1"] + resultSet.totalCount + page_res["tempStr2"] + resultSet.currentCount + page_res["tempStr3"] + "<br>";
			var pageCount = 0;
			var pageNo = 0;
			if (resultSet.customResponse) {
				try {
					eval(resultSet.customResponse);
				}
				catch (e) {
					alert(e);
				}
			}
			if (mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
				pageCount = 10;
			} else {
				pageCount = 10;
			}
			var totalPage = parseInt((resultSet.totalCount - 1) / pageCount + 1);
			if ((mapControl.get_action().type.indexOf("Query") != -1 && mapControl.get_action().type !== "PointQueryAction") || (mapControl._queryAction != null && mapControl._queryAction.type != "unused" && mapControl._queryAction._type.indexOf("Query") != -1 && mapControl._queryAction.type !== "PointQueryAction")) {
				if (totalPage > 0) {
					tempStr = page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					if (pageNo > 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (0) + ");'><u>\u9996\u9875</u></font>";
					}
					if (pageNo != 0) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (pageNo - 1) + ");'><u>\u4e0a\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 2) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (pageNo + 1) + ");'><u>\u4e0b\u4e00\u9875</u></font>";
					}
					if (pageNo < totalPage - 1) {
						tempStr += "&nbsp;&nbsp;<font color='blue' style='cursor:hand;' onclick='mapControl._queryAction.type=\"using\";mapControl._queryAction.query(" + pageCount + ", " + (totalPage - 1) + ");'><u>\u672b\u9875</u></font>";
					}
				}
			} else {
					// queryBySQL
				if (totalPage > 0) {
					//tempStr += page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					tempStr =tempStr+"共计:"+resultSet.totalCount +"条记录"+totalPage+"页,当前第"+(pageNo+1)+"页";
					tempStr += "<br>";
					var pageNoValue=document.getElementById("pageNoValue").value ;
					if(pageNo>0 && pageNo<(totalPage-1)){
						tempStr =tempStr+"<img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(0);'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					if (pageNo == 0) {
						tempStr =tempStr+"<img alt='首页' src='../../images/page/unfirst.gif'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/unforward.gif'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					
					if (pageNo == totalPage - 1) {
						tempStr =tempStr+"<img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(0);'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/unbackward.gif'>&nbsp;"
						+"<img alt='末页' src='../../images/page/unlast.gif'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					tempStr += "</td></tr>"
				}
			}
			strTableHTML += tempStr ;
		}else{
			var tempStr="" ;
			tempStr += "<tr class='trClass1' oriClass='' ><td colspan='2' align='right'>" ;
			tempStr =tempStr+"共计:"+resultSet.totalCount +"条记录1页,当前第1页";
			tempStr =tempStr+"<br><img alt='首页' src='../../images/page/unfirst.gif'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/unforward.gif'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/unbackward.gif'>&nbsp;"
						+"<img alt='末页' src='../../images/page/unlast.gif'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' >&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='1'>页" ;
			tempStr += "</td></tr>" ;	
			strTableHTML += tempStr ;		
		}
		
		if (strTableHTML != "") {
			//strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			//strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + "";
			strCloseHTML="<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#000066' id='downloadList'>"
			strCloseHTML=strCloseHTML+"<tr height='15px'><td align='center' class='oracolumncenterheader'>网点行号"+	
				"<td  align='center' class='oracolumncenterheader'>网点名称</td></tr>"
			strTableHTML=strCloseHTML+strTableHTML+"</table>"
			var myDiv =document.getElementById("resultDiv");//获得查询结果中的Div
			
			myDiv.innerHTML =strTableHTML  ;
		} else {
			alert(page_res["queryResultNull"]);
		}
	} else {
		alert(page_res["queryResultNull"]);
	}
}

function goTo(){
	var pageNoValue=document.getElementById("pageNoValue").value;
	mapControl.get_action()._needHighlight = false;
	bankLayoutQuery(pageNoValue-1);
}

//网点空间查询回调函数2
function onSelectTotalQueryComplete2(resultSet,context) {
	var recordSetCount;
	//var point2D = new Object();
	//判断地图比例尺，如果为网点不可见，则设置比例尺
	var scale = mapControl.get_mapScale();
	var layerName=document.getElementById("bankLayoutSelect").value.split("::")[0] ;
	if (scale < getMinScale(layerName)) {
		mapControl.set_mapScale(getMinScale(layerName));
	}
	if (resultSet && resultSet.recordSets) {
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		var strTR2 ;
		var bankIds ;
		strTableHTML = "";
		
		for (var i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			var nameFiledValue = recordSet.returnFields ;
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			for (var j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				//获取查询的第一个网点，平移到查询点的坐标位置去
				if(j==recordCount-1){
					//pointMove(record.center.x,record.center.y);
				}
				strTR = "<tr id='"+j+"' class='trClass"+(j%2)+"' oriClass='' onMouseOut='TrMove(this)' onMouseOver='TrMove(this)'>";
				for (var k = 0; k < record.fieldValues.length; k++) {
					//过滤其他数值信息
					if(nameFiledValue[k]=="C_BANK_ID"||nameFiledValue[k]=="C_BANK_NAME"){
						strTR += "<td align='center' width='50%'>";
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							if (_GetBrowser() != "ie") {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new window.opener.SuperMap.Point2D(" + record.center.x + "," + record.center.y + ");window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							} else {
								//strTR += "<font color='blue' style='cursor:hand;' onclick='var point2D = new Object();point2D.x=" + record.center.x + ";point2D.y=" + record.center.y + ";window.opener.mapControl.panToMapCoord(point2D);window.opener.clearCustomLayer();window.opener.insertTag(point2D);'><u>";
								strTR += "<font color='blue' style='cursor:hand;' onclick='pointMove("+record.center.x+","+record.center.y+");'><u>";
							}
							//把c_bank_id组合成字符串
							bankIds =bankIds+","+record.fieldValues[k];
						}
						var fieldValue = record.fieldValues[k];
						if (isNumber(fieldValue)) {
							fieldValue = parseFloat(fieldValue);
							fieldValue = parseInt(fieldValue * 100 + "") / 100;
						}
						strTR += fieldValue;
						if (recordSet.returnFields[k].toLowerCase() == "c_bank_id" && record.center != null) {
							strTR += "</u></font>";
						}
						strTR += "</td>";
					}
				}
				strTR += "</tr>";
				bankIds=bankIds.substring(1);
				strTableHTML += strTR;
			}
			
			//如果当前返回的记录条数少于10条
			if(recordCount<10){
				for(var k=0 ;k<10-recordCount ;k++){
					strTR2 = "<tr  class='trClass"+(k%2)+"' oriClass='' onMouseOut=TrMove(this) onMouseOver=TrMove(this)><td>&nbsp;</td><td>&nbsp;</td></tr>";
					strTableHTML +=strTR2 ;
				}
			}
		}
		if (resultSet.totalCount > resultSet.currentCount) {
			var tempStr="" ;
			tempStr += "<tr class='trClass1' oriClass='' ><td colspan='2' align='right'>" ;
			var pageCount = 0;
			var pageNo = 0;
			
			if (resultSet.customResponse) {
				try {
					eval(resultSet.customResponse);
				}
				catch (e) {
					alert(e);
				}
			}
			//如果回调函数的操作命令里没有对pageNo的操作,则看用户上下文
			if(context!=null){
				pageNo=context ;
			}
			if (mapControl.get_action().type.indexOf("Query") == -1 && mapControl.get_action().type != "PointQueryAction" && (mapControl._queryAction == null || mapControl._queryAction.type == "unused")) {
				pageCount = 10;
			} else {
				pageCount = 10;
			}
			var totalPage = parseInt((resultSet.totalCount - 1) / pageCount + 1);
			var pageNoValue=document.getElementById("pageNoValue").value ;
			if ((mapControl.get_action().type=="PointQueryAction") ||(mapControl.get_action().type=="CircleQueryAction")||(mapControl.get_action().type=="RectQueryAction")) {
				if (totalPage > 0) {
					tempStr =tempStr+"共计:"+resultSet.totalCount +"条记录"+totalPage+"页,当前第"+(pageNo+1)+"页";
					tempStr +="<br>";
					if(pageNo>0 && pageNo<(totalPage-1)){
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (0) + ");'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					if (pageNo == 0) {
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/unfirst.gif'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/unforward.gif'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					
					if (pageNo == totalPage - 1) {
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (0) + ");'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;mapControl.get_action().query(" + pageCount + ", " + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/unbackward.gif'>&nbsp;"
						+"<img alt='末页' src='../../images/page/unlast.gif'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					tempStr += "</td></tr>"
				}
			} else {
				if (totalPage > 0) {
					//tempStr += page_res["tempStr4"] + totalPage + page_res["tempStr5"] + (pageNo + 1) + page_res["tempStr6"];
					tempStr =tempStr+"共计:"+resultSet.totalCount +"条记录"+totalPage+"页,当前第"+(pageNo+1)+"页";
					tempStr += "<br>";
					if(pageNo>0 && pageNo<(totalPage-1)){
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(0);'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					if (pageNo == 0) {
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/unfirst.gif'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/unforward.gif'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/backward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo + 1) + ");'>&nbsp;"
						+"<img alt='末页' src='../../images/page/last.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (totalPage - 1) + ");'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					
					if (pageNo == totalPage - 1) {
						tempStr =tempStr+"<input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/first.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(0);'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/forward.gif' onclick='mapControl.get_action()._needHighlight = false;bankLayoutQuery(" + (pageNo - 1) + ");'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/unbackward.gif'>&nbsp;"
						+"<img alt='末页' src='../../images/page/unlast.gif'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' onclick='goTo()'>&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='"+pageNoValue+"'>页" ;
					}
					tempStr += "</td></tr>"
				}
			}
			strTableHTML += tempStr ;
		}else{
			var tempStr="" ;
			tempStr += "<tr class='trClass1' oriClass='' ><td colspan='2' align='right'>" ;
			tempStr =tempStr+"共计:"+resultSet.totalCount +"条记录1页,当前第1页";
			tempStr =tempStr+"<br><input type='button' value='统计' onClick='statOperationData(\""+bankIds+"\")'><img alt='首页' src='../../images/page/unfirst.gif'>&nbsp;"
						+"<img alt='上一页' src='../../images/page/unforward.gif'>&nbsp;" 
						+"<img alt='下一页' src='../../images/page/unbackward.gif'>&nbsp;"
						+"<img alt='末页' src='../../images/page/unlast.gif'>&nbsp;"
						+"<img alt='跳转' src='../../images/page/view_search.gif' >&nbsp;"
						+"<input id='pageNoValue' type='text' size='2' value='1'>页" ;
			tempStr += "</td></tr>" ;	
			strTableHTML += tempStr ;		
		}
		
		if (strTableHTML != "") {
			//strCloseHTML = "<body onunload = 'window.opener.clearCustomLayer()'></body>";
			//strTableHTML = "<title>" + page_res["queryResult"] + "</title><link href='styles/queryresult.css' rel='stylesheet'>" + strTableHTML + "" + strCloseHTML + "";
			strCloseHTML="<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#000066' id='downloadList'>"
			strCloseHTML=strCloseHTML+"<tr height='15px'><td align='center' class='oracolumncenterheader'>网点行号"+	
				"<td  align='center' class='oracolumncenterheader'>网点名称</td></tr>"
			strTableHTML=strCloseHTML+strTableHTML+"</table>"
			var myDiv =document.getElementById("resultDiv");//获得查询结果中的Div
			
			myDiv.innerHTML =strTableHTML  ;
		}
		 else {
			alert(page_res["queryResultNull"]);
		}
	} else {
		alert(page_res["queryResultNull"]);
	}
}

