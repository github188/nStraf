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

<script type="text/javascript">
	
	
	function query()
	{	
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
		var selectusername=document.getElementById("selectusername").value;
		var selectStarTime=document.getElementById("selectStartTime").value;
		var selectEndTime=document.getElementById("selectEndTime").value;
		var select=document.getElementById("selectStatus");
		var index = select.selectedIndex; // 选中索引
		var text = select.options[index].text; // 选中文本
		var selectStatus = select.options[index].value; // 选中值
		var select=document.getElementById("selectstatus");
		var actionUrl = "<%=request.getContextPath()%>/pages/adviceAndResponse/myAdviceandResponse!query.action?from=refresh&selectusername="+selectusername+"&pageNum="+pageNum+"&selectStatus="+selectStatus+"&selectStarTime="+selectStarTime+"&selectEndTime="+selectEndTime;
		actionUrl = encodeURI(actionUrl);
		var method="setHTML";
		
		<%int j = 0;//记录的索引
			int k = 1;%>

		sendAjaxRequest(actionUrl, method, pageNum, true);
	}
	
	//显示详细的信息
	function showadviceInfo(id){
	  	var strUrl="/pages/adviceAndResponse/myAdviceandResponse!details.action?ids="+id;
		var features="1000,800,project.view,um";
		var resultvalue = OpenModal_notlogin(strUrl,features);
	}

	//构建显示dataList的HTML
	
	function setHTML(entry,entryInfo)
	{
		var html = "";
	    html+=' <tr  class="trClass<%=j % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
		html += '<td><input type="checkbox" name="chkList" value="'+entryInfo['id']+'"/></td>';
		html += '<td width="25%">';
		html += '<a href="javascript:showadviceInfo(' + "'" + entryInfo['id']
				+ "'" + ')"><font color="#3366FF">' + entryInfo['adviceMan']
				+ '</font></a>';
		html += '</td>';
		html += '<td align="center" title="' + entryInfo["time"] + '">' + entryInfo['time'].substring(0, 10)
				+ '</td>';
		html += '<td align="center" >' + entryInfo['tel'] + '</td>';
		html += '<td align="center"  title="' + entryInfo["email"] + '">' + entryInfo['email'] + '</td>';
		html += '<td align="left"  title="' + entryInfo["content"] + '" >' + entryInfo['content'] + '</td>';
		html += '<td align="left"  title="' + entryInfo["reply"] + '" >' + entryInfo['reply'] + '</td>';
		html += '<td align="center">' + entryInfo['status'] + '</td>';
		html += '<td align="center">' + entryInfo['plantime'].substring(0, 10)
				+ '</td>';
		html += '</tr>';
 		<%j++;%>
	//每调用一次该方法，索引值加1
		return html;
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

	//全选
	function SelAll(chkAll) {
		var em = document.all.tags("input");
		for ( var i = 0; i < em.length; i++) {
			if (em[i].type == "checkbox") {
				em[i].checked = chkAll.checked
			}
		}
	}

	function del() {
		var islogin=$("#longin").val();
		if(islogin=="yes"){
			var aa = document.getElementsByName("chkList");
			var itemId;
			var j = 0;
			for ( var i = 0; i < aa.length; i++) {
				if (aa[i].checked) {
					itemId = aa[i].value;
					//alert(itemId);
					j = j + 1;
				}
			}
	
			if (j == 0)
				alert('请选择一条的记录');
			else if (j > 1)
				alert('你只能一次删除一条的记录');
			else {
				if (confirm('您确认删除该条记录吗？')) {
					var strUrl = "/pages/adviceAndResponse/myAdviceandResponse!delete.action?adviceandresponse.id=123&ids="
							+ itemId;
					var resultvalue = OpenModal_notlogin(strUrl,
							"600,380,operInfo.delete,um");
					/* query(); */
					refreshList();
				}
			}
		}else{
			if(confirm('当前页面不能删除数据,如果您要删除数据，请先登录')==false){
				return ;
			}else{
				location.href="/nStraf/index.jsp"
			}
		}
	}
	//添加
	function add() {
		var resultvalue = OpenModal_notlogin(
				'/pages/adviceAndResponse/myAdviceandResponse!add.action',
				'840,800,tmlInfo.addTmlTitle,tmlInfo');
		/* query(); */
		refreshList();
		//location="/nStraf/pages/adviceAndResponse/myAdviceandResponse!add.action";
	}

	function modify() {
		var islogin=$("#longin").val();
		if(islogin=="yes"){
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
				var strUrl = "/pages/adviceAndResponse/myAdviceandResponse!modify.action?ids="+ itemId;
				var features = "950,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal_notlogin(strUrl, features);
				query(); 
				//location="/nStraf"+strUrl;
			}
		}else{
			if(confirm('当前页面不能删除数据,如果您要删除数据，请先登录')==false){
				return ;
			}else{
				location.href="/nStraf/index.jsp"
			}
		}
		
	}
	//当页面加载完成对时候
	$(function() {
		//jqueryui的日期控件 
		$("#selectStartTime,#selectEndTime").datepicker({
			dateFormat : 'yy-mm-dd', //更改时间显示模式  
			changeMonth : true, //是否显示月份的下拉菜单，默认为false  
			changeYear : true
		//是否显示年份的下拉菜单，默认为false  
		});
		
	}); 
	
	function clearAll(){
		$("#selectusername").val("");
		$("#selectStartTime").val("");
		$("#selectEndTime").val("");
		$("select").val("");
	}

