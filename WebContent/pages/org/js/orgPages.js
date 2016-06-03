//全选与全不选  itemName为checkBox的name名称,e一般为this
function checkAll(e, itemName,formName)
{
  var aa = document.forms[formName].getElementsByName(itemName);
  for (var i=0; i<aa.length; i++)
   aa[i].checked = e.checked;
}
function checkAll(e, itemName)
{
  var aa = document.getElementsByName(itemName);
  for (var i=0; i<aa.length; i++)
   aa[i].checked = e.checked;
}
//当然若那个全选框是其它的控件比如是一个按钮，
//或者一个链接什么的话，那复选框里的 checkItem 点击事件可以去掉，以免出错
//allName为全选 按钮的name名字
function checkItem(e, allName)
{
  var all = document.getElementsByName(allName)[0];
  if(!e.checked) all.checked = false;
  else
  {
    var aa = document.getElementsByName(e.name);
    for (var i=0; i<aa.length; i++)
     if(!aa[i].checked) return;
    all.checked = true;
  }
}
function checkItem(e, allName,formName)
{
  var all = document.forms[formName].getElementsByName(allName)[0];
  if(!e.checked) all.checked = false;
  else
  {
    var aa = document.forms[formName].getElementsByName(e.name);
    for (var i=0; i<aa.length; i++)
     if(!aa[i].checked) return;
    all.checked = true;
  }
}
//弹出窗口
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
  
}
//弹出模态窗口

function ShowTitleModal(theURL,features){
  var strReturn=OpenModal(theURL,features);
   if (strReturn="undefined")
    strReturn='/feeltm/pages/org/orgMgr.do?action=query&amp;parentid='+document.forms[0].parentid.value  ;
    // alert(strReturn)
 window.location.href=strReturn
 }
function closeModal(){
  var theUrl='/feeltm/pages/org/orgMgr.do?action=query&amp;parentid='+document.forms[0].parentid.value  ;
   window.returnValue=theUrl
   // alert(theUrl)
  window.close();
}
//取得复选框的值
function checkedValues(e, itemName,formName)
{
  var aa = document.getElementsByName(itemName);
  var str="";
      for (var i=0; i<aa.length; i++){
        if (aa[i].checked ){
	    str+= aa[i].value+",";
	    }
     }
  alert(str);
  document.forms[formName].submit();
 // return str;

}
//delete command
function IsDeleteCheckValues(e, itemName){
    var aa = document.getElementsByName(itemName);
  var result=false;
      for (var i=0; i<aa.length; i++){
        if (aa[i].checked ){
	    result=true;
		break;
	    }
     }
	
	 return result;
}

//submit form
function formSubmit() {
  document.forms[0].submit();
}

//page close to another page
function closePage(url){
window.opener.location.reload();
window.close();

}
