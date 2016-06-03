//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.MapTile.js  
// 功能：			MapTile
// 最后修改时间：	2007-8-31
//========================================================================== 

Type.registerNamespace("SuperMap.UI");
SuperMap.UI.MapTile = function(container){
    /// <summary>该类描述格网地图的格网信息。格网地图是将从iServer服务器获取的图片切割为若干格网图片进行呈现。</summary>
    /// <param name="container" type="Object" domElement="true">呈现地图图片的容器，即DOM对象，如img,div。</param>
	/// <returns type="SuperMap.UI.MapTile">返回 MapTile 对象。</returns>
	/// <field name="queued" type="Boolean">是否螺旋加载。默认值为false。</field>
	var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
	if (e) throw e;

	this._container = container;
	this._indexX = 0;//tileX
	this._indexY = 0;//tileY
	this._mapScale = 0;//mapScale
	this._map = null;
	this._image = null;
	this._tempImage = null;
	// 覆盖图片，在imageCover里面显示图片
	this._imageCover = document.createElement("div");
	this._imageCover.id="_overlay.unInited";

	this._zIndex = 0;
	this._zoomTotalSteps = 0;
	this._xSteps = null;//steps x Array
	this._ySteps = null;
	this._widthSteps = null;
	this._heightSteps = null;
	this._factorable = true;
	this._curPositionX = 0, this._curPositionY = 0, this._curWidth = 0, this._curHeight = 0;//current xywh
	this._nextPositionX = 0, this._nextPositionY = 0, this._nextWidth = 0, this._nextHeight = 0;//next xywh
	this._initTime = null;
	this._tileSize = 256;
	this._imageSrc = null;
	this._debug = false;
	this.queued = false;// 螺旋加载用
	this._loaded = false;
};

