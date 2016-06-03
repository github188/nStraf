<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>workReport query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	
	<!-- 	private String username;  //对应用户表中主键id
	private String start;
	private String end;
	private String prjName; -->
	<script type="text/javascript"> 
		function query(){
			var username =  document.getElementById("username").value;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var prjName =  document.getElementById("prjName").value;
			var pageNum = document.getElementById("pageNum").value;
			 var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/report/reportinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&username="+username+"&start="+start+"&end="+end+"&prjName="+prjName;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
				var html = '';
				//var id=entryInfo["id"];
				var str = "javascript:show(\""+entryInfo["id"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td><a href='+str+'>' +entryInfo["dateString"] + '</a></td>';
			html += '<td>';
		//deleted by cjjie on 2010-12-17 html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["applyId"] + '</a>';
			html += entryInfo["taskDesc"];
			html += '</td>';
			html += '<td>' +entryInfo["prjName"] + '</td>';
			html += '<td>' + entryInfo["attachment"] + '</td>';
			html += '<td>' + entryInfo["finishRate"] + '</td>';
			html += '<td>' + entryInfo["status"] + '</td>';
			html += '<td>' + entryInfo["subtotal"] + '</td>';
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
		
		function del() {
			var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="tmlInfo.del.confirm" />')) {
			 	var strUrl="/pages/report/reportinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	query();
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
			  	var strUrl="/pages/report/reportinfo!edit.action?ids="+itemId;
			  	var features="600,500,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/report/reportinfo!add.action','600,400,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
				
		function show(id) {
			var strUrl="/pages/report/reportinfo!show.action?ids="+id;
			var features="650,700,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/report" action="reportinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%">
			  <tr>
				<td align="center">开始日期：</td>
				<td><input name="start"  type="text" id="start" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" > </td>
				<td align="center">结束日期：</td>
				<td> <input name="end"  type="text" id="end" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" > </td>
				<td align="right"> </td>
			  </tr>
			    <tr>
				<td align="center">员工姓名：</td>
				<td><input name="username" type="text" id="username"  class="MyInput" value="<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>"/></td>
				<td align="center">项目名称：</td>
				<td> <input name="prjName" type="text" id="prjName"  class="MyInput"> </td>
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
<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
			<tr bgcolor="#FFFFFF">
			<td> 
				<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>
 					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="reportInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="12%" class="oracolumncenterheader"><div align="center">创建时间 </div></td>
			<td nowrap width="20%" class="oracolumncenterheader"><div align="center">任务描述</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">交付件</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">完成百分比</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">状态</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">工时小计</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="workReportList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td nowrap width="12%"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'>
					<s:date name="startDate" format="yyyy-MM-dd"/>
		  		</a>
		  	</div></td>
		 	<td nowrap width="20%"><s:property value="taskDesc"/></td>
		 	<td nowrap width="10%"><s:property value="prjName"/></td>
		  	<td nowrap width="10%"><s:property value="attachment"/></td>
		    <td nowrap width="10%"><s:property value="finishRate"/></td>
		     <td nowrap width="10%"><s:property value="status"/></td>
		    <td nowrap width="10%">
		    	<div align="center"><s:property value="subtotal"/></div>
		    </td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

