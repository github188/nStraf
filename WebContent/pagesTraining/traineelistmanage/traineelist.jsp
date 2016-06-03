<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/inc/pagination.inc"%>
<%@page import="cn.grgbanking.feeltm.domain.*"%>
<html>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<script type="text/javascript">

//添加
function addOne() {
	var resultvalue = OpenModal_login(
			'/pagesTraining/traineemanage/traineemanage!addOne.action',
			'840,800,tmlInfo.addTmlTitle,tmlInfo');
	refreshList();
}

//批量添加学员
function GroupUserMenu(groupcode){
	var strUrl="/pagesTraining/traineemanage/traineemanage!findAllUser.action?grpcode="+groupcode;
	var feature="520,380,grpInfo.updateTitle,um";
 	var returnValue=OpenModal(strUrl,feature);
}



//获取所选复选框
function GetSelIds() {
	var idList = "";
	$("input[type='checkbox']").each(function() {
		if ($(this).is(":checked")) {
			idList += "," + $(this).val().split(",")[0];
		}
	})
	if (idList == "")
		return "";
	return idList.substring(1);
} 

//删除操作
function del() {
	
		var aa = document.getElementsByName("chkList");
		var itemId;
		var j = 0;
		for ( var i = 0; i < aa.length; i++) {
			if (aa[i].checked) {
				itemId = aa[i].value;
				alert(itemId);
				j = j + 1;
			}
		}
		if (j == 0)
			alert('请选择一条的记录');
		else if (j > 1)
			alert('你只能一次删除一条的记录');
		else {
			if (confirm('您确认删除该条记录吗？')) {
				var strUrl = "/pagesTraining/traineemanage/traineemanage!delete.action?ids="+itemId;
				var resultvalue = OpenModal_notlogin(strUrl,
						"600,380,operInfo.delete,um");
				/* query(); */
				refreshList();
			}
		}
}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="traineeListForm"
		 action="traineemanage!query.action" 
		namespace="/pagesTraining/traineemanage" method="post">
		<%@include file="/inc/navigationBar.inc"%>
		
		
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		
		
		<table width="100%" height="20" border="0" cellspacing="0"
			cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">&nbsp; <img
					src="../../images/share/list.gif" width="14" height="16"></td>
				<td class="orarowhead"><s:text name="grpInfo.table2title" /></td>
				<td align="right" width="50%"><%-- <tm:button site="2"></tm:button> --%>
				<input type="button" size="11" value="删除" onclick='del()'>
				</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<!-- 选择框 -->
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">姓名</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">培训名称</div>
				</td>
				<td nowrap width="20%" class="oracolumncenterheader"><div
						align="center">邮箱</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">部门</div>
				</td>
				
				<td nowrap width="12%" class="oracolumncenterheader" ><div
						align="center">讲师</div>
				</td>
				<td nowrap width="22%" class="oracolumncenterheader" ><div
						align="center">开课时间</div>
				</td>
				<td nowrap width="18%" class="oracolumncenterheader"><div
						align="center">来源</div>
				</td>
				
			</tr>
			<tbody id="formlist">
				<%
					int i = 1;
						int index = 0;
				%>
				<tr  id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					<td nowrap align="center"><input type="checkbox"
							name="chkList" value="8a81a0894aeae6ff014aeaed895d0008" />
					</td>
					<input type="hidden" name="trainingEnrollInfo.trianrecordid" id="trianrecordid" value="1" />
					<td nowrap>
							<div align="center"  >罗静莲</div>
					</td>
					<td nowrap>
							<div align="center"  >贸易</div>
					</td>
					<td nowrap>
							<div align="center"  >ljlian@grgbanking.com</div>
					</td>
					<td nowrap>
							<div align="center"  >开发二部</div>
					</td>
					<td nowrap>
							<div align="center"  >汪腾蛟</div>
					</td>
					<td nowrap>
							<div align="center"  >2015-01-06 9:：00</div>
					</td>
					<td nowrap>
							<div align="center"  >必修</div>
					</td>
				</tr>
				<tr  id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					<td nowrap align="center"><input type="checkbox"
							name="chkList" value="<s:property value='id'/>" />
					</td>
					<td nowrap>
							<div align="center"  >罗静莲</div>
					</td>
					<td nowrap>
							<div align="center"  >贸易</div>
					</td>
					<td nowrap>
							<div align="center"  >ljlian@grgbanking.com</div>
					</td>
					<td nowrap>
							<div align="center"  >开发二部</div>
					</td>
					<td nowrap>
							<div align="center"  >汪腾蛟</div>
					</td>
					<td nowrap>
							<div align="center"  >2015-01-06 9:：00</div>
					</td>
					<td nowrap>
							<div align="center"  >必修</div>
					</td>
				</tr>
				<%
						index++;
					%>
				
			</tbody>
		</table>
		
	</s:form>
	
	<table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1"
					cellspacing="1" bgcolor="#FFFFFF">
					<tr bgcolor="#FFFFFF">
						<td>
							<table width="100%" cellSpacing="0" cellPadding="0">
							<tr height="40px">
								<td>&nbsp;&nbsp;&nbsp;添加向导（如需添加学员，请选择如下方式）</td>
							</tr>
							<tr height="40px" >
								<td>&nbsp;&nbsp;&nbsp;+添加单个学员&nbsp;&nbsp;&nbsp;<input  type="button" value="添加"  onclick="addOne()"></td>
							</tr>
							<tr height="40px"  border="1px">
								<td>&nbsp;&nbsp;&nbsp;+从公司已有人员中添加学员&nbsp;&nbsp;&nbsp;<input  type="button" value="添加"  onClick='GroupUserMenu("<s:property value='grpcode'/>")'></td>
							</tr>
							</table>
						</td>
					</tr>
				</table>
				
	
</body
</html>

