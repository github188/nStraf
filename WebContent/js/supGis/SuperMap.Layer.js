//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。
// 作者：			SuperMap iServer Team
// 修改：
// 文件名：			SuperMap.Layer.js
// 功能：			图层相关类和接口
//========================================================================== 

Type.registerNamespace('SuperMap');

// SuperMap Javascript layer types implementation
SuperMap.LayerBase=function(container, param, map){
	/// <summary>客户端呈现图层的基础类。提供客户端呈现图层基本的属性、方法。
	/// ImageLayerBase、FeatureLayer都会继承LayerBase父类。
	/// </summary>
	/// <param name="container" type="Object">显示客户端呈现图层的图层容器，即DOM对象，如div，img。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该客户端呈现图层中的GIS信息，例如WMS（WMSParam）或者SuperMap地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param> 
	/// <returns type="SuperMap.LayerBase">返回 LayerBase 对象。</returns>
	
	// 图层容器 
	this._container = container;
	// 唯一编号
	this._id = null;
	// 名称
	this._name = null;
	// Map
	this._map = map;
	// 图层的透明属性
	this._transparent = false;
	// 投影坐标系
	this._projection = null;
	// 最大可见地理范围
	this._maxBounds = null;
	// 最小可见比例尺
	this._minScale = null;
	// 最大可见比例尺
	this._maxScale = null;
	// 图层参数设置
	this._param = null;
	this.set_param(param);
	// 超出最大范围后是否继续显示
	this._displayOutsideMaxBounds = false;
	// 不透明度
	this._opacity=100;
	// 是否使用全球数据
	this._global = false;
};

// LayerBase prototype
SuperMap.LayerBase.prototype={
	get_id:function(){
		/// <summary>获取或设置客户端呈现图层的ID标识。</summary>
       	/// <returns type="String">get_id()返回值类型为String。</returns>
		return this._id;
	},
	set_id:function(id){
		this._id = id;
	},
	get_map:function(){
		/// <summary>获取或设置客户端呈现图层对应的map对象。</summary>
    	/// <returns type="SuperMap.Map">get_map()返回值类型为&lt;see cref="T:SuperMap.Map"&gt;Map&lt;/see&gt;。</returns>
		return this._map;
	},
	get_maxScale:function(){
		/// <summary>获取或设置此图层的最大可见比例尺。&lt;br&gt;
		/// 最大可见比例尺不可为负，当地图的当前显示比例尺大于图层最大可见比例尺时，此图层将不显示。
		/// </summary>
    	/// <returns type="Number">get_maxScale()返回值类型为Number。</returns>
		return this._maxScale;
	},
	set_maxScale:function(maxScale){
		this._maxScale = maxScale;
	},
	get_minScale:function(){
		/// <summary>获取或设置此图层的最小可见比例尺。&lt;br&gt;
		/// 最小可见比例尺不可为负。当地图的当前显示比例尺小于图层最小可见比例尺时，此图层将不显示。 
		/// </summary>
    	/// <returns type="Number">get_minScale()返回值类型为Number。</returns>
		return this._minScale;
	},
	set_minScale:function(minScale){
		this._minScale = minScale;
	},
	set_map:function(map){
		if(!this._map){
			this._map = map;
		}
	 },
	get_param:function(){
		/// <summary>获取或设置数据参数。
		/// </summary>
    	/// <returns type="Object">get_param()返回值类型为Object。</returns>
		return this._param;
	},
	// 不同的param需要采用不同处理
	set_param:function(param){
		this._param = param;
	},
	get_opacity:function(){
		/// <summary>获取或设置客户端呈现图层的不透明度。100表示不透明，0表示完全透明。
		/// </summary>
    	/// <returns type="Number" integer="true">get_opacity()返回值类型为Number。</returns>
		return this._opacity;
	},
	set_opacity:function(opacity){
		this._opacity = opacity;
	},
	set_displayOutsideMaxBounds:function(displayOutsideMaxBounds){
		this._displayOutsideMaxBounds = displayOutsideMaxBounds;
	},
	get_displayOutsideMaxBounds:function(){
		/// <summary>超出最大范围后是否继续显示。客户端呈现图层的最大范围由map的最大范围决定。
		/// </summary>
    	/// <returns type="Boolean">get_displayOutsideMaxBounds()返回值类型为Boolean。</returns>
		return this._displayOutsideMaxBounds;
	},
	set_transparent:function(transparent){
		this._transparent = transparent;
	},
	get_transparent:function(){
		/// <summary>获取或设置客户端呈现图层是否透明。
		/// </summary>
    	/// <returns type="Boolean">get_transparent()返回值类型为Boolean。</returns>
		return this._transparent;
	},
	set_container:function(container){
		this._container = container;
	},
	get_container:function(){
		/// <summary>获取或设置客户端呈现图层的容器。
		/// </summary>
    	/// <returns type="Object" domElement="true">get_container()返回值类型为DOM对象。</returns>
		return this._container;
	},
	// 初始化各类对象,param 中存放各类参数
	initialize:function(){
		/// <summary>初始化函数。
		/// </summary>
		// todo here
	},
	dispose:function(){
		/// <summary>释放对象所占用的资源。
		/// </summary>
		this._params = null;
		this._container = null;
		this._map.dispose();
	},
	destroy:function(){
		/// <summary>强制释放对象所占用的资源。
		/// </summary>		
	},
	// 判断图层是否在显示范围和比例尺内
	inRange:function(){
		/// <summary>判断客户端呈现图层的数据是否在显示范围和比例尺内。显示范围和比例尺由map的显示范围和比例尺决定。
		/// </summary>		
		// todo here
		Error.notImplemented();
	},
	// 显示图层
	show:function(){
		/// <summary>显示客户端呈现图层。
		/// </summary>
		if(this._container){
			this._container.style.display = "block";
		}
	},
	// 隐藏图层
	hide:function(){
		/// <summary>隐藏该客户端呈现图层。
		/// </summary>
		if(this._container){
			this._container.style.display = "none";
		}
	},
	// 渲染图层
	render:function(){
		/// <summary>将GIS数据图片显示在客户端呈现图层中。
		/// </summary>
		// todo here
	}
};

SuperMap.LayerBase.registerClass("SuperMap.LayerBase", null, Sys.IDisposable);

//---------------------------------
// SuperMap.ImageLayerBase
// 图片图层对应的Layer基础对象
SuperMap.ImageLayerBase = function(container, param, map){
	/// <summary>图片图层对应的Layer基础类。该类继承LayerBase父类。图片图层主要呈现图片类型的地图数据。
	/// MapLayer、TiledLayerBase都是继承ImageLayerBase父类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息，例如WMS（WMSParam）或者SuperMap地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.ImageLayerBase">返回 ImageLayerBase 对象。</returns>
	
	SuperMap.ImageLayerBase.initializeBase(this);
	this._imageWidth = 256;
	this._imageHeight = 256;
	this._bufferSize = 0;
	this._container = container;
	this._map = map;
	this.set_param(param);
	this._enableImageBuffer = false;
	this._maxImageBufferCount = 128;
	this._imageBuffer = new Array();
};

SuperMap.ImageLayerBase.prototype = {
	getImageUrl:function(width, height, bounds){
		/// <summary>获取图片图层中图片的Url地址。图片的内容是根据图片宽度、高度以及图片需要表现的空间数据的地理范围来确定。
		/// </summary>
		/// <param name="width" type="Number">图片的宽度。</param>
		/// <param name="height" type="Number">图片的高度。</param>
		/// <param name="bounds" type="SueprMap.Rect2D">图片所表现的空间数据的地理范围。SueprMap.Rect2D类型。</param>
    	/// <returns type="String">返回图片的Url地址。</returns>
		return null;
	},
	set_imageHeight:function(height){
		this._imageHeight = height;
	},
	get_imageHeight:function(){
		/// <summary>获取或设置图片图层中图片的高度。
		/// </summary>
    	/// <returns type="Number">get_imageHeight()返回值类型为Number。</returns>
		return this._imageHeight;
	},
	set_imageWidth:function(width){
		this._imageWidth = width;
	},
	get_imageWidth:function(){
		/// <summary>获取或设置图片图层中图片的宽度。
		/// </summary>
    	/// <returns type="Number">get_imageWidth()返回值类型为Number。</returns>
		return this._imageWidth;
	},
	get_bufferSize:function(){
		/// <summary>获取当前缓存的图片对象个数。
		/// </summary>
    	/// <returns type="Number">get_bufferSize()返回值类型为Number。</returns>
		return this._bufferSize;
	},
	set_bufferSize:function(bufferSize){
		this._bufferSize = bufferSize;
	},
	set_enableImageBuffer:function(enabled){
		this._enableImageBuffer = enabled;
	},
	get_enableImageBuffer:function(){
		/// <summary>是否缓存图片对象。&lt;br&gt;
		/// 缓存图片能够提高图层显示速度。如果该值为true，图层呈现图片首先会到缓存图片中查找是否有合适的
		/// 图片，如果有，直接将其显示在图层中，如果没有，在通过服务器获取图片。
		/// </summary>
    	/// <returns type="Boolean">get_enableImageBuffer()返回值类型为Boolean。</returns>
		return this._enableImageBuffer;
	},
	set_maxImageBufferCount:function(maxImageBufferCount){
		this._maxImageBufferCount = maxImageBufferCount;
	},
	get_maxImageBufferCount:function(){
		/// <summary>获取或设置缓存的图片对象的最大个数。&lt;br&gt;
		///		（1）根据客户端的实际情况适当的定制该值，
		/// 数目过大会使客户端承担过重的负担。&lt;br&gt;
		///		（2）该属性只有当enableImageBuffer属性的值为true时，才生效。&lt;br&gt;
		///		（3）存图片能够提高图层显示速度，图层呈现图片首先会到缓存图片中查找是否有合适的
		/// 图片，如果有，直接将其显示在图层中，如果没有，在通过服务器获取图片。从服务器获取的图片也将会放入缓存中，如果
		/// 缓存中图片对象数目大于该属性的值，那么最早的图片对象将被移除，以保证缓存图片对象的个数小于等于该属性值。
		/// </summary>
    	/// <returns type="Number">get_maxImageBufferCount()返回值类型为Number。</returns>
		return this._maxImageBufferCount;
	},
	getBufferedImage:function(id){
		/// <summary>根据缓存图片对象的id获取缓存图片对象。该方法仅当enableImageBuffer属性的值为true时，才生效。
		/// </summary>
		/// <param name="id" type="String">缓存图片对象的id。</param>
    	/// <returns type="SuperMap.UI.MapTile">缓存的&lt;see cref="T:SuperMap.UI.MapTile"&gt;SuperMap.UI.MapTile&lt;/see&gt;对象。</returns>
		if(!this._enableImageBuffer){
			return;
		}
		if(this._imageBuffer && this._imageBuffer.length>0){
			for(var i = 0; i < this._imageBuffer.length; i++) {
				if(this._imageBuffer[i].id == id) {
					return this._imageBuffer[i].image;
				}
			}
		}
	},
	render:function(){
		/// <summary>将GIS数据图片显示在图片图层中。
		/// </summary>

		//  渲染图片
		// todo here
	},
	clearImageBuffer:function(){
		/// <summary>清除客户端缓存图片。
		/// </summary>
		if(this._imageBuffer){
			while(this._imageBuffer.length > 0) {
				var imageObject =  this._imageBuffer.pop();
				if(imageObject && imageObject.image) {
					if(imageObject.image.parentNode) {
						imageObject.image.parentNode.removeChild(imageObject.image);
					}
					imageObject.image.onload = null;
					imageObject.image.onerror = null;
					imageObject.image.onmousedown = null;
					imageObject.image.src = "";
					delete imageObject.image;
				}
				delete imageObject;
				imageObject = null;
			}
		} else {
			this._imageBuffer = new Array(); 
		}
	}
};

SuperMap.ImageLayerBase.registerClass('SuperMap.ImageLayerBase', SuperMap.LayerBase);

//-----------------------------
// MapLayer，支持原先从服务器端直接渲染的图层
// 需要等服务器端确定后再进行调试。
//----------------------------- 
SuperMap.MapLayer=function(container, param, map){
	/// <summary>客户端呈现图层的一种，该图层主要呈现从 iServer 服务器获取的图片对象。&lt;br&gt;
	/// 该类是 ImageLayerBase 的子类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即 DOM 对象，如 img，div。</param>
	/// <param name="param" type="Object">Object 对象，存储了要显示在该图片图层中的 GIS 信息，即 SuperMap 地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的 map 对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map 对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.MapLayer">返回 MapLayer 对象。</returns>
    SuperMap.MapLayer.initializeBase(this);
	this._container = container;
	this._name = null;
	this._map = map;
	this._param = param;
	this._image = null;
	this._imageSrc = null;
	if(param){
		this._name = param.mapName;
		this._maxBounds = param.maxBounds;
		if(param.mapScales){
			this._minScale = param.mapScales[param.mapScales.length - 1];
			this._maxScale = param.mapScales[0];
		}
		if(param.maxScale){
			this._maxScale = param.maxScale;
		}
		if(param.minScale){
			this._minScale = param.minScale;
		}
	}
};

SuperMap.MapLayer.prototype = {
	getImageUrl:function(width, height, bounds){
		/// <summary>获取 MapLayer 图层要呈现的图片的 URL 地址。该图片来做 iServer 服务器。图片的内容是根据图片宽度、高度以及图片需要表现的空间数据的地理范围来确定。
		/// </summary>
		/// <param name="width" type="Number">图片的宽度。</param>
		/// <param name="height" type="Number">图片的高度。</param>
		/// <param name="bounds" type="SueprMap.Rect2D">图片所表现的空间数据的地理范围。SueprMap.Rect2D 类型。</param>
    	/// <returns type="String">返回图片的 URL 地址。</returns>
		var bbox = bounds.leftBottom.x + "," + bounds.leftBottom.y + "," + bounds.rightTop.x + "," + bounds.rightTop.y;
		return this._param.mapHandler + "?method=getimage&imageFormat=png&width=" + width + "&height=" + height + "&bounds=" + bounds.toString() + "&mapscale=" + this._map.get_mapScale();
	},
	render:function(){
		/// <summary>将 GIS 数据图片显示在图片图层中。
		/// </summary>
        var width = this._container.style.width + "";
        width = eval(width.replace("px", "").replace("pt", ""));
        var height = this._container.style.height + "";
        height = eval(height.replace("px", "").replace("pt", ""));

		var imageUrl = null;
		if(this._imageSrc != null){
			imageUrl = this._imageSrc;
		} else if(!this._param.maphandler){
			var viewBounds = this._map.getViewBounds(width, height);
			imageUrl = this.getImageUrl(width, height, viewBounds);
		}
		if(imageUrl && imageUrl.length > 0){
			if(this._image){
				this._container.removeChild(this._image);
			}
			var offset = this._mapControl.getOffset();
			image = new Image(width, height);
			image.src = imageUrl;
			image.style.position = "absolute";
			image.style.left = offset.x + "px";
			image.style.top = offset.y + "px";
			var onerrorFunction = Function.createDelegate(this, this._imageError);
			image.onerror = onerrorFunction;
			this._container.appendChild(image);
			this._image = image;
		}
	},
	_imageError:function(){
		if(this._image){
			this._image.onload = null;
			this._image.onerror = null;
			this._image = null;
		}
	},
	getType:function(){
		/// <summary>获取该对象的图层类型，其值为 SuperMap.MapLayer。
		/// </summary>
    	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.MapLayer";
	}
};

