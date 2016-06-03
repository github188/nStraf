//========================================================================== 
// SuperMap iServer 客户端程序。版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.OverviewControl.js 
// 功能：			Ajax OverviewControl 类 
// 最后修改时间：	2007-10-30
//========================================================================== 

Type.registerNamespace('SuperMap.UI');

SuperMap.UI.OverviewControl = function(container){
    /// <summary>鹰眼控件。需要与MapControl配合使用。 鹰眼窗口显示与MapControl中相同的地图数据。&lt;br&gt;
	/// 默认情况下，鹰眼中的地图为全幅范围的地图。而且这个范围如果没有特殊需要不会改变。 
	/// </summary>
    /// <param name="container" type="Object" domElement="true">装载鹰眼控件的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.OverviewControl">返回 OverviewControl 对象。</returns>
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
    //SuperMap.UI.OverviewControl.initializeBase(this,[container]);
    
    this._container = container;
	this._container.style.overflow = "hidden";
    this._map = null;	// 对应的MapControl
    this._mapName = "";	// 地图名称 
    this._viewBounds = null;	// 鹰眼范围
    this._indexBounds = null;	//indexBox的范围 
    this._overviewUrl = "";	//鹰眼图片地址 
    //var _eventsList=new Array();
    //var _eventsNameList=new Array();
    this._indexBox = null;
    this._lastMapName = "";
	this._borderWidth = 2;
	
    this._width = parseInt(container.style.width) - this._borderWidth * 2;
    this._height = parseInt(container.style.height) - this._borderWidth * 2;

    //为避免鹰眼被放在可移动窗口时，每次用时都取一次，这里就不取了
    this._left = 0;
    this._top = 0;
    
    this._leftForIndexBox=0;
    this._topForIndexBox=0;
    this._widthForIndexBox=0;
    this._heightForIndexBox=0;
    this._originX=0;
    this._originY=0;
    this._offsetX=0;
    this._offsetY=0;
    this._bMouseDown=false;
    this._inited=false;
    this._triggeredEvent=false;// 是否已经触发IndexboxChanged事件
    this._url="";
    // 从服务器端传回的部分属性
    this._indexBoxBorderColor="red";     //indexBox边框颜色
    this._indexBoxBorderStyle="solid";     //indexBox边框样式
    this._indexBoxBorderWidth="2px";     //indexBox边框宽度
    
    //this._res=SuperMap.UI.OverviewControl.Res;
    
    this._isHidden = false;
	// 获取鹰眼出错尝试次数，最多三次
    this._tryTimes = 0;
  	this._onGetOverviewCompleteDelegate = Function.createDelegate(this, this._onGetOverviewComplete);
	this._onGetOverviewErrorDelegate = Function.createDelegate(this, this._onGetOverviewError);
    this._onMouseDownDelegate = Function.createDelegate(this,this._onMouseDown);
    this._onMouseMoveDelegate = Function.createDelegate(this,this._onMouseMove);
    this._onMouseUpDelegate = Function.createDelegate(this,this._onMouseUp);
    this._onChangeOverviewDelegate = Function.createDelegate(this,this._changeOverview);
	
	this._useQuickCache = false;
};

