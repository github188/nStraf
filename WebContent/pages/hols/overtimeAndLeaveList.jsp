<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String userid = request.getParameter("userid");
%>
<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">
	$(function(){
		shortShow();
	});
	
	function shortShow(){
		//当所属组别字段过长时，简化显示
		$("td[name='groupNameShow']").each(function(){
			$(this).html(shortGroupShow($(this).html()));
		});
	}
	
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<form name="reportInfoForm" action="" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="10" />
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead">加班数据</td>
				<td align="right" width="75%"></td>
			</tr>
		</table>
		<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div align="center">部门</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">加班开始时间</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">加班结束时间</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">时长(小时)</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="overtimeList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else>
						<td><s:property value="username" /></td>
						<td><s:property value="detname" /></td>
						<td><s:property value="prjname" /></td>
						<td><s:date name="startdate" format="yyyy-MM-dd HH:mm:ss" /></td>
						<td><s:date name="enddate" format="yyyy-MM-dd HH:mm:ss" /></td>
						<td><s:property value="sumtime" /></td>
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
									<tm:pagetag pageName="currPage" formName="reportInfoForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
	
	<form name="reportInfoForm" action="" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="10" />
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead">请假数据</td>
				<td align="right" width="75%"></td>
			</tr>
		</table>
		<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div align="center">部门</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div align="center">类型</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">开始时间</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">结束时间</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">时长(天)</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="leaveList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else>
						 <td width="8%">
						    <s:property value="username"/>
					    </td>  
					    <td width="11%">
					    		<s:property value="deptName"/>
					    </td>
						<td align="center" style="white-space: nowrap;"><span
							title='<s:property value="grpName"/>'> <s:if
									test="grpName.length()>17">
									<s:property value="grpName.substring(0,17)+'...'" />
								</s:if>
								<s:else><s:property value="grpName"/></s:else>
								 <span>
						</td>
					    <td width="5%">
					    		<s:property value="type"/>
					    </td>
					    <td  width="11%" nowrap="nowrap">
					    	<s:date name="startTime" format="yyyy-MM-dd HH:mm:ss"/>
					    </td>
					    <td  width="11%" nowrap="nowrap">
					    	<s:date name="endTime" format="yyyy-MM-dd HH:mm:ss"/>
					    </td>
					    <td width="8%">
					    	<s:property value="sumtime"/>
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
									<tm:pagetag pageName="currPage1" formName="reportInfoForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>