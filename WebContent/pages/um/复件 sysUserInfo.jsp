<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/pagination.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>operation info</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../calendar/fixDate.js"></script>
<script language="javascript" src="../../js/aa.js"></script>

<script language="javascript" src="../../js/jquery.js"></script>
<script language="javascript" src="../../js/jquery-1.4.2.js"></script>

<script language="JavaScript" type="text/javascript"> 
 var html="";



function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var pageSize = document.getElementById("pageSize").value;

	var date = new Date();
	var timestamp=date.getTime(); 
	
	var actionUrl = "/phbank/pages/um/sysUserInfo!refresh?from=refresh&pageNum="+pageNum+"&pageSize="+pageSize+"&timestamp="+timestamp ;
	
	var attrString = "userid,username,orgid,createdate,invaliddate,isvalid,usergroup";
	refresh(attrString , actionUrl , pageNum);
}

function refresh(attrString,actionUrl,pageNum){
   var pageCount=0;
   var recordCount=0;
   var html="";
   var attrArr = createAttrArr(attrString);
   
   $("#formlist").empty();
    $("#pagetag").empty();
   $.ajax({
     type:"post",
     url:actionUrl,
	 dataType:"json",
	 cache: false,
	 async:true,
	 success:function(data){
	 	 $.each(data,function(index,commit){
	 		pageCount = commit['pageCount'];
	  		recordCount = commit['recordCount'];
	  		parseData(index,commit,attrString);
	  		
	  });	  
	   $("#formlist").html(html);
	  
	   $("#pagetag").html(getPageTagHTML(pageNum,pageCount,recordCount));
	 }
   });   
}

function parseData(entry,entryInfo,attrString)
{
	var attrArr = createAttrArr(attrString);
	createHTML(entry,entryInfo,attrArr);
}

function createAttrArr(attrString)
{
	var attrArr = attrString.split(",");
	return attrArr;
}

//根据给定的属性名，构建所需的HTML
function createHTML(entry,entryInfo,attrArr)
{
	var html="";
	html+= '<tr id="'+entryInfo['userid']+'" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
  			html+= '<td nowrap align="center"><input type="checkbox" name="id" value="'+entryInfo['userid']+'"/></td>';
   			html+= '<td nowrap width="12%">';
	   		html+= '<div align="center" id="userid">';
	    	html+='<a href="">'+entryInfo['userid']+'</a>';
	   		html+= '</div>';
  			html+= ' </td>';
  			html+=  '<td nowrap width="12%">';
   			html+=' <div align="center" id="username">';
    		html+='<a href="">'+entryInfo['username']+'</a>';
    		html+=	'</div>';
   			html+= '</td>';
 			html+= '  <td nowrap width="12%">';
  			html+=  	'<div align="center"></div>';
			html+=	'</td>';
   			html+=' <td nowrap width="12%"><div align="center">'+entryInfo['createdate']+'</div></td>';
  			html+=   '<td nowrap width="12%"><div align="center">'+entryInfo['invaliddate']+'</div></td> ';
  			html+=  ' <td nowrap width="10%">';
			html+=    ' <div align="center">';
			
			if(entryInfo['isvalid']=="Y")   
	   			html+= ' <img src="../../images/share/button_ok.gif">';	
	   		else
	   		    html+= '<img src="../../images/share/mood9.gif">';    
	   		     	        
	  		html+=  ' </div>';
   			html+= ' </td>';
  			html+=  ' <td nowrap width="14%" align="center">'+entryInfo['groupName']+'</td>' ; 
 			html+=  '</tr>' ;
	return html;
}

