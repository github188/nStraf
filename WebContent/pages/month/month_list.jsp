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
	
	<!-- 	private String username;  //对应用户表中主键id
	private String start;
	private String end;
	private String prjName; -->
	<script type="text/javascript"> 
		var checkMonthStartFlag = true;
		var checkMonthEndFlag = true;
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
	
		function query(){
			if(!startMonthHdl()){
				alert("开始月份格式不正确,请使用日期选择!");
				return;
			}
			if(!endMonthHdl()){
				alert("结束月份格式不正确,请使用日期选择!");
				return;
			}
			if(!validateInputInfo()){
				return ;
			}
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var groupName1=document.getElementById("groupName1").value;
			var pageNum = document.getElementById("pageNum").value;
			var showdiv = document.getElementById("showdiv");
           	showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/month/monthinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&start="+start+"&end="+end+"&groupName="+groupName1;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
			
			var nowGroup = document.getElementById("groupName1").value;
			var yourGroup = document.getElementById("groupNameHidden").value;
			if(nowGroup.trim() != yourGroup.trim())
			{
				document.getElementById("showEditId").style.display = "none"; 
			}
			else
			{
				document.getElementById("showEditId").style.display = ""; 
			}
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["startDateString"] + '</font></a></td>';
			html += '<td>' +entryInfo["groupName"] + '</td>';
			html += '<td>' +entryInfo["finishInfoString"] + '</td>';
			html += '<td>' + entryInfo["responsor"] + '</td>';
			html += '<td>' + entryInfo["modifyDate"] + '</td>';
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
						var strUrl="/pages/month/monthinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/month/monthinfo!edit.action?ids="+itemId;
			  	var features="820,700,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		//window.location.href="weekinfo!add.action";
		var resultvalue = OpenModal('/pages/month/monthinfo!add.action','820,700,tmlInfo.addTmlTitle,tmlInfo');

	  		query();
	}
		
				
		function show(id) {
			var strUrl="/pages/month/monthinfo!show.action?ids="+id;
			var features="820,600,transmgr.traninfo,watch";
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
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g; 
	var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g; 
  	var thisDate = reportInfoForm.start.value.trim();
	var endDate = reportInfoForm.end.value.trim();
		if(thisDate.length>0&&endDate.length>0){
		var v = re.test(endDate);
		var a = re1.test(thisDate);
		if(!v||!a){
			//alert('月份格式不正确,请使用日期选择!');
			return false;
			}
		 
		 if(!DateValidate('start','end'))
		 {
		 	alert('开始月份大于结束月份，请重新输入！');
		 	return false;
		 }
	 }else if(thisDate.length>0&&endDate.length==0){
	     
		  var a = re1.test(thisDate);
		if(!a){
			//alert('月份格式不正确,请使用日期选择!');
			return false;
			}
	  }else if(thisDate.length==0&&endDate.length>0){
	      var v = re.test(endDate);
		if(!v){
			//alert('月份格式不正确,请使用日期选择!');
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
				}
			}
		   return flag;
		
		}
		
		 function validateInputStart(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 
		 function validateInputStartMonth(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
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
		
		function startMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("start").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("start").value.trim() != "")
			{
				if(validateInputStart("start"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("start"))
				{
					monthFlag = true;
				}
				else
				{
					//alert("开始月份格式不正确,请使用日期选择!");
					checkMonthStartFlag = false;
					return false;
				}
				if(dayFlag)
				{
					var str = document.getElementById("start").value.split("-");
					document.getElementById("start").value = parseInt(str[0],10) + "-" + parseInt(str[1],10);
				}
				checkMonthStartFlag = true;
				return true;
			}
		}
		
		function endMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("end").value.trim() == "")
			{
				checkMonthEndFlag = true;
				return true;
			}
			if(document.getElementById("end").value.trim() != "")
			{
				checkMonthEndFlag = true;
				if(validateInputStart("end"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("end"))
				{
					monthFlag = true;
				}
				else
				{
					//alert("结束月份格式不正确,请使用日期选择!");
					checkMonthEndFlag = false;
					return false;
				}
				if(dayFlag)
				{
					var str = document.getElementById("end").value.split("-");
					document.getElementById("end").value = parseInt(str[0],10) + "-" + parseInt(str[1],10);
				}
				checkMonthEndFlag = true;
				return true;
			}
		}
		
		/*function document.onclick()
		{		
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("start").value.trim() == "")
			{
				checkMonthStartFlag = true;
			}
			if(document.getElementById("end").value.trim() == "")
			{
				checkMonthEndFlag = true;
			}
			if(document.getElementById("start").value.trim() != "")
			{
				checkMonthStartFlag = true;
				if(validateInputStart("start"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("start"))
				{
					monthFlag = true;
				}
				else
				{
					//alert("开始月份格式不正确,请使用日期选择!");
					checkMonthStartFlag = false;
					return;
				}
				if(dayFlag)
				{
					var str = document.getElementById("start").value.split("-");
					document.getElementById("start").value = parseInt(str[0]) + "-" + parseInt(str[1]);
				}
				checkMonthStartFlag = true;
			}
			dayFlag = false;
			monthFlag = false;
			if(document.getElementById("end").value.trim() != "")
			{
				checkMonthEndFlag = true;
				if(validateInputStart("end"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("end"))
				{
					monthFlag = true;
				}
				else
				{
					//alert("结束月份格式不正确,请使用日期选择!");
					checkMonthEndFlag = false;
					return;
				}
				if(dayFlag)
				{
					var str = document.getElementById("end").value.split("-");
					document.getElementById("end").value = parseInt(str[0]) + "-" + parseInt(str[1]);
				}
				checkMonthEndFlag = true;
			}
		 }*/
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/month" action="monthinfo!list.action" method="post" >
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
				<td width="10%" align="center">开始月份：</td>
				<td width="30%" onClick="startMonthHdl()"><input name="start"  type="text" id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
				<td width="10%" align="center">结束月份：</td>
				<td width="30%" onClick="endMonthHdl()"><input name="end"  type="text" id="end" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /></td>
			  </tr>
              <tr>
				<td align="center">组别：</td>
				<td> 
					<input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>
					<select  name="groupName1" style="width:163px;" id="groupName1" >
								<option value="全选">全选</option>
                				<option value="质量管理组">质量管理组</option>
										<option  value="黑盒测试组">黑盒测试组</option>
										<option value="白盒测试组">白盒测试组</option>
										<option  value="自动化测试组">自动化测试组</option>	
           		    </select> 
				</td>
                <td>&nbsp;</td>
				<td align="right"> <tm:button site="1"/></td>
			  </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr>
	</table><br />
    <script type="text/javascript">
		if(document.getElementById("groupNameHidden").value.trim() == "测试部门经理")
		{
			document.getElementById("groupName1").value = "全选";
			query();
		}
		else
		{
			document.getElementById("groupName1").value = document.getElementById("groupNameHidden").value;
		}
	</script>
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				 <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
				 <td class="orarowhead"><s:text name="operInfo.title" /></td>
				 <td align="right" width="75%" id="showEditId"><tm:button site="2"></tm:button></td>
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
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">月份</div></td>
			<td nowrap width="20%" class="oracolumncenterheader"><div align="center">组别</div></td>
            <td nowrap width="20%" class="oracolumncenterheader"><div align="center">总体完成情况</div></td>
            <td nowrap width="20%" class="oracolumncenterheader"><div align="center">负责人</div></td>
             <td nowrap width="20%" class="oracolumncenterheader"><div align="center">最近更新时间</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="monthList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'>
					<font color="#3366FF"><s:property value="startDateString"/></font>
		  		</a>
		  	</div></td>
            <td><s:property value="groupName"/></td>
            <td><s:property value="finishInfoString"/></td>
		  	<td ><s:property value="responsor"/></a></td>
            <td ><s:property value="modifyDate"/></a></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

