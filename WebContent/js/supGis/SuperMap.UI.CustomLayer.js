//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.CustomLayer.js  
// 功能：			自定义图层
// 最后修改时间：	2007-8-21
//========================================================================== 

Type.registerNamespace('SuperMap.UI');

//CustomLayer
// 暂时在CustomLayer相关的实现里面加上mapControl，考虑以后优化，让CustomLayer可以独立使用的时候把mapControl去掉
// 动态效果可以用window.mapStatus中的东西
SuperMap.UI.CustomLayer = function(container){
	/// <summary>客户端自定义图层。该层位于地图窗口的最上层，背景透明，在该层主要显示用户自定义的注记、线、面等要素。
	/// </summary>
	/// <param name="container" type="Object">显示自定义图层的图层容器，即 DOM 对象，如 div，img。</param>
	/// <returns type="SuperMap.UI.CustomLayer">返回 CustomLayer 对象。</returns>
	/// <field name="container" type="Object">显示自定义图层的图层容器，即 DOM 对象，如div，img。</field>
	/// <field name="markers" type="Array" elementType="SuperMap.UI.Marker">自定义图层中自定义的注记集合。</field>
	/// <field name="lines" type="Array" elementType="SuperMap.UI.Polyline">自定义图层中自定义的线段集合。</field>
	/// <field name="polygons" type="Array" elementType="SuperMap.UI.Polyline">自定义图层中自定义的面集合。</field>
	/// <field name="circles" type="Array" elementType="SuperMap.UI.Circle">自定义图层中自定义的圆集合。</field>
	/// <field name="mapControl" type="SuperMap.MapControl">mapControl对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl对象的当前范围和比例尺即为该图层的对应值。&lt;br&gt;
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</field>
	
    this.container = container;
    this.markers = new Array();
    this.lines = new Array();
    this.polygons = new Array();
    this.circles = new Array();
    this.mapControl = null;
};
SuperMap.UI.CustomLayer.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化自定义图层。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl 对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl 对象的当前范围和比例尺即为该图层的对应值。CustomLayer
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</param>
         this.mapControl = mapControl;
    },
    addMarker:function(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, content, groupID){
	/// <summary>添加自定义注记。无论地图中是否已经存在与该注记 ID 相同的对象，都将添加自定义注记。
	/// </summary>
	/// <param name="id" type="String">自定义注记的 ID值。</param>	
	/// <param name="x" type="Number">自定义注记的 x 坐标（地理坐标）。Point2D.x 类型。</param>
	/// <param name="y" type="Number">自定义注记的 y 坐标（地理坐标）。Point2D.y 类型。</param>
	/// <param name="offsetX" type="Number">自定义注记的浮动标签的 x 方向偏移量，以像素（px）为单位。</param>
	/// <param name="offsetY" type="Number">自定义注记的浮动标签的 y 方向偏移量，以像素（px）为单位。</param>
	/// <param name="innerHtml" type="String">呈现时的 HTML。</param>
	/// <param name="className" type="String">呈现时的样式单 CSS 的ID。</param>
	/// <param name="zIndex" type="Number">自定义注记的 Index 值。</param>
	/// <param name="content" type="SuperMap.MarkerContent">自定义注记的内容对象。&lt;br&gt;
	/// </param>
	/// <param name="groupID" type="String">分组 ID，多个自定义标记可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Marker.div">返回自定义注记的容器 Marker.div。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
        var mk = new SuperMap.UI.Marker(this.container);
        mk.initialize(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, groupID, content, this.mapControl);
        this.markers.push(mk);
        this._setMarksHidden();
        return mk.div;
    },
    insertMarker:function(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, content, groupID){
	/// <summary>添加自定义注记。如果地图中已经存在与该注记 ID 相同的对象，则替换存在的注记。
	/// </summary>
	/// <param name="id" type="String">自定义注记的 ID值。</param>	
	/// <param name="x" type="Number">自定义注记的 x 坐标（地理坐标）。Point2D.x 类型。</param>
	/// <param name="y" type="Number">自定义注记的 y 坐标（地理坐标）。Point2D.y 类型。</param>
	/// <param name="offsetX" type="Number">自定义注记的浮动标签的 x 方向偏移量，以像素（px）为单位。</param>
	/// <param name="offsetY" type="Number">自定义注记的浮动标签的 y 方向偏移量，以像素（px）为单位。</param>
	/// <param name="innerHtml" type="String">呈现时的 HTML。</param>
	/// <param name="className" type="String">呈现时的样式单 CSS 的 ID。</param>
	/// <param name="zIndex" type="Number">自定义注记层的 Index 值。</param>
	/// <param name="content" type="SuperMap.MarkerContent">自定义注记的内容对象。&lt;br&gt;
	/// </param>
	/// <param name="groupID" type="String">分组 ID，多个自定义标记可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Marker.div">返回自定义注记的容器 Marker.div。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
        for(var i = 0; i < this.markers.length; i++){
            var mk = this.markers[i];
            if(mk.id == id){
                mk.initialize(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, groupID, content, this.mapControl);
                this._setMarksHidden();
                return mk.div;
            }
        }
        return this.addMarker(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, content, groupID);
    },
    removeMarker:function(id){
	/// <summary>移除指定的自定义注记。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义注记的 id。</param>
        for(var i = 0; i < this.markers.length; i++){
            var mk = this.markers[i];
            if(mk.id == id){
                this.markers.splice(i, 1);
                mk.dispose();
                mk = null;
                this._setMarksHidden();
                return;
            }
        }
    },
	removeMarkersByGroupID:function(groupID){
	/// <summary>移除指定 groupID 对应的一组自定义注记。
	/// </summary>
	/// <param name="groupID" type="String">待删除的自定义注记的 gourpID。</param>
		//xianchao，前面的算法有很严重的逻辑错误，因为markers数组的长度随着中间元素的删减会减少，而i值每次又在增加，
		//所以数组中后面的元素下标值会减少，最终导致不会被判断和比较，所以采用数组下标从大到小的方式去进行比较。
		for(var i = this.markers.length - 1; i > -1 ;i--){
			var mk = this.markers[i];
			if(mk.groupID == groupID){
				this.markers.splice(i, 1);
				mk.dispose();
				mk = null;
				this._setMarksHidden();
			}
		}
    },
    clearMarkers:function(){
	/// <summary>清除所有自定义注记。</summary>
        while(this.markers.length > 0){
            var mk = this.markers.pop();
            mk.dispose();
            mk = null;
        }
        this._setMarksHidden();
     },
    repositionMarkers:function (){
	/// <summary>根据Mapcontrol地图的变化重置自定义标记。</summary>
	/// <param name="originX" type="Number">地图显示起始点 X 像素坐标。</param>
	/// <param name="originY" type="Number">地图显示起始点 Y 像素坐标。</param>
	/// <param name="offsetX" type="Number">地图显示偏差值 X 像素坐标。</param>
	/// <param name="offsetY" type="Number">地图显示偏差值 Y 像素坐标。</param>
		for(var i = 0; i < this.markers.length;i++){
			this.markers[i].reposition();
        }
    },
    addLine:function(id, xs, ys, strokeWeight, strokeColor, opacity, zIndex, groupID){
	/// <summary>在自定义图层中添加一条线。无论地图中是否已经存在与该 ID 相同的对象，都将添加自定义线。
	/// </summary>
	/// <param name="id" type="String">添加的线的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">线的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">线的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">线的宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="opacity" type="Number">线的透明度。</param>
	/// <param name="zIndex" type="Number">线所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义线可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义线。</returns>
	    if(id == null || xs == null || ys == null){
	        return null;
	    }
		if(!zIndex) {
			zIndex = 40;
		}
	    var line = new SuperMap.UI.Polyline(false, this.container);
	    line.initialize(id, xs, ys, strokeWeight, strokeColor, null, opacity, zIndex, groupID, this.mapControl);
	    this.lines.push(line);
	    this._setLinesHidden();
	    return line;
    },
    insertLine:function(id,xs,ys,strokeWeight,strokeColor,opacity,zIndex,groupID){
	/// <summary>在自定义图层中添加一条线。如果地图中已经存在与该 ID 相同的对象，则替换存在的线。
	/// </summary>
	/// <param name="id" type="String">添加的线的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">线的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">线的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="opacity" type="Number">线的透明度。</param>
	/// <param name="zIndex" type="Number">线所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义线可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义线。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
	    if(id == null || xs == null || ys == null){
	        return null;
	    }
	    for(var i = 0; i < this.lines.length; i++){
	        var line = this.lines[i];
	        if(line.id == id){
			    line.initialize(id, xs, ys, strokeWeight, strokeColor, null, opacity, zIndex, groupID, this.mapControl);
			    this._setLinesHidden();
			    return line;
		    }
        }
		return this.addLine(id, xs, ys, strokeWeight, strokeColor, opacity, zIndex, groupID);
    },
    removeLine:function(id){
	/// <summary>移除指定的自定义线。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义线段的 id。</param>
	    for(var i = 0; i < this.lines.length; i++){
	        var line = this.lines[i];
	        if(line.id == id){
	            this.lines.splice(i, 1);
	            line.dispose();
	            this._setLinesHidden();
	            return;
	        }
	    }
    },
    clearLines:function(){
	/// <summary>清除所有自定义线。</summary>
        while(this.lines.length > 0)
            this.lines.pop().dispose();
        this._setLinesHidden();
    },
    repositionLines:function(){
        for(var i = 0; i < this.lines.length; i++){
            this.lines[i].reposition();
        }
    },
    addPolygon:function(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
	/// <summary>在自定义图层中添加自定义面。无论地图中是否已经存在与该 ID 相同的对象，都将添加自定义面。
	/// </summary>
	/// <param name="id" type="String">添加的面的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">面的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">面的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">面的边线宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">面的边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">面的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillOpacity" type="Number">面的透明度。</param>
	/// <param name="zIndex" type="Number">面所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义面可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义面。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
	    if(id == null || xs == null || ys == null){
	        return null;
	    }
	    var polygon = new SuperMap.UI.Polyline(true, this.container);
	    polygon.initialize(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID, this.mapControl);
	    this.polygons.push(polygon);
	    this._setPolygonsHidden();
	    return polygon;
    },
    insertPolygon:function(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
	/// <summary>在图层中添加自定义面。如果地图中已经存在与该 ID 相同的对象，则替换存在的面。
	/// </summary>
	/// <param name="id" type="String">添加的面的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">面的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">面的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">面的边线宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">面的边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">面的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillOpacity" type="Number">面的透明度。</param>
	/// <param name="zIndex" type="Number">面所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义面可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Polyline">返回自定义面。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
	    if(id == null || xs == null || ys == null){
	        return null;
	    }
	    var polygon = null;
	    for(var i = 0; i < this.polygons.length; i++){
	        polygon = this.polygons[i];
	        if(polygon.id == id){
	            polygon.initialize(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID ,this.mapControl);
	            this._setPolygonsHidden();
	            return polygon;
	        }
        }
        polygon = null;
        return this.addPolygon(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex);
    },
    removePolygon:function(id){
	/// <summary>移除指定的自定义面。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义面的id。</param>
	    for(var i = 0; this.polygons && i < this.polygons.length; i++){
	        var polygon = this.polygons[i];
	        if(polygon.id == id){
	            this.polygons.splice(i,1);
	            polygon.dispose();
	            this._setPolygonsHidden();
	            return;
	        }
	    }
    },
    clearPolygons:function(){
	/// <summary>清除所有自定义面。</summary>
        while(this.polygons.length > 0)
            this.polygons.pop().dispose();
        this._setPolygonsHidden();
    },
    repositionPolygons:function(){
        for(var i = 0; i < this.polygons.length; i++){
            this.polygons[i].reposition();
        }
    },
    _setMarksHidden:function(){
        var hidden = document.getElementById(this.container.id + "_hiddenMarks");
        if(hidden){
            //_marks无法直接ToJSON,先转换下
            var marks  = null;
            if(_marks){
                marks = new Array();
                for(var i=0;i<this.markers.length;i++ ){
                    var mark= new Object();
                    mark.id  = this.markers[i].id;
                    mark.x  = this.markers[i].x;
                    mark.y  = this.markers[i].y;
                    mark.w  = this.markers[i].w;
                    mark.h  = this.markers[i].h;
                    
                    mark.innerHtml  = SuperMap.Utility.convertHTMLToESC(this.markers[i].innerHtml);
                    mark.className  = this.markers[i].className;
                    mark.zIndex = this.markers[i].zIndex;
                    mark.groupID = this.markers[i].groupID;
                    marks.push(mark);
                }
            }
            //再将转换出来的marks toJSON
            var json = SuperMap.Utility.toJSON(marks);
            //清理一下
            if(marks){
                while(marks.length>0 ){
                    var mark = marks.pop();
                    mark = null;
                }
               marks = null; 
            }
            hidden.value = json;        
        }
    },
    _setLinesHidden:function(){
        var hidden = document.getElementById(this.container.id+"_hiddenLines");
        if(hidden){
            //_lines无法直接ToJSON,先转换下
            var lines  = null;
            if(this.lines){
                lines = new Array();
                for(var i = 0; i < this.lines.length; i++ ){
                    var line= new Object();
                    line.id  = this.lines[i].id;
                    line.xs  = this.lines[i].xs;
                    line.ys  = this.lines[i].ys;
                    line.strokeWeight  = this.lines[i].strokeWeight;
                    line.strokeColor  = this.lines[i].strokeColor;
                    line.zIndex = this.lines[i].zIndex;
                    line.groupID = this.lines[i].groupID;
                    lines.push(line);
                }
            }
            //再将转换出来的marks toJSON
            var json = SuperMap.Utility.toJSON(lines);
            //清理一下
            if(lines){
                while(lines.length>0 ){
                    var line = lines.pop();
                    line = null;
                }
               lines = null; 
            }
            hidden.value = json;
        }
    },
    _setPolygonsHidden:function(){
        var hidden = document.getElementById(this.container.id + "_hiddenPolygons");
        if(hidden){
            //_lines无法直接ToJSON,先转换下
            var polygons  = null;
            if(this.polygons){
                polygons = new Array();
                for(var i=0;i<this.polygons.length;i++ ){
                    var polygon= new Object();
                    polygon.id  = this.polygons[i].id;
                    polygon.xs  = this.polygons[i].xs;
                    polygon.ys  = this.polygons[i].ys;
                    polygon.strokeWeight  = this.polygons[i].strokeWeight;
                    polygon.strokeColor  = this.polygons[i].strokeColor;
                    polygon.fillColor  = this.polygons[i].fillColor;
                    polygon.fillOpacity  = this.polygons[i].fillOpacity;
                    polygon.zIndex = this.polygons[i].zIndex;
                    polygon.groupID = this.polygons[i].groupID;
                    polygons.push(polygon);
                }
            }
            //再将转换出来的polygons toJSON
            var json = SuperMap.Utility.toJSON(polygons);
            //清理一下
            if(polygons){
                while(polygons.length>0 ){
                    var polygon = polygons.pop();
                    polygon = null;
                }
               polygons = null; 
            }
            hidden.value = json;
        }
    },

    addCircle:function(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
	/// <summary>在自定义图层中添加自定义圆。无论地图中是否已经存在与该 ID 相同的对象，都将添加自定义圆。
	/// </summary>
	/// <param name="id" type="String">添加的圆的ID。</param>
	/// <param name="startPoint" type="SuperMap.Point2D">画圆的起点。</param>
	/// <param name="endPoint" type="SuperMap.Point2D">画圆的终点。</param>
	/// <param name="strokeWeight" type="Number">圆的边线宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">圆的边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">圆的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillOpacity" type="Number">圆的透明度。</param>
	/// <param name="zIndex" type="Number">圆所在的 div 层的 index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义面可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Circle">返回自定义圆。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
	    if(id == null || startPoint == null || endPoint == null){
	        return null;
	    }
	    var circle = new SuperMap.UI.Circle(this.container);
	    circle.initialize(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID, this.mapControl);
	    this.circles.push(circle);
	    return circle;
    },

    insertCircle:function(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID){
	/// <summary>在图层中添加自定义圆。如果地图中已经存在与该 ID 相同的对象，则替换存在的圆。
	/// </summary>
	/// <param name="id" type="String">添加的圆的 ID。</param>
	/// <param name="startPoint" type="SuperMap.Point2D">画圆的起点。</param>
	/// <param name="endPoint" type="SuperMap.Point2D">画圆的终点。</param>
	/// <param name="strokeWeight" type="Number">圆的边线宽度（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">圆的边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">圆的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillOpacity" type="Number">圆的透明度。</param>
	/// <param name="zIndex" type="Number">圆所在的 div 层的index。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义面可以属于同一组。</param>
	/// <returns type="SuperMap.UI.Circle">返回自定义圆。</returns>
		if(!zIndex) {
			zIndex = 40;
		}
	    //if(id == null || xs == null || ys == null){
		//xianchao xs和ys不存在，调用时会报错
		if(id == null){
	        return null;
	    }
	    var circle = null;
	    for(var i = 0; i < this.circles.length; i++){
	        circle = this.circles[i];
	        if(circle.id == id){
				//xianchao 直接去掉原有的圆
				this.removeCircle(id);
	            //circle.initialize(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID ,this.mapControl);
	            //return circle;
	        }
        }
        circle = null;
        return this.addCircle(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex);
    },

    removeCircle:function(id){
	/// <summary>移除指定的自定义圆。
	/// </summary>
	/// <param name="id" type="String">待删除的自定义圆的id。</param>
	    for(var i = 0; this.circles && i < this.circles.length; i++){
	        var circle = this.circles[i];
	        if(circle.id == id){
	            this.circles.splice(i,1);
	            circle.dispose();
	            return;
	        }
	    }
    },

    clearCircles:function(){
	/// <summary>清除所有自定义圆。</summary>
        while(this.circles.length > 0)
            this.circles.pop().dispose();
    },

    repositionCircles:function(){
        for(var i = 0; i < this.circles.length; i++){
            this.circles[i].reposition();
        }
    }
