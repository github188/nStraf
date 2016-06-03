
//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.SliderBarControl.js 
// 功能：			ScaleBarControl 类 
// 最后修改时间：	2007-11-16
//========================================================================== 

Type.registerNamespace("SuperMap.UI");

SuperMap.UI.SliderBarControl = function(container){
	/// <summary>地图缩放条控件。它的主要作用是用户通过改变缩放条比例来实现地图窗口中的地图缩放。&lt;br&gt;
	/// 1. 地图缩放条控件必须有一个与之关联的Mapcontrol，利用地图缩放条控件来控件Mapcontrol中的地图缩放，设置关联的MapControl可以通过SliderBarControl.mapControl属性完成。&lt;br&gt;
	/// 2. 地图缩放条可以有两种呈现方式-垂直缩放条和水平缩放条，可以通过SliderBarControl.position来设置其呈现方式，通过SliderBarControl.ordinal来设置刻度条刻度的排序方式，如从小比例尺到大比例尺还是从大比例尺到小比例尺。&lt;br&gt;
	/// 3. 地图缩放条主要有四个部分组成，放大键、缩小键、刻度尺以及滑动块，可以分别通过zoomOutImageUrl,zoomInImageUrl,zoomBarImageUrl和sliderImageUrl来设置他们的客户端呈现图片地址。&lt;br&gt;
	/// &lt;img src="../img/slidebar.png" /&gt;
	/// </summary>
    /// <param name="container" type="Object" domElement="true">装载缩放条的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.SliderBarControl">返回 SliderBarControl 对象。</returns>
    var e = Function._validateParams(arguments, [{name: "container", domElement: true}]);
    if (e) throw e;
    
//    SuperMap.UI.SliderBarControl.initializeBase(this, [container]);
    
    this._container = container;
    
    this._mapControl = null;
    this._params = null;
    this._referPosition = new SuperMap.ZoomPosition();
    
	this._useScaleBar = false;
	this._id = this._container.id;
	this._radio = 1;
	this._over = false;
	this._startMoving = false;
	this._leveLength;
	this._oldPositionX;
	this._oldPositionY;
	this._slider;
	this._zoomIn;
	this._zoomOut;
	this._zoombar;
	this._topForZoomBar;
	this._leftForZoomBar;
    this._containerInside = null;
    	
	this._width = 0;
	this._height = 0;
    this._zoomLevels = 0;
    this._curZoomLevel = 0;
    this._zoomBarStartAt = 0;
    this._ordinal = true;
    this._sliderImageUrl = "";
    this._position = this._referPosition.vertical;
    this._useIntersectedZoomBar = false;
    this._zoomBarImageLength = 0;
    this._zoomBarImageUrl = "";
    this._zoomOutImageUrl = "";
    this._zoomInImageUrl = "";
    
    this._setSliderDelegate = Function.createDelegate(this, this._setSlider);
    this._zoombarMouseDownDelegate = Function.createDelegate(this, this._zoombarMouseDown);
    this._zoombarMouseMoveDelegate = Function.createDelegate(this, this._zoombarMouseMove);
    this._zoombarMouseUpDelegate = Function.createDelegate(this, this._zoombarMouseUp);
    this._zoomOutDelegate = Function.createDelegate(this, this.zoomOut);
    this._zoomInDelegate = Function.createDelegate(this, this.zoomIn);

};

