<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>日志监控</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>


<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript"> 

/*同步数据*/
function synchronize(){
	var strUrl="/pages/loglistener/loglistener!synchronize.action";
	var feature="800,600,grpInfo.updateTitle,um";
	alert("同步需要较长时间，请耐心等待");
 	var returnValue=OpenModal(strUrl,feature);
}

//添加页面
function add(){
	var strUrl="/pages/loglistener/loglistener!addPage.action";
	var features="800,600,daylog.listener.add,um";	
	var resultvalue =OpenModal(strUrl,features);	
	refreshList();
	//location="<%=request.getContextPath()%>/pages/loglistener/loglistener!addPage.action";
}


function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/loglistener/loglistener!refresh.action?from=refresh&pageNum="+pageNum;
	actionUrl = encodeURI(actionUrl,"UTF-8");
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
 		html+= '<tr id="tr'+entryInfo['id']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td align="center"><input type="checkbox" name="id" value="'+entryInfo['id']+'"/></td>';
		html += '<td>';
		html += '<div align="center">';
		html += entryInfo['orgName'];
		html += '</div>';
		html += '</td>';
		html += '<td align="center">'+entryInfo['monitorName']+'</td>';
		html += '<td align="center">'+entryInfo['watchedName']+'</td>';
		html += '<td>';
		html += '<div align="center">';
		if (entryInfo['autoUpdate'] == 1) {
			html += '<font color="blue">自动更新</font>';
		}else if (entryInfo['autoUpdate'] == 0) {
			html += '<font color="green">手动更新</font>';
		}else{
			html += '<font color="red">异常</font>';
		}
		html += '</div>';
		html += '</td>';
		html += '</tr>';
<%k++;%>
	return html;
	}

	function showLeaveInfo(id) {
		var strUrl = "/pages/leave/leaveInfo!view.action?id=" + id;
		var features = "600,500,user.leave.detail,hols";
		var resultvalue = OpenModal(strUrl, features);
		refreshList();

	}

	//获取所选复选框
	function GetSelIds() {
		var idList = "";
		//var em = document.all.tags("input");
		var em = document.getElementsByName("id");
		for (var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox" && em[i].value != "all") {
				if (em[i].checked) {
					idList += "," + em[i].value.split(",")[0];
				}
			}
		}

		if (idList == "")
			return "";
		return idList.substring(1);
	}

	//全选
	function SelAll(chkAll) {
		//var em = document.all.tags("input");
		var em = document.getElementsByName("id");
		for (var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox") {
				em[i].checked = chkAll.checked
			}
		}
	}

	function del() {
		var idList = GetSelIds();
		if (idList == "") {
			alert("请选择需要删除的监控记录");
			return false;
		}
		if (confirm("确定删除该监控记录吗？")) {
			var strUrl = "/pages/loglistener/loglistener!delete.action?ids="
					+ idList;
			var features = "800,600,user.hols.deleteTitle,hols";
			var resultvalue = OpenModal(strUrl, features);
			refreshList();
		}
	}
	//修改
	function modify() {
		var idList = GetSelIds();
		var ids = idList.split(',');
		if (idList == "") {
			alert("请选择需要修改的监控记录");
			return false;
		}
		if (ids.length > 1) {
			alert('一次只能修改一条监控记录');
			return false;
		}
		var strUrl = "/pages/loglistener/loglistener!modifyPage.action?id=" + idList;
		var features = "800,600,daylog.listener.modify,um";
		var resultvalue = OpenModal(strUrl, features);
		//location="/nStraf/pages/loglistener/loglistener!modifyPage.action?id=" + idList;
		refreshList();
	}

	//设为自动更新
	function validate() {
		var idList = GetSelIds();
		if (idList == "") {
			alert("请选择需要设为自动更新的监控");
			return false;
		}
		if (confirm("确定设为自动更新吗?？")) {
			var strUrl = "/pages/loglistener/loglistener!validates.action?ids="
					+ idList;
			var features = "800,600,user.hols.deleteTitle,hols";
			var resultvalue = OpenModal(strUrl, features);
			refreshList();
		}
	}
	//设为手动更新
	function invalidate() {
		var idList = GetSelIds();
		if (idList == "") {
			alert("请选择需要设为手动更新的监控");
			return false;
		}
		if (confirm("确定设为手动更新吗?？")) {
			var strUrl = "/pages/loglistener/loglistener!invalidate.action?ids="
					+ idList;
			var features = "800,600,user.hols.deleteTitle,hols";
			var resultvalue = OpenModal(strUrl, features);
			refreshList();
		}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="loglistenerForm" action="loglistener!list.action"
		namespace="/pages/loglistener" method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
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
							<td align="right"><tm:button site="1" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<br>

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>



		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id="downloadList">
			<tr>
				<td nowrap width="3%" class="oracolumncenterheader"><s:text
						name="operInfo.checkall" /></td>
				<td nowrap width="15%" class="oracolumncenterheader"><div
						align="center">项目/部门</div></td>
				<td nowrap width="23%" class="oracolumncenterheader"><div
						align="center">监控人</div></td>
				<td nowrap width="49%" class="oracolumncenterheader"><div
						align="center">被监控人</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">自动/手动更新</div></td>
			</tr>
			<tbody id="formlist">
				<s:iterator value="#request.listener" id="log" status="s">
					<tr id='tr<s:property value="id"/>'
						class="trClass<s:property value='#s.index%2'/>" oriClass="">
						<td nowrap align="center"><input type="checkbox" name="id" value='<s:property value="id"/>' /></td>
						<td align="center"><s:property value="orgName"/></td>
						<td align="center"><s:property value="monitorName"/></td>
						<td align="center"><s:property value="watchedName" /></td>
						<td align="center">
							<s:if test="autoUpdate==\"1\"">
								<font color="blue">自动更新</font>
							</s:if>
							<s:elseif test="autoUpdate==\"0\"">
								<font color="green">手动更新</font>
							</s:elseif>
							 <s:else>
								<font color="red">异常</font>
							</s:else>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1"
			cellspacing="1" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="6%">
								<div align="center">
									<input type="checkbox" name="all" id=chkAll value="all"
										onClick="SelAll(this)">
								</div>
							</td>
							<td width="11%">
								<div align="left">
									<label for="chkAll"><s:text name="operInfo.checkall" /></label>
								</div>
							</td>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="sysUserForm" />
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