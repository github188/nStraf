<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>expense info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 			
          function query(){
			var uname = $("#idqueryUserName").find("option:selected").text();
			var detName = $("#queryUserDept").find("option:selected").text();
			var groupName =$("#queryGroup").find("option:selected").text();
			if($("#queryGroup").find("option:selected").attr("title")){
				groupName = $("#queryGroup").find("option:selected").attr("title");
			}
			var showdiv = document.getElementById("showdiv");
			var pageNum = document.getElementById("pageNum").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/newEmployeeManage/newEmployeeManageinfo!refresh.action?from=refresh&pageNum="+pageNum;
				if($("#idqueryUserName").is(":visible")==true&&uname!='----请选择人员----'){
	    		actionUrl += "&uname="+uname;//.substring(0,uname.indexOf('('));
	        }else if($("#idqueryUserName").is(":visible")==false){
				actionUrl += "&uname="+$("#queryUserName").val();
			}
			if(detName!='---请选择部门---'){
				actionUrl += "&detName="+detName;
			}
			if(groupName!='---请选择项目名称---'){
				actionUrl += "&groupName="+groupName;
			}	 	
			actionUrl = encodeURI(actionUrl,"UTF-8");
			var method="setHTML";
			<%int k = 0 ; %>
			sendAjaxRequest(actionUrl,method,pageNum,true);
         }
         function setHTML(entry,entryInfo){
				var html = '';
				var str = "javascript:show(\""+entryInfo["id"]+"\")";
				var str1="showDistributedCourse('" +entryInfo["id"] + "','"+ entryInfo["uname"]+"')";
				//var blackScore=entryInfo["blackScore"]==0.0?'--':entryInfo["blackScore"];
				//var whiteScore=entryInfo["whiteScore"]==0.0?'--':entryInfo["whiteScore"];
	          // var strURL = "javascript:openURL(\""+entryInfo["visitURL"]+"\",\""+entryInfo["netIP"]+"\",\""+entryInfo["pageURL"]+"\")";
				html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				html += '<td>';
				html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
				html += '</td>';
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["uname"] + '</font></a></td>';
				html += '<td>' +entryInfo["entryDate"] + '</td>';
				html += '<td align="center">' +entryInfo["detName"] + '</td>';
				html += '<td align="center">' +entryInfo["groupName"] + '</td>';
				html += '<td>' +entryInfo["position"] + '</td>';
				html += '<td>' +entryInfo["teacher"] + '</td>';
							
				html += '<td>' +entryInfo["studyStatus"] + '</td>';
				//html += '<td align="center">' +blackScore + '</td>';
				//html += '<td align="center">' +whiteScore + '</td>';
				html += '<td align="center">' +entryInfo["changeDate"] + '</td>';
				html += '<td align="center">' +'<input type="button" value="课程详情" onclick="' + str1+ '"/>' + '</td>';	
				html += '</tr>';			
		 		<% k++;%>;   
				showdiv.style.display = "none";  
				return html;
		   }
         
     	function  GetSelIds(){
			var idList="";
			//var  em= document.all.tags("input");
			var em= document.getElementsByName("chkList");
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
			//var   em=document.all.tags("input");
			var em= document.getElementsByName("chkList");
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
			if(confirm('确认删除吗?')){
			 	var strUrl="/pages/newEmployeeManage/newEmployeeManageinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	refreshList();
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
			  	var strUrl="/pages/newEmployeeManage/newEmployeeManageinfo!edit.action?ids="+itemId;
			  	var features="1000,350,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
		function distribute(){
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
			 	alert('请选择相应的员工进行课程分配')
			 else if (j>1)
			 	alert('请选择一个员工进行课程分配')
			else{
			  	var strUrl="/pages/newEmployeeManage/newEmployeeManageinfo!editDistribute.action?uid="+itemId;
			  	var features="1000,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
	function add(){
		   var resultvalue = OpenModal('/pages/newEmployeeManage/newEmployeeManageinfo!add.action','1000,350,tmlInfo.addTmlTitle,tmlInfo');
		   refreshList(); 
	 	 //location='/nStraf/pages/newEmployeeManage/newEmployeeManageinfo!add.action';
	}
	
	function show(id) {
			var strUrl="/pages/newEmployeeManage/newEmployeeManageinfo!show.action?ids="+id;
			var features="1000,350,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		
		function show1(path) {
			window.location.href=path;
		}  
		function openURL(url){
			window.open(url);
	    }
		function showDistributedCourse(id,uname){
			 var strUrl="/pages/newEmployeeManage/newEmployeeManageinfo!distribute.action?uid="+id;
			var features="800,820,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		//	window.location.href="../trip/tripinfo!query.action";
		}
	
			
		</script>
    <body id="bodyid"  style="margin: 0px">
	<s:form name="certificationInfoForm"  namespace="/pages/newEmployeeManage" action="newEmployeeManageinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 		<td >
			<table width="99%" class="select_area">
			  <tr>
							<tm:deptSelect 
							deptId="queryUserDept" 
							deptName="deptName"
							groupId="queryGroup"
							groupName="groupName"
							userId="queryUserName" 
							userName="uname"
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
							deptClass="align:left;width:12%;" 
							groupLabelClass="align:right; width:5%;class:input_label"
							groupClass="align:left;width:12%;" 
							userLabelClass="align:right; width:5%;class:input_label"
							userClass="align:left;width:15%;" 
							>
						</tm:deptSelect>
				<td width="6%" align="center">&nbsp;</td>
			    <td width="12%" align="center">&nbsp;</td>
			    <td width="15%" align="right"> <tm:button site="1"/></td>
		      </tr>
			</table> 
		</td> 
	  </tr> 
	</table><br/>
	
<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" vAlign="middle"/>
				 <td class="orarowhead"><s:text name="operInfo.title" /></td>
				 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>

 <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066"  id=downloadList>
    <tr>
      <td nowrap width="2%" class="oracolumncenterheader"></td>
      <td width="8%" nowrap class="oracolumncenterheader">姓名</td>
      <td nowrap width="8%" class="oracolumncenterheader">入职日期</td>
	  <td nowrap width="10%" class="oracolumncenterheader">部门</td>
	  <td nowrap width="13%" class="oracolumncenterheader">项目名称</td>
	  <td nowrap width="10%" class="oracolumncenterheader">岗位</td>
	  <td nowrap width="9%" class="oracolumncenterheader"><div align="center">督导师</div></td>
	  <td width="7%" nowrap class="oracolumncenterheader"><div align="center">学习状态</div></td>
	  <!-- <td width="6%" nowrap class="oracolumncenterheader">黑盒成绩</td>
	  <td width="6%" nowrap class="oracolumncenterheader">白盒成绩</td> -->
	  <td nowrap width="8%" class="oracolumncenterheader"><div align="center">转正日期</div></td>
      <td nowrap width="10%" class="oracolumncenterheader"><div align="center">学习课程</div></td>
    </tr>
    
	<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="list" id="tranInfo" status="row">
            <s:if test="#row.odd == true"> 
          	  <tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
            </s:if>
            <s:else>
           	 <tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
            </s:else> 
            
			<td nowrap align="center" >
            	<input type="checkbox" name="chkList" value='<s:property value="id"/>'/>   
            </td>
            
			<td align="center" >
            	<div align="center">
                    <a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
                        <s:property value="uname"/></font>
                    </a>		  	   
                </div>            
            </td>
            
		 	<td align="center" ><s:date name="entryDate" format="yyyy-MM-dd"/></td>
            <td align="center"><s:property value="detName"/></td>
             <td align="center"><s:property value="groupName"/></td>
            
             <td align="center"><s:property value="position"/></td>
             <td align="center"><s:property value="teacher"/></td>
             <td align="center"><s:property value="studyStatus"/></td>
             
             <!--  <td align="center"><s:property value="blackScore==0.0?'--':blackScore"/></td>
             <td align="center"><s:property value="whiteScore==0.0?'--':whiteScore"/></td>-->
             <td align="center" ><s:date name="changeDate" format="yyyy-MM-dd"/></td>
		     <td align="center"><input type="button" value="课程详情" onclick='showDistributedCourse("<s:property value="id"/>","<s:property value="uname"/>")'/></td>
        </tr>
	</s:iterator>   
 		</tbody> 
 	</table> 
 			<table width="100%" border="0">  
			<tr bgcolor="#FFFFFF">
			<td> 
				<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<!--<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="center"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>-->
 					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="certificationInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
 </s:form>       
</body>  
</html> 