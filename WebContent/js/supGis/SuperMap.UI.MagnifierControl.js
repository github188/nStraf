//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2007。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.MagnifierControl.js 
// 功能：			Ajax MagnifierControl 类 
// 最后修改时间：	2007-11-02
//========================================================================== 

Type.registerNamespace("SuperMap.UI");

SuperMap.UI.MagnifierControl = function(container){
	/// <summary>放大镜控件。该控件可以在地图窗口上自由移动，并将其所在的位置在放大镜控件中以指定的放大倍率进行放大显示。
	/// </summary>
    /// <param name="container" type="Object" domElement="true">装载放大镜控件的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.MagnifierControl">返回 MagnifierControl 对象。</returns>
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
//    SuperMap.UI.MagnifierControl.initializeBase(this, [container]);
    
    this._container = container;
    this._mapControl = null;
    this._params = null;

    this._width = 0;
    this._height = 0;
    this._headHeight = 0;
    this._headBackColor = "";
    this._minZoomRadio = 0;
    this._maxZoomRadio = 0;
    this._closeButton = true;
    this._closeButtonImg = "";
    this._draggable = true;
    this._expandCollapseButton = true;
    this._expandButtonImg = "";
    this._collapseButtonImg = "";
    this._expanded = true;
    this._title = "";
    this._headFont = null;
    this._headBackgroundImage = "";
    this._headSeparatorLine = true;
    
    this._magnifier = null;
    this._magnifierManager = null;//this._map.GetMagnifierManager();
    this._mapContainer = null;// new完了之后再赋值
    this._startMoving = false;
    this._leftForMagnifier = null;
    this._topForMagnifier = null;
    this._leftForMapContainer = null;
    this._topForMapContainer = null;
    this._magnifierBody = null;
    this._centerPix = null;
    this._curMapScale = null
    this._oldleft = null;
    this._oldtop = null;
    this._hidden = false;
    this._mapInMagnifier = null;
    this._vertCross = null;
    this._horizCross = null;
    this._lastMapName = null;
    this._oldMousePositionX = null;
    this._oldMousePositionY = null;
    
    this._mouseDownDelegate = Function.createDelegate(this, this._mouseDown);
    this._mouseMoveDelegate = Function.createDelegate(this, this._mouseMove);
    this._mouseUpDelegate = Function.createDelegate(this, this._mouseUp);
    this._onClickDelegate = Function.createDelegate(this, this._onClick);
    this._onDblClickDelegate = Function.createDelegate(this, this._onDblClick);
    this._getMapDelegate = Function.createDelegate(this, this.getMap);
    this._collapseDelegate = Function.createDelegate(this, this._collapse);
    this._expandDelegate = Function.createDelegate(this, this._expand);
    this._closeDelegate = Function.createDelegate(this, this.close);
    
    this._onGetMapCompeletDelegate = Function.createDelegate(this, this._onGetMapCompelet);
    this._onGetMapErrorDelegate = Function.createDelegate(this, this._onGetMapError);
}

