//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：		SuperMap.CommonTypes.js  
// 功能：			Ajax 基础类型 
// 最后修改时间：	2007-10-17
//========================================================================== 

Type.registerNamespace("SuperMap");

SuperMap.Point = function(x, y){
	/// <summary>像素坐标的点对象。</summary>
	/// <param name="x"   type="Number"  mayBeNull="true" optional="true" integer="true">点对象的 x 坐标。</param>
	/// <param name="y"  type="Number"  mayBeNull="true" optional="true" integer="true">点对象的 y 坐标。</param>
	/// <returns type="SuperMap.Point">返回 Point 对象。</returns>
    if(x == 0 || x){
        this.x = parseInt(x);
    }else{
        this.x = NaN;// 初始化错误不应该给0值，0有意义
    }
    if(y == 0 || y){
        this.y = parseInt(y);
    }else{
        this.y = NaN;
    }
};
SuperMap.Point.prototype = {
    toString:function(){
	/// <summary>返回一个表示此点对象坐标的格式化字符串，如点(2,3)，则返回的格式化的字符串为“(2,3)”。</summary>
	/// <returns type="String">表示此 Point 的字符串。</returns>
        return "(" + this.x + ", " + this.y + ")";
    },
    
    copy:function(point){
	/// <summary>复制点对象。</summary>
	/// <param name="point" type="SuperMap.Point">原 Point 对象。</param>
	/// <returns type="SuperMap.Point">&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;点对象。</returns>
        if(!point){
            return;
        } 
        this.x = point.x;
        this.y = point.y;
    },
    
    compare:function(object){
	/// <summary>判断指定的对象是否等于当前的点对象。</summary>
	/// <param name="object">要与当前点对象对比的对象。</param>
	/// <returns type="Object">返回 object 对象，用于表达两个比较对象之间有差异的值。</returns>
        if(!object){
            return null;
        }
        var newObject = null;
        if(this.x != object.x){
            if(!newObject){
                newObject = new Object();
            }
            newObject.x = object.x;
        }
        if(this.y != object.y){
            if(!newObject){
                newObject = new Object();
            }
            newObject.y = object.y;
        }
        return newObject;
	}
};
SuperMap.Point.fromJson = function(jsonObject){
	/// <summary>将 JSON 对象转换为点对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.Point">&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;点对象。</returns>
    if(!jsonObject){
        return null;
    } 
    var object = jsonObject; 
    if(typeof(jsonObject) === "string"){
        object = eval('(' + jsonObject + ')');
    }
    var point = new SuperMap.Point(object.x, object.y);
    return point;
};
SuperMap.Point.registerClass('SuperMap.Point');

SuperMap.Point2D = function(x, y){
	/// <summary>投影坐标系上的的点对象。</summary>
	/// <param name="x" type="Number" mayBeNull="true" optional="true">点对象的 X 坐标。双精度类型。</param>
	/// <param name="y" type="Number"  mayBeNull="true" optional="true">点对象的 Y 坐标。双精度类型。</param>
	/// <returns type="SuperMap.Point2D">返回 Point2D 对象。</returns>
    if(x == 0 || x){
        this.x = x;
    } else {
        this.x = NaN;
    }
    if(y == 0 || y){
        this.y = y;
    } else {
        this.y = NaN;
    }
};
SuperMap.Point2D.prototype = {
    toString: function(){
	/// <summary>返回一个表示此点对象坐标的格式化字符串，如点(2.0,3.0)，则返回的格式化的字符串为“{X=2.0,Y=3.0}”。</summary>
	/// <returns type="String">表示此 Point2D的字符串，格式为（x,y）。</returns>
        return "(" + this.x + ", " + this.y + ")";
    },

    copy: function(object){
	/// <summary>复制点对象。</summary>
	/// <param name="object" type="SuperMap.Point2D">原 Point2D 对象。</param>
	/// <returns type="SuperMap.Point2D">&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;点对象。</returns>
        if(!object){ 
            return;
        }
        this.x = object.x;
        this.y = object.y;
    },

    compare: function(object){
	/// <summary>判断指定的对象是否等于当前的点对象。</summary>
	/// <param name="object">要与当前点对象对比的对象。</param>
	/// <returns type="Object">返回 object 对象，用于表达两个比较对象之间有差异的值。</returns>
        if(!object){
            return null;
        }
        var newObject = null;
        if(this.x != object.x){
            if(!newObject){
                newObject = new Object();
            }
            newObject.x = object.x;
        }
        if(this.y != object.y){
            if(!newObject){
                newObject = new Object();
            }
            newObject.y = object.y;
        }
        return newObject;
    }
};
SuperMap.Point2D.fromJson = function(jsonObject){
	/// <summary>将 JSON 对象转换为点对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.Point2D">&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;点对象。</returns>
    if(!jsonObject){
        return null;
    }
    if(typeof(jsonObject) === "string"){
        jsonObject = eval('(' + jsonObject + ')');
    }
    var point2D = new SuperMap.Point2D(jsonObject.x, jsonObject.y);
    return point2D;
};
SuperMap.Point2D.registerClass('SuperMap.Point2D');

SuperMap.Rect = function(left, top, right, bottom){
	/// <summary>矩形类，用来表示像素坐标的矩形对象，其中左边界坐标值小于等于右边界坐标值, 下边界坐标值小于等于上边界坐标值。&lt;br&gt;
	/// 该类的对象通常用于确定范围，可用来表示几何对象的最小外接矩形、地图窗口的可视范围，数据集的范围等，
	/// 另外在进行矩形选择，矩形查询等时也会用到此类的对象。 
	/// </summary>
	/// <param name="left"  type="Number"  mayBeNull="true" optional="true" integer="true">矩形对象左边界的X坐标。</param>
	/// <param name="top"  type="Number"  mayBeNull="true" optional="true"  integer="true">矩形对象上边界的Y坐标。</param>
	/// <param name="right"  type="Number"  mayBeNull="true" optional="true"  integer="true">矩形对象右边界的X坐标。</param>
	/// <param name="bottom"  type="Number"  mayBeNull="true" optional="true" integer="true">矩形对象下边界的Y坐标。</param>
	/// <returns type="SuperMap.Rect">返回 Rect 对象。</returns>
	/// <field name="leftTop" type="SuperMap.Point">矩形对象左上角的点坐标。</field>
	/// <field name="rightBottom" type="SuperMap.Point">矩形对象右下角的点坐标。</field>
	this.leftTop = new SuperMap.Point(left,top);
    this.rightBottom = new SuperMap.Point(right,bottom);
};
SuperMap.Rect.prototype = {
	toString:function(){
	/// <summary>返回一个表示此矩形对象的坐标的格式化字符串，格式为(lefttop，rightbottom)。</summary>
	/// <returns type="String">表示此 Rect的字符串。</returns>
        return "(" + (this.leftTop ? this.leftTop.toString() : "null") + ", " + 
            (this.rightBottom ? this.rightBottom.toString() : "null") + ")";
    },
	
    copy:function(rect){
	/// <summary>复制矩形对象。</summary>
	/// <param name="rect" type="SuperMap.Rect">原矩形对象。</param>
	/// <returns type="SuperMap.Rect">&lt;see cref="T:SuperMap.Rect"&gt;Rect&lt;/see&gt;矩形对象。</returns>
        if(!rect){
            return;
        }
        if(!this.leftTop){
            this.leftTop = new SuperMap.Point();
        }
        if(!this.rightBottom){
            this.rightBottom = new SuperMap.Point();
        }
        this.leftTop.copy(rect.leftTop);
        this.rightBottom.copy(rect.rightBottom);
    },

	center:function(){
	/// <summary>此矩形对象的中心点。其 x 坐标为(左边界坐标值+右边界坐标值)/2，y 坐标值为(上边界坐标值+下边界坐标值)/2。</summary>
	/// <returns type="SuperMap.Point">表示矩形对象的中心点的&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;对象。</returns>
        return new SuperMap.Point(0.5 * (this.leftTop.x + this.rightBottom.x), 
                0.5*(this.leftTop.y + this.rightBottom.y));
    },
    
	width:function(){
	/// <summary>矩形对象的宽度。其值为右边界坐标值与左边界坐标值之差。 </summary>
	/// <returns type="Number" integer="true">矩形对象的宽度。</returns>
        return SuperMap.Utility.abs(this.rightBottom.x - this.leftTop.x);
    },
    
	height:function(){
	/// <summary>此矩形对象的高度。其值为上边界坐标值与下边界坐标值之差。  </summary>
	/// <returns type="Number" integer="true">矩形对象的高度。</returns>
        return SuperMap.Utility.abs(this.rightBottom.y - this.leftTop.y);
    },
	
    compare:function(object){
	/// <summary>判定此矩形对象是否与指定的对象相等，只有当指定的对象为矩形对象，
	/// 且与此矩形对象有相同的边界坐标值时才认为其相等。   </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回 object 对象，用于表达两个比较对象之间有差异的值。</returns>
	    if(!object){
            return;
        }
	    var newObject = null;
        if(object.leftTop){
            if(!this.leftTop){
                this.leftTop = new SuperMap.Point()
            }
            var oLeftTop = this.leftTop.compare(object.leftTop);
            if(oLeftTop){
                if(!newObject)
                    newObject = new Object();
                newObject.leftTop = oLeftTop;
            }
        }
        if(object.rightTop){
            if(!this.rightTop){
                this.rightTop = new SuperMap.Point()
            }
            var oRightTop = this.rightTop.compare(object.rightTop);
            if(oRightTop){
                if(!newObject)
                    newObject = new Object();
                newObject.rightTop = oRightTop;
            }
        }
        return newObject;
	}
};
SuperMap.Rect.fromJson=function(jsonObject){
	/// <summary>将JSON对象转换为矩形对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.Rect">&lt;see cref="T:SuperMap.Rect"&gt;Rect&lt;/see&gt;矩形对象。</returns>
	if(!jsonObject){
        return null;
    } 
    var object = jsonObject; 
    if(typeof(jsonObject) === "string"){
        object = eval('(' + jsonObject + ')');}
    var rect = new SuperMap.Rect(object.leftTop.x, object.leftTop.y, object.rightBottom.x, object.rightBottom.y);
    return rect;
};

SuperMap.Rect.registerClass('SuperMap.Rect');

SuperMap.Rect2D = function(left,bottom,right,top){
	/// <summary>矩形类，用来表示投影坐标的矩形对象，其中左边界坐标值小于等于右边界坐标值, 下边界坐标值小于等于上边界坐标值。&lt;br&gt;
	/// 该类的对象通常用于确定范围，可用来表示几何对象的最小外接矩形、地图窗口的可视范围，数据集的范围等，
	/// 另外在进行矩形选择，矩形查询等时也会用到此类的对象。 
	/// </summary>
	/// <param name="left"  type="Number"  mayBeNull="true" optional="true">矩形对象左边界的X坐标。</param>
	/// <param name="bottom"  type="Number"  mayBeNull="true" optional="true">矩形对象下边界的Y坐标。</param>
	/// <param name="right"  type="Number"  mayBeNull="true" optional="true">矩形对象右边界的X坐标。</param>
	/// <param name="top"  type="Number"  mayBeNull="true" optional="true" >矩形对象上边界的Y坐标。</param>	
	/// <returns type="SuperMap.Rect2D">返回 Rect2D 对象。</returns>
	/// <field name="leftBottom" type="SuperMap.Point2D">矩形对象左下角的点坐标。</field>
	/// <field name="rightTop" type="SuperMap.Point2D">矩形对象右上角的点坐标。</field>
	this.leftBottom = new SuperMap.Point2D(left,bottom);
	this.rightTop = new SuperMap.Point2D(right,top);
};
SuperMap.Rect2D.prototype={
	toString:function(){
	/// <summary>返回一个表示此矩形对象的坐标的格式化字符串，格式为(leftBottom,rightTop)。</summary>
	/// <returns type="String">表示此 Rect2D 的字符串。</returns>
        return "(" + (this.leftBottom ? this.leftBottom.toString() : "null") +
                ", " + (this.rightTop ? this.rightTop.toString() : "null") + ")";
    },
    
	copy:function(rect2D){
	/// <summary>复制矩形对象。</summary>
	/// <param name="rect" type="SuperMap.Rect2D">原矩形对象。</param>
	/// <returns type="SuperMap.Rect2D">&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;矩形对象。</returns>
        if(!rect2D){
            return;
        }
        if(!this.leftBottom){
            this.leftBottom = new SuperMap.Point2D();
        }
        if(!this.rightTop){
            this.rightTop = new SuperMap.Point2D();
        }
        this.leftBottom.copy(rect2D.leftBottom);
        this.rightTop.copy(rect2D.rightTop);
    },

	center:function(){
	/// <summary>此矩形对象的中心点。其 x 坐标为(左边界坐标值+右边界坐标值)/2，y 坐标值为(上边界坐标值+下边界坐标值)/2。</summary>
	/// <returns type="SuperMap.Point2D">表示矩形对象的中心点的&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;对象。</returns>	
        return new SuperMap.Point2D(0.5 * (this.leftBottom.x + this.rightTop.x), 
                0.5*(this.leftBottom.y + this.rightTop.y));
    },
    
	contains:function(point2D){
	/// <summary>判断指定的点是否包含在此矩形内。 当点在矩形的内部和边界上时，均认为其包含在该矩形内。 </summary>
	/// <param name="point2D" type="SuperMap.Point2D">要测试的 point2D 点。</param>
	/// <returns type="Boolean">如果指定的点在此矩形内部和边界上时，均返回 true；否则将返回 false。</returns>
        return point2D.x >= this.leftBottom.x && point2D.y >= this.leftBottom.y && 
                    point2D.x <= this.rightTop.x && point2D.y <= this.rightTop.y;
    },
	
    width:function(){
	/// <summary>矩形对象的宽度。其值为右边界坐标值与左边界坐标值之差。 </summary>
	/// <returns type="Number">矩形对象的宽度。</returns>
        return this.rightTop.x - this.leftBottom.x;
    },
    
	height:function(){
	/// <summary>矩形对象的高度。其值为上边界坐标值与下边界坐标值之差。 </summary>
	/// <returns type="Number">矩形对象的高度。</returns>
        return this.rightTop.y - this.leftBottom.y;
    },

	compare:function(object){
	/// <summary>对比此矩形对象与指定的对象。   </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回object对象，用于表达两个比较对象之间有差异的值。</returns>
	    if(!object){
            return;
        }
	    var newObject = null;
        if(object.leftBottom){
          if(!this.leftBottom){
              this.leftBottom = new SuperMap.Point2D()
          }
          var oLeftBottom = this.leftBottom.compare(object.leftBottom);
          if(oLeftBottom){
              if(!newObject)
                  newObject = new Object();
              newObject.leftBottom = oLeftBottom;
          }
        }
        if(object.rightTop){
          if(!this.rightTop){
              this.rightTop = new SuperMap.Point2D()
          }
          var oRightTop = this.rightTop.compare(object.rightTop);
          if(oRightTop){
              if(!newObject)
                  newObject = new Object();
              newObject.rightTop = oRightTop;
          }
        }
        return newObject;
	},

	equals:function(object){
	/// <summary>判定此矩形对象是否与指定的对象相等，只有当指定的对象为矩形对象，且与此矩形对象有相同的边界坐标值时才认为其相等。 </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Boolean">如相等，此方法将返回 true；否则将返回 false。</returns>
	    if(!object){
            return false;
        }
        if(object.leftBottom){
          if(!this.leftBottom){
              return false;
          }else{
              if(this.leftBottom.x != object.leftBottom.x){
                  return false;  
              }
              if(this.leftBottom.y != object.leftBottom.y){
                  return false;  
              }
          }
        }
        if(object.rightTop){
          if(!this.rightTop){
              return false;
          }else{
              if(this.rightTop.x != object.rightTop.x){
                  return false;  
              }
              if(this.rightTop.y != object.rightTop.y){
                  return false;  
              }
          }
        }

        return true;
	}
};
SuperMap.Rect2D.fromJson = function(jsonObject){
	/// <summary>将 JSON 对象转换为矩形对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.Rect2D">&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;矩形对象。</returns>
    if(!jsonObject){
        return null;
    }
    var object = jsonObject; 
    if(typeof(jsonObject) === "string"){
        object = eval('(' + jsonObject + ')');
    }
    var rect2D = new SuperMap.Rect2D(object.leftBottom.x, object.leftBottom.y, object.rightTop.x, object.rightTop.y);
    return rect2D;
};
SuperMap.Rect2D.registerClass('SuperMap.Rect2D');

SuperMap.MapParam = function(mapName, mapScale, mapScales){
	/// <summary> 该类描述地图的所有的特征信息。例如：地图的名称 MapParam.mapName、 地图的比例尺 MapParam.mapScale 等等。 &lt;br&gt;
	/// 该类通常作为各种 GIS 请求的参数传给服务器，服务器根据 MapParam 对象的各种地图参数值恢复地图状态并执行请求的处理。
	/// </summary>
	/// <param name="mapName"  type="String"  mayBeNull="true" optional="true">地图名称。</param>
	/// <param name="mapScale"  type="Number"  mayBeNull="true" optional="true" >地图的显示比例尺。</param>
	/// <param name="mapScales"  type="Array"  elementType="Number"  mayBeNull="true" optional="true">用以进行地图分级显示的比例尺 Double 数组。 </param>
	/// <returns type="SuperMap.MapParam">返回 MapParam 对象。</returns>
	/// <field name="mapScales" type="Number">用以进行地图分级显示的比例尺Double数组。</field>
	/// <field name="pixelCenter" type="SuperMap.Point">地图的中心点坐标，像素坐标。</field>
	/// <field name="center" type="SuperMap.Point2D">地图的中心点坐标，地理坐标。</field>
	/// <field name="pixelRect" type="SuperMap.Rect">地图的显示范围，像素坐标。</field>
	/// <field name="mapRect" type="SuperMap.Rect2D">地图的显示范围，地理坐标。</field>
	/// <field name="mapName" type="String">地图名称。</field>
	/// <field name="mapScale" type="String">地图的显示比例尺。</field>
	this.mapName = mapName;
    if(mapScale == 0 || mapScale){
    	this.mapScale = mapScale;
    }else{
        this.mapScale = NaN;
    }
	this.mapScales = mapScales;
	this.pixelCenter = new SuperMap.Point();
	this.center = new SuperMap.Point2D();
	this.pixelRect = new SuperMap.Rect();
	this.mapRect = new SuperMap.Rect2D();
	
    // 考虑改掉
    this._viewType = "pc"; //"pixelCenter","mc","pr","mapRect".
};
SuperMap.MapParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        this.pixelCenter = this.center = null;
    },
    
    makeCopy:function(){
	/// <summary>复制当前对象。</summary>
	/// <returns type="SuperMap.MapParam">MapParam 对象。</returns>
        var mapParam = new SuperMap.MapParam();
        mapParam.copy(this);
        return mapParam;
    },
    
	copy:function(param){
	/// <summary>复制点对象。</summary>
	/// <param name="param" type="Object">待复制的 MapParam 对象。</param>
        var self = this;
        self.mapScales = param.mapScales;
        self.mapName = param.mapName;
        self.mapScale = param.mapScale;
        self.pixelCenter.copy(param.pixelCenter);
        self.center.copy(param.center);
        self.pixelRect.copy(param.pixelRect);
        self.mapRect.copy(param.mapRect);
        self._viewType = param._viewType;
        self = null;
    },
    
	equals:function(param){
	/// <summary>判定此MapParam对象是否与指定的对象相等。 </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Boolean">如相等，此方法将返回 true；否则将返回 false。</returns>
        var self = this;
		if(param.pixelCenter == null){
			return false;
		}
        return param != null && self.mapName == param.mapName &&
                self.mapScale == param.mapScale && 
                Math.abs(self.pixelCenter.x - param.pixelCenter.x) < 0.000001 &&
                Math.abs(self.pixelCenter.y - param.pixelCenter.y) < 0.000001;
    },

	toString:function(){
	/// <summary>返回一个表示此 MapParam 对象的格式化字符串，格式为(mapName,center(格式化字符串形式),mapScale)。</summary>
	/// <returns type="String">表示此 MapParam 的字符串。</returns>
        var self = this;
        return "(" + self.mapName + ", " + self.center.toString() + ", " + self.mapScale + ")";
    },

	compare:function(object){
	/// <summary>对比此MapParam对象与指定的对象。</summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回 object 对象，用于表达两个比较对象之间有差异的值。</returns>
	    if(!object){
	        return;
	    }
	    var newObject = null
        if(this.mapName != object.mapName){
            if(!newObject)
                newObject = new Object();
            newObject.mapName = object.mapName;
        }
        if(this.mapScale != object.mapScale){
			if(!newObject)
				newObject = new Object();
			newObject.mapScale = object.mapScale;
		}
        if(object.pixelCenter){
            if(!this.pixelCenter){
				this.pixelCenter = new SuperMap.Point();
			}
            var oPixelCenter = this.pixelCenter.compare(object.pixelCenter);
            if(oPixelCenter){
				if(!newObject)
					newObject = new Object();
				newObject.pixelCenter = oPixelCenter;
			}
        }
        if(object.center){
            if(!this.center){
				this.center = new SuperMap.Point2D();
			}
            var oMapCenter = this.center.compare(object.center);
            if(oMapCenter){
				if(!newObject)
					newObject = new Object();
				newObject.center = oMapCenter;
			}
        }
        if(object.pixelRect){
            if(!this.pixelRect){
				this.pixelRect = new SuperMap.Rect();
			}
            var oPixelRect = this.pixelRect.compare(object.pixelRect);
            if(oPixelRect){
				if(!newObject)
					newObject = new Object();
				newObject.pixelRect = oPixelRect;
			}
        }
        if(object.mapRect){
            if(!this.mapRect){
				this.mapRect = new SuperMap.Rect2D();
			}
            var oMapRect = this.mapRect.compare(object.mapRect);
            if(oMapRect){
				if(!newObject)
					newObject = new Object();
				newObject.mapRect = oMapRect;
			}
        }
        return newObject;
	},

	setPixelCenter:function(pc){
	/// <summary>设置地图中心点的像素坐标。</summary>
	/// <param name="pc" type="SuperMap.Point">像素坐标点，&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</param>
        if(!pc){return;} 
        var self = this;
        self.pixelCenter = pc;
        self._viewType = "pc";
    },

	setMapCenter:function(mc){
	/// <summary>设置地图中心点的地理坐标。</summary>
	/// <param name="pc" type="SuperMap.Point2D">地理坐标点，&lt;see cref="T:SuperMap.Point2D"&gt;Point2D&lt;/see&gt;类型。</param>
        if(!mc){return;} 
        var self = this;
        self.center = mc;
        self._viewType = "mc";
    },
    
	setPixelRect:function(pr){
	/// <summary>设置地图窗口显示区域的范围，以像素坐标为基准。 </summary>
	/// <param name="pr" type="SuperMap.Rect">像素矩形对象，用以表达地图窗口显示范围。&lt;see cref="T:SuperMap.Rect"&gt;Rect&lt;/see&gt;类型。</param>
		if(!pr){return;}
		var self = this;
		self.pixelRect = pr;
		self._viewType = "pr";
	},

	setMapRect:function(mr){
	/// <summary>设置地图窗口显示区域的范围，以投影坐标系为基准。 </summary>
	/// <param name="mr" type="SuperMap.Rect2D">投影坐标系下的矩形对象，用以表达地图窗口显示范围。&lt;see cref="T:SuperMap.Rect2D"&gt;Rect2D&lt;/see&gt;类型。</param>
		if(!mr){return;}
		var self = this;
		self.mapRect = mr;
		self._viewType = "mr";
	},

	setMapScales:function(scales){
	/// <summary>设置用以进行地图分级显示的比例尺数组。  </summary>
	/// <param name="scales"  type="Array" elementType="Number">各级比例尺的值。Double 数组类型。</param>
        //todo:对比例尺级别进行校验和排序。
        if(!scales){return;}
		var self = this;
		self.mapScales = scales;
    },
    
	setMapScale:function(mapScale){
	/// <summary>设置地图的显示比例尺。  </summary>
	/// <param name="mapScale"  type="Number">地图显示比例尺。Double类型。</param>
	    if(!mapScale){return;}
		if(mapScale <= 0){return;}
        var self = this;
        mapScale= self._getLeveledScale(mapScale);
        var ratio = mapScale / self.mapScale;
		
		switch(self._viewType){
			case"pc":{
				self.pixelCenter.x *= ratio;
				self.pixelCenter.y *= ratio;
				break;
			}
			case"pr":{
				self.pixelRect.leftTop.x *= ratio;
				self.pixelRect.leftTop.y *= ratio;
				self.pixelRect.rightBottom.x *= ratio;
				self.pixelRect.rightBottom.y *= ratio;
				break;
			}
		}

		self.mapScale = mapScale;
	},

	setMapName:function(mapName){
	/// <summary>设置要浏览的地图名。  </summary>
	/// <param name="mapName"  type="String>地图名。</param>
        var self = this;
        self.mapName = mapName;
        if(self._viewType == "pc"){
            self._viewType = "mc";
        }
    },
    
	getPixelX:function(mapScale){
	/// <summary>获取地图中心点的x坐标值，以像素坐标为基准。   </summary>
	/// <param name="mapScale"  type="Number">地图显示比例尺</param>
	/// <returns type="Number" integer="true">mapScale比例尺下地图中心点x坐标值。</returns>
        var self = this;

		if(self.pixelCenter == null){
			return;
		}

		if(mapScale){
			return self.pixelCenter.x * (mapScale / self.mapScale);
		}

		return self.pixelCenter.x;
	},
    
	getPixelY:function(mapScale){
	/// <summary>获取地图中心点的y坐标值，以像素坐标为基准。   </summary>
	/// <param name="mapScale"  type="Number">地图显示比例尺</param>
	/// <returns type="Number" integer="true">mapScale比例尺下地图中心点y坐标值。</returns>
        var self = this;

		if(self.pixelCenter == null){
			return;
		}

		if(mapScale){
			return self.pixelCenter.y * (mapScale / self.mapScale);
		}

		return self.pixelCenter.y;
	},

	getPixelCenter:function(mapScale){
	/// <summary>获取地图中心点的坐标值，以像素坐标为基准。   </summary>
	/// <param name="mapScale"  type="Number">地图显示比例尺</param>
	/// <returns type="SuperMap.Point">mapScale比例尺下地图中心点坐标值。&lt;see cref="T:SuperMap.Point"&gt;Point&lt;/see&gt;类型。</returns>
        var self = this;
        if(!mapScale){
			return new SuperMap.Point(self.pixelCenter.x, self.pixelCenter.y);
		}

        return new SuperMap.Point(self.getPixelX(mapScale), self.getPixelY(mapScale));
    },

	getViewType:function(){
	/// <summary>获取当前视图校对基准类型。&lt;br&gt;
	/// 1. viewType包含四种类型：mc(代表MapCood), pc(代表PixelCoord）,mr(代表 MapRect) 和pr(代表 PixelRect)。 &lt;br&gt;
	/// 2. 地图的基本参数（sacle、center、bound等）可以通过多种设置方式获取，如SetPixelRect，SetMapRect，SetPixelCenter 和
	/// SetMapScale等。当这些方法得到的地图基本参数值发生冲突的时候，就需要使用ViewType来确定地图基本参数的基准值，
	/// 并使用resolve方法根据这个基准值来校对MapParam的各项参数。&lt;br&gt;
	/// 3. ViewType默认值为pc。 </summary>
	/// <returns type="String">视图校对基准类型.</returns>
        return this._viewType;
    },

	resolve:function(map, width, height){
	/// <summary>校对函数。通过该方法可以使 MapParam 的各项参数保持一致。&lt;br&gt;
	/// 1. 地图的基本参数（scale、center、bound 等）可能通过多种方式设置，如 setPixelRect，setMapRect，setPixelCenter 和 setMapScale 等。当获取的地图基本参数不一致时，需要调用该方法来将各参数进行校对，使其保持一致。&lt;br&gt;
	/// 2. 有四种校对的方式，包括：使用像素坐标中心点为基准点，使用地图坐标中心点为基准点，使用像素坐标范围为基准点和使用地图坐标范围为基准点。默认使用的是使用像素坐标中心点为基准点。
	/// </summary>
	/// <param name="map" type="SuperMap.Map">Map 对象。</param>
	/// <param name="width"  type="Number">地图控件宽度。</param>
	/// <param name="height"  type="Number">地图控件高度。</param>
        var self = this;
		switch(self._viewType){
			case"pc":
				self.center = map.pixelToMapCoord(self.pixelCenter, self.mapScale);
				break;
			case"mc":
				self.pixelCenter = map.mapCoordToPixel(self.center, self.mapScale);
				break;
			case"pr":
				self._validateByPixelRect(map, width, height);
				break;
			case"mr":{
				var mr = self.mapRect;
				//xianchao，第一遍计算目的是重现设置比例尺，第二遍的目的是按第一遍的比例尺计算新的范围，导致这个问题的原因是_validateByPixelRect方法中逻辑没有很好的分开。
				self.pixelRect.leftTop = map.mapCoordToPixel(new SuperMap.Point2D(mr.leftBottom.x, mr.rightTop.y), self.mapScale);
				self.pixelRect.rightBottom = map.mapCoordToPixel(new SuperMap.Point2D(mr.rightTop.x, mr.leftBottom.y), self.mapScale);
				self._validateByPixelRect(map, width, height, true);
				self.pixelRect.leftTop = map.mapCoordToPixel(new SuperMap.Point2D(mr.leftBottom.x, mr.rightTop.y), self.mapScale);
				self.pixelRect.rightBottom = map.mapCoordToPixel(new SuperMap.Point2D(mr.rightTop.x, mr.leftBottom.y), self.mapScale);
				// 尽量让 mapRect 都在可见范围内。
				self._validateByPixelRect(map, width, height, true);
				break;
			}
		}
		self._viewType = "pc";
	},
    
  	_getLeveledScale:function(scale){
        //Copy From MapControl.cs
        var self = this;
        if (!self.mapScales || self.mapScales.Length <= 0){
            return scale;
        }
        var leveledScale = 0.0;
        // 小于最小的,就应等于最小的
        if (scale <= self.mapScales[0]){
            leveledScale = self.mapScales[0];
            return leveledScale; 
        }
        var length = self.mapScales.length; 
        //大于最大的,就应等于最大的
        if (scale >= self.mapScales[length - 1]){
            leveledScale = self.mapScales[length - 1];
            return leveledScale;
        }
        var minScale = 0.0;
        var maxScale = 0.0;
        var flag = false;
        for (var i = 0; i < length; i++){
            if (scale <= self.mapScales[i]){
                minScale = self.mapScales[i - 1];
                maxScale = self.mapScales[i];
                break;
            }
        }
        //从minScale和maxScale里选一个
        if (scale / minScale <= maxScale / scale){
            return minScale;
        } else {
            return maxScale;
        }
    },
    
	_validateByPixelRect:function(map, width, height, ensureVisible){
        var self = this;
		// 调整中心点。
		self.pixelCenter.x = 0.5 * (self.pixelRect.leftTop.x + self.pixelRect.rightBottom.x);
		self.pixelCenter.y = 0.5 * (self.pixelRect.leftTop.y + self.pixelRect.rightBottom.y);
		self.center = map.pixelToMapCoord(self.pixelCenter, self.mapScale);

		// 校验传入的 mapRect 的高度与宽度比。以得到正确的比例尺。 
		// 正确的比例尺定义为：尽量接近原图的比例尺。 
		// 比如，宽度为原来的 1.1 倍，高度为原来的 1.2 倍，结果应当为 1.1 倍。 
		// 又如，宽度为原来的 0.9 倍，高度为原来的 0.8 倍，结果应当为 0.9 倍。 
		// 但是，宽度为原来的 0.9 倍，高度为原来的 1.1 倍，结果应当为 ??? 倍？ 
		// 结果是： 1/0.9 = 1.111 > 1.1，所以，结果为 1.1 倍。  

		var dScaleRatio;
		var dScaleRatioWidth = width / self.pixelRect.width();
		var dScaleRatioHeight = height / self.pixelRect.height();
		// 排列两者。使得 dScaleRatioWidth > dScaleRatioHeight。 
		dScaleRatio = Math.min(dScaleRatioWidth, dScaleRatioHeight);
		dScaleRatioWidth = Math.max(dScaleRatioWidth, dScaleRatioHeight);
		dScaleRatioHeight = dScaleRatio;

		if (dScaleRatioHeight > 1){	// 两者都大于1。
			dScaleRatio = dScaleRatioHeight;
		} else if (dScaleRatioWidth <= 1){	// 两者都小于等于于1。
			dScaleRatio = dScaleRatioWidth;
		} else {	// 一大一小。
			if (!ensureVisible && dScaleRatioWidth * dScaleRatioHeight < 1){	// a < 1/b，取a。
				dScaleRatio = dScaleRatioWidth;
			} else {
				dScaleRatio = dScaleRatioHeight;
			}
		}
		if (!self.mapScales || self.mapScales.Length <= 0){
           //不分级的情况
           self.mapScale = self.mapScale * dScaleRatio;
        } else {
            // 调整比例尺。
		    var ratio = 0;
		    var validScale = self.mapScale;
		    var diff = 0;
		    var minDiff = Number.MAX_VALUE;
		    for(var i = 0; i < self.mapScales.length; i++){
			    ratio = self.mapScales[i] / self.mapScale;
			    if((dScaleRatio > 1 && ratio < 1) || (dScaleRatio < 1 && ratio > 1)){
				    continue;
			    }
			    if(ensureVisible){
				    diff = dScaleRatio / ratio;
				    if(diff > 1 && diff < minDiff){
					    minDiff = diff;
					    validScale = self.mapScales[i];
				    }
			    } else {
				    diff = ratio / dScaleRatio > 1 ? ratio / dScaleRatio : dScaleRatio / ratio;
				    if(diff < minDiff){
					    minDiff = diff;
					    validScale = self.mapScales[i];
				    }
			    }
            }
            self.mapScale = validScale;
        }
		
		self.pixelCenter = map.mapCoordToPixel(self.center, self.mapScale); 
	}
};
SuperMap.MapParam.registerClass('SuperMap.MapParam', null, Sys.IDisposable);

