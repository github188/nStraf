//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.UI.Action.js  
// 功能：			Action 相关类和接口
// 最后修改时间：	2007-08-23
//========================================================================== 
Type.registerNamespace('SuperMap.UI');

SuperMap.UI.Action = function(){
	/// <summary>Action类是PanAction、ZoomInAction等与地图进行交互的操作类的基类。&lt;br&gt;
	/// 所有Action的类都是完成通过鼠标与地图进行交互的操作，如PanAction用来描述通过鼠标进行地图平移操作的类。
	/// 用户也可以开发一个Action子类，继承该基类，实现特定的GIS功能。
	/// </summary>
	/// <returns type="SuperMap.UI.Action">返回 Action 对象。</returns>
	/// <field name="title" type="String">交互操作的名称。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "";
    this._type = "Action";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type,[]);
    this._mapControl = null;
};

SuperMap.UI.Action.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化Action。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
        this._mapControl = mapControl;
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl = null;
    },
    
    onClick:function(e){
	/// <summary>在地图控件上鼠标单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.Action.registerClass('SuperMap.UI.Action', Sys.Component, Sys.IDisposable);

SuperMap.UI.PanAction = function(){
	/// <summary>该类完成通过鼠标平移地图的操作。
	/// </summary>
	/// <returns type="SuperMap.UI.PanAction">返回 PanAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示鼠标平移地图的操作是否开始。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "平移";
    this._type = "PanAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type,[]);
	this.actionStarted = false;
	this._lastMouseX;
	this._lastMouseY;
};

SuperMap.UI.PanAction.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化PanAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
		    this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/Pan.cur";
		}else{
		    this._mapControl.get_container().style.cursor = "move";
		}
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl = null;
    },
    
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this.actionStarted = true;
		this._lastMouseX = SuperMap.Utility.getMouseX(e);
		this._lastMouseY = SuperMap.Utility.getMouseY(e);
		if(this._mapControl.get_container().setCapture){
		    this._mapControl.get_container().setCapture();
		}
		this._mapControl.keepCurrentImage();
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
        this._mapControl.pan(this._lastMouseX - mouseX, this._lastMouseY - mouseY);
        this._lastMouseX = mouseX;
        this._lastMouseY = mouseY;
    },
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this.actionStarted = false;
		this._mapControl.stopDynamicPan();
        if(this._mapControl.get_container().releaseCapture){
            this._mapControl.get_container().releaseCapture();
        }
        this._mapControl.refreshCurrentImage();
    }
};
SuperMap.UI.PanAction.registerClass('SuperMap.UI.PanAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.ZoomInAction = function(){
	/// <summary>该类完成通过鼠标进行地图放大的操作。
	/// </summary>
	/// <returns type="SuperMap.UI.ZoomInAction">返回 ZoomInAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示地图放大的操作是否开始。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "拉框放大";
    this._type = "ZoomInAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, []);
    this._mapDiv = null;
	this._zoomRect = null;
	this._cx = 0, this._cy = 0, this._nx = 0, this._ny = 0;
	this._originX = 0; this._originY = 0;
	this.actionStarted = false;
};

SuperMap.UI.ZoomInAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化ZoomInAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
	    this._mapControl = mapControl;
	    this._mapDiv = this._mapControl.get_mapDiv();
		if(_GetBrowser() == "ie"){
    		this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/ZoomIn.cur";
        }else{
            this._mapControl.get_container().style.cursor = "crosshair";
        };
		this._zoomRect = $get('zoomRect');
		if(!this._zoomRect){
		    this._zoomRect = document.createElement("div");
			this._zoomRect.id = this._mapControl.get_id() + "_zoomRect";
		    this._zoomRect.className = "zoomRect";
		    this._hide();
    		this._mapDiv.appendChild(this._zoomRect);
		}
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
	    if(this._zoomRect){
	        this._mapDiv.removeChild(this._zoomRect);
	    }
	    this._mapDiv = null;
	    this._mapControl = null;
	},
	onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    this.actionStarted = true;
	    this._originX = e.pixelCoord.x - e.offsetCoord.x;
	    this._originY = e.pixelCoord.y - e.offsetCoord.y;
		this._cx = this._nx = e.offsetCoord.x;
		this._cy = this._ny = e.offsetCoord.y;
	},
	onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    if(!this.actionStarted){
	        return;
	    }
		this._nx = e.offsetCoord.x;
		this._ny = e.offsetCoord.y;
		this._draw(SuperMap.Utility.min(this._cx, this._nx), SuperMap.Utility.min(this._cy, this._ny), SuperMap.Utility.max(1, SuperMap.Utility.abs(this._nx - this._cx)), SuperMap.Utility.max(1, SuperMap.Utility.abs(this._ny - this._cy)));
		this._show();
	},
	onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    this.actionStarted = false;
		if(SuperMap.Utility.abs(this._cx - this._nx) > 1 && SuperMap.Utility.abs(this._cy - this._ny) > 1){
			var param = this._mapControl.getMapParam();
			param.setPixelRect(new SuperMap.Rect(this._originX + this._cx, this._originY + this._cy, this._originX + this._nx, this._originY + this._ny));
			this._mapControl.setMapParam(param);
		}
		else{
			this._mapControl.setCenterAndZoom(new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y), this._mapControl.get_mapScale() * 2);
		}
		this._hide();
	},
	_draw:function(x, y, width, height){
	    this._setPosAndSize(this._zoomRect, x, y, width, height);
	},
	_setPosAndSize:function(el, x, y, width, height){
	    el.style.left = x + "px";
	    el.style.top = y + "px";
	    el.style.width = width + "px";
	    el.style.height = height + "px";
	},
	_show:function(){
	    this._zoomRect.style.display = "block";
	},
	_hide:function(){
	    this._zoomRect.style.display = "none";
	}
};

SuperMap.UI.ZoomInAction.registerClass('SuperMap.UI.ZoomInAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.ZoomOutAction = function(){
	/// <summary>该类完成通过鼠标进行地图缩小的操作。
	/// </summary>
	/// <returns type="SuperMap.UI.ZoomOutAction">返回 ZoomOutAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示地图缩小的操作是否开始。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "拉框缩小";
    this._type = "ZoomOutAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, []);
    this._mapDiv = null;
	this._zoomRect = null;
	this._cx = 0, this._cy = 0, this._nx = 0, this._ny = 0;
	this._originX = 0;
	this._originY = 0;
	this.actionStarted = false;
};

SuperMap.UI.ZoomOutAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化ZoomOutAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		this._mapDiv = this._mapControl.get_mapDiv();
		if(_GetBrowser() == "ie"){
		    this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/ZoomOut.cur";
		}else{
		    this._mapControl.get_container().style.cursor = "crosshair";
		}
		this._zoomRect = $get('zoomRect');
		if(!this._zoomRect){
		    this._zoomRect = document.createElement("div");
			this._zoomRect.id = this._mapControl.get_id() + "_zoomRect";
		    this._zoomRect.className="zoomRect";
		    this._hide();
    		this._mapDiv.appendChild(this._zoomRect);
		}
	},
	
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
	    this._mapDiv.removeChild(this._zoomRect);
	    this._mapDiv = null;
	    this._mapControl = null;
	},
	
	onMouseDown:function (e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    this.actionStarted = true;
	    this._originX = e.pixelCoord.x - e.offsetCoord.x;
	    this._originY = e.pixelCoord.y - e.offsetCoord.y;
		this._cx = this._nx = e.offsetCoord.x;
		this._cy = this._ny = e.offsetCoord.y;
	},
	
	onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    if(!this.actionStarted){
	        return;
	    }
		this._nx = e.offsetCoord.x;
		this._ny = e.offsetCoord.y;
		this._draw(SuperMap.Utility.min(this._cx, this._nx) + 2, SuperMap.Utility.min(this._cy, this._ny) + 2, SuperMap.Utility.max(5, SuperMap.Utility.abs(this._nx - this._cx)) - 4, SuperMap.Utility.max(5, SuperMap.Utility.abs(this._ny - this._cy)) - 4);
		this._show();
	},
	
	onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    this.actionStarted = false;
		if(SuperMap.Utility.abs(this._cx - this._nx) > 1 && SuperMap.Utility.abs(this._cy - this._ny) > 1){
			var param = this._mapControl.getMapParam();
			// 用下面的方法，反画框会放大
			var tpr = new SuperMap.Rect(this._originX + this._cx, this._originY + this._cy, this._originX + this._nx, this._originY + this._ny);
			var	pr = new SuperMap.Rect();
			pr.copy(tpr);
			var mapSize = this._mapControl.getSize();
			var width = mapSize.width() * mapSize.width() / tpr.width();
			var height = mapSize.height() * mapSize.height() / tpr.height();
			pr.leftTop.x = tpr.leftTop.x - (width - tpr.width()) / 2;
			pr.rightBottom.x = tpr.rightBottom.x + (width - tpr.width()) / 2;
			pr.leftTop.y = tpr.leftTop.y - (height - tpr.height()) / 2;
			pr.rightBottom.y = tpr.rightBottom.y + (height - tpr.height()) / 2;
			param.setPixelRect(pr);
			this._mapControl.setMapParam(param);
		}else{
			this._mapControl.setCenterAndZoom(new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y), this._mapControl.get_mapScale() / 2);
		}
		this._hide();
	},
	
	_draw:function(x, y, width, height){
	    this._setPosAndSize(this._zoomRect, x, y, width, height);
	},
	
	_setPosAndSize:function(el, x, y, width, height){
		el.style.left = x + "px";
	    el.style.top = y + "px";
	    el.style.width = width + "px";
	    el.style.height = height + "px";
	},
	
	_show:function(){
	    this._zoomRect.style.display = "block";
	},
	
	_hide:function(){
	    this._zoomRect.style.display = "none";
	}
};

SuperMap.UI.ZoomOutAction.registerClass('SuperMap.UI.ZoomOutAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.DrawLineAction = function(){
	/// <summary>该类完成通过鼠标在地图中绘制线段的操作。
	/// </summary>
	/// <returns type="SuperMap.UI.DrawLineAction">返回 DrawLineAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示绘制线段的操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "绘制线段";
    this._type = "DrawLineAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type,[]);
    this._xs = new Array();
    this._ys = new Array();
	this.strokeColor = "red";
	this.strokeWeight = 3;
	this.actionStarted = false;
 };

SuperMap.UI.DrawLineAction.prototype = {
   initialize:function(mapControl){
   	/// <summary>初始化DrawLineAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
    	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
            this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/LineQuery.cur";
        }else{
            this._mapControl.get_container().style.cursor = "pointer";
        };
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._xs = null;
        this._ys = null;
        this._mapControl = null;
    },
    
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
			while(this._xs.length > 0) {
				this._xs.pop();
				this._ys.pop();
			}
			this.actionStarted = true;
		} else {
			this._xs.pop();
			this._ys.pop();
		}
		var offsetCoord = e.mapCoord;
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        
        this._mapControl.customLayer.insertLine("drawLine", this._xs, this._ys, this.strokeWeight, this.strokeColor);
    },
    
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(e.button == 2 && this.actionStarted){
            while(this._xs.length > 0){
                this._xs.pop();
            }
            while(this._ys.length > 0){
                this._ys.pop();
            }
        }
    },
	
	onMouseMove:function(e){
	/// <summary>在地图控件上鼠标移动事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){
            return;
        }
		this._xs.pop();
		this._ys.pop();
        var offsetCoord = e.mapCoord;
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		
		this._mapControl.customLayer.insertLine("drawLine", this._xs, this._ys, this.strokeWeight, this.strokeColor);
    },
    
	onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
		    return false;
		}
		this.actionStarted = false;
  	},
	
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    }
};
SuperMap.UI.DrawLineAction.registerClass('SuperMap.UI.DrawLineAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.DrawPolygonAction = function(){
	/// <summary>该类完成通过鼠标在地图中绘制多边形的操作。
	/// </summary>
	/// <returns type="SuperMap.UI.DrawPolygonAction">返回 DrawPolygonAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示绘制多边形的操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "绘制多边形";
    this.strokeColor = "red";
	this.strokeWeight = 3;
	this._type = "DrawPolygonAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type,[]);
    this._xs = new Array();
    this._ys = new Array();
	this.actionStarted = false;
};
SuperMap.UI.DrawPolygonAction.prototype = {
   initialize:function(mapControl){
   	/// <summary>初始化DrawPolygonAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
    	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
            this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PolygonQuery.cur";
        }else{
            this._mapControl.get_container().style.cursor = "pointer";
        };
    },
    
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._xs = null;
        this._ys = null;
        this._mapControl = null;
    },
    
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
			while(this._xs.length > 0) {
				this._xs.pop();
				this._ys.pop();
			}
			this.actionStarted = true;
		} else {
			this._xs.pop();
			this._ys.pop();
		}
		var offsetCoord = e.mapCoord;
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("drawPolygon", this._xs, this._ys, this.strokeWeight, this.strokeColor);
    },
    
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	
        if(e.button == 2 && this.actionStarted){
            while(this._xs.length > 0){
                this._xs.pop();
            }
            while(this._ys.length > 0){
                this._ys.pop();
            }
        }
    },
	
	onMouseMove:function(e){
	/// <summary>在地图控件上鼠标移动事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){
            return;
        }
		this._xs.pop();
		this._ys.pop();
        var offsetCoord = e.mapCoord;
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        
		this._mapControl.customLayer.insertPolygon("drawPolygon", this._xs, this._ys, this.strokeWeight, this.strokeColor);
    },
	
	onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
		    return false;
		}
		this.actionStarted = false;
  	},
    
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    }
};
SuperMap.UI.DrawPolygonAction.registerClass('SuperMap.UI.DrawPolygonAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.MeasureDistanceAction = function(onComplete, onError, onStart){
	/// <summary>该类完成通过鼠标在地图中量算距离的操作。&lt;br&gt;
	/// 通过鼠标量算距离的操作过程如下：&lt;br&gt;
	/// 在地图窗口通过鼠标绘制要计算距离的线段（直线或者折线），鼠标双击左键完成线段的绘制后，该线段的总长度就可以获得。
	/// </summary>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.MeasureResult"&gt;MeasureResult&lt;/see&gt; measureResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">用于操作完毕、向服务端提交开始时的事件处理函数。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.MeasuringEventArgs"&gt;MeasuringEventArgs&lt;/see&gt; measuringEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.MeasureDistanceAction">返回 MeasureDistanceAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示距离量算的操作是否开始。</field>
	/// <field name="type" type ="String">交互操作的类型，其值为 MeasureDistanceAction。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	this.title = "距离量算";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
    this._type = "MeasureDistanceAction";
    this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [onComplete, onError, onStart]);
	this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
	this.actionStarted = false;
	
    //onComplete, onError, onStart
    this._onStart = onStart;
    this._onComplete = onComplete;
    this._onError = onError;
};
SuperMap.UI.MeasureDistanceAction.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化MeasureDistanceAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
    	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
		    this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/MeasureDistance.cur";
		}else{
		    this._mapControl.get_container().style.cursor = "pointer";
		};
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this.actionStarted = false;
        this._mapControl.customLayer.removeLine("MeasureDistance");
        this._mapControl = null;
    },
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			this._keyPoints.push(offsetCoord);
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(offsetCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
  	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
		    return false;
		}
		this._keyPoints.push(offsetCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		//客户端量算前事件
		var me = new SuperMap.MeasuringEventArgs();
        me.clientActionArgs = new SuperMap.ActionEventArgs();
        me.clientActionArgs.mapCoords = new Array();
        for(var i = 0; i < this._keyPoints.length; i++){
            me.clientActionArgs.mapCoords[i] = this._keyPoints[i];
        }
		if(this._onStart){
		    this._onStart(me);
		}
        
        this.measureDistance(me);
  	},
   	measureDistance:function (me){
	/// <summary>量算距离。</summary>
	/// <param name="me" type="SuperMap.MeasuringEventArgs">为 Measuring 事件提供数据。</param>
   	    this._mapControl.customLayer.removeLine("MeasureDistance");
		this._mapControl.getMap().measureDistance(me.clientActionArgs.mapCoords, true, this._onComplete, this._onError);
		
		while(this._keyPoints.length > 0){
		   this._keyPoints.pop();
		   this._xs.pop();
		   this._ys.pop();
	   	}
		this.actionStarted = false;
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
		    return false;
		}
  	    var offsetCoord = e.mapCoord;
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(offsetCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertLine("MeasureDistance", this._xs, this._ys, this.strokeWeight, this.strokeColor);
	}
};
SuperMap.UI.MeasureDistanceAction.registerClass('SuperMap.UI.MeasureDistanceAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.MeasureAreaAction = function(onComplete,onError,onStart){
	/// <summary>该类完成通过鼠标在地图中量算某区域的面积的操作。&lt;br&gt;
	/// 通过鼠标量算面积的操作过程如下：&lt;br&gt;
	/// 在地图窗口通过鼠标绘制要量算的区域，双击鼠标左键结束鼠标绘制量算区域的操作后，
	/// MapControl就会计算绘制的区域的面积，最终给用户返回面积值。
	/// </summary>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.MeasureResult"&gt;MeasureResult&lt;/see&gt; measureResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">用于操作完毕、向服务端提交开始时的事件处理函数。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.MeasuringEventArgs"&gt;MeasuringEventArgs&lt;/see&gt; measuringEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.MeasureAreaAction">返回 MeasureAreaAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示面积量算的操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "面积量算";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this._type = "MeasureAreaAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type,[onComplete,onError,onStart]);
	this._keyPoints = new Array();
    this._xs=new Array();
    this._ys=new Array();
	this.actionStarted=false;
    this._firstMapCoord = null;

    //onComplete, onError, onStart
    this._onStart = onStart;
    this._onComplete = onComplete;
    this._onError = onError;
};
SuperMap.UI.MeasureAreaAction.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化MeasureAreaAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
    	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
		    this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/MeasureArea.cur";
		}else{
		    this._mapControl.get_container().style.cursor = "pointer";
		};
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl.customLayer.removePolygon("MeasureArea");
        this._mapControl = null;
    },
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
  	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			this._firstMapCoord = e.mapCoord;
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}else{
			this._xs.pop();
			this._ys.pop();
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(offsetCoord);
		this._xs.push(this._firstMapCoord.x);
		this._ys.push(this._firstMapCoord.y);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
  	    var offsetCoord = e.mapCoord;
		this._keyPoints.push(e.mapCoord);
		this._keyPoints.push(this._firstMapCoord);
		this._xs.pop();
		this._ys.pop();
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._xs.push(this._firstMapCoord.x);
		this._ys.push(this._firstMapCoord.y);
		
		//客户端量算前事件
		var me = new SuperMap.MeasuringEventArgs();
        me.clientActionArgs = new SuperMap.ActionEventArgs();
        me.clientActionArgs.mapCoords = new Array();
        for(var i = 0; i < this._keyPoints.length; i++){
            me.clientActionArgs.mapCoords[i] = this._keyPoints[i];
        }
		if(this._onStart){
		    this._onStart(me);
		}
		
		this.measureArea(me);
   	},
   	measureArea:function(me){
	/// <summary>量算面积。</summary>
	/// <param name="me" type="SuperMap.MeasuringEventArgs">为 Measuring 事件提供数据。</param>	
   	    this._mapControl.customLayer.removePolygon("MeasureArea");
		this._mapControl.getMap().measureArea(this._keyPoints, true, this._onComplete, this._onError);
		while(this._keyPoints.length > 0){
		   this._keyPoints.pop();
		   this._xs.pop();
		   this._ys.pop();
		   this._firstMapCoord = null;
	   	}
		this.actionStarted = false;
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){
		    return false;
		}
  	    var offsetCoord = e.mapCoord;
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("MeasureArea", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	}
};
SuperMap.UI.MeasureAreaAction.registerClass('SuperMap.UI.MeasureAreaAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.PointQueryAction = function(layerNames, sqlParam, tolerance, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标点选查询地物的操作。&lt;br&gt;
	/// 1. 点选查询是指指定地图的某一点的位置，
	/// 查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。&lt;br&gt;
	/// 2. 通过鼠标点选查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标点击要查询的位置，MapControl就会计算返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	///		（3）当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="tolerance" type="Number">查询容限。double 类型。该值不能小于0，建议取一个适中的值，比如5。&lt;br&gt;
	/// 该查询方法是以查询位置（点）为圆心，
	/// 在以tolerance的地理长度为半径的圆的范围内进行地物的查找。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PointQueryAction">返回 PointQueryAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="queryParam" type ="SuperMap.QueryParam">查询参数。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "点选查询";
	this.actionStarted = false;
	this.expectCount = 10;
	this.queryParam = null;
    this._type = "PointQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, tolerance, needHighlight, onComplete, onError, onStart]);
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._tolerance = tolerance;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
};
SuperMap.UI.PointQueryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化PointQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._tolerance){
			this._tolerance = 10;
		}
		
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this.queryParam.queryLayerParams = null;
			} else {
				this.queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this.queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this.queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this.queryParam) {
			this.queryParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
         //客户端鼠标点击事件
		this._keyPoint = null;
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this.queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = e.mapCoord;
        if(this._onStart){ this._onStart(qe); }
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this.queryByPoint(qe);
		}
    },
    
    queryByPoint:function(qe){
	/// <summary>点选查询。指定地图的某一点的位置，查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。
	/// </summary>
		this._keyPoint = qe.clientActionArgs.mapCoords[0];
		//alert("this._tolerance"+this._tolerance);
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().queryByPoint(this._keyPoint, this._tolerance, qe.queryParams, this._needHighlight, this._onComplete, this._onError,"",null);
		}
    },
    query:function(count, pageNo){
	/// <summary>以分页的方式获取矩形查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._keyPoint){
			return;
		}
		this.queryParam.startRecord = pageNo * count;
		this.queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			//this._mapControl.getMap().queryByPoint(this._mapRect, this.queryParam, false, this._onComplete, this._onError, customParam);
			this._mapControl.getMap().queryByPoint(this._keyPoint, this._tolerance, this.queryParam, false, this._onComplete,this._onError,customParam,pageNo);
		}
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.PointQueryAction.registerClass('SuperMap.UI.PointQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.RectQueryAction=function(layerNames, sqlParam, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制矩形并查询该矩形范围内地物的操作。&lt;br&gt;
	/// 1. 矩形查询是指指定的矩形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。&lt;br&gt;
	/// 2. 通过鼠标进行矩形查询的操作过程如下：&lt;br&gt;
	///		在地图窗口按住鼠标左键同时移动鼠标直至完成矩形的绘制，释放鼠标，MapControl就会在绘制的矩形范围内查询并返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	///		（3）当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.RectQueryAction">返回 RectQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="queryParam" type ="SuperMap.QueryParam">查询参数。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "框选查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this.queryParam = null;
    this._type = "RectQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, needHighlight, onComplete, onError, onStart]);
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this.actionStarted = false;
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
};
SuperMap.UI.RectQueryAction.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化RectQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl._container.style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		} else { 
			this._mapControl._container.style.cursor = "crosshair";
		}
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this.queryParam.queryLayerParams = null;
			} else {
				this.queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					/*
						queryLayer.sqlParam = new SuperMap.SqlParam();
						queryLayer.sqlParam.whereClause = this._sqlParam.whereClause;
						queryLayer.sqlParam.groupClause = this._sqlParam.groupClause;
						if(this._sqlParam.returnFields) {
							queryLayer.sqlParam.returnFields = new Array(this._sqlParam.returnFields);
						}
						queryLayer.sqlParam.sortClause = this._sqlParam.sortClause;
						if(this._sqlParam.ids) {
							queryLayer.sqlParam.ids = new Array(this._sqlParam.ids);
						}
						*/
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this.queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			// 设置期望返回数目
			this.queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removePolygon("RectQuery");
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
   	    var offsetCoord = e.mapCoord;
		while(this._xs.length>0){
			this._xs.pop();
			this._ys.pop();
		}
		this._xs.push(this._firstOffsetCoord.x);
		this._xs.push(offsetCoord.x);
		this._xs.push(offsetCoord.x);
		this._xs.push(this._firstOffsetCoord.x);
		this._ys.push(this._firstOffsetCoord.y);
		this._ys.push(this._firstOffsetCoord.y);
		this._ys.push(offsetCoord.y);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("RectQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted) {
			this._mapRect = null;
			this.actionStarted = true;
			this._firstMapCoord = e.mapCoord;
			this._firstOffsetCoord = e.mapCoord;
			while(this._xs.length > 0){
				this._xs.pop();
				this._ys.pop();
			}
		}
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = false;
		var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this.queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
	    qe.clientActionArgs.mapCoords[1] = e.mapCoord;//offsetCoord;
        if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this._mapControl.customLayer.removePolygon("RectQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			// 直接去调用查询
			this._queryByRect(qe);
		}
    },
	_queryByRect:function(qe){
		var left;
		var bottom;
		var right;
		var top;
		var startPoint = qe.clientActionArgs.mapCoords[0];
		var endPoint = qe.clientActionArgs.mapCoords[1];
		if(startPoint.x == endPoint.x){
			return;
		} else if(startPoint.x < endPoint.x){
			left = startPoint.x;
			right = endPoint.x;
		} else {
			left = endPoint.x;
			right = startPoint.x;
		}
		if(startPoint.y == endPoint.y){
			return;
		} else if(startPoint.y < endPoint.y){
			bottom = startPoint.y;
			top = endPoint.y;
		} else {
			bottom = endPoint.y;
			top = startPoint.y;
		}
		
		this._mapRect = new SuperMap.Rect2D(left, bottom, right, top);
		this._mapControl.getMap().queryByRect(this._mapRect, qe.queryParams, this._needHighlight, this._onComplete, this._onError);
		this._mapControl.customLayer.removePolygon("RectQuery");
	},
	query:function(count, pageNo){
	/// <summary>以分页的方式获取矩形查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapRect){
			return;
		}
		this.queryParam.startRecord = pageNo * count;
		this.queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().queryByRect(this._mapRect, this.queryParam, false, this._onComplete, this._onError, customParam,pageNo);
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.RectQueryAction.registerClass('SuperMap.UI.RectQueryAction', SuperMap.UI.Action ,Sys.IDisposable);

