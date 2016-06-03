<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>TranInfo query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
		function query(){
			var transResult =  document.getElementById("transResult").value;
			var accountNo =  document.getElementById("accountNo").value;
			var beginDate =  document.getElementById("beginDate").value;
			var endDate =  document.getElementById("endDate").value;
			var journalNo =  document.getElementById("journalNo").value;
			var pageNum = document.getElementById("pageNum").value;
			var reg1 = /^[0-9]+$/;
			var reg2 = /^[A-Za-z0-9]+$/;
			if(!reg1.test(accountNo)&&(accountNo.length>0)){
				alert('<s:text name="check.accountNoFormat"/>');
				return;
			}
			if(!reg2.test(journalNo)&&(journalNo.length>0)){
				alert('<s:text name="check.journalNoFormat"/>');
				return;
			}
			var actionUrl = "<%=request.getContextPath()%>/pages/transmgr/TransMgr!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&transResult="+transResult+"&accountNo="+accountNo+"&beginDate="+beginDate+"&endDate="+endDate+"&journalNo="+journalNo;
			
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
			sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = "";
			html += '<tr id="tr" class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td nowrap align="center"><input type="checkbox" name="id" value="' + entryInfo["id"] + '" /></td>';
			html += '<td nowrap width="12%"><div align="center">';
				html += '<a href=javascript:show("' + entryInfo["id"] + '")>'; 
					html += entryInfo["transCode"];
		  		html += '</a>';
		  	html += '</div></td>';
		 	html += '<td nowrap width="17%"><div align="center">' + entryInfo["accountNo"] + '</div></td>';
		 	html += '<td nowrap width="10%"><div align="center">';
		 	if(entryInfo["transResult"] == "交易成功")
			 	html += '<font color="#000000">';
			else
				html += '<font color="#FF0000">';
		 	html += entryInfo["transResult"] + '</font>';
			html += '</div></td>';
		  	html += '<td nowrap width="10%"><div align="center">' + entryInfo["noteNum"] + '</div></td>';
		    html += '<td nowrap width="10%"><div align="center">' + entryInfo["blNum"] + '</div></td>';
		    html += '<td nowrap width="10%"><div align="center">' + entryInfo["repeatNum"] + '</div></td>';
		    html += '<td nowrap width="10%"><div align="center">' + entryInfo["callBackNum"] + '</div></td>';
		    html += '<td nowrap width="17%"><div align="center">' + entryInfo["transTime"] + '</div></td>';
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
			var   em=document.all.tags("input");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}
				
		function show(id) {
			var strUrl="/pages/transmgr/TransMgr!show.action?ids="+id;
			var features="650,700,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="tranInfoForm"  namespace="/pages/TransMgr" action="TransMgr!list.action" method="post" >
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
				<td align="center"><s:text name="watch.trans_result"/><s:text name="label.colon" /></td>
				<td>
				  <tm:tmSelect name="transResult" id="transResult" selType="dataDir" beforeOption="all"  path="transMgr.transResult" />
				</td>
				<td align="center"><s:text name="watch.account_no"/><s:text name="label.colon" /></td>
				<td><input name="accountNo" type="text" id="accountNo"  class="MyInput"> </td>
				<td align="right"><s:text name="watch.journal_no"/><s:text name="label.colon" /></td>
				<td><input name="journalNo" type="text" id="journalNo"  class="MyInput"></td>
			  </tr> 
			  <tr>
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
						<div id="pagetag"> <tm:pagetag pageName="currPage" formName="tranInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="4%" class="oracolumncenterheader"><s:text name="label.select" /></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.trans_code" /></div></td>
			<td nowrap width="17%" class="oracolumncenterheader"><div align="center"><s:text name="watch.account_no" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.trans_result" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.note_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.blacklist_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.repeat_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.callback_count" /></div></td>
			<td nowrap width="17%" class="oracolumncenterheader">
				<div align="center"><s:text name="watch.trans_time" /></div>
			</td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="tranInfoList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="id" value='<s:property value="id"/>'/></td>
			<td nowrap width="12%"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'> 
					<s:property value="transCode"/>
		  		</a>
		  	</div></td>
		 	<td nowrap width="17%"><div align="center"><s:property value="accountNo"/></div></td>
		 	<td nowrap width="10%"><div align="center">
		 		<s:if test='transResult == "0"'>
		 			<font color="#000000">
		 		</s:if><s:else>
		 			<font color="#FF0000">
		 		</s:else>
		 		<tm:dataDir beanName="tranInfo" property="transResult" path="transMgr.transResult" scope="request"/>
			</font></div></td>
		  	<td nowrap width="10%"><div align="center"><s:property value="noteNum"/></div></td>
		    <td nowrap width="10%"><div align="center"><s:property value="blNum" /></div></td>
		    <td nowrap width="10%"><div align="center"><s:property value="repeatNum" /></div></td>
		    <td nowrap width="10%"><div align="center"><s:property value="callBackNum" /></div></td>
		    <td nowrap width="17%"><div align="center"><s:property value="transTime" /></div></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

