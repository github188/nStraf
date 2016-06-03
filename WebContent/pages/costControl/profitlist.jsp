<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/redirect.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<html>
<head>
<title>DayLog query</title>
<style type="text/css">
  .check_area{
     font-size:12px;
     margin-left:5%;
  }
  .select_text{
    margin-left:8px;
    margin-top:5px;
  }
  .td_date{
    margin-left:5px;
  }
</style>
</head>
<script type="text/javascript"> 
$(function(){
	$("#queryStartTime,#queryEndTime").focus(function(){  
		WdatePicker({ skin: 'twoer', dateFmt: 'yyyy-MM',isShowClear:false,readOnly:true });
	 }); 
});

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var queryStartTime=  document.getElementById("queryStartTime").value;
	var queryEndTime=  document.getElementById("queryEndTime").value;
	var queryDept=document.getElementById("queryDept").value; 
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/profit!list.action?from=refresh&pageNum="+pageNum;
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
	sendAjaxRequest(actionUrl,method,pageNum,true);
}

function toExport(){ 
	var queryStartTime= document.getElementById("queryStartTime").value;
	var queryEndTime=  document.getElementById("queryEndTime").value;
	var queryDept=document.getElementById("queryDept").value; 
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/profit!exportData.action?1=1";
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
	if(entryInfo['profit']<0){
	    html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) style="color:red;" >';
	}else{
		html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) style="color:black;">';
	}
    html+= '<td align="center" style="font-size:15px;padding:3px;" title=' + entryInfo['projectName'] + '>' +entryInfo['projectName']+'</td>' ; 
	html+= '<td align="center" style="font-size:15px;padding:3px;" title=' + entryInfo['month'] + '>' +entryInfo['month']+'</td>' ; 
	html+= '<td align="center" style="font-size:15px;padding:3px;" title=' + entryInfo['income'] + '>' +entryInfo['income']+'</td>' ; 
	html+= '<td align="center" style="font-size:15px;padding:3px;" title=' + entryInfo['cost'] + '>' +entryInfo['cost']+'</td>' ; 
	html+= '<td align="center" style="font-size:15px;padding:3px;" title=' + entryInfo['profit'] + '>' +entryInfo['profit']+'</td>' ; 
	html+= '</tr>' ;
	<% k++;%>
	k++;
	return html;
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="dayInfoForm" namespace="/pages/aa" action="aa!aa.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		
		<table width="100%"   cellSpacing="0" cellPadding="0"> 
		<tr>
	 		<td width="15%" style="text-align: right;">
			查询项目组:
			</td> 
			<td width="30%">
			<input name="queryDept" id="queryDept" type="text"  size="22" />
			</td> 
			<td class="input_label">查询时间：</td>
			<td  nowrap="nowrap" width="15%" align="left"><input name="queryStartTime" id="queryStartTime" type="text"  size="11"  value="<%=(String)request.getAttribute("queryStartTime") %>"/>
			至
			<input name="queryEndTime" id="queryEndTime" type="text"  size="11"  value="<%=(String)request.getAttribute("queryEndTime") %>"/></td>
            <td align="right"> 
            	<tm:button site="1"/>
            </td>
		</tr> 
		</table>

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="50%" class="oracolumncenterheader"><div align="center">项目名称 </div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">统计年月</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div align="center">人日收入</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div align="center">人日成本</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div align="center">人日收益</div></td>
			</tr>
			<tbody name="formlist" id="formlist" >
				<s:iterator value="profitlist" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)  <s:if test="profit<0">style="color: red;"</s:if><s:else>style="color: black;"</s:else> >
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)  <s:if test="profit<0">style="color: red;"</s:if><s:else>style="color: black;"</s:else> >
					</s:else>
						<td nowrap style="text-align: center;font-size:15px;padding:3px;"><s:property value="projectName"/></td>
						<td nowrap style="text-align: center;font-size:15px;padding:3px;"><s:property value="month"/></td>
						<td nowrap style="text-align: center;font-size:15px;padding:3px;"><s:property value="income"/></td>
						<td nowrap style="text-align: center;font-size:15px;padding:3px;"><s:property value="cost"/></td>
						<td nowrap style="text-align: center;font-size:15px;padding:3px;"><s:property value="profit"/></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage"
										formName="certificationInfoForm" />
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</s:form>
</body>
</html>

