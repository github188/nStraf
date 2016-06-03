<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>tool query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">		
	function query(){
		var cid = document.getElementById("cid").value;
		var category = document.getElementById("category").value;
		var courseName = document.getElementById("courseName").value;
		var teacher = document.getElementById("teacher").value;
		var showdiv = document.getElementById("showdiv");
		var pageNum = document.getElementById("pageNum").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/course/courseinfo!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&cid="+cid;
		actionUrl += "&category="+category;
		actionUrl += "&courseName="+courseName;
		actionUrl += "&teacher="+teacher;
		
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0;%>
   		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	function setHTML(entry,entryInfo){
		var html = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";	
		var str1="'javascript:show1(\""+entryInfo["path"]+"\")'";	
		html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["id"] + '</font></a></td>';
		html += '<td>' +entryInfo["courseName"] + '</td>';
		html += '<td align="center">' +entryInfo["category"] + '</td>';
		html += '<td>' +entryInfo["teacher"] + '</td>';
		html += '<td align="center"><a href=' +str1+'><font color="#3366FF">' +entryInfo["resourceName"] + '</font></a></td>';
		  
		html += '<td align="center">' +entryInfo["updateMan"] + '</td>';
		html += '<td align="center">' +entryInfo["updateDate"] + '</td>';
		html += '<td align="center"><input type="button" id="setModuleBut" name="setModuleBut" value="设置评估发放" onclick="setModuleDistribute(\''+entryInfo["id"]+'\');"></td>';
		html += '</tr>';
		<%k++;%>;    
		showdiv.style.display = "none"; 
		return html;
	}
         
	function show(id) {
		var strUrl="/pages/course/courseinfo!show.action?ids="+id;
		var features="1000,350,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features); 
	}  
	
	function setModuleDistribute(itemId){
		OpenModal('/pages/moduleDistribute/moduleDistributeInfo!addModule.action?courseId='+itemId,'750,650,tmlInfo.addTmlTitle,tmlInfo');
	}
</script>
<body id="bodyid" style="margin: 0">
	<s:form name="certificationInfoForm" namespace="/pages/trip"
		action="tripinfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="99%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td width="7%" height="27" class="input_label" align="center">课程编号:</td>
							<td width="16%"><input name="cid" type="text" id="cid" /></td>
							<td width="7%" class="input_label" align="center">课程名称:</td>
							<td width="17%"><input name="courseName" type="text"
								id="courseName" /></td>
							<td width="7%" class="input_label" align="center">分类:</td>
							<td width="13%" align="left"><s:select
									list="{'理论','方法','工具','业务','标准','技术','制度','流程'}"
									name="category" theme="simple" cssStyle="width:90px;"
									headerKey="" headerValue="--全部--" id="category"></s:select></td>
							<td width="6%" class="input_label" align="center">讲师:</td>
							<td width="12%" align="center"><input name="teacher"
								type="text" id="teacher" /></td>
							<td width="15%" align="right"><tm:button site="1" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" vAlign="middle">
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
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<td width="7%" nowrap class="oracolumncenterheader">课程编号</td>
				<td nowrap width="22%" class="oracolumncenterheader">课程名称</td>
				<td nowrap width="8%" class="oracolumncenterheader">分类</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">讲师</div></td>
				<td width="25%" nowrap class="oracolumncenterheader">资料下载
					<div align="center"></div>
				</td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">最近更新者</div></td>
				<td nowrap width="13%" class="oracolumncenterheader"><div
						align="center">最近更新日期</div></td>
				<td nowrap width="13%" class="oracolumncenterheader"><div
						align="center">操作</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="list" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value='<s:property value="id"/>' /></td>
					<td align="center">
						<div align="center">
							<a href='javascript:show("<s:property value="id"/>")'><font
								color="#3366FF"> <s:property value="id" />
							</font> </a>
						</div>
					</td>
					</td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="courseName"/>'> <s:if
								test="courseName.length()>19">
								<s:property value="courseName.substring(0,19)+'...'" />
							</s:if> <s:else>
								<s:property value="courseName" />
							</s:else> <span></td>
					<td align="center"><s:property value="category" /></td>
					<td align="center"><s:property value="teacher" /></td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="resourceName"/>'> <s:if
								test="resourceName.length()>22">
								<a href='javascript:show1("<s:property value="path"/>")'><font
									color="#3366FF"> <s:property
											value="resourceName.substring(0,22)+'...'" />
								</font> </a>
							</s:if> <s:else>
								<a href='javascript:show1("<s:property value="path"/>")'><font
									color="#3366FF"> <s:property value="resourceName" />
								</font> </a>
							</s:else> <span></td>
					<td align="center"><s:property value="updateMan" /></td>
					<td align="center"><s:property value="updateDate" /></td>
					<td align="center"><input type="button" id="setModuleBut" name="setModuleBut" value="设置评估发放" onclick="setModuleDistribute('<s:property value='id'/>');"></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" bgcolor="#FFFFFF" cellpadding="1"
			cellspacing="1" bgcolor="#000066">
			<tr>
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage"
										formName="certificationInfoForm" />
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