SuperMap.MapLayer.registerClass('SuperMap.MapLayer', SuperMap.ImageLayerBase);

// -----------------------------------------
// SuperMap.ITiledLayer
// -----------------------------------------
SuperMap.ITiledLayer = function(container, params, map){
	/// <summary>该类定义TiledLayer的接口。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息，例如WMS（WMSParam）或者SuperMap地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。&lt;br&gt;
	/// 该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.ITiledLayer">返回 ITiledLayer 对象。</returns>
	throw Error.notImplemented();
};

SuperMap.ITiledLayer.prototype = {
	// 需要具体的对象类实现该方法
	set_tileSize:function(){
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	get_tileSize:function(){
	/// <summary>获取或设置每一个格网的大小。该格网是正方形，其大小是指边长。&lt;br&gt;
	/// TiledLayer主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。
	/// </summary>
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	getTileUrl:function(xIndex,yIndex,mapScale){
	/// <summary>获取单个格网图片的Url地址。&lt;br&gt;
	/// 在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="xIndex" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="yIndex" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	render:function(){
		/// <summary>将GIS数据图片显示在图片图层中。
		/// </summary>
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	clearTiles:function(){
		/// <summary>清空所有格网块对象。
		/// </summary>
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	getBufferImage:function(id){
		/// <summary>根据缓存图片对象的id获取缓存图片对象。该方法仅当enableImageBuffer属性的值为true时，才生效。
		/// </summary>
		/// <param name="id" type="String">缓存图片对象的id。</param>
		throw Error.notImplemented();
	},
	// 需要具体的对象类实现该方法
	getType:function(){
		/// <summary>获取该对象的图层类型。
		/// </summary>
		throw Error.notImplemented();
	}
};
SuperMap.ITiledLayer.registerInterface('SuperMap.ITiledLayer');

//-------------------------------
// SuperMap.TiledLayerBase
//-------------------------------
SuperMap.TiledLayerBase=function(container, param, map){
	/// <summary>格网式图层是客户端呈现图层的一种，格网式图层主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。该类是ImageLayerBase的子类。&lt;br&gt;
	/// 格网式图层又分为WMS格网式图层(TiledWmsLayer)，Map格网式图层(TiledMapLayer)，跟踪层格网式图层（TiledTrackingLayer）。这些格网式图层都是继承TiledLayerBase父类的子类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息，即SuperMap地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.TiledLayerBase">返回 TiledLayerBase 对象。</returns>
	SuperMap.TiledLayerBase.initializeBase(this);
	this._debug = false;
	this._tileSize = 512;
	this._originX = 0;
	this._originY = 0;
	// tile开始位置索引
	this._tileStartX = 0;
	this._tileStartY = 0;
	// tile结束位置索引
	this._tileEndX = 0;
	this._tileEndY = 0;
	this._tileCountX = 0;
	this._tileCountY = 0;
	this._imageBuffer = new Array();
	// 图片最大缓存数目
	if(this._tileSize) {
		var rate = Math.pow(2, parseInt(this._tileSize / 256));
		this._maxImageBufferCount = 512 / rate;
	}
	
	this._preTiles = new Array();
	this._enableImageBuffer = true;
	// 是否允许螺旋加载
	this._enableSpiralLoad = true;
	this._oldCenter = null;
	this._param = param;
	// 保存前一次浏览的tile
	this._oldTiles = new Array();
	this._tiles = new Array();
	// 当前图层和Map之间的像素偏差（<tileSize）
	this._pixelOffset = null;
	// 临时使用对象，后面将使用Map对象的接口方法
	this._map = null;
	// 平移次数
	this._panCount = 0;
    this._checkTileLoadedDelegate = Function.createDelegate(this, this.checkTileLoaded);
    // 在useSimpleCache的时候用下面的5个变量
    this._tileWidth = 0;
    this._maxMapBoundsX = 0;
    this._minMapBoundsY = 0;
	this._minMapBoundsX = 0;
	this._maxMapBoundsY = 0;
};

SuperMap.TiledLayerBase.prototype={
	// 需要扩展类实现
	// 根据Tile的x,y索引和比例尺获取图片
	getTileUrl:function(x, y, mapScale){
	/// <summary>获取单个格网图片的Url地址。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="x" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="y" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
		throw Error.notImplemented();
	},
	set_tileSize:function(tileSize){
		this._tileSize = tileSize;
	},
	get_tileSize:function(){
	/// <summary>获取或设置每一个格网的大小。该格网是正方形，其大小是指边长。&lt;br&gt;
	/// TiledLayer主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。
	/// </summary>
  	/// <returns type="Number">get_tileSize()返回值类型为Number。</returns>
		return this._tileSize;
	},
	set_enableSpiralLoad:function(enableSpiralLoad){
		this._enableSpiralLoad = enableSpiralLoad;
	},
	get_enableSpiralLoad:function(){
	/// <summary>获取或设置是否运行格网图片以螺旋方式下载。true 表示使用螺旋加载的方式加载地图图片。
	/// </summary>
  	/// <returns type="Boolean">get_enableSpiralLoad() 返回值类型为 Boolean。</returns>
		return this._enableSpiralLoad;
	},
	dispose:function(){
	/// <summary>释放所占用的资源。
	/// </summary>
		if(this._tiles){
			while(this._tiles.length > 0){
				var tileRow = this._tiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile._image){
					    if(tile._image.parentNode != null) {
					        tile._image.parentNode.removeChild(tile._image);
					    }
					}
					if(tile._tempImage){
					    if(tile._tempImage.parentNode != null) {
					        tile_tempImage.parentNode.removeChild(tile._tempImage);
					    }
					}
					if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
					tile = null;
				}
				tileRow = null;
			}
			this._tiles = null;
		}
		if(this._oldTiles){
			while(this._oldTiles.length > 0){
				var tileRow = this._oldTiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile._image){
					    if(tile._image.parentNode != null) {
					        tile._image.parentNode.removeChild(tile._image);
					    }
					}
					if(tile._tempImage){
					    if(tile._tempImage.parentNode != null) {
					        ttile._tempImage.parentNode.removeChild(tile._tempImage);
					    }
					}
					if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
					tile = null;
				}
				tileRow = null;
			}
			this._oldTiles = null;
		}
		if(this._preTiles){
			while(this._preTiles.length > 0){
				var tile = this._preTiles.pop();
				if(tile._image){
				    if(tile._image.parentNode != null) {
				        tile._image.parentNode.removeChild(tile._image);
				    }
				}
				if(tile._tempImage){
				    if(tile._tempImage.parentNode != null) {
				        tile._tempImage.parentNode.removeChild(tile._tempImage);
				    }
				}
				if(tile._imageCover){
					    if(tile._imageCover.parentNode != null) {
					        tile._imageCover.parentNode.removeChild(tile._imageCover);
					    }
					}
				tile = null;
			}
			this._preTiles = null;
		}
		this._param = null;
		if(this._container){
		    if(this._container.parentNode != null){
		        this._container.parentNode.removeChild(this._container);
		    }
		}
		this._container = null;
		this._map = null;
	},

	/**
	 * xIndex -- tileXIndex Tile x 方向的索引
	 * yIndex -- tileYIndex Tile y 方向的索引
	 * mapScale -- 地图比例尺
	 * px -- x坐标
	 * py -- y坐标
	 */
	_createTile:function(xIndex, yIndex, mapScale, px, py){
		var tile = new SuperMap.UI.MapTile(this._container);
		tile._tileSize = this._tileSize;
		try{
			var imageId = xIndex + "," + yIndex + "," + mapScale;
			tile._image = this.getBufferedImage(imageId);
			if(!tile._image){
				tile._imageSrc = this.getTileUrl(xIndex, yIndex, mapScale);
				
				if(_GetBrowser() == "ie") {
					tile._image = new Image(this._tileSize,this._tileSize);
					tile._image.id=imageId;
					tile._image.src = tile._imageSrc;
					tile._image.style.zIndex = this._container.style.zIndex - 1 ;
					tile._image.onload = function() {};
				}
				// 修正坐标不能放在此处修改
	            if((px || px == 0) && (py || py == 0)){
					// 参数5代表放大的总步骤数为5
		            tile.initialize(xIndex, yIndex, mapScale, this._map, px, py, 5);
	            } else {
		            tile.initialize(xIndex, yIndex, mapScale, this._map, (xIndex * this._tileSize - this._originX) + this._pixelOffset.x, (yIndex * this._tileSize - this._originY) + this._pixelOffset.y, 5);
	            }
	            tile.id = imageId;
	            if(this._opacity < 100){
		            if(_GetBrowser() == 'ie'){
			            tile._image.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._opacity) + ')';
		            }
	            }
    			
				if(this._enableImageBuffer){
				    if(this._imageBuffer.length > this._maxImageBufferCount){
					    var bufferCount = this._imageBuffer.length;
						var bufferIndex = 0;
						var count = this._maxImageBufferCount / 4;
						var j = 0;
						var nIndex = 0;
						while(j < bufferCount) {
							if(this._imageBuffer[nIndex].index < count) {
								var delObject = this._imageBuffer.splice(nIndex, 1);
								delObject.image.onload = null;
								delObject.image.onerror = null;
								delObject.image.onmousedown = null;
								delete delObject;
								delObject = null;
							} else {
								this._imageBuffer[nIndex].index -= count;
								nIndex++;
							}
							j++;
						}
						this._index -= count;
				    }
    				
				    if(tile._tempImage && this.getBufferedImage(tile.id) == null){
				        if(!this._index){
				            this._index = 0;
				        }
						var imageObject = new Object();
						imageObject.image = tile._tempImage;
						imageObject.id = tile.id;
						imageObject.index = this._index++;
						this._imageBuffer.push(imageObject);
				    } else if(tile._image && this.getBufferedImage(tile.id) == null) {
				        if(!this._index){
				            this._index = 0;
				        }
						var imageObject = new Object();
						imageObject.image = tile._image;
						imageObject.id = tile.id;
						imageObject.index = this._index++;
				        this._imageBuffer.push(imageObject);
				    }
			    }
			}else{
				tile._image.style.visible = true;
       			tile._imageSrc = tile._image.src;
	            // 修正坐标不能放在此处修改
	            if((px || px == 0) && (py || py == 0)){
		            tile.initialize(xIndex, yIndex, mapScale, this._map, px, py, 5);
	            } else {
		            tile.initialize(xIndex, yIndex, mapScale, this._map, (xIndex * this._tileSize - this._originX) + this._pixelOffset.x, (yIndex * this._tileSize - this._originY) + this._pixelOffset.y, 5);
	            }
	            tile.id = imageId;
	            if(this._opacity < 100){
		            if(_GetBrowser() == 'ie'){
			            tile._image.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._opacity) + ')';
		            }
	            }

			}
		
			if(this.getType() == "SuperMap.TiledTrackingLayer") {
			    if(tile._image) {
			        tile._image.style.zIndex = this._container.style.zIndex + 2;
			    }
			    if(tile._tempImage) {
			        tile._tempImage.style.zIndex = this._container.style.zIndex + 2;
			    }
			}
		}catch(e){
			//alert(e);
		}
		
		return tile;
	},
	render:function(width, height, originX, originY, offsetX, offsetY){
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
		if(!this._map){//_initialized
		    this._map = new SuperMap.Map(this._param);
		    this._map.initialize();
		}
		this._width = width;
		this._height = height;
		var tempMapOrigin = this._map.get_referMapBounds().leftBottom;
		var curMapOrigin = this._map.get_referMapBounds().leftBottom;
		var offset = new SuperMap.Point2D();
		offset.x = this._map.get_referMapBounds().leftBottom.x - this._map.get_referMapBounds().leftBottom.x;
		offset.y = this._map.get_referMapBounds().rightTop.y - this._map.get_referMapBounds().rightTop.y;
 
		var pixelDistance = this._map.pixelToMapDistance(1, this._map.get_mapScale());
		var tileWidth = pixelDistance * this._tileSize;

		var tileOffsetX = Math.floor(offsetX/this._tileSize);
		var tileOffsetY = Math.floor(offsetY/this._tileSize);
		offsetX = Math.round(offset.x/pixelDistance);
		offsetY = Math.round(offset.y/pixelDistance);

		this._pixelOffset = new SuperMap.Point();
		this._pixelOffset.x = 0;//offsetX;
		this._pixelOffset.y = 0;//-offsetY;
        
        if(this._map.get_useSimpleCache()){
            this._tileWidth = this._tileSize * this._map.pixelToMapDistance(1, this._map.get_mapScale());
            this._maxMapBoundsX = this._map.get_referMapBounds().rightTop.x;
            this._minMapBoundsY = this._map.get_referMapBounds().leftBottom.y;
		    this._minMapBoundsX = this._map.get_referMapBounds().leftBottom.x;
		    this._maxMapBoundsY = this._map.get_referMapBounds().rightTop.y;
		    if(!this._param.outputContextPath){
		        this._param.outputContextPath = "http://localhost:7080/output/";
		    }
        }
		// 准备Tiles
		this._prepareTiles(tileOffsetX, tileOffsetY, originX, originY, offsetX, offsetY);
	},
	
	_prepareTiles:function(tileOffsetX, tileOffsetY, originX, originY, offsetX, offsetY){
	    if(!this._width){
	        this._width = (this._container.style.width + "").replace("px", "");
	        this._width = this._width.replace("pt", "");
	        this._width = eval(this._width);
	    }
	    if(!this._height){
	        this._height= (this._container.style.height + "").replace("px", "");
	        this._height = this._height.replace("pt", "");
	        this._height = eval(this._height);
	    }

		var mapScale = this._map.get_mapScale();
		var viewBounds = this._map.getViewBounds(this._width, this._height);

		this._originX = originX;
		this._originY = originY;
		this._offsetX = offsetX;
		this._offsetY = offsetY;
		// 客户端的宽度与高度
//		this._width = size.width();
//		this._height = size.height();
		// 偏移后的起始位置
		var startX = this._originX + this._offsetX;
		var startY = this._originY + this._offsetY;
		if(!this._bufferSize && this._bufferSize != 0){
			this._bufferSize = 1;
		}
		if(!this._tileSize){
			// 默认大小为256
			this._tileSize = 256;
		}
		
		// ---------------------
		// 移除不再需要的图片节点
		if(!this._preMapScale) {
			this._preMapScale = this._map.get_referMapScale();
			this._curMapScale = this._map.get_mapScale();
		} else {
			this._preMapScale = this._curMapScale;
			this._curMapScale = this._map.get_mapScale();
		}
		
		var curMapScale = "" + this._map.get_mapScale();
		var preMapScale = "" + this._preMapScale;
		
		if(curMapScale == preMapScale) {
			this._panCount++;
		} else {
			this._panCount = 0;
		}
		if(curMapScale !== preMapScale) {
			var j = 0;
			var length = this._container.childNodes.length;
			while(j < this._container.childNodes.length) {
				var tileId = this._container.childNodes[j].id;
				if(tileId && tileId.indexOf(curMapScale) == -1 && tileId.indexOf(preMapScale) == -1) {
					this._container.childNodes[j].onload = function() {};
					this._container.childNodes[j].onerror = function() {};
					this._container.childNodes[j].onmousedown = function() {};
					this._container.removeChild(this._container.childNodes[j]);
				} else {
					j++;
				}
			}
		}
		// ---------------------
	
		// 如果_tiles为null 或者长度为零则需要重新生成tiles
		// zoomchanged 或者显示范围与前面不存在交集, resize需要重新设置
		if((!this._tiles) || (this._tiles.length == 0)){
			this._tiles = null;
			this._tiles = new Array();
			this._tileStartX = Math.floor(startX / this._tileSize) - this._bufferSize;
			this._tileStartY = Math.floor(startY / this._tileSize) - this._bufferSize;
			this._tileEndX = Math.floor(((startX + this._width) / this._tileSize) + this._bufferSize);
			this._tileEndY = Math.floor(((startY + this._height) / this._tileSize) + this._bufferSize);
			this._tileStartX += tileOffsetX;
			this._tileStartY += tileOffsetY;
			this._tileEndX += tileOffsetX;
			this._tileEndY += tileOffsetY;
			// 重置tileCountX和tileCountY解决tileCount不一致造成图片缺失的情形
			this._tileCountX = this._tileEndX - this._tileStartX + 1;
			this._tileCountY = this._tileEndY - this._tileStartY + 1;
			var mapScale = this._map.get_mapScale();
			//  如果允许螺旋加载操作，则使用螺旋加载方式加载图片
			if(this._enableSpiralLoad){
				var  tempTiles = new Array();
				// 建立临时tiles用于建立螺旋加载的索引序列
				for(var y = this._tileStartY; y <= this._tileEndY; y++){
					var tileRow = new Array();
					for(var x = this._tileStartX; x <= this._tileEndX; x++){
						var tile = new Object();
						tile.x = x;
						tile.y = y;
						tile.queued = false;
						tileRow.push(tile);
					}
					tempTiles.push(tileRow);
					var rows = new Array();
					this._tiles.push(rows);
				}
				// 螺旋加载相关参数
				var tileQueue = new Array();
				var directions = ["right", "down", "left", "up"];
				var iRow = 0;
				var iCell = -1;
				var direction = 0; // default right
				var directionsTried = 0;
				
				while( directionsTried < directions.length) {
						var testRow = iRow;
						var testCell = iCell;
						switch (directions[direction]) {
							case "right":
								testCell++;
								break;
							case "down":
								testRow++;
								break;
							case "left":
								testCell--;
								break;
							case "up":
								testRow--;
								break;
						}
						// 获取对应范围内的tile，并加入到queue队列中
						var tile = null;
						if ((testRow < this._tileCountY) && (testRow >= 0) &&
							(testCell < this._tileCountX) && (testCell >= 0)) {
							tile = tempTiles[testRow][testCell];
						}
						if ((tile != null) && (!tile.queued)) {
							// 加入到队列头部，最后进入队列的先加载
							tileQueue.unshift(tile);
							tile.queued = true;
							//restart the directions counter and take on the new coords
							directionsTried = 0;
							iRow = testRow;
							iCell = testCell;
						} else {
							// 加载其他方向的tile
							direction = (direction + 1) % 4;
							directionsTried++;
						}
					} 
					// 按照顺序进行加载tile
					for(var i=0; i < tileQueue.length; i++) {
						var tempTile = tileQueue[i];
						var tile = this._createTile(tempTile.x,tempTile.y,mapScale);
						this._tiles[tempTile.y-this._tileStartY][tempTile.x-this._tileStartX] = tile;
					}
					tileQueue = null;
					// 清除临时tiles
					while(tempTiles.length > 0){
						var tileRow = tempTiles.pop();
						while(tileRow.length > 0){
							var tempTile = tileRow.pop();
							tempTile = null;
						}
						tileRow = null;
					}
					tempTiles = null;
			} else {
				// 不使用螺旋加载的情况
				for(var y = this._tileStartY; y <= this._tileEndY; y++){
					var tileRow = new Array();
					for(var x = this._tileStartX; x <= this._tileEndX; x++){
						// _createTile
						var tile=this._createTile(x, y, this._map.get_mapScale());
						tileRow.push(tile);
					}
					this._tiles.push(tileRow);
				}
			}
			// 解决centerAndZoom的时候造成的图片叠加问题，清除叠加的图片
			if((curMapScale == preMapScale)) {
				var length = this._container.childNodes.length;
				var j = 0;
				var buffer = Math.round(512 / this._tileSize);
				buffer = 0;
				while(j < this._container.childNodes.length) {
					var tileId = this._container.childNodes[j].id;
					var tileIdParts = tileId.split(",");
					var inrange = true;
					if(tileIdParts != null && tileIdParts.length > 0) {
						var xIndex = parseInt(tileIdParts[0]);
						var yIndex = parseInt(tileIdParts[1]);
						if(xIndex < this._tileStartX - buffer || xIndex > this._tileEndX + buffer || yIndex < this._tileStartY - buffer || yIndex > this._tileEndY + buffer) {
							inrange = false;
						}
					}
					if(!inrange) {
						this._container.childNodes[j].onload = null;
						this._container.childNodes[j].onerror = null;
						this._container.childNodes[j].onmousedown = null;
						this._container.removeChild(this._container.childNodes[j]);
					} else {
						j++;
					}
				}
			}
			
		} else {
			// 清除 oldTiles
			this._clearTiles(this._oldTiles, true);
			this._oldTiles = null;
			
			// minXIndex 
			this._minXIndex = SuperMap.Utility.floor(startX / this._tileSize) - this._bufferSize;
			// minYIndex
			this._minYIndex = SuperMap.Utility.floor(startY / this._tileSize) - this._bufferSize;
			// maxXIndex
			var maxXIndex = SuperMap.Utility.floor((startX + this._width) / this._tileSize) + this._bufferSize;
			// maxYIndex
			var maxYIndex = SuperMap.Utility.floor((startY + this._height) / this._tileSize) + this._bufferSize;
			
			// 不同地图的时候需要重新计算
			this._minXIndex += tileOffsetX;
			maxXIndex += (tileOffsetX + 1);//添加1个偏移量
			this._minYIndex += tileOffsetY;
			maxYIndex += (tileOffsetY + 1);
			this._tileCountX = maxXIndex - this._minXIndex + 1;
			this._tileCountY = maxYIndex - this._minYIndex + 1;
			// 重新计算后的x方向起始索引大于上一次x方向起始索引，那么需要将左边多余的tile移除
			while(this._tileStartX < this._minXIndex){
				// 移除对应列中的起始记录
				for(var y = this._tiles.length - 1; y >= 0; y--){
					var tile = this._tiles[y].shift();
					if(tile){
						this._removeFromMap(tile);
					}
					tile = null;
				}
				this._tileCountX--;
				this._tileStartX++;
			}
			// 重新计算后的x方向起始索引小于上一次x方向起始索引，那么需要补上左边欠缺的tile
			while(this._tileStartX > this._minXIndex){
				// 对应列中的起始位置插入新的记录
				this._tileStartX--;
				this._tileCountX++;
				for(var y = 0; y < this._tiles.length; y++){
					var tile=this._createTile(this._tileStartX, this._tileStartY + y, mapScale);
					this._tiles[y].unshift(tile);
					tile = null;
				}
			}
			// 重新计算后的x方向终止索引小于上一次x方向终止索引，那么需要将右边多余的tile移除
			while(this._tileEndX > maxXIndex){
				// 移除对应列中的尾部记录
				for(var y = 0; y < this._tiles.length; y++){
					var tile = this._tiles[y].pop();
					if(tile){
						this._removeFromMap(tile);
					}
					tile = null;
				}
				this._tileEndX--;
				this._tileCountX--;
			}
			// 重新计算后的x方向终止索引大于上一次x方向终止索引，那么需要将右边欠缺的tile补上
			while(this._tileEndX < maxXIndex){
				// 对应列中的尾部插入新的记录
				this._tileEndX++;
				this._tileCountX++;
				for(var y = 0; y < this._tiles.length; y++){
					var tile = this._createTile(this._tileEndX, this._tileStartY+y, mapScale);
					this._tiles[y].push(tile);
					tile = null;
				}
			}
			// 重新计算后的y方向起始索引大于上一次y方向起始索引，那么需要将上边多余的tile移除
			while(this._tileStartY < this._minYIndex){
				// 在整个队列的起始处，移除对应行的记录
				var tileRow = this._tiles.shift();
				if(tileRow){
					while(tileRow.length > 0){
						var tile = tileRow.pop();
						this._removeFromMap(tile);
						tile = null;
					}
					tileRow = null;
				}
				this._tileStartY++;
				this._tileCountY--;
			}
			// 重新计算后的y方向起始索引小于上一次y方向起始索引，那么需要将上边欠缺的tile补上
			while(this._tileStartY > this._minYIndex){
				// 在整个队列的起始处插入对应行的记录
				this._tileStartY--;
				this._tileCountY++;
				var tileRow = new Array();
				for(var i = 0; i < this._tileCountX; i++){
					var tile = this._createTile(this._tileStartX+i, this._tileStartY, mapScale);
					tileRow.push(tile);
					tile = null;
				}
				this._tiles.unshift(tileRow);
			}
			// 重新计算后的y方向终止索引小于上一次y方向终止索引，那么需要将下边多余的tile移除
			while(this._tileEndY > maxYIndex){
				// 在整个队列的尾部，移除对应行的记录
				var tileRow = this._tiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					if(tile){
						this._removeFromMap(tile);
					}
					tile = null;
				}
				this._tileEndY--;
				this._tileCountY--;
			}
			// 重新计算后的y方向终止索引大于上一次y方向终止索引，那么需要将下边欠缺的tile补上
			while(this._tileEndY < maxYIndex){
				// 在整个队列的尾部，添加对应行的记录
				this._tileEndY++;
				this._tileCountY++;
				var tileRow = new Array();
				for(var x = 0; x < this._tileCountX; x++){
					var tile = this._createTile(this._tileStartX + x, this._tileEndY, mapScale);
					tileRow.push(tile);
					tile = null;
				}
				this._tiles.push(tileRow);
			}
			
			//  清除平移时周围多余的图片
			if((curMapScale == preMapScale) && (this._panCount % 2 == 1)) {
				var length = this._container.childNodes.length;
				var j = 0;
				var buffer = Math.round(512 / this._tileSize);
				while(j < this._container.childNodes.length) {
					var tileId = this._container.childNodes[j].id;
					var tileIdParts = tileId.split(",");
					var inrange = true;
					if(tileIdParts != null && tileIdParts.length > 0) {
						var xIndex = parseInt(tileIdParts[0]);
						var yIndex = parseInt(tileIdParts[1]);
						if(xIndex < this._tileStartX - buffer || xIndex > this._tileEndX + buffer || yIndex < this._tileStartY - buffer || yIndex > this._tileEndY + buffer) {
							inrange = false;
						}
					}
					if(tileId && !inrange && this._container.childNodes[j].complete) {
						this._container.childNodes[j].onload = function() {};
						this._container.childNodes[j].onerror = function() {};
						this._container.childNodes[j].onmousedown = function() {};
						this._container.removeChild(this._container.childNodes[j]);
					} else {
						j++;
					}
				}
			}
		}
		
		// 校验tiles，删除多余的tile，同时补上缺失的tile
		if(this.getType() == "SuperMap.TiledTrackingLayer") {
			for(var i=0; i<=this._tileCountY; i++){
				var tileRow = this._tiles[i];
				if(tileRow){
					while(tileRow.length < this._tileCountX){
						var tile = this._createTile(this._tileStartX+tileRow.length, this._tileStartY+i, mapScale);
						tileRow.push(tile);
					}
					while(tileRow.length > this._tileCountX){
						var tile=tileRow.pop();
						if(tile){
							this._removeFromMap(tile);
							tile = null;
						}
					}
				} else{
					tileRow = new Array();
					for(var j = 0; j <= this._countX; j++){
						var tile=this._createTile(this._tileStartX + j, this._tileStartY + i, mapScale);
						tileRow.push(tile);
					}
					this._tiles.push(tileRow);
				}
			}
		}
	},
	// 在放大或缩小过程中设置图片大小
	setFactor:function(factor){
		/// <summary>设置在缩放过程中本地图片缩放的大小。&lt;br&gt;
		/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。
	/// </summary>
		/// <param name="factor" type="Number">当前图片缩放级别。</param>
		if(this._oldTiles){
			for(var i = 0; i < this._oldTiles.length; i++){
				for(var j = 0; j < this._oldTiles[i].length; j++){
					this._oldTiles[i][j].setFactor(factor);
				}
			}
		}
		if(this._tiles){
			for(var i = 0; i < this._tiles.length; i++){
				for(var j = 0; j < this._tiles[i].length; j++){
					this._tiles[i][j].setFactor(factor);
				}
			}
		}
	},
	// 移除layer时使用destroy
	destroy:function(){
		/// <summary>释放对象占用的资源。</summary>
		if(this._preTiles){
			while(this._preTiles.length > 0){
				var tile = this._preTiles.pop();
				this._removeFromMapTrue(tile);
				tile.dispose();
				tile = null;
			}
		}
		if(this._oldTiles){
			while(this._oldTiles.length > 0){
				var tileRow = this._oldTiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					this._removeFromMapTrue(tile);
					tile.dispose();
					tile = null;
				}
				tileRow = null;
			}
			this._oldTiles.length = null;
		}
		if(this._tiles){
			while(this._tiles.length > 0){
				var tileRow = this._tiles.pop();
				while(tileRow.length > 0){
					var tile = tileRow.pop();
					this._removeFromMapTrue(tile);
					tile.dispose();
					tile = null;
				}
				tileRow = null;
			}
			this._tiles.length = null;
		}
		if(this._enableImageBuffer && this._imageBuffer){
			while(this._imageBuffer.length>0){
				var image = this._imageBuffer.pop();
				if(image.parentNode){
					image.parentNode.removeChild(image);
				}
				image = null;
			}
			this._imageBuffer = null;
		}
		if(this._container && this._container.parentNode){
			this._container.parentNode.removeChild(this._container);
		}
	},
	checkTileLoaded:function(){ 
		if(this._map == null){
			return;
		}
	/// <summary>检查网格块图片是否完成加载。</summary>                                                                            
  	/// <returns type="Boolean">是否完成加载。</returns>
		if(this._tileLoadedChecking){return;}
		this._tileLoadedChecking=true;
		// Tile内部处理加载出错的情形（onerror事件）
		var loaded = true;
		// 加上校验，即超过4秒钟不加载的图片就不再去遍历，减少判断
		var curDate = new Date();
		if(this._tiles) {
			if(this._bufferSize == 0) {
				for(var i = 0; i < this._tiles.length; i++){
					for(var j = 0; j < this._tiles[i].length; j++){
						if(!this._tiles[i][j]._loaded){
							loaded = false;
							break;
						}
					}
				}
			} else {
				for(var i = this._bufferSize; i < this._tiles.length - 2* this._bufferSize; i++){
					for(var j = this._bufferSize; j < this._tiles[i].length - 2* this._bufferSize; j++){
						if(!this._tiles[i][j]._loaded){
							loaded = false;
							break;
						}
					}
				}
			}
		}
		window.status = "";

		if(loaded){
		    if(!this._loadedCheckCount) {
		        this._loadedCheckCount = 0;
		    }
		    this._loadedCheckCount++;
			if(this._loadedCheckCount > 5) {
				this._loadedCheckCount = 0;
			}
			var checkCount = 5;
			if(_GetBrowser() != 'ie') {
				checkCount = 1;
			}
			if(this._loadedCheckCount == checkCount) {
		        this._loadedCheckCount = 0;
			    var curMapScale = "" + this._map.get_mapScale();
			    var preMapScale = "" + this._preMapScale;
			    if(curMapScale !== preMapScale && this._container != null && this._container.childNodes != null) {
				    var j = 0;
				    var length = this._container.childNodes.length;
				    while(j < this._container.childNodes.length) {
					    var tileId = this._container.childNodes[j].id;
					    if(tileId && tileId.indexOf(curMapScale) == -1) {
						    this._container.childNodes[j].onload = function() {};
						    this._container.childNodes[j].onerror = function() {};
							this._container.childNodes[j].onmousedown = function() {};
						    this._container.removeChild(this._container.childNodes[j]);
					    } else {
						    j++;
					    }
				    }
			    }
				if(this._iTimeoutIDForCheckTileLoaded){
					window.clearTimeout(this._iTimeoutIDForCheckTileLoaded);
					this._iTimeoutIDForCheckTileLoaded = null;
				}
			} else {
				this._iTimeoutIDForCheckTileLoaded=window.setTimeout(this._checkTileLoadedDelegate, 100);
			}
		}else{
			this._iTimeoutIDForCheckTileLoaded=window.setTimeout(this._checkTileLoadedDelegate, 100);
		}
		this._tileLoadedChecking=false;
		if(loaded) {
			return true;
		} else {
			return false;
		}
	},
	swapStates:function(){
		/// <summary>新网格图片下载完成后，该方法执行将新网格图片替换原有图片的操作。</summary>
		//放在mapControl中实现对原有 tiles的清理
		for(var i = 0; i < this._tiles.length; i++){
			for(var j = 0; j < this._tiles[i].length; j++){
				this._tiles[i][j]._factorable = true;
				this._tiles[i][j].swapStates();
				this._tiles[i][j].clearSteps();
				this._tiles[i][j].setFactor(0);
				this._tiles[i][j]._factorable = false;
				
				if(this._tiles[i][j]._loaded) {
					this._tiles[i][j]._image.style.zIndex += 2;
				}
			}
		}
		this._iTimeoutIDForCheckTileLoaded = window.setTimeout(this._checkTileLoadedDelegate, 1);
	},
	clearTiles:function(){
		/// <summary>清空所有格网块对象。
		/// </summary>
		this._clearTiles(this._tiles);
	},
	_clearTiles:function(tiles, flag){
		if(tiles){
			while(tiles.length > 0){
					var tileRow = tiles.pop();
					while(tileRow.length > 0){
						var tile = tileRow.pop();
						if(tile){
						    if(flag) {
							    this._removeFromMapTrue(tile);
							} else {
							    this._removeFromMap(tile);
							}
						}
					}
					tileRow = null;
			}
			tiles = null;
		}
		if(this.getType() == "SuperMap.TiledTrackingLayer") {
		   	while(this._container.childNodes.length > 0) {
                this._container.childNodes[0].onload = function() {};
				this._container.childNodes[0].onerror = function() {};
				this._container.removeChild(this._container.childNodes[0]);
			}
		}
	},
	getBufferedImage:function(id){
		/// <summary>根据缓存图片对象的id获取缓存图片对象。该方法仅当enableImageBuffer属性的值为true时，才生效。
		/// </summary>
		/// <param name="id" type="String">缓存图片对象的id。</param>
    	/// <returns type="SuperMap.UI.MapTile">缓存的&lt;see cref="T:SuperMap.UI.MapTile"&gt;SuperMap.UI.MapTile&lt;/see&gt;对象。</returns>
		if(this._enableImageBuffer){
			for(var i = 0; this._imageBuffer && i < this._imageBuffer.length; i++) {
				if(this._imageBuffer[i].id == id) {
					return this._imageBuffer[i].image;
				}
			}
		}
		return null;
	},
	// 准备底图
	prepareBaseTile:function(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom){
		/// <summary>根据放大/缩小的比率将原图层图片进行缩放。该操作是在新图片下载完成前执行的。
		/// </summary>
		/// <param name="oldOriginX" type="Number">缩放前图片原点（左上角）的X坐标。</param>
		/// <param name="oldOriginY" type="Number">缩放前图片原点（左上角）的Y坐标。</param>
		/// <param name="oldZoom" type="Number">缩放前地图比例尺。</param>
		/// <param name="newOriginX" type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
		/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
		/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
		if(this._oldTiles){
			var pixelCenter = this._map.get_mapParam().getPixelCenter(this._map.get_mapScale());
			pixelCenter.x = pixelCenter.x - this._offsetX - oldOriginX;
			pixelCenter.y = pixelCenter.y - this._offsetY - oldOriginY;
			
			for(var i = 0; i < this._oldTiles.length; i++){
				for(var j = 0; j < this._oldTiles[i].length; j++){
					var tile = this._oldTiles[i][j];
					tile._setCurrentState(tile._curPositionX, tile._curPositionY, tile._curWidth, tile._curHeight);
					var ratio = newZoom / oldZoom;
					tile._nextPositionX = Math.floor(pixelCenter.x + (tile._curPositionX -pixelCenter.x) * ratio);
					tile._nextPositionY = Math.floor(pixelCenter.y + (tile._curPositionY - pixelCenter.y) * ratio);
					tile._nextWidth = SuperMap.Utility.ceil(tile._curWidth * ratio);
					tile._nextHeight = SuperMap.Utility.ceil(tile._curHeight * ratio);
					tile._factorable = true;
					tile._precomputeSteps();
					tile._zIndex = this._container.style.zIndex + 1;
					if(tile._image != null){
						tile._image.style.zIndex = tile._zIndex;
					}
				}
			}
		}
	},
	// 准备替换后的图片
	prepareSwapTile:function(oldOriginX, oldOriginY, oldZoom, newOriginX, newOriginY, newZoom){
		/// <summary>根据缩放前后的比例排列各个格网图片的位置。
		/// </summary>
		/// <param name="oldOriginX" type="Number">缩放前图片原点（左上角）的X坐标。</param>
		/// <param name="oldOriginY" type="Number">缩放前图片原点（左上角）的Y坐标。</param>
		/// <param name="oldZoom" type="Number">缩放前地图比例尺。</param>
		/// <param name="newOriginX"  type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
		/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
		/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
		if(this._tiles){
			var ratio = newZoom / oldZoom;
			for(var i = 0; i < this._tiles.length; i++){
				for(var j = 0; j < this._tiles[i].length; j++){
					var tile = this._tiles[i][j];
					tile._nextPositionX = SuperMap.Utility.floor((oldOriginX + tile._curPositionX) * ratio - newOriginX);
					tile._nextPositionY = SuperMap.Utility.floor((oldOriginY + tile._curPositionY) * ratio - newOriginY);
					tile._nextWidth = SuperMap.Utility.ceil(tile._curWidth * ratio);
					tile._nextHeight = SuperMap.Utility.ceil(tile._curHeight * ratio);
					var factorX = SuperMap.Utility.ceil(this._tileCountX * 0.25);
					var factorY = SuperMap.Utility.ceil(this._tileCountY * 0.25);
					tile._factorable = (newZoom > oldZoom) &&
							(tile._indexX < this._tileStartX + factorX || tile._indexX > this._tileEndX - factorX ||
							tile._indexY < this._tileStartY + factorY || tile._indexY > this._tileEndY - factorY);
					tile._factorable = false;
					tile.swapStates();
					tile._precomputeSteps();
					tile._zIndex = this._container.style.zIndex;
				}
			}
		}
	},
	backup:function(){
		/// <summary>备份当前的格网对象。
		/// </summary>
		this._clearTiles(this._oldTiles);
		this._oldTiles = this._tiles;
		this._tiles = new Array();
	},
	getType:function(){
		/// <summary>获取该对象的图层类型。
		/// </summary>
		throw Error.notImplemented();
	},
	_removeFromMap:function(tile){
		if(!this._enableImageBuffer){return this._removeFromMapTrue(tile);}
		var rate = Math.pow(2, parseInt(this._tileSize / 256));
		var maxPreTileslength = 64 / rate; 
		while(this._preTiles.length > maxPreTileslength){
			var deletingTiles = this._preTiles.splice(0, maxPreTileslength/2);
			while(deletingTiles.length > 0){
				var deletingTile = deletingTiles.pop();
				deletingTile.dispose();
				delete deletingTile;
				deletingTile = null;
			}
			deletingTiles = null;
		}
		if(!tile._image){
			this._removeFromMapTrue(tile); 
			return;
		}
		tile._image.style.zIndex = tile._container.style.zIndex - 1;
		if(tile._imageCover && tile._imageCover.parentNode) {
			tile._imageCover.parentNode.removeChild(tile._imageCover);
			tile._imageCover.id = null;
			tile._imageCover.innerHTML = null;
			tile._imageCover.onmousedown = null;
			delete tile._imageCover;
			tile._imageCover = null;
		}
		this._preTiles.push(tile);
		if(tile._debug){
			window.status = "preTiles:" + this._preTiles.length;
		}
		tile.unused = true;

		return;
	},
	// 完全从图层中移除
	_removeFromMapTrue:function(tile){
		for(var i = 0; i < this._preTiles.length; i++){
			if(this._preTiles[i].id == this.id){
				var preTile = this._preTiles.splice(i, 1);
				if(preTile._image) {
					preTile._image.src = "";
				}
				if(preTile._tempImage) {
					preTile._tempImage.src = ""
				}
				delete preTile;
				preTile = null;
				break;
			}
		}
		if(tile._tempImage){
			tile._tempImage.onload = null;
			tile._tempImage.onerror = null;
			tile._tempImage.onmousedown = null;
			tile._tempImage.src = "";
			delete tile._tempImage;
			tile._tempImage = null;
		}
		if(tile._image){
			if(tile._image.parentNode == this.get_container()){
			    tile._image.onload = null;
			    tile._image.onerror = null;
				tile._image.onmousedown = null;
				tile._image.src = "";
				
				this.get_container().removeChild(tile._image);
				delete tile._image;
			}
			// delete tile._image;
			tile._image = null;
		}
		if(this.getBufferedImage(tile.id) != null) {
			for(var i = 0; i < this._imageBuffer.length; i++) {
				if(this._imageBuffer[i].id == tile.id) {
					var imageObject = this._imageBuffer.splice(i, 1);
					if(imageObject && imageObject.image) {
						imageObject.image.src = "";
						imageObject.image.onload = null;
						imageObject.image.onerror = null;
						imageObject.image.onmousedown = null;
						if(imageObject.image.parentNode) {
							imageObject.image.parentNode.removeChild(imageObject.image);
						}
						delete  imageObject.image;
						imageObject.image = null;
					}
					delete imageObject;
					imageObject = null;
					break;
				}
			}
		}
		if(tile._imageCover && tile._imageCover.parentNode) {
			tile._imageCover.parentNode.removeChild(tile._imageCover);
			tile._imageCover.id = null;
			tile._imageCover.innerHTML = null;
			tile._imageCover.onmousedown = null;
			delete tile._imageCover;
			tile._imageCover = null;
		}
		tile.dispose();
		delete tile;
		tile = null;
	}
};

