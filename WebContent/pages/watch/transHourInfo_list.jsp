<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Map,cn.grgbanking.feeltm.config.BusnDataDir" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>TransHourInfo query</title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/cookie.js"></script>
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
	var actionUrl = "<%=request.getContextPath()%>/pages/watch/transHourInfo!refresh.action?from=refresh&pageNum="+pageNum;
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
   return ""
 return idList.substring(14)//去掉"自动刷新"和"全选"
 }
function  SelAll(chkAll){
 var   em=document.all.tags("input");
  for(var  i=0;i<em.length;i++){
  if(em[i].type=="checkbox")
    em[i].checked=chkAll.checked
}
  }
 function del(){
  var idList=GetSelIds();
  if(idList==""){return false}
 var strUrl="/pages/transHourInfo/watch!delete.action?ids="+idList;
 var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
   query();
}     
 
function modify(){
  var aa=document.getElementsByName("id");
 var ids;
 var j=0;
 for (var i=0; i<aa.length; i++){if (aa[i].checked){
ids=aa[i].value.split(",")[0];
j=j+1; 
} } 
 if (j==0) 
  alert('please select ')
else{
var strUrl="/pages/transHourInfo/watch!edit.action?ids="+ids;
var features="600,500,operInfo.updateTitle,um";
var resultvalue = OpenModal(strUrl,features);
 query();
}
 }  
function add(){
var resultvalue = OpenModal('/pages/watch/transHourInfo!add.action','750,650,roleInfo.addOperTitle,um');
if(resultvalue!=null)
  query();
}
function show(id){
	var strUrl="/pages/watch/transHourInfo!show.action?ids="+id;
	var features="650,700,transmgr.traninfo,watch";
	var resultvalue = OpenModal(strUrl,features);
 }  

function selectAll(chkAll)
{
	  var   em=document.all.tags("input");
	  var flag = 1;
	  for(var  i=0;i<em.length;i++){
		  if(em[i].type=="checkbox")
		  {
		  	if(flag !=1)
		    	em[i].checked=chkAll.checked
		    
		    flag++;
		    }
	  }
}
</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="transHourInfoForm"  namespace="/pages/transHourInfo" action="transHourInfo!list.action" method="post" >
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
	</table>
	<br />
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
		<tr>
		 <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
		 <td class="orarowhead"><s:text name="operInfo.title" /></td>
		 <td> <input name="autorefersh" type="checkbox" id="autorefersh" value="checkbox" checked onClick="showMiniSelect()">
		     <label for="autorefersh"><s:text name="watch.refresh"/></label>
		     <select name="minites" id="minites" style="visibility:visible">
		     	<option>15秒</option>
		     	<option>30秒</option>
		     	<option>45秒</option>
		     	<option>60秒</option>
		     	<option>75秒</option>
		     	<option>90秒</option>
		     </select>
		</td>
		 <td align="right" >
		 <tm:button site="2"></tm:button>
		</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
	<tr bgcolor="#FFFFFF">
	<td> 
	  <table width="100%" cellSpacing="0" cellPadding="0">
	    <tr>
		  <td width="6%">
		    <div align="center"><input type="checkbox" name="all"  id="chkAll" value="all"  onClick="selectAll(this)"></div>
		  </td>
	      <td width="11%">
			<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
	 	  </td>
	 	  <td width="83%" align="right">
			<div id="pagetag"> <tm:pagetag pageName="currPage" formName="transHourInfoForm" /></div>
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
	<%int index = 0;%>
	<tbody name="formlist" id="formlist"> 
	  <s:iterator  value="transHourInfoList" id="transHourInfo" > 
	  <tr id="tr" class="trClass<%=index%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
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
	 		<tm:dataDir beanName="transHourInfo" property="transResult" path="transMgr.transResult" scope="request"/>
		</font></div></td>
	  	<td nowrap width="10%"><div align="center"><s:property value="noteNum"/></div></td>
	    <td nowrap width="10%"><div align="center"><s:property value="blNum" /></div></td>
	    <td nowrap width="10%"><div align="center"><s:property value="repeatNum" /></div></td>
	    <td nowrap width="10%"><div align="center"><s:property value="callBackNum" /></div></td>
	    <td nowrap width="17%"><div align="center"><s:property value="transTime" /></div></td>
	  </tr>
	 <% index++;%> 
	</s:iterator>  
	</tbody> 
	</table> 
 </s:form>
</body>
</html>
<script type="text/javascript">
var reftimes=GetCookie('reftimes');
if (reftimes==null){
	SetCookie('reftimes',15);
	reftimes=15
}

function  FirstDoWatch(){
	//setTimeout("DoWatch()",reftimes*1000);
	setInterval("DoWatch()",reftimes*1000);
}

function DoWatch(){
	if (document.getElementById("autorefersh").checked)
	{
		reftimes = (document.getElementById("minites").selectedIndex+1)*15;
		query();
		
	}
}

function showMiniSelect()
{
	if (document.getElementById("autorefersh").checked)
		document.getElementById("minites").style.visibility="visible";
	else
		document.getElementById("minites").style.visibility="hidden";	
}
</script>
<script type="text/javascript">	
if (document.getElementById("autorefersh").checked){
	FirstDoWatch()
}
</script>
