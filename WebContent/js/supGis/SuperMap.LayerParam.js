//========================================================================== 
// SuperMap iServer 客户端程序，版权所有，北京超图软件股份有限公司，2000-2008。 
// 本程序只能在有效的授权许可下使用。未经许可，不得以任何手段擅自使用或传播。 
// 作者：			SuperMap iServer Team 
// 修改：	 
// 文件名：			SuperMap.LayerParam.js 
// 功能：			SuperMap Layer相关参数类 
// 最后修改时间：	2008-1-24
//========================================================================== 

Type.registerNamespace("SuperMap");
SuperMap.LayerParam = function(){
	/// <summary>该类描述图层的相关参数，如图层的最大范围，当前比例尺等。该类描述的图层是指iServer服务器
	/// 提供的地图所对应的各种图层，请与客户端呈现图层进行区别。</summary>
	/// <returns type="SuperMap.LayerParam">返回 LayerParam 对象。</returns>
	/// <field name="description" type="String">对图层的描述。</field>
	/// <field name="caption" type="String">图层的标题。</field>
	/// <field name="maxScale" type="Number">图层的最大显示比例尺。&lt;br&gt;
	/// 系统默认 MaxScale 为0可以无限放大；如果设置此属性为非0值，那么当地图显示比例超过设置值，地图便无法继续放大。
	/// </field>
	/// <field name="minScale" type="Number">图层的最小显示比例尺。&lt;br&gt;
	/// 系统默认 MinScale 为0可以无限缩小；如果设置此属性为非0值，那么当地图显示比例小于设置值，地图便无法继续缩小。
	/// </field>
	/// <field name="name" type="String">图层的名称。&lt;br&gt;
	/// 图层名称 = “图层对应的数据集的名称@数据源的别名”。例如：图层对应的数据集的名称是“Dataset1”，此数据集所在的数据源的别名是“DataSource”，则该图层名成为“Dataset1@DataSource”。不能直接用数据集或数据源的别名来访问图层。
	/// </field>
	/// <field name="queryable" type="Boolean">图层的可查性。默认为false，即在执行客户端查询操作时，不对该图层进行查询操作，除非在查询参数QueryParam中强制指定查询该图层对象。
	/// </field>
	/// <field name="maxBounds" type="SuperMap.Rect2D">该图层中所有对象的最小外接矩形。</field>
	/// <field name="viewBounds" type="SuperMap.Rect">当前显示的地图范围。</field>
	/// <field name="center" type="SuperMap.Point2D">当前图层的中心点。</field>
	/// <field name="mapScale" type="Number">当前图层的显示比例尺。</field>
	/// <field name="prjCoordSys" type="PrjCoordSys">地图数据的投影信息。</field>
	/// <field name="subLayers" type="Array" elementType="Object">包含专题图等信息的子图层。</field>
	/// <field name="visible" type="Boolean">是否可见。</field>
	// 描述	String
	this.description = null;	
	// 标题	String
	this.caption = null;
	this.maxScale = 0.0;
	this.minScale = 0.0;
	// 名称	String
	this.name = name;	
	// 原先的queryable	boolean
	this.queryable	= false;
	// 最大范围（地理坐标）	Rect2D
	this.maxBounds = null;
	// 视窗范围（像素坐标）	Rect
	this.viewBounds	= null;
	// 中心点	Point2D
	this.center	= null;
	// 通过type来区分地图来源	String
	this.type =	null;
	// 当前比例尺	double
	this.mapScale =	0.0;
	// 像素单位	Unit(String)
	this.coordUnit	= null; 
	// 距离单位	Unit
	this.distanceUnit = null;
	// 面积单位	Unit
	this.areaUnit = null;
	// 	是否需要实现坐标转换，兼容第三方数据的时候需要对应的坐标转换。PrjCoordSys
	this.prjCoordSys = null;
	// 包含专题图等信息的子图层	Array (SubLayer)
	this.subLayers = new Array();
	// 是否可见	boolean
	this.visible = false;
}
SuperMap.LayerParam.registerClass('SuperMap.LayerParam');

SuperMap.Layer = function(){
	/// <summary>该类描述GIS服务器提供的地图所对应的图层。&lt;br&gt;
	///		1. 该类型提供了图层显示和控制等便于地图管理的一系列属性。&lt;br&gt;
	///		2. 图层分为普通图层和专题图层，矢量的普通图层中所有要素采用相同的渲染风格，格网的栅格图层采用颜色表来显示其像元；
	/// 而专题图层的则采用指定类型的专题图风格来渲染其中的要素或像元。影像数据只对应普通图层。&lt;br&gt;
	///		3. 普通图层的风格通过 layerSetting 属性来获取或设置。&lt;br&gt;
	///		4. 该类型提供了对图层的显示，选择，编辑，捕捉等状态的控制的方法，同时可以也提供了图层显示的属性设置的方法，
    /// 如最大和最小显示比例尺以及过滤条件等。
	/// </summary>
	/// <returns type="SuperMap.Layer">返回 Layer 对象。</returns>
	/// <field name="name" type="String">图层的名称。&lt;br&gt;
	/// 图层名称 = “图层对应的数据集的名称@数据源的别名”。例如：图层对应的数据集的名称是“Dataset1”，此数据集所在的数据源的别名是“DataSource”，则该图层名成为“Dataset1@DataSource”。不能直接用数据集或数据源的别名来访问图层。
	/// </field>
	/// <field name="caption" type="String">图层的标题。</field>
	/// <field name="description" type="String">对图层的描述。</field>
	/// <field name="layerSetting" type="Object">图层的扩展设置，比如WMS、WFS、Image等的设置。</field>
	/// <field name="queryable" type="Boolean">图层是否可查询。True 表示该图层处于可查询状态，False 表示该图层不可查询。
	/// </field>
	/// <field name="opaqueRate" type="Number">图层的不透明度。0表示透明，100表示不透明。
	/// </field>
	/// <field name="isSymbolScalable" type="Boolean">图层的符号大小是否随图缩放。&lt;br&gt;
	///		1. 默认为False。True 表示随着图层的缩放而缩放，在图层放大的同时，符号同时也放大。&lt;br&gt;
	///		2. 一般在设置符号随图缩放之前，需要通过接口 symbolScale 对符号显示的比例进行设置，来定义与当前地图窗口的显示比例之间的关系。&lt;br&gt;
	///		3. 对于含有子图层的数据集，如网络数据集，只需要设置网络图层的符号随图缩放即可，其节点图层的符号缩放功能会自动生效。
	/// </field>
	/// <field name="symbolScale" type="Number">符号显示比例的大小。&lt;br&gt;
	/// 如果符号大小不随着地图的缩放而缩放（isSymbolScalable = False），该值无效，符号的大小按 Style.markerSize 大小显示；如果符号随着地图的缩放而缩放（isSymbolScalable = True），则符号的大小和图层的显示比例有关系，一般设置为和图层的显示比例相等。&lt;br&gt;
	/// 该接口一般和 isSymbolScalable 一起使用。
	/// </field>
	/// <field name="visible" type="Boolean">图层是否可见。</field>
	/// <field name="displayFilter" type="String">图层显示过滤参数。例如：displayFilter="SMID>20" 。</field>
	/// <field name="maxScale" type="Number">图层的最大显示比例尺。系统默认 MaxScale 为0可以无限放大；如果设置此属性为非0值，那么当地图显示比例超过设置值，地图便无法继续放大。
	/// </field>
	/// <field name="minScale" type="Number">图层的最小显示比例尺。系统默认 MinScale 为0可以无限缩小；如果设置此属性为非0值，那么当地图显示比例小于设置值，地图便无法继续缩小。</field>
	/// <field name="minVisibleGeometrySize" type="Number" integer="true">几何对象的最小显示大小，以几何对象Bounds宽、高的最大值为准，单位为0.1mm，默认值为0.4mm。&lt;br&gt;
	/// 当设置此属性后，如果对象的显示尺寸小于此值时将不显示。</field>
	// 名称	string
	this.name = null;
	// 标题	String
	this.caption = null;
	// 描述	String
	this.description = null;
	// 图层的扩展设置,比如WMS、WFS、Image等的设置	Object
	this.layerSetting = null;
	// 是否可查询	Boolean
	this.queryable = false;
	// 符号是否随地图改变而改变	boolean
	this.isSymbolScalable = false;
	this.symbolScale = 0.0;
	this.visible = true;
	// 	过滤条件	String
	this.displayFilter = null;
	// 	最大显示比例尺	Double
	this.maxScale = 0.0;
	// 	最小显示比例尺	double
	this.minScale = 0.0;
	this.opaqueRate = 100;
	// 	返回/设置几何对象的最小显示大小，以几何对象Bounds宽、高的最大值为准，单位为0.1mm，默认值为0.4mm。当设置此属性后，如果对象的显示尺寸小于此值时将不显示。	
	this.minimumVisibleSize = 0.4;
};

SuperMap.Layer.registerClass('SuperMap.Layer');


