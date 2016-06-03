//------------------------------------------
// �ⲿ�ĵ�ͼ������iServerJava������Ҫ����Ҫ�����ͼ�����ߺ�����ת�����⡣
// CustomTiledLayer������ʾ��α�ʾ�ⲿ�ĵ�ͼ������openlayers�еĵ�ͼ�����Լ���ͼ�����openlayers�е�ͼ����ġ�
//------------------------------------------
Type.registerNamespace("SuperMap.UI");

SuperMap.UI.CustomTiledLayer = function(container, params, map, mapObject, resolutions) {
	/// <summary>�ͻ����Զ����ⲿͼ���ࡣ
	/// </summary>
	/// <param name="container" type="Object">��ʾ�ͻ��˳���ͼ���ͼ����������DOM������div��img��</param>
	/// <param name="params" type="Object">Object���󣬴洢��Ҫ��ʾ�ڸÿͻ��˳���ͼ���е�GIS��Ϣ������WMS��WMSParam������SuperMap��ͼ��Ϣ��mapParam���ȡ�</param>
	/// <param name="map" type="SuperMap.UI.MapControl">�ͻ��˵�mapControl����&lt;br&gt;
	/// �ò�����Ҫ���ڸ��ͻ��˳���ͼ���ṩһ���������ϵ��map����ĵ�ǰ��Χ�ͱ����߼�Ϊ��ͼ��Ķ�Ӧֵ��
	/// �ò���Ҳ�����˿ͻ��˳���ͼ������Χ����Ϣ��</param>
	/// <param name="mapObject" type="Object">�ⲿͼ���mapControl����</param>
	/// <param name="resolutions" type="Array">�ⲿͼ��ķֱ������顣</param>
	/// <returns type="SuperMap.UI.CustomTiledLayer">���� CustomTiledLayer ����</returns>
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
	/// <summary>��ȡ�����ÿͻ��˳���ͼ���������
	/// </summary>
	/// <returns type="Object" domElement="true">get_container()����ֵ����ΪDOM����</returns>
		return this._container;
	},
	
	getType:function() {
	/// <summary>��ȡͼ�����͡�</summary>
		return "SuperMap.ForeignImageLayer";
	},
	
	set_resolutions:function(resolutions) {
		this._resolutions = resolutions;
	},
	
	_initializeProjection:function(zoomLevels) {
		// Ŀǰ��ʱ��д�̶�ֵ�����Ժ�仯�����޸�
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
	/// <summary>��ʼ����ͼ��</summary>
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
					// ���ܻ��д���+-1
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
		// ���ֱ��ʽ����ɶ�Ӧ�ķŴ󼶱�
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
		/// <summary>����γ��ת���ɵ�ͼ�������ꡣ</summary>
		/// <param name="latLng" type="SuperMap.Point2D">��γ������ֵ��</param>
		/// <param name="zoomLevel" type="Number">�Ŵ󼶱𣬿��Բ����á�</param>
		if(!zoomLevel) {
	        var mapScale = this._map.get_mapScale();
	        zoomLevel = this._resolveZoomLevel(mapScale)
	    }
	    // (0,0)������
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
		/// <summary>����ͼ��������ת���ɾ�γ�����ꡣ</summary>
		/// <param name="latLng" type="SuperMap.Point2D">��γ������ֵ��</param>
		/// <param name="zoomLevel" type="Number">�Ŵ󼶱𣬿��Բ����á�</param>
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
	/// <summary>��GIS����ͼƬ��ʾ��ͼƬͼ���С�
	/// </summary>
	/// <param name="width" type="Number">����TiledLayer�������Ŀ�ȡ�</param>
	/// <param name="height" type="Number">����TiledLayer�������ĸ߶ȡ�</param>
	/// <param name="originX" type="Number">map�����ʼ��ʱԭ�㣨���Ͻǣ���X����ֵ��
	/// �ò�����Ҫ���ڸ���ͼ���ṩһ���������ϵ��</param>
	/// <param name="originY" type="Number" >map�����ʼ��ʱԭ�㣨���Ͻǣ���Y����ֵ��
	/// �ò�����Ҫ���ڸ���ͼ���ṩһ���������ϵ��</param>
	/// <param name="offsetX" type="Number">����ʾ��ͼƬԭ�㣨���Ͻǣ����ʼ��ԭ��originX��ƫ������</param>
	/// <param name="offsetY" type="Number">����ʾ��ͼƬԭ�㣨���Ͻǣ����ʼ��ԭ��originY��ƫ������</param>
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
		/// <summary>�ͷ���ռ�õ���Դ��
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