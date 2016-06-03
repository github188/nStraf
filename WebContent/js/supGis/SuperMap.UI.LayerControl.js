//================================================================================ 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作 者:   SuperMap iServer Team 
// 文 件:   SuperMap.UI.LayerControl.js 
// 描 述:   图层控件 
// 更 新:   2007-11-13 
//================================================================================ 

Type.registerNamespace("SuperMap.UI");
SuperMap.UI.LayerControl = function(container){
    /// <summary>图层控制控件。 &lt;br&gt;
	/// 图层控制控件是用来呈现和修改当前地图的图层属性信息的，如图层的可见性，可查询。还可以在图层控制控件中浏览到每一个客户端呈现图层的图层类型，以及其子图层的所有的信息。 &lt;br&gt;
	/// 在图层控制控件中更改某一个或者某些图层的可显示以及可查询的属性， 
	/// 在地图窗口中就有互动的效果显示出来。  &lt;br&gt;
	/// 如果地图的图层很多，在图层控制控件可视范围内不能完全显示出来，可以通过属性LayerControl.overFlowEnabled 来
	/// 设置是否显示滚动条。  &lt;br&gt;
	/// LayerControl 提供两种方式来提交控制命令，第一种方式就是即时提交并刷新地图窗口，
	/// 即：当图层控制控件中有图层的属性被修改就会即时出发并提交修改信息， 同时地图窗口显示更好后的显示效果。
	/// 第二种方式就是统一提交，通过控件提供的提交按钮，统一提交所有的修改信息，并刷新地图窗口。
	/// 使用者可以通过属性 LayerControl.quickSubmitEnabled来设置是否通过第一种方式提交命令，如果为false，
	/// 则控件将以第二种方式进行。  &lt;br&gt;
	/// 采用第二种方式提交命令，使用者可以通过属性LayerControl.submitButtonText
	/// 设置提交按钮的显示文本，默认值为Refresh， 使用者可以根据需要修改成其他语言形式。 
	/// </summary>
    /// <param name="container" type="object" domElement="true">装载图层控制控件的容器，即DOM对象，如img，div。</param>
	/// <returns type="SuperMap.UI.LayerControl">返回 LayerControl 对象。</returns>

	/// <field name="layerNameText">图层名称列的标题。 在客户端呈现时显示。默认值为“LayerName”。</field>
	/// <field name="visibleText">图层可显示列的标题。 在客户端呈现时显示。默认值为“Visible”。</field>
	/// <field name="queryableText">图层可查询列的标题。 在客户端呈现时显示。默认值为“Queryable”。 </field>
	/// <field name="layerTypeText">图层类型列的标题。默认值为“LayerType”。</field>
	/// <field name="dataTypeText">数据类型属性列的标题。默认值为“DataType”。</field>
	/// <field name="submitButtonText">提交按钮的文本。 在客户端呈现时显示。图层控制控件有两种提交方式。
	///  如果选用统一提交命令的方式，则用户可以通过该属性更改提交按钮显示的文本。默认值为“Refresh”。 </field>
	/// <field name="resetButtonText">重置按钮的文本。 在客户端呈现时显示。 图层控制控件有两种提交方式，具体说明见LayerControl。 如果选用统一提交命令的方式，则用户可以通过该属性更改重置按钮显示的文本。默认值为“Reset”。 </field>
	/// <field name="buttonCssClass">图层控制控件按钮的级联样式表(CSS)类。 </field>
	/// <field name="checkboxCssClass">图层控制控件复选框的级联样式表(CSS)类。</field>
	/// <field name="separator">分隔符。在呈现图层名时，分隔符及其以后的文本将不显示。</field>
	/// <field name="overFlowEnabled">控件是否支持滚动条。如果支持滚动条该属性值为true；反之为false。默认值为true。</field>
	/// <field name="quickSubmitEnabled">每次改变图层列表设置时，是否自动立刻提交并刷新地图。
	/// 图层控制控件有两种提交方式。 控件默认的提交方式是统一提交，因此该属性的默认值为false。用户可以根据需求修改提交方式。 该属性值为true是代表控件的提交方式为即时提交方式。如果属性值为false时，表明控件将统一提交图层控制命令。</field>
	/// <field name="visibleEnabled">用于控制是否显示“可显示”列表项。</field>
	/// <field name="queryableEnabled">用于控制是否显示“可查询”列表项。</field>
	/// <field name="expand">控件加载时是否展开。如果加载时展开该属性值为true；反之为false。 </field>
	/// <field name="layerFilter">图层过滤方法。该方法主要用于设定过滤条件，来辅助决定在控件中显示哪些图层。 
	///  </field>
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
//    SuperMap.UI.LayerControl.initializeBase(this, [container]);

    this._container = container;
    this._params;
    this._map = null;	// 对应的Map
    this._layers = null;	//图层信息
    this._mapName = "";	// 地图名称 
    this._id = container.id;
    this._inited = false;
    this._imageFormat = 1;//png
    this._isNeedTitle = false;
    this._isShowInCurrentPage = true;
    this._eventsList = new Array();
    this._eventsNameList = new Array();
    this._curLayerContextMenu;
    this._lastMapName;
    this._indexs;
    this._filterResult;
    this._headerBackColor = null;
    this._headFontInfo = "{\'fontFamily\':{\'name\':\'Microsoft Sans Serif\'},\'bold\':false,\'italic\':false,\'name\':\'Microsoft Sans Serif\',\'strikeout\':false,\'underline\':false,\'size\':10}";
    this._headFont = eval('(' + this._headFontInfo + ')');
    this._headerForeColor = "Black";
    this._itemBackColor = null;
    this._itemFontInfo = "{\'fontFamily\':{\'name\':\'Microsoft Sans Serif\'},\'bold\':false,\'italic\':false,\'name\':\'Microsoft Sans Serif\',\'strikeout\':false,\'underline\':false,\'size\':10}";
    this._itemFont = eval('(' + this._itemFontInfo + ')');
    this._itemForeColor = "Black";
    this._isDescendServer = false;
    this._self = this;
    this._containerInside = null;
    this._lastLayers = null;
    this._backgroundColor = null;
    this._backgroundImage = null;

    this.layerNameText = "LayerName";
    this.visibleText = "Visible";
    this.queryableText = "Queryable";
    this.layerTypeText = "LayerType";
    this.dataTypeText = "DataType";
    this.submitButtonText = "Refresh";
    this.resetButtonText = "Reset";
    this.buttonCssClass = "";
    this.checkboxCssClass = "";
    this.separator = "@";
    this.overFlowEnabled = true;
    this.quickSubmitEnabled = false;
    this.visibleEnabled = true;
    this.queryableEnabled = true;
    this.expand = false;
    this.layerFilter = null;

    this.changeAllVisibleDelegate = Function.createDelegate(this, this._changeAllVisible);
    this.changeAllQueryableDelegate = Function.createDelegate(this, this._changeAllQueryable);
    this.submitAllDelegate = Function.createDelegate(this, this.submitAll);
    this.refreshLayersDelegate = Function.createDelegate(this, this.refreshLayers);
    
    this.layerControlItems = new Array();

    this._mapControl = null;
	this._hideThemeLayer = false;
//    if(map){map.AttachEvent("oninit",_Init);map.AttachEvent("ondestroying",_Destroy)};
};