SuperMap.ColorGradientType = function(){
	/// <summary>定义颜色渐变枚举。&lt;br&gt;
	/// 颜色渐变是多种颜色间的逐渐混合，可以是从起始色到终止色两种颜色的渐变，或者在起始色到终止色之间具有多种中间颜色进行渐变。该颜色渐变类型可应用于专题图对象的颜色方案设置中如：单值专题图、 分段专题图、 统计专题图、标签专题图、格网分段专题图和格网单值专题图。
	/// 都继承自该类。
	/// </summary>
	/// <field name="blackWhite" type="Number">blackWhite=0，黑白渐变色。&lt;br&gt;
	/// &lt;img src="../img/blackwhite.png" /&gt;
	/// </field>
	/// <field name="redWhite" type="Number">redWhite=1，红白渐变色。&lt;br&gt;
	/// &lt;img src="../img/redwhite.png" /&gt;
	/// </field>
	/// <field name="greenWhite" type="Number">greenWhite=2，绿白渐变色。&lt;br&gt;
	/// &lt;img src="../img/greenwhite.png" /&gt;
	/// </field>
	/// <field name="blueWhite" type="Number">blueWhite=3，蓝白渐变色。&lt;br&gt;
	/// &lt;img src="../img/bluewhite.png" /&gt;
	/// </field>
	/// <field name="yellowWhite" type="Number">yellowWhite=4，黄白渐变色。&lt;br&gt;
	/// &lt;img src="../img/yellowwhite.png" /&gt;
	/// </field>
	/// <field name="pinkWhite" type="Number">pinkWhite=5，粉红白渐变色。&lt;br&gt;
	/// &lt;img src="../img/pinkwhite.png" /&gt;
	/// </field>
	/// <field name="cyanWhite" type="Number">cyanWhite=6，青白渐变色。&lt;br&gt;
	/// &lt;img src="../img/cyanwhite.png" /&gt;
	/// </field>
	/// <field name="redBlack" type="Number">redBlack=7，红黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/redblack.png" /&gt;
	/// </field>
	/// <field name="greenBlack" type="Number">greenBlack=8，绿黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/greenblack.png" /&gt;
	/// </field>
	/// <field name="blueBlack" type="Number">blueBlack=9，蓝黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/blueblack.png" /&gt;
	/// </field>
	/// <field name="yellowBlack" type="Number">yellowBlack=10，黄黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/yellowblack.png" /&gt;
	/// </field>
	/// <field name="pinkBlack" type="Number">pinkBlack=11，粉红黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/pinkblack.png" /&gt;
	/// </field>
	/// <field name="cyanBlack" type="Number">cyanBlack=12，青黑渐变色。&lt;br&gt;
	/// &lt;img src="../img/cyanblack.png" /&gt;
	/// </field>
	/// <field name="yellowRed" type="Number">yellowRed=13，黄红渐变色。&lt;br&gt;
	/// &lt;img src="../img/yellowred.png" /&gt;
	/// </field>
	/// <field name="yellowGreen" type="Number">yellowGreen=14，黄绿渐变色。&lt;br&gt;
	/// &lt;img src="../img/yellowgreen.png" /&gt;
	/// </field>
	/// <field name="yellowBlue" type="Number">yellowBlue=15，黄蓝渐变色。&lt;br&gt;
	/// &lt;img src="../img/yellowblue.png" /&gt;
	/// </field>
	/// <field name="greenBlue" type="Number">greenBlue=16，绿蓝渐变色。&lt;br&gt;
	/// &lt;img src="../img/greenblue.png" /&gt;
	/// </field>
	/// <field name="greenRed" type="Number">greenRed=17，绿红渐变色。&lt;br&gt;
	/// &lt;img src="../img/greenred.png" /&gt;
	/// </field>
	/// <field name="blueRed" type="Number">blueRed=18，蓝红渐变色。&lt;br&gt;
	/// &lt;img src="../img/bluered.png" /&gt;
	/// </field>
	/// <field name="pinkRed" type="Number">pinkRed=19，粉红红渐变色。&lt;br&gt;
	/// &lt;img src="../img/pinkred.png" /&gt;
	/// </field>
	/// <field name="pinkBlue" type="Number">pinkBlue=20，粉红蓝渐变色。&lt;br&gt;
	/// &lt;img src="../img/pinkblue.png" /&gt;
	/// </field>
	/// <field name="cyanBlue" type="Number">cyanBlue=21，青蓝渐变色。&lt;br&gt;
	/// &lt;img src="../img/cyanblue.png" /&gt;
	/// </field>
	/// <field name="cyanGreen" type="Number">cyanGreen=22，青绿渐变色。&lt;br&gt;
	/// &lt;img src="../img/cyangreen.png" /&gt;
	/// </field>
	/// <field name="rainbow" type="Number">rainbow=23，彩虹色。&lt;br&gt;
	/// &lt;img src="../img/rainbow.png" /&gt;
	/// </field>
	/// <field name="greenOrangeViolet" type="Number">greenOrangeViolet=24，绿橙紫渐变色。&lt;br&gt;
	/// &lt;img src="../img/greenorangeviolet.png" /&gt;
	/// </field>
	/// <field name="terrain" type="Number">terrain=25，地形渐变。&lt;br&gt;
	/// &lt;img src="../img/terrain.png" /&gt;
	/// </field>
	/// <field name="spectrum" type="Number">spectrum=26，光谱渐变。&lt;br&gt;
	/// &lt;img src="../img/spectrum.png" /&gt;
	/// </field>

    this.blackWhite = 0; // 黑->白
	this.redWhite = 1; // 红->白
	this.greenWhite = 2; // 绿->白
	this.blueWhite = 3; // 蓝->白
	this.yellowWhite = 4; // 黄->白
	this.pinkWhite = 5; // 粉红->白
	this.cyanWhite = 6; // 青->白
	this.redBlack = 7; // 红->黑
	this.greenBlack = 8; // 绿->黑
	this.blueBlack = 9; // 蓝->黑
	this.yellowBlack = 10; // 黄->黑
	this.pinkBlack = 11; // 粉红->黑
	this.cyanBlack = 12; // 青->黑
	this.yellowRed = 13; // 黄->红
	this.yellowGreen = 14; // 黄->绿
	this.yellowBlue = 15; // 黄->蓝
	this.greenBlue = 16; // 绿->蓝
	this.greenRed = 17; // 绿->黑
	this.blueRed = 18; // 蓝->红
	this.pinkRed = 19; // 青->红
	this.pinkBlue = 20; // 青->蓝
	this.cyanBlue = 21; // 青->蓝
	this.cyanGreen = 22; // 青->绿
	this.rainbow = 23; // 彩->虹
	this.greenOrangeViolet = 24; // 绿->桔黄->紫罗兰
	this.terrain = 25; // 地形渐变,用于三维显示效果较好
	this.spectrum = 26; // 光谱渐变
};
SuperMap.ColorGradientType.registerClass('SuperMap.ColorGradientType');

SuperMap.Theme = function(){
	/// <summary>专题图类，该类是所有专题图的基类。所有专题图类，如单值专题图，标签专题图，范围分段专题图等
	/// 都继承自该类。
	/// </summary>
	/// <returns type="SuperMap.Theme">返回 Theme 对象。</returns>
	/// <field name="themeType" type="Number" >专题图类型。</field>
	this.themeType = null;
};

SuperMap.Theme.registerClass('SuperMap.Theme');

SuperMap.ThemeUnique = function(){
	/// <summary>单值专题图类。&lt;br&gt;
	///		1. 将字段或表达式的值相同的要素采用相同的风格来显示，从而用来区分不同的类别。&lt;br&gt;
	///		2. 在表示土地的面数据中表示土地利用类型的字段中有草地，林地，居民地，耕地等值，使用单值专题图进行渲染时，
	/// 每种类型的土地利用类型被赋予一种颜色或填充风格，从而可以看出每种类型的土地利用的分布区域和范围。&lt;br&gt;
	///		3. 単值专题图可用于地质图、地貌图、植被图、土地利用图、政治行政区划图、自然区划图、经济区划图等。 &lt;br&gt;
	///		4. 单值专题图着重表示现象质的差别，一般不表示数量的特征。尤其是有交叉或重叠现象时，不推荐使用此类型，例如：民族分布区等。
	/// </summary>
	/// <field name="items" type="Array" elementType="SuperMap.ThemeUniqueItem">单值专题图子项数组。单值专题图子项提供了单值专题图子项的名称、单值、是否可见和显示风格等属性。</field>
	/// <field name="uniqueExpression" type="String">单值专题图字段表达式。用于制作单值专题图的字段或字段表达式。该字段可以为要素的某一属性（如地质图中的年代或成份），其值的数据类型可以为数值型或字符型。 
	/// </field>
	/// <field name="defaultStyle" type="SuperMap.Style">单值专题图的默认风格，对于那些未在单值专题图子项之列的对象使用该风格显示。如未设置，则使用图层默认风格显示。
	/// </field>
	/// <field name="themeType" type="Number">获取专题图的类型对应的数值。详细信息请参见ThemeType类。
	/// </field>
	/// <field name="makeDefaultParam" type="SuperMap.ThemeUniqueParam">用于制作默认范围专题图的参数。</field>
	// 	单值项数组	Array
	this.items = new Array();
	// 单值表达式	String
	this.uniqueExpression	= null;
	// 	默认样式	GeoStyle
	this.defaultStyle = null;
	// 类型为单值专题图
	this.themeType = new SuperMap.ThemeType().unique;
	
	this.makeDefaultParam = null;
};
SuperMap.ThemeUnique.registerClass('SuperMap.ThemeUnique', SuperMap.Theme);