SuperMap.UI.PolygonQueryAction=function(layerNames, sqlParam, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制多边形并查询该多边形范围内地物的操作。&lt;br&gt;
	/// 1. 多边形查询是指指定的多边形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。&lt;br&gt;
	/// 2. 通过鼠标进行多边形查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标单击多边形每一个节点，最后双击鼠标完成多边形的绘制，MapControl就会在绘制的多边形范围内查询并返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	///		（3）当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PolygonQueryAction">返回 PolygonQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="queryParam" type ="SuperMap.QueryParam">查询参数。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "多边形查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this.queryParam = null;
    this._type = "PolygonQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, onComplete, onError, onStart]);
	this.actionStarted = false;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
    this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
};
SuperMap.UI.PolygonQueryAction.prototype={
    initialize:function(mapControl){
	/// <summary>初始化PolygonQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PolygonQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this.queryParam.queryLayerParams = null;
			}else{
				this.queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length>0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this.queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this.queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removePolygon("PolygonQuery");
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			if(this._keyPoints){
				while(this._keyPoints.length>0){
					this._keyPoints.pop();
					this._xs.pop();
					this._ys.pop();
				}
			}
			this._firstMapCoord = null;
		    this._firstOffsetCoord = null;
			this._keyPoints = new Array();
			this._firstMapCoord = e.mapCoord;
            this._firstOffsetCoord = offsetCoord;
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		} else {
			this._xs.pop();
			this._ys.pop();
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord);
		this._ys.push(offsetCoord);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		this._keyPoints.push(e.mapCoord);
		this._keyPoints.push(this._firstMapCoord);
		this._xs.pop();
		this._ys.pop();
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._xs.push(this._firstOffsetCoord.x);
		this._ys.push(this._firstOffsetCoord.y);
		this._mapControl.customLayer.insertPolygon("PolygonQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);		
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this.queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = this._keyPoints;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this.actionStarted = false;
			this._mapControl.customLayer.removePolygon("PolygonQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByPolygon(qe);
		}
   	},
   	_queryByPolygon:function(qe){
	    this._mapControl.getMap().queryByPolygon(qe.clientActionArgs.mapCoords, qe.queryParams, this._needHighlight, this._onComplete, this._onError);
		this.actionStarted = false;
		this._mapControl.customLayer.removePolygon("PolygonQuery");
	},
	query:function(count, pageNo){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._keyPoints)
			return;
		this.queryParam.startRecord = pageNo * count;
		this.queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().queryByPolygon(this._keyPoints, this.queryParam, false, this._onComplete, this._onError, customParam);
		}
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){return false;}
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("PolygonQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.PolygonQueryAction.registerClass('SuperMap.UI.PolygonQueryAction', SuperMap.UI.Action, Sys.IDisposable);


SuperMap.UI.LineQueryAction=function(layerNames, sqlParam, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制线段并查询该与该线段相交的地物的操作。&lt;br&gt;
	/// 1. 画线查询是指查询与指定的线相交的某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。&lt;br&gt;
	/// 2. 通过鼠标进行画线查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标单击线段的每一个节点，最后双击鼠标完成线段的绘制，MapControl就会在绘制的线段范围内查询并返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	///		（3）当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.LineQueryAction">返回 LineQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="queryParam" type ="SuperMap.QueryParam">查询参数。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "线选查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this.queryParam = null;
    this._type = "LineQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, onComplete, onError, onStart]);
	this.actionStarted = false;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
    this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
};
SuperMap.UI.LineQueryAction.prototype={
    initialize:function(mapControl){
	/// <summary>初始化LineQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/LineQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this.queryParam.queryLayerParams = null;
			}else{
				this.queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length>0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this.queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this.queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removeLine("LineQuery");
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			if(this._keyPoints){
				while(this._keyPoints.length>0){
					this._keyPoints.pop();
					this._xs.pop();
					this._ys.pop();
				}
			}
			this._firstMapCoord = null;
		    this._firstOffsetCoord = null;
			this._keyPoints = new Array();
			this._firstMapCoord = e.mapCoord;
		    this._firstOffsetCoord = offsetCoord;
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		} else {
			this._xs.pop();
			this._ys.pop();
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		this._keyPoints.push(e.mapCoord);
		this._keyPoints.push(this._firstMapCoord);
		this._xs.pop();
		this._ys.pop();
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._mapControl.customLayer.insertLine("LineQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor);		
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this.queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = this._keyPoints;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this.actionStarted = false;
			this._mapControl.customLayer.removeLine("LineQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByLine(qe);
		}
   	},
   	_queryByLine:function(qe){
	    this._mapControl.getMap().queryByLine(qe.clientActionArgs.mapCoords, qe.queryParams, this._needHighlight, this._onComplete, this._onError);
		this.actionStarted = false;
		this._mapControl.customLayer.removeLine("LineQuery");
	},
	query:function(count, pageNo){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._keyPoints)
			return;
		this.queryParam.startRecord = pageNo * count;
		this.queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().queryByLine(this._keyPoints, this.queryParam, false, this._onComplete, this._onError, customParam);
		}
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(e.mapCoord);
		var offsetCoord = e.mapCoord;
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertLine("LineQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor);
		//this._mapControl.CustomLayer.InsertLine("MeasureArea",xs,ys,2,"blue");
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.LineQueryAction.registerClass('SuperMap.UI.LineQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.CircleQueryAction = function(layerNames, sqlParam, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制圆并查询该与该圆范围内的地物的操作。&lt;br&gt;
	/// 1. 圆选查询是指在指定的圆范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。&lt;br&gt;
	/// 2. 通过鼠标进行圆选查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标单击圆的圆心，并按住鼠标不放拖动鼠标直至完成圆的绘制，双击鼠标结束圆的绘制，MapControl就会在绘制的圆范围内查询并返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	///		（3）当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.CircleQueryAction">返回 CircleQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="queryParam" type ="SuperMap.QueryParam">查询参数。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "圆选查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this.queryParam = null;
	this._type = "CircleQueryAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, needHighlight, onComplete, onError, onStart]);
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this.actionStarted = false;
    this._firstMapCoord = null;
	this._curMapCoord = null;
	this._firstPointX;
	this._firstPointY;
	this._curPointX;
	this._curPointY;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
	this._needHighlight = needHighlight;
	this._offsetX = 0
	this._offsetY = 0;
};
SuperMap.UI.CircleQueryAction.prototype={
	initialize:function(mapControl){
	/// <summary>初始化CircleQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/CircleQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this.queryParam.queryLayerParams = null;
			} else {
				this.queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this.queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this.queryParam.expectCount = this.expectCount;
		}
		if(this._mapControl.get_container().style.left == "") {
			this._offsetX = 0;
		} else {
			this._offsetX = parseInt(this._mapControl.get_container().style.left);
		}
		if(this._mapControl.get_container().style.top == "") {
			this._offsetY = 0;
		} else {
    		this._offsetY = parseInt(this._mapControl.get_container().style.top);
    	}
    },
	
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._SMISRemoveCircle();
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
		this._curMapCoord = e.mapCoord;
		var pixelCoord = this._mapControl.mapCoordToPixel(e.mapCoord);
		this._curPointX = pixelCoord.x + this._offsetX;
		this._curPointY = pixelCoord.y + this._offsetY;
		
		this._SMISDrawingCircle(this._firstPointX, this._firstPointY, this._curPointX, this._curPointY);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = true;
		this._firstMapCoord = e.mapCoord;
		var pixelCoord = this._mapControl.mapCoordToPixel(e.mapCoord);
		this._firstPointX = pixelCoord.x + this._offsetX;
		this._firstPointY = pixelCoord.y + this._offsetY;
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this._curMapCoord = e.mapCoord;
		this._SMISOnMouseUp(this._mapControl);
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
	_SMISDrawingCircle:function(startPointX, startPointY, curPointX, curPointY){
		var circle = document.getElementById("SMISCircle");
		var m_drawLayer;
		var mouseUpDelegate = Function.createDelegate(this, this._SMISOnMouseUp);
		if(_GetBrowser() != "ie"){
			if(!circle){
				circle = document.createElement("div");
				circle.id = "SMISCircle";
				circle.style.position = "absolute";
				circle.style.left = "0px";
				circle.style.top = "0px";
				circle.unselectable = "on";
				circle.onmouseup = mouseUpDelegate;
				document.body.appendChild(circle);
				var m_jg = new jsGraphics("SMISCircle");
				circle.jg = m_jg;
			} else {
				circle.jg.clear();
			}
			circle.jg.setColor(this.strokeColor);
			circle.jg.setStroke(this.strokeWeight);
			circle.style.zIndex = 2000;
			circle.style.opacity = 0.5;
			var radius = Math.sqrt(Math.pow((curPointX - startPointX), 2) + Math.pow((curPointY - startPointY), 2));
			circle.jg.drawEllipse(startPointX-radius,startPointY-radius, radius*2, radius*2);
			circle.jg.paint();
			return;
		}
		if(!circle){
			SuperMap.Utility.enableVML();
			circle = document.createElement("<v:arc startangle='0' endangle='360'/>");
			circle.style.position = "absolute";
			circle.style.visibility = 'visible';
			circle.id = "SMISCircle";
			circle.style.zIndex = 1000;
			circle.onmouseup = mouseUpDelegate;
			//circle.style.zIndex = map.parentElement.style.zIndex + 200;
			var fill = document.createElement("<v:fill opacity=0.3></v:fill>");
			var stroke = document.createElement("<v:stroke dashstyle='solid' Color='" + this.strokeColor+ "'></v:stroke>");
			stroke.setAttribute("weight", this.strokeWeight);
			document.body.appendChild(circle);
			circle.appendChild(fill);
			circle.appendChild(stroke);
		}
		var radius = Math.sqrt(Math.pow((curPointX - startPointX), 2) + Math.pow((curPointY - startPointY), 2))
		circle.style.left = (startPointX - radius) + "px";
		circle.style.top = (startPointY - radius) + "px";
		circle.style.width = 2*radius + "px";
		circle.style.height =  circle.style.width;
	},
	_SMISOnMouseUp:function(){
		this.actionStarted = false;
		this._SMISRemoveCircle();
		var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = new SuperMap.QueryParam();
	    qe.queryParams = this.queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
	    qe.clientActionArgs.mapCoords[1] = this._curMapCoord;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByCircle(qe);
		}
	},
    _queryByCircle:function(qe){
	    var startPoint = qe.clientActionArgs.mapCoords[0];
	    var endPoint = qe.clientActionArgs.mapCoords[1];
	    var left;
		var bottom;
		var right;
		var top;
		this.radius = Math.sqrt(Math.pow((endPoint.x-startPoint.x),2)+Math.pow((endPoint.y-startPoint.y),2));
		this._mapControl.getMap().queryByCircle(startPoint, this.radius, qe.queryParams, this._needHighlight, this._onComplete, this._onError);
	},
	query:function(count, pageNo){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._firstMapCoord || !this.radius){
			return;
		}
		this.queryParam.startRecord = pageNo * count;
		this.queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().queryByCircle(this._firstMapCoord, this.radius, this.queryParam, false, this._onComplete, this._onError, customParam,pageNo);
		}
	},
	_SMISRemoveCircle:function(){
		var circle = document.getElementById("SMISCircle");
		if(circle) { 
			circle.onMouseUp = null;
			document.body.removeChild(circle);
		}
		circle = null;
	}
};
SuperMap.UI.CircleQueryAction.registerClass("SuperMap.UI.CircleQueryAction", SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.FindNearestAction = function(layerNames, sqlParam, tolerance, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现通过鼠标设置事件点并查找距离该事件点最近的对象。&lt;br&gt;
	/// 1. 特别说明：在线图层或面图层中,这里的距离指的从指定起始点到目标对象边线的最短长度。&lt;br&gt;
	/// 2. 通过鼠标查找最近对象的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标点击要查询的位置，MapControl就会计算返回查询的结果。&lt;br&gt;
	/// 3. 对于返回的结果，有如下说明：&lt;br&gt;
	///		（1）默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	///		（2）返回的结果会在onComplete的处理函数中进行处理。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="tolerance" type="Number">从起始点开始允许查询的范围的最大直径。double 类型。&lt;br&gt;
	/// 该查询方法是以查询位置（点）为圆心，在以tolerance的地理长度为半径的圆的范围内进行地物的查找。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.FindNearestAction">返回 FindNearestAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "最近地物查找";
	this.expectCount = 10;
	this._type = "FindNearestAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, tolerance, needHighlight, onComplete, onError, onStart]);
	this._queryParam = null;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._tolerance = tolerance;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
};
SuperMap.UI.FindNearestAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化FindNearestAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._tolerance){
			this._tolerance = 100;
		}
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
			this._queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this._queryParam) {
			this._queryParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
         //客户端鼠标点击事件
		this._keyPoint = null;
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = e.mapCoord;
        if(this._onStart){ this._onStart(qe); }
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._findNearest(qe);
		}
    },
    _findNearest:function(qe){
	/// <summary>实现通过鼠标设置事件点并查找距离该事件点最近的对象。
	/// </summary>

		this._keyPoint = qe.clientActionArgs.mapCoords[0];
		this._mapControl.getMap().findNearest(this._keyPoint, this._tolerance, qe.queryParams, this._needHighlight, this._onComplete, this._onError, null);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.FindNearestAction.registerClass('SuperMap.UI.FindNearestAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.FindPathAction = function(networkModelSetting,pathParam,needHighlight, onComplete, onError, onStart){
	/// <summary>查找网络中结点间的最短路径或者最佳路径。&lt;br&gt;
	/// 1. 查找结果放入NetworkAnalystResult内。&lt;br&gt;
	/// 2. 网络中两结点间最短路径，通常是根据距离来计算的；而最佳路径则根据两点间所有弧段的正向和反向的阻值字段、
	/// 转向表等多种因素一起计算出两点间的最佳路径；&lt;br&gt;
	/// 3. 通过鼠标进行路径分析的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标分别单击要执行分析的网络结点，双击鼠标结束结点的选择，MapControl会返回分析的结果。
	/// </summary>
	/// <param name="networkModelSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="pathParam" type="SuperMap.PathParam">路径分析参数。</param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示分析结果。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type ="Function">在向服务器提交路径分析请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart() {};</param>
	/// <returns type="SuperMap.UI.FindPathAction">返回 FindPathAction 对象。</returns>
	/// <field name="type" type ="String">Action类型，其值为FindPathAction。</field>
	this.title = "最佳路径分析";
    this.type = "FindPathAction";
	this._json = SuperMap.Utility.actionToJSON(this.type, [networkModelSetting,pathParam,needHighlight, onComplete, onError, onStart]);
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
	this._networkModelSetting = networkModelSetting;
	this._pathParam = pathParam;
	if(this._pathParam.networkAnalystParam.point2Ds == null){
		this._pathParam.networkAnalystParam.point2Ds = new Array();
	}
	this._needHighlight = needHighlight;
	this._pointSelected = false;
	this._markerIDPrefix = "$"+this.type+"_";
};
SuperMap.UI.FindPathAction.prototype={
	_clearMarker:function(){
	    if(this._pathParam.networkAnalystParam.point2Ds){
			while(this._pathParam.networkAnalystParam.point2Ds.length>0){
				this._pathParam.networkAnalystParam.point2Ds.pop();
			}
		}
        this._pointSelected = false;
	},
	initialize:function(mapControl){
	/// <summary>初始化FindPathAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
	   	this._mapControl = null;
		if(this._networkModelSetting){
			this._networkModelSetting.dispose();
			this._networkModelSetting = null;
		}
		if(this._pathParam){
			this._pathParam.dispose();
			this._pathParam = null;
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(this._pointSelected){
			this._clearMarker();
			this._pointSelected = false;
		}
		var pt =  new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y);
		this._pathParam.networkAnalystParam.point2Ds.push(pt);
		var id = this._markerIDPrefix + this._pathParam.networkAnalystParam.point2Ds.length;
		var icon = new Object();
		icon.src = "../../images/supGis/marker_blue.gif";
		icon.width = 16;
		icon.height = 16;
        this._mapControl.customLayer.insertMarker(id+(new Date()).getTime(), offsetCoord.x, offsetCoord.y, 16, 16,null,null,null,icon);	
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this._pointSelected && e.button == 2 && this._pathParam.networkAnalystParam.point2Ds.length>=2){
			this._mapControl.getMap().findPath(this._networkModelSetting,this._pathParam, this._needHighlight,this._onComplete, this._onError);
			this._pointSelected = true;
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.FindPathAction.registerClass('SuperMap.UI.FindPathAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.FindTSPPathAction = function(networkModelSetting,tspPathParam,needHighlight, onComplete, onError, onStart){
	/// <summary>旅行商分析。&lt;br&gt;
	/// 1. 查找结果放入NetworkAnalystResult内。&lt;br&gt;
	/// 2. 通过指定出发和到达的位置及所有需要经过的游历点，
	/// 查找相对最佳的游历路线，保证每个游历点只经过一次的情况下，总耗费最小。
	/// </summary>
	/// <param name="networkModelSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="tspPathParam" type="SuperMap.TSPPathParam">旅行商分析参数。</param>
	/// <param name="needHighlight" type="boolean">是否需要高亮显示结果。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type ="Function">在向服务器提交旅行商分析请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart() {};
	/// </param>
	/// <returns type="SuperMap.UI.FindTSPPathAction">返回 FindTSPPathAction 对象。</returns>
	/// <field name="type" type ="String">Action类型，其值为FindTSPPathAction。</field>
	this.title = "旅行商分析";
  	this.type = "FindTSPPathAction";
	this._json = SuperMap.Utility.actionToJSON(this.type, [networkModelSetting,tspPathParam,needHighlight, onComplete, onError, onStart]);
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
	this._networkModelSetting = networkModelSetting;
	this._tspPathParam = tspPathParam;
	if(this._tspPathParam.networkAnalystParam.point2Ds == null){
		this._tspPathParam.networkAnalystParam.point2Ds = new Array();
	}
	this._needHighlight = needHighlight;
	this._pointSelected = false;
	this._markerIDPrefix = "$"+this.type+"_";
};
SuperMap.UI.FindTSPPathAction.prototype={
	_clearMarker:function(){
	/// <summary>删除自定义图层中的标记点。
	/// </summary>
		if(this._tspPathParam.networkAnalystParam.point2Ds){
			while(this._tspPathParam.networkAnalystParam.point2Ds.length>0){
				this._tspPathParam.networkAnalystParam.point2Ds.pop();
			}
		}
		 this._pointSelected = false;
	},
	initialize:function(mapControl){
	/// <summary>初始化FindTSPPathAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
	   	this._mapControl = null;
		if(this._networkModelSetting){
			this._networkModelSetting.dispose();
			this._networkModelSetting = null;
		}
		if(this._tspPathParam){
			this._tspPathParam.dispose();
			this._tspPathParam = null;
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(this._pointSelected){
		    this._clearMarker();
			this._pointSelected = false;
		}
		var pt =  new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y);
		this._tspPathParam.networkAnalystParam.point2Ds.push(pt);
		var id = this._markerIDPrefix+this._tspPathParam.networkAnalystParam.point2Ds.length;
		var icon = new Object();
		icon.src = "../../images/supGis/marker_blue.gif";
		icon.width = 16;
		icon.height = 16;
        this._mapControl.customLayer.insertMarker(id+(new Date()).getTime(), offsetCoord.x, offsetCoord.y, 16, 16,null,null,null,icon);	
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this._pointSelected && e.button == 2 && this._tspPathParam.networkAnalystParam.point2Ds.length>=2){
			this._mapControl.getMap().findTSPPath(this._networkModelSetting,this._tspPathParam, this._needHighlight,this._onComplete, this._onError);
			this._pointSelected = true;
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.FindTSPPathAction.registerClass('SuperMap.UI.FindTSPPathAction', SuperMap.UI.Action, Sys.IDisposable);


SuperMap.UI.ClosestFacilityAnalystAction = function(networkModelSetting,proximityParam,needHighlight,bSelectEventPoint, onComplete, onError, onStart){
	/// <summary>最近设施分析。&lt;br&gt;
	/// 1. 最近设施查找：&lt;br&gt;
	///		为事件点查找以最小耗费能到达的设施点，结果显示从事件点到设施点的最佳路径，耗费，及行驶方向。该功能还可以设置查找的阈值，
	/// 即搜索范围，超过该范围将不再进行查找，例如事件发生点是一起交通事故，要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。&lt;br&gt;
	/// 此例中，事故发生地即是一个事件点，周边的医院则是设施点。最近设施查找实际上也是一种路径分析，因此，同样可以应用障碍边和障碍点的设置，
	/// 在行驶路途上这些障碍将不能被穿越，在路径分析中会予以考虑。
	/// </summary>
	/// <param name="networkModelSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="proximityParam" type="SuperMap.ProximityParam">最近设施分析参数。</param>
	/// <param name="needHighlight" type="boolean">是否需要高亮显示结果。</param>
	/// <param name="bSelectEventPoint" type="boolean">当前是选择事件点还是设施点。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type ="Function">在向服务器提交最近设施分析请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart() {};
	/// </param>
	/// <returns type="SuperMap.UI.ClosestFacilityAnalystAction">返回 ClosestFacilityAnalystAction 对象。</returns>
	/// <field name="type" type ="String">Action类型，其值为ClosestFacilityAnalystAction。</field>
	this.title = "最近设施分析";
	this.type = "ClosestFacilityAnalystAction";
	this._json = SuperMap.Utility.actionToJSON(this.type, [networkModelSetting,proximityParam,needHighlight,bSelectEventPoint, onComplete, onError, onStart]);
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
	this._selectEventPoint = bSelectEventPoint;
	this._networkModelSetting = networkModelSetting;
	this._needHighlight = needHighlight;
	this._proximityParam = proximityParam;
	if(this._proximityParam.networkAnalystParam.point2Ds == null){
		this._proximityParam.networkAnalystParam.point2Ds = new Array();
	}
	this._eventPoint = new SuperMap.Point2D(0,0);
	this._facilitiesSelected = false;
	this._eventPointSelected = false;
	this._facilityPrefix = "$ClosestFacility_";
	this._eventPointMarkdID = "$EventPoint"+(new Date()).getTime();
	this._actionCount = 0;
};
SuperMap.UI.ClosestFacilityAnalystAction.prototype={
    _clearMarker:function(){
    /// <summary>删除自定义图层中的标记点。
	/// </summary>
        this._clearFacilities();
        this._clearEventPoint();
    },
	_clearFacilities:function(){
		if(this._proximityParam.networkAnalystParam.point2Ds){
			while(this._proximityParam.networkAnalystParam.point2Ds.length>0){
				this._proximityParam.networkAnalystParam.point2Ds.pop();
			}
		}
        this._facilitiesSelected = false;
	},
	_clearEventPoint:function(){
		if(!this._mapControl){
			return;
		}
		for(var i = this._mapControl.customLayer.markers.length - 1;i>=0; i < i--){
            var mk =  this._mapControl.customLayer.markers[i];
			if(mk.id.indexOf(this._eventPointMarkdID + this._actionCount)!= -1){
				this._mapControl.customLayer.removeMarker(mk.id);
			}
        }
	},
	_addPoint:function(e,id){
		var isEventPoint = (id == this._eventPointMarkdID);
   	    var offsetCoord = e.mapCoord;
   	    if(this._eventPointSelected && this._facilitiesSelected){
   	        this._clearEventPoint();
   	        this._eventPointSelected = false;
   	        this._clearFacilities();
   	        this._facilitiesSelected = false;
   	    }
		if(isEventPoint){
			if(this._eventPointSelected){
				this._clearEventPoint();
			}
			this._eventPoint.x = e.mapCoord.x;
			this._eventPoint.y = e.mapCoord.y;
			
			var icon = new Object();
			icon.src = "../../images/supGis/eventpoint.gif";
			icon.width = 16;
			icon.height = 16;
	        this._mapControl.customLayer.insertMarker(id+this._actionCount, offsetCoord.x,offsetCoord.y, 16, 16, null,null,null,icon);
			this._eventPointSelected = true;
		}else{
			var pt =  new SuperMap.Point2D(e.mapCoord.x,e.mapCoord.y);
			this._proximityParam.networkAnalystParam.point2Ds.push(pt);
			var icon = new Object();
			icon.src = "../../images/supGis/marker_blue.gif";
			icon.width = 16;
			icon.height = 16;
	        this._mapControl.customLayer.insertMarker(id+(new Date()).getTime(), offsetCoord.x, offsetCoord.y, 16, 16,null,null,null,icon);
		}
	},
	_isReady:function(){
		return this._eventPointSelected && this._proximityParam.networkAnalystParam.point2Ds.length>0;
	},
	initialize:function(mapControl){
	/// <summary>初始化ClosestFacilityAnalystAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
	},
    dispose:function(){
		/// <summary>释放对象占用的资源。
		/// </summary>
		this._eventPoint = null;
		if(this._networkModelSetting){
			this._networkModelSetting.dispose();
			this._networkModelSetting = null;
		}
		if(this._proximityParam){
			this._proximityParam.dispose();
			this._proximityParam = null;
		}
		this._mapControl = null;
	},
	setIsSelectingEventPoint:function(bSelectEventPoint){
	/// <summary>修改当前的选点状态。
	/// </summary>
	/// <param name="bSelectEventPoint" type="Boolean">当前是否在选择事件点。</param>
		this._selectEventPoint = bSelectEventPoint;
	},
    onClick:function(e){
		/// <summary>在地图控件上单击鼠标左键事件。
		/// </summary>
		/// <param name="e" type="Object">事件参数对象。</param>
		if(this._selectEventPoint){
			this._addPoint(e,this._eventPointMarkdID);
		}else{
			var id = this._facilityPrefix+this._proximityParam.networkAnalystParam.point2Ds.length;
			this._addPoint(e,id);
		}
    },
    onDblClick:function(e){
		/// <summary>在地图控件上鼠标双击事件。
		/// </summary>
		/// <param name="e" type="Object">事件参数对象。</param>
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    if(this._eventPointSelected && this._facilitiesSelected){
   	      return;
   	    }
		if(e.button == 2  && this._isReady()){
				this._mapControl.getMap().closestFacility(this._networkModelSetting,this._eventPoint,this._proximityParam, this._needHighlight,this._onComplete, this._onError);
				this._facilitiesSelected = true;
				this._actionCount++;
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
	
};
SuperMap.UI.ClosestFacilityAnalystAction.registerClass('SuperMap.UI.ClosestFacilityAnalystAction', SuperMap.UI.Action, Sys.IDisposable);


SuperMap.UI.ServiceAreaAction = function(networkModelSetting,serviceAreaParam,needHighlight,onServiceCenterAdded,onServiceCenterRemoved, onComplete, onError, onStart){
	/// <summary>服务区分析。&lt;br&gt;
	/// 1. 关于服务区及服务区分析：&lt;br&gt;
	///		服务区：以指定点为中心，在一定阻力范围内，包含所有可通达边的一个区域。&lt;br&gt;
	///		服务区分析：为网络上指定的位置点计算服务范围。例如：为网络上某点计算其30分钟的服务区，则结果服务区内，任意点出发到该点的时间都不会超过30分钟。
	/// </summary>
	/// <param name="networkModelSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="serviceAreaParam" type="SuperMap.ServiceAreaParam">服务区分析参数。</param>
	/// <param name="needHighlight" type="boolean">是否需要高亮显示结果。</param>
	/// <param name="onServiceCenterAdded" type="Function">当Action中有新的服务点添加时会触发该事件。事件新增中心点的ID，用户根据Index可以设置相应的服务区半径。</param>
	/// <param name="onServiceCenterRemoved" type="Function">当中心点被移除时会触发该事件。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type ="Function">在向服务器提交服务区分析请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart() {};
	/// </param>
	/// <returns type="SuperMap.UI.ServiceAreaAction">返回 ServiceAreaAction 对象。</returns>
	/// <field name="type" type ="String">Action类型，其值为ServiceAreaAction。</field>
	this.title = "服务区分析";
	this.type = "ServiceAreaAction";
	this._json = SuperMap.Utility.actionToJSON(this.type, [networkModelSetting,serviceAreaParam,needHighlight, onComplete, onError, onStart]);
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
	this._onServiceCenterRemoved = onServiceCenterRemoved;
	this._onServiceCenterAdded = onServiceCenterAdded;
	this._networkModelSetting = networkModelSetting;
	this._serviceAreaParam = serviceAreaParam;
	if(this._serviceAreaParam.networkAnalystParam.point2Ds == null){
		this._serviceAreaParam.networkAnalystParam.point2Ds = new Array();
	}
	if(this._serviceAreaParam.weights == null){
		this._serviceAreaParam.weights = new Array();
	}
	this._needHighlight = needHighlight;
	this._serviceCenterSelected = false;
	this._serviceCenterMarkerIDPrefix = "$ServiceCenter_";
};
SuperMap.UI.ServiceAreaAction.prototype={
	_clearMarker:function(){
		while(this._serviceAreaParam.networkAnalystParam.point2Ds.length>0){
			this._serviceAreaParam.networkAnalystParam.point2Ds.pop();
			if(this._onServiceCenterRemoved){
				this._onServiceCenterRemoved(this._serviceAreaParam.networkAnalystParam.point2Ds.length);
			}
		}
		while(this._serviceAreaParam.weights.length>0){
			this._serviceAreaParam.weights.pop();
		}
        this._facilitiesSelected = false;
	},
	_postQuest:function(){
		if(this._serviceAreaParam.networkAnalystParam.point2Ds.length>0 && this._serviceAreaParam.networkAnalystParam.point2Ds.length == this._serviceAreaParam.weights.length){
			this._mapControl.getMap().serviceArea(this._networkModelSetting,this._serviceAreaParam, this._needHighlight,this._onComplete, this._onError);
		}
	},
	setWeight:function(index,weight){
	/// <summary> 设置中心点的服务半径。
	/// </summary>
	/// <param  name="index" type="Number">服务中心点索引。</param>
	/// <param  name="weight" type="Number">服务半径。</param>
		if(index>=this._serviceAreaParam.weights.length){
			return;
		}
		this._serviceAreaParam.weights[index] = weight;
	},
	initialize:function(mapControl){
	/// <summary>初始化ServiceAreaAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._clearMarker();
		if(this._networkModelSetting){
			this._networkModelSetting.dispose();
			this._networkModelSetting = null;
		}
		if(this._serviceAreaParam){
			this._serviceAreaParam.dispose();
			this._serviceAreaParam = null;
		}
		this._mapControl = null;
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    var offsetCoord = e.mapCoord;
		if(this._serviceCenterSelected){
			//已经选择了节点，则清空所有的节点
			this._clearMarker();
			this._serviceCenterSelected = false;
		}
		var pt =  new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y);
		this._serviceAreaParam.networkAnalystParam.point2Ds.push(pt);
		var index = this._serviceAreaParam.networkAnalystParam.point2Ds.length-1;
		this._serviceAreaParam.weights.push(200.0);
		var id = this._serviceCenterMarkerIDPrefix+this._serviceAreaParam.networkAnalystParam.point2Ds.length;
		var icon = new Object();
		icon.src = "../../images/supGis/marker_blue.gif";
		icon.width = 16;
		icon.height = 16;
        this._mapControl.customLayer.insertMarker(id+(new Date()).getTime(), offsetCoord.x, offsetCoord.y, 16, 16,null,null,null,icon);	
        if(this._onServiceCenterAdded){
        	this._onServiceCenterAdded(index,new SuperMap.Point2D(offsetCoord.x, offsetCoord.y),this._serviceAreaParam.weights[index]);
        }
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		
   	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(e.button == 2 && !this._serviceCenterSelected){
			if(this._onStart) {
				this._onStart();
			}
			//右键提交请求
			this._serviceCenterSelected = true;
			this._postQuest();
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.ServiceAreaAction.registerClass('SuperMap.UI.ServiceAreaAction', SuperMap.UI.Action, Sys.IDisposable);

//左键选中,左键更新,右键取消添加
SuperMap.UI.AddEntityAction=function(layerName, layerType, needHighlight, onComplete, onError){
	/// <summary>在指定的iServer服务器端地图图层上增加一个同类型的实体。该实体的形状和位置是通过鼠标在地图上绘制出来的。&lt;br&gt;
	/// 目前支持在点、线、面、文本图层中添加对应类型的实体。
 	/// </summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="layerType" type="Number">图层的类型。数值型参数。点图层=1，线图层=3，面图层=5，文本图层=7。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示新添加的实体。True代表高亮显示该实体。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <returns type="SuperMap.UI.AddEntityAction">返回 AddEntityAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "添加地物";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this._type = "AddEntityAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerName, layerType, needHighlight, onComplete, onError]);
    this._keyPoints = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
    this._xs = new Array();
	this._ys = new Array();
    this.actionStarted = false;
	this._datasetType = new SuperMap.DatasetType();
	this._needHighlight = needHighlight;
	this._layerName = layerName;
	this._layerType = layerType;
	this._onComplete = onComplete;
	this._onError = onError;
	this.onClickE=null ;
};
SuperMap.UI.AddEntityAction.prototype={
	initialize:function(mapControl){
	/// <summary>初始化AddEntityAction。
	/// </summary>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		// 需要修改默认图层
		if(!this._layerName){
		    this._layerName = "";
		}
		if(!this._layerType){
		    this._layerType = this._datasetType.point;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removeMarker("PointAdding");
        this._mapControl.customLayer.removeLine("LineAdding");
        this._mapControl.customLayer.removePolygon("PolygonAdding");
        this._mapControl = null;
    },	
    _onAddEntityComplete:function(editResult){
	/// <summary>添加实体操作完成时触发的事件。
	/// </summary>
	/// <param name="editResult" type="SuperMap.EditResult">编辑结果类。 &lt;br&gt;
	/// 编辑操作返回的结果对象，包含操作成功是否的标识，以及被编辑的地物ID、地图受影响的范围和操作之后的地图描述。</param>
		if(this._keyPoints !=null && this._keyPoints.length>0){
			if(this._layerType == this._datasetType.line){
				this._mapControl.customLayer.insertLine("LineAdding", this._xs, this._ys, this.strokeWeight, this.strokeColor);
			}else if(this._layerType == this._datasetType.region){
				if(this._keyPoints !=null && this._keyPoints.length>0){
	           		this._mapControl.customLayer.insertPolygon("PolygonAdding", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
				}
			}
			while(this._keyPoints.length > 0){
		        this._keyPoints.pop();
		    }
            while(this._xs.length > 0){
                this._xs.pop();
				this._ys.pop();
            }
		}
		this._mapControl.getMap().isEditing = true;
		
	    //添加地图之后重新刷新
		var doubleRect=getDoubleRect();
		queryRect(doubleRect);
	    
		this._mapControl.getMap().isEditing = false;
        if(this._onComplete){
            this._onComplete(editResult);
        }
    },
    
    /**
    	2009-11-19 add by lyang
    	扩展map,测试网点是否存在的回调方法
    	isExsit:
    	0:标识业务数据网点存在，而GIS数据网点不存在，可以继续添加网点
    	1:标识业务数据网点不存在,不可以继续添加网点, 不可以继续添加网点
    	2:标识业务数据网点存在，而GIS数据网点也存在，不可以继续添加网点
    */
  	_onQueryBankCompete:function(message){
  		var isExist=message.split("|")[0];
  		var orgfloor=message.split("|")[1];
  		var status=message.split("|")[2];
  		var bankName=message.split("|")[3];
		if(isExist=="0"){
			if (this.onClickE!=null)
				{	
					
					var offsetCoord = this.onClickE.mapCoord;
					if(this._layerType == this._datasetType.point){
			            //添加点
			            var entity = new  SuperMap.Entity();
			            entity.shape = new SuperMap.Geometry();
			            entity.shape.feature = new SuperMap.FeatureType().point;
			            var testid = document.getElementById("addBankId").value; 
				         //zhong begin 2009-10-30
				         entity.fieldNames = ["C_BANK_ID","C_ORGFLOOR","C_BANK_STATUS","C_BANK_NAME"];
				         if(bankName!=null && bankName!=""){
				         	if(status!=null && status=="0"){
				        	 	bankName=bankName.substring(6,bankName.length);
				        	 }
				         }
				         entity.fieldValues =[testid,orgfloor,status,bankName];
					
				         //zhong end 2009-10-30
			         
			            entity.shape.point2Ds = new Array(this.onClickE.mapCoord);
			            var onAddEntityCompleteDelegate = Function.createDelegate(this, this._onAddEntityComplete);
			            this._mapControl.getMap().addEntity(this._layerName, entity, this._needHighlight, onAddEntityCompleteDelegate, this._onError);
						document.body.removeChild(document.getElementById("my"));
						mapControl.customLayer.removeMarker("marker");
						var msg = page_res["addEntitySuccess"] + " ID: "+testid+"\n";
						alert(msg);
				}else if(this._layerType == this._datasetType.line){
					// 每次加入两个点，起始后每点击一次将前面对应的点先去掉一个（鼠标移动中供显示用）
		            if(this.actionStarted){
						this._xs.pop();
						this._ys.pop();
						this._keyPoints.pop();
					}
					this._xs.push(offsetCoord.x);
					this._ys.push(offsetCoord.y);
					this._keyPoints.push(this.onClickE.mapCoord);
				    this.actionStarted = true;
				    this._keyPoints.push(this.onClickE.mapCoord);
				    this._xs.push(offsetCoord.x);
					this._ys.push(offsetCoord.y);
		        } else if(this._layerType == this._datasetType.region){
		            if(!this.actionStarted){
						this._firstMapCoord = this.onClickE.mapCoord;
					    this._firstOffsetCoord = offsetCoord;
				    } else {
					    this._xs.pop();
						this._ys.pop();
						this._keyPoints.pop();
				    }
					this._xs.push(offsetCoord.x);
					this._ys.push(offsetCoord.y);
					this._keyPoints.push(this.onClickE.mapCoord);
				    this.actionStarted = true;
				    this._keyPoints.push(this.onClickE.mapCoord);
				    this._xs.push(this._firstOffsetCoord.x);
					this._ys.push(this._firstOffsetCoord.y);
		        }
			 }
		}else if(isExist=="2"){
			// this._mapControl.customLayer.removeMarker("insertTag");
			alert("您要添加的网点编号(序列号)地图里已经存在");
		}else{
			//this._mapControl.customLayer.removeMarker("insertTag");
			alert("您要添加的网点编号(序列号)不存在！！");
		}
		//添加完网点之后让状态改为平移
  		//setPan();
  	},
  	
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
   	    //var offsetCoord = e.mapCoord;
   	    
   	    objectxy =e;
   	    var mapCoord = objectxy.mapCoord;
   	    //添加闪烁图片
   	    
        var markerContent = new SuperMap.MarkerContent();
		markerContent.innerHTML = "<img src ='../../images/supGis/marker_blue.gif' width ='20' height='20'/>";
		mapControl.customLayer.insertMarker("insertTag",mapCoord.x,mapCoord.y,0,0,null,null,20,markerContent);
		if(this._layerName==page_res["bankLayerName"]){
	   	    if(!document.getElementById("my")){
	       		Button2_onclick(e);
			 }else{
			 	var testid = document.getElementById("addBankId").value; 
				//var testname = document.getElementById("addBankName").value; 
				//var testaddress = document.getElementById("addBankAddress").value; 
				var pattern = /^\d+(\.\d+)?$/;
				var ids=new Array(testid);
				var orgfloor=document.getElementById("orgfloor").value ;		
				var obj= document.getElementsByName("type");
				var type="" ;
				for(var i=0 ;i<obj.length ;i++){
					if(obj[i].checked){
						type=obj[i].value ;
					}			
				}
				//判断网点行号是否为数字，如果是则通过扩展MAP，判断该网点行号是否存在，如果不存在，则在回调函数里增加该网点
				var onQueryBankIdDelegate = Function.createDelegate(this, this._onQueryBankCompete);
				if(type!=null){
					if(type=="1"){
						this.onClickE=e ;
						SuperMap.Committer.commitMapCmd('grgbankingGis','bankvalidate', ["BankId","orgfloor","type"], [ids[0],orgfloor,type], onQueryBankIdDelegate);
					}
					if (type=="0"&&pattern.test(testid)){
						this.onClickE=e ;
						SuperMap.Committer.commitMapCmd('grgbankingGis','bankvalidate', ["BankId","orgfloor","type"], [ids[0],orgfloor,type], onQueryBankIdDelegate);
					}
					if (type=="0"&&!pattern.test(testid)){
						alert("网点编号应该是数字");
					}
				}else{
					alert("请选择添加类型");
				}
		 	}
		 }else{
	 	alert("请选择Bank图层");
		 }
    },
    
    
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        //更新
        
        if(!this.actionStarted){return false;}
   	    var offsetCoord = e.mapCoord;
		var onAddEntityCompleteDelegate = Function.createDelegate(this, this._onAddEntityComplete);
        if(this._layerType == this._datasetType.line){
			this._keyPoints.pop();
			this._keyPoints.pop();
		    this._keyPoints.push(e.mapCoord);
		    this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		    this._mapControl.customLayer.removeLine("LineAdding");
		    var entity = new SuperMap.Entity();
            entity.shape = new SuperMap.Geometry();
            entity.shape.feature = new SuperMap.FeatureType().line;
            entity.shape.point2Ds = this._keyPoints;
			this._mapControl.getMap().addEntity(this._layerName, entity, this._needHighlight, onAddEntityCompleteDelegate, this._onError);
		    this.actionStarted = false;
        }
        if(this._layerType == this._datasetType.region){
			this._keyPoints.pop();
			this._keyPoints.pop();
            this._keyPoints.push(e.mapCoord);
		    this._keyPoints.push(this._firstMapCoord);
		    this._xs.pop();
			this._ys.pop();
		    this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		    this._xs.push(this._firstOffsetCoord.x);
			this._ys.push(this._firstOffsetCoord.y);
		    this._mapControl.customLayer.removePolygon("PolygonAdding");
		    var entity = new SuperMap.Entity();
            entity.shape = new SuperMap.Geometry();
            entity.shape.feature = new SuperMap.FeatureType().polygon;
            entity.shape.point2Ds = this._keyPoints;
            this._mapControl.getMap()._t = new Date().getTime();
			this._mapControl.getMap().addEntity(this._layerName, entity, this._needHighlight, onAddEntityCompleteDelegate, this._onError);
            this._firstMapCoord = null;
		    this.actionStarted = false;
        }
    },	
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){
			return false;
		}
   	    var offsetCoord = e.mapCoord;
		if(this._layerType == this._datasetType.line){
		    this._keyPoints.pop();
		    this._xs.pop();
			this._ys.pop();
		    this._keyPoints.push(e.mapCoord);
		    this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
            this._mapControl.customLayer.insertLine("LineAdding", this._xs, this._ys, this.strokeWeight, this.strokeColor);
        }
        if(this._layerType == this._datasetType.region){
		    this._keyPoints.pop();
		    this._xs.pop();
			this._ys.pop();
		    this._keyPoints.push(e.mapCoord);
		    this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
            this._mapControl.customLayer.insertPolygon("PolygonAdding", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
        }
    },
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this._selected = false;
        this.actionStarted = false;
        while(this._keyPoints.length > 0){
			this._keyPoints.pop();
        }
        while(this._xs.length > 0){
            this._xs.pop();
			this._ys.pop();
        }
        this._firstMapCoord = null;
        this._mapControl.customLayer.removeLine("LineAdding");
        this._mapControl.customLayer.removePolygon("PolygonAdding");
    }
};
SuperMap.UI.AddEntityAction.registerClass('SuperMap.UI.AddEntityAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.AddTextAction=function(layerName, textValue, needHighlight, onComplete, onError){
	/// <summary>在指定的 iServer 服务器端地图图层中添加一个文本对象。&lt;br&gt;
	/// 当地图处于 AddTextAction 操作状态时，将鼠标移至
	/// 地图上点击要添加文本的左上角的位置，此时文本对象就会添加到相应的图层上。
 	/// </summary>
	/// <param name="layerName" type="String">待编辑的 iServer 服务器端地图的图层名称。</param>
	/// <param name="textValue" type="String">要添加的文本的内容。 
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示新添加的实体。True 代表高亮显示该实体。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <returns type="SuperMap.UI.AddTextAction">返回 AddTextAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示添加文本对象的操作是否开始。</field>
	/// <field name="textValue" type="String">表示要添加的文本内容。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "添加文本";
    this._type = "AddTextAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerName, textValue, needHighlight, onComplete, onError]);
    this.actionStarted = false;
	this._datasetType = new SuperMap.DatasetType();
	this._needHighlight = needHighlight;
	this._layerName = layerName;
	this._layerType = new SuperMap.DatasetType().text;
	this._onComplete = onComplete;
	this._onError = onError;
	this.textValue = textValue;
};
SuperMap.UI.AddTextAction.prototype={
	initialize:function(mapControl){
	/// <summary>初始化 AddTextAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl 对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl = null;
    },	
    _onAddEntityComplete:function(editResult){
	/// <summary>添加文本操作完成时触发的事件。
	/// </summary>
	/// <param name="editResult" type="SuperMap.EditResult">编辑结果类。&lt;br&gt;
	/// 编辑操作返回的结果对象，包含操作成功是否的标识，以及被编辑的地物 ID、地图受影响的范围和操作之后的地图描述。</param>
        if(this._onComplete){
            this._onComplete(editResult);
        }
        this._mapControl.getMap().isEditing = true;
	    this._mapControl.refreshMapControl();
		this._mapControl.getMap().isEditing = false;
    },
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		var entity = new  SuperMap.Entity();
		entity.shape = new SuperMap.Geometry();
		entity.shape.feature = new SuperMap.FeatureType().text;
		entity.fieldValues = new Array();
		entity.fieldValues.push(this.textValue);
		var mapCoord = e.mapCoord;
		entity.shape.point2Ds = new Array(mapCoord);
		var onAddEntityCompleteDelegate = Function.createDelegate(this, this._onAddEntityComplete);
		this._mapControl.getMap().addEntity(this._layerName, entity, this._needHighlight, onAddEntityCompleteDelegate, this._onError);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        //更新
        if(!this.actionStarted){return false;}
    },	
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){
			return false;
		}
    },
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this.actionStarted = false;
    }
};
SuperMap.UI.AddTextAction.registerClass('SuperMap.UI.AddTextAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.UpdateEntityAction = function(layerName, layerType, needHighlight, onComplete,onError){
	/// <summary>在指定的iServer服务器端地图图层上更新指定的实体。&lt;br&gt;
	/// 1. 当地图处于UpdateEntityAction操作状态时，可以通过鼠标执行更新对象的操作。&lt;br&gt;
	/// 2. 更新实体有四个步骤：&lt;br&gt;
	///		第一鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，鼠标重新在地图上绘制该实体的位置与形状。右键表示取消更新操作。目前支持在点、线、面、文本图层中编辑对应类型的实体。
 	/// </summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="layerType" type="Number">图层的类型。数值型参数。 点图层=1，线图层=3，面图层=5，文本图层=7。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示新添加的实体。True代表高亮显示该实体。</param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <returns type="SuperMap.UI.UpdateEntityAction">返回 UpdateEntityAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "编辑地物";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
    this._type = "UpdateEntityAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerName, layerType, needHighlight, onComplete, onError]);
	this._datasetType = new SuperMap.DatasetType();
	this._queryParam  = null;
	this._layerType = layerType;
	this._selected = false;
	this._id = -1;//目标实体ID
	this.actionStarted = false;
	this._keyPoints = new Array();
	this._xs = new Array();
	this._ys = new Array();
    this._layerName = layerName;
	this._needHighlight = needHighlight;
    this._firstMapCoord;
    this._firstOffsetCoord = null;
    this._offsetX = 0;
    this._offsetY = 0;
	this._onComplete = onComplete;
	this._onError = onError;
};
SuperMap.UI.UpdateEntityAction.prototype={
    initialize:function(mapControl){
	/// <summary>初始化 UpdateEntityAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._layerName){
		    this._layerName = "";
		}
		if(!this._layerType){
		    this._layerType = this._datasetType.point;
		}
		this._queryParam = new SuperMap.QueryParam();
		this._queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = this._layerName;
		// queryLayer.sqlParam = new SuperMap.SqlParam();
		// queryLayer.sqlParam.whereClause = "smid > 0";
		this._queryParam.queryLayerParams.push(queryLayer);
		// 只返回一个地物
		this._queryParam.expectCount = 1;
		this._queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl.customLayer.removeMarker("PointUpdating");
        this._mapControl.customLayer.removeLine("LineUpdating");
        this._mapControl.customLayer.removeLine("LineDrawing");
        this._mapControl.customLayer.removePolygon("PolygonUpdating");
        this._mapControl.customLayer.removePolygon("PolygonDrawing");
        this._mapControl = null;
    },
    onUpdateEntityComplete:function(editResult){
	/// <summary>更新实体操作完成时触发的事件。
	/// </summary>
	/// <param name="editResult" type="SuperMap.EditResult">编辑结果类。&lt;br&gt;
	/// 编辑操作返回的结果对象，包含操作成功是否的标识，以及被编辑的地物ID、地图受影响的范围和操作之后的地图描述。</param>
        this._selected = false;
        this._mapControl.customLayer.removeMarker("PointUpdating");
        this._mapControl.customLayer.removeLine("LineUpdating");
		this._mapControl.customLayer.removePolygon("PolygonUpdating");
        this._mapControl.customLayer.removePolygon("PolygonDrawing");
	    this._mapControl.getMap().isEditing = true;
	    this._mapControl.refreshMapControl();
		this._mapControl.getMap().isEditing = false;
        if(this._onComplete){
            this._onComplete(editResult);
        }
    },
    onQueryComplete:function(resultSet){
	/// <summary>查询指定位置处对应的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 更新实体有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，鼠标重新在地图上绘制该实体的位置与形状。&lt;br&gt;
	/// 该事件在第二步完成时触发。
	/// </summary>
	/// <param name="resultSet" type="SuperMap.ResultSet">根据鼠标点击的空间位置，在编辑的图层中查找对应的实体对象的返回结果集合。</param>
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    this._mapControl.customLayer.removePolygon("RectQuery");
        if(resultSet != null && resultSet.totalCount > 0){
            //高亮出来
            if(this._layerType == this._datasetType.point){
                var x = resultSet.recordSets[0].records[0].center.x + this._offsetX;
                var y = resultSet.recordSets[0].records[0].center.y + this._offsetY;
               
                this._mapControl.customLayer.insertMarker("PointUpdating", x, y, 10, 10);
                this._id  = resultSet.recordSets[0].records[0].fieldValues[0];
                this._selected = true;
            }
			var onGetEntityCompleteDelegate = Function.createDelegate(this, this.onGetEntityComplete);
            if(this._layerType == this._datasetType.line){
                if(resultSet.recordSets[0].records[0].shape) {
					// 获取到对应的地物要素
					var entity = resultSet.recordSets[0].records[0];
					entity.id = resultSet.recordSets[0].records[0].fieldValues[0];
					this.onGetEntityComplete(entity);
				} else {
					//没有点串信息,只好再GetEntity
					this._mapControl.getMap().getEntity(this._layerName,eval(resultSet.recordSets[0].records[0].fieldValues[0]), false, onGetEntityCompleteDelegate, null, null);
				}
            } else if(this._layerType == this._datasetType.region){
				if(resultSet.recordSets[0].records[0].shape) {
					// 获取到对应的地物要素
					var entity = resultSet.recordSets[0].records[0];
					entity.id = resultSet.recordSets[0].records[0].fieldValues[0];
					this.onGetEntityComplete(entity);
				} else {
                //没有点串信息,只好再GetEntity
                this._mapControl.getMap().getEntity(this._layerName,eval(resultSet.recordSets[0].records[0].fieldValues[0]), false, onGetEntityCompleteDelegate, null, null);
				}
            }
        }else{
            //alert("没有选中地物");
			alert(SuperMap.Resource.ins.getMessage("noPickOnEntity"));
            this._mapControl.customLayer.removeMarker("PointUpdating");
            this._mapControl.customLayer.removeLine("LineUpdating");
            this._mapControl.customLayer.removePolygon("PolygonUpdating");
			this._id  = -1;
			this._selected = false;
        }
    },
    onGetEntityComplete:function(entity){
	/// <summary>在查询结果中选取更新的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 更新实体有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，鼠标重新在地图上绘制该实体的位置与形状。&lt;br&gt;
	/// 该事件在第三步完成时触发。
	/// </summary>
	/// <param name="entity" type="SuperMap.Entity">要更新的实体对象。</param>
        if(entity != null && entity.shape != null){
            this._id = entity.id;
            var pointsX = new Array();
            var pointsY = new Array();
            for(var i = 0; i < entity.shape.point2Ds.length; i++){
                pointsX.push(entity.shape.point2Ds[i].x + this._offsetX);
                pointsY.push(entity.shape.point2Ds[i].y + this._offsetY);
            }
            if(this._layerType == this._datasetType.line){
                this._mapControl.customLayer.insertLine("LineUpdating", pointsX, pointsY, this.strokeWeight, this.strokeColor); 
            } else if(this._layerType == this._datasetType.region){
                this._mapControl.customLayer.insertPolygon("PolygonUpdating", pointsX, pointsY, this.strokeWeight, this.strokeColor, "white", 0.6);
            } else if(this._layerType == this._datasetType.point || this._layerType == this._datasetType.text) {
				// 编辑点选地物所使用，文本地物暂时也采用点地物的方式
				var x = pointsX[0];
				var y = pointsY[0];
				this._mapControl.customLayer.insertMarker("PointUpdating", x, y, 10, 10);
			}
            //清空一下
            while(pointsX.length>0) {
				pointsX.pop();
				pointsY.pop();
	   	    }
	   	    pointsX = null;
	   	    pointsY = null;
            this._selected = true;
        }
    },
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		//  点地物和文本地物暂时使用同一种方式处理
        if(this._selected){
   	        var offsetCoord = e.mapCoord;
            if(this._layerType == this._datasetType.point || this._layerType == this._datasetType.text){
	            //进行更新
	            var entity = new SuperMap.Entity();
                entity.shape = new SuperMap.Geometry();
                entity.id = this._id;
				entity.shape.id = this._id;
                if(this._layerType == this._datasetType.point) {
					entity.shape.feature = new SuperMap.FeatureType().point;
				} else {
					entity.shape.feature = new SuperMap.FeatureType().text;
				}
                var mapCoord = e.mapCoord;
                entity.shape.point2Ds = new Array(e.mapCoord);
                var entities = new Array(entity);
			    var updateEntityCompleteDelegate = Function.createDelegate(this, this.onUpdateEntityComplete);
                this._mapControl.getMap().updateEntity(this._layerName, entities, this._needHighlight, updateEntityCompleteDelegate, this._onError);
		    }
		    if(this._layerType == this._datasetType.line){
	            // 已选中对象,可以开始更新
			    if(this.actionStarted){
				    this._xs.pop();
				    this._ys.pop();
				    this._keyPoints.pop();
			    }
			    this._xs.push(offsetCoord.x);
			    this._ys.push(offsetCoord.y);
			    this._keyPoints.push(e.mapCoord);
		        this.actionStarted = true;
		        this._keyPoints.push(e.mapCoord);
		        this._xs.push(offsetCoord.x);
			    this._ys.push(offsetCoord.y);
		    }
		    if(this._layerType == this._datasetType.region){
	            if(!this.actionStarted){
				    this._firstMapCoord = e.mapCoord;
        		    this._firstOffsetCoord = offsetCoord;
		        } else {
			        this._xs.pop();
				    this._ys.pop();
				    this._keyPoints.pop();
		        }
			    this._xs.push(offsetCoord.x);
			    this._ys.push(offsetCoord.y);
			    this._keyPoints.push(e.mapCoord);
		        this.actionStarted = true;
		        this._keyPoints.push(e.mapCoord);
		        this._xs.push(this._firstOffsetCoord.x);
			    this._ys.push(this._firstOffsetCoord.y);
		    }
        }
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){return false;}
        var offsetCoord = e.mapCoord;
        if(this._selected){
            if(this._layerType == this._datasetType.line){
				this._keyPoints.pop();
		        this._keyPoints.push(e.mapCoord);
		        this._xs.push(offsetCoord.x);
				this._ys.push(offsetCoord.y);
		        this._mapControl.customLayer.removeLine("LineDrawing");
		        var entity = new  SuperMap.Entity();
                entity.shape = new SuperMap.Geometry();
                entity.id = this._id;
                entity.shape.feature = new SuperMap.FeatureType().line;
                entity.shape.point2Ds = this._keyPoints;
				entity.shape.id = this._id;
                var entities = new Array(entity);
				var onUpdateEntityCompleteDelegate = Function.createDelegate(this, this.onUpdateEntityComplete);
                this._mapControl.getMap().updateEntity(this._layerName, entities, false, onUpdateEntityCompleteDelegate, this._onError);
		        while(this._keyPoints.length > 0){
                    this._keyPoints.pop();
                }
                while(this._xs.length > 0){
                    this._xs.pop();
					this._ys.pop();
                }
                this._firstMapCoord = null;
       		    this._firstOffsetCoord = null;
       		    this._offsetX = 0;
       		    this._offsetY = 0;
		        this.actionStarted = false;
		    } else if(this._layerType == this._datasetType.region){
				this._keyPoints.pop();
		        this._keyPoints.push(e.mapCoord);
		        this._keyPoints.push(this._firstMapCoord);
		        this._xs.pop();
				this._ys.pop();
		        this._xs.push(offsetCoord.x);
				this._ys.push(offsetCoord.y);
		        this._xs.push(this._firstOffsetCoord.x);
				this._ys.push(this._firstOffsetCoord.y);
		        this._mapControl.customLayer.removePolygon("PolygonDrawing");
		        var entity = new SuperMap.Entity();
                entity.shape = new SuperMap.Geometry();
                entity.id = this._id;
                entity.shape.feature = new SuperMap.FeatureType().polygon;
                entity.shape.point2Ds = this._keyPoints;
				entity.shape.id = this._id;
                var entities = new Array(entity);
				var onUpdateEntityCompleteDelegate = Function.createDelegate(this, this.onUpdateEntityComplete);
                this._mapControl.getMap().updateEntity(this._layerName, entities, false, onUpdateEntityCompleteDelegate, this._onError);
		        while(this._keyPoints.length > 0){
                    this._keyPoints.pop();
                }
                while(this._xs.length > 0){
                    this._xs.pop();
					this._ys.pop();
                }
                this._firstMapCoord = null;
       		    this._firstOffsetCoord = null;
       		    this._offsetX = 0;
       		    this._offsetY = 0;
		        this.actionStarted = false;
		    }
		}
    },
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){return false;}
        var offsetCoord = e.mapCoord;
        if(this._selected){
            if(this._layerType == this._datasetType.line){
		        this._keyPoints.pop();
		        this._xs.pop();
			    this._ys.pop();
		        this._keyPoints.push(e.mapCoord);
		        this._xs.push(offsetCoord.x);
			    this._ys.push(offsetCoord.y);
                this._mapControl.customLayer.insertLine("LineDrawing", this._xs, this._ys, this.strokeWeight, this.strokeColor);
            } else if(this._layerType == this._datasetType.region){
                this._keyPoints.pop();
		        this._xs.pop();
			    this._ys.pop();
		        this._keyPoints.push(e.mapCoord);
		        this._xs.push(offsetCoord.x);
			    this._ys.push(offsetCoord.y);
                this._mapControl.customLayer.insertPolygon("PolygonDrawing", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
            }
        }else{
		    if(!this.actionStarted){return false;}
		    while(this._xs.length>0){
			    this._xs.pop();
			    this._ys.pop();
		    }
		    this._xs.push(this._firstOffsetCoord.x);
		    this._xs.push(offsetCoord.x);
		    this._xs.push(offsetCoord.x);
		    this._xs.push(this._firstOffsetCoord.x);
		    this._ys.push(this._firstOffsetCoord.y);
		    this._ys.push(this._firstOffsetCoord.y);
		    this._ys.push(offsetCoord.y);
		    this._ys.push(offsetCoord.y);
            this._mapControl.customLayer.insertPolygon("RectQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
        }
    },
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this._selected){
   	        var offsetCoord = e.mapCoord;
		    this._mapRect = null;
		    this.actionStarted = true;
		    this._firstMapCoord = e.mapCoord;
		    this._firstOffsetCoord = offsetCoord;
		    this._offsetX = offsetCoord.x - e.mapCoord.x;
		    this._offsetY = offsetCoord.y - e.mapCoord.y;
		    while(this._xs.length > 0){
			    this._xs.pop();
			    this._ys.pop();
		    }
        }
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this._selected){
		    this.actionStarted = false;
		    var qe = new SuperMap.QueryingEventArgs();
	        qe.queryParams = this._queryParam;//point
	        qe.clientActionArgs = new SuperMap.ActionEventArgs();
	        qe.clientActionArgs.mapCoords = new Array();
	        qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
	        qe.clientActionArgs.mapCoords[1] = e.mapCoord;
            if(this._onStart){this._onStart(qe);}
		    // 直接去调用查询
		    var left = this._firstMapCoord.x > e.mapCoord.x ? e.mapCoord.x: this._firstMapCoord.x;
		    var right = this._firstMapCoord.x > e.mapCoord.x ?  this._firstMapCoord.x : e.mapCoord.x;
		    var bottom = this._firstMapCoord.y > e.mapCoord.y ? e.mapCoord.y : this._firstMapCoord.y;
		    var top = this._firstMapCoord.y > e.mapCoord.y ?  this._firstMapCoord.y : e.mapCoord.y;
		    var mapRect = new SuperMap.Rect2D(left, bottom, right, top);
		    var onQueryCompleteDelegate = Function.createDelegate(this, this.onQueryComplete);
		    this._mapControl.getMap().queryByRect(mapRect, this._queryParam, false, onQueryCompleteDelegate, this._onError)
        }
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this._selected = false;
        this.actionStarted = false
        while(this._keyPoints.length > 0){
            this._keyPoints.pop();
        }
        while(this._xs.length > 0){
            this._xs.pop();
			this._ys.pop();
        }
        this._firstMapCoord = null;
        this._mapControl.customLayer.removeMarker("PointUpdating");
        this._mapControl.customLayer.removeLine("LineUpdating");
        this._mapControl.customLayer.removeLine("LineDrawing");
        this._mapControl.customLayer.removePolygon("PolygonUpdating");
        this._mapControl.customLayer.removePolygon("PolygonDrawing");
    }
};
SuperMap.UI.UpdateEntityAction.registerClass('SuperMap.UI.UpdateEntityAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.DeleteEntityAction=function(layerName, layerType, onComplete, onError){
	/// <summary>在指定的 iServer 服务器端地图图层上删除指定的实体。&lt;br&gt;
	/// 当地图处于 DeleteEntityAction 操作状态时，可以通过鼠标执行删除对象的操作。&lt;br&gt;
	/// 删除实体有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要删除的实体；&lt;br&gt;
	///		第二，iServer 服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，单击鼠标表示执行删除选中实体对象的操作。&lt;br&gt;
	///	目前支持在点、线、面、文本图层中编辑对应类型的实体。&lt;br&gt;
	/// 注：当选中的实体对象不是要删除的对象时，可以从第一步重新开始选中实体对象。
 	/// </summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="layerType" type="Number">图层的类型。数值型参数。 点图层=1，线图层=3，面图层=5，文本图层=7。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <returns type="SuperMap.UI.DeleteEntityAction">返回 DeleteEntityAction 对象。</returns>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "删除地物";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this._type = "DeleteEntityAction";
	this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerName, layerType, onComplete, onError]);
	this._datasetType = new SuperMap.DatasetType();
	this._queryParam  = null;
	this._layerType = layerType;
	this._layerName = layerName;
	this._selected = false;
	this._id = -1;//目标实体ID
	this._onComplete = onComplete;
	this._onError = onError;
	this._xs = new Array();
	this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
    this._offsetX = 0;
    this._offsetY = 0;
};
SuperMap.UI.DeleteEntityAction.prototype={
    initialize:function(mapControl){
	/// <summary>初始化 DeleteEntityAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._layerName){
		    this._layerName = "";
		}
		if(!this._layerType){
		    this._layerType = type.point;
		}
		this._queryParam = new SuperMap.QueryParam();
		this._queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = this._layerName;
		//queryLayer.sqlParam = null;
		var orgfloor=document.getElementById("orgfloor").value ;
		queryLayer.sqlParam = new SuperMap.SqlParam();
		queryLayer.sqlParam.whereClause = " C_ORGFLOOR like '%"+orgfloor+"%'";
		this._queryParam.queryLayerParams.push(queryLayer);
		this._queryParam.expectCount = 10;
		this._queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl.customLayer.removeMarker("PointDeleting");
        this._mapControl.customLayer.removeLine("LineDeleting");
        this._mapControl.customLayer.removePolygon("PolygonDeleting");
        this._mapControl = null;
    },
    onDeleteEntityComplete:function(editResult){
	/// <summary>删除实体操作完成时触发的事件。
	/// </summary>
	/// <param name="editResult" type="SuperMap.EditResult">编辑结果类。&lt;br&gt;
	/// 编辑操作返回的结果对象，包含操作成功是否的标识，以及被编辑的地物ID、地图受影响的范围和操作之后的地图描述。</param>
        this._selected = false;
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
        this._mapControl.customLayer.removeMarker("PointDeleting");
        this._mapControl.customLayer.removeLine("LineDeleting");
        this._mapControl.customLayer.removePolygon("PolygonDeleting");
	    this._mapControl.getMap().isEditing = true;
	    //删除地图之后重新查询
		var doubleRect=getDoubleRect();
		queryRect(doubleRect);
	    
		this._mapControl.getMap().isEditing = false;
        if(this._onComplete){
            this._onComplete(editResult);
        }
    },
    onQueryComplete:function(resultSet){
	/// <summary>查询指定位置处对应的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 删除实体有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要删除的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，单击鼠标表示执行删除选中实体对象的操作。&lt;br&gt;
	// 该事件在第二步完成时触发。
	/// </summary>
	/// <param name="resultSet" type="SuperMap.ResultSet">根据鼠标点击的空间位置，在编辑的图层中查找对应的实体对象的返回结果集合。</param>
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    this._mapControl.customLayer.removePolygon("RectQuery");
	    var scale=this._mapControl.get_mapScale();
	    if(scale>=getMinScale(page_res["bankLayerName"])){
			if(resultSet != null && resultSet.totalCount > 0){
				if(resultSet.recordSets && resultSet.recordSets[0] && resultSet.recordSets[0].records && resultSet.recordSets[0].records[0].shape) {
					// 获取到对应的地物要素
					var entity = resultSet.recordSets[0].records[0];
					entity.id = resultSet.recordSets[0].records[0].fieldValues[0];
					this.onGetEntityComplete(entity);
				} else {
					var onGetEntityCompleteDelegate = Function.createDelegate(this, this.onGetEntityComplete);
				    //没有点串信息,只好再GetEntity
				    this._mapControl.getMap().getEntity(this._layerName, eval(resultSet.recordSets[0].records[0].fieldValues[0]), false, onGetEntityCompleteDelegate);
				}
	        } else {
	            //alert("没有选中地物");
				alert(SuperMap.Resource.ins.getMessage("noPickOnEntity"));
	            this._mapControl.customLayer.removeMarker("PointDeleting");
	            this._mapControl.customLayer.removeLine("LineDeleting");
	            this._mapControl.customLayer.removePolygon("PolygonDeleting");
				this._id  = -1;
				this._selected = false;
	        }
	     }
    },
    onGetEntityComplete:function(entity){
	/// <summary>在查询结果中选取更新的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 删除实体有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要删除的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，单击鼠标表示执行删除选中实体对象的操作。&lt;br&gt;
	/// 该事件在第三步完成时触发。
	/// </summary>
	/// <param name="entity" type="SuperMap.Entity">要更新的实体对象。</param>
        if(entity != null && entity.shape != null){
            this._id = entity.id;
			var index = 0;
			
			for(var j = 0 ; j < entity.shape.point2Ds.length;j++) {
	           // for(var i = 0; i < entity.shape.parts[j]; i++){
	                this._xs.push(entity.shape.point2Ds[index].x + this._offsetX);
	                this._ys.push(entity.shape.point2Ds[index++].y + this._offsetY);
	            }
	            if(this._layerType == this._datasetType.line){
	                this._mapControl.customLayer.insertLine("LineDeleting" + j, this._xs, this._ys, this.strokeWeight, this.strokeColor); 
	            } else if(this._layerType == this._datasetType.region){
	                this._mapControl.customLayer.insertPolygon("PolygonDeleting" + j, this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	            } else if(this._layerType == this._datasetType.point || this._layerType == this._datasetType.text){
					var x = this._xs[0];
					var y = this._ys[0];
					this._mapControl.customLayer.insertMarker("PointDeleting" + j, x, y, 10, 10);
				}
				/*
				while(this._xs.length > 0){
					this._xs.pop();
					this._ys.pop();
				}
			}*/
			//alert(entity.fieldNames[0]);
			//alert(entity.fieldValues[0]);
			var oResult = confirm("删除ID为：" + entity.id + "的地物？");
    		if(oResult){
				var ids = new Array();
				ids[0] = entity.id;
				var onDeleteEntityCompleteDelegate = Function.createDelegate(this, this.onDeleteEntityComplete);
				this._mapControl.getMap().deleteEntity(this._layerName, ids, onDeleteEntityCompleteDelegate, this._onError);
			}
            this._selected = true;
        }
    },
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){return false;}
        var offsetCoord = e.mapCoord;
	    if(!this.actionStarted){return false;}
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    this._xs.push(this._firstOffsetCoord.x);
	    this._xs.push(offsetCoord.x);
	    this._xs.push(offsetCoord.x);
	    this._xs.push(this._firstOffsetCoord.x);
	    this._ys.push(this._firstOffsetCoord.y);
	    this._ys.push(this._firstOffsetCoord.y);
	    this._ys.push(offsetCoord.y);
	    this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("RectQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
    },
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        var offsetCoord = e.mapCoord;
	    this._mapRect = null;
	    this.actionStarted = true;
	    this._firstMapCoord = e.mapCoord;
	    this._firstOffsetCoord = offsetCoord;
	    this._offsetX = offsetCoord.x - e.mapCoord.x;
	    this._offsetY = offsetCoord.y - e.mapCoord.y;
	    while(this._xs.length > 0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    this.actionStarted = false;
	    var qe = new SuperMap.QueryingEventArgs();
        qe.queryParams = this._queryParam;//point
        qe.clientActionArgs = new SuperMap.ActionEventArgs();
        qe.clientActionArgs.mapCoords = new Array();
        qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
        qe.clientActionArgs.mapCoords[1] = e.mapCoord;
        if(this._onStart){this._onStart(qe);}
	    // 直接去调用查询
	    var left = this._firstMapCoord.x > e.mapCoord.x ? e.mapCoord.x : this._firstMapCoord.x;
	    var right = this._firstMapCoord.x > e.mapCoord.x ? this._firstMapCoord.x : e.mapCoord.x;
	    var bottom = this._firstMapCoord.y > e.mapCoord.y ? e.mapCoord.y: this._firstMapCoord.y;
	    var top = this._firstMapCoord.y > e.mapCoord.y?  this._firstMapCoord.y : e.mapCoord.y;
	    var mapRect = new SuperMap.Rect2D(left, bottom, right, top);
	    var onQueryCompleteDelegate = Function.createDelegate(this, this.onQueryComplete);
	    this._mapControl.getMap().queryByRect(mapRect, this._queryParam, false, onQueryCompleteDelegate, this._onError)
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = false;
		this._mapControl.customLayer.removeMarker("PointDeleting");
        this._mapControl.customLayer.removeLine("LineDeleting");
        this._mapControl.customLayer.removePolygon("PolygonDeleting");
		this._id  = -1;
		this._selected = false;
		while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
    }
};
SuperMap.UI.DeleteEntityAction.registerClass('SuperMap.UI.DeleteEntityAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.UpdatePropertyAction=function(layerName, layerType, onComplete, onError){
	/// <summary>在指定的iServer服务器端地图图层上更新实体对象的属性信息。&lt;br&gt;
	/// 当地图处于UpdatePropertyAction操作状态时，可以通过鼠标执行更新对象属性信息的操作。&lt;br&gt;
	/// 更新实体的属性信息有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///		第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，通过updateFieldValues更新属性信息。&lt;br&gt;
	/// 目前支持在点、线、面、文本图层中编辑对应类型的实体。&lt;br&gt;
	/// 注：当选中的实体对象不是要编辑的对象时，可以从第一步重新开始选中实体对象。
 	/// </summary>
	/// <param name="layerName" type="String">待编辑的iServer服务器端地图的图层名称。</param>
	/// <param name="layerType" type="Number">图层的类型。数值型参数。 点图层=1，线图层=3，面图层=5，文本图层=7。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <returns type="SuperMap.UI.UpdatePropertyAction">返回 UpdatePropertyAction 对象。</returns>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "编辑属性";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
    this._type = "UpdatePropertyAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerName, layerType, onComplete, onError]);
	this._datasetType = new SuperMap.DatasetType();
	this._queryParam  = null;
	this._layerType = layerType;
	this._layerName = layerName;
	this._selected = false;
	this._onComplete = onComplete;
	this._onError = onError;
	this._xs = new Array();
	this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
    this._offsetX = 0;
    this._offsetY = 0;
    this._entity;
    this.captionNames=new Array();
    this.bankId=null ;
};
SuperMap.UI.UpdatePropertyAction.prototype={
    initialize:function(mapControl){
	/// <summary>初始化UpdatePropertyAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		} else {
			//this._mapControl.get_container().style.cursor = "crosshair";
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		}
		if(!this._layerName){
		    this._layerName = "";
		}
		if(!this._layerType){
		    this._layerType = type.point;
		}
		this._queryParam = new SuperMap.QueryParam();
		this._queryParam.queryLayerParams = new Array();
		var queryLayer = new SuperMap.QueryLayerParam();
		queryLayer.name = this._layerName;
		queryLayer.sqlParam = null;
		queryLayer.sqlParam = new SuperMap.SqlParam();
		var orgfloor=document.getElementById("orgfloor").value ;
		queryLayer.sqlParam.whereClause = " C_ORGFLOOR like '%"+orgfloor+"%'";
		this._queryParam.queryLayerParams.push(queryLayer);
		this._queryParam.expectCount = 10;
		this._queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
	},
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
        this._mapControl.customLayer.removeMarker("PointDeleting");
        this._mapControl.customLayer.removeLine("LineDeleting");
        this._mapControl.customLayer.removePolygon("PolygonDeleting");
        this._mapControl = null;
    },
    onQueryComplete:function(resultSet){
	/// <summary>查询指定位置处对应的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 更新实体的属性信息有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///     第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，通过updateFieldValues更新属性信息。&lt;br&gt;
	/// 该事件在第二步完成时触发。
	/// </summary>
	/// <param name="resultSet" type="SuperMap.ResultSet">根据鼠标点击的空间位置，在编辑的图层中查找对应的实体对象的返回结果集合。</param>
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    this._mapControl.customLayer.removePolygon("RectQuery");
	    var scale=this._mapControl.get_mapScale();
	    if(scale>=getMinScale(page_res["bankLayerName"])){
			if(resultSet != null && resultSet.totalCount > 0){
				if(resultSet.recordSets && resultSet.records && resultSet.records[0] && resultSet.records[0].shape) {
					var entity = resultSet.recordSets[0].records[0];
					entity.id = resultSet.recordSets[0].records[0].fieldValues[0];
					this.onGetEntityComplete(entity);
				} else {
					var onGetEntityCompleteDelegate = Function.createDelegate(this, this.onGetEntityComplete);
				    //没有点串信息,只好再GetEntity
				    this.captionNames = resultSet.recordSets[0].returnFieldCaptions;
				    //alert("captionName:"+this.captionNames);
				    this._mapControl.getMap().getEntity(this._layerName, eval(resultSet.recordSets[0].records[0].fieldValues[0]), true, onGetEntityCompleteDelegate);
				}
	        } else {
	            //alert("没有选中地物");
				alert(SuperMap.Resource.ins.getMessage("noPickOnEntity"));
	            this._mapControl.customLayer.removeMarker("PointDeleting");
	            this._mapControl.customLayer.removeLine("LineDeleting");
	            this._mapControl.customLayer.removePolygon("PolygonDeleting");
	            this._entity = null;
				this._selected = false;
	        }
        }
    },
    onGetEntityComplete:function(entity){
	/// <summary>在查询结果中选取更新的实体对象的操作完成时触发的事件。&lt;br&gt;
	/// 更新实体的属性信息有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///     第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，通过updateFieldValues更新属性信息。&lt;br&gt;
	/// 该事件在第三步完成时触发。
	/// </summary>
	/// <param name="entity" type="SuperMap.Entity">要更新的实体对象。</param>
        if(entity != null && entity.shape != null){
            this._entity = new SuperMap.Entity();
            this._entity.copy(entity);
			/**
			*	author zxxu
			**/
			var captionNames = this.captionNames;
			
			var index = 0;
			for(var j = 0 ; j < this._entity.shape.point2Ds.length;j++) {
	                this._xs.push(this._entity.shape.point2Ds[index].x + this._offsetX);
	                this._ys.push(this._entity.shape.point2Ds[index++].y + this._offsetY);
	            }
	            if(this._layerType == this._datasetType.line){
	                this._mapControl.customLayer.insertLine("LineDeleting" + j, this._xs, this._ys, this.strokeWeight, this.strokeColor); 
	            } else if(this._layerType == this._datasetType.region){
	                this._mapControl.customLayer.insertPolygon("PolygonDeleting" + j, this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	            } else if(this._layerType == this._datasetType.point || this._layerType == this._datasetType.text){
					var x = this._xs[0];
					var y = this._ys[0];
					this._mapControl.customLayer.insertMarker("PointDeleting" + j, x, y, 10, 10);
				}
			
			var smid = this._entity.id;
			var layerName = this._layerName;
			
			var newDiv=document.createElement("div");//创建div
			//newDiv.class="superShowDiv";
			newDiv.id = "updateProperty";
			newDiv.style.position="absolute";//relative
			newDiv.style.backgroundColor="#D0E4F7";
			newDiv.style.height="120px";
			newDiv.style.width="250px";
			newDiv.style.border="#CCCCCC 2px solid";
			newDiv.style.top=180;
			newDiv.style.left=360 ;
			newDiv.style.fontSize="12px"
			newDiv.style.lineHeight="25px"
			newDiv.style.padding="5px"
			newDiv.style.zIndex="100"
			
			var strHTML = "<table width='100%'><tr><td align='left'><b>修改网点属性：</b></td><td align='right'><img src='../../images/supGis/del.gif' onclick='document.body.removeChild(document.getElementById(\"updateProperty\"));'></td></tr></table>";
			strHTML += "<br><div  style=\'color:#CCCCCC;position:absolute; top:20%;\'><table cellspacing='1' cellpadding='1'>";
			var i;
			for(i = 0; i < this._entity.fieldNames.length; i++){
				var filedName = this._entity.fieldNames[i];
				
				//alert("fieldNames:"+this._entity.fieldNames[i]+" "+this._entity.fieldValues[i]);
				captionName=captionNames[i];
				/**
				*  author zxxu
				**/
				if(filedName == "C_BANK_ID"){
					strHTML += "<tr>";
					strHTML += "<td width='100%' align='center'>";
					strHTML += "<font class='superMapfont'>"+captionName+"：</font>";
					strHTML += "<input size='20' type=text id='update_"+filedName+"' name='" + filedName + "' value='" + this._entity.fieldValues[i] + "'>";
					strHTML += "</td>";
					strHTML += "</tr>";
				}
				if(filedName == "C_BANK_STATUS"){
					var type=this._entity.fieldValues[i];
					if(type=="0"){
						strHTML += "<tr>";
						strHTML += "<td width='100%' align='center'>";
						strHTML += "<font class='superMapfont'>类型"+"：</font>&nbsp;&nbsp;";
						strHTML += "网点<input type='radio' id='update_"+filedName+"' name='" + filedName + "' value='0' checked='checked'>";
						strHTML += "&nbsp;&nbsp;选址需求点<input type='radio' id='update_"+filedName+"' name='" + filedName + "' value='1' >";
						strHTML += "</td>";
						strHTML += "</tr>";
					}
					if(type=="1"){
					strHTML += "<tr>";
						strHTML += "<td width='100%' align='center'>";
						strHTML += "<font class='superMapfont'>类型"+"：</font>&nbsp;&nbsp;";
						strHTML += "网点<input type='radio' id='update_"+filedName+"' name='" + filedName + "' value='0' >";
						strHTML += "&nbsp;&nbsp;选址需求点<input type='radio' id='update_"+filedName+"' name='" + filedName + "' value='1' checked='checked'>";
						strHTML += "</td>";
						strHTML += "</tr>";
					}
				}
			}
			strHTML += "<tr><td align=center><input type=button value='更新' onclick=\"mapControl.get_action().updateFieldValues(" + this._entity.id + ", '" + this._layerName + "', document.getElementsByTagName('input'));\">";
			strHTML += "&nbsp;&nbsp;<input type='button' value='关闭' onclick='document.body.removeChild(document.getElementById(\"updateProperty\"));'></td></tr></table></div>";

			newDiv.style.opacity="0.5"; 
			newDiv.style.filter="alpha(opacity=70)"
			
			newDiv.innerHTML="<div class='superShowDiv'>"+strHTML+"</div>" ;
			document.body.appendChild(newDiv);
			//var aWin;
			//aWin = window.open("","更新地物属性","height=300,width=400,top=300,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
			//aWin.document.write(strHTML);
			//aWin.document.close();
			//aWin.focus();
			
            this._selected = true;
        }
    },
    
   
   
   	/**
   		2009-11-19 add by lyang
    	扩展map,测试网点是否存在的回调方法
    	isExsit:
    	0:标识业务数据网点存在，而GIS数据网点不存在，可以继续添加网点
    	1:标识业务数据网点不存在,不可以继续添加网点, 不可以继续添加网点
    	2:标识业务数据网点存在，而GIS数据网点也存在，不可以继续添加网点
   	*/
   	_onUpdateBankCompete:function(message){
   			var isExist=message.split("|")[0];
   			var orgfloor=message.split("|")[1];
   			var type=message.split("|")[2];
   			var bankName=message.split("|")[3];
   			var bankid=document.getElementById("update_C_BANK_ID").value ;
	    	if(isExist=="0"){
		    	// 去掉图形信息，只编辑属性信息 
			     this._entity.shape = null;
			     if(bankName!=null && bankName!=""){
			     	 if(type!=null && type=="0"){
			    		bankName=bankName.substring(6,bankName.length);
			    	 }
			     }
				 this._entity.fieldNames = ["C_ORGFLOOR","C_BANK_ID","C_BANK_STATUS","C_BANK_NAME"];
				 this._entity.fieldValues = [orgfloor,bankid,type,bankName];
			    this._mapControl.getMap().updateEntity(this._layerName, this._entity, false, this._onComplete, this._onError);
			    this._selected = false;
			}else if(isExist=="2"){
				alert("您要修改的网点编号地图里已经存在");
			}else{
				alert("您要修改的网点编号不存在");
			}
			document.body.removeChild(document.getElementById('updateProperty'));
	  },
	  
	  /**
	  *回调方法2，当bankid没变时，获取orgfloor的方法
	  */
	  _onUpdateBankCompete2:function(message){
		    	// 去掉图形信息，只编辑属性信息
			    this._entity.shape = null;
			    var orgfloor=message ;
			    var bankid=document.getElementById("update_C_BANK_ID").value ;
   				var bankname=document.getElementById("update_C_BANK_NAME").value ;
   				var bankaddress=document.getElementById("update_C_BANK_ADDRESS").value ;
			    
				this._entity.fieldNames = ["C_ORGFLOOR","C_BANK_ID","C_BANK_NAME","C_BANK_ADDRESS"];
				this._entity.fieldValues = [orgfloor,bankid,bankname,bankaddress];
			    
			    this._mapControl.getMap().updateEntity(this._layerName, this._entity, false, this._onComplete, this._onError);
			    this._selected = false;
			    document.body.removeChild(document.getElementById('updateProperty'));
	  },
    
    
    /**
    	add by lyang 2009-11-19
    	修改网点属性前，先判断，新的网点号是否属于业务数据库里存在的，而GIS数据库里不存在的
    */
    updateFieldValues:function(smid, layerName, objInputs){
	/// <summary>将layerName图层中的smid对应的几何实体的属性值进行更新。&lt;br&gt;
	/// 更新实体的属性信息有四个步骤：&lt;br&gt;
	///		第一，鼠标在地图上点击要更新的实体；&lt;br&gt;
	///		第二，iServer服务根据鼠标点击的空间位置到编辑的图层中查找对应的实体对象，返回查选的结果集合；&lt;br&gt;
	///     第三，在查询结果集合中选取距离查询点最近的一个实体；&lt;br&gt;
	///		第四，通过updateFieldValues更新属性信息。&lt;br&gt;
	/// 该方法执行第四步操作。
	/// </summary>
	/// <param name="smid" type="String">地物的smid。</param>
	/// <param name="layerName" type="String">地物所在图层的名称。</param>
	/// <param name="objInputs" type="Array" elementType="DomElement">DOM对象数组，地物需要修改的属性值数组。</param>
      try{
        var keys = new Array();
        var values = new Array();
        var iIndex = 0;
        var i = 0;
        var bankId=null ;
        var newBankId=null ;
        var type=null ;
        var newType=null ;
        
        for(i = 0; i < objInputs.length; i++){
          if(objInputs[i].type == "text"||objInputs[i].type=="radio"){
          	if(objInputs[i].id.indexOf("update_")>-1){
          		if(objInputs[i].name.indexOf("C_BANK_STATUS")>-1){
          			keys[iIndex] = objInputs[i].name;
          			if(objInputs[i].checked){
          				values[iIndex]=objInputs[i].value ;
          				newType=values[iIndex] ;
          			}
          			iIndex++;
          		}else{
		            keys[iIndex] = objInputs[i].name;
		            values[iIndex] = objInputs[i].value;
		            iIndex++;
		         }
	          }
          }
        }
        for(i = 0; i < this._entity.fieldNames.length; i++){
            for(var j = 0; j < keys.length; j++){
            	/**
            		modify by lyang 2009-11-19
            		1,有无修改类型，选址需求点-网点
            		2,先获取以前的网点行号，和新的网点行号，如不等，则需验证新网点行号的是否符合规则
            	*/
            	if(this._entity.fieldNames[i] == "C_BANK_ID"&&keys[j]=="C_BANK_ID"){
            		bankId=this._entity.fieldValues[i] ;
            		newBankId=values[j];
                }
                if(this._entity.fieldNames[i] == keys[j]){
                    this._entity.fieldValues[i] = values[j];
                }
            }
        }
		
		//update
		if(newBankId!=null && newBankId!=""){
			var ids=new Array(newBankId);
			var orgfloor=document.getElementById("orgfloor").value ;
			var onUpdateBankIdDelegate = Function.createDelegate(this, this._onUpdateBankCompete);
			SuperMap.Committer.commitMapCmd('grgbankingGis','bankvalidate', ["BankId","orgfloor","type"], [ids[0],orgfloor,newType], onUpdateBankIdDelegate);
    	}
	    
      }catch(e){
        alert(e);
      }
    },
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        if(!this.actionStarted){return false;}
        var offsetCoord = e.mapCoord;
	    if(!this.actionStarted){return false;}
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    this._xs.push(this._firstOffsetCoord.x);
	    this._xs.push(offsetCoord.x);
	    this._xs.push(offsetCoord.x);
	    this._xs.push(this._firstOffsetCoord.x);
	    this._ys.push(this._firstOffsetCoord.y);
	    this._ys.push(this._firstOffsetCoord.y);
	    this._ys.push(offsetCoord.y);
	    this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("RectQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
    },
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        var offsetCoord = e.mapCoord;
	    this._mapRect = null;
	    this.actionStarted = true;
	    this._firstMapCoord = e.mapCoord;
	    this._firstOffsetCoord = offsetCoord;
	    this._offsetX = offsetCoord.x - e.mapCoord.x;
	    this._offsetY = offsetCoord.y - e.mapCoord.y;
	    while(this._xs.length > 0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    this.actionStarted = false;
	    var qe = new SuperMap.QueryingEventArgs();
        qe.queryParams = this._queryParam;//point
        qe.clientActionArgs = new SuperMap.ActionEventArgs();
        qe.clientActionArgs.mapCoords = new Array();
        qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
        qe.clientActionArgs.mapCoords[1] = e.mapCoord;
        if(this._onStart){this._onStart(qe);}
	    // 直接去调用查询
	    var left = this._firstMapCoord.x > e.mapCoord.x ? e.mapCoord.x: this._firstMapCoord.x;
	    var right = this._firstMapCoord.x > e.mapCoord.x ?  this._firstMapCoord.x: e.mapCoord.x;
	    var bottom = this._firstMapCoord.y > e.mapCoord.y ? e.mapCoord.y: this._firstMapCoord.y;
	    var top = this._firstMapCoord.y > e.mapCoord.y ?  this._firstMapCoord.y: e.mapCoord.y;
	    var mapRect = new SuperMap.Rect2D(left, bottom, right, top);
	    var onQueryCompleteDelegate = Function.createDelegate(this, this.onQueryComplete);
	    this._mapControl.getMap().queryByRect(mapRect, this._queryParam, false, onQueryCompleteDelegate, this._onError)
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = false;
		this._mapControl.customLayer.removeMarker("PointDeleting");
        this._mapControl.customLayer.removeLine("LineDeleting");
        this._mapControl.customLayer.removePolygon("PolygonDeleting");
        this._entity = null;
		this._selected = false;
		while(this._xs.length > 0){
		    this._xs.pop();
		    this._ys.pop();
	    }
    }
};
SuperMap.UI.UpdatePropertyAction.registerClass('SuperMap.UI.UpdatePropertyAction', SuperMap.UI.Action, Sys.IDisposable);



