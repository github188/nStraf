//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：	    SuperMap.UI.MapControl.js 
// 功能：			Ajax MapControl 类 
// 最后修改时间：	2007-08-31
//========================================================================== 

var resource_error = "地图初始化失败，请检查地图名是否正确，MapHandler 是否已正确配置，远程服务是否已启动。";

Type.registerNamespace('SuperMap.UI');

SuperMap.UI.MapControl = function(container){
    /// <summary>地图控件。&lt;br&gt;
	/// 地图控件主要用于显示地图，并可以对地图进行一些基本的操作，地图的放大、缩小、查询等。&lt;br&gt;
	/// 地图是由多个图层叠加而成的，这些图层包括WMS数据图层，iServer服务提供的地图图层等，地图控件中有一个mapDiv容器，
	/// 该容器中装载了组成地图的所有图层。&lt;br&gt;
	/// 地图控件是基础核心控件，其他控件均是和地图控件实现联动，并更好的辅助地图控件展示效果，如鹰眼控件，图层控制控件等。
	/// </summary>
    /// <param name="container" type="Object" domElement="true">显示自定义图层的图层容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.MapControl">返回 MapControl 对象。</returns>
	/// <field name="customLayer" type="SuperMap.UI.CustomLayer">在地图控件中显示的自定义图层。</field>
	/// <field name="trackingLayer" type="SuperMap.TiledTrackingLayer">跟踪层。</field>
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
    SuperMap.UI.MapControl.initializeBase(this, [container]);
    
    // 初始化参数
    this._params = null;

    this._container = container;
    this._id = this._container.id;
    this._workLayer = document.createElement("div");
    this._workLayer.id = this._id + "_workLayer";
    this._mapDiv = document.createElement("div");
    this._mapDiv.id = this._id + "_mapDiv";
    this._kbInput = document.createElement("input");
    this._kbInput.id = this._id + "kbInput";

    // _mapName要在MapControl上存一份，对比是否和_currentMap上的一样，判断是否切换了地图
    this._mapName = "";
    this._currentMap = null;
    this._overview = null;
    this._magnifier = null;
    
    this._currentBounds = null;
    this._viewBoundsBefore = null;
    this._mapScale = 0.0;
    this._mapScales = null;
    this._mapScalesInfo = null;

    this._spacerTile = "../../images/supGis/spacer.gif";
    //container的位置
    this._x = 0; 
    this._y = 0;
    this._width = 0;
    this._height = 0;
    // 地图当前象素中心点与container中心点的偏移量
    this._originX = 0; 
    this._originY = 0;
    // mapDiv的偏移量
    this._offsetX = 0; 
    this._offsetY = 0;
    // 边界缓存的象素数
    this._buffer = 0;
    
    this._imageBufferSize = 128;
    this._useImageBuffer = true;
    
    this._zoomTotalSteps = 5;
    //地图操作状态标识
    this._zooming = false;
    // 放大到第几步的计数器
    this._zoomCounter = 0;

    this._panning = false;
    // 动态平移的计数器
    this._panCounter = 0;
    // 动态偏移量
    this._panningX = 0;
    this._panningY = 0;
    // 平移之后中心点的xy坐标
    this._panMapX = null;
    this._panMapY = null;

    this._dragging = false;

    // 是否使用动画效果
    this._animationEnabled = true;
    this._keyboardPanning = false;    
    this._keyboardPanDistance = 30;
    this._panStepDistance = 15;
    if(_GetBrowser() != "ie"){
        this._zoomTotalSteps = 3;
        this._keyboardPanDistance = 45;
        this._panStepDistance = 45;
    }

    // 默认实现的Action
    this._zoomInAction = null;
    this._panAction = null;
    // 当前Action
    this._curAction = null;

	this._queryAction = null;
    this._debug = false;
    this._inited = false;
    this._self = this;

    this._onFirstUpdateCompleteDelegate = Function.createDelegate(this, this._onFirstUpdateComplete);
    
    this._mouseDownDelegate = Function.createDelegate(this, this._mouseDown);
    this._mouseUpDelegate = Function.createDelegate(this, this._mouseUp);
    this._mouseMoveDelegate = Function.createDelegate(this, this._mouseMove);
    this._mouseWheelDelegate = Function.createDelegate(this, this._mouseWheel);
	this._mouseScrollDelegate = Function.createDelegate(this, this._mouseScroll);
    this._dblClickDelegate = Function.createDelegate(this, this._dblClick);
    this._contextMenuDelegate = Function.createDelegate(this, this._contextMenu);
    this._clickDelegate = Function.createDelegate(this, this._click);
    this._keyDownDelegate = Function.createDelegate(this, this._keyDown);
    this._keyUpDelegate = Function.createDelegate(this, this._keyUp);

    this._checkTileLoadedDelegate = Function.createDelegate(this, this._checkTileLoaded);
    this._updateMapDelegate = Function.createDelegate(this, this._updateMap);
    this._removeTrackingLayerImageDelegate = Function.createDelegate(this, this._removeTrackingLayerImage);
    this._saveMapParamAndLayersDelegate = Function.createDelegate(this, this._saveMapParamAndLayers);

    this._stepPanDelegate = Function.createDelegate(this, this._stepPan);
    this._stopDynamicPanDelegate = Function.createDelegate(this, this.stopDynamicPan);
    this._setFactorDelegate = Function.createDelegate(this, this._setFactor);
    
    this._eventsNameList = new Array();

    //标识用户的ID
    this._userID = "";
    // 是否正在切换地图
    this._changingMap = false;
    this._switching = false;
    
    this._iTimeoutIDForCheckTileLoaded = null;
    this._iTimeoutIDForUpdateMap = null;
    this._iTimeoutIDForStepPan = null;
    this._iTimeoutIDForSetFactor = null;
    this._iTimeoutIDForDynamicNavigate = null;
	
	this._customLayerContainer = document.createElement("div");
	this._customLayerContainer.id = this._mapDiv.id + "_customLayer_Div";
	this._mapDiv.appendChild(this._customLayerContainer);
	this._customLayerContainer.style.top = "0px";
	this._customLayerContainer.style.left = "0px";
    this.customLayer = new SuperMap.UI.CustomLayer(this._customLayerContainer);
    this.customLayer.mapControl = this;
    this._customLayer_baseZIndex = 11;
    this._customLayer_topZIndex = 20;
    this.trackingLayer = null;
    // 地图历史，以后改进
    this._histroyLength = 7;
    this._historyKey;
    this._bKeepHistory = true;//是否记录历史，pan系列操作都会用到
	this._resourceMessage = new SuperMap.Resource();
};