// 地图状态信息。
SuperMap.MapStatus = function(){
	/// <summary> 地图初始化或者地图更新后的状态信息。该类主要用于在地图初始化或者地图更新后从服务器获取的地图初始状态信息，如当前控件窗口显示的地图名称，
	/// 地图中所有图层信息，地图初始化时的显示比例尺，显示范围，能够获取的所有地图的列表等。
	/// </summary>
	/// <returns type="SuperMap.MapStatus">返回 MapStatus 对象。</returns>
	/// <field name="mapScales" type="Number">用以进行地图分级显示的比例尺Double数组。</field>
	/// <field name="mapBounds" type="SuperMap.Rect2D">当前地图的空间范围。地图的空间范围是其所显示的各数据集的范围的最小外接矩形，
	/// 即包含各数据集范围的最小的矩形。当地图显示的数据集增加或删除时，其空间范围也会相应发生变化。</field>
	/// <field name="referViewBounds" type="SuperMap.Rect2D">当前地图的可视范围。</field>
	/// <field name="referScale" type="Number">地图初始化时默认的显示比例尺。</field>
	/// <field name="referViewer" type="SuperMap.Rect">地图初始化时默认的地图图片的尺寸。Rect类型。</field>
	/// <field name="mapName" type="String">地图名称。</field>
	/// <field name="layers"  type="Array" elementType="Object">地图中的所有图层数组。JSON 数组。</field>
	this.mapName = "";
	this.mapScales = new Array();		// int型数组
	this.mapBounds = new SuperMap.Rect2D();		// Rect2D
	this.referViewBounds = new SuperMap.Rect2D();	// Rect2D
	this.referScale = 0;			// double
	this.referViewer = new SuperMap.Rect();		// PixelRect
	this.layers = new Array();
};
SuperMap.MapStatus.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        this.mapBounds = null;
        this.referViewBounds = null;
        this.referViewer = null;
    }
};
SuperMap.MapStatus.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为MapStatus对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.MapStatus">MapStatus对象。</returns>
    if(!jsonObject){
        return null;
    }
    var object = jsonObject;
    if(typeof(jsonObject) === "string"){
        object = eval('(' + jsonObject + ')');
    }

    var mapStatus = new SuperMap.MapStatus();
    mapStatus.mapName = object.mapName;
    mapStatus.mapScales = object.mapScales;
    if(!mapStatus.mapBounds){
        mapStatus.mapBounds = new SuperMap.Rect2D();
    }
    mapStatus.mapBounds.copy(object.mapBounds);
    if(!mapStatus.referViewBounds){
        mapStatus.referViewBounds = new SuperMap.Rect2D();
    }
    mapStatus.referViewBounds.copy(object.referViewBounds);
    mapStatus.referScale = object.referScale;
    if(!mapStatus.referViewer){
        mapStatus.referViewer = new SuperMap.Rect();
    }
    mapStatus.referViewer.copy(object.referViewer);
    if(object.layers){
        var count = object.layers.length;
        if(count>0){
            mapStatus.layers = new Array(count);
            for(var i=0;i<count;i++){
                mapStatus.layers[i]= new SuperMap.Layer();
                mapStatus.layers[i].copy(object.layers[i]);
            }
        }
    }

    return mapStatus;
};
SuperMap.MapStatus.registerClass('SuperMap.MapStatus', null, Sys.IDisposable);

SuperMap.Overview = function(){
	/// <summary> 鹰眼对象。该类用于表达鹰眼的特征信息，如鹰眼中地图的显示范围，鹰眼图片的url地址。
	/// </summary>
	/// <returns type="SuperMap.Overview">返回 Overview 对象。</returns>
	/// <field name="url" type="String">鹰眼图片的地址。</field>
	/// <field name="viewBounds" type="SuperMap.Rect2D">鹰眼控件中显示的地图的可视范围。</field>
	this.url = "";
	this.viewBounds = new SuperMap.Rect2D();// 要new，Decoder要求
};
SuperMap.Overview.prototype = {
	copy:function(object){
	/// <summary>复制鹰眼对象。</summary>
	/// <param name="object">原鹰眼对象。</param>
	/// <returns type="SuperMap.Overview">&lt;see cref="T:SuperMap.Overview"&gt;Overview&lt;/see&gt;鹰眼对象。</returns>
		this.url = object.url;
		this.viewBounds = new SuperMap.Rect2D();
		this.viewBounds.copy(object.viewBounds);
	}
};
SuperMap.Overview.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为Overview对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Overview">&lt;see cref="T:SuperMap.Overview"&gt;Overview&lt;/see&gt;鹰眼对象。</returns>
    if(!jsonObject){
        return null;
    }
    var object = jsonObject;
    if(typeof(jsonObject) === "string"){
        object = eval('(' + jsonObject + ')');
    }
    
    var overview = new SuperMap.Overview();

	overview.url = object.url;
	overview.viewBounds = new SuperMap.Rect2D();
	overview.viewBounds.copy(object.viewBounds);

    return overview;
};
SuperMap.Overview.registerClass('SuperMap.Overview', null, Sys.IDisposable);

SuperMap.Color = function(red, green, blue){
	/// <summary>颜色类，该类用三原色表达。</summary>
	/// <param name="red"  type="Number" >RGB中红色值。</param>
	/// <param name="green"  type="Number">RGB中绿色值。</param>
	/// <param name="blue"   type="Number">RGB中蓝色值。</param>
	/// <returns type="SuperMap.Color">返回 Color 对象。</returns>
	/// <field name="red"  type="Number">RGB中红色值。默认为255。</field>
	/// <field name="green"  type="Number">RGB中绿色值。默认为0。</field>
	/// <field name="blue"  type="Number">RGB中蓝色值。默认为0。</field>
    this.red = 255;
    this.green = 0;
    this.blue = 0;
    if(typeof(red)!="undefined" && red >= 0 && red <= 255){
        this.red = red;
    }
    if(typeof(green)!="undefined" && green >= 0 && green <= 255){
        this.green = green;
    }
    if(typeof(blue)!="undefined" && blue >= 0 && blue <= 255){
        this.blue = blue;
    }
};
SuperMap.Color.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
    },

    compare:function(object){
	/// <summary>对比此对象与指定的对象。   </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回object对象，用于表达两个比较对象之间有差异的值。</returns>
        if(!object){
            return false;
        }
        var newObject = null;
        if(this.red != object.red){
            if(!newObject)
                newObject = new Object();
            newObject.red = object.red;
        }
        if(this.green != object.green){
            if(!newObject)
                newObject = new Object();
            newObject.green = object.green;
        }
        if(this.blue != object.blue){
            if(!newObject)
                newObject = new Object();
            newObject.blue = object.blue;
        }
    }
};
SuperMap.Color.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为Color对象。</summary>
	/// <param name="object">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Color">&lt;see cref="T:SuperMap.Color"&gt;Color&lt;/see&gt;颜色对象。</returns>
    if(!jsonObject){return;}
	var color = new SuperMap.Color();
    color.red = jsonObject.red;
    color.green = jsonObject.green;
    color.blue = jsonObject.blue;

	return color;
};
SuperMap.Color.registerClass('SuperMap.Color', null, Sys.IDisposable);

SuperMap.Style = function(){
	/// <summary>风格类，该类用于定义点状符号、线状符号、填充符号风格及其相关属性。 &lt;br&gt;
	/// 1. 对于各种几何对象，文本对象只能设置文本风格，不能设置几何风格。 &lt;br&gt;
	/// 2. 该类的实例中只存储风格的ID值，具体的风格存储在相应的点状符号、线状符号、填充符号库中，有关点状符号、线状符号、填充符号库。 &lt;br&gt;
	/// 3. 除复合数据集(CAD 数据集)之外，其他类型数据集都不存储几何对象的风格信息。 &lt;br&gt;
	/// 4. 填充模式分为普通填充模式和渐变填充模式。在普通填充模式下，可以使用图片或矢量符号等进行填充；在渐变填充模式下，有四种渐变类型可供选择：线性渐变填充，辐射渐变填充，圆锥渐变填充和四角渐变填充。
	/// </summary>
	/// <returns type="SuperMap.Style">返回 Style 对象。</returns>
	/// <field name="fillGradientAngle" type="Number">渐变填充的旋转角度，以度为单位，精确到0.1度，逆时针方向为正方向。有关各渐变填充风格类型的定义，请参见FillGradientMode。&lt;br&gt; 
	/// 对于不同的渐变填充，其旋转的后的效果各异，但都是以最小外接矩形的中心为旋转中心，逆时针旋转的。&lt;br&gt; 
	///		1. 线性渐变（Linear）&lt;br&gt; 
	///			当设置的角度为0-360度的任意角度时，经过起始点和终止点的线以最小外接矩形的中心为旋转中心逆时针旋转，渐变风格随之旋转，依然从线的起始端渐变到终止端的线性渐变。。如下列举在特殊角度的渐变风格：&lt;br&gt; 
	///				(a)当渐变填充角度设置为0度或者360度的时候，那么渐变填充风格为由左到右从起始色到终止色的线性渐变，如图所示起始色为黄色，终止色为粉红色；&lt;br&gt; 
	///	&lt;img src="../img/Fill_360.png" /&gt; &lt;br&gt;
	///				(b)当渐变填充角度设置为180度时，渐变填充风格与(a)中描述的风格正好相反，即从右到左，从起始色到终止色线性渐变；&lt;br&gt; 
	///	&lt;img src="../img/Fill_180.png" /&gt; &lt;br&gt;
	/// 			(c)当渐变填充角度设置为90度时，渐变填充风格为由下到上，起始色到终止色的线性渐变。&lt;br&gt; 
	/// &lt;img src="../img/Fill_90.png" /&gt; &lt;br&gt;
	///				(d)当渐变填充角度设置为270度时，渐变填充风格与(c)中描述的风格正好相反，即从上到下，起始色到终止色线性渐变。&lt;br&gt; 
	/// &lt;img src="../img/Fill_270.png" /&gt; &lt;br&gt;
	///		2. 辐射渐变（Radial）&lt;br&gt; 
	///			渐变填充角度设置为任何角度（不超出正常范围）时，将定义辐射渐变的圆形按照设置的角度进行旋转，由于圆是关于填充范围的最小外接矩形的中心点对称的，所以旋转之后的渐变填充的风格始终保持一样，即从中心点到填充范围的边界，从前景色到背景色的辐射渐变。&lt;br&gt; 
	///		3. 圆锥渐变（Conical）&lt;br&gt; 
	///			当渐变角度设置为0-360度之间的任何角度，该圆锥的所有母线将发生旋转，以圆锥的中心点，即填充区域的最小外接矩形的中心为旋转中心，逆时针方向旋转。如图所示的例子中，旋转角度为90度，所有的母线都从起始位置（旋转角度为零的位置）开始旋转到指定角度，以经过起始点的母线为例，其从0度位置旋转到90度位置。&lt;br&gt; 
	/// &lt;table&gt;&lt;tr&gt;&lt;td&gt;&lt;img src="../img/GeoS_Angle1.png" /&gt;&lt;/td&gt;&lt;td&gt;&lt;img src="../img/GeoS_Angle2.png" /&gt;&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt; &lt;br&gt;
	///		4. 四角渐变（Square）&lt;br&gt; 
	///			根据给定的渐变填充角度，将发生渐变的正方形以填充区域范围的中心为中心进行相应的旋转，所有正方形都是从初始位置即旋转角度为零的默认位置开始旋转。渐变依然是从内部的正方形到外部的正方形发生从起始色到终止色的渐变。
	/// </field>
	/// <field name="fillBackColor" type="SuperMap.Color">填充背景颜色。&lt;br&gt; 
	/// 当填充模式为渐变填充时，该颜色为填充终止色。</field>
	/// <field name="fillBackOpaque"  type="Boolean">背景是否不透明，false 表示透明。默认为背景透明。</field>
	/// <field name="fillForeColor" type="SuperMap.Color">填充颜色。当填充模式为渐变填充时，该颜色为填充起始颜色。</field>
	/// <field name="fillGradientOffsetRatioX" type="Number">渐变填充中心点相对于填充区域范围中心点的水平偏移百分比。&lt;br&gt;
	/// 设填充区域范围中心点的坐标为（x0, y0），填充中心点的坐标为（x, y），填充区域范围的宽度为a，水平偏移百分比为dx，则&lt;br&gt; 
	/// x=x0 + a*dx/100  &lt;br&gt; 
	/// 该百分比可以为负，当其为负时，填充中心点相对于填充区域范围中心点向x轴负方向偏移。&lt;br&gt; 
	/// 该属性仅对辐射渐变、圆锥渐变和四角渐变填充有效，不适用于线性渐变填充。
	/// </field>
	/// <field name="fillGradientOffsetRatioY" type="Number">填充中心点相对于填充区域范围中心点的垂直偏移百分比。
	/// 设填充区域范围中心点的坐标为（x0, y0），填充中心点的坐标为（x, y），填充区域范围的高度为b，垂直偏移百分比为dy，则&lt;br&gt;  
	/// Y=y0 + b*dy/100  &lt;br&gt; 
	/// 该百分比可以为负，当其为负时，填充中心点相对于填充区域范围中心点向y轴负方向偏移。&lt;br&gt; 
	/// 该属性仅对辐射渐变、圆锥渐变和四角渐变填充有效，线性渐变填充不适用。
	/// </field>
	/// <field name="fillOpaqueRate" type="Number">填充不透明度，合法值为0--100的数值。其中为0表示完全透明；100表示完全不透明。赋值小于0时按照0处理，大于100时按照100处理。 &lt;br&gt; 
	/// 默认值为100，表示完全不透明。
	/// </field>
	/// <field name="fillGradientMode" type="Number">渐变填充风格的渐变类型。 &lt;br&gt; 
	/// 不同的渐变类型对应的常量值在FillGradientMode类中定义，关于各渐变填充类型的定义，请参见 FillGradientMode 类。
	/// </field>
	/// <field name="fillSymbolID" type="Number">填充符号的编码。此编码用于唯一标识各普通填充风格的填充符号。
	/// </field>
	/// <field name="lineColor"  type="SuperMap.Color">边线的颜色。</field>
	/// <field name="lineWidth"   type="Number">边线的宽度，单位为毫米，精度到0.1。 </field>
	/// <field name="lineSymbolID"   type="Number">线状符号的编码。此编码用于唯一标识各线状符号。 
	/// </field>
	/// <field name="markerAngle"  type="Number">点状符号的旋转角度，以度为单位，精确到0.1度，逆时针方向为正方向。 &lt;br&gt; 
	/// 此角度可以作为普通填充风格中填充符号的旋转角度。  &lt;br&gt; 
	/// 默认值为0。
	/// </field>
	/// <field name="markerSize"  type="Number">点状符号的大小，单位为毫米，精度为0.1。  &lt;br&gt; 
	///  其值必须为大于等于0，如果为0，则表示不显示，如果是小于0，会抛出异常。
	/// </field>
	/// <field name="markerSymbolID"  type="Number">点状符号的编码。此编码用于唯一标识各点状符号。
	/// </field>
	this.fillGradientAngle = 0.0;
	this.fillBackColor = new SuperMap.Color(255,255,255);
	this.fillBackOpaque = false;
	this.fillGradientOffsetRatioX = 0.0;
	this.fillGradientOffsetRatioY = 0.0;
	this.fillForeColor = new SuperMap.Color(0,0,255);
	this.fillGradientMode; // mode
	this.fillOpaqueRate = 100; // int
	this.fillSymbolID = 0; // int
	this.lineColor = new SuperMap.Color(0,0,0);
	this.lineSymbolID = 0;
	this.lineWidth = 0.01;
	this.markerAngle = 0.0;
	this.markerSize = 1;
	this.markerSymbolID = -1;
};
SuperMap.Style.prototype = {
    compare:function(object){
	/// <summary>对比此对象与指定的对象。   </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回object对象，用于表达两个比较对象之间有差异的值。</returns>
        if(!object){
            return false;
        }
        var newObject = null;
		if(this.markerAngle != object.markerAngle) {
			if(!newObject) {
				newObject = new Object();
			}
			newObject.markerAngle = object.markerAngle;
		}
        if(this.fillBackColor){
            var oBrushBackColor = this.fillBackColor.compare(object.fillBackColor);
            if(oBrushBackColor){
                if(!newObject)
                    newObject = new Object();
                    newObject.fillBackColor = oBrushBackColor;
            }
        }
        if(this.fillBackOpaque != object.fillBackOpaque){
            if(!newObject)
                newObject = new Object();
            newObject.fillBackOpaque = object.fillBackOpaque;
        }
		if(this.fillGradientOffsetRatioX != object.fillGradientOffsetRatioX){
            if(!newObject)
                newObject = new Object();
            newObject.fillGradientOffsetRatioX = object.fillGradientOffsetRatioX;
        }
		if(this.fillGradientOffsetRatioY != object.fillGradientOffsetRatioY){
            if(!newObject)
                newObject = new Object();
            newObject.fillGradientOffsetRatioY = object.fillGradientOffsetRatioY;
        }
		if(this.fillGradientMode != object.fillGradientMode){
            if(!newObject)
                newObject = new Object();
            newObject.fillGradientMode = object.fillGradientMode;
        }
		if(this.fillOpaqueRate != object.fillOpaqueRate){
            if(!newObject)
                newObject = new Object();
            newObject.fillOpaqueRate = object.fillOpaqueRate;
        }
        if(this.fillForeColor){
            var oBrushColor = this.fillForeColor.compare(object.fillForeColor);
            if(oBrushColor){
                if(!newObject)
                    newObject = new Object();
                newObject.fillForeColor = oBrushColor;
            }
        }
        if(this.brushStyle != object.brushStyle){
            if(!newObject)
                newObject = new Object();
            newObject.brushStyle = object.brushStyle;
        }
        if(this.lineColor){
            var oPenColor = this.lineColor.compare(object.lineColor);
            if(oPenColor){
                if(!newObject)
                    newObject = new Object();
                newObject.lineColor = oPenColor;
            }
        }
        if(this.lineSymbolID != object.lineSymbolID){
            if(!newObject)
                newObject = new Object();
            newObject.lineSymbolID = object.lineSymbolID;
        }
        if(this.lineWidth != object.lineWidth){
            if(!newObject)
                newObject = new Object();
            newObject.lineWidth = object.lineWidth;
        }
        if(this.markerAngle != object.markerAngle){
            if(!newObject)
                newObject = new Object();
            newObject.markerAngle = object.markerAngle;
        }
        if(this.markerSize != object.markerSize){
            if(!newObject)
                newObject = new Object();
            newObject.markerSize = object.markerSize;
        }
        if(this.markerSymbolID != object.markerSymbolID){
            if(!newObject)
                newObject = new Object();
            newObject.markerSymbolID = object.markerSymbolID;
        }

        return newObject;
    },

    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.fillBackColor){
            this.fillBackColor.dispose();
            this.fillBackColor = null;
        }   
        if(this.fillForeColor){
            this.fillForeColor.dispose();
            this.fillForeColor = null;
        }  
        if(this.lineColor){
            this.lineColor.dispose();
            this.lineColor = null;
        }   
    }
};
SuperMap.Style.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为Style对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Style">&lt;see cref="T:SuperMap.Style"&gt;Style&lt;/see&gt;风格对象。</returns>
    if(!jsonObject){
        return;
    }
	var style = new SuperMap.Style();
    style.markerAngle = jsonObject.markerAngle;
	if(jsonObject.fillBackColor){
        style.fillBackColor = SuperMap.Color.fromJson(jsonObject.fillBackColor);
    }
    style.fillBackOpaque = jsonObject.fillBackOpaque;
    style.fillGradientOffsetRatioX = jsonObject.fillGradientOffsetRatioX;
	style.fillGradientOffsetRatioY = jsonObject.fillGradientOffsetRatioY;
	if(jsonObject.fillForeColor){
        style.fillForeColor = SuperMap.Color.fromJson(jsonObject.fillForeColor);
    }
	style.fillGradientMode = jsonObject.fillGradientMode;
	style.fillOpaqueRate = jsonObject.fillOpaqueRate;
    style.fillSymbolID = jsonObject.fillSymbolID;
     if(jsonObject.lineColor){
        style.lineColor = SuperMap.Color.fromJson(jsonObject.lineColor);
    }
    style.lineSymbolID = jsonObject.lineSymbolID;
    style.lineWidth = jsonObject.lineWidth;
    style.markerAngle = jsonObject.markerAngle;
    style.markerSize = jsonObject.markerSize;
    style.markerSymbolID = jsonObject.markerSymbolID;
	return style;
};
SuperMap.Style.registerClass('SuperMap.Style', null, Sys.IDisposable);

