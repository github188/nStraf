<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ page import="cn.grgbanking.feeltm.config.Configure" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String getdate = Configure.getProperty("stopDateDay");
%>

<html>
<head>
	<title>prjContract info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
	function init(){
		
	}
 	var num=1;
 	String.prototype.trim = function()
	{
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
 	
	function query() {
		var deptName = $("#queryUserDept option:selected").text();
		var grpName = $("#queryGroup option:selected").text();		
		if($("#queryGroup option:selected").attr("title")){
			grpName = $("#queryGroup option:selected").attr("title");
		}		
		var userName = document.getElementById("idqueryUserName").value;
		var signTime = document.getElementById("signTime").value;
		var signEndTime = document.getElementById("signEndTime").value;
		var pageNum = document.getElementById("pageNum").value;
		var approveStatus = document.getElementById("approveStatus").value;
		var approvePerson = document.getElementById("approvePerson").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
		var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!refresh.action?from=refresh&pageNum="+pageNum;
		if(deptName!='---请选择部门---'){
		actionUrl += "&deptName="+deptName;
		}
		if(grpName!='---请选择项目名称---'){
			actionUrl += "&grpName="+grpName;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
    		actionUrl += "&userId="+userName;
        }else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&userId="+$("#queryUserName").val();
		}
		actionUrl += "&signTime="+signTime;
		actionUrl += "&signEndTime="+signEndTime;
		actionUrl += "&approveStatus="+approveStatus;
		actionUrl += "&approvePerson="+approvePerson;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		html += '<tr id="tr'+entryInfo['id']+'" class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td align="center"><a href="javascript:showInfo(\'' + entryInfo["id"] + '\')"><font color="#3366FF">'+entryInfo["username"] + '</font></a></td>';
		html += '<td align="center">' +entryInfo["deptName"] + '</td>';
		html += '<td align="center">' +entryInfo["grpName"] + '</td>';
		html += '<td align="center">' +entryInfo["signTime"].substring(0,19) + '</td>';
		html += '<td align="center">' +entryInfo["areaName"] + '</td>';
		html += '<td align="center">' ;
		if(entryInfo["vilid"]=='1'){
			html += '<font color="blue">范围内</font>';
		}else if(entryInfo["vilid"]=='0'){
			html += '<font color="red">范围外</font>';
		}else if(entryInfo["vilid"]=='-1'){
			html += '<font color="red">正在识别中</font>';
		}
		html += '</td>';
		html += '<td align="center">' +entryInfo["attendanceStatusValue"] + '</td>';
		html += '<td align="center">' ;
		if(entryInfo["type"]=='3'){
			html += '<font color="navy">补签到</font>';
		}else if(entryInfo["type"]=='2'&&entryInfo["approveStatus"]!='无'){
			html += '<font color="brown">备注签到</font>';
		}else{
			html += '正常签到';
		}
		html += '</td>';
		html += '<td align="center">' +entryInfo["approvePerson"] + '</td>';
		html += '<td align="center">' ;
		if(entryInfo["approveStatus"]=='0'){
			html += '新增';
		}else if(entryInfo["approveStatus"]=='2'){
			html += '<font color="red">项目经理审核中</font>';
		}else if(entryInfo["approveStatus"]=='3'){
			html += '<font color="red">部门经理审核中</font>';
		}else if(entryInfo["approveStatus"]=='4'){
			html += '<font color="red">行政审核中</font>';
		}else if(entryInfo["approveStatus"]=='5'){
			html += '<font color="grey">审核不通过</font>';
		}else if(entryInfo["approveStatus"]=='6'){
			html += '<font color="blue">审核通过</font>';
		}else if(entryInfo["approveStatus"]=='1'){
			html += '修改中';
		}
		html += '</td>';
		html += '<td align="center">';
		html += '<a target="_blank" href="http://api.map.baidu.com/marker?location='+entryInfo["latitude"]+','+entryInfo["longitude"]+'&title='+entryInfo["areaName"]+'&content=签到地点&output=html&src=nStraf"><font color="blue">查看地图</font></a>';
		html += '</td>';
  		html += '</tr>';
        num++;
 		<% k++;%>;     
		return html;
	}
	/* function toExport(){
		if( isHR ) {
			var strUrl="/pages/signrecord/signRecord!toExportPage.action";
			var features="1000,500,signrecord.title_audit";
			var resultvalue = OpenModal(strUrl,features);
			//var resultvalue = OpenModal('/pages/signrecord/exportData.jsp','500,300,page.export.title,contactInfo');
			//refreshList();
		} else {
			alert("非行政人员没有导出签到信息的权限!");
			return;
		}
	}  */
	
	function toExport(){
		
		
		var deptName = $("#queryUserDept option:selected").text();
		var grpName = $("#queryGroup option:selected").text();		
		if($("#queryGroup option:selected").attr("title")){
			grpName = $("#queryGroup option:selected").attr("title");
		}		
		var userName = document.getElementById("idqueryUserName").value;
		var signTime = document.getElementById("signTime").value;
		var signEndTime = document.getElementById("signEndTime").value;
		var pageNum = document.getElementById("pageNum").value;
		var approveStatus = document.getElementById("approveStatus").value;
		var approvePerson = document.getElementById("approvePerson").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
		var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!exportData.action?from=refresh";
		if(deptName!='---请选择部门---'){
		actionUrl += "&deptName="+deptName;
		}
		if(grpName!='---请选择项目名称---'){
			actionUrl += "&grpName="+grpName;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
    		actionUrl += "&userId="+userName;
        }else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&userId="+$("#queryUserName").val();
		}
		actionUrl += "&signTime="+signTime;
		actionUrl += "&signEndTime="+signEndTime;
		actionUrl += "&approveStatus="+approveStatus;
		actionUrl += "&approvePerson="+approvePerson;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		
		
		
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		//window.location.href=strReturn;
		setTimeout(function(){
			if(window==window.parent){
		        window.close();
			}else{
			   parent.close();  
			}
		},500);
		
	}
	function importOutData() {
		var resultvalue = OpenModal('/pages/signrecord/importOutData.jsp','800,350,page.import.title,contactInfo');
		//refreshList();
	}
	 function importInnerData(){
		 var resultvalue = OpenModal('/pages/signrecord/importInnerData.jsp','800,350,page.import.title,contactInfo');
		 //refreshList();
	 }
	
	//获取所选复选框
	 function  GetSelIds()
	 {
	     var idList="";
	 	var  em= document.getElementsByName("chkList");
	 	for(var  i=0;i<em.length;i++)
	 	{
	 	   if(em[i].type=="checkbox")
	 	   {
	 		   if(em[i].checked){
	 			   idList+=","+em[i].value.split(",")[0];
	 		   }
	 	   }
	 	}
	 	
	 	 if(idList=="")
	 	    return "";
	 	 return idList.substring(1);
	 }
	 
	 function resign(){
		var strUrl="/pages/signrecord/signRecord!toresignPage.action";
		var features="800,600,signrecord.title_resign";	
		var resultvalue =OpenModal(strUrl,features);	
		refreshList();
	 }
	 
	 function modify(){
		 var idList = GetSelIds();
		 var ids = idList.split(',');
		 if(idList==""){
			 alert('请选择需要修改的签到信息！');
			 return false;
		 }
		 if(ids.length>1){
			 alert('一次只能修改一个签到信息');
			 return false;
		 }
		 /*if($("#tr"+idList+" td:eq(6)").text().trim()=="范围内"){
			 alert("此签到信息属于范围内信息，不可修改");
			 return false;
		 }*/
		 if($("#tr"+idList+" td:eq(10)").text().trim().indexOf("审核中")>0){
			alert("此签到信息正在审核中，不可修改");
			return false;
		 }
		 if($("#tr"+idList+" td:eq(10)").text().trim().indexOf("审核通过")!=-1){
			alert("此签到信息已审核通过，不可修改");
			return false;
		 }
		 //添加是否超过锁定日期的判断
		 var signtime = $("#tr"+idList+" td:eq(4)").text().trim().substring(0,10);
		 var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!flagOuttime.action?signtime="+signtime;
		 $.ajax({
			url : actionUrl,
			type : "post",
			data : {},
			dataType: "json",
			success : function(data) {
				if(data.flag == "false") {
					 alert("该记录的签到日期已经超出锁定的日期，不能再进行操作了");
					 return;
				}else{
					var strUrl="/pages/signrecord/signRecord!toModifyResignPage.action?id="+idList;
					var features="1000,500,signrecord.title_audit";
					var resultvalue = OpenModal(strUrl,features);
					refreshList();
				}
			}
		 });
	 }
	 
	 function UpAuditing(){
		 var  em= document.getElementsByName("chkList");
		 var idList="";
		 for(var  i=0;i<em.length;i++)
		 {
		   if(em[i].checked){
			   if($("#tr"+em[i].value+" td:eq(10)").text().trim()=="新增"||$("#tr"+em[i].value+" td:eq(10)").text().trim()=="修改中")
			   {
				   idList+=","+em[i].value.split(",")[0];
			   }
			   else
				{
				   alert("只能提交审核状态为 ‘新增’或者‘修改中’的签到信息");
				   return false;
				}
		 	}
		 }
		 if(idList.length>1){
			 idList =  idList.substring(1);
		 }else{
			 alert("请选择需要提交审核的签到信息");
			   return false;
		 }
		 
		var strUrl="/pages/signrecord/signRecord!submitApproval.action?ids="+idList;
		var features="1000,500,signrecord.title_audit";
		var resultvalue = OpenModal(strUrl,features);
		refreshList();
	 }
	 
	 function auditing(){
		 var idList = GetSelIds();
		 var ids = idList.split(',');
		 if(idList==""){
			 alert('请选择需要审核的签到信息！');
			 return false;
		 }
		 if(ids.length>1){
			 alert('一次只能审核一个签到信息');
			 return false;
		 }
		 if($("#tr"+idList+" td:eq(10)").text().trim().indexOf("审核中")<0){
				alert("只能审核 在审核中的 签到信息，此签到信息不可审核");
				return false;
			 }
		//添加是否超过锁定日期的判断
		 var signtime = $("#tr"+idList+" td:eq(4)").text().trim().substring(0,10);
		 var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!flagOuttime.action?signtime="+signtime;
		 $.ajax({
			url : actionUrl,
			type : "post",
			data : {},
			dataType: "json",
			success : function(data) {
				if(data.flag == "false") {
					 alert("该记录的签到日期已经超出锁定的日期，不能再进行操作了");
					 return;
				}else{
					var strUrl="/pages/signrecord/signRecord!toapprovalsign.action?id="+idList;
					var features="1000,500,signrecord.title_edit";
					var resultvalue = OpenModal(strUrl,features);
					refreshList();
				}
			}
		 });
	 }
	 
	 function showInfo(id){
		 var strUrl="/pages/signrecord/signRecord!view.action?id="+id;
		var features="800,600,signrecord.title_view";	
		var resultvalue =OpenModal(strUrl,features);	
		//refreshList();
	 }
		var isHR = true;//是否是行政人员
		$(function() {
			var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!ifHR.action";
			$.ajax({
				url : actionUrl,
				type : "post",
				data : {},
				dataType: "json",
				success : function(data) {
					if(data.code == "-1") {
						isHR = false; 
						return;
					}
				}
			});
			
			//加载时部门经理、项目经理、HR默认显示需要自己审核的记录
/*			var grpIds = "<%=request.getAttribute("grpCodes")%>";
			var curusername = "<%=request.getAttribute("curusername")%>";
			if(grpIds.search("deptManager")!=-1){
				$("#approveStatus option[value='3']").attr("selected","selected");
				$("#approvePerson").val(curusername);
			}else if(grpIds.search("groupManager")!=-1){
				$("#approveStatus option[value='2']").attr("selected","selected");
				$("#approvePerson").val(curusername);
			}else if(grpIds.search("hr")!=-1){
				$("#approveStatus option[value='4']").attr("selected","selected");
			}
*/			
			//refreshList();
		});
		
		function download(){
			window.location="<%=request.getContextPath()%>/pages/signrecord/signRecord!download.action";
		}
		
		function resetInfo(){
			$("#queryUserDept").val('');
			$("#queryGroup").val('');
			$("#approveStatus").val('');
			$("#approvePerson").val('');
			$("#signTime").val('');
			$("#signEndTime").val('');
			var userName = $("#idqueryUserName").find("option:selected").text();
			if($("#idqueryUserName").is(":visible")==true){
				$("#idqueryUserName").val('');
			}else if($("#idqueryUserName").is(":visible")==false){
				$("#queryUserName").val('');
			}
		}
		
		function moreApprove(){
			var strUrl="/pages/signrecord/signRecord!toapprovalmoresign.action";
			var features="1000,600,signrecord.title_resign";	
			var resultvalue =OpenModal(strUrl,features);	
			refreshList();
		}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="signRecordInfoForm" action="signRecord!listAll.action"  namespace="/pages/signrecord" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	
	<%@include file="/inc/navigationBar.inc"%>
	
	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 			<td >
					<table width="100%"  class="select_area">
              			<tr>
							<tm:deptSelect 
							deptId="queryUserDept" 
							deptName="signRecord.deptName"
							groupId="queryGroup"
							groupName="signRecord.grpName"
							userId="queryUserName" 
							userName="signRecord.userName"
							isloadName="false" 
							deptHeadKey="---请选择部门---" 
							deptHeadValue="" 
							userHeadKey="----请选择人员----" 
							userHeadValue=""  
							groupHeadKey="---请选择项目名称---"
							groupHeadValue=""
							labelDept="部门 :" 
							labelGroup="项目名称:" 
							labelUser="姓名 :" 
							deptLabelClass="align:right; width:5%;class:input_label"
							deptClass="align:left;width:18%;" 
							groupLabelClass="align:right; width:5%;class:input_label"
							groupClass="align:left;width:18%;" 
							userLabelClass="align:right; width:5%;class:input_label"
							userClass="align:left;width:18%;" 
							>
						</tm:deptSelect>
						<td align="right" width:5%; class="input_label">审核人:</td> 
                			<td>
                				<input name="approvePerson" type="text" id="approvePerson"  
                				width="15%"  />
                			</td>
						
							</tr>
              			<tr>
              			 	<td align="right" width="5%" class="input_label">开始日期:</td> 
                			<td>
                				<input name="signTime" type="text" id="signTime"  
                				class="MyInput" width="18%" size="13" />
                			</td>
                			<td align="right" width="5%" class="input_label">结束日期:</td>
                			<td>
                				<input name="signEndTime" type="text" id="signEndTime"  
                				class="MyInput" size="13" width="18%"/>
                			</td>
                			
                			 <td align="right" width="5%" class="input_label">审核状态:</td> 
                			<td>
                				<select name="approveStatus" width="15%" id="approveStatus" >
                						<option value="" >---- 请选择 ----</option>
										<option value="0">新增</option>
										<option value="1">修改中</option>
										<option value="2">项目经理审核中</option>
										<option value="3">部门经理审核中</option>
										<!-- <option value="4">行政审核中</option> -->
										<option value="5">审核不通过</option>
										<option value="6">审核通过</option>
										<option value="无">无状态</option>
                				</select>
                			</td>
                			<td></td>
                			<td align="center">
                				<input type="button" name="resetBut" id="resetBut" value='清空' class="MyButton" image="../../images/share/Modify_icon.gif" onclick="resetInfo();">
                				<tm:button site="1" ></tm:button>
                			</td>
              			</tr>
					</table>
			</td> 
		</tr>
	</table>
	<br/>
	
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			<td width="25"  height="23" valign="middle">&nbsp;</td>
			<td class="orarowhead" width="55"><s:text name="operInfo.title" /></td>
			<td><font color="red" >每月<%=getdate %>号将锁定上月数据，上月数据将不能进行处理，请在每月<%=getdate %>号前处理完所有异常数据</font></td>
			<td align="right" width="50%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>

	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap class="oracolumncenterheader" width="2%"></td>
		  	<td nowrap width="6%" class="oracolumncenterheader"><div align="center">姓名</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">所属部门</div></td>
		  	<td nowrap class="oracolumncenterheader"><div align="center">所在项目名称</div></td>
		  	<td nowrap width="13%" class="oracolumncenterheader"><div align="center">签到日期</div></td>
		  	<td nowrap class="oracolumncenterheader"><div align="center">签到地址</div></td>
		  	<td nowrap width="5%" class="oracolumncenterheader"><div align="center">签到地点</div></td>
		  	<td nowrap width="5%" class="oracolumncenterheader"><div align="center">考勤状态</div></td>
		  	<td nowrap width="6%" class="oracolumncenterheader"><div align="center">签到类型</div></td>
		  	<td nowrap width="6%" class="oracolumncenterheader"><div align="center">审核人</div></td> 
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">审核状态</div></td>
		  	<td nowrap width="6%" class="oracolumncenterheader"><div align="center">查看地图</div></td>
		  	<!-- <td nowrap width="7%" class="oracolumncenterheader"><div align="center">经度</div></td>
		  	<td nowrap width="7%" class="oracolumncenterheader"><div align="center">纬度</div></td> -->
	  	</tr>
	  	<tbody name="formlist" id="formlist" align="center">
  		<s:iterator  value="signRecordlist" id="signRecordInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id='tr<s:property value="id"/>' class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if>
 		<s:else>
 		<tr id='tr<s:property value="id"/>' class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center">
				<input type="checkbox" name="chkList" value="<s:property value='Id'/>"/>
			</td>
			<td align="center"><a href='javascript:showInfo("<s:property value="id"/>")'><font color="#3366FF">
			 <s:property value="username"/></font></a> </td>
		 	<td align="center"><s:property value="deptName"/></td>
		 	<td align="center"><s:property value="grpName"/></td>
		 	<td align="center"><s:date name="signTime" format="yyyy-MM-dd HH:mm:ss"/></td>
            <td align="center"><s:property value="areaName"/></td>
            <td align="center">
            	<s:if test="vilid==1"><font color="blue">范围内</font></s:if>
            	<s:if test="vilid==0"><font color="red">范围外</font></s:if>
            	<s:if test="vilid==-1"><font color="green">正在识别中</font></s:if>
            </td>
            <td align="center"><s:property value="attendanceStatusValue"/></td>
            <td align="center">
            	<s:if test="type==3"><font color="navy">补签到</font></s:if>
            	<s:elseif test="type==2&&approveStatus!='无'"><font color="brown">备注签到</font></s:elseif>
            	<s:else>正常签到</s:else>
            </td>
             <td align="center">
            	<s:property value="approvePerson"/>
            </td>
            <td align="center">
            	<s:if test="approveStatus==0">新增</s:if>
            	<s:elseif test="approveStatus==2"><font color="red">项目经理审核中</font></s:elseif>
            	<s:elseif test="approveStatus==3"><font color="red">部门经理审核中</font></s:elseif>
            	<s:elseif test="approveStatus==4"><font color="red">行政审核中</font></s:elseif>
            	<s:elseif test="approveStatus==5"><font color="grey">审核不通过</font></s:elseif>
            	<s:elseif test="approveStatus==6"><font color="blue">审核通过</font></s:elseif>
            	<s:elseif test="approveStatus==1">修改中</s:elseif>
            </td>
            <td align="center">
            <a target="_blank" href="http://api.map.baidu.com/marker?location=<s:property value='latitude'/>,<s:property value='longitude'/>&title=<s:property value='areaName'/>&content=签到地点&output=html&src=nStraf"><font color="blue">查看地图</font></a>
            </td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
	<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<%-- <td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
					<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
					</td> --%>
					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="prjContractInfoForm" /></div>
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
</s:form>
</body>
</html>