SuperMap.UI.MapControl.prototype = {
//========================properties start========================
    get_id:function(){
	/// <summary>获取地图控件的id。</summary>
   	/// <returns type="String">get_id()返回值类型为String。</returns>
        return this._id; 
    }, 

    get_params:function(){
	/// <summary>获取或者设置地图参数。&lt;br&gt;
	/// 地图参数主要描述地图的一些基本信息，如地图名称，地图比例尺，地图图片的格式等，
	/// 地图控件与服务端之间通过params传递地图的参数信息。
	/// 目前params的属性包括：&lt;br&gt;
	/// contextPath：当前 Web 应用的虚拟目录。必设参数。例如 SuperMap iServer Java Demo 的 contextPath 为："http://localhost:7080/demo/"&lt;br&gt;
	/// mapName：地图名称。必设参数。&lt;br&gt;
	/// mapServicesAddress：基础地图服务IP地址。必设参数。&lt;br&gt;
	/// mapServicesPort：基础地图服务端口号。必设参数。&lt;br&gt;
	/// mapHandler：出图的 servlet 的地址。&lt;br&gt;
	/// tileSize：格网式图层的每一个格网的大小。&lt;br&gt;
	/// useSimpleCache：是否使用简易缓存策略生成的缓存。&lt;br&gt;
	/// outputContextPath：设置缓存图片的访问路径，默认是"http://localhost:7080/output/"。&lt;br&gt;
	/// mapScale：初始化地图比例尺。&lt;br&gt;
	/// mapScales：地图比例尺多个固定比例尺。&lt;br&gt;
	/// layersKey：标识当前图层的显示状态，是一个哈希码。&lt;br&gt;
	/// imageFormat：图片格式。&lt;br&gt;
	/// x：初始化时候中心点的 x 坐标。&lt;br&gt;
	/// y：初始化时候中心点的 y 坐标。&lt;br&gt;
	/// </summary>
   	/// <returns type="Object">get_params()返回值类型为Object。</returns>
        return this._params; 
    }, 
    
    set_params:function(params){
        this._params = params;
    },

    get_container:function(){
	/// <summary>获取装载地图控件的容器。</summary>
   	/// <returns type="Object">get_container()返回值类型为DOM Object。</returns>
        return this._container;
    }, 

    get_workLayer:function(){
	    return this._workLayer;
    }, 

    get_mapDiv:function(){
	/// <summary>装载地图的所有图层的DOM对象。</summary>
   	/// <returns type="Object">get_mapDiv()返回值类型为DIV Object。</returns>
        return this._mapDiv;
    }, 

    get_mapName:function(){
	/// <summary>获取或者设置地图名称。</summary>
   	/// <returns type="String">get_mapName()返回值类型为String。</returns>
        return this._currentMap.get_mapName();
    }, 

    set_mapName:function(mapName){
        this._currentMap.set_mapName(mapName);
    }, 
    
    set_layers:function(layers){
	/// <summary>当前地图的图层集合。</summary>
        this._currentMap.layers = layers;
    }, 
    
    get_mapScale:function(){
	/// <summary>获取或者设置地图比例尺。</summary>
   	/// <returns type="Number">get_mapName()返回值类型为Number。</returns>
        return this._currentMap.get_mapScale();
    }, 
    
    set_mapScale:function(mapScale){
        var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapScale(mapScale);
        this._mapScale = mapScale;
        this.setMapParam(param);
    },

    get_mapScales:function(){
	/// <summary>获取或者设置地图比例尺多个固定比例尺。</summary>
   	/// <returns type="Array">get_mapName()返回值类型为Array。</returns>
        return this._mapScales;
    }, 
    
    set_mapScales:function(mapScales){
        this._mapScales = mapScales;
    },
     
    get_action:function(){
	/// <summary>获取或者设置地图操作对象。&lt;br&gt;
	/// 地图操作对象完成通过鼠标与地图进行交互的操作，
	/// 如PanAction用来描述通过鼠标进行地图平移操作的类。</summary>
   	/// <returns type="SuperMap.UI.Action">get_action()返回值类型为&lt;see cref="T:SuperMap.UI.Action"&gt;Action&lt;/see&gt;。</returns>
        return this._curAction;
    },
    
    set_action:function(action){
        if(this._curAction && (this._curAction != this._zoomInAction &&  this._curAction != this._panAction)){
            this._curAction.dispose();
        }
        if(action){
			if (action._type=="RectQueryAction"){
				this._queryAction = new SuperMap.UI.RectQueryAction(action._layerNames, action._sqlParam, false, action._onComplete, action._onError);
				this._queryAction._mapRect = action._mapRect;
				this._queryAction._onComplete = action._onComplete;
				this._queryAction.queryParam = action.queryParam;
				this._queryAction._mapControl = this._self;
			} else if(action._type=="CircleQueryAction"){
				this._queryAction = new SuperMap.UI.CircleQueryAction(action._layerNames, action._sqlParam, false, action._onComplete, action._onError);
				this._queryAction._firstMapCoord = action._firstMapCoord;
				this._queryAction.radius = action.radius;
				this._queryAction._onComplete = action._onComplete;
				this._queryAction.queryParam = action.queryParam;
				this._queryAction._mapControl = this._self;
			} else if (action._type=="LineQueryAction"){
				this._queryAction = new SuperMap.UI.LineQueryAction(action._layerNames, action._sqlParam, false, action._onComplete, action._onError);
				this._queryAction._keyPoints = action._keyPoints;
				this._queryAction._onComplete = action._onComplete;
				this._queryAction.queryParam = action.queryParam;
				this._queryAction._mapControl = this._self;
			} else if (action._type=="PolygonQueryAction"){
				this._queryAction = new SuperMap.UI.PolygonQueryAction(action._layerNames, action._sqlParam, false, action._onComplete, action._onError);
				this._queryAction._keyPoints = action._keyPoints;
				this._queryAction._onComplete = action._onComplete;
				this._queryAction.queryParam = action.queryParam;
				this._queryAction._mapControl = this._self;
			} else if (this._queryAction != null){
				this._queryAction.type="unused";
			}
            action.initialize(this._self);
        }
        window.defaultStatus  = action.title;
        this._curAction = action;

        this._setClientActionHidden(this._curAction);
    },

	get_queryAction:function(){
	/// <summary>获取或者设置地图查询操作对象。&lt;br&gt;</summary>
   	/// <returns type="SuperMap.UI.Action">get_action()返回值类型为&lt;see cref="T:SuperMap.UI.Action"&gt;Action&lt;/see&gt;。</returns>
        return this._queryAction;
    },
    
    set_queryAction:function(action){
        if(this._queryAction){
            this._queryAction.dispose();
        }
        if(action){
            action.initialize(this._self);
        }
        this._queryAction = action;
    },

    get_animation:function(){
	/// <summary>获取或者设置一个布尔值，该值表示是否以动画的形式加载地图。</summary>
   	/// <returns type="Boolean">get_animation()返回值类型为Boolean。</returns>
        return this._animationEnabled;
    }, 
    
    set_animation:function(enabled){
        this._animationEnabled = enabled;
    },

    get_debug:function(){
	/// <summary>是否调试。</summary>
   	/// <returns type="Boolean">get_debug()返回值类型为Boolean。</returns>
        return this._debug;
    }, 
    
    set_debug:function(enabled){
        this._debug = enabled;
    },

    get_containerX:function(){
	/// <summary>获取或者设置container位置的x值。</summary>
   	/// <returns type="Number">get_containerX()返回值类型为Number。</returns>
        return this._x;
    }, 
    
    set_containerX:function(x){
        this._x = x;
    },

    get_containerY:function(){
	/// <summary>获取或者设置container位置的y值。</summary>
   	/// <returns type="Number">get_containerY()返回值类型为Number。</returns>
        return this._y;
    }, 
    
    set_containerY:function(y){
        this._y = y;
    },

    get_zoomTotalSteps:function(){
	/// <summary>获取或者设置放大的步数。</summary>
   	/// <returns type="Number">get_zoomTotalSteps()返回值类型为Number。</returns>
        return this._zoomTotalSteps;
    }, 
    
    set_zoomTotalSteps:function(zoomTotalSteps){
        this._zoomTotalSteps = zoomTotalSteps;
    },
//========================properties end========================

//========================initialize start========================
    initialize: function(jsonMapStatus) {
	/// <summary>初始化地图控件。</summary>
	/// <param name="jsonMapStatus" type="Object">MapStatus的JSON对象，描述地图状态信息。</param>
        this._workLayer.style.width = this._container.style.width;
        this._workLayer.style.height = this._container.style.height;
        this._workLayer.style.overflow = "hidden";
        this._workLayer.style.position = "relative";
        this._container.appendChild(this._workLayer);
	    if(this._params.mapScales != null){
	        if(this._params.mapScales.length > 0){
	            this._mapScales = this._params.mapScales;    	        
	        }
	    }
	    if(this._params.mapScalesInfo != null){
	        this._mapScalesInfo = this._params.mapScalesInfo;
	    }
	    if(this._params.imageFormat){
            this._imageFormat = this._params.imageFormat;
        }else{
            this._params.imageFormat = this._imageFormat;
        }
	    if(this._params.mapName){
            this._self.mapName = this._mapName = this._params.mapName;
        }else{
            function ongetMapNamesComplete(jsonNames){
                var mapNames =eval(jsonNames);
                this._params.mapName = mapNames[0];
            }
            var ongetMapNamesCompleteDelegate = Function.createDelegate(this, ongetMapNamesComplete);
            SuperMap.Committer.commitAjax("", SuperMap.Committer.handler, "getmapnames", [], [], true, ongetMapNamesCompleteDelegate);
        }
	    if(this._params.mapScale){
            this._mapScale = this._params.mapScale;
        }else{
            this._params.mapScale = "";
        }
	    if(this._params.useImageBuffer){
            this._useImageBuffer = this._params.useImageBuffer;
        }else{
            this._params.useImageBuffer = this._useImageBuffer;
        }
	    
	    this.add_viewChanged(this._saveMapParamAndLayersDelegate);
        this.add_startZoom(this._removeTrackingLayerImageDelegate);

	    this._params.id = this._container.id;

        this._currentMap = new SuperMap.Map(this._params);
        var _mapStatus = null;
        
      	var onMapInitDelegate = Function.createDelegate(this, this.onMapInit);
        // 允许传初始值进来，少一次调用
        if(!jsonMapStatus){
            _mapStatus = this._currentMap.initialize(onMapInitDelegate);
        }else{
            _mapStatus = this._currentMap._onInitComplete(jsonMapStatus);
            onMapInitDelegate(jsonMapStatus);
        }
    },

    onMapInit:function(jsonMapStatus) {
	/// <summary>初始化 Map 之后对地图控件的设置。例如：中心点、比例尺、图层等信息。</summary>
	/// <param name="jsonMapStatus" type="Object">MapStatus 的 JSON 对象，描述地图状态信息。</param>
        var onTrackingLayerChangedDelegate = Function.createDelegate(this, this._onTrackingLayerChanged);
        this._currentMap.add_trackinglayerChanged(onTrackingLayerChangedDelegate);
        
        if(jsonMapStatus){
            if(this._changingMap){
                this._params.mapBounds=jsonMapStatus.mapBounds;
                this._params.x=(jsonMapStatus.referViewBounds.rightTop.x + jsonMapStatus.referViewBounds.leftBottom.x) * 0.5;
                this._params.y=(jsonMapStatus.referViewBounds.rightTop.y + jsonMapStatus.referViewBounds.leftBottom.y) * 0.5;
            }
            this._mapName = jsonMapStatus.mapName;
            this._params.mapName = jsonMapStatus.mapName;
		    this._inited = true;
	        if(!this._params.layers){
	            //切换地图this._params.layers为null;
	            this._params.layers = jsonMapStatus.layers;
	        }
	        if(jsonMapStatus.layersKey){
	        	this._params.layersKey = jsonMapStatus.layersKey;
	        }
	    }
	    this._currentMap.layers = this._params.layers;
	    
	    if(!this._changingMap){
	    }

	    this._initEvents();

	    if(!this._inited){ 
	        this._raiseEvent("onerror", Sys.EventArgs.Empty);
		    return false;
	    }
	    SuperMap.Utility.enableVML();
	    this.update(false, this._onFirstUpdateCompleteDelegate, null, null);
	    
	    this._currentMap.add_layerChanged(this._updateMapDelegate);
    },    
//========================initialize end========================           
    
//========================dispose start========================           
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
        this._raiseEvent("ondisposing", Sys.EventArgs.Empty);
        this._clearTimeout();
	    this._inited = false;
	    
        if(!this._params.fixedView && this._container && this.get_events()){
            if(this.get_events().getHandler("mousedown")){
                $removeHandler(this._container, "mousedown", this._mouseDownDelegate);
            }
            if(this.get_events().getHandler("mouseup")){
                $removeHandler(this._container, "mouseup", this._mouseUpDelegate);
            }
            if(this.get_events().getHandler("mousemove")){
                $removeHandler(this._container, "mousemove", this._mouseMoveDelegate);
            }
            if(this.get_events().getHandler("mousewheel")){
                $removeHandler(this._container, "mousewheel", this._mouseWheelDelegate);
            }
            if(this.get_events().getHandler("dblclick")){
                $removeHandler(this._container, "dblclick", this._dblClickDelegate);
            }
            if(this.get_events().getHandler("contextmenu")){
                $removeHandler(this._container, "contextmenu", this._contextMenuDelegate);
            }
            if(this.get_events().getHandler("click")){
                $removeHandler(this._container, "click", this._clickDelegate);
            }
            if(this.get_events().getHandler("keydown")){
                $removeHandler(this._kbInput, "keydown", this._keyDownDelegate);
            }
            if(this.get_events().getHandler("keyup")){
                $removeHandler(this._kbInput, "keyup", this._keyUpDelegate);
            }
			if(document.addEventListener) {
				this._container.removeEventListener("DOMMouseScroll", this._mouseScrollDelegate, true);
			}
        }
        this._kbInput = null;
        
        if(this._zoomInAction){
            this._zoomInAction.dispose();
            this._zoomInAction=null;
        }
        if(this._panAction){
            this._panAction.dispose();
            this._panAction = null;
        }
	    if(this._curAction){
            this._curAction.dispose();
            this._curAction = null;
        }
		if(this._queryAction){
            this._queryAction.dispose();
            this._queryAction = null;
        }
        if(this._logo){
            this._logo.dispose();
            this._logo = null;
        }
        this.customLayer.clearMarkers();
        this.customLayer.markers = null;
        this.customLayer.clearLines();
        this.customLayer.lines = null;
        this.customLayer.clearPolygons();
        this.customLayer.polygons = null;
        this.customLayer = null;
        
        this._clearTrackingLayerTiles(true);
        //清除高亮层
        var img = $get(this._container.id + "_TrackingLayer");
        if(img){
            img.parentNode.removeChild(img);
            img = null;
        }
        
        //要后dispose        
        if(this._currentMap){
            this._currentMap.dispose();
            this._currentMap = null;
        }

        this._clearEvents();
        this._mapDiv = this.workLayer = this._container = this._self = null;
    },
//========================dispose end========================           

