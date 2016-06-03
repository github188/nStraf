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
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
			var firstLogin = true;
			var Num = 21,Num2 = 20;
			var pageNumTmp = 1;
			String.prototype.trim = function()
			{
			return this.replace(/(^\s*)|(\s*$)/g, "");
			}
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
			var username = document.getElementById("username").value;
			var groupname = document.getElementById("groupname").value;
			var status = document.getElementById("status").value;
			var pageNum = document.getElementById("pageNum").value;
				//var showdiv = document.getElementById("showdiv");
           		//showdiv.style.display = "block";
				if(pageNumTmp == 0 || pageNum == 1)
				{
					Num = 1;
				
				}
				else if(pageNumTmp == (pageNum+1))
				{
					Num = Num - Num2 - 20;
			
				}
				else if(pageNumTmp < pageNum)
				{
					Num+= (pageNum-pageNumTmp)*20-Num2;
				
				}
				else if(pageNumTmp > pageNum)
				{
					Num+= (pageNum-pageNumTmp)*20-Num2;
				
				}
				else if(pageNumTmp == pageNum)
				{
					Num-= Num2;
				}
				Num2 = 0;
				pageNumTmp = pageNum;
			var actionUrl = "<%=request.getContextPath()%>/pages/sysinfo/sysinfoinfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&username="+username;
            actionUrl += "&groupname="+groupname;
            actionUrl += "&status="+status;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			//var status=entryInfo["status"];
			var color='';

			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' + Num + '</font></a></td>';
			html += '<td align="center">' +entryInfo["username"] + '</td>';
			html += '<td align="center">' +entryInfo["groupname"] + '</td>';
			html += '<td align="center">' +entryInfo["workstation"] + '</td>';
			html += '<td align="center">' +entryInfo["workingdate"] + '</td>';
			//html += '<td>' +'<font color=\''+color+'\'>'+entryInfo["status"] +'</font>' +'</td>';
			html += '<td align="center">' +entryInfo["grgage"] + '</td>';
			html += '<td align="center">' +entryInfo["sumage"] + '</td>';
			html += '<td align="center">' +entryInfo["mobile"] + '</td>';
			html += '<td align="center">' +entryInfo["birthplace"] + '</td>';
			html += '<td align="center">' +entryInfo["status"] + '</td>';

			html += '<td align="center">' +entryInfo["qq"] + '</td>';
			
	  		html += '</tr>';
			Num++;
			Num2++;
	 		<% k++;%>;
			showdiv.style.display = "none";
			return html;
		}
		
		function  GetSelIds(){
			var idList="";
			var  em= document.getElementsByTagName("input");
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
			var  em= document.getElementsByTagName("input");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}
		
		function del() {

			var idList=GetSelIds();
		  	if(idList=="") {
		  		//alert("de");
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
				//alert("dddddd");
			 	var strUrl="/pages/sysinfo/sysinfoinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/sysinfo/sysinfoinfo!edit.action?ids="+itemId;
			  	var features="1000,550,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
	function add(){
      
    	var resultvalue = OpenModal('/pages/sysinfo/sysinfoinfo!add.action','1000,500,tmlInfo.addTmlTitle,tmlInfo');
		  if(resultvalue!=null)
	  		 query();
	}
		
				
		function show(id) {
			var strUrl="/pages/sysinfo/sysinfoinfo!show.action?ids="+id;
			var features="1000,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
	}
	
			function search()
		{
			var url="sysinfoinfo!queryNames.action";
			var params={groupname:$("#groupname").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {		
		 	var seleteAll = false;
		  	$("#username option").remove();
		  	if(json!=null&&json.length>0)
			{	
				$("#username").append("<option value='全选'>全选</option>");
				for(var i=0;i<json.length;i++)
				//for(var i=json.length-1;i>=0;i--)
				{
						if(json[i].trim() != "全选")
						{
					    	$("#username").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
						}
						//query();
				 }
				 seleteAll = true;
				 document.getElementById("username").focus();
				 document.getElementById("username").value = "全选";
				 document.getElementById("bodyid").focus();
			}
			if(firstLogin)
			{
				firstLogin = false;
				var curUsername = document.getElementById("usernameHidden").value;
				//for(var i=0; i<document.getElementById("username").length; i++)
				//{
				//	if(document.getElementById("username").item(i).value.indexOf(curUsername)>=0)
				//	{
						document.getElementById("username").focus();
						document.getElementById("username").value = curUsername;
						if(seleteAll)
						{
							document.getElementById("username").value = "全选";
							//query();
						}
						document.getElementById("bodyid").focus();
				//	}
				//}				
			}	
		 }
		 
	</script>
    
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/sysinfo" action="sysinfoinfo!list.action" method="post" >
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

                
     <td width="4%" align="center">组别:</td>    
              <td width="10%" align="left"> 
              <input name="usernameHidden" type="hidden" id="usernameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>'/>
              <input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>

              <select name="groupname" style="width:163px;" id="groupname" onChange="search()">		
			  <option value="全选">全选</option>
              <option value="质量管理组">质量管理组</option>
              <option  value="应用软件测试组">应用软件测试组</option>
              <option value="基础软件测试组">基础软件测试组</option>
              <option  value="技术支持组">技术支持组</option>
              </select> 
	</td>
    <td width="4%" align="center">姓名:</td>
                <td width="8%" align="left"><select name="username"  id="username"  style="width:90px;"></select></td>
    <td width="4%" align="center">状态:</td>
                <td width="6%" ><select name="status" style="width:90px;" id="status">		
			  <option value="全选">全选</option>
              <option value="在职">在职</option>
              <option  value="离职">离职</option>
              </select> </td>
    			<script type="text/javascript">

                search();
                </script>
                <td width="1%"></td>
                <td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <tm:button site="1"></tm:button></td>
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
			<td nowrap class="oracolumncenterheader" width="2%"></td>
	  <td nowrap width="3%" class="oracolumncenterheader"><div align="center">序号</div></td>
	  <td nowrap width="4%" class="oracolumncenterheader"><div align="center">姓名</div></td>
	  <td nowrap width="7%" class="oracolumncenterheader"><div align="center">组别</div></td>
	  <td nowrap width="8%" class="oracolumncenterheader"><div align="center">岗位</div></td>
	  <td nowrap width="7%" class="oracolumncenterheader"><div align="center">入职日期</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">运通工作年限</div></td>
	  <td nowrap width="4%" class="oracolumncenterheader"><div align="center">总工龄</div></td>
      <td nowrap width="8%" class="oracolumncenterheader"><div align="center">手机号码</div></td>
      <td nowrap width="7%" class="oracolumncenterheader"><div align="center">籍贯</div></td>
      <td nowrap width="4%" class="oracolumncenterheader"><div align="center">状态</div></td>
      <td nowrap width="8%" class="oracolumncenterheader"><div align="center">QQ</div></td>
	  </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="behaviorList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value="<s:property value='id'/>"/></td>
			<td align="center">
				<div align="center">
					<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
				<s:property value="#row.count"/></font>
			  		</a>
			  	</div>
			</td>
			<td align="center"><s:property value="username"/></td>
		 	<td><s:property value="groupname"/></td>
		 	<td align="center"><s:property value="workstation"/></td>
		 	<td align="center"><s:date name="workingdate" format="yyyy-MM-dd"/></td>
			<td align="center"><s:property value="grgage"/></td>
		 	<td align="center"><s:property value="sumage"/></td>
            
            <td align="center"><s:property value="mobile"/></td>
            <td align="center"><s:property value="birthplace"/></td>
            
            <td align="center"><s:property value="status"/></td>
            <td align="center"><s:property value="qq"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

