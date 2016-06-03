<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Map,cn.grgbanking.feeltm.config.BusnDataDir"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%><%@ taglib
	uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
	<head>
		<title>TranAbnoinfo query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" src="../../js/cookie.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script language="JavaScript" type="text/javascript"> 
function query(){
var pageNum = document.getElementById("pageNum").value;
var accountNo = document.getElementById("accountNo").value;
var beginDate = document.getElementById("beginDate").value;
var endDate = document.getElementById("endDate").value;
var reg1 = /^[0-9]+$/;
//var reg2 = /^[A-Za-z0-9]+$/;
if(!reg1.test(accountNo)&&(accountNo.length>0)){
	alert('<s:text name="check.accountNoFormat"/>');
	return;
}

var actionUrl = "<%=request.getContextPath()%>/pages/watch/tranAbnoinfo!refresh.action?from=refresh"+"&pageNum="+pageNum+"&accountNo="+accountNo+"&beginDate="+beginDate+"&endDate="+endDate;
  actionUrl=encodeURI(actionUrl);
var method="setHTML";
  <%int k = 0;%>
   sendAjaxRequest(actionUrl,method,pageNum,true);
    }


function setHTML(entry,entryInfo){
var html = "";
html += '<tr id="tr" class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
html += '<td nowrap align="center"><input type="checkbox" name="id" value="' + entryInfo["id"] + '" /></td>';
html += '<td nowrap width="12%"><div align="center">';
	html += '<a href=javascript:show("' + entryInfo["tranId"] + '")>'; 
		html += entryInfo["transCode"];
		html += '</a>';
	html += '</div></td>';
	html += '<td nowrap width="17%"><div align="center">' + entryInfo["accountNo"] + '</div></td>';
	html += '<td nowrap width="20%"><div align="center">';
		html += entryInfo["regulation"];
html += '</div></td>';
html += '<td nowrap width="10%"><div align="center">' + entryInfo["blackNotenum"] + '</div></td>';
html += '<td nowrap width="10%"><div align="center">' + entryInfo["repeatNotenum"] + '</div></td>';
html += '<td nowrap width="10%"><div align="center">' + entryInfo["callbackNotenum"] + '</div></td>';
html += '<td nowrap width="17%"><div align="center">' + entryInfo["transTime"] + '</div></td>';
	html += '</tr>';
 <%k++;%>;
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
 return idList.substring(1)//delete autoRefersh
 }
function  selectAll(chkAll){
 var   em=document.all.tags("input");
 var flag =1;
 
  for(var  i=0;i<em.length;i++){
  if(em[i].type=="checkbox")
    if(flag!=1){
    	em[i].checked=chkAll.checked;
    }
  flag++;
}
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
		<s:form name="tranAbnoinfoForm" namespace="/pages/watch"
			action="tranAbnoinfo!list.action" method="post">
			<input type="hidden" name="pageNum" id="pageNum" value="1" />
			<%@include file="/inc/navigationBar.inc"%>
			<input type="hidden" name="menuid"
				value="<%=request.getParameter("menuid")%>">
			<table width="100%" cellSpacing="0" cellPadding="0"
				class="selectTableBackground">
				<tr>
					<td>
						<fieldset width="100%">
							<legend>
								<s:text name="queryCondition.query" />
							</legend>
							<table width="100%">
								<tr>
									<td align="center">
										<s:text name="watch.trans_time" />
										<s:text name="label.colon" />
									</td>
									<td>
										<input name="beginDate" type="text" id="beginDate" size="22"
											class="MyInput" isSel="true" isDate="true"
											onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)">
									</td>
									<td align="center">
										<s:text name="calendar.to" />
									</td>
									<td align="center">
										<input name="endDate" type="text" id="endDate" size="22"
											class="MyInput" isSel="true" isDate="true"
											onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)">
									</td>
									<td align="center">
										<s:text name="watch.account_no" />
										<s:text name="label.colon" />
									</td>
									<td>
										<input name="accountNo" type="text" id="accountNo"
											class="MyInput">
									</td>
									<td align="right">
										<tm:button site="1" />
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<br>
			<table width="100%" height="23" border="0" cellspacing="0"
				cellpadding="0" class="bgbuttonselect">
				<tr>
					<td width="25" height="23" valign="middle">
						&nbsp;
						<img src="../../images/share/list.gif" width="14" height="16">
					</td>
					<td class="orarowhead">
						<s:text name="operInfo.title" />
					</td>
					<td align="right" width="75%">
						<tm:button site="2"></tm:button>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				bgcolor="#000066">
				<tr bgcolor="#FFFFFF">
					<td>
						<table width="100%" cellSpacing="0" cellPadding="0">
							<tr>
								<td width="6%">
									<div align="center">
										<input type="checkbox" name="all" id="chkAll" value="all"
											onClick="selectAll(this)">
									</div>
								</td>
								<td width="11%">
									<div align="left">
										<label for=chkAll>
											<s:text name="operInfo.checkall" />
										</label>
									</div>
								</td>
								<td width="83%" align="right">
									<div id="pagetag">
										<tm:pagetag pageName="currPage" formName="tranAbnoinfoForm" />
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				bgcolor="#000066" id="downloadList">
				<tr>
					<td nowrap width="4%" class="oracolumncenterheader">
						<s:text name="label.select" />
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.trans_code" />
						</div>
					</td>
					<td nowrap width="17%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.account_no" />
						</div>
					</td>
					<td nowrap width="20%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.regulation" />
						</div>
					</td>
					<td nowrap width="10%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.blacklist_count" />
						</div>
					</td>
					<td nowrap width="10%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.repeat_count" />
						</div>
					</td>
					<td nowrap width="10%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.callback_count" />
						</div>
					</td>
					<td nowrap width="17%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="watch.trans_time" />
						</div>
					</td>
				</tr>
				<%
					int index = 0;
				%>
				<tbody name="formlist" id="formlist">
					<s:iterator value="tranAbnoinfoList" id="tranAbnoinfo">
						<tr id="tr" class="trClass<%=index % 2%>" oriClass=""
							onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
							<td nowrap align="center">
								<input type="checkbox" name="id"
									value='<s:property value="id"/>' />
							</td>
							<td nowrap width="12%">
								<div align="center">
									<a href='javascript:show("<s:property value="tranId"/>")'> <s:property
											value="transCode" /> </a>
								</div>
							</td>
							
							<td nowrap width="17%">
								<div align="center">
									<s:property value="accountNo" />
								</div>
							</td>
							<td nowrap width="20%">
								<div align="center">
									<s:property value="regulation"/>
								</div>
							</td>
							<td nowrap width="10%">
								<div align="center">
									<s:property value="blackNotenum" />
								</div>
							</td>
							<td nowrap width="10%">
								<div align="center">
									<s:property value="repeatNotenum" />
								</div>
							</td>
							<td nowrap width="10%">
								<div align="center">
									<s:property value="callbackNotenum" />
								</div>
							</td>
							<td nowrap width="17%">
								<div align="center">
									<s:property value="transTime" />
								</div>
							</td>
						</tr>
						<%
							index++;
						%>
					</s:iterator>
				</tbody>
			</table>
		</s:form>
	</body>
</html>