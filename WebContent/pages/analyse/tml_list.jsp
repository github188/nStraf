<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
	<head>
		<title>Account analyse query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript">
	function query(){
		var termid =  document.getElementById("termid").value;
		var beginDate =  document.getElementById("beginDate").value;
		var endDate =  document.getElementById("endDate").value;
		var pageNum = document.getElementById("pageNum").value;
		 var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
		var actionUrl = "<%=request.getContextPath()%>/pages/analyse/tmlAction!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&termid="+termid+"&beginDate="+beginDate+"&endDate="+endDate;
		
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0 ; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	
	function setHTML(entry,entryInfo){
		var html = "";
		html += '<tr id="tr" class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td align="center">';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["termid"] + '" />';
		html += '</td>';
		html += '<td align="center">';
		html += '<a href=javascript:showTransInfo("' + entryInfo["termid"] + '")>' + entryInfo["termid"] + '</a>';
		html += '</td>';
		html += '<td align="center">' + entryInfo["count"] + '</td>';
		html += '</tr>';
		<% k++;%>;
		showdiv.style.display = "none";
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
	   		return ""
	 	return idList.substring(1)
	 }
	
	function SelAll(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox")
	    		em[i].checked=chkAll.checked;
		}
	}
	
	function showTransInfo(termid) {
		var beginDate =  document.getElementById("beginDate").value;
		var endDate =  document.getElementById("endDate").value;
		termid += "|" + beginDate + "|" + endDate;
		var strUrl="/pages/watch/tranAbnoinfo!list.action?termid="+termid;
		//alert(accountNo);
		var features="700,500,watch.list.term,transmgr";
		var resultvalue = OpenModal(strUrl,features);
	}  
	</script>
 
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="tmlForm"  namespace="/pages/analyse" action="/pages/analyse/tmlAction!list.action" method="post" >
 		<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend><s:text name="queryCondition.query"/></legend>
			<table width="100%">
			  <tr>
			  	<td align="center"><s:text name="page.infoTermid"/><s:text name="label.colon" /></td>
				<td><input name="termid" type="text" id="termid"  class="MyInput"> </td>
				<td align="center"><s:text name="watch.trans_time"/><s:text name="label.colon" /></td>
				<td><input name="beginDate"  type="text" id="beginDate" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > </td>
				<td align="center"> <s:text name="calendar.to"/></td>
				<td align="center"> <input name="endDate"  type="text" id="endDate" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > </td>
				<td align="right"> <tm:button site="1"/></td>
			  </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr>
		</table><br />
		<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				 <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
				 <td class="orarowhead"><s:text name="operInfo.title" /></td>
				 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
			<tr bgcolor="#FFFFFF">
				<td> 
					<table width="100%" cellSpacing="0" cellPadding="0">
					<tr>
						<td width="6%"> 
							<div align="center"> 
								<input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="SelAll(this)">
							</div> 
						</td>
						<td width="11%">
							<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
	 					</td>
	 					<td width="83%" align="right">
							<div id="pagetag"> <tm:pagetag pageName="currPage" formName="tmlForm" /></div>
						</td>
					</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr class="oracolumncenterheader"> 
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="label.analyse.termid" /></td>
				<td width="10%" style="cursor:hand"><s:text name="label.analyse.count" /></td>
			</tr>
			<tbody name="formlist" id="formlist">
				<s:iterator  value="tmlList" id="tmlAnalyse" status="row">
					<s:if test="#row.odd == true"> 
					 <tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if><s:else>
					<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else> 
						<td align="center">
							<input type="checkbox" name="chkList" value='<s:property value="termid" />' />
						</td>
						<td align="center">
							<a href='javascript:showTransInfo("<s:property value="termid"/>")'><s:property value="termid" /></a>
						</td>
						<td align="center"><s:property value="count" /></td>
					</tr>
				</s:iterator> 
 			</tbody> 
 		</table> 
 </s:form>
</body>
</html>

