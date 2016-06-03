//------------------------------------------
// 外部的地图服务与iServerJava集成首要的是要解决地图比例尺和坐标转换问题。
// CustomTiledLayer用于演示如何表示外部的地图服务如openlayers中的地图服务以及地图坐标和openlayers中地图服务的。
//------------------------------------------
Type.registerNamespace("SuperMap.UI");

SuperMap.UI.CustomTiledLayer = function(container, params, map, mapObject, resolutions) {
	/// <summary>客户端自定义外部图层类。
	/// </summary>
	/// <param name="container" type="Object">显示客户端呈现图层的图层容器，即DOM对象，如div，img。</param>
	/// <param name="params" type="Object">Object对象，存储了要显示在该客户端呈现图层中的GIS信息，例如WMS（WMSParam）或者SuperMap地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.UI.MapControl">客户端的mapControl对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <param name="mapObject" type="Object">外部图层的mapControl对象。</param>
	/// <param name="resolutions" type="Array">外部图层的分辨率数组。</param>
	/// <returns type="SuperMap.UI.CustomTiledLayer">返回 CustomTiledLayer 对象。</returns>
	this._container = container;
	this._params = params;
	this._map = map;
	this._mapObject = mapObject;
	this._resolutions = resolutions;
	if(!this._resolutions) {
		this._resolutions = [1.40625,0.703125,0.3515625,0.17578125,0.087890625,0.0439453125,0.02197265625,0.010986328125,0.0054931640625,0.00274658203125,0.001373291015625,0.0006866455078125,0.00034332275390625,0.000171661376953125,0.0000858306884765625,0.00004291534423828125];
		// [1.40625,0.703125,0.3515625,0.17578125,0.087890625,0.0439453125,0.02197265625,0.010986328125,0.0054931640625,0.00274658203125,0.001373291015625,0.0006866455078125,0.00034332275390625,0.000171661376953125,0.0000858306884765625,0.00004291534423828125,.00002145767211914062,.00001072883605957031,.00000536441802978515,.00000268220901489257]; // 20 level resolutions
	}
	this._maxZoomLevel = 15; // 19 max zoomlevel
	this._projection = null;
	this._tileLengths = new Array();
	this._inited = false;
};

