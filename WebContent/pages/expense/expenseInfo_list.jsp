<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>expense info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
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
		//location.href='/pages/expense/expenseInfo!toAddPage.action';
		OpenModal('/pages/expense/expenseInfo!toAddPage.action','1200,600,tmlInfo.addTmlTitle,tmlInfo');

		refreshList();
	}
	function query() {
		var userName = $("#idqueryUserName").find("option:selected").text();
		var detName = document.getElementById("queryUserDept").value;
		var prjName = $("#queryGroup").find("option:selected").text();
		if($("#queryGroup").find("option:selected").attr("title")){
			prjName = $("#queryGroup").find("option:selected").attr("title");
		}
		var status = document.getElementById("status").value;
		var raiseDate = document.getElementById("submitdate").value;
		var raiseEndDate = document.getElementById("submitEndDate").value;
		var expenseprjname = document.getElementById("expenseprjname").value;
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum==1)
			num=1;
		var actionUrl = "<%=request.getContextPath()%>/pages/expense/expenseInfo!refresh.action?from=refresh&pageNum="+pageNum;
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
			actionUrl += "&expenseAccount.userName="+userName;//.substring(0,userName.indexOf('('));
		}else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&expenseAccount.userName="+$("#queryUserName").val();
		}
		if(detName!='---请选择部门---'){
			actionUrl += "&expenseAccount.detName="+detName;
		}
		if(prjName!='---请选择项目名称---'){
			actionUrl += "&expenseAccount.prjName="+prjName;
		}
		if(expenseprjname!=''){
			actionUrl += "&expenseAccount.expenseprjname="+expenseprjname;
		}
		actionUrl += "&expenseAccount.status="+status;
		actionUrl += "&submitDate="+raiseDate;
		actionUrl += "&submitEndDate="+raiseEndDate;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		var str = "javascript:show(\""+entryInfo["expenseNum"]+"\")";
		html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["expenseNum"] + '" />';
		html += '</td>';
		html += '<td align="center">';
		html += '<div align="center" id="expenseNum">'; 
    	html += '<a href="javascript:showExpenseAccountInfo('+"'"+entryInfo['expenseNum']+"'"+')"><font color="#3366FF">'+entryInfo['expenseNum']+'</font></a>';
   		html += '</div>'; 
		html += '</td>';
		html += '<td align="center">';
		html += '<div align="center" id="userName">'; 
    	html += '<a href="javascript:showExpenseAccountInfo('+"'"+entryInfo['expenseNum']+"'"+')"><font color="#3366FF">'+entryInfo['userName']+'</font></a>';
   		html += '</div>'; 
		html += '</td>';
		html += '<td align="center">' +entryInfo["detName"] + '</td>';
		html += '<td align="center" title='+entryInfo["prjName"]+'>' +entryInfo["prjName"] + '</td>';
		html += '<td align="center" title='+entryInfo["expenseprjname"]+'>' +entryInfo["expenseprjname"] + '</td>';
		html += '<td align="center">' +entryInfo["type"] + '</td>';
		html += '<td align="center">' +entryInfo["sum"] + '</td>';
		html += '<td align="center">' +entryInfo["approveSum"] + '</td>';
		var status="";
		if(entryInfo["status"]=='0'){
			status="新增";
 		}
 		if(entryInfo["status"]=='1'){
 			status="行政审核中";
 		}
 		if(entryInfo["status"]=='2'){
 			status="财务审核中";
 		}
 		if(entryInfo["status"]=='3'){
 			status="审核通过，已发款";
 		}
 		if(entryInfo["status"]=='4'){
 			status="行政审核未通过";
 		}
 		if(entryInfo["status"]=='5'){
 			status="财务审核未通过";
 		}
		html+= '<td nowrap title='+status+'>';
 		if(entryInfo["status"]=='0'){
 			html += '<font color="blue">新增</font>';
 		}
 		if(entryInfo["status"]=='1'){
 			html += '<font color="red">行政审核中</font>';
 		}
 		if(entryInfo["status"]=='2'){
 			html += '<font color="red">财务审核中</font>';
 		}
 		if(entryInfo["status"]=='3'){
 			html += '<font color="green">审核通过，已发款</font>';
 		}
 		if(entryInfo["status"]=='4'){
 			html += '<font color="gray">行政审核未通过</font>';
 		}
 		if(entryInfo["status"]=='5'){
 			html += '<font color="gray">财务审核未通过</font>';
 		}
		html+= '</td>';
		html += '<td align="center">' +entryInfo["submitDate"].substring(0,10) + '</td>';
		html += '<td align="center">' +entryInfo["updateMan"] + '</td>';
		html += '<td align="center">' +entryInfo["updateDate"].substring(0,10) + '</td>';
  		html += '</tr>';
        num++;
 		<% k++;%>;     
		return html;
	}
	
	function showExpenseAccountInfo(expenseNum){
		var strUrl = "/pages/expense/expenseInfo!view.action?expenseNum="+expenseNum;
		var features="1200,600,expense.list.detail,staffManager";
		var resultvalue = OpenModal(strUrl,features);
		refreshList();
	}
	
	function modify(){
		var aa=document.getElementsByName("chkList");
		var itemId;
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
		  	var strUrl="/pages/expense/expenseInfo!toUpdatePage.action?expenseId="+itemId;
		  	var features="1200,700,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
	}
	function  GetSelIds(){
		var idList="";
		//var  em= document.all.tags("input");
		var em = $("input[type='checkbox']"); 
		for(var  i=0;i<em.length;i++){
			if(em[i].type=="checkbox"){
				if(em[i].checked){
					idList+=","+em[i].value.split(",")[0];
				}
		  	} 
	 	} 
		if(idList=="") 
		   return "";
		return idList.substring(1);
	}
	function del() {
		var idList=GetSelIds();
	  	if(idList=="") {
		  	alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
	  	}
		if(confirm('<s:text name="确认删除该记录吗？" />')) {
		 	var strUrl="/pages/expense/expenseInfo!delete.action?ids="+idList;
		 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
		 	refreshList();
		}
	}
	
	function download(){
		window.location="<%=request.getContextPath()%>/pages/expense/expenseInfo!download.action";
	}
	
	function auditing(){
		var aa=document.getElementsByName("chkList");
		var itemId;
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
		}  
		if (j==0)
		 	alert('请选择需要审核的报销单');
		 else if (j>1)
		 	alert('一次只能审核一条报销单');
		else{
		  	var strUrl="/pages/expense/expenseInfo!toAuditPage.action?expenseId="+itemId;
		  	var features="1200,700,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
	}

	//提交审核
	function UpAuditing() {
		var aa=document.getElementsByName("chkList");
		var itemId;
		var j=0;
		for (var i=0; i<aa.length; i++){
		   if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
		}  
		if (j==0)
		 	alert('请选择要提交的报销记录!')
		 else if (j>1)
		 	alert('一次只能提交一条报销记录!')
		else{
		  	var strUrl="/pages/expense/expenseInfo!submit.action?expenseId="+itemId;
		  	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
		  	refreshList();
		}
	}
	
	$(function() {
		var status = "<%=request.getAttribute("status")%>";
		if(status=="1"){
			$("#status option[value='1']").attr("selected","selected");
		}else if(status=="2"){
			$("#status option[value='2']").attr("selected","selected");
		}
		//query();
	});
	
	function resetInfo(){
		$("#queryUserDept").val('');
		$("#queryGroup").val('');
		$("#expenseprjname").val('');
		$("#status").val('6');
		$("#approvePerson").val('');
		$("#submitdate").val('');
		$("#submitEndDate").val('');
		var userName = $("#idqueryUserName").find("option:selected").text();
		if($("#idqueryUserName").is(":visible")==true){
			$("#idqueryUserName").val('');
		}else if($("#idqueryUserName").is(":visible")==false){
			$("#queryUserName").val('');
		}
	}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="expenseInfoForm" action="expenseInfo!list.action"  namespace="/pages/expense" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	
	<%@include file="/inc/navigationBar.inc"%>
	
	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 			<td >
					<table width="100%" class="select_area">
              			<tr>
							<tm:deptSelect 
								deptId="queryUserDept" 
								deptName="expenseAccount.deptName"
								groupId="queryGroup"
								groupName="expenseAccount.prjName"
								userId="queryUserName" 
								userName="expenseAccount.userName"
								isloadName="true" 
								deptHeadKey="---请选择部门---" 
								deptHeadValue="" 
								userHeadKey="----请选择人员----" 
								userHeadValue=""  
								groupHeadKey="---请选择项目名称---"
								groupHeadValue=""
								labelDept="部门 :" 
								labelGroup="项目名称:" 
								labelUser="姓名 :" 
								deptLabelClass="align:right; width:7%;class:input_label"
								deptClass="align:left;width:17%;" 
								groupLabelClass="align:right; width:7%;class:input_label"
								groupClass="align:left;width:17%;" 
								userLabelClass="align:right; width:7%;class:input_label"
								userClass="align:left;width:17%;" 
								>
							</tm:deptSelect>
							<td width="8%">报销项目名称:</td>
							<td>
								<select name="expenseprjname" id="expenseprjname" style="width:100%">
									<option value="">----请选择----</option>
									<s:iterator value="#request.projectSelect" id="project">
										   <option value="<s:property value='name'/>"><s:property value="name"/></option>
									</s:iterator>
								</select>
							</td>
						</tr>
              			<tr>
              				<td align="right" class="input_label">报销单状态:</td>
                			<td align="left">
                				<!--<s:textfield name="expenseNum" size="15" id="expenseNum"/>-->
                				<select name="status" width="15%" id="status" >
                						<option value="6" >---- 请选择 ----</option>
										<option value="0">新增</option>
										<option value="1">行政审核中</option>
										<option value="2">财务审核中</option>
										<option value="3">审核通过，已发款</option>
										<option value="4">行政审核未通过</option>
										<option value="5">财务审核未通过</option>
                				</select>
                			</td>
              			 	<td align="right" class="input_label">提交开始日期:</td> 
                			<td>
                				<input name="submitdate" type="text" id="submitdate"  
                				class="MyInput" size="11"  />
                			</td>
                			<td align="right" class="input_label">提交结束日期:</td> 
                			
                			<td>
                			<input name="submitEndDate" type="text" id="submitEndDate"  
                				class="MyInput" size="11" />
                			</td>
                			<td colspan="6" align="right">
                				<input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">
                				<tm:button site="1"></tm:button>
                			</td>
                			</tr>
					</table>
			</td> 
		</tr>
	</table>
	<br/>
	
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			<td width="25"  height="23" valign="middle">&nbsp;</td>
			<td class="orarowhead"><s:text name="operInfo.title" /></td>
			<td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>
	
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap class="oracolumncenterheader" width="2%"></td>
		  	<td nowrap width="4%" class="oracolumncenterheader"><div align="center">流水号</div></td>
		  	<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">部门</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
		  	<td nowrap width="12%" class="oracolumncenterheader"><div align="center">报销项目名称</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">报销类型</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">报销金额</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">核销金额</div></td>
	      	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">报销单状态</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">提交日期</div></td>
	      	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">更新人</div></td>
	      	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">更新时间</div></td>
	  	</tr>
	  	<tbody name="formlist" id="formlist" align="center">
  		<s:iterator  value="behaviorList" id="expenseInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if>
 		<s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center">
				<input type="checkbox" name="chkList" value="<s:property value='expenseNum'/>"/>
			</td>
			<td align="center">
			<div align="center">
		    <a href='javascript:showExpenseAccountInfo("<s:property value="expenseNum"/>")'><font color="#3366FF">
		    <s:property value="expenseNum"/></font></a></div>
		    </td>
		 	<td align="center">
		 	<div align="center">
		    <a href='javascript:showExpenseAccountInfo("<s:property value="expenseNum"/>")'><font color="#3366FF">
		    <s:property value="userName"/></font></a></div>
		 	</td>
		 	<td align="center"><s:property value="detName"/></td>
		 	<td align="center" style="white-space: nowrap;" title='<s:property value="prjName"/>'>
		 		<span title='<s:property value="prjName"/>'> <s:if test="prjName.length()>19"> <s:property value="prjName.substring(0,19)+'...'" /> </s:if>
								<s:else><s:property value="prjName"/></s:else>
				<span>
			</td>
			<td align="center" title='<s:property value="prjName"/>'><s:property value="expenseprjname"/></td>
		 	<td align="center"><s:property value="type"/></td>
            <td align="center"><s:property value="sum"/></td>
            <td align="center"><s:property value="approveSum"/></td>
            <td nowrap title='<s:if test="status==0">新增</s:if><s:elseif test="status==1">行政审核中</s:elseif><s:elseif test="status==2">财务审核中</s:elseif><s:elseif test="status==3">审核通过，已发款</s:elseif><s:elseif test="status==4">行政审核未通过</s:elseif><s:elseif test="status==5">财务审核未通过</s:elseif>'>
			    <s:if test="status==0"><font color="blue">新增</font></s:if>
            	<s:elseif test="status==1"><font color="red">行政审核中</font></s:elseif>
            	<s:elseif test="status==2"><font color="red">财务审核中</font></s:elseif>
            	<s:elseif test="status==3"><font color="green">审核通过，已发款</font></s:elseif>
            	<s:elseif test="status==4"><font color="gray">行政审核未通过</font></s:elseif>
            	<s:elseif test="status==5"><font color="gray">财务审核未通过</font></s:elseif>
			</td>
            <td align="center"><s:date name="submitDate" format="yyyy-MM-dd"/></td>
            <td align="center"><s:property value="updateMan"/></td>
		 	<td align="center"><s:date name="updateDate" format="yyyy-MM-dd"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 	<table width="100%" border="0" cellpadding="1" cellspacing="1"  bgcolor="#FFFFFF">  
	<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<!--<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
					<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
					</td>-->
					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="expenseInfoForm" /></div>
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
</s:form>
</body>
</html>