SuperMap.ActionEventArgs = function(){
	/// <summary>为 Action 事件提供数据。 </summary>
	/// <returns type="SuperMap.ActionEventArgs">返回 ActionEventArgs 对象。</returns>
	/// <field name="actionParams">地图鼠标操作对象的参数。 </field>
	/// <field name="mapCoords">Action 事件传回的地理坐标对MapCoord数组。 </field>
	
    this.actionParams = "";
    this.mapCoords = null;
};
SuperMap.ActionEventArgs.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.mapCoords){
            while(this.mapCoords.length > 0){
                var temp = this.mapCoords.pop();
                temp.dispose();
                temp = null;
            }
        }
    }
};
SuperMap.ActionEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ActionEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ActionEventArgs">&lt;see cref="T:SuperMap.ActionEventArgs"&gt;ActionEventArgs&lt;/see&gt;对象。</returns>
	if(!jsonObject){
		return;
	}
	var eventArgs = new SuperMap.ActionEventArgs();
	eventArgs.actionParams = jsonObject.actionParams;
	if(jsonObject.mapCoords && jsonObject.mapCoords.length > 0){
		eventArgs.mapCoords = new Array();
		for(var i = 0; i < jsonObject.mapCoords.length; i++){
			eventArgs.mapCoords[i] = new SuperMap.Point2D();
			eventArgs.mapCoords[i].x = jsonObject.mapCoords[i].x;
			eventArgs.mapCoords[i].y = jsonObject.mapCoords[i].y;
		}
	}
	
	return eventArgs;
};
SuperMap.ActionEventArgs.registerClass('SuperMap.ActionEventArgs', null, Sys.IDisposable);

SuperMap.MeasuringEventArgs = function(){
	/// <summary>为 Measuring 事件提供数据。 </summary>
	/// <returns type="SuperMap.MeasuringEventArgs">返回 MeasuringEventArgs 对象。</returns>
	/// <field name="isHighlight"   type="Boolean">是否要高亮沿线或者沿线所组成的多边形区域。默认为 true。 </field>
	/// <field name="highlightStyle"   type="SuperMap.Style">高亮的风格。 </field>
	/// <field name="clientActionArgs" type="SuperMap.ActionEventArgs">鼠标操作时的参数。 </field>
	/// <field name="cancel"   type="Boolean">指示是否应取消事件的值。默认为false。 </field>
    this.isHighlight = true;
    this.highlightStyle = null;
    this.clientActionArgs = null;
    this.cancel = false;
};
SuperMap.MeasuringEventArgs.prototype = {
    dipose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.highlightStyle){
            this.highlightStyle = null;
        }
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
        }
    }
};
SuperMap.MeasuringEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 MeasuringEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.MeasuringEventArgs">&lt;see cref="T:SuperMap.MeasuringEventArgs"&gt;MeasuringEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){
        return;
    }
	var eventArgs = new SuperMap.MeasuredEventArgs();
    eventArgs.isHighlight = jsonObject.isHighlight;
    if(object.highlightStyle){
        eventArgs.highlightStyle = SuperMap.Style.fromJson(object.highlightStyle);
    }
    if(object.clientActionArgs){
        eventArgs.clientActionArgs = SuperMap.ActionEventArgs.fromJson(jsonObject.clientActionArgs);
    }
    eventArgs.cancel = jsonObject.cancel;
	
	return eventArgs;
};
SuperMap.MeasuringEventArgs.registerClass('SuperMap.MeasuringEventArgs', null, Sys.IDisposable);

SuperMap.MeasuredEventArgs = function(){
	/// <summary>为 Measured 事件提供数据。 </summary>
	/// <returns type="SuperMap.MeasuredEventArgs">返回 MeasuredEventArgs 对象。</returns>
	/// <field name="area"   type="Number">量算返回的面积结果。 </field>
	/// <field name="distance"  type="Number">量算返回的距离结果。  </field>
    this.area =0.0;
    this.distance = 0.0;
};
SuperMap.MeasuredEventArgs.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
    }
};
SuperMap.MeasuredEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 MeasuredEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.MeasuredEventArgs">&lt;see cref="T:SuperMap.MeasuredEventArgs"&gt;MeasuredEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){
        return;
    }
	var eventArgs = new SuperMap.MeasuredEventArgs();
    eventArgs.area = jsonObject.area;
    eventArgs.distance = jsonObject.distance;
	
	return eventArgs;
};
SuperMap.MeasuredEventArgs.registerClass('SuperMap.MeasuredEventArgs', null, Sys.IDisposable);

SuperMap.MeasureResult = function(){
	/// <summary>该类主要用于量算结果的描述。进行距离量算和面积量算时测量的结果通过该类存储。&lt;br&gt;
	/// 1. 面积量算的结果由MeasureResult.area获得； &lt;br&gt;
	/// 2. 距离量算的结果由MeasureResult.distance获得。 </summary>
	/// <returns type="SuperMap.MeasureResult">返回 MeasureResult 对象。</returns>
	/// <field name="area"   type="Number">量算返回的面积结果。 </field>
	/// <field name="distance"   type="Number">量算返回的距离结果。  </field>
    this.area = 0.0;
    this.distance = 0.0;
};
SuperMap.MeasureResult.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
    }  
};
SuperMap.MeasureResult.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 MeasureResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.MeasureResult">&lt;see cref="T:SuperMap.MeasureResult"&gt;MeasureResult&lt;/see&gt;对象。</returns>
    if(!jsonObject){
        return;
    }
	var result = new SuperMap.MeasureResult();
    result.area = jsonObject.area;
    result.distance = jsonObject.distance;
	
	return result;
};
SuperMap.MeasureResult.registerClass('SuperMap.MeasureResult', null, Sys.IDisposable);

SuperMap.QueryingEventArgs = function(){
	/// <summary>为Querying事件提供数据。 </summary>
	/// <returns type="SuperMap.QueryingEventArgs">返回 QueryingEventArgs 对象。</returns>
	/// <field name="clientActionArgs" type="SuperMap.ActionEventArgs">客户端 Action 事件参数。  </field>
	/// <field name="queryParams"  type="SuperMap.QueryParam">查询参数对象。</field>
	/// <field name="cancel"   type="Boolean">指示是否应取消事件的值。 默认为false。  </field>
    this.clientActionArgs = null;
    this.queryParams = null;
    this.cancel = false;
};
SuperMap.QueryingEventArgs.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.queryParams){
            this.queryParams.dispose();
        }
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
        }
    }
};
SuperMap.QueryingEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 QueryingEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.QueryingEventArgs">&lt;see cref="T:SuperMap.QueryingEventArgs"&gt;QueryingEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.QueryingEventArgs();
    if(jsonObject.clientActionArgs){
        eventArgs.clientActionArgs = SuperMap.ActionEventArgs.fromJson(jsonObject.clientActionArgs);
    }
    if(jsonObject.queryParams){
        eventArgs.queryParams = SuperMap.QueryParam.fromJson(jsonObject.queryParams);
    }
    eventArgs.cancel = jsonObject.cancel;
    
	return eventArgs;
};
SuperMap.QueryingEventArgs.registerClass('SuperMap.QueryingEventArgs', null, Sys.IDisposable);

SuperMap.SqlParam = function(){
	/// <summary>该类可以用来设置查询的属性过滤条件。</summary> 
	/// <returns type="SuperMap.SqlParam">返回 SqlParam 对象。</returns>
	/// <field name="groupClause"  type="String">Group By 语句。如设置 groupClause = "SmID"，表示 group by SmID。   </field>
	/// <field name="returnFields" type="Array"  elementType="String">需要返回的字段名称数组。  </field>
	/// <field name="sortClause" type="String">Order By 语句。如设置 sortClause = "SmID"，表示 order by SmID。  </field>
	/// <field name="whereClause"  type="String">对该图层设置的查询条件语句。   </field>
	/// <field name="ids"  type="Array"  elementType="Number">要查找的地物的 SMID 数组。  </field>
    this.groupClause = "";    // Group By 语句。 比如“group by SmID”。
	this.returnFields = null; // 需要返回的字段。  
    this.sortClause = "";		// Order By 语句。 比如“order by SmID”。
    this.whereClause = "";    // 对该图层设置的查询条件语句。  
	this.ids = null;
};
SuperMap.SqlParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.groupClause = null;
		this.returnFields = null;
	    this.sortClause = null;
	    this.whereClause = null;
		this.ids = null;
	},
	toJSON:function() {
	/// <summary>将对象转换成json字符串。</summary>
	/// <returns type="String">SuperMap.SqlParam的JSON字符串对象。</returns>
		var json = '{';
		var v = [];
		if(this.groupClause && this.groupClause != null && this.groupClause != "") {
			v.push('"groupClause":' + '"' + this.groupClause + '"');
		}
		if(this.sortClause && this.sortClause != null && this.sortClause != "") {
			v.push('"sortClause":' + '"' + this.sortClause + '"');
		}
		if(this.whereClause && this.whereClause != null && this.whereClause != "") {
			v.push('"whereClause":' + '"' + this.whereClause + '"');
		}
		if(this.returnFields && this.returnFields.length > 0) {
			v.push('"returnFields":' + '[' + this.returnFields.join(", ") + ']');
		}
		if(this.ids && this.ids.length > 0) {
			v.push('"ids":' + '[' + this.ids.join(", ") + ']');
		}
		json += v.join(", ");
		json += '}';
		json = json.replace(/\n/g,"\\n");
		json = json.replace(/\r/g,"\\r");
		json = json.replace("<", "&lt;");
		json = json.replace(">", "&gt;");
		json = json.replace(/%/g, "%25");
		json = json.replace(/&/g, "%26");
		
		return json;
	},
	clone:function() {
	/// <summary>将对象复制并返回。</summary>
	/// <returns type="String">SuperMap.SqlParam的复制对象。</returns>
		var sqlParam = new SuperMap.SqlParam();
		sqlParam.whereClause = this.whereClause;
		sqlParam.groupClause = this.groupClause;
		if(this.returnFields) {
			sqlParam.returnFields = new Array(this.returnFields);
		}
		sqlParam.sortClause = this.sortClause;
		if(this.ids) {
			sqlParam.ids = new Array(this.ids);
		}
		
		return sqlParam;
	}
};
SuperMap.SqlParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 SqlParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.SqlParam">&lt;see cref="T:SuperMap.SqlParam"&gt;SqlParam&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var sqlParam = new SuperMap.SqlParam();
	sqlParam.groupClause = jsonObject.groupClause;
	sqlParam.returnFields = jsonObject.returnFields;
    sqlParam.sortClause = jsonObject.sortClause;
    sqlParam.whereClause = jsonObject.whereClause;
	sqlParam.ids = jsonObject.ids;

	return sqlParam;
};
SuperMap.SqlParam.registerClass('SuperMap.SqlParam', null, Sys.IDisposable);

SuperMap.QueryLayerParam = function(){
	/// <summary>该类用于设置某查询图层的查询参数。如该查询图层的名称，该查询图层的过滤条件（sqlParam）。&lt;br&gt;
	/// 注意：查询时，图层名（name）的长度和返回字段名（sqlParam.returnFields）的长度之和不能大于27个字符，否则查询结果为空。
	/// </summary>
	/// <returns type="SuperMap.QueryLayerParam">返回 QueryLayerParam 对象。</returns>
	/// <field name="name"  type="String">查询图层的名称。     </field>
	/// <field name="sqlParam"  type="SuperMap.SqlParam">过滤条件设置对象。SuperMap.SqlParam类型。  </field>
	
//    this.layerId = 0;			// 查询图层的ID。
    this.name = "";		// 查询图层的名称。  
    this.sqlParam = new SuperMap.SqlParam();
};
SuperMap.QueryLayerParam.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        this.name = null;
		if(this.sqlParam) {
			this.sqlParam.dispose();
		}
		this.sqlParam = null;
    },
	toJSON:function() {
	/// <summary>将对象转换成json字符串。</summary>
	/// <returns type="String">SuperMap.QueryLayerParam的json字符串对象。</returns>
		var json = '{';
		var v = [];
		if(this.name) {
			v.push('"name":' + '"' + this.name + '"');
		}
		if(this.sqlParam) {
			var sqlParamJSON = this.sqlParam.toJSON();
			if(sqlParamJSON && sqlParamJSON.length > 2) {
				v.push('"sqlParam":' + sqlParamJSON);
			}
		}
		json += v.join(", ");
		json += '}';

		return json;
	}
};
SuperMap.QueryLayerParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 QueryLayerParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.QueryLayerParam">&lt;see cref="T:SuperMap.QueryLayerParam"&gt;QueryLayerParam&lt;/see&gt;对象。</returns>
    if(!jsonObject){return null;} 
	var object = jsonObject; 
	if(typeof(json) == "string"){
		object = eval('(' + jsonObject + ')');
	}
    var ql = new SuperMap.QueryLayerParam();
 //   ql.layerId = object.layerId;
    ql.name = object.name;
    ql.sqlParam = SuperMap.SqlParam.fromJson(object.sqlParam);

    return ql;
};
SuperMap.QueryLayerParam.registerClass('SuperMap.QueryLayerParam', null, Sys.IDisposable);

SuperMap.QueryParam = function(){
	/// <summary>查询参数。&lt;br&gt;
	/// 1. 进行地图查询时设置条件的类。诸如进行点选查询、 框选查询、查找距离指定点一定范围内的所有对象 、
	/// 查找一定距离内最近对象、 SQL 查询等，都需要通过设置查询参数然后进行查询。 
	/// 通过该类可以设置查询图层的过滤条件 queryLayerParams、 设置返回的结果类型（如返回属性还是空间属性、全部都返回）returnResultSetInfo 等。&lt;br&gt;
	/// 2. 每一个查询图层的过滤条件需要通过对应的 QueryLayerParam 进行设置，如查询图层名称，查询条件 WhereClause 和返回字段 ReturnFields。
	/// </summary>
	/// <returns type="SuperMap.QueryParam">返回 QueryParam 对象。</returns>
	/// <field name="customParams">自定义参数。  </field>
	/// <field name="expectCount"  type="Number">期望返回的记录数。  </field>
	/// <field name="queryLayerParams"  type="Array" elementType="SuperMap.QueryLayerParam">待查询的图层。QueryLayerParam 数组。   </field>
	/// <field name="startRecord"  type="Number">起始记录号。默认从0开始计数。  </field>
	/// <field name="returnResultSetInfo"  type="SuperMap.ReturnResultSetInfo">返回结果类型。返回的类型包括属性，属性和空间属性，空间属性等，默认返回属性信息 。   </field>
	/// <field name="networkType"  type="Number">如果查找网络图层数据，该属性设置查询网络图层的哪种子图层。  </field>
    this.customParams = "";		// 自定义参数。
    this.expectCount = 10;		// 期望返回的记录数。
	this.queryLayerParams = null;      // 待查询的图层。  QueryLayerParam 数组。
    this.startRecord = 0;			// 起始记录号。默认从0开始计数。
	this.returnResultSetInfo = new SuperMap.ReturnResultSetInfo().returnAttribute;  // 返回结果类型，属性，属性和空间属性， 空间属性等, 默认返回属性信息
	this.networkType = new SuperMap.LayerType().line; // 查询网络图层类型
};
SuperMap.QueryParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.queryLayerParams){
            for(var i = 0; i < this.queryLayerParams.length; i++){
				var queryLayer = this.queryLayerParams.pop();
				queryLayer.dispose();
			}
            this.queryLayerParams = null;
        }
    },
	toJSON:function() {
	/// <summary>将对象转换成json字符串。</summary>
	/// <returns type="String">SuperMap.QueryParam的json字符串对象。</returns>
		var json = '{';
		var v = [];
		if(this.customParams && this.cusomParams != "") {
			v.push('"customParams":"' + this.customParams + '"');
		}
		if(this.expectCount) {
			v.push('"expectCount":' + this.expectCount);
		}
		if(this.startRecord) {
			v.push('"startRecord":' + this.startRecord);
		}
		if(this.returnResultSetInfo) {
			v.push('"returnResultSetInfo":' + this.returnResultSetInfo);
		}
		if(this.networkType) {
			v.push('"networkType":' + this.networkType);
		}
		if(this.queryLayerParams && this.queryLayerParams.length > 0) {
			var vLayerParams = [];
			for(var i = 0; i < this.queryLayerParams.length; i++) {
				vLayerParams.push(this.queryLayerParams[i].toJSON());
			}
			v.push('"queryLayerParams":' + "[" + vLayerParams.join(", ") + "]");
		}
		json += v.join(", ");
		json += '}'

		return json;
	}
};
SuperMap.QueryParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 QueryParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.QueryParam">&lt;see cref="T:SuperMap.QueryParam"&gt;QueryParam&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var queryParam = new SuperMap.QueryParam();
    queryParam.customParams = jsonObject.customParams;
    queryParam.expectCount = jsonObject.expectCount;
	queryParam.startRecord = jsonObject.startRecord;
    if(jsonObject.queryLayerParams != null){
        if(!queryParam.queryLayerParams){
			queryParam.queryLayerParams = new Array();
		}
        for(var i = 0; i < jsonObject.queryLayerParams.length; i++){
            if(jsonObject.queryLayerParams[i]){
                queryParam.queryLayers[i] = SuperMap.QueryLayerParam.fromJson(jsonObject.queryLayers[i]);
            }
        }
    }
	
	return queryParam;
};

SuperMap.QueryParam.registerClass('SuperMap.QueryParam');

SuperMap.DataSourceInfo = function(){
	/// <summary>数据源信息类。该类主要描述数据源的基本信息，如名称（dataSourceName）。</summary>
	/// <returns type="SuperMap.DataSourceInfo">返回 DataSourceInfo 对象。</returns>
	/// <field name="dataSourceName"  type="String">数据源名称。  </field>
	this.dataSourceName = null;
};
SuperMap.DataSourceInfo.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.dataSourceName = null;
	}
};
SuperMap.DataSourceInfo.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 DataSourceInfo 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.DataSourceInfo">&lt;see cref="T:SuperMap.DataSourceInfo"&gt;DataSourceInfo&lt;/see&gt;对象。</returns>
	if(!jsonObject) return;
	var dataSourceInfo = new SuperMap.DataSourceInfo();
	dataSourceInfo.dataSourceName = jsonObject.dataSourceName;
	
	return dataSourceInfo;
};
SuperMap.DataSourceInfo.registerClass('SuperMap.DataSourceInfo');

SuperMap.DatasetInfo = function(){
	/// <summary>数据集信息类。该类主要描述数据集的基本信息，如名称（datasetName），
	/// 数据集的类型（datasetType），数据集所属的数据源名称（datasourceName）。</summary>
	/// <returns type="SuperMap.DatasetInfo">返回 DatasetInfo 对象。</returns>
	/// <field name="datasetName"  type="String">数据集名称。  </field>
	/// <field name="datasetType" type="Number">数据集类型。  </field>
	/// <field name="datasourceName"  type="String">数据源名称。  </field>
	/// <field name="bounds" type="SuperMap.Rect2D">数据集中包含所有对象的最小外接矩形。  </field>
	this.datasetName = null;
	this.datasetType = null;
	this.datasourceName = null; 
	this.bounds = null;
};
SuperMap.DatasetInfo.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.datasetName = null;
		this.datasetType = null;
		this.datasourceName = null;
		if(this.bounds) {
			this.bounds.dispose();
		}
		this.bounds = null;
	}
};
SuperMap.DatasetInfo.fromJson = function(jsonObject) {
	/// <summary>将JSON对象转换为 DatasetInfo 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.DatasetInfo">&lt;see cref="T:SuperMap.DatasetInfo"&gt;DatasetInfo&lt;/see&gt;对象。</returns>
	if(!jsonObject) return;
	var datasetInfo = new SuperMap.DatasetInfo();
	datasetInfo.datasourceName = jsonObject.datasourceName;
	datasetInfo.datasetName = jsonObject.datasetName;
	datasetInfo.datasetType = jsonObject.datasetType;
	datasetInfo.bounds = SuperMap.Rect2D.fromJson(jsonObject.bounds);
	
	return datasetInfo;
};
SuperMap.DatasetInfo.registerClass('SuperMap.DatasetInfo');

SuperMap.DatasetType = function(){
	/// <summary>数据集类型枚举。该类定义了目前支持的数据集的类型，并以数值表示。&lt;br&gt;
	/// 数据集一般为存储在一起的相关数据的集合；根据数据类型的不同，分为矢量数据集和栅格数据集，
	/// 以及为了处理特定问题而设计的如拓扑数据集，网络数据集等。根据要素的空间特征的不同，
	/// 矢量数据集又分为点数据集，线数据集，面数据集等；栅格数据集包括影像数据集和格网数据集。
	/// </summary>
	/// <returns type="SuperMap.DatasetType">返回 DatasetType 对象。</returns>
	/// <field name="undefined" type="Number"   static="true">未定义。默认值为-1。  </field>
	/// <field name="point"  type="Number"  static="true">点数据集。默认值为1。&lt;br&gt;
	/// 用于存储点对象的数据集，例如离散点的分布。&lt;br&gt;
	///  &lt;img src="../img/Dstype2.png" /&gt; 
	/// </field>
	/// <field name="line" type="Number"   static="true">线数据集。默认值为3。 &lt;br&gt;
	/// 用于存储线对象的数据集，例如河流、道路、国家边界线的分布。&lt;br&gt;
	///  &lt;img src="../img/DSType3.png" /&gt; 
	/// </field>
	/// <field name="region"  type="Number"  static="true">面数据集。默认值为5。&lt;br&gt;   
	/// 用于存储数据集类型相同的面对象，例如表示房屋的分布、国家区域等。&lt;br&gt;
	///  &lt;img src="../img/DSType4.png" /&gt; 
	/// </field>
	/// <field name="text" type="Number"   static="true">文本数据集。默认值为7。&lt;br&gt;
	/// 用于存储文本对象的数据集，那么文本数据集中只能存储文本对象，而不能存储其他几何对象。例如表示注记的文本对象。&lt;br&gt;
	///  &lt;img src="../img/DSType5.png" /&gt; 
	/// </field>
	/// <field name="network"  type="Number"  static="true">网络数据集。默认值为4。&lt;br&gt;
	/// 网络数据集是用于存储具有网络拓扑关系的数据集。如道路交通网络等。&lt;br&gt;
	/// 1. 网络数据集和点数据集、线数据集不同，它既包含了网络线对象，也包含了网络结点对象，还包含了两种对象之间的空间拓扑关系。&lt;br&gt;
	/// 2. 基于网络数据集，可以进行路径分析、服务区分析、最近设施查找、资源分配、选址分区、公交换乘以及邻接点、
	/// 通达点分析等多种网络分析。&lt;br&gt;
	///  &lt;img src="../img/DSType8.png" /&gt; 
	/// </field>
	/// <field name="grid" type="Number"   static="true">格网数据集。默认值为83。&lt;br&gt;
	/// 属于栅格数据类型，
	/// 例如高程数据集和土地利用图。其中每一个栅格存储的是表示地物的属性值（例如高程值）。 &lt;br&gt;
	///  &lt;img src="../img/DSType10.png" /&gt; 
	/// </field>
	/// <field name="image"    static="true">影像数据集。默认值为81。&lt;br&gt;
	/// 属于栅格数据类型，不具备属性信息，例如影像地图、多波段影像和实物地图等。其中每一个栅格存储的是一个颜色值或颜色的索引值（RGB值）。&lt;br&gt;
	///  &lt;img src="../img/DSType9.png" /&gt; 
	/// </field>
	/// <field name="cad"  type="Number"  static="true">CAD数据集。默认值为149。&lt;br&gt;
	/// 指可以存储多种几何对象的数据集，即用来存储点、线、面、文本等不同类型的对象的集合CAD数据集中各对象可以有不同的风格，CAD数据集为每个对象存储风格。&lt;br&gt;
	///  &lt;img src="../img/DSType6.png" /&gt; 
	/// </field>
	/// <field name="linem"  type="Number"  static="true">路由数据集。默认值为35。 &lt;br&gt;
	/// 在进行网络分析的时候，因为要标识事件发生的度量值（Measure Value），路由只能用路由数据集，即 LineM 类。
	/// &lt;img src="../img/DSType12.png" /&gt; 
	///   </field>
	/// <field name="tabular"  type="Number"  static="true">纯属性数据集。默认值为0。&lt;br&gt;
	/// 用于存储和管理纯属性数据，纯属性数据用来描述地形地物特征、形状等信息，如河流的长度、宽度等。该数据集没有空间图形数据。即纯属性数据集不能作为图层被添加到地图窗口中显示。&lt;br&gt;
	///  &lt;img src="../img/Attribute.png" /&gt; 
	/// </field>
	this.undefined = -1;
	this.point = 1;
	this.line = 3;
	this.region = 5;
	this.text = 7;
	this.network = 4;
	this.grid = 83;
	this.image = 81;
	this.cad = 149;
	this.linem = 35;
	this.tabular = 0;
};
SuperMap.DatasetType.registerClass('SuperMap.DatasetType');

