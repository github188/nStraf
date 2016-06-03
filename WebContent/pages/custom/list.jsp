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
<%-- <script language="javascript" src="../../js/jquery.js"></script> --%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
 var num=1;
	function add(){
		OpenModal('/pages/custom/customInfo!add.action','900,600,tmlInfo.addTmlTitle,tmlInfo');
		refreshList();

	}
	function query() {
		var client = document.getElementById("client").value;
		var mouthPiece = document.getElementById("mouthPiece").value;
		var prjList = document.getElementById("prjList").value;
		var creatDate = document.getElementById("creatDate").value;
		var creatEndDate = document.getElementById("creatEndDate").value;
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum==1)
			num=1;
		var actionUrl = "<%=request.getContextPath()%>/pages/custom/customInfo!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&custom.client="+client;
		actionUrl += "&custom.mouthPiece="+mouthPiece;
		actionUrl += "&custom.prjList="+prjList;
		actionUrl += "&custom.creatDate="+creatDate;
		actionUrl += "&creatEndDate="+creatEndDate;
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
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td align="center">';
		html += '<div align="center" id="customId">';
		html += '<a href="javascript:showCustomInfo(' + "'" + entryInfo['id']
				+ "'" + ')"><font color="#3366FF">' + entryInfo['client']
				+ '</font></a>';
		html += '</div>';
		html += '</td>';
		html += '<td align="center">';
		html += '<div align="center" id="customId">';
		html += '<a href="javascript:showCustomInfo(' + "'" + entryInfo['id']
				+ "'" + ')"><font color="#3366FF">' + entryInfo['address']
				+ '</font></a>';
		html += '</div>';
		html += '</td>';
		html += '<td align="center">' + entryInfo["mouthPiece"] + '</td>';
		html += '<td align="center">' + entryInfo["tel"] + '</td>';
		html += '<td align="center">' + entryInfo["mail"] + '</td>';
		html += '<td align="center">' + entryInfo["updateMan"] + '</td>';
		html += '<td align="center">' + entryInfo["creatDate"].substring(0, 10)
				+ '</td>';
		html += '<td align="center">' + entryInfo["upDate"].substring(0, 10)
				+ '</td>';
		var str = entryInfo["prjList"];
		if (str.length > 12) {
			str = str.substr(0, 12);
		}
		html += '<td align="center" nowrap="nowrap" title=' + entryInfo["prjList"] + '>'
				+ str + '</td>';
		/* html += '<td align="center">' +entryInfo["note"] + '</td>'; */
		html += '</tr>';
		num++;
<%k++;%>
	;
		return html;
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
			var strUrl = "/pages/custom/customInfo!edit.action?id=" + itemId;
			var features = "900,600,tmlInfo.updateTitle,tmlInfo";
			OpenModal(strUrl, features);
			refreshList();
		}
	}
	//全选
	function SelAll(chkAll) {
		//var em = document.all.tags("input");
		var em= document.getElementsByName("chkList");
		for ( var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox") {
				em[i].checked = chkAll.checked
			}
		}
	}
	function GetSelIds() {
		var idList = "";
		//var em = document.all.tags("input");
		var em= document.getElementsByName("chkList");
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

	function showCustomInfo(id) {
		var strUrl = "/pages/custom/customInfo!view.action?customId=" + id;
		var features = "600,500,prjChance.list.detail,customManager";
		OpenModal(strUrl, features);
		refreshList();

	}

	function del() {
		var idList = GetSelIds();
		if (idList == "") {
			alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
		}
		if (confirm('<s:text name="确认删除该记录吗？" />')) {
			var strUrl = "/pages/custom/customInfo!delete.action?ids=" + idList;
			OpenModal(strUrl, "600,380,operInfo.delete,um")
			refreshList();
		}
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="customInfoForm" action="customInfo!listAll.action"
		namespace="/pages/custom" method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">

		<%@include file="/inc/navigationBar.inc"%>

		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td align="left" class="input_label" width="5%">项目名称:</td>
							<td align="left" width="8%"><s:select list="groupNameList" id="prjList"
									headerKey="" headerValue="请选择"></s:select>
							<script type="text/javascript">
								var node = document.getElementById("prjList").options;
								for(var i=0;i<node.length;i++){
									if(node[i].text.length>10){
										node[i].title = node[i].text;
										node[i].text = node[i].text.substring(0,8)+"...";
									}
								}
							</script> 
							</td>
							<td  class="input_label" width="5%">客户名称:</td>
							<td align="left" width="8%"><input name="client" type="text"
								id="client" />
							</td>

							<td  class="input_label" width="5%">联系人:</td>
							<td align="left" width="5%"><input name="mouthPiece" type="text"
								id="mouthPiece" />
							</td>							
						</tr>
						<tr>
							<td class="input_label" nowrap="nowrap" width="5%">登记日期:</td>
							<td colspan=3><input name="creatDate" type="text" id="creatDate"
								class="MyInput" size="11" />
								&nbsp;&nbsp;至&nbsp;&nbsp;
								<input name="creatEndDate" type="text" id="creatEndDate"
								class="MyInput" size="11" />
							</td>
							<td colspan=2 align="right"><tm:button site="1"></tm:button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
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
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap class="oracolumncenterheader" width="2%"></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">客户名称</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">联系地址</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">联系人</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">联系电话</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">联系邮箱</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">更新人</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">登记日期</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">更新时间</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">项目列表</div></td>
				<!-- <td nowrap width="7%" class="oracolumncenterheader"><div align="center">备注</div></td> -->
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="customlist" id="customInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value="<s:property value='id'/>" />
					</td>
					<%-- <td align="center">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="#row.count"/></font>
			  		</a>
			  	</div>
			</td> --%>
					<%-- <td align="center"><s:property value="client"/></td> --%>
					<td align="center">
						<div align="center">
							<a href='javascript:showCustomInfo("<s:property value="id"/>")'><font
								color="#3366FF"> <s:property value="client" /> </font> </a>
						</div>
					</td>
					<%-- <td align="center"><s:property value="address"/></td> --%>
					<td align="center"><a href='javascript:showCustomInfo("<s:property value="id"/>")'><font
								color="#3366FF"><s:property value="address" /></font></a>
					</td>
					<td align="center"><s:property value="mouthPiece" /></td>
					<td align="center"><s:property value="tel" /></td>
					<td align="center"><s:property value="mail" /></td>
					<td align="center"><s:property value="updateMan" /></td>
					<td align="center" nowrap="nowrap"><s:date name="creatDate"
							format="yyyy-MM-dd" /></td>
					<td align="center" nowrap="nowrap"><s:date name="upDate"
							format="yyyy-MM-dd" /></td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="prjList"/>'> <s:if
								test="prjList.length()>12">
								<s:property value="prjList.substring(0,12)+'...'" />
							</s:if> 
							<s:else><s:property value="prjList" /></s:else>
							<span>
					</td>
					<%--  <td align="center"><s:property value="note"/></td> --%>
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
							<%-- <td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
					<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
					</td> --%>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="customForm" />
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