//========================provider start========================
    getDataManager:function(){
	/// <summary>获取数据处理操作对象。</summary>
	/// <returns type="SuperMap.DataManager">返回一个&lt;see cref="T:SuperMap.DataManager"&gt;DataManager&lt;/see&gt;对象。</returns>
		if(!this.dataManager){
			var handlerUrl =  this.getMap()._params.contextPath + "datahandler";
			this.dataManager = new SuperMap.DataManager(handlerUrl, this._mapName);
		}
		
        return this.dataManager;
    },
	
    getSpatialAnalyst:function(){
	/// <summary>获取空间分析操作对象。</summary>
	/// <returns type="SuperMap.SpatialAnalystManager">返回一个&lt;see cref="T:SuperMap.SpatialAnalystManager"&gt;SpatialAnalystManager&lt;/see&gt;对象。</returns>
		var queryUrl = this.getMap()._params.contextPath + "spatialanalysthandler";
        if(!this.spatialAnalyst){
			this.spatialAnalyst = new SuperMap.SpatialAnalystManager(queryUrl, this._mapName);
		}
		
		return this.spatialAnalyst;
    },
	
	getNetworkAnalyst:function(){
	/// <summary>获取网络分析操作对象。</summary>
	/// <returns type="SuperMap.NetworkAnalystManager">返回一个&lt;see cref="T:SuperMap.NetworkAnalystManager"&gt;NetworkAnalystManager&lt;/see&gt;对象。</returns>
		var queryUrl = this.getMap()._params.contextPath + "commonhandler";
        if(!this.networkAnalyst){
			this.networkAnalyst = new SuperMap.NetworkAnalystManager(queryUrl, this._mapName);
		}
		
		return this.networkAnalyst;
    },
    
    getMap:function(){
	/// <summary>获取Map对象。</summary>
	/// <returns type="SuperMap.Map">返回一个&lt;see cref="T:SuperMap.Map"&gt;Map&lt;/see&gt;对象。</returns>
        return this._currentMap;
    },

    getOverviewManager:function(mapName){
	/// <summary>获取鹰眼操作对象。</summary>
	/// <param name="mapName" type="String">鹰眼地图的名字。</param>
	/// <returns type="SuperMap.OverviewManager">返回一个&lt;see cref="T:SuperMap.OverviewManager"&gt;OverviewManager&lt;/see&gt;对象。</returns>
        if(!this._overview){
            if(mapName){
                this._overview = new SuperMap.OverviewManager(this.getMap()._params.commonHandler, mapName);
            }else{
                this._overview = new SuperMap.OverviewManager(this.getMap()._params.commonHandler, this.get_mapName());
            }
        }
        return this._overview;
    },

    getMagnifierManager:function(){
	/// <summary>获取放大镜操作对象。</summary>
	/// <returns type="SuperMap.MagnifierManager">返回一个&lt;see cref="T:SuperMap.MagnifierManager"&gt;MagnifierManager&lt;/see&gt;对象。</returns>
	    if(!this._magnifier){
            this._magnifier = new SuperMap.MagnifierManager(this.getMap()._params.commonHandler, this.get_mapName());
        }
        return this._magnifier;
    },
//========================provider end========================

