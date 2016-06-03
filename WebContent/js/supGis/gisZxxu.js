






/**
 * 填充编辑图层下拉框
 */
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
								/**
								* author zxxu, 过滤掉其他图层，只显示Bank和ATM图层
								**/
								if(layerName!="Bank"&&layerName!="ATM"){
									continue;
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

//========================地图编辑========================
var objectxy=new Object();

function setAddEntity(){
	var layerInfos = document.getElementById("editLayersSelect").value.split("::");
	var layerName = layerInfos[0];
	var dataSetType = parseInt(layerInfos[1]);
	
	//alert(layerName+":"+dataSetType);
	//var addEntityAction = new SuperMap.UI.AddEntityAction(layerName, dataSetType, false, addEntityFinish, onError);
	var addEntityAction = new SuperMap.UI.AddEntityAction(layerName, dataSetType, false, onAddEntityComplete, onError);
	mapControl.set_action(addEntityAction);
};

//修改状态为平移
function onAddEntityComplete(result){
	//var msg = "";
	//if(result && result.succeed){
	//	msg += page_res["addEntitySuccess"] + " ID: "+result.ids[0]+"\n";
	//}else{
	//	msg += page_res["addEntityFail"] + "\n";
	//	msg += result?result.message:"";
	//}
	//alert(msg);
	try{
		var varPanAction=new SuperMap.UI.PanAction();
		mapControl.set_action(varPanAction);}
		catch(e){alert(e.Message)};
};

function setDeleteEntity(){
	var layerInfos = document.getElementById("editLayersSelect").value.split("::");
	var layerName = layerInfos[0];
	var dataSetType = parseInt(layerInfos[1]);
	var deleteEntityAction = new SuperMap.UI.DeleteEntityAction(layerName, dataSetType, onDeleteEntityComplete, onError);
	mapControl.set_action(deleteEntityAction);
};

function onDeleteEntityComplete(result){
	var msg = "";
	if(result && result.succeed){
		msg += page_res["deleteEntitySuccess"] + " ID: "+result.ids[0]+"\n";
	}else{
		msg += page_res["deleteEntityFail"] + "\n";
		msg += result?result.message:"";
	}
	alert(msg);
};


//测试增加标注点的弹出窗口操作
function addEntityFinish(result){
	var msg = "";
	if(result && result.succeed){
		window.showModalDialog("a.jsp","dialogWidth=200px;dialogHeight=100px");
	}else{
		msg += page_res["addEntityFail"] + "\n";
		msg += result?result.message:"";
	}
}

function onError(responseText) {
	alert(responseText);
};


function Button2_onclick(e) //先把建立一个新的DIV的方法写成一个通用方法，然后通过调用方法实例化建立DIV
{
var bankLayout_x = e.clientX + document.body.scrollLeft;
var bankLayout_y = e.clientY + document.body.scrollTop;

var newDiv=document.createElement("div");//创建div
newDiv.id = "my";
newDiv.style.position="absolute";//relative
//newDiv.style.backgroundColor="#D0E4F7";
newDiv.style.height="150px";
newDiv.style.width="250px";
//newDiv.style.border="#CCCCCC 4px solid";
newDiv.style.top=bankLayout_y-80 ;
newDiv.style.left=bankLayout_x-120;
newDiv.style.fontSize="12px"
newDiv.style.lineHeight="25px"
newDiv.style.padding="5px"
newDiv.style.zIndex="100"

//newDiv.style.visibility ="visible";//hidden
newDiv.style.opacity="0.5"; 
newDiv.style.filter="alpha(opacity=70)" 
newDiv.innerHTML="<div class='superShowDiv' style='border:#CCCCCC 2px solid'><table cellspacing='1' cellpadding='1'  width='100%'>"+
"<tr><td align='left' width='90%'><b>输入网点(选址需求点)信息:</b></td><td align='right' width=''30%><img src='../../images/supGis/del.gif' onclick='document.body.removeChild(document.getElementById(\"my\"));'></td></tr></table>"+
"<table  cellspacing='1' cellpadding='1'  width='100%'><tr><td><font class='superMapfont'>网点ID/序列号：</font></td><td><input type='text' id='addBankId' size='15' value=''></td></tr>"+
"<tr><td><font class='superMapfont'>类型：</font></td><td>网点<input type='radio' name='type' value='0' checked='checked'>"+
"&nbsp;&nbsp;选址需求点<input type='radio' name='type' value='1'>"+
"</td><tr></table><br>"+
"<div align='center'><input type='button' id='diwutijiao' value='提交' onclick='cancel();'>"+
"&nbsp;&nbsp;&nbsp;<input type='button' id='del' value='关闭' onclick='document.body.removeChild(document.getElementById(\"my\"))'></div></div>";

document.body.appendChild(newDiv);//把创建好的div插入到body节点后，否则newDiv会处于游离状态，无法显示；
//alert(newDiv.innerHTML);
//newDiv.appendChild(newA);
}
function Button3_onclick(){
	var layerInfos = document.getElementById("editLayersSelect").value.split("::");
	var queryLayerName = layerInfos[0];
	var url ="addBank_gis.jsp?orgfloor=0&bankId=&bankName=&bankAddress=";
	window.open(url,'增加页面','height=400,width=400,top=300,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	//window.showModalDialog(url);
}

function cancel()
{ 	
	//mapControl.get_action().onClick(objectxy);
	//var bool =sqlQuery();
		mapControl.get_action().onClick(objectxy)
	//if(bool=="1"){
		//alert("该网点行号已被使用，不能再增加");
		//document.body.removeChild(document.getElementById("my"));
	//}
	//else{
		//alert("增加成功");
		//此处先从phbank的网点基本信息表中查询出输入网点的基本信息
		//if(changeDa()){//当查询数据完成，然后进行数据增加操作
			
		//}
		
	//}
}







function sqlQuery(pageNo){
	pan();
	var testid = document.getElementById("bankId").value; 
	var sqlClause = "c_bank_id='1'";
	var layerInfos = document.getElementById("editLayersSelect").value.split("::");
	var queryLayerName = layerInfos[0];
	var highlightSQL = "false";
	var queryParam = new SuperMap.QueryParam();
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.whereClause = sqlClause;
	if(!queryLayerName){
		queryParam.queryLayerParams = null;
	}else{
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
	mapControl.getMap().queryBySql(queryParam, highlightSQL, onQueryComplete, onError, customParam);
	//alert("ifExit:"+ifExit);//此处怎么得到ifExit()的返回值
};

//查询结果是否存在
function ifExit(resultSet){
	var recordSetCount;
	if(resultSet && resultSet.recordSets){
		recordSetCount = resultSet.recordSets.length;
		//alert("recordSetCount:"+recordSetCount);
		if(recordSetCount>0){
			bool="1";
		}		
	}
	return bool;
	
}

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

function isNumber(str){
	if(str == null || str.length == 0){
		return false;
	}
	if( str.indexOf(':') != -1 || str.indexOf('-') > 0 ) {
		return false;
	}
	return /^-?\d+\.?\d?/.test(str);
}
//更新属性
function setUpdateProperty(){
	var layerInfos = document.getElementById("editLayersSelect").value.split("::");
	var layerName = layerInfos[0];
	var dataSetType = parseInt(layerInfos[1]);
	var updatePropertyAction = new SuperMap.UI.UpdatePropertyAction(layerName, dataSetType, onUpdatePropertyComplete, onError);
	mapControl.set_action(updatePropertyAction);
};

function onUpdatePropertyComplete(result){
	var msg = "";
	if(result && result.succeed){
		msg += page_res["updatePropertySuccess"] + "\n";
	}else{
		msg += page_res["updatePropertyFail"] + "\n";
		msg += result?result.message:"";
	}
	alert(msg);
	//mapControl.refreshMapControl();
	//修改之后重新刷新
	var doubleRect=getDoubleRect();
	queryRect(doubleRect);
};







