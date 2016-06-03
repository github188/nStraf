<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>menuInfoList</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<script language="javascript" src="../../js/jquery-1.11.0.js"></script>

<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript">

function query()
{
	var parentid = document.getElementById("parentid").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/menu/MenuInfo!refresh?from=refresh&parentid="+parentid;
	
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,"",false,true);

	
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)">';
	    html += '<td nowrap width="5%">'; 
	    html += '<div align="center">';
	    html += '<input type="checkbox" name="chkList" value="' + entryInfo["menuid"] + '">';
	    html += '</div></td>';
	    html += '<td nowrap width="5%"><div align="center">';
	    var childnum = entryInfo["childnum"];
	    if(childnum>0) 
	        html += '<img src="../../images/share/foldericon.gif">';
	    else
	        html += '<img src="../../images/share/modify.gif">'; 
	    html += '</div></td>';
	    html += '<td nowrap width="18%">'; 
	    html += '<div align="left"><a href="MenuInfo!search.action?menuid=' + entryInfo["menuid"] + '"><font color="#3366FF">'; 
	    html += entryInfo["menuitem"] + '</font></a></div></td>';
	    html += '<td nowrap width="48%">'; 
	    html += '<div align="left">' + entryInfo["actionto"] + '</div></td>';
	    html += '<td nowrap width="14%">'; 
	    html += '<div align="left">' + entryInfo["pic"] + '</div></td>';
	    html += '<td nowrap width="10%">'; 
	    html += '<div align="center"><img src="../../images/share/setting.gif" style="CURSOR: hand" onclick=setfun("' + entryInfo["menuid"] + '")></div></td>';
	    html += '</tr>';

 			<% k++;%>
	return html;
}


function  SelAll(chkAll)
{
   var chkSet = document.all.tags("input")
   for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
		   chkSet[i].checked=chkAll.checked
		}   
	 }
}

function  ComeIn(itemId)
{
   var strUrl="<%=request.getContextPath()%>/pages/menu/MenuInfo!search.action?menuid="+itemId;

   window.location.href=strUrl
}

function ComeOut(itemId)
{
   var strUrl="<%=request.getContextPath()%>/pages/menu/MenuInfo!search.action?control=back&menuid="+itemId
   window.location.href=strUrl
}

function  GetSelIds()
{
     var idList=""
     //var chkSet = document.all.tags("input")
     var chkSet = document.getElementsByName("chkList");
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
		   if(chkSet[i].checked)
		   {
		      idList+=","+chkSet[i].value
		   }    
		}   
	 }
	 
	 if(idList=="")
	    return ""
	 return idList.substring(1)
}

function  add()
{
	var parentid = document.getElementById("parentid").value;
  	var strUrl="/pages/menu/MenuInfo!add.action?parentid="+parentid;

  	var returnValue=OpenModal(strUrl,"600,380,menuInfo.title.add,menu")
	query();

  //	 ajaxAnywhere.submitAJAX();;
  
}

function del()
{
   var idList=GetSelIds();
   if(idList=="")
   {
       alert('<s:text name="del.selectNone"/>')
	   return false
   }
   if(!confirm('<s:text name="menuOperate.del.confirm"/>'))
   {
      return false
   }
  var strUrl="/pages/menu/MenuInfo!delselected.action?menuid="+idList
  var returnValue=OpenModal(strUrl,"600,380,menuInfo.title.del,menu")
  //ajaxAnywhere.submitAJAX();
  query();
}
function Sort()
{
	var parentid = document.getElementById("parentid").value;
  var strUrl="/pages/menu/MenuInfoSort!sort.action?menuid="+parentid;
  var returnValue=OpenModal(strUrl,"600,400,menuInfo.title.sort,menu")

	query();

}
function setfun(itemId)
{
	var strUrl="/pages/menu/MenuInfo!setting.action?menuid="+itemId;
  var returnValue=OpenModal(strUrl,"600,580,menuInfo.title.setting,menu")
	query();
 
}
function ModifyItem(itemId)
{
  var strUrl="/pages/menu/MenuInfo!update.action?menuid="+itemId;
  var returnValue=OpenModal(strUrl,"600,380,menuInfo.title.modify,menu")
  
     query();
    
}
function modify(){
   var aa=document.getElementsByName("chkList");
   var itemId
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			itemId=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<s:text name="operator.update" />')
 	else if (j>1)
 		alert('<s:text name="operator.updateone" />')
	else{
  		var strUrl="/pages/menu/MenuInfo!update.action?menuid="+itemId;
  		var returnValue=OpenModal(strUrl,"600,380,menuInfo.title.modify,menu")
  		
    		 query();
 	   
	}
}

</script>
<body id="bodyid"  leftmargin="0" topmargin="0">

