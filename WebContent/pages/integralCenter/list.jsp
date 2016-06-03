<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title></title>

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<!-- 行内文字过长自动省略显示 -->
<style type="text/css">
	#downloadList{table-layout:fixed;}
	#downloadList td {
		/* word-wrap: break-word; 
		word-break: break-all; */
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
</style>

<script type="text/javascript">

function query(){
	var createDate = document.getElementById("createDate").value;
	var pageNum = document.getElementById("pageNum").value;
	//var planFinishDate=document.getElementById("planFinishDate").value;
	var endDate=document.getElementById("endDate").value;
	//var pn=document.getElementById("pn").value;
	var showdiv = document.getElementById("showdiv");
	var actionUrl = "<%=request.getContextPath()%>/pages/integralCenter/integralCenter!detailList.action?from=refresh&pageNum="+pageNum;
    actionUrl += "&createDate="+createDate;
	// actionUrl += "&planFinishDate="+planFinishDate;
	 //actionUrl += "&pn="+pn;
	  actionUrl += "&endDate="+endDate;
	actionUrl=encodeURI(actionUrl);
	var method="setHTML";
	i=0;
	sendAjaxRequest(actionUrl,method,pageNum,true);
	
}
var i =0; 
function setHTML(entry,entryInfo){
	//alert("123");
	var html = '';
	//var str = "javascript:show(\""+entryInfo["id"]+"\")";
	//var status=entryInfo["status"];
	var color='';
	html += '<tr>';
	/*html +='<td align="center">';
		 html +='<div align="center">'+(++i)+'</div></td>'; */
	    html += '<td align="center">'+entryInfo["integral"]+'</td>';
		html += '<td align="center">'+entryInfo["gategory"]+'</td>';
		html +='<td align="center">'+entryInfo["reason"]+'</td>';
	    html +='<td align="left">'+entryInfo["createTime"].substring(0, entryInfo["createTime"].length-2);+'</td>';
		html += '</tr>';
	return html;
}
</script>


</head>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="reportInfoForm" namespace=""
		action="" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							

						</tr>
						<tr>
							<td width="8%" align="center" class="input_label">开始日期:</td>
							<td width="20%" align="left"><input name="createDate" size="20"
								type="text" id="createDate"  class="MyInput" />
							</td>

							<td width="8%" align="center" class="input_label">结束日期:</td>
							<td width="20%" align="left"><input name="endDate" size="20"
								type="text" id="endDate" class="MyInput" />
							<td  colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <tm:button
									site="1"></tm:button></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>


		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="" id=downloadList>
			<tr>
				<!--  <td nowrap class="oracolumncenterheader" width="2%"></td> 
				-->
				<!-- <td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">编号</div></td> -->
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">获得积分</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">类别</div></td>
				<td nowrap class="oracolumncenterheader" width="33%" style="word-wrap: break-word; word-break: break-all;"><div
						align="center">事件</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div
						align="center">时间</div></td>
				
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="detailList" id="integralInfo"  status="row">
					
					<!--  <td nowrap align="center"><input type="checkbox"
						name="chkList" value="<s:property value='#integralInfo.id'/>" /></td> -->
					<%-- <td align="center">
						<div align="center">
							<s:property  value="#row.index+1"/>
						</div>
					</td> --%>
					<td align="center"><s:property value="integral" /></td>
					<td align="center"><s:property value="gategory" /></td>
					<td align="center"><s:property value="reason" /></td>
					<td align="left"><s:date name="createTime" format="yyyy-MM-dd HH:mm:ss" /> <%-- <s:property value="createTime" /> --%></td>
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
							<!--<td width="6%"> 
				<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
			</td>
			<td width="11%">
				<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
				</td>-->
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="reportInfoForm" />
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