SuperMap.UI.LayerControl.prototype = {
//========================properties start========================
    get_params:function(){
	/// <summary>地图参数对象。Object 对象。</summary>
   	/// <returns type="Object">get_params()返回值类型为 Object。</returns>
        return this._params;
    }, 
    
    set_params:function(params){
        this._params = params;
    },

    get_container:function(){
	/// <summary>装载图层控制控件的容器，即 DOM 对象，如 div，img。</summary>
   	/// <returns type="Object" domElement="true">get_container()返回值类型为 DOM 对象。</returns>
        return this._container;
    }, 

    set_map:function(map){
        var e = Function._validateParams(arguments, [
            {name: "map", type: SuperMap.Map}
        ]);
        if (e) throw e;
        this._map = map;
    }, 
    
    get_map:function(){
	/// <summary>map对象。</summary>
   	/// <returns type="SuperMap.Map">get_map()返回值类型为&lt;see cref="T:SuperMap.Map"&gt;Map&lt;/see&gt;。</returns>
        return this._map;
    }, 

    set_mapControl:function(mapControl){
        var e = Function._validateParams(arguments, [
            {name: "mapControl", type: SuperMap.UI.MapControl}
        ]);
        if (e) throw e;
        this._mapControl = mapControl;
    }, 
    
    get_mapControl:function(){
	/// <summary>map对象。</summary>
   	/// <returns type="SuperMap.UI.MapControl">get_mapControl()返回值类型为&lt;see cref="T:SuperMap.UI.MapControl"&gt;MapControl&lt;/see&gt;。</returns>
        return this._mapControl;
    }, 

    set_layers:function(layers){
        var e = Function._validateParams(arguments, [
            {name: "layers", type: Array}
        ]);
        if (e) throw e;
        this._layers = layers;
    }, 

    get_layers:function(){
	/// <summary>获取或者设置地图的图层数组。</summary>
   	/// <returns type="Array" elementType="Object">get_layers()返回值类型为 JSON 数组。</returns>
        return this._layers;
    }, 
//========================properties end========================

//========================initialize start========================
    initialize:function(){
	/// <summary>初始化LayerControl控件。</summary>
	    if(this._inited){
	        return;
	    }
		if(this._container.style.backgroundColor){
			this._backgroundColor = this._container.style.backgroundColor; 
		}
		if(this._container.style.backgroundImage){
			this._backgroundImage = this._container.style.backgroundImage; 
		}
        if(this._params){
            if(this._params.backgroundColor){
                this._backgroundColor = this._params.backgroundColor;
            }
        	if(this._params.backgroundImage){
                this._backgroundImage = this._params.backgroundImage;
            }
            if(this._params.isNeedTitle){
                this._isNeedTitle = this._params.isNeedTitle;
            }
            if(this._params.isShowInCurrentPage){
                this._isShowInCurrentPage = this._params.isShowInCurrentPage;
            }
            if(this._params.headerBackColor){
                this._headerBackColor = this._params.headerBackColor;
            }
            if(this._params.headFont){
                this._headFont = eval('(' + this._params.headFont + ')');
            }
            if(this._params.headerForeColor){
                this._headerForeColor = this._params.headerForeColor;
            }
            if(this._params.itemBackColor){
                this._itemBackColor = this._params.itemBackColor;
            }
            if(this._params.itemFont){
                this._itemFont = eval('(' + this._params.itemFont + ')');
            }
            if(this._params.itemForeColor){
                this._itemForeColor = this._params.itemForeColor;
            }
            if(this._params.layerNameText){
                this.layerNameText = this._params.layerNameText;
            }
            if(this._params.visibleText){
                this.visibleText = this._params.visibleText;
            }
            if(this._params.queryableText){
                this.queryableText = this._params.queryableText;
            }
            if(this._params.submitButtonText){
                this.submitButtonText = this._params.submitButtonText;
            }
            if(this._params.resetButtonText){
                this.resetButtonText = this._params.resetButtonText;
            }
            if(this._params.buttonCssClass){
                this.buttonCssClass = this._params.buttonCssClass;
            }
            if(this._params.checkboxCssClass){
                this.checkboxCssClass = this._params.checkboxCssClass;
            }
            if(this._params.separator){
                this.separator = this._params.separator;
            }
            if(this._params.overFlowEnabled){
                this.overFlowEnabled = this._params.overFlowEnabled;
            }
            if(this._params.quickSubmitEnabled){
                this.quickSubmitEnabled = this._params.quickSubmitEnabled;
            }
            if(this._params.visibleEnabled){
                this.visibleEnabled = this._params.visibleEnabled;
            }
            if(this._params.queryableEnabled){
                this.queryableEnabled = this._params.queryableEnabled;
            }
            if(this._params.expand){
                this.expand = this._params.expand;
            }
            if(this._params.isDescendServer){
                this._isDescendServer = this._params.isDescendServer;
            }
        }

	    if(this._containerInside){
            if(this._containerInside.parentNode){
                this._containerInside.parentNode.removeChild(this._containerInside);   
            }
            this._containerInside = null;
        }
	    this._containerInside = document.createElement("div");
	    var width = parseInt(this._container.style.width);
	    var height = parseInt(this._container.style.height);
	    if(this._params){
	        if(this._params.width){
	            width = this._params.width;
	        }
	        if(this._params.height){
	            height = this._params.height;
	        }
	    }
	    this._containerInside.id = this._id + ":Body";
        this._containerInside.style.width = width + "px";
        this._containerInside.style.height = height + "px";
        this._containerInside.style.position = "relative";
        if(this._backgroundColor){
        	this._containerInside.style.backgroundColor = this._backgroundColor;
        }
        if(this._backgroundImage){
        	this._containerInside.style.backgroundImage = this._backgroundImage;
        }
        this._container.appendChild(this._containerInside);
        if(this.overFlowEnabled){
            this._containerInside.style.overflow = "auto";
            this._containerInside.style.overflowX = "auto";
            this._containerInside.style.overflowY = "auto";
        }
	    this._inited = true;
        
        if(this._map){
    	    this._mapName = this._map.get_mapName();
            this._lastMapName = this._map.get_mapName();
            this._layers = this._map.get_layers();
        }else{
            //alert("relative map is null or undefined");
			alert(SuperMap.Resource.ins.getMessage("mapNullOrUndefined"));
        }
        
        this._initIndexs();
        this._initLayerFilter();
        //开始render
        this._renderLayers();
        this._initContext();
        this._initEvents();
        
//        if(map){map.AttachEvent("onviewchanged",_Update);}
//        if(map){map.AttachEvent("onviewchanged",_RenderLayers);}
    },
    
    _initContext:function(){
        var hidden = $get(this._container.id + "_hiddenLayerContextMenu");
        if(hidden && hidden.value){
            var layerContextMenuJSON = hidden.value;
            this._curLayerContextMenu = SuperMap.Utility.jSONToContextMenu(layerContextMenuJSON);
            this.setLayerContextMenu(_curLayerContextMenu);
        }

        if(!this._curLayerContextMenu){
            this._curLayerContextMenu = new SuperMap.UI.LayerContextMenu();
            this._setLayerContextMenu(this._curLayerContextMenu);
        }
    },

    _initEvents:function(){
        var hidden = $get(this._container.id + "_hiddenClientEvents");
        if(hidden && hidden.value){
            var strEvents = hidden.value;
            var eventsInfo = strEvents.split(';');
            for(var i = 0; i < eventsInfo.length; i++){
                var eventInfo = eventsInfo[i].split(':');
                if(eventInfo && eventInfo.length == 2){
                    var eventName = eventInfo[0];
                    var eventListners = eventInfo[1].split(',');
                    for(var j  =0; j < eventListners.length; j++){
                        try{
                            //toremove:_AttachEvent(eventName,eval(eventListners[j]));
                            this.get_events().addHandler(eventName, eval(eventListners[j]));
                        }catch(e){
                        }// events[i](arguments,userContext);
                    }
                }
            }
        }
    },

    _initLayerFilter:function(){
        var hidden = $get(this._container.id + "_hiddenFilter");
        if(hidden){
            this._filterResult = hidden.value.split(',');
        }
    },

    _initIndexs:function(){
        this._indexs = new Array();
        for(var i = 0; i < this._map._layers.length; i++){
            this._indexs[i] = i;
        }
    },
//========================initialize end========================           
    
//========================dispose start========================           
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
    // 事件机制以后实现
//        if(_map){_map.DetachEvent("onchangeview",_Update);_map.DetachEvent("onviewchanged",_RenderLayers);map.DetachEvent("oninit",_Init);map.DetachEvent("ondestroying",_Destroy)}
        this._layers = null;
        this._mapName = null;
        this._id = null;
        this._inited = null;
        this._imageFormat = null;
        this._isNeedTitle = null;
        this._isShowInCurrentPage = null;
//        this._eventsList = null;
//        this._eventsNameList = null;
        this._curLayerContextMenu = null;
        this._lastMapName = null;
        this._indexs = null;
        this._filterResult = null;
        this._headerBackColor = null;
        this._headFont = null;
        this._headerForeColor = null;
        this._itemBackColor = null;
        this._itemFont = null;
        this._itemForeColor = null;
	    if(this._container){
	        this._container.innerHTML="";
	    }
	    this._self = null;
	    this._map = null;
	    this.container = null;    	
    },
//========================dispose end========================          

