//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.Decoder.js  
// 功能：			解码器
// 最后修改时间：	2007-8-23 
//========================================================================== 

Type.registerNamespace('SuperMap');

SuperMap.Decoder = function(){
/// <summary>该类主要用于将一个JSON对象转化成脚本对象。</summary>
/// <returns type="SuperMap.Decoder">返回 Decoder 对象。</returns>
};
SuperMap.Decoder.fromJson = function(object, json){
/// <summary>将一个JSON字符串转化成脚本对象。</summary>
/// <param name="object" type="Object">转换后的对象。</param>
/// <param name="json" type="Object">JSON对象。</param>
    if(!json){return;}
    for(var m in json){
        if(typeof(object[m]) == "object"){
            SuperMap.Decoder.fromJson(object[m], json[m]);
//        } else if(typeof(object[m]) != "function" && typeof(object[m]) != "undefined"){
        } else if(typeof(object[m]) != "function"){
            object[m] = json[m];
        }
    }
};
SuperMap.Decoder.fromXml = function(object, xml){
/// <summary>将一个XML字符串转化成脚本对象。</summary>
/// <param name="object" type="Object">转换后的对象。</param>
/// <param name="xml" type="String">XML字符串。</param>
};
SuperMap.Decoder.registerClass('SuperMap.Decoder', null, Sys.IDisposable);