SuperMap.TiledLayerBase.registerClass('SuperMap.TiledLayerBase',SuperMap.ImageLayerBase, SuperMap.ITiledLayer, Sys.IDisposable);

//-----------------
// TiledMapLayer类
// 实现了对图片分块加载功能，和以前的Ajax Map大致相同
//-----------------
SuperMap.TiledMapLayer=function(container, param, map){
	/// <summary>Map 格网式图层，该类是 TiledLayerBase 的子类，属于格网式图层的一种。&lt;br&gt;
	/// 格网式图层是客户端呈现图层的一种，格网式图层主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。&lt;br&gt;
	/// TileMapLayer主要将从 iServer 服务器获取的图片切割为若干格网图片进行呈现。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即 DOM 对象，如 img，div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的 GIS 信息，即 SuperMap 地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的 map 对象。该参数主要用于给客户端呈现图层提供一个坐标参照系，map 对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.TiledMapLayer">返回 TiledMapLayer 对象。</returns>
	SuperMap.TiledMapLayer.initializeBase(this);
	this._container = container;
	this._param = param;
	this._debug = false;
	this._map = map;
};

// prototype of SuperMap.TiledMapLayer
SuperMap.TiledMapLayer.prototype={
	getTileUrl:function(x, y, mapScale){
	/// <summary>获取单个格网图片的 URL 地址。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="x" type="Number">格网图片所在的 x（横向）索引值。</param>
	/// <param name="y" type="Number">格网图片所在的 y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="String">单个格网图片的 URL 地址。</returns>
	    var url = "";
	    if(this._map.get_useSimpleCache()){
			if(x < 0 || y < 0 || x * this._tileWidth + this._minMapBoundsX > this._maxMapBoundsX || this._maxMapBoundsY - y * this._tileWidth < this._minMapBoundsY){
	            url = this._param.outputContextPath + "/cache/blank.png";
	        }else{
                url = this._param.outputContextPath + "/cache/" + this._param.mapName + "_" + this._tileSize + "x" + this._tileSize + "/" + Math.round(1 / this._map.get_mapScale()) + "/" + x + "/" + y + ".png";
	        }
		}else{
			var compression = "";
			if (this._param.compression > 0 && this._param.compression <100){
				compression = "&compression="+this._param.compression
			}
		    url = this._param.mapHandler + "?mapName=" + encodeURI(this._param.mapName) + "&x=" + x + "&y=" + y + "&imageFormat="+this._param.imageFormat+compression+"&width=" + this._tileSize + "&height=" + this._tileSize + "&mapScale=" + mapScale + "&layersKey=" + this._map._params.layersKey + "&method=gettiledimage&t=" + this._map._t;
			// use global map
		    if(this._global) {
			    var bounds = this._map.getBounds();
			    var ratio = bounds.height() / bounds.width();
			    url += "&global=1&ratio=" + ratio;
		    }
	    }
		if(this._map.isEditing) {
			url += "&isEdit=1";
		}
		
		return url;
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.TiledMapLayer";
	}
};