//========================public methods start========================
    update:function(){
	/// <summary>重新初始化layercontrol。</summary>
        this._inited = false;
        this.initialize();
    },

    submitAll:function(){
	/// <summary>提交全部图层状态。</summary>
        var refreshMapControlDelegate = Function.createDelegate(this, this._refreshMapControl);
		if(this._hideThemeLayer) {
			if(this._layers && this._layers instanceof Array) {
				for(var i = 0; i < this._layers.length; i++) {
					if(this._layers[i].subLayers && this._layers[i].subLayers instanceof Array) {
						for(var j = 0; j < this._layers[i].subLayers.length; j++) {
							var layer = this._layers[i].subLayers[j];
							if(layer.name && layer.name.indexOf("#") != -1) {
								continue;
							}
							for(var k = 0; k < this._layers[i].subLayers.length; k++) {
								if(this._layers[i].subLayers[k].name && this._layers[i].subLayers[k].name.indexOf(layer.name) != -1) {
									this._layers[i].subLayers[k].visible = layer.visible;
									this._layers[i].subLayers[k].queryable = layer.queryable;
								}
							}
						}
					}
				}
			}
		}
		var layerNames = new Array();
		var queryableArgs = new Array();
		var visibleArgs = new Array();
		var layerSettingType = new SuperMap.LayerSettingType();
		for(var i = 0; i < this._layers.length; i++) {
			layerNames.push(this._layers[i].name);
			queryableArgs.push(this._layers[i].queryable);
			visibleArgs.push(this._layers[i].visible);
			if(this._layers[i].layerSetting && this._layers[i].layerSetting.layerSettingType == layerSettingType.wms) {
				var imageLayers = this._map.get_imageLayers();
				for(var j = 0; j < imageLayers.length; j++) {
					var tempLayer = imageLayers[j];
					if(!this._layers[i].visible && tempLayer._param.layerSettingType == layerSettingType.wms) {
						if(tempLayer._param.url == this._layers[i].layerSetting.url) {
							tempLayer.get_container().style.display = "none";
						}
					} else {
						tempLayer.get_container().style.display = "block";
					}
				}
			}
			if(this._layers[i].subLayers && this._layers[i].subLayers.length > 0) {
				for(var j = 0; j < this._layers[i].subLayers.length; j++) {
					layerNames.push(this._layers[i].subLayers[j].name);
					queryableArgs.push(this._layers[i].subLayers[j].queryable);
					visibleArgs.push(this._layers[i].subLayers[j].visible);
				}
			}
		}

		this._map._setLayerStatus(layerNames, visibleArgs, queryableArgs, refreshMapControlDelegate);
        this._lastLayers = this._copyJson(this._layers);
    },
    
    refreshLayers:function(){
	/// <summary>提交全部图层状态。</summary>
		 var allVisible = true;
	     var allQueryable = true;
        for(var i = 0; i < this._layers.length; i++){
        	//刷新图层自身信息
        	this._layers[i].visible = this._lastLayers[i].visible;
        	this._layers[i].queryable = this._lastLayers[i].queryable;
        	var parentCheckBoxV = $get(this._layers[i].name + ":V");
            var parentCheckBoxQ = $get(this._layers[i].name + ":Q");
        	if(this._layers[i].visible){
                parentCheckBoxV.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
            }else{
                parentCheckBoxV.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
            }
            if(this._layers[i].queryable){
                parentCheckBoxQ.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
            }else{
                parentCheckBoxQ.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
            }
        	if(!this._layers[i].visible){
        		allVisible = false;
        	}
        	if(!this._layers[i].queryable){
        		allQueryable = false;
        	}
        	//刷新子图层信息
            if(this._layers[i].subLayers && this._layers[i].subLayers instanceof Array){
                for(var j = 0; j < this._layers[i].subLayers.length; j++){
                	var visible = this._lastLayers[i].subLayers[j].visible;
                	var queryable = this._lastLayers[i].subLayers[j].queryable;
                	if(!visible){
		        		allVisible = false;
		        	}
		        	if(!queryable){
		        		allQueryable = false;
		        	}
                    this._layers[i].subLayers[j].visible = visible;  
                    this._layers[i].subLayers[j].queryable = queryable;
                    this._map.layers[i].subLayers[j].visible = visible;
                    this._map.layers[i].subLayers[j].queryable = queryable;
                    var subCheckBoxV = $get(this._layers[i].subLayers[j].name + ":V");
                    var subCheckBoxQ = $get(this._layers[i].subLayers[j].name + ":Q");
                    if(subCheckBoxV){
                        subCheckBoxV.checked = visible;
                        if(visible){
                            subCheckBoxV.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
                        }else{
                            subCheckBoxV.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
                        }
                    }
                    if(subCheckBoxQ){
                        subCheckBoxQ.checked = queryable;
                        if(queryable){
                            subCheckBoxQ.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
                        }else{
                            subCheckBoxQ.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
                        }
                    }
                }            
            }
        }
        //刷新全部可见与可查复选框
        var layerAllV = $get("layer:VAll");
        var layerAllQ = $get("layer:QAll");
        if(layerAllV){
         	layerAllV.checked = allVisible;
         }
         if(layerAllQ){
         	layerAllQ.checked = allQueryable;
         }
    },
    
//========================public methods end========================

//========================control events start========================
    _addEvent:function(eventName, handler){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "handler", type: Object}
        ]);
        if (e) throw e;
        
        this.get_events().addHandler(eventName, handler);
        if(this._eventsNameList){
            // 保证this._eventsNameList数组中没有同名的事件名，否则会多次触发
            if(!Array.contains(this._eventsNameList, eventName)){
                this._eventsNameList.push(eventName);    
            }
        }else{
            this._eventsNameList = new Array();
            this._eventsNameList.push(eventName); 
        }
    },

    _removeEvent:function(eventName, handler){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "handler", type: Object}
        ]);
        if (e) throw e;
        
        this.get_events().removeHandler(eventName, handler);
        // 如果eventName的事件没有了，需要把this._eventsNameList中的事件名删除，貌似不删也行
        if(!this.get_events().getHandler(eventName)){
            this._eventsNameList.pop(eventName);
        }
    },

    _raiseEvent:function(eventName, args, userContext){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "args", type: Object, mayBeNull: true, optional: true},
            {name: "userContext", type: String, mayBeNull: true, optional: true}
        ]);
        if (e) throw e;
        
        var handler = this.get_events().getHandler(eventName);
        if (handler) {
            handler(arguments, userContext);
        }
    },
//========================control events start========================