SuperMap.UI.RectBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制矩形，iServer服务对自绘的矩形制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对矩形做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.RectBufferQueryAction">返回 RectBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "矩形缓冲区查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
    this._type = "RectBufferQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._queryParam  = null;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._bufferParam = bufferAnalystParam;
	this._actionStarted = false;
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
};
SuperMap.UI.RectBufferQueryAction.prototype = {
    initialize:function(mapControl){
	/// <summary>初始化RectBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/RectQuery.cur";
		} else { 
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removePolygon("RectBufferQuery");
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
		var offsetCoord = e.mapCoord;
		while(this._xs.length > 0){
			this._xs.pop();
			this._ys.pop();
		}
		this._xs.push(this._firstOffsetCoord.x);
		this._xs.push(offsetCoord.x);
		this._xs.push(offsetCoord.x);
		this._xs.push(this._firstOffsetCoord.x);
		this._ys.push(this._firstOffsetCoord.y);
		this._ys.push(this._firstOffsetCoord.y);
		this._ys.push(offsetCoord.y);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("RectBufferQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted) {
			this.actionStarted = true;
			this._firstMapCoord = e.mapCoord;
			this._firstOffsetCoord = e.mapCoord;
			while(this._xs.length>0){
				this._xs.pop();
				this._ys.pop();
			}
		}
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = false;
		
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
	    qe.clientActionArgs.mapCoords[1] = e.mapCoord;
        if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this._mapControl.customLayer.removePolygon("RectBufferQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			//服务器查询前事件
			this._queryByRect(qe);
		}
    },
	_queryByRect:function(qe){
		var left;
		var bottom;
		var right;
		var top;
		var startPoint = qe.clientActionArgs.mapCoords[0];
		var endPoint = qe.clientActionArgs.mapCoords[1];
		if(startPoint.x == endPoint.x){
			return;
		} else if(startPoint.x < endPoint.x){
			left = startPoint.x;
			right = endPoint.x;
		} else {
			left = endPoint.x;
			right = startPoint.x;
		}
		if(startPoint.y == endPoint.y){
			return;
		} else if(startPoint.y < endPoint.y){
			bottom = startPoint.y;
			top = endPoint.y;
		} else {
			bottom = endPoint.y;
			top = startPoint.y;
		}
		
		this._mapRect = new SuperMap.Rect2D(left, bottom, right, top);
		this._mapControl.getMap().rectBufferQuery(this._mapRect, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		this._mapControl.customLayer.removePolygon("RectBufferQuery");
	},
	query:function(count, pageIndex){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageIndex" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapRect) return;
		this._queryParam.startRecord = pageIndex * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageIndex + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().rectBufferQuery(this._mapRect, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.RectBufferQueryAction.registerClass('SuperMap.UI.RectBufferQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.PointBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制点,iServer服务对自绘的点制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对点做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PointBufferQueryAction">返回 PointBufferQueryAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "点缓冲查询";
	this.expectCount = 10;
	this._type = "PointBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._queryParam = null;
	this._layerNames = layerNames;
	this._bufferParam = bufferAnalystParam;
	this._sqlParam = sqlParam;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	// 当前鼠标点击位置
	this._mapCoord = null;
};
SuperMap.UI.PointBufferQueryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化PointBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.sqlParam = new SuperMap.SqlParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this._queryParam) {
			this._queryParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
         //客户端鼠标点击事件
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = e.mapCoord;
		this._mapCoord = new SuperMap.Point2D(e.mapCoord.x, e.mapCoord.y);
        if(this._onStart){ this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByPoint(qe);
		}
    },
    _queryByPoint:function(qe){
		this._mapControl.getMap().pointBufferQuery(qe.clientActionArgs.mapCoords[0], this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
    },
	query:function(count, pageNo){
	/// <summary>以分页的方式获取点选查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapCoord)
			return;
		this._queryParam.startRecord = pageNo * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().pointBufferQuery(this._mapCoord, this._bufferParam, this._queryParam, this._queryMode, this._needHighlight, this._onComplete, this._onError, customParam);
		}
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.PointBufferQueryAction.registerClass('SuperMap.UI.PointBufferQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.MultiPointBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制多个点,iServer服务对自绘的点制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对点做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PointBufferQueryAction">返回 PointBufferQueryAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "点缓冲查询";
	this.expectCount = 10;
	this._type = "PointBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._queryParam = null;
	this._layerNames = layerNames;
	this._bufferParam = bufferAnalystParam;
	this._sqlParam = sqlParam;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	// 当前鼠标点击位置
	this._mapCoord = null;
	this._points = [];
	this._markerIDPrefix = "$MultiBuffer";
};
SuperMap.UI.MultiPointBufferQueryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化PointBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.sqlParam = new SuperMap.SqlParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this._queryParam) {
			this._queryParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
         //客户端鼠标点击事件
        this._points.push(e.mapCoord);
		var id = this._markerIDPrefix + this._points.length;
		var icon = new Object();
		icon.src = "../../images/supGis/marker_blue.gif";
		icon.width = 16;
		icon.height = 16;
        this._mapControl.customLayer.insertMarker(id+(new Date()).getTime(), e.mapCoord.x, e.mapCoord.y, 16, 16,null,null,null,icon);	
    },
    _queryByMultiPoint:function(){
    	if(this._points.length > 0){
			this._mapControl.getMap().multiPointBufferQuery(this._points, this._bufferParam, this._queryParam, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		}
    },
	query:function(count, pageNo){
	/// <summary>以分页的方式获取点选查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapCoord)
			return;
		this._queryParam.startRecord = pageNo * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().pointBufferQuery(this._mapCoord, this._bufferParam, this._queryParam, this._queryMode, this._needHighlight, this._onComplete, this._onError, customParam);
		}
		// 自动清除所有的标识点
		for(var i = this._mapControl.customLayer.markers.length - 1;i>=0; i < i--){
            var mk =  this._mapControl.customLayer.markers[i];
			if(mk.id.indexOf(this._markerIDPrefix)!= -1){
				this._mapControl.customLayer.removeMarker(mk.id);
			}
		}
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(this._points.length > 0 && e.button == 2){
			if(!this._layerNames  || this._layerNames.length == 0) {
				//alert("没有可以查询的图层对象。");
				alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
			}else{
				this._queryByMultiPoint();
				while(this._points.length > 0){
					this._points.pop(0);
				}
			}
			for(var i = this._mapControl.customLayer.markers.length - 1;i>=0; i < i--){
	            var mk =  this._mapControl.customLayer.markers[i];
				if(mk.id.indexOf(this._markerIDPrefix)!= -1){
					this._mapControl.customLayer.removeMarker(mk.id);
				}
			}
		}
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.MultiPointBufferQueryAction.registerClass('SuperMap.UI.MultiPointBufferQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.PolygonBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制多边形,iServer服务对自绘的多边形制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对多边形做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PolygonBufferQueryAction">返回 PolygonBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "多边形缓冲查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._type = "PolygonBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
};
SuperMap.UI.PolygonBufferQueryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化PolygonBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PolygonQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.sqlParam = new SuperMap.SqlParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		this._firstMapCoord = null;
		if(this._xs){
			while(this._xs.length > 0){
				this._xs.pop();
			}
			this._xs = null;
		}
		if(this._ys){
			while(this._ys.length > 0){
				this._ys.pop();
			}
			this._ys = null;
		}
		if(this._queryParam) {
			this._queryParam.dispose();
			this._queryParam = null;
		}
		if(this._bufferParam) {
			this._bufferParam.dispose();
			this._bufferParam = null;
		}
		this._layerNames = null;
		this._sqlParam = null;
		if(this._keyPoints){
			while(this._keyPoints.length > 0){
				this._keyPoints.pop();
			}
			this._keyPoints = null;
		}
	    this._onComplete = null;
		this._onStart = null;
		this._onError = null;
	},	
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
  	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			if(this._keyPoints){
				while(this._keyPoints.length>0){
					this._keyPoints.pop();
					this._xs.pop();
					this._ys.pop();
				}
			}
			this._firstMapCoord = e.mapCoord;
            this._firstOffsetCoord = offsetCoord;
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		} else {
			this._xs.pop();
			this._ys.pop();
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
  	    var offsetCoord = e.mapCoord;
		this._keyPoints.push(e.mapCoord);
		this._keyPoints.push(this._firstMapCoord);
		this._xs.pop();
		this._ys.pop();
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._xs.push(this._firstOffsetCoord.x);
		this._ys.push(this._firstOffsetCoord.y);
		this._mapControl.customLayer.insertPolygon("PolygonBufferQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
		
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = this._keyPoints;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this.actionStarted = false;
			this._mapControl.customLayer.removePolygon("PolygonBufferQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByPolygon(qe);
		}
   	},
   	_queryByPolygon:function(qe){
	    this._mapControl.getMap().polygonBufferQuery(qe.clientActionArgs.mapCoords, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		this.actionStarted = false;
		this._mapControl.customLayer.removePolygon("PolygonBufferQuery");
		this._firstMapCoord = null;
	},
	query:function(count, pageIndex){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageIndex" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapRect) return;
		this._queryParam.startRecord = pageIndex * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageIndex + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().polygonBufferQuery(this._keyPoints, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
  	    var offsetCoord = e.mapCoord;
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertPolygon("PolygonBufferQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor, "white", 0.6);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.PolygonBufferQueryAction.registerClass('SuperMap.UI.PolygonBufferQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.LineBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制线段,iServer服务对自绘的线段制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对线段做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.LineBufferQueryAction">返回 LineBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "线缓冲查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._type = "LineBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
    this._firstOffsetCoord = null;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
};
SuperMap.UI.LineBufferQueryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化LineBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/LineQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			} else {
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length > 0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.sqlParam = new SuperMap.SqlParam();
					queryLayer.name = tempLayerNames.pop();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		this._firstMapCoord = null;
		if(this._xs){
			while(this._xs.length > 0){
				this._xs.pop();
			}
			this._xs = null;
		}
		if(this._ys){
			while(this._ys.length > 0){
				this._ys.pop();
			}
			this._ys = null;
		}
		if(this._queryParam) {
			this._queryParam.dispose();
			this._queryParam = null;
		}
		if(this._bufferParam) {
			this._bufferParam.dispose();
			this._bufferParam = null;
		}
		this._layerNames = null;
		this._sqlParam = null;
		if(this._keyPoints){
			while(this._keyPoints.length > 0){
				this._keyPoints.pop();
			}
			this._keyPoints = null;
		}
	    this._onComplete = null;
		this._onStart = null;
		this._onError = null;
	},	
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    var offsetCoord = e.mapCoord;
		if(!this.actionStarted){
			if(this._keyPoints) {
				while(this._keyPoints.length > 0){
				   this._keyPoints.pop();
				   this._xs.pop();
				   this._ys.pop();
			   	}
			}
			this._firstMapCoord = e.mapCoord;
			this._firstOffsetCoord = offsetCoord;
			this._keyPoints.push(e.mapCoord);
			this._xs.push(offsetCoord.x);
			this._ys.push(offsetCoord.y);
		}
		this.actionStarted = true;
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
    },
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	    var offsetCoord = e.mapCoord;
		this._keyPoints.push(e.mapCoord);
		this._xs.pop();
		this._ys.pop();
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
		this._mapControl.customLayer.insertLine("LineBufferQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor);
		
        var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = this._keyPoints;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			this.actionStarted = false;
			this._mapControl.customLayer.removeLine("LineBufferQuery");
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByLine(qe);
		}
   	},
   	_queryByLine:function(qe){
	    this._mapControl.getMap().lineBufferQuery(qe.clientActionArgs.mapCoords, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		/*
		while(this._keyPoints.length > 0){
		   this._keyPoints.pop();
		   this._xs.pop();
		   this._ys.pop();
	   	}
		*/
		this.actionStarted = false;
		this._mapControl.customLayer.removeLine("LineBufferQuery");
	},
	query:function(count, pageIndex){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageIndex" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._keyPoints) return;
		this._queryParam.startRecord = pageIndex * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageIndex + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().lineBufferQuery(this._keyPoints, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
	    var offsetCoord = e.mapCoord;
		this._keyPoints.pop();
		this._xs.pop();
		this._ys.pop();
		this._keyPoints.push(e.mapCoord);
		this._xs.push(offsetCoord.x);
		this._ys.push(offsetCoord.y);
        this._mapControl.customLayer.insertLine("LineBufferQuery", this._xs, this._ys, this.strokeWeight, this.strokeColor);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	}
};
SuperMap.UI.LineBufferQueryAction.registerClass('SuperMap.UI.LineBufferQueryAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.CircleBufferQueryAction = function(layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>该类实现在地图中通过鼠标绘制圆,iServer服务对自绘的圆制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 通过鼠标进行圆选查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标单击圆的圆心，并按住鼠标不放拖动鼠标直至完成圆的绘制，双击鼠标结束圆的绘制，MapControl就会在绘制的圆范围内查询并返回查询的结果。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">对圆做缓冲区的设置参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.CircleBufferQueryAction">返回 CircleBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "圆缓冲查询";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._type = "CircleBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._firstMapCoord = null;
	this._curMapCoord = null;
	this._firstPointX;
	this._firstPointY;
	this._curPointX;
	this._curPointY;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
   	this._offsetX = 0;
   	this._offsetY = 0;
};
SuperMap.UI.CircleBufferQueryAction.prototype={
	initialize:function(mapControl){
	/// <summary>初始化CircleBufferQueryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
		this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/CircleQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._queryParam){
			this._queryParam = new SuperMap.QueryParam();
			if(!this._layerNames){
				this._queryParam.queryLayerParams = null;
			}else{
				this._queryParam.queryLayerParams = new Array();
				var tempLayerNames = this._layerNames.concat();
				while(tempLayerNames.length>0){
					var queryLayer = new SuperMap.QueryLayerParam();
					queryLayer.name = tempLayerNames.pop();
					queryLayer.sqlParam = new SuperMap.SqlParam();
					if(this._sqlParam){
						queryLayer.sqlParam = this._sqlParam.clone();
					} else {
						// queryLayer.sqlParam = new SuperMap.SqlParam();
						// queryLayer.sqlParam.whereClause = "smid > 0";
					}
					this._queryParam.queryLayerParams.push(queryLayer);
				}
				tempLayerNames = null;
			}
			this._queryParam.expectCount = this.expectCount;
		}
		if(this._mapControl.get_container().style.left == "") {
			this._offsetX = 0;
		} else {
    		this._offsetX = parseInt(this._mapControl.get_container().style.left);
		}
		if(this._mapControl.get_container().style.top == "") {
			this._offsetY = 0;
		} else {
    		this._offsetY = parseInt(this._mapControl.get_container().style.top);
		}
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._SMISRemoveCircle();
		this._mapControl = null;
	},
	onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		// 清除上次参数
		this._firstMapCoord = null;
		this._curMapCoord = null;
	},
    onDblClick:function(e){
	/// <summary>在地图控件上鼠标双击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
    onMouseMove:function(e){
	/// <summary>在地图控件上鼠标平移事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		if(!this.actionStarted){return false;}
		var pixelCoord = this._mapControl.mapCoordToPixel(e.mapCoord);
		this._curPointX = pixelCoord.x + this._offsetX;
		this._curPointY = pixelCoord.y + this._offsetY;
		this._curMapCoord = e.mapCoord;
		this._SMISDrawingCircle(this._firstPointX, this._firstPointY, this._curPointX, this._curPointY);
	},
    onMouseDown:function(e){
	/// <summary>在地图控件上鼠标左键按下事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
		this.actionStarted = true;
		this._firstMapCoord = e.mapCoord;
		var pixelCoord = this._mapControl.mapCoordToPixel(e.mapCoord);
		this._firstPointX = pixelCoord.x + this._offsetX;
		this._firstPointY = pixelCoord.y + this._offsetY;
		this._curMapCoord = e.mapCoord;
	},
    onMouseUp:function(e){
	/// <summary>在地图控件上鼠标左键抬起事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
        this._curMapCoord = e.mapCoord;
		this._SMISOnMouseUp(this._mapControl);
	},
    onContextMenu:function(e){
	/// <summary>在地图控件上鼠标右键单击事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
	},
	// 画圆
	_SMISDrawingCircle:function(startPointX, startPointY, curPointX, curPointY){
		var circle = document.getElementById("SMISCircle");
		var m_drawLayer;
		var mouseUpDelegate = Function.createDelegate(this, this._SMISOnMouseUp);
		if(_GetBrowser() != "ie"){
			if(!circle){
				circle = document.createElement("div");
				circle.id = "SMISCircle";
				circle.style.position = "absolute";
				circle.style.left = "0px";
				circle.style.top = "0px";
				circle.unselectable = "on";
				circle.onmouseup = mouseUpDelegate;
				document.body.appendChild(circle);
				var m_jg = new jsGraphics("SMISCircle");
				circle.jg = m_jg;
			} else {
				circle.jg.clear();
			}
			circle.jg.setColor(this.strokeColor);
			circle.jg.setStroke(this.strokeWeight);
			circle.style.zIndex = 2000;
			circle.style.opacity = 0.5;
			var radius = Math.sqrt(Math.pow((curPointX - startPointX), 2) + Math.pow((curPointY - startPointY), 2));
			circle.jg.drawEllipse(startPointX-radius,startPointY-radius, radius*2, radius*2);
			circle.jg.paint();
			return;
		}
		if(!circle){
			SuperMap.Utility.enableVML();
			circle = document.createElement("<v:arc startangle='0' endangle='360'/>");
			circle.style.position = "absolute";
			circle.style.visibility = 'visible';
			circle.id = "SMISCircle";
			circle.style.zIndex = 1000;
			circle.onmouseup = mouseUpDelegate;
			//circle.style.zIndex = map.parentElement.style.zIndex + 200;
			var fill = document.createElement("<v:fill opacity=0.3></v:fill>");
			var stroke = document.createElement("<v:stroke dashstyle='solid' Color='" + this.strokeWeight + "'></v:stroke>");
			stroke.setAttribute("weight", this.strokeWeight);
			document.body.appendChild(circle);
			circle.appendChild(fill);
			circle.appendChild(stroke);
		}
		var radius = Math.sqrt(Math.pow((curPointX - startPointX), 2) + Math.pow((curPointY - startPointY), 2))
		circle.style.left = (startPointX - radius) + "px";
		circle.style.top = (startPointY - radius) + "px";
		circle.style.width = 2*radius + "px";
		circle.style.height =  circle.style.width;
	},
	// 鼠标释放处理事件
	_SMISOnMouseUp:function(){
		this.actionStarted = false;
		this._SMISRemoveCircle();
		var qe = new SuperMap.QueryingEventArgs();
	    qe.queryParams = new SuperMap.QueryParam();
	    qe.queryParams = this._queryParam;//point
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = this._firstMapCoord;
	    qe.clientActionArgs.mapCoords[1] = this._curMapCoord;
	    if(this._onStart){this._onStart(qe);}
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._queryByCircle(qe);
		}
	},
    _queryByCircle:function(qe){
	    var startPoint = qe.clientActionArgs.mapCoords[0];
	    var endPoint = qe.clientActionArgs.mapCoords[1];
	    radius = Math.sqrt(Math.pow((endPoint.x-startPoint.x), 2)+Math.pow((endPoint.y-startPoint.y),2));
		this._mapControl.getMap().circleBufferQuery(startPoint, radius, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
	},
	query:function(count, pageNo) {
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._firstMapCoord || !this._curMapCoord) {
			return;
		}
		var startPoint = this._firstMapCoord;
	    var endPoint = this._curMapCoord;
		var radius = Math.sqrt(Math.pow((endPoint.x-startPoint.x), 2)+Math.pow((endPoint.y-startPoint.y),2));
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().circleBufferQuery(startPoint, radius, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
	_SMISRemoveCircle:function(){
		var circle = document.getElementById("SMISCircle");
		if(circle) { 
			circle.onMouseUp = null;
			document.body.removeChild(circle);
		}
		circle = null;
	}
};
SuperMap.UI.CircleBufferQueryAction.registerClass("SuperMap.UI.CircleBufferQueryAction", SuperMap.UI.Action, Sys.IDisposable);

// EntityBufferQuery
SuperMap.UI.RectEntityBufferQueryAction = function(fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由鼠标绘制的矩形有交集的几何对象。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。</param>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.RectEntityBufferQueryAction">返回 RectEntityBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "地物缓冲查询(框选)";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
    this._type = "RectEntityBufferQueryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._queryParam  = null;
	this._fromLayer = fromLayer;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._queryMode = queryMode;
	this._bufferParam = bufferAnalystParam;
	this.actionStarted = false;
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onError = onError;
	this._onStart = onStart;
};
SuperMap.UI.RectEntityBufferQueryAction.prototype = {
	_queryByRect:function(qe){
		var left;
		var bottom;
		var right;
		var top;
		var startPoint = qe.clientActionArgs.mapCoords[0];
		var endPoint = qe.clientActionArgs.mapCoords[1];
		if(startPoint.x == endPoint.x){
			return;
		} else if(startPoint.x < endPoint.x){
			left = startPoint.x;
			right = endPoint.x;
		} else {
			left = endPoint.x;
			right = startPoint.x;
		}
		if(startPoint.y == endPoint.y){
			return;
		} else if(startPoint.y < endPoint.y){
			bottom = startPoint.y;
			top = endPoint.y;
		} else {
			bottom = endPoint.y;
			top = startPoint.y;
		}
		
		this._mapRect = new SuperMap.Rect2D(left, bottom, right, top);
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().rectEntityBufferQuery(this._mapRect, this._fromLayer, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		}
		this._mapControl.customLayer.removePolygon("RectBufferQuery");
	},
	query:function(count, pageIndex){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageIndex" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapRect) return;
		this._queryParam.startRecord = pageIndex * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageIndex + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().rectEntityBufferQuery(this._mapRect, this._fromLayer, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl.customLayer.removePolygon("RectBufferQuery");
		this._mapControl = null;
	}
};

SuperMap.UI.RectEntityBufferQueryAction.registerClass('SuperMap.UI.RectEntityBufferQueryAction', SuperMap.UI.RectBufferQueryAction, Sys.IDisposable);

SuperMap.UI.CircleEntityBufferQueryAction = function(fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由鼠标绘制的圆有交集的几何对象。&lt;br&gt;
	/// 通过鼠标进行圆选查询的操作过程如下：&lt;br&gt;
	///		在地图窗口通过鼠标单击圆的圆心，并按住鼠标不放拖动鼠标直至完成圆的绘制，双击鼠标结束圆的绘制，MapControl就会在绘制的圆范围内查询并返回查询的结果。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。</param>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.CircleEntityBufferQueryAction">返回 CircleEntityBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "地物缓冲查询(圆选)";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._fromLayer = fromLayer;
	this._type = "CircleEntityBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._firstMapCoord = null;
	this._curMapCoord = null;
	this._firstPointX;
	this._firstPointY;
	this._curPointX;
	this._curPointY;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
};

SuperMap.UI.CircleEntityBufferQueryAction.prototype = {
	_queryByCircle:function(qe){
	    var startPoint = qe.clientActionArgs.mapCoords[0];
	    var endPoint = qe.clientActionArgs.mapCoords[1];
	    var left;
		var bottom;
		var right;
		var top;
		radius = Math.sqrt(Math.pow((endPoint.x - startPoint.x), 2) + Math.pow((endPoint.y-startPoint.y), 2));
		this._mapControl.getMap().circleEntityBufferQuery(startPoint, radius, this._fromLayer, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
	},
	query:function(count, pageNo) {
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._firstMapCoord || !this._curMapCoord) {
			return;
		}
		var startPoint = this._firstMapCoord;
	    var endPoint = this._curMapCoord;
		var radius = Math.sqrt(Math.pow((endPoint.x-startPoint.x), 2)+Math.pow((endPoint.y-startPoint.y),2));
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().circleEntityBufferQuery(startPoint, radius, this._fromLayer, this._bufferParam, this._queryParam, this._queryMode, false, this._onComplete, this._onError, customParam);
		}
	},
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._SMISRemoveCircle();
		this._mapControl = null;
	}
};
SuperMap.UI.CircleEntityBufferQueryAction.registerClass('SuperMap.UI.CircleEntityBufferQueryAction', SuperMap.UI.CircleBufferQueryAction, Sys.IDisposable);

SuperMap.UI.PointEntityBufferQueryAction = function(fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由鼠标绘制的点有交集的几何对象。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。</param>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PointEntityBufferQueryAction">返回 PointEntityBufferQueryAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "地物缓冲查询(点选)";
	this.expectCount = 10;
	this._fromLayer = fromLayer;
	this._type = "PointEntityBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [fromLayer, layerNames, sqlParam, bufferAnalystParam, needHighlight, onComplete, onError,onStart]);
	this._queryParam = null;
	this._layerNames = layerNames;
	this._bufferParam = bufferAnalystParam;
	this._sqlParam = sqlParam;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this._mapCoord = null;
};
SuperMap.UI.PointEntityBufferQueryAction.prototype = {
	_queryByPoint:function(qe){
		this._mapControl.getMap().pointEntityBufferQuery(qe.clientActionArgs.mapCoords[0], this._fromLayer, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
    },
	query:function(count, pageNo){
	/// <summary>以分页的方式获取点选查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._mapCoord)
			return;
		this._queryParam.startRecord = pageNo * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().pointBufferQuery(this._mapCoord, this._fromLayer, this._bufferParam, this._queryParam, this._queryMode, this._needHighlight, this._onComplete, this._onError, customParam);
		}
	},
	
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
	}
};
SuperMap.UI.PointEntityBufferQueryAction.registerClass('SuperMap.UI.PointEntityBufferQueryAction', SuperMap.UI.PointBufferQueryAction, Sys.IDisposable);

SuperMap.UI.PolygonEntityBufferQueryAction = function(fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由鼠标绘制的多边形有交集的几何对象。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。</param>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.PolygonEntityBufferQueryAction">返回 PolygonEntityBufferQueryAction 对象。</returns>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "地物缓冲查询(多边形选)";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._fromLayer = fromLayer;
	this._type = "PolygonEntityBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [fromLayer, layerNames, sqlParam, bufferAnalystParam, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
};
SuperMap.UI.PolygonEntityBufferQueryAction.prototype = {
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		this._firstMapCoord = null;
		if(this._xs){
			while(this._xs.length > 0){
				this._xs.pop();
			}
			this._xs = null;
		}
		if(this._ys){
			while(this._ys.length > 0){
				this._ys.pop();
			}
			this._ys = null;
		}
		if(this._queryParam){
			this._queryParam.dispose();
			this._queryParam = null;
		}
		if(this._bufferParam){
			this._bufferParam.dispose();
			this._bufferParam = null;
		}
		this._layerNames = null;
		this._sqlParam = null;
		if(this._keyPoints){
			while(this._keyPoints.length > 0){
				this._keyPoints.pop();
			}
			this._keyPoints = null;
		}
	    this._onComplete = null;
		this._onStart = null;
		this._onError = null;
	},
	_queryByPolygon:function(qe){
	    this._mapControl.getMap().polygonEntityBufferQuery(qe.clientActionArgs.mapCoords, this._fromLayer, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		this._firstMapCoord = null;
		this.actionStarted = false;
		this._mapControl.customLayer.removePolygon("PolygonBufferQuery");
	},
	query:function(count, pageNo){
	/// <summary>以分页的方式获取查询的结果。&lt;br&gt;
	/// 该方法根据每页显示的结果记录数以及要获取的分页页码来获取该页的查询结果集。
	/// 例如：以每页显示10条查询结果的规则计算，获取第2页的查询记录集，输入query（10，1）即可。
	/// </summary>
	/// <param name="count" type="Number">每页显示的最多记录数。</param>
	/// <param name="pageNo" type="Number">要显示的页码，从0开始计数。</param>	
		if(!this._keyPoints)
			return;
		this._queryParam.startRecord = pageNo * count;
		this._queryParam.expectCount = count;
		var customParam = "[pageNo]" + pageNo + "[/pageNo]";
		if(!this._layerNames  || this._layerNames.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this._mapControl.getMap().polygonEntityBufferQuery(this._keyPoints, this._fromLayer, this._queryParam, false, this._onComplete, this._onError, customParam);
		}
	}
};
SuperMap.UI.PolygonEntityBufferQueryAction.registerClass('SuperMap.UI.PolygonEntityBufferQueryAction', SuperMap.UI.PolygonBufferQueryAction, Sys.IDisposable);

SuperMap.UI.LineEntityBufferQueryAction = function(fromLayer, layerNames, sqlParam, bufferAnalystParam, queryMode, needHighlight, onComplete, onError, onStart){
	/// <summary>对iServer服务的地图图层中指定的几何对象制作缓冲区，并查询与该缓冲区符合某空间关系的某(几)个图层的空间地物信息。&lt;br&gt;
	/// 制作缓冲区的几何对象是在fromLayer图层中查询出来的、与由鼠标绘制的线段有交集的几何对象。&lt;br&gt;
	/// 对于返回的结果，有如下说明：&lt;br&gt;
	/// 1. 默认情况下，地图服务每次会返回最多10条符合条件的记录。
	/// 用户也可以通过expectCount参数来设置每次返回的最多记录数。&lt;br&gt;
	/// 2. 返回的结果会在onComplete的处理函数中进行处理。&lt;br&gt;
	/// 3. 当查找的结果数目大于expectCount时，用户可以通过分页的方式获取所有结果，此时
	/// 通过query()方法获取指定分页的结果。
	/// </summary>
	/// <param name="fromLayer" type="String">缓冲对象所在的iServer服务的地图图层的名称。</param>
	/// <param name="layerNames" type="Array" elementType="String">要查询的图层名称。字符串数组。</param>
	/// <param name="sqlParam" type="SuperMap.SqlParam">设置查询的过滤条件。SuperMap.SqlParam 类型。</param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">制作缓冲区需要的参数。
	/// </param>
	/// <param name="queryMode" type="Number">空间位置关系的查询模式。该参数主要用于设置缓冲区与被查找对象的位置关系，如查找与缓冲区相交的空间地物，那么就
	/// 设置该值为5。&lt;br&gt;
	/// 具体空间位置关系与其对应的数值对应表可以参考SpatialQueryMode的描述。
	/// </param>
	/// <param name="needHighlight" type="Boolean">是否高亮显示被查询出的对象。True代表高亮显示。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onStart" type="Function">在向服务器提交查询请求之前触发的事件处理函数。用户可以在该函数中设置其他过滤条件等。&lt;br&gt; function onStart(&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt; queryingEventArgs) {};
	/// </param>
	/// <returns type="SuperMap.UI.LineEntityBufferQueryAction">返回 LineEntityBufferQueryAction 对象。</returns>
	/// <field name="expectCount" type ="Number">数值类型，其值默认为10。</field>
	/// <field name="actionStarted" type="Boolean">布尔型，表示该操作是否开始。</field>
	/// <field name="strokeWeight" type="Number">线的宽度。（显示宽度），以像素（px）为单位。</field>
	/// <field name="strokeColor" type="String">线的颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。</field>
	/// <field name="type" type="String">交互操作的类型。</field>
	this.title = "地物缓冲查询(线选)";
    this.strokeColor = "blue";
	this.strokeWeight = 2;
	this.expectCount = 10;
	this._fromLayer = fromLayer;
	this._type = "LineEntityBufferQueryAction";
	this.type = this._type;
	this._json = SuperMap.Utility.actionToJSON(this._type, [fromLayer, layerNames, sqlParam, bufferAnalystParam, needHighlight, onComplete, onError, onStart]);
	this._bufferParam = bufferAnalystParam;
	this._layerNames = layerNames;
	this._sqlParam = sqlParam;
	this._keyPoints = new Array();
    this._xs = new Array();
    this._ys = new Array();
    this._firstMapCoord = null;
	this._queryMode = queryMode;
	this._needHighlight = needHighlight;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;
	this.actionStarted = false;
};
SuperMap.UI.LineEntityBufferQueryAction.prototype = {
	dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		this._firstMapCoord = null;
		if(this._xs){
			while(this._xs.length > 0){
				this._xs.pop();
			}
			this._xs = null;
		}
		if(this._ys){
			while(this._ys.length > 0){
				this._ys.pop();
			}
			this._ys = null;
		}
		if(this._queryParam){
			this._queryParam.dispose();
			this._queryParam = null;
		}
		if(this._bufferParam){
			this._bufferParam.dispose();
			this._bufferParam = null;
		}
		this._layerNames = null;
		this._sqlParam = null;
		if(this._keyPoints){
			while(this._keyPoints.length > 0){
				this._keyPoints.pop();
			}
			this._keyPoints = null;
		}
	    this._onComplete = null;
		this._onStart = null;
		this._onError = null;
	},
	_queryByLine:function(qe){
	    this._mapControl.getMap().lineEntityBufferQuery(qe.clientActionArgs.mapCoords, this._fromLayer, this._bufferParam, qe.queryParams, this._queryMode, this._needHighlight, this._onComplete, this._onError);
		while(this._keyPoints.length > 0) {
		   this._keyPoints.pop();
		   this._xs.pop();
		   this._ys.pop();
	   	}
		this._firstMapCoord = null;
		this.actionStarted = false;
		this._mapControl.customLayer.removeLine("LineBufferQuery");
	}
};
SuperMap.UI.LineEntityBufferQueryAction.registerClass('SuperMap.UI.LineEntityBufferQueryAction', SuperMap.UI.LineBufferQueryAction, Sys.IDisposable);

SuperMap.UI.SpatialOperateAction = function(sourceLayer, operateLayer, tolerance, needHighlight, onComplete, onError, onStart){
	this.title = "空间操作";
	this.actionStarted = false;
    this._type = "SpatialOperateAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [sourceLayer, operateLayer, tolerance, needHighlight, onComplete, onError, onStart]);
    this.type = this._type;

    this.sourceLayer = sourceLayer;
    this.operateLayer = operateLayer;
	this.tolerance = tolerance;
	this.needHighlight = needHighlight;
    this.sourceQueryParam = null;
    this.operateQueryParam = null;
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;

	this.selectSourceGeometry = true;
	this.sourceGeometry = null;
	this.operateGeometry = null;
	this.spatialOperationType = 1;

	this.strokeWeight = 2;
	this.strokeColor = "red";
	this._xs = new Array();
	this._ys = new Array();
    this._offsetX = 0;
    this._offsetY = 0;
    this._keyPoint = null;
    this._lastSourceGeometryParts = 0;
    this._lastOperateGeometryParts = 0;
};
SuperMap.UI.SpatialOperateAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化SpatialOperateAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._tolerance){
			this._tolerance = 100;
		}
		
		if(!this.sourceQueryParam){
			this.sourceQueryParam = new SuperMap.QueryParam();
			this.sourceQueryParam.queryLayerParams = new Array();
			var queryLayer1 = new SuperMap.QueryLayerParam();
			queryLayer1.name = this.sourceLayer;
			// queryLayer1.sqlParam = new SuperMap.SqlParam();
			// queryLayer1.sqlParam.whereClause = "smid > 0";
			this.sourceQueryParam.queryLayerParams.push(queryLayer1);
			this.sourceQueryParam.expectCount = 1;
		}
		if(!this.operateQueryParam){
			this.operateQueryParam = new SuperMap.QueryParam();
			this.operateQueryParam.queryLayerParams = new Array();
			var queryLayer2 = new SuperMap.QueryLayerParam();
			queryLayer2.name = this.operateLayer;
			// queryLayer2.sqlParam = new SuperMap.SqlParam();
			// queryLayer2.sqlParam.whereClause = "smid > 0";
			this.operateQueryParam.queryLayerParams.push(queryLayer2);
			this.operateQueryParam.expectCount = 1;
		}
		this.sourceQueryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
		this.operateQueryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this.sourceQueryParam) {
			this.sourceQueryParam.dispose();
		}
		if(this.operateQueryLayer) {
			this.operateQueryParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    //客户端鼠标点击事件
		this._keyPoint = null;
        var qe = new SuperMap.QueryingEventArgs();
        if(this.selectSourceGeometry){
    	    qe.queryParams = this.sourceQueryParam;
        }else{
    	    qe.queryParams = this.operateQueryParam;
        }
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = e.mapCoord;
        if(this._onStart){ this._onStart(qe); }
		if(!qe.queryParams.queryLayerParams  || qe.queryParams.queryLayerParams.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this.queryByPoint(qe);
		}
    },
    queryByPoint:function(qe){
	/// <summary>点选查询。指定地图的某一点的位置，查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。
	/// </summary>
		this._keyPoint = qe.clientActionArgs.mapCoords[0];
		var onQueryCompleteDelegate = Function.createDelegate(this, this.onQueryComplete);
		qe.queryParams.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry
   		this._mapControl.getMap().queryByPoint(this._keyPoint, this.tolerance, qe.queryParams, this._needHighlight, onQueryCompleteDelegate, this._onError);
    },
    onQueryComplete:function(resultSet){
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    var queryLayer = this.sourceLayer;
	    if(this.selectSourceGeometry){
	        for(var i = 0; i < this._lastSourceGeometryParts; i++){
        	    this._mapControl.customLayer.removePolygon("SpatialOperateSourceQuery_" + i);
	        }
	    }else{
	        queryLayer = this.operateLayer;
	        for(var i = 0; i < this._lastOperateGeometryParts; i++){
        	    this._mapControl.customLayer.removePolygon("SpatialOperateOperateQuery_" + i);
	        }
	    }
        if(resultSet != null && resultSet.totalCount > 0){
            //高亮出来
			var onGetEntityCompleteDelegate = Function.createDelegate(this, this.onGetEntityComplete);
            //没有点串信息,只好再GetEntity
			// 一次请求返回对象信息
			this.onGetEntityComplete(resultSet.recordSets[0].records[0]);
            // this._mapControl.getMap().getEntity(queryLayer,eval(resultSet.recordSets[0].records[0].fieldValues[0]), false, onGetEntityCompleteDelegate, null, null);
        }else{
            //alert("没有选中地物");
			alert(SuperMap.Resource.ins.getMessage("noPickOnEntity"));
	        for(var i = 0; i < this._lastSourceGeometryParts; i++){
        	    this._mapControl.customLayer.removePolygon("SpatialOperateSourceQuery_" + i);
	        }
	        for(var i = 0; i < this._lastOperateGeometryParts; i++){
        	    this._mapControl.customLayer.removePolygon("SpatialOperateOperateQuery_" + i);
	        }
        }
    },
    
    onGetEntityComplete:function(entity){
        if(entity != null && entity.shape != null){
            var pointsX = new Array();
            var pointsY = new Array();
            
            var index = 0;

            if(this.selectSourceGeometry){
                this._lastSourceGeometryParts = entity.shape.parts.length;
            }else{
                this._lastOperateGeometryParts = entity.shape.parts.length;
            }
            
            for(var i = 0; i < entity.shape.parts.length; i++){
                var tempPartsX = new Array();
                var tempPartsY = new Array();
                if(i != 0){
                    index += entity.shape.parts[i - 1];
                }
                for(var j = index; j < index + entity.shape.parts[i]; j++){
                    tempPartsX.push(entity.shape.point2Ds[j].x + this._offsetX);
                    tempPartsY.push(entity.shape.point2Ds[j].y + this._offsetY);
                }
                pointsX.push(tempPartsX);
                pointsY.push(tempPartsY);
            }
            
            /*for(var i = 0; i < entity.shape.point2Ds.length; i++){
                pointsX.push(entity.shape.point2Ds[i].x + this._offsetX);
                pointsY.push(entity.shape.point2Ds[i].y + this._offsetY);
            }*/
            if(this.selectSourceGeometry){
                for(var i = 0; i < entity.shape.parts.length; i++){
                    this._mapControl.customLayer.insertPolygon("SpatialOperateSourceQuery_" + i, pointsX[i], pointsY[i], this.strokeWeight, this.strokeColor, "white", 0.6);
                }
                this.sourceGeometry = new SuperMap.Geometry()
                this.sourceGeometry.copy(entity.shape);
            }else{
                for(var i = 0; i < entity.shape.parts.length; i++){
                    this._mapControl.customLayer.insertPolygon("SpatialOperateOperateQuery_" + i, pointsX[i], pointsY[i], this.strokeWeight, this.strokeColor, "white", 0.6);
                }
                this.operateGeometry = new SuperMap.Geometry()
                this.operateGeometry.copy(entity.shape);
            }
        }
    },
    spatialOperate:function(){
        if(this.sourceGeometry && this.operateGeometry){
            this._mapControl.getMap().spatialOperate(this.sourceGeometry, this.operateGeometry, this.spatialOperationType, this.needHighlight,this._onComplete, this._onError);
        }
    }
};
SuperMap.UI.SpatialOperateAction.registerClass('SuperMap.UI.SpatialOperateAction', SuperMap.UI.Action, Sys.IDisposable);

