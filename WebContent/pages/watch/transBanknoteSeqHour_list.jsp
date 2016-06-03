<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>TransBanknoteSeqHour query</title></head>
<link href="../../css/css_v2.css" type="text/css" rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/cookie.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript"> 
	function query(){
		var pageNum = document.getElementById("pageNum").value;
		var beginDate = document.getElementById("beginDate").value;
		var endDate = document.getElementById("endDate").value;
		var seriaNo = document.getElementById("seriaNo").value;
		//var reg1 = /^[0-9]+$/;
		var reg2 = /^[A-Za-z0-9]+$/;
		if(!reg2.test(seriaNo)&&(seriaNo.length>0)){
			alert('<s:text name="check.serialNoFormat"/>');
			return false;
		}
		
		var actionUrl = "<%=request.getContextPath()%>/pages/watch/transBanknoteSeqHour!refresh.action?from=refresh&pageNum="+pageNum+"&beginDate="+beginDate+"&endDate="+endDate+"&seriaNo="+seriaNo;
	  
	  	actionUrl=encodeURI(actionUrl);
		var method="setHTML";
	  	<%int k = 0 ; %>
	   sendAjaxRequest(actionUrl,method,pageNum,true);
	}
	
	
	function setHTML(entry,entryInfo){
		var html = "";
			html+='<tr id="'+entryInfo['id']+'" class="trClass<%=(k%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td nowrap align="center"><input type="checkbox" name="id" value="' + entryInfo["id"] + '" /></td>';
			html += '<td nowrap width="12%"><div align="center">';
				html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["seriaNo"] + '</a>'; 
		  	html += '</div></td>';
			html += '<td nowrap width="12%"><div align="center">' + entryInfo["noteFlag"] + '</div></td>';
			html += '<td nowrap width="12%"><div align="center">';
			if(entryInfo["noteType"] == "真钞")
				html += '<font color="#00AA00">';
			else if(entryInfo["noteType"] == "重号")
				html += '<font color="#FF9900">';
			else if(entryInfo["noteType"] == "假钞")
				html += '<font color="#FF0000">';
			else
				html += '<font color="#0000AA">';
			html += entryInfo["noteType"] + '</font>';
			html += '</div></td>';
			html += '<td nowrap width="12%"><div align="center">';
				html += entryInfo["denomination"];
			html += '</div></td>';
			html += '<td nowrap width="12%"><div align="center">' + entryInfo["termid"] + '</div></td>';
			html += '<td nowrap width="12%"><div align="center">' + entryInfo["tranTime"] + '</div></td>';
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
	 	return idList.substring(14)//去掉"自动刷新"和"全选"
	else
		return idList.substring(10)//去掉"自动刷新"和"全选"
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
		 if(idList==""){
		 	return false;
		 }
		 var strUrl="/pages/watch/transBanknoteSeqHour!delete.action?ids="+idList;
		 var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
		   query();
	}     
	 
	function modify(){
		 var aa=document.getElementsByName("id");
		 var ids;
		 var j=0;
		 for (var i=0; i<aa.length; i++){
			 if (aa[i].checked){
			ids=aa[i].value.split(",")[0];
			j=j+1; 
			} 
		 } 
		 if (j==0) 
		  alert('请选择要修改的记录 ')
		 else{
			var strUrl="/pages/watch/transBanknoteSeqHour!edit.action?ids="+ids;
			var features="600,500,operInfo.updateTitle,um";
			var resultvalue = OpenModal(strUrl,features);
		 	query();
	    }
	 }  
	function add(){
		var resultvalue = OpenModal('/pages/watch/transBanknoteSeqHour!add.action','750,650,roleInfo.addOperTitle,um');
		if(resultvalue!=null)
		  query();
	}
	function show(id){
			var strUrl="/pages/watch/transBanknoteSeqHour!show.action?ids="+id;
			var features="650,700,transmgr.traninfo,um";
			var resultvalue = OpenModal(strUrl,features);
	 } 
	 
	 function checkPic()
	 {
	 	 var idList=GetSelIds();
		 if(idList==""){
		 	alert("请选择要查看的交易项！");
		 }
		 else{
			 var strUrl="/pages/watch/transBanknoteSeqHour!checkPic.action?ids="+idList;
			 //alert(idList);
			 var returnValue=OpenModal(strUrl,"600,600,watch.viewSeqPic,um")
			//var strUrl = '<%=request.getContextPath()%>/pages/watch/transBanknoteSeqHour!checkPic.action?ids=' + idList;
			//window.open(strUrl);
		 }
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
 <s:form name="transBanknoteSeqHourForm"  namespace="/pages/watch" action="transBanknoteSeqHour!list.action" method="post" >
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
			<td  width="10%" align="center"><s:text name="watch.seriaNo"/><s:text name="label.colon" /></td>
			<td width="15%"><input name="seriaNo" type="text" id="seriaNo"  class="MyInput"> </td>
			<td width="10%" align="center"><s:text name="watch.trans_time"/><s:text name="label.colon" /></td>
			<td width="25%"> <input name="beginDate"  type="text" id="beginDate" size="21" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > </td>
			<td  width="5%" align="left"> <s:text name="calendar.to"/></td>
			<td width="25%" align="left"> <input name="endDate"  type="text" id="endDate" size="21" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > </td>
			<td width="10%" align="right"> <tm:button site="1"/></td>
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
	 <td align="right"><tm:button site="2"></tm:button></td>
	</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
		<tr bgcolor="#FFFFFF">
			<td> 
				<table width="100%" cellSpacing="0" cellPadding="0">
					<tr>
						<td width="6%"> <div align="center"><input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="selectAll(this)"></div> </td>
						<td width="11%"><div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div></td>
						<td width="83%" align="right">
							<div id="pagetag"><tm:pagetag pageName="currPage" formName="transBanknoteSeqHourForm" /></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="4%" class="oracolumncenterheader"><s:text name="label.select" /></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.seriaNo"/></div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.note_flag"/></div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.note_type"/></div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.denomination"/></div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.termID"/></div></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="watch.trans_time"/></div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
		<s:iterator value="transBanknoteSeqHourList" id="transBanknoteSeqHour" status="status">
		<s:if test="#status.odd == true">
			<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
		</s:if><s:else>
			<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
		</s:else>
			<td nowrap align="center"><input type="checkbox" name="id" value='<s:property value="id"/>'/></td>
			<td nowrap width="12%"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'> 
		 			<s:property value="seriaNo"/>
		  		</a>
		  	</div></td>
			<td nowrap width="12%"><div align="center">
				<tm:dataDir beanName="transBanknoteSeqHour" property="noteFlag" path="transMgr.noteFlag" scope="request"/>
			</div></td>
			<td nowrap width="12%"><div align="center">
				<s:if test='noteType=="0"'>
							<font color="#00AA00">
						</s:if><s:elseif test='noteType=="1"'>
							<font color="#FF0000">
						</s:elseif><s:elseif test='noteType=="3"'>
							<font color="#FF9900">
						</s:elseif><s:else>
							<font color="#0000AA">
						</s:else>
					<tm:dataDir beanName="transBanknoteSeqHour" property="noteType" path="transMgr.noteType" scope="request"/>
				</font>
			</div></td>
			<td nowrap width="12%"><div align="center">
				<tm:dataDir beanName="transBanknoteSeqHour" property="denomination" path="ruleMgr.moneyDenomination" scope="request"/>
			</div></td>
			<td nowrap width="12%"><div align="center"><s:property value="termid"/></div></td>
			<td nowrap width="12%"><div align="center"><s:property value="tranTime"/></div></td>
		  </tr>
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