function getPageTagHTML(pageNum,pageCount,recordCount)
{
	var pagetag = "";
	pagetag += '<table border="0" cellspacing="1" cellpadding="0" class="Pagination_table">';
	pagetag += '<input type="hidden" name="pageSize" value="10">'
	pagetag += '<td nowrap class="Pagination">'
	
	//last page
	if(pageNum>1 && pageNum<= pageCount)
		pagetag += '<INPUT class=BtnDisable type=button value="<s:property value="%{getText('page.nav.forward')}"/>" name=lastpage onClick="MovePrevious()" >'
	else
		pagetag += '<INPUT class=BtnDisable type=button value="<s:property value="%{getText('page.nav.forward')}"/>" name=lastpage  disabled="disabled" onClick="MovePrevious()">'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination">'
	
	//next page
	if(pageNum>=1 && pageNum<pageCount)
		pagetag += '<INPUT class=Btn type=button value="<s:property value="%{getText('page.nav.backward')}"/>" name=nextpage onClick="MoveNext()"></td>'
	else
		pagetag += '<INPUT class=Btn type=button value="<s:property value="%{getText('page.nav.backward')}"/>" name=nextpage onClick="MoveNext()"  disabled="disabled"></td>'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination"><s:property value="%{getText('page.nav.total')}"/>'+recordCount+'<s:property value="%{getText('page.nav.size')}"/>'+pageCount+'<s:property value="%{getText('page.nav.page')}"/></td>'
	pagetag += '<td nowrap class="Pagination"><s:property value="%{getText('page.nav.go')}"/>'
	pagetag += '<input type="text" name="gotoPageNo" id="gotoPageNo" maxlength="8" class=textbox size="2" value="'+pageNum+'" onfocus="this.select()" onKeyPress="if(window.event.keyCode==13) GotoPage()">'
	pagetag += '<s:property value="%{getText('page.nav.page')}"/>'
	pagetag += '<INPUT class=Btn type=button value=GO name=GO onclick="GotoPage()">'
	pagetag += '</td>'
	pagetag += '</tr>'
	pagetag += '</table>'
 
    return pagetag;
}
function test2(data,pageCount,recordCount,html)
{
  var pageCount=0;
   var recordCount=0;
   var html="";
   var attrArr = createAttrArr(attrString);
	$.each(data,function(entry,entryInfo){
	 		pageCount = entryInfo['pageCount'];
	  		recordCount = entryInfo['recordCount'];
	  		parseData(entry,entryInfo,attrString);
	  		//html += createHTML(entry,entryInfo,attrArr);
	  });	  
}
//遍历data中的数据
function test(data)
{
	var str = "";
    for (var one in data)//对外层对象的遍历，one是外层对象名
    {
        for(var key in data[one])//对对象中的属性进行遍历，key是内层属性名，data[one][key]是内层属性值
        {
        	if(key == "userid")
        	{
              str += '<tr id="'+data[one][key]+'" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
       		  str +=  '<td nowrap align="center"><input type="checkbox" name="id" value="'+data[one][key]+'"/></td>';
   			  str +=  '<td nowrap width="12%">';
	   		  str +=  '<div align="center" id="userid">';
	    	  str += '<a href="">'+data[one][key]+'</a>';
	   		  str +=  '</div>';
  			  str +=  ' </td>';
  			  str +=  '<td nowrap width="12%">';
   			
   			else if()key == "username"}
   			{
   			  str += ' <div align="center" id="username">';
   			  str+='<a href="">'+data[one][key]+'</a>';
    		  str+=	'</div>';
   			  str+= '</td>';
 			  str+= '  <td nowrap width="12%">';
  			  str+=  	'<div align="center"></div>';
			  str+=	'</td>';
   			}
   			else if(key =="createdate")
   			{
   			  str+=' <td nowrap width="12%"><div align="center">'+data[one][key]+'</div></td>';
  			  str+=   '<td nowrap width="12%"><div align="center">'+data[one][key]+'</div></td> ';
  			  str+=  ' <td nowrap width="10%">';
			  str+=    ' <div align="center">';
   			}
   			else if(key == "isvalid")
   			{
   			  if(data[one][key]=="Y")   
	   			str+= ' <img src="../../images/share/button_ok.gif">';	
	   		  else
	   		    str+= '<img src="../../images/share/mood9.gif">';    
	   		     	        
	  		  str+=  ' </div>';
   			  str+= ' </td>';
   			}
   			else if(key == "groupName")
   			{
   			  str+=  ' <td nowrap width="14%" align="center">'+data[one][key]+'</td>' ; 
 			  str+=  '</tr>' ;
   			}
        }
    }
    
   $.each(data,function(entry,entryInfo){
    	alert(entryInfo['userid']);
    })
 
   return str;
  
}