SuperMap.TiledMapLayer.registerClass('SuperMap.TiledMapLayer', SuperMap.TiledLayerBase, Sys.IDisposable);

SuperMap.TiledTrackingLayer = function(container, param, map){
	/// <summary>跟踪层格网式图层，该类是 TiledLayerBase 的子类，属于格网式图层的一种。&lt;br&gt;
	/// 格网式图层是客户端呈现图层的一种，格网式图层主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。&lt;br&gt;
	/// TiledTrackingLayer 主要将从 iServer 服务器获取的 SuperMap 的跟踪层图片切割为若干格网图片进行呈现。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即 DOM 对象，如img，div。</param>
	/// <param name="param" type="Object">Object 对象，存储了要显示在该图片图层中的 GIS 信息，即 SuperMap 地图信息（mapParam）等。</param>
	/// <param name="map" type="SuperMap.Map">客户端的 map 对象。该参数主要用于给客户端呈现图层提供一个坐标参照系，map 对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.TiledTrackingLayer">返回 TiledTrackingLayer 对象。</returns>
	SuperMap.TiledTrackingLayer.initializeBase(this);
	this._container = container;
	this._param = param;
	this._debug = false;
	this._trackingLayerCached = param._trackingLayerCached;
	this._map = map;
	// 是否分块出高亮图 
	this._isTrackingLayerInTileMode = false;
	this._clearOldTiledTrackingLayerDelegate = Function.createDelegate(this, this.clearOldTiledTrackingLayer);
};

