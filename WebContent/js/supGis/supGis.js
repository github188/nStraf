var mapControl;
var overviewControl;
var layerControl;
var navigationControl;
var magnifierControl;
var sliderBarControl;
var params = new Object();

/*
//========================页面初始化========================
function onPageLoad(){

    // 设置SuperMap.Committer.handler和params.contextPath的路径
    SuperMap.Committer.handler = "http://127.0.0.1:8080/phbank/commonhandler";
    
    params.contextPath = "http://127.0.0.1:8080/phbank/";
    params.mapHandler = params.contextPath + "maphandler";
    
    // 指定要加载的地图名称
    params.mapName = "grgbankingGis";
    
    // 指定基础地图服务IP地址    
    params.mapServicesAddress = "10.1.90.12";
    // 指定基础地图服务端口号      
    params.mapServicesPort = 8600;
    
    // 设置返回图片的格式
    params.imageFormat = "png";

    // new一个MapControl对象
    mapControl = new SuperMap.UI.MapControl($get("myMap"));
    mapControl.set_params(params);
    
    // 初始化mapControl
    mapControl.initialize();
};
*/
//========================静态页面初始化========================
function onPageLoad(){
	// 把SuperMap.Committer.handler和params.contextPath都改成本机应用的相应路径
	SuperMap.Resource.ins = new SuperMap.Resource();
	SuperMap.Resource.ins.setLanguage("Chinese");
	 // 设置SuperMap.Committer.handler和params.contextPath的路径
    SuperMap.Committer.handler = "http://10.1.90.14:8080/phbank/commonhandler";
    
    params.contextPath = "http://10.1.90.14:8080/phbank/";
    params.mapHandler = params.contextPath + "maphandler";

    params.mapName = "grgbankingGis";
    params.mapServicesAddress = "10.1.90.12";
    params.mapServicesPort = 8600;
    params.imageFormat = "png";
    params.buffer=1;

	mapControl = new SuperMap.UI.MapControl($get("myMap"));
    mapControl.set_params(params);
    
    mapControl.initialize();
};

function onPageLoaded(){
	setinitMenu();
	setMenu("overviewMenu");
	onMenuMouseOut(document.getElementById("overviewMenuTD"));
	//initOverview();
	//initNavigationControl();
	//initLayerControl();
	//initSliderBarControl();
	mapControl.add_init(initOverview);
	mapControl.add_init(initNavigationControl);
	mapControl.add_init(initLayerControl);
	mapControl.add_init(initSliderBarControl);
	mapControl.add_mouseMove(onDemoMouseMoving);
	mapControl.add_viewChanged(updateMapScaleInput);
	if(mapControl.get_mapName() == "grgbankingGis"){
		mapControl._mapScales = [1/80000, 1/50000, 1/20000, 1/8000, 1/5000, 1/2000, 1/800, 1/500, 1/200];
		mapControl._params.mapScales = [1/80000, 1/50000, 1/20000, 1/8000, 1/5000, 1/2000, 1/800, 1/500, 1/200];
	}
	mapControl.getMap().set_useQuickCache(true);
    mapControl.get_params().outputContextPath="http://10.1.90.12:7080/output/";
	
    mapControl.add_init(initSliderBarControl);
    mapControl.add_init(rerangePage);
    mapControl.add_init(function(){
    		document.body.onresize = rerangePage;
	    }
    );
};

function onComplete(json){
    var rect = new SuperMap.Rect2D();
    SuperMap.Decoder.fromJson(rect, json);
    mapControl.viewByBounds(rect);
};
// 全图显示
function viewEntire(){
    mapControl.viewEntire();
};

// 拉框放大
function setZoomIn(){
    var zoomInAction = new SuperMap.UI.ZoomInAction();
    mapControl.set_action(zoomInAction);
};

// 拉框缩小
function setZoomOut(){
    var zoomOutAction = new SuperMap.UI.ZoomOutAction();
    mapControl.set_action(zoomOutAction);
};

// 平移
function setPan(){
    var panAction = new SuperMap.UI.PanAction();
    mapControl.set_action(panAction);
};
function onMenuMouseOver(oTD){
	if(eval(oTD._expand)){
		oTD.background = "../../images/supGis/leftbuttontitle_open_s.jpg";
	}else{
		oTD.background = "../../images/supGis/leftbuttontitle_close_s.jpg";
	}
	oTD.style.color = "244E88";
};