SuperMap.ThemeUniqueParam = function(){
	/// <summary>单值专题图用于制作默认专题图的参数。
	/// </summary>
	/// <returns type="SuperMap.ThemeUniqueParam">返回 ThemeUniqueParam 对象。</returns>
	/// <field name="layerName" type="String">指定的图层名称。</field>
	/// <field name="colorGradientType" type="Number">颜色渐变类型的枚举值。 </field>
	this.layerName="";
	this.colorGradientType = 0;
};
SuperMap.ThemeUniqueParam.registerClass('SuperMap.ThemeUniqueParam');

SuperMap.ThemeUniqueItem = function(){
	/// <summary>单值专题图的子项。在単值专题图中每一个表达值对应一个子项。通过该类可以设置各个単值专题图子项的可见属性等。
	/// </summary>
	/// <returns type="SuperMap.ThemeUniqueItem">返回 ThemeUniqueItem 对象。</returns>
	/// <field name="unique" type="String">单值专题图子项的单值。</field>
	/// <field name="visible" type="Boolean">单值专题图子项是否可见。 </field>
	/// <field name="caption" type="String">每个单值专题图子项的名称。</field>
	/// <field name="style" type="SuperMap.Style">单值专题图子项的显示风格。</field>
	// 一个唯一的数值	String
	this.unique	= null;
	// 	是否可见	Boolean
	this.visible = false;
	this.caption = null;
	// 	样式	Style
	this.style = null;
};
SuperMap.ThemeUniqueItem.registerClass('SuperMap.ThemeUniqueItem');

SuperMap.ThemeLabel = function(){
	/// <summary>标签专题图类。&lt;br&gt;
	///		1. 用文本的形式在图层上直接显示属性表中的数据，其实质就是对图层的标注。&lt;br&gt;
	///		2. 标签专题图的标注可以是数字、字母与文字，例如：河流、湖泊、海洋、山脉、城镇、村庄等地理名称，高程、等值线数值、河流流速、公路段里程、航海线里程等。&lt;br&gt;
	///		3. 在标签专题图中，你可以对标签的显示风格和位置进行设置或控制，你可以为所有的标签都设置统一的显示风格和位置选项来显示，也可以通过分段的方式，对单个或每个分段内的标签的风格分别进行设置，另外，单个标签的位置也是可以修改的。&lt;br&gt;
	///		4. 地图上一般还会出现图例说明，图名，比例尺等等，那些都是制图元素，不属于标签专题图标注的范畴。&lt;br&gt;
	///		5. 标签专题图有两种制作方式:
	///				第一种是使用成员变量 items 的方式制作专题图，这种方法可以对标签专题图进行分段，但是成员变量 rangeExpression 必须不为 null 并且合法，因为 items 中的每个 item 对象的 start 和 end 的值就是成员变量 rangeExpression 所对应的字段的值；
	///				第二种是指定 uniformStyle，将指定图层的表达式的所有值使用统一的风格输出，这种情况下必须设置成员变量 labelExpression 用于设定做标签专题图所使用的字段名，因此这种情况下成员变量 labelExpression 必须合法。两种方法以前者为主，即如果两种值都设置合法，则优先使用 items 的方式进行标签专题图的制作。
	/// </summary>
	/// <returns type="SuperMap.ThemeLabel">返回 ThemeLabel 对象。</returns>
	/// <field name="items" type="Array" elementType="SuperMap.ThemeLabelItem">标签专题图中标签专题图子项数组。标签专题图允许以分段的方式进行显示，其中每一个分段就是一个子项，每一个子项都具有其名称、风格、起始值和终止值。</field>
	/// <field name="backStyle" type="Style">标签专题图中的标签背景风格。当背景形状BackShape属性设为默认值，即不显示任何风格，BackStyle属性无效。</field>
	/// <field name="labelBackShape" type="Number">标签专题图中的标签背景的形状类型对应的数值。&lt;br&gt;
	/// 背景类型可以是矩形、圆角矩形、菱形、椭圆形、三角形和符号等，默认为0，即不显示任何背景。详细信息可参考LabelBackShape枚举类。 </field>
	/// <field name="maxLabelLength" type="Number">标签在每一行显示的最大长度，如果超过这个长度，可以采用两种方式来处理，一种是换行的模式进行显示，另一种是以省略号方式显示。&lt;br&gt;
	/// 默认最大长度为256。</field>
	/// <field name="overLengthLabelMode" type="Number">超长标签处理模式。&lt;br&gt;
	/// 对于标签的长度超过设置的标签最大长度的标签称为超长标签，标签的最大长度可以通过 maxLabelLength 来设置。&lt;br&gt;
	/// SuperMap 提供三种超长标签的处理方式来控制超长标签的显示行为。超长标签处理模式：不处理=0、省略=1、换行=2。
	/// </field>
	/// <field name="isFlowEnabled" type="Boolean">是否流动显示标签。默认为 false。流动显示只适合于线和面要素的标注。 
	/// </field>
	/// <field name="isAlongLine" type="Boolean">是否沿线显示文本。true 表示沿线显示文本，false 表示正常显示文本。默认为 false。&lt;br&gt;
	/// 沿线标注属性只适用于线数据集专题图。
	/// </field>
	/// <field name="isAngleFixed" type="Boolean">当沿线显示文本时，是否将文本角度固定。true 表示按固定文本角度显示文本，false 表示按照沿线角度显示文本。&lt;br&gt;
	/// 默认值为fasle。 
	/// </field>
	/// <field name="isOverlapAvoided" type="Boolean">是否允许以文本避让方式显示文本。默认值为 true， 即自动避免文本叠盖。只针对该标签专题图层中的文本数据。&lt;br&gt; 
	/// 在标签重叠度很大的情况下，即使使用自动避让功能，可能也无法完全避免标签重叠现象。
	/// </field>
	/// <field name="isLabelRepeated" type="Boolean">在沿线标注时是否进行循环标注。例如：用于道路标注时，需要设定是否每隔一段距离重复显示道路标注。 &lt;br&gt;
	/// 默认值为 false，表示不进行循环标注。该属性只有当标签沿线标注时有效。
	/// </field>
	/// <field name="labelRepeatInterval" type="Number">在沿线标注时循环标注的间隔。长度的单位与地图的地理单位一致。只有设定 isLabelRepeated 为 true 的时候，labelRepeatInterval 属性才有效。</field>
	/// <field name="offsetX" type="String">标签专题图中标记文本相对于要素内点的水平偏移量。&lt;br&gt;
	/// 标签偏移量的单位为地图单位。&lt;br&gt;
	/// 该偏移量的值为一个常量值或者字段表达式所表示的值，即如果字段表达式为 SmID，其中 SmID = 2，那么偏移量的值为2。</field>
	/// <field name="offsetY" type="String">标签专题图中标记文本相对于要素内点的垂直偏移量。&lt;br&gt;
	/// 标签偏移量的单位为地图单位。&lt;br&gt;
	/// 该偏移量的值为一个常量值或者字段表达式所表示的值，即如果字段表达式为 SmID，其中 SmID = 2，那么偏移量的值为2。</field>
	/// <field name="leaderLineStyle" type="Style">标签与其标注对象之间牵引线的风格。</field>
	/// <field name="isLeaderLineDisplayed" type="Boolean">是否显示标签和它标注的对象之间的牵引线。&lt;br&gt;
	/// 默认值为 false，即不显示标签和它标注的对象之间的牵引线。&lt;br&gt;
	/// 只有设置了 isFlowEnabled 为 true 时，牵引线才起作用。</field>
	/// <field name="rangeExpression" type="String">分段字段表达式。该表达式对应的字段（或者字段组合）的值应该为数值型。&lt;br&gt;
	/// 用户根据给定的分段字段表达式（rangeExpression）的值来比较其从开始到结束的每一个分段值，来确定采用什么风格来显示给定标注字段表达式（labelExpression）相应的标注文本。
	/// </field>
	/// <field name="labelExpression" type="String">标注字段表达式。 系统将 labelExpression 对应的字段值以标签的形式显示在图层中。</field>
	/// <field name="uniformStyle" type="TextStyle">统一文本风格。当标签专题图子项的个数大于等于1的时候，uniformStyle 就不起作用。</field>
	/// <field name="themeType" type="Number">专题图的类型。详细信息请参见 ThemeType 类。
	/// </field>
	// 	标签项数组	Array
	this.items = null;
	// 	背景风格	Style
	this.backStyle = null;
	//	枚举：空背景、矩形背景、圆角矩形背景、椭圆形背景、菱形背景、三角形背景、符号背景。	LabelBackShape 
	this.labelBackShape = null;
	// 标签显示最大长度,如果超过这个长度,将以省略号显示。	Int
	this.maxLabelLength	= 256;
	// 	超长标签处理模式：不处理、省略、换行。	Int
	this.overLengthLabelMode = 0;
	// 是否允许流动显示	Boolean
	this.isFlowEnabled	= false;
	// 	是否允许沿线标注 Boolean
	this.isAlongLine = false;
	// 	是否固定角度	Boolean
	this.isAngleFixed = false;
	// 	是否避免重叠	Boolean
	this.isOverlapAvoided = false;
	// 是否标签重复	Boolean
	this.isLabelRepeated = false;
	// 	标签重复间隔	Int
	this.labelRepeatInterval = 0;
	// 	X方向偏移量	String
	this.offsetX = "0.0";
	// 	Y方向偏移量	String
	this.offsetY = "0.0";
	// 	牵引线样式	GeoStyle
	this.leaderLineStyle = null;
	// 	是否显示牵引线	Boolean
	this.isLeaderLineDisplayed = null;
	// 	范围表达式	String
	this.rangeExpression = null;
	// 	标签表达式	String
	this.labelExpression = null;
	// 	统一文本风格	TextStyle
	this.uniformStyle = null;
	// 专题图类型，为标签专题图
	this.themeType = new SuperMap.ThemeType().label;
};
SuperMap.ThemeLabel.registerClass('SuperMap.ThemeLabel', SuperMap.Theme);

