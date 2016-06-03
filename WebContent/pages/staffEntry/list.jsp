<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%
	String ctxPath = "http://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<title>tool query</title>
</head>
<script type="text/javascript">
		//加载时限制下拉框宽度,主要是针对名称过长时出现的问题,设置100%是为了让它适应外部td
		$(function(){
			$("#grpCode").attr("style","width:100%");
		})
		function query(){
			var queryDept = $("#queryDept").find("option:selected").text();
			var queryGroup = $("#queryGroup").find("option:selected").text();
			
			if($("#queryGroup").find("option:selected").attr("title")){
				queryGroup = $("#queryGroup").find("option:selected").attr("title");
			}
			
			var queryUserName = $("#idqueryUserName").find("option:selected").text();
			var queryEntryStartTime = document.getElementById("queryEntryStartTime").value;
			var queryEntryEndTime = document.getElementById("queryEntryEndTime").value;
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = '<%=request.getContextPath()%>/pages/staffEntry/entryInfo!query.action?from=refresh&pageNum='+pageNum;
			if(queryDept.indexOf('--')!=0)
				actionUrl += "&entryInfo.detName="+$("#queryDept").find("option:selected").text();
			if(queryGroup.indexOf('--')!=0)
				actionUrl += "&entryInfo.groupName="+$("#queryGroup").find("option:selected").text();
			
			var userText=$("#idqueryUserName").find("option:selected").text();
			if($("#idqueryUserName").is(":visible")==true&&queryUserName.indexOf('--')!=0){
				if(userText.indexOf('(')>0){
					userText=$("#idqueryUserName").find("option:selected").text().substring(0,userText.indexOf('('));
				}
				actionUrl += "&entryInfo.userName="+userText;
			}else if($("#idqueryUserName").is(":visible")==false){
				actionUrl += "&entryInfo.userName="+$("#queryUserName").val();
			}
			
			actionUrl += "&queryEntryStartTime="+queryEntryStartTime;
			actionUrl += "&queryEntryEndTime="+queryEntryEndTime;
			actionUrl=encodeURI(actionUrl);
			<%int k = 0;%>
	   		ajaxRequest(actionUrl,"setHTML",pageNum,true);
	    }
		
		//回写页面
		function setHTML(entry,entryInfo){
			var html = '';
			var str ="";
			alert
			if(entryInfo["extendStatus"]!=""){
				str="<a href=\"javascript:showExtendStatus('"+entryInfo["id"]+"')\">[点击查看详情]</a>";
			}
			var color='';
			html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td nowrap align="center">';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		//具体列
		html += '<td align="center">' + entryInfo["serialNumber"] + '</td>';
		html += '<td align="center">' + entryInfo["userId"] + '</td>';
		html += '<td align="center">' + entryInfo["userName"] + '</td>';
		html += '<td align="center">'
				+ entryInfo["grgBeginDate"].substring(0, 10) + '</td>';
		html += '<td align="center">' + entryInfo["detName"] + '</td>';
		html += '<td align="center" name="groupNameShow">'
				+ shortGroupShow(entryInfo["groupName"]) + '</td>';
		html += '<td align="center">'
				+ entryInfo["regularDate"].substring(0, 10) + '</td>';
		html += '<td align="center" name="noteShow">'
				+ shortNoteShow(entryInfo["note"]) + '</td>';
		html += '<td align="center">' + str + '</td>';
		html += '<td align="center">' + entryInfo["updateMan"] + '</td>';
		html += '<td align="center">'
				+ entryInfo["updateTime"].substring(0, 10) + '</td>';
		html += '</tr>';
	    <%k++;%>
		return html;
	}

	function ajaxRequest(actionUrl, method, pageNum, ifPage, setRecordCount) {
		$("#formlist").empty();
		$.ajax({
			url : actionUrl,
			data : {},
			type : 'POST',
			dataType : 'json',
			cache : false,
			async : true,
			timeout : 10000,
			success : function(data) {
				var pageCount = data.pageCount;
				var recordCount = data.recordCount;
				var data2 = data.jsonObj;
				parseData(data2, method, pageNum, ifPage, setRecordCount,
						pageCount, recordCount);
			},
			error : function(data, data1) {
				alert("ajaxRequest错误:" + data1);
			}
		});
	}
	function parseData(data, method, pageNum, ifPage, setRecordCount,
			pageCount, recordCount) {
		var content = "";
		var fullMethdName = method + "(entry,entryInfo)";
		$.each(data, function(entry, entryInfo) {
			content += eval(fullMethdName);
		});

		if (ifPage)//如果要分页
		{
			$("#pagetag").html(getPageTagHTML(pageCount, recordCount, pageNum));
		}

		$("#formlist").html(content);

		if (setRecordCount)//如果要显示"共有记录数"
		{
			$("#recordCount").html(getRecordCountHTML(recordCount));
		}
	}
	function getPageTagHTML(pageCount, recordCount, pageNum) {
		var pagetag = "";
		pagetag += '<table border="0" cellspacing="1" cellpadding="0" class="Pagination_table">';
		pagetag += '<input type="hidden" name="pageSize" value="10">'
		pagetag += '<td nowrap class="Pagination">'

		pageNum = parseInt(pageNum);
		pageCount = parseInt(pageCount);

		//上一页
		if (pageNum > 1 && pageNum <= pageCount)
			pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage onClick="MovePrevious()" >'
		else if (pageNum > pageCount)
			pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage onClick="MovePrevious()" >'
		else
			pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage  disabled="disabled" onClick="MovePrevious()">'

		pagetag += '</td>'
		pagetag += '<td nowrap class="Pagination">'

		//下一页
		if (pageNum >= 1 && pageNum < pageCount)
			pagetag += '<INPUT class="Btn" type="button" value="<s:text name="page.nav.backward"/>" name="nextpage" onClick="MoveNext()"></td>'
		else
			pagetag += '<INPUT class="Btn" type="button" value="<s:text name="page.nav.backward"/>" name="nextpage" onClick="MoveNext()"  disabled="disabled"></td>'

		pagetag += '</td>'
		pagetag += '<td nowrap class="Pagination"><s:text name="page.nav.total"/>'
				+ recordCount
				+ '<s:text name="page.nav.size"/>'
				+ pageCount
				+ '<s:text name="page.nav.page"/></td>'
		pagetag += '<td nowrap class="Pagination"><s:text name="page.nav.go"/>'
		pagetag += '<input type="text" name="gotoPageNo" id="gotoPageNo" maxlength="8" class="textbox" size="2" value="'
				+ pageNum
				+ '" onfocus="this.select()" onKeyPress="if(window.event.keyCode==13) GotoPage()">'
		pagetag += '<s:text name="page.nav.page"/>'
		pagetag += '<INPUT class="Btn" type="button" value="GO" name="GO" onclick="GotoPage()">'
		pagetag += '</td>'
		pagetag += '</tr>'
		pagetag += '</table>'

		return pagetag;
	}
	function getRecordCountHTML(recordCount) {
		var recordCountHTML = "";
		recordCountHTML += '<tr><td height="22"><s:text name="page.nav.total"/>'
		recordCountHTML += recordCount;
		recordCountHTML += '</td>'
		recordCountHTML += '<td width="21%" height="22" valign="middle">'
		recordCountHTML += '<div align="center"> </div>'
		recordCountHTML += ' </td>'
		recordCountHTML += '<td height="22">&nbsp;&nbsp; '
		recordCountHTML += ' </td>'
		recordCountHTML += ' </tr>'

		return recordCountHTML;
	}

	function add() {
		var result = OpenModal('/pages/staffEntry/entryInfo!add.action','900,600,staffEntry.list_jsp.toAdd,tmlInfo');
		refreshList();
		//location='/nStraf/pages/staffEntry/entryInfo!add.action';
	}

	function modify() {
		var aa = document.getElementsByName("chkList");
		var itemId;
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
			var strUrl = "/pages/staffEntry/entryInfo!edit.action?ids="
					+ itemId;
			var features = "900,600,staffEntry.list_jsp.toModify,tmlInfo";
			var resultvalue = OpenModal(strUrl, features);
		}
		refreshList();
	}

	function showExtendStatus(id) {
		OpenModal(
				'/pages/staffEntry/entryInfo!showExtendStatus.action?staffEntryId='
						+ id,
				'900,300,staffEntry.list_jsp.toShowStatuDetail,tmlInfo');
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

	function del() {
		var idList = GetSelIds();
		if (idList == "") {
			alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
		}
		if (confirm('确认删除吗?')) {
			var strUrl = "/pages/staffEntry/entryInfo!delete.action?ids="
					+ idList;
			var returnValue = OpenModal(strUrl, "600,380,operInfo.delete,um")
			refreshList();
		}
	}

	$(function() {
		shortShow();
	});

	function shortShow() {
		//当所属组别字段过长时，简化显示
		$("td[name='groupNameShow']").each(function() {
			$(this).html(shortGroupShow($(this).html()));
		});

		//当备注字段过长时，简化显示
		$("td[name='noteShow']").each(function() {
			$(this).html(shortNoteShow($(this).html()));
		});
	}

	function shortGroupShow(groupName) {
		//首先去掉最后的逗号
		if (groupName.charAt(groupName.length - 1) == ',') {
			groupName = groupName.substring(0, groupName.length - 1);
		}
		var tmp = groupName;
		//如果还有逗号的话，说明是几个组，这里只展示一个组，其他组在鼠标移上去后显示
		if (groupName.indexOf(',') > 0) {
			var shortShow = groupName.substring(0, groupName.indexOf(','))
					+ '...';
			shortShow = '<a title="'+tmp+'">'
					+ shortShow + '</a>';
			return shortShow;
		} else {
			return groupName
		}
	}

	function shortNoteShow(noteName) {
		var tmp = noteName;
		//如果长度大于8，简化
		if (noteName.length > 8) {
			var shortShow = noteName.substring(0, 8) + '...';
			shortShow = '<a title="'+tmp+'">'
					+ shortShow + '</a>';
			return shortShow;
		} else {
			return noteName;
		}
	}
</script>
<body  id="bodyid" centermargin="0" topmargin="0"
	leftmargin="0">
	<s:form name="entryInfoForm" namespace="/pages/staffEntry"
		action="entryInfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0"
			class='selectTableBackground'>
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<tm:deptSelect deptId="queryDept" groupId="queryGroup"
								userId="queryUserName" isloadName="false"
								deptHeadKey="---请选择部门---" deptHeadValue="全选"
								userHeadKey="----请选择人员----" userHeadValue="全选"
								groupHeadKey="---请选择项目名称---" groupHeadValue="全选" labelDept="部门 ："
								labelGroup="项目名称：" labelUser="姓名："
								deptLabelClass="align:left; width:5%;class:input_label;"
								deptClass="align:left;width:11%;"
								groupLabelClass="align:left; width:5%;class:input_label;"
								groupClass="align:left;width:11%;"
								userLabelClass="align:left; width:5%;class:input_label;"
								userClass="align:left;width:11%;">
							</tm:deptSelect>
						</tr>
							
					</table>
					</td>
					<td height="27" align="right" class="input_label">入职日期：</td>
							<td colspan="4" width="11%" align="left" nowrap="nowrap"><input name="" 
								id="queryEntryStartTime" type="text" id="start" size="11"
								class="MyInput" readonly="readonly" />
							至
							<input name=""
								id="queryEntryEndTime" type="text" id="end" size="11"
								class="MyInput" readonly="readonly" />
							</td>
							<td width="11%" align="right"><tm:button site="1" />
							</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0"
			background="../../images_new/main/listarea/listtop.jpg">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="3%" class="oracolumncenterheader"></td>
				<td nowrap width="7%" class="oracolumncenterheader">
					<div align="center">
						流水号
					</div>
				</td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">
						<s:text name="staffEntry.list_jsp.userId" />
					</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">
						<s:text name="staffEntry.list_jsp.userName" />
					</div>
				</td>
				<td nowrap width="9%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.grgBeginDate" />
				</td>
				<td nowrap width="9%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.detName" />
				</td>
				<td nowrap width="9%" class="oracolumncenterheader">
					<s:text name="lable.projectName" />
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.regularDate" />
				</td>
				<td nowrap width="12%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.note" />
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.extendStatus" />
				</td>
				<td nowrap width="7%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.updateMan" />
				</td>
				<td nowrap width="9%" class="oracolumncenterheader"><s:text
						name="staffEntry.list_jsp.updateTime" />
				</td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="entryInfoList" id="entryInfo" status="row">
					<tr id="tr" class="" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					<!-- %{formatDouble(WorkLoad/8)}  -->
					<td nowrap align="center"><s:if test="id!=null">
							<input type="checkbox" name="chkList"
								value='<s:property value="id"/>' />
						</s:if></td>
					<td align="center"><s:property value="serialNumber" />
					</td>
					<td align="center"><s:property value="userId" />
					</td>
					<td align="center"><s:property value="userName" />
					</td>
					<td align="center"><s:date name="grgBeginDate"
							format="yyyy-MM-dd" />
					</td>
					<td align="center"><s:property value="detName" />
					</td>
					<td align="center" name="groupNameShow"><s:property
							value="groupName" />
					</td>
					<td align="center"><s:date name="regularDate"
							format="yyyy-MM-dd" />
					</td>
					<td align="center" name="noteShow"><s:property value="note" />
					</td>
					<td align="center"><s:if test="extendStatus!=null">
							<a href="javascript:showExtendStatus('<s:property value='id'/>')"><s:text
									name="staffEntry.list_jsp.extendStatuDetail" />
							</a>
						</s:if></td>
					<td align="center"><s:property value="updateMan" />
					</td>
					<td align="center"><s:date name="updateTime"
							format="yyyy-MM-dd" />
					</td>
					</tr>
					<script type="text/javascript">
						$("#formlist tr:odd").addClass("trClass0");
						$("#formlist tr:even").addClass("trClass1");
					</script>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1"  cellspacing="1">
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
