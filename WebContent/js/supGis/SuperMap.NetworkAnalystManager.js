//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：		SuperMap.NetworkAnalystManager.js  
// 功能：			SuperMap 空间分析功能  
// 最后修改时间：	2007-12-18 
//========================================================================== 
Type.registerNamespace("SuperMap");
SuperMap.NetworkAnalystManager=function(mapHandler){
	/// <summary>网络分析操作类。该类主要提供各种网络分析方法，如路径分析、最近设施分析等。</summary>
	/// <param name="mapHandler" type="String">网络操作对象将请求提交到服务端的代理器的访问地址。&lt;br&gt;
	/// 客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
    /// handler会将GIS请求的处理结果返回给客户端。因此该属性主要是用来设置命令提交器提交请求的服务端代理器的地址。</param>
	/// <returns type="SuperMap.NetworkAnalystManager">返回 NetworkAnalystManager 对象。</returns>
	/// <field name="queryUrl" type="String">GIS数据处理代理器的地址。</field>
	
	 // 如"http://localhost:7080/demo/networkAnalysthandler"
    this.queryUrl = mapHandler;
};
SuperMap.NetworkAnalystManager.prototype={
	dispose:function(){
	/// <summary>释放对象占用的资源。</summary>
	},
	 _findPathBase:function(methodName, paramNames, paramValues,onComplete,onError,userContext){
        function onFindPathComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var result = SuperMap.NetworkAnalystResult.fromJson(responseResult);
			if(onComplete){
				onComplete(result, userContext);
			}
			if(result != null){
				result.dispose();
				result = null;
			}
		};
        SuperMap.Committer.commitAjax(this.mapName, this.queryUrl, methodName, paramNames, paramValues, false, onFindPathComplete, onError, userContext);
    },
	
	findPath:function(networkSetting, pathParam, onComplete, onError, userContext){
	/// <summary>查找网络中结点间的最短路径或者最佳路径。&lt;br&gt;
	///		1. 查找结果放入NetworkAnalystResult内。&lt;br&gt;
	///		2. 网络中两结点间最短路径，通常是根据距离来计算的；而最佳路径则根据两点间所有弧段的正向和反向的阻值字段、
	/// 转向表等多种因素一起计算出两点间的最佳路径。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="pathParam" type="SuperMap.PathParam">路径分析参数。PathParam类型。路径分析功能必选参数。
	/// 可以通过PathParam.networkAnalystParam来设置路径分析需要的各种参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>
		this._findPathBase("findpath", ["pathParam", "networkSetting"],[pathParam, networkSetting], onComplete, onError, userContext);
    },

	_closestFacilityBase:function(methodName, paramNames, paramValues, onComplete, onError, userContext){
        function onClosestFacilityComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var proximityResult = SuperMap.ProximityResult.fromJson(responseResult);
			if(onComplete){
				onComplete(proximityResult, userContext);
			}
			if(proximityResult != null){
				proximityResult.dispose();
				proximityResult = null;
			}
		};
        SuperMap.Committer.commitAjax("", this.queryUrl, methodName, paramNames, paramValues, false, onClosestFacilityComplete, onError, userContext);
    },
	
	findTSPPath:function(networkSetting, tspPathParam, onComplete, onError, userContext){
	/// <summary>旅行商分析。&lt;br&gt;
	///		1. 查找结果放入NetworkAnalystResult内。&lt;br&gt;
	///		2. 通过指定出发和到达的位置及所有需要经过的游历点，
	/// 查找相对最佳的游历路线，保证每个游历点只经过一次的情况下，总耗费最小。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="tspPathParam" type="SuperMap.TSPPathParam">旅行商分析参数。TSPPathParam类型。旅行商分析功能必选参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
		this._findPathBase("findTSPPath", ["tspPathParam", "networkSetting"], [tspPathParam, networkSetting], onComplete, onError, userContext);
	},
	_serviceAreaBase:function(methodName, paramNames, paramValues,onComplete,onError,userContext){
        function onServiceAreaAnalystComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var result = SuperMap.ServiceAreaResult.fromJson(responseResult);
			if(onComplete){
				onComplete(result, userContext);
			}
			if(result != null){
				result.dispose();
				result = null;
			}
		};
        SuperMap.Committer.commitAjax("", this.queryUrl, methodName, paramNames, paramValues, false, onServiceAreaAnalystComplete, onError, userContext);
    },
	serviceArea:function(networkSetting, serviceAreaParam, onComplete, onError, userContext){
	/// <summary>服务区分析。&lt;br&gt;
	/// 1. 关于服务区及服务区分析：&lt;br&gt;
	///		服务区：以指定点为中心，在一定阻力范围内，包含所有可通达边的一个区域。&lt;br&gt;
	///		服务区分析：为网络上指定的位置点计算服务范围。例如：为网络上某点计算其30分钟的服务区，则结果服务区内，任意点出发到该点的时间都不会超过30分钟。&lt;br&gt;
	/// 2. 服务区分析步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过ServiceAreaParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；&lt;br&gt;
	/// （3）准备服务中心点信息。通过ServiceAreaParam.networkAnalystParam.point2Ds或者nodeIDs设置各个服务中心点，可以是普通坐标点，也可以是网络结点，
	/// 之后设置每个服务中心点的服务阻值ServiceAreaParam.weights（以阻力为单位，该单位基于网络分析环境中阻力字段的单位）。&lt;br&gt;
	/// （4）服务区分析。通过NetworkAnalystManager.serviceArea方法，传入服务中心点和服务半径，设定计算方向。&lt;br&gt;
	/// 3. 服务区分析结果：&lt;br&gt;
	/// 分析成功之后，通过ServiceAreaResult返回两类结果,一个表示各个服务中心点的服务路径，另一个表示各个服务中心点的服务区域。
	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="serviceAreaParam" type="SuperMap.ServiceAreaParam">服务区分析参数。ServiceAreaParam 类型。服务区分析功能必选参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>	
		this._serviceAreaBase("serviceArea", ["serviceAreaParam", "networkSetting"], [serviceAreaParam, networkSetting], onComplete, onError, userContext);
	},
	closestFacilityByPoint:function(networkSetting, point, proximityParam, onComplete, onError, userContext){
	/// <summary>最近设施查找。&lt;br&gt;
	/// 1. 最近设施查找：&lt;br&gt;
	///		为事件点查找以最小耗费能到达的设施点，结果显示从事件点到设施点的最佳路径，耗费，及行驶方向。该功能还可以设置查找的阈值，
	/// 即搜索范围，超过该范围将不再进行查找，例如事件发生点是一起交通事故，要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。&lt;br&gt;
	/// 此例中，事故发生地即是一个事件点，周边的医院则是设施点。最近设施查找实际上也是一种路径分析，因此，同样可以应用障碍边和障碍点的设置，
	/// 在行驶路途上这些障碍将不能被穿越，在路径分析中会予以考虑。&lt;br&gt;
	/// 2. 最近设施查找的步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过proximityParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；
	/// 通过proximityParam 对象设定查找的设施个数，设定查找的方向，设定查找范围（单位同网络分析环境中设置的阻力字段，如不限定查找范围，该值可设为0）。&lt;br&gt;
	/// （3）事件点和设施点准备。该方法每次为一个事件点进行最近设施查找，该点是在分析区域内的坐标点，也可以是网络结点。
	/// 设施点通常由多个点组成，同样既可以是坐标点，也可以是网络结点，通常设施点通过proximityParam.networkAnalystParam.point2Ds设定。&lt;br&gt;
	/// （4）最近设施查找。通过NetworkAnalystManager.closestFacilityByPoint方法，传入事件点(point),最近设施查询的参数（proximityParam）。&lt;br&gt;
	/// （5）最近设施查找结果。该结果是NetworkAnalystResult类型，其中从事件点到设施点或者从设施点到事件点的最佳路径。&lt;br&gt;
	/// 3. 需要注意：事件点和设施点的类型必须一致，都是坐标点或者都是网络结点，否则该方法会失败。因此使用该方法时，设施点必须通过通过proximityParam.networkAnalystParam.point2Ds设定。
 	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="point" type="SuperMap.Point2D">事件点坐标（Point2D）。 
	/// </param>
	/// <param name="proximityParam" type="SuperMap.ProximityParam">最近设施分析参数。ProximityParam 类型。最近设施分析功能必选参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
	    this._findPathBase("ClosestFacilityByPoint2D", ["point2D", "proximityParam", "networkSetting"], [point, proximityParam, networkSetting], onComplete, onError, userContext);
	},
	
   closestFacilityByEventID:function(networkSetting, eventID, proximityParam, onComplete, onError, userContext){
	/// <summary>最近设施查找。该方法仅限于在同一个网络数据集中进行最近设施查找。&lt;br&gt;
	/// 1. 最近设施查找：&lt;br&gt;
	///		为事件点查找以最小耗费能到达的设施点，结果显示从事件点到设施点的最佳路径，耗费，及行驶方向。该功能还可以设置查找的阈值，
	/// 即搜索范围，超过该范围将不再进行查找，例如事件发生点是一起交通事故，要求查找在10分钟内能到达的最近医院，超过10分钟能到达的都不予考虑。&lt;br&gt;
	///		此例中，事故发生地即是一个事件点，周边的医院则是设施点。最近设施查找实际上也是一种路径分析，因此，同样可以应用障碍边和障碍点的设置，
	/// 在行驶路途上这些障碍将不能被穿越，在路径分析中会予以考虑。&lt;br&gt;
	/// 2. 最近设施查找的步骤：&lt;br&gt;
	/// （1）准备网络数据集。网络数据集要求带有弧段标识、正向、反向阻值字段；结点数据集中带有结点标识字段；&lt;br&gt;
	/// （2）网络分析环境设置。通过proximityParam.networkAnalystParam 设置弧段标识字段、结点标识字段、正反向阻力值和分析弧段过滤条件等；
	/// 通过proximityParam 对象设定查找的设施个数，设定查找的方向，设定查找范围（单位同网络分析环境中设置的阻力字段，如不限定查找范围，该值可设为0）。&lt;br&gt;
	/// （3）事件点和设施点准备。该方法每次为一个事件点进行最近设施查找，该点必须是与设施点在相同网络数据集中的网络结点，而且必须通过该点的SMID值设定。
	/// 设施点通常由多个点组成，同样该点必须是与事件点在相同网络数据集中的网络结点，通常设施点通过proximityParam.networkAnalystParam.nodeIDs设定。&lt;br&gt;
	/// （4）最近设施查找。通过NetworkAnalystManager.closestFacilityByPoint方法，传入事件点id(eventID),最近设施查询的参数（proximityParam）。&lt;br&gt;
	/// （5）最近设施查找结果。该结果是NetworkAnalystResult类型，其中从事件点到设施点或者从设施点到事件点的最佳路径。&lt;br&gt;
	/// 3. 需要注意：该方法仅支持事件点和设施点都是同一网络数据集的网络结点，否则该方法会失败。因此使用该方法时，设施点必须通过通过proximityParam.networkAnalystParam.nodeIDs设定。
 	/// </summary>
	/// <param name="networkSetting" type="SuperMap.NetworkModelSetting">网络数据的模型参数设置对象。</param>
	/// <param name="eventID" type="Number">事件点的SMID值。 
	/// </param>
	/// <param name="proximityParam" type="SuperMap.ProximityParam">最近设施分析参数。ProximityParam 类型。最近设施分析功能必选参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.NetworkAnalystResult"&gt;NetworkAnalystResult&lt;/see&gt; networkAnalystResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
	    this._findPathBase("ClosestFacilityByEventID", ["eventID", "proximityParam", "networkSetting"], [eventID, proximityParam, networkSetting], onComplete, onError, userContext);
	}
};
SuperMap.NetworkAnalystManager.registerClass('SuperMap.NetworkAnalystManager', null, Sys.IDisposable)