SuperMap.ReturnResultSetInfo = function(){
	/// <summary>该类定义了查询后返回结果的类型。&lt;br&gt;
	/// 查询返回的结果可以分为三个类型，仅返回属性信息，仅返回空间信息，属性和空间信息都返回。
	/// </summary>
	/// <returns type="SuperMap.ReturnResultSetInfo">返回 ReturnResultSetInfo 对象。</returns>
	/// <field name="returnAttribute" type="Number" static="true">仅返回属性信息。默认值为0。
	/// </field>
	/// <field name="returnGeometry" type="Number" static="true">仅返回空间信息。默认值为1。   </field>
	/// <field name="returnAttributeAndGeometry" type="Number" static="true">既返回空间信息也返回属性信息。默认值为2。
	/// </field>
	this.returnAttribute = 0;
	this.returnGeometry = 1;
	this.returnAttributeAndGeometry = 2;
};
SuperMap.ReturnResultSetInfo.registerClass('SuperMap.ReturnResultSetInfo');

SuperMap.ResultSet = function(){
	/// <summary>查询结果类。查询的所有返回结果全部通过 ResultSet 对象进行操作。</summary>
	/// <returns type="SuperMap.ResultSet">返回 ResultSet 对象。</returns>
	/// <field name="currentCount" type="Number">当次查询返回的记录数。
	/// </field>
	/// <field name="customResponse">自定义操作处理的结果。</field>
	/// <field name="recordSets" type="Array" elementType="SuperMap.RecordSet">查询记录集 Recordset 数组。
	/// </field>
	/// <field name="totalCount" type="Number">根据查询条件查询到的记录的总数。
	/// </field>
    this.currentCount = 0;
    this.customResponse = ""; // 自定义处理结果。  
    this.recordSets = null;   // 查询记录集。
    this.totalCount = 0;      // 查询返回记录的总数。
};
SuperMap.ResultSet.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.recordSets){
			while(this.recordSets.length > 0){
				this.recordSets.pop().dispose();
			}
		}
		this.recordSets = null;
	}
};
SuperMap.ResultSet.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ResultSet 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ResultSet">&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var resultSet = new SuperMap.ResultSet(); 
    resultSet.currentCount = jsonObject.currentCount;
    resultSet.customResponse = jsonObject.customResponse;
    if(jsonObject.recordSets){
		resultSet.recordSets = new Array();
	}
    for(var i = 0; jsonObject.recordSets && i < jsonObject.recordSets.length; i++){
        if(jsonObject.recordSets[i]){
			resultSet.recordSets[i] = SuperMap.RecordSet.fromJson(jsonObject.recordSets[i]);
		}
    }
    resultSet.totalCount = jsonObject.totalCount;

	return resultSet;
};
SuperMap.ResultSet.registerClass('SuperMap.ResultSet');

SuperMap.RecordSet = function(){
	/// <summary>查询记录集对象。将查询出来的几何对象按照图层进行划分，
	/// 一个查询记录集分别存放一个图层的查询结果。 
	/// </summary>
	/// <returns type="SuperMap.RecordSet">返回 RecordSet 对象。</returns>
	/// <field name="layerId" type="Number">图层唯一标识。 
	/// </field>
	/// <field name="layerName" type="String">结果集中的几何对象所在的图层名称
	/// </field>
	/// <field name="records" type="Array" elementType="SuperMap.Record"> 结果集。SuperMap.Record 数组。 </field>
	/// <field name="returnFields" type="Array" elementType="String">返回的字段名称数组。  
	/// </field>
	/// <field name="returnFieldCaptions" type="Array" elementType="String">返回字段的 Caption 数组。  
	/// </field>
    this.layerId = 0;			// 图层唯一标识。  
    this.layerName = "";		// 结果集中的几何对象所在的图层名称
    this.records = null;		// 结果集。SuperMap.Record 数组。
    this.returnFields = null; // 返回的字段名称数组。  
    this.returnFieldCaptions = null; // 返回字段的 Caption 数组。  
};
SuperMap.RecordSet.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.records){
			while(this.records.length > 0){
				this.records.pop().dispose();
			}
		}
		this.records = null;
	}
};
SuperMap.RecordSet.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 RecordSet 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.RecordSet">&lt;see cref="T:SuperMap.RecordSet"&gt;RecordSet&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
    var recordSet = new SuperMap.RecordSet(); 
	recordSet.layerId = jsonObject.layerId;
    recordSet.layerName = jsonObject.layerName;
    if(jsonObject.records){
		recordSet.records = new Array();
	}
    for(var i = 0; jsonObject.records && i < jsonObject.records.length; i++){
        if(jsonObject.records[i]){
			recordSet.records[i] = SuperMap.Record.fromJson(jsonObject.records[i]);
		}
    }
    recordSet.returnFields = jsonObject.returnFields;
    recordSet.returnFieldCaptions = jsonObject.returnFieldCaptions;

	return recordSet;
};
SuperMap.RecordSet.registerClass('SuperMap.RecordSet');

SuperMap.Record = function(){
	/// <summary>存放空间对象信息的记录。 &lt;br&gt;
	/// 1. 一般查询等操作返回的符合条件的几何对象都以记录集的形式放在 Record 中，
	/// 查询出多少空间对象就有多少条 Record 记录。 &lt;br&gt;
	/// 2. 在 Record 中记录了空间几何对象的中心点位置 Center。 
	/// 同时还包括空间对象的属性信息集合 FieldValues。&lt;br&gt; 
	/// 3. Record 和 Recordset 的关系： &lt;br&gt;
	/// Recordset 中包含 Record。一个 Recordset 中包括属性字段的列表，还包括 Record 的集合。
	/// 在进行查询的时候，对于每一个图层，符合条件的每一个空间对象的记录（包括需要查询的属性信息）
	/// 都分别存放在单独的 Record 中，所有这些 Record 集合在一起，与该图层返回的属性字段名称的集合共同构
	/// 成一个 Recordset。
	/// </summary>
	/// <returns type="SuperMap.Record">返回 Record 对象。</returns>
	/// <field name="center" type="SuperMap.Point2D"> 实体的中心点坐标。SuperMap.Point2D 类型。 </field>
	/// <field name="fieldValues" type="Array" elementType="String">实体的属性信息集合。String 数组。
	/// </field>
	/// <field name="shape" type="SuperMap.Geometry">实体的几何形状。
	/// </field>
    this.center = null;		// 实体的中心点坐标。  MapCoord.
    this.fieldValues = null;  // 实体的属性信息集合。  string[].
	this.shape = null;	//实体的几何形状。  Geometry.
};
SuperMap.Record.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.center = null;
		this.shape = null;
	}
};
SuperMap.Record.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 Record 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Record">&lt;see cref="T:SuperMap.Record"&gt;Record&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var record = new SuperMap.Record();
	if(jsonObject.center){
		record.center = SuperMap.Point2D.fromJson(jsonObject.center);
		record.center.x = jsonObject.center.x;
		record.center.y = jsonObject.center.y;
	}
	if(jsonObject.shape) {
		record.shape = SuperMap.Geometry.fromJson(jsonObject.shape);
	}
    record.fieldValues = jsonObject.fieldValues;

	return record;
};
SuperMap.Record.registerClass('SuperMap.Record');

SuperMap.EventArguments = function(param, error, e){
	/// <summary>为DOM所触发的事件（如onclick事件等）提供数据。</summary>
	/// <param name="param">地图参数。</param>
	/// <param name="error">错误信息。如果没有错误信息，则该参数的值为""。</param>
	/// <param name="e">浏览器事件参数。如果当前事件不是浏览器事件，则该参数为null。</param>
	/// <returns type="SuperMap.EventArguments">返回 EventArguments 对象。</returns>
	/// <field name="param">地图参数。</field>
	/// <field name="error">错误信息。如果没有错误信息，则该参数的值为""。</field>
	/// <field name="e">浏览器事件参数。如果当前事件不是浏览器事件，则该参数为null。</field>
    this.param = param;   // 地图参数。
    this.error = error;   // 错误信息。如果没有错误信息，则该参数的值为""。
    this.e = e;           // 浏览器事件参数。如果当前事件不是浏览器事件，则该参数为null。
};
SuperMap.EventArguments.registerClass('SuperMap.EventArguments');

SuperMap.PathParam = function(){
	/// <summary>路径分析参数。路径分析功能必选参数。可以通过PathParam的networkAnalystParam来设置路径分析需要的各种参数。</summary>
	/// <returns type="SuperMap.PathParam">返回 PathParam 对象。</returns>
	/// <field name="networkAnalystParam" type="SuperMap.NetworkAnalystParam">网络参数信息，用于设置网络中各种参数，包括障碍边、障碍点等。 </field>
	/// <field name="hasLeastEdgeCount" type="Boolean">是否弧段数最少。默认值为false。</field>
	this.networkAnalystParam = null;			//网络分析设置参数
	this.hasLeastEdgeCount = false;
};
SuperMap.PathParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.networkAnalystParam){
			this.networkAnalystParam.dispose();
			this.networkAnalystParam = null;
		}
	}
};
SuperMap.PathParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 PathParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.PathParam">&lt;see cref="T:SuperMap.PathParam"&gt;PathParam&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var pathParam = new SuperMap.PathParam();
	if(jsonObject.networkAnalystParam){
    pathParam.networkAnalystParam = SuperMap.NetworkAnalystParam.fromJson(jsonObject.networkAnalystParam);
	}
	pathParam.hasLeastEdgeCount = jsonObject.hasLeastEdgeCount;
	return pathParam;
};
SuperMap.PathParam.registerClass('SuperMap.PathParam');

SuperMap.NetworkAnalystParam = function() {
	/// <summary>网络分析参数类，该类用于设置执行网络分析时需要的相关参数，包括设置分析时所用的障碍边、障碍点列表，分析结果是否要包含路由对象集合等。 </summary>
	/// <returns type="SuperMap.NetworkAnalystParam">返回 NetworkAnalystParam 对象。</returns>
	/// <field name="weightName" type="String">权重信息的名字标识。</field>
	/// <field name="nodeIDs" type="Array" elementType="Number">分析时途经结点 ID 的集合。 </field>
	/// <field name="point2Ds" type="Array" elementType="SuperMap.Point2D">分析时途经点的集合。 SuperMap.Point2D 数组类型。</field>
	/// <field name="barrierNodes" type="Array" elementType="Number">障碍结点 ID 列表。 </field>
	/// <field name="barrierEdges" type="Array" elementType="Number">障碍弧段 ID 列表。 </field>
	/// <field name="turnWeightField" type="String">转向权重字段。 </field>
	/// <field name="isNodesReturn" type="Boolean">分析结果中是否包含分析途经的结点集合。</field>
	/// <field name="isEdgesReturn" type="Boolean">分析结果中是否包含经过弧段集合。</field>
	/// <field name="isPathGuidesReturn" type="Boolean">分析结果中是否包含行驶导引集合。 </field>
	/// <field name="isPathsReturn" type="Boolean">分析结果中是否包含路由对象的集合。 </field>
	/// <field name="isStopsReturn" type="Boolean">分析结果中是否包含站点的集合。 </field>
	this.weightName = "";
	this.nodeIDs = null;
	this.point2Ds = null;
	this.barrierNodes = null;
	this.barrierEdges = null;
	this.turnWeightField = "";
	this.isNodesReturn = false;
	this.isEdgesReturn = false;
	this.isPathGuidesReturn = false;
	this.isPathsReturn = false;
	this.isStopsReturn = false;
};
SuperMap.NetworkAnalystParam.prototype = {
	
	dispose:function() {
	/// <summary>释放对象所占用的资源。</summary>
		if(this.nodeIDs) {
			while(this.nodeIDs.length > 0) {
				this.nodeIDs.pop();
			}
			this.nodeIDs = null;
		}
		
		if(this.point2Ds) {
			while(this.point2Ds.length > 0) {
				this.point2Ds.pop();
			}
			this.point2Ds = null;
		}
		
		if(this.barrierNodes) {
			while(this.barrierNodes.length > 0) {
				this.barrierNodes.pop();
			}
			this.barrierNodes = null;
		}
		
		
		if(this.barrierEdges) {
			while(this.barrierEdges.length > 0) {
				this.barrierEdges.pop();
			}
			this.barrierEdges = null;
		}
	}
};
SuperMap.NetworkAnalystParam.fromJson = function(jsonObject) {
	/// <summary>将JSON对象转换为 NetworkAnalystParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.NetworkAnalystParam">&lt;see cref="T:SuperMap.NetworkAnalystParam"&gt;NetworkAnalystParam&lt;/see&gt;对象。</returns>
	if(!jsonObject) {
		return;
	}
	var param = new SuperMap.NetworkAnalystParam();
	param.weightName = jsonObject.weightName;
	
	if(jsonObject.nodeIDs) {
		param.nodeIDs = new Array();
		for(var i = 0; i < jsonObject.nodeIDs.length; i++) {
			param.nodeIDs.push(jsonObject.nodeIDs[i]);
		}
	}
	
	if(jsonObject.point2Ds) {
		param.point2Ds = new Array();
		for(var i = 0; i < jsonObject.point2Ds.length; i++) {
			param.point2Ds.push(SuperMap.Point2D.fromJson(jsonObject.point2Ds[i]));
		}
	}
	
	if(jsonObject.barrierNodes) {
		param.barrierNodes = new Array();
		for(var i = 0; i < jsonObject.barrierNodes.length; i++) {
			param.barrierNodes.push(jsonObject.barrierNodes[i]);
		}
	}
	
	if(jsonObject.barrierEdges) {
		param.barrierEdges = new Array();
		for(var i = 0; i < jsonObject.barrierEdges.length; i++) {
			param.barrierEdges.push(jsonObject.barrierEdges[i]);
		}
	}
	
	param.turnWeightField = jsonObject.turnWeightField;
	param.isNodesReturn = jsonObject.isNodesReturn;
	param.isEdgesReturn = jsonObject.isEdgesReturn;
	param.isPathGuidesReturn = jsonObject.isPathGuidesReturn;
	param.isPathsReturn = jsonObject.isPathsReturn;
	param.isStopsReturn = jsonObject.isStopsReturn;
};
SuperMap.NetworkAnalystParam.registerClass('SuperMap.NetworkAnalystParam');

