<!--20110107 11:59-->
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
<head><title></title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	
	<script type="text/javascript"> 
		function query(){
			var seachname = document.getElementById("seachname").value;
			var seachyear = document.getElementById("seachyear").value;
			var seachstatus = document.getElementById("seachstatus").value;
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/personaldevelop/personaldevelopinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&seachname="+seachname+"&seachyear="+seachyear+"&seachstatus="+seachstatus;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function UpAuditing(){
			if(!confirm('确认提交审核该记录吗？'))
			{
				return;				
			}
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
			  	var strUrl="/pages/personaldevelop/personaldevelopinfo!UpAuditing.action?ids="+itemId;
			  	var features="1000,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";		
           var strURL = "javascript:openURL(\""+entryInfo["visitURL"]+"\",\""+entryInfo["netIP"]+"\",\""+entryInfo["pageURL"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["createyear"] + '</font></a></td>';
		    html += '<td>' +entryInfo["name"]  + '</td>'; //同步跟新设备状态
			html += '<td>' +entryInfo["groupname"] + '</td>';
			html += '<td align="center">' +entryInfo["status"] + '</td>';
			html += '<td>' +entryInfo["updateman"] + '</td>';
            html += '<td align="center">' +entryInfo["updatedate"] + '</td>';
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
		
		function del() {
			var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
			 	var strUrl="/pages/personaldevelop/personaldevelopinfo!delete.action?ids="+idList;
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
			 else
          {
              var strUrl="/pages/personaldevelop/atminfo_tab2.jsp?id="+itemId;
              var features="1300,700,tmlInfo.updateTitle,tmlInfo";
              //window.location.href="atminfo_tab.jsp?id="+itemId;
              var resultvalue = OpenModal(strUrl,features);
              query();
            }
		}
		
	function add(){
		//var resultvalue = OpenModal('/pages/server/serverinfo!add.action','800,300,tmlInfo.addTmlTitle,tmlInfo');
		
		var resultvalue = OpenModal('/pages/personaldevelop/atminfo_tab.jsp','1300,700,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
		
	}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
				
		function show(id) {
			//var strUrl="/pages/atm/atminfo!show.action?ids="+id;
			//var features="800,300,transmgr.traninfo,watch";
			//var resultvalue = OpenModal(strUrl,features);
			 var strUrl="/pages/personaldevelop/atminfo_tab.jsp?showid="+id+"@@1";
			// var strUrl="/pages/atm/atminfo_tab.jsp?showFlag=1";
             var features="1300,700,transmgr.traninfo,watch";
		
             var resultvalue = OpenModal(strUrl,features); 
				//	strUrl="atminfo_tab.jsp?id="+id+"&showFlag=1";
			//	 window.location.href=strUrl; 
				 //window.showModalDialog(strUrl,window,"dialogWidth:"+800+"px;dialogHeight:"+700+"px;resizable:no;scroll:yes;help:no;status:no;toolbar=yes")
		}
		
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="atmInfoForm"  namespace="/pages/personaldevelop" action="personaldevelopinfo!list.action" method="post" >
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
				<td align="center">姓名:</td>
				<td><input name="seachname" type="text" id="seachname"  class="MyInput"> </td>
				<td align="center">年份:</td>
				<td> <input name="seachyear" type="text" id="seachyear"  class="MyInput"> </td>
                <td align="center">状态:</td>
				<td> <input name="seachstatus" type="text" id="seachstatus"  class="MyInput"> </td>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">年份</div></td> 
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">姓名</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">组别</div></td>
            <td nowrap width="15%" class="oracolumncenterheader"><div align="center">状态</div></td>
            <td nowrap width="15%" class="oracolumncenterheader"><div align="center">最近更新者</div></td>
             <td nowrap width="15%" class="oracolumncenterheader"><div align="center">最近更新日期</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="personaldevelopList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		  <tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)  onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td align="center"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="createyear"/></font>
		  		</a>
		  	</div></td>  
                     <td align="center"><s:property value="name"/>
		 </td>
		 	<td align="center"><s:property value="groupname"/></td>
            <td align="center"><s:property value="status"/></td>
		     <td align="center"><s:property value="updateman"/></td>
             <td align="center"><s:property value="updatedate"/></td>
	  </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 
</body>
</html>