SuperMap.UI.MagnifierControl.prototype={
    
    get_mapControl:function(){
	/// <summary>获取或者设置与放大镜控件关联的MapControl控件。</summary>
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
            this._magnifierManager = this._mapControl.getMagnifierManager();
            this._mapContainer = this._mapControl.get_container();

            return true;
        }else{
            return false;
        }
    },
    
    get_params:function(){
	/// <summary>获取或者设置放大镜控件的参数。</summary>
   	/// <returns type="Object">get_params()返回值类型为Object。</returns>
        if (arguments.length !== 0) throw Error.parameterCount();
        return this._params;
    },

    set_params:function(params){
        var e = Function._validateParams(arguments, [
            {name: "params", type: Object,mayBeNull: true}
        ]);
        if (e) throw e;

        this._params = params;
        if(!params){
            this._params = new Object();
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
        if(this._params.headHeight){
            this._headHeight = this._params.headHeight;
        }else{
            this._params.headHeight = this._headHeight = "20";
        }
        if(this._params.headBackColor){
	        this._headBackColor = this._params.headBackColor;
        }else{
	        this._params.headBackColor = this._headBackColor = "";
        }
        if(this._params.minZoomRadio){
	        this._minZoomRadio = this._params.minZoomRadio;
        }else{
	        this._params.minZoomRadio = this._minZoomRadio = 2;
        }
        if(this._params.maxZoomRadio){
	        this._maxZoomRadio = this._params.maxZoomRadio;
        }else{
	        this._params.maxZoomRadio = this._maxZoomRadio = 5;
        }
        if(this._params.closeButton){
	        this._closeButton = this._params.closeButton;
        }else{
	        this._params.closeButton = this._closeButton = true;
        }
        if(this._params.closeButtonImg){
	        this._closeButtonImg = this._params.closeButtonImg;
        }else{
	        this._params.closeButtonImg = this._closeButtonImg = "../../images/supGis/close.gif";
        }
        if(this._params.draggable){
	        this._draggable = this._params.draggable;
        }else{
	        this._params.draggable = this._draggable = true;
        }
        if(this._params.expandCollapseButton){
	        this._expandCollapseButton = this._params.expandCollapseButton;
        }else{
	        this._params.expandCollapseButton = this._expandCollapseButton = true;
        }
        if(this._params.expandButtonImg){
	        this._expandButtonImg = this._params.expandButtonImg;
        }else{
	        this._params.expandButtonImg = this._expandButtonImg = "../../images/supGis/collapse.gif";
        }
        if(this._params.collapseButtonImg){
	        this._collapseButtonImg = this._params.collapseButtonImg;
        }else{
	        this._params.collapseButtonImg = this._collapseButtonImg = "../../images/supGis/expand.gif";
        }
        if(this._params.expanded){
	        this._expanded = this._params.expanded;
        }else{
	        this._params.expanded = this._expanded = false;
        }
        if(this._params.title){
	        this._title = this._params.title;
        }else{
	        this._params.title = this._title = " Magnifier ";
        }
        if(this._params.headFont){
	        this._headFont = eval('(' + this._param.headFont + ')');
        }else{
	        this._params.headFont = this._headFont = null;
        }
        if(this._params.headBackgroundImage){
	        this._headBackgroundImage = this._params.headBackgroundImage;
        }else{
	        this._params.headBackgroundImage = this._headBackgroundImage = "../../images/supGis/backgroud.bmp";
        }
        if(this._params.headSeparatorLine){
	        this._headSeparatorLine = this._params.headSeparatorLine;
        }else{
	        this._params.headSeparatorLine = this._headSeparatorLine = true;
        }
    },
    
    get_width: function() {
	/// <summary>获取或者设置放大镜的宽度。</summary>
   	/// <returns type="Number">get_width()返回值类型为Number。</returns>
        return this._width;
    },
    
    set_width: function(value) {
        this._width = value;
    },
    
    get_height: function() {
	/// <summary>获取或者设置放大镜的高度。</summary>
   	/// <returns type="Number">get_height()返回值类型为Number。</returns>
        return this._height;
    },
    set_height: function(value) {
        this._height= value;
    },
    
    get_headHeight: function() {
	/// <summary>获取或者设置放大镜头部的高度。放大镜的头部一般包括放大倍率的选择框，关闭按钮等。</summary>
   	/// <returns type="Number">get_headHeight()返回值类型为Number。</returns>
        return this._headHeight;
    },
    set_headHeight: function(value) {
        this._headHeight = value;
    },
    
    get_headBackColor: function() {
	/// <summary>获取或者设置放大镜头部的背景色。</summary>
   	/// <returns type="String">get_headBackColor()返回值类型为String。</returns>
        return this._headBackColor;
    },
    set_headBackColor: function(value) {
        this._headBackColor = value;
    },
    
    get_minZoomRadio: function() {
	/// <summary>获取或者设置放大镜最小的放大倍数。</summary>
   	/// <returns type="Number">get_minZoomRadio()返回值类型为Number。</returns>
        return this._minZoomRadio;
    },
    set_minZoomRadio: function(value) {
        this._minZoomRadio = value;
    },
    
    get_maxZoomRadio: function() {
	/// <summary>获取或者设置放大镜最大的放大倍数。</summary>
   	/// <returns type="Number">get_maxZoomRadio()返回值类型为Number。</returns>
        return this._maxZoomRadio;
    },
    set_maxZoomRadio: function(value) {
        this._maxZoomRadio = value;
    },
    
    get_closeButton: function() {
	/// <summary>获取或者设置一个布尔值，该值表明是否在放大镜控件的头部显示关闭按钮。&lt;br&gt;
	/// 如果该值为true，可以通过
	/// closeButtonImg属性来设置关闭按钮的呈现图片。
	/// </summary>
   	/// <returns type="Boolean">get_closeButton()返回值类型为Boolean。</returns>
        return this._closeButton;
    },
    set_closeButton: function(value) {
        this._closeButton = value;
    },
    
    get_closeButtonImg: function() {
	/// <summary>在放大镜控件的头部的关闭按钮的呈现图片的地址。</summary>
   	/// <returns type="String">get_closeButtonImg()返回值类型为String。</returns>
        return this._closeButtonImg;
    },
    set_closeButtonImg: function(value) {
        this._closeButtonImg = value;
    },
    
    get_draggable: function() {
	/// <summary>是否能拖动，true为可以拖动，false为不能拖动。</summary>
   	/// <returns type="Boolean">get_draggable()返回值类型为Boolean。</returns>
        return this._draggable;
    },
    set_draggable: function(value) {
        this._draggable = value;
    },
    
    get_expandCollapseButton: function() {
	/// <summary>是否显示展开/折叠放大镜控件的控制按钮，true为显示，false为不显示，默认为true 。</summary>
   	/// <returns type="Boolean">get_expandCollapseButton()返回值类型为Boolean。</returns>
        return this._expandCollapseButton;
    },
    set_expandCollapseButton: function(value) {
        this._expandCollapseButton = value;
    },
    
    get_expandButtonImg: function() {
	/// <summary>展开按钮图片地址。</summary>
   	/// <returns type="String">get_expandButtonImg()返回值类型为String。</returns>
        return this._expandButtonImg;
    },
    set_expandButtonImg: function(value) {
        this._expandButtonImg = value;
    },
    
    get_collapseButtonImg: function() {
	/// <summary>折叠按钮图片地址。</summary>
   	/// <returns type="String">get_collapseButtonImg()返回值类型为String。</returns>
        return this._collapseButtonImg;
    },
    set_collapseButtonImg: function(value) {
        this._collapseButtonImg = value;
    },
    
    get_expanded: function() {
	/// <summary>加载控件时是否折叠，true为折叠，false为不折叠，默认为true 。</summary>
   	/// <returns type="Boolean">get_expanded()返回值类型为Boolean。</returns>
        return this._expanded;
    },
    set_expanded: function(value) {
        this._expanded = value;
    },
    
    get_title: function() {
	/// <summary>放大镜控件标题行（头部）显示的标题文本。</summary>
   	/// <returns type="String">get_title()返回值类型为String。</returns>
        return this._title;
    },
    set_title: function(value) {
        this._title = value;
    },
    
    get_headFont: function() {
	/// <summary>获取或者设置放大镜控件头部字体风格。</summary>
   	/// <returns type="String">get_headFont()返回值类型为String。</returns>
        return this._headFont;
    },
    set_headFont: function(value) {
        this._headFont = value;
    },
    
    get_headBackgroundImage: function() {
	/// <summary>获取或者设置放大镜控件头部背景图片。 </summary>
   	/// <returns type="String">get_headBackgroundImage()返回值类型为String。</returns>
        return this._headBackgroundImage;
    },
    set_headBackgroundImage: function(value) {
        this._headBackgroundImage = value;
    },
    
    get_headSeparatorLine: function() {
	/// <summary>获取或者设置是否显示放大镜控件的分隔线。布尔值。</summary>
   	/// <returns type="Boolean">get_headSeparatorLine()返回值类型为Boolean。</returns>
        return this._headSeparatorLine;
    },
    set_headSeparatorLine: function(value) {
        this._headSeparatorLine = value;
    },
    
    initialize:function(){
	/// <summary>初始化放大镜控件。</summary>
        this._container.onmousedown = this._mouseDownDelegate;
		this._container.onmousemove = this._mouseMoveDelegate;
		this._container.onmouseup = this._mouseUpDelegate;
		this._container.onclick = this._onClickDelegate;
		this._container.ondblclick = this._onDblClickDelegate;
		
		this._mapContainer.appendChild(this._container);
		this._container.style.display = "block";
		this._container.style.zIndex = this._mapContainer.style.zIndex + 10;
		this._magnifierManager.registerHandler(this._onGetMapCompeletDelegate, this._onGetMapErrorDelegate);
		this._container.style.left = parseFloat(this._mapContainer.style.width) - parseFloat(this._container.style.width) + "px";
	    this._container.style.top = "0px";
	    this._render();
	    this._mapControl.add_changeView(this._getMapDelegate);
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
        this._mapControl.remove_viewChanged(this._getMapDelegate);
        this._magnifier = null;
        this._mapContainer = null;
        this._startMoving = null;
        this._leftForMagnifier = null;
        this._topForMagnifier = null;
        this._leftForMapContainer = null;
        this._topForMapContainer = null;
        this._magnifierManager = null;
        this._magnifierBody = null;
        this._centerPix = null;
        this._curMapScale = null;
        this._oldleft = null;
        this._oldtop = null;
        this._hidden = null;
        this._mapInMagnifier = null;
        this._vertCross = null;
        this._horizCross = null;
        this._lastMapName = null;
        this._oldMousePositionX = null;
        this._oldMousePositionY = null;
        this._container.innerHTML = "";
	    this._container = this._mapControl = null;
    },
    
    getMap:function(){
	/// <summary>根据当前放大镜控件在地图窗口的位置获取放大后的地图。</summary>
	    // 如果是第一次加载控件，不获取地图。
	    if(!this._lastMapName){
	        this._lastMapName = this._mapControl.get_mapName();
	        return;
	    }
	    if(this._lastMapName != this._mapControl.get_mapName()){
	        this._magnifierManager = this._mapControl.getMagnifierManager();
    		this._magnifierManager.registerHandler(this._onGetMapCompeletDelegate, this._onGetMapErrorDelegate);
	    }
	    this._lastMapName = this._mapControl.get_mapName();
        this._centerPix = new SuperMap.Point(this._leftForMagnifier + this._width / 2, this._topForMagnifier + this._height / 2 + this._headHeight / 2);
        this._curMapScale = this._mapControl.get_mapScale();
        var center = this._mapControl.pixelToMapCoord(this._centerPix);
        var viewer = new SuperMap.Rect(0, 0, parseFloat(this._width), parseFloat(this._height - this._headHeight));
        var select = $get(this._container.id + "_ZoomRadio");
        if(!center.x || !center.y || !viewer || !select){
            return;
        }
        var zoomRadio = select.options[select.selectedIndex].value;
        
        this._magnifierManager.getMagnifier(center, viewer, this._curMapScale * zoomRadio);
	},
	
	close:function(e){
	/// <summary>关闭放大镜控件事件。当点击放大镜的关闭按钮时触发该事件。</summary>
	/// <param name="e" type="Object">事件参数。</param>
	    e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }
	    this._magnifierBody.style.display = "none";
	    this._container.style.height = this._headHeight + "px";
	    this._oldleft = this._container.style.left;
	    this._oldtop = this._container.style.top;
	    this._container.style.left = parseFloat(this._mapContainer.style.width) - parseFloat(this._container.style.width) + "px";
	    this._container.style.top = "0px";
        this._mapInMagnifier.src = "../../images/supGis/spacer.gif";
        var img = $get("expandCollapse");
	    img.src = "../../images/supGis/restore.gif";
	    img.onclick = this._expandDelegate;
	    this._hidden = true;
	},
	
	_expand:function(e){
	    if( !this._hidden){
	        return;
	    }
	    e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }
	    this._magnifierBody.style.display = "block";
	    this._container.style.height = this._height + "px";
	    this._container.style.left = this._oldleft;
	    this._container.style.top = this._oldtop;
	    this._hidden = false;
	    var img = $get("expandCollapse");
	    img.src = this._collapseButtonImg;
	    img.onclick = this._collapseDelegate;
	},

	_collapse:function (e){
	    e = SuperMap.Utility.getEvent(e);
	    if(e){
	        SuperMap.Utility.cancelBubble(e);
	    }
	    this._magnifierBody.style.display = "none";
	    this._oldleft = this._container.style.left;
	    this._oldtop = this._container.style.top;
	    this._container.style.height = this._headHeight + "px";
	    this._mapInMagnifier.src = "../../images/supGis/spacer.gif";
	    var img = $get("expandCollapse");
	    img.src = this._expandButtonImg;
	    img.onclick = this._expandDelegate;
	    this._hidden = true;
	},
	
	_mouseDown:function (e){
	    e = SuperMap.Utility.getEvent(e);
	    var target = SuperMap.Utility.getTarget(e);
	    SuperMap.Utility.cancelBubble(e);
	    if(target.tagName == "IMG" || target.tagName == "SELECT" || target.tagName == "OPTION"){
	        return;
	    }
	    if(!this._draggable){
	        return;
	    }
        var mouseX = SuperMap.Utility.getMouseX(e);
        var mouseY = SuperMap.Utility.getMouseY(e);
        this._oldMousePositionX = mouseX;
        this._oldMousePositionY = mouseY;
        this._mapInMagnifier.src = "../../images/supGis/spacer.gif";
	    this._startMoving = true;
	    this._vertCross.style.display = "block";
        this._horizCross.style.display = "block";

	    if(this._container.setCapture){
	        this._container.setCapture();
	    }
	    
	    return false;
	},
	
	_mouseMove:function (e){
	    if(this._startMoving){
	        e = SuperMap.Utility.getEvent(e);
	        var mouseX = SuperMap.Utility.getMouseX(e);
            var mouseY = SuperMap.Utility.getMouseY(e);
            var offsetX = mouseX - this._oldMousePositionX;
            var offsetY = mouseY - this._oldMousePositionY;
            this._leftForMagnifier = parseInt(this._container.style.left) + offsetX;
            this._topForMagnifier = parseInt(this._container.style.top) + offsetY;//mouseY-this._topForMapContainer-this._headHeight*0.8;
            this._container.style.left = this._leftForMagnifier + "px";
            this._container.style.top = this._topForMagnifier + "px";
            this._oldMousePositionX = mouseX;
            this._oldMousePositionY = mouseY;
            SuperMap.Utility.cancelBubble(e);
	    }
	    return false;
	},
	
	_mouseUp:function (e){
	    e = SuperMap.Utility.getEvent(e);
	    if(!this._hidden){
	        this.getMap();
	    }
	    this._startMoving = false;
	    SuperMap.Utility.cancelBubble(e);
	    this._vertCross.style.display = "none";
        this._horizCross.style.display = "none";
        this._oldleft = this._container.style.left;
	    this._oldtop = this._container.style.top;

	    if(this._container.releaseCapture){
	        this._container.releaseCapture();
	    }
	    
	    return false;
	},
	
    _onClick:function(e){
	    e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
    },
    
    _onDblClick:function(e){
	    e = SuperMap.Utility.getEvent(e);
        SuperMap.Utility.cancelBubble(e);
    },

	_getMapCompelet:function(mapUrl){
	    this._mapInMagnifier.src = mapUrl;   
	},
	
	_getMapError:function (){
	},
	
	_renderHead:function (){
	    var head = document.createElement("div");
	    head.id = this._container.id + "_Head";
        head.style.backgroundColor = this._headBackColor;
        head.style.width = this._width + "px";
        head.style.height = this._headHeight + "px";
        if(this._headBackgroundImage){
            head.style.backgroundImage = "url(" + this._headBackgroundImage + ")";
        }
        if(this._headSeparatorLine){
            head.style.borderBottom = "1px solid black";
        }
        if(this._headFont){
            head.style.fontFamily = this._headFont.fontFamily.name;
            head.style.fontSize = this._headFont.size;
            if(this._headFont.bold){
                head.style.fontWeight = "bold";
            }
            if(this._headFont.italic){
                head.style.fontStyle = "italic";
            }
            if(this._headFont.underline && this._headFont.strikeout){
                head.style.textDecoration = "underline line-through";
            }else if(this._headFont.underline){
                head.style.textDecoration = "underline";
            }else if(this._headFont.strikeout){
                head.style.textDecoration = "line-through"
            }
        }
        if(this.headrBackgroundImage){
            head.style.backgroundImage = this._headrBackgroundImage;
        }
	    var titleDiv = document.createElement("div");
        titleDiv.style.width = this._width * 0.6;
        titleDiv.style.height = "100%";
        titleDiv.style.position = "absolute";
        titleDiv.style.top = 0;
        titleDiv.style.left = 0;
        titleDiv.style.cursor = "default";
        if(this._title){
            titleDiv.innerHTML = this._title;
        }
        head.appendChild(titleDiv);
	    var selectDiv = document.createElement("div");
        selectDiv.style.width = this._width * 0.2;
        selectDiv.style.height = "100%";
        selectDiv.style.top = parseFloat(titleDiv.style.top);
        selectDiv.style.left = parseFloat(titleDiv.style.left) + parseFloat(titleDiv.style.width);
        selectDiv.style.position = "absolute";
        selectDiv.style.valign = "top";
        var selectInput = document.createElement("select");
        selectInput.id = this._container.id + "_ZoomRadio";
        selectInput.style.width = "100%";
        selectInput.style.fontFamily = SuperMap.Resource.ins.getMessage("fontSong");
        selectInput.style.fontSize = this._headHeight * 0.5 + "px";
        selectInput.onchange = this._getMapDelegate;
        for(var i = this._minZoomRadio; i <= this._maxZoomRadio; i++){
            var option = document.createElement("option");
            option.value = i;
            option.innerHTML  = i + "X";
            option.title = i + "X";
            if(i == 2){
                option.selected = true;
            }
            selectInput.appendChild(option);
        }
        selectDiv.appendChild(selectInput);
        head.appendChild(selectDiv);
	    var imgDiv = document.createElement("div");
        imgDiv.style.width = this._width * 0.2;
        imgDiv.style.height = "100%";
        imgDiv.style.top = parseFloat(titleDiv.style.top);
        imgDiv.style.left = parseFloat(head.style.width) - parseFloat(imgDiv.style.width);
        imgDiv.style.position = "absolute";
        if(this._expandCollapseButton){
            var expandCollapseImg = new Image();
            expandCollapseImg.id = "expandCollapse";
            expandCollapseImg.src = this._collapseButtonImg;
            expandCollapseImg.onclick = this._collapseDelegate;
            expandCollapseImg.style.cursor = "default";
            imgDiv.appendChild(expandCollapseImg);
        }
        if(this._closeButton){
            var closeImg = new Image();
            closeImg.id = "close";
            closeImg.src = this._closeButtonImg;
            closeImg.onclick = this._closeDelegate;
            closeImg.style.cursor = "default";
            imgDiv.appendChild(closeImg);
        }
        head.appendChild(imgDiv);
        this._container.appendChild(head);
	},
	
	_renderBody:function (){
	    this._magnifierBody = document.createElement("div");
	    this._magnifierBody.style.width = this._width + "px";
	    this._magnifierBody.style.height = this._height - this._headHeight + "px";
	    this._magnifierBody.style.position = "relative";
	    this._magnifierBody.id = this._container.id + "_Body";
	    this._vertCross = document.createElement("div");
	    this._vertCross.style.width = "20px";
	    this._vertCross.style.height = "2px";
	    this._vertCross.style.fontSize = 0;
	    this._vertCross.style.backgroundColor = "red";
	    this._vertCross.style.position = "absolute";
	    this._vertCross.id = this._container.id + "_vertCross";
	    this._vertCross.style.top = parseFloat(this._magnifierBody.style.height.replace("px","")) / 2 - parseFloat(this._vertCross.style.height) / 2 + "px";
	    this._vertCross.style.left = parseFloat(this._magnifierBody.style.width) / 2 - parseFloat(this._vertCross.style.width) / 2 + "px";
	    this._magnifierBody.appendChild(this._vertCross);
	    this._horizCross = document.createElement("div");
	    this._horizCross.style.width = "2px";
	    this._horizCross.style.height = "20px";
	    this._horizCross.style.backgroundColor = "red";
	    this._horizCross.style.position = "absolute";
	    this._horizCross.id = this._container.id + "_HorizCross";
	    this._horizCross.style.top = parseFloat(this._magnifierBody.style.height) / 2 - parseFloat(this._horizCross.style.height) / 2 + "px";
	    this._horizCross.style.left = parseFloat(this._magnifierBody.style.width) / 2 - parseFloat(this._horizCross.style.width) / 2 + "px";
	    this._magnifierBody.appendChild(this._horizCross);
	    this._mapInMagnifier = new Image();
	    this._mapInMagnifier.src = "../../images/supGis/spacer.gif";
	    this._mapInMagnifier.style.width = this._width;
	    this._mapInMagnifier.style.height = this._height - this._headHeight;
	    this._magnifierBody.appendChild(this._mapInMagnifier);
	    this._container.appendChild(this._magnifierBody);
	},
	
	_render:function (){
	    this._renderHead();
	    this._renderBody();
	    if(!this._expanded){
	        this._collapse()
	    }else{
	        this._container.style.left = parseFloat(this._mapContainer.style.width) - parseFloat(this._width);
	        this._container.style.top = 0;
	    }
	},
	
	_setPropertyHidden:function (){
	    var o = new Object();
	    o.width = this._width;
        o.height = this._height;
        o.headHeight = this._headHeight;
        o.minZoomRadio = this._minZoomRadio;
        o.maxZoomRadio = this._maxZoomRadio;
        o.closeButton = this._closeButton;
        o.draggable = this._draggable;
        o.expandCollapseButton = this._expandCollapseButton;
        o.expanded = this._expanded;
        o.title = this._title;
        var propertyJSON = SuperMap.Utility.toJSON(o);
        var hidden = $find(this._container.id + "_hiddenProperty");
        if(hidden){
            hidden.value = propertyJSON;
        }
	},
	
	_update:function(){
	    this._render();
	    this._setPropertyHidden();
	},
	
	_onGetMapCompelet:function (mapUrl){
	    this._mapInMagnifier.src = mapUrl;
	},
	
	_onGetMapError:function(){}
}
   
