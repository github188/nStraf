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
			var type = document.getElementById("type").value;
			var seriaNum = document.getElementById("seriaNum").value;
			var pageNum = document.getElementById("pageNum").value;
			 var showdiv = document.getElementById("showdiv");
           	showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/atm/atminfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&type="+type+"&seriaNum="+seriaNum;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";		
           var strURL = "javascript:openURL(\""+entryInfo["visitURL"]+"\",\""+entryInfo["netIP"]+"\",\""+entryInfo["pageURL"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["deviceNo"] + '</font></a></td>';
		    html += '<td>' +entryInfo["configStatus"]  + '</td>'; //同步跟新设备状态
			html += '<td>' +entryInfo["machineNo"] + '</td>';
			html += '<td align="center">' +entryInfo["netIP"] + '</td>';
			html += '<td>' +entryInfo["browerer"] + '</td>';
                      
			html += '<td align="center">' +entryInfo["browerDateString"] + '</td>';
			html += '<td align="center">' +entryInfo["browerOA"] + '</td>';
                         html += '<td align="center">' +entryInfo["updateTimeString"] + '</td>';
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
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
			 	var strUrl="/pages/atm/atminfo!delete.action?ids="+idList;
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
              var strUrl="/pages/atm/atminfo_tab.jsp?id="+itemId;
              var features="800,700,tmlInfo.updateTitle,tmlInfo";
              //window.location.href="atminfo_tab.jsp?id="+itemId;
              var resultvalue = OpenModal(strUrl,features);
              query();
            }
		}
		
	function add(){
		//var resultvalue = OpenModal('/pages/server/serverinfo!add.action','800,300,tmlInfo.addTmlTitle,tmlInfo');
		
		var resultvalue = OpenModal('/pages/atm/atminfo_tab.jsp','800,700,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
		//	window.location.href="atminfo_tab.jsp";
	}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
				
		function show(id) {
			//var strUrl="/pages/atm/atminfo!show.action?ids="+id;
			//var features="800,300,transmgr.traninfo,watch";
			//var resultvalue = OpenModal(strUrl,features);
			 var strUrl="/pages/atm/atminfo_tab.jsp?showid="+id+"@@1";
			// var strUrl="/pages/atm/atminfo_tab.jsp?showFlag=1";
             var features="800,700,transmgr.traninfo,watch";
		
             var resultvalue = OpenModal(strUrl,features); 
				//	strUrl="atminfo_tab.jsp?id="+id+"&showFlag=1";
			//	 window.location.href=strUrl; 
				 //window.showModalDialog(strUrl,window,"dialogWidth:"+800+"px;dialogHeight:"+700+"px;resizable:no;scroll:yes;help:no;status:no;toolbar=yes")
		}
		
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="atmInfoForm"  namespace="/pages/atm" action="atminfo!list.action" method="post" >
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
				<td align="center">机器型号:</td>
				<td><input name="type" type="text" id="type"  class="MyInput"> </td>
				<td align="center">设备编号:</td>
				<td> <input name="seriaNum" type="text" id="seriaNum"  class="MyInput"> </td>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">设备编号</div></td> 
			 <td nowrap width="10%" class="oracolumncenterheader"><div align="center">设备状态</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">机器型号</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">IP地址</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">借用者</div></td>
            <td nowrap width="15%" class="oracolumncenterheader"><div align="center">借用日期</div></td>
            <td nowrap width="15%" class="oracolumncenterheader"><div align="center">借用OA流水号</div></td>
             <td nowrap width="15%" class="oracolumncenterheader"><div align="center">最近更新时间</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="atmList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		  <tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)  onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td align="center"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="deviceNo"/></font>
		  		</a>
		  	</div></td>  
                     <td align="center"><s:property value="configStatus"/>
		 </td>
		 	<td align="center"><s:property value="machineNo"/></td>
            <td align="center"><s:property value="netIP"/></td>
            <td align="center"><s:property value="browerer"/></td>
		     <td align="center"><s:property value="browerDateString"/></td>
		     <td align="center"><s:property value="browerOA"/></td>
             <td align="center"><s:property value="updateTimeString"/></td>
	  </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 
</body>
</html>