// prototype of SuperMap.TiledMapLayer
SuperMap.TiledTrackingLayer.prototype={
	clearOldTiledTrackingLayer:function(){
		/// <summary>清除先前的一个高亮图。
		/// </summary>
		while(this._container.childNodes.length > 1) {
			this._container.childNodes[0].onload= null;
			this._container.childNodes[0].onerror= null;
			this._container.removeChild(this._container.childNodes[0]);
		}
	},
	clearTiles:function(){
		/// <summary>清空所有格网块对象。
		/// </summary>
		this._clearTiles(this._tiles, true);
		this.clearImageBuffer();
	},
	_clearTiles:function(tiles, flag){
		if(tiles){
			while(tiles.length > 0){
					var tileRow = tiles.pop();
					while(tileRow.length > 0){
						var tile = tileRow.pop();
						if(tile){
							if(flag) {
							    this._removeFromMapTrue(tile);
							} else {
							    this._removeFromMap(tile);
							}
						}
						delete tile;
					}
					tileRow = null;
			}
			tiles = null;
		}
	},
	getTileUrl:function(x, y, mapScale){
	/// <summary>获取单个格网图片的Url地址。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="x" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="y" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="String">单个格网图片的Url地址。</returns>
	    var returnString = this._param.mapHandler + "?mapName=" + encodeURI(this._param.mapName) + "&x=" + x + "&y=" + y + "&imageFormat=gif&width=" + this._tileSize + "&height=" + this._tileSize + "&mapScale=" + mapScale + "&layersKey=" + this._map._params.layersKey + "&method=gettiledtrackinglayerimage&t=" + new Date().getTime();
        return  returnString; 
	},

	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.TiledTrackingLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.TiledTrackingLayer";
	},
	
	render:function(width, height, originX, originY, offsetX, offsetY) {
	/// <summary>将 GIS 数据图片显示在图片图层中。
	/// </summary>
	/// <param name="width" type="Number">呈现 TiledLayer 的容器的宽度。</param>
	/// <param name="height" type="Number">呈现TiledLayer 的容器的高度。</param>
	/// <param name="originX" type="Number">map 对象初始化时原点（左上角）的 X 坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="originY" type="Number" >map 对象初始化时原点（左上角）的 Y 坐标值。
	/// 该参数主要用于给该图层提供一个坐标参照系。</param>
	/// <param name="offsetX" type="Number">待显示的图片原点（左上角）与初始化原点 originX 的偏移量。</param>
	/// <param name="offsetY" type="Number">待显示的图片原点（左上角）与初始化原点 originY 的偏移量。</param>
		// var image = new Image();

		if(!this._isTrackingLayerInTileMode) {
			// 只出一张高亮图 
			var viewBounds = this._map.getViewBounds(width, height);
			var minx = viewBounds.leftBottom.x;
			var miny = viewBounds.leftBottom.y;
			var maxx = viewBounds.rightTop.x;
			var maxy = viewBounds.rightTop.y;
			
			var url = this._param.mapHandler + "?mapName=" + encodeURI(this._param.mapName) + "&imageFormat=gif&width=" + width + "&minx=" + minx + "&maxx="+maxx+"&miny="+miny+"&maxy="+maxy+"&height=" + height + "&mapScale=" + 1.0 + "&layersKey=" + this._map._params.layersKey + "&method=gettiledtrackinglayerimagebybounds&t=" + new Date().getTime();
			while(this._container.childNodes.length > 0) {
				this._container.childNodes[0].onload= null;
				this._container.childNodes[0].onerror= null;
				this._container.removeChild(this._container.childNodes[0]);
			}
			
			var image = new Image();
			image.src = url;
			image.style.zIndex = this._container.style.zIndex + 10;
			image.style.position = "absolute";
			image.style.display = "";
			// image.style.left = originX + offsetX;
			// image.style.top = originY + offsetY;
			
			 image.style.left = offsetX;
			 image.style.top = offsetY;
			 //去除再次添加高亮后的闪烁效果
			 //image.onload = this._clearOldTiledTrackingLayerDelegate;
			
			this._container.appendChild(image);
		} else {
			// 分块出高亮图 
			if(!this._map){//_initialized
				this._map = new SuperMap.Map(this._param);
				this._map.initialize();
			}
			this._width = width;
			this._height = height;
			var tempMapOrigin = this._map.get_referMapBounds().leftBottom;
			var curMapOrigin = this._map.get_referMapBounds().leftBottom;
			var offset = new SuperMap.Point2D();
			offset.x = this._map.get_referMapBounds().leftBottom.x - this._map.get_referMapBounds().leftBottom.x;
			offset.y = this._map.get_referMapBounds().rightTop.y - this._map.get_referMapBounds().rightTop.y;
	 
			var pixelDistance = this._map.pixelToMapDistance(1, this._map.get_mapScale());
			var tileWidth = pixelDistance * this._tileSize;

			var tileOffsetX = Math.floor(offsetX/this._tileSize);
			var tileOffsetY = Math.floor(offsetY/this._tileSize);
			offsetX = Math.round(offset.x/pixelDistance);
			offsetY = Math.round(offset.y/pixelDistance);

			this._pixelOffset = new SuperMap.Point();
			this._pixelOffset.x = 0;//offsetX;
			this._pixelOffset.y = 0;//-offsetY;
			
			if(this._map.get_useSimpleCache()){
				this._tileWidth = this._tileSize * this._map.pixelToMapDistance(1, this._map.get_mapScale());
				this._maxMapBoundsX = this._map.get_referMapBounds().rightTop.x;
				this._minMapBoundsY = this._map.get_referMapBounds().leftBottom.y;
				this._minMapBoundsX = this._map.get_referMapBounds().leftBottom.x;
				this._maxMapBoundsY = this._map.get_referMapBounds().rightTop.y;
				if(!this._param.outputContextPath){
					this._param.outputContextPath = "http://localhost:7080/output/";
				}
			}
			// 准备Tiles
			this._prepareTiles(tileOffsetX, tileOffsetY, originX, originY, offsetX, offsetY);
		}
	}
};

SuperMap.TiledTrackingLayer.registerClass('SuperMap.TiledTrackingLayer', SuperMap.TiledLayerBase, Sys.IDisposable);

//--------------------------------------------------
// 需要计算偏差
SuperMap.LeveledTileLayerBase=function(container, param, map){
	/// <summary>等级比例尺格网式图层，该类是TiledLayerBase的子类，属于格网式图层的一种。&lt;br&gt;
	/// 该图层的数据来源有共同的特点：数据是按照固定的比例尺进行显示的。同时也是将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息。</param>
	/// <param name="map" type="SuperMap.Map">客户端的map对象。该参数主要用于给客户端呈现图层提供一个坐标参照系，map对象的当前范围和比例尺即为该图层的对应值。&lt;br&gt;
	/// 该参数也决定了客户端呈现图层的最大范围等信息。</param>
	/// <returns type="SuperMap.LeveledTileLayerBase">返回 LeveledTileLayerBase 对象。</returns>
	SuperMap.LeveledTileLayerBase.initializeBase(this);	
	// 最大级别
	this._ratio = 1;
	this._maxZoomLevel = 0;
	this._mapScales = new Array();
	this._param = null;
	this._maxBounds = new SuperMap.Rect(-180, -90, 180, 90);
	if(param){
		this.set_param(param);
		if(param.mapScales){
			this._zoomLevel = param.zoomLevel;
			this._maxZoomLevel = param.mapScales.length+1;
		}else{
			//alert("mapScales property in parameter 'param' is not properly set.");
		}
		if(param.maxBounds){
			this._maxBounds = param.maxBounds;
		}
	}
	this._zoomLevel = 0;
	this._minZoomLevel = 0;
	this._origin = null;
	this._startPosition = null;
	this._bufferSize = 1;
	this._enableImageBuffer = false;
	this._resolutions = null;
	this._map = map;
};

