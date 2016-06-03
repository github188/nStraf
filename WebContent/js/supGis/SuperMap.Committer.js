//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iSserver Team 
// 修改：	 
// 文件名：			SuperMap.Committer.js  
// 功能：			命令提交器
// 最后修改时间：	2007-8-31
//========================================================================== 

Type.registerNamespace('SuperMap');
SuperMap.Committer = function(){
	/// <summary>
	/// 命令提交器。客户端的脚本对象对GIS请求通过该命令提交器提交给对应的handler（代理器）处理。handler再与GIS服务器交互，
	/// 再将GIS处理结果返回给客户端的事件处理函数。&lt;br&gt;
	/// 命令提交器主要有三部分处理内容。第一，整理脚本对象提出的GIS请求的参数；第二，设置与GIS服务器交互的方式是否为异步传输；
	/// 第三，指定用于处理返回结果的事件处理函数及其上下文环境。因此使用命令提交器的方法提交GIS请求时，需要提交以上三部分的内容。
	/// </summary>
	/// <returns type="SuperMap.Committer">返回 Committer 对象。</returns>
	/// <field name="handler" type="String" static="true">命令提交器将请求提交到服务端的代理器的访问地址。客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
	/// handler会将GIS请求的处理结果返回给客户端。因此该属性主要是用来设置命令提交器提交请求的服务端代理器的地址。</field>
};

// 所有提交到SuperMap.Committer.handler
SuperMap.Committer.handler = "http://localhost:7080/demo/commonhandler";
SuperMap.Committer.commitMapCmd = function(mapName, method, paramNames, paramValues, onComplete, onError, userContext){
    /// <summary>
	/// 向Web服务端提交GIS请求。该方法将GIS请求传输给默认的代理器，即SuperMap.Committer.handler的默认地址。同时该方法将会
	/// 以异步传输的方式获取处理结果。
	/// </summary>
	/// <param name="mapName" type="String">地图名称。该值不能为空。</param>
	/// <param name="method" >地图命令。&lt;br&gt;
	/// (1)地图命令主要用来表达需要服务端执行何种地图操作以实现对应的GIS功能,
	/// 每一个GIS功能都会对应一个地图命令和一些必要的地图参数，如SQL查询的操作对应的地图命令是“QueryBySql”，对应的地图参数有"queryParam", "needHighlight"等。
	/// 具体的地图命令及其参数列表请参考联机帮助中的详细说明。
	/// &lt;br&gt;(2)该值可以是字符串也可以是数组。如果该值为数组型，即多个地图命令以数组的形式存储于该参数中，
	/// 表示此次请求要执行多个地图操作,服务器对这些命令按照数组的顺序一次进行处理。
	/// </param>
	/// <param name="paramNames" type="Array" elementType="String">命令参数的名称数组。执行一个GIS功能，除了提交地图命令外，还需要提交相关的地图参数。
	/// 如SQL查询需要的地图参数有"queryParam", "needHighlight"等。</param>
	/// <param name="paramValues" type="Array" elementType="Object">命令参数的值数组。paramNames对应的参数值。
	/// </param>
	/// <param name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt;
	/// (1)该值可以是Function类型，也可以是Function数组类型。
	/// &lt;br&gt;(2)通常，一次提交多个地图命令时（即method参数为数组时），可以通过该参数
	/// 设置多个事件处理函数来接收每一个地图命令的处理结果，也可以设置一个事件处理函数来处理所有命令的返回结果。
	/// &lt;br&gt;(3)onComplete函数中object参数的类型是某个具体操作的返回值类型，比如进行距离量算操作，object参数的类型为MeasureResult。
	/// </param>
	/// <param name="onError" type="Function" mayBeNull="true">用于获取错误信息的事件处理函数。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="Function" mayBeNull="true">用户上下文(可以为空)。
	/// </param>
	SuperMap.Committer.commitAjax(mapName, SuperMap.Committer.handler, method, paramNames, paramValues, false, onComplete, onError, userContext);
};

