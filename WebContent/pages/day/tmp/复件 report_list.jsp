<!--本页面为list新版页面，修改日期2010-1-5，后台字段未改动，但字段名需根据新需求改动-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- 每天具体的记录 -->
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
		var firstLogin = true;
		var returnIt = false;
		function query(){
			var username =  document.getElementById("username").value;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var groupName1=document.getElementById("groupName1").value;
		//	var prjName =  document.getElementById("prjName").value;
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/day/reportinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&username="+username+"&start="+start+"&end="+end+"&groupName1="+groupName1;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
				var html = '';
				//var id=entryInfo["id"];
				var username = entryInfo["username"];
				var dateString = entryInfo["dateString"];
				var tmpAllUrl =  username + "\",\"" + dateString;
				var str = "javascript:show(\""+ tmpAllUrl +"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + dateString+'@'+username + '" />';
			html += '</td>';
			html += '<td><a href='+str+'>' +entryInfo["dateString"] + '</a></td>';
			html += '<td>';
		//deleted by cjjie on 2010-12-17 html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["applyId"] + '</a>';
			html += entryInfo["username"];
			html += '</td>';
			html += '<td>' +entryInfo["groupName"] + '</td>';
			html += '<td>' + entryInfo["subsum"] + '</td>';
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
			  	var strUrl="/pages/day/reportinfo!edit.action?dateString="+encodeURI(itemId);
			  	var features="1000,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			        query();
				//var strUrl="reportinfo!edit.action?dateString="+encodeURI(itemId);
				//window.location.href=strUrl;
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/day/reportinfo!add.action','1000,800,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
				
		function show(username,dateString) {

			//var strUrl="/pages/day/reportinfo!show.action?username="+username+"&dateString="+dateString.toString();
			var strUrl="/pages/day/reportinfo!show.action?dateString="+dateString.toString()+"@"+username;
			strUrl=encodeURI(strUrl);
			var features="1000,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}

		function search()
		{
			var url="reportinfo!queryNames.action";
			var params={groupName1:$("#groupName1").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {		
		  	$("#username option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				//for(var i=json.length-1;i>=0;i--)
				{
					    $("#username").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
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
						document.getElementById("bodyid").focus();
				//	}
				//}				
			}	
		 }
		 
		 function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode == 13 || keycode == 108){ 
				document.getElementById("bodyid").focus();
				query();
			} 
		} 
		
		function listenKey(){ 
			if (document.addEventListener) { 
				document.addEventListener("keyup",getKey,false); 
			} else if (document.attachEvent) { 
				document.attachEvent("onkeyup",getKey); 
			} else { 
				document.onkeyup = getKey; 
			} 
		} 
		listenKey();
		
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/day" action="reportinfo!list.action" method="post" >
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
				<td width="10%" align="center">开始日期：</td>
				<td width="25%"><input name="start"  type="text" id="start" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" > </td>
				<td width="10%" align="center">结束日期：</td>
				<td width="25%"> <input name="end"  type="text" id="end" size="22" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" > </td>
			  </tr>
              <tr>
				<td width="10%" align="center">员工组别：</td>
				<td width="25%"> 
					<select  name="groupName1" style="width:163px;" id="groupName1" onChange="search()" >
								<option value="全选">全选</option>
                				<option value="银行终端测试组">银行终端测试组</option>
                				<option  value="系统测试组">系统测试组</option>
                				<option value="AFC测试组">AFC测试组</option>
                				<option  value="驱动测试组">驱动测试组</option>
                                <option value="项目管理组">项目管理组</option>
			 					<option value="测试部门经理">测试部门经理</option>
              		</select > 
				</td>
              	<td align="center">员工姓名：</td>
				<td >
					<input name="groupNameHidden" type="hidden" id="groupNameHidden"  value="<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>"/>
					<input name="usernameHidden" type="hidden" id="usernameHidden"  value="<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>"/>
					<select name="username" id="username"   style="width:163px;"></select>
				</td>
				<td align="right">&nbsp;</td>
				<td align="right"> <tm:button site="1"/></td>
              </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr>
	</table><br />
<script type="text/javascript">
	document.getElementById("groupName1").value = document.getElementById("groupNameHidden").value;
	search();
</script>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">创建时间 </div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">员工姓名</div></td>
			<td nowrap width="20%" class="oracolumncenterheader"><div align="center">所属小组</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">工时小计</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="reportDayList" id="info" status="row" var="info">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:date name="createDate" format="yyyy-MM-dd"/>@<s:property value="username"/>'/></td>
			<td nowrap width="12%"><div align="center">
				<a href='javascript:show("<s:property value="username"/>","<s:date name="createDate" format="yyyy-MM-dd"/>")'>
					<s:date name="createDate" format="yyyy-MM-dd"/>
		  		</a>
		  	</div></td><!--字段需修改与后台一致-->
		 	<td nowrap width="20%"><s:property value="username"/></td>
		 	<td nowrap width="10%"><s:property value="groupName"/></td>
			<td nowrap width="10%">
		    	<div align="center"><s:property value="subsum"/></div>
		    </td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