//========================private methods start========================
    _setLayerContextMenuHidden:function(){
        var hidden = $get(this._container.id + "_hiddenLayerContextMenu");
        if(hidden){
            hidden.value = this._curLayerContextMenu.json;
        }
    },

    _sortLayers:function(isDescend){
        if(!isDescend){
            isDescend = false;
        }
        for(var i = 0; i < this._indexs.length - 1; i++){
            for(var j = i + 1; j < this._indexs.length; j++){
            	var interchange = false;
                if(this._compare(this._map.layers[_indexs[i]], this._map.layers[_indexs[j]]) == isDescend){
                    interchange = true;
                }
                if(!interchange){
                    var tempIndex = this._indexs[i];
                    this._indexs[i] = this._indexs[j];
                    this._indexs[j] = tempIndex;
                }
            }
        }
    },

    _compare:function(layerP,layerN){
        // 返回顺序是按照Layer中的Name属性的从大到小
        if(layerP.name >= layerN.name){
            return true;
        }else{
            return false;
        }
    },
    
    _renderHead:function(){
  	    var width = this._container.style.width.replace("px","");

   	    var headDiv = document.createElement("div");
   	    headDiv.id = this._id + ":Head"
        headDiv.style.position = "absolute";
        headDiv.style.top = "0px";
        headDiv.style.left = "0px";
        headDiv.style.width = width + "px";
        headDiv.style.height = "20px";
        headDiv.style.align = "center";
   	    headDiv.style.backgroundColor = this._headerBackColor;
   	    headDiv.style.color = this._headerForeColor;
   	    headDiv.style.fontFamily = this._headFont.fontFamily.name;
   	    headDiv.style.fontSize = this._headFont.size;
        if(this._headFont.bold){
            headDiv.style.fontWeight = "bold";
        }
        if(this._headFont.italic){
            headDiv.style.fontStyle = "italic";
        }
        if(this._headFont.underline && this._headFont.strikeout){
            headDiv.style.textDecoration = "underline line-through";
        }else if(this._headFont.underline){
            headDiv.style.textDecoration = "underline";
        }else if(this._headFont.strikeout){
            headDiv.style.textDecoration = "line-through";
        }
 	    
   	    var visibleP = document.createElement("p");
   	    visibleP.align = "center";
        visibleP.style.width = "20px";
        visibleP.style.height = "20px";
        visibleP.innerHTML = "V";//this.visibleText;
        visibleP.style.position = "absolute";
        visibleP.style.top = 0;
        visibleP.style.left = 20;
        headDiv.appendChild(visibleP);

   	    var queryableP = document.createElement("p");
   	    queryableP.align = "center";
        queryableP.style.width = "20px";
        queryableP.style.height = "20px";
        queryableP.innerHTML = "Q";//this.queryableText;
        queryableP.style.position = "absolute";
        queryableP.style.top = 0;
        queryableP.style.left = 40;
        headDiv.appendChild(queryableP);

   	    var layerTypeP = document.createElement("p");
   	    layerTypeP.align = "center";
        layerTypeP.style.width = "20px";
        layerTypeP.style.height = "20px";
        layerTypeP.innerHTML = "LT";//this.layerTypeText;
        layerTypeP.style.position = "absolute";
        layerTypeP.style.top = 0;
        layerTypeP.style.left = 60;
        headDiv.appendChild(layerTypeP);

   	    var dataTypeP = document.createElement("p");
   	    dataTypeP.align = "center";
        dataTypeP.style.width = "20px";
        dataTypeP.style.height = "20px";
        dataTypeP.innerHTML = "DT";//this.dataTypeText;
        dataTypeP.style.position = "absolute";
        dataTypeP.style.top = 0;
        dataTypeP.style.left = 80;
        headDiv.appendChild(dataTypeP);

   	    var layerNameP = document.createElement("p");
   	    layerNameP.align = "center";
        //lnSpan.style.width = "20px";
        layerNameP.style.height = "20px";
        layerNameP.innerHTML = this.layerNameText;
        layerNameP.style.position = "absolute";
        layerNameP.style.top = 0;
        layerNameP.style.left = 100;
        headDiv.appendChild(layerNameP);

        this._container.appendChild(headDiv);
    },
    
    _renderBottom:function(bottomTop){
  	    var width = parseInt(this._container.style.width);
        var bottomDiv = $get(this._id + ":Bottom");

   	    if(!bottomDiv){
   	        bottomDiv = document.createElement("div");
   	        bottomDiv.id = this._id + ":Bottom";
            bottomDiv.style.position = "absolute";
            bottomDiv.style.left = "0px";
            bottomDiv.style.width = width + "px";
            bottomDiv.style.height = "16px";
            bottomDiv.style.align = "left";
   	        bottomDiv.style.backgroundColor = this._headerBackColor;
   	        bottomDiv.style.color = this._headerForeColor;
   	        bottomDiv.style.fontFamily = this._headFont.fontFamily.name;
   	        bottomDiv.style.fontSize = this._headFont.size;
            if(this._headFont.bold){
                bottomDiv.style.fontWeight = "bold";
            }
            if(this._headFont.italic){
                bottomDiv.style.fontStyle = "italic";
            }
            if(this._headFont.underline && this._headFont.strikeout){
                bottomDiv.style.textDecoration = "underline line-through";
            }else if(this._headFont.underline){
                bottomDiv.style.textDecoration = "underline";
            }else if(this._headFont.strikeout){
                bottomDiv.style.textDecoration = "line-through";
            }

   	        var visibleAll = document.createElement("input");
   	        visibleAll.id = this._id + ":VAll";
            visibleAll.type = "checkbox";
            visibleAll.style.width = "16px";
            visibleAll.style.height = "16px";
            visibleAll.style.position = "absolute";
            visibleAll.style.top = 0;
            visibleAll.style.left = 20;
            visibleAll.onclick = this.changeAllVisibleDelegate;
            visibleAll.title = SuperMap.Resource.ins.getMessage("visibleAll");
            visibleAll.style.marginLeft = 0;
            visibleAll.style.display = "none";
            bottomDiv.appendChild(visibleAll);

   	        var queryableAll = document.createElement("input");
   	        queryableAll.id = this._id + ":QAll";
            queryableAll.type = "checkbox";
            queryableAll.style.width = "16px";
            queryableAll.style.height = "16px";
            queryableAll.style.position = "absolute";
            queryableAll.style.top = 0;
            queryableAll.style.left = 40;
            queryableAll.onclick = this.changeAllQueryableDelegate;
            queryableAll.title = SuperMap.Resource.ins.getMessage("queryableAll");
            queryableAll.style.marginLeft = 0;
            queryableAll.style.display = "none";
            bottomDiv.appendChild(queryableAll);

   	        var submitAll = document.createElement("IMG");
			var image = new Image();
			image.src = "../../images/supGis/button_submit.jpg";
			submitAll.style.width = image.width;
			submitAll.style.height = image.height;
			
   	        submitAll.alt = SuperMap.Resource.ins.getMessage("submitAll");
   	        submitAll.id = this._id + ":SubmitAll";
            submitAll.style.position = "absolute";
            submitAll.style.top = 0;
            submitAll.style.left = 20;
            submitAll.style.marginLeft = 0;
            submitAll.onclick = this.submitAllDelegate;
            submitAll.src="../../images/supGis/button_submit.jpg";
            submitAll.onmouseover = function (){
            	this.src = '../../images/supGis/button_submit_down.jpg';
            }
            submitAll.onmouseout = function (){
            	this.src = '../../images/supGis/button_submit.jpg';
            }
            bottomDiv.appendChild(submitAll);
			
			var refreshLayers = document.createElement("IMG");
			refreshLayers.alt = SuperMap.Resource.ins.getMessage("refreshLayersInfo");
			refreshLayers.src = "../../images/supGis/button_refresh.jpg";
   	        refreshLayers.id = this._id + ":RefreshLayers";
            refreshLayers.type = "button";
            refreshLayers.style.position = "absolute";
            refreshLayers.style.top = 0;
            refreshLayers.style.left = 80;
			refreshLayers.style.width = image.width;
			refreshLayers.style.height = image.height;
			delete image;
			image = null;
			
            refreshLayers.value = SuperMap.Resource.ins.getMessage("refresh");
            refreshLayers.onclick = this.refreshLayersDelegate;
            refreshLayers.onmouseover = function (){
            	this.src = '../../images/supGis/button_refresh_down.jpg';
            }
            refreshLayers.onmouseout = function (){
            	this.src = '../../images/supGis/button_refresh.jpg';
            }
            bottomDiv.appendChild(refreshLayers);
            this._containerInside.appendChild(bottomDiv);
   	    }

        bottomDiv.style.top = bottomTop + "px";
    },
    _copyJson:function(json){
      	var obj = new Object();
       	if(!json){return null;}
   		for(var m in json){
       		if(typeof(json[m]) == "object"){
		       obj[m] = this._copyJson(json[m]);
		    } else{
	            obj[m] = json[m];
	        }
	    }
	    return obj;
     },
    _renderLayers:function(){
        var url;
        var innerHTML = "";

   	    var width = parseInt(this._container.style.width);
   	    var headerHeight = 0;

        if(!$get(this._id + ":Head") && this._isNeedTitle){
            this._renderHead();
            headerHeight = parseInt($get(this._id + ":Head").style.height)
            this._containerInside.style.top = headerHeight + "px";
            this._containerInside.style.height = (parseInt(this._containerInside.style.height) - headerHeight) + "px";
        }

        for(var i = 0; i < this._indexs.length; i++){
            var m = this._indexs[i];
            if(!this._layers[m]){
                continue;
            }
            if(this._filterResult && this._filterResult[m] == "false"){
                continue;
            }
            if(this._self.layerFilter && !this._self.layerFilter(this._layers[m])){
                continue;
            }
			if(this._hideThemeLayer && this._layers[m].name && this._layers[m].name.indexOf("#") != -1) {
				continue;
			}
            var layerControlItem = $create(SuperMap.UI.LayerControlItem,{layer:this._layers[m], top:m * 20, layerControl:this, index:m, width:width},null, null, this._containerInside);
			layerControlItem._hideThemeLayer = this._hideThemeLayer;
            layerControlItem.render();
            this.layerControlItems.push(layerControlItem);
        }
        
        var bottomTop = this._indexs.length * 20;
        this._renderBottom(bottomTop);
        this._lastLayers = this._copyJson(this._layers);
    },

    _expandCollapseLayer:function(args){ 
        var i = args.index;
        var m = this._indexs[i];
        if(!this._layers[m]){
            return;
        }
        if(this._filterResult && this._filterResult[m] == "false"){
            return;
        }
        if(this._self.layerFilter && !this._self.layerFilter(this._layers[m])){
            return;
        }
        
        var offsetHeight = 0;
        var tempLayerItemDiv = $get(this._layers[m].name + ":LayerItemDiv");
        if(tempLayerItemDiv && this._layers[m].subLayers && this._layers[m].subLayers instanceof Array){
			if(this._hideThemeLayer) {
				var layerCount = 0;	
				for(var i = 0; i < this._layers[m].subLayers.length; i++) {
					if(this._layers[m].subLayers[i].name.indexOf("#") == -1){
						layerCount++;
					}
				}
				offsetHeight += (layerCount * 20);
			} else {
				offsetHeight += (this._layers[m].subLayers.length * 20);
			}
        }
        for(var j = i + 1; j < this._indexs.length; j++){
            var n = this._indexs[j];

            var tempLayerItemDiv2 = $get(this._layers[n].name + ":LayerItemDiv");
            if(args.expanded){
                tempLayerItemDiv2.style.top = parseFloat(tempLayerItemDiv2.style.top) - offsetHeight;
            }else{
                tempLayerItemDiv2.style.top = parseFloat(tempLayerItemDiv2.style.top) + offsetHeight;
            }
        }

        var tempLayerItemDivLast = $get(this._layers[this._indexs.length - 1].name + ":LayerItemDiv");
        var bottomTop;
        if(j == i + 1){
            if(args.expanded){
                bottomTop = parseFloat(tempLayerItemDivLast.style.top) + 20;
            }else{
                bottomTop = parseFloat(tempLayerItemDivLast.style.top) + 20 + offsetHeight;
            }
        }else{
            bottomTop = parseFloat(tempLayerItemDivLast.style.top) + 20;
        }
        

        this._renderBottom(bottomTop);
    },
    
    _changeAllVisible:function(){
        var allVisible = $get(this._id + ":VAll").checked;
        for(var i = 0; i < this._layers.length; i++){
            this._layers[i].visible = allVisible;
            var tempCheckBox = $get(this._layers[i].name + ":V");
            if(tempCheckBox){
                tempCheckBox.checked = allVisible;
                if(allVisible){
                    tempCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
                }else{
                    tempCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
                }
            }
            if(this._layers[i].subLayers && this._layers[i].subLayers instanceof Array){
                for(var j = 0; j < this._layers[i].subLayers.length; j++){
                    this._layers[i].subLayers[j].visible = allVisible;
                    var tempSubCheckBox = $get(this._layers[i].subLayers[j].name + ":V");
                    if(tempSubCheckBox){
                        tempSubCheckBox.checked = allVisible;
                        if(allVisible){
                            tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
                        }else{
                            tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
                        }
                    }
                }            
            }
        }
    },

    _changeAllQueryable:function(){
	/// <summary></summary>
        var allQueryable = $get(this._id + ":QAll").checked;
        for(var i = 0; i < this._layers.length; i++){
            this._layers[i].queryable = allQueryable;
            var tempCheckBox = $get(this._layers[i].name + ":Q");
            if(tempCheckBox){
                tempCheckBox.checked = allQueryable;
                if(allQueryable){
                    tempCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
                }else{
                    tempCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
                }
            }
            if(this._layers[i].subLayers && this._layers[i].subLayers instanceof Array){
                for(var j = 0; j < this._layers[i].subLayers.length; j++){
                    this._layers[i].subLayers[j].queryable = allQueryable;
                    var tempSubCheckBox = $get(this._layers[i].subLayers[j].name + ":Q");
                    if(tempSubCheckBox){
                        tempSubCheckBox.checked = allQueryable;
                        if(allQueryable){
                            tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
                        }else{
                            tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
                        }
                    }
                }            
            }
        }
    },

    _clearItems:function(){
        if(this.layerControlItems){
            while(this.layerControlItems.length > 0){
                var tempLayerControlItem = this.layerControlItems.pop();
                tempLayerControlItem._clearSubLayerItems();
                tempLayerControlItem._clearSelfDivs();
            }
        }
    },

    _setEventsHidden:function(){
        // 格式:   eventName1:listener1,listerner2,...listernerN;eventName2:listener1,listerner2,...listernerN
        var strEvent = "";
        for(var i = 0; i < this._eventsNameList.length; i++){
            var name = this._eventsNameList[i];
            var events = this._eventsList[name];
            if(!events){
                continue;
            }
            strEvent += name + ":";
            // events方式要换掉
            for(var j = 0; j < this.events.length; j++){
                // 只要名称,不要内容
                var eventContent = events[j].toString();
                var startIndex = eventContent.indexOf(" ");
                var endIndex = eventContent.indexOf("(");
                var strEventName = eventContent.substring(startIndex+1,endIndex); 
                strEvent+= strEventName;
                if(j!=events.length-1 ){strEvent+=",";}
            }
            if(i!=_eventsNameList.length-1 ){strEvent+=";";}
        }
        var hidden = document.getElementById(container.id+"_hiddenClientEvents");
        if(hidden){
            hidden.value = strEvent;
        }
    },

    _updateCheckBox:function(layer){
        var checkboxV = $get(this._container.id + "_" + layer.name + ":V");
        var checkboxQ = $get(this._container.id + "_" + layer.name + ":Q");
        if(layer.visible){
            checkboxV.checked = "checked";
        }else{
            checkboxV.checked = "";
        }
        if(layer.queryable){
            checkboxQ.checked = "checked";
        }else{
            checkboxQ.checked = "";
        }
    },

    _getResult:function(arg){
        if(this._filterResult.join(",") == arg){
            return;
        }
        this._filterResult = arg.split(',');
        if(this._lastMapName != this._map.get_mapName()){
            this._layers = this._map.get_layers();
            for(var i = 0; i < this._layers.length; i++){
                this._indexs[i] = i;
            }
            this._lastMapName = this._map.get_mapName();
        }
        if(this._isShowInCurrentPage){
            this._renderLayers();
        }else{
            this._container.style.visibility = "hidden";
        }
    },

    _setLayerContextMenu:function(layerContextMenu){
	/// <summary></summary>
	/// <param name="layerContextMenu" type="SuperMap.UI.LayerContextMenu"></param>
    //    if(_curLayerContextMenu ){_curLayerContextMenu.Destroy();}
        if(layerContextMenu){
            layerContextMenu.initialize(this._map);
        }
        this._curLayerContextMenu = layerContextMenu;
        this._curLayerContextMenu.initialize(this._map);
        this._setLayerContextMenuHidden();
    },

    _refreshMapControl:function(){
        this._mapControl.refreshMapControl();
    },