SuperMap.UI.OverlayWithGeometryAction = function(overlayParam, operateLayerName, tolerance, showOverlayResult, onComplete, onError, onStart){
	this.title = "叠加分析";
	this.actionStarted = false;
    this._type = "OverlayWithGeometryAction";
    this.type = this._type;
    this._json = SuperMap.Utility.actionToJSON(this._type, [overlayParam, tolerance, showOverlayResult, onComplete, onError, onStart]);
    this.type = this._type;

    this.overlayParam = null;
	this.tolerance = tolerance;
    this.showOverlayResult = showOverlayResult;
    this.queryParam = null;
    this.operateLayerName = operateLayerName;
    
	this._onComplete = onComplete;
	this._onStart = onStart;
	this._onError = onError;

	this.strokeWeight = 2;
	this.strokeColor = "blue";
	this._xs = new Array();
	this._ys = new Array();
    this._offsetX = 0;
    this._offsetY = 0;
    this._keyPoint = null;
};
SuperMap.UI.OverlayWithGeometryAction.prototype = {
	initialize:function(mapControl){
	/// <summary>初始化 OverlayWithGeometryAction。
	/// </summary>
	/// <param name="mapControl" type="SuperMap.UI.MapControl">mapControl对象。</param>
 	  	this._mapControl = mapControl;
		if(_GetBrowser() == "ie"){
			this._mapControl.get_container().style.cursor = "../../images/supGis/cursors/PointQuery.cur";
		} else {
			this._mapControl.get_container().style.cursor = "crosshair";
		}
		if(!this._tolerance){
			this._tolerance = 100;
		}
		
		if(!this.overlayParam){
			this.overlayParam = new SuperMap.OverlayParam();
		}
		if(!this.queryParam){
			this.queryParam = new SuperMap.QueryParam();
			this.queryParam.queryLayerParams = new Array();
			var queryLayer = new SuperMap.QueryLayerParam();
			queryLayer.name = this.operateLayerName;
			// queryLayer.sqlParam = new SuperMap.SqlParam();
			// queryLayer.sqlParam.whereClause = "smid > 0";
			this.queryParam.queryLayerParams.push(queryLayer);
			this.queryParam.expectCount = 1;
		}
		this.queryParam.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttributeAndGeometry;
    },
    dispose:function(){
	/// <summary>释放对象占用的资源。
	/// </summary>
		this._mapControl = null;
		if(this.overlayParam) {
			this.overlayParam.dispose();
		}
	},
    onClick:function(e){
	/// <summary>在地图控件上单击鼠标左键事件。
	/// </summary>
	/// <param name="e" type="Object">事件参数对象。</param>
    //客户端鼠标点击事件
		this._keyPoint = null;
        var qe = new SuperMap.QueryingEventArgs();
   	    qe.queryParams = this.queryParam;
	    qe.clientActionArgs = new SuperMap.ActionEventArgs();
	    qe.clientActionArgs.mapCoords = new Array();
	    qe.clientActionArgs.mapCoords[0] = e.mapCoord;
        if(this._onStart){ this._onStart(qe); }
		if(!qe.queryParams.queryLayerParams  || qe.queryParams.queryLayerParams.length == 0) {
			//alert("没有可以查询的图层对象。");
			alert(SuperMap.Resource.ins.getMessage("noQueryLayer"));
		} else {
			this.queryByPoint(qe);
		}
    },
    queryByPoint:function(qe){
	/// <summary>点选查询。指定地图的某一点的位置，查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。
	/// </summary>
		this._keyPoint = qe.clientActionArgs.mapCoords[0];
		var onQueryCompleteDelegate = Function.createDelegate(this, this.onQueryComplete);
   		this._mapControl.getMap().queryByPoint(this._keyPoint, this.tolerance, qe.queryParams, false, onQueryCompleteDelegate, this._onError);
    },
    onQueryComplete:function(resultSet){
	    while(this._xs.length>0){
		    this._xs.pop();
		    this._ys.pop();
	    }
	    var queryLayer = this.operateLayerName;
   	    if(this._lastSourceGeometryParts){
	        for(var i = 0; i < this._lastSourceGeometryParts; i++){
        	    this._mapControl.customLayer.removePolygon("GeometryForOverlay_" + i);
	        }
	    }
        if(resultSet != null && resultSet.totalCount > 0){
            //高亮出来
			var onGetEntityCompleteDelegate = Function.createDelegate(this, this.onGetEntityComplete);
            //没有点串信息,只好再GetEntity
			this.onGetEntityComplete(resultSet.recordSets[0].records[0]);
           //  this._mapControl.getMap().getEntity(queryLayer,eval(resultSet.recordSets[0].records[0].fieldValues[0]), false, onGetEntityCompleteDelegate, null, null);
        }else{
            //alert("没有选中地物");
			alert(SuperMap.Resource.ins.getMessage("noPickOnEntity"));
            this._mapControl.customLayer.removePolygon("GeometryForOverlay");
        }
    },

	onGetEntityComplete:function(entity){
        if(entity != null && entity.shape != null){
            var pointsX = new Array();
            var pointsY = new Array();
            var index = 0;
            this._lastSourceGeometryParts = entity.shape.parts.length;
            
            for(var i = 0; i < entity.shape.parts.length; i++){
                var tempPartsX = new Array();
                var tempPartsY = new Array();
                if(i != 0){
                    index += entity.shape.parts[i - 1];
                }
                for(var j = index; j < index + entity.shape.parts[i]; j++){
                    tempPartsX.push(entity.shape.point2Ds[j].x + this._offsetX);
                    tempPartsY.push(entity.shape.point2Ds[j].y + this._offsetY);
                }
                pointsX.push(tempPartsX);
                pointsY.push(tempPartsY);
            }
            
			for(var i = 0; i < entity.shape.parts.length; i++){
				this._mapControl.customLayer.insertPolygon("GeometryForOverlay_" + i, pointsX[i], pointsY[i], this.strokeWeight, this.strokeColor, "white", 0.6);
			}
			
			this.overlayParam.operateRegion = new SuperMap.Geometry();
			this.overlayParam.operateRegion.copy(entity.shape);
        }
    },
    overlayAnalyst:function(){
        if(this.overlayParam){
            this._mapControl.getMap().overlayAnalyst(this.overlayParam, this.showOverlayResult, this._onComplete, this._onError);
        }
    }
};
SuperMap.UI.OverlayWithGeometryAction.registerClass('SuperMap.UI.OverlayWithGeometryAction', SuperMap.UI.Action, Sys.IDisposable);