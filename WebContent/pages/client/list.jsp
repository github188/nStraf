<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>

<%-- <script language="javascript" src="../../js/jquery.js"></script> --%>

<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript"> 

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var startTime =  document.getElementById("startTime").value;
	var endTime =  document.getElementById("endTime").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/client/clientUpload!refresh.action?form=refresh&pageNum="+pageNum+"&startTime="+startTime+"&endTime="+endTime;
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
 		html+= '<tr id="'+entryInfo['id']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td width="15%">';
		html += '<div align="left"><a href="/nStraf/pages/client/clientUpload!downloadFile.action?id='+entryInfo['id']+'" ><font style="color: #3366FF">' + entryInfo['fileName'] + '</font></a></div>';
		html += '</td>';
		html += '<td width="20%">';
		html += '<div align="left">' + entryInfo['url'] + '</div>';
		html += '</td>';
		html += '<td width="5%">';
		html += '<div align="center">' + entryInfo["version"] + '</div>';
		html += '</td>';
		html += '<td  width="8%">';
		html += '<div align="center">'
				+ entryInfo["uploadTime"].substring(0, 19) + '</div>';
		html += '</td>';
		html += '<td  width="5%">';
		html += '<div align="center">'
		if(entryInfo["status"]=="0"){
			html += '选择更新'
		}else{
			html += '强制更新';
		}
		html += '</div>';
		html += '</td>';
		html += '<td  width="5%">';
		html += '<div align="center">' + entryInfo["username"]
				+ '</div>';
		html += '</td>';
		html += '</tr>';
		<%k++;%>
	return html;
	}
	
	function upload(){
		var strUrl="/pages/client/clientUpload!toUpload.action";
		var features="600,300,'上传文件'";	
		var resultvalue =OpenModal(strUrl,features);	
		query();
		//location.href="/nStraf/pages/client/clientUpload!toUpload.action";
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="leaveForm" action="leaveInfo!list.action"
		namespace="/pages/leave" method="post">

		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />

		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0"
			class="select_area">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td width="10%" align="right" class="input_label">上传开始时间
								<s:text name="label.colon" /></td>
							<td width="20%"><input name="startTime" type="text"
								id="startTime" size="12" maxlength="12" class="MyInput"
								readonly="readonly" 
								value='<s:date name="startTime" format="yyyy-MM-dd"/>'>
							</td>
							<td width="10%" align="right" class="input_label">上传结束时间
								<s:text name="label.colon" /></td>
							<td width="20%"><input name="endTime" type="text"
								id="endTime" size="12" maxlength="12" class="MyInput"
								readonly="readonly" 
								value='<s:date name="endTime" format="yyyy-MM-dd"/>'></td>
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
				<td nowrap width="15%" class="oracolumncenterheader"><div align="center">
						文件名
					</div></td>
				<td nowrap width="20%" class="oracolumncenterheader"><div
						align="center">文件路径</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">版本</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">
						上传时间
					</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">
						更新类型
					</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">
						上传人
					</div></td>
			</tr>
			<tbody id="formlist">
				<s:iterator value="#request.clients" id="client" status="s">
					<tr id='tr<s:property value="id"/>' class="trClass<s:property value='#s.index%2'/>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						<td nowrap width="15%">
							<div align="left">
								<a href="/nStraf/pages/client/clientUpload!downloadFile.action?id=<s:property value="id" />"><font style="color: #3366FF"><s:property value="fileName" /></font></a>
							</div>
						</td>
						<td nowrap width="20%">
							<div align="left">
								<s:property value="url" />
							</div>
						</td>
						<td nowrap width="5%">
							<div align="center">
								<s:property value="version" />
							</div>
						</td>
						<td nowrap width="8%" nowrap="nowrap">
							<div align="center" id="startTime">
								<s:date name="uploadTime" format="yyyy-MM-dd HH:mm:ss" />
							</div>
						</td>
						<td nowrap width="8%">
							<div align="center" id="startTime">
								<s:if test="status==0">选择更新</s:if>
								<s:else>强制更新</s:else>
							</div>
						</td>
						<td nowrap width="5%">
							<div align="center">
								<s:property value="username" />
							</div>
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