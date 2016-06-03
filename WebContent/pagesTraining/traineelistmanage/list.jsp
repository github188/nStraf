<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/inc/pagination.inc"%>
<%@page import="cn.grgbanking.training.bean.*"%>
<html>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>



<script type="text/javascript">

//当页面加载完成对时候
$(function() {
	//jqueryui的日期控件 
	$("#startTime,#endTime").datepicker({
		dateFormat : 'yy-mm-dd', //更改时间显示模式  
		changeMonth : true, //是否显示月份的下拉菜单，默认为false  
		changeYear : true
	//是否显示年份的下拉菜单，默认为false  
	});
	/* query(); */
});  





	
 function query(){
	 
	 var pageNum = document.getElementById("pageNum").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
	var trainingname = document.getElementById("trainingname").value;
	var teacher = document.getElementById("teacher").value;
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var pageNum = document.getElementById("pageNum").value;
	var actionUrl = "<%=request.getContextPath()%>/pagesTraining/traineemanage/traineemanage!query.action?from=refresh&pageNum="+pageNum;
	actionUrl += "&trainingname="+trainingname+"&teacher="+teacher+"&startTime="+startTime+"&endTime="+endTime;
	//alert(actionUrl);
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
	html += '<td><input type="checkbox" name="chkList" value="'+entryInfo['trianrecordid']+'"/></td>';
	html += '<td width="25%">';
	html += '<a href="javascript:showadviceInfo(' + "'" + entryInfo['id']
			+ "'" + ')"><font color="#3366FF">' + entryInfo['trainName']
			+ '</font></a>';
	html += '</td>';
	html += '<td align="center" title="' + entryInfo["trainStartTime"] + '">' + entryInfo['time'].substring(0, 10)
			+ '</td>';
	html += '<td align="center" >' + entryInfo['lecturerName'] + '</td>';
	html += '<td align="center"  title="' + entryInfo["traineeNum"] + '">' + entryInfo['email'] + '</td>';
	html+='<td nowrap><div align="center"  ><a href="#"><font color="#0000CD">通知   </a><a href="#"><font color="#0000CD"> 报名   </a><a href="#"><font color="#0000CD">评估 </a></div></td>';
	html += '</tr>';
		<%j++;%>
//每调用一次该方法，索引值加1
	return html;
}


</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form   name="traineeListForm"
			 	action="traineemanage!query.action" 
				namespace="/pagesTraining/traineemanage" 
				method="post">
				
	<input type="hidden" name="pageNum" id="pageNum" value="1" />
	<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>

	<table width="100%" cellSpacing="0" cellPadding="0" > 
 		<tr>
	 		<td >
				<table width="100%" class="select_area">
	             <tr >
					<td align="right" >培训名称:</td>				
					<td width="50px" ><input id="trainingname" name="trainingame" type="text" value="" />
					<%--  --%>
					</td>
					<td align="right" >讲师:</td>
						<td width="50px" ><input id="teacher" name="teacher" type="text" value=""/>
					</td>
					<td align="right" >培训时间:</td>
					<td  width="150px"><input
						name="startTime" id="startTime" 
						type="text" ></td>
					<td align="center" >至</td>
					<td  width="150px"><input id="endTime" name="endTime" type="text">
					</td>
					<%-- <td align="right"><tm:button site="1" /></td> --%>
					 <td align="center" ><input type="button" size="11" value="查询" onclick='query()'> </td>
					<td align="center" ><input type="button" size="11" value="清空所有" onclick='clearAll()'> </td> 
					
				</tr>
				</table> 
			</td> 
		</tr>
	</table>
	
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0"  class="bgbuttonselect">
		<tr>
		 <td width="25"  height="23" valign="middle">&nbsp;</td>
		 <td class="orarowhead"><s:text name="operInfo.title" /></td>
		</tr>
	</table>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				
				<td nowrap width="8%" class="oracolumncenterheader">
					<div align="center">培训名称</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader">
				    <div align="center">培训时间</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader">
				    <div align="center">讲师</div>
				</td>
				<td nowrap width="20%" class="oracolumncenterheader">
				    <div align="center">学员名单</div>
				</td>
				
				<td nowrap width="12%" class="oracolumncenterheader" >
				    <div align="center">操作</div>
				</td>
				
			</tr>
			
			
			
			<tbody id="formlist">
				<%
					int i = 1;
					int index = 0;
				%>
				 <s:iterator id="grp" value="#request.Training" status="status">
				
				  <tr     id="<%="tr" + ++i%>" class="trClass<%=(index % 2)%>"
						oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) >
					<td><input type="checkbox" name="chkList" value=<s:property value="trianrecordid"/></td>
					<td nowrap>
							<div align="center" ><s:property value="trainName"/></div>
					</td>
					
					<td nowrap>
							<div align="center"><font color="#0000CD"><s:property value="trainStartTime"/> </font></div>
					</td>
					<td nowrap>
							<div align="center"><s:property value="lecturerName"/></div>
					</td>
					
					
					<td nowrap>
							<div align="center"  ><a href="traineelist.jsp"><font
									color="#0000CD"><s:property value="traineeNum"/></a></div>
					</td>
					<td nowrap>
							<div align="center"  >
							<a href="#"><font color="#0000CD">通知   </a>
							<a href="#"><font color="#0000CD"> 报名   </a>											
							<a href="#"><font color="#0000CD">评估 </a>		
							</div>
					</td>
				</tr> 
				</s:iterator>
				<%
						index++;
					%>
				<br/>
				
			</tbody>
			
		</table>
		 <br/>
		<br/>
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
												formName="traineeListForm"/>
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