SuperMap.UI.SliderBarControl.prototype={

    get_mapControl:function(){
	/// <summary>获取或者设置关联的 MapControl 控件。</summary>
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
	/// <summary>获取或者设置地图参数对象。</summary>
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
            this._params.width = this._width = 20;
        }
        if(this._params.height){
	        this._height = this._params.height;
        }else{
	        this._height = parseInt(this._container.style.height);
            this._params.height = this._height;
        }
        if(this._params.zoomLevels){
	        this._zoomLevels = this._params.zoomLevels;
        }else{
            this._params.zoomLevels = this._zoomLevels = 9;
        }
        if(this._params.curZoomLevel){
	        this._curZoomLevel = this._params.curZoomLevel;
        }else{
            this._params.curZoomLevel = this._curZoomLevel = 3;
        }
        if(this._params.zoomBarStartAt){
	        this._zoomBarStartAt = this._params.zoomBarStartAt;
        }else{
            this._params.zoomBarStartAt = this._zoomBarStartAt = 3;
        }
        if(this._params.ordinal){
	        this._ordinal = this._params.ordinal;
        }else{
            this._params.ordinal = this._ordinal = true;
        }
        if(this._params.sliderImageUrl){
	        this._sliderImageUrl = this._params.sliderImageUrl;
        }else{
            this._params.sliderImageUrl = this._sliderImageUrl = "../../images/supGis/slider.png";
        }
        if(this._params.position){
	        this._position = this._params.position;
        }else{
            this._params.position = this._position = this._referPosition.vertical;
        }
        if(this._params._useIntersectedZoomBar){
	        this._useIntersectedZoomBar = this._params.useIntersectedZoomBar;
        }else{
            this._params.useIntersectedZoomBar = this._useIntersectedZoomBar = false;
        }
        if(this._params.zoomBarImageLength){
	        this._zoomBarImageLength = this._params.zoomBarImageLength;
        }else{
            this._params.zoomBarImageLength = this._zoomBarImageLength = 90;
        }
        if(this._params.zoomBarImageUrl){
	        this._zoomBarImageUrl = this._params.zoomBarImageUrl;
        }else{
            this._params.zoomBarImageUrl = this._zoomBarImageUrl = "../../images/supGis/zoom-bg.png";
        }
        if(this._params.zoomOutImageUrl){
	        this._zoomOutImageUrl = this._params.zoomOutImageUrl;
        }else{
            this._params.zoomOutImageUrl = this._zoomOutImageUrl = "../../images/supGis/zoom-minus.png";
        }
        if(this._params.zoomInImageUrl){
	        this._zoomInImageUrl = this._params.zoomInImageUrl;
        }else{
            this._params.zoomInImageUrl = this._zoomInImageUrl = "../../images/supGis/zoom-plus.png";
        }
    },
    
    get_width:function(){
	/// <summary>获取或者设置SliderBarControl的宽度。</summary>
   	/// <returns type="Number">get_width()返回值类型为Number。</returns>
        return this._width;
    },
    
    set_width: function(value) {
        this._width = value;
    },
    
    get_height:function(){
	/// <summary>获取或者设置SliderBarControl的高度。</summary>
   	/// <returns type="Number">get_height()返回值类型为Number。</returns>
        return this._height;
    },
    
    set_height: function(value) {
        this._height = value;
    },
    
    get_zoomLevels:function(){
	/// <summary>获取或者设置地图缩放级别。</summary>
   	/// <returns type="Array" elementType="Number">get_zoomLevels()返回值类型为 Integer 数组。</returns>
        return this._zoomLevels;
    },
    
    set_zoomLevels: function(value) {
        this._zoomLevels = value;
    },
    
    get_curZoomLevel:function(){
	/// <summary>当前地图比例尺级别。</summary>
   	/// <returns type="Number">get_curZoomLevel()返回值类型为Number。</returns>
        return this._curZoomLevel;
    },
    
    set_curZoomLevel: function(value) {
        this._curZoomLevel = value;
    },
    
    get_zoomBarStartAt:function(){
	/// <summary>获取或者设置滑动块在初始点的位置。如图所示。&lt;br&gt;
	/// &lt;img src="../img/sliderbar_zoomBarStartAt.png" /&gt;  &lt;br&gt;
	/// 由于滑动块始终在刻度条范围内滑动，因此滑动块的初始点位置是相对于刻度条起点位置的，
	/// 即滑动块初始点到刻度条起始点的距离。单位为像素。</summary>
   	/// <returns type="Number">get_zoomBarStartAt()返回值类型为Number。</returns>
        return this._zoomBarStartAt;
    },
    
    set_zoomBarStartAt: function(value) {
        this._zoomBarStartAt = value;
    },
    
    get_ordinal:function(){
	/// <summary>表示地图缩放条中刻度尺刻度是否按照有放大——>缩小的顺序排序。&lt;br&gt;
	/// true表示刻度尺刻度按照放大——>缩小的顺序排列。&lt;br&gt;
	/// 如果地图缩放条是水平方式呈现的，即position=Horizontal，那么ordinal=true表示刻度条从左向右的方式依次为放大——>缩小的顺序。&lt;br&gt;
	/// 如果地图缩放条是垂直方式呈现的，即position=Vertical，那么ordinal=true表示刻度条从上向下的方式依次为放大——>缩小的顺序。
	/// </summary>
   	/// <returns type="Boolean">get_ordinal()返回值类型为Boolean。</returns>
        return this._ordinal;
    },
    
    set_ordinal: function(value) {
        this._ordinal = value;
    },
    
    get_sliderImageUrl:function(){
	/// <summary>获取或者设置滑动块客户端呈现图片的地址。&lt;br&gt;
	/// 滑动块如图所示：&lt;br&gt;
	/// &lt;img src="../img/slidebar.png" /&gt;
	/// </summary>
   	/// <returns type="String">get_sliderImageUrl()返回值类型为String。</returns>
        return this._sliderImageUrl;
    },
    
    set_sliderImageUrl: function(value) {
        this._sliderImageUrl = value;
    },
    
    get_position:function(){
    /// <summary>获取或者设置地图缩放条的呈现方式，Vertical为垂直呈现，Horizontal为水平呈现，默认为Vertical。 </summary>
   	/// <returns type="String">get_position()返回值类型为String。</returns>
        return this._position;
    },
    
    set_position: function(value) {
        this._position = value;
    },
    
    get_useIntersectedZoomBar:function(){
	/// <summary>是否使用由多个单位刻度图片组合成刻度条的呈现方式，默认为 false。&lt;br&gt;
	/// 刻度条的图片可以做成两种形式的，一种是单位刻度的图片，第二种是一条完整刻度条图片。&lt;br&gt;
	///	1. 如果使用图片表达的是单位刻度的，那么需要在 UseIntersectedZoomBar 中设置true，并且在 ZoomBarImageLength 中设置单位刻度的高度，
	/// 这样刻度条就由一定数目（地图比例尺级别总数）个单位刻度图片组合而成，每个图片都设置高度为 ZoomBarImageLength。&lt;br&gt;
	///	2. 如果使用一条完整的刻度条图片，那么需要在 UseIntersectedZoomBar 中设置 false，并且在 ZoomBarImageLength 中设置能够表达指定刻度数的高度，
	/// 这样刻度条就是由一个图片来表达，通过 ZoomBarImageLength 来控制刻度条中刻度数目即地图比例尺级别总数。例如，如果这个完整的刻度条图片表达了9个刻度（比例尺级别），每个刻度大约4px高，
	/// 但是地图比例尺级别总数为5，那么可以设置 ZoomBarImageLength = 5*4 = 20px。</summary>
   	/// <returns type="Boolean">get_useIntersectedZoomBar() 返回值类型为 Boolean。</returns>
        return this._useIntersectedZoomBar;
    },
    
    set_useIntersectedZoomBar: function(value) {
        this._useIntersectedZoomBar = value;
    },
    
    get_zoomBarImageLength:function(){
	/// <summary>用来表示刻度条的图片的呈现高度。&lt;br&gt;
	/// 刻度条的图片可以做成两种形式的，一种是单位刻度的图片，第二种是一条完整刻度条图片。&lt;br&gt;
	///	1. 如果使用图片表达的是单位刻度的，那么需要在 UseIntersectedZoomBar 中设置true，并且在 ZoomBarImageLength 中设置单位刻度的高度，
	/// 这样刻度条就由一定数目（地图比例尺级别总数）个单位刻度图片组合而成，每个图片都设置高度为 ZoomBarImageLength。&lt;br&gt;
	///	2. 如果使用一条完整的刻度条图片，那么需要在 UseIntersectedZoomBar 中设置 false，并且在 ZoomBarImageLength 中设置能够表达指定刻度数的高度，
	/// 这样刻度条就是由一个图片来表达，通过 ZoomBarImageLength 来控制刻度条中刻度数目即地图比例尺级别总数。例如，如果这个完整的刻度条图片表达了9个刻度（比例尺级别），每个刻度大约4px高，
	/// 但是地图比例尺级别总数为5，那么可以设置 ZoomBarImageLength = 5*4 = 20px。
	/// </summary>
   	/// <returns type="Number">get_zoomBarImageLength() 返回值类型为 Number。</returns>
        return this._zoomBarImageLength;
    },
    
    set_zoomBarImageLength: function(value) {
        this._zoomBarImageLength = value;
    },
    
    get_zoomBarImageUrl:function(){
	/// <summary>获取或者设置刻度条客户端呈现图片的地址。&lt;br&gt;
	/// 刻度条如图所示：&lt;br&gt;
	/// &lt;img src="../img/slidebar.png" /&gt;  &lt;br&gt;
	/// 刻度条的图片可以做成两种形式的，一种是单位刻度的图片，第二种是一条完整刻度条图片。&lt;br&gt;
	///	1. 如果使用图片表达的是单位刻度的，那么需要在 UseIntersectedZoomBar 中设置 true，并且在 ZoomBarImageLength 中设置单位刻度的高度，
	/// 这样刻度条就由一定数目（地图比例尺级别总数）个单位刻度图片组合而成，每个图片都设置高度为 ZoomBarImageLength。&lt;br&gt;
	///	2. 如果使用一条完整的刻度条图片，那么需要在 UseIntersectedZoomBar 中设置 false，并且在 ZoomBarImageLength 中设置能够表达指定刻度数的高度，
	/// 这样刻度条就是由一个图片来表达，通过 ZoomBarImageLength 来控制刻度条中刻度数目即地图比例尺级别总数。例如，如果这个完整的刻度条图片表达了9个刻度（比例尺级别），每个刻度大约4px高，
	/// 但是地图比例尺级别总数为5，那么可以设置 ZoomBarImageLength = 5*4 = 20px。
	/// </summary>
   	/// <returns type="String">get_zoomBarImageUrl() 返回值类型为 String。</returns>
        return this._zoomBarImageUrl;
    },
    
    set_zoomBarImageUrl: function(value) {
        this._zoomBarImageUrl = value;
    },
    
    get_zoomOutImageUrl:function(){
	/// <summary>缩小键图片地址。&lt;br&gt;
	/// 缩小键如图所示：&lt;br&gt;
	/// &lt;img src="../img/slidebar.png" /&gt;  
	/// </summary>
   	/// <returns type="String">get_zoomOutImageUrl()返回值类型为String。</returns>
        return this._zoomOutImageUrl;
    },
    
    set_zoomOutImageUrl: function(value) {
        this._zoomOutImageUrl = value;
    },
    
    get_zoomInImageUrl:function(){
	/// <summary>放大键图片地址。&lt;br&gt;
	/// 放大键如图所示：&lt;br&gt;
	/// &lt;img src="../img/slidebar.png" /&gt;  
	/// </summary>
   	/// <returns type="String">get_zoomInImageUrl()返回值类型为String。</returns>
        return this._zoomInImageUrl;
    },
    
    set_zoomInImageUrl: function(value) {
        this._zoomInImageUrl = value;
    },

	initialize:function (){
	/// <summary>初始化SliderBarControl。</summary>
	    if(this._containerInside){
            if(this._containerInside.parentNode){
                this._containerInside.parentNode.removeChild(this._containerInside);   
            } 
            this._containerInside = null;
        }
	    this._containerInside = document.createElement("div");
	    this._containerInside.id = "sliderBar_inside";
	    this._containerInside.style.position = "";
	    this._containerInside.style.width = this._params.width + "px";
	    this._containerInside.style.height = this._params.height + "px";
	    this._container.appendChild(this._containerInside);
        this._initProperty();
	    this._render();
	    this._mapControl.add_viewChanged(this._setSliderDelegate);
	},

	dispose:function(){
        if(this._mapControl && this._setSliderDelegate){
            this._mapControl.remove_viewChanged(this._setSliderDelegate);
        }
        this._params = null;
        this._useScaleBar = null;
        this._id = null;
        this._radio = null;
        this._over = null;
        this._startMoving = null;
        this._leveLength = null;
        this._oldPositionX = null;
        this._oldPositionY = null;

        if(this._slider){
            if(this._slider.parentNode) {
                this._slider.onclick = null;
                this._slider.onmousedown = null;
                this._slider.onmouseover = null;
                this._slider.onmousemove = null;
                this._slider.onmouseup = null;
                this._slider.parentNode.removeChild(this._slider);
            }
            delete this._slider;
            this._slider = null;
        }
        if(this._zoombar){
            this._zoombar.onmousedown = null;
            this._zoombar.onmousemove = null;
            this._zoombar.onmouseup = null;
            this._zoombar.onclick = null;
	        if(this._zoombar.parentNode) {
                this._zoombar.parentNode.removeChild(this._zoombar);
            }
            while(this._zoombar.childNodes.length > 0) {
                var child = this._zoombar.childNodes[0];
                child.onclick = null;
                child.onmousedown = null;
                child.onmouseover = null;
                child.onmousemove = null;
                child.onmouseup = null;
                this._zoombar.removeChild(child);
                delete child;
                child = null;
            }
            delete this._zoombar;
            this._zoombar = null;
        }
        if(this._zoomIn){
            this._zoomIn.onclick = null;
            this._zoomIn.onmousedown = null;
            this._zoomIn.onmouseover = null;
            this._zoomIn.onmousemove = null;
            this._zoomIn.onmouseup = null;
            if(this._zoomIn.parentNode) {
                this._zoomIn.parentNode.removeChild(this._zoomIn);
            }
            delete this._zoomIn;
            this._zoomIn = null;
        }
        if(this._zoomOut){
            this._zoomOut.onclick = null;
            this._zoomOut.onmousedown = null;
            this._zoomOut.onmouseover = null;
            this._zoomOut.onmousemove = null;
            this._zoomOut.onmouseup = null;
            if(this._zoomOut.parentNode) {
                this._zoomOut.parentNode.removeChild(this._zoomOut);
            }
            delete this._zoomOut;
            this._zoomOut = null;
        }
        this._topForZoomBar = null;
        this._leftForZoomBar = null;
        delete this._setSliderDelegate;
        
        var node = document.getElementById('sliderBar_inside');
        if(node && node.parentNode) {
            node.parentNode.removeChild(node);
        }
        delete node;
        node = null;
        if(this._containerInside){
            while(this._containerInside.childNodes.length > 0) {
                var child = this._containerInside.childNodes[0];
                child.onclick = null;
                child.onmousedown = null;
                child.onmouseover = null;
                child.onmousemove = null;
                child.onmouseup = null;
                this._containerInside.removeChild(child);
                delete child;
                child = null;
            }
            
            this._containerInside.innerHTML = "";
            if(this._containerInside.parentNode) {
                this._containerInside.parentNode.removeChild(this._containerInside);
            }
            delete this._containerInside;
            this._containerInside = null;
        }
        if(this._container){
	        while(this._container.childNodes.length > 0) {
                var child = this._container.childNodes[0];
                child.onclick = null;
                child.onmousedown = null;
                child.onmouseover = null;
                child.onmousemove = null;
                child.onmouseup = null;
                this._container.removeChild(child);
                delete child;
                child = null;
            }
            this._container.innerHTML = "";
            var parentNode = this._container.parentNode;
            if(parentNode) {
                parentNode.removeChild(this._container);
            }
            
            var style = this._container.style;
            var position = style.position;
            var left = style.left;
            var top = style.top;
            var width = style.width;
            var height = style.height
            var containerID = this._container.id;
            
            delete this._container;
            this._container = null;
            
            this._container = document.createElement("div");
            this._container.style.position = position;
            this._container.style.left = left;
            this._container.style.top = top;
            this._container.style.height = height;
            this._container.style.width = width;
            this._container.id = containerID;
            if(parentNode) {
                parentNode.appendChild(this._container);
            }
        }        
        
        SuperMap.UI.SliderBarControl.callBaseMethod(this, 'dispose');
	    
	},

	_initProperty:function(){
	    this._width = 20;
	    if(this._params && this._params.width != "undefined"){
	        this._width = this._params.width;
	    }
	    this._height = 150;
	    if(this._params && this._params.height != "undefined"){
	        this._height = this._params.height;
	     }
	    this._zoomBarStartAt = 5;
	    if(this._params && this._params.zoomBarStartAt != "undefined"){
	        this._zoomBarStartAt = this._params.zoomBarStartAt;
	    }
	    this._ordinal = true;
	    if(this._params && this._params.ordinal != "undefined"){
	        this._ordinal = this._params.ordinal;
	    }
	    this._sliderImageUrl = "../../images/supGis/slider.gif";
        if(this._params && this._params.sliderImageUrl != "undefined"){
            this._sliderImageUrl = this._params.sliderImageUrl;
        }
        this._position = this._referPosition.vertical;
        if(this._params && this._params.sliderImageUrl != "undefined"){
            this._position = this._params.position;
        }
        this._useIntersectedZoomBar = true;
        if(this._params && this._params.useIntersectedZoomBar != "undefined"){
            this._useIntersectedZoomBar = this._params.useIntersectedZoomBar;
        }
        this._zoomBarImageLength = 15;
        if(this._params && this._params.zoomBarImageLength != "undefined"){
            this._zoomBarImageLength = this._params.zoomBarImageLength;
        }
        this._zoomBarImageUrl = "../../images/supGis/zoom-bg-intersected.gif";
        if(this._params && this._params.zoomBarImageUrl != "undefined"){
            this._zoomBarImageUrl = this._params.zoomBarImageUrl;
        }
        this._zoomOutImageUrl = "../../images/supGis/ZoomOut.gif";
        if(this._params && this._params.zoomOutImageUrl != "undefined"){
            this._zoomOutImageUrl = this._params.zoomOutImageUrl;
        }
        this._zoomInImageUrl = "../../images/supGis/ZoomIn.gif";
        if(this._params && this._params.zoomInImageUrl != "undefined"){
            this._zoomInImageUrl = this._params.zoomInImageUrl;
        }
        var mapParam = this._mapControl.getMapParam();
	    var mapScales = mapParam.mapScales;
	    if(!mapScales || mapScales.length <= 0){
            this._leveLength = 0;
            this._curZoomLevel = 0;
            this._zoomLevels = 0;
            return ;
        }
        this._zoomLevels = mapScales.length;
        if(!this._useIntersectedZoomBar){
	        this._leveLength = (this._zoomBarImageLength - this._zoomBarStartAt) / this._zoomLevels;
	    }else{
	        this._leveLength = this._zoomBarImageLength;
	    }
	    
	    var curScale = mapParam.mapScale;
	    for(var i = 0; i < mapScales.length; i++){
	        //?
	        if(curScale == mapScales[i]){
	            this._curZoomLevel=i;
	            break;
	        }
	    }
	},
		
	_zoombarMouseDown:function (e){
	    if(this._mapControl.getMapParam().mapScales.length <= 0){
	        return;
	    }
	    if (this._position != this._referPosition.vertical){
            this._slider = $get(this._container.id + "_Slider");
            this._zoombar = $get(this._container.id + "_Zoombar");
        }	    
	    e = SuperMap.Utility.getEvent(e);
	    this._oldPositionY = e.clientY;
	    this._oldPositionX = e.clientX;
	    var mouseX = SuperMap.Utility.getMouseX(e);
	    var mouseY = SuperMap.Utility.getMouseY(e);
	    var zoomBarStartAt;
	    if (this._position == this._referPosition.vertical){
	        zoomBarStartAt = SuperMap.Utility.getElementY(this._zoombar);
	        this._slider.style.top = mouseY - zoomBarStartAt - this._slider.height / 2 + "px";
	    }else{
	        zoomBarStartAt = SuperMap.Utility.getElementX(this._zoombar);
	        this._slider.style.left = mouseX - zoomBarStartAt - this._slider.width / 2 + "px";
	    }
	    this._startMoving = true;
	    SuperMap.Utility.cancelBubble(e);
	    return false;
	},
	
	 _zoombarMouseMove:function(e){
	    if(this._startMoving){
	        if (this._position != this._referPosition.vertical){
	            this._slider = $get(this._container.id + "_Slider");
	        }
	        e = SuperMap.Utility.getEvent(e);
	        SuperMap.Utility.cancelBubble(e);
	        var offsetX = e.clientX - this._oldPositionX;
	        var offsetY = e.clientY - this._oldPositionY;
	        var top = parseInt(this._slider.style.top);
	        var left = parseInt(this._slider.style.left);
	        
	        if (this._position == this._referPosition.vertical){
	            top = top + offsetY;
	            if(top < 0 || top > (this._zoomLevels - 1) * this._leveLength + this._zoomBarStartAt){
	                return false;
	            }
	            this._slider.style.top = top + "px";
	        }else{
	            left = left + offsetX;
	            if(left < 0 || left > (this._zoomLevels - 1) * this._leveLength + this._zoomBarStartAt){
	                return false;
	            }
	            this._slider.style.left = left + "px";
	        }
	        this._oldPositionY = e.clientY;
	        this._oldPositionX = e.clientX;
	    }
	    return false;
	},
	
	_zoombarMouseUp:function (e){
	    e = SuperMap.Utility.getEvent(e);
        if (this._position != this._referPosition.vertical){
            this._slider = $get(this._container.id + "_Slider");
	        this._zoombar = $get(this._container.id + "_Zoombar");
        }
	    SuperMap.Utility.cancelBubble(e);
	    var temp = SuperMap.Utility.getMouseY(e);
	    var _curPosition;
	    if (this._position == this._referPosition.vertical){
	        _curPosition = parseInt(this._slider.style.top);
	    }else{
	        _curPosition = parseInt(this._slider.style.left);
	    }
	    var level = Math.round((_curPosition - this._zoomBarStartAt) / this._leveLength);
	    this._setSliderPosition(level);
	    if(!this._ordinal){
	       level = this._zoomLevels - level - 1;
	    }
	   
	    this._curZoomLevel = level;
	    this._zoomMap(this._zoomLevels - level - 1);
	    
	    this._startMoving = false;
        return false;
	},
	
	zoomIn:function(){
	/// <summary>利用SliderBar控件放大一级比例尺。</summary>
        if(this._ordinal){
             this._setSliderPosition(this._curZoomLevel - 1);
            if(!this._over){
                this._curZoomLevel--;
                this._zoomMap(this._zoomLevels - this._curZoomLevel - 1);
            }
        }else{
            this._setSliderPosition(this._curZoomLevel + 1);
            if(!this._over){
                this._curZoomLevel++;
                this._zoomMap(this._curZoomLevel);
            }
        }
	},
	
	zoomOut:function(){
	/// <summary>利用SliderBar控件缩小一级比例尺。</summary>
        if(this._ordinal){
            this._setSliderPosition(this._curZoomLevel + 1);
            if(!this._over){
                this._curZoomLevel++;
                this._zoomMap(this._zoomLevels - this._curZoomLevel - 1); 
            }
        }else{
            this._setSliderPosition(this._curZoomLevel - 1);
            if(!this._over){
                this._curZoomLevel--;
                this._zoomMap(this._curZoomLevel);
            }
        }
	},
	
	_setSlider:function(){
        var mapParam = this._mapControl.getMapParam();
	    var mapScales = mapParam.mapScales;
	    if(!mapScales || mapScales.length <= 0){
	        this._zoomLevels = 0;
	        this._curZoomLevel = 0;
	        if(this._container){
    	        this._container.style.display = "none";
	        }
	        return;
	    }
	    this._container.style.display = "block";
	    this._zoomLevels = mapScales.length;
	    if(this._useIntersectedZoomBar){
	        this._leveLength = this._zoomBarImageLength;
	    }else{
	        this._leveLength = (this._zoomBarImageLength - this._zoomBarStartAt) / this._zoomLevels;
	    }
	    var mapScale = mapParam.mapScale;
	    var zoomLevels = this._zoomLevels;
        for (var i = 0; i < this._zoomLevels; i++){
            if (mapScale == mapScales[i]){
                this._curZoomLevel = i;
                break;
            }
        }
        this._curZoomLevel = this._zoomLevels - this._curZoomLevel-1;
        if(!this._ordinal){
            this._curZoomLevel = this._zoomLevels - this._curZoomLevel - 1;
        }
        this._setSliderPosition(this._curZoomLevel)
	},
	
	_setSliderPosition:function(level){
	    if(level < 0 || level >= this._zoomLevels){
	        this._over = true;
	        return;
	    }
	    var position = level * this._leveLength + this._zoomBarStartAt;
	    if(!this._ordinal){
	        position += this._zoomBarStartAt;
	    }
	    if(this._position == this._referPosition.vertical){
	        this._slider.style.top = position + "px";
	    }else{
	        this._slider.style.left = position + "px";
	    }
	    this._over = false;
	},
	
	_zoomMap:function (level){
        var mapParam = this._mapControl.getMapParam();
        if(mapParam.mapScales[level]){
    	    this._mapControl.set_mapScale(mapParam.mapScales[level]);
        }else{
            // todo:如果本身MapControl的scale没有分级怎么处理？
        }
	},
	
	_setPropertyHidden:function(){
        var o = new Object();
        o._ordinal = this._ordinal;
        o.position = this._position;
        o._sliderImageUrl = this._sliderImageUrl;
        o._userIntersectedZoomBar = this._userIntersectedZoomBar;
        o._zoomBarImageLength = this._zoomBarImageLength;
        o._zoomBarImageUrl = this._zoomBarImageUrl;
        o._zoomBarStartAt = this._zoomBarStartAt;
        o._zoomOutImageUrl = this._zoomOutImageUrl;
        o._zoomInImageUrl = this._zoomInImageUrl;
        var propertyJSON = SuperMap.Utility.toJSON(o);
        var hidden = $get(this._container.id + "_hiddenProperty");
        if(hidden){
            hidden.value = propertyJSON;
        }
    },
    
    _renderZoomIn:function(){
        this._zoomIn = new Image();
//        this._zoomIn.style.styleFloat = "left";
        this._zoomIn.onclick = this._zoomInDelegate;
        this._containerInside.appendChild(this._zoomIn);
        if(this._position == this._referPosition.vertical){
            this._zoomIn.style.width = this._width + "px";
        }else{
            this._zoomIn.style.height = this._height + "px";
            if(_GetBrowser() != "ie"){
                if(!this._ordinal){
                    this._zoomIn.style.position = "relative";
                    this._zoomIn.style.top = (0 - this._height * 2) + "px";
                    this._zoomIn.style.left = this._zoomOut.offsetWidth + this._zoomBarImageLength + "px";
                }
            }
        }
        this._zoomIn.src = this._zoomInImageUrl;
    },
    
    _renderZoomOut:function (){
        this._zoomOut = new Image();
//        this._zoomOut.style.styleFloat = "left";
        this._zoomOut.onclick = this._zoomOutDelegate;
        this._containerInside.appendChild(this._zoomOut);
        if(this._position == this._referPosition.vertical){
            this._zoomOut.style.width = this._width + "px";
        }else{
            this._zoomOut.style.height = this._height + "px";
            if(_GetBrowser() != "ie"){
                if(this._ordinal){
                    this._zoomOut.style.position = "relative";
                    this._zoomOut.style.top = (0 - this._height * 2) + "px";
                    this._zoomOut.style.left = this._zoomIn.offsetWidth + this._zoomBarImageLength + "px";
                }
            }
        }
        this._zoomOut.src = this._zoomOutImageUrl;
    },
    
    _renderZoomBar:function(){
        this._curZoomLevel = this._zoomLevels - this._curZoomLevel - 1;
        if (!this._ordinal){
            this._curZoomLevel = this._zoomLevels - this._curZoomLevel - 1;
        }
        var sliderPosition;
        
        this._zoombar = document.createElement("DIV");
        this._zoombar.id = this._container.id + "_Zoombar";
        this._zoombar.style.position = "relative";
        if (this._position == this._referPosition.vertical){
            this._zoombar.style.width = this._width + "px";
            if (!this._useIntersectedZoomBar){
               this._zoombar.style.height = this._zoomBarImageLength + "px";
               this._zoombar.style.backgroundImage = "url(" + this._zoomBarImageUrl + ")";
               this._zoombar.style.backgroundRepeat = "no-repeat"
               this._zoombar.style.backgroundPosition = " 40% 0%";
            }
        }else{
            this._zoombar.style.height = this._height + "px";
            if(_GetBrowser() != "ie"){
                this._zoombar.style.top = (0 - this._height) + "px";
                if(this._ordinal){
                    this._zoombar.style.left = this._zoomIn.offsetWidth + "px";
                }else{
                    this._zoombar.style.left = this._zoomOut.offsetWidth + "px";
                }
            }
            this._zoombar.style.styleFloat = "left";
            if (!this._useIntersectedZoomBar){
               this._zoombar.style.width = this._zoomBarImageLength + "px";
               this._zoombar.style.backgroundImage = "url(" + this._zoomBarImageUrl + ")";
            }
        } 
        if (this._useIntersectedZoomBar){
            for (var i = 0; i < this._zoomLevels; i++){
                var pic = new Image();
                pic.src = this._zoomBarImageUrl;
                if (this._position == this._referPosition.vertical){
                    pic.style.height = this._zoomBarImageLength + "px";
                    this._zoombar.style.height = this._zoomBarImageLength * this._zoomLevels + "px";
                }else{
                    pic.style.width = this._zoomBarImageLength + "px";
                    this._zoombar.style.width = this._zoomBarImageLength * this._zoomLevels + "px";
                }
                this._zoombar.appendChild(pic);
            }
        }
        this._slider = new Image();
        this._slider.id = this._container.id + "_Slider";
        this._slider.style.position =  "absolute";
        if (!this._useIntersectedZoomBar){
            sliderPosition = this._curZoomLevel * this._leveLength + this._zoomBarStartAt;
        }else{
            sliderPosition = this._curZoomLevel * this._zoomBarImageLength + this._zoomBarStartAt;
            if (this._position == this._referPosition.vertical){
                this._slider.style.left = "0px";
            }else{
                this._slider.style.top = "0px";
            }
        } 
        this._slider.src = this._sliderImageUrl;
        this._zoombar.appendChild(this._slider);
        if (this._position == this._referPosition.vertical){
            this._slider.style.width = this._width + "px";
            this._slider.style.top = sliderPosition + "px";
            this._zoombar.onmousedown = this._zoombarMouseDownDelegate;
            this._zoombar.onmousemove = this._zoombarMouseMoveDelegate;
            this._zoombar.onmouseup = this._zoombarMouseUpDelegate;
            this._containerInside.appendChild(this._zoombar);
        }else{
            this._slider.style.height = this._height + "px";
            this._slider.style.left = sliderPosition - this._slider.width / 2 + "px";
            this._zoombar.onmousedown = this._zoombarMouseDownDelegate;
            this._zoombar.onmousemove = this._zoombarMouseMoveDelegate;
            this._zoombar.onmouseup = this._zoombarMouseUpDelegate;
            this._containerInside.appendChild(this._zoombar);
        } 
    },
    
    _render:function (){
        this._setPropertyHidden();
        this._container.style.styleFloat = "left";
        var mapParam = this._mapControl.getMapParam();
	    var mapScales = mapParam.mapScales;
	    if(!mapScales){
	        this._container.style.display = "none";
	    }
        if (this._ordinal){
            this._renderZoomIn();
            this._renderZoomBar();
            this._renderZoomOut();
        }else{
            this._renderZoomOut();
            this._renderZoomBar();
            this._renderZoomIn();            
        }
    },
    
    update:function(){
	/// <summary>更新SliderBarControl。当SliderBarControl的属性值发生变化时，可以通过该方法变更SliderBarControl的客户端呈现。</summary>
         this._setPropertyHidden();
         this._render();
    }
};

SuperMap.UI.SliderBarControl.registerClass('SuperMap.UI.SliderBarControl', Sys.UI.Control);

// Since this script is not loaded by System.Web.Handlers.ScriptResourceHandler
// invoke Sys.Application.notifyScriptLoaded to notify ScriptManager 
// that this is the end of the script.
if (typeof(Sys) !== 'undefined') Sys.Application.notifyScriptLoaded();