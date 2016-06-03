<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
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
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">

$(function(){
	shortShow();
});

function shortShow(){
	//当所属组别字段过长时，简化显示
	$("td[name='groupNameShow']").each(function(){
		$(this).html(shortGroupShow($(this).html()));
	});
}


//添加页面
function add(){
	var strUrl="/pages/hols/holsInfo!addPage.action";
	var features="600,300,user.hols.addpage,hols";	
	var resultvalue =OpenModal(strUrl,features);	
	query();
}

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var deptcode =  document.getElementById("queryDept").value;
	var grpcode =  document.getElementById("queryGroup").value;
	var userids =  document.getElementById("queryUserName").value;
	
	var actionUrl = "<%=request.getContextPath()%>/pages/hols/holsInfo!refresh.action?form=refresh&pageNum="+pageNum+"&deptcode="+deptcode+"&grpcode="+grpcode+"&userids="+userids;
	actionUrl = encodeURI(actionUrl,"UTF-8");
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var str = "javascript:show(\""+entryInfo["userid"]+"\")";
	var html = "";
	    html+= '<tr id="'+entryInfo['id']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html+= '<td align="center"><input type="checkbox" name="id" value="'+entryInfo['id']+'"/></td>';
		html+= '<td width="10%">';
   		html+= '<div align="center" id="username">'+entryInfo['userid']+'</div>';
 		html+= '</td>';
		html+= '<td width="10%">';
   		html+= '<div align="center" id="username">'+entryInfo['username']+'</div>';
 		html+= '</td>';
 		html+= '<td width="20%">';
  		html+= '<div align="center">'+entryInfo['deptName']+'</div>';
  		html+= '</td>';
 		html+= '<td width="40%">';
  		html+= '<div align="center">'+entryInfo['groupName']+'</div>';
  		html+= '</td>';
		html+= '<td width="10%">';
 		html+= '<div align="center">'+entryInfo["yearholsTime"]+'</div>';
		html+= '</td>';
		html+= '<td width="10%">';
 		html+= '<div align="center">'+entryInfo["deferredTime"]+'</div>';
		html+= '</td>';
		html+= '<td width="10%">';
 		html+= '<div align="center"><a href='+str+'><font color="#3366FF">详情</font></a></div>';
		html+= '</td>';
			//html += '<td ><a href='+str+'><font color="#3366FF">'+entryInfo["username"] + '</font></a></td>';
		html+= '</tr>' ;
 		<% k++;%>
	return html;
}

//获取所选复选框
function  GetSelIds()
{
    var idList="";
	//var  em= document.all.tags("input");
	var em= document.getElementsByName("id");
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
	    return "";
	 return idList.substring(1);
}

//全选
function  SelAll(chkAll)
{
     //var em=document.all.tags("input");
     var em= document.getElementsByName("id");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked
		   }
	   }
}

function modify(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要修改的假期数据");
	   return false;
  }
  var strUrl="/pages/hols/holsInfo!modifyPage.action?holsids="+idList;
	var features="800,600,operInfo.updateTitle,hols";
	var resultvalue = OpenModal(strUrl,features);
	query();

}

function del(){
	var idList=GetSelIds();
	 if(idList=="")
  {   
	   alert("请选择需要删除的假期数据");
	   return false;
  }
	 var checked = $(":checked").length;
	 var checkbox = $(":checkbox").length-1;
	 
	 var chkAll = document.getElementById("chkAll");
	 
	 if(chkAll.checked){
		 checked = checked - 1;
	 }
	var string = "确定删除这"+checked+"条信息吗?";
	 if(checked==checkbox){
		 string = "确定删除全部信息吗?";
	 }else if(checked==1){
		 string = "确定删除此信息吗?";
	 }
	 if(confirm("删除数据后无法再恢复，"+string)){
	  var strUrl="/pages/hols/holsInfo!delete.action?holsids="+idList;
	  var features="800,600,user.hols.deleteTitle,hols";
	  var resultvalue = OpenModal(strUrl,features);
		query();
	 }
}

function showNotifyInfo(notifyNum){
	var strUrl="/pages/notify/notifyInfo!view.action?notifyNum="+notifyNum;
	var features="800,600,operInfo.viewOperator,um";
	var resultvalue = OpenModal(strUrl,features);
	if(resultvalue!=null)
	{
		query();
	}
}

function resetYearTime(){
	var idList=GetSelIds();
	 if(idList=="")
	 {   
		   alert("请选择需要删除的假期数据");
		   return false;
	 }
	 if(confirm("确定清空年假吗?")){
		 var strUrl="/pages/hols/holsInfo!resetYearTime.action?holsids="+idList;
		  var features="800,600,user.hols.deleteTitle,hols";
		  var resultvalue = OpenModal(strUrl,features);
			query();
	 }
}