SuperMap.NetworkAnalystResult = function() {
	/// <summary>网络分析返回的结果类。 &lt;br&gt;
	/// 1. 该类可以返回分析结果的路由集合、分析途经的结点集合以及弧段集合、行驶导引集合、站点集合和阻力值集合等。&lt;br&gt;
	/// 2. 用户可以通过该类灵活得到路经分析、旅行商分析、最近设施查找等分析的结果。
	/// </summary>
	/// <returns type="SuperMap.NetworkAnalystResult">返回 NetworkAnalystResult 对象。</returns>
	/// <field name="paths" type="Array" elementType="SuperMap.Geometry">分析结果的路由对象集合。  </field>
	/// <field name="nodes" type="Array" elementType="Number"> 分析结果的途经结点 ID 的集合。分析结果的途经结点 ID 的集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的结点 ID。 </field>
	/// <field name="edges" type="Array" elementType="Number">分析结果的途经弧段集合。分析结果的途经结点 ID 的集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的弧段。 </field>
	/// <field name="pathGuides" type="Array" elementType="SuperMap.PathGuide">行驶导引集合。  </field>
	/// <field name="stops" type="Array" elementType="Number"> 站点 ID 的集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的站点 ID。 </field>
	/// <field name="weights" type="Array" elementType="Number">阻力值的集合。Double 类型数组。  </field>
	/// <field name="message" type="String">路径分析过程中出现的异常信息，失败信息。 </field>
	this.paths = null; // Geometry[]
	this.nodes = null; // int[][]	
	this.edges = null; // int[][]
	this.pathGuides = null; // PathGuide[]
	this.stops = null; // int[][]
	this.weights = null; // double[]
	this.message = "";  // 异常信息，失败信息
};
SuperMap.NetworkAnalystResult.prototype = {
	dispose:function() {
		/// <summary>释放对象所占用的资源。</summary>
		if(this.paths) {
			while(this.paths.length > 0) {
				this.paths.pop().dispose();
			}
		}
		this.paths = null;
		
		if(this.pathGuides) {
			while(this.pathGuides.length > 0) {
				this.pathGuides.pop().dispose();
			}
		}
		this.pathGuides = null;
		if(this.nodes) {
			while(this.nodes.length > 0) {
				var intArray = this.nodes.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.nodes = null;
		
		if(this.edges) {
			while(this.edges.length > 0) {
				var intArray = this.edges.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.edges = null;
		
		if(this.stops) {
			while(this.stops.length > 0) {
				var intArray = this.stops.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.stops = null;
		
		if(this.weights) {
			while(this.weights.length > 0) {
				this.weights.pop();
			}
		}
		this.weights = null;
		this.message = null;
	}
};
SuperMap.NetworkAnalystResult.fromJson = function(jsonObject) {
	/// <summary>将JSON对象转换为 NetworkAnalystResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.NetworkAnalystResult">&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt;对象。</returns>
	if(!jsonObject) {
		return;
	}
	
	var result = new SuperMap.NetworkAnalystResult();
	if(jsonObject.paths) {
		result.paths = new Array();
		for(var i = 0; i < jsonObject.paths.length; i++) {
			result.paths.push(SuperMap.Geometry.fromJson(jsonObject.paths[i]));
		}
	}
	
	if(jsonObject.pathGuides) {
		result.pathGuides = new Array();
		for(var i = 0; i < jsonObject.pathGuides.length; i++) {
			result.pathGuides.push(SuperMap.PathGuide.fromJson(jsonObject.pathGuides[i]));
		}
	}
	
	if(jsonObject.nodes) {
		result.nodes = new Array();
		for(var i=0;i<jsonObject.nodes.length;i++){
			result.nodes[i] = new Array();
			for(var j=0;j<jsonObject.nodes[i].length;j++){
				result.nodes[i][j] = jsonObject.nodes[i][j];
			}
		}
	}
	
	if(jsonObject.edges) {
		result.edges = new Array();
		for(var i=0;i<jsonObject.edges.length;i++){
			result.edges[i] = new Array();
			for(var j=0;j<jsonObject.edges[i].length;j++){
				result.edges[i][j] = jsonObject.edges[i][j];
			}
		}
	}
	
	if(jsonObject.stops) {
		result.stops = new Array();
		for(var i=0;i<jsonObject.stops.length;i++){
			result.stops[i] = new Array();
			for(var j=0;j<jsonObject.stops[i].length;j++){
				result.stops[i][j] = jsonObject.stops[i][j];
			}
		}
	}
	
	if(jsonObject.weights) {
		result.weights = new Array();
		for(var i=0;i<jsonObject.weights.length;i++){
			result.weights[i] = jsonObject.weights[i];
		}
	}
	result.message = jsonObject.message; 
	
	return result;
};
SuperMap.NetworkAnalystResult.registerClass('SuperMap.NetworkAnalystResult');

SuperMap.PathGuide = function() {
	/// <summary>行驶导引类型。&lt;br&gt;
	/// 行驶导引记录了从起点到终点的行驶路线。行驶导引由行驶导引子项构成，每一个导引子项可以表示一个弧段，
	/// 一个结点或一个路由点。&lt;br&gt;
	/// 通过本类型可以返回行使导引对象中子项的个数以及根据序号获取行驶导引中的导引子
	/// 项对象。&lt;br&gt;
	/// 注意：路由点指的是用户输入的用于做路径分析的点。
	/// </summary>
	/// <returns type="SuperMap.PathGuide">返回 PathGuide 对象。</returns>
	/// <field name="items" type="Array" elementType="SuperMap.PathGuideItem">行驶导引记录集合。由若干行驶导引子项构成。每一个导引子项可以表示一个弧段，一个结点或一个路由点。PathGuideItem 数组。</field>
	this.items = null; //PathGuideItem[] ;
};
SuperMap.PathGuide.prototype = {
	dispose:function() {
	/// <summary>释放对象所占用的资源。</summary>
		if(this.items) {
			while(this.items.length > 0) {
				this.items.pop().dispose();
			}
		}
		
		this.items = null;
	}
};
SuperMap.PathGuide.fromJson = function(jsonObject) {
	/// <summary>将JSON对象转换为 PathGuide 对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.PathGuide">&lt;see cref="T:SuperMap.PathGuide"&gt;PathGuide&lt;/see&gt;对象。</returns>
	if(!jsonObject) return;
	
	var pathGuide = new SuperMap.PathGuide();
	if(jsonObject.items) {
		pathGuide.items = new Array();
		for(var i = 0; i < jsonObject.items.length; i++) {
			pathGuide.items.push(SuperMap.PathGuideItem.fromJson(jsonObject.items[i]));
		}
	}
	
	return pathGuide;
};
SuperMap.PathGuide.registerClass('SuperMap.PathGuide');

SuperMap.PathGuideItem = function() {
	/// <summary>行驶导引子项类型。&lt;br&gt;
	/// 行驶导引记录了如何一步步从起点行驶到终点，其中每一步就是一个行驶导引子项。
	/// 利用该类型可以对行驶导引对象的子项进行一些设置，诸如返回子项的 ID、名称、序号、花费等，可以判断
	/// 子项是弧段还是路由点，还可以返回行驶方向、转弯方向等等。
	/// </summary>
	/// <returns type="SuperMap.PathGuideItem">返回 PathGuideItem 对象。</returns>
	/// <field name="id" type="Number">行驶导引对象子项的ID。 </field>
	/// <field name="name" type="String">行驶导引对象子项的名称。  </field>
	/// <field name="index" type="Number">行驶导引对象子项的序号。 </field>
	/// <field name="weight" type="Number">行驶导引对象子项的花费。 </field>
	/// <field name="length" type="Number">行驶导引对象子项为弧段时获取弧段的长度。 </field>
	/// <field name="distance" type="Number">路由点到弧段的距离。&lt;br&gt;
	/// 该距离是指站点到最近一条弧段的距离。如下图所示，桔色点代表网络结点，蓝色代表弧段，灰色点为站点，红色线段代表距离。举例说明，比如用户希望计算出从家里出发乘坐公交车去医院的最短路程，除了计算出公交线路的最短路程外，还需要计算出从家走到公交站的最短路程，这个路程就是 distance 字段的值。&lt;br&gt;
	/// &lt;img src ="../img/PathGuideItemDistance.png"&gt;
	/// </field>
	/// <field name="directionType"  type="Number">返回行驶方向。共有五个方向，即东、南、西、北、无方向。当 IsEdge 值为 False 时，行驶方向返回的类型为无方向。 </field>
	/// <field name="turnType"  type="Number">转弯方向。单位为度，精确到0.1度。 </field>
	/// <field name="sideType" type="Number">SideType类型，是在路的左侧、右侧还是在路上。</field>
	/// <field name="turnAngle" type="Number">转弯角度。 </field>
	/// <field name="isEdge" type="Boolean">是否是弧段。 </field>
	/// <field name="isStop" type="Boolean">是否为路由点。 </field>
	/// <field name="bounds" type="SuperMap.Rect2D">对象的范围，对弧段而言，为弧段的外接矩形；对点而言，为点本身。 </field>
	this.id = -1;
	this.name = "";
	this.index = -1;
	this.weight = 0.0;
	this.length = 0.0;
	this.distance = 0.0;
	this.directionType; //DirectionType
	this.turnType; //TurnType
	this.sideType; //SideType
	this.turnAngle = 0.0;
	this.isEdge = false;
	this.isStop = false;
	this.bounds; // Rect2D
};
SuperMap.PathGuideItem.prototype = {
	dispose:function() {
	/// <summary>释放对象所占用的资源。</summary>
	}
};
SuperMap.PathGuideItem.fromJson = function(jsonObject) {
	/// <summary>将JSON对象转换为 PathGuideItem 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.PathGuideItem">&lt;see cref="T:SuperMap.PathGuideItem"&gt;PathGuideItem&lt;/see&gt;对象。</returns>
	if(!jsonObject) return;
	var item = new SuperMap.PathGuideItem();
	item.id = jsonObject.id;
	item.name = jsonObject.name;
	item.index = jsonObject.index;
	item.weight = jsonObject.weight;
	item.length = jsonObject.length;
	item.distance = jsonObject.distance;
	item.directionType = jsonObject.directionType; //DirectionType
	item.turnType = jsonObject.turnType; //TurnType
	item.sideType = jsonObject.sideType; //SideType
	item.turnAngle = jsonObject.turnAngle;
	item.isEdge = jsonObject.isEdge;
	item.isStop = jsonObject.isStop;
	if(jsonObject.bounds) {
		item.bounds = SuperMap.Rect2D.fromJson(jsonObject.bounds); // Rect2D
	}
	return item;
};
SuperMap.PathGuideItem.registerClass('SuperMap.PathGuideItem');

SuperMap.ServiceAreaParam = function(){
	/// <summary>服务区分析参数对象。</summary>
	/// <returns type="SuperMap.ServiceAreaParam">返回 ServiceAreaParam 对象。</returns>
	/// <field name="networkAnalystParam" type="SuperMap.NetworkAnalystParam"> 设置网络参数，用于设置网络中各种参数，包括障碍边、障碍点等。 </field>
	/// <field name="weights" elementType="Number" type="Array">阻力字段值的集合。</field>
	/// <field name="isFromCenter" type="Boolean"> 是否从中心查找，默认为 True。 </field>
	/// <field name="isCenterMutuallyExclusive" type="Boolean">是否中心点互斥。为true，表示若两个或多个相邻的服务区有交集，则将它们进行互斥处理。 </field>
	this.networkAnalystParam;
	this.weights = null; // double[]
	this.isFromCenter = false;
	this.isCenterMutuallyExclusive = false;
};
SuperMap.ServiceAreaParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.networkAnalystParam.dispose();
		if(this.weights) {
			for(var i = 0; i < this.weights.length; i++) {
				this.weights.pop();
			}
		}
		this.weights = null;
	}
};
SuperMap.ServiceAreaParam.registerClass('SuperMap.ServiceAreaParam');

SuperMap.ServiceAreaResult = function(){
	/// <summary>服务区分析结果对象。</summary>
	/// <returns type="SuperMap.ServiceAreaResult">返回 ServiceAreaResult 对象。</returns>
	/// <field name="areaRegions" type="Array" elementType="SuperMap.Geometry">分析结果的服务区面对象集合。 </field>
	/// <field name="lines" type="Array" elementType="SuperMap.Geometry">分析结果的路由对象集合。 </field>
	/// <field name="nodes" type="Array" elementType="Number">分析结果的途经结点 ID 的集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的结点 ID。 </field>
	/// <field name="edges" type="Array" elementType="Number">分析结果的途经弧段集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的弧段。</field>
	/// <field name="pathGuides" type="Array" elementType="SuperMap.PathGuide">行驶导引集合。 </field>
	/// <field name="stops" type="Array" elementType="Number">站点 ID 的集合。该字段是二维数组，第一维表示返回的路径，第二维表示每条路径途径的站点 ID。</field>
	/// <field name="weights"  type="Array" elementType="Number">阻力字段值的集合。Double类型的数组。</field>
	/// <field name="message" type="String">服务区分析过程中出现的异常信息，失败信息。 </field>
	this.areaRegions = null; // Geometry[]
	this.lines; // Geometry[]
	this.nodes; // int[][]	
	this.edges; // int[][]
	this.pathGuides; // PathGuide[]
	this.stops; // int[][]
	this.weights; // double[]
	this.message;  // 异常信息，失败信息
};
SuperMap.ServiceAreaResult.prototype={
	dispose:function(){	
	/// <summary>释放对象所占用的资源。</summary>
		if(this.areaRegions) {
			for(var i = 0; i < this.areaRegions; i++){
				var geometry = this.areaRegions.pop();
				geometry.dispose();
				geometry = null;
			}
		}
		this.areaRegions = null;
		if(this.lines) {
			while(this.lines.length > 0) {
				this.lines.pop().dispose();
			}
		}
		this.lines = null;
		
		if(this.pathGuides) {
			while(this.pathGuides.length > 0) {
				this.pathGuides.pop().dispose();
			}
		}
		this.pathGuides = null;
		if(this.nodes) {
			while(this.nodes.length > 0) {
				var intArray = this.nodes.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.nodes = null;
		
		if(this.edges) {
			while(this.edges.length > 0) {
				var intArray = this.edges.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.edges = null;
		
		if(this.stops) {
			while(this.stops.length > 0) {
				var intArray = this.stops.pop();
				if(intArray) {
					while(intArray.length > 0) {
						intArray.pop();
					}
				}
			}
		}
		this.stops = null;
		
		if(this.weights) {
			while(this.weights.length > 0) {
				this.weights.pop();
			}
		}
		this.weights = null;
		this.message = null;
	}
};
SuperMap.ServiceAreaResult.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ServiceAreaResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ServiceAreaResult">&lt;see cref="T:SuperMap.ServiceAreaResult"&gt;ServiceAreaResult&lt;/see&gt;对象。</returns>
	if(jsonObject == null) {
		return;
	}
	var result = new SuperMap.ServiceAreaResult();
	if(jsonObject.areaRegions) {
		result.areaRegions = new Array();
		for(var i = 0; i < jsonObject.areaRegions.length; i++) {
			result.areaRegions.push(SuperMap.Geometry.fromJson(jsonObject.areaRegions[i]));
		}
		for(var j = 0; j < jsonObject.areaRegions.length; j++) {
			var geometry = jsonObject.areaRegions.pop();
			geometry = null;
		}
	}
	if(jsonObject.lines) {
		result.lines = new Array();
		for(var i = 0; i < jsonObject.lines.length; i++) {
			result.lines.add(SuperMap.Geometry.fromJson(jsonObject.lines[i]));
		}
	}
	
	if(jsonObject.pathGuides) {
		result.pathGuides = new Array();
		for(var i = 0; i < jsonObject.pathGuides.length; i++) {
			result.pathGuides.add(SuperMap.PathGuide.fromJson(jsonObject.pathGuides[i]));
		}
	}
	result.nodes = jsonObject.nodes; // int[][]	
	result.edges = jsonObject.edges; // int[][]
	result.stops = jsonObject.stops; // int[][]
	result.weights = jsonObject.weights; // double[]
	result.message = jsonObject.message; 
	jsonObject = null;
	
	return result;
};
SuperMap.ServiceAreaResult.registerClass('SuperMap.ServiceAreaResult');

SuperMap.WeightFieldInfo = function(){
	/// <summary>网络数据集中权值字段信息对象。
	/// </summary>
	/// <returns type="SuperMap.WeightFieldInfo">返回 WeightFieldInfo 对象。</returns>
	/// <field name="name" type="String"> 权值字段信息的名称，默认为"LENGTH"。</field>
	/// <field name="fTWeightField " type="String"> 正向权值字段，默认为"SMLENGTH"。</field>
	/// <field name="tFWeightField" type="String"> 反向权值字段，默认为"SMLENGTH"。</field>
	this.name = "LENGTH";
	this.fTWeightField = "SMLENGTH";
	this.tFWeightField = "SMLENGTH";
};
SuperMap.WeightFieldInfo.fromJson = function(jsonObject){
	/// <summary>将 JSON 对象转换为 WeightFieldInfo 对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.WeightFieldInfo">&lt;see cref="T:SuperMap.WeightFieldInfo"&gt;WeightFieldInfo&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var weightFieldInfo = new SuperMap.WeightFieldInfo();
	weightFieldInfo.name = jsonObject.name;
	weightFieldInfo.fTWeightField = jsonObject.fTWeightField;
	weightFieldInfo.tFWeightField = jsonObject.tFWeightField;
	return weightFieldInfo;
};
SuperMap.WeightFieldInfo.registerClass('SuperMap.WeightFieldInfo');

SuperMap.TurnTableSetting = function(){
	/// <summary>转向表设置对象。 &lt;br&gt;
	/// 通常在网络图层中相交的弧段之间是可以连通的，
	/// 比如AE、BE、CE、DE四个弧段相交于E点，这四个弧段呈十字交叉形状，
	/// （通常可以代表一个十字路口） 如果在没有任何特殊设置的情况下，从A出发到达E结点
	/// 然后可以向任意路口（B点、C点、D点）转弯。但是在现实世界中，往往会有单行线，
	/// 禁行线等的特殊 情况，比如从结点E到C点是禁行的，从结点E到B点可以通行，
	/// 但是通行的耗费比较大（比如堵车等情况），从结点E到 D点可以畅通无阻的通行，
	/// 那么如果从A出发所能通行的线路最优选择就是先到达结点E然后转向ED弧段，
	/// 其次的选择就是到达E结点后转向EB弧段，其他的转弯方向都不可行。
	/// &lt;br&gt; &lt;img src="../img/TurnTableSetting.png" /&gt; 
	/// &lt;br&gt;
	///  为了能够准确的表达出与 现实世界相同规则，在网络分析中专门提供了转向表，
	/// 通过转向表来设置这些特殊规则，如设置AE弧段通过E结点到EB弧段的通达性等。&lt;br&gt; 
	/// 转向表是一个纯二维表格的属性表数据集，每一条记录都表达从一个弧段途径网络结点
	/// 到达相连的另一弧段的相关信息。其中该转向表需要有一个字段专门表示起始弧段 
	/// （即进入转弯结点前所在的弧段）的标识ID； 需要一个字段来表示从起始弧段和终止的
	/// 弧段相交的转弯结点（网络结点）的标识ID（在网络图层的子结点数据层）；需要一个字
	/// 段来表示与起始弧段相连的终止弧段 （即进入转弯结点前所在的弧段）的标识ID；
	///  最后还需要一个字段来表示从起始弧段到终止弧段转向的耗费权重，该字段为数值型，
	/// 并规定正数表示可以转向，数值越大表示从起始弧段到终止弧段的转向耗费越大；
	///  负值表示不能转向。 
	/// </summary>
	/// <returns type="SuperMap.TurnTableSetting">返回 TurnTableSetting 对象。</returns>
	/// <field name="turnDatasetName" type="String"> 转向表数据集的名字。该数据集应当是一个 Tabular 类型的数据集。 </field>
	/// <field name="turnDataSourceName" type="String"> 转向表数据源的名字。</field>
	/// <field name="turnFromEdgeIDField" type="String"> 转向表中起始弧段（即进入转弯结点前所在的弧段）标识ID对应的字段名称。</field>
	/// <field name="turnNodeIDField" type="String"> 转向表中转弯结点标识ID对应的字段名称。 </field>
	/// <field name="turnToEdgeIDField" type="String">转向表中终止弧段（即离开转弯结点后所在饿弧段）标识ID对应的字段名称。  </field>
	/// <field name="turnWeightFields" type="Array" elementType="String">转向表中从起始弧段到终止弧段转向的耗费权重对应的字段名称数组。  </field>
	this.turnFromEdgeIDField = "";
	this.turnNodeIDField = "";
	this.turnToEdgeIDField = "";
	this.turnWeightFields = null; //String[]
	this.turnDatasetName = "";
	this.turnDataSourceName = "";
};
SuperMap.TurnTableSetting.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 TurnTableSetting 对象。</summary>
	/// <param name="jsonObject">要转换的 JSON 对象。</param>
	/// <returns type="SuperMap.TurnTableSetting">&lt;see cref="T:SuperMap.TurnTableSetting"&gt;TurnTableSetting&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var turnTableSetting = new SuperMap.TurnTableSetting();
	turnTableSetting.turnFromEdgeIDField = jsonObject.turnFromEdgeIDField;
	turnTableSetting.turnNodeIDField = jsonObject.turnNodeIDField;
	turnTableSetting.turnToEdgeIDField = jsonObject.turnToEdgeIDField;
	turnTableSetting.turnDatasetName = jsonObject.turnDatasetName;
	turnTableSetting.turnDataSourceName = jsonObject.turnDataSourceName;
	if(jsonObject.turnWeightFields && jsonObject.turnWeightFields.length>0){
		turnTableSetting.turnWeightFields = new Array();
		for(var i=0;i<jsonObject.turnWeightFields.length;i++){
			turnTableSetting.turnWeightFields.push(jsonObject.turnWeightFields[i]);
		}
	}
	return turnTableSetting;
};

SuperMap.TurnTableSetting.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.turnTableSetting){
			while(this.turnTableSetting.length>0){
				this.turnTableSetting.pop();
			}
			this.turnTableSetting = null;
		}
	}
};
SuperMap.TurnTableSetting.registerClass('SuperMap.TurnTableSetting');

SuperMap.NetworkModelSetting = function(){
	/// <summary>NetworkModelSetting 对象，用于描述网络数据的模型参数设置。&lt;br&gt;
	/// 通常执行网络分析第一步就是对网络数据进行建模，通过 NetworkModelSetting 对象对网络数据的模型的参数进行设置。&lt;br&gt;
	/// 网络数据的模型参数设置会直接影响到分析的结果，网络分析包括路径分析，旅行商分析，服务区分析和最近设施查找。
	/// 网络数据集不同于一般线数据集的是，包含了一个网络结点的子数据集，因此网络数据集拥有两个属性表，一个是记录弧段信息的属性表，一个是记录结点信息的属性表。&lt;br&gt;
	/// NetworkModelSetting可以设置：&lt;br&gt;
	/// 	1. 进行网络分析的数据集（networkDatasetName）；&lt;br&gt;
	/// 	2. 网络中唯一标识结点的字段（NodeIDField），该字段来源于网络结点属性表；&lt;br&gt;
	/// 	3. 唯一标识弧段的字段（edgeIDField），该字段来源于弧段属性表；&lt;br&gt;
	/// 	4. 记录弧段起、终结点 ID 的字段（fNodeIDField 和 tNodeIDField），来源于网络结点属性表；&lt;br&gt;
	/// 	5. 记录要经过某一弧段需要多少权值字段信息（weightFieldInfos），来源于弧段属性表；&lt;br&gt;
	/// 	6. 结点到弧段的距离容限（tolerance）；&lt;br&gt;
	/// 	7. 表示转向表的一系列属性（转向表设置对象）；&lt;br&gt;
	/// 	8. 网络中障碍边和障碍点的 ID 集合（barrierEdges、barrierNodes）。
	/// </summary>
	/// <returns type="SuperMap.NetworkModelSetting">返回 NetworkModelSetting 对象。</returns>
	/// <field name="networkDatasetName" type="String"> 网络数据对应的数据集名称。 </field>
	/// <field name="networkDataSourceName" type="String"> 网络数据对应的数据源名称。 </field>
	/// <field name="tolerance" type="Number">网络模型中的容限参数，默认为100.0。&lt;br&gt;
	/// 原则上，网络分析中可以指定网络中任意的点作为路径分析中的一个路由点，路由点应该在网络附近。如果这个点不在网络上（既不在弧段上也不在结点上），网络分析会根据该距离容限把路由点归结到网络上。 &lt;br&gt;
	/// 如下图所示，桔色点代表网络结点，蓝色代表弧段，灰色点为路由点，红色线段是路由点到弧段AB的距离，如果该距离在设定的距离容限内，则把路由点归结到弧段AB上。&lt;br&gt;
	///  &lt;img src="../img/Tolerance.png" /&gt; 
	/// </field>
	/// <field name="nodeIDField" type="String">网络数据中的节点 ID 对应的字段名。</field>
	/// <field name="edgeIDField" type="String">网络数据集中的弧段 ID 对应的字段名。</field>
	/// <field name="weightFieldInfos" type="Array" elementType="SuperMap.WeightFieldInfo">网络数据集中的权重字段信息数组。</field>
	/// <field name="tNodeIDField" type="String">网络数据集中标志弧段终止结点 ID 的字段名。</field>
	/// <field name="fNodeIDField" type="String">网络数据集中标志弧段起始结点 ID 的字段名。</field>
	/// <field name="nodeNameField" type="String">网络数据集中的节点名称对应的字段名。</field>
	/// <field name="edgeNameField" type="String">网络数据集中的弧段名称对应的字段名。</field>
	/// <field name="barrierEdges" type="Array" elementType="Number">网络数据集中的障碍弧段的 ID 数组。</field>
	/// <field name="barrierNodes" type="Array" elementType="Number">网络数据集中的障碍结点的 ID 数组。</field>
	/// <field name="turnTableSetting" type="SuperMap.TurnTableSetting">转向表设置对象。</field>
	this.networkDatasetName = "";
    this.networkDataSourceName = "";
	this.tolerance = 100.0;
	this.nodeIDField = "SMNODEID";
	this.edgeIDField = "SMID";
	this.weightFieldInfos = null;
	this.tNodeIDField = "SMTNODE";
	this.fNodeIDField = "SMFNODE";
	this.nodeNameField = "";
	this.edgeNameField = "";
	this.barrierEdges = null;
	this.barrierNodes = null;
	this.turnTableSetting = null;
};
SuperMap.NetworkModelSetting.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 NetworkModelSetting 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.NetworkModelSetting">&lt;see cref="T:SuperMap.NetworkModelSetting"&gt;NetworkModelSetting&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var networkModelSetting = new SuperMap.NetworkModelSetting();
   	networkModelSetting.networkDatasetName = networkModelSetting.networkDatasetName;
	networkModelSetting.networkDataSourceName = networkModelSetting.networkDataSourceName;
	networkModelSetting.tolerance = networkModelSetting.tolerance;
	networkModelSetting.nodeIDField = networkModelSetting.nodeIDField;
	networkModelSetting.edgeIDField = networkModelSetting.edgeIDField;
	networkModelSetting.tNodeIDField = networkModelSetting.tNodeIDField;
	networkModelSetting.fNodeIDField = networkModelSetting.fNodeIDField;
	networkModelSetting.nodeNameField = networkModelSetting.nodeNameField;
	networkModelSetting.edgeNameField = networkModelSetting.edgeNameField;
	
	if(jsonObject.weightFieldInfos){
		networkModelSetting.weightFieldInfos = new Array();
		for(var i=0;i<jsonObject.weightFieldInfos.length;i++){
			networkModelSetting.weightFieldInfos.push(SuperMap.WeightFieldInfo.fromJson(jsonObject.weightFieldInfos[i]));
		}
	}
	
	if(jsonObject.barrierEdges){
		networkModelSetting.barrierEdges = new Array();
		for(var i=0;i<jsonObject.barrierEdges.length;i++){
			networkModelSetting.barrierEdges.push(jsonObject.barrierEdges[i]);
		}
	}
	
	if(jsonObject.barrierNodes){
		networkModelSetting.barrierNodes = new Array();
		for(var i=0;i<jsonObject.barrierNodes.length;i++){
			networkModelSetting.barrierNodes.push(jsonObject.barrierNodes[i]);
		}
	}
	
	if(jsonObject.turnTableSetting){
		networkModelSetting.turnTableSetting = SuperMap.TurnTableSetting.fromJson(jsonObject.turnTableSetting);
	}
	
	return networkModelSetting;
};
SuperMap.NetworkModelSetting.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.weightFieldInfos){
			while(this.weightFieldInfos.length > 0) {
				this.weightFieldInfos.pop();
			}
			this.weightFieldInfos=null;
		}
		
		if(this.barrierEdges){
			while(this.barrierEdges.length > 0) {
				this.barrierEdges.pop();
			}
			this.barrierEdges=null;
		}
		
		if(this.barrierNodes){
			while(this.barrierNodes.length > 0) {
				this.barrierNodes.pop();
			}
			this.barrierNodes = null;
		}
		
		if(this.turnTableSetting){
			this.turnTableSetting.dispose();
			this.turnTableSetting=null;
		}
	}
};
SuperMap.NetworkModelSetting.registerClass('SuperMap.NetworkModelSetting');

SuperMap.ClosestFacilityFindingEventArgs = function(){
	/// <summary>为ClosestFacilityFinding事件提供数据。</summary>
	/// <returns type="SuperMap.ClosestFacilityFindingEventArgs">返回 ClosestFacilityFindingEventArgs 对象。</returns>
	/// <field name="clientActionArgs" type="SuperMap.ActionEventArgs">客户端 Action 事件参数。 </field>
	/// <field name="proximityParams" type="SuperMap.ProximityParam">最近设施分析参数。 </field>
	/// <field name="cancel" type="Boolean">指示是否应取消事件的值。 </field>
    this.clientActionArgs = null;
    this.proximityParams = null;
    this.cancel = false;
};
SuperMap.ClosestFacilityFindingEventArgs.prototype={
   	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
            this.clientActionArgs = null;
        }
        if(this.proximityParams){
            this.proximityParams.dispose();
            this.proximityParams = null;
        }
    }
};
SuperMap.ClosestFacilityFindingEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ClosestFacilityFindingEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ClosestFacilityFindingEventArgs">&lt;see cref="T:SuperMap.ClosestFacilityFindingEventArgs"&gt;ClosestFacilityFindingEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.ClosestFacilityFindingEventArgs();
    if(jsonObject.clientActionArgs){
        eventArgs.clientActionArgs = SuperMap.ActionEventArgs.fromJson(jsonObject.clientActionArgs);
    }
    if(jsonObject.proximityParams){
        eventArgs.proximityParams = SuperMap.ProximityParam.fromJson(jsonObject.proximityParams);
    }
    eventArgs.cancel = jsonObject.cancel;

	return eventArgs;
};
SuperMap.ClosestFacilityFindingEventArgs.registerClass('SuperMap.ClosestFacilityFindingEventArgs');

SuperMap.ClosestFacilityFoundEventArgs = function(){
	/// <summary>为ClosestFacilityFound事件提供数据。</summary>
	/// <returns type="SuperMap.ClosestFacilityFoundEventArgs">返回 ClosestFacilityFoundEventArgs 对象。</returns>
	/// <field name="result" type="SuperMap.ProximityResult">最近设施分析结果。</field>
    this.result = null;
};
SuperMap.ClosestFacilityFoundEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ClosestFacilityFoundEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ClosestFacilityFoundEventArgs">&lt;see cref="T:SuperMap.ClosestFacilityFoundEventArgs"&gt;ClosestFacilityFoundEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.ClosestFacilityFoundEventArgs(); 
    if(jsonObject.result){
        eventArgs.result = SuperMap.Recrodset.fromJson(jsonObject.result);
    }

	return eventArgs;
};
SuperMap.ClosestFacilityFoundEventArgs.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.result){
            this.result.dispose();
            this.result = null;
        }
    }
};
SuperMap.ClosestFacilityFoundEventArgs.registerClass('SuperMap.ClosestFacilityFoundEventArgs');

SuperMap.ServiceAreaFindingEventArgs = function(){
	/// <summary>为ServiceAreaFinding事件提供数据。</summary>
	/// <returns type="SuperMap.ServiceAreaFindingEventArgs">返回 ServiceAreaFindingEventArgs 对象。</returns>
	/// <field name="clientActionArgs" type="SuperMap.ActionEventArgs">客户端 Action 事件参数。 </field>
	/// <field name="networkModelSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。 </field>
	/// <field name="serviceAreaParam" type="SuperMap.ServiceAreaParam">服务区分析参数。 </field>
	/// <field name="needHighlight" type="Boolean">是否需要高亮显示。</field>
	/// <field name="cancel" type="Boolean">指示是否应取消事件的值。 </field>
    this.clientActionArgs = null;
    this.networkModelSetting = null;
	this.serviceAreaParam = null;
	this.needHighlight = false;
    this.cancel = false;
};
SuperMap.ServiceAreaFindingEventArgs.prototype={
   	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
            this.clientActionArgs = null;
        }
		if(this.networkModelSetting){
            this.networkModelSetting.dispose();
            this.networkModelSetting = null;
        }
        if(this.serviceAreaParam){
            this.serviceAreaParam.dispose();
            this.serviceAreaParam = null;
        }
    }
};
SuperMap.ServiceAreaFindingEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ServiceAreaFindingEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ServiceAreaFindingEventArgs">&lt;see cref="T:SuperMap.ServiceAreaFindingEventArgs"&gt;ServiceAreaFindingEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.ServiceAreaFindingEventArgs();
    if(jsonObject.clientActionArgs){
        eventArgs.clientActionArgs = SuperMap.ActionEventArgs.fromJson(jsonObject.clientActionArgs);
    }
    if(jsonObject.networkModelSetting){
        eventArgs.networkModelSetting = SuperMap.NetworkModelSetting.fromJson(jsonObject.networkModelSetting);
    }
	if(jsonObject.serviceAreaParam){
        eventArgs.serviceAreaParam = SuperMap.ServiceAreaParam.fromJson(jsonObject.serviceAreaParam);
    }
	eventArgs.needHighlight = jsonObject.needHighlight;
    eventArgs.cancel = jsonObject.cancel;
	return eventArgs;
};
SuperMap.ServiceAreaFindingEventArgs.registerClass('SuperMap.ServiceAreaFindingEventArgs');

SuperMap.ServiceAreaFoundEventArgs = function(){
	/// <summary>为ServiceAreaFound事件提供数据。</summary>
	/// <returns type="SuperMap.ServiceAreaFoundEventArgs">返回 ServiceAreaFoundEventArgs 对象。</returns>
	/// <field name="result" type="SuperMap.ServiceAreaResult">服务区分析结果。</field>
    this.result = null;
};
SuperMap.ServiceAreaFoundEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ServiceAreaFoundEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ServiceAreaFoundEventArgs">&lt;see cref="T:SuperMap.ServiceAreaFoundEventArgs"&gt;ServiceAreaFoundEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.ServiceAreaFoundEventArgs(); 
    if(jsonObject.result){
        eventArgs.result = SuperMap.ServiceAreaResult.fromJson(jsonObject.result);
    }

	return eventArgs;
};
SuperMap.ServiceAreaFoundEventArgs.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.result){
            this.result.dispose();
            this.result = null;
        }
    }
};
SuperMap.ServiceAreaFoundEventArgs.registerClass('SuperMap.ServiceAreaFoundEventArgs');

