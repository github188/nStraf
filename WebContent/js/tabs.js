document.write('<link rel="stylesheet" href="/js/tabs.css" type="text/css">');
document.getElementsByClassName = function(className,oBox) 
{
	//适用于获取某个HTML区块内部含有某一特定className的所有HTML元素
	this.d= oBox || document;
	var children = this.d.getElementsByTagName('*') || document.all;
	var elements = new Array();
	for (var ii = 0; ii < children.length; ii++) {
		var child = children[ii];
		if(child.className==className)
		{
		    elements.push(child);
			continue;
		}
		var classNames = child.className.split(' ');
		for (var j = 0; j < classNames.length; j++) 
		{
			if (classNames[j] == className) 
			{
				elements.push(child);
				continue;
			}
		}
	}
	return elements;
}

document.getElementsByType = function(sTypeValue,oBox) 
{
	//适用于获取某个HTML区块内部同属于某一特定type的所有HTML元素，如:input,script,link等等
	this.d= oBox || document;
	var children = this.d.getElementsByTagName('*') || document.all;
	var elements = new Array();
	for (var ii = 0; ii < children.length; ii++) {
		if (children[ii].type == sTypeValue) {
			elements.push(children[ii]);
		}
	}
	return elements;
}

function $F() 
{
	var elements = new Array();
	for (var ii = 0; ii < arguments.length; ii++) 
	{
		var element = arguments[ii];
		if (typeof element == 'string')
			element = document.getElementById(element);
		if (arguments.length == 1)
			return element;
		elements.push(element);
	}
	return elements;
}

$Cls = function (s,o){
	return document.getElementsByClassName(s,o);
};

$Type = function (s,o){
	return document.getElementsByType(s,o);
};

$Tag = function (s,o){
	this.d=o || document;
	return this.d.getElementsByTagName(s);
};

function UI_TAB()
{
	var id="";
	var arr=[];
	var panelList=[];
	this.init=function(id)
	{
		id=id;//alert(id);
		arr=$Tag("LI",
		$Cls("ui-tabs-nav",$F(id))[0]
		);
		
		panelList=$Cls("ui-tabs-panel ui-tabs-hide",$F(id));
		/******************注册tab的事件*/
		for(var i=0;i<arr.length;i++)
			arr[i].onclick=function(){
				activeLI(this);
			};
		/******************刷新后保持状态*/
		//alert(location.hash);
		var defaultID=location.hash.replace(/#/gi,"");//alert(defaultID);
		var defaultIndex=-1;
		if(defaultID!=null && defaultID!="")
		{
			for (var x=0; x< panelList.length;x++)
			{
				if(panelList[x].id==defaultID)
				{
					defaultIndex=x;
					break;
				}
			}
		}
		//alert(defaultIndex);
		
		if(defaultIndex!=-1)
		{active(defaultIndex);}
	}
	
	/******************激活*/
	function active(i)
	{
		activeLI(arr[i]);
	}
	
	function activeLI(LI)
	{
		//alert(LI.className);return;//没有使用LI.getAttribute("Class");问题是在IE7里得不到值
		if(LI.className=="ui-tabs-selected")
		{return;}
		else
		{
			var a=$Tag("a",LI)[0];//alert(a.href);
			var str=a.href;
			var temp_arr=str.split("#");
			var _id=temp_arr[temp_arr.length-1];//alert(_id);
			
			for ( var j=0 ;j<arr.length;j++ ) //用for in 无法修改classname，因为for in 是只读的
				arr[j].className="";
			
			LI.className="ui-tabs-selected";//设置当前的为激活状态
			
			//设置所有的ID隐藏
			//alert(id);//alert(panelList.length);
			for(j=0;j<panelList.length;j++)
				panelList[j].style.display="none";
			
			//设置当前的id为show
			$F(_id).style.display="block";
		}
	}
	
	this.activeIndex=function(i)
	{
		activeLI(arr[i]);
	}
	return this;
}