SuperMap.LeveledTileLayerBase.prototype={
	getMaxZoomLevel:function(){
	/// <summary>获取最大的地图显示比例尺级别。&lt;br&gt;
	/// 在LeveledTileLayerBase图层中显示的数据都是按照固定的比例尺显示的，
	/// 将每一个比例尺设为一级。这个属性表示最大的级别数。比如某
	/// 显示在LeveledTileLayerBase的数据最高的比例尺级别为6级，那么该属性值为6。
	/// </summary>
   	/// <returns type="Number">最大的地图显示比例尺级别。</returns>
		return this._maxZoomLevel;
	},
	getMinZoomLevel:function(){
	/// <summary>获取最小的地图显示比例尺级别。&lt;br&gt;
	/// 在LeveledTileLayerBase图层中显示的数据都是按照固定的比例尺显示的，
	/// 将每一个比例尺设为一级。这个属性表示显示在LeveledTileLayerBase的数据能够显示的最小的级别数。比如某
	/// 显示在LeveledTileLayerBase的数据最小的比例尺级别为2级，那么该属性值为2。
	/// </summary>
   	/// <returns type="Number">最小的地图显示比例尺级别。</returns>
		return this._minZoomLevel;
	},
	getMaxScale:function(){
	/// <summary>获取最大的地图显示比例尺。
	/// </summary>
   	/// <returns type="Number">最大的地图显示比例尺。</returns>
		// error handle
		return this._param.mapScales[this._maxZoomLevel-1];	
	},
	getMinScale:function(){
	/// <summary>获取最小的地图显示比例尺。
	/// </summary>
   	/// <returns type="Number">最小的地图显示比例尺。</returns>
		return this._param.mapScales[this._mapScales.length - 1];	
	},
	// 获取最为接近的比例尺
	getMapScale:function(){
	/// <summary>获取最为接近的比例尺。
	/// </summary>
   	/// <returns type="Number">最为接近的比例尺。</returns>
		// 假定MapScales和ZoomLevels 顺序都是从小到大排列
		var mapScale = this._map.get_mapScale();
		var index = 0;
		var delta = 100;
		var resolution = 1/mapScale;
		for(var i=1; i < this._param.mapScales.length; i++){
			if(Math.abs(resolution-1/this._param.mapScales[i-1]) < Math.abs(1/this._param.mapScales[i]-resolution)){
				index = i-1;
			} else {
				index = i;
			}
			if(Math.abs(resolution-1/this._param.mapScales[i-1]) < resolution){
				break;
			}
		}
		//? 是否需要判断rate，即超出比率多大以后就不再显示				
		return this._param.mapScales[index];
		
	},
	set_param:function(param){
		if(param!=null && typeof(param)!='undefined'){
			// 
			this._param = param;
			this._maxBounds = this._param.maxBounds;   // maxBounds
			this._maxScale = this._param.maxScale;     // maxScale
			this._minScale = this._param.minScale;     // minScale
			this._projection = this._param.projection;
		}
	},
	getZoomLevel:function(){
	/// <summary>获取当前比例尺级别。
	/// </summary>
   	/// <returns type="Number">当前比例尺级别。</returns>
		return this._zoomLevel;
	},
	render:function(){
	/// <summary>将GIS数据图片显示在图层中。
	/// </summary>
		// 计算左右上下的交界出的tileIndex
		// 获取对应的图片
		// 决定图片的范围left, top, right, bottom
		// 计算每个tile所占距离 pixelDistance * tileSize;
		this._ratio =  this._map.get_mapScale() / this.getMapScale(); 
		if(!this._resolutions){
			this._resolutions = new Array();
			var map = this._map;
			for(var i=0; i < this._param.mapScales.length; i++){
				var resolution = map.pixelToMapDistance(1, this._param.mapScales[i]);
				resolution /= this._ratio; 
				this._resolutions[i] = resolution;
			}
		}
		var tileDistanceX = this._resolutions[this._zoomLevel] * this._tileSize;
		var tileDistanceY = this._resolutions[this._zoomLevel] * this._tileSize;
		this._leftTop = new SuperMap.Point2D();// 左上角，起点

		var mapOrigin = this._mapControl.getOrigin();
		var offset = this._mapControl.getOffset();

		mapOrigin.x += offset.x;
		mapOrigin.y += offset.y;

		var size = this._mapControl.getSize();

		var x = Math.round(size.width() / this._tileSize);
		var y = Math.round(size.height() / this._tileSize);
		this._leftTop = this._mapControl.pixelToMapCoord(mapOrigin);
		var endPoint = new SuperMap.Point();
		endPoint.x = mapOrigin.x + size.width();
		endPoint.y = mapOrigin.y + size.height();
		this._rightBottom = new SuperMap.Point2D(); // 右下角，终点
		this._rightBottom = this._mapControl.pixelToMapCoord(endPoint);
		var extent = this._maxBounds;
		extent = this._mapControl.getMap().get_referMapBounds();
		this._tileIndexX = Math.round((this._leftTop.x - extent.leftBottom.x) / tileDistanceX) - this._bufferSize;
		this._tileIndexY = Math.round((this._leftTop.y - extent.rightTop.y) / tileDistanceY - 0.25) + this._bufferSize;
		this._tileEndX = Math.round((this._rightBottom.x - extent.leftBottom.x) / tileDistanceX - 0.25)- this._bufferSize;
		this._tileEndY = Math.round((this._rightBottom.y - extent.rightTop.y) / tileDistanceY - 0.25)+ this._bufferSize;

		var width = this._tileSize * ratio;
		var height = width;
		var startPoint2D = new SuperMap.Point2D();
		startPoint2D.x = extent.leftBottom.x + (this._tileIndexX - 1) * tileDistanceX;
		startPoint2D.y = extent.rightTop.y + (this._tileIndexY - 1) * tileDistanceY;
		// mapControl转换已经计算偏移量
		var startPoint = this._map.mapCoordToPixel(startPoint2D, this._map.get_mapScale());
		if(this._tiles){
			this._clearTiles(this._tiles);
		}
		this._tiles = new Array();
		for(var i = this._tileIndexX;i < this._tileEndX; i++){
			var tileRow = new Array()
			for(var j = this._tileIndexY; j < this._tileEndY; j++){
				var px = startPoint.x + (i - this._tileIndexX) * width;
				var py = startPoint.y + (j - this._tileIndexY) * height;
				var tile = this._createTile(i, j, this._zoomLevel, true, px, py, width, height);
				tileRow.push(tile);
			}
			this._tiles.push(tileRow);
		}
	},
	_createTile:function(xIndex, yIndex, zoomLevel, requireImage, pX, pY){
		var tile = new SuperMap.MapTile(this._container);
		tile._tileSize = this._tileSize;
		// todo:需要实现超出范围后显示透明图片或者不设置图片
		var imageId = xIndex + "," + yIndex + mapScale;
		var referBounds = this._map.get_referMapBounds();
		
		tile._image = this.getBufferedImage(imageId);
		if(tile._image){
    		tile._imageSrc = tile._image.src;
		}else{
    		tile._imageSrc = this.getTileUrl(xIndex, yIndex, mapScale);
		}

		tile._tileSize = this._tileSize * ratio;//  支持长方形图片tile时需要设置height
		// maxZoomSteps
		// 暂时设置为5
		tile.initialize(xIndex, yIndex, zoomLevel, this._map, pX, pY, 5);
		tile.id = imageId;
		// 在图片的onload里面进行客户端缓存处理
		if(this._opacity <100){
			if(_GetBrowser() == 'ie'){
				tile._tempImage.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._opacity) + ')';
			}
		}
		if(this._enableImageBuffer){
			if(this._imageBuffer.length>this._imageBufferSize){
				for(var i=0;i<this._imageBufferSize/3;i++){
					var image = this._imageBuffer[this._imageBuffer[0]]
					if(image){
						this._container.removeChild(image);
						image.onload = null;
						image.onerror = null;
						delete image;
					}
					image = null;
					this._imageBuffer.shift();
				}
			}
			this._imageBuffer[tile.id] = tile._tempImage;
			this._imageBuffer.push(tile.id);
		}
		
		return tile;
	},
	getTileUrl:function(xIndex, yIndex, zoomLevel){
	/// <summary>获取单个格网图片的Url地址。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="xIndex" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="yIndex" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="zoomLevel" type="Number">地图显示的级别。</param>
	/// <returns type="String">单个格网图片的Url地址。</returns>
		var mapScale=this.getMapScale();

		return this._map.getTileUrl(xIndex, yIndex, mapScale);
	},
	_addToBuffer:function(id, tile){
		if(this._imageBuffer){
			if(this._imageBuffer.length > this._imageBufferSize){
				for(var i = 0; i < this._imageBufferSize.length; i++){
					var image = this._imageBuffer.shift();
					if(image){
						this._container.removeChild(image);
						image.onload = null;
						image.onerror = null;
					}
					image = null;
				}
			}
		}
		this._imageBuffer[id] = tile;
	},
	_removeTile:function(tile){
		if(tile){
			if(typeof(tile)=='SuperMap.UI.MapTile'){
				tile.removeFromMap();
			}else{
				// image object
				tile.onload = null;
				tile.onerror = null;
				if(tile.parentNode){
					tile.parentNode.removeChild(tile);
				} 
				tile = null;
			}
		}
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.LeveledTileLayerBase。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.LeveledTileLayerBase";
	}
};

SuperMap.LeveledTileLayerBase.registerClass('SuperMap.LeveledTileLayerBase', SuperMap.TiledLayerBase);

// TiledWmsLayer
// 默认图片大小为256*256
SuperMap.TiledWmsLayer = function(container, wmsParam){
	/// <summary>WMS格网式图层，该类是TiledLayerBase的子类，属于格网式图层的一种。&lt;br&gt;
	/// 格网式图层是客户端呈现图层的一种，格网式图层主要将一个完整的地图切割成多个相同大小的正方形格网块，那么地图就可以由多个块拼合而成。
	/// 这种由多个格网块拼合的图层叫格网式图层（TiledLayer）。&lt;br&gt;
	/// TileWmsLayer主要呈现WMS类型的数据。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="wmsParam" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息。WMSParam类型。</param>
	/// <returns type="SuperMap.TiledWmsLayer">返回 TiledWmsLayer 对象。</returns>
	SuperMap.TiledWmsLayer.initializeBase(this);
	this._param = wmsParam;
	// layernames
	this._layers = null;
	this._version = "1.3.0";
	if(this._param){
		this._layers = "";
		var index = 0;
		if(this._param.layers){
			for( ;index < this._param.layers.length - 1; index++){
				this._layers += this._param.layers[index] + ",";
			}
			this._layers += this._param.layers[index];
		}
	}
	this._enableImageBuffer = true;
	this._debug = true;
	if(wmsParam.manageOnServer) {
		this._manageOnServer = wmsParam.manageOnServer;
	} else {
		this._manageOnServer = false;
	}
};

SuperMap.TiledWmsLayer.prototype = {
	getFullRequestUrl:function(bounds, width, height){
	/// <summary>获取单个格网图片的Url地址。该方法是从WMS服务端获取。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="bounds" type="SuperMap.Rect2D">请求的WMS数据的范围，Rect2D类型。</param>
	/// <param name="width" type="Number">格网图片的宽度。</param>
	/// <param name="height" type="Number">格网图片的高度。</param>
   	/// <returns type="String">单个格网图片的Url地址。</returns>
	    var bbox = bounds.leftBottom.x + "," + bounds.leftBottom.y + "," + bounds.rightTop.x + "," + bounds.rightTop.y;
		var url = this._param.url;
		if(this._param.url.indexOf("?") == -1) {
			url += "?";
		}
		if(this._param.url.indexOf("=") != -1) {
			url += "&";
		}
	    url += "REQUEST=GetMap&SERVICE=WMS&VERSION=" + this._param.version + "&STYLES=&LAYERS=" + this._layers + "&BGCOLOR=" + this._param.bgcolor + "&TRANSPARENT=" + this._param.transparent + "&BBOX=" + bbox + "&HEIGHT=" + height + "&WIDTH=" + width + "&FORMAT=" + this._param.format;
	    if(this._param.srs){
	    	url +="&SRS=";
	    	url += this._param.srs;
	    }

	    return url;
	},

	getTileUrlOnServer:function(x, y, mapScale){
	/// <summary>获取单个格网图片的Url地址。&lt;br&gt;
	/// 该方法从Web服务端的MapHandler（地图服务代理）获取数据。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="x" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="y" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="String">单个格网图片的Url地址。</returns>
        // 添加在MapHandler中处理getTiledWmsImage请求的逻辑，传参和普通图片一样
		var url = this._map._params.mapHandler + "?mapName=" + encodeURI(this._map.get_mapName()) + "&x=" + x + "&y=" + y + "&imageFormat=png&width=" + this._tileSize + "&height=" + this._tileSize + "&mapScale=" + mapScale + "&method=getTiledWmsImage&url=" + escape(this._param.url) + "&VERSION=" + this._param.version + "&transparent=" + this._param.transparent;

		return url;
	},

	//此处为WMSLayer的图层名称。
	set_layers: function(layers){
		this._layers = layers;
	},
	get_layers:function(){
	/// <summary>获取或者设置 WMS 的图层的名称。
	/// </summary>
   	/// <returns type="Array" elementType="String">get_layers()返回值类型是字符串数组。</returns>
		return this._layers;
	},
	getTileUrl:function(x, y, mapScale){
	/// <summary>获取单个格网图片的Url地址。在一个地图比例尺下，地图可以分割为若干格网块，每一个格网块都是有编号的，
	/// 如，（0，0），（-1，2），（1，3）等。根据格网的编号和比例尺就可以确定一个格网图片。
	/// </summary>
	/// <param name="x" type="Number">格网图片所在的x（横向）索引值。</param>
	/// <param name="y" type="Number">格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number">地图比例尺。</param>
	/// <returns type="String">单个格网图片的Url地址。</returns>
		var url = "";

		if(this._manageOnServer){
		    url = this.getTileUrlOnServer(x, y, mapScale);
		}else{
		    var bounds = this._getTileBounds(x, y);
		    url = this.getFullRequestUrl(bounds, this._tileSize, this._tileSize);
		}

		if(this._debug){
    		window.status = "" + url;
		}

		return url;
	},
	_getTileBounds:function(x, y){
		var origin = new SuperMap.Point2D(this._originX, this._originY);
		var offset = new SuperMap.Point2D(this._offsetX, this._offsetY);
		var pxPoint = new SuperMap.Point(-this._tileSize, - this._tileSize);
		var pxPoint2 = new SuperMap.Point(pxPoint.x + this._tileSize, pxPoint.y + this._tileSize);
		// 地图坐标
		var mcPoint = this._map.pixelToMapCoord(pxPoint, this._map.get_mapScale());
		var mcPoint2 = this._map.pixelToMapCoord(pxPoint2, this._map.get_mapScale());
		this._tileWidth = mcPoint2.x - mcPoint.x;
		this._tileHeight = mcPoint2.y - mcPoint.y;

		var leftTop = new SuperMap.Point2D(mcPoint.x + (x + 1) * this._tileWidth, mcPoint.y + (y + 1) * this._tileHeight);
		var rightDown = new SuperMap.Point2D(leftTop.x + this._tileWidth, leftTop.y + this._tileHeight);

		return new SuperMap.Rect2D(leftTop.x, rightDown.y, rightDown.x, leftTop.y);
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.WmsLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.TiledWmsLayer";
	}
};
SuperMap.TiledWmsLayer.registerClass('SuperMap.TiledWmsLayer', SuperMap.TiledLayerBase);

// SuperMap.FeatureLayer
SuperMap.FeatureLayer = function(container){
	/// <summary>要素图层。该类是各种第三方地图服务呈现数据的图层父类。WfsLayer、GeorssLayer、KmlLayer
	/// 都继承该类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即 DOM 对象，如 img，div。</param>
	/// <returns type="SuperMap.FeatureLayer">返回 FeatureLayer 对象。</returns>
	SuperMap.FeatureLayer.initializeBase(this);
	this._container = container;
	this._markers = new Array();
	this._polygons = new Array();
	this._lines = new Array();
	this._initialized = false;
	this._origin = null; // SuperMap.Point对象
	this._mapControl = null;
	this._iconImage = "../../images/supGis/walk2.gif";
	// 请求的xml文本内容编码格式
	this._encoding = "utf-8";
};