//========================public methods start========================
    update:function(bRefresh, onComplete, onError, userContext){
	/// <summary>更新地图。</summary>
	/// <param name="bRefresh" type="Boolean">地图更新完成后，客户端是否同时更新地图。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String layersKey, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
        var _bRefresh = bRefresh;

        //判断地图名是否改变
        if(this._mapName != this._currentMap.get_mapName()){
            if(this._switching){
                return;
            }
            this._switching = true;
            //地图已被切换
            this._params.mapName = this._mapName;
            this._params.x = null;
            this._params.y = null;
            this._params.mapScale = null;
            this._params.mapScales = null;
            this._params.layers = null;
            while(this._mapScales && this._mapScales.length > 0){
                this._mapScales.pop();
            }
            if(this._mapScalesInfo){
                if(this._mapScalesInfo[this._mapName]){
                    var mapScalesReciprocal = this._mapScalesInfo[this._mapName].split(',');
                    for(var i = 0; i < mapScalesReciprocal.length; i++){
                        this._mapScales.push(1 / parseFloat(mapScalesReciprocal[i]));
                    }
                }
            }
            var inputLayersKey = $get(this._container.id + "_hiddenLayersKey");
            if(inputLayersKey){
                inputLayersKey.value = "" + this._mapName;
            }
            
            try{
                this._switchMap();
                return;
            } catch(e) {
                if(onError){
                    onError(inputLayersKey,userContext);
                }
                return
            }
            //切换地图成功
            if(onComplete){
                onComplete(inputLayersKey,userContext);
            }
            return;
        }else{
            if(this._changingMap){
                function _updateComplete(layersKey, userContext){
                    if(bRefresh){
                        this._refreshMap();
                    }
	                if(onComplete){
	                    onComplete(layersKey, userContext); 
	                }
	                this._switching = false;
                }
                
                var updateCompleteDelegate = Function.createDelegate(this, _updateComplete);
                this._currentMap.update(updateCompleteDelegate,onError,userContext);
            }else{
                setTimeout(onComplete, 1); 
            }
        }
    },

    getMapParam:function(){
	/// <summary>获取地图参数对象-MapParam。&lt;br&gt;
	/// 地图参数-MapParam描述了当前地图的状态，包括当前地图的显示比例尺，当前地图名称，显示范围等。&lt;br&gt;
	/// 当地图发生变化时，可以通过这个方法获取最新的地图参数信息，以便更新客户端的地图状态。
	///</summary>
	/// <returns type="SuperMap.MapParam">返回地图参数对象-&lt;see cref="T:SuperMap.MapParam"&gt;MapParam&lt;/see&gt;。</returns>
        return this._currentMap.get_mapParam().makeCopy();
    },

    setMapParam:function(mapParam){
	/// <summary>设置地图参数对象-MapParam。&lt;br&gt;
	/// 当要执行某些地图操作时，地图参数需要做相应的变更，该方法用来设置变更后的地图参数信息。
	///</summary>	
	/// <param name="mapParam" type="Object">地图参数对象。
    /// </param>
    	if(this._zooming || this._panning || this._dragging){
            return;
        }
    	var center = mapParam.center;
    	if(mapParam.getViewType() == "mr"){
            center = mapParam.mapRect.center();
        }
    	this._viewBoundsBefore = this.getViewBounds();
		if(!this._params.disableMapLayer) {
			this._container.style.backgroundColor = "#eeeeee";
		}
    	
    	mapParam.resolve(this._currentMap, this._width, this._height);
    	center = mapParam.center;
    	
    	// 会把_currentMap的_mapScale的值设置成mapParam的，在划分比例尺级别的时候可能和最初的referMapScale不一样
    	this._currentMap.validateMapScale(mapParam);
    	
    	this._currentBounds = this._currentMap.getBounds();
        var testPoint = this._ensureWithinBounds(mapParam, this._currentBounds);
    	mapParam.resolve(this._currentMap, this._width, this._height);
    	if(mapParam.equals(this._currentMap.get_mapParam())){
    	    this._onGetTrackingLayerImageComplete("ViewHistory");
    	    return;
    	}
		var deltaX, deltaY;
		var pixelCenterX = 0;
		var pixelCenterY = 0;
		if(this._currentMap.get_mapParam().pixelCenter != null){
			pixelCenterX = this._currentMap.get_mapParam().pixelCenter.x;
			pixelCenterY = this._currentMap.get_mapParam().pixelCenter.y;
		}
    	var deltaX = mapParam.getPixelX(this._currentMap.get_mapParam().mapScale) - pixelCenterX;
        var deltaY = mapParam.getPixelY(this._currentMap.get_mapParam().mapScale) - pixelCenterY;
    	var distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    	var panOnly = (distance < this._width && distance < this._height) &&
            (mapParam.mapScale == this._currentMap.get_mapParam().mapScale) && this._animationEnabled;
    	if(panOnly){
    		this.panToMapCoord(new SuperMap.Point2D(center.x, center.y));
    		return;
    	}
    	
    	if(this._currentMap.get_mapScale() != undefined && this._currentMap.get_mapScale() != mapParam.mapScale){
            this._currentMap.set_mapScale(mapParam.mapScale);
            this.raise_startZoom(Sys.EventArgs.Empty);
            this._zooming = true;
            this._setIsZooming();
        }

    	var dynamic = (distance < this._width && distance < this._height) && this._animationEnabled;
    	if(dynamic){
    	    this._offsetX = testPoint.x - this._originX - this._width / 2;
            this._offsetY = testPoint.y - this._originY - this._height / 2;
    	    this._mapDiv.style.top = -this._offsetY + "px";
            this._mapDiv.style.left = -this._offsetX + "px";
    		var oldOriginX = this._originX + this._offsetX;
            var oldOriginY = this._originY + this._offsetY;
    		var oldZoom = this._currentMap.get_mapParam().mapScale;
    		var newOriginX = SuperMap.Utility.round(mapParam.pixelCenter.x-this._width/2);
            var newOriginY = SuperMap.Utility.round(mapParam.pixelCenter.y-this._height/2);
    		var newZoom = mapParam.mapScale;
    		
            this._currentMap.prepareBaseTile(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom);
            
            this._currentMap.get_mapParam().dispose();
            this._currentMap.set_mapParam(mapParam);

    		this._startMap();
            
            this._currentMap.prepareSwapTile(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom);

    		this._zoomCounter = 1;
    		this._setZoomCounter();
    		
    		for(var i = 0; i < this.customLayer.markers.length; i++){
    		    this.customLayer.markers[i].prepareForZoom(this._offsetX, this._offsetY, newOriginX, newOriginY, newZoom);
    		}
            this.customLayer.repositionPolygons();
            this.customLayer.repositionLines();
    		this.customLayer.repositionCircles();
    		this._setFactor();
    		return;
    	}

        this._currentMap.backupLayers();
		
        this._currentMap.get_mapParam().dispose();
        this._currentMap.set_mapParam(mapParam);
        this._startMap();
        this._swapStates();
        this.customLayer.repositionMarkers();
        this.customLayer.repositionPolygons();
        this.customLayer.repositionLines();
        this.customLayer.repositionCircles();
    },

    getContainerX:function (){
	/// <summary>获取装载地图控件的容器的左边界的像素值。
	///</summary>
	/// <returns type="Number">返回装载地图控件的容器的左边界的像素值。</returns>
        var strLeft = this._container.style.left;
		strLeft = strLeft.replace("px", "");
		strLeft = strLeft.replace("pt", "");
        return eval(strLeft);
    },

    getContainerY:function(){
	/// <summary>获取装载地图控件的容器的上边界的像素值。
	///</summary>
	/// <returns type="Number">返回装载地图控件的容器的上边界的像素值。</returns>
        var strTop = this._container.style.top;
		strTop = strTop.replace("px", "");
		strTop = strTop.replace("pt", "");
        return eval(strTop);
    },

    getMapBounds:function(){
	/// <summary>获取当前地图的空间范围。&lt;br&gt;
	/// 地图的空间范围是其所显示的各数据集的范围的最小外接矩形，
	/// 即包含各数据集范围的最小的矩形。当地图显示的数据集增加或删除时，其空间范围也会相应发生变化。
	/// </summary>
	/// <returns type="SuperMap.Rect2D">返回地图的空间范围，&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;类型。</returns>
    	return this._currentMap.getMapBounds();
    },

    getViewBounds:function(){
	/// <summary>获取当前的可视范围。</summary>
	/// <returns type="SuperMap.Rect2D">返回地图的可视范围，&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;类型。</returns>
        return this._currentMap.getViewBounds(this._width, this._height);
    },

    getMapCenter:function(){
	/// <summary>获取当前地图的中心点。单位：地理坐标。</summary>
	/// <returns type="SuperMap.Point2D">返回当前地图的中心点，&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;类型。</returns>
        return this._currentMap.get_mapParam().center;
    },

    getPixelCenter:function(){
	/// <summary>获取当前地图的中心点。单位：像素坐标。</summary>
	/// <returns type="SuperMap.Point">返回当前地图的中心点，&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</returns>
        return this._currentMap.get_mapParam().pixelCenter;
    },

    getOffset:function(){	
	/// <summary>获取当前地图原点与初始化原点之间的偏移量。</summary>
	/// <returns type="SuperMap.Point">返回map对象初始化时偏移的坐标值，&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</returns>
        return new SuperMap.Point(this._offsetX,this._offsetY);
    },

    getOrigin:function(){	
	/// <summary>获取map对象初始化时原点坐标值，Point类型。
	/// </summary>
	/// <returns type="SuperMap.Point">返回map对象初始化时原点坐标值，&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</returns>
        return new SuperMap.Point(this._originX,this._originY);
    },

    getSize:function(){
	/// <summary>获取地图控件Mapcontrol的尺寸（宽度和高度），像素单位。
	/// </summary>
	/// <returns type="SuperMap.Rect">返回地图控件Mapcontrol的尺寸（宽度和高度），&lt;see cref="T:SuperMap.Rect"&gt;Rect&lt;/see&gt;类型。</returns>
        return new SuperMap.Rect(0, 0, this._width, this._height);
    },
    
    resize:function(width, height){
	/// <summary>重新设置地图控件的尺寸。
	/// </summary>
	/// <param name="width" type="Number">地图控件的宽度，单位:像素。</param>
	/// <param name="height" type="Number">地图控件的高度，单位:像素。</param>
    	if((!width || width <= 0) && (!height || height <= 0)){
            return;
        }
    	if(width && width > 0){
            this._width = width;
        }
        if(height && height > 0){
            this._height = height;
        }
    	this._container.style.width = width + "px";
        this._container.style.height = height + "px";
    	this._workLayer.style.width = width + "px";
        this._workLayer.style.height = height + "px";
    	this.panToMapCoord(this._currentMap.get_mapParam().center);
    	if(this._logo){
            this._logo.reposition();
        }
    	this.raise_resize(Sys.EventArgs.Empty);
    },

    mapCoordToPixel:function(point2D){
	/// <summary>将地图中指定点的地图坐标转换为像素坐标。</summary>
	/// <param name="point2D" type="SuperMap.Point2D">要转换的地图坐标点，Point2D类型。</param>
	/// <returns type="SuperMap.Point">返回像素坐标。&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型</returns>
        var pc = this._currentMap.mapCoordToPixel(point2D, this.get_mapScale());
        pc.x -= this._originX + this._offsetX;
        pc.y -= this._originY + this._offsetY;
        return pc;
    },
    
    pixelToMapCoord:function(point){
	/// <summary>将地图中指定点的像素坐标转换为地图坐标。</summary>
	/// <param name="point" type="SuperMap.Point">要转换的像素坐标，Point类型。</param>
	/// <returns type="SuperMap.Point2D">返回地图坐标。&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;类型</returns>
        var pj = new SuperMap.Point(point.x + this._originX + this._offsetX,
                                    point.y + this._originY + this._offsetY);
        var mc = this._currentMap.pixelToMapCoord(pj, this._currentMap.get_mapParam().mapScale);
        return mc;
    },
    
    pixelToMapDistance:function(pixelDistance,mapScale){
	/// <summary>将地图中像素单位的长度转换为投影坐标系下的长度。</summary>
	/// <param name="pixelDistance" type="Number">要转换的像素单位的长度。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="Number">返回投影坐标系下的长度。</returns>
        return this._currentMap.pixelToMapDistance(pixelDistance, mapScale);
    },

    pan:function(deltaX, deltaY){  
    /// <summary>平移地图。</summary>
	/// <param name="deltaX" type="Number">地图平移的x向偏移量。</param>
	/// <param name="deltaY" type="Number">地图平移的y向偏移量。</param>                                       
    	if(deltaX == 0 && deltaY == 0){
    	    //此时不会触发onviewchanged事件
    	    //所以要看看是否是查看历史，如果是,则需要触发一下onviewchanged，去切换一下TrackingLayer
    	    if(this._params.bViewHistory){
    	        this.raise_viewChanged(Sys.EventArgs.Empty);
    	    }
    	    return;
    	}
    	this._panning = true;
    	this._panWithinBounds(this._currentMap.get_mapParam(), this._currentBounds, deltaX, deltaY);
    	this._offsetX = this._currentMap.get_mapParam().pixelCenter.x - this._originX - this._width / 2;
        this._offsetY = this._currentMap.get_mapParam().pixelCenter.y - this._originY - this._height / 2;
    	if (this._offsetY != NaN && this._offsetX != NaN)
		{
			this._mapDiv.style.top = -this._offsetY + "px";
	        this._mapDiv.style.left = -this._offsetX + "px";
		}

        if(this.customLayer.markers && this.customLayer.markers.length > 0) {
            this.customLayer.repositionMarkers();
        }
		if(this.customLayer.lines && this.customLayer.lines.length > 0) {
            this.customLayer.repositionLines();
        }
		if(this.customLayer.polygons && this.customLayer.polygons.length > 0) {
			this.customLayer.repositionPolygons();
		}
		if(this.customLayer.circles && this.customLayer.circles.length > 0) {
    		this.customLayer.repositionCircles();
		}

		this._updateMap();
		this._panning = false;
    },

    dynamicPan:function(deltaX, deltaY, count, keyboardPan){
	/// <summary>动态平移地图。</summary>
	/// <param name="deltaX" type="Number">地图平移的x向偏移量。</param>
	/// <param name="deltaY" type="Number">地图平移的y向偏移量。</param>   
	/// <param name="count" type="Number"></param>
	/// <param name="keyboardPan" type="Boolean">是否采用键盘移动地图的方式。True表示使用键盘执行地图平移的操作。</param>            
        //动态平移时不记录中间历史
        this._bKeepHistory = false;
    	if(this._zooming){
            return;
        }
        if(!count){
            count=-1;
        }
        this._panningX = deltaX;
        this._panningY = deltaY;
        this._panCounter = count;
    	if(!deltaX && !deltaY){
            this.stopDynamicPan();
            return;
        }
    	this._keyboardPanning = keyboardPan;
    	if(!this._panning){
            this._panning = true;
            this._stepPan();
            this.raise_startDynamicPan(Sys.EventArgs.Empty);
        }
    },    

    stopDynamicPan:function (){
	/// <summary>停止动态平移地图。</summary>
    	//动态平移完成，需要记录一次历史
        this._bKeepHistory = true;
    	if(this._panMapX != null && this._panMapY != null){
    		var mc = new SuperMap.Point2D(this._panMapX, this._panMapY);
            var pc = this._currentMap.mapCoordToPixel(mc, this._currentMap.get_mapParam().mapScale);
    		var dx = pc.x - (this._originX + this._offsetX + this._width / 2);
            var dy = pc.y - (this._originY + this._offsetY + this._height / 2);
    		this.pan(dx, dy);
            this._panMapX = null;
            this._panMapY = null;
    	}
    	
    	this._computeCenterPoint(true);
    	if(this._debug){
            window.status = "this._panning:" + this._panning;
        }
//    	if(this._panning){
            this.raise_endDynamicPan(Sys.EventArgs.Empty);
    		this.raise_changeView(Sys.EventArgs.Empty);
    		this._iTimeoutIDForCheckTileLoaded = setTimeout(this._checkTileLoadedDelegate, 100);
//    	}
    	this._panningX = 0;
        this._panningY = 0;
        this._panning = false;
        this._keyboardPanning = false;
    },

    panToMapCoord:function(point2D){
	/// <summary>平移地图到指定的位置。</summary>
	/// <param name="point2D" type="SuperMap.Point2D">平移后地图中心点的位置。SuperMap.Point2D类型。</param>	
        if (point2D != null)
        {
			if(point2D.x){
            this._panMapX = point2D.x;
        }
        if(point2D.y){
            this._panMapY = point2D.y;
        }
        this.panToPixelCoord(this._currentMap.mapCoordToPixel(point2D, this._currentMap.get_mapParam().mapScale));
        }
    },

    panToPixelCoord:function(point){
	/// <summary>平移地图到指定的位置。</summary>
	/// <param name="point"  type="SuperMap.Point">平移后地图中心点的位置。SuperMap.Point类型。</param>
    	if (point == null || point.x == null || point.y == null)
		{
			return null;
		}
		var dx = point.x - (this._originX + this._offsetX + this._width / 2);
        var dy = point.y - (this._originY + this._offsetY + this._height / 2);
    	var distance = Math.sqrt(dx * dx + dy * dy);
    	if(!this._animationEnabled || SuperMap.Utility.abs(dx) > 2 * this._width || SuperMap.Utility.abs(dy) > 2 * this._height ||
            distance > 2.5 * Math.sqrt(this._width * this._width + this._height * this._height)){
    		var param = this._currentMap.get_mapParam().makeCopy();
    		param.setPixelCenter(point);
    		this.setMapParam(param);
    		return;
    	}
    	var dt=Math.atan2(dy,dx);
    	var count = SuperMap.Utility.ceil(distance / this._panStepDistance);
    	var actualStepDistance = SuperMap.Utility.round(distance / count);
    	dx = SuperMap.Utility.round(Math.cos(dt) * actualStepDistance);
        dy = SuperMap.Utility.round(Math.sin(dt) * actualStepDistance);
        
        if(!dx){
            dx = 0;
        }
        if(!dy){
            dy = 0;
        }
    	this.dynamicPan(dx, dy, count, false);
    },

    viewEntire:function(){
	/// <summary>全幅显示地图。</summary>
        var param = this._currentMap.get_mapParam().makeCopy();
        var mapBounds = this.getMapBounds();
        var viewBounds = this.getViewBounds();
        var widthRatio = mapBounds.width() / viewBounds.width();
        var heightRatio = mapBounds.height() / viewBounds.height();
        var ratio = widthRatio > heightRatio ? widthRatio : heightRatio;
        var mapScale = param.mapScale/ratio;
        param.setMapScale(mapScale);
        param.setMapCenter(mapBounds.center());

    	var deltaX = param.getPixelX(this._currentMap.get_mapParam().mapScale) - this._currentMap.get_mapParam().pixelCenter.x;
        var deltaY = param.getPixelY(this._currentMap.get_mapParam().mapScale) - this._currentMap.get_mapParam().pixelCenter.y;
    	var distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
       	var panOnly = (distance < this._width && distance < this._height) &&
            (param.mapScale == this._currentMap.get_mapParam().mapScale) && this._animationEnabled;
    	if(panOnly){
    	    this.panToMapCoord(new SuperMap.Point2D(param.center.x, param.center.y));
    	}else{
            this.setMapParam(param);
    	}
    },
    
    viewByBounds:function(rect2D){
	/// <summary>显示指定范围的地图。</summary>
	/// <param name="rect2D"  type="SuperMap.Rect2D">要显示的地图范围。SuperMap.Rect2D类型。</param>
        var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapRect(rect2D);
        this.setMapParam(param);
    },
    
    viewByPoint:function(point2D){
	/// <summary>移动地图使指定的点显示出来。当指定的点已经在当前的地图范围内，则不做任何操作，如果
	/// 指定的点不在地图显示范围内，则平移地图，直至指定的点在地图的中心点位置上。</summary>
	/// <param name="point2D"  type="SuperMap.Point2D">要显示的点。SuperMap.Point2D类型。</param>
    	var viewBounds = this.getViewBounds();
    	var oldWidth = viewBounds.width();
        var oldHeight = viewBounds.height();
    	viewBounds.leftBottom.x += oldWidth * 0.1;
        viewBounds.leftBottom.y += oldHeight * 0.1;
    	viewBounds.rightTop.x -= oldWidth * 0.1;
        viewBounds.rightTop.y -= oldHeight * 0.1;
    	if(viewBounds.contains(point2D)){
            return;
        }
    	var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapCenter(point2D);
        this.setMapParam(param);
    },
    
    viewByPoints:function(point2Ds){
	/// <summary>设置最佳的地图可视范围，使指定的坐标数组都在地图可视范围内。
	/// 同时地图显示的范围为这些点组成的最大外切矩形。</summary>
	/// <param name="pint2Ds" type="Array" elementType="SuperMap.Point2D">要显示的点数组。SuperMap.Point2D数组类型。</param>
    	if(!point2Ds || point2Ds.constructor != Array){
            return;
        }
        var a = point2Ds[0].x;
        var b = point2Ds[0].y;
        var c = a;
        var d = b;
        for(var i=1;i<point2Ds.length;i++){
            a = SuperMap.Utility.min(a,point2Ds[i].x);
            b = SuperMap.Utility.min(b,point2Ds[i].y);
            c = SuperMap.Utility.max(c,point2Ds[i].x);
            d = SuperMap.Utility.max(d,point2Ds[i].y);
        }
        var dx = (c-a) * 0.1;
        var dy = (d-b) * 0.1;
        a -= dx;
        b -= dy;
        c += dx;
        d += dy;
        var rect2D = new SuperMap.Rect2D(this._clipMapX(a), this._clipMapY(b),
                                        this._clipMapX(c), this._clipMapY(d));
        this.viewByBounds(rect2D);
    },

    zoomIn:function(){
	/// <summary>2倍放大地图。</summary>
        var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapScale(this._currentMap.get_mapParam().mapScale * 2);
        this.setMapParam(param);
    },
    
    zoomOut:function(){
	/// <summary>缩小地图为原来的1/2。</summary>
        var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapScale(this._currentMap.get_mapParam().mapScale / 2);
        this.setMapParam(param);
    },

    zoom:function(ratio){
	/// <summary>按缩放比率缩放地图，中心点保持不变。</summary>
	/// <param name="ratio"  type="Number">缩放比率。</param>
        var param = this._currentMap.get_mapParam().makeCopy();
        param.setMapScale(this._currentMap.get_mapParam().mapScale * ratio);
        this.setMapParam(param);
    },

    setCenterAndZoom:function(point2D, mapScale){
	/// <summary>获取地图，使地图的中心点和比例尺为指定的值。 </summary>
	/// <param name="point2D" type="SuperMap.Point2D">新地图的中心点。</param>
	/// <param name="mapScale" type="Number">>新地图的比例尺。</param>
		var param = this._currentMap.get_mapParam().makeCopy();
		param.setMapCenter(point2D);
		param.setMapScale(mapScale? mapScale: this._mapScale);
		this.setMapParam(param);
    },

    switchMap:function(mapName){
	/// <summary>切换地图。 </summary>
	/// <param name="mapName" type="String">新地图的名字。</param>
        if(mapName){
            this._params.mapName = mapName;
            this._switchMap();
        }else{
            //alert("传入的地图名非法");
			alert(SuperMap.Resource.ins.getMessage("mapNameIllegal"));
        }
    },
    
    clearHighlight:function(onComplete, onError, userContext){
	/// <summary>清除高亮对象。</summary>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(Boolean succeed, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
        this._clearTrackingLayerTiles(true);
        this._currentMap.clearHighlight(onComplete, onError, userContext);
    },
    
    keepCurrentImage:function(){
	/// <summary>保持当前地图，在后续的操作中将不返回地图图片，并且不会将后续的操作加入到历史记录中， 
	/// 直到调用了 RefreshCurrentImage() 方法之后。&lt;br&gt;
	/// 调用本方法之后，后续的操作将不会加入到历史记录中，此时如果调用 ViewPrevious() 、ViewNext() 将 不会得到预期的结果。
	/// 所以，请不要在 KeepCurrentImage() 之后、RefreshCurrentImage() 之前进行历史 回溯操作。
	/// </summary>
        this._bKeepHistory = false;
    },

    refreshCurrentImage:function(){
	/// <summary>更新当前地图，在后续的操作中将返回地图图片，并且将会恢复历史记录的更新。&lt;br&gt;
	/// 调用本方法之前、调用 KeepCurrentImage() 之后的后续操作将不会加入到历史记录中，此时如果调用 ViewPrevious() 、
	/// ViewNext() 将不会得到预期的结果。所以，请不要在 KeepCurrentImage() 之后、RefreshCurrentImage() 之前进行历史 回溯操作。&lt;br&gt;
	/// 本方法内部调用了 Update() 方法，所以不必在调用本方法之后再次调用 Update() 方法。 
	/// </summary>
        this._bKeepHistory = true;
        this.raise_viewBoundsChanged(Sys.EventArgs.Empty);
        this.raise_viewChanged(Sys.EventArgs.Empty);
    },

    debug:function(enabled){
	/// <summary>调试。
	/// </summary>
	/// <param name="enabled">是否调试。</param>
        this._debug = enabled;
        for(var i = 0; i < this._tiles.length; i++){
            this._tiles[i].debug(enabled);
        }
    },

    refreshMapControl:function(){
	/// <summary>刷新地图控件。
	/// </summary>
        this.getMap()._t = new Date().getTime();
        this.getMap().clearImageBuffer();
        this._refreshMap();
    },
    
	refreshTrackingLayer:function(){
	/// <summary>刷新地图控件的高亮层。
	/// </summary>
		this._clearTrackingLayerTiles(true);
		this.getMap().set_hasHighlight(true);
		this._refreshTrackingLayer();
	},