SuperMap.ThemeLabelItem = function(){
	/// <summary>标签专题图子项。&lt;br&gt;
	/// 标签专题图用专题值对点、线、面等对象做标注，值得注意的是，标签专题图允许用户设置范围段，同一范围段内的标签具有相同的显示风格，其中每一个范围段就是一个专题图子项，每一个子项都具有其名称、风格、起始值和终止值。&lt;br&gt;
	/// 每个分段所表示的范围为 [Start, End)。例如：标签专题图的分段点有两个子项，他们所代表的分段区间分别为[0,5)，[5,10)。那么需要分别设置 ThemeLabelItem[0].start=0，ThemeLabelItem[0].end=5，ThemeLabelItem[1].start=5，ThemeLabelItem[1].end=10。
	/// </summary>
	/// <returns type="SuperMap.ThemeLabelItem">返回 ThemeLabelItem 对象。</returns>
	/// <field name="caption" type="String">标签专题子项的标题。</field>
	/// <field name="style" type="style">标签专题图子项所对应的显示风格。 
	/// </field>
	/// <field name="visible" type="Boolean">标签专题图子项是否可见。 如果标签专题图子项可见，则为true，否则为false。默认值为 True。</field>
	/// <field name="start" type="Number">标签专题图子项的分段起始值。&lt;br&gt;
	/// 如果该子项是分段中第一项，那么该起始值就是分段的最小值；如果该子项的序号大于等于 1 的时候，该起始值必须与前一子项的终止值相同，否则系统会抛出异常。
	/// </field>
	/// <field name="end" type="Number">标签专题图子项的终止值。&lt;br&gt;
	/// 如果该子项是分段中最后一个子项，那么该终止值就是分段的最大值；如果不是最后一项，该终止值必须与其下一子项的起始值相同，否则系统抛出异常。
	/// </field>
	// 	标题	String
	this.caption = null;
	// 样式	TextStyle
	this.style = null;	
	// 	是否可见	Boolean
	this.visible = true;
	// 	开始数值	Double
	this.start = 0.0;
	// 	结束数值	Double
	this.end = 0.0;
};
SuperMap.ThemeLabelItem.registerClass('SuperMap.ThemeLabelItem');

SuperMap.RangeMode = function(){
	/// <summary>指定分段专题图的分段方式常量。 &lt;br&gt;
	/// 在分段专题图中，作为专题变量的字段或表达式的值按照某种分段方式被分成多个范围段，要素或记录根据其所对应的字段值或表达式值被分配到其中一个分段中，在同一个范围段中要素或记录使用相同的风格进行显示。分段专题图一般用来表现连续分布现象的数量或程度特征，如降水量的分布，土壤侵蚀强度的分布等，从而反映现象在各区域的集中程度或发展水平的分布差异。&lt;br&gt;
	/// SuperMap 提供多种分类方法，包括等距离分段法、平方根分段法、标准差分段法、对数分段法、等计数分段法和自定义距离法，显然这些分段方法根据一定的距离进行分段，因而分段专题图所基于的专题变量必须为数值型。
	/// </summary>
	/// <returns type="SuperMap.RangeMode">返回 RangeMode 对象。</returns>
	/// <field name="equalInterval" type="Number">
	/// 等距离分段。equalInterval = 0。&lt;br&gt;
	/// 等距离分段是根据作为专题变量的字段或表达式的最大值和最小值，按照用户设定的分段数进行相等间距的分段。在等距离分段中，每一段具有相等的长度。求算等距分段的距离间隔公式为：&lt;br&gt;
	/// &lt;img src="../img/EqualInterval_d.png" /&gt; &lt;br&gt;
	/// 其中，d 为分段的距离间隔，Vmax 为专题变量的最大值，Vmin 为专题变量的最小值，count 为用户指定的分段数。则每一分段的分段点的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/EqualInterval_v.png" /&gt; &lt;br&gt;
	/// 其中，Vi 为分段点的值，i 为从0到count的正整数，表示各分段，当 i 等于0时，Vi 为 Vmin；当 i 等于 count 时，Vi 为 Vmax。&lt;br&gt;
	/// 例如你选择一个字段作为专题变量，其值是从1到10，你需要用等距离分段法将其分为4段，则分别为1-2.5，2.5-5，5-7.5和7.5-10（注意，分段中使用“=&gt;”和“&lt;”，所以分段点的值划归到下一段）。&lt;br&gt;
	/// 注意：按照这种分段方式，很有可能某个分段中没有数值，即落到该段中的记录或要素为0个。
	/// </field>
	/// <field name="squareRoot" type="Number">
	/// 平方根分段。squareRoot = 1。&lt;br&gt;
	/// 平方根分段方法实质上是对原数据的平方根的等距离分段，其首先取所有数据的平方根进行等距离分段，得到处理后数据的分段点，然后将这些分段点的值进行平方作为原数据的分段点，从而得到原数据的分段方案。所以，按照这种分段方式，也很有可能某个分段中没有数值，即落到该段中的记录或要素为0个。该方法适用于一些特定数据，如最小值与最大值之间相差比较大时，用等距离分段法可能需要分成很多的段才能区分，用平方根分段方法可以压缩数据间的差异，用较少的分段数却比较准确地进行分段。专题变量的平方根的分段间隔距离计算公式为：&lt;br&gt;
	/// &lt;img src="../img/SquareRoot_d.png" /&gt; &lt;br&gt;
	/// 其中，d 为分段的距离间隔，Vmax 为专题变量的最大值，Vmin 为专题变量的最小值，count 为用户指定的分段数。则专题变量的分段的段点的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/SquareRoot_v.png" /&gt; &lt;br&gt;
	/// 其中，Vi 为分段点的值，i 为从0到 count 的正整数，表示各分段，当 i 等于0时，Vi 为 Vmin。&lt;br&gt;
	/// 注意：数据中有负数则不适合这种方法。
	/// </field>
	/// <field name="stdDeviation" type="Number">
	/// 标准差分段。stdDeviation = 2。&lt;br&gt;
	/// 标准差分段方法反映了各要素的某属性值对其平均值的偏离。该方法首先计算出专题变量的平均值和标准偏差，在此基础上进行分段。标准差分段的每个分段长度都是一个标准差，最中间的那一段以平均值为中心，左边分段点和右边分段点分别与平均值相差0.5个标准差。设专题变量值的平均值为 mean，标准偏差为 std，则分段效果如图所示：&lt;br&gt;
	/// &lt;img src="../img/rangemode_little.png" /&gt; &lt;br&gt;
	/// 例如对专题变量为1-100之间的值，且专题变量的平均值为50，标准偏差为20，则分段为40-60，20-40，60-80，0-20，80-100共5段。落在不同分段范围内的要素分别被设置为不同的显示风格。&lt;br&gt;
	/// 注意：标准差的段数由计算结果决定，用户不可控制。 
	/// </field>
	/// <field name="logarithm" type="Number">
	/// 对数分段。logarithm = 3。&lt;br&gt;
	/// 对数分段方法的实现的原理与平方根分段方法基本相同，所不同的是平方根方法是对原数据取平方根，而对数分段方法是对原数据取对数，即对原数据的以10为底的对数值的等距离分段，其首先对原数据所有值的对数进行等距离分段，得到处理后数据的分段点，然后以10为底，这些分段点的值作为指数的幂得到原数据的各分段点的值，从而得到分段方案。适用于最大值与最小值相差很大，用等距离分段不是很理想的情况，对数分段法比平方根分段法具有更高的压缩率，使数据间的差异尺度更小，优化分段结果。专题变量的对数的等距离分段的距离间隔的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/Logarithm_d.png" /&gt; &lt;br&gt;
	/// 其中，d 为分段的距离间隔，Vmax 为专题变量的最大值，Vmin 为专题变量的最小值，count 为用户指定的分段数。从而专题变量的分段点的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/Logarithm_v.png" /&gt; &lt;br&gt;
	/// 其中，Vi 为分段点的值，i 为从0到 count 的正整数，表示各分段，当 i 等于0时，Vi 为 Vmin；当 i 等于 count 时，Vi 为 Vmax。&lt;br&gt;
	/// 注意：数据中有负数则不适合这种方法。
	/// </field>
	/// <field name="quantile" type="Number">
	/// 等计数分段。quantile = 4。&lt;br&gt;
	/// 在等计数分段中，每个分段中具有相等数目的要素个数，当然，这个相等的个数是多少是由用户指定的分段数以及实际的要素个数来决定的，在可以均分的情况下，每段中对象数目应该是一样的，但是当对象数目不可均分时，分段结果在最后一段内会有出入，例如75个数据，分成8段，则每一段有9个要素，但最后一段只有3个要素。这种分段方法适合于线性分布的数据。等计数分段的每段中的要素个数的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/Quantile_n.png" /&gt; &lt;br&gt;
	/// 其中，n 为每段中的要素个数，N 为要进行分段的要素的总个数，count 为用户指定的分段数。当 n 的计算结果不是整数时，采用四舍五入的方式取整。
	/// </field>
	/// <field name="customInterval" type="Number">
	/// 自定义分段。customInterval = 5。&lt;br&gt;
	/// 在自定义分段中，由用户指定各段的长度，即间隔距离来进行分段，分段数由 SuperMap 根据指定的间隔距离以及专题变量的最大和最小值来计算。各分段点的求算公式为：&lt;br&gt;
	/// &lt;img src="../img/CustomInterval.png" /&gt; &lt;br&gt;
	/// 其中，Vi 为各分段点的值，Vmin 为专题变量的最小值，d 为用户指定的距离，count 为计算出来的分段数，i 为从0到 count 的正整数，表示各分段，当 i 等于0时，Vi 为 Vmin；当 i 等于 count 时，Vi 为 Vmax。
	/// </field>
	//等距离分段
 	this.equalInterval = 0;
    //平方根分段
    this.squareRoot = 1;
    //标准差分段
    this.stdDeviation  = 2;
    //对数分段
    this.logarithm  = 3;
    //等计数分段
    this.quantile  = 4;
   //自定义距离分段模式
    this.customInterval = 5;
};
SuperMap.RangeMode.registerClass('SuperMap.RangeMode');

