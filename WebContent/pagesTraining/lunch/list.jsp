<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript"> 
//加载时限制下拉框宽度,主要是针对名称过长时出现的问题,设置100%是为了让它适应外部td
$(function(){
	//jqueryui的日期控件 
	$("#_trainStartTime,#_trainEndTime").datepicker({  
        dateFormat:'yy-mm-dd',  //更改时间显示模式  
        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
        changeYear:true        //是否显示年份的下拉菜单，默认为false  
     });  
});

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var trainName =  document.getElementById("_trainName").value;
	var lecturerName =  document.getElementById("_lecturerName").value;
	var trainStatus =  document.getElementById("_trainStatus").value;
	var trainStartTime =  document.getElementById("_trainStartTime").value;
	var trainEndTime =  document.getElementById("_trainEndTime").value;
	
	var actionUrl = "<%=request.getContextPath()%>/pagesTraining/lunch/lunch!list.action?from=refresh&pageNum="+pageNum+"&userTrainRecord.trainName="+trainName+"&userTrainRecord.lecturerName="+lecturerName+"&userTrainRecord.trainStatus="+trainStatus+"&userTrainRecord.trainStartTime="+trainStartTime+"&userTrainRecord.trainEndTime="+ trainEndTime;
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	k=0;
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{	
	var html = "";
    html+= '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
	html+= '<td align="center"><input type="checkbox" name="trainRecordId" value="'+entryInfo['trainRecordId']+'"/></td>';
	html+= '<td align="left" title='+entryInfo['trainName']+'>';
	html+= '<div style="margin:10px;font-size:15px;"><a href="javascript:showInfo('+"'"+entryInfo['trainRecordId']+"'"+')"><font color="#3366FF">'+entryInfo['trainName']+'</font></a></div>';
	html+= '<div style="margin:10px;">';
	html+= '<span><a>报名</a></span>&nbsp;&nbsp;';
	html+= '<span><a>通知</a></span>&nbsp;&nbsp;';
	html+= '<span><a>出勤</a></span>&nbsp;&nbsp;';
	html+= '<span><a>评估</a></span>';
	html+= '</div>';
	html+= '</td>';
	html+= '<td title='+entryInfo["trainTo"]+'>'+entryInfo["trainTo"]+'</div></td>';
	html+= '<td title=' + entryInfo['lecturerName'] + '>' +entryInfo['lecturerName']+'</td>' ; 
	html+= '<td title=' + entryInfo['courseHours'] + '>' +entryInfo['courseHours']+'</td>' ; 
	html+= '<td title=' + entryInfo['trainStartTime'].substring(0,16) + '>' +entryInfo['trainStartTime'].substring(0,16)+'</td>' ; 
	html+= '<td title=' + entryInfo['trainBudget'] + '>' +entryInfo['trainBudget']+'</td>' ; 
	html+= '<td>';
	if(entryInfo['trainStatus']=='0'){
		html+= '未开始';	
	}else if(entryInfo['trainStatus']=='1'){
	    html+= '进行中';    
	}else if(entryInfo['trainStatus']=='2'){
	    html+= '已结束';    
	}
	html+= '</td>';
	html+= '</tr>' ;
	<%k++;%>
	return html;
}

//添加
function add(){
	 var strUrl="/pages/staff/staffInfo!addpage.action";
	 var features="1000,500,staff.list.add,staffManager";
	 var resultvalue =OpenModal(strUrl,features);	
	 refreshList();
}

//获取所选复选框
function  GetSelIds()
{
    var idList="";
	//var  em= document.all.tags("input");
	var  em= document.getElementsByName("userid");
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
      var  em= document.getElementsByName("userid");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked;
		   }
	   }
}

//删除
function del()
{	
   var idList=GetSelIds();  
   if(idList=="")
   {   
	   alert("请选择要删除的员工");
	   return false;
   }
 	if (confirm('确定要删除所选用户吗？'))
    {
	   // window.open(strUrl);
	 var strUrl="/pages/staff/staffInfo!delete.action?userids="+idList;
       var returnValue=OpenModal(strUrl,"600,380,staff.list.delete,staffManager");
   
       refreshList();
    }
}


