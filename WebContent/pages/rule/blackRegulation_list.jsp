<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
	<head><title><s:text name="rule.title.blacklist" /></title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
		function query(){
			var regulation =  document.getElementById("regulation").value;
			var moneyType =  document.getElementById("moneyType").value;
			var reguStatus =  document.getElementById("reguStatus").value;
			
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/rule/blackRegulation!refresh.action?from=refresh";
			actionUrl += '&pageNum=' + pageNum + '&regulation=' + regulation + '&moneyType=' + moneyType + '&reguStatus=' + reguStatus;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
			sendAjaxRequest(actionUrl,method,pageNum,true);
		}
	
		function setHTML(entry,entryInfo){
			var html = "";
			html += '<tr align="center" id="tr" class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>'; 
				html += '<td align="center">';
					html += '<input type="checkbox" name="chkList" value="' + entryInfo["applyId"] + '" />';
				html += '</td>';
				html += '<td>';
					html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["regulation"] + '</a>';
				html += '</td>';
				html += '<td>' + entryInfo["moneyType"] + '</td>';
				html += '<td>' + entryInfo["moneyDenomination"] + '</td>';
				//html += '<td>' + entryInfo["regulationStatus"] + '</td>';
				html += '<td>' + entryInfo["reguStatus"] + '</td>';
				html += '<td>' + entryInfo["createDate"].substring(0, entryInfo["createDate"].length - 2) + '</td>';
				html += '<td>' + entryInfo["reversionDate"].substring(0, entryInfo["reversionDate"].length - 2) + '</td>';
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
	 			return idList.substring(5) //去掉"全选"
			 return idList.substring(1)
		}
		
		function  SelAll(chkAll){
		 	var em=document.all.tags("input");
		  	for(var  i=0;i<em.length;i++){
		  		if(em[i].type=="checkbox")
		    		em[i].checked=chkAll.checked
			}
		}
		
		function del() {
			var idList=GetSelIds();
			//alert(idList);
		  	if(idList=="") {
			  	alert('<s:text name="rule.errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="rule.del.confirm" />')) {
			 	var strUrl="/pages/rule/blackRegulation!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,rule.delete,rule");
			   	query();
			}
		}
		
		function add(){
			var resultvalue = OpenModal('/pages/rule/blackRegulation!add.action','600,400,rule.addTitle,rule');
			if(resultvalue!=null)
		  		query();
		}
		
		function show(id){
			var strUrl="/pages/rule/blackRegulation!show.action?ids="+id+"&menuid=<%=request.getParameter("menuid")%>";
			var features="600,500,rule.title.info,rule";
			var resultvalue = OpenModal(strUrl,features);
	  		if(resultvalue!=null)
				query();
		 }

		function cancleRegu() {
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
			 	alert('<s:text name="rule.select" />');
			 else if (j>1)
			 	alert('<s:text name="rule.selectone" />');
			else{
			  	var strUrl="/pages/rule/regulationDeliver!cancleRegu.action?applyId="+itemId;
			  	var features="600,500,rule.cancleTitle,rule";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}

		function regulationDeliver() {
			//alert("in");
			var idList=GetSelIds();
			if(idList=="") {
			  	alert('<s:text name="请选择要下发的规则！" />');
				return false;
		  	}
			var strUrl="/pages/rule/regulationDeliver!deliver.action?applyIds="+idList;
			var features="600,500,rule.deliverTitle,rule";
			var resultvalue = OpenModal(strUrl,features);
			query();
		}  
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="blackRegulationForm"  namespace="/pages/rule" action="/pages/rule/blackRegulation!query.action" method="post" >
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
					<td width="10%" align="right"><s:text name="rule.list.regulation" /><s:text name="label.colon" /></td>
					<td width="10%"><input name="regulation" type="text" id="regulation"  class="MyInput"> </td>
					<td width="10%" align="right"> <s:text name="rule.list.moneyType"/><s:text name="label.colon"/></td>
					<td width="10%">
						<tm:tmSelect name="moneyType" id="moneyType" selType="dataDir" beforeOption="all" path="ruleMgr.moneyType" />
					</td>
					<td width="10%" align="right"> <s:text name="rule.list.status"/><s:text name="label.colon"/></td>
					<td width="10%">
						<tm:tmSelect name="reguStatus" id="reguStatus" selType="dataDir" defaultValue="1" beforeOption="all" path="ruleMgr.reguDeliverStatus" />
					</td>
					<td width="10%" align="right">&nbsp;</td>
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
 				<td	align="right" width="75%"><tm:button site="2"></tm:button></td>
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
							<div align="left">
								<label for=chkAll><s:text name="operInfo.checkall" /></label>
							</div>
 						</td>
 						<td width="83%" align="right">
							<div id="pagetag"> 
								<tm:pagetag pageName="currPage" formName="blackRegulationForm" />
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
			<tr class="oracolumncenterheader"> 
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="rule.list.regulation" /></td>
				<td width="10%" style="cursor:hand"><s:text name="rule.list.moneyType" /></td>
				<td width="8%" style="cursor:hand"><s:text name="rule.list.denomination" /></td>
				<!-- <td width="11%" style="cursor:hand"><s:text name="rule.list.status" /></td> -->
				<td width="11%" style="cursor:hand"><s:text name="rule.reguStatus" /></td>
				<td width="15%" style="cursor:hand"><s:text name="rule.list.date" /></td>
				<td width="10%" style="cursor:hand"><s:text name="rule.list.reversion" /></td>
			</tr>
   			<%int index = 0;%>
			<tbody name="formlist" id="formlist"> 
  			<s:iterator  value="blackRegulationList" id="blackRegulation" > 
 			<tr align="center" class="trClass<%=index%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
				<td align="center">
					<input type="checkbox" name="chkList" value='<s:property value="applyId" />' />
				</td>
				<td>
					<a href='javascript:show("<s:property value="id"/>")'><s:property value="regulation" /></a>
				</td>
				<td><tm:dataDir beanName="blackRegulation" property="moneyType" path="ruleMgr.moneyType" /></td>
				<td><tm:dataDir beanName="blackRegulation" property="moneyDenomination" path="ruleMgr.moneyDenomination" /></td>
				<!-- <td><tm:dataDir beanName="blackRegulation" property="regulationStatus" path="ruleMgr.blMgr" /></td> -->
				<td><tm:dataDir beanName="blackRegulation" property="reguStatus" path="ruleMgr.reguDeliverStatus" /></td>
				<td><s:date name="createDate" format="yyyy-MM-dd HH:mm:ss" /></td>
				<td><s:date name="reversionDate" format="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<% index++;%>
			</s:iterator>  
 			</tbody> 
 		</table> 
 </s:form>
</body>
</html>