SuperMap.ThemeRange = function(){
	/// <summary>分段专题图类。 &lt;br&gt;
	/// 通过对比某要素分段字段表达式的值与（按照一定的分段模式确定的）各分段范围的分段值，来确定该要素所在的范围段，从而对落在不同分段内的要素设置为不同的风格。
	/// </summary>
	/// <returns type="SuperMap.ThemeRange">返回 ThemeRange 对象。</returns>
	/// <field name="rangeExpression" type="String">分段字段表达式。用户根据给定的分段字段表达式（RangeExpression）的值判断所属的分段区间，从而来确定采用什么风格来显示。</field>
	/// <field name="items" type="Array" elementType="SuperMap.ThemeRangeItem">分段专题图中分段专题图子项数组。&lt;br&gt;
	/// 在分段专题图中，将分段字段的表达式的值按照某种分段模式被分成多个范围段。每个分段都有其分段起始值、终止值、名称和风格等。每个分段所表示的范围为[Start, End)。</field>
	/// <field name="themeType" type="Number">专题图的类型。详细信息请参见ThemeType类。</field>
	/// <field name="makeDefaultParam" type="SuperMap.ThemeRangeParam">用于制作默认范围专题图的参数。</field>

	// 	范围表达式	String
	this.rangeExpression = null;
	// 	范围项数组	Array
	this.items = new Array();
	// 专题图类型为范围专题图
	this.themeType = new SuperMap.ThemeType().range;
	this.makeDefaultParam = null;
};
SuperMap.ThemeRange.registerClass('SuperMap.ThemeRange', SuperMap.Theme);

SuperMap.ThemeRangeItem = function(){
	/// <summary>分段专题图子项类。&lt;br&gt;
	/// 在分段专题图中，将分段字段的表达式的值按照某种分段模式被分成多个范围段。
	/// 每个分段都有其分段起始值、终止值、名称和风格等。每个分段所表示的范围为[start, end)。
	/// </summary>
	/// <returns type="SuperMap.ThemeRangeItem">返回 ThemeRangeItem 对象。</returns>
	/// <field name="start" type="Number">分段专题图子项的起始值。&lt;br&gt;
	/// 如果该子项是分段中第一个子项，那么该起始值就是分段的最小值；如果子项的序号 大于等于 1 的时候，该起始值必须与前一子项的终止值相同，否则系统会抛出异常。
	/// </field>
	/// <field name="end" type="Number">分段专题图子项的终止值。&lt;br&gt;
	/// 如果该子项是分段中最后一个子项，那么该终止值就是分段的最大值；如果不是最后一项，该终止值必须与其下一子项的起始值相同，否则系统抛出异常。</field>
	/// <field name="style" type="Style">分段专题图中每一个分段专题图子项的对应的风格。</field>
	/// <field name="visible" type="Boolean">分段专题图中的子项是否可见。</field>
	/// <field name="caption" type="String">分段专题图子项的标题。</field>
	// 	开始数值	Double
	this.start = 0.0;
	// 结束数值
	this.end = 0.0;
	// 	样式	Style
	this.style = null;
	// 	是否可见	Boolean
	this.visible = true;
	//	标题	String
	this.caption = null;
};
SuperMap.ThemeRangeItem.registerClass('SuperMap.ThemeRangeItem');

SuperMap.ThemeRangeParam = function(){
	/// <summary>统计专题图用于制作默认范围专题图的参数。 
	/// </summary>
	/// <returns type="SuperMap.ThemeRangeParam">返回 ThemeRangeParam 对象。</returns>
	/// <field name="layerName" type="String">指定图层名称。</field>
	/// <field name="colorGradientType" type="NUmber">颜色渐变类型的枚举值。</field>
	/// <field name="rangeMode" type="Number">范围分段模式。</field>
	/// <field name="rangeParameter" type="Number">范围参数。</field>
	this.layerName="";
	this.colorGradientType = 0;
	this.rangeMode = 0;
	this.rangeParameter = -1;
};
SuperMap.ThemeRangeParam.registerClass('SuperMap.ThemeRangeParam');