/*    
OpenInfoWindow
CloseInfoWindow
SetGroupVisible
SetGroupZindex
*/
};
SuperMap.UI.CustomLayer.registerClass("SuperMap.UI.CustomLayer", null, Sys.IDisposable);

SuperMap.UI.Marker = function(container){
	/// <summary>该类主要描述自定义图层中的自定义注记。
	/// </summary>
	/// <param name="container" type="Object">装载自定义图层的容器，即 DOM 对象，如 div，img。</param>
	/// <returns type="SuperMap.UI.Marker">返回 Marker 对象。</returns>
	/// <field name="div" type="Object">自定义注记的容器。</field>
	/// <field name="id" type="String">自定义的注记的id。</field>
	/// <field name="xSteps" type="Number">自定义注记在每一个缩放级别中的x坐标。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。
	/// 这一过程也存在着图片位置的变化，因此该属性主要表示在每一个缩放级别自定义注记的x坐标。</field>
	/// <field name="ySteps" type="Number">自定义注记在每一个缩放级别中的y坐标。&lt;br&gt;
	/// 在执行地图缩放过程中，由于从发出缩放请求到接收并完全下载缩放的结果图片
	/// 存在一个响应时间的间隔时期，在这段时间内，会先将原有的本地地图图片按照缩放比例进行图片的缩放，这一缩放也是逐级进行的。
	/// 这一过程也存在着图片位置的变化，因此该属性主要表示在每一个缩放级别自定义注记的y坐标。</field>
	/// <field name="innerHtml" type="String">自定义注记呈现时的 HTML。</field>
	/// <field name="zIndex" type="Number">自定义注记的 Index 值。</field>
	/// <field name="groupID" type="String">分组 ID，多个自定义标记可以属于同一组。</field>
	/// <field name="content" type="SuperMap.MarkerContent">自定义注记的内容对象。&lt;br&gt;
	/// </field>
	/// <field name="mapControl" type="SuperMap.MapControl">mapControl 对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl 对象的当前范围和比例尺即为该图层的对应值。&lt;br&gt;
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</field>
	this.div = document.createElement("div");
	this.div.mk = this;
	this.id = null;
	this._position = new SuperMap.Point2D(0, 0);// 地理坐标作为一个属性存在，并不直接决定它的象素位置，象素位置和地理坐标都是MapControl赋的
	this._currentPositionX = 0;
	this._currentPositionY = 0;
	this._nextPositionX = 0;
	this._nextPositionY = 0;
	this._container = container;

	//this.width = 0; // <field name="width" type="Number">自定义注记的宽度（显示宽度），以像素（px）为单位。</field>
	//this.height = 0;	// <field name="height" type="Number">自定义注记的高度（显示高度），以像素（px）为单位。</field>
	this._offsetX = 0;
	this._offsetY = 0;
	this._zoomTotalSteps = 0
//	var n = this._zoomTotalSteps + 1;
	this.xSteps = null;//new Array(n);
	this.ySteps = null;//new Array(n);
	this.innerHtml = null;
	this.className = null;
	this.zIndex  = 11; //_customLayer_baseZIndex = 11
	this.groupID = null;
	this._originX = null;
	this._originY = null;
	this.content = null;// 
	this._image = null;
	// 通过使用代理的事件，脱离和MapControl以及Layer的直接关联
	this._events = new Array();
	this.mapControl = null;
	this._lastMouseX;
	this._lastMouseY;
};
SuperMap.UI.Marker.prototype={
	initialize:function(id, x, y, offsetX, offsetY, innerHtml, className, zIndex, groupID, content, mapControl){
	/// <summary>初始化自定义注记对象。
	/// </summary>
	/// <param name="id" type="String">自定义注记的 ID 值。</param>	
	/// <param name="x" type="Number">自定义注记的 x 坐标（地理坐标）。Point2D.x 类型。</param>
	/// <param name="y" type="Number">自定义注记的 y 坐标（地理坐标）。Point2D.y 类型。</param>
	/// <param name="offsetX" type="Number">自定义注记的浮动标签的 x 方向偏移量，以像素（px）为单位。</param>
	/// <param name="offsetY" type="Number">自定义注记的浮动标签的 y 方向偏移量，以像素（px）为单位。</param>
	/// <param name="innerHtml" type="String">呈现时的 HTML。</param>
	/// <param name="className" type="String">呈现时的样式单 CSS 的 ID。</param>
	/// <param name="zIndex" type="Number">自定义注记的 Index 值。</param>
	/// <param name="groupID" type="String">分组 ID，多个自定义注记可以属于同一组。</param>
	/// <param name="content" type="SuperMap.MarkerContent">自定义注记的内容对象。&lt;br&gt;
	/// </param>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl 对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl 对象的当前范围和比例尺即为该图层的对应值。
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</param>
	    this.mapControl = mapControl;
		this.id = id;
		this.div.id = id;
		this.div.className = className;
		this.className = className;
		this.groupID = groupID;
      	var html = "<table style='background-color:White;'><tr><td style='width: 100px;height:15px;background-color: lightcyan;'>&nbsp;</td>";
		html += "<td style='background-color: lightcyan;'><img src='../../images/supGis/close.gif' onclick='var div = document.getElementById(\""+SuperMap.UI.Marker._toolTipID + "\"); if(div) {div.style.display = \"none\";}'  style='cursor:\"hand\";'/></td></tr>";
		html += "<tr><td style='width: 150px;'>" + innerHtml + "</td><td></td></tr>";
		html += "</table>";
		if(innerHtml && innerHtml != null) {
			this.innerHtml = html;
		}
		
		this._zoomTotalSteps = mapControl.get_zoomTotalSteps();
		var n = this._zoomTotalSteps + 1;
		this.xSteps = new Array(n);
		this.ySteps = new Array(n);
	    
		this._originX = mapControl.getOrigin().x;
		this._originY = mapControl.getOrigin().y;

		if(!zIndex){
    		this.zIndex = this._container.style.zIndex; 
		}else{
	    	this.zIndex = zIndex; 
		}
		if(content && content.src!=null && content.src.trim()!= ""){
			this.content = new SuperMap.MarkerContent();
			this.content.imageSrc = content.src;
			this.content.imageWidth = content.width;
			this.content.imageHeight = content.height
		} else if(content && content.imageSrc != null && content.imageSrc.trim() != "") {
			this.content = new SuperMap.MarkerContent();
			if(content.width) {
				this.content.imageWidth = content.width;
			}
			if(content.height) {
				this.content.imageHeight = content.height;
			}
			if(content.imageWidth) {
				this.content.imageWidth = content.imageWidth;
			}
			if(content.imageHeight) {
				this.content.imageHeight = content.imageHeight;
			}
			if(content.src) {
				this.content.imageSrc = content.src;
			}
			if(content.imageSrc) {
				this.content.imageSrc = content.imageSrc;
			}
		}
		if(content && content.innerHTML) {
			this.content = new SuperMap.MarkerContent();
			this.content.innerHTML = content.innerHTML;
		}
		if(!content) {
			this.content = new SuperMap.MarkerContent();
			this.content.innerHTML = "";
			var params = this.mapControl.get_params();
			this.content.imageSrc = params.contextPath + "images/supGis/icon_pink.gif";
		}
		
		if(false){
			var ondblclickFunction = this._events["ondblclick"];
			var onmousewheelFunction = this._events["onmousewheel"];
			this.div.attachEvent("ondblclick", ondblclickFunction);
			this.div.attachEvent("onmousewheel", onmousewheelFunction);
		}

		this.div.style.position="absolute";

        this._position.x = x;
        this._position.y = y;
		this._offsetX = offsetX;
		this._offsetY = offsetY;
      //  var pc = this.mapControl.mapCoordToPixel(this._position);
		var pc = this.mapControl.getMap().mapCoordToPixel(this._position, this.mapControl.getMap().get_mapScale()); 
		if(pc){
			this._currentPositionX = Math.round(pc.x - this.mapControl.getOrigin().x);
			this._currentPositionY = Math.round(pc.y - this.mapControl.getOrigin().y);
			this._nextPositionX = this._currentPositionX;
    		this._nextPositionY = this._currentPositionY;

           // this.div.style.width = this.width + "px";
           // this.div.style.height = this.height + "px";
    		this.div.style.zIndex = this.zIndex;
			if(this.content && !this.content.innerHTML){
				var imageText = "<img src='" + this.content.imageSrc + "' ";
				if(this.content.imageHeight && this.content.imageHeight > 0) {
					imageText += " height='" + this.content.imageHeight + "'";
				}
				if(this.content.imageWidth && this.content.imageWidth > 0) {
					imageText += " width='" + this.content.imageWidth + "'";
				}
				imageText += "></img>";
				this.div.style.display = "none";
				this.div.innerHTML = imageText;
				this.image = this.div.getElementsByTagName("img")[0];
				if(this.innerHtml != null && this.innerHtml !== ""){
					this.image.onclick = Function.createDelegate(this, this._showToolTip);
					//add by lyang.增加marker图片的onmouseover事件
					this.image.onmouseover = Function.createDelegate(this, this._showToolTip);
					this.image.onmouseout = Function.createDelegate(this, this._delDiv);
				}
				if(!this.content.imageHeight || this.content.imageHeight == 0) {
					this.content.imageHeight = this.image.height;
				}
				if(!this.content.imageWidth || this.content.imageWidth == 0) {
					this.content.imageWidth = this.image.width;
				}
				if(this.content.imageWidht == 0 || this.content.imageHeight == 0) {
					var tempImage = new Image();
					tempImage.src = this.content.imageSrc;
					//xianchao可以在这里把this.content.imageWidth和this.content.imageHeight写死成images/icon_pink.gif的宽和高，不然在第一次访问的时候得到的结果是0
					this.content.imageWidth = tempImage.width;
					this.content.imageHeight = tempImage.height;
					delete tempImage;
					tempImage = null;
				}
			} else {
				this.div.innerHTML = this.content.innerHTML;
				if(this.innerHtml != null && this.innerHtml !== ""){
					this.div.onclick = Function.createDelegate(this, this._showToolTip);
					// this.div.onmouseout = Function.createDelegate(this, this._hideToolTip);
					//this.div.onmouseover = Function.createDelegate(this, this._mouseover);
				}
			}
			this._precomputeSteps();
			this._setFactor(0);
			this.div.style.display="block";
		}else{
		    this.div.style.display="none";
		}
		this._container.appendChild(this.div);
		//this.removeFromMap(); 本来的设计可能是开始不可见
		//this._selectGroup(this.groupID, this.div);
	},
	
	onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){
            return;
        }
        var mouseX = SuperMap.Utility.getMouseX(e);
        var mouseY = SuperMap.Utility.getMouseY(e);
        this._lastMouseX = mouseX;
        this._lastMouseY = mouseY;
    },
	
	
	dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
		// event delegate
