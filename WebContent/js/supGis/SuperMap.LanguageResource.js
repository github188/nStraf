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

SuperMap.LanguageResBase = function() {
    /// <summary>资源化基类。
    /// </summary>
    this.mapInitError = null;
    this.mapNameIllegal = null;
	this.noQueryLayer = null;
	this.noPickOnEntity = null;
	this.mapNullOrUndefined = null;
	this.getOverivewError = null;
	this.cantSetMapTwice = null;
	this.visibleAll = null;
	this.queryableAll = null;
	this.submitAll = null;
	this.refreshLayersInfo = null;
	this.refresh = null;
	this.ThemeDotDensity = null;
	this.ThemeGraduatedSymbol = null;
	this.ThemeGraph = null;
	this.ThemeLabel = null;
	this.ThemeRange = null;
	this.ThemeUnique = null;
	this.unVisible = null;
	this.visible = null;
	this.unQuery = null;
	this.query = null;
	this.makeThemeLabel = null;
	this.noTheme = null;
	this.nuknownDatasetType = null;
	this.layerCollection = null;
	this.wmsLayer = null;
	this.wfsLayer = null;
	this.datasetPoint = null;
	this.datasetLine = null;
	this.datasetNetwork = null;
	this.datasetRegion = null;
	this.datasetText = null;
	this.datasetLineM = null;
	this.datasetImage = null;
	this.datasetGrid = null;
	this.datasetCAD = null;
	this.datasetTabular = null;
	this.fontSong = null;
	this.viewPreviousNoImple = null;
	this.viewNextNoImple = null;
	this.submit = null;
};
SuperMap.LanguageResBase.prototype = {
    get: function(messageId) {
        /// <summary>根据参数提取资源</summary>
        /// <param name="messageId" type="String">String对象，用于查找资源。</param>
        /// <returns type="String">资源信息。</returns>
        return this[messageId];
    }
};
SuperMap.LanguageResBase.registerClass('SuperMap.LanguageResBase');

SuperMap.Zh_Res = function() {
    /// <summary>中文资源化类。
    /// </summary>
    SuperMap.Zh_Res.initializeBase(this);
    this.mapInitError = "初始化地图失败";
    this.mapNameIllegal = "传入的地图名非法";
	this.noQueryLayer = "没有可以查询的图层对象。";
	this.noPickOnEntity = "没有选中地物";
	this.mapNullOrUndefined = "关联的地图为空或者未定义";
	this.getOverivewError = "获取鹰眼图出错";
	this.cantSetMapTwice = "Map属性只能设置一次";
	this.visibleAll = "全部可见";
	this.queryableAll = "全部可查";
	this.submitAll = "提交图层信息";
	this.refreshLayersInfo = "刷新图层信息";
	this.refresh = "刷新";
	this.ThemeDotDensity = "点密度专题图";
	this.ThemeGraduatedSymbol = "等级符号专题图";
	this.ThemeGraph = "统计专题图";
	this.ThemeLabel = "标签专题图";
	this.ThemeRange = "分段专题图";
	this.ThemeUnique = "单值专题图";
	this.unVisible = "不可见";
	this.visible = "可见";
	this.unQuery = "不可查";
	this.query = "可查";
	this.makeThemeLabel = "制作标签专题图";
	this.noTheme = "无专题图";
	this.nuknownDatasetType = "未知数据集类型";
	this.layerCollection = "图层集合";
	this.wmsLayer = "WMS图层";
	this.wfsLayer = "WFS图层";
	this.datasetPoint = "点数据集";
	this.datasetLine = "线数据集";
	this.datasetNetwork = "网络数据集";
	this.datasetRegion = "面数据集";
	this.datasetText = "文本数据集";
	this.datasetLineM = "M线数据集";
	this.datasetImage = "image数据集";
	this.datasetGrid = "grid数据集";
	this.datasetCAD = "cad数据集";
	this.datasetTabular = "属性表数据集";
	this.fontSong = "宋体";
	this.viewPreviousNoImple = "viewPrevious方法在当前版本未实现，作为预留方法";
	this.viewNextNoImple = "viewNext方法在当前版本未实现，作为预留方法";
	this.submit = "提交";
};
SuperMap.Zh_Res.registerClass('SuperMap.Zh_Res', SuperMap.LanguageResBase);

