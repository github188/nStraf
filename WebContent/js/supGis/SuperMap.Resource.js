//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.Resource.js  
// 功能：			资源化
// 最后修改时间：	2008-10-31
//========================================================================== 
Type.registerNamespace('SuperMap');

SuperMap.Resource = function() {
    /// <summary>资源化操作类。
    /// </summary>
	this._language = null;
};

SuperMap.Resource.prototype = {

	getLanguage: function() {
	/// <summary>获取资源对象的语言类型。</summary>
	/// <returns type="String">语言类型。</returns>
		return this._language;
	},
	
	setLanguage: function(language) {
	/// <summary>设置资源对象的语言类型。</summary>
	/// <param name="language" type="String">语言类型。</param>
		this._language = language;
	},

    getMessage: function(messageId, language, className) {
        /// <summary>提取资源信息。</summary>
        /// <param name="messageId" type="String">String 对象，用于查找资源。</param>
        /// <param name="language" type="String">String 对象，用于选择语言种类。</param>
        /// <param name="className" type="String">String 对象，用于进行资源化扩展。</param>
        /// <returns type="String">资源信息。</returns>
        if (className == null || className == "") {
            try {
                //                return eval("this._getSelectLanguage(language, className).get(" + messageId + ")");
                return this._selectLanguage(language, className).get(messageId);
            } catch (e) {
                return messageId;
            }
        }
        else {
            try {
                var extendClass = eval('(' + "new " + className + "()" + ')');
                return extendClass.get(messageId);
            } catch (e) {
                return "className error!"
            }
        }
    },

    _selectLanguage: function(language) {
        // <summary>语言选择</summary>
        // <param name="language" type="String">String对象，用于选择语言种类。</param>
        // <returns type="Object">资源类对象。</returns>
		language = this._language;
        if (language == null || language == "") {//language为undefined也会进入
            if (navigator.appName == "Microsoft Internet Explorer") {
                if (navigator.browserLanguage == "zh-cn") {
                    return new SuperMap.Zh_Res();
                }
                else if (navigator.browserLanguage == "en-US") {
                    return new SuperMap.En_Res();
                }
				else if (navigator.browserLanguage == "ja-JP") {
					return new SuperMap.Ja_Res();
				}
            }
            else if (navigator.appName == "Netscape") {
                if (navigator.language == "zh-CN") {
                    return new SuperMap.Zh_Res();
                }
                else if (navigator.language == "en-US") {
                    return new SuperMap.En_Res();
                }
				else if (navigator.language == "ja-JP") {
					return new SuperMap.Ja_Res();
				}
            }
        }
        else {
            var key;
            var languageLib = new SuperMap.Language_Enum();
            for (key in languageLib.language) {
                if (language == key) {
                    languageLib.language[key];
                    if (languageLib.language[key] == "zh-cn") {
                        return new SuperMap.Zh_Res();
                    }
                    else if (languageLib.language[key] == "en-US") {
                        return new SuperMap.En_Res();
                    }
					else if (languageLib.language[key] == "ja-JP") {
						return new SuperMap.Ja_Res();
					}
                }
            }
        }
    }
};
SuperMap.Resource.registerClass('SuperMap.Resource', null, Sys.IDisposable);

SuperMap.Language_Enum = function() {
    /// <summary>语言类。提供可供设置的资源的语言种类，总共有两种：Chinese(中文)、English(英文)。为 SuperMap.Resource 对象的 setLanguage 方法提供语言资源选择种类。
    /// </summary>
    /// <field name="language" type="Object">语言库。</field>
    this.language = {
        "Chinese": "zh-cn",
        "English": "en-US",
		"Japanese": "ja-JP"
    }
};
SuperMap.Language_Enum.registerClass('SuperMap.Language_Enum', null, Sys.IDisposable);