SuperMap.UI.MagnifierControl.registerClass('SuperMap.UI.MagnifierControl', Sys.UI.Control);


//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.MagnifierManager.js 
// 功能：			Ajax MagnifierManager 类 
// 最后修改时间：	2007-11-02
//========================================================================== 
Type.registerNamespace("SuperMap");
    
SuperMap.MagnifierManager = function(handler, mapName){
	/// <summary>
	/// 放大镜控件管理器。该类主要用于将放大镜控件的请求提交给服务端代理器。
	/// </summary>
	/// <param name="handler" type="String">命令提交器将请求提交到服务端代理器的地址。&lt;br&gt;
	/// 客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
	/// handler会将GIS请求的处理结果返回给客户端。</param>
	/// <param name="mapName" type="String">地图名称。该值不能为空。</param>
	/// <returns type="SuperMap.MagnifierManager">返回 MagnifierManager 对象。</returns>
    this._serverUrl = handler;
    this._mapName = mapName;
    this._onComplete = null;
    this._onError = null;
    
    // 用于注册事件处理函数。
    // onComplete, 原型参数: onComplete(resultSet); 
    // onError, 原型参数: onError(responseText);
}

SuperMap.MagnifierManager.prototype = {

    registerHandler:function(onGetMapComplete, onGetMapError){
	/// <summary>注册默认的事件处理函数。</summary>
	/// <param name="onGetMapComplete" type="Function">完成从服务端获取放大图片时触发的事件处理函数。</param>
	/// <param name="onGetMapError" type="Function">获取放大图片过程中返回异常信息时触发的事件处理函数。</param>
        this._onComplete = onGetMapComplete;
        this._onError = onGetMapError;
    },
    
    getMagnifier:function(center,viewer,mapScale){
	/// <summary>根据放大的参数获取放大的结果。</summary>
	/// <param name="center" type="SuperMap.Point2D">放大的区域的中心点，SuperMap.Point2D类型。</param>
	/// <param name="viewer" type="SuperMap.Rect2D">放大的区域，SuperMap.Rect2D类型。</param>
	/// <param name="mapScale" type="Number">放大后的地图比例尺。</param>
    	this._getGetMagnifierBase("GetMagnifier", ["center", "viewer", "mapScale"], [center, viewer, mapScale]);
    },
    
     _getGetMagnifierBase:function(methodName, paramNames, paramValues){
        SuperMap.Committer.commitAjax(this._mapName, this._serverUrl, methodName, paramNames, paramValues, false, this._onComplete, this._onError, null);
	}
}
   
SuperMap.MagnifierManager.registerClass('SuperMap.MagnifierManager', Sys.Component, Sys.IDisposable);

// Since this script is not loaded by System.Web.Handlers.ScriptResourceHandler
// invoke Sys.Application.notifyScriptLoaded to notify ScriptManager 
// that this is the end of the script.
if (typeof(Sys) !== 'undefined') Sys.Application.notifyScriptLoaded();