//========================public methods end========================

//========================control events start========================
    add_changeView:function(handler){
	/// <summary>向MapControl控件添加onchangeView事件。&lt;br&gt;
	/// 当地图发生变化时（如地图可视范围发生变化，地图图层发生变化等,只要地图控件的地图中任何图层的图片刷新都会视为地图发生变化。）触发onchangeview事件。
	/// </summary>
	/// <param name="handler" type="Function" >changeView事件的处理函数。</param>
        this._addEvent("onchangeview", handler);
    },
    
    remove_changeView:function(handler){
	/// <summary>从MapControl控件中移除onchangeView事件。
	/// </summary>
	/// <param name="handler" type="Function">changeView事件的处理函数。</param>
        this._removeEvent("onchangeview", handler);
    },
    
    raise_changeView:function(arguments, userContext){
	/// <summary>触发onchangeView事件。&lt;br&gt;
	/// 当地图发生变化时（如地图可视范围发生变化，地图图层发生变化等，只要地图控件的地图中任何图层的图片刷新都会视为地图发生变化。）触发onchangeview事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onchangeview", arguments, userContext);
    },

    add_init:function(handler){
	/// <summary>向MapControl控件添加oninit事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("oninit", handler);
    },
    
    remove_init:function(handler){
	/// <summary>从MapControl控件中移除oninit事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("oninit", handler);
    },
    
    raise_init:function(arguments, userContext){
	/// <summary>触发oninit事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("oninit", arguments, userContext);
    },
    
    // onchangeview是图的简单变化 onviewchanged包括trackingLayer的变化
    add_viewChanged:function(handler){
	/// <summary>向MapControl控件添加onviewchanged事件。&lt;br&gt;
	/// 当地图发生变化（如地图可视范围发生变化，地图图层发生变化等,只要地图控件的地图中任何图层的图片刷新都会视为地图发生变化。），
	/// 并且已经完成图片的更新时触发onviewchanged事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onviewchanged", handler);
    },
    
    remove_viewChanged:function(handler){
	/// <summary>从MapControl控件中移除onviewchanged事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onviewchanged", handler);
    },
    
    raise_viewChanged:function(arguments, userContext){
	/// <summary>触发onviewchanged事件。&lt;br&gt;
	/// 当地图发生变化（如地图可视范围发生变化，地图图层发生变化等,只要地图控件的地图中任何图层的图片刷新都会视为地图发生变化。），
	/// 并且已经完成图片的更新时触发onviewchanged事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onviewchanged", arguments, userContext);
    },

    add_viewBoundsChanged:function(handler){
	/// <summary>向MapControl控件添加onviewboundschanged事件。&lt;br&gt;
	/// 当地图可视范围发生变化，
	/// 并且已经完成变化时触发onviewboundschanged事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onviewboundschanged", handler);
    },
    
    remove_viewBoundsChanged:function(handler){
	/// <summary>从MapControl控件中移除onviewboundschanged事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onviewboundschanged", handler);
    },
    
    raise_viewBoundsChanged:function(arguments, userContext){
	/// <summary>触发onviewboundschanged事件。&lt;br&gt;
	/// 当地图可视范围发生变化，
	/// 并且已经完成图片的更新时触发onviewboundschanged事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onviewboundschanged", arguments, userContext);
    },

    add_startZoom:function(handler){
	/// <summary>向MapControl控件添加onstartzoom事件。&lt;br&gt;
	/// 当要执行地图缩放操作时，触发onstartzoom事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onstartzoom", handler);
    },
    
    remove_startZoom:function(handler){
	/// <summary>从MapControl控件中移除onstartzoom事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onstartzoom", handler);
    },
    
    raise_startZoom:function(arguments, userContext){
	/// <summary>当要执行地图缩放操作时，触发onstartzoom事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onstartzoom", arguments, userContext);
    },

    add_endZoom:function(handler){
	/// <summary>向MapControl控件添加onendzoom事件。&lt;br&gt;
	/// 当完成地图缩放操作时，触发onendzoom事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onendzoom", handler);
    },
    
    remove_endZoom:function(handler){
	/// <summary>从MapControl控件中移除onendzoom事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onendzoom", handler);
    },
    
    raise_endZoom:function(arguments, userContext){
	/// <summary>当完成地图缩放操作时，触发onendzoom事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onendzoom", arguments, userContext);
    },

    add_resize:function(handler){
	/// <summary>向MapControl控件添加onresize事件。&lt;br&gt;
	/// 当地图控件的尺寸发生重置时，触发该事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onresize", handler);
    },
    
    remove_resize:function(handler){
	/// <summary>从MapControl控件中移除onresize事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onresize", handler);
    },
    
    raise_resize:function(arguments, userContext){
	/// <summary>触发onresize事件。当地图控件的尺寸发生重置时，触发该事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onresize", arguments, userContext);
    },
    
    add_startDynamicPan:function(handler){
	/// <summary>向MapControl控件添加onstartdynamicpan事件。&lt;br&gt;
	/// 当开始动态平移地图时，触发该事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onstartdynamicpan", handler);
    },
    
    remove_startDynamicPan:function(handler){
	/// <summary>从MapControl控件中移除onstartdynamicpan事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onstartdynamicpan", handler);
    },
    
    raise_startDynamicPan:function(arguments, userContext){
	/// <summary>触发onstartdynamicpan事件。&lt;br&gt;
	/// 当开始动态平移地图时，触发该事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onstartdynamicpan", arguments, userContext);
    },

    add_endDynamicPan:function(handler){
	/// <summary>向MapControl控件添加onenddynamicpan事件。&lt;br&gt;
	/// 当动态平移地图结束时触发该事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._addEvent("onenddynamicpan", handler);
    },
    
    remove_endDynamicPan:function(handler){
	/// <summary>从MapControl控件中移除onenddynamicpan事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
        this._removeEvent("onenddynamicpan", handler);
    },
    
    raise_endDynamicPan:function(arguments, userContext){
	/// <summary>触发onenddynamicpan事件。&lt;br&gt;
	/// 当动态平移地图结束时触发该事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onenddynamicpan", arguments, userContext);
    },
	
	add_mouseMove:function(handler){
	/// <summary>向MapControl控件添加onmousemoving事件。&lt;br&gt;
	/// 当鼠标开始在地图上移动时触发onmousemoving事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
		this._addEvent("onmousemoving", handler);
	},
	
	raise_mouseMove:function(arguments, userContext){
	/// <summary>触发onmousemoving事件。&lt;br&gt;
	/// 当鼠标开始在地图上移动时触发onmousemoving事件。
	/// </summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>
        this._raiseEvent("onmousemoving", arguments, userContext);
	},
	
	remove_mouseMove:function(handler){
	/// <summary>从MapControl控件中移除onmousemoving事件。
	/// </summary>
	/// <param name="handler" type="Function">事件的处理函数。</param>
		this._removeEvent("onmousemoving", handler);
	},

    clearEvents:function(){
	/// <summary>从MapControl控件中移除所有的事件。
	/// </summary>
        while(this._eventsNameList.length){
            var eventName = this._eventsNameList.pop();
            this.get_events().removeHandler(eventName, this.get_events().getHandler(eventName));
        }
        this._eventsNameList = null;
    },
    
    _addEvent:function(eventName, handler){
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
        this.get_events().removeHandler(eventName, handler);
        // 如果eventName的事件没有了，需要把this._eventsNameList中的事件名删除
        if(!this.get_events().getHandler(eventName)){
            this._eventsNameList.pop(eventName);
        }
    },

    _raiseEvent:function(eventName, args, userContext){
        var handler = this.get_events().getHandler(eventName);
        if (handler) {
            handler(args, userContext);
        }
    },