SuperMap.Committer.commitAjax = function(mapName, handler, method, paramNames, paramValues, isSynchronized, onComplete, onError, userContext){
    /// <summary>
	/// 向Web服务端提交GIS请求。
	/// </summary>
	/// <param name="mapName" type="String">地图名称。该值不能为空。</param>
	/// <param name="handler" type="String">命令提交器将请求提交到服务端代理器的地址。客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
	/// handler会将GIS请求的处理结果返回给客户端。</param>
	/// <param name="method">地图命令。&lt;br&gt;
	/// 1. 地图命令主要用来表达需要服务端执行何种地图操作以实现对应的GIS功能,
	/// 每一个GIS功能都会对应一个地图命令和一些必要的地图参数，如SQL查询的操作对应的地图命令是“QueryBySql”，对应的地图参数有"queryParam", "needHighlight"等。
	/// 具体的地图命令及其参数列表请参考联机帮助中的详细说明。&lt;br&gt;
	/// 2. 该值可以是字符串也可以是数组。如果该值为数组型，即多个地图命令以数组的形式存储于该参数中，
	/// 表示此次请求要执行多个地图操作,服务器对这些命令按照数组的顺序一次进行处理。
	/// </param>
	/// <param name="paramNames" type="Array" elementType="String">命令参数的名称数组。执行一个GIS功能，除了提交地图命令外，还需要提交相关的地图参数。
	/// 如SQL查询需要的地图参数有"queryParam", "needHighlight"等。</param>
	/// <param name="paramValues" type="Array" elementType="Object">命令参数的值数组。paramNames对应的参数值。
	/// </param>
	/// <param name="isSynchronized" type="Boolean">该处理过程是否为同步处理方式。true表示同步处理；false，表示异步处理。
	/// </param>
	/// <param name="onComplete" type="Function" mayBeNull="true">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(Object object, String userContext) {};
	/// &lt;br&gt;1. 该值可以是字符串类型，也可以是数组类型。
	/// &lt;br&gt;2. 通常，一次提交多个地图命令时（即mehod参数为数组时），可以通过该参数
	/// 设置多个事件处理函数来接收每一个地图命令的处理结果，也可以设置一个事件处理函数来处理所有命令的返回结果。
	/// &lt;br&gt;3. onComplete函数中object参数的类型是某个具体操作的返回值类型，比如进行距离量算操作，object参数的类型为MeasureResult。
	/// </param>
	/// <param name="onError" type="Function" mayBeNull="true">用于获取错误信息的事件处理函数的名称该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="Function" mayBeNull="true">用户上下文(可以为空)。
	/// </param>
    var mapName = encodeURI(mapName);

    var requestParams = "";
	//  区分单个和多个命令的情形
	if(typeof(method) == "string"){
		requestParams += "<uis><command><name>" + method + "</name>";
		requestParams += "<parameter>";
		requestParams += "<mapName>";
		requestParams += mapName;
		requestParams += "</mapName>";
		if(typeof(paramNames) != "undefined" && paramNames != null && typeof(paramValues) != "undefined" && paramValues != null){
			var i, count;
			count = Math.min(paramNames.length, paramValues.length);
			
			for(i = 0; i < count; i++){
				var paramValue;
				if(paramValues[i] != "undefined" && paramValues[i] != null){
				    if(typeof(paramValues[i]) == "string" && paramValues[i].length > 0){
					    var re;
					    re = /%/g;
//					    paramValue = (paramValues[i] + "").replace(re, "%25");// 做一下转字符串处理
					    re = /&/g;
//					    paramValue = paramValue.replace(re, "%26");
				        paramValue = paramValues[i];
				    } else if (typeof(paramValues[i]) == "object"){
					    paramValue = SuperMap.Utility.toJSON(paramValues[i]);
				    } else {
				        paramValue = paramValues[i];
				    }
				}else{
				    paramValue = "";
				} 
				requestParams += "<" + paramNames[i] + ">";
				requestParams += paramValue;
				requestParams += "</" + paramNames[i] + ">";
			}
		}
		requestParams += "</parameter>";
		requestParams += "</command></uis>";
	} else if(typeof(method) == "array"){
		requestParams += "%3Cuis%3E";
		for(var i = 0; i  < method.length; i++){
			requestParams += "%3Ccommand%3E%3Cname%3E" + method[i] + "%3C%2Fname%3E";
			requestParams += "%3Cparameter%3E";
			requestParams += "%3CmapName%3E";
			requestParams += mapName;
			requestParams += "%3C%2FmapName%3E";
			var paramNamesItem = paramNames.pop();
			var paramValuesItem = paramValues.pop();
			
			if(typeof(paramNamesItem) != "undefined" && paramNamesItem != null && typeof(paramValuesItem) != "undefined" && paramValuesItem != null){
				var i, count;
				count = Math.min(paramNamesItem.length, paramValuesItem.length);
				
				for(i = 0; i < count; i++){
					var paramValue;
					if(paramValuesItem[i] != "undefined" && paramValuesItem[i] != null){
					    if(typeof(paramValuesItem[i]) == "string" && paramValuesItem[i].length > 0){
						    var re;
						    re = /%/g;
						    paramValue = (paramValuesItem[i] + "").replace(re, "%25");// 做一下转字符串处理
						    re = /&/g;
						    paramValue = paramValue.replace(re, "%26");
					    } else if (typeof(paramValuesItem[i]) == "object"){
						    paramValue = SuperMap.Utility.toJSON(paramValuesItem[i]);
					    } else {
					        paramValue = paramValuesItem[i];
					    }
					}else{
					    paramValue = "";
					} 
					requestParams += "%3C" + paramNamesItem[i] + "%3E";
					requestParams += encodeURIComponent(paramValue);
					requestParams += "%3C%2F" + paramNamesItem[i] + "%3E";
				}
			}
			requestParams += "%3C%2Fparameter%3E";
			requestParams += "%3C%2Fcommand%3E";
		}
		requestParams += "%3C%2Fuis%3E";
	}
	var commandName = "";
	if(typeof(method) == "string") {
		commandName = method;
	} else if (typeof(method) == "object"){
		commandName = SuperMap.Utility.toJSON(method);
	}

    if(SuperMap.Utility.isInTheSameDomain(handler)){
	    var xHRSender = new SuperMap.XHRSender(handler, "mapName=" + mapName + "&params=" + requestParams + "&method=" + commandName + "&t=" + new Date().getTime());
        return xHRSender.send(onComplete, onError, isSynchronized, userContext);
    }else{
	    var jSONPSender = new SuperMap.JSONPSender(handler, onComplete, onError, userContext);
        jSONPSender.addQueryStrings(["mapName", "params", "method", "t"], [mapName, requestParams, commandName, new Date().getTime()]);
        return jSONPSender.send();
    }
};