//		var _mouseDoubleClickDelegate = Function.createDelegate(this,this._mouseDoubleClick);
//		var _mouseWheelDelegate = Function.createDelegate(this,this._mouseWheel);
//		this.div.detachEvent("onmousedown", function(){});
//		this.div.detachEvent("ondblclick", _mouseDoubleClickDelegate);
//		this.div.detachEvent("onmousewheel", _mouseWheelDelegate);
        /*
        var  ondblclickFunction = this._events["ondblclick"];
        var  onmousewheelFunction = this._events["onmousewheel"];
        
        this.div.detachEvent("onmousedown", function(){});
		this.div.detachEvent("ondblclick", ondblclickFunction);
		this.div.detachEvent("onmousewheel", onmousewheelFunction);
        */
		this.div.mk = null;
		while(this.div.childNodes.length>0){
			this.div.removeChild(this.div.childNodes[0]);
		}
		this.removeFromMap();
		this.div = null;
		while(this.xSteps.length>0){this.xSteps.pop();}
		this._xSteps = null;
		while(this.ySteps.length>0){this.ySteps.pop();}
		this.ySteps = null;
		this._hideToolTip();
		this.content = null;
	},
	_showToolTip:function(){
		if(SuperMap.UI.Marker._toolTip == null){
			SuperMap.UI.Marker._toolTip = document.createElement("DIV");
			SuperMap.UI.Marker._toolTip.id = SuperMap.UI.Marker._toolTipID;
			SuperMap.UI.Marker._toolTip.style.height=50 ;
			SuperMap.UI.Marker._toolTip.style.width=150 ;
			SuperMap.UI.Marker._toolTip.style.opacity = "0.5";
			SuperMap.UI.Marker._toolTip.style.filter = "alpha(opacity=70)";
		}
		SuperMap.UI.Marker._toolTip.name = this.id;
		SuperMap.UI.Marker._toolTip.style.zIndex = this.zIndex +1;
		this._container.appendChild(SuperMap.UI.Marker._toolTip);	
		SuperMap.UI.Marker._toolTip.innerHTML = this.innerHtml ;
		// var pc = this.mapControl.mapCoordToPixel(this._position);
		SuperMap.UI.Marker._toolTip.style.position ="absolute";
		var pc = this.mapControl.getMap().mapCoordToPixel(this._position, this.mapControl.getMap().get_mapScale()); 
		pc.x = Math.round(pc.x - this.mapControl.getOrigin().x);
    	pc.y = Math.round(pc.y - this.mapControl.getOrigin().y);

		// 考虑是否设置为固定值
		SuperMap.UI.Marker._toolTip.style.left = pc.x + this._offsetX;
		SuperMap.UI.Marker._toolTip.style.top = pc.y + this._offsetY;
		SuperMap.UI.Marker._toolTip.style.display = "";
	},
	
	
	_mouseover:function(){
		if(this.id!=null){
			if(this.id.indexOf("bank_")>-1){
				var bankid=this.id.split("_")[1] ;
				//开始查找
				var orgfloor=document.getElementById("orgfloor").value ;
				var layerName = page_res["bankLayerName"];
				var queryParam = new SuperMap.QueryParam();
				var sqlParam = new SuperMap.SqlParam();
				sqlParam.whereClause ="C_BANK_ID='"+bankid+"' and  C_ORGFLOOR like '%"+orgfloor+"%'" ;
				var highlightGetNearest = true;//高亮显示
				if (!layerName) {
				queryParam.queryLayerParams = null;
				} else {
				//待查询的图层。QueryLayerParam 数组。
					queryParam.queryLayerParams = new Array();
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = layerName;
					queryLayer.sqlParam = sqlParam;
					queryParam.queryLayerParams.push(queryLayer);
				}
				queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
				queryParam.expectCount = 10;
				
				
				var onQueryCompeteDelegate = Function.createDelegate(this, this._getQueryCompete);
				mapControl.getMap().queryBySql(queryParam, highlightGetNearest, onQueryCompeteDelegate, null, "", "");
			}
		}
	},
	
  _getQueryCompete:function(resultSet) {
	var recordSetCount;
	var point2D = new Object();
	if (resultSet && resultSet.recordSets) {
		//先清除信息
		if(document.getElementById("newDivId")!=null){
					document.body.removeChild(document.getElementById("newDivId"));
		}
		this.mapControl.customLayer.removeMarker("insertTag");
		try{this.mapControl.clearHighlight();}catch(e){alert(e.Message);}
		
		var newDiv = document.createElement("div");//创建div
		newDiv.id = "newDivId";
		recordSetCount = resultSet.recordSets.length;
		var i, j, k;
		var record;
		var strTableHTML;
		var strTR;
		//显示网点属性
		var bankId = "C_BANK_ID";
		var bankName = "C_BANK_NAME";
		var bankaddress = "C_BANK_ADDRESS";
		var bankIdValue;
		var bankNameValue;
		var bankaddressValue;
				
		//newDiv.style.visibility ="visible";//hidden
		newDiv.style.opacity = "0.5";
		newDiv.style.filter = "alpha(opacity=70)";
		for (i = 0; i < recordSetCount; i++) {
			recordSet = resultSet.recordSets[i];
			
			//首先判断网点的三个字段有无取别名，如果又别名就取别名，否则就默认取字段名
			if (recordSet.returnFieldCaptions) {
				for (j = 0; j < recordSet.returnFieldCaptions.length; j++) {
					if (recordSet.returnFields[j] == "C_BANK_ID") {
						bankId = recordSet.returnFieldCaptions[j];
					}
					if (recordSet.returnFields[j] == "C_BANK_NAME") {
						bankName = recordSet.returnFieldCaptions[j];
					}
					if (recordSet.returnFields[j] == "C_BANK_ADDRESS") {
						bankaddress = recordSet.returnFieldCaptions[j];
					}
				}
			}
			var recordCount;
			if (recordSet.records != null) {
				recordCount = recordSet.records.length;
			} else {
				recordCount = 0;
			}
			for (j = 0; j < recordCount; j++) {
				record = recordSet.records[j];
				newDiv.style.top=record.center.x ;
				newDiv.style.left=record.center.y ;
				for (k = 0; k < record.fieldValues.length; k++) {
					if (recordSet.returnFields[k] == "C_BANK_ID") {
						bankIdValue = record.fieldValues[k];
					}
					if (recordSet.returnFields[k] == "C_BANK_NAME") {
						bankNameValue = record.fieldValues[k];
					}
					if (recordSet.returnFields[k] == "C_BANK_ADDRESS") {
						bankaddressValue = record.fieldValues[k];
					}
				}
			}
			newDiv.innerHTML =
			"<div  class='superShowDiv'  style='position:absolute;left:"+this._currentPositionX+" ;top:"+this._currentPositionY+"; height:120px;width:250px;border:gray 2px solid;fontSize:12px;lineHeight:25px;padding:5px;zIndex:100;opacity:0.5;filter:alpha(opacity=70)'>"+
			"<table cellspacing='1'  cellpadding='3'>" + "<tr><td><font color='red'>" + bankId +
			 "</font></td><td>" + bankIdValue + "</td><td><img src='../../images/supGis/del.gif' onclick='divClose("+newDiv.id+")'></td></tr>" + "<tr><td><font color='red'>" 
			 + bankName + "</font></td><td colspan='2'>" + bankNameValue + "</td></tr>" + 
			 "<tr><td><font color='red'>" + bankaddress + "</font></td><td colspan='2'>" + bankaddressValue +
			  "</td></tr>" + 
			  "<tr><td><input type='button' value='查看详细信息' size='3' onClick='showDetail("+bankIdValue+")'></td><td><input type='button' value='查看业务信息' size='3'></td><tr>"
			  "</table></div>";
			newDiv.style.display = "block";
			document.body.appendChild(newDiv);
			}
		}
	},
	
	_delDiv:function(){
		//var onDelDelegate = Function.createDelegate(this, this._deleteDiv);
		setTimeout("if(document.getElementById('newDivId')!=null){document.body.removeChild(document.getElementById('newDivId'));}",1500);
	},
	
	
	
	_hideToolTip:function(){
		if(SuperMap.UI.Marker._toolTip){
			if(SuperMap.UI.Marker._toolTip.parentNode) {
				SuperMap.UI.Marker._toolTip.parentNode.removeChild(SuperMap.UI.Marker._toolTip);
				delete SuperMap.UI.Marker._toolTip;
				SuperMap.UI.Marker._toolTip = null;
			}
		}
	},
	getMapCoordX:function(){
	/// <summary>获取自定义注记的x坐标，SuperMap.Point2D.x类型。</summary>
	/// <returns type="Number">自定义注记的x坐标。</returns>
	    return this._position.x;
	},
	getMapCoordY:function(){
	/// <summary>获取自定义注记的y坐标，SuperMap.Point2D.y类型。</summary>
	/// <returns type="Number">自定义注记的y坐标。</returns>
	    return this._position.y;
	},
	_selectGroup:function(groupID, curObject){
	    if(!groupID){
	        groupID = "unClassified"
	    }
	    var groupDiv = document.getElementById(groupID);
	    if(!groupDiv){
	        groupDiv = document.createElement("div");
	        groupDiv.id = groupID;
	        curObject.parentNode.appendChild(groupDiv);
	    }
	    if(curObject.parentNode){
	        curObject.parentNode.removeChild(curObject);
	    }
	    groupDiv.appendChild(curObject);
	},
	clearSteps:function(){
	/// <summary>清除自定义注记在每一个缩放级别中的坐标值，将其统一恢复为当前位置的坐标。</summary>
		var steps = this._zoomTotalSteps;
		for(var i = 0; i <= steps; i++){
			this.xSteps[i] = this._currentPositionX;
			this.ySteps[i] = this._currentPositionY;
			if(this.content && this.content.imageWidth) {
				this.xSteps[i] -= this.content.imageWidth/2;
			}
			if(this.content && this.content.imageHeight) {
				this.ySteps[i] -= this.content.imageHeight/2;
			}
		}
	},
	_precomputeSteps:function(){
		var steps = this._zoomTotalSteps;
		for(var i = 0; i <= steps; i++){
			var a = i / steps;
			var b = 1.0 - a;
			this.xSteps[i] = Math.floor(b * this._currentPositionX + a * this._nextPositionX);
			this.ySteps[i] = Math.floor(b * this._currentPositionY + a * this._nextPositionY);
			if(this.content && this.content.imageWidth) {
				this.xSteps[i] -= this.content.imageWidth/2;
			}
			if(this.content && this.content.imageHeight) {
				this.ySteps[i] -= this.content.imageHeight/2;
			}
		}
	},
	_setFactor:function(i){
		this.div.style.left = this.xSteps[i] + "px";
        this.div.style.top = this.ySteps[i] + "px";
		// 使用名称来避免受到其他marker的影响
		if(SuperMap.UI.Marker._toolTip && SuperMap.UI.Marker._toolTip.name == this.id) {
			var tempImage = new Image();
			tempImage.src = this.content.imageSrc;
			var width  = tempImage.width;
			var height = tempImage.height;
			delete tempImage;
			tempImage = null;
			SuperMap.UI.Marker._toolTip.style.left = this.xSteps[i] + this._offsetX + width / 2 + "px";
			SuperMap.UI.Marker._toolTip.style.top = this.ySteps[i] + this._offsetY + height / 2 + "px";
		}
//		this.image.style.left = this.xSteps[i] + "px";
//      this.image.style.top = this.ySteps[i] + "px";
	},
	swapStates:function(){
	/// <summary>交换当前和下一步操作的位置状态。</summary>
	    var temp = 0; temp = this._currentPositionX; this._currentPositionX = this._nextPositionX; this._nextPositionX = temp;
	    temp = this._currentPositionY; this._currentPositionY = this._nextPositionY; this._nextPositionY = temp;
	},
	
	reposition:function(){//origin,offset
	/// <summary>根据Mapcontrol的地图的变化重置自定义注记。</summary>
		//var pc = this.mapControl.mapCoordToPixel(this._position);
		var pc = this.mapControl.getMap().mapCoordToPixel(this._position, this.mapControl.getMap().get_mapScale()); 
		if(pc){
			this._curPositionX = Math.round(pc.x - this.mapControl.getOrigin().x);
			this._curPositionY = Math.round(pc.y - this.mapControl.getOrigin().y);
			// this._curPositionY = Math.round(pc.y - this.mapControl._originY);
			// this._nextPositionX = Math.round(pc.x - this.mapControl._originX - this.mapControl._offsetX);
			// this._nextPositionY = Math.round(pc.x - this.mapControl._originY - this.mapControl._offsetY);
			// this._precomputeSteps();
			//alert(this.mapControl._originX + "-" + this.mapControl._originY +" ,"+this.mapControl._offsetX + " - " +this.mapControl._offsetY);
			//this._curPositionX = Math.round(pc.x);
			//this._curPositionY = Math.round(pc.y);
			this._nextPositionX = Math.round(pc.x - this.mapControl.getOrigin().x);
			this._nextPositionY = Math.round(pc.y - this.mapControl.getOrigin().y);
			//this.clearSteps();
			this._precomputeSteps();
			this._setFactor(this._zoomTotalSteps);
			
			this.div.style.display = "";
		}else{
			this.div.style.display = "none";
		}
	},
	prepareForZoom:function(offsetX, offsetY, newOriginX, newOriginY, newZoom){
	/// <summary>缩放操作后自定义注记在图层中的位置。
	/// </summary>
	/// <param name="offsetX" type="Number">x方向的偏移量。</param>
	/// <param name="offsetY" type="Number">y方向的偏移量。</param>
	/// <param name="newOriginX" type="Number">缩放后图片新的原点（左上角）的X坐标。</param>
	/// <param name="newOriginY" type="Number">缩放后图片新的原点（左上角）的Y坐标。</param>
	/// <param name="newZoom" type="Number">缩放后地图比例尺。</param>
		this._currentPositionX -= offsetX;
		this._currentPositionY -= offsetY;
		var pc = this.mapControl.getMap().mapCoordToPixel(this._position, newZoom);
		if(pc){
			this._nextPositionX = SuperMap.Utility.round(pc.x - newOriginX);
			this._nextPositionY = SuperMap.Utility.round(pc.y - newOriginY);
			this._precomputeSteps();
			this.div.style.display = "";
		}else{
			this.div.style.display = "none";
		}
	},
	removeFromMap:function(){
	/// <summary>将自定义注记从自定义图层中移除。
	/// </summary>
		if(this.div.parentNode == this._container){
			this._container.removeChild(this.div);}
	}
};
SuperMap.UI.Marker._offsetX = 10;
SuperMap.UI.Marker._offsetY = 10;
SuperMap.UI.Marker._toolTipID = "$SuperMapToolTip";
SuperMap.UI.Marker._toolTip = null;