SuperMap.UI.CustomTiledLayer.prototype = {
	get_contaienr:function(){
	/// <summary>获取或设置客户端呈现图层的容器。
	/// </summary>
	/// <returns type="Object" domElement="true">get_container()返回值类型为DOM对象。</returns>
		return this._container;
	},
	
	getType:function() {
	/// <summary>获取图层类型。</summary>
		return "SuperMap.ForeignImageLayer";
	},
	
	set_resolutions:function(resolutions) {
		this._resolutions = resolutions;
	},
	
	_initializeProjection:function(zoomLevels) {
		// 目前暂时先写固定值，等以后变化了再修改
		var size = 256;
		var tileLat = 170.0;
		var tileLon = 360.0;
		this._tileLengths = new Array();
		for(var d=0; d<zoomLevels; d++){
	        this._tileLengths.push(size);
	        size*=2;
    	}
	},
	
	initialize:function() {
	/// <summary>初始化地图。</summary>
		if(!this._container || this._container == null) {
			this._container = document.getElementById("mapFrame");
		}
		if(!this._mapObject || this._mapObject == null) {
			try {
				if(_GetBrowser() == "ie") {
					this._mapObject = this._container.map;
				} else {
					// IE5.5+, MOZ/N6+
					this._mapObject = this._container.contentWindow.map;
				}
			} catch(e) {
				
			}
			try {
				
				if(this._mapObject) {
					// openlayers 
					// 可能会有错误+-1
					try {
						this._maxZoomLevel = this._mapObject.getNumZoomLevels();
					} catch(e) {
						this._maxZoomLevel = 15;
					}
					this._projection = new SuperMap.MercatorProjection(this._maxZoomLevel);
					this._initializeProjection(this._maxZoomLevel)
				} else {
					this._maxZoomLevel = 15;
					
					this._projection = new SuperMap.MercatorProjection(this._maxZoomLevel);
					this._initializeProjection(this._maxZoomLevel)
				}
			} catch(e) {
				
			}
		}
	},
	
	_resolveZoomLevel:function(mapScale) {
		// 将分辨率解析成对应的放大级别
		var zoom = -1;
		if(mapScale >= this._map._params.mapScales[0]){
			zoom = 0;
		} else if(mapScale < this._map._params.mapScales[this._maxZoomLevel-1]) {
			zoom = this._maxZoomLevel;
		}
		for(var i = 0; i < this._map._params.mapScales.length; i++) {
			if((mapScale * 0.7 <= this._map._params.mapScales[i]) && (mapScale * 1.3 > this._map._params.mapScales[i])) {
				zoom = i;
				break;
			}
		}
		this._zoomLevel = zoom;
		
		return zoom;
	},
	latLngToPixel:function(latLng, zoomLevel){
		/// <summary>将经纬度转换成地图像素坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		if(!zoomLevel) {
	        var mapScale = this._map.get_mapScale();
	        zoomLevel = this._resolveZoomLevel(mapScale)
	    }
	    // (0,0)点坐标
	    var originCenter = new SuperMap.Point2D(0, 0); 
		var originPixel = this._map.pixelToMapCoord(originCenter, this._map.get_mapScale());
	    var mapLength = this._tileLengths[zoomLevel];
	    originPixel.x = originPixel.x - mapLength/2;
	    originPixel.y = originPixel.y - mapLength/2;
		originPixel.x += this._offsetX + this._originX;
		originPixel.y += this._offsetY + this._originY;
	    var position = this._projection.latLngToPixel(point, zoomLevel);
        
		return new SuperMap.Point(position.x + originPixel.x , position.y + originPixel.y);
	},
	
	pixelToLatLng:function(point, zoomLevel){
		/// <summary>将地图像素坐标转换成经纬度坐标。</summary>
		/// <param name="latLng" type="SuperMap.Point2D">经纬度坐标值。</param>
		/// <param name="zoomLevel" type="Number">放大级别，可以不设置。</param>
		if(!zoomLevel) {
	        var mapScale = this._map.get_mapScale();
	        zoomLevel = this._resolveZoomLevel(mapScale)
	    }
	    var originCenter = new SuperMap.Point2D(0, 0); 
		var originPixel = this._map.mapCoordToPixel(originCenter, this._map.get_mapScale());
		originPixel.x -= this._offsetX + this._originX;
		originPixel.y -= this._offsetY + this._originY;
		
		var mapLength = this._tileLengths[zoomLevel];
		
	    originPixel.x = originPixel.x - mapLength/2;
	    originPixel.y = originPixel.y - mapLength/2
	    point.x -= originPixel.x;
	    point.y -= originPixel.y;
		
	    return this._projection.pixelToLatLng(point, zoomLevel);
	},
	render:function(width, height, originX, originY, offsetX, offsetY) {
	/// <summary>将GIS数据图片显示在图片图层中。
	/// </summary>
	/// <param name="width" type="Number">呈现TiledLayer的容器的宽度。</param>
	/// <param name="height" type="Number">呈现TiledLayer的容器的高度。</param>
	/// <param name="originX" type="Number">map对象初始化时原点（左上角）的X坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="originY" type="Number" >map对象初始化时原点（左上角）的Y坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="offsetX" type="Number">待显示的图片原点（左上角）与初始化原点originX的偏移量。</param>
	/// <param name="offsetY" type="Number">待显示的图片原点（左上角）与初始化原点originY的偏移量。</param>
		try {
			if(!this._inited || !this._mapObject) {
				this.initialize();
				this._inited = true;
			}
			if(this._mapObject == null || !this._mapObject) {
				this._container = document.getElementById("mapFrame");
				if(_GetBrowser() == "ie") {
					this._mapObject = this._container.map;
				} else {
					// IE5.5+, MOZ/N6+
					this._mapObject = this._container.contentWindow.map;
				}
			}
			this._originX = originX;
			this._originY = originY;
			this._offsetX = offsetX;
			this._offsetY = offsetY;
			
			var mapScale = this._map.get_mapScale();
			var zoomLevel = this._resolveZoomLevel(mapScale);
			// var resolution = this._resolutions[zoomLevel];
			var curMapCenter = this._map.get_mapParam().center;
			var curPixelCenter = new SuperMap.Point(offsetX + width/2, offsetY + height/2);

			var latlng = this.pixelToLatLng(curPixelCenter, zoomLevel);
			while(latlng.x < -180) {
				latlng.x += 360;
			}
			while(latlng.x > 180) {
				latlng.x -= 360;
			}
			while(latlng.y < -85) {
				latlng.y += 170;
			}
			while(latlng.y > 85) {
				latlng.y -= 170;
			}
			this._mapObject.setCenterAndZoom(latlng.x, latlng.y, zoomLevel);
		} catch(e) {
			// alert(e);
		}
	},
	dispose:function(width, height, originX, originY, offsetX, offsetY){
		/// <summary>释放所占用的资源。
		/// </summary>
		this._map = null;
		this._params = null;
		this._projection = null;
		this._tileLengths = null;
		this._mapObject = null;
		this._resolutions = null;
	}
};
SuperMap.UI.CustomTiledLayer.registerClass("SuperMap.UI.CustomTiledLayer", SuperMap.TiledLayerBase, Sys.IDisposable);