SuperMap.UI.OverviewControl.prototype = {
    get_map:function(){
	/// <summary> 获取或者设置与鹰眼相关联的MapControl对象。
	/// </summary>
   	/// <returns type="SuperMap.UI.MapControl">get_map()返回值类型为&lt;see cref="T:SuperMap.UI.MapControl"&gt;MapControl&lt;/see&gt;。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._map;
    },
    set_map:function(value){
        var e = Function._validateParams(arguments, [{name: "value", type: SuperMap.UI.MapControl}]); 
        if (e) throw e;
        if (this._map){ throw Error.invalidOperation(SuperMap.Resource.ins.getMessage("cantSetMapTwice"));}
        this._map = value;
    },
        
    get_param:function(){
	/// <summary> 获取或者设置鹰眼控件相关的参数，如索引框边线颜色，风格，宽度等。
	/// </summary>
   	/// <returns type="Object">get_params()返回值类型为Object。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._param;
    },
    set_param:function(value){
        var e = Function._validateParams(arguments, [
            {name: "value", type: Object,mayBeNull: true}
        ]);
        if (e) throw e;
        this._param = value;
        if(this._param){
            this.set_indexBoxBorderColor(this._param.indexBoxBorderColor);     //indexBox边框颜色
            this.set_indexBoxBorderStyle(this._param.indexBoxBorderStyle);     //indexBox边框样式
            this.set_indexBoxBorderWidth(this._param.indexBoxBorderWidth);     //indexBox边框宽度
        }
    },

    get_mapName:function(){
	/// <summary> 获取或者设置鹰眼控件的地图名。
	/// </summary>
   	/// <returns type="String">get_mapName()返回值类型为String。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._mapName;
    },
    set_mapName:function(mapName){
        var e = Function._validateParams(arguments, [
            {name: "value", type: String, mayBeNull: true}
        ]);
        if (e) throw e;
        if(mapName){
            this._mapName = mapName;
        }
    },
    
    get_url:function(){
	/// <summary> 鹰眼图片的URL。 
	/// </summary>
   	/// <returns type="String">get_url()返回值类型为String。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._url;
    },
    
    get_indexBoxBorderColor:function(){
	/// <summary> 获取或者设置鹰眼索引框的边框颜色。 
	/// </summary>
   	/// <returns type="String">get_indexBoxBorderColor()返回值类型为String。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._indexBoxBorderColor;
    },
    set_indexBoxBorderColor:function(value){
        if(value){this._indexBoxBorderColor = value;}
    },
    
    get_indexBoxBorderStyle:function(){
	/// <summary> 获取或者设置鹰眼索引框的边框样式。
	/// </summary>
   	/// <returns type="String">get_indexBoxBorderStyle()返回值类型为String。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._indexBoxBorderStyle;
    },
    set_indexBoxBorderStyle:function(value){
        if(value){this._indexBoxBorderStyle = value;}
    },
    
    get_indexBoxBorderWidth:function(){
	/// <summary> 获取或者设置鹰眼索引框的边框宽度。
	/// </summary>
   	/// <returns type="Number">get_indexBoxBorderWidth()返回值类型为Number。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._indexBoxBorderWidth;
    },
    set_indexBoxBorderWidth:function(value){
        if(value){this._indexBoxBorderWidth = value;}
    },

    get_isHidden:function(){
	/// <summary> 获取或者设置鹰眼当前是否处于可见状态。
	/// </summary>
   	/// <returns type="Boolean">get_isHidden()返回值类型为Boolean。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._isHidden;
    },

    set_isHidden:function(isHidden){
        this._isHidden = isHidden;
    },

    initialize: function() {
	/// <summary> 初始化鹰眼控件。
	/// </summary>
        this._inited=true;
	    this._lastMapName = this._map.mapName;
	    if(!this._mapName){
	        this._mapName=this._map.mapName;
	    }
	    this._viewBounds = null;// 初始化的时候传空，获取全幅的图片
        this._indexBounds = this._map.getViewBounds();
        //todo:先创建indexBox等dom元素.
	    //获取鹰眼图
	    var overviewManager = this._map.getOverviewManager(this._mapName);
		if(!this._useQuickCache) {
			overviewManager.getOverview(this._width, this._height, this._viewBounds, this._onGetOverviewCompleteDelegate, this._onGetOverviewErrorDelegate);
		}
		else if(this._useQuickCache) {
			this._onGetOverviewComplete();
		}
	    
	    //InitEvent
	    var hidden=document.getElementById(this._container.id+"_hiddenClientEvents");
        if(hidden && hidden.value){
            var strEvents=hidden.value;
            var eventsInfo=strEvents.split(';');
            for(var i=0; i<eventsInfo.length; i++){
                var eventInfo=eventsInfo[i].split(':');
                if(eventInfo && eventInfo.length==2){
                    var eventName=eventInfo[0];
                    var eventListners=eventInfo[1].split(',');
                    for(var j=0;j<eventListners.length;j++){
                        try{
                            //toremove:_AttachEvent(eventName,eval(eventListners[j]));
                            this.get_events().addHandler(eventName, eval(eventListners[j]));
                        }
                        catch(e){}
                    }
                }
            }
        }
        
        SuperMap.UI.OverviewControl.callBaseMethod(this, 'initialize');
    },
    
    dispose:function(){
	/// <summary> 释放对象占用的资源。
	/// </summary>
        //todo: 没有attach,干吗detach.
        //todo: if(this._map){this._map.get_events().removeHandler("oninit",_Init);this._map.removeHandler("onchangeview",_ChangeOverview);this._map.get_events().removeHandler("ondestroying",_Destroy);}
        if(this._map){
      	    this._map.remove_changeView(this._onChangeOverviewDelegate);
//            this._map.DetachEvent("oninit",this._initialize);
//            this._map.DetachEvent("ondestroying",this._dispose);
        }
        try{
            $removeHandler(this._container, "mousedown", this._onMouseDownDelegate);
            $removeHandler(this._container, "mousemove", this._onMouseMoveDelegate);
            $removeHandler(this._container, "mouseup", this._onMouseUpDelegate);
        }catch(e){
        }
	    //用this._container.innerHTML不易出错,removeChild易报错
	    this._container.innerHTML="";
	    //this._container.removeChild(this._indexBox);
	    this._map=null;
	    this._container=null;
	    this._viewBounds=null;
	    this._indexBounds=null;
	    this._indexBox=null;
	    
	    delete this._onGetOverviewCompleteDelegate;
	    delete this._onGetOverviewErrorDelegate;
	    delete this._onMouseDownDelegate;
	    delete this._onMouseMoveDelegate;
	    delete this._onMouseUpDelegate;
	    delete this._onChangeOverviewDelegate;
	    
	    SuperMap.UI.OverviewControl.callBaseMethod(this, 'dispose');
    },
    
    show:function(){
	/// <summary> 显示鹰眼控件。
	/// </summary>
	    this._container.style.visibility="visible";
	    this._indexBox.style.visibility = "visible";
	    this._isHidden = false;
    },

    hide:function(){
	/// <summary> 隐藏鹰眼控件。
	/// </summary>
	    this._container.style.visibility="hidden";
	    this._indexBox.style.visibility = "hidden";
	    this._isHidden = true;
    },
    
    getViewBounds:function(){
	/// <summary> 获取鹰眼当前的可见范围。
	/// </summary>
   	/// <returns type="SuperMap.Rect2D">getViewBounds()返回值类型为&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;。</returns>
        return this._viewBounds;
    },
    
    setViewBounds:function(viewBounds){
	/// <summary> 设置鹰眼当前的可见范围。
	/// </summary>
    /// <param name="viewBounds" type="SuperMap.Rect2D">鹰眼的可见地理范围。</param>
        this._viewBounds = viewBounds;
	    //获取鹰眼图
	    var overviewManager = this._map.getOverviewManager(this._mapName);
	    overviewManager.getOverview(this._width, this._height, this._viewBounds, this._onGetOverviewCompleteDelegate, this._onGetOverviewErrorDelegate);
    },

    _changeOverview:function(){
        if(!this._triggeredEvent){
            this._originX=this._indexBox.style.left;
	        this._originY=this._indexBox.style.top;
        }
        
        // 重新获取 this._viewBounds,不获取的话当地图切换时就会不正确了.
        if(this._lastMapName!=this._map.mapName){
             this._viewBounds = this._getValidBounds(this._map.getMapBounds());
             var overviewManager = this._map.getOverviewManager(this._mapName);
             
             overviewManager.set_mapName(this._map.mapName);// 一定要改变manager的地图名，要么就在生成url请求的时候能传入mapName
             this._lastMapName = this._map.mapName;
             
             overviewManager.getOverview(this._width,this._height,this._viewBounds, this._onGetOverviewCompleteDelegate,this._onGetOverviewErrorDelegate);
        }
	    // 重新获取this._indexBounds
        this._indexBounds=this._map.getViewBounds();
	    // 计算IndexBox的大小和位置
	    this._computeIndexBox();
	    this._drawIndexBox(true);
	    this._triggeredEvent=false;
    },

    _computeIndexBox:function(){
        //根据this._viewBounds和_indexBound和this._container的大小来计算
	    //this._leftFoxIndexBox,this._topForIndexBox,this._widthForIndexBox,this._heightForIndexBox
	    this._indexBounds = this._map.getViewBounds();// 重新取一次，防出错
	    var leftRatio=(this._indexBounds.leftBottom.x-this._viewBounds.leftBottom.x)/(this._viewBounds.rightTop.x-this._viewBounds.leftBottom.x);
	    this._leftForIndexBox=this._width*leftRatio;
	    var topRatio=(this._viewBounds.rightTop.y-this._indexBounds.rightTop.y)/(this._viewBounds.rightTop.y-this._viewBounds.leftBottom.y);
	    this._topForIndexBox=this._height*topRatio;
	    var widthRatio =(this._indexBounds.rightTop.x-this._indexBounds.leftBottom.x)/(this._viewBounds.rightTop.x-this._viewBounds.leftBottom.x);
	    this._widthForIndexBox =this._width*widthRatio;
	    this._indexBox.realWidth=this._widthForIndexBox;
	    if(this._widthForIndexBox < 5){this._widthForIndexBox=5;}
	    var heightRatio =(this._indexBounds.rightTop.y-this._indexBounds.leftBottom.y)/(this._viewBounds.rightTop.y-this._viewBounds.leftBottom.y);
	    this._heightForIndexBox = this._height*heightRatio;
	    this._indexBox.realHeight=this._heightForIndexBox;
	    if(this._heightForIndexBox < 5){this._heightForIndexBox =5;}
	    if(this._leftForIndexBox<0){
		    this._widthForIndexBox = this._widthForIndexBox+this._leftForIndexBox;
		    this._leftForIndexBox=0;
	    }
	    if(this._leftForIndexBox+this._widthForIndexBox>this._width){
		    this._widthForIndexBox = this._width-this._leftForIndexBox;
	    }
	    if(this._topForIndexBox<0){
		    this._heightForIndexBox = this._heightForIndexBox+this._topForIndexBox;		
		    this._topForIndexBox=0;
	    }
	    if(this._topForIndexBox+this._heightForIndexBox>this._height){
		    this._heightForIndexBox = this._height-this._topForIndexBox;
	    }
    },

    _drawIndexBox:function (isTrigger){
		with(this._indexBox.style)
	    {
			if(this._widthForIndexBox) {
				width =this._widthForIndexBox+"px";
			}
			if(this._heightForIndexBox) {
				height = this._heightForIndexBox+"px";
			}
			if(this._leftForIndexBox || this._leftForIndexBox == 0) {
				left=this._leftForIndexBox+"px";
			}
			if(this._topForIndexBox || this._topForIndexBox == 0) {
				top=this._topForIndexBox+"px";
			}
	    }
	    if(!this._triggeredEvent&&isTrigger){
	        this._offsetX=parseInt(this._indexBox.style.left)-parseInt(this._originX);
	        this._offsetY=parseInt(this._indexBox.style.top)-parseInt(this._originY);
	        if(this._offsetX==0||this._offsetY==0){return;}
	        var mapSize = this._map.getSize();
	        var ratioX = mapSize.width() / this._indexBox.realWidth;
	        var ratioY = mapSize.height() / this._indexBox.realHeight;
	        this._offsetX = this._offsetX * ratioX;
	        this._offsetY = this._offsetY * ratioY;
	        this._triggeredEvent=true;
	        //_TriggerEvent("indexboxChanged",this._offset,"IndexboxChanged"); 
	        var handler = this.get_events().getHandler("indexBoxChanged");
            if (handler) {
                handler(this, Sys.EventArgs.Empty);
            }
	    }
	    //SetPropertyHidden;
	    var o=new Object();
        o.viewBounds=this._viewBounds;
        o.indexBounds=this._indexBounds;
        var propertyJSON=SuperMap.Utility.toJSON(o);
        var hidden=$get(this._container.id+"_hiddenProperty");
        if(hidden){
            hidden.value=propertyJSON;
        }
    },

    _onMouseDown:function(e)
    {
	    this._bMouseDown = true;
	    // 记下当前索引框的左上角位置。
	    this._originX = this._leftForIndexBox;
	    this._originY = this._topForIndexBox;
	    this._left=SuperMap.Utility.getElementX(this._container);
	    this._top=SuperMap.Utility.getElementY(this._container);
	    e=SuperMap.Utility.getEvent(e);
        var mouseX=SuperMap.Utility.getMouseX(e);var mouseY=SuperMap.Utility.getMouseY(e);
	    this._leftForIndexBox = mouseX-this._left-this._widthForIndexBox/2;
	    this._topForIndexBox = mouseY-this._top-this._heightForIndexBox/2;
	    this._drawIndexBox(false);
	    SuperMap.Utility.cancelBubble(e);
    },
    
    _onMouseMove:function(e){
	    if(!this._bMouseDown){return;}
	    this._left=SuperMap.Utility.getElementX(this._container);
	    this._top=SuperMap.Utility.getElementY(this._container);
	    e=SuperMap.Utility.getEvent(e);
        var mouseX=SuperMap.Utility.getMouseX(e);var mouseY=SuperMap.Utility.getMouseY(e);
	    this._leftForIndexBox = mouseX-this._left-this._widthForIndexBox/2;
	    this._topForIndexBox = mouseY-this._top-this._heightForIndexBox/2; 
	    this._drawIndexBox(false);
	    SuperMap.Utility.cancelBubble(SuperMap.Utility.getEvent(e));
	    return false;
    },

    _onMouseUp:function(e){
	    if(!this._bMouseDown){return;}
	    this._bMouseDown=false;
	    var leftX = this._viewBounds.leftBottom.x;
		var topY = this._viewBounds.rightTop.y;
		var ms=this._map.get_mapScale();
		// 计算左上角起点到中心点的距离
	    var distanceX = this._viewBounds.width()/ this._width * (this._leftForIndexBox + this._indexBox.realWidth/2);
		var distanceY = this._viewBounds.height()/ this._height * (this._topForIndexBox + this._indexBox.realHeight/2);
		this._map.setCenterAndZoom(new SuperMap.Point2D(leftX + distanceX, topY - distanceY), this._map.get_mapScale());
	    this._drawIndexBox(true);
	    this._offsetX=0;
	    this._offsetY=0;
	    SuperMap.Utility.cancelBubble(SuperMap.Utility.getEvent(e));
	    return false;
    },
    
    _onGetOverviewComplete:function(overviewJ){
		if(!overviewJ) {
			this._tryTimes++;
			if(this._tryTimes > 3) {
				this._tryTimes = 0;
				//throw Error.create(this._res.getOverivewError+"!");
				throw Error.create(SuperMap.Resource.ins.getMessage("getOverivewError")+"!");
			} else {
				var overviewManager = this._map.getOverviewManager(this._mapName);
				overviewManager.getOverview(this._width, this._height, this._viewBounds, this._onGetOverviewCompleteDelegate, this._onGetOverviewErrorDelegate);
				return;
			}
		}
		
		if(!this._useQuickCache) {
			var overview = new SuperMap.Overview();
			SuperMap.Decoder.fromJson(overview, overviewJ);
			this._url = overview.url;
			this._container.style.backgroundImage = "url(" + overview.url + ")";
			this._viewBounds = overview.viewBounds;
		}
		else if(this._useQuickCache) {
			alert(this._map._params.outputContextPath);
			var width = this._width;
			var high = this._height;
			var imageUrl = this._map._params.outputContextPath + "cache/overview/" + this._mapName + "_" + width + "x" + high + ".png";
			alert(imageUrl);
			this._url = imageUrl;
			this._container.style.backgroundImage = "url(" + imageUrl + ")";
			this._viewBounds = this._map.getMap().getMapBounds();//_referViewBounds
		}
        
	    this._container.style.backgroundRepeat = "no-repeat";
    	
	    // 计算IndexBox的大小和位置
	    //如果是切换图的话可能已有这个indexBox了
	    this._indexBox = $get(this._container.id + "_IndexBox");
	    if(!this._indexBox){
	        this._indexBox = document.createElement("div");
	        this._indexBox.id = this._container.id + "_IndexBox";
    	    this._container.appendChild(this._indexBox);
	    }
        this._indexBox.style.borderColor = this._indexBoxBorderColor;
        this._indexBox.style.borderStyle = this._indexBoxBorderStyle;
        this._indexBox.style.borderWidth = this._indexBoxBorderWidth;
    	
	    //计算IndexBox的大小和位置
	    this._computeIndexBox();
	    with(this._indexBox.style){
			if(this._widthForIndexBox || this._widthForIndexBox == 0) {
				width = this._widthForIndexBox + "px";
			}
			if(this._heightForIndexBox || this._heightForIndexBox == 0) {
				height = this._heightForIndexBox + "px";
			}
		    position = "relative";
		    visibility = "visible";
			if(this._leftForIndexBox || this._leftForIndexBox == 0) {
				left = this._leftForIndexBox + "px";
			}
			if(this._topForIndexBox || this._topForIndexBox == 0) {
				top = this._topForIndexBox + "px";
			}
		    zIndex = 1;
		    fontSize = "1px";
        }
        
        $addHandlers(this._container, {mousedown:this._onMouseDownDelegate, mousemove:this._onMouseMoveDelegate, mouseup:this._onMouseUpDelegate});

	    this._map.add_changeView(this._onChangeOverviewDelegate);
    },
    
    _onGetOverviewError:function(responseText) {
		this._tryTimes++;
		if(this._tryTimes > 3) {
			this._tryTimes = 0;
			//if(responseText){throw Error.create(this._res.getOverivewError+":"+responseText);}else{throw Error.create(this._res.getOverivewError+"!");}
			if(responseText){throw Error.create(SuperMap.Resource.ins.getMessage("getOverivewError")+":"+responseText);}else{throw Error.create(SuperMap.Resource.ins.getMessage("getOverivewError")+"!");}
		} else {
			var overviewManager = this._map.getOverviewManager(this._mapName);
			overviewManager.getOverview(this._width, this._height, this._viewBounds, this._onGetOverviewCompleteDelegate, this._onGetOverviewErrorDelegate);
		}
    },
	
	_getValidBounds:function(mapRect) {
		var width = this._width;
		var height = this._height;
		if(mapRect && mapRect != null) {
			var bounds = new SuperMap.Rect2D(mapRect.leftBottom.x, mapRect.leftBottom.y, mapRect.rightTop.x, mapRect.rightTop.y);
			var boundWidth = mapRect.width();
			var boundHeight = mapRect.height();
			if(boundWidth / boundHeight > width / height) {
				boundHeight = boundWidth * height / width;
				bounds.leftBottom.y = bounds.rightTop.y - boundHeight;
			} else {
				boundWidth = boundHeight * width / height;
				bounds.rightTop.x = bounds.leftBottom.x + boundWidth;
			}
			return bounds;
		} else {
			return null;
		}
	}
}

SuperMap.UI.OverviewControl.registerClass('SuperMap.UI.OverviewControl', Sys.UI.Control);

SuperMap.UI.OverviewControl.Res = {
'getOverivewError': '获取鹰眼图出错',
'cantSetMapTwice': 'Map属性只能设置一次'
};