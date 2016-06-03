//========================================================================== 
// SuperMap iServer 客户端程序,版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.DataManager.js  
// 功能：			主要对工作空间中数据源和数据集进行各种处理，比如获取数据集、数据源的信息，
//              对数据集进行添加、修改、删除实体的操作，对数据集进行各种方式的查询等。
// 最后修改时间：	2007-10-9 
//========================================================================== 
Type.registerNamespace("SuperMap");
SuperMap.DataManager = function(dataHandler){
	/// <summary>
	/// GIS 数据处理类。它主要对工作空间中数据源和数据集进行各种处理，比如获取数据集、数据源的信息，
	/// 对数据集进行添加、修改、删除实体的操作，对数据集进行各种方式的查询等。 
	/// </summary>
	/// <param name="dataHandler" type="String"> GIS 数据处理代理器的地址。所有该类的处理都会由 DataHandler 代理器来与服务端进行交互。
	/// 因此需要设置该参数用来指定DataHandler的访问地址，通常，代理器的访问地址由两部分组成：
	/// “http://Web服务器地址（或IP）/” + "DataHandler"。例如："http://localhost:7080/demo/Datahandler"。
	/// </param>
	/// <returns type="SuperMap.DataManager">返回 DataManager 对象。</returns>
	/// <field name="queryUrl" type="String">GIS 数据处理代理器的地址。</field>
	// url 如http://localhost:8080/demo/queryhandler
    this.queryUrl = dataHandler;
    this.onComplete = null;
    this.onError = null;
	this._eventsList = new Array();
	this._eventsNameList = new Array();
};
SuperMap.DataManager.prototype = {
	statistic:function(datasourceName,datasetName,fieldName,statisticMode,onComplete, onError,userContext){
		/// <summary>
		/// 统计分析函数。根据指定的数据源和数据集信息，对其中的某个字段信息进行统计（如平均值、最大值和最小值）。
		/// </summary>
		/// <param name="datasourceName" type="String" elementType="String">数据源名称。
		/// </param>
		/// <param name="datasetName" type="String" elementType="String">数据集名称。
		/// </param>
		/// <param name="fieldName" type="String" elementType="String">待统计字段名称。
		/// </param>
		/// <param name="statisticMode" type="Number">统计分析模式。&lt;br&gt;
		/// 定义了不同的统计模式，包含：最大值、最小值、平均值、方差、标准差等。&lt;br&gt;
		/// 每一个数值对应一种统计模式，具体数值与统计模式的对应请参见SuperMap.StatisticMode。</param>
		/// <param name="onComplete" type="Function">负责统计结果的获取与处理的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext){};</param>
		/// <param name="onError" type="Function">负责接受错误、异常信息的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};</param>
		/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		SuperMap.Committer.commitAjax("", this.queryUrl, "statistic", ["datasourceName","datasetName","fieldName","statisticMode"], [datasourceName,datasetName,fieldName,statisticMode], false, onComplete, onError, userContext);
	},
    // 用于注册事件处理函数。
    // onComplete, 原型参数: onComplete(resultSet); 
    // onError, 原型参数: onError(responseText); 
    registerHandler:function(onQueryComplete, onQueryError){
		/// <summary>
		/// 注册事件处理函数。在GIS数据处理过程中，需要有事件处理函数专门来获取并处理服务端返回的结果。&lt;br&gt;
		/// 该方法可以将某个函数设定为默认的事件处理函数，所有的DataManager的操作结果的获取和处理都会自动转由该函数执行。
		/// </summary>
		/// <param name="onQueryComplete" type="Function">负责查询结果的获取与处理的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext){};</param>
		/// <param name="onQueryError" type="Function">负责接受错误、异常信息的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};</param>
	    this.onComplete = onQueryComplete;
	    this.onError = onQueryError;
    },

    queryBase:function(methodName, paramNames, paramValues, onComplete, onError,userContext){
	/// <summary>各种查询功能的通用方法。该方法的执行是以异步的方式进行的。</summary>
	/// <param name="methodName">查询的命令名称。查询命令主要用来表达需要服务端执行哪种查询操作。&lt;br&gt;
	/// 每一个查询类型都会对应一个查询命令和一些必要的地图参数，如SQL查询的操作对应的地图命令是“QueryBySql”，对应的地图参数有"queryParam"， "needHighlight"等。
	/// 具体的地图命令及其参数列表请参考联机帮助中的详细说明。&lt;br&gt;
	/// 该值可以是字符串也可以是数组。如果该值为数组型，即多个地图查询命令以数组的形式存储于该参数中，
	/// 表示此次请求要执行多个地图查询操作，服务器对这些命令按照数组的顺序一次进行处理。
	/// </param>
	/// <param name="paramNames"  type="Array" elementType="String">命令参数的名称。执行一个GIS查询功能，除了提交查询命令外，还需要提交相关的地图参数。
	/// 如SQL查询需要的地图参数有"queryParam", "needHighlight"等。</param>
	/// <param name="paramValues" type="Array" elementType="Object">命令参数的值。paramNames对应的参数值。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。该值可以是一个function，也可以是function数组。&lt;br&gt;
	/// 通常，一次提交多个地图命令时（即method参数为数组时），可以通过该参数设置多个事件处理函数来接收每一个地图命令的处理结果，也可以设置一个事件处理函数来处理所有命令的返回结果。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext){};&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。
	/// </param>
	/// <param name="userContext" type="Function">上下文环境。
	/// </param>
        function onQueryComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var resultSet = SuperMap.ResultSet.fromJson(responseResult);
			if(onComplete){
				onComplete(resultSet, userContext);
			}
			if(resultSet != null){
				resultSet.dispose();
				resultSet = null;
			}
		};
		SuperMap.Committer.commitAjax("", this.queryUrl, methodName, paramNames, paramValues, false, onQueryComplete, onError, userContext);
    },
    
    queryByPoint:function(point, tolerance, queryParam, onComplete, onError, userContext){
		/// <summary>点选查询。&lt;br&gt;
		/// 指定地图的某一点的位置，查询该处某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
		/// <param name="point" type="SuperMap.Point2D">查询的位置。SuperMap.Point2D类型。</param>
		/// <param name="tolerance" type="Numbers">查询容限。该查询方法是以查询位置（点）为圆心，
		/// 在以tolerance的地理长度为半径的圆的范围内进行地物的查找。double 类型。该值不能小于0，建议取一个适中的值，比如5。
		/// </param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		this.queryBase("QueryByPoint", ["point", "tolerance", "queryParam"], [point, tolerance, queryParam], onComplete, onError, userContext);
    },
	queryBySql:function(queryParam, onComplete, onError, userContext){
        /// <summary>执行SQL查询。 </summary>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	    /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
        /// </param>
	    /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	    /// </param>
	    /// <param name="userContext" type="Function">上下文环境。</param>
    	this.queryBase("QueryBySql", ["queryParam"], [queryParam], onComplete, onError, userContext);
    },
	queryByRect:function(mapRect,queryParam,onComplete,onError,userContext){
		/// <summary>在指定的矩形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
		/// <param name="mapRect" type="SuperMap.Rect">矩形。矩形的范围就是查询的范围，SuperMap.Rect类型。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this.queryBase("QueryByRect", ["mapRect", "queryParam"],[mapRect, queryParam], onComplete, onError, userContext);
    },
	queryByPolygon:function(points,queryParam,onComplete,onError,userContext){
		/// <summary>在指定的多边形范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
		/// <param name="points" type="SuperMap.Point2D">组成多边形的点数组,SuperMap.Point2D数组类型。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this.queryBase("QueryByPolygon", ["points", "queryParam"], [points, queryParam], onComplete, onError, userContext);
    },
	queryByCircle:function(center, radius, queryParam, onComplete, onError, userContext){
		/// <summary>在指定的圆范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
		/// <param name="center" type="SuperMap.Point2D">圆的中心点, SuperMap.Point2D类型。</param>
		/// <param name="radius" type="Number">圆的半径,double类型。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this.queryBase("QueryByCircle", ["center", "radius", "queryParam"], [center, radius, queryParam],onComplete, onError, userContext);
    },
	queryByDistance:function(geometry, distance, queryParam, onComplete, onError, userContext) {
		/// <summary>在指定的某个几何对象以及它的缓冲区范围内查询某(几)个图层的空间地物信息。在指定的查询图层中与查询范围有交集的几何对象都会被查询出来。</summary>
		/// <param name="geometry" type="SuperMap.Geometry">几何对象。根据该对象制作缓冲区，并以几何对象和缓冲区的范围作为查询范围。</param>
		/// <param name="distance" type="Number">几何对象的缓冲半径。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		this.queryBase("QueryByDistance", ["geometry", "distance", "queryParam"], [geometry, distance, queryParam], onComplete, onError, userContext);
	},
    query:function(dataSourceName, queryParam, onComplete, onError, userContext){   
		/// <summary>查询指定的数据源中符合查询条件的几何对象。</summary>
		/// <param name="dataSourceName" type="String">数据源的名称。该数据源中所有的数据集的数据将被查询。如果设置该参数值为空，那么返回的结果也为空。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        this.queryBase("query", ["dataSourceName", "queryParam"], [dataSourceName, queryParam], onComplete, onError, userContext);
    },
    findNearest:function(dataSourceName, point, tolerance, expectResultCount, queryParam, onComplete, onError, userContext){
		/// <summary>查找某范围内最近的对象。特别说明：在线图层或面图层中,这里的距离指的从指定起始点到目标对象边线的最短长度。 </summary>
		/// <param name="dataSourceName" type="String">数据源的名称。该数据源中所有的数据集的数据将被查询。如果设置该参数值为空，那么返回的结果也为空。</param>
		/// <param name="point" type="SuperMap.Point2D">查询的起始点位置。SuperMap.Point2D类型。</param>
		/// <param name="tolerance" type="Number">从起始点开始允许查询的范围的最大直径。Double类型。
		/// </param>
		/// <param name="expectResultCount" type="Number">返回的最近地物的最大个数。该值必须为正整数。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。
		/// </param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		this.queryBase("FindNearest", ["dataSourceName", "point", "tolerance", "expectResultCount", "queryParam"], [dataSourceName, point, tolerance, expectResultCount, queryParam], onComplete, onError, userContext);
    },
    attachEvent:function(event, listener){
		/// <summary>向MapControl中添加一个事件。</summary>
		/// <param name="event" >事件名称，如Onclick。</param>
		/// <param name="listener">事件被触发时执行的函数。</param>
        var events = this._eventsList[event];
        if(!events){
            events = new Array();
            this._eventsList[event] = events;
            this._eventsNameList.push(event); 
        }
        for(var i = 0; i < events.length; i++){
            if(events[i] == listener){return true;}
        }
        events.push(listener);
    },
	detachEvent:function(event, listener){
		/// <summary>取消对象一个事件。</summary>
		/// <param name="event">事件名称，如Onclick。</param>
		/// <param name="listener">事件被触发时执行的函数。</param>
        var events = this._eventsList[event];
        if(!events){return;}
        for(var i = 0; i < events.length; i++){
            if(events[i] == listener){
                events.splice(i, 1);
            }
        }
    },
    _raiseEvent:function(event, arguments, userContext){
        var events = this._eventsList[event];
        if(!events){return;}
        if(!arguments){
            arguments = this._generateEventArg();
        }
        for(var i = 0; i < events.length; i++){
            events[i](arguments, userContext);
        }
    },
	_generateEventArg:function(error, e){
	    var param = new Object();
	    if(!error){error = "";}
	    return new SuperMap.EventArguments(param, error, e);
    },
	// 编辑相关的代码
	_EditBase:function(methodName, paramNames, paramValues,onComplete,onError,userContext){
        function onEditComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var editResult = SuperMap.EditResult.fromJson(responseResult);
			if(onComplete){
				onComplete(editResult, userContext);
			}
			if(editResult != null){
				editResult.dispose();
				editResult = null;
			}
		};
		var lockID = new Date().getTime().toString() + Math.floor(Math.random( )*1000000000);
		paramNames.push("lockID");
		paramValues.push(lockID);
		// 设置地图名为""
        SuperMap.Committer.commitAjax("", this.queryUrl, methodName, paramNames, paramValues, false, onEditComplete, onError, userContext);
    },
    addEntity:function(dataSourceName, datasetName, entity, onComplete, onError, userContext){
		/// <summary>在指定的数据集上增加一个同类型的实体。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="entity" type="SuperMap.Entity">新增的实体。SuperMap.Entity 类型。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this._EditBase("AddEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
    deleteEntity:function(dataSourceName, datasetName, ids, onComplete, onError, userContext){
		/// <summary>删除指定数据集中的某些几何实体。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="ids" type="Array" elementType="Number">实体的SmID数组，该组实体对象将被删除。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this._EditBase("DeleteEntity", ["dataSourceName", "datasetName", "ids"], [dataSourceName, datasetName, ids], onComplete, onError, userContext);
    },
    updateEntity:function(dataSourceName, datasetName, entity, onComplete, onError, userContext){
		/// <summary>更新一个实体。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="entity" type="SuperMap.Entity">待更新的实体对象。SuperMap.Entity 类型。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
    	this._EditBase("UpdateEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
    addPoint:function(dataSourceName, datasetName, point, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>在指定的数据集中添加一个点，包括点的属性。</summary>
		/// <param name="dataSourceName"  type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="point" type="SuperMap.Point2D">新添加的点的位置。SuperMap.Point2D 类型。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">新增的点对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.shape.feature = new SuperMap.LayerType().point;
        entity.shape.point2Ds = new Array(point);
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
        this._EditBase("AddEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },	
    addLine:function(dataSourceName, datasetName, points, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>在指定的数据集中添加一条线，包括线的属性。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="points" type="Array" elementType="SuperMap.Point2D">新添加的线对象的有序点数组。该数组长度必须大于等于2。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">新增的线对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new  SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.shape.feature = new SuperMap.LayerType().line;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
        this._EditBase("AddEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },    
    addPolygon:function(dataSourceName, datasetName, points, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>在指定的数据集中添加一个面，包括面的属性。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="points" type="Array" elementType="SuperMap.Point2D">用来创建面对象的有序点集合。该数组长度必须大于等于3。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">新增的面对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.shape.feature = new SuperMap.LayerType().polygon;
        entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
        this._EditBase("AddEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
    updatePoint:function(dataSourceName, datasetName, id, point, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>更新指定数据集中的点，包括属性信息。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="id" type="Number">待更新的点的Smid值。</param>
		/// <param name="point" type="SuperMap.Point2D">该点的新的坐标位置。SuperMap.Point2D 类型。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">点对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().point;
        entity.shape.point2Ds = new Array(point);
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
		// mapName 为空
        this._EditBase("UpdateEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
    updateLine:function(dataSourceName, datasetName, id, points, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>更新指定数据集中的线，包括属性信息。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="id" type="Number">待更新的线的Smid值。</param>
		/// <param name="points" type="Array" elementType="SuperMap.Point2D">该线的新的坐标位置。SuperMap.Point2D 数组。该数组长度必须大于等于2。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">线对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().line;
		entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
        this._EditBase("UpdateEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
    updatePolygon:function(dataSourceName, datasetName, id, points, fieldNames, fieldValues, onComplete, onError, userContext){
		/// <summary>更新指定数据集中的面，包括属性信息。</summary>
		/// <param name="dataSourceName" type="String">待编辑的数据集所属的数据源。</param>
		/// <param name="datasetName" type="String">待编辑的数据集名称。</param>
		/// <param name="id" type="Number">待更新的面的Smid值。</param>
		/// <param name="points" type="Array" elementType="SuperMap.Point2D">该面的新的坐标位置。SuperMap.Point2D 数组。该数组长度必须大于等于3。</param>
		/// <param name="fieldNames" type="Array" elementType="String">属性字段名数组。</param>
		/// <param name="fieldValues" type="Array" elementType="String">面对象的各个属性字段值数组。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.EditResult"&gt;EditResult&lt;/see&gt; editResult, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
        var entity = new SuperMap.Entity();
        entity.shape = new SuperMap.Geometry();
        entity.shape.parts = new Array();
        entity.id = id;
        entity.shape.feature = new SuperMap.LayerType().polygon;
		entity.shape.point2Ds = points;
        entity.fieldNames = fieldNames;
        entity.fieldValues = fieldValues;
        this._EditBase("UpdateEntity", ["dataSourceName", "datasetName", "entity"], [dataSourceName, datasetName, entity], onComplete, onError, userContext);
    },
	getDataSourceInfos:function(onComplete, onError, userContext){
		/// <summary>获取当前工作空间中存储的所有数据源的信息。通过该方法可以获取数据源的名称。</summary>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.DataSourceInfo"&gt;DataSourceInfo[]&lt;/see&gt; dataSourceInfos, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		function onQueryComplete(responseText, userContext){
			if(responseText == null || responseText == ""){
				onComplete(null, userContext);
				return;
			}
			var result = eval(responseText);
			var dataSourceInfos = new Array();
			for(var i = 0; i < result.length; i++){
				dataSourceInfos.push(SuperMap.DataSourceInfo.fromJson(result.pop()));
			}
			if(onComplete){
				onComplete(dataSourceInfos, userContext);
			}
		};
        SuperMap.Committer.commitAjax("", this.queryUrl, "getDataSourceInfos", null, null, false, onQueryComplete, onError, userContext);
	},
	getDatasetInfos:function(dataSourceName, onComplete, onError, userContext){
		/// <summary>获取指定数据源中存储的所有数据集的信息。</summary>
		/// <param name="dataSourceName" type="String">数据源名称。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.DatasetInfo"&gt;DatasetInfo[]&lt;/see&gt; datasetInfo, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		function onQueryComplete(responseText, userContext){
			if(responseText == null || responseText == ""){
				onComplete(null, userContext);
				return;
			}
			var result = eval(responseText);
			var datasetInfos = new Array();
			for(var i = 0; i < result.length; i++){
				var obj = result[i];
				datasetInfos.push(SuperMap.DatasetInfo.fromJson(obj));
			}
			if(onComplete){
				onComplete(datasetInfos, userContext);
			}
		};
        SuperMap.Committer.commitAjax("", this.queryUrl, "getDatasetInfos", ["dataSourceName"], [dataSourceName], false, onQueryComplete, onError, userContext);
	},
	queryByGeometry:function(dataSourceName, geometry, mode, queryParam, onComplete, onError, userContext){
		/// <summary>查询与指定的几何对象符合某种空间关系和查询条件的几何对象。 </summary>
		/// <param name="dataSourceName" type="String">指定的几何对象所属的数据源。</param>
		/// <param name="geometry" type="SuperMap.Geometry">指定的几何对象，queryByGeometry方法会查找与该对象符合某空间关系的几何对象。</param>
		/// <param name="mode" type="Number">空间查询模式。&lt;br&gt;
		/// 定义了一些几何对象之间的空间位置关系，根据这些空间关系来构建过滤条件执行查询。
		/// 例如：查询可被包含在面对象中的空间对象，与面有相离或者相邻关系的空间对象等。&lt;br&gt;
		/// 每一个数值对应一种空间关系，具体数值与空间关系的对应请参见supermap.commontypes.SpatialQueryMode的说明。</param>
		/// <param name="queryParam" type="SuperMap.QueryParam">查询参数。可以设置查询的过滤条件，查询图层等。SuperMap.QueryParam 类型。</param>
	   /// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.ResultSet"&gt;ResultSet&lt;/see&gt; resultSet, String userContext) {};
       /// </param>
	  /// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	  /// </param>
	  /// <param name="userContext" type="Function">上下文环境。</param>
		this.queryBase("query", ["dataSourceName", "geometry", "mode", "queryParam"], [dataSourceName, geometry, mode, queryParam], onComplete, onError, userContext);
	},
	dispose:function(){
	/// <summary>释放对象资源。</summary>
	}
};
SuperMap.DataManager.registerClass('SuperMap.DataManager', null, Sys.IDisposable);
