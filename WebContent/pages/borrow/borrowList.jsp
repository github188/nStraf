<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%-- <script type="text/javascript" src="../../js/jquery.js"></script> --%>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<html>
<head><title></title></head>
	<script type="text/javascript"> 
		function query(){
			
			var userDept= $("#queryUserDept").find("option:selected").text();
			var userGroup= $("#queryUserGroup").find("option:selected").text();
			if($("#queryUserGroup").find("option:selected").attr("title")){
				userGroup = $("#queryUserGroup").find("option:selected").attr("title");
			}
			var userName= $("#idqueryUserName").find("option:selected").text();
			var status= $("#queryStatus").find("option:selected").val();
			status = (typeof(status) == "undefined" ? '99' : status);
			var pageNum= document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/borrow/borrowInfo!query.action?from=refresh&pageNum="+pageNum;
			
			if(userDept.indexOf('--')!=0){
				actionUrl +="&borrow.detname="+userDept;
			}
			if(userGroup.indexOf('--')!=0){
				actionUrl +="&borrow.groupname="+userGroup;
			}
			if($("#queryUserName").is(":visible")==false&&userName.indexOf('--')!=0){
				actionUrl+="&borrow.userman="+userName;//.substring(0,userName.indexOf('('));
			}else if($("#queryUserName").is(":visible")==true){
				actionUrl+="&borrow.userman="+$("#queryUserName").val();
			}
			if(status!=99){
				actionUrl+="&borrow.status="+status;
			}
			var borrowTime1=$("#borrowTime1").val();
			var borrowTime2=$("#borrowTime2").val();
			if(borrowTime1!=''){
				actionUrl+="&borrowTime1="+borrowTime1;
			}
			if(borrowTime2!=''){
				actionUrl+="&borrowTime2="+borrowTime2;
			}
			actionUrl=encodeURI(actionUrl);
			
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
	function setHTML(entry,entryInfo){
		var html = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td  width="2%">';
		html += '<input type="checkbox" name="chkList" id="' + entryInfo["id"] + '" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td width="5%" align="center"><a href='+str+'><font style="color: #3366FF">' +entryInfo["createdate"] + '</font></a></td>';
		html += '<td width="5%" align="center" id="' + entryInfo["id"] + 'n"> ' +entryInfo["userman"]+ '</td>';
		html += '<td width="5%" align="center">'+entryInfo["detname"]+'</td>';
		if(entryInfo["status"]==0){
			html += '<td width="5%" align="left"><span style="color: red">财务审核通过，已发款，代还款</span></td>';
		}else if(entryInfo["status"]==1){
			html += '<td width="5%" align="left"><span style="color: blue">财务审核未通过</span></td>';
		}else if(entryInfo["status"]==2){
			html += '<td width="5%" align="left"><span style="color: #ff236B">待审核</span></td>';
		}else if(entryInfo["status"]==3){
			html += '<td width="5%" align="left"><span style="color: blue">新增</span></td>';
		}else if(entryInfo["status"]==4){
			html += '<td width="5%" align="left"><span style="color: block">还款结束</span></td>';
		}else{
			html += '<td width="5%" align="left"></td>';
		}
		html += '<td width="5%" align="center" id="' + entryInfo["id"] + 'a">' +entryInfo["amount"]+ '</td>';
		html += '<td width="5%" align="center" id="' + entryInfo["id"] + 'a">' +entryInfo["approveSum"]+ '</td>';
		html += '<td width="5%" align="center">' +entryInfo["borrowdate"]+ '</td>';
		html += '<td width="5%" align="center">' +entryInfo["expectedRepaydate"]+ '</td>';
		html += '<td width="5%" align="center">' +entryInfo["repaydate"]+ '</td>';
		html += '<td width="5%" align="center">' +entryInfo["updateMan"]+ '</td>';
		html += '<td width="5%" align="center">' +entryInfo["updateDate"]+ '</td>';
	  	html += '</tr>';
	 	<% k++;%>;
		return html;
	}
	
	function  GetSelIds(){
		var idList="";
		var  em= document.all.tags("input");
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
	
	function  SelAll(chkAll){
		//var   em=document.all.tags("input");
		var em = $("input[type='checkbox']"); 
		for(var  i=0;i<em.length;i++){
			if(em[i].type=="checkbox")
		    	em[i].checked=chkAll.checked
		}
	}
	
	function del() {
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
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次删除一条记录');
		else{
			if(confirm('您确认删除该记录吗？')) {
		  		var strUrl="/pages/borrow/borrowInfo!delete.action?ids="+itemId;
				var resultvalue = OpenModal(strUrl,"600,380,operInfo.delete,um");
				refreshList();
			}
		}
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
			var strUrl="/pages/borrow/borrowInfo!edit.action?ids="+itemId;
			var features="900,550,tmlInfo.updateTitle,tmlInfo";
			OpenModal(strUrl,features);
			refreshList();
			//location="/nStraf/pages/projectplan/projectplanInfo!edit.action?ids="+itemId;
		}
	}
		
	function add(){
	     var resultvalue = OpenModal('/pages/borrow/borrowInfo!add.action','900,550,tmlInfo.addTmlTitle,tmlInfo');
	     refreshList();  
		//location='/nStraf/pages/projectplan/projectplanInfo!add.action';
	}
		
	function UpAuditing(){
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
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次提审一条记录');
		else{
			if(confirm('您确认提审该记录吗？')) {
		  		var strUrl="/pages/borrow/borrowInfo!upAuditing.action?ids="+itemId;
				OpenModal(strUrl,"600,380,operInfo.delete,um");
				refreshList();
			}
		}
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
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次提审一条记录');
		else{
		  	var strUrl="/pages/borrow/borrowInfo!auditing.action?ids="+itemId;
			OpenModal(strUrl,"900,550,,");
			refreshList();
		}
	}
				
	function show(id) {
		var strUrl="/pages/borrow/borrowInfo!show.action?ids="+id;
		var features="900,550,transmgr.traninfo,watch";
		var resultvalue = OpenModal(strUrl,features);
		//location="/nStraf"+strUrl;
	}
	
	function borrow(){
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
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次提审一条记录');
		else{
			var name = $('#'+itemId+'n').html();
			var amount = $('#'+itemId+'a').html();
			if(confirm('您确认对此登记发款吗？ \n 姓名：'+name+'\n 金额：' +amount)) {
		  		var strUrl="/pages/borrow/borrowInfo!borrow.action?ids="+itemId;
				OpenModal(strUrl,"900,550,,");
				refreshList();
		    }
		}
	}
	
	function repay(){
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
		 	alert('请选择一条记录');
		 else if (j>1)
		 	alert('你只能一次提审一条记录');
		else{
			/*var name = $('#'+itemId+'n').html();
			var amount = $('#'+itemId+'a').html();
			if(confirm('您确定此借款已还清吗？ \n 姓名：'+name+'\n 金额：' +amount)) {
		  		var strUrl="/pages/borrow/borrowInfo!repay.action?ids="+itemId;
				OpenModal(strUrl,"900,550,,");
				refreshList();
		    }*/
			var strUrl="/pages/borrow/borrowInfo!repay.action?ids="+itemId;
			OpenModal(strUrl,"900,550,,");
			refreshList();
		}
	}
	
	
	$(function(){
		//隐藏部门查询
		$("#prjName").parent().prev().hide();
		$("#prjName").parent().hide();
		var status = "<%=request.getAttribute("status")%>";
		if(status=="2"){
			$("#queryStatus option[value='2']").attr("selected","selected");
		}else if(status=="0"){
			$("#queryStatus option[value='0']").attr("selected","selected");
		}
	});
	
	//还款提醒设置
	function repayRemind(){
		 var resultvalue = OpenModal('/pages/borrow/borrowInfo!remind.action','500,350,tmlInfo.addTmlTitle,tmlInfo');
		 //refreshList();  
	}
	
	function resetInfo(){
		$("#queryUserDept").val('全选');
		$("#queryUserGroup").val('全选');
		$("#queryStatus").val('6');
		$("#borrowTime1").val('');
		$("#borrowTime2").val('');
		var userName = $("#idqueryUserName").find("option:selected").text();
		if($("#idqueryUserName").is(":visible")==true){
			$("#idqueryUserName").val('全选');
		}else if($("#idqueryUserName").is(":visible")==false){
			$("#queryUserName").val('');
		}
	}
	
</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="certificationInfoForm"  namespace="/pages/borrow" action="borrowInfo!query.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%"   cellSpacing="0" cellPadding="0" > 
 		<tr>
	 		<td>
				<table width="100%" class="select_area">
					<tr>
						<tm:deptSelect 
							deptId="queryUserDept" 
							deptName="dateDayLog.userDept"
							groupId="queryUserGroup"
							groupName="dateDayLog.userGroup"
							userId="queryUserName" 
							userName="dateDayLog.userName"
							isloadName="false" 
							deptHeadKey="---请选择部门---" 
							deptHeadValue="全选" 
							userHeadKey="---请选择人员---" 
							userHeadValue="全选"  
							groupHeadKey="---请选择项目名称---"
							groupHeadValue="全选"
							labelDept="所属部门 :" 
							labelGroup="所在项目名称:" 
							labelUser="姓名 :" 
							deptLabelClass="align:right; width:0%;class:input_label;"
							deptClass="align:left;width:0%;" 
							groupLabelClass="align:right; width:8%;class:input_label;width:115px;"
							groupClass="align:left;width:26%;" 
							userLabelClass="align:right; width:8%;class:input_label;width:115px"
							userClass="align:left;width:26%;" 
							>
						</tm:deptSelect>
					</tr>
					<tr>
						<td width="8%" height="27" align="right" class="input_label">借款时间&nbsp;&nbsp;:</td>
						<td width="26%" align="left">
							<table width="100%"  cellSpacing="0" cellPadding="0" >
								<tr>
									<td colspan="4" width="11%" align="left" nowrap="nowrap"><input name="borrowTime1" id="borrowTime1" type="text"  size="11"  class="MyInput" readonly="readonly" />
									至 
									<input name="borrowTime2" id="borrowTime2" type="text"  size="11" class="MyInput" readonly="readonly" /></td>
								</tr>
							</table>
						</td>
						<td width="8%" height="27" align="right" class="input_label">状态:</td>
						<td width="26%" align="left">
							<table width="100%"   cellSpacing="0" cellPadding="0" >
								<tr>
									<td>
										<select name="queryStatus" id="queryStatus">
											<!-- <option value="99">全部</option>
											<option value="4">新增</option>
											<option value="1">待审核</option>
											<option value="3">打回</option>
											<option value="2">审核通过</option>
											<option value="5">已发款</option>
											<option value="6">已还款</option>
											<option value="0">还款超时</option> -->
											<option value="6" >---- 请选择 ----</option>
											<option value="3">新增</option>
											<option value="2">待审核</option>
											<option value="0">财务审核通过，已发款，代还款</option>
											<option value="1">财务审核未通过</option>
											<option value="4">还款结束</option>
										</select>
									</td>
								</tr>
							</table>	
						</td>
						<td align="right" colspan="2"> 
							<input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">
							<tm:button site="1"/>
						</td>
					</tr>
				</table>  
			</td> 
		</tr> 
	</table><br />
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			 <td width="25"  height="23" valign="middle"></td>
			 <td class="orarowhead"><s:text name="operInfo.title" /></td>
			 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">申请日期</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">借款人</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">部门</div></td>
			<td nowrap width="18%" class="oracolumncenterheader"><div align="center">借款状态</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">借款金额</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">核准金额</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">借款日期</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">预计还款日期</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">还款结束日期</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">更新人</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">更新时间</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="borrowList" id="tranInfo" status="row">
	  		<s:if test="#row.odd == true">
	  			<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:if>
	 		<s:else>
	 			<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>' id="<s:property value='id'/>"/></td>
			<td nowrap width="5%" align="center">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'>
						<font style="color: #3366FF"><s:date name="createdate" format="yyyy-MM-dd" /></font>
			  		</a>
			  	</div>
		  	</td>
		  	<td nowrap width="5%" align="center" id="<s:property value='id'/>n"><s:property value="userman" /></td>
		  	<td nowrap width="5%" align="center"><s:property value="detname" /></td>
		 	<td nowrap width="5%" align="left">
		 		<s:if test="status==0"><span style="color: red">财务审核通过，已发款，代还款</span></s:if>
		 		<s:elseif test="status==1"><span style="color: blue">财务审核未通过</span></s:elseif>
		 		<s:elseif test="status==2"><span style="color: #ff236B">待审核</span></s:elseif>
		 		<s:elseif test="status==3"><span style="color: blue">新增</span></s:elseif>
		 		<s:elseif test="status==4"><span style="color: block">还款结束</span></s:elseif>
		 	</td>
		 	<td nowrap width="5%" align="center" id="<s:property value='id'/>a"><s:property value="amount" /></td>
		 	<td nowrap width="5%" align="center" id="<s:property value='id'/>a"><s:property value="approveSum" /></td>
		 	<td nowrap width="5%" align="center"><s:date name="borrowdate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="5%" align="center"><s:date name="expectedRepaydate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="5%" align="center"><s:date name="repaydate" format="yyyy-MM-dd" /></td>
		 	<td nowrap width="5%" align="center"><s:property value="updateMan" /></td>
		 	<td nowrap width="5%" align="center"><s:date name="updateDate" format="yyyy-MM-dd" /></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
		<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
					<td width="83%" align="right">
					<div id="pagetag"><tm:pagetag pageName="currPage" formName="certificationInfoForm" /></div>
				</td>
			</tr>
			</table>
		</td>
		</tr>
	</table>
 </s:form>
</body>
</html>