SuperMap.En_Res = function() {
    /// <summary>英文资源化类。
    /// </summary>
    SuperMap.En_Res.initializeBase(this);
    this.mapInitError = "initialize map fail";
    this.mapNameIllegal = "input mapName illegal";
	this.noQueryLayer = "no query layer";
	this.noPickOnEntity = "no pick on some entity";
	this.mapNullOrUndefined = "relative map is null or undefined";
	this.getOverivewError = "get overviewMap error";
	this.cantSetMapTwice = "Map property can be setted only once";
	this.visibleAll = "visibleAll";
	this.queryableAll = "queryableAll";
	this.submitAll = "queryableAll";
	this.refreshLayersInfo = "refreshLayersInfo";
	this.refresh = "refresh";
	this.ThemeDotDensity = "ThemeDotDensity";
	this.ThemeGraduatedSymbol = "ThemeGraduatedSymbol";
	this.ThemeGraph = "ThemeGraph";
	this.ThemeLabel = "ThemeLabel";
	this.ThemeRange = "ThemeRange";
	this.ThemeUnique = "ThemeUnique";
	this.unVisible = "unVisible";
	this.visible = "visible";
	this.unQuery = "unQuery";
	this.query = "query";
	this.makeThemeLabel = "makeThemeLabel";
	this.noTheme = "noTheme";
	this.nuknownDatasetType = "nuknownDatasetType";
	this.layerCollection = "layerCollection";
	this.wmsLayer = "wmsLayer";
	this.wfsLayer = "wfsLayer";
	this.datasetPoint = "datasetPoint";
	this.datasetLine = "datasetLine";
	this.datasetNetwork = "datasetNetwork";
	this.datasetRegion = "datasetRegion";
	this.datasetText = "datasetText";
	this.datasetLineM = "datasetLineM";
	this.datasetImage = "datasetImage";
	this.datasetGrid = "datasetGrid";
	this.datasetCAD = "datasetCAD";
	this.datasetTabular = "datasetTabular";
	this.fontSong = "fontSong";
	this.viewPreviousNoImple = "viewPrevious no implement in this edition，it is reserve method";
	this.viewNextNoImple = "viewNext no implement in this edition，it is reserve method";
	this.submit = "submit";
};
SuperMap.En_Res.registerClass('SuperMap.En_Res', SuperMap.LanguageResBase);

SuperMap.Ja_Res = function() {
    /// <summary>日文资源化类。
    /// </summary>
    SuperMap.Ja_Res.initializeBase(this);
    this.mapInitError = "initialize map fail";
    this.mapNameIllegal = "input mapName illegal";
	this.noQueryLayer = "no query layer";
	this.noPickOnEntity = "no pick on some emtity";
	this.mapNullOrUndefined = "relative map is null or undefined";
	this.getOverivewError = "get overviewMap error";
	this.cantSetMapTwice = "Map property can be setted only once";
	this.visibleAll = "visibleAll";
	this.queryableAll = "queryableAll";
	this.submitAll = "queryableAll";
	this.refreshLayersInfo = "refreshLayersInfo";
	this.refresh = "refresh";
	this.ThemeDotDensity = "ThemeDotDensity";
	this.ThemeGraduatedSymbol = "ThemeGraduatedSymbol";
	this.ThemeGraph = "ThemeGraph";
	this.ThemeLabel = "ThemeLabel";
	this.ThemeRange = "ThemeRange";
	this.ThemeUnique = "ThemeUnique";
	this.unVisible = "unVisible";
	this.visible = "visible";
	this.unQuery = "unQuery";
	this.query = "query";
	this.makeThemeLabel = "makeThemeLabel";
	this.noTheme = "noTheme";
	this.nuknownDatasetType = "nuknownDatasetType";
	this.layerCollection = "layerCollection";
	this.wmsLayer = "wmsLayer";
	this.wfsLayer = "wfsLayer";
	this.datasetPoint = "datasetPoint";
	this.datasetLine = "datasetLine";
	this.datasetNetwork = "datasetNetwork";
	this.datasetRegion = "datasetRegion";
	this.datasetText = "datasetText";
	this.datasetLineM = "datasetLineM";
	this.datasetImage = "datasetImage";
	this.datasetGrid = "datasetGrid";
	this.datasetCAD = "datasetCAD";
	this.datasetTabular = "datasetTabular";
	this.fontSong = "fontSong";
	this.viewPreviousNoImple = "viewPrevious no implement in this edition，it is reserve method";
	this.viewNextNoImple = "viewNext no implement in this edition，it is reserve method";
	this.submit = "submit";
};
SuperMap.Ja_Res.registerClass('SuperMap.Ja_Res', SuperMap.LanguageResBase);