SuperMap.UI.Marker.registerClass("SuperMap.UI.Marker", null, Sys.IDisposable);

SuperMap.UI.Polyline = function(closed, container){//container
	/// <summary>该类主要描述自定义图层中的自定义线。
	/// </summary>
	/// <param name="closed" type="Boolean">布尔型，表达该线是否构成一个面。True 表示自定义线是闭合线，此时该自定义线构成一个面，False 表示自定义线为非闭合线。</param>
	/// <param name="container" type="Object">装载自定义对象的容器，即 DOM 对象，如 div，img。</param>
	/// <returns type="SuperMap.UI.Polyline">返回 Polyline 对象。</returns>
	/// <field name="container" type="Object">装载自定义对象的容器，即 DOM 对象，如div，img。</field>
	/// <field name="id" type="String">自定义的线的 id。</field>
	/// <field name="strokeWeight" type="Number">线的宽度（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="xs" type="Array" elementType="Number">线的所有节点的 x 坐标。Point2D.x 数组。</field>
	/// <field name="ys" type="Array" elementType="Number">线的所有节点的 y 坐标。Point2D.y 数组。</field>
	/// <field name="fillColor" type="String">闭合线的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。&lt;br&gt;
	/// 当自定义线为闭合线时，该属性有效。</field>
	/// <field name="zIndex" type="Number">自定义线的 Index 值。</field>
	/// <field name="groupID" type="String">分组ID，多条自定义线可以属于同一组。</field>
	/// <field name="fillOpacity" type="Number">闭合线的填充透明度。当自定义线为闭合线时，该属性有效。</field>
	if(!closed){
	    closed=false;
	}
	this.container = container;
	
	this.m_isPolygon = closed;	// false:line, true:polygon.
	this.m_defaultStrokeWeight = "3pt"; this.m_defaultStrokeColor = "#316AC5"; this.m_defaultFillColor = "#316AC5"; this.m_defaultFillOpacity = "0.6"; this.m_defaultZIndex = 4;
	this.m_visible = true; this.m_polyline = null; this.m_drawLayer = null; this.m_jg = null; this.m_fill = null; this.m_stroke = null;
	this.m_lineId = ""; this.m_strokeWeight = ""; this.m_strokeColor = ""; this.m_fillColor = ""; this.m_fillOpacity = ""; this.m_zIndex = 0; this.m_groupID = "";
	this.m_startX = 0;this.m_startY = 0; this.m_endX = 0; this.m_endY = 0;
	this.m_arrayX = null; this.m_arrayY = null;
	
	this.id = -1;
    this.xs = null;
	this.ys = null;
	this.strokeWeight = null;
	this.strokeColor = null;
	this.fillColor = null;
	this.fillOpacity = null;
	this.zIndex = -1;
	this.groupID = -1;
};
SuperMap.UI.Polyline.prototype={
	initialize:function(id, xs, ys, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID, mapControl){
	/// <summary>初始化自定义线。
	/// </summary>
	/// <param name="id" type="String">自定义线的 ID。</param>
	/// <param name="xs" type="Array" elementType="Number">线的所有节点的 x 坐标。Point2D.x 数组。</param>
	/// <param name="ys" type="Array" elementType="Number">线的所有节点的 y 坐标。Point2D.y 数组。</param>
	/// <param name="strokeWeight" type="Number">边线宽度。（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">闭合线的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。&lt;br&gt;
	/// 当自定义线为闭合线时，该属性有效。</param>
	/// <param name="fillOpacity" type="Number">闭合线的填充透明度。当自定义线为闭合线时，该属性有效。</param>
	/// <param name="zIndex" type="Number">面所在的div层的index。</param>
	/// <param name="groupID" type="String">分组ID，多个自定义面可以属于同一组。</param>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl对象的当前范围和比例尺即为该图层的对应值。&lt;br&gt;
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</param>
		if(!strokeWeight && strokeWeight != 0){
		    strokeWeight = this.m_defaultStrokeWeight;
		}else{
		    //确保是个字符串,否则在转化到服务器时会出错
		    strokeWeight+="";
		}
		if(!strokeColor){
		    strokeColor = this.m_defaultStrokeColor;
		}
		if(!fillColor){
		    fillColor = this.m_defaultFillColor;
		}
		if(!fillOpacity && fillOpacity != 0){
		    fillOpacity = this.m_defaultFillOpacity;
		}
		if(!zIndex){
		    zIndex = this.m_defaultZIndex;
		}
		this.id = id; this.m_lineId = id;
		this.xs = xs;
		this.ys = ys;
		this.strokeWeight = strokeWeight;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.fillOpacity = fillOpacity;
		this.zIndex = zIndex;
		this.groupID = groupID;
		this.m_groupID =groupID
		this.m_strokeWeight = strokeWeight; this.m_strokeColor = strokeColor; this.m_fillColor = fillColor; this.m_fillOpacity = fillOpacity; this.m_zIndex = zIndex;
		this.m_arrayX = xs.concat(); this.m_arrayY = ys.concat();
		this.m_startX = this.m_arrayX[0]; this.m_startY = this.m_arrayY[0]; this.m_endX = this.m_arrayX[this.m_arrayX.length - 1]; this.m_endY = this.m_arrayY[this.m_arrayY.length - 1];
		
		this.mapControl = mapControl; 
		
		this.start();
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
        this.removeFromMap();
        if(this.m_jg){
            this.m_jg.clear();
        }
        this.m_arrayX = this.m_arrayY = this.m_polyline = this.m_jg = this.m_drawLayer = this.m_fill = this.m_stroke = null;
    },
	removeFromMap:function(){
	/// <summary>将自定义线从自定义图层中移除。
	/// </summary>
		if(this.m_polyline && this.m_polyline.parentNode != null){
		    this.m_polyline.parentNode.removeChild(this.m_polyline);
		}
		if(this.m_drawLayer && this.m_drawLayer.parentNode != null){
		    this.m_drawLayer.parentNode.removeChild(this.m_drawLayer);
		}
	},
	show:function(){
	/// <summary>将自定义线显示在自定义图层中。
		/// </summary>
	    if(!this.m_visible){
	        this.hide();
	        return;
	    }
	    if(this.m_polyline){
	        this.m_polyline.style.display = "block";
	    }
	    if(this.m_drawLayer){
	        this.m_drawLayer.style.display = "block";
	    }
	},
	hide:function(){
	/// <summary>隐藏自定义线。
	/// </summary>
	    if(this.m_polyline){
	        this.m_polyline.style.display = "none";
	    }
	    if(this.m_drawLayer){
	        this.m_drawLayer.style.display = "none";
	    }
	},
	setVisible:function(bool){
	/// <summary>设置自定义线可见。
	/// </summary>
	    this.m_visible = bool;
	    if(!this.m_visible){
	        this.hide();
	    }
	},
	start:function(){
	/// <summary>开始绘制自定义线。
	/// </summary>
	    this._drawPolyline();
	},
	update:function(){
	/// <summary>更新自定义线。
	/// </summary>
	    this._drawPolyline();
	},
	reposition:function(){
	/// <summary>重定位自定义线。
	/// </summary>
	    this.start();
	},

	_drawPolyline:function(){
	    var ps = new Array();
	    this._generatePoints(ps);
	    this._createPolyline(ps);
	    ps = null;
	},
	_generatePoints:function(ps){
		var mc = new SuperMap.Point2D();
		for(var i = 0; i < this.m_arrayX.length; i++){
			mc.x = this.m_arrayX[i]; mc.y = this.m_arrayY[i];
			var pc = this.mapControl.mapCoordToPixel(mc);
			pc.x = pc.x + this.mapControl.getOffset().x;
			pc.y = pc.y + this.mapControl.getOffset().y;
			ps.push(pc.x);
			ps.push(pc.y);
		}
		if(this.m_isPolygon){
			mc.x = this.m_arrayX[0]; mc.y = this.m_arrayY[0];
			var pc = this.mapControl.mapCoordToPixel(mc);
			pc.x = pc.x + this.mapControl.getOffset().x;
			pc.y = pc.y + this.mapControl.getOffset().y;
			ps.push(pc.x);
			ps.push(pc.y);
		}
		mc = null;
	},
	_selectGroup:function(groupID, curObject){
	    if(!groupID){
	        groupID = "unClassified"
	    }
	    var groupDiv = document.getElementById(groupID);
	    if(!groupDiv){
	        groupDiv = document.createElement("div");
	        groupDiv.id = groupID;
	        curObject.parentNode.appendChild(groupDiv);
	    }
	    if(curObject.parentNode){
	        curObject.parentNode.removeChild(curObject);
	    }
	    groupDiv.appendChild(curObject);
	},
	_createPolyline:function(ps){
	   	if(_GetBrowser() != "ie"){
			if(!this.m_drawLayer){
				this.m_drawLayer = document.createElement("div");
				this.m_drawLayer.id = this.m_lineId;
				this.m_drawLayer.style.position = "absolute";
				this.m_drawLayer.style.left = "0px";
				this.m_drawLayer.style.top = "0px";
				this.m_drawLayer.unselectable = "on";
				
				this.container.appendChild(this.m_drawLayer);
				
				//this._selectGroup(this.m_groupID, this.m_drawLayer);
				
				this.m_jg = new jsGraphics(this.m_lineId);
				this.m_jg.setColor(this.m_strokeColor); 
				this.m_jg.setStroke(this.m_strokeWeight);
			}else{
			    this.m_jg.clear();
			}
			this.m_drawLayer.style.zIndex = this.m_zIndex;
			this.m_drawLayer.style.opacity = this.m_fillOpacity;
			if(this.m_visible){
			    this.m_drawLayer.style.display = "block";
			    this.m_drawLayer.style.visibility = "visible";
			}else{
			    this.m_drawLayer.style.display = "none";
			    this.m_drawLayer.style.visibility = "";
			}
			var pxs = SuperMap.Utility.splitX(ps);
			var pys = SuperMap.Utility.splitY(ps);
			if(this.m_isPolygon){
				this.m_jg.setColor(this.m_fillColor);
				this.m_jg.fillPolygon(pxs, pys);
				this.m_jg.setColor(this.m_strokeColor);
				this.m_jg.drawPolygon(pxs, pys);
			}else{
				this.m_jg.setColor(this.m_strokeColor);
				this.m_jg.drawPolyline(pxs, pys);
			}
			this.m_jg.paint();
			
			return false;
		}
	    var created = false; 
	    if(this.m_polyline){
	        created = true;
	    }
		var pointsValue = ps.join(",");
        if(!created){
		    this.m_polyline = document.createElement("v:polyline");
    		this.m_polyline.setAttribute("points", pointsValue);

			this.container.appendChild(this.m_polyline);
		}else{
            this.m_polyline.points.value = pointsValue;
        }
		this.m_polyline.id = this.m_lineId;
		if(this.m_isPolygon){
		    this.m_polyline.filled = "true";
		}else{
		    this.m_polyline.filled="false";
		}
		this.m_polyline.style.zIndex = this.m_zIndex;
		this.m_polyline.unselectable = "on";
		this.m_polyline.style.position = "absolute";
		this.m_polyline.style.border = "0px";
		// this.m_polyline.style.filter = "alpha(opacity=" + this.m_fillOpacity * 100 + ")";
		if(this.m_visible){
		    this.m_polyline.style.display = "block";
		}else{
		    this.m_polyline.style.display = "none";
		}
		
		if(this.m_isPolygon){
			if(!created){
				this.m_fill = document.createElement("<v:fill opacity=" + this.m_fillOpacity + " color=" + this.m_fillColor + "></v:fill>");
				this.m_polyline.appendChild(this.m_fill);
			}
			//this.m_fill.setAttribute("opacity", this.m_fillOpacity);
			//this.m_fill.setAttribute("color", this.m_fillColor);
		}
		if(this.strokeWeight > 0) {
			if(!created){
				this.m_stroke = document.createElement("v:stroke");
				this.m_polyline.appendChild(this.m_stroke);
			}
			this.m_stroke.setAttribute("weight", this.m_strokeWeight);
			this.m_stroke.setAttribute("color", this.m_strokeColor);
			this.m_stroke.setAttribute("joinstyle", "round");
			this.m_stroke.setAttribute("endcap", "round");
			this.m_stroke.setAttribute("opacity", "0.75");
		}
	    if(!created){
    		//this._selectGroup(this.m_groupID, this.m_polyline);
    	}
	}	
};
SuperMap.UI.Polyline.registerClass("SuperMap.UI.Polyline", null, Sys.IDisposable);