//========================control events end========================

//========================dom events start========================
    _keyDown:function(e){
    	e = SuperMap.Utility.getEvent(e);
    	var s = this._keyboardPanDistance;
    	var x = this._panningX;
    	var y = this._panningY;
    	switch(e.keyCode){
    		case 9://Tab
    		    if(this._panning && this._keyboardPanning){
    		        this.stopDynamicPan();
    		    }
    		    return true;
    		case 37://Left Arrow
    		    x = -s;
    		    break;
    		case 38://Up Arrow
    		    y = -s;
    		    break;
    		case 39://Right Arrow
    		    x = s;
    		    break;
    		case 40://Down Arrow
    		    y = s;
    		    break;
    		case 107:   //+ in IE & Firefox (small keyboard)
    		case 187:   //+ in IE
    		case 61:    //+ in Opera(sk) & Firefox
    		case 43:    //+ in Opera
    		    x = 0;
    		    y = 0;
    		    this.zoomIn();
    		    break;
    		case 109:
    		case 189:
    		case 45:    //- in Opera
    		    x = 0;
    		    y = 0;
    		    this.zoomOut();
    		    break; 
    		default:return false;
    	}
    	if(x || y){
    	    this.dynamicPan(x, y, null, true);
    	}
    	if(this._debug){
    	    window.status = "e.keyCode:" + e.keyCode;
    	}
    	return false;
    },

    _keyUp:function(e){
        e = SuperMap.Utility.getEvent(e);
        var x = this._panningX;
        var y = this._panningY;
        switch(e.keyCode){
            case 37:
                x = 0;
                break;
            case 38:
                y = 0;
                break;
            case 39:
                x = 0;
                break;
            case 40:
                y = 0;
                break;
            default:
                return false;
        }
        this.dynamicPan(x, y, null, true);
        return false;
    },
    
    _mouseDown:function(e){
    	e = SuperMap.Utility.getEvent(e);
    	SuperMap.Utility.cancelBubble(e);
    	this._getEventPosition(e);
    	if(this._curAction && this._curAction.onMouseDown){
    	    this._curAction.onMouseDown(e);
    	}
    	return false;
    },
    
    _mouseMove:function(e){
        e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
        this._getEventPosition(e);
		this.raise_mouseMove(e);
        if(this._curAction && this._curAction.onMouseMove){
            this._curAction.onMouseMove(e);
        }
        return false;
    },
    
    _mouseUp:function(e){
    	e = SuperMap.Utility.getEvent(e);
    	SuperMap.Utility.cancelBubble(e);
    	this._getEventPosition(e);
    	if(this._curAction && this._curAction.onMouseUp){
    	    this._curAction.onMouseUp(e);
    	}
    	this._kbInput.focus();
    	return false;
    },
    
    _click:function(e){
    	e = SuperMap.Utility.getEvent(e); 
    	SuperMap.Utility.cancelBubble(e); 
    	this._getEventPosition(e);
    	if(this._curAction && this._curAction.onClick){
    	    this._curAction.onClick(e);
    	}
    },
    
    _dblClick:function(e){
    	e = SuperMap.Utility.getEvent(e);
    	SuperMap.Utility.cancelBubble(e);
    	this._getEventPosition(e);
    	if(this._curAction && this._curAction.onDblClick){
            this._curAction.onDblClick(e);
        }else{
            this._getPosAndSize();
    	    if(this._panning || this._zooming){
                return false;
            }
    	    var param = this._currentMap.get_mapParam().makeCopy();
    	    param.setPixelCenter(new SuperMap.Point(this._originX + this._offsetX + SuperMap.Utility.getMouseX(e) - this._x, this._originY + this._offsetY + SuperMap.Utility.getMouseY(e) - this._y));
    	    if(!e.altKey){
                param.set_mapScale(this._currentMap.get_mapParam().mapScale * 2);
            }else{
                param.set_mapScale(this._currentMap.get_mapParam().mapScale / 2);
            }
    	    this.setMapParam(param);
    	}
    },
    
    _contextMenu:function(e){
    	e = SuperMap.Utility.getEvent(e);
    	SuperMap.Utility.cancelBubble(e);
    	this._getEventPosition(e);
    	if(this._curAction && this._curAction.onContextMenu){
    	    this._curAction.onContextMenu(e);
    	}
    	if(e.preventDefault){
    	    e.preventDefault();
    	}
    	return false;
    },
    
    ///捕获鼠标滚轮事件
    _mouseWheel:function(e){
		e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
        if(this._panning || this._zooming){
            return false;
        }
		var pixelX = SuperMap.Utility.getMouseX(e);
		var pixelY = SuperMap.Utility.getMouseY(e);
		var offsetTop = this.get_container().offsetTop;
		var offsetLeft = this.get_container().offsetLeft;
		var pixelPoint = new SuperMap.Point(pixelX - offsetLeft, pixelY - offsetTop); 
		var mapPoint = this.pixelToMapCoord(pixelPoint);
        var delta = SuperMap.Utility.getMouseScrollDelta(e);
        var mapScale = this.get_mapScale();
        
        
        if(delta > 0){
			var center = this.getMapCenter();
			var centerPx = this.mapCoordToPixel(center);
			var offset = new SuperMap.Point(centerPx.x - pixelPoint.x, centerPx.y - pixelPoint.y);
			
			var newMapScale = mapScale;
			if(this._mapScales) {
				for(var i = 0; i < this._mapScales.length; i++) {
					if(mapScale == this._mapScales[i] && i < this._mapScales.length-1) {
						newMapScale = this._mapScales[i + 1];
						break;
					}
				}
			}else{
			    newMapScale *= 2;
			}
			var mapDistance = this.pixelToMapDistance(1, newMapScale);
			var newCenter = new SuperMap.Point2D();
			var ratio = newMapScale / mapScale;
			var newOffset = new SuperMap.Point(Math.floor(ratio * offset.x), Math.floor(ratio * offset.y));
			newCenter.x = center.x - (ratio * offset.x - offset.x) * mapDistance;
			newCenter.y = center.y + (ratio * offset.y - offset.y) * mapDistance;
			this.setCenterAndZoom(newCenter, newMapScale);
        }else if(delta < 0){
			var center = this.getMapCenter();
			var centerPx = this.mapCoordToPixel(center);
			var offset = new SuperMap.Point(centerPx.x - pixelPoint.x, centerPx.y - pixelPoint.y);
			
			var newMapScale = mapScale;
			if(this._mapScales) {
				for(var i = 0; i < this._mapScales.length; i++) {
					if(mapScale == this._mapScales[i] && i > 0) {
						newMapScale = this._mapScales[i-1];
						break;
					}
				}
			}else{
			    newMapScale /= 2;
			}
			var mapDistance = this.pixelToMapDistance(1, newMapScale);
			var newCenter = new SuperMap.Point2D();
			var ratio = newMapScale / mapScale;
			var newOffset = new SuperMap.Point(Math.floor(ratio * offset.x), Math.floor(ratio * offset.y));
			newCenter.x = center.x - (ratio * offset.x - offset.x) * mapDistance;
			newCenter.y = center.y + (ratio * offset.y - offset.y) * mapDistance;
			this.setCenterAndZoom(newCenter, newMapScale);
        }
    	if(e.preventDefault){
    	    e.preventDefault();
    	}
    	return false;
    },
	
	_mouseScroll:function(e){
		e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
        if(this._panning || this._zooming){
            return false;
        }
		var delta = -e.detail/3;
		var pixelX = SuperMap.Utility.getMouseX(e);
		var pixelY = SuperMap.Utility.getMouseY(e);
		var offsetTop = this.get_container().offsetTop;
		var offsetLeft = this.get_container().offsetLeft;
		var pixelPoint = new SuperMap.Point(pixelX - offsetLeft, pixelY - offsetTop); 
		var mapPoint = this.pixelToMapCoord(pixelPoint);
		if(delta > 0) {
			this.panToMapCoord(mapPoint);
			this.zoomIn();
		} else if(delta < 0) {
			this.panToMapCoord(mapPoint);
			this.zoomOut();
		}
		if(e.preventDefault){
    	    e.preventDefault();
    	}
    	return false;
	},
	
    _generateEventArg:function(curCenter, ms, error, e){
    	var param = this._currentMap.get_mapParam().makeCopy();
    	if(curCenter){
    	    param.center = curCenter;
    	}
    	if(ms){
    	    param.mapScale = ms;
    	}
    	if(!error){
    	    error="";
    	}
    	return new SuperMap.EventArguments(param,error,e);
    },

    _getEventPosition:function(e){
        // 像素坐标。
        e.pixelCoord = new SuperMap.Point(this._originX + this._offsetX + SuperMap.Utility.getMouseX(e) - this._x, this._originY + this._offsetY + SuperMap.Utility.getMouseY(e) - this._y);   
        // 地理坐标。
        e.mapCoord = this._currentMap.pixelToMapCoord(e.pixelCoord, this._currentMap.get_mapParam().mapScale);
        // 相对地图左上角偏移量坐标。
        e.offsetCoord = new SuperMap.Point(e.pixelCoord.x - this._originX, e.pixelCoord.y - this._originY);                   
        if(this._debug){
            window.status = "e.pixelCoord:" + e.pixelCoord.x + "," + e.pixelCoord.y + "  e.mapCoord:" + e.mapCoord.x + "," + e.mapCoord.y + "  e.offsetCoord:" + e.offsetCoord.x + "," + e.offsetCoord.y;
        }
    },
