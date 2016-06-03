<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="/inc/pagination.inc"%>
<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/customTableSort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript"> 
function query(){
	var queryStartTime=  document.getElementById("queryStartTime").value;
	var queryEndTime=  document.getElementById("queryEndTime").value;
	var queryDept=document.getElementById("queryDept").value; 
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/general!list.action?from=refresh";
	if(queryStartTime!=''){
		actionUrl+="&queryStartTime="+queryStartTime;
	}
	if(queryEndTime!=''){
		actionUrl+="&queryEndTime="+queryEndTime;
	}
	if(queryDept!=''){
		actionUrl+="&queryDept="+queryDept;
	}
	
	actionUrl=encodeURI(actionUrl);
	var method="setHTML";
	<%int k = 0 ; %>
	sendAjaxRequest(actionUrl,method,1,true);
}

function toExport(){ 
	var queryStartTime= document.getElementById("queryStartTime").value;
	var queryEndTime=  document.getElementById("queryEndTime").value;
	var queryDept=document.getElementById("queryDept").value; 
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/general!exportData.action?1=1";
	if(queryStartTime!=''){
		actionUrl+="&queryStartTime="+queryStartTime;
	}
	if(queryEndTime!=''){
		actionUrl+="&queryEndTime="+queryEndTime;
	}
	if(queryDept!=''){
		actionUrl+="&queryDept="+queryDept;
	}
	
	//actionUrl = encodeURI(actionUrl,"UTF-8");
	window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
	window.close();
}

//构建显示dataList的HTML
var k=0;
function setHTML(entry,entryInfo)
{	
	var html = "";
	    html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
 		html+= '<td align="center" title=' + entryInfo['deptName'] + '>' +entryInfo['deptName']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['deptMembersNo'] + '>' +entryInfo['deptMembersNo']+'人</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['deptMembersNoStatistic'] + '>' +entryInfo['deptMembersNoStatistic']+'人</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['queryStartTime'] + '>' +entryInfo['queryStartTime']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['queryEndTime'] + '>' +entryInfo['queryEndTime']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['projectManagerConfirm'] + '>' +entryInfo['projectManagerConfirm']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['deptManagerConfirm'] + '>' +entryInfo['deptManagerConfirm']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['notConfirm'] + '>' +entryInfo['notConfirm']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['notRegist'] + '>' +entryInfo['notRegist']+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['notRegist'] + '>' +showNumers(entryInfo['traineeCost'])+'</td>' ; 
 		html+= '<td align="center" title=' + entryInfo['notProjectPercent'] + '>' +entryInfo['notProjectPercent']+'</td>' ; 
		html+= '</tr>' ;
 		<% k++;%>
 		k++;
	return html;
}

function showNumers(num){
	if(num>0){
		return num;
	}else{
		return '-'
	}
}

</script>
</head>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="form" action="costControl!cost.action"  namespace="/pages/costControl" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<input type="hidden" name="pageSize" id="pageSize" value="20" />
  
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%"   cellSpacing="0" cellPadding="0"> 
			<tr>
		 		<td width="15%" style="text-align: right;">
				查询部门:
				</td> 
				<td width="30%">
				<input name="queryDept" id="queryDept" type="text"  size="22" />
				</td> 
				<td class="input_label">查询时间：</td>
				<td  nowrap="nowrap" width="15%" align="left"><input name="queryStartTime" id="queryStartTime" type="text"  size="11"  class="MyInput" value="<%=(String)request.getAttribute("queryStartTime") %>"/>
				至
				<input name="queryEndTime" id="queryEndTime" type="text"  size="11" class="MyInput" value="<%=(String)request.getAttribute("queryEndTime") %>"/></td>
	            <td align="right"> 
	            	<tm:button site="1"/>
	            </td>
			</tr> 
		</table>
		
		<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			<tr>
				 <td width="25"  height="23" valign="middle"></td>
				 <td class="orarowhead"><s:text name="operInfo.title" /></td>
				 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr> 
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">部门 </div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">部门人数</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">部门统计人数</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">开始时间</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">结束时间</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">项目经理确认</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">部门经理确认</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">未确认</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">未登记</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">实习生消耗</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">非项目比例(%)</div></td>
			</tr>
			<tbody name="formlist" id="formlist"> 
	  		<s:iterator  value="generallist" id="tranInfo" status="row">
	  		<s:if test="#row.odd == true"> 
	 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:if><s:else>
	 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:else> 
				<td nowrap style="text-align: center;"><s:property value="deptName"/></td>
				<td nowrap style="text-align: center;"><s:property value="deptMembersNo"/>人</td>
				<td nowrap style="text-align: center;"><s:property value="deptMembersNoStatistic"/>人</td>
				<td nowrap style="text-align: center;"><s:property value="queryStartTime"/></td>
				<td nowrap style="text-align: center;"><s:property value="queryEndTime"/></td>
				<td nowrap style="text-align: center;"><s:property value="projectManagerConfirm"/></td>
				<td nowrap style="text-align: center;"><s:property value="deptManagerConfirm"/></td>
				<td nowrap style="text-align: center;"><s:property value="notConfirm"/></td>
				<td nowrap style="text-align: center;"><s:property value="notRegist"/></td>
				<td nowrap style="text-align: center;"><s:if test="traineeCost>0"><s:property value="traineeCost"/></s:if><s:else>-</s:else></td>
				<td nowrap style="text-align: center;"><s:property value="notProjectPercent"/></td>
			</tr>
			</s:iterator>  
	 		</tbody> 
	 	</table>
		
	</s:form>
</body>
</html>