SuperMap.UI.Circle = function(container){//container
	/// <summary>该类主要描述自定义图层中的自定义圆。
	/// </summary>
	/// <param name="container" type="Object">装载自定义对象的容器，即DOM对象，如div，img。</param>
	/// <returns type="SuperMap.UI.Circle">返回 Circle 对象。</returns>
	/// <field name="container" type="Object">装载自定义对象的容器，即DOM对象，如div，img。</field>
	/// <field name="id" type="String">自定义的线的id。</field>
	/// <field name="strokeWeight" type="Number">线的宽度（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="fillColor" type="String">闭合线的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。&lt;br&gt;
	/// 当自定义线为闭合线时，该属性有效。</field>
	/// <field name="zIndex" type="Number">自定义圆的 Index 值。</field>
	/// <field name="groupID" type="String">分组ID，多个自定义圆可以属于同一组。</field>
	/// <field name="fillOpacity" type="Number">闭合线的填充透明度。当自定义线为闭合线时，该属性有效。</field>
	this.container = container;
	
	this.m_defaultStrokeWeight = "3pt"; 
	this.m_defaultStrokeColor = "#316AC5"; 
	this.m_defaultFillColor = "#316AC5"; 
	this.m_defaultFillOpacity = "0.6"; 
	this.m_defaultZIndex = 4;
	this.m_visible = true; 
	this.m_circle = null; 
	this.m_jg = null; 
	this.m_fill = null; 
	this.m_stroke = null;
	this.m_circleId = ""; 
	this.m_strokeWeight = ""; 
	this.m_strokeColor = ""; 
	this.m_fillColor = ""; 
	this.m_fillOpacity = ""; 
	this.m_zIndex = 0; 
	this.m_groupID = "";
	this.m_startPoint = null;
	this.m_endPoint = null; 
	
	this.id = -1;
	this.strokeWeight = null;
	this.strokeColor = null;
	this.fillColor = null;
	this.fillOpacity = null;
	this.zIndex = -1;
	this.groupID = -1;
};
SuperMap.UI.Circle.prototype={
	initialize:function(id, startPoint, endPoint, strokeWeight, strokeColor, fillColor, fillOpacity, zIndex, groupID, mapControl){
	/// <summary>初始化自定义圆。
	/// </summary>
	/// <param name="id" type="String">自定义圆的 ID。</param>
	/// <param name="startPoint" type="SuperMap.Point2D">画圆的起点坐标。</param>
	/// <param name="endPoint" type="SuperMap.Point2D">画圆的终点坐标。</param>
	/// <param name="strokeWeight" type="Number">边线宽度。（显示宽度），以像素（px）为单位。</param>
	/// <param name="strokeColor" type="String">边线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</param>
	/// <param name="fillColor" type="String">闭合线的填充颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。&lt;br&gt;
	/// 当自定义线为闭合线时，该属性有效。</param>
	/// <param name="fillOpacity" type="Number">闭合线的填充透明度。当自定义线为闭合线时，该属性有效。</param>
	/// <param name="zIndex" type="Number">面所在的div层的index。</param>
	/// <param name="groupID" type="String">分组ID，多个自定义面可以属于同一组。</param>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。该参数主要用于给客户端自定义图层提供一个坐标参照系，
	/// mapControl对象的当前范围和比例尺即为该图层的对应值。&lt;br&gt;
	/// 该参数也决定了客户端自定义图层的最大范围等信息。</param>
		if(!strokeWeight && strokeWeight != 0){
		    strokeWeight = this.m_defaultStrokeWeight;
		}else{
		    //确保是个字符串,否则在转化到服务器时会出错
		    strokeWeight+="";
		}
		if(!strokeColor){
		    strokeColor = this.m_defaultStrokeColor;
		}
		if(!fillColor){
		    fillColor = this.m_defaultFillColor;
		}
		if(!fillOpacity && fillOpacity != 0){
		    fillOpacity = this.m_defaultFillOpacity;
		}
		if(!zIndex){
		    zIndex = this.m_defaultZIndex;
		}
		this.id = id; 
		this.m_circleId = id;
		this.strokeWeight = strokeWeight;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.fillOpacity = fillOpacity;
		this.zIndex = zIndex;
		this.groupID = groupID;
		this.m_groupID =groupID
		this.m_strokeWeight = strokeWeight; 
		this.m_strokeColor = strokeColor; 
		this.m_fillColor = fillColor; 
		this.m_fillOpacity = fillOpacity; 
		this.m_zIndex = zIndex;

		this.m_startPoint = startPoint;
		this.m_endPoint = endPoint; 
		
		this.mapControl = mapControl; 
		
		this.start();
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
        this.removeFromMap();
        if(this.m_jg){
            this.m_jg.clear();
        }
		//xianchao这两个方法不存在
        //this.m_startPoint.dispose();
        //this.m_endPoint.dispose();
        this.m_startPoint = this.m_endPoint = this.m_circle = this.m_jg = this.m_drawLayer = this.m_fill = this.m_stroke = null;
    },
	removeFromMap:function(){
	/// <summary>将自定义圆从自定义图层中移除。
	/// </summary>
		if(this.m_circle && this.m_circle.parentNode != null){
		    this.m_circle.parentNode.removeChild(this.m_circle);
		}
	},
	show:function(){
	/// <summary>将自定义圆显示在自定义图层中。
		/// </summary>
	    if(!this.m_visible){
	        this.hide();
	        return;
	    }
	    if(this.m_circle){
	        this.m_circle.style.display = "block";
	    }
	},
	hide:function(){
	/// <summary>隐藏自定义圆。
	/// </summary>
	    if(this.m_circle){
	        this.m_circle.style.display = "none";
	    }
	},
	setVisible:function(bool){
	/// <summary>设置自定义圆可见。
	/// </summary>
	    this.m_visible = bool;
	    if(!this.m_visible){
	        this.hide();
	    }
	},
	start:function(){
	/// <summary>开始绘制自定义圆。
	/// </summary>
	    this._drawCircle();
	},
	update:function(){
	/// <summary>更新自定义圆。
	/// </summary>
	    this._drawCircle();
	},
	reposition:function(){
	/// <summary>重定位自定义圆。
	/// </summary>
	    this.start();
	},
	
	_drawCircle:function(){
		//this.m_circle = document.getElementById(this.m_circleId);
		if(_GetBrowser() != "ie"){
			if(!this.m_circle){
				this.m_circle = document.createElement("div");
				this.m_circle.id = this.m_circleId;
				this.m_circle.style.position = "absolute";
				this.m_circle.style.left = "0px";
				this.m_circle.style.top = "0px";
				this.m_circle.unselectable = "on";
				this.container.appendChild(this.m_circle);
				this.m_jg = new jsGraphics(this.m_circleId);
				this.m_circle.jg = this.m_jg;
			} else {
				this.m_circle.jg.clear();
			}
			this.m_circle.jg.setColor(this.m_strokeColor);
			this.m_circle.jg.setStroke(this.m_strokeWeight);
			this.m_circle.style.zIndex = this.m_zIndex;
			this.m_circle.style.opacity = this.m_fillOpacity;
			var startPc = this.mapControl.mapCoordToPixel(this.m_startPoint);
			var endPc = this.mapControl.mapCoordToPixel(this.m_endPoint);
		    startPc.x = startPc.x + this.mapControl.getOffset().x;
		    startPc.y = startPc.y + this.mapControl.getOffset().y;
		    endPc.x = endPc.x + this.mapControl.getOffset().x;
		    endPc.y = endPc.y + this.mapControl.getOffset().y;

			var radius = Math.sqrt(Math.pow((endPc.x - startPc.x), 2) + Math.pow((endPc.y - startPc.y), 2));
			this.m_circle.jg.drawEllipse(startPc.x-radius,startPc.y-radius, radius*2, radius*2);
			this.m_circle.jg.paint();
			return;
		}
		if(!this.m_circle){
			SuperMap.Utility.enableVML();
			this.m_circle = document.createElement("<v:arc startangle='0' endangle='360'/>");
			this.m_circle.style.position = "absolute";
			this.m_circle.style.visibility = 'visible';
			this.m_circle.id = this.m_circleId;
			this.m_circle.style.zIndex = this.m_zIndex;
			var fill = document.createElement("<v:fill opacity=" + this.m_fillOpacity + "></v:fill>");
			fill.setAttribute("color", this.m_fillColor);
			this.container.appendChild(this.m_circle);
			this.m_circle.appendChild(fill);
			if(this.strokeWeight.valueOf() > 0) {
				var stroke = document.createElement("<v:stroke dashstyle='solid' Color='" + this.m_strokeColor+ "'></v:stroke>");
				stroke.setAttribute("weight", this.m_strokeWeight);
				this.m_circle.appendChild(stroke);
			}
		} 
		var startPc = this.mapControl.mapCoordToPixel(this.m_startPoint);
		var endPc = this.mapControl.mapCoordToPixel(this.m_endPoint);
		startPc.x = startPc.x + this.mapControl.getOffset().x;
		startPc.y = startPc.y + this.mapControl.getOffset().y;
		endPc.x = endPc.x + this.mapControl.getOffset().x;
		endPc.y = endPc.y + this.mapControl.getOffset().y;

		var radius = Math.sqrt(Math.pow((endPc.x - startPc.x), 2) + Math.pow((endPc.y - startPc.y), 2));
		this.m_circle.style.left = (startPc.x - radius) + "px";
		this.m_circle.style.top = (startPc.y - radius) + "px";
		this.m_circle.style.width = 2 * radius + "px";
		this.m_circle.style.height =  this.m_circle.style.width;
	}
};
SuperMap.UI.Circle.registerClass("SuperMap.UI.Circle", null, Sys.IDisposable);