//========================dom events end========================

//========================private methods start========================
    _onFirstUpdateComplete:function(layersKey){
        // 给layersKey赋值
        if(layersKey){
	        this._currentMap._params.layersKey = layersKey;
        }
    	// 获取地图位置、尺寸 //
    	this._getPosAndSize();
    	if(this._width < 100){
            this._container.style.width = "100px";
        }
        if(this._height<100){
            this._container.style.height = "100px";
        }
    	this._getPosAndSize();
    	
    	this._container.style.overflow = "hidden";
    	this._container.style.padding = "0px";
    	this._container.style.margin = "0px";
    	this._mapDiv.style.position = "relative";
    	this._mapDiv.style.padding = "0px";
        this._mapDiv.style.margin = "0px";
        this._mapDiv.style.zIndex = 1;
        this._mapDiv.style.width = this._workLayer.style.width;
        this._mapDiv.style.height = this._workLayer.style.height;
        this._workLayer.appendChild(this._mapDiv);        

        this._kbInput.value = "";
        var ks = this._kbInput.style;
        ks.position = "absolute";
        ks.top = this._height / 2 + "px";
        ks.left = this._width / 2 + "px";
        ks.width = "0px";
        ks.height = "0px";
        ks.padding = "0px";
        ks.margin = "0px";
        ks.border = "0px solid white";
        ks.zIndex = -1;
        ks = null;
        this._workLayer.appendChild(this._kbInput);
    	if(!this._params.fixedView){
    		this._zoomInAction = new SuperMap.UI.ZoomInAction();
            this._zoomInAction.initialize(this._self);
    		this._panAction = new SuperMap.UI.PanAction();
            this._panAction.initialize(this._self);
    		this._curAction = this._panAction;
    		if(!this._changingMap){
                $addHandler(this._container, "mousedown", this._mouseDownDelegate);
                $addHandler(this._container, "mouseup", this._mouseUpDelegate);
                $addHandler(this._container, "mousemove", this._mouseMoveDelegate);
                $addHandler(this._container, "mousewheel", this._mouseWheelDelegate);
				if(document.addEventListener) {
					this._container.addEventListener("DOMMouseScroll", this._mouseScrollDelegate, true);
				}
				
                $addHandler(this._container, "dblclick", this._dblClickDelegate);
                $addHandler(this._container, "contextmenu", this._contextMenuDelegate);
                $addHandler(this._container, "click", this._clickDelegate);
                $addHandler(this._kbInput, "keydown", this._keyDownDelegate);
                $addHandler(this._kbInput, "keyup", this._keyUpDelegate);
    		}
    		this._changingMap = false;
    	}
    	if(this._params.x != null && this._params.y != null && this._params.mapScale){
			var errorHappened = false;
    		try{
    			var mp = new SuperMap.MapParam(this.get_mapName(), this._currentMap._mapScale, this._mapScales);
    			mp.setMapScale(eval(this._params.mapScale));
                mp.setMapCenter(new SuperMap.Point2D(this._params.x, this._params.y));
    			this.setMapParam(mp);
    		}catch(e){
				errorHappened = true;
    		    //this._setDefaultParam();
    	    }
			if(errorHappened){
				this._setDefaultParam();
			}
    	}else{
            this._setDefaultParam();
        }
    	
    	this._initClientAction();
        
        // oninit
        this.raise_init(Sys.EventArgs.Empty);
    },
    
    _startMap:function(){
	    if(this._currentMap.trackingLayer && this._currentMap.trackingLayer._tiles && this._currentMap.trackingLayer._tiles.length > 0){
    	    this._clearTrackingLayerTiles();
        }
    	this._mapDiv.style.top = "0px";
        this._mapDiv.style.left = "0px";
        this._mapDiv.style.width = this._width;
        this._mapDiv.style.height = this._height;
		if(this._currentMap.get_mapParam().pixelCenter != null){
			this._originX = SuperMap.Utility.round(this._currentMap.get_mapParam().pixelCenter.x - this._width / 2);
			this._originY = SuperMap.Utility.round(this._currentMap.get_mapParam().pixelCenter.y - this._height / 2);
		}
    	this._offsetX = 0;
    	this._offsetY = 0;
        
        this._currentMap.clearTiles(this._mapDiv);
        this._currentMap.render(this._mapDiv, this._width, this._height, this._originX,this._originY,this._offsetX,this._offsetY);
        this._currentMap.renderTrackingLayer(this._mapDiv, this._width, this._height, this._originX,this._originY,this._offsetX,this._offsetY);
        
		if(this._viewBoundsBefore != null && !this._viewBoundsBefore.equals(this._currentBounds)){
    	    this.raise_viewBoundsChanged(null, null);
    	    this.raise_viewChanged(null, null);
    	}
    },
    
    _updateMap:function(){
    	if(this._zooming){
    	    return;
    	}
        
        this._currentMap.render(this._mapDiv, this._width, this._height, this._originX,this._originY,this._offsetX,this._offsetY);
        this._currentMap.renderTrackingLayer(this._mapDiv, this._width, this._height, this._originX,this._originY,this._offsetX,this._offsetY);
		
        this.raise_viewBoundsChanged(Sys.EventArgs.Empty);
    	this.raise_viewChanged(Sys.EventArgs.Empty);

    	this._clearTimeout();
    },
    
    _setFactor:function(){
    	if(!this._zooming){
    	    return;
    	}

        this._currentMap.setFactor(this._zoomCounter);
        	
    	if(this._zoomCounter < this._zoomTotalSteps){
            this._zoomCounter++;
            this._setZoomCounter();
            this._iTimeoutIDForSetFactor = window.setTimeout(this._setFactorDelegate, 1);
        }else{
            this._zoomCounter = 0;
            this._setZoomCounter();
            window.clearTimeout(this._iTimeoutIDForSetFactor);
            this._swapStates();
        }
        this.customLayer.repositionMarkers();
    },
    
    _swapStates:function(){
    	this._zooming = false;
    	this._setIsZooming();
        
        this._currentMap.swapStates();
        this.raise_endZoom(Sys.EventArgs.Empty);       
        this.raise_changeView(Sys.EventArgs.Empty);
    	
    	this._iTimeoutIDForCheckTileLoaded=setTimeout(this._checkTileLoadedDelegate, 100);
    	try{
    	    CollectGarbage();
    	}catch(ex){}
    },
  
    _switchMap:function(){
        this._changingMap = true;

        //所有的Manager都清空
        this._query = null;
        this._spatialAnalyst = null;
        this._overview = null;
        this._edit = null;
        this._magnifier = null;

        this._currentMap.clearLayers(this._mapDiv);

        if(this._logo){
            this._logo.dispose();
            this._logo = null;
        }

        if(this._currentMap){
     //       this._currentMap.dispose();
     //       this._currentMap = null;
        }
        this.customLayer.clearMarkers();
        this.customLayer.clearLines();
        this.customLayer.clearPolygons();
    
        var divTL = $get("trackingLayer");
        if(divTL){
            divTL.parentNode.removeChild(divTL); 
            divTL = null;
        }
        var inputTL = $get(this._workLayer.id + "_TrackingLayer");
        if(inputTL){
            inputTL.parentNode.removeChild(inputTL);
            inputTL = null;
        }
        if(this._workLayer){
            this._workLayer.parentNode.removeChild(this._workLayer);
            this._workLayer.innerHTML="";//没有置为null
        }

        while(this._eventsNameList.length){
            this._eventsNameList.pop();
        }

        this._clearEvents();
        this._self.initialize();
		if(this.customLayer && this.customLayer.container && this.customLayer.parentNode == null) {
			this._mapDiv.appendChild(this.customLayer.container);
		}
    },

    _refreshMap:function(){
        this._currentMap.clearTiles(this._mapDiv);
        this._updateMap();
        this._swapStates();
        this._setFactor();
        this.customLayer.repositionMarkers();
        this.customLayer.repositionPolygons();
        this.customLayer.repositionLines();
        this.customLayer.repositionCircles();
    },
    
    _ensureWithinBounds:function(param, bounds){
        return this._panWithinBounds(param, bounds, 0, 0);
    },
    
    _panWithinBounds:function(param, bounds, deltaX, deltaY){
		if(param.pixelCenter == null){
			return;
		}
        var newPosition = this._currentMap.pixelToMapCoord(new SuperMap.Point(deltaX + param.pixelCenter.x, deltaY + param.pixelCenter.y), param.mapScale);
    	if(this._debug){
    	    window.status = "oldX:" + newPosition.x;
    	}
        var changed = false;
        if(!this._params.global) {
	        if(newPosition.x < bounds.leftBottom.x){
	            newPosition.x = bounds.leftBottom.x;
	            changed = true;
	        }
	        if(newPosition.y < bounds.leftBottom.y){
	            newPosition.y = bounds.leftBottom.y;
	            changed = true;
	        }
	        if(newPosition.x > bounds.rightTop.x){
	            newPosition.x = bounds.rightTop.x;
	            changed = true;
	        }
	        if(newPosition.y > bounds.rightTop.y){
	            newPosition.y = bounds.rightTop.y;
	            changed = true;
	        }
		}
        var newPixelPosition;
        if(changed){
            newPixelPosition = this._currentMap.mapCoordToPixel(newPosition, param.mapScale);
            param.setPixelCenter(newPixelPosition);
        }else{
            newPixelPosition = new SuperMap.Point(param.pixelCenter.x + deltaX, param.pixelCenter.y + deltaY);
            param.setPixelCenter(newPixelPosition);
        }
    	param.setMapCenter(newPosition);
    	if(this._debug){
    	    window.status += "newX:" + newPosition.x;
    	}
    	
    	return newPixelPosition;
    },

    _stepPan:function(){
    	if(!this._panning){
    	    return;
    	}
    	this.pan(this._panningX, this._panningY);
    	while(this._panCounter > 0){
			this._panCounter--;
			if(this._panCounter != 0) {
				this.pan(this._panningX, this._panningY);
			}
    	}
		this.stopDynamicPan();
    },

    _setDefaultParam:function(){
        var param = new SuperMap.MapParam(this.get_mapName(), this._mapScale, this._mapScales);
        param.setMapCenter(new SuperMap.Point2D(0.0, 0.0));
        param.setMapScale(1);
        this.setMapParam(param);
    },

    _onViewBoundsChanged:function(viewBounds){
       //在公交高亮时会发生viewbounds的变化
        if(viewBounds!=null){
            //todo:
            this._viewByBounds(viewBounds.leftBottom.x, viewBounds.leftBottom.y,
                        viewBounds.rightTop.x, viewBounds.rightTop.y);
        }
    },

    _getPosAndSize:function(){
        this._x = SuperMap.Utility.getElementX(this._workLayer);
        this._y = SuperMap.Utility.getElementY(this._workLayer);
        // 还要加上 border 的尺寸。 //
        if(this._container.style.borderLeftWidth){
            this._x += parseInt(this._container.style.borderLeftWidth);
        }
        if(this._container.style.borderTopWidth){
            this._y += parseInt(this._container.style.borderTopWidth);
        }
        this._width = this._container.offsetWidth;
        this._height = this._container.offsetHeight;
    },

    _saveMapParamAndLayers:function(){
        //如果不记录历史也就没有必要回调了
        if(!this._bKeepHistory){
            return;
        }
        this._setMapParamHidden();
        
        //todo:保存mapParam状态和layer信息,以后要合并mapParam状态和layer信息
        //如果是前一视图后一视图的操作就没有必要再回调了:)
        if(this._params.bViewHistory){
            this._params.bViewHistory = false;
            this._onGetTrackingLayerImageComplete("ViewHistory");
            return;
        }
        this._onGetTrackingLayerImageComplete(null);
    },
    
    //输出TrackingLayer的图后，需要将其加载到图上
    //todo 只是记录历史，需要改变名称
    _onGetTrackingLayerImageComplete:function(userContext){
        if(userContext == "ViewHistory"){
            //不记录历史
            this._params.bPostBack = false;
            return;
        }
        //记录历史
        if(this._params.bPostBack){
            this._params.bPostBack = false;
            return;
        }
       
        var mapParam = new Object();
        mapParam.center = new SuperMap.Point2D();
        mapParam.center.x = this.getMapCenter().x;
        mapParam.center.y = this.getMapCenter().y;
        mapParam.mapScale = this._currentMap.get_mapParam().mapScale;
        mapParam.mapBounds = this.getMapBounds();
        mapParam.viewBounds = this.getViewBounds();
        mapParam.mapName = this.get_mapName();
        mapParam.trackingLayerUrl = this._trackingLayerUrl;
        var mapParamJSON = SuperMap.Utility.toJSON(mapParam);
        var changedlayersJSON = SuperMap.Utility.findDifference(this._currentMap.layersBackupForHistroy, this._currentMap.layers);
        var o = new Array(mapParamJSON, changedlayersJSON);
        var eJSON = SuperMap.Utility.toJSON(o);
        try{
            eval(container.id+"_CallBack('SaveMapParamAndLayers|'+eJSON,_GetHistoryKey,null)");
        }catch(e){
        }
    },

    _computeCenterPoint:function(capture){
        this._currentMap.get_mapParam().center = this._currentMap.pixelToMapCoord(this._currentMap.get_mapParam().pixelCenter, this._currentMap.get_mapParam().mapScale);
    },
    
    _clipDelta:function(delta, size, offset, ms, min, max){
        var mpp = this._currentMap.pixelToMapDistance(ms);
        var pos = (offset + delta) * mpp;
        if(pos > max){
            return delta * (max / pos);
        }else if(pos < min){
            return delta * (pos / min);
        }
        return delta;
    },

    _clipMapX:function(x){
        var curMapBounds = this._currentMap.get_referMapBounds();
        return this._clip(x, curMapBounds.leftBottom.x, curMapBounds.rightTop.x);
    },

    _clipMapY:function(y){
        var curMapBounds = this._currentMap.get_referMapBounds();
        return this._clip(y, curMapBounds.leftBottom.y, curMapBounds.rightTop.y);
    },

    _clip:function(n, minValue, maxValue){
        if(n < minValue){
            return minValue;
        }
        if(n > maxValue){
            return maxValue;
        }
        return n;
    },

    _onTrackingLayerChanged:function(arguments){
        //需要通知MapControl中的Map和QueryManager和SpatialAnalystManager去更新它们的Key;
       this._trackingLayerCached = false;//肯定没缓存
       this._currentMap._trackingLayerCached = false;//肯定没缓存
       this._refreshTrackingLayer();
    },

    _refreshTrackingLayer:function(){
        //地图没有发生变化,但跟踪层变化后,需要刷新一下跟踪层
        this._currentMap.trackingLayer.clearTiles();
        this._currentMap.renderTrackingLayer(this._mapDiv, this._width, this._height, this._originX,this._originY,this._offsetX,this._offsetY);
    },

    _removeTrackingLayerImage:function(){
        var img = $get(this._container.id + "_TrackingLayer");
        if(img){
            this._mapDiv.removeChild(img);
        }
    },

    _clearTrackingLayerTiles:function(flag){
        if(this._currentMap.trackingLayer){
            this._currentMap.trackingLayer.clearTiles();
            if(flag) {
                this._currentMap.trackingLayer._clearTiles(this._currentMap.trackingLayer._oldTiles, true);
                this._currentMap.trackingLayer._clearTiles(this._currentMap.trackingLayer._preTiles, true);
            }
			var container = this._currentMap.trackingLayer.get_container();
			if(container && container.childNodes) {
				while(container.childNodes.length > 0) {
					container.childNodes[0].onload = null;
					container.childNodes[0].onerror = null;
					container.childNodes[0].onmousedown = null;
					container.childNodes[0].onmouseup = null;
					container.removeChild(container.childNodes[0]);
				}
			}
        }
    },

    _setMapParamHidden:function(){
        var mapParam = new Object();
        mapParam.center = new SuperMap.Point2D();
        mapParam.center.x = this.getMapCenter().x;
        mapParam.center.y = this.getMapCenter().y;
        mapParam.mapScale = this._currentMap.get_mapParam().mapScale;
        mapParam.mapBounds = this.getMapBounds();
        mapParam.viewBounds = this.getViewBounds();
        mapParam.mapName = this.get_mapName();
        var hiddenMapParam = document.getElementById(this._container.id + "_hiddenMapParam");
        if(hiddenMapParam){
            hiddenMapParam.value = SuperMap.Utility.toJSON(mapParam);
        }
    },

    // 保留此方法，以后可能还要统一检查图层是否全部被加载
    _checkTileLoaded:function(){
    	if(this._trackingLayerCached){
    	    return;
    	}
    	this._trackingLayerCached = true;
    
    	var loaded = true;
    	if(loaded){
			for(var i = 0; i < this._currentMap._imageLayers.length; i++){
				if(this._currentMap._imageLayers[i].getType() == "SuperMap.TiledMapLayer" || this._currentMap._imageLayers[i].getType()=="SuperMap.GlobalMapLayer" || this._currentMap._imageLayers[i].getType() == "SuperMap.TiledWmsLayer"){
					this._currentMap._imageLayers[i]._clearTiles(this._currentMap._imageLayers[i]._oldTiles);
					this._currentMap._imageLayers[i]._oldTiles = null;
				}
			}
			if(this._currentMap.trackingLayer){
				this._currentMap.trackingLayer._clearTiles(this._currentMap.trackingLayer._oldTiles);
				this._currentMap.trackingLayer._oldTiles = null;
			}
    		this._raiseEvent("onimagesload", Sys.EventArgs.Empty);
			window.clearTimeout(this._iTimeoutIDForCheckTileLoaded);
			this._iTimeoutIDForCheckTileLoaded = null;
    	}else{
    		this._iTimeoutIDForCheckTileLoaded = window.setTimeout(this._checkTileLoadedDelegate, 100);
    	}
    	this._trackingLayerCached = false;
    },

    _initClientAction:function(){
        var hidden = $get(this._id + "_hiddenClientAction");
        if(hidden && hidden.value){
            var json = hidden.value;
            var action =SuperMap.Utility.jSONToAction(json);
            this.set_action(action);
        }
    },  

    _initEvents:function(){
        if(this._container){
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
        }
    },
    
    //todo:remove it
    _clearEvents:function(){
         if(this._container){
            var hidden = $get(this._container.id + "_hiddenClientEvents");
            if(hidden && hidden.value){
                var strEvents = hidden.value;
                var eventsInfo = strEvents.split(';');
                for(var i = 0; i < eventsInfo.length; i++){
                    var eventInfo = eventsInfo[i].split(':');
                    if(eventInfo && eventInfo.length == 2){
                        var eventName = eventInfo[0];
                        var eventListners = eventInfo[1].split(',');
                        for(var j = 0; j < eventListners.length; j++){
                            try{
                                //toremove:_AttachEvent(eventName,eval(eventListners[j]));
                                this.get_events().removeHandler(eventName, eval(eventListners[j]));
                            }catch(e){
                            }// events[i](arguments,userContext);
                        }
                    }
                }
            }
        }
    },

    _clearTimeout:function(){
    	if(this._iTimeoutIDForCheckTileLoaded){
    		window.clearTimeout(this._iTimeoutIDForCheckTileLoaded);
    		this._iTimeoutIDForCheckTileLoaded = null;
    	}
    	if(this._iTimeoutIDForShowPolylines){
    		window.clearTimeout(this._iTimeoutIDForShowPolylines);
    		this._iTimeoutIDForShowPolylines = null;
    	}
    	if(this._iTimeoutIDForUpdateMap){
    		window.clearTimeout(this._iTimeoutIDForUpdateMap);
    		this._iTimeoutIDForUpdateMap = null;
    	}
    	if(this._iTimeoutIDForStepPan){
    		window.clearTimeout(this._iTimeoutIDForStepPan);
    		this._iTimeoutIDForStepPan = null;
    	}
    	if(this._iTimeoutIDForSetFactor){
    		window.clearTimeout(this._iTimeoutIDForSetFactor);
    		this._iTimeoutIDForSetFactor = null;
    	}
    	if(this._iTimeoutIDForDynamicNavigate){
    		window.clearTimeout(this._iTimeoutIDForDynamicNavigate);
    		this._iTimeoutIDForSetFactor = null;
    	}
    },    
    
    _setIsZooming:function(){
        this._currentMap.setIsZooming(this._zooming);
    },
    
    _setZoomCounter:function(zoomCounter){
        this._currentMap.setZoomCounter(this._zoomCounter);
    },
    
    _setClientActionHidden:function(action){
        var hiddenLayer = $get(this._container.id + "_hiddenClientAction");
        if(!hiddenLayer){
            return;
        }
        if(action == null){
            hiddenLayer.value = "";
        }else{
            hiddenLayer.value = action._json;
        }
    },

//========================private methods end========================
    viewPrevious:function(){
		/// <summary>获取前一操作的地图。该方法在当前版本未实现，作为预留方法。 </summary>
		alert(SuperMap.Resource.ins.getMessage("viewPreviousNoImple"));
    },
    
    viewNext:function(){
	/// <summary>获取后一操作的地图，该方法在当前版本未实现，作为预留方法。 </summary>
		alert(SuperMap.Resource.ins.getMessage("viewNextNoImple"));
    },

    //前一视图后一视图回调完成后调用的事件.
    _viewHistory:function(e){

    },

    _getHistoryKey:function(e){
        this._historyKey = e;
    }
};
SuperMap.UI.MapControl.registerClass('SuperMap.UI.MapControl', Sys.UI.Control, Sys.IDisposable);