SuperMap.Committer.commitForm = function(mapName, method, paramNames, paramValues){
	/// <summary>
	/// 通过表单向Web服务端提交GIS请求。
	/// </summary>
	/// <param name="mapName" type="String">地图名称。该值不能为空。</param>
	/// <param name="handler" type="String">命令提交器将请求提交到服务端代理器的地址。客户端脚本对象的GIS请求通过命令提交器提交给指定的Web服务端的handler（代理器），
	/// handler会将GIS请求的处理结果返回给客户端。</param>
	/// <param name="method">地图命令。&lt;br&gt;
	/// 1. 地图命令主要用来表达需要服务端执行何种地图操作以实现对应的GIS功能,
	/// 每一个GIS功能都会对应一个地图命令和一些必要的地图参数，如SQL查询的操作对应的地图命令是“QueryBySql”，对应的地图参数有"queryParam", "needHighlight"等。
	/// 具体的地图命令及其参数列表请参考联机帮助中的详细说明。&lt;br&gt;
	/// 2. 该值可以是字符串也可以是数组。如果该值为数组型，即多个地图命令以数组的形式存储于该参数中，
	/// 表示此次请求要执行多个地图操作,服务器对这些命令按照数组的顺序一次进行处理。
	/// </param>
	/// <param name="paramNames" type="Array" elementType="String">命令参数的名称数组。执行一个GIS功能，除了提交地图命令外，还需要提交相关的地图参数。
	/// 如SQL查询需要的地图参数有"queryParam", "needHighlight"等。</param>
	/// <param name="paramValues" type="Array" elementType="Object">命令参数的值数组。paramNames对应的参数值。
	/// </param>
	/// <param name="userContext" type="Function" mayBeNull="true">用户上下文(可以为空)。
	/// </param>
};

