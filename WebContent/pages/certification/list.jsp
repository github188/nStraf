<!--20100107 14:00-->
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
<head><title>tool query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript">

			function document.onkeydown()
			{	
				var e=event.srcElement;        	
				if(event.keyCode==13) 
				{
					document.getElementById("bodyid").focus();
					query();
				}
			 }
			
            function query(){
				var certificationNo = document.getElementById("certificationNo").value;
				var cerName = document.getElementById("cerName").value;
				var pageNum = document.getElementById("pageNum").value;
				 var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
				var actionUrl = "<%=request.getContextPath()%>/pages/certification/cerinfo!refresh.action?from=refresh&pageNum="+pageNum;
				actionUrl += "&certificationNo="+certificationNo;
				actionUrl += "&cerName="+cerName;
				actionUrl=encodeURI(actionUrl);
				var method="setHTML";
				<%int k = 0 ; %>
		   		sendAjaxRequest(actionUrl,method,pageNum,true);
            }
         function setHTML(entry,entryInfo){
				var html = '';
				var str = "javascript:show(\""+entryInfo["id"]+"\")";		
	          // var strURL = "javascript:openURL(\""+entryInfo["visitURL"]+"\",\""+entryInfo["netIP"]+"\",\""+entryInfo["pageURL"]+"\")";
				html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				html += '<td>';
				html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
				html += '</td>';
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["certificationNo"] + '</font></a></td>';
				html += '<td>' +entryInfo["cerName"] + '</td>';
				html += '<td align="center">' +entryInfo["productName"] + '</td>';
				html += '<td>' +entryInfo["validTimeString"] + '</td>';
				html += '<td align="center">' +entryInfo["awardDateString"] + '</td>';
				html += '<td align="center">' +entryInfo["fileDepartment"] + '</td>';
	            html += '<td align="center">' +entryInfo["remark"] + '</td>';
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
			if(confirm('确认删除吗?')){
			 	var strUrl="/pages/certification/cerinfo!delete.action?ids="+idList;
			 	OpenModal(strUrl,"600,380,operInfo.delete,um")
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
			  	var strUrl="/pages/certification/cerinfo!edit.action?ids="+itemId;
			  	var features="1000,350,tmlInfo.updateTitle,tmlInfo";
				OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		OpenModal('/pages/certification/cerinfo!add.action','1000,350,tmlInfo.addTmlTitle,tmlInfo');
		query();
	
	}
		function show(id) {
			var strUrl="/pages/certification/cerinfo!show.action?ids="+id;
			var features="800,400,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		function openURL(url){
			window.open(url);
	}
	</script>
    <body id="bodyid"  leftmargin="0" topmargin="10">
	<s:form name="certificationInfoForm"  namespace="/pages/certification" action="cerinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%">
			  <tr>
				<td align="center">认定编号:</td>
				<td><input name="certificationNo" type="text" id="certificationNo"  class="MyInput"> </td>
				<td align="center">证书名称:</td>
				<td> <input name="cerName" type="text" id="cerName"  class="MyInput"> </td>
				<td align="right">&nbsp;</td>
				<td width="15%" align="right"> <tm:button site="1"/></td>
			  </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr> 
	</table><br/>
	
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
					<!--<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>-->
 					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="certificationInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
 <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
    <tr>
             <td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">认定编号</div></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">证书名称</div></td>
			<td nowrap width="30%" class="oracolumncenterheader"><div align="center">产品名称</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">有效日期</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">颁发日期</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">归档部门</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">备注</div></td>
    </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="certificationList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td align="left"><div align="left">
				<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="certificationNo"/></font>
		  		</a>
		  	     </div></td>
		 	<td align="left"><s:property value="cerName"/></td>
             <td align="left"><s:property value="productName"/></td>
             <td align="left"><s:property value="validTimeString"/></td>
		     <td align="left"><s:property value="awardDateString" /></td>
		     <td align="left"><s:property value="fileDepartment"/></td>
             <td align="left"><s:property value="remark"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>       
</body>  
</html> 