SuperMap.FeatureLayer.prototype ={
	initialize:function(mapControl){
		/// <summary>初始化函数。</summary>
		/// <param name="mapControl" type="SuperMap.UI.MapControl">MapControl对象。</param>
		this._mapControl = mapControl;
		this._map = mapControl.getMap();
		this._markers = new Array();
		this._polygons = new Array();
		this._polylines = new Array();
	},
	_addMarker:function(id, x, y, w, h, innerHtml, className, zIndex, groupID, icon){
		var marker = new SuperMap.UI.Marker(this._container); 
		var origin = this._origin;
		if(origin) {
			marker._originX = origin.x;
			marker._originY = origin.y;
		}
		var onDbClickDelegate = Function.createDelegate(this, this._mouseDoubleClick);
		var onMouseWheelDelegate = Function.createDelegate(this, this._mouseWheel);
		marker._events["ondblclick"] = onDbClickDelegate;
		marker._events.push(onDbClickDelegate);
		marker._events["onmousewheel"] = onMouseWheelDelegate;
		marker._events.push(onMouseWheelDelegate);
		if(marker.div.parentNode != this._container){
			this._container.appendChild(marker.div);
		}
		marker.initialize(id, x, y, w, h, innerHtml, className, zIndex, groupID, icon, this._mapControl);
		this._markers.push(marker);
		//this._setMarksHidden();
		return marker.div;
	},
	addMarker:function(id, x, y, w, h, innerHtml, className, zIndex, groupID, icon){
	/// <summary>在图层中添加自定义注记。
	/// </summary>
	/// <param name="id" type="String">自定义注记的 ID 值。</param>
	/// <param name="x" type="Number">注记的 x 坐标。</param>
	/// <param name="y" type="Number">注记的 y 坐标。</param>
	/// <param name="w" type="Number">注记的宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="h" type="Number">注记的高度（显示高度），以像素（px）为单位。</param>
	/// <param name="innerHtml" type="String" >呈现时的 HTML。</param>
	/// <param name="className" type="String">呈现时的样式单 CSS 的 ID。</param>
	/// <param name="zIndex" type="Number">自定义注记层的 Index 值。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义标记可以属于同一组。</param>
	/// <param name="icon" type="String">自定义注记的图标的地址。</param>
	/// <returns type="SuperMap.UI.Marker.div">返回自定义注记的容器 Marker.div。</returns>
		if(this._markers) {
			for(var i = 0; i < this._markers.length; i++){
				var mk = this._markers[i];
				if(mk.id == id){
					mk.dispose();
					delete mk;
					mk = null;
					var marker = new SuperMap.UI.Marker(this._container); 
					var origin = this._origin;
					if(origin) {
						marker._originX = origin.x;
						marker._originY = origin.y;
					}
					var onDbClickDelegate = Function.createDelegate(this, this._mouseDoubleClick);
					var onMouseWheelDelegate = Function.createDelegate(this, this._mouseWheel);
					marker._events["ondblclick"] = onDbClickDelegate;
					marker._events.push(onDbClickDelegate);
					marker._events["onmousewheel"] = onMouseWheelDelegate;
					marker._events.push(onMouseWheelDelegate);
					if(marker.div.parentNode != this._container){
						this._container.appendChild(marker.div);
					}
					marker.initialize(id, x, y, w, h, innerHtml, className, zIndex, groupID, icon, this._mapControl);
					this._markers[i] = marker;
					//mk.initialize(id, x, y, w, h, innerHtml, className, zIndex, groupID, this._mapControl);
					//this._setMarkersHidden();
					return marker.div;
				}
			}
		} else {
			this._markers = new Array();
		}
		
		if(typeof(icon) == "undefined") {
			icon = new Object();
			icon.src = this._iconImage;
		}
		return this._addMarker(id, x, y, w, h, innerHtml, className, zIndex, groupID, icon);
	},
	removeMarker:function(id){
	/// <summary>移除指定的自定义注记。
	/// </summary>
	/// <param name="id" type="String" >待删除的自定义注记的 id。</param>
		for(var i = 0; i < this._markers.length; i++){
			var mk = this._markers[i];
			if(mk.id == id){
				this._markers.splice(i, 1);
				mk.dispose();
				mk = null;
				//this._setMarksHidden();
				return;
			}
		}
	},
	destroy:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		if(this._markers){
			while(this._markers.length > 0){
				this._markers.pop().dispose();	
			}
			this._markers = null;
		}
		if(this._lines){
			while(this._lines.length>0){
				this._lines.pop.dispose();
			}
			this._lines=null;
		}
		if(this._polygons){
			while(this._polygons.length>0){
				this._polygons.pop.dispose();
			}
			this._polygons=null;
		}
	},
	render:function(originX, originY, offsetX, offsetY){
	/// <summary>将 GIS 数据图片显示在图层中。
	/// </summary>
	/// <param name="originX" type="Number">地图显示起始点 X 像素坐标。</param>
	/// <param name="originY" type="Number">地图显示起始点 Y 像素坐标。</param>
	/// <param name="offsetX" type="Number">地图显示偏差值 X 像素坐标。</param>
	/// <param name="offsetY" type="Number">地图显示偏差值 Y 像素坐标。</param>
		var origin = new SuperMap.Point(originX, originY);
		var offset = new SuperMap.Point(offsetX, offsetY);
		this._origin = origin;
		this._offset = offset;
		if(this._markers){
			for(var i = 0; i < this._markers.length; i++){
				// 坐标转换
				this._markers[i].reposition(origin, offset);
			}
		}
//      暂时没实现		
//		if(this._polygons){
//			for(var i=0;i<this._polygons.length;i++){
//				this._polygons.Update();
//			}
//		}
//		if(this._lines){
//			for(var i=0;i<this._lines.length;i++){
//				this._lines.Update();
//			}
//		}
	},
	_inRange:function(point, viewBounds){
		var inrange = false;
		if(point){
			if(point.x <= viewBounds.rightBottom.x && point.x >= viewBounds.leftTop.x&&point.y<=viewBounds.rightBottom.y&&point.y>=leftTop.y){
				inrange =true;
			}
		}
		return inrange;
	},
	_addLine:function(id,xs,ys,strokeWeight,strokeColor,opacity,zIndex,groupID){
		if(id==null||xs==null||ys==null){return null;}
		var line=new Polyline();
		line.set_layer(this);
		line.Init(id,xs,ys,strokeWeight,strokeColor,null,opacity,zIndex,groupID);
		this._lines.push(line);
		//this._SetLinesHidden();
		return line;
	},
	addLine:function(id,xs,ys,strokeWeight,strokeColor,opacity,zIndex,groupID){ 
	/// <summary>在图层中添加一条线。
	/// </summary>
	/// <param name="id" type="String">添加的线的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">线的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">线的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number" >线的宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="opacity" type="Number">线的透明度。</param>
	/// <param name="zIndex" type="Number" >线所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义线可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义线。</returns>
		if(id==null||xs==null||ys==null){return null;}
		for(var i=0;i<this._lines.length;i++){
			var line=this._lines[i];
			if(line.id==id){
				line.Init(id,xs,ys,strokeWeight,strokeColor,null,opacity,zIndex,groupID);
			//	this._SetLinesHidden();
				return line;
			}
		}

		return this._addLine(id,xs,ys,strokeWeight,strokeColor,zIndex,groupID);
	},
	removeLine:function(id){
	/// <summary>移除指定的自定义线。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义线的 id。</param>
		for(var i=0;i<this._lines.length;i++){
			var line=this._lines[i];
			if(line.id==id){
				this._lines.splice(i,1);
				line.destroy();
				this._setLinesHidden();
				return;
			}
		}
	},
	 clearLines:function(){
	 /// <summary>清除所有自定义线。</summary>
	    while(this._lines.length>0)this._lines.pop().destroy();
	    this._detLinesHidden();
	},
	 _addPolygon:function(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
		if(id == null || xs == null || ys == null){return null;}
		var polygon = new Polyline(true);
		polygon.set_layer(this);
		polygon.Init(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID);
		this._polygons.push(polygon);
		//this._setPolygonsHidden();
		return polygon;
	},
	addPolygon:function(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
	/// <summary>在图层中添加自定义面。
	/// </summary>
	/// <param name="id" type="String">添加的面的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">面的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">面的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">面的边线宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor">面的边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor">面的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillOpacity" type="Number">面的透明度。</param>
	/// <param name="zIndex" type="Number">面所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义面可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义面。</returns>
		if(id == null || xs == null || ys == null){return null;}
		var polygon = null;
		for(var i = 0; i < this._polygons.length; i++){
			polygon = this._polygons[i];
			if(polygon.id == id){
				polygon.init(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID);
			  //  this._setPolygonsHidden();
				return polygon;
			}
		}
		polygon=null;

		return _addPolygon(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex);
	},
	
	removePolygon:function(id){
	/// <summary>移除指定的自定义面。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义面的id。</param>
		for(var i = 0; this._polygons && i < this._polygons.length; i++){
			var polygon = this._polygons[i];
			if(polygon.id == id){
				this._polygons.splice(i,1);
				polygon.Destroy();
				_SetPolygonsHidden();
				return;
			}
		}
	},
	clearPolygons:function (){
	 /// <summary>清除所有自定义面。</summary>
		while(this._polygons.length > 0)
			this._polygons.pop().destroy();
			this._setPolygonsHidden();
	},
	_setMarkersHidden:function(){
        var hidden = $get(container.id + "_hiddenMarks");
        //? this._marks无法直接ToJSON,先转换下
        var markers = null;
        if(this._markers){
            markers = new Array();
            for(var i = 0; i < this._markers.length; i++ ){
                var marker = new Object();
                marker.id  = this._markers[i].id;
                marker.x  = this._marks[i].x;
                marker.y  = this._marks[i].y;
                marker.w  = this._marks[i].w;
                marker.h  = this._marks[i].h;
                marker.innerHtml  = SuperMap.Utility.convertHTMLToESC(this._markers[i].innerHtml);
                marker.className  = this._markers[i].className;
                marker.zIndex = this._markers[i].zIndex;
                marker.groupID = this._markers[i].groupID;
                markers.push(marker);
            }
        }
        //再将转换出来的marks toJSON
        var json = SuperMap.Utility.toJSON(markers);
        //清理对象
        if(markers){
            while(markers.length > 0){
                var marker = markers.pop();
                marker = null;
            }
           markers = null; 
        }
        hidden.value = json;
    },
	_setLinesHidden:function(){
		var hidden=document.getElementById(container.id + "_hiddenLines");
		//?this._lines无法直接ToJSON,先转换下
		var lines  = null;
		if(this._lines){
			lines = new Array();
			for(var i=0;i < this._lines.length; i++){
				var line = new Object();
				line.id  = this._lines[i].id;
				line.xs  = this._lines[i].xs;
				line.ys  = this._lines[i].ys;
				line.strokeWeight = this._lines[i].strokeWeight;
				line.strokeColor = this._lines[i].strokeColor;
				line.zIndex = this._lines[i].zIndex;
				line.groupID = this._lines[i].groupID;
				lines.push(line);
			}
		}
		//再将转换出来的lines toJSON
		var json = SuperMap.Utility.toJSON(lines);
		//清理对象
		if(lines){
			while(lines.length > 0){
				var line = lines.pop();
				line = null;
			}
			lines = null; 
		}
		hidden.value = json;
    },
	_setPolygonsHidden:function(){
		var hidden = $get(container.id + "_hiddenPolygons");
		//?this._polygons无法直接ToJSON,先转换下
		var polygons = null;
		if(this._polygons){
			polygons = new Array();
			for(var i = 0; i < this._polygons.length; i++){
				var polygon = new Object();
				polygon.id = this._polygons[i].id;
				polygon.xs = this._polygons[i].xs;
				polygon.ys = this._polygons[i].ys;
				polygon.strokeWeight = this._polygons[i].strokeWeight;
				polygon.strokeColor = this._polygons[i].strokeColor;
				polygon.fillColor = this._polygons[i].fillColor;
				polygon.fillOpacity = this._polygons[i].fillOpacity;
				polygon.zIndex = this._polygons[i].zIndex;
				polygon.groupID = this._polygons[i].groupID;
				polygons.push(polygon);
			}
		}
		//再将转换出来的polygons toJSON
		var json = SuperMap.Utility.toJSON(polygons);
		//清理对象
		if(polygons){
			while(polygons.length > 0){
				var polygon = polygons.pop();
				polygon = null;
			}
			polygons = null; 
		}
		hidden.value = json;
	},
	// 交换标记的状态
	swapStates:function(){
    /// <summary>新标注（Marker）图标下载完成后，该方法执行将新标注（Marker）图标替换原有图标的操作。</summary>
		for(var i = 0; i < this._markers.length; i++){
			this._markers[i].swapStates();
			this._markers[i].clearSteps();
			this._markers[i]._setFactor(0);
		}
	},
	get_markers:function(){
	/// <summary>获取所有的自定义注记对象。</summary>
   	/// <returns type="Array" elementType="SuperMap.UI.Marker">所有的自定义注记对象。</returns>
		return this._markers;
	},
	get_lines:function(){
	/// <summary>获取所有的自定义线对象。</summary>
   	/// <returns type="Array" elementType="SuperMap.UI.Polyline">所有的自定义线对象。</returns>
		return this._lines;
	}, 
	get_polygons:function(){
	/// <summary>获取所有的自定义面对象。</summary>
   	/// <returns type="Array" elementType="SuperMap.UI.Polyline">所有的自定义面对象。</returns>
		return this._polygons;
	},
	setFactor:function(factor){
	/// <summary>设置自定义图片缩放的级别。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。
	/// </summary>
	/// <param name="factor" type="Number">当前本地图片缩放的级别。</param>
		if(this._markers){
			for(var i = 0; i < this._markers.length; i++){
				this._markers[i]._setFactor(factor);
			}
		}
	},
	prepareForZoom:function(newOriginX, newOriginY, newZoom){
	/// <summary>放置缩放操作后自定义注记在图层中的位置。
	/// </summary>
	/// <param name="newOriginX" type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
	/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
	/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
	    var offsetX = this._mapControl.getOffset().x;
		var offsetY = this._mapControl.getOffset().y;
		for(var i = 0; i < this._markers.length; i++){
			this._markers[i].prepareForZoom(offsetX, offsetY, newOriginX, newOriginY, newZoom);
		}
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.FeatureLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.FeatureLayer";
	},
	_mouseDoubleClick:function(e){
		e = SuperMap.Utility.getEvent(e);
		SuperMap.Utility.cancelBubble(e);
		if(this._map.getIsZooming() || this._map.getPanning()){return false;}
		var mapScale = this._map.get_mapScale();
		if(e.altKey){
			mapScale = mapScale * 2;
		}else{
			mapScale = mapScale / 2;
		}
		// this.get_mapControl().setCenterAndZoom(this._mc, mapScale);

		return false;
	},
	_mouseWheel:function(e){
		e = SuperMap.Utility.getEvent(e);
		SuperMap.Utility.cancelBubble(e);
		if(this._map.getIsZooming() || this._map.getPanning()){return false;}
		var delta = SuperMap.Utility.getMouseScrollDelta(e);
		//if(delta > 0){
		//	this.get_mapControl().zoomIn();
		//}else if(delta < 0){
		//	this.get_mapControl().zoomOut();
		//}
		return false;
	}, 
	repositionMarkers:function(originX, originY, offsetX, offsetY){
	/// <summary>根据客户端Mapcontrol的地图的变化重置自定义标记。</summary>
	/// <param name="originX" type="Number">地图显示起始点X像素坐标。</param>
	/// <param name="originY" type="Number">地图显示起始点Y像素坐标。</param>
	/// <param name="offsetX" type="Number">地图显示偏差值X像素坐标。</param>
	/// <param name="offsetY" type="Number">地图显示偏差值Y像素坐标。</param>
		var origin = new SuperMap.Point(originX, originY);
		var offset = new SuperMap.Point(offsetX, offsetY);
		if(this._markers){
			for(var i = 0; i < this._markers.length; i++){
				// 坐标转换
				this._markers[i].reposition(origin, offset);
			}
		}
	},

	set_iconImage:function(url) {
		this._iconImage = url;
	},

	get_iconImage:function() {
	/// <summary>获取或者设置默认的标注显示图标图像的地址。</summary>
	/// <returns type="String">返回值类型为String，默认的标注显示图标图像的地址。</returns>
		return this._iconImage;
	}
};
SuperMap.FeatureLayer.registerClass('SuperMap.FeatureLayer', SuperMap.LayerBase);

//-----------------
// SuperMap.WfsLayer
//-----------------
SuperMap.WfsLayer = function(container, param){
	/// <summary>WFS要素图层。该类呈现Wfs要素。该类是FeatureLayer的子类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="param" type="Object">Object对象，存储了要显示在该图片图层中的GIS信息。</param>
	/// <returns type="SuperMap.WfsLayer">返回 WfsLayer 对象。</returns>
	SuperMap.WfsLayer.initializeBase(this);
	this._container = container;
	this._param = param;
	this._groupID = "unkown";
	this._mapControl = null;
};

