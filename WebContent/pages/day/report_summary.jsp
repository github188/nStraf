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
			if(!validateInputInfo()){
				return ;
			}

			var username =  document.getElementById("username").value;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var groupName1=document.getElementById("groupName1").value;
			var prjName =  document.getElementById("prjName").value;
			var pageNum = document.getElementById("pageNum").value;
			if(prjName.trim() == "--------黑盒测试组项目列表--------" || prjName.trim() == "------自动化测试组项目列表------" || prjName.trim() == "--------白盒测试组项目列表--------" || prjName.trim() == "--------质量管理组项目列表--------" || prjName.trim() == "------------其他事项列表------------")
					{
						alert("项目名称请确认是否正确");
						document.getElementById("prjName").value = "";
						return;
					}
			var actionUrl = "<%=request.getContextPath()%>/pages/day/reportinfo!refresh1.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&username="+username+"&start="+start+"&end="+end+"&groupName1="+groupName1;
			actionUrl += "&prjName1="+prjName;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
				var html = '';
				if(entryInfo["prjName"] == "汇总")
				{
					html += '<tr bgcolor="#6699FF" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				}
				else if(entryInfo["prjName"] == "请假放假")
				{
					html += '<tr bgcolor="#4153ED" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				}
				else
				{
					html += '<tr class="trClass<%=k%2 %>" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				}
			html += '<td >' +entryInfo["prjName"] + '</td>';
			html += '<td>'+ entryInfo["managerment"]+ '</td>';
			html += '<td>' +entryInfo["requirement"] + '</td>';
			html += '<td>' + entryInfo["design"] + '</td>';
			html += '<td>' + entryInfo["code"] + '</td>';
			html += '<td>' + entryInfo["test"] + '</td>';
			html += '<td>' + entryInfo["other"] + '</td>';
			html += '<td>' + entryInfo["project"] + '</td>';
			html += '<td>' + entryInfo["personNum"] + '</td>';
			
			if(entryInfo["prjName"] == "请假放假")
			{
				html += '<td bgcolor="#4153ED">' + entryInfo["subtotal"] + '</td>';
			}
			else
			{
				html += '<td bgcolor="#6699FF">' + entryInfo["subtotal"] + '</td>';
			}
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
		if(parseInt(beginStr[0], 10) > parseInt(endStr[0], 10))
		{
			flag = false;
		}
		else if(parseInt(beginStr[0], 10) == parseInt(endStr[0], 10))
		{
			if(parseInt(beginStr[1], 10) > parseInt(endStr[1], 10))
			{
				flag = false;
			}
			else if(parseInt(beginStr[1], 10) == parseInt(endStr[1], 10))
			{
				if(parseInt(beginStr[2], 10) > parseInt(endStr[2], 10))
				{
					flag = false;
				}
			}
		}
	}
   return flag;

}

		function search()
		{
			var url="reportinfo!queryNames.action";
			var params={groupName1:$("#groupName1").val()};
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
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		listenKey();
		
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/day" action="reportinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <input name="levelHidden" type="hidden" id="levelHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getLevel() %>'/>
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%">
			  <tr>
				<td width="10%" align="center">开始日期：</td>
				<td width="25%"><input name="start"  type="text" id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
				<td width="10%" align="center">结束日期：</td>
				<td width="25%"> <input name="end"  type="text" id="end" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
                <td width="10%" align="center">项目名称：</td>
                <td width="20%"><tm:tmSelect name="prjName" id="prjName" selType="dataDir" path="systemConfig.projectname"  style="width:186px" />
               </td>
               <script type="text/javascript">
			   $("#prjName").append("<option value=' '></option>");
               document.getElementById("prjName").value = "";
			   </script>
			  </tr>
              <tr>
				<td width="10%" align="center">员工组别：</td>
				<td width="25%"> 
					<input name="usernameHidden" type="hidden" id="usernameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>'/>
					<input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>
					<select  name="groupName1" style="width:163px;" id="groupName1" onChange="search()" >
								<option value="全选" >全选</option>
                				<option value="质量管理组">质量管理组</option>
									<option  value="黑盒测试组">黑盒测试组</option>
									<option value="白盒测试组">白盒测试组</option>
									<option  value="自动化测试组">自动化测试组</option>
              		</select > 
				</td>
              	<td align="center">员工姓名：</td>
				<td >
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
		if(document.getElementById("groupNameHidden").value.trim() == "测试部门经理")
		{
			document.getElementById("groupName1").value = "全选";
			//query();
		}
		else
		{
			document.getElementById("groupName1").value = "全选";
		}
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
			<!--<td nowrap width="2%" class="oracolumncenterheader"></td>-->
			<td nowrap width="18%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">管理</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">需求</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">设计</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">编码</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">测试</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">其他</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">工程</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">参与人数</div></td>
            <td nowrap width="8%" class="oracolumncenterheader"><div align="center">小计</div></td>
		</tr>
		<tbody name="formlist" id="formlist" align="center"> 
		<s:if test="summaryList!=null">
  		<s:iterator  value="summaryList" id="info" status="row" var="info">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<!--<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:date name="createDate" format="yyyy-MM-dd"/>@<s:property value="username"/>'/></td>-->
			<td nowrap ><s:property value="prjName"/></td><!--字段需修改与后台一致-->
		 	<td nowrap ><s:property value="managerment"/></td>
		 	<td nowrap ><s:property value="requirement"/></td>
			<td nowrap ><s:property value="design"/></td>
            <td nowrap >
		    	<s:property value="code"/>
		    </td>
            <td nowrap >
		    	<s:property value="test"/>
		    </td>
            <td nowrap >
		    	<s:property value="other"/>
		    </td>
            <td nowrap >
		    	<s:property value="project"/>
		    </td>
            <td nowrap >
		    	<s:property value="subtotal"/>
		    </td>
	    </tr>
		</s:iterator>  
		</s:if>
 		<td height="2"></tbody> 
 	</table> 
 </s:form>
</body>
</html>