//========================private methods end========================

//========================deprecated methods start========================

	_saveLayerStatus:function(){
	/// <summary></summary>
        var changed = false;
        for(var i = 0; i < this._map.layers.length;i++){
            var layer = this._map.layers[i];
            var checkboxV = $get(this._container.id + "_" + layer.name + ":V");
            if(checkboxV){
                var visible = checkboxV.checked;
                if(this._map.layers[i].visible != visible){
                    this._map.layers[i].visible = visible;
                    changed = true;
                }
            }
            var checkboxQ = $get(this._container.id + "_" + layer.name + ":Q");
            if(checkboxQ){
                var queryable = checkboxQ.checked;
                if(this._map.layers[i].queryable != queryable){
                    this._map.layers[i].queryable = queryable;
                    changed = true;
                }
            }
        }
        if(changed){
            // Map的update行为要变
            this._map.update(true);
//            _TriggerEvent("layeritemschanged",null,"LayerItemsChanged");
        }
    },

    _sortLayerItem:function(isDescend){
	/// <summary></summary>
	/// <param name="isDescend" type=""></param>
        this._sortLayers(isDescend);
        this._renderLayers(this);
    },

    _switchThemesDisplay:function(id){
	/// <summary></summary>
	/// <param name="id" type=""></param>
	    var img = $get("Switch_"+id);
	    var o = $get(id);
	    if(o.style.display=='block'){
		    o.style.display='none';
		    img.src = "../../images/supGis/collapse.gif";
	    }else{
		    o.style.display ='block';
		    img.src = "../../images/supGis/expand.gif";
	    }
    },

    _switchThemesVisible:function(index, themeType, value){
	/// <summary></summary>
	/// <param name="index" type=""></param>
	/// <param name="themeType" type=""></param>
	/// <param name="value" type=""></param>
	    if(!index || !themeType){
	        return;
	    }
	    if(index == -1){
	        return;
	    }
	    switch(themeType){
	        case "DotDensity":
	            if(this._layers[index].themeDotDensity){
	                this._layers[index].themeDotDensity.enabled = value;
	            }
                break;
            case "GraduatedSymbol":
                if(this._layers[index].themeGraduatedSymbol){
                    this._layers[index].themeGraduatedSymboly.enabled = value;
                }
                break;
            case "Graph":
                if(this._layers[index].themeGraph){
                    this._layers[index].themeGraph.enabled = value;
                }
                break;
            case "Label":
                if(this._layers[index].themeLabel){
                    this._layers[index].themeLabel.enabled = value;
                }
                break;
            case "Range":
                if(this._layers[index].themeRange){
                    this._layers[index].themeRange.enabled = value;
                }
                break;
            case "Unique":
                if(this._layers[index].themeUnique){
                    this._layers[index].themeUnique.enabled = value;
                }
                break;
            
	    }
	    // 这个地方的map不是MapControl，map上也要能update
//	    this._map.update(true);
//	    _TriggerEvent("themeswitched",null,"ThemeSwitched");
    },
    
    _onContextMenu:function(e, index){
	/// <summary></summary>
	/// <param name="e" type=""></param>
	/// <param name="index" type=""></param>
        e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
        e.index = index;
	    if(this._curLayerContextMenu && this._curLayerContextMenu.openContextMenu){
	        this._curLayerContextMenu.openContextMenu(e, this._self);
	    }
	    return false;
    },

    _checkedAllQueryable:function(){
	/// <summary></summary>

        var layers = this._map.get_layers();
        for(var i = 0; i < layers.length; i++){
            var layer = layers[i];
            var queryableCheckbox = $get(this._container.id + "_" + layer.name + ":Q");
            if(queryableCheckbox){
                var queryableCheckboxAll = $get(this._container.id + "_CheckQueryableAll");
                queryableCheckbox.checked = queryableCheckboxAll.checked;
            }
        }
    },

    _checkedAllVisible:function(){
	/// <summary></summary>

        var layers = this._map.get_layers();
        for(var i = 0; i < layers.length; i++){
            var layer = layers[i];
            var visibleCheckbox = $get(this._container.id + "_" + layer.name + ":V");
            if(visibleCheckbox){
                var visibleCheckboxAll = $get(this._container.id + "_CheckVisibleAll");
                visibleCheckbox.checked = visibleCheckboxAll.checked;
            } 
        }
    },
    _writeStartScriptPart:function(index,themeType,enabled ){
	/// <summary></summary>
	/// <param name="index" type=""></param>
	/// <param name="themeType" type=""></param>
	/// <param name="enabled" type=""></param>
        var str ="<tr><td valign=\"top\" width=\"20px\">"
        if(enabled){
            str+="<input type=checkbox checked onclick="+_id+".SwitchThemesVisible("+index+",\""+themeType+"\",this.checked)></td>";
        }
        else{
            str+="<input type=checkbox onclick="+_id+".SwitchThemesVisible(\""+index+"\",\""+themeType+"\",this.checked)></td>";
        }
        str+="<td valign=\"top\" width=\"20px\">";
        //标签专题图不能展开,其它专题图需要展开
        if (themeType != "Label"){
            str+="<img id=\"Switch_"+_id+"_"+_layers[index].name+"_"+themeType+"\"  src=\"../../images/supGis/expand.gif\" onclick="+_id+".SwitchThemesDisplay(\""+_id+"_"+_layers[index].name+"_"+themeType+"\")>";
        }
        var themeName = "";
        var picName = "";
        switch (themeType){
            case "DotDensity":
                themeName = SuperMap.Resource.ins.getMessage("ThemeDotDensity");
                picName = "ThemeDotDensity.gif";
                break;
            case "GraduatedSymbol":
                themeName = SuperMap.Resource.ins.getMessage("ThemeGraduatedSymbol");
                picName = "ThemeGraduatedSymbol.gif";
                break;
            case "Graph":
                themeName = SuperMap.Resource.ins.getMessage("ThemeGraph");
                picName = "ThemeGraph.gif";
                break;
            case "Label":
                themeName = SuperMap.Resource.ins.getMessage("ThemeLabel");
                picName = "ThemeLabel.gif";
                break;
            case "Range":
                themeName = SuperMap.Resource.ins.getMessage("ThemeRange");
                picName = "ThemeRange.gif";
                break;
            case "Unique":
                themeName = SuperMap.Resource.ins.getMessage("ThemeUnique");
                picName = "ThemeUnique.gif";
                break;

        }
        str+="</td><td><table><tr><td valign=\"top\" width=\"20px\"> <img src=\"../../images/supGis/"+picName+"\"> </td><td><span>"+themeName+"</span><div id=\""+_id+"_"+_layers[index].name+"_"+themeType+"\" style=\"display:block\">";
        return str;
    },

    _writeEndScriptPart:function(){
	/// <summary></summary>
	/// <returns></returns>
        var str = "</div></td></tr></table></td></tr>";
        return str;
    },

    _onGetResourceComplete:function(url,userContext){
	/// <summary></summary>
	/// <param name="url" type=""></param>
	/// <param name="userContext" type=""></param>
        var img = document.getElementById(userContext);
        img.src =url;
    },
    
    _onGetResourceError:function(){
	/// <summary></summary>
	}
//========================deprecated methods end========================
};
SuperMap.UI.LayerControl.registerClass('SuperMap.UI.LayerControl', Sys.UI.Control, Sys.IDisposable);

