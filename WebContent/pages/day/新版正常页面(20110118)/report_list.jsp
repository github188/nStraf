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
		function query(){
			if(!validateInputInfo()){
				return ;
			}	
			var username =  document.getElementById("username").value;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
		//	var prjName =  document.getElementById("prjName").value;
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/day/reportinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&username="+username+"&start="+start+"&end="+end;
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
			/*var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="tmlInfo.del.confirm" />')) {
			 	var strUrl="/pages/report/reportinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	query();
			}*/
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
			 	alert('请选择一天的记录');
			 else if (j>1)
			 	alert('你只能一次删除一天的记录');
			else{
				if(confirm('您确认删除该天的工作记录吗？')) {
					itemId=encodeURI(itemId+"@");
			  		var strUrl="/pages/day/reportinfo!delete.action?dateString="+itemId;
					var resultvalue = OpenModal(strUrl,"600,380,operInfo.delete,um");
					document.getElementById("start").value = "";
					document.getElementById("end").value = "";
			        query();
				}
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
				itemId=encodeURI(itemId+"@");
			  	var strUrl="/pages/day/reportinfo!edit.action?dateString="+itemId;
			  	var features="780,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			document.getElementById("start").value = "";
			document.getElementById("end").value = "";
			        query();
				//var strUrl="reportinfo!edit.action?dateString="+encodeURI(itemId);
				//window.location.href=strUrl;
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/day/reportinfo!add.action','780,800,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
			document.getElementById("start").value = "";
			document.getElementById("end").value = "";
	  		query();
	}
		
				
		function show(username,dateString) {

			//var strUrl="/pages/day/reportinfo!show.action?username="+username+"&dateString="+dateString.toString();
			var strUrl="/pages/day/reportinfo!show.action?dateString="+dateString.toString()+"@"+username+"@";
			strUrl=encodeURI(strUrl);
			var features="780,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = reportInfoForm.start.value.trim();
	var endDate = reportInfoForm.end.value.trim();
		if(thisDate.length>0&&endDate.length>0){
		var v = re.test(endDate);
		var a = re1.test(thisDate);
		if(!v||!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 
		 if(!DateValidate('start','end'))
		 {
		 	alert('开始日期大于结束日期，请重新输入！');
		 	return false;
		 }
	 }else if(thisDate.length>0&&endDate.length==0){
	     
		  var a = re1.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }else if(thisDate.length==0&&endDate.length>0){
	      var v = re.test(endDate);
		if(!v){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }
	  return true;
	 }
	 
	 function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementsByName(beginDate)[0].value.trim();
    var end=document.getElementsByName(endDate)[0].value.trim();

	var flag=true;

    /*if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;*/
	if(Require.test(begin) && Require.test(end))
	{
		var beginStr = begin.split("-");
		var endStr = end.split("-");
		if(parseInt(beginStr[0]) > parseInt(endStr[0]))
		{
			flag = false;
		}
		else if(parseInt(beginStr[0]) == parseInt(endStr[0]))
		{
			if(parseInt(beginStr[1]) > parseInt(endStr[1]))
			{
				flag = false;
			}
			else if(parseInt(beginStr[1]) == parseInt(endStr[1]))
			{
				if(parseInt(beginStr[2]) > parseInt(endStr[2]))
				{
					flag = false;
				}
			}
		}
	}
   return flag;

}
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
				<td width="30%"><input name="start"  type="text" id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
				<td width="10%" align="center">结束日期：</td>
				<td width="30%"> <input name="end"  type="text" id="end" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
			  </tr>
              <tr>
              	<td align="center">员工姓名：</td>
				<td ><input name="username" type="text" id="username"  class="MyInput" value="<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>"/></td>
				<td align="right">&nbsp;</td>
				<td align="right"> <tm:button site="1"/></td>
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