function add(){

	 // var resultvalue = window.showModalDialog();('/pages/um/sysUserInfo!addpage','750,650,roleInfo.addOperTitle,um');
	// var resultvalue =window.showModalDialog('<%=request.getContextPath()%>/modalDialog.jsp?urlStr=<%=request.getContextPath()%>/pages/um/sysUserInfo!addpage',window,"dialogWidth:750px;dialogHeight:650px;resizable:no;scroll:0;help:0;status:0")
	 var strUrl="/pages/um/sysUserInfo!addpage";
	 var features="750,650,roleInfo.addOperTitle,um";
	  var resultvalue =OpenModal(strUrl,features);	
		query();
   
}
function  GetSelIds()
{
    var idList="";
	var  em= document.all.tags("input");
	for(var  i=0;i<em.length;i++)
	{
	   if(em[i].type=="checkbox")
	   {
		   if(em[i].checked){
			   idList+=","+em[i].value.split(",")[0];
		   }
	   }
	}
	
	 if(idList=="")
	    return ""
	 return idList.substring(1)
}
function  SelAll(chkAll)
{
     var   em=document.all.tags("input");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked
		   }
	   }
}

function del()
{	

   var idList=GetSelIds();   
   if(idList=="")
   {
     
	   return false
   }
   
   
 	if (confirm(''))
    {
	  var strUrl="/pages/um/sysUserInfo!delete?userids="+idList;
       var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um");
      
		query();
    }
}

function refreshUsrGrp(){

     var aa=document.getElementsByName("id");
     var userid ;
     var level ;
     var j=0;
     
	 for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
   		
			userid=aa[i].value.split(",")[0];
			level=aa[i].value.split(",")[1];
			j=j+1;
		}
    }
	if (j==0)
 		alert('请选择需要设置的用户组的用户');
 	else if (j>1)
 		alert('每次只能对一个用户进行用户组的设置');
 	else{
		//var strUrl="/pages/um/sysUserGroup.do?userid="+userid+"&level="+level;
		//var features="600,450,grpInfo.updateTitle,um";
		//var resultvalue = OpenModal(strUrl,features);
		query();
	}
}

function modify(){
   var aa=document.getElementsByName("id");
   var userid
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value.split(",")[0];
			j=j+1;
		}
   }
	if (j==0)
 		alert('')
 	else if (j>1)
 		alert('')
	else{
		var strUrl="/pages/um/sysUserInfo!modify?userid="+userid;
		var features="600,500,operInfo.updateTitle,um";
		var resultvalue = OpenModal(strUrl,features);
	  if(resultvalue!=null)
    {
		query();
    }
	}
}
//show sysUserInfo 
function showSysUserInfo(userid){
		var strUrl="/pages/um/sysUserInfo!view?menuid=<%=request.getParameter("menuid")%>&userid="+userid;
		
		var features="600,500,operInfo.viewOperator,um";
		var resultvalue = OpenModal(strUrl,features);
	  if(resultvalue!=null)
    {
		query();
    }
}


function resetPassWd(){
	  var aa=document.getElementsByName("id");
   var userid
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value.split(",")[0];
			j=j+1;
		}
   }
	if (j==0)
 		alert('')
 	else if (j>1)
 		alert('')
 	else if (confirm("")){ 		
		var strUrl="/pages/um/sysUserInfo!resetPW&userid="+userid;
		var features="500,350,pwdsetting.title,um";
		var resultvalue = OpenModal(strUrl,features);
		query();
	}
}
function resetLock()
{
    var aa=document.getElementsByName("id");
   var userid
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value.split(",")[0];
			j=j+1;
		}
   }
	if (j==0)
 		alert('')
 	else if (j>1)
 		alert('')
 	else if (confirm("")){ 		
		var strUrl="/pages/um/sysUserInfo!resetUser?userid="+userid;
		var features="500,350,um.unlock,um";
		var resultvalue = OpenModal(strUrl,features);
		query();
	}
}


