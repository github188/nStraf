<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE>left</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<SCRIPT src="js/jquery-1.11.0.js" type=text/javascript></SCRIPT>
<SCRIPT src="js/prototype.lite.js" type=text/javascript></SCRIPT>
<SCRIPT src="js/moo.fx.js" type=text/javascript></SCRIPT>
<SCRIPT src="js/moo.fx.pack.js" type=text/javascript></SCRIPT>
<script type="text/javascript" src="js/ajax/ajax.js"></script>
<script type="text/javascript" src="js/ajax/all.js"></script>
<link rel="stylesheet" href = "css/tree.css">
<STYLE type=text/css>
.itemContainer{
	background:url("images_new/main/leftFrameItemBg.jpg") left top;
	height:58px;
	width: 180px;
}
.itemContainerTd1{
	width:10%;
}
.itemContainerTd2{
	width:30%
}
.itemContainerTd3{
	width:0%;
}
.itemContainerTd4{
	width: 40%
}
.itemContainerTd5{
	width:20%;
}
BODY {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-SIZE: 13px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px; BACKGROUND: url("images_new/main/leftFrameBg.jpg")
}
A {
	TEXT-DECORATION: none
}
IMG {
	BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px
}
H1 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px;font-weight:normal
}
#container {
	WIDTH: 180px
}
UL LI {
	PADDING-TOP: 3px; LIST-STYLE-TYPE: none; TEXT-ALIGN: center
}
UL LI A:hover {
	COLOR: #b18311
}
UL LI A {
	COLOR: #000; FONT-SIZE: 12px;
}
UL LI A SPAN {
	PADDING-RIGHT: 0px; DISPLAY: block; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
UL {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
.title {
	BACKGROUND: url(images_new/main/leftFrameTitleBg.jpg) no-repeat 0px 0px; CURSOR: pointer; HEIGHT: 30px; 
}
.title A {
	PADDING-RIGHT: 0px; DISPLAY: block; PADDING-LEFT: 0px; PADDING-BOTTOM: 7px; PADDING-TOP: 9px; TEXT-ALIGN: center;color:#FFFFFF;font-size:13px;font-weight:bold;font-family:'宋体';letter-spacing:1px
}

.MyCheckBox{
	behavior:url(<%=request.getContextPath()%>/htc/checkbox.htc);
}
.MyButton{
	behavior:url(<%=request.getContextPath()%>/htc/button.htc);
}
.MyInput{
  behavior:url(<%=request.getContextPath()%>/htc/input.htc);
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
}
.MySelect{
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
  visibility: visible;
}

.CheckBoxTable{
	display:inline;
	cursor: hand;
	height:16;
}

.txtSel{
   font-size:9pt;
   color:green;
}
.txtNoSel{
   font-size:9pt;
   color:black;
}
.checkBoxImg{

}
.checkBoxDisable{
    font-size:9pt;
	color:#000000;cursor:default;
}
.ButtonTable{
	display:inline;
	cursor: hand;
	height:19;
}
.ButtonTable_td{
	border-top:1pt solid #000000;border-bottom:1pt solid #000000;
	padding-top:0;
	background-image: url(<%=request.getContextPath()%>/images/btbak.gif);
	background-repeat: repeat-x;
	cursor:hand;
	color:#000000;
}
.ButtonDisable{
	filter:dropshadow(color=#FFFFFF,offx=1,offy=1);color:#b2b2b2;cursor:default;
}
.ButtonValue{
	color:#222222;
}

.global{FONT-SIZE: 9pt; COLOR: #000000; padding-left:3px; padding-right:5px; }
A.global:link{COLOR: #000000; TEXT-DECORATION: none}
A.global:visited { COLOR: #000000; TEXT-DECORATION: none}
A.global:hover { COLOR: #ffffff; TEXT-DECORATION: none}
</STYLE>

<META content="MSHTML 6.00.2900.5726" name=GENERATOR></HEAD>
<BODY>
<bean:define id="loginUser" name ="tm.loginUser" property="username"/>
<%
String menuid=(String)request.getAttribute("menuid");
%>
	<DIV id=container>
	<%
		String menulist = (String)request.getAttribute("lefttree");
		out.print(menulist);
	%>
	</DIV>
	<SCRIPT type=text/javascript>
			var contents = document.getElementsByClassName('content');
			var toggles = document.getElementsByClassName('title');
			var prevele;//定义点击对象全局变量以保存前一对象
			var myAccordion = new fx.Accordion(
				toggles, contents, {opacity: true, duration: 100}
			);
			//从连接中获取参数
			var Request = new Object();
			Request = GetRequest();
			//打开第几个菜单contents
			var open = Request["open"];
			if(open!=undefined){
				myAccordion.showThisHideOpen(contents[open]);
				//定位对应打开页面菜单的背景
				var changeBg = Request["changeBg"];	
				if(changeBg!=undefined){
					//根据链接返回参数查询出第几个contents中的菜单DIV[class="itemBackground"]改变底色
					prevele = document.getElementsByClassName("content")[open].firstChild.children[changeBg].firstChild.firstChild;
					document.getElementsByClassName("content")[open].firstChild.children[changeBg].firstChild.firstChild.style.backgroundColor = "#A1C1CE";
					 /* document.getElementsByClassName("itemBackground")[changeBg].style.backgroundColor = "#A1C1CE";  */
					/* document.getElementsByClassName("content")[open].style.backgroundColor = "#A1C1CE"; */
				}
			}else{
				myAccordion.showThisHideOpen(contents[0]);
			}
			
			//判断对象是否为空
			 function isEmpty(Request){
				if (typeof Request === "object" && !(Request instanceof Array)){
					var hasProp = false;
					for (var prop in Request){
						hasProp = true;
						break;
					}
					return hasProp;
				}
				return false;
			} 
            /**
           	获取连接中的参数
            */
            function GetRequest() {
            	  var url = location.search; //获取url中"?"符后的字串
            	   var theRequest = new Object();
            	   if (url.indexOf("?") != -1) {
            	      var str = url.substr(1);
            	      strs = str.split("&");
            	      for(var i = 0; i < strs.length; i ++) {
            	         theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
            	      }
            	   }
            	   return theRequest;
            	}
            
			//兼容IE8,如果不支持getElementsByClassName,getElementsByClassName重写
			if(!document.getElementsByClassName){ 
					document.getElementsByClassName = function(className, element){ 
						var children = (element || document).getElementsByTagName('*'); 
						var elements = new Array(); 
						for (var i=0; i<children.length; i++){ 
						var child = children[i]; 
						var classNames = child.className.split(' '); 
							for (var j=0; j<classNames.length; j++){ 
							if (classNames[j] == className){ 
								elements.push(child); 
								break; 
								} 
							} 
						} 
						return elements; 
					}; 
				}
			 //监听方法
			 function addEvent(elm, evType, useCapture) {
			 	var ele = elm;
			 	//非IE
			 	if (elm.addEventListener) { elm.addEventListener(evType,function(){
			 		///点击的前一对象恢复mouseout状态
			 				  changeBgColor(prevele,1); 
			 				//当前点击对象改变为mouseover状态
			 				 changeBgColor(ele,0); 
			 				 prevele = ele; 

			 	}, useCapture);//DOM2.0 
			 		return true; }
			 	else if (elm.attachEvent) { //IE
			 		var r = elm.attachEvent('on' + evType, function(){
			 				///点击的前一对象恢状态
			 				  changeBgColor(prevele,1); 
			 				//当前点击对象改变为#e8eaf7状态
			 				 changeBgColor(ele,0); 
			 				 prevele = ele; 

			 		});//IE5+ return r; 
			 		} 
			 	else { 
			 			elm['on' + evType] = fn;//DOM 0 
			 		} 
			 	} 
			 
			 //改变背景颜色
			 function changeBgColor(ele,operate){
					if(operate==0){
						ele.style.backgroundColor = "#A1C1CE";
					}
					else{
						if(ele!=undefined){
						ele.style.backgroundColor = "#e8eaf7";
						}
					}
			}
			 window.onload = function(){
				 //添加监听事件
				   for(var i=document.getElementsByClassName("itemBackground").length-1;i>=0;i--){
									ele = document.getElementsByClassName("itemBackground")[i];
									addEvent(ele,"click",false)
								} 
				}
			 
			 //重定向原来目录时调用
			 function redirectOriginal(open,changeBg){
				 changeOriginalBg();//所有backgroundColor改为最初颜色
				 if(changeBg!=undefined&&open!=undefined){
						//根据链接返回参数查询出第几个contents中的菜单DIV[class="itemBackground"]改变底色
						prevele = document.getElementsByClassName("content")[open].firstChild.children[changeBg].firstChild.firstChild;
						document.getElementsByClassName("content")[open].firstChild.children[changeBg].firstChild.firstChild.style.backgroundColor = "#A1C1CE";
					}
				 myAccordion.showThisHideOpen(contents[open]);
			 }
			//-------------------------------------------------

			//改变原本背景颜色
			 function changeOriginalBg(){
				 for(var i=document.getElementsByClassName("itemBackground").length-1;i>=0;i--){
					 document.getElementsByClassName("itemBackground")[i].style.backgroundColor = "#e8eaf7";
					} 
			 }
	</SCRIPT>

</BODY></HTML>
