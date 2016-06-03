<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>operation info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>


<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var userid =  document.getElementById("userid").value;
	var username =  document.getElementById("username").value;
	
	
	var actionUrl = "<%=request.getContextPath()%>/pages/um/sysUserInfo!refresh.action?from=refresh&pageNum="+pageNum+"&userid="+userid+"&username="+username;
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
	    html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
		html+= '<td nowrap align="center"><input type="checkbox" name="id" value="'+entryInfo['userid']+','+entryInfo['orgLevel']+'"/></td>';
		html+= '<td nowrap width="12%">';
   		html+= '<div align="center" id="userid">';
    	html+= '<a href="javascript:showSysUserInfo('+"'"+entryInfo['userid']+"'"+')"><font color="#3366FF">'+entryInfo['userid']+'</font></a>';
   		html+= '</div>';
 		html+= '</td>';
 		html+= '<td nowrap width="12%">';
  		html+= '<div align="center" id="username">';
   		html+= '<a href="javascript:showSysUserInfo('+"'"+entryInfo['userid']+"'"+')"><font color="#3366FF">'+entryInfo['username']+'</font></a>';
   		html+= '</div>';
  		html+= '</td>';
		html+= '<td nowrap width="12%">';
 		html+= '<div align="center">'+entryInfo["orgid"]+'</div>';
		html+= '</td>';
  		html+= '<td nowrap width="12%"><div align="center">'+entryInfo['createdate']+'</div></td>';
 		html+= '<td nowrap width="12%"><div align="center">'+entryInfo['invaliddate'].substring(0,10)+'</div></td> ';
 		html+= '<td nowrap width="10%">';
		html+= '<div align="center">';
		
		if(entryInfo['isvalid']=="Y")   
   			html+= '<img src="../../images/share/button_ok.gif">';	
   		else
   		    html+= '<img src="../../images/share/mood9.gif">';    
   		     	        
  		html+= '</div>';
  		html+= '</td>';
 		html+= '<td nowrap width="14%" align="center">'+entryInfo['groupName']+'</td>' ; 
		html+= '</tr>' ;
 		<% k++;%>
	return html;
}



function add(){
	 var strUrl="/pages/um/sysUserInfo!addpage.action";
	 var features="750,650,roleInfo.addOperTitle,um";
	  var resultvalue =OpenModal(strUrl,features);	
		/* query(); */
	  refreshList();
   
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
     var  em= document.getElementsByTagName("input");
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
 	if (confirm('确定要删除所选用户吗？'))
    {
	 // var strUrl="<%=request.getContextPath()%>/pages/um/sysUserInfo!delete.action?userids="+idList;
	   // window.open(strUrl);
	 var strUrl="/pages/um/sysUserInfo!delete.action?userids="+idList;
       var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um");
   
		/* query(); */
       refreshList();
    }
}

function refreshUsrGrp(){

     var aa=document.getElementsByName("id");
     var userid ;
     var level ;
     var j=0;
     var param;
     
	 for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
   			param = aa[i].value;
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
 		//不知道为什么不能传两个参数，只能用下面那种方法将两个参数合并
 	   //var strUrl="/pages/um/sysUserGroup!findSysUserGroupList?userid="+userid+"&level="+level;
		var strUrl="/pages/um/sysUserGroup!findSysUserGroupList.action?param="+param;	
		var features="600,450,grpInfo.updateTitle,um";
		var resultvalue = OpenModal(strUrl,features);
		/* query(); */
		refreshList();
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
 		alert('请选择需要修改的用户！')
 	else if (j>1)
 		alert('一次只能修改一个用户的信息')
	else{
		var strUrl="/pages/um/sysUserInfo!modify.action?userid="+userid;
		var features="600,500,operInfo.updateTitle,um";
		var resultvalue = OpenModal(strUrl,features);
	  if(resultvalue!=null)
    {
		/* query(); */
		  refreshList();
    }
	}
}
//show sysUserInfo 
function showSysUserInfo(userid){
		var strUrl="/pages/um/sysUserInfo!view.action?userid="+userid+"&menuid=<%=request.getParameter("menuid")%>";
		var features="600,500,operInfo.viewOperator,um";
		var resultvalue = OpenModal(strUrl,features);
	  if(resultvalue!=null)
    {
		/* query(); */
		  refreshList();
    }
}


