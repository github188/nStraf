//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：		SuperMap.OverviewManager.js  
// 功能：			鹰眼功能类 
// 最后修改时间：	2007-10-30 
//========================================================================== 
Type.registerNamespace('SuperMap');

SuperMap.OverviewManager = function(handler, mapName){
	/// <summary>该类主要用于执行与鹰眼相关的操作。</summary>
	/// <param name="handler" type="String">鹰眼操作对象将请求提交到服务端的代理器的访问地址。&lt;br&gt;
	/// 客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
    /// handler会将GIS请求的处理结果返回给客户端。因此该属性主要是用来设置命令提交器提交请求的服务端代理器的地址。</param>
	/// <returns type="SuperMap.OverviewManager">返回 OverviewManager 对象。</returns>
	/// <param name="mapName" type="String">要在鹰眼中显示的地图名称。</param>
    this._queryUrl = handler;
    this._mapName = mapName;
    this._onComplete = null;
    this._onError = null;   
};

SuperMap.OverviewManager.prototype = {
    get_mapName:function(){
	/// <summary>获取或者设置地图名称。</summary>	
   	/// <returns type="String">get_mapName()返回值类型为String。</returns>
        return this._mapName;
    },
    
    set_mapName:function(mapName){
        //todo: 参数校验
        this._mapName = mapName;
    },
    
    _getOverviewBase:function(methodName, paramNames, paramValues, onComplete, onError){    
        SuperMap.Committer.commitAjax(this._mapName, this._queryUrl, methodName, paramNames, paramValues, false, onComplete, onError, "");
    },

    getOverview:function(viewerWidth, viewerHeight, mapRect, onComplete, onError){
	/// <summary>获取鹰眼图片。</summary>	
	/// <param name="viewerWidth" type="Number">鹰眼图片的宽度。</param>	
	/// <param name="viewerHeight" type="Number">鹰眼图片的高度。</param>	
	/// <param name="mapRect" type="SuperMap.Rect2D">鹰眼中显示的地图范围。</param>	
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数的名称。该值是字符串类型。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.Overview"&gt;Overview&lt;/see&gt; overview, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数的名称。该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
		
    	this._getOverviewBase("GetOverview", ["viewerWidth", "viewerHeight", "mapRect"], [viewerWidth, viewerHeight, SuperMap.Encoder.toJson(mapRect)], onComplete, onError);
    }
};
SuperMap.OverviewManager.registerClass('SuperMap.OverviewManager', Sys.Component);