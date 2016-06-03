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
		var count = 0;
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function query(){
			if(!startMonthHdl())
			{
				return;
			}
			var monthDate = document.getElementById("monthDate").value;
			var currentName=document.getElementById("currentName").value;
			var pageNum = document.getElementById("pageNum").value;
			var showdiv = document.getElementById("showdiv");
           	//showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/totalPrice/totalPriceinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&monthDate="+monthDate+"&currentName="+currentName;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
			count = 0;
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			count++;
			var ret = 0;
			var fret = 0;
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			var msid = "javascript:showMS(\""+entryInfo["id"]+"\")";
			var asid = "javascript:showAS(\""+entryInfo["asId"]+"\")";
			var bsid = "javascript:showBS(\""+entryInfo["bsId"]+"\")";
			var ksid = "javascript:showKS(\""+entryInfo["id"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td >'+count + '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["monthDate"] + '</font></a></td>';
			html += '<td >'+entryInfo["currentName"] + '</td>';
			
			ret = parseFloat(entryInfo["totalScore"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["totalScore"] = (fret/10.0).toString();
			html += '<td>' +entryInfo["totalScore"] + '</td>';
			
			html += '<td>' +entryInfo["workTitle"] + '</td>';
			html += '<td>' +entryInfo["bachela"] + '</td>';
			
			ret = parseFloat(entryInfo["workLen"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["workLen"] = (fret/10.0).toString();
			html += '<td>' +entryInfo["workLen"] + '</td>';
			
			ret = parseFloat(entryInfo["sworkLen"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["sworkLen"] = (fret/10.0).toString();
			html += '<td>' +entryInfo["sworkLen"] + '</td>';
			
			html += '<td>' +entryInfo["technology"] + '</td>';
			
			ret = parseFloat(entryInfo["monthScore"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["monthScore"] = (fret/10.0).toString();
			html += '<td align="center"><a href='+msid+'><font color="#3366FF">' +entryInfo["monthScore"] + '</font></a></td>';
			
			ret = parseFloat(entryInfo["abilityScore"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["abilityScore"] = (fret/10.0).toString();
			html += '<td align="center"><a href='+asid+'><font color="#3366FF">' +entryInfo["abilityScore"] + '</font></a></td>';
			
			ret = parseFloat(entryInfo["behaviorScore"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["behaviorScore"] = (fret/10.0).toString();
			html += '<td align="center"><a href='+bsid+'><font color="#3366FF">' +entryInfo["behaviorScore"] + '</font></a></td>';
			
			ret = parseFloat(entryInfo["kpiScore"].trim());
			fret = Math.round(ret*10.0, 1);
			if(isNaN(fret))	fret = 0;
			entryInfo["kpiScore"] = (fret/10.0).toString();
			html += '<td align="center"><a href='+ksid+'><font color="#3366FF">' +entryInfo["kpiScore"] + '</font></a></td>';
			
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
	
				if(j==0)
				{
					alert('请选择要删除的记录')
					return;
				}
				var idList=GetSelIds();
				if(confirm('<s:text name="确认删除该记录吗？" />')) {
					var strUrl="/pages/totalPrice/totalPriceinfo!delete.action?ids="+idList;
					var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
					query();
				}
				
		}
		
		function modifybat(){
			var resultvalue = OpenModal('/pages/monthMission/monthMissioninfo!selmonth.action','740,470,tmlInfo.updateTitle,tmlInfo');
			if(resultvalue!=null)
	  			query();
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
			  	var strUrl="/pages/monthMission/monthMissioninfo!edit.action?ids="+itemId;
			  	var features="740,470,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/totalPrice/totalPriceinfo!add.action','740,470,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
	
	function up()
	{
		var resultvalue = OpenModal('/pages/totalPrice/totalPriceinfo!up.action','740,470,tmlInfo.updateTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
	
	function refreshUp()
	{
		var resultvalue = OpenModal('/pages/totalPrice/totalPriceinfo!selmonth.action','740,470,tmlInfo.updateTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
				
		function show(id) {
			var strUrl="/pages/totalPrice/totalPriceinfo!show.action?ids="+id;
			var features="740,500,transmgr.traninfo,watch";
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
		
		function validateInputInfo(){
			var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
		    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
		    var reMonth=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;
		  	var thisDate = reportInfoForm.monthDate.value.trim();
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
		
		function validateInputStart(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;    
  	var thisDate = document.getElementById("monthDate").value.trim();
		if(thisDate.length>0)
		{
			var a = re.test(thisDate);
			if(!a)
			{
				return false;
			}
			return true;
		}
		else if(thisDate.length==0)
		{
		  	return true;
		}
	}
		 
		 function validateInputStartMonth(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;    
  	var thisDate = document.getElementById("monthDate").value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 
		function startMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("monthDate").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("monthDate").value.trim() != "")
			{
				if(validateInputStartMonth("monthDate"))
				{
					monthFlag = true;
				}
				else if(validateInputStart("monthDate"))
				{
					dayFlag = true;
				}
				else
				{
					alert("月份格式不正确,请使用选择!");
					checkMonthStartFlag = false;
					return false;
				}
				if(dayFlag)
				{
					var str = document.getElementById("monthDate").value.split("-");
					var intMonth = parseInt(str[1],10);
					if(intMonth<10)
						intMonth = '0'+intMonth.toString();
					document.getElementById("monthDate").value = parseInt(str[0],10) + "-" + intMonth;
				}
				return true;
			}
		}
		
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
			var groupRet = "";
			if($("#levelHidden").val()=='2')
			{
				return;
			}
			else if($("#levelHidden").val()=='1')
			{
				groupRet = $("#groupNameHidden").val();
			}
			else
			{
				groupRet = '全选';
			}
			var url="totalPriceinfo!queryNames.action";
			var params={groupName:groupRet};
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
				 //document.getElementById("currentName").value = $("#userNameHidden").val();
				 document.getElementById("currentName").value = "全选";
				 document.getElementById("bodyid").focus();
			}
		 }
		 
		 function showMS(id)
		 {
		 	if(id==null || id.trim()=="")
			{
				alert("尚未提交月度分");
				return;
			}
		 	var strUrl="/pages/totalPrice/totalPriceinfo!showMS.action?ids="+id;
			var features="740,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		 }
		 function showAS(id)
		 {
		 	if(id==null || id.trim()=="")
			{
				alert("尚未提交素质与能力分");
				return;
			}
		 	var strUrl="/pages/stufftandability/stufftandabilityinfo!show.action?ids="+id;
			var features="1200,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		 }
		 function showBS(id)
		 {
		 	if(id==null || id.trim()=="")
			{
				alert("尚未提交日常能力分");
				return;
			}
		 	var strUrl="/pages/dailydeed/dailydeedinfo!show.action?ids="+id;
			var features="1200,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		 }
		 function showKS(id)
		 {
		 	if(id==null || id.trim()=="")
			{
				alert("尚未提交KPI");
				return;
			}
		 	var strUrl="/pages/totalPrice/totalPriceinfo!showKS.action?ids="+id;
			var features="740,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		 }
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<%
		java.util.Date now = new java.util.Date();
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(java.util.Calendar.MONTH,calendar.get(java.util.Calendar.MONTH)-1);
		request.setAttribute ( "querylm", calendar.getTime());
		request.setAttribute ( "queryLevel", ((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel());
	%>
 	<s:form name="reportInfoForm"  namespace="/pages/totalPrice" action="totalPriceinfo!list.action" method="post" >
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
				<td width="10%" align="center">月度：</td>
				<td width="20%" onClick="startMonthHdl()"><input name="monthDate"  type="text" id="monthDate" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" value='<s:date name="#request.querylm" format="yyyy-MM"/>'/> </td>
				<td width="10%" align="center">姓名：</td>
				<td> 
                	<div id="userDiv">
                        <!--<select name="currentName" id="currentName" style="width:163px;"></select>
                        </select> -->
                    </div>
				</td>
				<td> 
					<input name="userNameHidden" type="hidden" id="userNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>'/>
					<input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>
					<input name="levelHidden" type="hidden" id="levelHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel() %>'/>
				</td>
                <td align="center">&nbsp;</td>
				<td >&nbsp;
					
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
            <td nowrap width="3%" class="oracolumncenterheader"><div align="center">序号</div></td>
			<td nowrap width="6%" class="oracolumncenterheader"><div align="center">月份</div></td>
			<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">总分</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">现岗位</div></td>
            <td nowrap width="9%" class="oracolumncenterheader"><div align="center">学历</div></td>
            <td nowrap width="5%" class="oracolumncenterheader"><div align="center">在运通年限</div></td>
            <td nowrap width="5%" class="oracolumncenterheader"><div align="center">总工龄</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">技术职称</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">月度任务得分</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">素质和能力得分</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">日常行为得分</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">KPI得分</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="toatlPriceList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
            <td><s:property value="#row.count"/></td>
		 	<td><div>
				<a href='javascript:show("<s:property value="id"/>")'>
                <font color="#3366FF"><s:property value="monthDate"/></font>
                </a>
            </div></td>
		 	<td><s:property value="currentName"/></td>
            <td><s:property value="totalScore"/></td>
            <td><s:property value="workTitle"/></td>
		  	<td><s:property value="bachela"/></td>
            <td><s:property value="workLen"/></td>
            <td><s:property value="sworkLen"/></td>
            <td><s:property value="technology"/></td>
            <td><div>
				<a href='javascript:showMS("<s:property value="id"/>")'>
                <font color="#3366FF"><s:property value="monthScore"/></font>
                </a>
            </div></td>
            <td><div>
				<a href='javascript:showAS("<s:property value="asId"/>")'>
                <font color="#3366FF"><s:property value="abilityScore"/></font>
                </a>
            </div></td>
            <td><div>
				<a href='javascript:showBS("<s:property value="bsId"/>")'>
                <font color="#3366FF"><s:property value="behaviorScore"/></font>
                </a>
            </div></td>
            <td><div>
				<a href='javascript:showKS("<s:property value="id"/>")'>
                <font color="#3366FF"><s:property value="kpiScore"/></font>
                </a>
            </div></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 <script type="text/javascript">
 //omitInput();
 var htmlshow = "";
 if($("#levelHidden").val()=='1')
 {
 	htmlshow = '<select name="currentName" id="currentName" style="width:163px;"></select></select>';
	$("#userDiv").html(htmlshow);
 }
 else if($("#levelHidden").val()=='2')
 {
	htmlshow = "<input name='currentName' type='text' id='currentName' readonly value='" + $("#userNameHidden").val().trim() + "' style='background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000'/>"
	$("#userDiv").html(htmlshow);
 }
 else
 {
 	htmlshow = '<select name="currentName" id="currentName" style="width:163px;"></select></select>';
	$("#userDiv").html(htmlshow);
 }
 search();
 query();
 </script>
</body>
</html>

