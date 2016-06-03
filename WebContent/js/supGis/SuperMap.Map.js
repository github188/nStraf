 //========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.Map.js  
// 功能：			地图对象类
//========================================================================== 

Type.registerNamespace('SuperMap');

// Map，可以直接创建并用以出图或者进行地图分析
SuperMap.Map = function(params){
	/// <summary>地图对象，该类可以直接创建map对象，并用以进行简单的地图分析、地图编辑，制作默认专题图等。
	/// </summary>
	/// <param name="params" type="Object">Object对象，用于设置map初始化时需要的参数信息，如imageFormat、mapScale等。</param>
	/// <returns type="SuperMap.Map">返回Map对象。</returns>
	/// <field name="queryUrl" type="String">查询地址。</field>
	/// <field name="trackingLayer" type="SuperMap.TiledTrackingLayer">跟踪层。</field>
	/// <field name="isEditing" type="Boolean">是否处于编辑操作状态。</field>
	// 校验 params 参数
	if(!params.contextPath){
	    params.contextPath = "http://localhost:7080/demo/";
	}
	if(!params.imageFormat){
		params.imageFormat = "png";
	}
	if(!params.mapHandler){
		params.mapHandler = params.contextPath + "maphandler";
	}
	if(!params.mapName){
		params.mapName = "";
	}
	if(!params.mapScale){
		params.mapScale = null;
	}
	if(!params.useImageBuffer){
		params.useImageBuffer = true;
	}
	if(!params.tileSize){
		params.tileSize = 256;
	}
	if(!params.layersKey){
		params.layersKey = 0;
	}

	this._params = params;
	
	this.queryUrl = this._params.contextPath + "commonhandler";
    this._params.commonHandler = this._params.contextPath + "commonhandler";
    
	this._mapParam = new SuperMap.MapParam();
    
	if(this._params.mapName){
    	this._mapName = this._params.mapName;
	}else{
    	this._mapName = "";
	}
	if(this._params.mapServicesAddress){
        this._mapServicesAddress = this._params.mapServicesAddress;
	}else{
        this._mapServicesAddress = "localhost";
	}
	if(this._params.mapServicesPort){
        this._mapServicesPort = this._params.mapServicesPort;
	}else{
        this._mapServicesPort = 8600;
	}
	this._mapScale = this._params.mapScale;
	// 参考的地图参数
	this._referMapScale = 0;
	this._referMapBounds = null;
	this._referViewBounds = null;
	this._referViewer = null;

	// 暂时存MapLayer的subLayer，以后存ILayer
	this._layers = new Array();
	this._layersBackupForHistroy = new Array();
	this._layersBackupForHandler = new Array();

	this._trackingLayerCached = false;

	// 放到到第几步
	this._zoomCounter = 0;
	this._isZooming = false;

	this._imageLayers = new Array();
    this.trackingLayer = new SuperMap.TiledTrackingLayer(null, this._params, this);
	this.trackingLayer.id = this._mapName + "_TiledTrackingLayer";
	this._hasHighlight = false;

	// 是否螺旋加载图层
	this._enableSpiralLoad = true;
    this._coordsSys = null;
	this._global = this._params.global;
	
	this._onSetLayerCompleteDelegate = Function.createDelegate(this, this.onSetLayerComplete);
	
	this._t = new Date().getTime();
    this._useSimpleCache = false;
	this.isEditing = false;
	this._onInitCompleted;
};