SuperMap.ProximityParam = function(){
	/// <summary>最近设施分析参数。&lt;br&gt;
	/// 1. 最近设施查找：&lt;br&gt;
	/// 为事件点查找以最小耗费能到达的设施点，结果显示从事件点到设施点的最佳路径，耗费，及行驶方向。该功能还可以设置查找的阈值，
	/// 即搜索范围，超过该范围将不再进行查找，例如事件发生点是一起交通事故，要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。
	/// 此例中，事故发生地即是一个事件点，周边的医院则是设施点。最近设施查找实际上也是一种路径分析，因此，同样可以应用障碍边和障碍点的设置，&lt;br&gt;
	/// 在行驶路途上这些障碍将不能被穿越，在路径分析中会予以考虑。&lt;br&gt;
	/// 2. 最近设施查找的步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过proximityParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；&lt;br&gt;
	/// 通过proximityParam 对象设定查找的设施个数，设定查找的方向，设定查找范围（单位同网络分析环境中设置的阻力字段，如不限定查找范围，该值可设为0）。&lt;br&gt;
	/// （3）事件点和设施点准备。该方法每次为一个事件点进行最近设施查找，该点可以是在分析区域内的坐标点，也可以是网络结点。&lt;br&gt;
	/// 设施点通常由多个点组成，同样既可以是坐标点，也可以是网络结点，通常设施点通过proximityParam.networkAnalystParampoint2Ds或者nodeIDs设定。&lt;br&gt;
	/// （4）最近设施查找。通过NetworkAnalystManager.closestFacilityByPoint方法，传入事件点(point),最近设施查询的参数（proximityParam）。&lt;br&gt;
	/// （5）最近设施查找结果。该结果是ProximityResult类型，其中包括从事件点到设施点或者从设施点到事件点的最佳路径。另外，可以通过GetPathTable方法将结果最佳路径中经过的结点和弧段信息导出到一个属性表数据集中。&lt;br&gt;
	/// 3. 需要注意：事件点和设施点的类型必须一致，都是坐标点或者都是网络结点，否则该方法会失败。&lt;br&gt;
	/// </summary>
	/// <returns type="SuperMap.ProximityParam">返回 ProximityParam 对象。</returns>
	/// <field name="networkAnalystParam" type="SuperMap.NetworkAnalystParam">网络参数对象。</field>
	/// <field name="isFromEvent" type="Boolean">最近设施分析查找的方向，即是否按照从事件点到设施点的方向进行查找。默认值为false。&lt;br&gt;
	/// 最近设施分析主要是通过分析设施点和事件点之间最优的路线来判断分析出在一定范围内哪个或哪些设施与事件点有最优路线的关系。 这个行走线路是通过网络图层进行网络分析算法计算出来 的两点间的最优路线，如果起点和终点互换，可能分析出来的最优路线就不同。因此在进行最近设施分析之前，需要设置获取的最优路线的方向，即是以事件点 作为起点到最近设施点的方向分析，还是以最近设施点为起点到事件点的方向分析。 &lt;br&gt;
	/// 如果需要以事件点作为起点到设施点方向进行查找，设置该字段值为true。 &lt;br&gt;
	/// 如果该值设置为False，表示从设施点到事件点进行查找。</field>
	/// <field name="maxImpedance" type="Number">查找范围的最大半径。&lt;br&gt;
	/// 单位同分析环境中阻力字段的单位，如果查找全网络，该值设为0。例如事件发生点是一起交通事故， 要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。那么在ProximityParam.NetworkParams的网络分析参数中 需要设置正（反）阻力字段为单位为时间的字段，然后设置查找范围的半径即设置该字段的值为10。 该属性默认值为0。</field>
	/// <field name="facilityCount" type="Number">要查找的最近设施点个数，默认为1。</field>
    this.networkAnalystParam = null;
    this.isFromEvent = false;
    this.maxImpedance = 0.0;
    this.facilityCount = 1;
};
SuperMap.ProximityParam.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.networkParams){
	        this.networkParams.dispose();
	        this.networkParams = null;
	    }
    }
};
SuperMap.ProximityParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 ProximityParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ProximityParam">&lt;see cref="T:SuperMap.ProximityParam"&gt;ProximityParam&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var proximityParam = new SuperMap.ProximityParam();
    if(jsonObject.networkAnalystParam){
        proximityParam.networkAnalystParam = SuperMap.NetworkAnalystParams.fromJson(jsonObject.networkAnalystParam);
    }	    
    proximityParam.isFromEvent = jsonObject.isFromEvent;
    proximityParam.maxImpedance = jsonObject.maxImpedance;
    proximityParam.facilityCount = jsonObject.facilityCount;
	
	return proximityParam;
};
SuperMap.ProximityParam.registerClass('SuperMap.ProximityParam');

SuperMap.ProximityResult = function(){
	/// <summary>最近设施分析结果参数。 该类用于存放最近设施分析的结果，符合条件的最近设施点的记录集。 </summary>
	/// <returns type="SuperMap.ProximityResult">返回 ProximityResult 对象。</returns>
	/// <field name="returnRecordset" type="Array">返回的最近设施的记录集。</field>
    this.returnRecordset = null;
};
SuperMap.ProximityResult.prototype={
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.returnRecordset != null){
            this.returnRecordset.dispose();
            this.returnRecrodset = null;
        }
    }
};
SuperMap.ProximityResult.fromJson=function(jsonObject){
	/// <summary>将JSON对象转换为 ProximityResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.ProximityResult">&lt;see cref="T:SuperMap.ProximityResult"&gt;ProximityResult&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var result = new SuperMap.ProximityResult();
    if(jsonObject.returnRecordset){
        result.returnRecordset = SuperMap.RecordSet.fromJson(jsonObject.returnRecordset);
    }

	return result;
};
SuperMap.ProximityResult.registerClass('SuperMap.ProximityResult');

SuperMap.PathFindingEventArgs = function(){
	/// <summary>为PathFinding事件提供数据。</summary>
	/// <returns type="SuperMap.PathFindingEventArgs">返回 PathFindingEventArgs 对象。</returns>
	/// <field name="clientActionArgs" type="SuperMap.ActionEventArgs">客户端事件参数。 </field>
	/// <field name="routeParams">路径分析参数。</field>
	/// <field name="cancel">指示是否应取消事件的值。</field>
    this.clientActionArgs = null;
    this.routeParams = null;
    this.cancel = false;
};
SuperMap.PathFindingEventArgs.prototype={
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.routeParams){
            this.routeParams.dispose();
        }
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
        }
    }
};
SuperMap.PathFindingEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 PathFindingEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.PathFindingEventArgs">&lt;see cref="T:SuperMap.PathFindingEventArgs"&gt;PathFindingEventArgs&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var eventArgs = new SuperMap.PathFindingEventArgs();
    if(jsonObject.clientActionArgs){
        eventArgs.clientActionArgs = SuperMap.ActionEventArgs.fromJson(jsonObject.clientActionArgs);
    }
    if(jsonObject.routeParams){
        eventArgs.routeParams = SuperMap.RouteParam.fromJson(jsonObject.routeParams);
    }
    eventArgs.cancel = jsonObject.cancel;
    
	return eventArgs;
};
SuperMap.PathFindingEventArgs.registerClass('SuperMap.PathFindingEventArgs');

SuperMap.PathFoundEventArgs = function(pathResult){
	/// <summary>为 PathFound 事件提供数据。</summary>
	/// <param name="pathResult" type="object">JSON 字符串对象，该对象中表达了路径分析结果。</param>
	/// <returns type="SuperMap.PathFoundEventArgs">返回 PathFoundEventArgs 对象。</returns>
	/// <field name="bounds"  type="SuperMap.Rect2D">路径的范围（最小外接矩形）。  </field>
	/// <field name="edgeIDs" type="Array" elementType="Number">路径所经过边的 ID 数组。</field>
	/// <field name="nodeIDs" type="Array" elementType="Number">路径所经过结点的 ID 数组。</field>
	/// <field name="nodePositions" type="Array" elementType="SuperMap.Point2D">路径所经过结点的位置信息，Point2D 数组。 </field>
	/// <field name="totalLength" type="Number">结果路径的总长度。</field>
	
	if(pathResult) {
		this.bounds = pathResult.bounds;
		this.edgeIDs = pathResult.edgeIDs;
		this.nodeIDs = pathResult.nodeIDs;
		this.nodePositions = pathResult.nodePositions
		this.totalLength = pathResult.totalLength;
	}
};
SuperMap.PathFoundEventArgs.prototype = {
    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
        if(this.bounds){
            this.bounds.dispose();
        }
        if(this.clientActionArgs){
            this.clientActionArgs.dispose();
        }
        if(this.nodePositions){
            while(this.nodePositions.length > 0){
                this.nodePositions.pop() = null;
            }
        }
    }
};
SuperMap.PathFoundEventArgs.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 PathFoundEventArgs 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.PathFoundEventArgs">&lt;see cref="T:SuperMap.PathFoundEventArgs"&gt;PathFoundEventArgs&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var eventArgs = new SuperMap.PathFoundEventArgs();
	if(jsonObject.bounds){
	    eventArgs.bounds = SuperMap.Rect2D.fromJson(jsonObject.bounds);
	}
   eventArgs.edgeIDs = jsonObject.edgeIDs;
   eventArgs.nodeIDs = jsonObject.nodeIDs;
   if(jsonObject.nodePositions){
	    eventArgs.nodePositions = new Array();
	    for(var i = 0;i < jsonObject.nodePositions.length; i++)
	    {
	        eventArgs.nodePositions[i] = new SuperMap.Point2D();
	        nodePositions[i].x = jsonObject.nodePositions[i].x;
	        nodePositions[i].y = jsonObject.nodePositions[i].y;
	    }
   // eventArgs.bounds.fromJson(object.nodePositions);
	}
	eventArgs.totalLength = jsonObject.totalLength;
	  
	return eventArgs;  
};
SuperMap.PathFoundEventArgs.registerClass('SuperMap.PathFoundEventArgs');

SuperMap.TSPPathParam = function(){
	/// <summary>该类描述旅行商分析中所用到的网络分析参数和是否指定终点标示。 </summary>
	/// <returns type="SuperMap.TSPPathParam">返回 TSPPathParam 对象。</returns>
	/// <field name="networkAnalystParam" type="SuperMap.NetworkAnalystParam">网络参数设置对象。</field>
	/// <field name="isEndNodeAssigned" type="Boolean">是否指定旅行终点。默认为不指定，则按照代价最小的原则迭代得到旅行的最佳路线。如果指定旅行终点，则相当于在原有计算基础上加了一个限制条件。  </field>
	this.networkAnalystParam = null;
	this.isEndNodeAssigned = false;
};
SuperMap.TSPPathParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.networkAnalystParam){
			this.networkAnalystParam.dispose();
			this.networkAnalystParam = null;
		}
	}
};
SuperMap.TSPPathParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 PathParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.PathParam">&lt;see cref="T:SuperMap.PathParam"&gt;PathParam&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var tspPathParam = new SuperMap.TSPPathParam();
	if(jsonObject.networkAnalystParam){
    tspPathParam.networkAnalystParam = SuperMap.NetworkAnalystParam.fromJson(jsonObject.networkAnalystParam);
	}
	tspPathParam.isEndNodeAssigned = jsonObject.isEndNodeAssigned;
	return tspPathParam;
};
SuperMap.TSPPathParam.registerClass('SuperMap.TSPPathParam');

SuperMap.Entity = function(){
	/// <summary>描述空间实体对象的类，他不仅描述空间信息，还能够描述实体的属性信息。&lt;br&gt;
	/// 该类与Geometry的主要区别就是 Geometry所表达的几何对象仅包含空间信息、形状信息，并不包括属性信息。
	/// 如果希望获取地图图层中某个几何实体，可以通过 MapControl 或者 Map 等的 GetEntity 方法首先获
	/// 取几何对象对应的实体-Entity， 然后通过该实体 Entity 的 Shape 从而获取对应的 Geometry。 
	/// </summary>
	/// <field name="fieldNames" type="Array" elementType="String">字段列表 String 数组。 </field>
	/// <field name="fieldValues" type="Array" elementType="String"> 属性字段值 String 数组。 对于文本对象而言，FieldValues[0] 是文本的内容，其他的是属性字段值。 </field>
	/// <field name="shape" type="SuperMap.Geometry"> 几何形状。表示几何对象仅包含空间信息、形状信息，如几何对象的类型（点、线、面等等）。   </field>
	/// <field name="id" type="Number">实体对象的标识符，也即数据集中 SmID 字段的值。&lt;br&gt;
	/// 数据集中 smid 的值是系统自动设置的，不能更改。因此通常通过该字段来标识目前 entity 实体代表的是哪个几何对象。或者通过该字段获取实体的 smid，然后根据这个smid的值来执行删除或更新实体的操作。
	/// </field>
	this.fieldNames = null;// 字段列表。
    this.fieldValues = null;// 属性字段值。
	this.shape = null;//几何形状。
	this.id = -1;
};
SuperMap.Entity.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.fieldNames){
			while(this.fieldNames.length > 0){
				this.fieldNames.pop();
			}
			this.fieldNames = null;
		}
		if(this.fieldValues){
			while(this.fieldValues.length > 0){
				this.fieldValues.pop();
			}
			this.fieldValues = null;
		}
		if(this.shape){
			this.shape.dispose();
		}
	},

	copy:function(object){
	/// <summary>复制 Entity 对象。</summary>
	/// <param name="object">原 Entity 对象。</param>
	/// <returns type="SuperMap.Entity">&lt;see cref="T:SuperMap.Entity"&gt;Entity&lt;/see&gt;对象。</returns>
		if(!object){return;}
		if(object.fieldNames && !this.fieldNames){
			this.fieldNames = new Array();
		}
		for(var i = 0; object.fieldNames && i < object.fieldNames.length; i++){
			this.fieldNames[i] = object.fieldNames[i];
		}
		if(object.fieldValues && !this.fieldValues){
			this.fieldValues = new Array();
		}
		for(var i = 0; object.fieldValues && i < object.fieldValues.length; i++){
			this.fieldValues[i] = object.fieldValues[i];
		}
		if(object.shape && !this.shape){
			this.shape = new SuperMap.Geometry();
		}
		if(object.shape){
			this.shape.copy(object.shape);
		}
		this.id = object.id;
	}
};
SuperMap.Entity.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 Entity 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Entity">&lt;see cref="T:SuperMap.Entity"&gt;Entity&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var entity = new SuperMap.Entity();
	if(jsonObject.fieldNames){
		entity.fieldNames = new Array();
	}
	for(var i = 0; jsonObject.fieldNames && i < jsonObject.fieldNames.length; i++){
		entity.fieldNames[i] = jsonObject.fieldNames[i];
	}
	if(jsonObject.fieldValues){
		entity.fieldValues = new Array();
	}
	for(var i = 0; jsonObject.fieldValues && i < jsonObject.fieldValues.length; i++){
		entity.fieldValues[i] = jsonObject.fieldValues[i];
	}
	if(jsonObject.shape){
		entity.shape = new SuperMap.Geometry();
	}
	if(jsonObject.shape){
		entity.shape = SuperMap.Geometry.fromJson(jsonObject.shape) ; 
	}
	entity.id = jsonObject.id;
	
	return entity;	
};
SuperMap.Entity.registerClass('SuperMap.Entity');

SuperMap.Geometry = function(){
	/// <summary>该类用于描述几何对象。描述几何对象（矢量）的特征数据（坐标点对、 几何对象的类型等）以及
	/// 相关的处理方法。&lt;br&gt;
	/// 几何对象通常可用于空间分析、空间关系运算、缓冲区查询分析等。
	/// 该类所表达的几何对象仅包含空间信息、形状信息等，并不包括属性信息。&lt;br&gt;
	/// 而Entity是 一个能够描述空间实体对
	/// 象的类，他不仅描述空间信息，还能够描述实体的属性信息。 
	/// </summary>
	/// <field name="feature" type="Number"> 几何对象的类型。包括点、线、多边形、文本等。&lt;br&gt;
	/// 未知类型 = -1，点对象 = 1，点对象 = 3，面对象 = 5，文本对象 = 7，路线对象 = 35。
	/// </field>
	/// <field name="id" type="Number"> 几何对象唯一标识符。</field>
	/// <field name="parts" type="Array" elementType="Number"> 描述几何对象中各个子对象所包含的节点数组。&lt;br&gt;
	/// 1. 几何对象从结构上可以分为简单几何对象和复杂几何对象。简单几何对象与复杂几何对象的区别：&lt;br&gt;
	/// 简单的几何对象一般为单一对象， 而复杂的几何对象由多个简单对象组成或经过一定的空间运算之后产生，
	/// 如：矩形为简单的区域对象，而中空的矩形为复杂的区域对象。&lt;br&gt;
	/// 2. 通常情况，一个简单几何对象的子对象就是它本身，因此对于简单对象 来说的该字段为长度为1的整型数组，该字段的值就是这个简单对象节点的个数。如果一个几何对象是由几个简单对象组合而成的， 例如，一个岛状几何对象由3个简单的多边形组成而成，那么这个岛状的几何对象的Parts字段值就是一个长度为3的整型数组， 数组中每个成员的值分别代表这三个多边形所包含的节点个数。 
	/// </field>
	/// <field name="point2Ds" type="Array" elementType="SuperMap.Point2D">组成几何对象的节点的坐标对数组。&lt;br&gt; 
	/// 对于点对象和文本对象而言，Point2Ds[0]代表坐标，其他的值无效。&lt;br&gt;
	/// 1. 几何对象从结构上可以分为简单几何对象和复杂几何对象。简单几何对象与复杂几何对象的区别：简单的几何对象一般为单一对象， 而复杂的几何对象由多个简单对象组成或经过一定的空间运算之后产生，如：矩形为简单的区域对象，而中空的矩形为复杂的区域对象。 &lt;br&gt;
	/// 2. 所有几何对象（点、线、面）都是由一些简单的点坐标组成的，该字段存放了组成几何对象的点坐标的数组。对于简单的面对象，他的起点和终点的坐标点相同。&lt;br&gt; 
	/// 3. 对于复杂的几何对象，根据 Parts 属性来确定每一个组成复杂几何对象的简单对象所对应的节点的个数，从而确定Point2Ds字段中坐标对的分配归属问题。&lt;br&gt; 
	/// 4. 对于点对象和文本对象而言，Point2Ds[0]代表坐标，其他的值无效。 
	///  </field>
    this.feature = 0;
	this.id = -1;// 几何对象唯一标识符。
	this.parts = null;// 坐标对分段。 
	this.point2Ds = null;// 坐标点对。 对于点对象和文本对象而言，Point2Ds[0]代表坐标，其他的值无效。
};
SuperMap.Geometry.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.parts){
			while(this.parts.length > 0){
				this.parts.pop();
			}
			this.parts = null;
		}
		if(this.point2Ds){
			while(this.point2Ds.length > 0){
				var temp = this.point2Ds.pop(); 
				temp = null;
			}
			this.point2Ds = null;
		}
	},

	copy:function(object){
	/// <summary>复制 Geometry 对象。</summary>
	/// <param name="object">原 Geometry 对象。</param>
	/// <returns type="SuperMap.Geometry">&lt;see cref="T:SuperMap.Geometry"&gt;Geometry&lt;/see&gt;对象。</returns>
	    this.feature = object.feature;
		this.id = object.id;
		if(object.parts && !this.parts){
			this.parts = new Array();
		}
		for(var i = 0; object.parts && i < object.parts.length; i++){
			this.parts[i] = object.parts[i];
		}
		if(object.point2Ds && !this.point2Ds){
			this.point2Ds = new Array();
		}
		for(var i = 0; object.point2Ds && i < object.point2Ds.length; i++){
			this.point2Ds[i] = new SuperMap.Point2D();
			this.point2Ds[i].x = object.point2Ds[i].x;
			this.point2Ds[i].y = object.point2Ds[i].y;
		}
	}
};
SuperMap.Geometry.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 Geometry 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.Geometry">&lt;see cref="T:SuperMap.Geometry"&gt;Geometry&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
	var geometry = new SuperMap.Geometry();
	geometry.feature = jsonObject.feature;
	geometry.id = jsonObject.id;
	if(jsonObject.parts){
		geometry.parts = new Array();
	}
	for(var i = 0; jsonObject.parts && i < jsonObject.parts.length; i++){
		geometry.parts[i] = jsonObject.parts[i];
	}
	if(jsonObject.point2Ds){
		geometry.point2Ds = new Array();
	}
	for(var i = 0; jsonObject.point2Ds && i < jsonObject.point2Ds.length; i++){
		geometry.point2Ds[i] = new SuperMap.Point2D();
		geometry.point2Ds[i].x = jsonObject.point2Ds[i].x;
		geometry.point2Ds[i].y = jsonObject.point2Ds[i].y;
	}
	
	return geometry;
};
SuperMap.Geometry.registerClass('SuperMap.Geometry');

SuperMap.FeatureType = function(){
    /// <summary>要素类型枚举对象。该类定义了目前支持的要素的类型，并以数值表示。
	/// </summary>
	/// <returns type="SuperMap.FeatureType">返回 FeatureType 对象。</returns>
	/// <field name="unknown" type="Number" static="true">未知类型。默认值为-1。  </field>
	/// <field name="point" type="Number" static="true">点要素类型。默认值为1。
	/// </field>
	/// <field name="line" type="Number" static="true">线要素类型。默认值为3。
	/// </field>
	/// <field name="polygon" type="Number"  static="true">面要素类型。默认值为5。
	/// </field>
	/// <field name="text" type="Number" static="true">文本要素类型。默认值为7。
	/// </field>
	/// <field name="linem" type="Number" static="true">路线要素类型。默认值为35。
	///   </field>
	this.unknown = -1;
	this.point = 1;
	this.line = 3;
	this.polygon = 5;
	this.text = 7;
	this.linem = 35;
};
SuperMap.FeatureType.registerClass("SuperMap.FeatureType");

SuperMap.TextStyle = function(){
	/// <summary>文本对象的风格。</summary>
	/// <field name="bgColor" type="SuperMap.Color">文本的背景颜色。</field>
	/// <field name="align" type="Number">对齐方式。具体数值参照 SuperMap.TextAlignment 的说明。</field>
	/// <field name="bold"  type="Boolean">文本是否为粗体字。 布尔型。默认值为false。</field>
	/// <field name="color" type="SuperMap.Color">文本的颜色。</field>
	/// <field name="fixedSize" type="Boolean">是否固定文本的大小。默认值为false。&lt;br&gt;
	/// 固定文本的尺寸是指当地图进行缩放的时候文本的尺寸不随之进行缩放。 如果该值为true，那么文本的尺寸不会随地图的缩放而相应的进行缩放。
	///  </field>
	/// <field name="fixedTextSize" type="Boolean">固定文本的大小尺寸。默认值为false。&lt;br&gt;
	/// 当 TextStyle.fixedSize 为 true 时，该字段有效。它表示地图缩放的时候文本尺寸始终按照该字段值的大小来显示。 </field>
	/// <field name="fontHeight" type="Number">文本的字体高度。默认值为50。使用地图单位。 </field>
	/// <field name="fontWeight" type="Number">文本字体的磅数，表示粗体的具体数值。默认值为400。 </field>
	/// <field name="fontName" type="String">文本的字体名称。</field>
	/// <field name="fontWidth" type="Numbers">文本的字体宽度。默认值为20。使用地图单位。</field>
	/// <field name="italic"  type="Boolean">文本的字体是否采用斜体。 布尔型。默认值为false。 </field>
	/// <field name="outline" type="Boolean">以轮廓的方式来显示文本的背景。  布尔型。默认值为false。 </field>
	/// <field name="rotation" type="Number">文本旋转的角度。逆时针方向，单位为度。 </field>
	/// <field name="shadow"   type="Boolean">文本是否有阴影。 布尔型。默认值为false。 </field>
	/// <field name="stroke" type="Boolean">文本的字体是否加删除线。  布尔型。默认值为false。 </field>
	/// <field name="transparent" type="Boolean">文本背景是否透明。  布尔型。默认值为false。 </field>
	/// <field name="underline" type="Boolean">文本的字体是否加下划线。 布尔型。默认值为false。 </field>
    this.align = 1;
    this.bgColor = null;
    this.bold = false;
    this.color = 0;
    this.fixedSize = false;
    this.fixedTextSize = 0;
    this.fontHeight = 50.0;
	this.fontWeight = 400;
    this.fontName = null;
    this.fontWidth = 20.0;
    this.italic = false;
    this.outline = false;
    this.rotation = 0.0;
    this.shadow = false;
    this.stroke = false;
    this.transparent = false;
    this.underline = false;
};
SuperMap.TextStyle.prototype = {
    compare:function(object){
	/// <summary>对比此对象与指定的对象。   </summary>
	/// <param name="object">要测试的对象。</param>
	/// <returns type="Object">返回object对象，用于表达两个比较对象之间有差异的值。</returns>
        if(!object){return false;}
        var newObject = null;
        if(this.align != object.align){
			if(!newObject)
				newObject = new Object();
			newObject.align = object.align;
		}
        if(this.bgColor != object.bgColor){
			if(!newObject)
				newObject = new Object();
			newObject.bgColor = object.bgColor;
		}
        if(this.bold != object.bold){
			if(!newObject)
				newObject = new Object();
			newObject.bold = object.bold;
		}
        if(this.color != object.color){
			if(!newObject)
				newObject = new Object();
			newObject.color = object.color;
		}
        if(this.fixedSize != object.fixedSize){
			if(!newObject)
				newObject = new Object();
			newObject.fixedSize = object.fixedSize;
		}
        if(this.fixedTextSize != object.fixedTextSize){
			if(!newObject)
				newObject = new Object();
			newObject.fixedTextSize = object.fixedTextSize;
		}
        if(this.fontHeight != object.fontHeight){
			if(!newObject)
				newObject = new Object();
			newObject.fontHeight = object.fontHeight;
		}
        if(this.fontName != object.fontName){
			if(!newObject)
				newObject = new Object();
			newObject.fontName = object.fontName;
		}
        if(this.fontWidth != object.fontWidth){
			if(!newObject)
				newObject = new Object();
			newObject.fontWidth = object.fontWidth;
		}
        if(this.italic != object.italic){
			if(!newObject)
				newObject = new Object();
			newObject.italic = object.italic;
		}
        if(this.outline != object.outline){
			if(!newObject)
				newObject = new Object();
			newObject.outline = object.outline;
		}
        if(this.rotation != object.rotation){
			if(!newObject)
				newObject = new Object();
			newObject.rotation = object.rotation;
		}
        if(this.shadow != object.shadow){
			if(!newObject)
				newObject = new Object();
			newObject.shadow = object.shadow;
		}
        if(this.stroke != object.stroke){
			if(!newObject)
				newObject = new Object();
			newObject.stroke = object.stroke;
		}
        if(this.transparent != object.transparent){
			if(!newObject)
				newObject = new Object();
			newObject.transparent = object.transparent;
		}
        if(this.underline != object.underline){
			if(!newObject)
				newObject = new Object();
			newObject.underline = object.underline;
		}

        return newObject;
    },

    dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
    }
};
SuperMap.TextStyle.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 TextStyle 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.TextStyle">&lt;see cref="T:SuperMap.TextStyle"&gt;TextStyle&lt;/see&gt;对象。</returns>
    if(!jsonObject){return;}
    var textStyle = new SuperMap.TextStyle();
	textStyle.bgColor = jsonObject.bgColor;
    textStyle.bold = jsonObject.bold;
    textStyle.color = jsonObject.color;
    textStyle.fixedSize = jsonObject.fixedSize;
    textStyle.fixedTextSize = jsonObject.fixedTextSize;
    textStyle.fontHeight = jsonObject.fontHeight;
    textStyle.fontName = jsonObject.fontName;
    textStyle.fontWidth = jsonObject.fontWidth;
    textStyle.italic = jsonObject.italic;
    textStyle.outline = jsonObject.outline;
    textStyle.rotation = jsonObject.rotation;
    textStyle.shadow = jsonObject.shadow;
    textStyle.stroke = jsonObject.stroke;
    textStyle.transparent = jsonObject.transparent;
    textStyle.underline = jsonObject.underline;
    textStyle.align = jsonObject.align;

	return textStyle
};
SuperMap.TextStyle.registerClass('SuperMap.TextStyle');