//修改
function modify(){
	var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择需要修改的用户！');
		 return false;
	 }
	 if(ids.length>1){
		 alert('一次只能修改一个用户的信息');
		 return false;
	 }
	 var strUrl="/pages/staff/staffInfo!modifyPage.action?userid="+idList;
		var features="1000,500,staff.list.modify,staffManager";
		var resultvalue = OpenModal(strUrl,features);

		refreshList();

}

	
//点击超链接查看详情
function showInfo(userid){
	var strUrl="/pages/staff/staffInfo!view.action?userid="+userid;
	var features="600,500,staff.list.detail,staffManager";
	var resultvalue = OpenModal(strUrl,features);
}


</script>
</head>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="listForm" action="staffInfo!list.action" namespace="/pages/staff" method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="20" />

		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0" style="margin-top:10px;">
			<tr>
				<td align="right" width="8%">培训名称:</td>
				<td align="left" width="20%">
					<input id="_trainName" name="trainName" style="width:100%"/>
				</td>
				<td align="right" width="8%" nowrap="nowrap">培训讲师:</td>
				<td align="left" width="12%" nowrap="nowrap">
					<input id="_lecturerName" name="userTrainRecord.lecturerName" style="width:100%"/>
				</td>
				<td align="right" width="8%" nowrap="nowrap">培训状态:</td>
				<td align="left" width="10%" nowrap="nowrap">
					<s:select list="#{0:'未开始',1:'进行中',2:'已结束' }" id="_trainStatus" name="userTrainRecord.trainStatus" headerKey="" headerValue="全部"></s:select>
				</td>
				<td align="right" nowrap="nowrap">培训时间:</td>
				<td align="left" width="27%" nowrap="nowrap">
					<input id="_trainStartTime" name="userTrainRecord.trainStartTime" readonly="true" type="text"  size="15" value=''>
					至<input id="_trainEndTime" name="userTrainRecord.trainEndTime" readonly="true" type="text"  size="15" value=''>
				</td>
				<td width="7%" nowrap="nowrap">&nbsp;&nbsp;<tm:button site="1" /></td>
			</tr>
		</table>

		<br>

		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" vAlign="middle"><td class="orarowhead"><s:text name="operInfo.title"/></td>
			    <td	align="right" width="75%">
			       <tm:button site="2"></tm:button>
			    </td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
			<tr>
				<td width="3%" class="oracolumncenterheader" style="CURSOR: hand">
					<div align="center">选择</div>
				</td>
				<td width="32%" class="oracolumncenterheader">
					<div align="center">培训名称</div>
				</td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">培训对象</div>
				</td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">培训讲师</div>
				</td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">培训课时</div>
				</td>
				<td width="15%" class="oracolumncenterheader">
					<div align="center">培训时间</div>
				</td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">培训预算</div>
				</td>
				<td width="10%" class="oracolumncenterheader">
					<div align="center">培训状态</div>
				</td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="datalist" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else>
					<td align="center"><input type="checkbox" name="id" value='<s:property value="trainRecordId"/>' /></td>
					<td align="left">
						<div style="margin:10px;font-size:15px;"><a href='javascript:showInfo("<s:property value="trainRecordId"/>")'><font color="#3366FF"><s:property value="trainName" /></font></a></div>
						<div style="margin:10px;">
							<span><a>报名</a></span>&nbsp;&nbsp;
							<span><a>通知</a></span>&nbsp;&nbsp;
							<span><a>出勤</a></span>&nbsp;&nbsp;
							<span><a>评估</a></span>
						</div>
					</td>
					<td align="center"><s:property value="trainTo" /></td>
					<td align="center"><s:property value="lecturerName" /></td>
					<td align="center"><s:property value="courseHours" /></td>
					<td align="center"><s:date name="trainStartTime" format="yyyy-MM-dd HH:mm" /></td>
					<td align="center"><s:property value="trainBudget" /></td>
					<td align="center"><s:if test="trainStatus==0">未开始</s:if> <s:if
							test="trainStatus==1">进行中</s:if> <s:if test="trainStatus==2">已结束</s:if>
						<s:else></s:else></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr>
				<td>
					<table width="100%" cellSpacing="0" border="0" cellPadding="0">
						<tr>
							<td width="12%">
								<div align="center">
									<input type="checkbox" name="all" id=chkAll value="all"
										onClick="SelAll(this)">
									<s:text name="operInfo.checkall" />
								</div>

							</td>
							<td width="11%">
								<div align="left">
									<label for="chkAll"></label>
								</div>
							</td>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="listForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>