SuperMap.Map.prototype = {
//========================properties start========================

	get_useQuickCache:function(){
	///此方法已改名，新版本中用户请使用get_useSimpleCache()获取简单缓存参数。
	/// <summary>获取或者设置是否采用快速缓存。</summary>
   	/// <returns type="Boolean">get_useQuickCache()返回值类型为 Boolean，true 表示采用快速缓存。</returns>
        return this._useSimpleCache; 
    }, 

    set_useQuickCache:function(useQuickCache){
	///此方法已改名，新版本中用户请使用set_useSimpleCache()设置简单缓存参数。
        this._useSimpleCache = useQuickCache; 
    },
	
	get_useSimpleCache:function(){
	/// <summary>获取或者设置是否采用简单缓存。</summary>
   	/// <returns type="Boolean">get_useSimpleCache()返回值类型为 Boolean，true 表示采用简单缓存。</returns>
        return this._useSimpleCache; 
    }, 

    set_useSimpleCache:function(useSimpleCache){
        this._useSimpleCache = useSimpleCache; 
    }, 

	get_params:function(){
	/// <summary>获取或者设置地图参数。</summary>
   	/// <returns type="String">get_params()返回值类型为Object。</returns>
		return this._params;
	},

	set_params:function(params){
		this._params = params;
	},

    get_mapName:function(){
	/// <summary>获取或者设置地图名称。</summary>
   	/// <returns type="String">get_mapName()返回值类型为String。</returns>
		return this._mapName;
	},

	set_mapName:function(mapName){
		this._mapName = mapName;
	},
	
	get_mapServicesAddress:function(){
	/// <summary>获取或者设置地图服务的地址。</summary>
   	/// <returns type="String">get_mapServicesAddress()返回值类型为String。</returns>
		return this._mapServicesAddress;
	},

	set_mapServicesAddress:function(mapServicesAddress){
		this._mapServicesAddress = mapServicesAddress;
	},

	get_mapServicesPort:function(){
	/// <summary>获取或者设置地图服务的端口。</summary>
   	/// <returns type="String">get_mapServicesPort()返回值类型为String。</returns>
		return this._mapServicesPort;
	},

	set_mapServicesPort:function(mapServicesPort){
		this._mapServicesPort = mapServicesPort;
	},

	get_mapScale:function(){
	/// <summary>获取或者设置当前地图比例尺。</summary>
   	/// <returns type="Number">get_mapScale()返回值类型为Number。</returns>
		return this._mapScale;
	},

	set_mapScale:function(mapScale){
		this._mapScale = mapScale;
	},

	get_mapParam:function(){
	/// <summary>获取或者设置地图参数对象。</summary>
   	/// <returns type="SuperMap.MapParam">get_mapParam()返回值类型为&lt;see cref="T:SuperMap.MapParam"&gt;MapParam&lt;/see&gt;。</returns>
		// 返回引用，可以直接改掉
		return this._mapParam;
	},

	set_mapParam:function(mapParam){
		this._mapParam = mapParam;
	},

	// 只读属性
	get_referMapScale:function(){
	/// <summary>获取地图初始化时默认的显示比例尺。</summary>
   	/// <returns type="Number">get_referMapScale()返回值类型为Number。</returns>
		return this._referMapScale;
	},

	// 只读属性
	get_referMapBounds:function(){
	/// <summary>获取当前地图的空间范围。&lt;br&gt;
	/// 地图的空间范围是其所显示的各数据集的范围的最小外接矩形，
	/// 即包含各数据集范围的最小的矩形。当地图显示的数据集增加或删除时，其空间范围也会相应发生变化。
	/// Rect2D类型。</summary>
   	/// <returns type="SuperMap.Rect2D">get_referMapBounds()返回值类型为&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;。</returns>
	    var mapBounds = new SuperMap.Rect2D();
		mapBounds.copy(this._referMapBounds);
		return mapBounds;
	},

	// 只读属性
	get_referViewBounds:function(){
	/// <summary>获取地图初始化时默认的地图显示范围。Rect2D类型。</summary>
   	/// <returns type="SuperMap.Rect2D">get_referViewBounds()返回值类型为&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;。</returns>
		return this._referViewBounds;
	},

	// 只读属性
	get_referViewer:function(){
	/// <summary>获取地图初始化时默认的地图图片的尺寸。Rect类型。</summary>
   	/// <returns type="SuperMap.Rect">get_referViewer()返回值类型为&lt;see cref="T:SuperMap.Rect"&gt;Rect&lt;/see&gt;。</returns>
		return this._referViewer;
	},

	get_layers:function(){
	/// <summary>获取或者设置当前地图中的所有图层。&lt;br&gt;
	/// 1. 此处获取的Layers是一个JSON数组对象，对应服务端的MapParam.layerCollection对象。&lt;br&gt;
	/// 2. layers数组中的元素和MapParam.layerCollection中的每一个元素一一对应。&lt;br&gt;
	/// 3. 如果layers中的某一个元素（layers[i]）对应服务器端的SuperMapLayerCollection对象，则该元素（layers[i]）中包含一个subLayers数组属性来保存SuperMapLayerCollection对象中每一个图层的信息。</summary>
   	/// <returns type="Array" elementType="Object">get_layers()返回值类型为JSON数组。</returns>
		return this._layers;
	}, 

	set_layers:function(layers){
		var e = Function._validateParams(arguments, [
			{name: "layers", type: Array}
		]);
		if (e) throw e;
		this._layers = layers;
	},

	get_enableSpiralLoad:function(){
	/// <summary>获取或者设置地图图片是否采用螺旋加载的方式。true表示使用螺旋加载的方式加载地图图片。</summary>
   	/// <returns type="Boolean">get_enableSpiralLoad()返回值类型为Boolean。</returns>
		return this._enableSpiralLoad;
	}, 

	set_enableSpiralLoad:function(enableSpiralLoad){
		var e = Function._validateParams(arguments, [
			{name: "enableSpiralLoad", type: Boolean}
		]);
		if (e) throw e;
		this._enableSpiralLoad = enableSpiralLoad;
	},
	
	get_coordsSys:function(){
	/// <summary>获取或者设置地图的坐标系信息。</summary>
   	/// <returns type="Object">get_coordsSys()返回值类型为Object。</returns>
		return 	this._coordsSys;
	}, 

	set_coordsSys:function(coordsSys){
		var e = Function._validateParams(arguments, [
			{name: "coordsSys", type: Object}
		]);
		if (e) throw e;
		this._coordsSys = coordsSys;
	},

	get_hasHighlight:function(){
	/// <summary>获取或者设置地图是否需要高亮。</summary>
   	/// <returns type="Boolean">get_hasHighlight()返回值类型为Boolean。</returns>
		return 	this._hasHighlight;
	},

	set_hasHighlight:function(hasHighlight){
		var e = Function._validateParams(arguments, [
			{name: "hasHighlight", type: Boolean}
		]);
		if (e) throw e;
		this._hasHighlight = hasHighlight;
	},

	get_imageLayers:function() {
		/// <summary>获取当前地图相关的图层数组。</summary>
		/// <returns type="Array" elementType="Object">当前地图相关的图层数组。</returns>
		return this._imageLayers;
	},

	get_featureLayers:function() {
		/// <summary>获取当前地物要素相关的图层数组。</summary>
		/// <returns type="Array" elementType="Object">地物要素相关的图层数组。</returns>
		return this._featureLayers;
	},
//========================properties end========================

//========================initialize start========================
	initialize:function(onComplete, customParam){
	/// <summary>初始化函数。</summary>
	/// <param name="customParam" mayBeNull="true" type="String">自定义参数。&lt;br&gt;
	/// iServer 允许用户在Web服务端或者iServer服务器端
	/// 根据业务需求进行功能扩展，该参数主要用于传递自定义的功能扩展需要的参数。</param>
	/// <returns type="SuperMap.MapStatus">&lt;see cref="T:SuperMap.MapStatus"&gt;MapStatus&lt;/see&gt;对象。该对象描述了当前地图的状态，如地图名称，显示范围等。</returns>
		this._initLayersKey();
		this._initHandlerLayers();
	    var handler = this._params.commonHandler;

		// 创建完成回调代理
		var onInitCompleteDelegate = Function.createDelegate(this, this._onInitComplete);
		var onInitErrorDelegate = Function.createDelegate(this, this._onInitError);
		this._onInitCompleted = onComplete;

		// 从服务器取得一个参考地图参数，并回调给自己赋值。
		return SuperMap.Committer.commitAjax(this._params.mapName, handler, "GetMapStatus", ["mapServicesAddress", "mapServicesPort", "customParam"], [this._mapServicesAddress, this._mapServicesPort, customParam], false, onInitCompleteDelegate, onInitErrorDelegate, null);
	},

	_onInitComplete:function(jsonMapStatus){
		if(jsonMapStatus){
			var _mapStatus = new SuperMap.MapStatus();
			SuperMap.Decoder.fromJson(_mapStatus, jsonMapStatus);
			_mapStatus.coordsSys = jsonMapStatus.coordsSys;
			if(this._params.x == null){
				this._params.x = _mapStatus.referViewBounds.center().x;
			}
			if(this._params.y == null){
				this._params.y = _mapStatus.referViewBounds.center().y;
			}
			if(this._params.mapScale == null){
				this._mapScale = this._params.mapScale = _mapStatus.referScale;
			}
		    if(this._params.useQuickCache){
				this._useSimpleCache = this._params.useQuickCache;
			}
		    if(this._params.useSimpleCache){
				this._useSimpleCache = this._params.useSimpleCache;
			}

			this._referViewBounds = _mapStatus.referViewBounds;
			this._referMapScale = _mapStatus.referScale;
			this._referViewer = _mapStatus.referViewer;
			this._referMapBounds = _mapStatus.mapBounds;
			this._coordsSys = _mapStatus.coordsSys;
			this._mapName = _mapStatus.mapName;
			// 暂时用这个方法给imageLayer赋值，Layers的处理还没做
			if(!this._global && !this._params.disableMapLayer) {
			    this.getLayersFromJson(_mapStatus.layers);
			} else if(!this._params.disableMapLayer){
			    var globalMapParam = new Object();
			    globalMapParam.mapHandler = this._params.mapHandler;
			    var globalMapLayer = new SuperMap.GlobalMapLayer(null, globalMapParam, this);
			    this._imageLayers.push(globalMapLayer);
			}
            this._layers = _mapStatus.layers;
			
			if(_mapStatus.layersKey){
				this._params.layersKey = _mapStatus.layersKey;
			}
			if(this._onInitCompleted){
    			this._onInitCompleted(_mapStatus);
			}
			return _mapStatus;
		}
		return null;
	},
	
	_onInitError:function(responseText){
	    //alert("初始化地图失败 " + responseText);
		alert(SuperMap.Resource.ins.getMessage("mapInitError") + responseText);
	},
//========================initialize end========================           

//========================dispose start========================           
	dispose:function(){
	/// <summary>释放对象占用的资源。</summary>	
	},
//========================dispose end========================

//========================public methods start========================
	getIsZooming:function(){
	/// <summary>判断当前缩放功能操作的状态。&lt;br&gt;
	/// true表示目前正处于缩放功能处理中，即缩放请求已经提交给Web服务，但是
	/// 缩放请求的结果还没有完全下载到客户端。</summary>
	/// <returns type="Boolean">返回一个布尔值，该值表示目前是否正处于缩放处理中。</returns>
		return this._isZooming;
	},

	setIsZooming:function(isZooming){
	/// <summary>设置当前缩放功能操作的状态。&lt;br&gt;
	/// true表示目前正处于缩放功能处理中，即缩放请求已经提交给Web服务，但是
	/// 缩放请求的结果还没有完全下载到客户端。</summary>
	/// <param name="isZooming" type="Boolean">是否正处于缩放功能处理中。</param>
		this._isZooming = isZooming;
	},

	getZoomCounter:function(){
	/// <summary>获取地图缩放过程中本地地图图片缩放的级别。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。&lt;br&gt;
	/// 该方法主要用于获取当前地图图片缩放到哪一个级别了。
	/// </summary>
	/// <returns type="Number" integer="true">返回一个数值，该值表示地图缩放过程中本地地图图片缩放的级别。</returns>
		return this._zoomCounter;
	},

	setZoomCounter:function(zoomCounter){
	/// <summary>设置地图缩放过程中本地地图图片缩放的级别。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。&lt;br&gt;
	/// 该方法主要用于设置当前地图图片缩放到哪一个级别了。
	/// </summary>
	/// <param name="zoomCounter" type="Number">地图缩放过程中本地地图图片缩放的级别。</param>
		this._zoomCounter = zoomCounter;
	},

	getBounds:function(){
	/// <summary>获取当前地图的空间范围。&lt;br&gt;
	/// 地图的空间范围是其所显示的各数据集的范围的最小外接矩形，即包含各数据集范围的最小的矩形。当地图显示的数据集增加或删除时，其空间范围也会相应发生变化。</summary>
	/// <returns type="SuperMap.Rect2D">返回一个&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;对象，表达当前地图的空间范围。</returns>
		return this._referMapBounds;
	},

	getViewBounds:function(width, height){
	/// <summary>获取当前的可视范围。</summary>
	/// <param name="width" type="Number">宽度。</param>
	/// <param name="height" type="Number">高度。</param>
	/// <returns type="SuperMap.Rect2D">返回一个&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;对象，表达当前地图的可视范围。</returns>
		// 判断是否传入了width和height
		if(!width || !height){
			return this.get_referViewBounds();
		}
		if(this._referViewBounds == null){
			return null;
		}

		var pixelRect = new SuperMap.Rect();
		var leftTop = pixelRect.leftTop;
		var rightBottom = pixelRect.rightBottom;
		leftTop.x = this.get_mapParam().getPixelCenter(this.get_mapScale()).x - width / 2;
		leftTop.y = this.get_mapParam().getPixelCenter(this.get_mapScale()).y - height / 2;

		rightBottom.x = leftTop.x + width;
		rightBottom.y = leftTop.y + height;
		var mapRect = new SuperMap.Rect2D();
		var leftBottom = mapRect.leftBottom;
		var rightTop = mapRect.rightTop;
		var mapLeftTop = this.pixelToMapCoord(leftTop, this.get_mapScale());
		var mapRightBottom = this.pixelToMapCoord(rightBottom, this.get_mapScale());
		leftBottom.x = mapLeftTop.x;
		leftBottom.y = mapRightBottom.y;
		rightTop.x = mapRightBottom.x;
		rightTop.y = mapLeftTop.y;

		return mapRect;
	},
	
	clearImageBuffer:function() {
		/// <summary>清除客户端缓存图片。
		/// </summary>
		if(this._imageLayers) {
			for(var i = 0; i < this._imageLayers.length; i++){
				this._imageLayers[i].clearImageBuffer();
			}
		}
	},
	
	render:function(container, width, height, originX, originY, offsetX, offsetY){
	/// <summary>将各种图层呈现到地图容器中。</summary>
	/// <param name="container" type="object" domElement="true">地图容器。呈现地图的容器，即DOM对象，如img,div。&lt;br&gt;
	/// 地图是对地理数据的可视化，通常由一个或多个图层组成。地图通过地图容器来呈现，每一个图层由各自的图层容器来呈现，这些图层容器都包含在地图容器内。</param>
	/// <param name="width" type="Number">呈现图层的图层容器的宽度。</param>
	/// <param name="height" type="Number">呈现图层的图层容器的高度。</param>
	/// <param name="originX" type="Number">map对象初始化时原点（左上角）的X坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="originY" type="Number">map对象初始化时原点（左上角）的Y坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="offsetX" type="Number">待显示的图层图片原点（左上角）与初始化原点originX的偏移量。</param>
	/// <param name="offsetY" type="Number">待显示的图层图片原点（左上角）与初始化原点originY的偏移量。</param>
		if(this._imageLayers){
			for(var index = 0; index < this._imageLayers.length; index++){
				var layerContainer = this._imageLayers[index].get_container();
				if(layerContainer == null || typeof(layerContainer) == "undefined"){
					if(this._imageLayers[index].getType() != "SuperMap.ForeignImageLayer") {
						layerContainer = document.createElement("div");
						layerContainer.id = container.id + "_" + this._imageLayers[index]._id + "_Div";
						layerContainer.style.top = "0px";
						layerContainer.style.left = "0px";
						layerContainer.style.zIndex = index * 5
						container.appendChild(layerContainer);

						this._imageLayers[index]._container = layerContainer; 
					}
				}
				this._imageLayers[index].set_enableSpiralLoad(this._enableSpiralLoad);
				this._imageLayers[index].render(width, height, originX, originY, offsetX, offsetY);
			}
		}
		
		if(this._featureLayers) {
			for(var index = 0; index < this._featureLayers.length; index++) {
				var layerContainer = this._featureLayers[index].get_container();
				if(layerContainer == null || typeof(layerContainer) == "undefined"){
					layerContainer = document.createElement("div");
					layerContainer.id = container.id + "_" + this._featureLayers[index]._id + "_Div";
					layerContainer.style.top = "0px";
					layerContainer.style.left = "0px";
					layerContainer.style.zIndex = this._imageLayers.length * 5 + this._featureLayers.length; 
					container.appendChild(layerContainer);

					this._featureLayers[index]._container = layerContainer; 
				}
				
				if(this._featureLayers[index]._container.parentNode != container) {
					container.appendChild(this._featureLayers[index]._container);
				}
				this._featureLayers[index].render(originX, originY, offsetX, offsetY);
			}
		}
	},

	renderTrackingLayer:function(container, width, height, originX, originY, offsetX, offsetY){
	/// <summary>呈现跟踪层到跟踪层容器中。</summary>
	/// <param name="container" type="Object" domElement="true">跟踪层容器。呈现跟踪层的容器，即DOM对象，如img,div。</param>
	/// <param name="width" type="Number">呈现跟踪层的图层容器的宽度。</param>
	/// <param name="height" type="Number">呈现跟踪层的图层容器的高度。</param>
	/// <param name="originX" type="Number">map对象初始化时原点（左上角）的X坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="originY" type="Number">map对象初始化时原点（左上角）的Y坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="offsetX" type="Number">待显示的跟踪层原点（左上角）与初始化原点originX的偏移量。</param>
	/// <param name="offsetY" type="Number">待显示的跟踪层原点（左上角）与初始化原点originY的偏移量。</param>
		if(this.trackingLayer && this._hasHighlight){
			var trackingLayerContainer = document.getElementById(container.id + "_" + this.trackingLayer.id + "_Div");
			if(trackingLayerContainer == null || typeof(trackingLayerContainer) == "undefined"){
				trackingLayerContainer = document.createElement("div");
				trackingLayerContainer.id = container.id + "_" + this.trackingLayer.id + "_Div";
				trackingLayerContainer.style.top = "0px";
				trackingLayerContainer.style.left = "0px";
				trackingLayerContainer.style.zIndex = 35;
   			    container.appendChild(trackingLayerContainer);

				this.trackingLayer._container = trackingLayerContainer; 
			}
			this.trackingLayer._bufferSize = 1;
			this.trackingLayer.set_enableSpiralLoad(this._enableSpiralLoad);
			this.trackingLayer.render(width, height, originX, originY, offsetX, offsetY);
		}

		this._trackingLayerCached = true;
	},

	clearLayers:function(container){
	/// <summary>清除所有的图层。</summary>
	/// <param name="container" type="Object">地图容器。</param>
		if(this._imageLayers){
			while(this._imageLayers.length > 0){
				var tempLayer = this._imageLayers.pop();

				if(tempLayer._tiles){
					tempLayer.clearTiles(container);
				}
				tempLayer.dispose();
				tempLayer = null;
			}
		}
		if(this.trackingLayer){
			this.trackingLayer.clearTiles(container);
			this.trackingLayer.dispose();
		}
	},

	clearTiles:function(container){
	/// <summary>清空格网式图层的所有格网块对象。
	/// </summary>
	/// <param name="container" type="Object">格网式图层的容器。</param>
		if(this._imageLayers){
			for(var index = 0; index < this._imageLayers.length; index++){
				if(this._imageLayers[index]._tiles){
					// this._imageLayers[index].clearTiles(container);
					this._imageLayers[index]._clearTiles(this._imageLayers[index]._tiles, true);
					
					// this._imageLayers[index]._clearTiles(this._imageLayers[index]._oldTiles, true);
					// this._imageLayers[index]._clearTiles(this._imageLayers[index]._oldTiles, true);
				}
			}
		}
		if(this.trackingLayer){
			this.trackingLayer.clearTiles(container);
		}
	},

	prepareBaseTile:function(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom){
	/// <summary>根据放大/缩小的比率将格网式图层图片进行缩放。该操作是在新图片下载完成前执行的。
	/// </summary>
	/// <param name="oldOriginX" type="Number">缩放前图片原点（左上角）的X坐标。</param>
	/// <param name="oldOriginY" type="Number">缩放前图片原点（左上角）的Y坐标。</param>
	/// <param name="oldZoom" type="Number">缩放前地图比例尺。</param>
	/// <param name="newOriginX" type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
	/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
	/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
		for(var i = 0; i < this._imageLayers.length; i++){
			if(this._imageLayers[i].getType()=="SuperMap.TiledMapLayer" || this._imageLayers[i].getType()=="SuperMap.TiledWmsLayer" || this._imageLayers[i].getType()=="SuperMap.GlobalMapLayer"){
				this._imageLayers[i].backup();
				this._imageLayers[i].prepareBaseTile(oldOriginX,oldOriginY,oldZoom,newOriginX,newOriginY,newZoom);
			}
		}
		if(this._featureLayers) {
			for(var index = 0; index < this._featureLayers.length; index++) {
				this._featureLayers[index].prepareForZoom(newOriginX,newOriginY,newZoom);
			}
		}
		if(this.trackingLayer && this._hasHighlight){
			this.trackingLayer.prepareBaseTile(oldOriginX,oldOriginY,oldZoom,newOriginX,newOriginY,newZoom);
		}
	},

	prepareSwapTile:function(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom){
	/// <summary>根据缩放前后的比例排列格网式图层中各个格网图片的位置。
	/// </summary>
	/// <param name="oldOriginX" type="Number">缩放前图片原点（左上角）的X坐标。</param>
	/// <param name="oldOriginY" type="Number">缩放前图片原点（左上角）的Y坐标。</param>
	/// <param name="oldZoom" type="Number">缩放前地图比例尺。</param>
	/// <param name="newOriginX" type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
	/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
	/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
		for(var i = 0; i < this._imageLayers.length; i++){
			if(this._imageLayers[i].getType()=="SuperMap.TiledMapLayer" || this._imageLayers[i].getType()=="SuperMap.TiledWmsLayer" || this._imageLayers[i].getType()=="SuperMap.GlobalMapLayer"){
				this._imageLayers[i].prepareSwapTile(oldOriginX,oldOriginY,oldZoom,newOriginX,newOriginY,newZoom);
			}
		}
		if(this.trackingLayer && this._hasHighlight){
			this.trackingLayer.prepareSwapTile(oldOriginX,oldOriginY,oldZoom,newOriginX,newOriginY,newZoom);
		}
	},

	setFactor:function(step){
	/// <summary>设置地图缩放过程中当前缩放状态下本地格网式图层中的图片缩放的大小。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。</summary>
	/// <param name="step" type="Number">当前缩放过程所处的级别。</param>
		for(var i = 0; i < this._imageLayers.length; i++){
			if(this._imageLayers[i].getType() == "SuperMap.TiledMapLayer" || this._imageLayers[i].getType()=="SuperMap.TiledWmsLayer" || this._imageLayers[i].getType() == "SuperMap.WfsLayer" || this._imageLayers[i].getType() == "SuperMap.GeoRssLayer" || this._imageLayers[i].getType() == "SuperMap.KmlLayer" || this._imageLayers[i].getType()=="SuperMap.GlobalMapLayer"){
				this._imageLayers[i].setFactor(step);
			}
		}
		if(this.trackingLayer && this._hasHighlight){
			this.trackingLayer.setFactor(step);
		}
	},

	swapStates:function(){
	/// <summary>格网式图层中新网格图片下载完成后，该方法执行将新网格图片替换原有图片的操作。</summary>
		for(var i = 0; i < this._imageLayers.length; i++){
			if(this._imageLayers[i].getType() == "SuperMap.TiledMapLayer" || this._imageLayers[i].getType()=="SuperMap.TiledWmsLayer" || this._imageLayers[i].getType()=="SuperMap.GlobalMapLayer"){
				this._imageLayers[i].swapStates();
			}
		}
		if(this.trackingLayer && this._hasHighlight){
			this.trackingLayer.swapStates();
		}
	},

	backupLayers:function(){
	/// <summary>备份当前所有的格网式图层对象的格网对象。
	/// </summary>
		for(var i = 0; i < this._imageLayers.length; i++){
			if(this._imageLayers[i].getType() == "SuperMap.TiledMapLayer" || this._imageLayers[i].getType()=="SuperMap.TiledWmsLayer" || this._imageLayers[i].getType()=="SuperMap.GlobalMapLayer"){
				this._imageLayers[i].backup();
			}
		}
		if(this.trackingLayer && this._hasHighlight){
			this.trackingLayer.backup();
		}
	},

	// layer 相关
	addLayer:function(layer){
	/// <summary>添加一个图层。</summary>
	/// <param name="layer" type="SuperMap.layer">要添加的图层对象。</param>
		if(!this._imageLayers){
			this._imageLayers = new Array();
		}
		
		if(!this._featureLayers) {
			this._featureLayers = new Array();
		}
		
		layer.set_map(this);
		var layerType = layer.getType();
		// TrackingLayer单独管理
		if(layerType == "SuperMap.TiledMapLayer" || layerType == "SuperMap.GlobalMapLayer" || layerType == "SuperMap.TiledWmsLayer" || layerType == "SuperMap.ForeignImageLayer") {
			this._imageLayers.push(layer);
		}
		
		if(layerType == "SuperMap.WfsLayer" || layerType == "SuperMap.GeorssLayer" || layerType == "SuperMap.KmlLayer") {
			this._featureLayers.push(layer);
		}
	},

	isValidTile:function(_tx, _ty, ms){
	/// <summary>判断格网式图层的格网对象是否合法。主要是以当前地图比例尺为判断标准，计算出格网对象的图片所在的
	/// x（横向）索引值和y（纵向）索引值是否在合法的范围内。</summary>
	/// <param name="_tx" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="_ty" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="ms" type="Number">地图比例尺。</param>
	/// <returns type="Boolean">返回布尔值，true表示该格网对象合法。</returns>
		return true;
	},

	pixelToMapDistance:function(pixelDistance, mapScale){ 
	/// <summary>将地图中像素单位的长度转换为投影坐标系下的长度。</summary>
	/// <param name="pixelDistance" type="Number">要转换的像素单位的长度。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="Number">返回投影坐标系下的长度。</returns>
		if(this._referViewBounds == null){
			return null;
		}
		return (pixelDistance * this._referMapScale * this._referViewBounds.width())/ (mapScale * this._referViewer.width());
	},

	pixelToMapCoord:function(point, mapScale){
		if(this._referViewBounds == null){
			return null;
		}
	/// <summary>将地图中指定点的像素坐标转换为地图坐标。</summary>
	/// <param name="point" type="SuperMap.Point">要转换的像素坐标，Point类型。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="SuperMap.Point2D">返回地图坐标。&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;类型。</returns>
		var distance = this.pixelToMapDistance(1, mapScale);
		var mc = new SuperMap.Point2D();
		mc.x = this._referMapBounds.leftBottom.x + (point.x * distance);
		mc.y = this._referMapBounds.rightTop.y - (point.y * distance);
		return mc;
	},

	mapCoordToPixel:function(point2D, mapScale){
		if(this._referViewBounds == null){
			return null;
		}
	/// <summary>将地图中指定点的地图坐标转换为像素坐标。</summary>
	/// <param name="point2D" type="SuperMap.Point2D">要转换的地图坐标点，Point2D类型。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="SuperMap.Point">返回像素坐标。&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</returns>
		var distance = this.pixelToMapDistance(1, mapScale);
		var pc = new SuperMap.Point();
		pc.x = Math.floor((point2D.x - this._referMapBounds.leftBottom.x) / distance);
		pc.y = Math.floor((this._referMapBounds.rightTop.y - point2D.y) / distance);
		return pc;
	},

	validateMapScale:function(mapParam){
	/// <summary>验证MapScale的值是否合法,MapScale应该大于等于零。</summary>
	/// <param name="mapParam" type="Object">地图参数对象。根据该参数对象的MapSacle进行合法性验证。</param>
	/// <returns type="Boolean">返回一个布尔值，true代表该MapScale是一个大于等于零的合法值。</returns>
		if(mapParam.mapScale <= 0){
			mapParam.setMapScale(1);
		}
		return mapParam.mapScale > 0;
	},

	update:function(onComplete, onError, customParam, userContext){
	/// <summary>更新地图。</summary>
	/// name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String layersKey, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
		var changedLayersFromHandlerJSON = SuperMap.Utility.findDifference(this._layersBackupForHandler, this._params.layers);
		var methodName = "UpdateLayers";
//		var paramNames = ["layers","layersKey", "bModifiedByServer", "mapScale", "customParam"];
		var paramNames = ["layersKey", "bModifiedByServer", "mapScale", "customParam"];
		var bModifiedByServer = false;
		if(this._params.bModifiedByServer == true){
			//在server上修改了，提交到mapHandler上清理一下缓存.
			bModifiedByServer = true;
			this._params.bModifiedByServer = false;
		}
//		var paramValues = [changedLayersFromHandlerJSON, this._params.layersKey, bModifiedByServer, this._mapScale, customParam];
		var paramValues = [this._params.layersKey, bModifiedByServer, this._mapScale, customParam];

		SuperMap.Committer.commitMapCmd(this._mapName, "UpdateLayers", paramNames, paramValues , onComplete, onError, userContext);
	},

	//距离量算
	measureDistance:function(points, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>量算距离。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成沿线的地理坐标数组。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示量算路线。True代表高亮显示线路。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.MeasureResult"&gt;MeasureResult&lt;/see&gt; measureResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
		var methodName = "MeasureDistance";
		var paramNames = ["points","needHighLight", "customParam"];
		var paramValues = [points, needHighlight, customParam];
		var mapName = this._params.mapName;
        this._hasHighlight = needHighlight;

		var onCompleteArray = new Array();
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}

        if(onComplete && onComplete instanceof Array){
	        while(onComplete.length > 0){
		        var tempOnComplete = onComplete.pop();
				if(tempOnComplete){
					onCompleteArray.push(tempOnComplete);
				}
			}
		}else{
			onCompleteArray.push(onComplete);
		}
		
		SuperMap.Committer.commitMapCmd(mapName, methodName, paramNames, paramValues, onCompleteArray, null, null);
	},
	
	//面积量算
	measureArea:function(points, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>量算面积。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成量算区域的地理坐标点数组。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示量算区域。True代表高亮显示区域。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.MeasureResult"&gt;MeasureResult&lt;/see&gt; measureResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
		var methodName = "MeasureArea";
		var paramNames = ["points", "needHighLight", "customParam"];
		var paramValues = [points, needHighlight, customParam];
		var mapName = this._params.mapName;
        this._hasHighlight = needHighlight;

		var onCompleteArray = new Array();
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}

        if(onComplete && onComplete instanceof Array){
	        while(onComplete.length > 0){
		        var tempOnComplete = onComplete.pop();
				if(tempOnComplete){
					onCompleteArray.push(tempOnComplete);
				}
			}
		}else{
			onCompleteArray.push(onComplete);
		}
		
		SuperMap.Committer.commitMapCmd(mapName, methodName, paramNames, paramValues, onCompleteArray, null, null);
	},

	clearHighlight:function(onComplete, onError, customParam, userContext){
	/// <summary>清除高亮对象。</summary>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(Boolean succeed, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
		var methodName = "ClearHighlight";
		var paramNames = ["customParam"];

		var changeTrackingLayerKeyInternalDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
		
		var paramValues = [customParam];
		var mapName = this._params.mapName;

		var onCompleteArray = new Array();
		onCompleteArray.push(changeTrackingLayerKeyInternalDelegate);
		onCompleteArray.push(onComplete);
        this._hasHighlight = false;

		SuperMap.Committer.commitMapCmd(mapName, methodName, paramNames, paramValues, onCompleteArray, null, null);
	},

	setLayers:function(layers, onComplete, onError, customParam, userContext) {
	/// <summary>更新iServer服务器提供的地图中的图层。</summary>
	/// <param name="layers" type="Array" elementType="Object">GIS 服务器提供的地图的图层对象数组。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String layersKey, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
		var onCompleteArray = new Array();
		onCompleteArray.push(onComplete);
		onCompleteArray.push(this._onSetLayerCompleteDelegate);
		// mapName 设置
        SuperMap.Committer.commitAjax(this._mapName, this.queryUrl, "setLayers", ["layers", "customParam"], [layers, customParam], false, onCompleteArray, onError, userContext);
	},
	
	onSetLayerComplete:function(responseText, userContext){
	/// <summary>完成更新iServer服务器提供的地图中的图层操作后触发的事件，去修改客户端的图层信息。
	/// </summary>
	/// <param name="responseText" type="String">服务器端返回的文本信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。
	/// </param>
	    // 刷新图片
	    if(responseText){
            var layerKey = eval(responseText);
            if(layerKey){
                this._params.layersKey = layerKey;
                this.raise_layerChanged();
            }
	    }
	},

    // 查询相关方法
    queryByPoint:function(point, tolerance, queryParam, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>点选查询。指定地图的某一点的位置，查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
	/// <param name="point" type="SuperMap.Point2D">查询的位置。SuperMap.Point2D类型。</param>
	/// <param name="tolerance" type="Number">查询容限。double 类型。&lt;br&gt;
	/// 该值不能小于0，建议取一个适中的值，比如5。&lt;br&gt;
	/// 在该查询方法是以查询位置（点）为圆心，以tolerance的地理长度为半径的圆的范围内进行地物的查找。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
	
		this._queryBase("QueryByPoint", ["point", "tolerance", "queryParam", "needHighlight", "customParam"], [point, tolerance, queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },

    queryBySql:function(queryParam, needHighlight, onComplete, onError, customParam, userContext){
	 /// <summary>执行SQL查询。 </summary>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._queryBase("QueryBySql", ["queryParam", "needHighlight", "customParam"], [queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },
	
	queryByLine:function(points, queryParam, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>查询与指定的线相交的某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成线的点数组，SuperMap.Point2D类型。</param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._queryBase("QueryByLine", ["points", "queryParam", "needHighlight", "customParam"],[points, queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },
	
	queryByRect:function(mapRect, queryParam, needHighlight, onComplete, onError, customParam, userContext){
 	/// <summary>在指定的矩形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
	/// <param name="mapRect" type="SuperMap.Rect2D">矩形。矩形的范围就是查询的范围，SuperMap.Rect2D类型。</param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._queryBase("QueryByRect", ["mapRect", "queryParam", "needHighlight", "customParam"],[mapRect, queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },
	
	queryByPolygon:function(points, queryParam, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>在指定的多边形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成多边形的点数组，SuperMap.Point2D类型。</param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
    	this._queryBase("QueryByPolygon", ["points", "queryParam", "needHighlight", "customParam"], [points, queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },
	
	queryByCircle:function(center, radius, queryParam, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>在指定的圆范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
	/// <param name="center" type="SuperMap.Point2D">圆的中心点, SuperMap.Point2D类型。</param>
	/// <param name="radius" type="Number">圆的半径,double类型。</param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._queryBase("QueryByCircle", ["center", "radius", "queryParam", "needHighlight", "customParam"], [center, radius, queryParam, needHighlight, customParam],onComplete, onError, userContext);
    },
	
    findNearest:function(point, tolerance, queryParam, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>查找某范围内最近的对象。&lt;br&gt;
	/// 特别说明：在线图层或面图层中,这里的距离指的从指定起始点到目标对象边线的最短长度。 </summary>
	/// <param name="point" type="SuperMap.Point2D">查询的起始点位置。SuperMap.Point2D类型。</param>
	/// <param name="tolerance" type="Number">从起始点开始允许查询的范围的最大直径。Double类型。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
        this._queryBase("FindNearest", ["point", "tolerance", "queryParam", "needHighlight", "customParam"], [point, tolerance, queryParam, needHighlight, customParam], onComplete, onError, userContext);
    },
	
	// 编辑相关方法
	addEntity:function(layerName, entity, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>在指定的iServer服务器端地图图层上增加一个同类型的实体。</summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="entity" type="SuperMap.Entity">新增的实体。SuperMap.Entity 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示新添加的实体。True代表高亮显示该实体。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var entities = new Array();
		entities.push(entity);
    	this._editBase(this.get_mapName(), "AddEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    deleteEntity:function(layerName, ids, onComplete, onError, customParam, userContext) {
    /// <summary>删除指定iServer服务器端地图图层中的某些几何实体。</summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="ids" type="Array" elementType="Number">实体的SmID数组，该组实体对象将被删除。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._editBase(this.get_mapName(), "DeleteEntity", ["layerName", "ids", "customParam"], [layerName, ids, customParam], onComplete, onError, userContext);
    },
    
    updateEntity:function(layerName, entity, needHighlight, onComplete, onError, customParam, userContext) {
	/// <summary>更新一个实体。</summary>
	/// <param name="layerName" type="String">待更新的iServer服务器端地图的图层名称。</param>
	/// <param name="entity" type="SuperMap.Entity">待更新的实体对象。SuperMap.Entity 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示更新的实体。True代表高亮显示该实体。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var entities = new Array();
		entities.push(entity);
    	this._editBase(this.get_mapName(), "UpdateEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    addPoint:function(layerName, point, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext) {
	/// <summary>在指定的iServer服务器端地图图层中添加一个点，包括点的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="point" type="SuperMap.Point2D">新添加的点的位置。SuperMap.Point2D类型。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">新增的点对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的点。True代表高亮显示该点。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.feature = new SuperMap.LayerType().point;
        entity.shape.point2Ds = new Array(point);
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "AddEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    addLine:function(layerName, points, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext) {
    /// <summary>在指定的iServer服务器端地图图层中添加一条线，包括线的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">新添加的线对象的有序点数组。该数组长度必须大于等于2。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">新增的线对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的线。True代表高亮显示该对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
	    var entity = new  SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.feature = new SuperMap.LayerType().line;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "AddEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
       
    addPolygon:function(layerName, points, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext) {
    /// <summary>在指定的iServer服务器端地图图层中添加一个面，包括面的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">用来创建面对象的有序点集合。该数组长度必须大于等于3。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">新增的面对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的面。True代表高亮显示该对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param> 
	    var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.feature = new SuperMap.LayerType().polygon;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "AddEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    addText:function(layerName, point, textValue, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>在指定的iServer服务器端地图图层中添加一个文本对象。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="point" type="SuperMap.Point2D">文本对象的中心点的坐标。Point2D对象。</param>
	/// <param name="textValue" type="String">文本对象的文本内容。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的文本。True代表高亮显示该对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.feature = new SuperMap.LayerType().text;
        entity.shape.point2Ds = new Array();
		entity.shape.point2Ds.push(point);
        entity.fieldValues = new Array();
		entity.fieldValues.push(textValue);
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "AddEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
	},
	
    updatePoint:function(layerName, id, point, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>在指定的iServer服务器端地图图层中更新一个点，包括点的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="id" type="Number">要更新的点对象的SMID值。</param>
	/// <param name="point" type="SuperMap.Point2D">更新的点的新坐标。SuperMap.Point2D类型。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">新增的点对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的点。True代表高亮显示该点。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
	    var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().point;
        entity.shape.point2Ds = new Array(point);
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
		// mapName 为空
        this._editBase(this.get_mapName(), "UpdateEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    updateLine:function(layerName, id, points, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>在指定的iServer服务器端地图图层中更新一条线，包括线的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="id" type="Number">要更新的线对象的SMID值。</param>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">更新的线对象的新的有序点数组。该数组长度必须大于等于2。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">更新的线对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示添加的线。True代表高亮显示该对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().line;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "UpdateEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
    updatePolygon:function(layerName, id, points, fieldNames, fieldValues, needHighlight, onComplete, onError, customParam, userContext){
    /// <summary>在指定的iServer服务器端地图图层中更新一个面，包括面的属性。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="id" type="Number">更新的面对象的SMID值。</param>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">要更新的面对象的新的有序点集合。该数组长度必须大于等于3。</param>
	/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
	/// <param name="fieldValues" type="Array" elementType="String">更新的面对象的各个属性字段值数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示更新的面。True代表高亮显示该对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param> 
	    var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().polygon;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		var entities = new Array();
		entities.push(entity);
        this._editBase(this.get_mapName(), "UpdateEntity", ["layerName", "entities", "needHighlight", "customParam"], [layerName, entities, needHighlight, customParam], onComplete, onError, userContext);
    },
    
	getEntity:function(layerName, id, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>在指定的iServer服务器端地图图层中获取一个实体对象。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="id" type="Number">实体对象的SMID值。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示。True代表高亮显示该对象。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.Entity"&gt;Entity&lt;/see&gt; entity, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param> 
		var methodName = "GetEntity";
		var paramNames = ["layerName", "id", "needHighlight","customParam"];
		var paramValues = [layerName, id, needHighlight, customParam];
        this._hasHighlight = needHighlight;

		function onGetEntityComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var entity = SuperMap.Entity.fromJson(responseResult);
			if(onComplete){
				onComplete(entity, userContext);
				entity.dispose();
				entity = null;
			}
			if(entity != null){
				entity.dispose();
				entity = null;
			}
		};
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onGetEntityComplete, onError, userContext);
    },

	getEntities:function(layerName, ids, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>在指定的iServer服务器端地图图层中获取指定的实体对象。该方法可以获取多个实体对象。</summary>
	/// <param name="layerName" type="String">iServer服务器端地图的图层名称。</param>
	/// <param name="ids" type="Arrary">实体对象的SMID数组。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示获得的实体对象。True代表高亮显示这些对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.Entity"&gt;Entity[]&lt;/see&gt; entitys, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param> 
		var methodName = "GetEntities";
		var paramNames = ["layerName", "ids", "needHighlight", "customParam"];
		var paramValues = [layerName, ids, needHighlight, customParam];
        this._hasHighlight = needHighlight;
		function onGetEntitiesComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var entities = new Array();
			for(var i = 0; i < responseResult.length; i++){
				entities[i] = SuperMap.Entity.fromJson(responseResult);
			}
			if(onComplete){
				onComplete(entities, userContext);
			}
			while(entities.length > 0){
				var entity = entities.pop();
				entity.dispose();
				entity = null;
			}
			entities = null;
		};
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onGetEntitiesComplete, onError, userContext);
	},

    // 控件分析相关内容
	lineBufferQuery:function(points, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的线制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成线的点数组，SuperMap.Point2D数组类型。</param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对线做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("LineBufferQuery", ["points", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [points, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	circleBufferQuery:function(point, radius, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的圆制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="point" type="SuperMap.Point2D">圆的中心店坐标,SuperMap.Point2D类型。</param>
	/// <param name="radius" type="Number">圆的半径。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对圆做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("CircleBufferQuery", ["center", "radius", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [point, radius, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	rectBufferQuery:function(mapRect, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的矩形制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="mapRect" type="SuperMap.Rect2D">矩形。矩形的范围就是查询的范围，SuperMap.Rect类型。</param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对矩形做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就&lt;br&gt;设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("RectBufferQuery",["mapRect", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [mapRect, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	pointBufferQuery:function(point, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的点制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="point" type="SuperMap.Point2D">点的坐标。SuperMap.Point2D类型。</param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对点做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("PointBufferQuery", ["point", "bufferParam", "queryParam", "queryMode",  "needHighlight", "customParam"], [point, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	multiPointBufferQuery:function(points, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的多点制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">自定义多点数组，SuperMap.Point2D数组类型。</param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对多边形做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("MultiPointBufferQuery", ["points", "bufferParam", "queryParam", "queryMode",  "needHighlight", "customParam"], [points, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	polygonBufferQuery:function(points, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对自绘的多边形制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成多边形的点数组，SuperMap.Point2D数组类型。</param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">对多边形做缓冲区的设置参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("PolygonBufferQuery", ["points", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [points, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	lineEntityBufferQuery:function(points, fromLayer, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与points组成的线段有交集的几何对象。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成线的点数组，SuperMap.Point2D数组类型。</param>
	/// <param name="fromLayer" type="String">缓冲线对象所在的iServer服务的地图图层的名称。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("LineEntityBufferQuery", ["points", "fromLayer", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [points, fromLayer, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	circleEntityBufferQuery:function(point, radius, fromLayer, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由point和radius组成的圆有交集的几何对象。</summary>
	/// <param name="point" type="SuperMap.Point2D">圆的中心点坐标,SuperMap.Point2D类型。</param>
	/// <param name="radius" type="Number">圆的半径。
	/// </param>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("CircleEntityBufferQuery", ["center", "radius", "fromLayer", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [point, radius, fromLayer, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	rectEntityBufferQuery:function(mapRect, fromLayer, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与mapRect矩形有交集的几何对象。</summary>
	/// <param name="mapRect" type="SuperMap.Rect2D">矩形。矩形的范围就是查询的范围，SuperMap.Rect类型。</param>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("RectEntityBufferQuery",["mapRect", "fromLayer", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [mapRect, fromLayer, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	pointEntityBufferQuery:function(point, fromLayer, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与point点有交集的几何对象。</summary>
	/// <param name="point" type="SuperMap.Point2D">点的坐标。SuperMap.Point2D类型。</param>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("PointEntityBufferQuery", ["point", "fromLayer", "bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [point, fromLayer, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	polygonEntityBufferQuery:function(points, fromLayer, bufferParam, queryParam, queryMode, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由points组成的多边形有交集的几何对象。</summary>
	/// <param name="points" type="Array" elementType="SuperMap.Point2D">组成多边形的点数组，SuperMap.Point2D数组类型。</param>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。
	/// </param>
	/// <param name="bufferParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._bufferQueryBase("PolygonEntityBufferQuery", ["points", "fromLayer","bufferParam", "queryParam", "queryMode", "needHighlight", "customParam"], [points, fromLayer, bufferParam, queryParam, queryMode, needHighlight, customParam], onComplete, onError, userContext);
	},
	
	overlayAnalyst:function(overlayParam, showResult, onComplete, onError, customParam, userContext){
	/// <summary>叠加分析。&lt;br&gt;
	///		（1）叠加分析是指在两个数据集之间进行的一系列集合运算，是GIS中的一项非常重要的空间分析功能。
	/// 比如，我们需要了解某一个行政区内的土壤分布情况，我们就根据全国的土地利用图和行政区规划图这两个数据集进行叠加分析，
	/// 然后得到我们需要的结果，从而进行各种分析评价。&lt;br&gt; 
	///		（2）叠加分析涉及到两个数据集，其中一个数据集为操作数据集，必须为面数据集，另一个数据集为被操作数据集， 
	/// 除合并运算和对称差运算必须是面数据集外，其他运算可以是点、线、面或者复合数据集。&lt;br&gt;
	///		（3）可以进行点与面的叠加、 线与面的叠加、多边形与面的叠加。 &lt;br&gt;
	///		（4）进行叠加分析之前，根据OverlayParam设置叠加分析的条件， 包括操作数据集的名称operateDatasetName、叠加分析的类型Operation等。&lt;br&gt; 
	///		（5）叠加分析的结果通过OverlayResult获取，包括叠加分析结果数据集resultDatasetName 和叠加分析操作是否成功Succeed。
	/// </summary>
	/// <param name="overlayParam" type="SuperMap.OverlayParam">叠加分析参数。
	/// </param>
	/// <param name="showResult" type="Boolean">是否显示叠加分析结果。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.OverlayResult"&gt;OverlayResult&lt;/see&gt; overlayResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>	
		var methodName = "OverlayAnalyst";
		function onOverlayAnalystComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var overlayResult = SuperMap.OverlayResult.fromJson(responseResult);
			if(onComplete){
				onComplete(overlayResult, userContext);
			}
			if(overlayResult != null){
				overlayResult.dispose();
				overlayResult = null;
			}
		};
		if(showResult) {
		    // 应该刷新一下layersKey
		}
        SuperMap.Committer.commitAjax(this._mapName, this.queryUrl, methodName, ["overlayParam", "showResult", "customParam"], [overlayParam, showResult, customParam], false, onOverlayAnalystComplete, onError, userContext);
	},

	spatialOperate:function(sourceGeometry, operatorGeometry, spatialOperationType, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>对指定的地物对象和目标地物对象之间进行求交、并、差等空间操作。
	/// </summary>
	/// <param name="sourceGeometry" type="SuperMap.Geometry">指定的源地物对象。</param>
	/// <param name="operatorGeometry" type="SuperMap.Geometry">目标地物对象。
	/// </param>
	/// <param name="spatialOperationType" type="SuperMap.SpatialOperationType">空间操作类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var methodName = "spatialOperate";
		var onCompleteArray = new Array();
        this._hasHighlight = needHighlight;
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}
		onCompleteArray.push(onComplete);

        SuperMap.Committer.commitAjax(this._mapName, this.queryUrl, methodName, ["sourceGeometry", "operatorGeometry", "spatialOperationType", "needHighlight", "customParam"], [sourceGeometry, operatorGeometry, spatialOperationType, needHighlight, customParam], false, onCompleteArray, onError, userContext);
	},
	
	findPath:function(networkSetting,pathParam, needHighlight,onComplete, onError, customParam, userContext){
	/// <summary>查找网络中结点间的最短路径或者最佳路径。查找结果放入NetworkAnalystResult内。&lt;br&gt;
	/// 网络中两结点间最短路径，通常是根据距离来计算的；而最佳路径则根据两点间所有弧段的正向和反向的阻值字段、
	/// 转向表等多种因素一起计算出两点间的最佳路径。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="pathParam" type="SuperMap.PathParam">路径分析参数。PathParam类型。路径分析功能必选参数。
	/// 可以通过PathParam.networkAnalystParam来设置路径分析需要的各种参数。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示分析结果。True代表高亮显示。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		if(networkSetting!=null && pathParam != null){
			this._findPathBase("FindPath", ["networkSetting","pathParam", "needHighlight","customParam"],[networkSetting,pathParam, needHighlight, customParam], onComplete, onError, userContext);
		}
    },
    
	findTSPPath:function(networkSetting,tspPathParam, needHighlight,onComplete, onError, customParam, userContext){
	/// <summary>旅行商分析。&lt;br&gt;
	///		（1）查找结果放入NetworkAnalystResult内。&lt;br&gt;
	///		（2）通过指定出发和到达的位置及所有需要经过的游历点，查找相对最佳的游历路线，保证每个游历点只经过一次的情况下，总耗费最小。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="tspPathParam" type="SuperMap.TSPPathParam">旅行商分析参数。TSPPathParam类型。路径分析功能必选参数。
	/// 可以通过TSPPathParam.networkAnalystParam来设置路径分析需要的各种参数。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示分析结果。True代表高亮显示。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		if(networkSetting!=null && tspPathParam != null){
			this._findPathBase("FindTSPPath", ["networkSetting","tspPathParam", "needHighlight", "customParam"],[networkSetting,tspPathParam, needHighlight, customParam], onComplete, onError, userContext);
		}
    },
    
	closestFacility:function(networkSetting, eventPoint, proximityParam, needHighlight, onComplete, onError, customParam, userContext){
	/// <summary>最近设施分析。&lt;br&gt;
	/// 1. 最近设施查找：&lt;br&gt;
	///		为事件点查找以最小耗费能到达的设施点，结果显示从事件点到设施点的最佳路径，耗费，及行驶方向。该功能还可以设置查找的阈值，
	/// 即搜索范围，超过该范围将不再进行查找，例如事件发生点是一起交通事故，要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。&lt;br&gt;
	/// 此例中，事故发生地即是一个事件点，周边的医院则是设施点。最近设施查找实际上也是一种路径分析，因此，同样可以应用障碍边和障碍点的设置，
	/// 在行驶路途上这些障碍将不能被穿越，在路径分析中会予以考虑。&lt;br&gt;
	/// 2. 最近设施查找的步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过proximityParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；
	/// 通过proximityParam 对象设定查找的设施个数，设定查找的方向，设定查找范围（单位同网络分析环境中设置的阻力字段，如不限定查找范围，该值可设为0）。&lt;br&gt;
	/// （3）事件点和设施点准备。该方法每次为一个事件点进行最近设施查找，该点是在分析区域内的坐标点，也可以是网络结点。
	/// 设施点通常由多个点组成，同样既可以是坐标点，也可以是网络结点，通常设施点通过proximityParam.networkAnalystParam.point2Ds设定。&lt;br&gt;
	/// （4）最近设施查找。通过NetworkAnalystManager.closestFacility方法，传入事件点(point),最近设施查询的参数（proximityParam）。&lt;br&gt;
	/// （5）最近设施查找结果。该结果是NetworkAnalystResult类型，其中从事件点到设施点或者从设施点到事件点的最佳路径。&lt;br&gt;
	/// 3. 需要注意：事件点和设施点的类型必须一致，都是坐标点或者都是网络结点，否则该方法会失败。因此使用该方法时，设施点必须通过通过proximityParam.networkAnalystParam.point2Ds设定。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="eventPoint" type="SuperMap.Point2D">事件点坐标（Point2D）。 
	/// </param>
	/// <param name="proximityParam" type="SuperMap.ProximityParam">最近设施分析参数。最近设施分析功能必选参数。</param>
	/// <param name="needHighlight" type="Boolean">是否需要高亮显示分析结果。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		if(networkSetting!=null && proximityParam != null && eventPoint!=null){
			this._findPathBase("ClosestFacility", ["networkSetting","eventPoint","proximityParam", "needHighlight","customParam"], [networkSetting, eventPoint, proximityParam, needHighlight, customParam], onComplete, onError, userContext);
		}
    },
	serviceArea:function(networkSetting,serviceAreaParam, needHighlight,onComplete, onError, customParam, userContext){
	/// <summary>服务区分析。&lt;br&gt;
	/// 1. 关于服务区及服务区分析：&lt;br&gt;
	///		服务区：以指定点为中心，在一定阻力范围内，包含所有可通达边的一个区域。&lt;br&gt;
	///		服务区分析：为网络上指定的位置点计算服务范围。例如：为网络上某点计算其30分钟的服务区，则结果服务区内，任意点出发到该点的时间都不会超过30分钟。&lt;br&gt;
	/// 2. 服务区分析步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过ServiceAreaParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；&lt;br&gt;
	/// （3）准备服务中心点信息。通过ServiceAreaParam.networkAnalystParam.point2Ds或者nodeIDs设置各个服务中心点，可以是普通坐标点，也可以是网络结点，
	/// 之后设置每个服务中心点的服务阻值ServiceAreaParam.weights（以阻力为单位，该单位基于网络分析环境中阻力字段的单位）。&lt;br&gt;
	/// （4）服务区分析。通过NetworkAnalystManager.serviceArea方法，传入服务中心点和服务半径，设定计算方向。&lt;br&gt;
	/// 3. 服务区分析结果：&lt;br&gt;
	///		分析成功之后，通过ServiceAreaResult返回两类结果,一个表示各个服务中心点的服务路径，另一个表示各个服务中心点的服务区域。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="serviceAreaParam" type="SuperMap.serviceAreaParam">服务区分析参数。</param>
	/// <param name="needHighlight" type="Boolean">是否需要高亮显示分析结果。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		if(networkSetting!=null && serviceAreaParam != null){
			this._findPathBase("ServiceArea", ["networkSetting","serviceAreaParam", "needHighlight","customParam"],[networkSetting,serviceAreaParam, needHighlight, customParam], onComplete, onError, userContext);
		}
    },
    
    getMapBounds:function(){
	/// <summary>获取当前地图的空间范围。&lt;br&gt;
	/// 地图的空间范围是其所显示的各数据集的范围的最小外接矩形，
	/// 即包含各数据集范围的最小的矩形。当地图显示的数据集增加或删除时，其空间范围也会相应发生变化。
	/// </summary>
	/// <returns type="SuperMap.Rect2D">返回地图的空间范围。&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;类型。</returns>
		var mb = new SuperMap.Rect2D();
		mb.copy(this._referMapBounds);
		return mb;
	},
	
	// jsonLayer和脚本中的Layer结构不同，需要整体转化一下
	getLayersFromJson:function(jsonLayers){
	/// <summary>将JSON类型的客户端呈现图层转化为客户端呈现图层对象。</summary>
	/// <param name="jsonLayers" type="Object">JSON类型的客户端呈现图层。</param>
	/// <returns type="SuperMap.LayerBase">返回一个&lt;see cref="T:SuperMap.LayerBase"&gt;Layer&lt;/see&gt;对象。</returns>
        if(jsonLayers && jsonLayers instanceof Array){
            var layerSettingType = new SuperMap.LayerSettingType();
            for(var i = 0; i < jsonLayers.length; i++){
                var tempLayer = jsonLayers[i];
                if(tempLayer.layerSetting){
                    if(tempLayer.layerSetting.layerSettingType == layerSettingType.collection || tempLayer.layerSetting.layerSettingType == layerSettingType.supermapcollection){//?layerSettingType.collection要去掉
                        var mapLayer = new SuperMap.TiledMapLayer(null, this._params, this);
                	    mapLayer._id = tempLayer.name;
                	    mapLayer._name = tempLayer.caption;
	            	    mapLayer._transparent = false;
                	    mapLayer._opacity = tempLayer.opaqueRate;                    
                	    mapLayer._minScale = tempLayer.minScale;
                	    mapLayer._maxScale = tempLayer.maxScale;
                	    mapLayer._enableSpiralLoad = true;
                	    this._imageLayers.push(mapLayer);
                    }else if(tempLayer.layerSetting.layerSettingType == layerSettingType.wms){
                        var wmsLayer = new SuperMap.TiledWmsLayer(null, tempLayer.layerSetting);
						if(tempLayer.opaqueRate == 0) {
							if(!wmsLayer._param) {
								wmsLayer._param = new Object();
							}
							wmsLayer._param.transparent = true;
						}
						wmsLayer._manageOnServer = true;
						wmsLayer.set_map(this);
						// wmsLayer._enableImageBuffer = false;
						wmsLayer._id = "Wmslayer" + this._imageLayers.length;
						this._imageLayers.push(wmsLayer);
                    }
                }
            }
        }
	},
	
	addTheme:function(layerName, themeType, theme, onComplete, onError, customParam, userContext) {
	/// <summary>在iServer服务端地图中添加专题图。</summary>
	/// <param name="layerName" type="String">制作专题图的图层名称。该图层指iServer服务端地图图层。</param>
	/// <param name="themeType" type="Number">专题图的类型，如単值专题图，统计专题图等。</param>
	/// <param name="theme" type="Object">专题图对象。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String succeed, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
		var methodName = "addTheme";
		var paramNames = ["layerName", "themeType", "theme", "customParam"];
		var paramValues = [layerName, themeType, theme, customParam];
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onComplete, onError, userContext);
	},
	
	removeSuperMapLayer:function(layerName, onComplete, onError, customParam, userContext) {
	/// <summary>移除iServer服务端地图中某个图层。</summary>
	/// <param name="layerName" type="String">要移除的图层名称。该图层指iServer服务端地图图层。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String succeed, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var methodName = "removeSuperMapLayer";
		var paramNames = ["layerName"];
		var paramValues = [layerName];
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onComplete, onError, userContext);
	},
	
	removeSuperMapLayers:function(layerNames, onComplete, onError, customParam, userContext) {
	/// <summary>移除 iServer 服务端地图中某几个图层。</summary>
	/// <param name="layerNames" type="String">要移除的图层名称。该图层指 iServer 服务端地图图层。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(String succeed, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="customParam" type="String">用户自定义操作的参数对象，该值用于存储自定义操作需要的参数信息。
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		var methodName = "removeSuperMapLayers";
		var paramNames = ["layerNames"];
		var paramValues = [layerNames];
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onComplete, onError, userContext);
	},
//========================public methods end========================

//========================component events start========================
	clearEvents:function(){
	/// <summary>清除所有的自定义事件。</summary>
		while(this._eventsNameList.length){
			var eventName = this._eventsNameList.pop();
			this.get_events().removeHandler(eventName, this.get_events().getHandler(eventName));
		}
		this._eventsNameList = null;
	},

	add_trackinglayerChanged:function(handler){
	/// <summary>添加跟踪层变化事件。当跟踪层有变化时，触发指定的事件函数（handler）。</summary>
	/// <param name="handler" type="Function">事件被触发时执行的函数。</param>
		this._addEvent("ontrackinglayerchanged", handler);
	},

	remove_trackinglayerChanged:function(handler){
	/// <summary>移除跟踪层变化事件。</summary>
	/// <param name="handler" type="Function">事件被触发时执行的函数。</param>
		this._removeEvent("ontrackinglayerchanged", handler);
	},

	raise_trackinglayerChanged:function(arguments, userContext){
	/// <summary>触发跟踪层变化事件。</summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>	
		this._raiseEvent("ontrackinglayerchanged", arguments, userContext);
	},

	add_layerChanged:function(handler){
	/// <summary>添加图层变化事件。当客户端呈现图层发生变化时，触发指定的事件函数（handler）。</summary>
	/// <param name="handler" type="Function">事件被触发时执行的函数。</param>
		this._addEvent("onlayerchanged", handler);
	},

	remove_layerChanged:function(handler){
	/// <summary>移除图层变化事件。该图层指客户端呈现图层。</summary>
	/// <param name="handler" type="Function">事件被触发时执行的函数。</param>	
		this._removeEvent("onlayerchanged", handler);
	},

	raise_layerChanged:function(arguments, userContext){
	/// <summary>触发图层变化事件。该图层指客户端呈现图层。</summary>
	/// <param name="arguments" type="Array">为该事件提供的任意参数。可以是任意类型。</param>
	/// <param name="userContext" type="Function">上下文环境。</param>	
		this._raiseEvent("onlayerchanged", arguments, userContext);
	},

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
		// 如果eventName的事件没有了，需要把this._eventsNameList中的事件名删除
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
//========================component events end========================

//========================private methods start========================
	_initLayersKey:function(){
		var layersKey 
		var hidden = $get(this._params.id + "_hiddenLayersKey")
		if(hidden){
			layersKey = hidden.value;
		}
		if(!layersKey){
			this._params.layersKey = 0;
		}else{
			this._params.layersKey = eval(layersKey);
		}
	},
	
	_initHandlerLayers:function (){
		var layersInfo 
		var hidden = $get(this._params.id + "_hiddenHandlerLayers")
		if(hidden){
			layersInfo = hidden.value;
		}
		if(layersInfo){
			var o = eval(layersInfo);
			if(o){
				for(var i = 0;i < o.length; i++){
					this._layersBackupForHandler[i] = SuperMap.Layer.fromJson(o[i]);
				}
			}
		}
	},
	
	//对内,更新是由服务器完成,触发事件
	_changeTrackingLayerKeyInternal:function(){
		this.raise_trackinglayerChanged(null);
	},
	
	// 查询相关方法
	_queryBase:function(methodName, paramNames, paramValues, onComplete, onError, userContext){
        function onQueryComplete(result){
			var resultSet = SuperMap.ResultSet.fromJson(result);
			if(onComplete){
				if(onComplete instanceof Array) {
					for(var i = 0; i < onComplete.length; i++) {
						var onCompleteFunction = onComplete[i];
						onCompleteFunction(resultSet, userContext);
					}
				} else {
					onComplete(resultSet, userContext);
				}
			}
			if(resultSet != null){
				resultSet.dispose();
				resultSet = null;
			}
		};
		
		var onCompleteArray = new Array();
		var needHighlight = false;
		if(paramNames != null) {
			for(var j = 0; j < paramNames.length; j++) {
				if(paramNames[j] == "needHighlight" && paramValues[j]) {
					needHighlight = true;
					break;
				}
				if(paramNames[j] == "queryParam" && paramValues[j]) {
					var queryParam = paramValues[j];
					if(queryParam.queryLayerParams && queryParam.queryLayerParams.length > 0) {
						for(var k = 0;k < queryParam.queryLayerParams.length; k++) {
							var queryLayerParam = queryParam.queryLayerParams[k];
							var layerName = queryLayerParam.name;
							var index = layerName.indexOf("@");
							if(index != -1) {
								layerName = layerName.substring(0, index);
							}
							if(queryParam.queryLayerParams[k].sqlParam) {
								var whereClause = queryParam.queryLayerParams[k].sqlParam.whereClause;
								if(whereClause) {
									whereClause = whereClause.toLowerCase();
									if(whereClause.indexOf("smid") != -1 && whereClause.indexOf(layerName.toLowerCase()) ==  -1) {
										whereClause = whereClause.replace("smid", layerName + "." + "smid");
										queryParam.queryLayerParams[k].sqlParam.whereClause = whereClause;
									}
								}
							}
						}
					}
				}
			}
		}
        this._hasHighlight = needHighlight;
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}
		onCompleteArray.push(onQueryComplete);
		SuperMap.Committer.commitAjax(this._params.mapName, this.queryUrl, methodName, paramNames, paramValues, false, onCompleteArray, onError, userContext);
    },
    
    // 编辑相关方法
	_editBase:function(mapName, methodName, paramNames, paramValues, onComplete, onError, userContext){
        function onEditComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var editResult = SuperMap.EditResult.fromJson(responseResult);
			if(onComplete){
				onComplete(editResult, userContext);
			}
			if(editResult != null){
				editResult.dispose();
				editResult = null;
			}
		};
		var onCompleteArray = new Array();
		var needHighlight = false;
		if(paramNames != null) {
			for(var j = 0; j < paramNames.length; j++) {
				if(paramNames[j] == "needHighlight" && paramValues[j]) {
					needHighlight = true;
					break;
				}
			}
		}
        this._hasHighlight = needHighlight;
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}
		onCompleteArray.push(onEditComplete);
		var lockID = new Date().getTime().toString() + Math.floor(Math.random( )*1000000000);
		paramNames.push("lockID");
		paramValues.push(lockID);
        SuperMap.Committer.commitAjax(mapName, this.queryUrl, methodName, paramNames, paramValues, false, onCompleteArray, onError, userContext);
    },
	
    _findPathBase:function(methodName, paramNames, paramValues,onComplete,onError,userContext){
        function onFindPathComplete(responseResult, userContext){
			var result = null;
			if(responseResult){
				result = SuperMap.NetworkAnalystResult.fromJson(responseResult);
			}
			if(onComplete){
				if(onComplete instanceof Array) {
					for(var i = 0; i < onComplete.length; i++) {
						var onCompleteFunction = onComplete[i];
						onCompleteFunction(result, userContext);
					}
				} else {
					onComplete(result, userContext);
				}
			}
			
			if(result != null){
				result.dispose();
				result = null;
			}
		};
		
		var onCompleteArray = new Array();
		onCompleteArray.push(onFindPathComplete);
		var needHighlight = false;
		if(paramNames != null && paramValues!=null) {
			for(var j = 0; j < paramNames.length; j++) {
				if(paramNames[j] == "needHighlight" && paramValues[j]) {
					if(paramValues[j]!=null && typeof(paramValues[j]) == "boolean"){
						needHighlight = paramValues[j];
					}
					break;
				}
			}
			
		}
        this._hasHighlight = needHighlight;
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}
        SuperMap.Committer.commitAjax(this._params.mapName, this.queryUrl, methodName, paramNames, paramValues, false, onCompleteArray, onError, userContext);
    },
	
	// 空间分析相关方法
	_bufferQueryBase:function(methodName, paramNames, paramValues, onComplete, onError, userContext){
		function onBufferQueryComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var resultSet = SuperMap.ResultSet.fromJson(responseResult);
			if(onComplete){
				onComplete(resultSet, userContext);
			}
			if(resultSet != null){
				resultSet.dispose();
				resultSet = null;
			}
		};
		var onCompleteArray = new Array();
		var needHighlight = false;
		if(paramNames != null) {
			for(var j = 0; j < paramNames.length; j++) {
				if(paramNames[j] == "needHighlight" && paramValues[j]) {
					needHighlight = true;
					break;
				}
			}
		}
        this._hasHighlight = needHighlight;
		if(needHighlight) {
			var changeTrackingLayerKeyDelegate = Function.createDelegate(this, this._changeTrackingLayerKeyInternal);
			onCompleteArray.push(changeTrackingLayerKeyDelegate);
		}
		onCompleteArray.push(onBufferQueryComplete);
        SuperMap.Committer.commitAjax(this.get_mapName(), this.queryUrl, methodName, paramNames, paramValues, false, onCompleteArray, onError, userContext);
    },
	
	_setLayerStatus:function(layerNames, visibleArgs, queryableArgs, onComplete, onError, customParam, userContext) {
		var onCompleteArray = new Array();
		onCompleteArray.push(onComplete);
		onCompleteArray.push(this._onSetLayerCompleteDelegate);
		// mapName 设置
        SuperMap.Committer.commitAjax(this._mapName, this.queryUrl, "setLayerStatus", ["layerNames", "visibleArgs", "queryableArgs", "customParam"], [layerNames, visibleArgs, queryableArgs, customParam], false, onCompleteArray, onError, userContext);
	}
//========================private methods end========================
};
SuperMap.Map.registerClass('SuperMap.Map', Sys.Component, Sys.IDisposable);