SuperMap.ThemeGraph = function() {
	/// <summary>统计专题图类。 
	/// 按照提供的统计方法对字段的属性值进行统计，并根据每个属性值所占比率赋予相应对象的显示风格。
	/// </summary>
	/// <returns type="SuperMap.ThemeGraph">返回 ThemeGraph 对象。</returns>
	/// <field name="items" type="Array" elementType="SuperMap.ThemeGraphItem">统计专题图子项数组。 </field>
	/// <field name="offsetX" type="String">统计图的水平偏移量或者表达偏移量的字段(该字段必须为数值型字段)表达式。默认使用地图单位。</field>
	/// <field name="offsetY" type="String">统计图的垂直偏移量或者表达偏移量的字段(该字段必须为数值型字段)表达式。默认使用地图单位。</field>
	/// <field name="graphType" type="Number">统计专题图类型。SuperMap 支持多种类型的统计图类型。其中包括面积图、阶梯图、折线图、点状图、柱状图、三维柱状图、饼图、三维饼图、玫瑰图、三维玫瑰图、条状金字塔图、面状金字塔图、堆叠柱状图、三维堆叠柱状图、环状图。详细信息请参见 GraphType 枚举类。</field>
	/// <field name="barWidth" type="Number">柱状专题图中每一个柱的宽度。使用地图坐标单位。&lt;br&gt;
	/// 只有选择的统计图类型为柱状图（柱状图、三维柱状图、堆叠柱状图、三维堆叠柱状图、条状金字塔）时，此项才可设置。 
	/// </field>
	/// <field name="startAngle" type="Number">饼状统计图的起始角度，默认以水平方向为正向。单位为度，精确到0.1度。&lt;br&gt;
	/// 只有选择的统计图类型为饼状图（饼图、三维饼图、玫瑰图、三维玫瑰图）时，此项才可设置。
	/// </field>
	/// <field name="roseAngle" type="Number">统计图中玫瑰图或三维玫瑰图分片的角度，单位为度，精确到0.1度。&lt;br&gt;
	/// 在角度为 0 或者大于 360 度的情况下均使用360度来等分制作统计图的字段数。</field>
	/// <field name="isFlowEnabled" type="Boolean">专题图的渲染风格是否流动显示。</field>
	/// <field name="leaderLineStyle" type="Style">统计图与其表示对象之间牵引线的风格。</field>
	/// <field name="isLeaderLineDisplayed" type="Boolean">是否显示统计图和它所表示的对象之间的牵引线。
	/// 如果渲染符号偏移该对象，图与对象之间可以采用牵引线进行连接。 &lt;br&gt;
	/// 只有设置了IsFlowEnabled为True时，牵引线才起作用。
	/// </field>
	/// <field name="isNegativeDisplayed" type="Boolean">专题图中是否显示属性为负值的数据。</field>
	/// <field name="axesColor" >坐标轴颜色。颜色的表达形式有两种，形如“#FFFFFF”或者“red”。 </field>
	/// <field name="isAxesDisplayed" type="Boolean">是否显示坐标轴。</field>
	/// <field name="axesTextStyle" type="SuperMap.TextStyle">统计图坐标轴文本的风格。</field>
	/// <field name="isAxesTextDisplayed" type="Boolean">是否显示坐标轴的文本标注。</field>
	/// <field name="isAxesGridDisplayed" type="Boolean">是否在统计图坐标轴上显示网格。</field>
	/// <field name="graphTextStyle" type="SuperMap.TextStyle">统计图上的文字标注风格。</field>
	/// <field name="graphTextFormat" type="Number">统计专题图文本显示格式，如百分数、真实数值、标题、标题+百分数、标题+真实数值。有关统计专题图文本显示格式详细信息，请参考GraphTextFormat枚举类。 </field>
	/// <field name="isGraphTextDisplayed" type="Boolean">是否显示统计图上的文本标注。 </field>
	/// <field name="minGraphSize" type="Number">统计图表专题图最小的图表大小，单位为像素。在最大和最小图表之间采用逐渐变化。 </field>
	/// <field name="maxGraphSize" type="Number">统计图表专题图最大图表的尺寸，单位为像素。在最大和最小图表之间采用逐渐变化。 </field>
	/// <field name="isGraphSizeFixed" type="Boolean">一个布尔值指定在放大或者缩小地图时统计图是否固定大小。 </field>
	/// <field name="graduatedMode" type="Nmber">统计图中地理要素的值与图表尺寸间的映射关系（常数，对数，平方根），即分级方式。 &lt;br&gt;
	/// 分级主要是为了减少制作统计专题图中数据大小之间的差异。如果数据之间差距较大，则可以采用对数或者平方根的分级方式来进行，这样就减少了数据之间的绝对大小的差异，使得统计图的视觉效果比较好，同时不同类别之间的比较也还是有意义的。 &lt;br&gt;
	/// 有三种分级模式：常数、对数和平方根，对于有值为负数的字段，不可以采用对数和平方根的分级方式。不同的等级方式用于确定符号大小的数值是不相同的，常数则是按照字段的原始数据进行，对数则是对各分量的总和先取其自然对数、平方根则是对各分量的总和先求其平方根，用最终得到的结果来确定其等级符号的大小。 &lt;br&gt;
	/// 详情请参考GraduatedMode枚举类。</field>
	/// <field name="themeType" type="Number">专题图的类型。详细信息请参见ThemeType类。</field>
	// 统计专题图子项
	this.items = null;
	// x轴坐标偏差值
	this.offsetX = "0.0";
	// y轴坐标偏差值
	this.offsetY = "0.0";
	// 统计类型
	this.graphType;
	// 条状宽度
	this.barWidth = 0.0;
	// 起始角度
	this.startAngle = 0.0;
	this.roseAngle = 0.0;
	this.isFlowEnabled = false;
	this.leaderLineStyle = null;
	this.isLeaderLineDisplayed = false;
	this.isNegativeDisplayed = false;
	this.axesColor = null;
	this.isAxesDisplayed = false;
	this.axesTextStyle = null;
	this.isAxesTextDisplayed = false;
	this.isAxesGridDisplayed = false;
	this.graphTextStyle = null;
	this.graphTextFormat = 0;
	this.isGraphTextDisplayed = false;
	this.minGraphSize = 0.0;
	this.maxGraphSize = 0.0;
	// 是否固定统计图大小
	this.isGraphSizeFixed = true;
	this.graduatedMode = 0;
	this.themeType = new SuperMap.ThemeType().graph;
};
SuperMap.ThemeGraph.registerClass('SuperMap.ThemeGraph', SuperMap.Theme);

SuperMap.ThemeGraphItem = function() {
	/// <summary>统计专题图子项类。该类用来设置统计专题图子项的名称，专题变量，显示风格和分段风格。 &lt;br&gt;
	/// 统计专题图通过为每个要素或记录绘制统计图来反映其对应的专题值的大小。
	/// 统计专题图可以基于多个变量，反映多种属性，即可以将多个专题变量的值绘制在一个统计图上。
	/// 每一个专题变量对应的统计图即为一个专题图子项。 
	/// </summary>
	/// <returns type="SuperMap.ThemeGraphItem">返回 ThemeGraphItem 对象。</returns>
	/// <field name="caption" type="String">专题图子项的名称。 </field>
	/// <field name="graphExpression" type="String">统计专题图的专题变量。专题变量可以是一个字段或字段表达式。</field>
	/// <field name="uniformStyle" type="Style">统计专题图子项的显示风格。 </field>
	/// <field name="rangeSetting" type="SuperMap.ThemeRange">统计专题图子项的分段风格。通过rangeSetting属性，可以对作为专题变量的字段或表达式进行分段，并对每段赋予不同的显示风格。 </field>
	this.caption;
	this.graphExpression;
	this.uniformStyle;
	this.rangeSetting;
};
SuperMap.ThemeGraphItem.registerClass('SuperMap.ThemeGraphItem');

SuperMap.ThemeDotDensity = function() {
	/// <summary>点密度专题图类型。点密度专题图用一定大小、形状相同的点表示现象分布范围、数量特征和分布密度。点的多少和所代表的意义由地图的内容确定。
	/// </summary>
	/// <returns type="SuperMap.ThemeDotDensity">返回 ThemeDotDensity 对象。</returns>
	/// <field name="value" type="Number">专题图中每一个点所代表的数值。 &lt;br&gt;
	/// 点值的确定与地图比例尺以及点的大小有关。地图比例尺越大，相应的图面范围也越大，点相应就可以越多，此时点值就可以设置相对小一些。点形状越大，点值相应就应该设置的小一些。点值过大或过小都是不合适的。</field>
	/// <field name="dotExpression" type="String">用于创建点密度专题图的字段或字段表达式。</field>
	/// <field name="style" type="Style"> 点密度专题图中点的风格。</field>
	/// <field name="themeType" type="Number">专题图的类型。详细信息请参见ThemeType类。</field>
	this.value;
	this.dotExpression;
	this.style;
	this.themeType = new SuperMap.ThemeType().dotDensity;
};
SuperMap.ThemeDotDensity.registerClass("SuperMap.ThemeDotDensity", SuperMap.Theme);

