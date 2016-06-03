<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript"> 
		function init(){
			var myDate= new Date();
			var month=myDate.getMonth();
			month=parseInt(month)+1;
			var day=0;
			var year=parseInt(myDate.getFullYear());
			if(month!=2){
				if(month==1 || month ==3 || month ==5 || month ==7 || month ==8 || month ==10 || month ==12){ 
					day="31";
				}else{
					day="30";
				}
			}else{
				if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
					day="28";
				}else{
					day="29"
				}
			}
			var s1 = myDate.getFullYear() + "-" + month + "-01";//第一天
			var s2 = myDate.getFullYear() + "-" + month + "-" + day;//最后一天
			//document.getElementById("overtimeDay").value=s1;
			//document.getElementById("overtimeDayEnd").value=s2;
			//query();
		}
		String.prototype.trim = function(){
			return this.replace(/(^\s*)|(\s*$)/g, "");
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
			 	alert('请选择记录')
			 else if (j>1)
			 	alert('一次只能处理一条记录')
			else{
			  	var strUrl="/pages/overtime/overtimeinfo!upauditing.action?ids="+itemId;
			  	var features="790,520,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}

		function query(){
			if(!validateInputInfo()){
				return ;
			}
			//var overtimeDay = fixMonth(document.getElementById("overtimeDay").value, "-01");
			//var overtimeDayEnd = fixMonth(document.getElementById("overtimeDayEnd").value, "-31");
			var overtimeDay = document.getElementById("overtimeDay").value;
			var overtimeDayEnd = document.getElementById("overtimeDayEnd").value;
			var username=document.getElementById("idusername").options[document.getElementById("idusername").selectedIndex].text;
			var groupName=document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].text;
			var deptName=document.getElementById("deptName").options[document.getElementById("deptName").selectedIndex].text;
			
			if(document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].title){
				groupName = document.getElementById("groupName").options[document.getElementById("groupName").selectedIndex].title;
			}
			
			if($("#idusername").is(":visible")==true&&username!='----请选择人员----'){
			//	username=username.substring(0, username.indexOf("(", 0));
	        }else if($("#idusername").is(":visible")==false){
	        	username=$("#username").val();
			}else if(username=="----请选择人员----"){
				username="";
			}
			if(groupName=="---请选择项目名称---")
				groupName="";
			if(deptName=="---请选择部门---")
				deptName="";
//			var prjname=document.getElementById("prjName").value;
			var pageNum = document.getElementById("pageNum").value;
			var showdiv = document.getElementById("showdiv");
			var actionUrl = "<%=request.getContextPath()%>/pages/overtime/overtimeinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&startdate="+overtimeDay+"&enddate="+overtimeDayEnd+"&username="+username+"&groupname="+groupName+"&deptname="+deptName;
			//actionUrl += "&prjname="+groupName;
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
			//提交日期暂时屏蔽掉
			//html += '<td align="center">' +entryInfo["createdate"] + '</td>';
			html += '<td ><a href='+str+'><font color="#3366FF">'+entryInfo["username"] + '</font></a></td>';
			html += '<td>' +entryInfo["detname"] + '</td>';
			var groupname=entryInfo["prjname"];
			html += '<td title='+groupname+'>' +((groupname.length>20)?(groupname.substring(0,20)+'...'): groupname)+'</td>';
			//html += '<td>' +entryInfo["groupname"] + '</td>';
//			html += '<td>' +entryInfo["prjname"] + '</td>';
			html += '<td>' +entryInfo["startdate"].substr(0,entryInfo["startdate"].length-2) + '</td>';
			html += '<td>' + entryInfo["enddate"].substr(0,entryInfo["enddate"].length-2) + '</td>';
			html += '<td>' + entryInfo["sumtime"] + '</td>';
			html += '<td>' + entryInfo["auditing_manname"]+'</td>';
			var status=entryInfo["status"];
			if(status=="新增"){
				color='blue';
			}else if(status=="待审核"){
				color='red';
			}else if(status=="审核通过"){
				color='green';
			}else{
				color='grey';
			}
			html += '<td><font color="'+color+'">' + entryInfo["status"] + '</font></td>';
			/*var reason=entryInfo["reason"];
			html += '<td align="left" title='+reason+'>' +((reason.length>20)?(reason.substring(0,20)+'...'): reason)+'</td>';*/
	  		html += '</tr>';
	 		<%k++;%>;
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
					var strUrl="/pages/overtime/overtimeinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/overtime/overtimeinfo!edit.action?ids="+itemId;
			  	var features="790,520,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
		function add(){
			var resultvalue = OpenModal('/pages/overtime/overtimeinfo!add.action','790,520,tmlInfo.addTmlTitle,tmlInfo');
		  		query();
		}
		
				
		function show(id) {
			var strUrl="/pages/overtime/overtimeinfo!show.action?ids="+id;
			var features="790,520,transmgr.traninfo,watch";
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
		function crlfLine(str, trunNum, cr){
			var testStr = str;
			var allStr = "";
			var start = 0;
			for(start = 0; start<= testStr.length; start+=trunNum){
				allStr = allStr + testStr.substr(start, trunNum) + cr;
			}
			return allStr;
		}
		
		function validateInputInfo() {
			var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
			var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
			var thisDate = document.getElementById("overtimeDay").value;
			var endDate = document.getElementById("overtimeDayEnd").value;
			if (thisDate.length > 0 && endDate.length > 0) {
				var v = re.test(endDate);
				var a = re1.test(thisDate);
				if (!v || !a) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
				if (!DateValidate('overtimeDay', 'overtimeDayEnd')) {
					alert('开始日期大于结束日期，请重新输入！');
					return false;
				}
			} else if (thisDate.length > 0 && endDate.length == 0) {
				var a = re1.test(thisDate);
				if (!a) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
			} else if (thisDate.length == 0 && endDate.length > 0) {
				var v = re.test(endDate);
				if (!v) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
			}
			return true;
		}

		function DateValidate(beginDate, endDate) {
			var Require = /.+/;
			var begin = document.getElementsByName(beginDate)[0].value.trim();
			var end = document.getElementsByName(endDate)[0].value.trim();
			var flag = true;
			if (Require.test(begin) && Require.test(end)) {
				var beginStr = begin.split("-");
				var endStr = end.split("-");
				if (parseInt(beginStr[0], 10) > parseInt(endStr[0], 10)) {
					flag = false;
				} else if (parseInt(beginStr[0], 10) == parseInt(endStr[0], 10)) {
					if (parseInt(beginStr[1], 10) > parseInt(endStr[1], 10)) {
						flag = false;
					} else if (parseInt(beginStr[1], 10) == parseInt(endStr[1], 10)) {
						if (parseInt(beginStr[2], 10) > parseInt(endStr[2], 10)) {
							flag = false;
						}
					}
				}
			}
			return flag;
		}
		 
		 function fixMonth(thisDate, suffix){
		 	var reMonth=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;
			thisDate = thisDate.trim();
			if(thisDate.length>0){
				var b = reMonth.test(thisDate);
				if(b){
					return thisDate + suffix;
				}
				return thisDate;
		 	}
			return thisDate;
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
		
		/* function omitInput(){
			for(var i=0; i<document.getElementsByTagName("td").length; i++){
				var tmpStr = document.getElementsByTagName("td")[i].innerText.trim();
				if(document.getElementsByTagName("td")[i].id=="tdReason" && tmpStr.length > 35){
					document.getElementsByTagName("td")[i].innerText = tmpStr.substr(0,35) + "...";
				}
			 }
		}
		
		function document.onclick(){	
			omitInput();
		} */
		
		function auditing(){
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
			 	alert('请选择记录')
			 else if (j>1)
			 	alert('一次只能处理一条记录')
			else{
			  	var strUrl="/pages/overtime/overtimeinfo!auditing.action?ids="+itemId;
			  	var features="790,520,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
		function fromExport(){
			var resultvalue = OpenModal('/pages/overtime/importData.jsp','1000,500,contactInfo.importContactTitle,contactInfo');
		    query();
		}
	</script>
	<body id="bodyid" leftmargin="0" topmargin="0" onload="init();">
	<s:form name="reportInfoForm" namespace="/pages/overtime"
		action="overtimeinfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td >
						<table width="100%" class="select_area">
							<tr>
								<td width="10%" align="center" class="input_label">开始日期：</td>
								<td width="20%"><input name="overtime.startdate" type="text"
									id="overtimeDay" size="13" class="MyInput" 
									value='<s:date name="overtime.startdate" format="yyyy-MM-dd"/>' />
								</td>
								<td width="10%" align="center" class="input_label">结束日期：</td>
								<td width="20%"><input name="overtime.enddate" type="text"
									id="overtimeDayEnd" size="13" class="MyInput" 
									value='<s:date name="overtime.enddate" format="yyyy-MM-dd"/>' />
								</td>
							<!--  	<td width="10%" align="center" class="input_label">项目名称：</td>
								<td><tm:tmSelect name="overtime.prjname" id="prjName"
										selType="dataDir" path="systemConfig.projectname"
										style="width:100%;" /> 
									<script type="text/javascript">
										var html=$("#prjName").html();
										$("#prjName").html("");
										html="<option value='' selected></option>"+html;
										$("#prjName").html(html);
										$("#prjName").val("");
				   					</script>
				   				</td>-->
							</tr>
							<tr>
								<tm:deptSelect deptId="deptName" groupId="groupName"
									userId="username" deptName="overtime.detname" 
									groupName="overtime.groupname" userName="overtime.username"
									isloadName="false" deptHeadKey="---请选择部门---"
									deptHeadValue="全选" userHeadKey="----请选择人员----"
									userHeadValue="全选" groupHeadKey="---请选择项目名称---"
									groupHeadValue="全选" labelDept="部门 ：" labelGroup="项目名称："
									labelUser="姓名 ：" deptLabelClass="align:center; width:10%;class:input_label"
									deptClass="align:left;width:15%;"
									groupLabelClass="align:center; width:10%;class:input_label"
									groupClass="align:left;width:15%;"
									userLabelClass="align:center; width:10%;class:input_label"
									userClass="align:left;width:15%;" >
								</tm:deptSelect>
								<td align="right"><tm:button site="1" /></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<!-- 提交日期暂时屏蔽掉 -->
				<!-- <td nowrap width="8%" class="oracolumncenterheader"><div align="center">提交日期</div></td> -->
				<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div align="center">部门</div></td>
					<!--  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目组</div></td>-->
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">加班开始时间</div></td>
				<td nowrap width="14%" class="oracolumncenterheader"><div align="center">加班结束时间</div></td>
				<td nowrap width="9%" class="oracolumncenterheader"><div align="center">时长(小时)</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div align="center">审核人</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div align="center">状态</div></td>
				<!-- <td nowrap width="19%" class="oracolumncenterheader"><div align="center">加班原因</div></td> -->
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="overtimeList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value='<s:property value="id"/>' /></td>
					<%-- <td><div>
							<a href='javascript:show("<s:property value="id"/>")'> <font
								color="#3366FF"><s:date name="createdate"
										format="yyyy-MM-dd" /></font>
							</a>
						</div></td> --%>
					<td><a style="color:#3366FF" href="javascript:show('<s:property value="id"/>')"><s:property value="username" /></a></td>
					<td><s:property value="detname" /></td>
					<%-- <td><s:property value="groupname" /></td> --%>
					<td><s:property value="prjname" /></td>
					<td><s:date name="startdate" format="yyyy-MM-dd HH:mm:ss" /></td>
					<td><s:date name="enddate" format="yyyy-MM-dd HH:mm:ss" /></td>
					<td><s:property value="sumtime" /></td>
					<td><s:property value="auditing_manname" /></td>
					<td>
						<s:if test="status.equals('新增')">
							<font color='blue'>
						</s:if> <s:elseif test="status.equals('待审核')">
							<font color='red'>
						</s:elseif> <s:elseif test="status.equals('审核通过')">
							<font color='green'>
						</s:elseif> <s:else>
							<font color='grey'>
						</s:else><s:property value="status" /></font>
					</td>
					<!-- <td id="tdReason" align="left"><s:property value="reason" /></td> -->
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="reportInfoForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>

