<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>AuditInfo query</title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript"> 
	function query(){
		var applytyp = document.getElementById("applytyp").value;
		var auditStatus = document.getElementById("flowStatus").value;
		var beginDate =  document.getElementById("beginDate").value;
		var endDate =  document.getElementById("endDate").value;
		var flowStatus=document.getElementById("flowStatus").value;
		var pageNum = document.getElementById("pageNum").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/audit/auditInfo!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += '&applytyp=' + applytyp + '&auditStatus=' + auditStatus + '&beginDate=' + beginDate + '&endDate=' + endDate+'&flowStatus='+flowStatus;
		  actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		  <%int k = 0 ; %>
		   sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html = '';
		html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td>';
		//deleted by cjjie on 2010-12-17 html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["applyId"] + '</a>';
		html += entryInfo["applyId"];
		html += '</td>';
		html += '<td>' + entryInfo["applyName"] + '</td>';
		html += '<td>' + entryInfo["applayDate"] + '</td>';
		html += '<td>' + entryInfo["applytyp"] + '</td>';
		html += '<td>' + entryInfo["orgId"] + '</td>';
		//html += '<td>' + entryInfo["auditStatus"] + '</td>';
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
 		return idList.substring(1)
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
	 	var strUrl="/pages/audit/auditInfo!delete.action?ids="+idList;
	 	OpenModal(strUrl,"600,380,operInfo.delete,um")
	   	query();
	}     	
	function show(id){
		var strUrl="/pages/audit/auditInfo!show.action?ids="+id+"&menuid=<%=request.getParameter("menuid")%>";
		var features="600,500,audit.info,audit";
		var resultvalue = OpenModal(strUrl,features);
		query();
	}
	
	function auditing() {
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
		 	alert('<s:text name="audit.exam" />')
		 else if (j>1)
		 	alert('<s:text name="audit.selectone" />')
		else{
		  	var strUrl="/pages/audit/auditInfo!audit.action?ids="+itemId;
		  	var features="600,500,audit.auditTitle,audit";
			OpenModal(strUrl,features);
		    query();
		    //window.open("<%=request.getContextPath()%>/pages/audit/auditInfo!audit.action?ids=" + itemId);
		}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="auditInfoForm" namespace="/pages/audit" action="auditInfo!list.action" method="post" >
 	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
 	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 	  <tr>
 		<td>
		  <fieldset  width="100%">
			<legend><s:text name="queryCondition.query"/></legend>
			<table width="100%">
				<tr>
					<td><s:text name="audit.info.applyType" /><s:text name="label.colon" /></td>
					<td>
						<tm:tmSelect name="applytyp" id="applytyp" selType="dataDir" beforeOption="all"  path="ruleMgr.applyType" />
					</td>
					
					<td><s:text name="audit.info.auditStatus" /><s:text name="label.colon" /></td>
					<td>
						<tm:tmSelect name="flowStatus" id="flowStatus" selType="dataDir"   path="auditMgr.flowStatus" />
					</td>
				</tr>
				
				<tr>
					<td><s:text name="audit.info.applydate" /><s:text name="label.colon" /></td>
					<td>
						<input name="beginDate"  type="text" id="beginDate" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > 
					</td>
					
					<td> <s:text name="calendar.to"/></td>
					<td> 
						<input name="endDate"  type="text" id="endDate" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > 
					</td>
					
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
				  <div align="center"> 
					<input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="SelAll(this)">
				  </div> 
				</td>
				<td width="11%">
				  <div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 				</td>
 				<td width="83%" align="right">
				  <div id="pagetag"><tm:pagetag pageName="currPage" formName="auditInfoForm" /></div>
				</td>
			  </tr>
			</table>
		  </td>
		</tr>
	  </table>
	  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr class="oracolumncenterheader"> 
		  <td width="4%"><s:text name="label.select" /></td>
		  <td width="9%" style="cursor:hand"><s:text name="audit.info.applyId" /></td>
		  <td width="10%" style="cursor:hand"><s:text name="audit.info.creater" /></td>
		  <td width="15%" style="cursor:hand"><s:text name="audit.info.applydate" /></td>
		  <td width="8%" style="cursor:hand"><s:text name="audit.info.applyType" /></td>
		  <td width="15%" style="cursor:hand"><s:text name="audit.info.orgid" /></td>
		 <!--   <td width="10%" style="cursor:hand"><s:text name="audit.info.auditStatus" /></td>-->
		</tr>
   		<%int index = 0;%>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="auditInfoList" id="auditInfo">
  		  <tr class="trClass<%=index%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)  align="center">
			<td>
			   <input type="checkbox" name="chkList" value='<s:property value="id" />' />
			</td>
			<!-- deleted by cjjie on 2010-12-17 <td>
			   <a href='javascript:show("<s:property value="id"/>")'><s:property value="applyId" /></a>
			</td>-->
			<td><s:property value="applyId"/></td>
			<td><s:property value="applyName" /></td>
			<td><s:property value="applayDate" /></td>
			<td><tm:dataDir beanName="auditInfo" property="applytyp" path="ruleMgr.applyType" /></td>
			<td><tm:dataMap name="auditInfo" property="orgId" hashMap="orgInfoMap" type="oscache" /></td>
			<!--  <td><tm:dataDir beanName="auditInfo" property="auditStatus" path="auditMgr.flowStatus" /></td>-->
		  </tr>
		  <% index++;%>
		</s:iterator>  
 		</tbody> 
 	  </table> 
</s:form>
</body>
</html>

