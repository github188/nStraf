<%@ page contentType="text/html; charset=UTF-8" %>
<script language="javascript">
/**
 * strHref: URL
 * strCustom:"width,height,title,bundle"
**/
function OpenModal(strHref,strCustom)
{

  var aryCustom=strCustom.split(",");
  var width=aryCustom.length>0?aryCustom[0]:300
  var height=aryCustom.length>1?aryCustom[1]:300
  var title=aryCustom.length>2?aryCustom[2]:""
  var bundle=aryCustom.length>3?aryCustom[3]:""
  //alert(title);
 
  return OpenModal2(strHref,width,height,title,bundle)	
}

function OpenModal2(strHref,width,height,title,bundle)
{
   var strContextPath='<%=request.getContextPath()%>'
   if(strHref.indexOf('http')<0)
   {
      if(strHref.substr(0,1)=='/')	
        strHref=strContextPath+strHref
      else  
        strHref=strContextPath+"/"+strHref
   }
   if(title==null)
     title=""
     
   if(bundle==null)
     bundle=""
     
   strHref=strContextPath+"/modalDialog.jsp?urlStr="+strHref+"&title="+title
 
   if(bundle!="")
   {
    strHref=strHref+"&bundle="+bundle	
   } 
   
	var iWidth = width;
    var iHeight = height;
    var iTop = (window.screen.availHeight - 20 - iHeight) / 2;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
	//兼容google、火狐高版本，将window.showModalDialog修改为window.open方式打开【IE版本的按window.showModalDialog的方式打开】 ling.tu 2014-12-16
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
		//模态窗口，IE10会有问题
		return window.showModalDialog(strHref,window,"dialogWidth="+iWidth+"px;dialogHeight="+iHeight+"px;dialogTop="+iTop+"px;dialogLeft="+iLeft+"px;resizable=yes;scroll=no;center=yes;help=no;status=no;toolbar=yes")
	}else{//非ie版本浏览器,目前存在的一个问题是，在完成新增、修改等操作前会自动刷新一次，因为*list页面中使用了query()方法，待操作完成后，再对*list进行一次刷新
		window.open (strHref,'','height='+iHeight+',width='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=yes,center=yes,location=no, status=no');
	}
}



function OpenModal_notlogin(strHref,strCustom)
{
	
  var aryCustom=strCustom.split(",");
  var width=aryCustom.length>0?aryCustom[0]:300
  var height=aryCustom.length>1?aryCustom[1]:300
  var title=aryCustom.length>2?aryCustom[2]:""
  var bundle=aryCustom.length>3?aryCustom[3]:""
  
 
  return OpenModal2_notlogin(strHref,width,height,title,bundle)	
  
}

function OpenModal2_notlogin(strHref,width,height,title,bundle)
{
	
   var strContextPath='<%=request.getContextPath()%>'
   if(strHref.indexOf('http')<0)
   {
      if(strHref.substr(0,1)=='/')	
        strHref=strContextPath+strHref
      else  
        strHref=strContextPath+"/"+strHref
   }
   if(title==null)
     title=""
     
   if(bundle==null)
     bundle=""
     
   strHref=strContextPath+"/modalDialog_notlogin.jsp?urlStr="+strHref+"&title="+title
 
   if(bundle!="")
   {
    strHref=strHref+"&bundle="+bundle	
   } 
   
	var iWidth = width;
    var iHeight = height;
    var iTop = (window.screen.availHeight - 20 - iHeight) / 2;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
 //兼容google、火狐高版本，将window.showModalDialog修改为window.open方式打开【IE版本的按window.showModalDialog的方式打开】 ling.tu 2014-12-16
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
		//模态窗口，IE10会有问题
		return window.showModalDialog(strHref,window,"dialogWidth="+iWidth+"px;dialogHeight="+iHeight+"px;dialogTop="+iTop+"px;dialogLeft="+iLeft+"px;resizable=yes;scroll=no;center=yes;help=no;status=no;toolbar=yes")
	}else{//非ie版本浏览器,目前存在的一个问题是，在完成新增、修改等操作前会自动刷新一次，因为*list页面中使用了query()方法，待操作完成后，再对*list进行一次刷新
		window.open (strHref,'','height='+iHeight+',width='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=yes,center=yes,location=no, status=no');
	}
}


function closeWindow(){
	window.close();
}

//处理页面关闭事件，ie版本用window.close(),其他浏览器用parent.close();ling.tu 2014.12.19
function closeModal(flag) {
	//如果需要用户确认是否关闭，请传递参数true或者"true"
	if(flag || flag=="true"){
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
	}
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
		window.close();
	}else{
		parent.close();
	}
}

//新增、修改、删除等操作，在window.open打开方式下，等操作完成后再刷新列表，在操作完成前不刷新列表ling.tu 2014.12.19
function refreshList(){
	if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
		query();
	}else{
		//非ie版本，先不做query()刷新，等操作完成后，在调用回调函数进行刷新
	}
}
</script>