SuperMap.UI.LayerContextMenu = function(){
	/// <summary>LayerControl的菜单，可扩展。</summary>
	/// <returns type="SuperMap.UI.LayerContextMenu">返回 LayerContextMenu 对象。</returns>
    this.type="SuperMap.UI.LayerContextMenu";
    this._mainMenu = null;
    this._width = "100px";
    this._height = "200px";
    this._self  = this;
    this._index;
    this._layerControl;
    this.json=SuperMap.Utility.contextMenuToJSON(this.type,[]);
};

SuperMap.UI.LayerContextMenu.prototype = {
    
    initialize:function(mapControl){
	/// <summary>初始化LayerContextMenu。</summary>
        this.mapControl = mapControl;
    },
    
    openContextMenu:function(e,layerControl){
	/// <summary>打开菜单。</summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	/// <param name="layerControl" type="object">LayerControl 对象。</param>
        
        //开始扩展，先做一个菜单窗口
        if(_mainMenu!=null){
            document.body.removeChild(_mainMenu);
            _mainMenu = null;
        }
        _CreateMenu(e);
        _index = e.index;
        _layerControl=layerControl
        document.body.appendChild(_mainMenu);       
        //创建一个选择项
        var subMenu0 = document.createElement("Div");
        subMenu0.id  = "menuForLayer"+e.index+"_0";
        subMenu0.style.index =5;
        subMenu0.style.width = _width;
        subMenu0.style.paddingLeft = "20px";
        subMenu0.style.paddingTop = "5px";
        subMenu0.onclick = _ChangeVisible;
        subMenu0.onmouseover=_OnMouseOver;
        _mainMenu.style.height = "10px";
        if(_self.mapControl.layers[e.index].visible){
            subMenu0.innerHTML = SuperMap.Resource.ins.getMessage("unVisible");
        }
        else{
            subMenu0.innerHTML = SuperMap.Resource.ins.getMessage("visible");
        }
        _mainMenu.appendChild(subMenu0); 
         var subMenu1 = document.createElement("Div");
        subMenu1.id  = "menuForLayer"+e.index+"_1";
        subMenu1.style.index =5;
        subMenu1.style.width = _width;
        subMenu1.style.paddingLeft = "20px";
        subMenu1.style.paddingTop = "5px";
        subMenu1.style.borderTop = "1px solid gray";
        subMenu1.onclick = _ChangeQueryable;
        subMenu1.onmouseover=_OnMouseOver;
        _mainMenu.style.height = "10px";
        if(_self.mapControl.layers[e.index].queryable){
            subMenu1.innerHTML = SuperMap.Resource.ins.getMessage("unQuery");
        }
        else{
            subMenu1.innerHTML = SuperMap.Resource.ins.getMessage("query");
        }
        _mainMenu.appendChild(subMenu1); 
        if(_self.mapControl.layers[e.index].themeLabel){return;}
        var subMenu2 = document.createElement("Div");
        subMenu2.id  = "menuForLayer"+e.index+"_1";
        subMenu2.style.index =5;
        subMenu2.style.width = _width;
        subMenu2.style.paddingLeft = "20px";
        subMenu2.style.paddingTop = "5px";
        subMenu2.style.borderTop = "1px solid gray";
        subMenu2.onclick = _MakeDefaultTheme;
        subMenu2.onmouseover=_OnMouseOver;
        _mainMenu.style.height = "10px";
        subMenu2.innerHTML = SuperMap.Resource.ins.getMessage("makeThemeLabel");
        _mainMenu.appendChild(subMenu2); 
    },
    
    _onMouseLeave:function (e){
        document.body.removeChild(_mainMenu);
        _mainMenu = null;
    },
    
    _onMouseOver:function(e){
        var target=SuperMap.Utility.getTarget(e);
        target.style.cursor="../../images/supGis/cursors/Pan.cur";
    },
    
    //changevisible
    _changeVisible:function (e){
        if(_self.mapControl.layers[_index].visible){
            _self.mapControl.layers[_index].visible = false;
        }
        else
        {
            _self.mapControl.layers[_index].visible =true; 
        }
        
        _self.mapControl.Update(true);
    },
    
    _changeQueryable:function(e){
        if(_self.mapControl.layers[_index].queryable){
            _self.mapControl.layers[_index].queryable = false;
        }
        else
        {
            _self.mapControl.layers[_index].queryable =true; 
        }
        _self.mapControl.Update(true);
    },
    
    _makeDefaultTheme:function(e){
        if(!_self.mapControl.layers[_index]){return;}
        var layer=_self.mapControl.layers[_index];
        if(!layer.themeLabel){layer.themeLabel=new SuperMap.IS.LabelTheme();}
        layer.themeLabel.caption=layer.name+"Label";
        layer.themeLabel.expression="smid";
        layer.themeLabel.fixedAngle=true;
        layer.themeLabel.display=new SuperMap.IS.TextStyle();
        layer.themeLabel.display.fixedSize=true;
        layer.themeLabel.display.bgColor=859845;
        layer.themeLabel.enabled=true;
        _self.mapControl.Update(true);
    },
    
    //创建主菜单
    _createMenu:function(e){
        _mainMenu = document.createElement("Div");
        _mainMenu.id = "menuForLayer"+e.index;
        _mainMenu.style.index =5;
        _mainMenu.style.border = "solid 2px gray";
        _mainMenu.style.position = "absolute";
        var scroll = _GetScroll();
        _mainMenu.style.width = _width
        _mainMenu.style.height = _height;
        _mainMenu.style.left = e.clientX+scroll.left;
        _mainMenu.style.top = e.clientY+scroll.top;
        _mainMenu.style.fontSize = "10px";;
        _mainMenu.onmouseleave = _OnMouseLeave; 
        _mainMenu.style.backgroundImage="url(../../images/supGis/backgroud.bmp)";
    }
};
SuperMap.UI.LayerContextMenu.registerClass('SuperMap.UI.LayerContextMenu', Sys.UI.Control, Sys.IDisposable);

SuperMap.UI.LayerControlItem = function(container){
	/// <summary>图层控制子项。图层控制控件中列出了若干地图图层的信息，每一个图层的信息作为一个图层控制子项。</summary>
	/// <param name="container"  type="object" domElement="true">装载图层控制子项的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.LayerControlItem">返回 LayerControlItem 对象。</returns>
	
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
//    SuperMap.UI.LayerControlItem.initializeBase(this, [container]);

    this._container = container;
    this._layerItemDiv = null;
    this._subLayerItemDiv = null;
    
    this._index;
    this._layer;
    this._isSuperMapLayer;

    this._expandButtonImg = null;
    this._collapseButtonImg = null;
    this._expanded = false;
    this._layerTypeImg;
    this._dataTypeImg;
    
    this._hasSubLayers = false;
    this._subLayerControlItems = new Array();
    
    this._collapseDelegate = Function.createDelegate(this, this._collapse);
    this._expandDelegate = Function.createDelegate(this, this._expand);
    this._onCheckVisibleBoxClickDelegate = Function.createDelegate(this, this._onCheckVisibleBoxClick);
    this._onCheckQueryableBoxClickDelegate = Function.createDelegate(this, this._onCheckQueryableBoxClick);
    
    this._layerControl = null;
    this._top = 0;
    this._left = 0;
    this._width = 200;
    this._height = 20;
    this._eventsNameList = new Array();
    
    this._layerTypeImg = "../../images/supGis/resources/theme_none.gif";
    this._dataTypeImg = "../../images/supGis/resources/dataset_unknown.gif";
    this._layerTypeTitle = SuperMap.Resource.ins.getMessage("noTheme");
	this._dataTypeTitle = SuperMap.Resource.ins.getMessage("nuknownDatasetType");
};

