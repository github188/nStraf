<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>prjContract info</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
 var num=1;
	function add(){
		var resultvalue = OpenModal('/pages/prjContract/prjContractInfo!add.action','900,600,prjContract.title_add,tmlInfo');

		refreshList();

	}
	function payment(){
		var aa=document.getElementsByName("chkList");
		var itemId=GetSelIds();
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
		}  
		if (j==0)
		 	alert('<s:text name="operator.update" />')
		 else if (j>1)
		 	alert('<s:text name="operator.updateone" />')
		else{
		  	var strUrl="/pages/prjContract/prjContractInfo!payment.action?prjContractId="+itemId;
		  	var features="900,600,prjContract.title_edit,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
	}

	function query() {
		
		
		var prjName = document.getElementById("prjName").value;
		var prjManager = document.getElementById("prjManager").value;
		var status = document.getElementById("status").value;
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum==1)
			num=1;
		var actionUrl = "<%=request.getContextPath()%>/pages/prjContract/prjContractInfo!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&prjContract.prjName="+prjName;
		actionUrl += "&prjContract.prjManager="+prjManager;
		actionUrl += "&prjContract.status="+status;
		actionUrl += "&prjContract.startDate="+startDate;
		actionUrl += "&prjContract.endDate="+endDate;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0;%>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["Id"] + '" />';
		html += '</td>';
		/* html += '<td align="center">'+entryInfo["prjName"] + '</td>';
		html += '<td align="center">' +entryInfo["prjManager"] + '</td>'; */
		html += '<td align="center">';
		html += '<div align="center" id="prjContractId">';
		html += '<a href="javascript:showPrjContractInfo(' + "'"
				+ entryInfo['Id'] + "'" + ')"><font color="#3366FF">'
				+ entryInfo['prjName'] + '</font></a>';
		html += '</div>';
		html += '</td>';
		html += '<td align="center">';
		html += '<div align="center" id="prjContractId">';
		html += '<a href="javascript:showPrjContractInfo(' + "'"
				+ entryInfo['Id'] + "'" + ')"><font color="#3366FF">'
				+ entryInfo['prjManager'] + '</font></a>';
		html += '</div>';
		html += '</td>';
		html += '<td align="center">' + entryInfo["client"] + '</td>';
		html += '<td align="center">' + entryInfo["signDate"].substring(0, 10)
		+ '</td>';
		html += '<td align="center">' + entryInfo["startDate"].substring(0, 10)
				+ '</td>';
		html += '<td align="center">' + entryInfo["endDate"].substring(0, 10)
				+ '</td>';
		html += '<td align="center">'
				+ entryInfo["finishDate"].substring(0, 10) + '</td>';
		html += '<td align="center">' + entryInfo["total"] + '</td>';
		html += '<td align="center">' + entryInfo["payment"] + '</td>';
		html += '<td align="center">' + entryInfo["status"] + '</td>';
		/* html += '<td align="center">' +entryInfo["risk"] + '</td>'; */
		html += '<td align="center">' + entryInfo["fare"] + '</td>';
		html += '<td align="center">' + entryInfo["updateMan"] + '</td>';
		html += '<td align="center">' + entryInfo["upDate"].substring(0, 10)
				+ '</td>';
		/* html += '<td align="center">' +entryInfo["note"] + '</td>'; */
		html += '</tr>';
		num++;
		<%k++;%>
	;
		return html;
	}

	function showPrjContractInfo(Id) {
		var strUrl = "/pages/prjContract/prjContractInfo!view.action?prjContractId="
				+ Id;
		var features = "700,500,prjContract.title_detail,prjContractManager";
		var resultvalue = OpenModal(strUrl, features);

		refreshList();

	}

	function modify() {
		var aa = document.getElementsByName("chkList");
		var itemId = GetSelIds();
		var j = 0;
		for ( var i = 0; i < aa.length; i++) {
			if (aa[i].checked) {
				itemId = aa[i].value;
				j = j + 1;
			}
		}
		if (j == 0)
			alert('<s:text name="operator.update" />')
		else if (j > 1)
			alert('<s:text name="operator.updateone" />')
		else {
			var strUrl = "/pages/prjContract/prjContractInfo!edit.action?Id="
					+ itemId;
			var features = "900,600,prjContract.title_edit,tmlInfo";
			var resultvalue = OpenModal(strUrl, features);
			refreshList();
		}
	}
	function GetSelIds() {
		var idList = "";
		//var em = document.all.tags("input");
		var em = document.getElementsByName("chkList");
		for ( var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox") {
				if (em[i].checked) {
					idList += "," + em[i].value.split(",")[0];
				}
			}
		}
		if (idList == "")
			return "";
		return idList.substring(1);
	}
	function del() {
		var idList = GetSelIds();
		if (idList == "") {
			alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
		}
		if (confirm('<s:text name="确认删除该记录吗？" />')) {
			var strUrl = "/pages/prjContract/prjContractInfo!delete.action?ids="
					+ idList;
			var returnValue = OpenModal(strUrl, "600,380,operInfo.delete,um")
			refreshList();
		}
	}
	//加载时限制下拉列表宽度,主要是针对名称过长时出现的问题
	$(function(){
		$("#prjName").attr("style","width:100%");
	})
	
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="prjContractInfoForm"
		action="prjContractInfo!listAll.action" namespace="/pages/prjContract"
		method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">

		<%@include file="/inc/navigationBar.inc"%>
		<table width="100%" cellSpacing="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td align="left" class="input_label" width="5%">项目名称:</td>
							<td align="left" width="10%"><s:select list="groupNameList"  id="prjName" headerKey="" headerValue="请选择"></s:select>
							
							<script type="text/javascript">
								var node = document.getElementById("prjName").options;
								for(var i=0;i<node.length;i++){
									if(node[i].text.length>10){
										node[i].title = node[i].text;
										node[i].text = node[i].text.substring(0,8)+"...";
									}
								}
							</script> 
							
							
							</td>
							<td align="left" class="input_label" width="8%">项目经理:</td>
							<td align="left" width="8%">
								<!-- <input name="prjManager" type="text" id="prjManager"  class="MyInput"> -->
								<select name="prjContract.prjManager" id="prjManager">
									<option value="">
										&nbsp;
										<s:text name="notify.option"></s:text>
										&nbsp;
									</option>
									<s:iterator value="#request.groupManager" var="list">
										<option value="<s:property value='username'/>">
											<s:property value="username" />
										</option>
									</s:iterator>
							</select></td>
							<td align="left" class="input_label" width="8%">合同状态:</td>
							<td align="left" width="8%">
								<!-- <input name="status" type="text" id="status"  class="MyInput"> -->
								<select name="prjContract.status" id="status">
									<option value="">
										&nbsp;
										<s:text name="notify.option"></s:text>
										&nbsp;
									</option>
									<option value='新增'>新增</option>
									<option value='执行中'>执行中</option>
									<option value='意外中止'>意外中止</option>
									<option value='结束'>结束</option>
							</select></td>
							<td align="left" class="input_label" width="8%">项目起止日期:</td>
							<td nowrap="nowrap" width="5%"><input name="startDate"
								type="text" id="startDate" class="MyInput"  />&nbsp;&nbsp;至&nbsp;&nbsp;
								<input name="endDate" type="text"
								id="endDate" class="MyInput" />
								</td>
							<td width="10%" align="left"><tm:button site="1"></tm:button></td>
						</tr>
						<tr>

						</tr>
					</table></td>
			</tr>
		</table>
		<br />

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

		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>


		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap class="oracolumncenterheader" width="2%"></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div
						align="center">项目名称</div>
				</td>
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">项目经理</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">甲方</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">项目签订时间</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">项目开始日期</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">项目结束日期</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">项目交付日期</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">合同金额(元)</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">已付金额(元)</div>
				</td>
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">合同状态</div>
				</td>
				<!-- <td nowrap width="5%" class="oracolumncenterheader"><div align="center">合同风险</div></td> -->
				<td nowrap width="4%" class="oracolumncenterheader"><div
						align="center">进度</div>
				</td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">更新人</div>
				</td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">更新时间</div>
				</td>
				<!-- <td nowrap width="10%" class="oracolumncenterheader"><div align="center">备注</div></td> -->
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="prjContractlist" id="prjContractInfo"
					status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value="<s:property value='Id'/>" /></td>
					<td align="center">
						<div align="center">
							<a
								href='javascript:showPrjContractInfo("<s:property value="Id"/>")'><font
								color="#3366FF"> <s:property value="prjName" /> </font> </a>
						</div></td>
					<td align="center"><s:property value="prjManager" /></td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="client"/>'> <s:if
								test="client.length()>15">
								<s:property value="client.substring(0,15)+'...'" />
							</s:if> 
							<s:else><s:property value="client"/></s:else>
							<span>
					</td>
					<td align="center" nowrap="nowrap"><s:date name="signDate"
							format="yyyy-MM-dd" />
					</td>
					<td align="center" nowrap="nowrap"><s:date name="startDate"
							format="yyyy-MM-dd" />
					</td>
					<td align="center" nowrap="nowrap"><s:date name="endDate" format="yyyy-MM-dd" />
					</td>
					<td align="center" nowrap="nowrap"><s:date name="finishDate"
							format="yyyy-MM-dd" />
					</td>
					<td align="center"><s:property value="total" />
					</td>
					<td align="center"><s:property value="payment" />
					</td>
					<td align="center"><s:property value="status" />
					</td>
					<td align="center"><s:property value="fare" />
					</td>
					<td align="center"><s:property value="updateMan" />
					</td>
					<td align="center" nowrap="nowrap"><s:date name="upDate" format="yyyy-MM-dd" />
					</td>
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
									<tm:pagetag pageName="currPage" formName="prjContractInfoForm" />
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</s:form>
</body>
</html>