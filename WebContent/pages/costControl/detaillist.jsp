<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
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
function query()
{
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var queryStartTime=  document.getElementById("queryStartTime").value;
	var queryEndTime=  document.getElementById("queryEndTime").value;
	var queryDept=document.getElementById("queryDept").value; 
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/detail!list.action?from=refresh&pageNum="+pageNum;
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
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/detail!exportData.action?1=1";
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

//导出项目人日明细
function exportProjectPersonDay(){
	var strUrl="/pages/costControl/detail!exportProjectPersonDay.action";
	var features="400,300,transmgr.traninfo,watch";
	OpenModal(strUrl,features);
}

//构建显示dataList的HTML
var k=0;
function setHTML(entry,entryInfo)
{	
	var html = "";
    html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
    html+= '<td align="center" title=' + entryInfo['deptName'] + '>' +entryInfo['deptName']+'</td>' ; 
    html+= '<td align="center" title=' + entryInfo['userName']+'('+entryInfo['userId']+')' + '>' +entryInfo['userName']+'('+entryInfo['userId']+')'+'</td>' ; 
	html+= '<td align="center" title=' + entryInfo['queryStartDate'] + '>' +entryInfo['queryStartDate']+'</td>' ; 
	html+= '<td align="center" title=' + entryInfo['queryEndDate'] + '>' +entryInfo['queryEndDate']+'</td>' ; 
	html+= '<td align="center" title=' + doubleFormatShow(entryInfo['projectManagerConfirm']) + '>' +doubleFormatShow(entryInfo['projectManagerConfirm'])+'</td>' ; 
	html+= '<td align="center" title=' + doubleFormatShow(entryInfo['deptManagerConfrim']) + '>' +doubleFormatShow(entryInfo['deptManagerConfrim'])+'</td>' ; 
	html+= '<td align="center" title=' + doubleFormatShow(entryInfo['notConfirm']) + '>' +doubleFormatShow(entryInfo['notConfirm'])+'</td>' ; 
	html+= '<td align="center" title=' + doubleFormatShow(entryInfo['notRegist']) + '>' +doubleFormatShow(entryInfo['notRegist'])+'</td>' ; 
	html+= '<td align="center" title=' + doubleFormatShow(entryInfo['notRegist']) + '>' +showNumers(doubleFormatShow(entryInfo['traineeCost']))+'</td>' ; 
	html+= '<td align="center" title=' + entryInfo['notProjectPercent'] + '>' +doubleFormatShow(entryInfo['notProjectPercent'])+'</td>' ; 
	html+= '</tr>' ;
	<% k++;%>
	k++;
	return html;
}

function doubleFormatShow(s){
	if(s.indexOf('.')>0 && s.length>=(s.indexOf('.')+3)){
		s=s.substring(0,s.indexOf('.')+3);
	}
	return s;
}

function showNumers(num){
	if(num>0){
		return num;
	}else{
		return '-'
	}
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
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">所属部门 </div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">姓名</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">开始时间</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">结束时间</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目经理确认</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">部门经理确认</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">未确认</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">未登记</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">实习生消耗</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">非项目比例(%)</div></td>
			</tr>
			<tbody name="formlist" id="formlist">
				<s:iterator value="detaillist" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else>
						<td nowrap style="text-align: center;"><s:property value="deptName"/></td>
						<td nowrap style="text-align: center;"><s:property value="userName"/>(<s:property value="userId"/>)</td>
						<td nowrap style="text-align: center;"><s:property value="queryStartDate"/></td>
						<td nowrap style="text-align: center;"><s:property value="queryEndDate"/></td>
						<td nowrap style="text-align: center;"><s:property value="%{formatDoubleShow(projectManagerConfirm+'')}"/></td>
						<td nowrap style="text-align: center;"><s:property value="%{formatDoubleShow(deptManagerConfrim+'')}"/></td>
						<td nowrap style="text-align: center;"><s:property value="%{formatDoubleShow(notConfirm+'')}"/></td>
						<td nowrap style="text-align: center;"><s:property value="%{formatDoubleShow(notRegist+'')}"/></td>
						<td nowrap style="text-align: center;"><s:if test="%{formatDoubleShow(traineeCost+'')}>0"><s:property value="%{formatDoubleShow(traineeCost+'')}"/></s:if><s:else>-</s:else></td>
						<td nowrap style="text-align: center;"><s:property value="notProjectPercent"/></td>
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

