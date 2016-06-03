function showHide( id )
{
  var el= document.getElementById( id );
  var bExpand = true;
  var images = el.getElementsByTagName("IMG");
  if (images[0].src.indexOf("minus.gif")!=-1)
  {
    bExpand = false;
    images[0].src="../../nStraf/images/ajax/plus.gif";
     images[1].src="../../nStraf/images/ajax/closedfolder.gif";
  }else{
    images[0].src="../../nStraf/images/ajax/minus.gif";
    images[1].src="../../nStraf/images/ajax/openedfolder.gif";
  }
  var subs=el.lastChild;
  if(bExpand)
    subs.style.display="block";
  else
    subs.style.display="none";
}

function getSubTree( id )
{
  var submitURL="treeview?parent="+id;
  postXmlHttp( submitURL, 'parseSubTree("'+id+'")' ,'load("'+id+'")');
 
}
function parseSubTree(id)
{

  var el= document.getElementById( id );
 
 var el= document.getElementById( id );
  var ulElmt= document.createElement("UL");
  ulElmt.innerHTML=_xmlHttpRequestObj.responseText;
  el.appendChild(ulElmt);
  var images = el.getElementsByTagName("IMG");
  images[0].setAttribute("src", "../../nStraf/images/ajax/minus.gif");
  images[1].setAttribute("src", "../../nStraf/images/ajax/openedfolder.gif");
  images[0].setAttribute("onclick", new Function("showHide('"+id+"')"));
  var aTag = el.getElementsByTagName("A");
  aTag[0].setAttribute("onclick", new Function("showHide('"+id+"')"));
  var loadDiv= document.getElementById( "load" );
  loadDiv.style.display="none";
 
}
function load(id)
{
 var loadDiv= document.getElementById( "load" );
 loadDiv.style.display="block";
}
var _postXmlHttpProcessPostChangeCallBack;
var _xmlHttpRequestObj;
var _loadingFunction;
function postXmlHttp( submitUrl, callbackFunc ,loadFunc)
{
  _postXmlHttpProcessPostChangeCallBack = callbackFunc;
  _loadingFunction = loadFunc;
  if(window.createRequest)
  {
    try{
      _xmlHttpRequestObj=window.createRequest();
      _xmlHttpRequestObj.open('POST',submitUrl,true);
      _xmlHttpRequestObj.onreadystatechange=postXmlHttpProcessPostChange;
      _xmlHttpRequestObj.send();
    }
    catch(ee){}
  }
  else if(window.ActiveXObject)
  {
    _xmlHttpRequestObj=new ActiveXObject("Microsoft.XMLHTTP");
    _xmlHttpRequestObj.open('POST',submitUrl,true);
    _xmlHttpRequestObj.onreadystatechange=postXmlHttpProcessPostChange;
    _xmlHttpRequestObj.send();
  }
  else if(window.XMLHttpRequest)
  {
  //alert(window.XMLHttpRequest);
    _xmlHttpRequestObj=new XMLHttpRequest();
    _xmlHttpRequestObj.overrideMimeType('text/xml');
    _xmlHttpRequestObj.open('POST',submitUrl,true);
      
    _xmlHttpRequestObj.onreadystatechange=postXmlHttpProcessPostChange;
    _xmlHttpRequestObj.send("");
  }
  
};

function postXmlHttpProcessPostChange( )
{
  if( _xmlHttpRequestObj.readyState==4 && _xmlHttpRequestObj.status==200 )
  {
    setTimeout( _postXmlHttpProcessPostChangeCallBack, 2 );
  }
  if ( _xmlHttpRequestObj.readyState==1 )
  {
    setTimeout( _loadingFunction, 2 );
  }
}