<form name="menuInfoForm" action="<%=request.getContextPath()%>/pages/menu/MenuInfo!search" method="post">
	<s:iterator value="menuInfo">
		<input type="hidden" name="menuid" id="menuid" value='<s:property value="parentid"/>'>	
		<input type="hidden" name="parentid" id="parentid" value='<s:property value="parentid"/>'>
		<!--<input type="hidden" name="floor" value='<s:property value="floor" />'>-->
	</s:iterator>
<aa:zone name="formlist1">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
  <tr>
    <td width="25" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
    <td class="orarowhead">
	      <!--navigation-->
	     <s:property value="%{getText('menuInfo.navigation')}" /><s:property value="%{getText('label.colon')}"/>
	     
			  <s:property value="menuNavigation"/>
	</td>
	<td	width="50%"	align="right">
	<s:property value="menuinfo.menuid"/>
		<tm:button  menuid="menuinfo.menuid" type="session" site="2"/>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>
      <table width="100%" cellSpacing="0" cellPadding="0">
           <tr>
            <td width="5%"> 
              <div align="center">
              <input type="checkbox" name="chkAll"  id="chkAll"   value="all"  onClick="SelAll(this)">
            </div>
          </td>
            <td width="12%"> 
              <div align="left"><label for=chkAll><s:text name="label.select.all"/></label></div>
    </td>
    <td width="83%">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    		 <tbody id="recordCount">
    		 <tr>
    			 <td height="22"><s:text name="page.nav.total"/>
    			<bean:size id="iTotal" name ="menuList"/><%=iTotal%>
    			 </td>
    			<td width="21%" height="22" valign="middle">
    				<div align="center"> </div>
                  </td>
    			<td height="22">&nbsp;&nbsp; 
                  </td>
                </tr>
               </tbody>
                </table>
                </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=tabDirList>
    <tr> 
      <td nowrap width="5%" class="oracolumncenterheader" > 
        <div align="center"><s:text name="label.select"/></div>
      </td>
      <td nowrap width="5%" class="oracolumncenterheader"> 
        <!--modify -->
        <s:property value="%{getText('label.label')}"/></td>
      <td nowrap width="18%" class="oracolumncenterheader"> 
        <!--menuname -->
        <s:property value="%{getText('menuInfoForm.menuname')}"/></td>
      <td nowrap width="48%" class="oracolumncenterheader""> 
        <!--order -->
        <s:property value="%{getText('menuInfoForm.setorder')}"/></td>
      <td nowrap width="14%" class="oracolumncenterheader""> 
        <!--order -->
        <s:property value="%{getText('menuInfoForm.pic')}"/></td>
      <td nowrap width="10%" class="oracolumncenterheader"> 
        <!--option -->
        <s:property value="%{getText('menuInfoForm.setoption')}" /></td>
    </tr>
    
    <s:iterator value="menuInfo">
    <s:if test='parentid!="0"'>
	    <tr class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	      <td nowrap width="5%">&nbsp;</td>
	      <td nowrap width="5%">&nbsp;</td>
	      <td nowrap width="18%"> 
			<a href='javascript:ComeOut("<s:property value="parentid"/>")'>
			<s:property value="%{getText('menuInfo.list.back')}" /></a>
	      </td>
	      <td nowrap width="48%">&nbsp;</td>
	      <td nowrap width="14%">&nbsp;</td>
	      <td nowrap width="10%">&nbsp;</td>
	    </tr>
    </s:if>
    </s:iterator>
    <!-- list start -->
  <tbody id="formlist">
    <s:iterator value="menuList" status="rowstatus"> 
    <s:if test="#rowstatus.odd == true">
    	<tr class="trClass0"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
    </s:if> <s:else>
    	<tr class="trClass1"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
    </s:else> 
      <td nowrap width="5%"> 
        <div align="center"> 
          <input type="checkbox" name="chkList" value='<s:property value="menuid"/>'>
        </div>
      </td>
      <td nowrap width="5%"> 
        <div align="center"> <s:if test="childnum>0" > 
          <img src="../../images/share/foldericon.gif"> 
          </s:if> <s:else> 
          <img src="../../images/share/modify.gif"> 
          </s:else> </div>
      </td>
      <td nowrap width="18%"> 
        <div align="left"> <a href="MenuInfo!search?menuid=<s:property value="menuid"/>"> <font color="#3366FF">
          <s:property value="menuitem"/> </font></a> </div>
      </td>
      <td nowrap width="48%"> 
        <div align="left"><s:property value="actionto"/></div>
      </td>
      <td nowrap width="14%"> 
        <div align="left"><s:property value="pic"/></div>
      </td>
      <td nowrap width="10%"> 
        <div align="center"><img src="../../images/share/setting.gif" style="CURSOR: hand" onClick="setfun('<s:property value="menuid"/>')"> </div>
      </td>
    </tr>
    </s:iterator> 
</tbody>
    <!-- list end -->
  </table>
  </aa:zone>
</form>
</body>
</html>