function resetFreeTime(){
	var idList=GetSelIds();
	 if(idList=="")
	 {   
		   alert("请选择需要删除的假期数据");
		   return false;
	 }
	 if(confirm("确定清空调休吗?")){
		 var strUrl="/pages/hols/holsInfo!resetFreeTime.action?holsids="+idList;
		  var features="800,600,user.hols.deleteTitle,hols";
		  var resultvalue = OpenModal(strUrl,features);
			query();
	 }
}

function shortGroupShow(groupName){
	var tmp;
	//首先去掉最后的逗号
	if(groupName.charAt(groupName.length-1)==','){
		groupName=groupName.substring(0,groupName.length-1);
	}else{
		tmp=groupName;
		
	}
	//如果还有逗号的话，说明是几个组，这里只展示一个组，其他组在鼠标移上去后显示
	if(groupName.indexOf(',')>0){
		var shortShow=groupName.substring(0,groupName.indexOf(','))+'...';
		shortShow='<a style="font-weight:bold;" title="'+tmp+'">'+shortShow+'</a>';
		return shortShow;
	}else{
		return groupName;
	}
}

//查看加班、请假数据
function show(userid){
	var resultvalue = OpenModal('/pages/overtime/overtimeinfo!overtimeList.action?userid='+userid,'790,520,tmlInfo.addTmlTitle,tmlInfo');
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="userholsForm" action="holsInfo!list.action"  namespace="/pages/hols" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
        	<table width="100%"> 
                    <tr>
                        <td >
							<table width="100%" class="select_area">
							    <tr> 
							           <tm:deptSelect 
								deptId="queryDept" 
								deptName="deptcode"
								groupId="queryGroup"
								groupName="grpcode"
								userId="queryUserName"
								userName="userids" 
								isloadName="false" 
								deptHeadKey="---请选择部门---" 
								deptHeadValue="" 
								userHeadKey="----请选择人员----" 
								userHeadValue=""  
								groupHeadKey="---请选择项目名称---"
								groupHeadValue=""
								labelDept="部门 :" 
								labelGroup="项目名称:" 
								labelUser="姓名 :" 
								deptLabelClass="align:right; width:5%;class:input_label"
								deptClass="align:left;width:12%;" 
								groupLabelClass="align:right; width:5%;class:input_label"
								groupClass="align:left;width:12%;" 
								userLabelClass="align:right; width:5%;class:input_label"
								userClass="align:left;width:15%;" 
								>
							</tm:deptSelect>
							            <td width="15%" align="right"><tm:button site="1"/></td>
							          </tr>
							        </table> 
                        </td>
                    </tr>
			</table>
			
			<br>

			<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			  <tr>
			    <td width="25"  height="23" valign="middle">&nbsp;</td>
			    <td class="orarowhead"><s:text name="operInfo.title"/></td>
			    <td	align="right" width="75%">
			       <tm:button site="2"></tm:button>
			    </td>
			  </tr>
			</table>

			

		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
		    <tr> 
		      <td nowrap width="12%" class="oracolumncenterheader"></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.userid"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.username"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.department"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="lable.projectName" /></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.year"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.deferred"/></div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center">详情</div></td>
		    </tr>
		    <tbody id="formlist">   
			     <s:iterator value="userhols" id="hols" status="s">      
			    <tr id='tr<s:property value="id"/>' class="trClass<s:property value='#s.index%2'/>" oriClass=""> 
			    <td align="center"><input type="checkbox" name="id" value='<s:property value="id"/>'/></td>
			      
			    <td width="10%">
			    <div align="center" id="userid">
			    		<s:property value="userid"/></font>
			    	</div>
			    </td>
			    <td width="10%">
			    <div align="center" id="username">
			    		<s:property value="username"/></font>
			    	</div>
			    </td>
			    <td width="20%">
			    <div align="center" id="deptName">
			    		<s:property value="deptName"/></font>
			    	</div>
			    </td>
			    <td width="40%">
			    <div align="center" id="groupName">
			    		<s:property value="groupName"/></font>
			    	</div>
			    </td>
			    <td width="10%">
			    <div align="center" id="yearholsTime">
			    		<s:property value="yearholsTime"/></font>
			    	</div>
			    </td>
			    <td width="10%">
			    <div align="center" id="deferredTime">
			    		<s:property value="deferredTime"/></font>
			    	</div>
			    </td>
			    <td width="10%">
			    	<div align="center" id="moreinfo">
			    		<a style="color:#3366FF" href="javascript:show('<s:property value="userid"/>')">详情</a>
			    	</div>
			    </td>
			    </tr>
			 </s:iterator> 
		    </tbody>
		  </table>
		  <table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
			  <tr bgcolor="#FFFFFF"> 
			  	<td>
			                <table width="100%" cellSpacing="0" cellPadding="0">
			                    <tr>
			    <td width="6%"> 
			      <div align="center"> 
			        <input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="SelAll(this)">
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
 </s:form>
</body>
</html>