
//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.NavigationControl.js 
// 功能：			Ajax NavigationControl 类 
// 最后修改时间：	2007-11-02
//========================================================================== 

Type.registerNamespace("SuperMap.UI");

SuperMap.UI.NavigationControl = function(container){
	/// <summary>指北针控件。&lt;br&gt;
	/// 指北针控件可以按照方向标的方位来移动地图。当按住指北针控件的北向的方向标时，地图将向北匀速平移。
	/// 当点击指北针控件的某方向的方向标时，地图将向该方向移动固定图幅的范围。</summary>
    /// <param name="container" type="Object" domElement="true">装载放大镜控件的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.NavigationControl">返回 NavigationControl 对象。</returns>
   
    var e = Function._validateParams(arguments, [
        {name: "container", domElement: true}
    ]);
    if (e) throw e;
    
//    SuperMap.UI.NavigationControl.initializeBase(this, [container]);
    
	this._container = container;
	this._mapControl = null;
	
	this._backgroundUrl = "../../images/supGis/navigation.jpg";
	this._navigationRate = 0;
	this._navigationInterval = 0;
	this._width = 0;
	this._height = 0;
	this._id = this._container.id;
	this._angle = 0;
	this._image = null;
	this._useNavigation = false;
	
    this._moveMapDelegate = Function.createDelegate(this, this._moveMap);
    this._startNavigateDelegate = Function.createDelegate(this, this._startNavigate);
    this._countNavigateDelegate = Function.createDelegate(this, this._countNavigate);
    this._endNavigateDelegate = Function.createDelegate(this, this._endNavigate);
    
    this._iTimeoutIDForMoveMap = null;
    
    this._debug = false;
}

	
SuperMap.UI.NavigationControl.prototype={

    get_mapControl:function(){
	/// <summary>获取或者设置与指北针控件关联的MapControl控件。</summary>
   	/// <returns type="SuperMap.UI.MapControl">get_mapControl()返回值类型为&lt;see cref="T:SuperMap.UI.MapControl"&gt;MapControl&lt;/see&gt;。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._mapControl;
        
    },
    
    set_mapControl:function(mapControl){
        var e = Function._validateParams(arguments, [
            {name: "mapControl", type: SuperMap.UI.MapControl}
        ]);
        if (e) throw e;

        if(!this._mapControl){
            this._mapControl = mapControl;
            return true;
        }else{
            return false;
        }
    },
    
    get_params:function(){
	/// <summary>获取或者设置指北针控件的参数。</summary>
   	/// <returns type="Object">get_params()返回值类型为Object。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._params;
    },
    
    set_params:function(params){
        var e = Function._validateParams(arguments, [
            {name: "params", type: Object, mayBeNull: true}
        ]);
        if (e) throw e;

        this._params = params;
        if(!params){
            this._params = new Object();
        }

        if(this._params.backgroundUrl){
            this._backgroundUrl = this._params.backgroundUrl;
        }else{
            this._params.backgroundUrl = this._backgroundUrl = "../../images/supGis/navigation.jpg";
        }
        if(this._params.navigationRate){
	        this._navigationRate = this._params.navigationRate;
        }else{
	        this._params.navigationRate = this._navigationRate = 5;
        }
        if(this._params.navigationInterval){
	        this._navigationInterval = this._params.navigationInterval;
        }else{
	        this._params.navigationInterval = this._navigationInterval = 10;
        }
        if(this._params.width){
	        this._width = this._params.width;
        }else{
	        this._width = this._container.style.width.replace("px", "");
	        this._width = this._width.replace("pt", "");
	        this._params.width = this._width;
        }
        if(this._params.height){
	        this._height = this._params.height;
        }else{
	        this._height = this._container.style.height.replace("px", "");
	        this._height = this._height.replace("pt", "");
	        this._params.height = this._height;
        }
    },

    get_backgroundUrl:function(){
	/// <summary>获取或者设置指北针控件的背景图片的地址。</summary>
   	/// <returns type="String">get_backgroundUrl()返回值类型为String。</returns>
        return this._backgroundUrl;
    },
    
    set_backgroundUrl:function(value) {
        this._backgroundUrl = value;
    },
    
    get_navigationRate:function(){
	/// <summary>获取或者设置通过指北针控件移动地图的速度，单位为像素/毫秒 。&lt;br&gt;
	/// 当按住指北针控件的某方向标来匀速移动地图的时候，通过该属性控制地图移动的速度。</summary>
   	/// <returns type="Number">get_navigationRate()返回值类型为Number。</returns>
        return this._navigationRate;
    },
    
    set_navigationRate: function(value) {
        this._navigationRate = value;
    },
    
    get_navigationInterval:function(){
	/// <summary>获取或者设置地图移动的时间间隔。&lt;br&gt;
	/// 当按住指北针控件的某方向标来匀速移动地图的时候，
	/// 指北针控件会根据这个时间间隔不断的向服务器请求地图数据。单位为毫秒 。</summary>
   	/// <returns type="Number">get_navigationInterval()返回值类型为Number。</returns>
        return this._navigationInterval;
    },
    
    set_navigationInterval:function(value) {
        this._navigationInterval = value;
    },
    
    get_width:function(){
	/// <summary>获取或者设置指北针控件的宽度。</summary>
   	/// <returns type="Number">get_width()返回值类型为Number。</returns>
        return this._width;
    },
    
    set_width: function(value) {
        this._width = value;
    },
    
    get_height:function(){
	/// <summary>获取或者设置指北针控件的高度。</summary>
   	/// <returns type="Number">get_height()返回值类型为Number。</returns>
        return this._height;
    },
    
    set_height: function(value) {
        this._height = value;
    },
    
	initialize:function(){
	/// <summary>初始化指北针控件。</summary>
	    this.update();
		this._container.onmouseover = this._moveMapDelegate;
		this._container.onmousedown = this._startNavigateDelegate;
		this._container.onmousemove = this._countNavigateDelegate;
		this._container.onmouseup = this._endNavigateDelegate;
		this._container.onmouseout = this._endNavigateDelegate;
	},
	
	dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
	    if(this._container){
		    this._container.onmouseover = null;
		    this._container.onmousedown = null;
		    this._container.onmousemove = null;
		    this._container.onmouseup = null;
		    this._container.onmouseout = null;
            this._container.innerHTML = "";
	        this._mapControl = null;
	        //this._container = null;
	    }

        this._id = null;
        this._angle = null;
	    this._image = null;
	    this._useNavigation = null;
        this._angle = null;
        this._map = null;
        this._image = null;
        this._backgroundUrl = null;
	    this._navigationRate = null;
	    this._navigationInterval = null;
	    this._width = null;
	    this._height = null;

		delete this._moveMapDelegate;
		delete this._startNavigateDelegate;
		delete this._countNavigateDelegate;
		delete this._endNavigateDelegate;
		delete this._endNavigateDelegate;
	    
	    SuperMap.UI.NavigationControl.callBaseMethod(this, 'dispose');
	},
		
	_startNavigate:function(e){
		this._useNavigation = true;
		this._countNavigate(e);
	},
	
	_countNavigate:function(e){
		if(!this._useNavigation){
		    return;
		}
		this._useNavigation = false;
		var event = SuperMap.Utility.getEvent(e);
		var img = $get(this._id);
		var centerX = img.offsetWidth / 2;
		var centerY = img.offsetHeight / 2;
		var x;
		var y;
		if(_GetBrowser() == "ie"){
			x = event.offsetX - centerX;
			y = centerY - event.offsetY;
		} else {
			var left = SuperMap.Utility.getElementX(this._container);
			var top = SuperMap.Utility.getElementY(this._container);
			x = event.clientX - left - centerX ;
			y = centerY - (event.clientY - top);
		}
	    this._angle = Math.atan(y / x) * 180 / Math.PI;
		if(x < 0){
		    this._angle += 180;
		}
		this._useNavigation = true;
		if(this._debug){
    		window.status = "x:" + x + " y:" + y + "," + this._angle;
		}

		return false;
	},
	
	_endNavigate:function(e){
		this._useNavigation = false;
		this._mapControl.stopDynamicPan();
	},
	
	_moveMap:function(){
		//先计算角度
		if(!this._useNavigation){
			//没有按下鼠标，一会后再来查看
			if(this._moveMapDelegate && this._navigationInterval != null) {
				this._iTimeoutIDForMoveMap = window.setTimeout(this._moveMapDelegate, this._navigationInterval);
			}
			return;
		}
		//算出来的角度是-90---270
		//暂时分为8个方向吧:正东,东北,正北,西北,正西,西南,正南,东南,
		if(this._angle>=-30&&this._angle<30){
			//正东
			this._mapControl.pan(this._navigationRate,0);
		}else if(this._angle >= 30 && this._angle < 60){
			//东北
			this._mapControl.pan(this._navigationRate, -1 * this._navigationRate);
		}else if(this._angle >=60 && this._angle < 120){
			//正北
			this._mapControl.pan(0, -1 * this._navigationRate);
		}else if(this._angle >= 120 && this._angle < 150){
			//西北
			this._mapControl.pan(-1 * this._navigationRate, -1 * this._navigationRate);
		}else if(this._angle >= 150 && this._angle < 210){
			//正西
			this._mapControl.pan(-1 * this._navigationRate, 0);
		}else if(this._angle >= 210 && this._angle < 240){
			//西南
			this._mapControl.pan(-1 * this._navigationRate, this._navigationRate);
		}else if(this._angle >= 240 && this._angle <= 270 || this._angle < -60 && this._angle >= -90){
			//正南
			this._mapControl.pan(0, this._navigationRate);
		}else if(this._angle >= -60 && this._angle < -30){
			//东南
			this._mapControl.pan(this._navigationRate, this._navigationRate);
		}
		
		if(this._useNavigation){
			this._iTimeoutIDForMoveMap = window.setTimeout(this._moveMapDelegate, this._navigationInterval);
		}
		return false;
	},
	
	_setPropertyHidden:function(){
        var o = new Object();
        o.backgroundImageUrl = this._backgroundImageUrl;
        o._navigationInterval = this._navigationInterval;
        o._navigationRate = this._navigationRate;
        o._width = this._width;
	    o._height = this._height;
        var propertyJSON = SuperMap.Utility.toJSON(o);
        var hidden = $get(this._container.id + "_hiddenProperty");
        if(hidden){
            hidden.value = propertyJSON;
        }
    },
    
    update:function(){
	/// <summary>更新指北针控件的客户端呈现。</summary>
        //this._initProperty();
        if(!this._image){
            this._image = new Image();
        }
        if(this._image.parentNode){
            this._image.parentNode.removeChild(this._image);
        }
        this._image.src = this._backgroundUrl;
        this._image.style.width = this._width + "px";
        this._image.style.height = this._height + "px";
        this._container.appendChild(this._image);
		this._setPropertyHidden();
    }
}

SuperMap.UI.NavigationControl.registerClass('SuperMap.UI.NavigationControl', Sys.UI.Control);

// Since this script is not loaded by System.Web.Handlers.ScriptResourceHandler
// invoke Sys.Application.notifyScriptLoaded to notify ScriptManager 
// that this is the end of the script.
if (typeof(Sys) !== 'undefined') Sys.Application.notifyScriptLoaded();