SuperMap.Committer.registerClass('SuperMap.Committer', null, Sys.IDisposable);

SuperMap.XHRSender = function(url, requestParams){
	/// <summary>
	/// AJAX 提交器。
	/// </summary>
	/// <param name="url" type="String">AJAX 请求地址，url + requestParams 作为一个完整的 AJAX 命令。&lt;br&gt;
	/// 例如，http://localhost:7080/demo/commonhandler。
	/// </param>
	/// <param name="requestParams" type="String">AJAX 请求参数，url + requestParams 作为一个完整的 AJAX 命令。&lt;br&gt;
	/// 例如，http://localhost:7080/demo/commonhandler?a=1&amp;b=2，“?”之后的部分“a=1&amp;b=2”是 requestParams。
	/// </param>
	/// <returns type="SuperMap.XHRSender">返回 XHRSender 对象。</returns>
	/// <field name="url" type="String">AJAX 请求地址，url + requestParams 作为一个完整的 AJAX 命令。&lt;br&gt;
	/// 例如，http://localhost:7080/demo/commonhandler。
	/// </field>
	/// <field name="requestParams" type="String">AJAX 请求参数，url + requestParams 作为一个完整的 AJAX 命令。&lt;br&gt;
	/// 例如，http://localhost:7080/demo/commonhandler?a=1&amp;b=2，“?”之后的部分“a=1&amp;b=2”是 requestParams。
	/// </field>
	this.url = url;
    this.requestParams = requestParams;
};
SuperMap.XHRSender.prototype = {
    send:function(onComplete, onError, isSynchronized, userContext){
	/// <summary>发送 AJAX 请求。</summary>
	/// <param name="onComplete" type="Function" mayBeNull="true">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; function onComplete(Object JSON, String userContext) {};
	/// &lt;br&gt;1. 该值可以是 Function 类型，也可以是 Function 数组。
	/// &lt;br&gt;2. onComplete 函数中 JSON 参数的类型是某个具体操作的返回值类型，比如进行距离量算操作，JSON 参数的类型为 MeasureResult。
	/// </param>
	/// <param name="onError" type="Function" mayBeNull="true">用于获取错误信息的事件处理函数的名称该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="isSynchronized" type="Boolean">该处理过程是否为同步处理方式。true表示同步处理；false，表示异步处理。
	/// </param>
	/// <param name="userContext" type="Function" mayBeNull="true">用户上下文(可以为空)。
	/// </param>
		var xhr = SuperMap.Utility.getXmlHttpRequest();
        if(isSynchronized){ // 阻塞，同步 //
            xhr.open("post", this.url + "?" + this.requestParams, !isSynchronized);
            xhr.send(null);
            if(xhr.status == 200){
			    if(xhr.responseText && xhr.responseText != ""){
		            var responseText = eval('(' + xhr.responseText + ')');
			        if(responseText){
					    if(onComplete){
						    return onComplete(responseText, userContext);
					    }
			        } else {
					    if(onError){
						    return onError(xhr.responseText, userContext);
					    }
			        }
			    } else {
				    if(onError){
					    return onError(xhr.responseText, userContext);
				    }
			    }
            }
        }else{ // 异步 //
	        xhr.open("post", this.url, !isSynchronized);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            xhr.onreadystatechange = function(){
                var readyState = xhr.readyState;
                if (readyState == 4){
	                var status = xhr.status;
	                if(status == 200){
		                if(!xhr.responseText || xhr.responseText == ""){
                            if(onComplete){
    		                    // onComplete可以是数组，支持多个onComplete
			                    if(onComplete instanceof Array){
			                        while(onComplete.length > 0){
			                            var tempOnComplete = onComplete.pop();
			                            if(tempOnComplete){
    			                            tempOnComplete(null, userContext);
			                            }
			                        }
    	                            onComplete = null;
                                }else{
                                    onComplete(null, userContext);
                                }

                            }
                            return;
                        }
		                var responseText = eval('(' + xhr.responseText + ')');
    		            if(onComplete){
    		                // onComplete可以是数组，支持多个onComplete
			                if(onComplete instanceof Array){
			                    while(onComplete.length > 0){
			                        var tempOnComplete = onComplete.pop();
    	                            if(tempOnComplete){
			                            tempOnComplete(responseText, userContext);
			                        }
			                    }
    	                        onComplete = null;
                            }else{
                                onComplete(responseText, userContext);
                            }
			            }
	                }else{
		                if(onError){
                            onError(xhr.responseText, userContext);
                        }
	                }
	                xhr.onreadystatechange = function(){};
	                xhr = null;
	            }
            };
	        xhr.send(this.requestParams);
        }
    }
};
SuperMap.XHRSender.registerClass('SuperMap.XHRSender', null, Sys.IDisposable);