SuperMap.ThemeGraduatedSymbol = function() {
	/// <summary>等级符号专题图类。 &lt;br&gt;
	/// 等级符号专题图是采用不同的形状、颜色和大小的符号，表示各自独立的、以整体概念显示的各个物体的数量与质量特征。通常，以符号的形状、颜色和大小反映物体的特定属性；符号的形状与颜色表示质量特征，符号的大小表示数量特征。&lt;br&gt;
	/// 注意：如果通过连接（Join）或关联（Link）的方式与一个外部表建立了联系，当专题图的专题变量用到外部表的字段时，在显示专题图时，需要设置Layer.DisplayFilter 属性，否则专题图将不能显示外部表的要素。
	/// </summary>
	/// <returns type="SuperMap.ThemeGraduatedSymbol">返回 ThemeGraduatedSymbol 对象。</returns>
	/// <field name="expression" type="String">用于创建等级符号专题图的字段或字段表达式。用于制作等级符号专题图的字段或者字段表达式应为数值型。 </field>
	/// <field name="graduatedMode" type="Number">等级符号专题图分级模式。 &lt;br&gt;
	/// 分级主要是为了减少制作统计专题图中数据大小之间的差异。如果数据之间差距较大，则可以采用对数或者平方根的分级方式来进行，这样就减少了数据之间的绝对大小的差异，使得等级符号图的视觉效果比较好，同时不同类别之间的比较也还是有意义的。&lt;br&gt;
	/// 有三种分级模式：常数、对数和平方根，对于有值为负数的字段，不可以采用对数和平方根的分级方式。&lt;br&gt;
	/// 不同的分级模式用于确定符号大小的数值是不相同的，常数按照字段的原始数据进行，对数则是对每条记录对应的专题变量取自然对数、平方根则是对其取平方根，用最终得到的结果来确定其等级符号的大小。
	/// </field>
	/// <field name="offsetX" type="String">等级符号专题图的水平偏移量或者表达偏移量的字段(该字段必须为数值型字段)表达式。默认使用地图单位。</field>
	/// <field name="offsetY" type="String">等级符号专题图的垂直偏移量或者表达偏移量的字段(该字段必须为数值型字段)表达式。默认使用地图单位。</field>
	/// <field name="leaderLineStyle" type="Style">统计图与其表示对象之间牵引线的风格。</field>
	/// <field name="isLeaderLineDisplayed" type="Boolean">是否显示统计图和它所表示的对象之间的牵引线。如果渲染符号偏移该对象，图与对象之间可以采用牵引线进行连接。  &lt;br&gt;
	/// 只有设置了IsFlowEnabled为True时，牵引线才起作用。
	/// </field>
	/// <field name="isFlowEnabled" type="Boolean">是否允许等级符号图追随其对应的对象流动显示。缺省为 False，即不允许对象流动显示。 </field>
	/// <field name="negativeStyle" type="Style">负值的等级符号风格。</field>
	/// <field name="isNegativeDisplayed" type="Boolean">是否显示负值的等级符号风格，True 表示显示。 </field>
	/// <field name="zeroStyle" type="Style">0值的等级符号风格。</field>
	/// <field name="isZeroDisplayed" type="Boolean">是否显示0值的等级符号风格，True 表示显示。 </field>
	/// <field name="positiveStyle" type="Style">正值的等级符号风格。</field>
	/// <field name="baseValue" type="Number">等级符号专题图的基准值，单位同专题变量的单位。 &lt;br&gt;
	/// 每个符号的显示大小 = PositiveStyle（ZeroStyle 或 NegativeStyle）. getMarkerSize * value / Basevalue，这里的 value 就是 Expression 所对应的值，即 value 是经过分级计算之后的值。 </field>
	/// <field name="themeType" type="Number">专题图的类型。详细信息请参见ThemeType类。</field>
	this.expression;
	this.graduatedMode;
	this.offsetX;
	this.offsetY;
	this.leaderLineStyle;
	this.isLeaderLineDisplayed = false;
	this.isFlowEnabled = false;
	this.negativeStyle;
	this.isNegativeDisplayed = false;
	this.zeroStyle;
	this.isZeroDisplayed = false;
	this.positiveStyle;
	this.baseValue;
	this.themeType = new SuperMap.ThemeType().graduatedSymbol
};
SuperMap.ThemeGraduatedSymbol.registerClass('SuperMap.ThemeGraduatedSymbol', SuperMap.Theme);

SuperMap.ThemeType = function(){
	/// <summary>专题图类型的枚举常量。 &lt;br&gt;
	/// 其中 unique=1; range=2; graph=3; 
	/// graduatedSymbol=4; dotDensity=5; themLabel=7;
	/// </summary>
	/// <returns type="SuperMap.ThemeType">返回 ThemeType 对象。</returns>
	/// <field static="true"  name="unique" type="Number">unique=1，単值专题图。 &lt;br&gt;
	/// 单值专题图中，专题变量的值相同的要素归为一类，为每一类设定一种渲染风格，如颜色或符号等，作为专题变量的字段或表达式的值相同的要素采用相同的渲染风格，从而用来区分不同的类别。&lt;br&gt;
	/// &lt;img src="../img/uniqueValue.png" /&gt;
	/// </field>
	/// <field static="true"  name="range" type="Number">range=2，分段专题图。 &lt;br&gt;
	/// 在分段专题图中，专题变量的值被分成多个范围段，在同一个范围段中要素或记录使用相同的颜色或符号风格进行显示。可使用的分段的方法有等距离分段法，平方根分段法，标准差分段法，对数分段法，等计数分段法。范围分段专题图所基于的专题变量必须为数值型。&lt;br&gt;
	/// &lt;img src="../img/ranges.png" /&gt;
	/// </field>
	/// <field static="true"  name="graph" type="Number">graph=3，统计专题图。 &lt;br&gt;
	/// 统计专题图为每个要素或记录绘制统计图来反映其对应的专题变量的值的大小。统计专题图可以基于多个变量，反映多种属性，即可以将多个变量的值绘制在一个统计图上。目前提供的统计图类型有：面积图，阶梯图，折线图，点状图，柱状图，三维柱状图，饼图，三维饼图，玫瑰图，三维玫瑰图，堆叠柱状图以及三维堆叠柱状图。&lt;br&gt;
	/// &lt;img src="../img/graphy.png" /&gt;
	/// </field>
	/// <field static="true"  name="graduatedSymbol" type="Number">graduatedSymbol=4，等级符号专题图。 &lt;br&gt;
	/// 等级符号专题图用符号的大小来表现要素或记录的所对应的字段或表达式（专题变量）的值的大小。使用渐变的符号来绘制要素时，专题变量的值也被分成很多范围段，在一个范围段中的要素或记录用同样大小的符号来绘制。等级符号专题图所基于的专题变量必须为数值型。&lt;br&gt;
	/// &lt;img src="../img/graduatedSymbol.png" /&gt;
	/// </field>
	/// <field static="true"  name="dotDensity" type="Number">dotDensity=5，点密度专题图。 &lt;br&gt;
	/// 点密度专题图使用点的个数的多少或密集程度来反映一个区域或范围所对应的专题数据的值，其中一个点代表了一定数量，则一个区域内的点的个数乘以点所表示的数量就是此区域对应的专题变量的值。点的个数越多越密集，则数据反映的事物或现象在该区域的密度或浓度越大。点密度专题图所基于的专题变量必须为数值型。&lt;br&gt;
	/// &lt;img src="../img/dotDensity.png" /&gt;
	/// </field>
	/// <field static="true"  name="label" type="Number">label=7，标签专题图。 &lt;br&gt;
	/// 标签专题图是用文本形式在图层上直接显示属性表中的数据，实质上是对图层的标注。&lt;br&gt;
	/// &lt;img src="../img/labelM.png" /&gt;
	/// </field>

	// 矢量数据和栅格数据都可以用来制作专题图，所不同的是矢量数据的专题图是基于其属性表中的属性信息，而栅格数据则是基于像元值。SuperMap提供用于矢量数据（点，线，面以及复合数据集）的专题图，包括单值专题图，范围分段专题图，点密度专题图，统计专题图，等级符号专题图，标签专题图和自定义专题图，也提供适合于栅格数据（格网数据集）的专题图功能，包括栅格范围分段专题图和栅格单值专题图。
	// 栅格单值专题图。&lt;br&gt;
	// 栅格单值专题图中，栅格中像元值相同的像元归为一类，为每一类设定一种颜色，从而用来区分不同的类别。如土地利用分类图中，土地利用类型相同的像元的值相同，将使用相同的颜色来渲染，从而区分不同的土地利用类型。&lt;br&gt;
	// &lt;img src="../img/gridUnique.png"&gt;
	// 栅格范围分段专题图。&lt;br&gt;
	// 在栅格范围分段专题图中，栅格的所有像元值被分成多个范围段，像元值在同一个范围段中的像元使用相同的颜色进行显示。可使用的分段的方法有等距离分段法，平方根分段法，对数分段法。&lt;br&gt;
    // &lt;img src="../img/gridRanges.png"&gt;


	this.unique = 1;
	this.range = 2;
	this.graph = 3;
	this.graduatedSymbol = 4;
	this.dotDensity = 5;
	this.label = 7;
};
SuperMap.ThemeType.registerClass('SuperMap.ThemeType');

SuperMap.GraduatedMode = function() {
	/// <summary>专题图分级模式。主要用在统计专题图和等级符号专题图中。 &lt;br&gt;
	/// 有三种分级模式：常数、对数和平方根，对于有值为负数的字段，不可以采用对数和平方根的分级方式。其中constant = 0; 
	/// squareroot = 1; logarithm = 2;
	/// </summary>
	/// <returns type="SuperMap.GraduatedMode">返回 GraduatedMode 对象。</returns>
	/// <field static="true"  name="constant" type="Number">constant=0，常量分级模式。按属性表中原始数值的线性比例进行分级运算。 </field>
	/// <field static="true"  name="qsquareroot" type="Number">qsquareroot=1，平方根分级模式。按属性表中原始数值自然对数的线性比例进行分级运算。</field>
	/// <field static="true"  name="logarithm" type="Number">logarithm=2，对数分级模式。按属性表中原始数值平方根的线性比例进行分级运算。 </field>
	this.constant = 0;
	this.qsquareroot = 1;
	this.logarithm = 2;
};
SuperMap.GraduatedMode.registerClass('SuperMap.GraduatedMode');