SuperMap.UI.LayerControlItem.prototype = {

    initialize:function(){
	/// <summary>初始化 LayerControlItem。 </summary>
        if(this._layer && this._layer.subLayers){
            this._hasSubLayers = true;
        }
        this._layerItemDiv = document.createElement("div");
        this._layerItemDiv.style.position = "absolute";
        this._layerItemDiv.id = this._layer.name + ":LayerItemDiv";
        this._layerItemDiv.style.width = this._width + "px";
        this._layerItemDiv.style.height = this._height + "px";
        //this._layerItemDiv.style.left = this._left;
        this._layerItemDiv.style.top = this._top;
        
        if(this._layerControl){
   	        this._layerItemDiv.style.backgroundColor = this._layerControl._itemBackColor;
   	        this._layerItemDiv.style.color = this._layerControl._itemForeColor;
   	        this._layerItemDiv.style.fontFamily = this._layerControl._itemFont.fontFamily.name;
   	        this._layerItemDiv.style.fontSize = this._layerControl._itemFont.size;
            if(this._layerControl._itemFont.bold){
                this._layerItemDiv.style.fontWeight = "bold";
            }
            if(this._layerControl._itemFont.italic){
                this._layerItemDiv.style.fontStyle = "italic";
            }
            if(this._layerControl._itemFont.underline && this._layerControl._itemFont.strikeout){
                this._layerItemDiv.style.textDecoration = "underline line-through";
            }else if(this._layerControl._itemFont.underline){
                this._layerItemDiv.style.textDecoration = "underline";
            }else if(this._layerControl._itemFont.strikeout){
                this._layerItemDiv.style.textDecoration = "line-through";
            }
        }
        
        if(this._hasSubLayers && this._layerControl){
            var expandCollapseDelegate = Function.createDelegate(this._layerControl, this._layerControl._expandCollapseLayer);
            this.add_expandCollapse(expandCollapseDelegate);
        }
        // 只有SuperMap的图层才有数据集类型
        var layerSettingType = new SuperMap.LayerSettingType();
        if(this._layer){
            this._dataTypeImg = this._getDatasetTypeImg();
            this._layerTypeImg = this._getLayerTypeImg();
        }
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
    
    },
    
    get_layer:function(){
	/// <summary>获取或者设置图层控制子项对应的layer对象。</summary>
   	/// <returns type="Object">get_layer()返回值类型为Object。</returns>
        return this._layer;
    },

    set_layer:function(layer){
        this._layer = layer;
    },

    get_top:function(){
	/// <summary>获取或者设置图层控制子项的上方位置，相对于图层控制控件来说。单位：像素。</summary>
   	/// <returns type="Number">get_top()返回值类型为Number。</returns>
        return this._top;
    },

    set_top:function(top){
        this._top = top;
    },

    get_left:function(){
	/// <summary>获取或者设置图层控制子项的左方位置，相对于图层控制控件来说。单位：像素。</summary>
   	/// <returns type="Number">get_left()返回值类型为Number。</returns>
        return this._left;
    },

    set_left:function(left){
        this._left = left;
    },

    get_layerControl:function(){
	/// <summary>获取或者设置图层控制控件对象。</summary>
   	/// <returns type="SuperMap.UI.LayerControl">get_layerControl()返回值类型为&lt;see cref="T:SuperMap.UI.LayerControl"&gt;LayerControl&lt;/see&gt;。</returns>
        return this._layerControl;
    },

    set_layerControl:function(layerControl){
        this._layerControl = layerControl;
    },

    get_index:function(){
	/// <summary>获取或者设置图层控制子项的索引值。</summary>
   	/// <returns type="Number">get_index()返回值类型为Number。</returns>
        return this._index;
    },

    set_index:function(index){
        this._index = index;
    },

    get_width:function(){
	/// <summary>获取或者设置图层控制子项的宽度。</summary>
   	/// <returns type="Number">get_width()返回值类型为Number。</returns>
        return this._width;
    },

    set_width:function(width){
        this._width = width;
    },

    render:function(){
	/// <summary>呈现图层控制子项。</summary>
        // 加减号 20x20
        if(this._hasSubLayers){
            this._expandButtonImg = "../../images/supGis/collapse.gif";
            this._collapseButtonImg = "../../images/supGis/expand.gif";;
            var expandCollapse = new Image();
            expandCollapse.id = this._layer.name + ":expandCollapse";
            expandCollapse.style.position = "absolute";
            //expandCollapse.style.top = this._top;
            //expandCollapse.style.left = this._left;
            if(this._expanded){
                expandCollapse.src = this._collapseButtonImg;
                expandCollapse.onclick = this._collapseDelegate;
            }else{
                expandCollapse.src = this._expandButtonImg;
                expandCollapse.onclick = this._expandDelegate;
            }

            this._layerItemDiv.appendChild(expandCollapse);
        }

        // 是否可见的checkbox 20x20
   	    var checkVisibleBox = document.createElement("div");
   	    checkVisibleBox.id = this._layer.name + ":V";
        checkVisibleBox.style.position = "absolute";
        checkVisibleBox.style.width = "16px";
        checkVisibleBox.style.height = "16px";
        checkVisibleBox.style.left = 20;//this._left + 20;
        checkVisibleBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
        checkVisibleBox.title = SuperMap.Resource.ins.getMessage("visible");
        
        this._layerItemDiv.appendChild(checkVisibleBox);
        // 不放在后面不能显示
        if(this._layer.visible){
            checkVisibleBox.checked = true;
            checkVisibleBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
        }
        checkVisibleBox.onclick = this._onCheckVisibleBoxClickDelegate;

        // 是否可查的checkbox 20x20
   	    var checkQueryableBox = document.createElement("div");
   	    checkQueryableBox.id = this._layer.name + ":Q";
        checkQueryableBox.style.position = "absolute";
        checkQueryableBox.style.width = "16px";
        checkQueryableBox.style.height = "16px";
        checkQueryableBox.style.left = 40;//this._left + 40;
        checkQueryableBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
        checkQueryableBox.title = SuperMap.Resource.ins.getMessage("query");
        
        this._layerItemDiv.appendChild(checkQueryableBox);
        // 不放在后面不能显示
        if(this._layer.queryable){
            checkQueryableBox.checked = true;
            checkQueryableBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
        }
        checkQueryableBox.onclick = this._onCheckQueryableBoxClickDelegate;

        // 图层类型的图片 20x20
        var layerTypeImg = new Image();
        layerTypeImg.id = this._layer.name + ":LT";
        layerTypeImg.width = "16px";
        layerTypeImg.height = "16px";
        layerTypeImg.style.width = "16px";
        layerTypeImg.style.height = "16px";
        layerTypeImg.style.position = "absolute";
        //layerTypeImg.style.top = this._top;
        layerTypeImg.style.left = 60;//this._left + 60;
        layerTypeImg.src = this._layerTypeImg;
        layerTypeImg.alt = this._layerTypeTitle;
        layerTypeImg.title = this._layerTypeTitle;
        this._layerItemDiv.appendChild(layerTypeImg);
        
        // 数据集类型的图片 20x20
        var dataTypeImg = new Image();
        dataTypeImg.id = this._layer.name + ":DT";
        dataTypeImg.width = "16px";
        dataTypeImg.height = "16px";
        dataTypeImg.style.width = "16px";
        dataTypeImg.style.height = "16px";
        dataTypeImg.style.position = "absolute";
        dataTypeImg.style.left = 80;//this._left + 80;
        dataTypeImg.src = this._dataTypeImg;
        dataTypeImg.alt = this._dataTypeTitle;
        dataTypeImg.title = this._dataTypeTitle;
        this._layerItemDiv.appendChild(dataTypeImg);
		
		
        // layerName 100x20
        var tempLayerName = this._layer.caption ? this._layer.caption : "";
   	    var layerNameSpan = document.createElement("span");
        layerNameSpan.id = tempLayerName + ":SP";
        layerNameSpan.style.height = "20px";
        layerNameSpan.innerHTML = tempLayerName;
        layerNameSpan.style.position = "absolute";
        //layerNameSpan.style.top = this._top;
        layerNameSpan.style.left = 100;
        layerNameSpan.style.whiteSpace = "nowrap";
        layerNameSpan.style.marginTop = "2px";
        this._layerItemDiv.appendChild(layerNameSpan);
        
        if(this._hasSubLayers && this._expanded){
            this._renderSubLayers();
        }
        
        this._container.appendChild(this._layerItemDiv);
    },
    
    _renderSubLayers:function(){
        var tempTop = this._top;
        this._subLayerItemDiv = document.createElement("div");
        this._subLayerItemDiv.style.position = "absolute";
        this._subLayerItemDiv.style.top =  20;
		var subLayerTopIndex = 0;
        for(var i = 0; i < this._layer.subLayers.length; i++){
			if(this._hideThemeLayer && this._layer.subLayers[i].name && this._layer.subLayers[i].name.indexOf("#") != -1) {
				continue;
			}
			
            var layerControlItem = $create(SuperMap.UI.LayerControlItem,{layer:this._layer.subLayers[i], top:subLayerTopIndex * 20, left:10, width: (this._width - 10), layerControl: this._layerControl},null, null, this._subLayerItemDiv);
            layerControlItem.render();
            this._subLayerControlItems.push(layerControlItem);
			subLayerTopIndex++;
        }
        this._layerItemDiv.appendChild(this._subLayerItemDiv);
    },
    
    _clearSubLayerItems:function(){
        if(this._subLayerControlItems){
            for(var i = 0; i < this._subLayerControlItems.length; i++){
                var tempControlItem = this._subLayerControlItems.pop();
                tempControlItem._clearSelfDivs();
            }
            if(this._subLayerItemDiv && this._subLayerItemDiv.parentNode){
                this._subLayerItemDiv.parentNode.removeChild(this._subLayerItemDiv);
            }
        }
    },

    _clearSelfDivs:function(){
        var expandButtonImg = $get(this._layer.name + ":expandCollapse");
        if(expandButtonImg){
            expandButtonImg.parentNode.removeChild(expandButtonImg);
        }

        var layerTypeImg = $get(this._layer.name + ":LT");
        if(layerTypeImg){
            layerTypeImg.parentNode.removeChild(layerTypeImg);
        }
    
        var dataTypeImg = $get(this._layer.name + ":DT");
        if(dataTypeImg){
            dataTypeImg.parentNode.removeChild(dataTypeImg);
        }

        var dataTypeImg = $get(this._layer.name + ":DT");
        if(dataTypeImg){
            dataTypeImg.parentNode.removeChild(dataTypeImg);
        }

        var layerNameSpan = $get(this._layer.name + ":SP");
        if(layerNameSpan){
            layerNameSpan.parentNode.removeChild(layerNameSpan);
        }

        this._layerItemDiv.parentNode.removeChild(this._layerItemDiv);
    },
        
	_expand:function(e){
	    if(this._expanded){
	        return;
	    }
	    e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }
	    var img = $get(this._layer.name + ":expandCollapse");
	    img.src = this._collapseButtonImg;
	    img.onclick = this._collapseDelegate;
        this._expanded = true;
        this._renderSubLayers();
        var args = new Object();
        args.index = this._index;
        args.expanded = false;
        this.raise_expandCollapse(args);
	},

	_collapse:function (e){
	    e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }
	    var img = $get(this._layer.name + ":expandCollapse");
	    img.src = this._expandButtonImg;
	    img.onclick = this._expandDelegate;
        this._expanded = false;
        this._clearSubLayerItems();
        var args = new Object();
        args.index = this._index;
        args.expanded = true;
        this.raise_expandCollapse(args);
	},
	
	_onCheckVisibleBoxClick:function(e){
        e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }

	    var checkVisibleBox = $get(this._layer.name + ":V");
        if(checkVisibleBox.checked){
            checkVisibleBox.checked = false;
            checkVisibleBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
        }else{
            checkVisibleBox.checked = true;
            checkVisibleBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
        }
        this._layer.visible = checkVisibleBox.checked;
        //更新子图层状态
        if(this._hasSubLayers){
	       	 for(var i = 0; i < this._layer.subLayers.length; i++){
	              this._layer.subLayers[i].visible = this._layer.visible;
	              var tempSubCheckBox = $get(this._layer.subLayers[i].name + ":V");
	              if(tempSubCheckBox){
	                  tempSubCheckBox.checked = this._layer.visible;
	                  if(this._layer.visible){
	                      tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible.gif)";
	                  }else{
	                      tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_visible_g.gif)";
	                  }
	              }
	          }        
        }
	},

	_onCheckQueryableBoxClick:function(e){
        e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }

	    var checkQueryableBox = $get(this._layer.name + ":Q");
        if(checkQueryableBox.checked){
            checkQueryableBox.checked = false;
            checkQueryableBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
        }else{
            checkQueryableBox.checked = true;
            checkQueryableBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
        }
        this._layer.queryable = checkQueryableBox.checked;
        
        if(this._hasSubLayers){
	       	 for(var i = 0; i < this._layer.subLayers.length; i++){
	              this._layer.subLayers[i].queryable = this._layer.queryable;
	              var tempSubCheckBox = $get(this._layer.subLayers[i].name + ":Q");
	              if(tempSubCheckBox){
	                  tempSubCheckBox.checked = this._layer.queryable;
	                  if(this._layer.queryable){
	                      tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable.gif)";
	                  }else{
	                      tempSubCheckBox.style.backgroundImage = "url(../../images/supGis/resources/layer_selectable_g.gif)";
	                  }
	              }
	          }        
        }
	},
	
	add_expandCollapse:function(handler){
	/// <summary>添加 expandCollapse 事件。&lt;br&gt;
	/// 该事件在展开/折叠图层控制子项时触发。</summary>
	/// <param name="handler" type="Function" >expandCollapse 事件的处理函数。</param>
		this._addEvent("expandCollapse", handler);
	},
	
	raise_expandCollapse:function(arguments, userContext){
	/// <summary>触发 expandCollapse 事件。&lt;br&gt;
	/// 该事件在展开/折叠图层控制子项时触发。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("expandCollapse", arguments, userContext);
	},
	
	remove_expandCollapse:function(handler){
	/// <summary>移除 expandCollapse 事件。 </summary>
	/// <param name="handler" type="Function" >expandCollapse 事件的处理函数。</param>
		this._removeEvent("expandCollapse", handler);
	},

    clearEvents:function(){
	/// <summary>从图层控制子项中移除所有的事件。</summary>
	
        while(this._eventsNameList.length){
            var eventName = this._eventsNameList.pop();
            this.get_events().removeHandler(eventName, this.get_events().getHandler(eventName));
        }
        this._eventsNameList = null;
    },
    
    _addEvent:function(eventName, handler){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "handler", type: Object}
        ]);
        if (e) throw e;
        
        this.get_events().addHandler(eventName, handler);
        if(this._eventsNameList){
            if(!Array.contains(this._eventsNameList, eventName)){
                this._eventsNameList.push(eventName);    
            }
        }else{
            this._eventsNameList = new Array();
            this._eventsNameList.push(eventName); 
        }
    },

    _removeEvent:function(eventName, handler){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "handler", type: Object}
        ]);
        if (e) throw e;
        
        this.get_events().removeHandler(eventName, handler);
        if(!this.get_events().getHandler(eventName)){
            this._eventsNameList.pop(eventName);
        }
    },

    _raiseEvent:function(eventName, args, userContext){
        var e = Function._validateParams(arguments, [
            {name: "eventName", type: String},
            {name: "args", type: Object, mayBeNull: true, optional: true},
            {name: "userContext", type: String, mayBeNull: true, optional: true}
        ]);
        if (e) throw e;
        
        var handler = this.get_events().getHandler(eventName);
        if (handler) {
            handler(args, userContext);
        }
    },
    
    _getLayerTypeImg:function(){
        var layerTypeImg = "../../images/supGis/resources/theme_none.gif";
        var layerSettingType = new SuperMap.LayerSettingType();
        if(this._hasSubLayers){
            layerTypeImg = "../../images/supGis/resources/layertype_sm.gif";
            this._layerTypeTitle = SuperMap.Resource.ins.getMessage("layerCollection");
        }
        if(this._layer.layerSetting){
            if(this._layer.layerSetting.theme){
                switch (this._layer.layerSetting.theme.themeType){
                    case 1://UNIQUE
                        layerTypeImg = "../../images/supGis/resources/theme_unique.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeUnique");
                        break;
                    case 3://GRAPH
                        layerTypeImg = "../../images/supGis/resources/theme_graph.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeGraph");
                        break;
                    case 2://RANGE
                        layerTypeImg = "../../images/supGis/resources/theme_range.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeRange");
                        break;
                    case 5://DOTDENSITY
                        layerTypeImg = "../../images/supGis/resources/theme_none.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeDotDensity");
                        break;
                    case 7://LABEL
                        layerTypeImg = "../../images/supGis/resources/theme_label.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeLabel");
                        break;
                    case 4://GRADUATEDSYMBOL
                        layerTypeImg = "../../images/supGis/resources/theme_graduatesymbol.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("ThemeGraduatedSymbol");
                        break;
                    case null:
                        layerTypeImg = "../../images/supGis/resources/theme_none.gif";
                        this._layerTypeTitle = SuperMap.Resource.ins.getMessage("noTheme");
                        break;
                    
                }
            }else{
                layerTypeImg = "../../images/supGis/resources/theme_none.gif";
                this._layerTypeTitle = SuperMap.Resource.ins.getMessage("noTheme");
            }

            switch (this._layer.layerSetting.layerSettingType){
                case layerSettingType.wms:
                    layerTypeImg = "../../images/supGis/resources/layertype_wms.gif";
                    this._layerTypeTitle = SuperMap.Resource.ins.getMessage("wmsLayer");
                    break;
                case layerSettingType.wfs:
                    layerTypeImg = "../../images/supGis/resources/layertype_wfs.gif";
                    this._layerTypeTitle = SuperMap.Resource.ins.getMessage("wfsLayer");
                    break;
                case layerSettingType.supermapcollection:
                    layerTypeImg = "../../images/supGis/resources/layertype_sm.gif";
                    this._layerTypeTitle = SuperMap.Resource.ins.getMessage("layerCollection");
                    break;
            }
        }

        return layerTypeImg;
    },

    _getDatasetTypeImg:function(){
        var datasetTypeImg = "../../images/supGis/resources/dataset_unknown.gif";
        var datasetType = new SuperMap.DatasetType();
        var layerSettingType = new SuperMap.LayerSettingType();
        if(this._layer.layerSetting && this._layer.layerSetting.layerSettingType){
            switch (this._layer.layerSetting.layerSettingType){
                    case layerSettingType.wms:
                    case layerSettingType.wfs:
                        datasetTypeImg = "../../images/supGis/resources/datasettype_web.gif";
                        this._dataTypeTitle = "";
                        break;
                    case layerSettingType.supermapcollection:
                        datasetTypeImg = "../../images/supGis/resources/datasettype.gif";
                        this._dataTypeTitle = "";
                        break;
            }
        }
        if(this._layer.layerSetting && this._layer.layerSetting.datasetInfo){
            switch (this._layer.layerSetting.datasetInfo.datasetType){
                case datasetType.point:
                    datasetTypeImg = "../../images/supGis/resources/dataset_point.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetPoint");
                    break;
                case datasetType.line:
                    datasetTypeImg = "../../images/supGis/resources/dataset_line.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetLine");
                    break;
                case datasetType.network:
                    datasetTypeImg = "../../images/supGis/resources/dataset_network.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetNetwork");
                    break;
                case datasetType.region:
                    datasetTypeImg = "../../images/supGis/resources/dataset_region.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetRegion");
                    break;
                case datasetType.text:
                    datasetTypeImg = "../../images/supGis/resources/dataset_text.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetText");
                    break;
                case datasetType.lineM:
                    datasetTypeImg = "../../images/supGis/resources/dataset_line.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetLineM");
                    break;
                case datasetType.image:
                    datasetTypeImg = "../../images/supGis/resources/dataset_image.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetImage");
                    break;
                case datasetType.grid:
                    datasetTypeImg = "../../images/supGis/resources/dataset_grid.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetGrid");
                    break;
                case datasetType.cad:
                    datasetTypeImg = "../../images/supGis/resources/dataset_cad.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetCAD");
                    break;
                case datasetType.tabular:
                    datasetTypeImg = "../../images/supGis/resources/dataset_tabular.gif";
                    this._dataTypeTitle = SuperMap.Resource.ins.getMessage("datasetTabular");
                    break;
		        default:
            }
            
        }
        return datasetTypeImg;
    }
};

SuperMap.UI.LayerControlItem.registerClass('SuperMap.UI.LayerControlItem', Sys.UI.Control, Sys.IDisposable);