</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="addAdvanceAndResponseForm"
		 action="myAdviceandResponse!query.action" 
		namespace="/pages/adviceAndResponse" method="post">
		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" id="longin" name="longin"  value="<%=(String)request.getAttribute("longin")%>"><!-- 数据的状态 -->
		<table width="100%" cellSpacing="0" cellPadding="0" margin-top="20px"
			margin-button="20px">
			<tr height="50px">
				<td align="right" >提出人:</td>				
				<td width="50px" ><input id="selectusername" name="selectusername" type="text" value="<%=(String)request.getAttribute("curUser")%>" />
				<%--  --%>
				</td>
				<td align="right" >处理状态:</td>
				<td align="center" nowrap="nowrap"    width="150px">
					<select name="selectStatus"
						id="selectStatus" name="status" style="width: 90%">
							<option value="" selected="selected">请选择状态</option>
							<s:iterator value="#request.adviceARStautsMap" id="ele">
								<option value="<s:property value='#ele.key'/>">
									<s:property value="#ele.value" />
								</option>
							</s:iterator>
					</select>
				</td>
				<td align="right" >提出时间:</td>
				<td  width="150px"><input
					name="selectStartTime" id="selectStartTime" name="selectStarTime"
					type="text" ></td>
				<td align="center" >至</td>
				<td  width="150px"><input id="selectEndTime" name="selectEndTime" type="text"
					>
				</td>
				<%-- <td align="right"><tm:button site="1" /></td> --%>
				<td align="center" ><input type="button" size="11" value="查询" onclick='query()'> </td>
				<td align="center" ><input type="button" size="11" value="清空所有" onclick='clearAll()'> </td>
				
			</tr>
		</table>

		<table width="100%" height="20" border="0" cellspacing="0"
			cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">&nbsp; <img
					src="../../images/share/list.gif" width="14" height="16"></td>
				<td class="orarowhead"><s:text name="grpInfo.table2title" /></td>
				<td align="right" width="50%"><%-- <tm:button site="2"></tm:button> --%>
				<input type="button" size="11" value="新增" onclick='add()'>
				<input type="button" size="11" value="修改" onclick='modify()'>
				<input type="button" size="11" value="删除" onclick='del()'>
				</td>
			</tr>
		</table>



		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<!-- 选择框 -->
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">意见人</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">时间</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">电话</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">邮箱</div>
				</td>
				
				<td nowrap width="22%" class="oracolumncenterheader" ><div
						align="center">意见内容</div>
				</td>
				<td nowrap width="22%" class="oracolumncenterheader" ><div
						align="center">意见回复</div>
				</td>
				<td nowrap width="9%" class="oracolumncenterheader"><div
						align="center">处理状态</div>
				</td>
				<td nowrap width="9%" class="oracolumncenterheader"><div
						align="center">计划执行时间</div>
				</td>
			</tr>
			<tbody id="formlist">
				<%
					int i = 1;
						int index = 0;
				%>

				 <s:iterator id="grp" value="#request.advicelist" status="status">
					<tr id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						<td nowrap align="center"><input type="checkbox"
							name="chkList" value="<s:property value='id'/>" />
						</td>
						<td nowrap>
							<div align="center">
								<a href='javascript:showadviceInfo("<s:property value="id"/>")'><font
									color="#3366FF"> <s:property value="adviceMan" />
							</div></td>
						<td nowrap  id="time">
							<div align="center"   title="<s:date name="time" format="yyyy-MM-dd hh:mm:ss" />">
								<s:date name="time" format="yyyy-MM-dd" />
									<%-- <s:property value="time" /> --%>
			

							</div></td>
						<td nowrap>
							<div align="center"  >
								<s:property value="tel" />
							</div>
						</td>
						
						<td nowrap>
							<div align="center" title="<s:property value="email"/>" >
								<s:property value="email" />
							</div>
						</td>
						<td nowrap>
							<div align="left" title="<s:property value="content"/>">
								<s:property value="content" />
							</div>
						</td>
						<td nowrap>
							<div align="left" title="<s:property value="reply"/>">
								<s:property value="reply" />
							</div>
						</td>
						<td nowrap>
							<div align="center">
								
								<s:property value="status"/>
								<!-- 数据的状态 -->
							</div></td>
						<td nowrap>
							<div align="center">
								<s:date format="yyyy-MM-dd" name="plantime" />
																<s:property value="plantime" />

							</div></td>
					</tr>
					<%
						index++;
					%>
				</s:iterator> 
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
												formName="addAdvanceAndResponseForm" />
										</div></td>
								</tr>
							</table>
							</td>
					</tr>
				</table>
			</tbody>
		</table>
	</s:form>
</body
</html>