SuperMap.GraphType = function() {
	/// <summary>统计专题图的统计图类，分别为面积图、阶梯图、折线图、点状图、柱状图、三维柱状图、饼图、三维饼图、玫瑰图、三维玫瑰图、堆叠柱状图、三维堆叠柱状图、环状图。 &lt;br&gt;
	/// 其中area = 0;
	/// step = 1; line = 2; point = 3; bar = 4; bar3d = 5;
	/// pie = 6; pie3d = 7; rose = 8; rose3d = 9; 
    /// stack_bar = 12; stack_bar3d = 13;
    /// ring = 14;
	/// </summary>
	/// <field static="true"  name="area" type="Number">area=0，面积图。&lt;br&gt;
	///  &lt;img src="../img/Area.png"&gt;
	/// </field>
	/// <field static="true"  name="step" type="Number">step=1，阶梯图。&lt;br&gt;
	///  &lt;img src="../img/Step.png"&gt;
	/// </field>
	/// <field static="true"  name="line" type="Number">line=2，折线图。&lt;br&gt;
	///  &lt;img src="../img/Line.png"&gt;
	/// </field>
	/// <field static="true"  name="point" type="Number">point=3，点状图。&lt;br&gt;
	///  &lt;img src="../img/Point.png"&gt;
	/// </field>
	/// <field static="true"  name="bar" type="Number">bar=4，柱状图。&lt;br&gt;
	///  &lt;img src="../img/Bar.png"&gt;
	/// </field>
	/// <field static="true"  name="bar3d" type="Number">bar3d=5，三维柱状图。&lt;br&gt;
	///  &lt;img src="../img/Bar3D.png"&gt;
	/// </field>
	/// <field static="true"  name="pie" type="Number">pie=6，饼图。&lt;br&gt;
	///  &lt;img src="../img/Pie.png"&gt;
	/// </field>
	/// <field static="true"  name="pie3d" type="Number">pie3d=7，三维饼图。&lt;br&gt;
	///  &lt;img src="../img/Pie3D.png"&gt;
	/// </field>
	/// <field static="true"  name="rose" type="Number">rose=8，玫瑰图。&lt;br&gt;
	///  &lt;img src="../img/Rose.png"&gt;
	/// </field>
	/// <field static="true"  name="rose3d" type="Number">rose3d=9，三维玫瑰图 。&lt;br&gt;
	///  &lt;img src="../img/Rose3D.png"&gt;
	/// </field>
	/// <field static="true"  name="stack_bar" type="Number">stack_bar=12，堆叠柱状图 。&lt;br&gt;
	///  &lt;img src="../img/StackedBar.png"&gt;
	/// </field>
	/// <field static="true"  name="stack_bar3d" type="Number">stack_bar3d=13，三维堆叠柱状图。&lt;br&gt;
	///  &lt;img src="../img/StackedBar3D.png"&gt;
	/// </field>
	/// <field static="true"  name="ring" type="Number">ring=14，环状图。&lt;br&gt;
	///  &lt;img src="../img/Ring.png"&gt;
	/// </field>
	this.area = 0;
	this.step = 1;
	this.line = 2;
	this.point = 3;
	this.bar = 4;
	this.bar3d = 5;
	this.pie = 6;
	this.pie3d = 7;
	this.rose = 8;
	this.rose3d = 9;
    this.stack_bar = 12;
    this.stack_bar3d = 13;
    this.ring = 14;
};
SuperMap.GraphType.registerClass('SuperMap.GraphType');

SuperMap.GraphTextFormat = function() {
	/// <summary>统计专题图文本显示格式。在统计专题图中，您可以设置各子项文本的显示形式，有百分数、真实数值、标题、标题+百分数、标题+真实数值五种形式。 &lt;br&gt;
	/// 其中percent = 1;
	/// value = 2; caption = 3; caption_percent = 4; textcaption_value = 5;
	/// </summary>
	/// <returns type="SuperMap.GraphTextFormat">返回 GraphTextFormat 对象。</returns>
	/// <field static="true"  name="percent" type="Number">percent=1，百分数。以各子项所占的百分比来进行标注。&lt;br&gt;
	///  &lt;img src="../img/percent.png"&gt;
	/// </field>
	/// <field static="true"  name="value" type="Number">value=2，真实数值。以各子项的真实数值来进行标注。 &lt;br&gt;
	///  &lt;img src="../img/value.png"&gt;
	/// </field>
	/// <field static="true"  name="caption" type="Number">caption=3，标题。以各子项的标题来进行标注。&lt;br&gt;
	///  &lt;img src="../img/caption.png"&gt;
	/// </field>
	/// <field static="true"  name="caption_percent" type="Number">caption_percent=4，标题+百分数。以各子项的标题和百分比来进行标注。&lt;br&gt;
	///  &lt;img src="../img/captionpercent.png"&gt;
	/// </field>
	/// <field static="true"  name="caption_value" type="Number">caption_value=5，标题+真实数值。以各子项的标题和真实数值来进行标注。&lt;br&gt;
	///  &lt;img src="../img/captionvalue.png"&gt;
	/// </field>
	this.percent = 1;
	this.value = 2;
	this.caption = 3;
	this.caption_percent = 4;
	this.caption_value = 5;
};
SuperMap.GraphTextFormat.registerClass('SuperMap.GraphTextFormat');

SuperMap.LabelBackShape = function() {
	/// <summary>指定标签专题图中的标签背景的形状类型。 &lt;br&gt;
	/// 标签背景是一种标签的显示风格，是使用一定颜色的各种形状作为各标签背景，从而可以突出显示标签或者使标签专题图更美观。其中none = 0;
	/// rect = 1; roundrect = 2; ellipse = 3; diamond = 4;
	/// triangle = 5; marker = 100;
	/// </summary>
	/// <returns type="SuperMap.LabelBackShape">返回 LabelBackShape 对象。</returns>
	/// <field static="true"  name="none" type="Number">none=0，空背景。不使用任何的形状作为标签的背景。此为默认值。</field>
	/// <field static="true"  name="rect" type="Number">rect=1，矩形背景。标签背景的形状为矩形。&lt;br&gt;
	///  &lt;img src="../img/rec.png"&gt;
	/// </field>
	/// <field static="true"  name="roundrect" type="Number">roundrect=2，圆角矩形背景。标签背景的形状为圆角矩形。&lt;br&gt;
	///  &lt;img src="../img/RouRec.png"&gt;
	/// </field>
	/// <field static="true"  name="ellipse" type="Number">ellipse=3，椭圆形背景。标签背景的形状为椭圆形。&lt;br&gt;
	///  &lt;img src="../img/ellips.png"&gt;
	/// </field>
	/// <field static="true"  name="diamond" type="Number">diamond=4，菱形背景。标签背景的形状为菱形。&lt;br&gt;
	///  &lt;img src="../img/diamond.png"&gt;
	/// </field>
	/// <field static="true"  name="triangle" type="Number">triangle=5，三角形背景。标签背景的形状为三角形。&lt;br&gt;
	///  &lt;img src="../img/triang.png"&gt;
	/// </field>
	/// <field static="true"  name="marker" type="Number">marker=100，符号背景。标签背景的形状为设定的符号。</field>
	this.none = 0;
	this.rect = 1;
	this.roundrect = 2;
	this.ellipse = 3;
	this.diamond = 4;
	this.triangle = 5;
	this.marker = 100;
};
SuperMap.LabelBackShape.registerClass('SuperMap.LabelBackShape');

SuperMap.OverLengthLabelMode = function() {
	/// <summary>定义标签专题图中超长标签的处理模式。 &lt;br&gt;
	/// 对于标签的长度超过设置的标签最大长度的标签称为超长标签，SuperMap提供三种超长标签的处理方式来控制超长标签的显示行为。 &lt;br&gt;
	/// 其中none = 0;
	/// omit = 1; newline = 2;
	/// </summary>
	/// <returns type="SuperMap.OverLengthLabelMode">返回 OverLengthLabelMode 对象。</returns>
	/// <field static="true"  name="none" type="Number">none=0，对超长标签不进行处理，此为默认模式，即在一行中全部显示此超长标签。</field>
	/// <field static="true"  name="omit" type="Number">omit=1，省略超出部分。此模式将超长标签中超出指定的标签最大长度（MaxLabelLength）的部分用省略号表示。</field>
	/// <field static="true"  name="newline" type="Number">newline=2，换行显示。此模式将超长标签中超出指定的标签最大长度的部分换行显示，即用多行来显示超长标签。</field>
	this.none = 0;
	this.omit = 1;
	this.newline = 2
};
SuperMap.OverLengthLabelMode.registerClass('SuperMap.OverLengthLabelMode');

SuperMap.LayerSetting = function(){
	/// <summary>图层设置基类。目前支持 SuperMapLayerSetting，WmsLayerSetting，WfsLayerSetting。
	/// </summary>
	/// <returns type="SuperMap.LayerSetting">返回 LayerSetting 对象。</returns>
	/// <field name="layerSettingType" type="Number">获取此图层设置的类型。</field>
	
	this.layerSettingType = -1;
};
SuperMap.LayerSetting.registerClass('SuperMap.LayerSetting');