SuperMap.WfsLayer.prototype = {
	set_param:function(param){
		this._param = param;
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		
	},
	initialize:function(mapControl){
	/// <summary>初始化函数。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">MapControl对象。</param>
		this._mapControl = mapControl;
		this._map = mapControl.getMap();
		
	},
	_getFeatures:function(){
		var _groupID =this._groupID;
		try{
			var mapBounds=this._mapControl.GetViewBounds();
			var param = this._param;
			var url = param.url;
			var addMarkerDelegate=Function.createDelegate(this, this.addMarker);
			url+="?SERVICE=WFS&VERSION="+param.version+"&REQUEST="+param.request+"&TYPENAME="+param.typeName+"&BBOX="+mapBounds.leftBottom.x+","+mapBounds.rightTop.y+","+mapBounds.rightTop.x+","+mapBounds.leftBottom.y;
			window.status=""+"&BBOX="+mapBounds.leftBottom.x+","+mapBounds.rightTop.y+","+mapBounds.rightTop.x+","+mapBounds.leftBottom.y;
			var xhr=SuperMap.Utility.getXmlHttpRequest();
			xhr.open("post", url, false);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.onreadystatechange=function(){
				var readyState=xhr.readyState;
				if (readyState==4){
					var status=xhr.status;
					if(status==200){
						if(!xhr.responseText){return;}
						var xmldom = new ActiveXObject('Microsoft.XMLDOM');
						xmldom.loadXML(xhr.responseText);
						var members=xmldom.getElementsByTagName("gml:featureMember");

						if(members.length>0){	
							var typeNode;
							for(var j=0;j<members.length;j++){
								for (var k =0; k<members[j].childNodes.length;k++){
									var o=members[j].childNodes[k];
									var nameNode;
									var nodeX;
									var nodeY;
									for(var n=0;n<o.childNodes.length;n++){
										var node=o.childNodes[n];
										if(node.tagName=="NAME"){
											nameNode=node;
										}else if(node.tagName=="X"){
											nodeX=node;
										}else if(node.tagName=="Y"){
											nodeY=node;
										}
									}
									if(nodeX&&nodeY&&nodeX.text!='undefined'&&nodeY.text!='undefined'){
										var point=new SuperMap.Point2D(nodeX.text,nodeY.text);
										try{
											addMarkerDelegate(nameNode.text,point.x,point.y,10,10,nameNode.text,"",50,_groupID);
										}catch(ex){
										//? handle exception
										}
									}
							   } 
							 }
						}
					} else{
						return;
					}
					xhr.onreadystatechange = function(){};
					xhr=null;
				 }
			 };
			xhr.send();
		}catch(e){
			//alert("getfeature" +e);
		}
	},
	render:function(){
	/// <summary>将GIS数据图片显示在图层中。
	/// </summary>
		// 从服务器端直接出
		if(!this._map.getIsZooming() || !this._map.getPanning()){
			this._getFeatures();
		}
		if(this._markers){
			for(var i = 0; i < this._markers.length; i++){
				// 坐标转换
				this._markers[i].reposition();
			}
		}
		if(this._polygons){
			for(var i=0;i<this._polygons.length;i++){
				this._polygons.Update();
			}
		}
		if(this._lines){
			for(var i=0;i<this._lines.length;i++){
				this._lines.Update();
			}
		}
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.WfsLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.WfsLayer";
	}
};

SuperMap.WfsLayer.registerClass('SuperMap.WfsLayer', SuperMap.FeatureLayer);
// GeoRssLayer 目前支持从xml文件直接获取内容
// 服务器端可以直接调用addMarker()
SuperMap.GeorssLayer=function(container, url){
	/// <summary>GeoRSS要素图层。该类呈现GeoRSS要素。该类是FeatureLayer的子类。&lt;br&gt;
	/// GeoRssLayer 目前支持从xml文件直接获取内容，服务器端可以直接调用addMarker()。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="url" type="String">GeoRSS文档的访问地址。</param>
	/// <returns type="SuperMap.GeorssLayer">返回 GeorssLayer 对象。</returns>
    SuperMap.GeorssLayer.initializeBase(this);
    this._container = container;
    this._url = escape(url);
    this._initialized = false;
	this._mapControl = null;
};
SuperMap.GeorssLayer.prototype={
	initialize:function(mapControl){
	/// <summary>初始化函数。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">MapControl对象。</param>
		this._mapControl = mapControl;
		this._map = mapControl.getMap();
	},
	_init:function(){
		var xmlReq = SuperMap.Utility.getXmlHttpRequest();
		var contextPath = this._map._params.contextPath;
		xmlReq.open("get", contextPath + "ajaxhandler?url=" + this._url, true);
		xmlReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + this._encoding);
		var m = this;
		xmlReq.onreadystatechange = function() {
			if(xmlReq.readyState == 4 && xmlReq.status == 200) {
				m._callback(xmlReq.responseText, xmlReq.status);
				xmlReq.onreadystatechange = function(){};
				xmlReq = null;
			}
		};
		xmlReq.send(null);
	},
	render:function(){
	/// <summary>将GIS数据图片显示在图层中。
	/// </summary>
		if(!this._initialized){
			this._init();
			this._initialized=true;
		}

		var origin = this._mapControl.getOrigin();
		var offset = this._mapControl.getOffset();
		if(this._markers){
			for(var i =0;i<this._markers.length;i++){
				this._markers[i].reposition(origin, offset);
			}
		}
	},
	_callback:function(responseText, status){
		try {
			var xmldoc;
			if (window.ActiveXObject) {
				xmldoc = new ActiveXObject("microsoft.XMLDOM");
			} else if (document.implementation&&document.implementation.createDocument) {
				xmldoc = document.implementation.createDocument("","doc",null);
			}
			xmldoc.async = false;
			if(window.ActiveXObject) {
				xmldoc.loadXML(responseText);
			} else {
				var parser = new DOMParser();
                xmldoc = parser.parseFromString(responseText, "text/xml");
			}
			try {
				var root = xmldoc.documentElement;
				var items = root.getElementsByTagName("item");
				for (var i = 0; i < items.length; i++) {
					try {
						this._createMarker(items[i]);
					} catch (e) {
						alert(e);
					}
				}
			} catch(e) {
				alert(e);
			}
		} catch(ex) {
			// alert("callback:" + ex);
		}
	},
	_createMarker:function(item){
		// 需要注意对应xml文件内容的命名空间
		var title = item.getElementsByTagName("title")[0].childNodes[0].nodeValue;
		var description;
		try {
			description = item.getElementsByTagName("description")[0].childNodes[0].nodeValue;
		} catch (e) {}
		var link;
		try {
			link = item.getElementsByTagName("link")[0].childNodes[0].nodeValue;
		} catch (e) {}

		/* namespaces are handled by spec in moz, not in ie */
		var latlng = ""; 
		var lat; 
		var lng; 
		var box = ""; 
		var img = "";
		if (navigator.userAgent.toLowerCase().indexOf("msie") < 0) {
			try {
				latlng = item.getElementsByTagNameNS("http://www.georss.org/georss","point")[0].childNodes[0].nodeValue;
			} catch (e) {latlng = "";}
			try {
				lat = item.getElementsByTagNameNS("http://www.w3.org/2003/01/geo/wgs84_pos#","lat")[0].childNodes[0].nodeValue;
				lng = item.getElementsByTagNameNS("http://www.w3.org/2003/01/geo/wgs84_pos#","long")[0].childNodes[0].nodeValue;
			} catch (e) {
				try {
					lat = item.getElementsByTagNameNS("http://www.geo.org/geo","lat")[0].childNodes[0].nodeValue;
					lng = item.getElementsByTagNameNS("http://www.geo.org/geo","long")[0].childNodes[0].nodeValue;
				} catch(e) {
				}
			}
			try {
				var rel = item.getElementsByTagNameNS("http://www.georss.org/georss","box")[0].getAttribute("relationshiptag");
				if (rel == "image-extent") {
					box = item.getElementsByTagNameNS("http://www.georss.org/georss","box")[0].childNodes[0].nodeValue;
					img = item.getElementsByTagNameNS("http://search.yahoo.com/mrss","content")[0].getAttribute("url");
				}
			} catch (e) { }
		} else {
			try {
				latlng = item.getElementsByTagName("georss:point")[0].childNodes[0].nodeValue;
			} catch (e) {latlng = "";}
			try {
				lat = item.getElementsByTagName("geo:lat")[0].childNodes[0].nodeValue;
				lng = item.getElementsByTagName("geo:long")[0].childNodes[0].nodeValue;
			} catch(e) {}
			try {
				box = item.getElementsByTagName("georss:box")[0].childNodes[0].nodeValue;
			} catch (e) {}
			try {
				var rel = item.getElementsByTagName("georss:box")[0].getAttribute("relationshiptag");
				if (rel == "image-extent") {
					box = item.getElementsByTagName("georss:box")[0].childNodes[0].nodeValue;
					img = item.getElementsByTagName("media:content")[0].getAttribute("url");
				}
			} catch (e) {}
		}
		if (latlng.length > 0) {
			lat = latlng.split(" ")[0];
			lng = latlng.split(" ")[1];
		}
		if (lat.length > 0 && lng.length > 0) {
			var point = new SuperMap.Point2D();
			point.x=parseFloat(lng);
			point.y=parseFloat(lat);
			var marker = new Object();
			marker.title=title;
			marker.link=link;
			marker.description=description;
			marker.position=point;
			var html="<table style='background-color:White;'>";
			html+="<tr><td style='width: 150px;height:15px;'><span style='font-size: 8pt; '>标题:"+title+"</span></td><td></td></tr>";
			html+="<tr><td style='width: 150px;height:15px;'><span style='font-size: 8pt; '>描述:"+description+"</span></td><td></td></tr>";
			html+="<tr><td style='width:175px;height:15px;'><span style='font-size: 8pt; '><a href='"+link+"'   target='_blank'>相关链接</a></span></td><td></td></tr><tr><td style='height:15px;'></td><td></td></tr></table>";
			try{
				this.addMarker(title,point.x,point.y,5,5,html,"", this._container.style.zIndex,"");
			}catch(e){
				//alert(e);
			}
		 }
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.GeorssLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.GeorssLayer";
	}
};
SuperMap.GeorssLayer.registerClass("SuperMap.GeorssLayer", SuperMap.FeatureLayer);

//------------------
// SuperMap.KmlLayer
//------------------
SuperMap.KmlLayer = function(container, url){
	/// <summary>KML要素图层。该类呈现KML要素。该类是FeatureLayer的子类。
	/// </summary>
	/// <param name="container" type="Object" domElement="true">呈现图片图层数据的图层容器，即DOM对象，如img,div。</param>
	/// <param name="url" type="String">KML的地址。</param>
	/// <returns type="SuperMap.KmlLayer">返回 KmlLayer 对象。</returns>
	SuperMap.KmlLayer.initializeBase(this);
	this._container = container;
	this._url = escape(url);
	this._initialized = false;
	this._mapControl = null;
};
SuperMap.KmlLayer.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化函数。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">MapControl对象。</param>
		this._mapControl = mapControl;
		this._map = mapControl.getMap();
    },
	_init:function() {
		try{
			var xmlReq = SuperMap.Utility.getXmlHttpRequest();
			var contextPath = this._map._params.contextPath;
			xmlReq.open("get", contextPath + "ajaxhandler?url=" + this._url, true);
			xmlReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + this._encoding);
			var m = this;
			xmlReq.onreadystatechange = function() {
				if(xmlReq.readyState == 4 && xmlReq.status == 200) {
					m._callback(xmlReq.responseText, xmlReq.status);
				}
			}
			xmlReq.send(null);
		}catch(e){
			alert(e);
		}
	},
	render:function(){
	/// <summary>将GIS数据图片显示在图层中。
	/// </summary>
		if(!this._initialized){
		 	this._init();
			this._initialized = true;
		 } else {
			 if(this._markers){
			 	for(var i = 0; i<this._markers.length; i++){
					this._markers[i].reposition();
				}
			 }
		 }
	},
	_callback:function(responseText, status){
		try{
			if (status == 200) {
				// 默认采用xml形式，当xml文档内容过大时，建议采用在服务器端做转换后直接添加到layer。
				var xmldoc;
				if (window.ActiveXObject) {
					xmldoc = new ActiveXObject("microsoft.XMLDOM");
				} else if (document.implementation&&document.implementation.createDocument) {
					xmldoc = document.implementation.createDocument("","doc",null);
				}
				xmldoc.async = false;
				if(window.ActiveXObject) {
					xmldoc.loadXML(responseText);
				} else {
					var parser = new DOMParser();
	                xmldoc = parser.parseFromString(responseText, "text/xml");
				}
				var items = xmldoc.getElementsByTagName("Placemark");
				for (var i = 0; i < items.length; i++) {
					try {
						this._createMarker(items[i]);
					} catch (e) {
						alert(e);
					}
				}
			}
		}catch(ex){
			//alert(ex);
		}
	},
	_createMarker:function(item){
		var iconStyle = item.getElementsByTagName("Icon")[0] || item.getElementsByTagName("icon")[0];
		// 单独设置一个Icon对象
		var icon = new Object();
		try{
			for(var i=0;iconStyle != null && i < iconStyle.childNodes.length; i++){
				var node = iconStyle.childNodrs[i];
				if(node.getTagName == "href"){
					icon.src = node.childNodes[0].nodeValue;
				}else if(node.getTagName == "x"){
					icon.x = node.childNodes[0].nodeValue;
				}else if(node.getTagName == "y"){
					icon.y = node.childNodes[0].nodeValue;
				}else if(node.getTagName == "w"){
					icon.width = parseInt(node.childNodes[0].nodeValue);
				}else if(node.getTagName == "h"){
					icon.height = parseInt(node.childNodes[0].nodeValue);
				}
			}
		}catch(ex){
		}
		var name=item.getElementsByTagName("name")[0].childNodes[0].nodeValue;
		var description = false;
		try{
			description=item.getElementsByTagName("description")[0].childNodes[0].nodeValue;
		}catch(e){}
		var point=item.getElementsByTagName("Point")[0];
		if(point != null) {
			var coordinates = point.getElementsByTagName("coordinates")[0].childNodes[0].nodeValue;
			var c=coordinates.split(",");
			var position = new SuperMap.Point2D();
			position.x=parseFloat(c[0]);
			position.y=parseFloat(c[1]);
			//id,x,y,w,h,innerHtml,className,zIndex,groupID
			//todo here
			var innerHtml = "";
			if(description) {
				innerHtml = name + "<br><br>" + description;
			}
			if(typeof(icon.x)=='undefined' || !icon.x || icon.x==null){
				// use default Icon，默认图片使用其他图片替代
				icon.src = this._iconImage;
			}
			this.addMarker(name,position.x,position.y,5,5,innerHtml,"",this._container.style.zIndex,"",icon);
		}
	},
	getType:function(){
	/// <summary>获取该对象的图层类型。默认为SuperMap.KmlLayer。
	/// </summary>
   	/// <returns type="String">返回图层类型。</returns>
		return "SuperMap.KmlLayer";
	}
};
SuperMap.KmlLayer.registerClass("SuperMap.KmlLayer", SuperMap.FeatureLayer);