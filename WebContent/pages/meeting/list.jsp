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
				var subject = document.getElementById("subject").value;
				var compere = document.getElementById("compere").value;
				var writer = document.getElementById("writer").value;
				var start = document.getElementById("start").value;
				var end = document.getElementById("end").value;
				var content = document.getElementById("content").value;
				var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
				var pageNum = document.getElementById("pageNum").value;
				var actionUrl = "<%=request.getContextPath()%>/pages/meeting/meetinginfo!refresh.action?from=refresh&pageNum="+pageNum;
				actionUrl += "&subject="+subject;
				actionUrl += "&compere="+compere;
				actionUrl += "&writer="+writer;
				actionUrl += "&content="+content;
				actionUrl += "&start="+start;
				actionUrl += "&end="+end;
				
				actionUrl=encodeURI(actionUrl);
				var method="setHTML";
				<%int k = 0 ; %>
		   		sendAjaxRequest(actionUrl,method,pageNum,true);
            }
         function setHTML(entry,entryInfo){
				var html = '';
				var str = "javascript:show(\""+entryInfo["id"]+"\")";
				html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				html += '<td>';
				html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
				html += '</td>';
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["currentDateTime"].substring(0,10) + '</font></a></td>';
				html += '<td align="left">' +entryInfo["currentDateTime"].substring(11,19) + '</td>';
				html += '<td align="center">' +entryInfo["hour"] + '</td>';
				html += '<td>' +entryInfo["addr"] + '</td>';
		        html += '<td>' +entryInfo["subject"] + '</td>';
			    html += '<td align="center">' +entryInfo["compere"]+ '</td>';
				html += '<td align="center">' +entryInfo["writer"] + '</td>';
				html += '<td align="center">' +entryInfo["auditStatus"] + '</td>';
				 
				html += '<td align="center">' +entryInfo["sendStatus"] + '</td>';
			
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
			 	var strUrl="/pages/meeting/meetinginfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/meeting/meetinginfo!edit.action?ids="+itemId;
			  	var features="1050,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
		function UpAuditing(){
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
			  	var strUrl="/pages/meeting/meetinginfo!UpAuditing.action?ids="+itemId;
			  	var features="1000,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
		
	function add(){
		var resultvalue = OpenModal('/pages/meeting/meetinginfo!add.action','1050,800,tmlInfo.addTmlTitle,tmlInfo');
	 	 if(resultvalue!=null)
	  		query();
	}
	
	function add12(){
			var resultvalue = OpenModal('/pages/meeting/meetinginfo!addWeek.action','1050,800,tmlInfo.addTmlTitle,tmlInfo');
	 	 if(resultvalue!=null)
	  		query();
	}
	
	function show(id) {
			var strUrl="/pages/meeting/meetinginfo!show.action?ids="+id;
			var features="1000,800,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		  
		function openURL(url){
			window.open(url);
	      }
	
	</script>
	<body>
	<s:form name="certificationInfoForm"  namespace="/pages/train" action="traininfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td height="83">
	    <fieldset  width="100%">
			<legend></legend>
			<table width="100%">
			  <tr>
			    <td height="27" align="center">会议开始日期:</td>
			    <td><input name="start" type="text" id="start"  size="18" class="MyInput" issel="true" isdate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)"></td>
			    <td align="center">会议结束日期:</td>
			    <td width="14%">
			    <!--<input name="student" type="text" id="student"  class="MyInput">-->
			    <input name="end" type="text" id="end" size="18" class="MyInput" issel="true" isdate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)"></td>
			    <td width="8%" align="left">会议主题:</td>
	       <td width="15%" align="left">
       	      <input name="subject" type="text" id="subject"  class="MyInput"></td>
			    <td align="right">主持人:</td>
			    <td align="center"><input name="compere" type="text" id="compere"  class="MyInput"></td>
		      </tr>
			  <tr>
				<td width="13%" height="27" align="center">记录人：</td>
				<td width="17%"><input name="writer" type="text" id="writer"  class="MyInput"></td>
				<td width="13%" align="center">会议内容：</td>
				<td colspan="3"><input name="content" type="text" id="content" size="63" class="MyInput"></td>
			    <td width="7%" align="center">&nbsp;</td>
                <td width="12%" align="right"> <tm:button site="1"/></td>
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
						<div align="center"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>-->
 					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="certificationInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
 <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066"  id=downloadList>
    <tr>
             <td nowrap width="2%" class="oracolumncenterheader"></td>
      <td width="7%" nowrap class="oracolumncenterheader">会议日期</td>
      <td nowrap width="5%" class="oracolumncenterheader">时间</td>
	  <td nowrap width="5%" class="oracolumncenterheader">时长</td>
	  <td nowrap width="15%" class="oracolumncenterheader">地点</td>
	  <td width="36%" nowrap class="oracolumncenterheader"><div align="center">会议主题</div></td>
	  <td width="5%" nowrap class="oracolumncenterheader">主持人</td>
	  <td width="5%" nowrap class="oracolumncenterheader">记录人</td>
	  <td width="6%" nowrap class="oracolumncenterheader">审核状态</td>
	  <td width="6%" nowrap class="oracolumncenterheader">发送状态
      <div align="center"></div></td>
    </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="meetingList" id="meeting" status="row">
           <s:if test="#row.odd == true"> 
 			<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		   </s:if>
           <s:else>
 				<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		  </s:else> 
          
			<td nowrap align="center" >
            	<input type="checkbox" name="chkList" value='<s:property value="id"/>'/>           
            </td>
            
			<td align="center" >
            	<div align="center">
                    <a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
                        <s:date name="currentDateTime" format="yyyy-MM-dd"/></font>
                    </a>                 </div>           
            </td>
		 	<td align="left" ><s:date name="currentDateTime" format="HH:mm:ss"/></td>
             <td align="center"><s:property value="hour"/></td>
             <td align="center"><s:property value="addr"/></td>
             <td align="center">
             	<s:property value="subject"/>             
             </td>
             
             <td align="center">
             	<s:property value="compere"/>            
             </td>
             
             <td align="center"><s:property value="writer"/></td>
             <td align="center"><s:property value="auditStatus"/></td>
             <td align="center" ><s:property value="sendStatus" />     </td>
		     </tr>
	</s:iterator>   
 		</tbody> 
 	</table> 
 </s:form>       
</body>  
</html> 