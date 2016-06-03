<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title><s:text name="menuOperateInfo.table1title" /></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">

function  SelAll(chkAll)
{
     var chkSet = document.all.tags("input");
     var chkSet = document.getElementsByName("chkList");
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
		   chkSet[i].checked=chkAll.checked
		}   
	 }
}
function add()
{
	var strUrl="/pages/menu/MenuOperate!add.action";
  var returnValue=OpenModal(strUrl,"600,480,menuOperate.title.add,menu")
     query()
}

function update(code)
{
	var strUrl="/pages/menu/MenuOperate!update.action?objcode="+code;
    var returnValue=OpenModal(strUrl,"600,380,menuOperate.title.modify,menu")
     query()
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
  var strUrl="/pages/menu/MenuOperate!delselected.action?operid="+idList;
  var returnValue=OpenModal(strUrl,"600,380,menuOperate.title.del,menu")
  query()
}

function modify(){
   var aa=document.getElementsByName("chkList");
   var code
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			code=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<s:text  name="operator.update" />')
 	else if (j>1)
 		alert('<s:text  name="operator.updateone" />')
	else{
		var strUrl="/pages/menu/MenuOperate!update.action?objcode="+code;
 		 var returnValue=OpenModal(strUrl,"600,380,menuOperate.title.modify,menu")
     		query()
	}
}

function query()
{
	var pageNum = document.getElementById("pageNum").value;

	var actionUrl = "<%=request.getContextPath()%>/pages/menu/MenuOperate!refresh?from=refresh&pageNum="+pageNum;

	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
   <% if (k%2 == 1){%>
    	html += "<tr id='tr<%=k%>' class='oracletdone' onClick='pass(downloadList,this)'>"; 
   <%
   } else
   {%>
    	html += "<tr class='oracletdtwo' onClick='pass(downloadList,this)'>";
    <%}%>
    html += "<td nowrap width='8%' align='center'><div align='center'>"; 
    html += "<input type='checkbox' name='chkList' value='" + entryInfo["operid"] + "'></div></td>";
    html += "<td nowrap><div align='center'>" + entryInfo["operid"] + "</div></td>";
    html += "<td nowrap><div align='center'>" + entryInfo["opername"] + "</div></td>";
    html += "<td nowrap><div align='center'>" + entryInfo["clickname"] + "</div></td>";
    html += "<td nowrap><div align='center'>" + entryInfo["picpath"] + "</div></td>";
	html += "<td nowrap><div align='center'>" + entryInfo["keys"] + "</div></td>";
	html += "<td nowrap><div align='center'>" + entryInfo["types"] + "</div></td>";	
	html += "<td nowrap><div align='center'>" 
	var site = entryInfo["site"];
	if(site == 1)	
		html += "位置1";
	else if(site == 2)
		html += "位置2";
	else if(site == 3)
		html += "位置3";
	else if(site == 0)
		html += "通用";
    html += "</div></td>";  
    html += "</tr>";
 			<% k++;%>
	return html;
}

</script>
<body id="bodyid"  leftmargin="0" topmargin="0">
 <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
<form name="menuOperateForm" method="post" action="<%=request.getContextPath()%>/pages/menu/MenuOperate!list.action">		
<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
<input type="hidden" name="pageSize" id="pageSize" value="20" />
<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
  <tr>
    <td width="25" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
    <td class="orarowhead"><s:text name="menuOperateInfo.table1title1" /></td>
    <td	width="50%" align="right"><tm:button site="2"/></td>
  </tr>
</table>

<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>
       <table width="100%" cellSpacing="0" cellPadding="0">
          <tr>
            <td width="5%"> 
              <div align="center">
              <input type="checkbox" name="chkAll" id="chkAll" value="all" onClick="SelAll(this)">
              </div>
          	</td>
          	<td width="12%"> 
             	<div align="left"><label for=chkAll><s:text name="label.select.all" /></label></div>
		  	</td>
    <td width="83%"	align="right">
    <div id="pagetag"><tm:pagetag pageName="menu.currPage" formName="menuOperateForm" /></div></td></tr></table>
		</td>
	</tr>
</table>

  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
    <tr> 
      <td width="6%" class="oracolumncenterheader"><s:text name="label.select"/></td>
      <td width="8%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.id" /></td>
      <td width="15%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.opername" /></td>
      <td width="15%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.clickname" /></td>
      <td width="25%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.picpath" /></td>
      <td width="10%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.keys" /></td>
      <td width="10%" class="oracolumncenterheader"><s:text name="menuOperateInfoForm.types" /></td>
	  <td width="10%" class="oracolumncenterheader"><s:text name="menuInfo.site" /></td> 	
    </tr>
    <%--<s:if test="grpLst ? exists"> 
    --%><%int i = 1;%>
    <tbody id="formlist">
    <s:iterator value="grpLst" status="rowstatus" id="item"> 
    <%if ( i%2 == 1 ) {%>
    <tr id="tr<%=i%>" class="oracletdone" onClick="pass(downloadList,this)"> 
      <%} else {%>
    <tr id="tr<%=i%>" class="oracletdtwo" onClick="pass(downloadList,this)"> 
      <%}%>
      <td nowrap width="8%" align="center"><div align="center"> 
          <input type="checkbox" name="chkList" value='<s:property value="operid"/>'>
        </div></td>
      <td nowrap><div align="center"><s:property value="operid"/></div></td>
      <td nowrap><div align="center"><s:property value="opername"/></div></td>
      <td nowrap><div align="center"><s:property value="clickname"/></div></td>
      <td nowrap><div align="center"><s:property value="picpath"/></div></td>
	    <td nowrap><div align="center"><s:property value="keys"/></div></td>
	    <td nowrap><div align="center"><s:property value="types"/></div></td>	
	    <td nowrap><div align="center">
	    	
	    	<tm:dataDir beanName="item" property="site" path="menuMgr.buttonSite"></tm:dataDir>
	   </div></td>  
    </tr>
    <%i++;%>
    </s:iterator><%-- </s:if>
    </tbody> 
  --%></table>
 </form>		
</body>
</html>