<!--20110107 11:59-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.domain.SysUser"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>

<head><title> </title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
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
				var status = document.getElementById("status").value;
				var groupname = document.getElementById("groupname").value;
				var username = document.getElementById("username").value;
				var pageNum = document.getElementById("pageNum").value;

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
			var actionUrl = "<%=request.getContextPath()%>/pages/EmployeeInformation/EmployeeInformationinfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&status="+status;
            actionUrl += "&groupname="+groupname;
            actionUrl += "&username="+username;
			// actionUrl += "&summary="+summary;
			// actionUrl += "&planFinishDate="+planFinishDate;
			 //actionUrl += "&pn="+pn;
			//  actionUrl += "&raiseEndDate="+raiseEndDate;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			var color='';
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' + Num + '</font></a></td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["subsum"] + '</font></a></td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["username"] + '</font></a></td>';
			html += '<td>' +entryInfo["groupName"] + '</td>';
			html += '<td>' +entryInfo["monthdate"] + '</td>';
			html += '<td align="left">'+entryInfo["remarks"].replace("\r\n","<br>").replace("\n","<br>")+ '</td>';
			html += '<td>' +entryInfo["dateString"] + '</td>';
			html += '<td align="center">' +entryInfo["modifyDate"] + '</td>';		
	  		html += '</tr>';
			Num++;
			Num2++;
	 		<% k++;%>;
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
			 	var strUrl="/pages/performance/performanceinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/performance/performanceinfo!edit.action?ids="+itemId;
			  	var features="1200,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
	function add(){
      
    	OpenModal('/pages/performance/performanceinfo!add.action','1100,300,tmlInfo.addTmlTitle,tmlInfo');
	  	query();
	}
	function up(){
      
    	OpenModal('/pages/performance/performanceinfo!up.action','1100,300,tmlInfo.updateTitle,tmlInfo');
	  	query();
	}
				
		function show(id) {
			var strUrl="/pages/performance/performanceinfo!show.action?ids="+id;
			var features="1200,800,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
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
		
		
		
		var checkMonthStartFlag = true;
		
		function startMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("month").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("month").value.trim() != "")
			{
				if(validateInputStart("month"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("month"))
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
					var str = document.getElementById("month").value.split("-");
					if(str[1] != "10"&& str[1]!="11" && str[1]!="12")
					{
						document.getElementById("month").value = parseInt(str[0],10) + "-" + "0" + parseInt(str[1],10);
					}
					else
					{
						document.getElementById("month").value = parseInt(str[0],10) + "-" + parseInt(str[1],10);
					}
				}
				checkMonthStartFlag = true;
				return true;
			}
		}
	
		function startMonthHdlfirst()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("month").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("month").value.trim() != "")
			{
				if(validateInputStart("month"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("month"))
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
					var str = document.getElementById("month").value.split("-");
					if(str[1] != "01" && str[1]!="11" && str[1]!="12")
					{
						document.getElementById("month").value = parseInt(str[0],10) + "-" + "0" + parseInt(str[1]-1,10);
					}
					else if(str[1] == "01")
					{
						document.getElementById("month").value = parseInt(str[0]-1,10) + "-" + parseInt("12",10);
					}
					else if(str[1] == "10")
					{
						document.getElementById("month").value = parseInt(str[0]-1,10) + "-" + "0"+ parseInt("9",10);
					}
					else
					{
						document.getElementById("month").value = parseInt(str[0],10) + "-" + parseInt(str[1]-1,10);
					}
				}
				checkMonthStartFlag = true;
				return true;
			}
		}
	
		function search()
		{
			var url="performanceinfo!queryNames.action";
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
		 
		 function addgroupname()
		{
			$("#groupname option").remove();
		  	if(document.getElementById("groupNameHidden").value=="测试部门经理")
			{	
				$("#groupname").append("<option value='全选'>全选</option>");
				$("#groupname").append("<option value='白盒测试组'>白盒测试组</option>");
				$("#groupname").append("<option value='黑盒测试组'>黑盒测试组</option>");
				$("#groupname").append("<option value='自动化测试组'>自动化测试组</option>");
				$("#groupname").append("<option value='质量管理组'>质量管理组</option>");
				 document.getElementById("groupname").focus();
				 document.getElementById("groupname").value = "全选";
				 document.getElementById("bodyid").focus();
			}
			else
			{
				$("#groupname").append("<option value='" + document.getElementById("groupNameHidden").value + "'>" + document.getElementById("groupNameHidden").value + "</option>");
				 document.getElementById("groupname").focus();
				 document.getElementById("groupname").value = document.getElementById("groupNameHidden").value;
				 document.getElementById("bodyid").focus();
			}
		}
		  
	</script>
    
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/EmployeeInformation" action="EmployeeInformationinfo!list.action" method="post" >
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
                 <td width="6%" align="center">姓名:</td>
                <td width="20%" onClick="startMonthHdl()">
				<select name="username"  id="username"  style="width:163px;"></select>
				 </td>
               
                <td width="6%" align="center">项目组:</td>    
              <td width="20%" align="left"> 
              <input name="usernameHidden" type="hidden" id="usernameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>'/>
              <input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>

              <select name="groupname" style="width:163px;" id="groupname" onChange="search()" >		
			  <option value='全选'>全选</option>
              <option value='白盒测试组'>白盒测试组</option>
              <option value='黑盒测试组'>黑盒测试组</option>
              <option value='自动化测试组'>自动化测试组</option>
              <option value='质量管理组'>质量管理组</option>
              </select> 
	</td>
 <td width="6%" align="center">状态:</td>
       
                <td width="20%" align="left"> 
				<select name="status" style="width:63px;" id="status" onChange="search()" >		
			  <option value='全选'>全选</option>
              <option value='在职'>在职</option>
              <option value='离职'>离职</option>
              </select> 
                 <td width="14%"> 
 </td>
            </tr>
              <tr>
              	<td>
				<script type="text/javascript">
				//startMonthHdl();
				//query();
                search();
                </script>
                </td>
				<td></td>
                <td></td>
				<td></td>
                <td></td>
				<td></td>


                <td width="1%"></td>

                <td width="25%">
                <tm:button site="1"></tm:button></td>
              </tr>
			</table> 
		    </fieldset>  
		</td> 
		</tr>
	</table><br/>

	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
	<tr>
		 <td width="25"  height="23" valign="middle"><img src="../../images/share/list.gif" width="14" height="16"></td>
		 <td class="orarowhead"><s:text name="operInfo.title" /></td>
		 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
	<tr bgcolor="#FFFFFF">
	<td> 
		<table width="100%" cellSpacing="0" cellPadding="0">
		<tr>

				<td width="83%" align="right">
				<div id="pagetag"><tm:pagetag pageName="currPage" formName="reportInfoForm" /></div>
			</td>
		</tr>
		</table>
	</td>
	</tr>
</table>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" id=downloadList>
      <tr> 
	  <td nowrap class="oracolumncenterheader" width="2%"></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">序号</div></td>
	  <td nowrap width="8%" class="oracolumncenterheader"><div align="center">姓名</div></td>
	  <td nowrap width="15%" class="oracolumncenterheader"><div align="center">入职日期</div></td>
	  <td nowrap width="8%" class="oracolumncenterheader"><div align="center">学历</div></td>
	  <td nowrap width="10%" class="oracolumncenterheader"><div align="center">技术职称</div></td>
	  <td nowrap width="6%" class="oracolumncenterheader"><div align="center">在运通年限</div></td>
	  <td nowrap width="10%" class="oracolumncenterheader"><div align="center">岗位</div></td>
	  <td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目组</div></td>
	  <td nowrap width="10%" class="oracolumncenterheader"><div align="center">手机号码</div></td>
	  </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="employeeinformationList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			
			<td nowrap align="center"><input type="checkbox" name="chkList" value="<s:property value='id'/>"/><input type="hidden" name="remarks" id="remarks" value="<s:property value='remarks'/>"/></td>

			<td align="center"><div align="center">
					<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="#row.count"/></font>
			  		</a></div>
			</td>
		 	<td><div align="center">
					<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="username"/></font>
			  		</a></div>
			</td>
			<td align="center"><s:property value="hiredate"/></td>
			<td align="center"><s:property value="education"/></td>
			<td align="center"><s:property value="technicaltitle"/></td>
			<td align="center"><s:property value="yearnum"/></td>
			<td align="center"><s:property value="position"/></td>
		 	<td align="center"><s:property value="groupName"/></td>
		 	<td align="center"><s:property value="mobile"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
	
 </s:form>
</body>
</html>