SuperMap.ImageFormat = function(){
	/// <summary>图片格式枚举类型。 1代表png，2代表jpg，3代表bmp，4代表tiff，5代表gif。</summary>
	/// <field name="defalut" type="Number"  >默认格式，其值为0。&lt;br&gt;
	/// 如果采用默认格式，iServer服务会根据地图配置文件（或者服务管理工具iServerManager）中设置的图片格式进行处理。</field>
	/// <field name="png" type="Number"  static="true">png格式，其值为1。</field>
	/// <field name="jpg" type="Number"  static="true">jpg格式，其值为2。</field>
	/// <field name="bmp" type="Number"  static="true">bmp格式，其值为3。</field>
	/// <field name="tiff" type="Number" static="true">tiff格式，其值为4。</field>
	/// <field name="gif" type="Number"  static="true">gif格式，其值为5。</field>
	
    this.defalut = 0;
    this.png = 1;
    this.jpg= 2;
    this.bmp = 3;
    this.tiff = 4;
    this.gif = 5;
};
SuperMap.ImageFormat.registerClass('SuperMap.ImageFormat');

SuperMap.LayerType = function(){
	/// <summary>图层类型。图层类型与数据集的类型是一一对应的。 </summary>
	/// <field name="undefined" type="Number"  ></field>
	/// <field name="point" type="Number"  static="true">点图层，其值为1。</field>
	/// <field name="line" type="Number"  static="true">线图层，其值为3。</field>
	/// <field name="network" type="Number"  static="true">网络图层，其值为4。</field>
	/// <field name="polygon" type="Number" static="true">面图层，其值为5。</field>
	/// <field name="text" type="Number" static="true" >文本图层，其值为7。</field>
	/// <field name="image" type="Number"  static="true">影像图层，其值为81。</field>
	/// <field name="mrSID" type="Number"  static="true">mrSID数据图层，其值为82。</field>
	/// <field name="grid" type="Number" static="true" >Grid图层，其值为83。</field>
	/// <field name="dem" type="Number" static="true" >DEM图层，其值为84。</field>
	/// <field name="ecw" type="Number" static="true">ECW图层，其值为85。</field>
	/// <field name="cad" type="Number" static="true" >CAD图层，其值为149。</field>
    this.undefined = 0,
    this.point = 1;
    this.line = 3;
    this.network = 4;
    this.polygon = 5;
    this.text = 7;
    this.image = 81;
    this.mrSID = 82;
    this.grid = 83;
    this.dem = 84;
    this.ecw = 85;
    this.cad = 149;
};
SuperMap.LayerType.registerClass('SuperMap.LayerType');

SuperMap.BufferAnalystParam = function(){
	/// <summary>该类主要描述建立缓冲区的相关参数。&lt;br&gt;
	/// 缓冲区实际上是在基本空间要素周围建立的具有一定宽度的邻近区域。缓冲区分析可以有以下应用，
	/// 比如确定街道拓宽的范围，确定放射源影响的范围等等。&lt;br&gt;
	///  1. 对于生成线对象的缓冲区，用户可以对缓冲边的方向进行设置，可以选择是在这条线的左边生成缓冲区还是在右边。&lt;br&gt;
	///  2. 对于点和面对象而言，只有左缓冲区没有右缓冲区。&lt;br&gt;
	/// 注意：线对象的左右与线对象的方向密切相关。&lt;br&gt;
	///  3. 对于面对象而言，在做缓冲区分析前最好先经过拓扑检查，排除面内相交的情况，所谓面内相交，指的是面对象自身相交。&lt;br&gt;
	/// &lt;img src="../img/BufferAnalystParam.png" /&gt; 
	/// </summary>
	/// <field name="leftDistance" type="Number">1. 线缓冲区分析时：&lt;br&gt;
	/// (1) 当 bufferSideType = LEFT 时，表示左边缓冲区的距离，该值必须大于0；&lt;br&gt;
	/// (2) 当 bufferSideType = FULL 时，表示左右相等的双边缓冲（左右两边的缓冲距离都为 leftDistance），该值必须大于0；&lt;br&gt;
	/// (3) 当 bufferSideType = RIGHT 时，该值无效。&lt;br&gt;
	/// 2. 面缓冲区分析时，代表缓冲区半径，该值不能为0，大于0时表示向外缓冲(面膨胀)，小于0时表示向内缓冲(面紧缩)。&lt;br&gt;
	/// 3. 点缓冲区分析时，代表缓冲半径，必须大于0。
	/// </field>
	/// <field name="rightDistance" type="Number">线缓冲区分析时，表示右边缓冲区的距离，当 bufferSideType = RIGHT 时，该值必须大于0。&lt;br&gt;
	/// 点或者面缓冲区分析时该参数无效。
	/// </field>
	/// <field name="semicircleLineSegment" type="Number">保存结果面时，线段拟合半圆弧的线段数，即将半圆弧采用多少条线段来进行拟合。该数字必须大于等于4小于200。拟合线段数太小，结果面误差较大；拟合线段数量太大，结果面占用较大存储空间。&lt;br&gt;
	/// 缺省值为12。</field>
	/// <field name="bufferEndType" type="Number">线缓冲区分析的端点类型，目前仅支持圆头缓冲。</field>
	/// <field name="bufferSideType" type="Number"> 线缓冲区分析的缓冲边的类型，目前支持缓冲左边、缓冲右边、两边同时缓冲三种类型。&lt;br&gt;
	/// 1. 当该属性值为 LEFT (1)时，通过 leftDistance 属性设置线对象左边缓冲距离，而此时 rightDistance 属性无效；&lt;br&gt;
	/// 2. 当该属性值为 RIGHT (2)时，通过 rightDistance 属性设置线对象右边缓冲距离，而此时 leftDistance 属性无效；&lt;br&gt;
	/// 3. 当该属性值为 FULL (0)时，通过 leftDistance 属性设置线对象的缓冲距离，而此时 rightDistance 属性无效；
	/// </field>
	this.leftDistance = 0.0;
	this.rightDistance = 0.0;
	this.semicircleLineSegment = 12;
	this.bufferSideType = 0;
	this.bufferEndType = 1;
};
SuperMap.BufferAnalystParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
    }
};
SuperMap.BufferAnalystParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 BufferAnalystParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.BufferAnalystParam">&lt;see cref="T:SuperMap.BufferAnalystParam"&gt;BufferAnalystParam&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var bufferParam = new SuperMap.BufferAnalystParam();
	bufferParam.leftDistance = jsonObject.leftDistance;
	bufferParam.rightDistance = jsonObject.rightDistance;
	bufferParam.semicircleLineSegment = jsonObject.semicircleLineSegment;
	bufferParam.bufferSideType = jsonObject.bufferSideType;
	bufferParam.bufferEndType = jsonObject.bufferEndType;
	return bufferParam;
};
SuperMap.BufferAnalystParam.registerClass('SuperMap.BufferAnalystParam');

SuperMap.EditResult = function(){
	/// <summary>编辑结果类。 编辑操作返回的结果对象，包含操作成功是否的标识，以及被编辑的地物 ID、地图受影响的范围和操作之后的地图描述。 </summary>
	/// <field name="succeed" type="Boolean">操作是否成功。 </field>
	/// <field name="ids" type="Array" elementType="Number">所有数据被更新的实体的 ID 数组。  </field>
	/// <field name="editBounds" type="SuperMap.Rect2D">地图受影响的范围，即被编辑过的地图数据所对应的范围。 </field>
	/// <field name="message" type="String">获取编辑过程中产生的相关信息。通常这些信息都是编辑的时候出现的错误信息，如图层不存在等。 </field>
	this.succeed = false;
	this.ids = null;
	this.editBounds = null;
	this.message = null;
};
SuperMap.EditResult.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		this.ids = null;
		this.editBounds = null;
		this.message = null;
	}
};
SuperMap.EditResult.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 EditResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.EditResult">&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt;对象。</returns>
	if(!jsonObject){
		return;
	}
	var editResult = new SuperMap.EditResult();
	editResult.succeed = jsonObject.succeed;
	editResult.editBounds = SuperMap.Rect2D.fromJson(jsonObject.editBounds);
	if(jsonObject.ids){
		editResult.ids = new Array();
		for(var i = 0; i < jsonObject.ids.length; i++){
			editResult.ids[i] = jsonObject.ids[i];
		}
	}
	editResult.message = jsonObject.message;
	
	return editResult;
};
SuperMap.EditResult.registerClass('SuperMap.EditResult', null, Sys.IDisposable);

SuperMap.OverlayParam  = function(){
	/// <summary>叠加分析参数类。&lt;br&gt;
	/// 该类型主要提供叠加分析时，源数据集、操作数据集、叠加类型、输出结果数据集的字段选择等的。&lt;br&gt;
	/// 在矢量叠加分析中至少涉及到三个数据集，其中一个数据集的类型除合并运算和对称差运算必须是面数据集外，其他运算可以是点、线、面或者复合数据集，被称作源数据集；另一个数据集是面数据集被称作操作数据集；还有一个数据集就是叠加结果数据集，包含叠加后数据的几何信息和属性信息。&lt;br&gt;
	/// 叠加分析可以进行点与面的叠加、线与面的叠加、多边形与面的叠加。参与运算的两个数据集中的相交对象都要进行分解，形成新的子对象。&lt;br&gt;
	/// 叠加结果数据集中的属性信息来自于第一数据集和第二数据集的属性表，在进行叠加分析的时候，用户可以根据自己的需要在这两个数据集的属性表中选择需要保留的属性字段。
	/// </summary>
	/// <field name="operation" type="Number">要进行叠加分析的操作类型。&lt;br&gt;
	/// 裁剪操作=1，擦除操作=2，同一操作=3，求交操作=4，
	/// 交集取反操作=5，求并集操作=6。
	/// </field>
	/// <field name="sourceDatasetName" type="String">进行叠加分析的源数据集的名称。也称第一数据集。该数据集类型可以是点、线和面。 </field>
	/// <field name="sourceDataSourceName" type="String">进行叠加分析的源数据集所在数据源的名称。</field>
	/// <field name="operateDatasetName" type="String">操作数据集的名字。也称第二数据集。该数据集类型必须是面。 </field>
	/// <field name="operateDataSourceName" type="String">操作数据集所在数据源的名字。也称第二数据集。该数据集类型必须是面。 </field>
	/// <field name="operateRegion">操作区域。只有当 operateDatasetName 为空时，本属性才会被使用。 </field>
	/// <field name="resultDatasetName" type="String">期望的结果数据集名字。不能与已有数据集重名。&lt;br&gt;
	/// 数据集的名称限制：数据集名称的长度限制为30个字符（也就是可以为30个英文字母或者15个汉字）。</field>
	/// <field name="resultDataSourceName" type="String">期望的结果数据集所在数据源的名字。</field>
	/// <field name="deleteResultIfExists" type="Boolean">是否删除同名的结果数据集。 </field>
	/// <field name="sourceDatasetFields" type="Array" elementType="String">源数据集中保留字段名称数组。 </field>
	/// <field name="operateDatasetFields" type="Array" elementType="String">操作数据集中保留字段名称数组。 </field>
	/**
	 * 要进行叠加分析的操作类型。例如：求交、擦除、合并、分割等。
	 */
	this.operation = 0;
	/**
	 * 进行叠加分析的源数据集的名称。
	 */
	this.sourceDatasetName = "";
	/**
	 * 进行叠加分析的源数据集所在数据源的名称。
	 */
	this.sourceDataSourceName = "";
	/**
	 * 操作数据集的名字。
	 */
	this.operateDatasetName = "";
	/**
	 * 操作数据集数据源的名字。
	 */
	this.operateDataSourceName = "";
	/**
	 * 操作区域。只有当 operateDatasetName 为空时，本属性才会被使用。
	 */
    this.operateRegion = null;
	/**
	 * 期望的结果数据集名字。不能与已有数据集重名。 数据集的名称限制：数据集名称的长度限制为30个字符（也就是可以为30个英文字母或者15个汉字）。
	 */
    this.resultDatasetName = "";
	/**
	 * 期望的结果数据集所在数据源的名字。
	 */
    this.resultDataSourceName = "";
	/**
	 * 是否删除同名的结果数据集。
	 */
    this.deleteResultIfExists = true;
	/**
	 * 源数据集中保留字段名称数组。
	 */
    this.sourceDatasetFields = null;
	/**
	 * 操作数据集中保留字段名称数组。
	 */
    this.operateDatasetFields = null;
};
SuperMap.OverlayParam.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
		if(this.operateRegion){
            this.operateRegion.dispose();
            this.operateRegion = null;
        }
	}
};
SuperMap.OverlayParam.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 OverlayParam 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.OverlayParam">&lt;see cref="T:SuperMap.OverlayParam"&gt;OverlayParam&lt;/see&gt;对象。</returns>
	if(!jsonObject) return;
	
	var overlayParam = new SuperMap.OverlayParam();
	if(!jsonObject){return;}
	overlayParam.operation = jsonObject.operation;
	overlayParam.sourceDatasetName = jsonObject.sourceDatasetName;
	overlayParam.operateDatasetName = jsonObject.operateDatasetName;
	if(jsonObject.operateRegion){
		overlayParam.operateRegion = SuperMap.Geometry.fromJson(jsonObject.operateRegion);
	}
	overlayParam.resultDatasetName = jsonObject.resultDatasetName;
	overlayParam.deleteResultIfExists = jsonObject.deleteResultIfExists;
	
	return overlayParam;
};
SuperMap.OverlayParam.registerClass('SuperMap.OverlayParam', null, Sys.IDisposable);

SuperMap.OverlayResult = function(){
	/// <summary>叠加分析结果。&lt;br&gt;
	/// 地图服务根据叠加分析的参数的设置进行相应叠加分析的操作后，通过该类的对象表达叠加分析结果中。
	/// 开发者可以通过该类的属性、方法等获取叠加分析的结果。 </summary>
	/// <field name="succeed" type="Boolean"> 表明叠加分析操作是否成功。 如果叠加分析操作不成功，即返回值为false时，在地图服务的日志文件中会记载叠加分析失败的原因。</field>
	/// <field name="resultDatasetName" type="String">叠加分析结果数据集所对应的图层的名称。 </field>
	/// <field name="message" type="String">叠加分析过程中产生的相关信息。 </field>
	/**
	 * 表明叠加分析操作是否成功。 如果叠加分析操作不成功，即返回值为false时，在地图服务的日志文件中会记载叠加分析失败的原因。
	 */
	this.succeed = false;
	/**
	 * 叠加分析结果数据集所对应的图层的名称。
	 */
	this.resultDatasetName = "";
	/**
	 * 叠加分析过程中产生的相关信息。
	 */
	this.message = "";
};
SuperMap.OverlayResult.prototype = {
	dispose:function(){
	/// <summary>释放对象所占用的资源。</summary>
	}
};
SuperMap.OverlayResult.fromJson = function(jsonObject){
	/// <summary>将JSON对象转换为 OverlayResult 对象。</summary>
	/// <param name="jsonObject">要转换的JSON对象。</param>
	/// <returns type="SuperMap.OverlayResult">&lt;see cref="T:SuperMap.OverlayResult"&gt;OverlayResult&lt;/see&gt;对象。</returns>
	if(!jsonObject){return;}
	var overlayResult = new SuperMap.OverlayResult();
	overlayResult.succeed = jsonObject.succeed;
	overlayResult.resultDatasetName = jsonObject.resultDatasetName;
	overlayResult.message = jsonObject.message;
	
	return overlayResult;
};
SuperMap.OverlayResult.registerClass('SuperMap.OverlayResult', null, Sys.IDisposable);

SuperMap.OverlayAnalystType = function(){
	/// <summary>叠加分析操作的类型。例如：求交、擦除、合并、分割等。 </summary>
	/// <field name="undefined" type="Number" static="true"> 未知类型。可以处理自定义操作。 undefined = 0。</field>
	/// <field name="clip" type="Number" static="true">用Clip数据集从被剪取数据集中抽取部分特征（点、线、面）集合的运算。 结果数据集来自于被剪取数据集，因此其类型与被剪取数据集是相同的。  clip = 1。&lt;br&gt;
	/// 1. 裁剪数据集（第二数据集）的类型必须是面，被剪裁的数据集（第一数据集）可以是点、线、面；&lt;br&gt;
	/// 2. 在被裁剪数据集中，只有落在裁剪数据集多边形内的对象才会被输出到结果数据集中；&lt;br&gt;
	/// 3. Clip与Intersect在空间处理上是一致的，不同在于对结果数据集属性的处理，Clip分析只是用来做裁剪，结果数据集只保留第一个数据集的非系统字段，而Intersect求交分析的结果则可以根据字段设置情况来保留两个数据集的字段。clip图示：&lt;br&gt;
	/// &lt;img src="../img/OverlayClip.png" /&gt; 
	/// </field>
	/// <field name="erase" type="Number" static="true"> 用于对数据集进行擦除方式的叠加分析，将第一个数据集中包含在第二个数据集内的对象裁剪并删除。结果数据集中仅保留第一个数据集的用户定义字段，其它系统字段则在擦除之后自动更新。 erase = 2。&lt;br&gt;
	/// 1. 擦除数据集（第二数据集）的类型必须是面，被擦除的数据集（第一数据集）可以是点、线、面数据集。&lt;br&gt;
	/// 2. 擦除数据集中的多边形集合定义了擦除区域，被擦除数据集中凡是落在这些多边形区域内的特征都将被去除，而落在多边形区域外的特征要素都将被输出到结果数据集中，与Clip运算相反。&lt;br&gt;
	/// &lt;img src="../img/OverlayErase.png" /&gt; 
	/// </field>
	/// <field name="identity" type="Number" static="true">用于对数据集进行同一方式的叠加分析。identity = 3。&lt;br&gt;
	///  1. 同一运算就是第一数据集与第二数据集先求交，然后求交结果再与第一数据集求并的一个运算。其中，第二数据集的类型必须是面，第一数据集的类型可以是点、线、面数据集。如果第一个数据集为点数集，则新生成的数据集中保留第一个数据集的所有对象；如果第一个数据集为线数据集，则新生成的数据集中保留第一个数据集的所有对象，但是把与第二个数据集相交的对象在相交的地方打断；如果第一个数据集为面数据集，则结果数据集保留以第一数据集为控制边界之内的所有多边形，并且把与第二个数据集相交的对象在相交的地方分割成多个对象。&lt;br&gt;
	/// 2. Identiy运算与Union运算有相似之处，所不同之处在于Union运算保留了两个数据集的所有部分，而Identity运算是把第一个数据集中与第二个数据集不相交的部分进行保留。Identity运算的结果属性表来自于两个数据集的属性表。&lt;br&gt;
	/// &lt;img src="../img/OverlayIdentity.png" /&gt; 
	/// </field>
	/// <field name="intersect" type="Number" static="true">求两个数据集的交集的操作，两个数据集中共同的部分将被输出到结果数据集中，其余部分将被排除。intersect = 4。&lt;br&gt;
	/// 1. 第一数据集的类型可以是点类型、线类型和面类型，第二数据集必须是面类型。第一数据集的特征对象（点、线和面）在与第二数据集中的多边形相交处被分裂（点对象除外），分裂结果被输出到结果数据集中。&lt;br&gt;
	/// 2. 求交运算与裁剪运算得到的结果数据集的空间几何信息相同的，但是裁剪运算不对属性表做任何处理，而求交运算可以让用户选择需要保留的属性字段。&lt;br&gt;
	/// &lt;img src="../img/OverlayIntersect.png" /&gt; 
	/// </field>
	/// <field name="symmetricDifference" type="Number" static="true">对两个面数据集进行对称差分析运算。即交集取反运算。&lt;br&gt;
	/// 对称差运算是两个数据集的异或运算。操作的结果是，对于每一个面对象，去掉其与另一个数据集中的几何对象相交的部分，而保留剩下的部分。对称差运算的输出结果的属性表包含两个输入数据集的非系统属性字段。symmetricDifference = 5。 &lt;br&gt;
	/// &lt;img src="../img/OverlayXOR.png" /&gt; 
	/// </field>
	/// <field name="union" type="Number" static="true">用于对两个面数据集进行合并方式的叠加分析，结果数据集中保存被合并叠加分析的数据集和用于合并叠加分析的数据集中的全部对象，并且对相交部分进行求交和分割运算。union = 6。 &lt;br&gt;
	/// 合并是求两个数据集并的运算，合并后的图层保留两个数据集所有图层要素，只限于两个面数据集之间进行。 &lt;br&gt;
	/// 进行Union运算后，两个面数据集在相交处多边形被分割，且两个数据集的几何和属性信息都被输出到结果数据集中。 &lt;br&gt;
	/// &lt;img src="../img/OverlayUnion.png" /&gt; 
	/// </field>
    this.undefined = 0;
    this.clip = 1;
    this.erase = 2;
    this.identity = 3;
    this.intersect = 4;
    this.symmetricDifference = 5;
    this.union = 6;
};
SuperMap.OverlayAnalystType.registerClass('SuperMap.OverlayAnalystType');

SuperMap.LayerSettingType = function(){
	/// <summary>图层显示风格设置对应的图层类型。如WMS图层设置、SuperMap图层设置等。该类所描述的图层主要指iServer服务端提供的地图的图层。</summary>
	/// <field name="undefined" type="Number" static="true">undefined = -1， 未知的图层类型。</field>
	/// <field name="supermap" type="Number" static="true">supermap = 1，SuperMap类型的图层。</field>
	/// <field name="wms" type="Number" static="true">wms = 2，WMS图层。</field>
	/// <field name="wfs" type="Number" static="true">wfs = 3， WFS图层。</field>
	/// <field name="googlemap" type="Number" static="true">googlemap = 4，GoogleMap图层。</field>
	/// <field name="kml" type="Number" static="true">kml = 5，KML图层。</field>
	/// <field name="yahoomap" type="Number" static="true">yahoomap = 6，yahoomap图层。</field>
	/// <field name="virtualearthmap" type="Number" static="true">virtualearthmap = 7， virtualearthmap图层。</field>
	/// <field name="georss" type="Number" static="true">georss = 8，GeoRSS图层。</field>
	/// <field name="collection" type="Number" static="true">collection = 11，图层集合。</field>
	/// <field name="supermapcollection" type="Number" static="true">supermapcollection = 12，SuperMap类型的图层集合。</field>
    this.undefined = -1;
    this.supermap = 1;
    this.wms = 2;
    this.wfs = 3;
    this.googlemap = 4;
    this.kml = 5;
    this.yahoomap = 6;
    this.virtualearthmap = 7;
    this.georss = 8;
    this.collection = 11;
    this.supermapcollection = 12;
};
SuperMap.LayerSettingType.registerClass('SuperMap.LayerSettingType');

SuperMap.SuperMapLayerType = function(){
	/// <summary>SuperMap的图层类型常量。该类主要对SuerMap iServer 服务中获取的图层进行分类，SuperMap提供
	/// 矢量图层，影像图层，grid图层，WMS图层等。这些图层类型与SuperMap Objects .NET 的LayerSettingType相同。	
	/// 当LayerSettingType=supermap时，该SuperMapLayerType的设置才有效。
	/// </summary>
	/// <field name="undefined" type="Number"  >undefined = -1，未定义图层。</field>
	/// <field name="vector" type="Number"  static="true">矢量数据集对应的图层，其值为0。</field>
	/// <field name="grid" type="Number" static="true" >Grid图层，其值为1。</field>
	/// <field name="image" type="Number"  static="true">影像图层，其值为2。</field>
	/// <field name="wms" type="Number"  static="true">WMS图层，其值为3。</field>
	/// <field name="wfs" type="Number"  static="true">WFS图层，其值为4。</field>
	/// <field name="theme" type="Number" static="true">专题图层，其值为6。</field>
	
    this.undefined = -1,
    this.vector = 0;
    this.grid = 1;
    this.image = 2;
    this.wms = 3;
    this.wfs = 4;
    this.theme = 6;
};
SuperMap.SuperMapLayerType.registerClass('SuperMap.SuperMapLayerType');