/*
// CustomDiv - Line, Polygon
function _ShowPolylines(){
	for(var i=0;i<_lines.length;i++){_lines[i].Start();_lines[i].Show();}
	for(var i=0;i<_polygons.length;i++){_polygons[i].Start();_polygons[i].Show();}
}
function _HidePolylines(){
	for(var i=0;i<_lines.length;i++){_lines[i].Hide();}
	for(var i=0;i<_polygons.length;i++){_polygons[i].Hide();}
}
function _UpdatePolylines(){
	for(var i=0;i<_lines.length;i++){_lines[i].Update();}
	for(var i=0;i<_polygons.length;i++){_polygons[i].Update();}
}
function _CloseInfoWindow(id)
{
	_RemoveMark(id);
}
function _OpenInfoWindow(id,x,y,width,height,title,content,opacity)
{
	var zIndex="1000";
	var left = 0;
	var top = 0;
	if(!x){return;}
	if(!y){return;}
	if(!width){width=100;}
	if(!height){height=100;}
	if(!opacity){opacity=0.5}
	//最小50X50吧.
	//todo:随文本而决定大小?
	if(width<50){width=50;}
	if(height<50){height=50;}
	if(!title){title="title";}
	if(!content){content="content"}
	var normal="white";
	var str = ""
                + "<div "
                + "style='"
				+ "filter:alpha(opacity="+(opacity)*100+");"
				+ "opacity:"+opacity+";"
                + "z-index:" + zIndex + ";"
                + "width:" + width*2 + "px;"
                + "height:" +height + "px;"
				+ "left:" +left + "px;"
                + "top:" + top + "px;"
				+ "color:" + normal + ";"
                + "font-size:12px;"
                + "font-family:Verdana;"
                + "position:absolute;"
                + "cursor:default;"
                + "border:0px solid white;"
				+ "' "
				+" onclick=SuperMap.Utility.cancelBubble(window.event) "
				+" ondblclick =SuperMap.Utility.cancelBubble(window.event) "
				+" onmousemove =SuperMap.Utility.cancelBubble(window.event) "
				+ ">"
						//shadow
						+ "<div style='position:absolute;left:"+width/10+"px;top:"+height/2+"px;width:70%;height:50%;z-index:"+(zIndex-3)+";'>"
						+ "<img src='images/shadow.gif' style='width:100%;height:100%;' />"
						+ "</div>"
						//image 
						+ "<div style='position:absolute;left:0px;top:0px;width:50%;height:100%;z-index:"+(zIndex-2)+";'>"
						+"<img src='images/form-white.gif' style='width:100%;height:100%;' />"
						+"</div>"
						//title
						+ "<div style='position:absolute;left:0px;top:0px;z-index:"+(zIndex-1)+";width:100%;height:20px;>"
						+ "<table border=0 cellspacing=0 cellpadding=0>"
						+ "<tr><td style='width="+width+"'><span style='width:" + (width-12) + ";color:orange;padding-left:3px;font-size:12px' >" + title+ "</span></td>"
			            + "<td align=right style='width:12'><span style='position:absolute;left:"+(width-10)+"px;width:12px;border-width:0px;color:orange;' onclick='var infoWindow=document.getElementById(\""+id+"\");infoWindow.parentNode.removeChild(infoWindow);infoWindow=null;'>X</span>"
						+ "</td>"
                        + "</tr>"
                        + "</table>"
						//content
						+ "<div style='"
						+ "poistion:absolute;"
						+ "width:"+(width-12)+"px;"
						+ "height:" + (height*0.7-20-4) + "px;"
						+ "color: orange;"
						+ "line-height:14px;"
						+ "word-break:break-all;"
						+ "padding:3px;"
						+ "overflow:hidden"
						+ "'>" + content + "</div>"
						//+ "</div>"		
                + "</div>";
	_InsertMark(id,x,y,width,height*2,str);
	
}*/