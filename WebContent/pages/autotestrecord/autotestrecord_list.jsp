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
	
	<!-- private String PrjName;
	private String VersionNo;
	private String Status;
	private Date ExecTime;
	private String TotalCase;
	private String PassCase;
	private String FailCase;
	private String AllTime;
	private String ExecMan;
	private String Note;
	private String StartMonth;
	private String EndMonth; -->
    
	<script type="text/javascript"> 
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}

		function query(){
			if(!validateInputInfo($("#StartMonth").val().trim())){
				return ;
			}
			if(!validateInputInfo($("#EndMonth").val().trim())){
				return ;
			}
			var StartMonth = document.getElementById("StartMonth").value;
			var EndMonth = document.getElementById("EndMonth").value;
			var Status=document.getElementById("Status").value;
			var PrjName=document.getElementById("PrjName").value;
			var VersionNo = document.getElementById("VersionNo").value;
			var pageNum = document.getElementById("pageNum").value;
			var showdiv = document.getElementById("showdiv");
           	//showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/autotestrecord/autotestrecordinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&StartMonth="+StartMonth+"&EndMonth="+EndMonth+"&Status="+Status+"&PrjName="+PrjName+"&VersionNo="+VersionNo;
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
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["PrjName"] + '</font></a></td>';
			html += '<td >'+entryInfo["VersionNo"] + '</td>';
			html += '<td>' +entryInfo["Status"] + '</td>';
			var tmp = entryInfo["ExecTime"];
			if(tmp.lastIndexOf(".")!=-1)
			{
				tmp = tmp.substr(0, tmp.lastIndexOf("."));
			}
			html += '<td>' +tmp + '</td>';
			html += '<td>' + entryInfo["ExecMan"] + '</td>';
			html += '<td>' + entryInfo["AllTime"] + '</td>';
			html += '<td>' + entryInfo["TotalCase"] + '</td>';	
			html += '<td>' + entryInfo["PassCase"] + '</td>';	
			html += '<td>' + entryInfo["FailCase"] + '</td>';				
			html += '<td align="left">' + entryInfo["Note"] + '</td>';
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
					alert('请选择一条记录');
				 else if (j>1)
					alert('你只能一次删除一条的记录');
				else{
					var idList=GetSelIds();
					if(confirm('<s:text name="确认删除该记录吗？" />')) {
						var strUrl="/pages/autotestrecord/autotestrecordinfo!delete.action?ids="+idList;
						var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
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
			  	var strUrl="/pages/autotestrecord/autotestrecordinfo!edit.action?ids="+itemId;
			  	var features="740,470,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/autotestrecord/autotestrecordinfo!add.action','740,470,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
				
		function show(id) {
			var strUrl="/pages/autotestrecord/autotestrecordinfo!show.action?ids="+id;
			var features="740,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function showAllInMonth(id) {
			//alert("该功能正在实现中....");
			//return;
			var strUrl="/pages/week/weekinfo!show.action?ids="+id;
			var features="820,700,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
		}
		function crlfLine(str, trunNum, cr)
		{
			var testStr = str;
			var allStr = "";
			var start = 0;
			for(start = 0; start<= testStr.length; start+=trunNum)
			{
				allStr = allStr + testStr.substr(start, trunNum) + cr;
			}
			return allStr;
		}
		
		function validateInputInfo(thisDate){
			var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
		    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
		    var reMonth=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;
		  	//var thisDate = reportInfoForm.overtimeDay.value.trim();
			if(thisDate.length>0)
			{
				var a = re1.test(thisDate);
				var b = reMonth.test(thisDate);
				if(!a && !b){
					alert('日期格式不正确,请使用日期选择!');
					return false;
					}
		 	}
		 	else if(thisDate.length>0&&endDate.length==0)
		 	{
				var a = re1.test(thisDate);
				var b = reMonth.test(thisDate);
				if(!a && !b){
				alert('日期格式不正确,请使用日期选择!');
				return false;
				}
			}
		  return true;
		 }
		 
		 function fixMonth(thisDate, suffix){
		 	var reMonth=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;
			thisDate = thisDate.trim();
			if(thisDate.length>0)
			{
				var b = reMonth.test(thisDate);
				if(b)
				{
					return thisDate + suffix;
				}
				return thisDate;
		 	}
			return thisDate;
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
		
		function omitInput()
		{
			for(var i=0; i<document.getElementsByTagName("td").length; i++)
			 {
				var tmpStr = document.getElementsByTagName("td")[i].innerText.trim();
				if(document.getElementsByTagName("td")[i].id=="tdReason" && tmpStr.length > 35)
				{
					document.getElementsByTagName("td")[i].innerText = tmpStr.substr(0,35) + "...";
				}
			 }
		}
		
		function document.onclick()
		{	
			omitInput();
		}
		
		function search()
		{
			var url="overtimeinfo!queryNames.action";
			var params={groupName:$("#groupName").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {	
		  	$("#currentName option").remove();
		  	if(json!=null&&json.length>0)
			{	
				$("#currentName").append("<option value='全选'>全选</option>");
				for(var i=0;i<json.length;i++)
				//for(var i=json.length-1;i>=0;i--)
				{
					if(json[i].trim()!="")
					{
						$("#currentName").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
					}
				 }
				 document.getElementById("currentName").focus();
				 document.getElementById("currentName").value = "全选";
				 document.getElementById("bodyid").focus();
			}
		 }
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/autotestrecord" action="autotestrecordinfo!list.action" method="post" >
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
				<td width="10%" align="center">开始日期：</td>
				<td width="20%"><input name="StartMonth"  type="text" id="StartMonth" size="17" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value=''/> </td>
                <td width="10%" align="center">结束日期：</td>
				<td width="20%"><input name="EndMonth"  type="text" id="EndMonth" size="17" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value=''/> </td>
				<td width="10%" align="center">执行状态：</td>
				<td> 
                    <select name="Status" style="width:130px;" id="Status">
                				<option value="通过">通过</option>
                				<option value="失败">失败</option>
                				<option value="执行未完成">执行未完成</option>
              			</select> 
				</td>
			  </tr>
              <tr>
				<td align="center">项目名称：</td>
				<td> 
					<input name="PrjName" type="text" id="PrjName"  class="MyInput">
				</td>
                <td align="center">版本号：</td>
				<td >
					<input name="VersionNo" type="text" id="VersionNo"  class="MyInput">
				</td>
                <td>&nbsp;</td>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">版本号</div></td>
			<td nowrap width="5%" class="oracolumncenterheader"><div align="center">执行状态</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">执行时间</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">执行人</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">总执行时间</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">总用例数</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">通过用例数</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">失败用例数</div></td>
            <td nowrap width="30%" class="oracolumncenterheader"><div align="center">备注</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="overtimeList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
		 	<td><div>
				<a href='javascript:show("<s:property value="id"/>")'>
                <font color="#3366FF"><s:property value="PrjName"/></font>
                </a>
            </div></td>
		 	<td><s:property value="VersionNo"/></td>
            <td><s:property value="Status"/></td>
            <td><s:date name="ExecTime" format="yyyy-MM-dd"/></td>
            <td><s:property value="ExecMan"/></td>
		  	<td><s:property value="AllTime"/></td>
            <td><s:property value="TotalCase"/></td>
            <td><s:property value="PassCase"/></td>
            <td><s:property value="FailCase"/></td>
            <td><s:property value="Note"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 <script type="text/javascript">
 omitInput();
 query();
 </script>
</body>
</html>

