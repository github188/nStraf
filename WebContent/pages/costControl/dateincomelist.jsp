<%@ page contentType="text/html; charset=UTF-8"%>
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
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@page import="cn.grgbanking.feeltm.domain.*"%>
<html>
<head>
<title>date income manage</title>
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

function query(){
	 var pageNum = document.getElementById("pageNum").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
	var queryPrj = document.getElementById("queryPrj").value;
	var pageNum = document.getElementById("pageNum").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/costControl/dateincome!list.action?from=refresh&pageNum="+pageNum;
	actionUrl += "&queryPrj="+queryPrj;
	/* alert(actionUrl); */
	actionUrl=encodeURI(actionUrl);
	var method="setHTML";
	<%int j = 0;//记录的索引
			int k = 1;%>
		sendAjaxRequest(actionUrl,method,pageNum,true);
} 


function setHTML(entry,entryInfo)
{
	var html = "";
    html+=' <tr  class="trClass<%=j % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
	html += '<td><input type="checkbox" name="chkList" value="'+entryInfo['dateIncomeId']+'"/></td>';
	html += '<td align="center" >' + entryInfo['prjGroup'] + '</td>';
	html += '<td align="center" >' + entryInfo['startTime'].substring(0, 10)
			+ '</td>';
	html += '<td align="center" >' + entryInfo['endTime'].substring(0, 10) + '</td>';
	html += '<td align="center" >' + entryInfo['dateIncome'] + '</td>';
	html += '<td align="center" >' + entryInfo['entryPeople'] + '</td>';
	html += '<td align="center" >' + entryInfo['entryTime'].substring(0, 10) + '</td>';
	html += '</tr>';
		<%j++;%>
//每调用一次该方法，索引值加1
	return html;
}


//添加
function add() {
	var resultvalue = OpenModal(
			'/pages/costControl/dateincome!add.action',
			'840,800,tmlInfo.addTmlTitle,tmlInfo');
	
	query();
}
/* /*
 * 修改数据
 */
function modify() {
	var aa = document.getElementsByName("chkList");
	var itemId;
	var j=0;
	for ( var i = 0; i < aa.length; i++) {
		if (aa[i].checked) {
			itemId = aa[i].value;
			j++;
		}
		
	}
	if (j==0){
	 	alert('请选择记录')
	}else if (j>1){
	 	alert('一次只能处理一条记录')
	}else{
		var resultvalue = OpenModal(
				'/pages/costControl/dateincome!update.action?ids='+ itemId,
				'840,800,tmlInfo.addTmlTitle,tmlInfo');
		query();
	}
	
} 
/*
 * 获取被选中的复选框的ids
 */
function GetSelIds() {
	var idList = "";
	var em= document.getElementsByName("chkList");
	for ( var i = 0; i < em.length; i++) {
		if (em[i].type == "checkbox") {
			if (em[i].checked) {
				idList += "," + em[i].value;
			}
		}
	}
	if (idList == "")
		return "";
	return idList.substring(1);
}

function del(){
	var idList = GetSelIds();
	if (idList == "") {
		alert('<s:text name="errorMsg.notInputDelete" />');
		return false;
	}
	if (confirm('<s:text name="确认删除该记录吗？" />')) {
		var strUrl = "/pages/costControl/dateincome!del.action?ids="+idList;
		OpenModal(strUrl, "600,380,operInfo.delete,um")
		query();
	}
	
	
}


</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="dateincomeInfoForm" namespace="/pages/costControl" action="dateincome!del.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<%-- <input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>"> --%>
		
		<table width="100%"   cellSpacing="0" cellPadding="0"> 
		<tr>
	 		<td width="15%" style="text-align: right;">
			查询项目名称:
			</td> 
			<td width="30%">
			<select name="prjGroup"	id="queryPrj"  style="width: 90%">
					<option  selected="selected" value=''>请选择项目</option>
							<s:iterator value="#request.pro" id="ele">
									<option value="<s:property value='name'/>"><s:property value='name'/></option>
							</s:iterator>
					</select>
			</td> 
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
				<td nowrap width="4%" class="oracolumncenterheader"><div align="center">选择 </div></td>
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">项目组</div></td>
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">起始日期</div></td>
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">结束日期</div></td>
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">人日收入</div></td>
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">录入人</div></td>		
				<td nowrap width="16%" class="oracolumncenterheader"><div align="center">录入时间</div></td>		
			</tr>
			<tbody name="formlist" id="formlist">
				<%
					int i = 1;
						int index = 0;
				%>
				<s:iterator value="#request.dateincomelist" id="dateincomeInfo" status="status">
					<tr  id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						<td nowrap align="center"><input type="checkbox"
							name="chkList" value="<s:property value='dateIncomeId'/>" />
						 <input type="hidden" id="dateIncomeId" name="dateincomeManage.dateIncomeId"  value="<s:property value='dateIncomeId'/>" >
							
						</td>
						<td nowrap style="text-align: center;"><s:property value="prjGroup"/></td>
						<td nowrap style="text-align: center;"><s:date name="startTime" format="yyyy-MM-dd" /></td>
						<td nowrap style="text-align: center;"><s:date name="endTime" format="yyyy-MM-dd" /></td>
						<td nowrap style="text-align: center;"><s:property value="dateIncome"/></td>
						<td nowrap style="text-align: center;"><s:property value="entryPeople"/></td>
						<td nowrap style="text-align: center;"><s:date name="entryTime" format="yyyy-MM-dd" /></td>
					</tr>
					<%
						index++;
					%>
				</s:iterator>
				
			</tbody>
		</table>
		<table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1"
					cellspacing="1" bgcolor="#FFFFFF">
					<tr bgcolor="#FFFFFF">
						<td>
							<table width="100%" cellSpacing="0" cellPadding="0">
								<tr>
									<td width="6%"></td>
									<td width="11%"></td>
									<td width="83%" align="right">
										<div id="pagetag">
											<tm:pagetag pageName="currPage"
												formName="dateincomeInfoForm" />
										</div></td>
								</tr>
							</table>
							</td>
					</tr>
				</table>
	</s:form>
	
	
	
</body>
</html>

