<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
	<head>
		<title>TmlInfo query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
	function query(){
		var pageNum = document.getElementById("pageNum").value;
		var termid = document.getElementById("termid").value;
		var termtype = document.getElementById("termtype").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/tmlInfo/tmlInfo!refresh.action?from=refresh&pageNum="+pageNum+"&termid="+termid+"&termtype="+termtype;
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
	  	<%int k = 0 ; %>
	   	sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	
	
	function setHTML(entry,entryInfo){
		var html = "";
		html += '<tr class="trClass<%=(k%2)%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)">';
		html += '<td align="center">';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '"';
		html += '</td>';
		html += '<td><a href=javascript:showTmlInfo("' + entryInfo["id"] + '")>' + entryInfo["termid"] + '</a></td>';
		html += '<td>' + entryInfo["termtype"] + '</td>';
		html += '<td>' + entryInfo["brand"] + '</td>';
		html += '<td>' + entryInfo["netaddr"] + '</td>';
		html += '<td>' + entryInfo["orgid"] + '</td>';
		//html += '<td>' + entryInfo["areaid"] + '</td>';
		html += '<td>' + entryInfo["termaddress"] + '</td>';
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
	   	if(document.getElementById("chkAll").checked)
	   		return idList.substring(5)//去掉"全选"
	 	return idList.substring(1)
	 }
	
	function SelAll(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox")
	    		em[i].checked=chkAll.checked;
		}
	}
	
	function del() {
		var idList=GetSelIds();
	  	if(idList=="") {
		  	alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
	  	}
		if(confirm('<s:text name="tmlInfo.del.confirm" />')) {
		 	var strUrl="/pages/tmlInfo/tmlInfo!delete.action?ids="+idList;
		 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
		   	query();
		}
	}

	function modify(){
		var aa=document.getElementsByName("chkList");
		var itemId
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
		  	var strUrl="/pages/tmlInfo/tmlInfo!edit.action?ids="+itemId;
		  	var features="600,500,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
		    query();
		}
	}
		
	function add(){
		var resultvalue = OpenModal('/pages/tmlInfo/tmlInfo!add.action','650,400,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}

	function showTmlInfo(id) {
		var strUrl="/pages/tmlInfo/tmlInfo!show.action?id="+id+"&menuid=<%=request.getParameter("menuid")%>";
		var features="600,500,tmlInfo.viewTmlInfo,tmlInfo";
		var resultvalue = OpenModal(strUrl,features);
	  	if(resultvalue!=null)
			query();
	}  
	</script>
 
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="tmlInfoForm"  namespace="/pages/tmlInfo" action="/pages/tmlInfo/tmlInfo!list.action" method="post" >
  		<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 		<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 			<tr>
 				<td>
					<fieldset>
						<legend><s:text name="queryCondition.query"/></legend>
						<table width="100%">
						<tr>
							<td width="15%" align="right"><s:text name="page.infoTermid" /><s:text name="label.colon" /></td>
							<td width="15%"><input name="termid" type="text" id="termid"  class="MyInput"> </td>
							<td width="15%" align="right"> <s:text name="page.infoTermtype"/><s:text name="label.colon"/></td>
							<td width="15%">
								<tm:tmSelect name="termtype" id="termtype" selType="dataDir" beforeOption="all" path="tmlMgr.termType" />
							</td>
							<td width="15%" align="right">&nbsp;</td>
							<td width="15%" align="right">&nbsp;</td>
							<td width="15%" align="right"> <tm:button site="1"/></td>
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
						<div align="center"><input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>
 					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="tmlInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr class="oracolumncenterheader"> 
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="page.infoTermid" /></td>
				<td width="10%" style="cursor:hand"><s:text name="page.infoTermtype" /></td>
				<td width="8%" style="cursor:hand"><s:text name="page.infoBrand" /></td>
				<td width="11%" style="cursor:hand"><s:text name="page.netaddr" /></td>
				<td width="15%" style="cursor:hand"><s:text name="page.infoOrgid" /></td>
				<!--deleted by cjjie on 2010-12-15 <td width="10%" style="cursor:hand"><s:text name="page.infoAreaid" /></td>-->
				<td width="29%" style="cursor:hand"><s:text name="model.equipmentAddress" /></td>
			</tr>
   			<%int index = 0;%>
			<tbody name="formlist" id="formlist"> 
		  <s:iterator  value="tmlInfoList" id="tmlInfo" > 
			 <tr id="tr" class="trClass<%=index%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
				<td align="center">
					<input type="checkbox" name="chkList" value='<s:property value="id" />' />
				</td>
				<td>
					<a href='javascript:showTmlInfo("<s:property value="id"/>")'><s:property value="termid" /></a>
				</td>
				<td><tm:dataDir beanName="tmlInfo" property="termtype" path="tmlMgr.termType" /></td>
				<td>
					<tm:dataDir beanName="tmlInfo" property="brand" path="tmlMgr.brand" />
				</td>
				<td><s:property value="netaddr" /></td>
				<!--  <td><s:property value="orgid" /></td>-->
				<td><tm:dataMap name="tmlInfo" property="orgid" hashMap="orgInfoMap" type="oscache" /></td>
				<!--deleted by cjjie on 2010-12-15 <td><s:property value="areaid" /></td>-->
				<td><s:property value="termaddress" /></td>
			</tr>
		 <% index++;%> 
		</s:iterator>  
 			</tbody> 
 		</table> 
 </s:form>
</body>
</html>