function onMenuMouseOut(oTD){
	if(eval(oTD._expand)){
		oTD.background = "../../images/supGis/leftbuttontitle_open.jpg";
	}else{
		oTD.background = "../../images/supGis/leftbuttontitle_close.jpg";
	}
	oTD.style.color = "white";
};
//========================页面地图操作========================
//========================页面地图操作========================

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

//显示查询结果的方法
function onQueryComplete(resultSet){
	alert("显示查询结果");
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
};

//========================属性查询=======================
function sqlQuery(pageNo){
	setPan();
	//sql语句
	var sqlClause = document.getElementById('sqlClause').value;
	//var sqlClause="C_BANK_ID='44444'";
	//图层名
	var queryLayerName = document.getElementById('sqlLayersSelect').value;
	//var queryLayerName="Bank@广州市"
	//是否高清晰显示
	var highlightSQL = document.getElementById('highlightSQL').checked;
	//var highlightSQL="false" ;
	
	//进行地图查询设置条件的类
	var queryParam = new SuperMap.QueryParam();
	//设置查询属性条件的类
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = sqlClause;
	if(!queryLayerName){
		queryParam.queryLayerParams = null;
	}else{
		//待查询的图层。QueryLayerParam 数组。
		alert("sql:"+sqlParam.whereClause);
		queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = queryLayerName;
		queryLayer.sqlParam = sqlParam;
		queryParam.queryLayerParams.push(queryLayer);
	}
	if(pageNo) {
		queryParam.startRecord = pageNo * 100;
	} else {
		pageNo = 0;
	}
	queryParam.expectCount = 100;
	var customParam = "[pageNo]" + pageNo + "[/pageNo]";
	
	alert(highlightSQL);
	alert(sqlParam.whereClause);
	mapControl.getMap().queryBySql(queryParam, highlightSQL, onQueryComplete, onError, customParam);
};
//========================属性查询=======================

//点选查询的方法
var queryLayerNames = new Array();
function setPointQuery(){
    var queryAction = getQueryAction("PointQueryAction");
	if (queryAction){
		mapControl.set_action(queryAction);
		queryAction.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
	}
};

function getQueryAction(queryType){
	var queryAction = null;

	queryLayerNames = getQueryLayerNames();

	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = document.getElementById('spatialSqlClause').value;

    switch(queryType){
        case "PointQueryAction":
        	var tolerance = document.getElementById("pointQueryTolerance").value;
			var patrn=/^\s*\d*\s*$/; 
			if (!patrn.exec(tolerance))
			{
				alert("容限必须是数值");
				return false 
			}
	    	queryAction = new SuperMap.UI.PointQueryAction(queryLayerNames, sqlParam, tolerance, true, onQueryComplete, onError);
               break;
        case "CircleQueryAction":
		    queryAction = new SuperMap.UI.CircleQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
        case "LineQueryAction":
		    queryAction = new SuperMap.UI.LineQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
        case "RectQueryAction":
	    	queryAction = new SuperMap.UI.RectQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
        case "PolygonQueryAction":
		    queryAction = new SuperMap.UI.PolygonQueryAction(queryLayerNames, sqlParam, true, onQueryComplete, onError);
               break;
	}

	return queryAction;
};
//========================路径分析========================
//========================最近地物查找=======================
function setFindNearest(){
	var layerName = document.getElementById("findNearestLayersSelect").value;
	var sqlParam = new SuperMap.SqlParam();
   	var maxDistance = document.getElementById("maxDistance").value;
	var highlightFindNearest = document.getElementById("highlightFindNearest").checked;
    var findNearestAction = new SuperMap.UI.FindNearestAction([layerName], sqlParam, maxDistance, highlightFindNearest, onQueryComplete, onError);
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
function onError(responseText) {
	alert("error:"+responseText);
};
function onQueryComplete(resultSet){

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
};
function isNumber(s) {
	return /^-?\d+$/.test(s) || /^-?\d+.\d+$/.test(s) || /^-?\d+.\d+E-?\d+$/.test(s);
};