function resetPassWd(){
	  var aa=document.getElementsByName("id");
   var userid;
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value.split(",")[0];
			j=j+1;
		}
   }
	if (j==0)
 		alert('请选择一个需要重置密码的用户')
 	else if (j>1)
 		alert('一次只能重置一个用户的密码')
 	else if (confirm("确定要重置该用户的密码吗？")){ 		
		var strUrl="/pages/um/sysUserInfo!resetPW.action?userid="+userid;
		var features="500,350,pwdsetting.title,um";
		var resultvalue = OpenModal(strUrl,features);
		/* query(); */
		refreshList();
	}
}
function resetLock()
{
    var aa=document.getElementsByName("id");
   var userid;
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value.split(",")[0];
			j=j+1;
		}
   }
	if (j==0)
 		alert('请选择一个需要解锁的用户！');
 	else if (j>1)
 		alert('一次只能为一个用户解锁！');
 	else if (confirm("确定要为该用户解锁吗?")){ 		
		var strUrl="/pages/um/sysUserInfo!resetUser.action?userid="+userid;
		var features="500,350,um.unlock,um";
		var resultvalue = OpenModal(strUrl,features);
		/* query(); */
		refreshList();
	}
}


</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="sysUserForm" action="sysUserInfo!list.action"  namespace="/pages/um" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	
  
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
        	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
                    <tr>
                        <td>
                            <fieldset  width="100%">
                                <legend><s:text name="queryCondition.query"/></legend>                  
							        <table width="100%">
							          <tr> 
							            <td width="15%" align="right"> <s:text name="operInfoform.userIdentifier"/><s:text name="label.colon"/></td>
							            <td width="15%"><input name="userid" type="text" id="userid"  class="MyInput"> </td>
							            <td width="15%" align="right"> <s:text name="operInfoform.username"/><s:text name="label.colon"/></td>
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

			<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			  <tr>
			    <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
			    <td class="orarowhead"><s:text name="operInfo.title"/></td>
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
			      <div align="left"><label for="chkAll"><s:text name="operInfo.checkall"/></label></div>
			    </td>
			    <td width="83%" align="right">
				<div id="pagetag"><tm:pagetag pageName="currPage" formName="sysUserForm" /></div>
				</td></tr></table>
					</td>
				</tr>
		  </table>

		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
		    <tr> 
		      <td nowrap width="4%" class="oracolumncenterheader"><s:text name="operInfo.checkall"/></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="operInfoform.userIdentifier"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="operInfoform.username"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="queryCondition.org"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="operInfoform.createdate"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="operInfoform.invaliddate"/></div></td>
		      <td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="operInfoform.flag"/></div></td>
		      <td nowrap width="14%" class="oracolumncenterheader"><s:text name="operInfo.userGrp"/></td>
		    </tr>
		    <%
		    	List groupNameList=(ArrayList)request.getAttribute("groupNameList");
		    	int i=0 ;
		    	int index = 0;
		     %>
		     
		    <tbody name="formlist" id="formlist">   
			    <s:iterator value="userList" id="user">      
			    <tr id='tr<s:property value="userid"/>' class="trClass<%=index%2 %>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
			    <td nowrap align="center"><input type="checkbox" name="id" value='<s:property value="userid"/>,<s:property value="orgLevel"/>'/></td>
			    <td nowrap width="12%">
				    <div align="center">
				    	<a href='javascript:showSysUserInfo("<s:property value="userid"/>")'><font color="#3366FF">
				    	<s:property value="userid"/></font>
				      </a>
				    </div>
			    </td>
			      
			    <td nowrap width="12%">
			    <div align="center" id="username">
			    	<a href='javascript:showSysUserInfo("<s:property value="userid"/>")'> <font color="#3366FF">
			    		<s:property value="username"/></font>
			    	</a>
			    	</div>
			    </td>
			    <td nowrap width="12%">
			    	<div align="center">
			    		<s:property value="orgid" /> 
			    	</div>
				</td>
			
			    <td nowrap width="12%"><div align="center"><s:date name="createdate"  format="yyyy-mm-dd"/></div></td>
			     <td nowrap width="12%"><div align="center"><s:date name="invaliddate"  format="yyyy-MM-dd"/></div></td> 
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
			    <%index++; %>
			 </s:iterator> 
		    </tbody>
		  </table>
 </s:form>
</body>
</html>