SuperMap.DirectionType = function() {
	/// <summary>方向枚举类型。 
	/// 用在行驶导引子项类型中。 
	/// </summary>
	/// <field name="east" type="Number" static="true">东，其值为0。</field>
	/// <field name="south" type="Number" static="true">南，其值为1。</field>
	/// <field name="west" type="Number" static="true">西，其值为2。</field>
	/// <field name="north" type="Number" static="true">北，其值为3。</field>
	/// <field name="none" type="Number" static="true">没有方向，其值为255。</field>
	this.east = 0;
	this.south = 1;
	this.west = 2;
	this.north = 3;
	this.none = 255;
};
SuperMap.DirectionType.registerClass('SuperMap.DirectionType');

SuperMap.TurnType = function() {
	/// <summary>转弯方向枚举类型。用在行驶导引子项类型中。</summary>
	/// <field name="end" type="Number" static="true">终点，不转弯，其值为0。</field>
	/// <field name="left" type="Number" static="true">左转弯，其值为1。</field>
	/// <field name="right" type="Number" static="true">右转弯，其值为2。</field>
	/// <field name="ahead" type="Number" static="true">前行。其值为3。</field>
	/// <field name="back" type="Number" static="true">掉头。其值为4。</field>
	/// <field name="none" type="Number" static="true">不设置方向。其值为255。</field>
	this.end = 0;
	this.left = 1;
	this.right = 2;
	this.ahead = 3;
	this.back = 4;
	this.none = 255;
};
SuperMap.TurnType.registerClass('SuperMap.TurnType');

SuperMap.SideType = function() {
	/// <summary>表示在路的左边、右边还是在路上的枚举类型。用在行驶导引子项类型中。 </summary>
	/// <field name="middle" type="Number" static="true">在路上（即路的中间），其值为0。</field>
	/// <field name="left" type="Number" static="true">在路的左侧，其值为1。</field>
	/// <field name="right" type="Number" static="true">在路的右侧，其值为-1。</field>
	/// <field name="none" type="Number" static="true">不设置侧边，其值为255。</field>
	this.middle = 0;
	this.left = 1;
	this.right = -1;
	this.none = 255;
};
SuperMap.SideType.registerClass('SuperMap.SideType');

SuperMap.SpatialOperationType = function(){
	/// <summary>空间操作枚举类型 </summary>
	/// <field name="clip" type="Number" static="true">生成被操作对象经过操作对象裁减后的几何对象。  clip = 1。&lt;br&gt;
	/// 1. 被操作几何对象只有落在操作几何对象内的那部分才会被输出为结果几何对象；&lt;br&gt;
	/// 2. Clip与 Intersect 在空间处理上是一致的，不同在于对结果几何对象属性的处理，Clip分析只是用来做裁剪，结果几何对象只保留被操作几何对象的非系统字段，而 Intersect 求交分析的结果则可以根据字段设置情况来保留两个几何对象的字段。&lt;br&gt;
	/// 3. 该操作适合的几何对象类型： 操作几何对象：面； 被操作几何对象：线、面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ Clip.png" /&gt; &lt;br&gt;
	/// </field>
	/// <field name="erase" type="Number" static="true"> 在被操作对象上擦除掉与操作对象相重合的部分。 erase = 2。&lt;br&gt;
	/// 1. 如果对象全部被擦除了，则返回Null；&lt;br&gt;
	/// 2. 操作几何对象定义了擦除区域，凡是落在操作几何对象区域内的被操作几何对象都将被去除，而落在区域外的特征要素都将被输出为结果几何对象，与 Clip运算相反；&lt;br&gt;
	/// 3. 该操作适合的几何对象类型： 操作几何对象：面； 被操作几何对象：点、线、面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ Erase.png" /&gt; &lt;br&gt;
	/// </field>
	/// <field name="identity" type="Number" static="true">对被操作对象进行同一操作，即操作执行后，被操作几何对象包含来自操作几何对象的几何形状。 identity = 3。 &lt;br&gt;
	/// 1. 同一运算就是操作几何对象与被操作几何对象先求交，然后求交结果再与被操作几何对象求并的运算。&lt;br&gt;
	/// 2. 如果被操作几何对象为点类型，则结果几何对象为被操作几何对象；&lt;br&gt;
	/// 3. 如果被操作几何对象为线类型，则结果几何对象为被操作几何对象，但是操作几何对象相交的部分将被打断；&lt;br&gt;
	/// 4. 如果被操作几何对象为面类型，则结果几何对象保留以被操作几何对象为控制边界之内的所有多边形，并且把与操作几何对象相交的地方分割成多个对象。&lt;br&gt;
	/// 5. 该操作适合的几何对象类型：操作几何对象：面；被操作几何对象：点、线、面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ Identity.png" /&gt; &lt;br&gt;
	/// </field>
	/// <field name="intersection" type="Number" static="true">对两个几何对象求交，返回两个几何对象的交集。intersect = 4。&lt;br&gt; 
	/// 1. 求交运算与裁剪运算得到的结果几何对象的空间几何信息相同的，但是裁剪运算不对属性表做任何处理，而求交运算可以让用户选择需要保留的属性字段；&lt;br&gt;
	/// 2. 进行求交运算的两个几何对象必须是同类型的，目前版本只支持面类型的求交；&lt;br&gt;
	/// 3. 该操作适合的几何对象类型： 操作几何对象：面； 被操作几何对象：面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ Intersect.png" /&gt; &lt;br&gt;
	/// </field>
	/// <field name="xor" type="Number" static="true">对两个对象进行对称差操作，即对于每一个被操作几何对象，去掉其与操作几何对象相交的部分，而保留剩下的部分。其值为5。&lt;br&gt;
	/// 1. 对称差运算的结果几何对象的属性表包含两个输入几何对象的非系统属性字段；&lt;br&gt;
	/// 2. 该操作适合的几何对象类型：a)操作几何对象：面；b)被操作几何对象：面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ XOR.png" /&gt; &lt;br&gt;
	/// </field>
	/// <field name="union" type="Number" static="true">对两个对象进行合并操作，进行合并后，两个面对象在相交处被多边形分割。其值为6。&lt;br&gt;
	/// 1. 进行求并运算的两个几何对象必须是同类型的，目前版本只支持面类型的合并；&lt;br&gt;
	/// 2. 该操作适合的几何对象类型： 操作几何对象：面； 被操作几何对象：面。&lt;br&gt;
	/// &lt;img src="../img/Geometrist_ Union.png" /&gt; &lt;br&gt;
	/// </field>
	this.clip = 1;
	this.erase = 2;
	this.identity = 3;
	this.intersection = 4;
	this.xor = 5;
	this.union = 6;
};
SuperMap.SpatialOperationType.registerClass('SuperMap.SpatialOperationType');

SuperMap.Units = function(){
	/// <summary>地图坐标单位枚举类型。 </summary>
	/// <field name="meter" type="Number" static="true">米，meter = 10000。</field>
	/// <field name="kilometer" type="Number" static="true">千米，kilometer = 10000000。</field>
	/// <field name="mile" type="Number" static="true">英里，mile=16090000。</field>
	/// <field name="yard" type="Number" static="true">码，yard = 9144。</field>
	/// <field name="degree" type="Number" static="true">度，degree = 1001745329</field>
	/// <field name="millimeter" type="Number" static="true">毫米，millimeter = 10。</field>
	/// <field name="centimeter" type="Number" static="true">厘米，centimeter = 100。</field>
	/// <field name="inch" type="Number" static="true">英寸，inch = 254。</field>
	/// <field name="decimeter" type="Number" static="true">分米，decimeter = 1000。</field>
	/// <field name="foot" type="Number" static="true">英尺，foot = 3048。</field>
	/// <field name="second" type="Number" static="true">秒，second = 1000000485。</field>
	/// <field name="minute" type="Number" static="true">分，minute = 1000029089。</field>
	/// <field name="radian" type="Number" static="true">弧度，radian = 1100000000。</field>
	this.meter = 10000;
	this.kilometer = 10000000;
	this.mile = 16090000;
	this.yard = 9144;
	this.degree = 1001745329;
	this.millimeter = 10;
	this.centimeter = 100;
	this.inch = 254;
	this.decimeter = 1000;
	this.foot = 3048;
	this.second = 1000000485;
	this.minute = 1000029089;
	this.radian = 1100000000;
};
SuperMap.Units.registerClass('SuperMap.Units');

SuperMap.SpatialQueryMode = function(){
	/// <summary>定义空间查询操作模式常量。&lt;br&gt;	
	/// 空间查询是通过几何对象之间的空间位置关系来构建过滤条件的一种查询方式。例如：通过空间查询可以找到被包含在面中的空间对象，相离或者相邻的空间对象等。&lt;br&gt;	
	/// 注意：当前版本提供对点、线、面、网络和文本类型数据的空间查询，其中文本类型仅支持Intersect和 Contain两种空间查询模式，而且只能作为被搜索对象不能作为搜索对象。
	/// </summary>
	/// <field name="none" type="Number" static="true">无空间查询。  none = -1。
	/// </field>
	/// <field name="identity" type="Number" static="true">重合空间查询模式。  identity = 0。&lt;br&gt;	
	///  返回被搜索图层中与搜索对象完全重合的对象。包括对象类型和坐标。&lt;br&gt;	
	/// 注意：搜索对象与被搜索对象的类型必须相同；且两个对象的交集不为空，搜索对象的边界及内部分别和被搜索对象的外部交集为空。&lt;br&gt;	
	/// 该关系适合的对象类型：搜索对象：点、线、面；被搜索对象：点、线、面。&lt;br&gt;	
	/// 如图所示为重合查询的示例：&lt;br&gt;	
	/// &lt;img src="../img/SQIdentical.png" /&gt;  &lt;br&gt;	
    /// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="disjoint" type="Number" static="true">分离空间查询模式。  disjoint = 1。&lt;br&gt;
	///  返回被搜索图层中与搜索对象相离的对象。&lt;br&gt;
	/// 注意：搜索对象和被搜索对象相离，即无任何交集。&lt;br&gt;
	/// 该关系适合的对象类型：搜索对象：点、线、面；被搜索对象：点、线、面。&lt;br&gt;
	/// 如图所示，分离空间查询的示例。&lt;br&gt;
	/// &lt;img src="../img/SQDisjoint.png" /&gt;  &lt;br&gt;
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="intersect" type="F" static="true">相交空间查询模式。  intersect = 2。&lt;br&gt;
	///  返回与搜索对象相交的所有对象。&lt;br&gt;
	/// 注意：如果搜索对象是面，返回全部或部分被搜索对象包含的对象以及全部或部分包含搜索对象的对象；如果搜索对象不是面，返回全部或部分包含搜索对象的对象。&lt;br&gt;
	/// 该关系适合的对象类型：搜索对象：点、线、面；被搜索对象：点、线、面。&lt;br&gt;
	/// 如图所示，相交空间查询的示例。&lt;br&gt;	
	/// &lt;img src="../img/SQIntersect.png" /&gt;  &lt;br&gt;
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="touch" type="Number" static="true">邻接空间查询模式。  touch = 3。&lt;br&gt;	
	///  返回被搜索图层中其边界与搜索对象边界相触的对象。&lt;br&gt;	
	/// 注意：搜索对象和被搜索对象的内部交集为空。&lt;br&gt;	
	/// 该关系不适合的对象类型为：点查询点的空间关系。&lt;br&gt;	
	/// 如图所示，邻接空间查询的示例。&lt;br&gt;	
	/// &lt;img src="../img/SQTouch.png" /&gt;  &lt;br&gt;	
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="overlap" type="Number" static="true">叠加空间查询模式。  overlap = 4。&lt;br&gt;	
	///  返回被搜索图层中与搜索对象部分重叠的对象。&lt;br&gt;	
	/// 该关系适合的对象类型为：线/线，面/面。其中，两个几何对象的维数必须一致，而且他们交集的维数也应该和几何对象的维数一样；&lt;br&gt;	
	/// 注意：点与任何一种几何对象都不存在部分重叠的情况。&lt;br&gt;	
	/// 如图所示为叠加查询的图示。&lt;br&gt;	
	/// &lt;img src="../img/SQOverlap.png" /&gt; &lt;br&gt;	
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="cross" type="Number" static="true">交叉空间查询模式。  cross = 5。&lt;br&gt;	
	/// 返回被搜索图层中与搜索对象（线或面）相交的所有对象（点、线或面）。&lt;br&gt;	
	/// 注意：搜索对象和被搜索对象内部的交集不能为空；参与交叉（Cross）关系运算的两个对象必须有一个是线对象。&lt;br&gt;	
	/// 该关系适合的对象类型：搜索对象：线；被搜索对象：线、面。&lt;br&gt;	
	/// 如图所示为交叉查询示例。&lt;br&gt;	
	/// &lt;img src="../img/SQCross.png" /&gt;  &lt;br&gt;	
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="within" type="Number" static="true">被包含空间查询模式。  within = 6。&lt;br&gt;
	///  返回被搜索图层中完全包含搜索对象的对象。如果返回的对象是面，其必须全部包含（包括边接触）搜索对象；如果返回的对象是线，其必须完全包含搜索对象；如果返回的对象是点，其必须与搜索对象重合。该类型与包含（Contain）的查询模式正好相反。&lt;br&gt;	
	/// 该关系适合的对象类型：搜索对象： 点、线、面；被搜索对象： 点、线、面。&lt;br&gt;	
	/// 注意：线查询点，面查询线或面查询点都不存在被包含的情况。&lt;br&gt;	
	/// 如图所示为点查询面的示例。&lt;br&gt;	
	/// &lt;img src="../img/SQWithin.png" /&gt;  &lt;br&gt;	
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	/// <field name="contain" type="Number" static="true">包含空间查询模式。  contain = 7。&lt;br&gt;
	///  返回被搜索图层中完全被搜索对象包含的对象。&lt;br&gt;
	/// 注：搜索对象和被搜索对象的边界交集可以不为空；点查线/点查面/线查面，不存在包含情况。&lt;br&gt;
	/// 该关系适合的对象类型：搜索对象：点、线、面；被搜索对象：点、线、面。&lt;br&gt;
	/// 如图所示，包含空间查询的图示。&lt;br&gt;
	/// &lt;img src="../img/SQContain.png" /&gt;  &lt;br&gt;
	/// 其中搜索对象用绿色表示，被搜索图层上的对象用黑色表示，结果记录集几何对象用红色表示。
	/// </field>
	this.none = -1;
	this.identity = 0;
	this.disjoint = 1;
	this.intersect = 2;
	this.touch = 3;
	this.overlap = 4;
	this.cross = 5;
	this.within = 6;
	this.contain = 7;
};
SuperMap.SpatialQueryMode.registerClass('SuperMap.SpatialQueryMode');

SuperMap.FillGradientMode = function(){
	/// <summary>定义渐变填充模式的渐变类型。所有渐变类型都是两种颜色之间的渐变，即从渐变起始色到渐变终止色之间的渐变。&lt;br&gt;	
	/// 渐变风格的计算都是以填充区域的边界矩形，即最小外接矩形作为基础的，因而以下提到的填充区域范围即为填充区域的最小外接矩形。
    /// </summary>
	/// <field name="none" type="Number" static="true">无渐变模式。  none = 0。&lt;br&gt;	
	/// 当使用普通填充模式时，设置渐变类型为无渐变。
	/// </field>
	/// <field name="linear" type="Number" static="true">线性渐变模式。  linear = 1。&lt;br&gt;	
	/// 从水平线段的起始点到终止点，如图所示，其颜色从起始色均匀渐变到终止色，垂直于该线段的直线上颜色不发生渐变。&lt;br&gt;	
	/// &lt;img src="../img/Gra_Linear.png" /&gt; 
	/// </field>
	/// <field name="radial" type="Number" static="true">辐射渐变模式。  radial = 2。&lt;br&gt;	
	/// 以填充区域范围的中心点作为渐变填充的起始点，距离中心点最远的边界点作为终止点的圆形渐变。&lt;br&gt;	
	/// 注意在同一个圆周上颜色不发生变化，不同的圆之间颜色发生渐变。&lt;br&gt;	
	/// 如图所示，从渐变填充的起始点到终止点，其以起始点为圆心的各个圆的颜色随着圆的半径的增大从起始色均匀渐变到终止色。&lt;br&gt;	
	/// &lt;img src="../img/Gra_Radial.png" /&gt; 
	/// </field>
	/// <field name="conical" type="Number" static="true">圆锥渐变模式。  conical = 3。&lt;br&gt;	
	/// 从起始母线到终止母线，渐变在逆时针和顺时针两个方向发生渐变，都是从起始色渐变到终止色。&lt;br&gt;	
	/// 注意填充区域范围中心点为圆锥的顶点，在圆锥的母线上颜色不发生变化。&lt;br&gt;	
	/// 如图所示，渐变的起始母线在填充区域范围中心点右侧的并经过该中心点的水平线上，上半圆锥颜色按逆时针发生渐变，下半圆锥按顺时针发生渐变，两个方向渐变的起始母线和终止母线分别相同，在逆时针方向和顺时针方向两个方向从起始母线转到终止母线的过程中，渐变都是从起始色均匀渐变到终止色。&lt;br&gt;	
	/// &lt;img src="../img/Gra_Conical.png" /&gt; 
	/// </field>
	/// <field name="square" type="Number" static="true">四角渐变模式。  square = 4。&lt;br&gt;	
	/// 以填充区域范围的中心点作为渐变填充的起始点，以填充区域范围的最小外接矩形的较短边的中点为终止点的正方形渐变。&lt;br&gt;	
	/// 注意在每个正方形上的颜色不发生变化, 不同的正方形之间颜色发生变化。&lt;br&gt;	
	/// 如图所示，从渐变填充的起始点到终止点，其以起始点为中心的正方形的颜色随着边长的增大从起始色均匀渐变到终止色。&lt;br&gt;	
	/// &lt;img src="../img/Gra_Square2.png" /&gt; 
	/// </field>
	this.none = 0;
	this.linear = 1;
	this.radial = 2;
	this.conical = 3;
	this.square = 4;
};
SuperMap.FillGradientMode.registerClass('SuperMap.FillGradientMode');

SuperMap.TextAlignment = function(){
	/// <summary>指定文本中的各子对象的对齐方式。&lt;br&gt;
	/// 文本对象的每个子对象的位置是由文本的锚点和文本的对齐方式共同确定的。当文本子对象的锚点固定，对齐方式确定文本子对象与锚点的相对位置，从而确定文本子对象的位置。
	/// </summary>
	/// <field name="topleft" type="Number" static="true">左上角对齐。 topleft = 0。&lt;br&gt;	
	/// 当文本的对齐方式为左上角对齐时，文本子对象的最小外接矩形的左上角点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig1n.png" /&gt; 
	/// </field>
	/// <field name="topcenter" type="Number" static="true">顶部居中对齐。 topcenter = 1。&lt;br&gt;	
	/// 当文本的对齐方式为上面居中对齐时，文本子对象的最小外接矩形的上边线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig2n.png" /&gt; 
	/// </field>
	/// <field name="topright" type="Number" static="true">右上角对齐。 topright = 2。&lt;br&gt;
	/// 当文本的对齐方式为上面居中对齐时，文本子对象的最小外接矩形的右上角点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig3n.png" /&gt; 
	/// </field>
	/// <field name="baselineleft" type="Number" static="true">基准线左对齐。  baselineleft = 3。&lt;br&gt;
	/// 当文本的对齐方式为基准线左对齐时，文本子对象的基线的左端点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig4n.png" /&gt; 
	/// </field>
	/// <field name="baselinecenter" type="Number" static="true">基准线居中对齐。  baselinecenter = 4。&lt;br&gt;	
	/// 当文本的对齐方式为基准线居中对齐时，文本子对象的基线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig5n.png" /&gt; 
	/// </field>
	/// <field name="baselineright" type="Number" static="true">基准线右对齐。  baselineright = 5。&lt;br&gt;	
	/// 当文本的对齐方式为基准线右对齐时，文本子对象的基线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig6n.png" /&gt; 
	/// </field>
	/// <field name="bottomleft" type="Number" static="true"> 左下角对齐。 bottomleft = 6。&lt;br&gt;	
	/// 当文本的对齐方式为左下角对齐时，文本子对象的最小外接矩形的左下角点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig7n.png" /&gt; 
	/// </field>
	/// <field name="bottomcenter" type="intersect" static="true">底部居中对齐。 bottomcenter = 7。&lt;br&gt;	
	///  当文本的对齐方式为底线居中对齐时，文本子对象的最小外接矩形的底线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig8n.png" /&gt; 
	/// </field>
	/// <field name="bottomright" type="Number" static="true">右下角对齐。 bottomright = 8。&lt;br&gt;	
	/// 当文本的对齐方式为右下角对齐时，文本子对象的最小外接矩形的右下角点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig9n.png" /&gt; 
	/// </field>
	/// <field name="middleright" type="Number" static="true">右中对齐。middleright = 11。&lt;br&gt;
	/// 当文本的对齐方式为右中对齐时，文本子对象的最小外接矩形的右边线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	/// &lt;img src="../img/TextAlig12n.png" /&gt; </field>
	/// <field name="middlecenter" type="Number" static="true">中心对齐。middlecenter = 10。 &lt;br&gt;
	/// 当文本的对齐方式为中心对齐时，文本子对象的最小外接矩形的中心点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	/// &lt;img src="../img/TextAlig11n.png" /&gt; </field>
	/// <field name="middleleft" type="Number" static="true">左中对齐。middleleft = 9。 &lt;br&gt;
	/// 当文本的对齐方式为左中对齐时，文本子对象的最小外接矩形的左边线的中点在该文本子对象的锚点位置，如图所示。&lt;br&gt;	
	///  &lt;img src="../img/TextAlig10n.png" /&gt; </field>
	this.topleft = 0;
	this.topcenter = 1;
	this.topright = 2;
	this.baselineleft = 3;
	this.baselinecenter = 4;
	this.baselineright = 5;
	this.bottomleft = 6;
	this.bottomcenter = 7;
	this.bottomright = 8;
	this.middleleft = 9;
	this.middlecenter = 10;
	this.middleright = 11;
};
SuperMap.TextAlignment.registerClass('SuperMap.TextAlignment');

SuperMap.ZoomPosition = function(){
	/// <summary>SuperMap.UI.SliderBar控件的摆放方式。&lt;br&gt;
	/// SuperMap.UI.SliderBar控件既可以纵向摆放也可以横向摆放，ZoomPosition用来标识当前的SliderBar控件的摆放方式。
	/// </summary>
	/// <field name="vertical" type="Number" static="true">纵向。 vertical = 0。&lt;br&gt;	
	/// </field>
	/// <field name="horizontal" type="Number" static="true">横向。 horizontal = 1。&lt;br&gt;	
	/// </field>
    this.vertical = 0;
    this.horizontal = 1;
};
SuperMap.ZoomPosition.registerClass('SuperMap.ZoomPosition');

SuperMap.MarkerContent = function() {
	/// <summary>SuperMap.MarkerContent为SuperMap.UI.Marker对象中标注对应的标注内容。&lt;br&gt;
	///	如果不设置innerHTML属性，则指定为图标类型的标注内容。
	/// </summary>
	/// <field name="imageWidth" type="Number" static="true">当标注内容为图片类型时对应图片的宽度。&lt;br&gt;	
	/// </field>
	/// <field name="imageHeight" type="Number" static="true">当标注内容为图片类型时对应图片的高度。&lt;br&gt;	
	/// </field>
	/// <field name="imageSrc" type="String" static="true">当标注内容为图片类型时对应图片的访问路径。&lt;br&gt;	
	/// </field>
	/// <field name="innerHTML" type="String" static="true">当标注内容不为图片类型时对应内容。&lt;br&gt;	
	/// </field>
	this.imageWidth = 0;
	this.imageHeight = 0;
	this.imageSrc = null;
	this.innerHTML = null;
};
SuperMap.MarkerContent.registerClass('SuperMap.MarkerContent');


SuperMap.StatisticMode = function(){
	/// <summary>字段统计方法枚举。&lt;br&gt;
	/// 对单一字段提供常用统计功能。SuperMap 提供的统计功能有6种，统计字段的最大值，最小值，平均值，总和，标准差以及方差。	
	/// </summary>
	/// <field name="max" type="Number" static="true">统计所选字段的最大值。max = 1。
	/// </field>
	/// <field name="min" type="Number" static="true">统计所选字段的最小值。min = 2。	
	/// </field>
	/// <field name="average" type="Number" static="true">统计所选字段的平均值。average = 3。	
	/// </field>
	/// <field name="sum" type="Number" static="true">统计所选字段的总和。sum = 4。	
	/// </field>
	/// <field name="stddeviation" type="Number" static="true">统计所选字段的标准差。stddeviation = 5。	&lt;br&gt;
	/// 标准差计算公式为：&lt;br&gt;
	///	&lt;img src="../img/std1.png" /&gt; &lt;br&gt;
	/// σ 为标准差，x 为所选自选字段的字段值，n 代表该字段所包含的要素个数，i 为从1到 n 的整数，M 为该字段值的平均值。
	/// </field>
	/// <field name="variance" type="Number" static="true">统计所选字段的方差。variance = 6。	&lt;br&gt;
	/// 方差计算公式为：&lt;br&gt;
	/// &lt;img src="../img/Variance1.png" /&gt; &lt;br&gt;
	/// V 为方差，x 为所选自选字段的字段值，n 代表该字段所包含的要素个数，i 为从1到 n 的整数，M 为该字段值的平均值。
	/// </field>
	this.max = 1;
	this.min = 2;
	this.average = 3;
	this.sum = 4;
	this.stddeviation = 5;
	this.variance = 6;
};
SuperMap.StatisticMode.registerClass('SuperMap.StatisticMode');