</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="sysUserForm" action="/pages/um/sysUserInfo!list" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这个隐藏域 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	
  
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
           
                <table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
                    <tr>
                        <td>
                            <fieldset  width="100%">
                                <legend><s:property value="%{getText('queryCondition.query')}"/></legend>
                             
        <table width="100%">
          <tr> 
            <td width="15%" align="right"> <s:property value="%{getText('operInfoform.userIdentifier')}"/><s:property value="%{getText('label.colon')}"/></td>
            <td width="15%"><input name="userid" type="text" id="userid"  class="MyInput"> </td>
            <td width="15%" align="right"> <s:property value="%{getText('operInfoform.username')}"/><s:property value="%{getText('label.colon')}"/></td>
            <td width="15%"><input name="username" type="text" id="username"  class="MyInput"></td>
            <td width="15%" align="right">&nbsp;</td>
            <td width="15%" align="right">&nbsp;</td>
            <td width="15%" align="right"><tm:button site="1"/></td>
          </tr>
        </table>
                            </fieldset>
                        </td>
                    </tr>
</table>
<br>

<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
  <tr>
    <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
    <td class="orarowhead"><s:property value="%{getText('operInfo.title')}"/></td>
    <td	align="right" width="75%">
       <tm:button site="2"></tm:button>
    </td>
  </tr>
</table>

<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
  <tr bgcolor="#FFFFFF"> 
  	<td>
                <table width="100%" cellSpacing="0" cellPadding="0">
                    <tr>
    <td width="6%"> 
      <div align="center"> 
        <input type="checkbox" name="all"  id=chkAll   value="all"  onClick="SelAll(this)">
      </div>
    </td>
    <td width="11%"> 
      <div align="left"><label for=chkAll><s:property value="%{getText('operInfo.checkall')}"/></label></div>
    </td>
    <td width="83%" align="right">
	<div id="pagetag"><tm:pagetag pageName="currPage" formName="sysUserForm" /></div>
	</td></tr></table>
		</td>
	</tr>
</table>

  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
    <tr> 
      <td nowrap width="4%" class="oracolumncenterheader"><s:property value="%{getText('operInfo.checkall')}"/></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('operInfoform.userIdentifier')}"/></div></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('operInfoform.username')}"/></div></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('queryCondition.org')}"/></div></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('operInfoform.createdate')}"/></div></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('operInfoform.invaliddate')}"/></div></td>
      <td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:property value="%{getText('operInfoform.flag')}"/></div></td>
      <td nowrap width="14%" class="oracolumncenterheader"><s:property value="%{getText('operInfo.userGrp')}"/></td>
    </tr>
    <%
    	List groupNameList=(ArrayList)request.getAttribute("groupNameList");
    	int i=0 ;
     %>
     
    <tbody name="formlist" id="formlist">   
    <s:iterator value="userList" id="user">      
    <tr id='tr<s:property value="userid"/>' class="trClass0" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
    <td nowrap align="center"><input type="checkbox" name="id" value='<s:property value="userid"/>,<s:property value="orgLevel"/>'/></td>
    <td nowrap width="12%">
	    <div align="center" id="userid"">
	    	<a href='javascript:showSysUserInfo('<s:property value="userid"/>')'> 
	    	<s:property value="userid"/>  
	      </a>
	    </div>
    </td>
      
    <td nowrap width="12%">
    <div align="center" id="username">
    	<a href='javascript:showSysUserInfo("<s:property value="userid"/>")'> 
    		<s:property value="username"/>
    	</a>
    	</div>
    </td>
    <td nowrap width="12%">
    	<div align="center"></div>
	</td>

    <td nowrap width="12%"><div align="center"><s:date name="createdate"  format="yyyy-mm-dd"/></div></td>
     <td nowrap width="12%"><div align="center"><s:date name="invaliddate"  format="yyyy-mm-dd"/></div></td> 
     <td nowrap width="10%">
	     <div align="center">     
	     <s:if test='isvalid=="Y"'>
	           <img src="../../images/share/button_ok.gif">
	        </s:if>     
	        <s:else>
	           <img src="../../images/share/mood9.gif">
	        </s:else>  
	     </div>
     </td>
     <td nowrap width="14%" align="center"><%=(String)groupNameList.get(i)%><%i++ ;%></td>
  
    </tr>

 </s:iterator> 
    </tbody>
  </table>
 </form>
 <div id="resText">adfasd</div>
 
</body>
</html>