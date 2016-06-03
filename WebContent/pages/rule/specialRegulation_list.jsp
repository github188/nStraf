<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%><%@ taglib
	uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
	<head>
		<title>SpecialRegulation query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script language="javascript" src="../../js/tablesort.js"></script>
	<script language="javascript" src="../../calendar/fixDate.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script language="JavaScript" type="text/javascript"> 
function query(){
var pageNum = document.getElementById("pageNum").value;
var source = document.getElementById("source").value;
var actionUrl = "<%=request.getContextPath()%>/pages/rule/specialRegulation!refresh.action?from=refresh"
		+"&pageNum="+pageNum+"&source="+source;
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
html += '<a href=javascript:show("' + entryInfo["id"] + '")>'; 
	html += entryInfo["regulation"];
	html += '</a>';
html += '</div></td>';
html += '<td nowrap width="12%"><div align="center">' + entryInfo["source"] + '</div></td>';
html += '<td nowrap width="12%"><div align="center">' + entryInfo["result"] + '</div></td>';
html += '<td nowrap width="12%"><div align="center">' + entryInfo["createDate"] + '</div></td>';
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
 return idList.substring(1)
 }
function  SelAll(chkAll){
 var   em=document.all.tags("input");
  for(var i=0;i<em.length;i++){
  if(em[i].type=="checkbox")
    em[i].checked=chkAll.checked
}
  }
 function del(){
	 var idList=GetSelIds();
	 //alert(idList);
	 //return false;
	 if(idList=="") {
		  	alert('<s:text name="rule.errorMsg.notInputDelete" />');
			return false;
	  	}
	 if(confirm('<s:text name="rule.del.confirm" />')) {
 var strUrl="/pages/rule/specialRegulation!delete.action?ids="+idList;
 var returnValue=OpenModal(strUrl,"600,380,rule.delete,rule")
   query();
	 }
}     
 
function modify(){
var all = GetSelIds();
if(all==""){
	alert('<s:text name="rule.mustSelect"/>');
	return false;
}
var aa = all.split(",");
if(aa.length>1){
	alert('<s:text name="rule.mustSelect"/>');
	return false;
}
var ids = aa[0];
var strUrl="/pages/rule/specialRegulation!edit.action?ids="+ids;
var features="600,500,rule.modifyTitle,rule";
var resultvalue = OpenModal(strUrl,features);
 query();
}
  
function add(){
var resultvalue = OpenModal('/pages/rule/specialRegulation!add.action','650,550,rule.addTitle,rule');
//if(resultvalue!=null)
  query();
}
function show(id){
	var strUrl="/pages/rule/specialRegulation!show.action?ids="+id+"&menuid=<%=request.getParameter("menuid")%>";
	var features="500,350,rule.title.info,rule";
	var resultvalue = OpenModal(strUrl,features);
	if(resultvalue!=null)
		query();
}

</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
		<s:form name="specialRegulationForm" namespace="/pages/rule"
			action="specialRegulation!list.action" method="post">
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
									<td align="right">
										<s:text name="rule.source" />
										<s:text name="label.colon" />
									</td>
									<td>
										<tm:tmSelect name="source" id="source" selType="dataDir"
											beforeOption="all" path="transMgr.cometype" />
									</td>
									<td>
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
											onClick="SelAll(this)">
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
										<tm:pagetag pageName="currPage"
											formName="specialRegulationForm" />
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
						<s:text name="operInfo.checkall" />
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.regulation" />
						</div>
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.source" />
						</div>
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.result" />
						</div>
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.createDate" />
						</div>
					</td>
				</tr>
				<%
					int index = 0;
				%>
				<tbody name="formlist" id="formlist">
					<s:iterator value="specialRegulationList" id="specialRegulation">
						<tr id="tr" class="trClass<%=index % 2%>" oriClass=""
							onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
							<td width="4%" nowrap align="center">
								<input type="checkbox" name="id"
									value='<s:property value="id"/>' />
							</td>
							<td nowrap width="12%">
								<div align="center">
									<a href='javascript:show("<s:property value="id"/>")'> <s:property
											value="regulation" /> </a>
								</div>
							</td>
							<td nowrap width="12%">
								<div align="center">
									<tm:dataDir beanName="specialRegulation" property="source"
										path="transMgr.cometype" />
								</div>
							</td>
							<td nowrap width="12%">
								<div align="center">
									<s:property value="result" />
								</div>
							</td>
							<td nowrap width="12%">
								<div align="center">
									<s:property value="createDate" />
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

