//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.Encoder.js  
// 功能：			编码器
// 最后修改时间：	2007-8-23 
//========================================================================== 

Type.registerNamespace('SuperMap');

// 编码器。
SuperMap.Encoder = function(){
	/// <summary>该类主要用于将一个脚本对象转化成JSON字符串。</summary>
	/// <returns type="SuperMap.Encoder">返回 Encoder 对象。</returns>
};
SuperMap.Encoder.toJson = function(object){
/// <summary>将一个脚本对象转化为JSON字符串。</summary>
/// <param name="object">指定的脚本对象。</param>
/// <returns type="String">JSON字符串。如果object为null，则返回值为"null"。</returns>
	if(object == null)
		return "null";

	switch(object.constructor) {
		case String:
			var s = object; // .encodeURI();
			s = '"' + s.replace(/(["\\])/g, '\\$1') + '"';
			s = s.replace(/\n/g,"\\n");
			s = s.replace(/\r/g,"\\r");
			return s;
		case Array:
			var v = [];
			for(var i=0; i<object.length; i++)
				v.push(SuperMap.Encoder.toJson(object[i])) ;
			return "[" + v.join(", ") + "]";
		case Number:
			return isFinite(object) ? object.toString() : SuperMap.Encoder.toJson(null);
		case Boolean:
			return object.toString();
		case Date:
			var d = new Object();
			d.__type = "System.DateTime";
			d.Year = object.getUTCFullYear();
			d.Month = object.getUTCMonth() +1;
			d.Day = object.getUTCDate();
			d.Hour = object.getUTCHours();
			d.Minute = object.getUTCMinutes();
			d.Second = object.getUTCSeconds();
			d.Millisecond = object.getUTCMilliseconds();
			d.TimezoneOffset = object.getTimezoneOffset();
			return SuperMap.Encoder.toJson(d);
		default:
			if(object["toJson"] != null && typeof object["toJson"] == "function")
				return object.toJson();
			if(typeof object == "object") {
				var v=[];
				for(attr in object) {
					if(typeof object[attr] != "function")
						v.push('"' + attr + '":' + SuperMap.Encoder.toJson(object[attr]));
				}

				if(v.length>0)
					return "{" + v.join(", ") + "}";
				else
					return "{}";		
			}
			return object.toString();
	}
};
SuperMap.Encoder.toXml = function(object){
/// <summary>将一个脚本对象转化成XML字符串。</summary>
/// <param name="object">指定的脚本对象。</param>
/// <returns type="String">XML字符串</returns>
};
SuperMap.Encoder.registerClass('SuperMap.Encoder', null, Sys.IDisposable);