SuperMap.UI.MapTile.prototype = {
    initialize:function(tileIndexX, tileIndexY, mapScale, mapComponent, x, y, zoomTotalSteps){
	/// <summary>初始化MapTile。
	/// </summary>
	/// <param name="tileIndexX" type="Number" >格网图片所在的x（横向）索引值。</param>
	/// <param name="tileIndexY" type="Number" >格网图片所在的y（纵向）索引值。</param>
	/// <param name="mapScale" type="Number" >地图比例尺。</param>
	/// <param name="mapComponent" type="SuperMap.Map">Map对象。</param>
	/// <param name="x" type="Number" >地图格网左上角的x值。</param>
	/// <param name="y" type="Number" >地图格网左上角的y值。</param>
	/// <param name="zoomTotalSteps" type="Number" integer="true">地图缩放过程中本地格网图片缩放的总级别数。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，
	/// 这一缩放也是逐级进行的。</param>
        var e = Function._validateParams(arguments, [
            {name: "tileIndexX", type: Number},
            {name: "tileIndexY", type: Number},
            {name: "mapScale", type: Number},
            {name: "mapComponent", type: SuperMap.Map},
            {name: "x", type: Number},
            {name: "y", type: Number},
            {name: "zoomTotalSteps", type: Number, integer: true}
        ]);
		if (e) throw e;
		this._indexX = tileIndexX;
		this._indexY = tileIndexY;
		this._mapScale = mapScale;
		this._map = mapComponent;
		if(!this._map.isValidTile(this._indexX, this._indexY, this._mapScale)){
			return;
		}

		this._zoomTotalSteps = zoomTotalSteps;
		if(!this._zoomTotalSteps){
			this._zoomTotalSteps = 5;	
		}
		var n = this._zoomTotalSteps + 1;
		this._xSteps = new Array(n);
		this._ySteps = new Array(n);
		this._widthSteps = new Array(n);
		this._heightSteps = new Array(n);
		this._zIndex = this._container.style.zIndex
		this._imageCover.style.font = "7pt Verdana, sans-sansserif";
		this._imageCover.style.color = "Red";
		this._imageCover.style.backgroundColor = "White";

		// 原来是只能初始化正方形 是否要改掉?
		this._setCurrentState(x, y, this._tileSize, this._tileSize);
		this._setNextState(x, y, this._tileSize, this._tileSize);
		this._precomputeSteps();
		
		var imageId = this._indexX + "," + this._indexY + "," + this._mapScale;
		this.id = imageId;
    	this._imageCover.id="_overlay." + imageId;

		try{
			this._initTime = new Date();
			if(this._image && this._image.src != null){
				this._image.onmousedown = function(e){return false;};
				if(!this._map.getIsZooming()){
					this.setFactor(this._map.getZoomCounter());
				}
								
				this._loaded = true;
			}else{
				this._tempImage=new Image(this._tileSize,this._tileSize);
				this._tempImage.id=imageId;
				
				var _imgLoadDelegate = Function.createDelegate(this, this._imgLoad);
				var _imgErrorDelegate = Function.createDelegate(this, this._imgError);
				
				this._tempImage.onload = _imgLoadDelegate;
				this._tempImage.onerror = _imgErrorDelegate;
				this._tempImage.src = this._imageSrc;
			 }
		}catch(e){
			// 是否处理成加载出错图片
		}
	},
    
    set_tileSize:function(tileSize){
	/// <summary>设置格网的大小。</summary>
	/// <param name="tileSize" type="Number" integer="true">格网的边长。</param>
	    this._tileSize = tileSize;
	    if(this._tileSize < 128){
	        this._tileSize = 128;
	    }
	},
	dispose:function(){
		/// <summary>释放对象占用的资源。</summary>
        if(this._image){
            this._image.onmousedown = null;
        }
        if (this._xSteps)
        {
			while(this._xSteps.length > 0){
            this._xSteps.pop();
			}
        }
        while(this._ySteps.length > 0){
            this._ySteps.pop();
        }
        while(this._widthSteps.length > 0){
            this._widthSteps.pop();
        }
        while(this._heightSteps.length > 0){
            this._heightSteps.pop();
        }
        this._xSteps = this._ySteps = this._widthSteps = this._heightSteps = null;
        // this._tileSelf = null;
		this._nextPositionX = null;
        this._nextPositionY = null;
        this._nextWidth = null;
        this._nextHeight = null;
		this._initTime = null;
		this._curPositionX = null;
        this._curPositionY = null;
        this._curWidth = null;
        this._curHeight = null;
		if(this._imageCover) {
			this._imageCover.onmousedown = null;
			if(this._imageCover.parentNode) {
				this._imageCover.parentNode.removeChild(this._imageCover);
			}
			delete this._imageCover;
			this._imageCover = null;
		}
		this._initTime = null;
		this._map = null;
    },
	
    _setCurrentState:function(x, y, width, height){
        this._curPositionX = x;
        this._curPositionY = y;
        this._curWidth = width;
        this._curHeight = height;
    },
    
	_setNextState:function(x, y, width, height){
        this._nextPositionX = x;
        this._nextPositionY = y;
        this._nextWidth = width;
        this._nextHeight = height;
    },
    
	clearSteps:function(){
	/// <summary>清除缩放过程中生成的临时数据。</summary>
	    if(!this._zoomTotalSteps){
	        this._zoomTotalSteps = 5;
	    }
		for(var i = 0; i <= this._zoomTotalSteps; i++){
            this._xSteps[i] = this._curPositionX; 
            this._ySteps[i] = this._curPositionY;
            this._widthSteps[i] = this._curWidth;
            this._heightSteps[i] = this._curHeight;
        }
    },
    
	_precomputeSteps:function(){
	    if(!this._zoomTotalSteps){
	        this._zoomTotalSteps = 5;
	    }
		if(this._xSteps == null){
			return;
		}
	    for(var i = 0; i <= this._zoomTotalSteps; i++){
			var a = i / this._zoomTotalSteps;
			var b = 1.0 - a;
			this._xSteps[i] = SuperMap.Utility.floor(b * this._curPositionX + a * this._nextPositionX);
            this._ySteps[i] = SuperMap.Utility.floor(b * this._curPositionY + a * this._nextPositionY);
			this._widthSteps[i] = SuperMap.Utility.ceil(b * this._curWidth + a * this._nextWidth);
            this._heightSteps[i] = SuperMap.Utility.ceil(b * this._curHeight + a * this._nextHeight);
		}
	},
    
	setFactor:function(i){
	/// <summary>设置地图缩放过程中当前缩放状态下本地格网式图层中的图片缩放的大小。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。</summary>
	/// <param name="i" type="Number" integer="true">当前缩放过程所处的级别。</param>
		if(this._image == null || (this._map.getIsZooming() && !this._factorable)){
			return;
		}

		var is = this._image.style;
		is.left = this._xSteps[i] + "px";
		is.top = this._ySteps[i] + "px";
		is.width = this._widthSteps[i] + "px";
		is.height = this._heightSteps[i] + "px";
		var os = this._imageCover.style;
		if(this._debug && i == 0){
			is.border = "1px dashed red";
			os.left = this._xSteps[i] + "px";
			os.top = this._ySteps[i] + "px";
		}
		if(this._image.parentNode != this._container){
			is.position = "absolute";
			is.zIndex = this._zIndex;
			this._container.appendChild(this._image);
			if(this._debug && this._imageCover.parentNode != this._container){
				this._imageCover.innerHTML = this._image.id;
				os.position = "absolute";
				os.zIndex = this._zIndex + 1;
				this._container.appendChild(this._imageCover);
			}
			
		}
	},
	
	swapStates:function(){
	/// <summary>新网格图片下载完成后，该方法执行将新网格图片替换原有图片的操作。</summary>
		var temp = 0;
		temp = this._curPositionX; this._curPositionX = this._nextPositionX; this._nextPositionX = temp;
		temp = this._curPositionY; this._curPositionY = this._nextPositionY; this._nextPositionY = temp;
		temp = this._curWidth; this._curWidth = this._nextWidth; this._nextWidth = temp;
		temp = this._curHeight; this._curHeight = this._nextHeight; this._nextHeight = temp;
	},
	
	debug:function(enabled){
	/// <summary>是否开启调试信息。</summary>
	/// <param name="enabled" type="Boolean">布尔值，true代表开启。</param>
		this._debug = enabled;
		if(this._image != null){
			this._image.style.border = enabled ? "1px dashed blue" : "0px";
		}
		this._imageCover.style.display = enabled ? "block" : "none";
	},
	
	_imgLoad:function(){
		if(this._mapScale != this._map.get_mapScale() || this._tempImage == null){
			return;
		}
		
		if(this._debug){
			var loadedTime = new Date();
			var elapsed = loadedTime.getTime() - this._initTime.getTime();
			window.status = "elapsedTime=" + elapsed;
		}
		this._tempImage.onload = null;
		this._tempImage.onerror = null;
		this._image = this._tempImage;
		this._image.onmousedown = function(e){
			return false;
		};

		delete this._tempImage; 
		this._tempImage = null;
		if(!this._map.getIsZooming()){
			this.setFactor(this._map.getZoomCounter());
		}

		this._loaded = true;
	},
	
	_imgError:function(){
		if(this._mapScale != this._map._mapScale || this._tempImage == null){
			return;
		}
		var loadedTime = new Date();
		var elapsed = loadedTime.getTime() - this._initTime.getTime();
		loadedTime = null;
		if(this._image){
			this._image.onload = null;
			this._image.onerror = null;
			this._image = null;
		}
	}
};

SuperMap.UI.MapTile.registerClass('SuperMap.UI.MapTile', Sys.UI.Control, Sys.IDisposable);