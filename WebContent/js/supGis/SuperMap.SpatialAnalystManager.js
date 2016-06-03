//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：		SuperMap.SpatialAnalystManager.js  
// 功能：			SuperMap 空间分析功能  
// 最后修改时间：	2006-12-12 
//========================================================================== 
Type.registerNamespace("SuperMap");
SuperMap.SpatialAnalystManager=function(mapHandler){
	/// <summary>空间分析操作类。该类主要提供各种空间分析方法，如叠加分析、缓冲区分析等。</summary>
	/// <param name="mapHandler" type="String">空间操作对象将请求提交到服务端的代理器的访问地址。&lt;br&gt;
	/// 客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
    /// handler会将GIS请求的处理结果返回给客户端。因此该属性主要是用来设置命令提交器提交请求的服务端代理器的地址。</param>
	/// <returns type="SuperMap.SpatialAnalystManager">返回 SpatialAnalystManager 对象。</returns>
	/// <field name="queryUrl" type="String">GIS数据处理代理器的地址。</field>
	
    this.queryUrl = mapHandler;
	this.onComplete = null;
    this.onError = null;
    // 用于注册事件处理函数。
    // onComplete, 原型参数: onComplete(resultSet); 
    // onError, 原型参数: onError(responseText); 
};
SuperMap.SpatialAnalystManager.prototype={
    overlayAnalyst:function(overlayParam, onComplete, onError, userContext){
	/// <summary>叠加分析。&lt;br&gt;
	///	1. 叠加分析是指在两个数据集之间进行的一系列集合运算，是GIS中的一项非常重要的空间分析功能。
	/// 比如，我们需要了解某一个行政区内的土壤分布情况，我们就根据全国的土地利用图和行政区规划图这两个数据集进行叠加分析，
	/// 然后得到我们需要的结果，从而进行各种分析评价。 &lt;br&gt;
	///	2. 叠加分析涉及到两个数据集，其中一个数据集为操作数据集，必须为面数据集，另一个数据集为被操作数据集， 
	/// 除合并运算和对称差运算必须是面数据集外，其他运算可以是点、线、面或者复合数据集。可以进行点与面的叠加、 线与面的叠加、
	/// 多边形与面的叠加。 &lt;br&gt;
	///	3. 进行叠加分析之前，根据OverlayParam设置叠加分析的条件， 包括操作数据集的名称operateDatasetName、叠加分析的类型Operation等。 
	/// 叠加分析的结果通过OverlayResult获取，包括叠加分析结果数据集resultDatasetName 和叠加分析操作是否成功Succeed。
	/// </summary>
	/// <param name="overlayParam" type="SuperMap.OverlayParam">叠加分析参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.OverlayResult"&gt;OverlayResult&lt;/see&gt; overlayResult, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取并整理服务端返回结果的事件处理函数。该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>	
		function onOverlayAnalystComplete(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var overlayResult = SuperMap.OverlayResult.fromJson(responseResult);
			if(onComplete){
				onComplete(overlayResult, userContext);
			}
			if(overlayResult != null){
				overlayResult.dispose();
				overlayResult = null;
			}
		};
        SuperMap.Committer.commitAjax("", this.queryUrl, methodName, ["overlayParam"], [overlayParam], false, onOverlayAnalystComplete, onError, userContext);
	},
	
	spatialOperate:function(sourceGeometry, operatorGeometry, spatialOperationType, onComplete, onError, userContext) {
	/// <summary>使用几何对象（operatorGeometry）对几何对象（sourceGeometry）进行空间运算，如相交、相并、相减、对称差、裁减等运算。 &lt;br&gt;
	/// 几何对象的空间运算是指对几何对象source进行相交、合并等操作。
	/// 相交、合并、裁减等操作被称为运算因子（空间操作类型）。
	/// 几何对象source称作被操作对象，几何对象param称作操作对象。
	/// 用操作对象去对被操作对象进行空间运算，从而得到一个新的几何对象，这个新的几何对象 就是对被操作对象的运算结果。
	/// </summary>
	/// <param name="sourceGeometry" type="SuperMap.Geometry">进行空间运算的被操作的几何对象。
	/// </param>
	/// <param name="operatorGeometry" type="SuperMap.Geometry">进行空间运算的操作的几何对象。
	/// </param>
	/// <param name="spatialOperationType" type="Number" >空间操作类型，如相交、相并、相减、对称差、裁减等。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.Geometry"&gt;Geometry&lt;/see&gt; geometry, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取错误信息的事件处理函数。该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
		function onSpatialOperateCompleted(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var result = SuperMap.Geometry.fromJson(responseResult);
			if(onComplete){
				onComplete(result, userContext);
			}
			if(result != null){
				result.dispose();
				result = null;
			}
		};
		// mapName 设置为""
		var spatialOperationParam = new Object();
		spatialOperationParam.spatialOperationType = spatialOperationType;
        SuperMap.Committer.commitAjax("", this.queryUrl, "spatialOperate", ["sourceGeometry", "operatorGeometry", "spatialOperationParam"], [sourceGeometry, operatorGeometry, spatialOperationParam], false, onSpatialOperateCompleted, onError, userContext);
	},
	
	distance:function(sourceGeometry, targetGeometry, onComplete, onError, userContext) {
	/// <summary>计算两个几何对象间的最小距离。&lt;br&gt;
	/// 1. 两个几何对象之间的空间距离是基于地理位置的，反映了几何对象之间的接近程度，从形式上来说，空间几何对象可以分为点、线、面三类，
	/// 根据各类空间几何对象之间的组合，就表现出更多的形式，归纳起来，可以概括成六种形式：点点、点线、点面、线线、线面、面面。&lt;br&gt;
	/// 2. 接口 Distance 指的是空间几何对象上空间位置最近的两点之间的距离，如果空间对象相交，则距离为0。
	/// </summary>
	/// <param name="sourceGeometry" type="SuperMap.Geometry">进行距离计算的第一个对象。
	/// </param>
	/// <param name="targetGeometry" type="SuperMap.Geometry">进行距离计算的第二个对象。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(Double result, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取并整理服务端返回结果的事件处理函数。该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>		
		function onDistanceCompleted(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var result = eval(responseResult);
			if(onComplete){
				onComplete(result, userContext);
			}
		};
		// mapName 设置为""
        SuperMap.Committer.commitAjax("", this.queryUrl, "spatialOperate", ["sourceGeometry", "targetGeometry", "spatialOperationParam"], [sourceGeometry, targetGeometry], false, onDistanceCompleted, onError, userContext);
	},
	
	buffer:function(geometry, bufferAnalystParam, onComplete, onError, userContext) {
	/// <summary>创建几何对象的缓冲区。&lt;br&gt;
	/// 1. 缓冲区实际上是在基本空间要素周围建立的具有一定宽度的邻近区域。&lt;br&gt;
	/// 2. 缓冲区分析可以有以下应用，比如确定街道拓宽的范围，确定放射源影响的范围等等。
	/// </summary>
	/// <param name="geometry" type="SuperMap.Geometry">要生成缓冲区的几何对象。Geometry对象。
	/// </param>
	/// <param name="bufferAnalystParam" type="SuperMap.BufferAnalystParam">建立缓冲区的相关参数。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(&lt;see cref="T:SuperMap.Geometry"&gt;Geometry&lt;/see&gt; geometry, String userContext) {};
    /// </param>
	/// <param name="onError" type="Function">用于获取并整理服务端返回结果的事件处理函数。该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="String">用户上下文(可以为空)。</param>	
		function onBufferCompleted(responseResult, userContext){
			if(responseResult == null){
				onComplete(null, userContext);
				return;
			}
			var result = SuperMap.Geometry.fromJson(responseResult);
			if(onComplete){
				onComplete(result, userContext);
			}
			if(result != null){
				result.dispose();
				result = null;
			}
		};
		// mapName 设置为""
        SuperMap.Committer.commitAjax("", this.queryUrl, "spatialOperate", ["geometry", "bufferAnalystParam"], [geometry, bufferAnalystParam], false, onBufferCompleted, onError, userContext);
	}
};
SuperMap.SpatialAnalystManager.registerClass('SuperMap.SpatialAnalystManager', null, Sys.IDisposable)