var supermap_callbacks = {};
SuperMap.JSONPSender = function(url, onComplete, onError, userContext) {
	/// <summary>
	/// JSONP 提交器。
	/// </summary>
	/// <param name="url" type="String">JSONP 请求地址。例如，http://localhost:7080/demo/commonhandler?a=1&amp;b=2。&lt;br&gt; 
	/// 与 AJAX 请求相区别的是 JSONP 请求可以将参数分开进行发送，例如，可以第一次发送 http://localhost:7080/demo/commonhandler?a=1，第二次发送http://localhost:7080/demo/commonhandler?b=2。
	/// </param>
	/// <param name="onComplete" type="Function" mayBeNull="true">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; 
	/// function onComplete(Object JSON, String userContext) {};&lt;br&gt;
	/// 1. 该值可以是 Function 类型，也可以是 Function 数组。&lt;br&gt;
	/// 2. onComplete 函数中 JSON 参数的类型是某个具体操作的返回值类型，比如进行距离量算操作，JSON 参数的类型为 MeasureResult。
	/// </param>
	/// <param name="onError" type="Function" mayBeNull="true">用于获取错误信息的事件处理函数的名称该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; 
	/// function onError(String responseText, String userContext){};
	/// </param>
	/// <param name="userContext" type="Function" mayBeNull="true">用户上下文(可以为空)。
	/// </param>
	/// <returns type="SuperMap.JSONPSender">返回 JSONPSender 对象。</returns>
	/// <field name="url" type="String">JSONP 请求地址。&lt;br&gt;
	/// 例如，http://localhost:7080/demo/commonhandler。
	/// </field>
	/// <field name="onComplete" type="Function">用于获取并整理服务端返回结果的事件处理函数。&lt;br&gt; 
	/// function onComplete(Object JSON, String userContext) {};&lt;br&gt;
	/// 1. 该值可以是 Function 类型，也可以是 Function 数组。&lt;br&gt;
	/// 2. onComplete 函数中 JSON 参数的类型是某个具体操作的返回值类型，比如进行距离量算操作，JSON 参数的类型为 MeasureResult。
	/// </field>
	/// <field name="onError" type="Function">用于获取错误信息的事件处理函数的名称该值是字符串类型。当服务器执行地图命令过程中出现异常，则会将异常信息返回给该参数对应的事件处理函数。&lt;br&gt; 
	/// function onError(String responseText, String userContext){};
	/// </field>
	/// <field name="userContext" type="Function">用户上下文(可以为空)。
	/// </field>
	this.url = url;
    if(onComplete){
        this.onComplete = onComplete;
    }
    this.onError = onError;
    this.userContext = userContext;
    
    this._queryKeys = null;
    this._queryValues = null;
    this._splitQuestUrl = null;
    this._limitLength = 1900;//ie:2048,firefox:16000,opera:16000
    if (_GetBrowser() != "ie") {
        this._limitLength = 15900;
    }
};
SuperMap.JSONPSender.prototype = {
    addQueryString:function(key, value) {
    /// <summary>
	/// 添加一个 JSONP 请求参数。
	/// </summary>
	/// <param name="key" type="String">JSONP 请求参数的名称。</param>
	/// <param name="value" type="String">JSONP 请求参数的值。</param>
		if (this._queryKeys == null) {
            this._queryKeys = new Array();
        }
        if (this._queryValues == null) {
            this._queryValues = new Array();
        }
        this._queryKeys.push(key);
        this._queryValues.push(value);
    },
    
    addQueryStrings:function(keys, values) {
    /// <summary>
	/// 添加多个 JSONP 请求参数。
	/// </summary>
	/// <param name="key" type="Array" elementType="String">JSONP 请求参数名称的数组。</param>
	/// <param name="value" type="Array" elementType="String">JSONP 请求参数值的数组。</param>
		if (!keys || keys.length <= 0) { return; }
        if (!values || values.length <= 0) { return; }
        if (keys.length != values.length) { return; }
        if (this._queryKeys == null) {
            this._queryKeys = new Array();
        }
        if (this._queryValues == null) {
            this._queryValues = new Array();
        }
        for (var i = 0; i < keys.length; i++) {
            this._queryKeys.push(keys[i]);
            if(typeof(values[i]) == "string"){
                this._queryValues.push(values[i]);
            }else{
                this._queryValues.push(SuperMap.Utility.toJSON(values[i]));
            }
        }
    },

    send:function() {
    /// <summary>
	/// 发送 JSONP 请求。
	/// </summary>
		if (this.url.length > this._limitLength) {
            return false;
        }
        var uid = new Date().getTime();
        //只用时间还不保险,Demo切换地图是就有可能uid相同,在加上一个4位数的随机码
        var randomNum = Math.floor(Math.random() * 10000);
        uid = uid * 10000 + randomNum;
        var onCompleteDelegate;
        if(this.onComplete){
            if (this.onComplete instanceof Array) {
                onCompleteDelegate = new Array();
                for(var i = 0; i < this.onComplete.length; i ++){
                    if(this.onComplete[i]){
                        var tempDelegete = Function.createDelegate(this, this.onComplete[i]);
                        onCompleteDelegate.push(tempDelegete);
                    }
                }
            }else{
                onCompleteDelegate = Function.createDelegate(this, this.onComplete);
            }
        }
        var userContext = this.userContext;
        supermap_callbacks[uid] = function(json) {
            if(onCompleteDelegate){
                if (onCompleteDelegate instanceof Array) {
                    for(var i = 0; i < onCompleteDelegate.length; i ++){
                        onCompleteDelegate[i](json, userContext);
                    }
                }else{
                    onCompleteDelegate(json, userContext);
                }
            }
            delete supermap_callbacks[uid];
        };
        this.addQueryString("jsonp", "supermap_callbacks[" + uid + "]");
        var url = this.url;
        //此次url中有多少个key
        var keysCount = 0;
        if (this._queryKeys && this._queryKeys.length > 0) {
            for (var i = 0; i < this._queryKeys.length; i++) {
                if (url.length + this._queryKeys[i].length + 2 >= this._limitLength)//+2 for ("&"or"?")and"="
                {
                    if (keysCount == 0) { return false; }
                    if (this._splitQuestUrl == null) {
                        this._splitQuestUrl = new Array();
                    }
                    this._splitQuestUrl.push(url);
                    url = this.url;
                    keysCount = 0;
                    i--;
                } else {
                    if (url.length + this._queryKeys[i].length + 2 + this._queryValues[i].length > this._limitLength) {
                        var leftValue = this._queryValues[i];
                        
                        while (leftValue.length > 0) {
                            var leftLength = this._limitLength - url.length - this._queryKeys[i].length - 2;//+2 for ("&"or"?")and"="
                            if (url.indexOf("?") > -1) {
                                url += "&";
                            } else {
                                url += "?";
                            }
                            var tempLeftValue = leftValue.substring(0, leftLength);
                            var re;
                            re = /%/g;
                            tempLeftValue = tempLeftValue.replace(re, "%25");// 做一下转字符串处理
                            re = /&/g;
                            tempLeftValue = tempLeftValue.replace(re, "%26");
                            re = /</g;
                            tempLeftValue = tempLeftValue.replace(re, "%3C");
                            re = />/g;
                            tempLeftValue = tempLeftValue.replace(re, "%3E");
                            re = /[/]/g;
                            tempLeftValue = tempLeftValue.replace(re, "%2F");
                            url += this._queryKeys[i] + "=" + tempLeftValue;
                            leftValue = leftValue.substring(leftLength);
                            if (tempLeftValue.length > 0) {
                                if (this._splitQuestUrl == null) {
                                    this._splitQuestUrl = new Array();
                                }
                                this._splitQuestUrl.push(url);
                                url = this.url;
                                keysCount = 0;
                            }
                        }
                    } else {
                        keysCount++;
                        if (url.indexOf("?") > -1) {
                            url += "&";
                        } else {
                            url += "?";
                        }
                        var tempLeftValue = this._queryValues[i];
                        var re;
                        re = /%/g;
                        tempLeftValue = tempLeftValue.replace(re, "%25");// 做一下转字符串处理
                        re = /&/g;
                        tempLeftValue = tempLeftValue.replace(re, "%26");
                        re = /</g;
                        tempLeftValue = tempLeftValue.replace(re, "%3C");
                        re = />/g;
                        tempLeftValue = tempLeftValue.replace(re, "%3E");
                        re = /[/]/g;
                        tempLeftValue = tempLeftValue.replace(re, "%2F");
                        url += this._queryKeys[i] + "=" + tempLeftValue;
                    }
                }
            }
            if (this._splitQuestUrl == null) {
                this._splitQuestUrl = new Array();
            }
            this._splitQuestUrl.push(url);
        }
        return this._sendInternal();
    },
    
    _sendInternal:function() {
        if (this._splitQuestUrl && this._splitQuestUrl.length > 0) {
            var jsonpUserID = new Date().getTime();
            for (var i = 0; i < this._splitQuestUrl.length; i++) {
                var script = document.createElement("script");
                var url = this._splitQuestUrl[i];
                if (url.indexOf("?") > -1) {
                    url += "&";
                } else {
                    url += "?";
                }
                url += "sectionCount=" + this._splitQuestUrl.length;
                url += "&sectionIndex=" + i;
                url += "&jsonpUserID=" + jsonpUserID;
                url += "&t=" + new Date().getTime();//防止caching
                script.setAttribute("src", url);
                script.setAttribute("type", "text/javascript");

                if (navigator.userAgent.indexOf("IE") >= 0) {
                    script.onreadystatechange = function() {
                        if (this && ("loaded" == this.readyState || "complete" == this.readyState)) {
                            this.onreadystatechange = null;
                            try{
                                document.body.removeChild(this)
                            }catch(e){
                                if(this.parentNode){
                                    this.parentNode.removeChild(this);
                                }
                                delete this;
                            }
                        }
                    };
                }else{
                    script.onload = function() {
                        script.onload = null;
                        document.body.removeChild(script)
                    };
                }
                document.body.appendChild(script);
            }
            return true;
        }
    }
};
SuperMap.JSONPSender.registerClass('SuperMap.JSONPSender', null, Sys.IDisposable);
