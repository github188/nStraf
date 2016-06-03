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

<head><title> </title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
			var firstLogin = true;
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
				var querydate = document.getElementById("querydate").value;
				var prjName = document.getElementById("prjName1").value;
				var pageNum = document.getElementById("pageNum").value;

	
			var actionUrl = "<%=request.getContextPath()%>/pages/projectquality/projectqualityinfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&querydate="+querydate;
            actionUrl += "&prjName="+prjName;
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
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' + entryInfo["prjQno"] + '</font></a></td>';
			html += '<td>' +entryInfo["prjName"] + '</td>';
			html += '<td>' +entryInfo["prjStart"] + '</td>';
			html += '<td>' +entryInfo["prjEnd"] + '</td>';
			html += '<td>' +entryInfo["qualityMark"] + '</td>';
			html += '<td>' +entryInfo["bugDensity"] + '</td>';
			html += '<td>' +entryInfo["bugResolverate"] + '</td>';
			html += '<td>' +entryInfo["bugReopenrate"] + '</td>';	
			html += '<td>' +entryInfo["documentQualitymark"] + '</td>';	
			html += '<td>' +entryInfo["codeQuality"] + '</td>';	
			html += '<td>' +entryInfo["performanceQuality"] + '</td>';	
			html += '<td>' +entryInfo["moduleUsed"] + '</td>';	
			html += '<td>' +entryInfo["testupNum"] + '</td>';	
			html += '<td>' +entryInfo["teststopNum"] + '</td>';
			html += '<td>' +entryInfo["projectcomplainNum"] + '</td>';
	  		html += '</tr>';
	 		<% k++;%>;
			return html;
		}
		
		function  GetSelIds(){
			var idList="";
			var  em= document.all.tags("input");
			for(var  i=0;i<em.length;i++){
			  if(em[i].type=="checkbox"&&em[i].name!="chkall"){
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
		  		//alert("de");
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
				//alert("dddddd");
			 	var strUrl="/pages/projectquality/projectqualityinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	query();
			}
		}
		
				function modify(){

			var idList=GetSelIds();
		  	if(idList=="") {
		  		//alert("de");
			  	alert("请选择要修改的列");
				return false;
		  	}

			  	var strUrl="/pages/projectquality/projectqualityinfo!edit.action?ids="+idList;
			  	var features="1200,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			
		}

		function modifyold(){
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
			  	var strUrl="/pages/projectquality/projectqualityinfo!edit.action?ids="+itemId;
			  	var features="1200,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
	function add(){
      
    	var resultvalue = OpenModal('/pages/projectquality/projectqualityinfo!add.action','1100,300,tmlInfo.addTmlTitle,tmlInfo');
		  if(resultvalue!=null)
	  		 query();
	}
	function up(){
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
			  	var strUrl="/pages/stufftandability/stufftandabilityinfo!up.action?ids="+itemId;
			  	var features="600,300,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
	}
		
		function show(id) {
			var strUrl="/pages/projectquality/projectqualityinfo!show.action?ids="+id;
			var features="1200,400,transmgr.traninfo,watch";
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
		
		
		
	
		function search()
		{
			var url="stufftandabilityinfo!queryNames.action";
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
		 
		  function searchPrj()
		{
			var url="projectqualityinfo!queryPrjs.action";
			var params="";
			jQuery.post(url, params, $(document).callbackFun1, 'json');
			document.getElementById("prjName1").disabled = true;
		}
		$.fn.callbackFun1=function (json)
		 {
		 	document.getElementById("prjName1").disabled = false;	
		  	$("#prjName1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					 $("#prjName1").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
				 document.getElementById("prjName1").focus();
				 document.getElementById("prjName1").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
		 
		function searchType()
		{
			var url="projectqualityinfo!selectDB.action";
			var params={prjType1:$("#prjType1").val()};
			jQuery.post(url, params, $(document).callbackFun3, 'json');
			document.getElementById("prjName1").disabled = true;
			//document.getElementById("versionNO1").disabled = true;
		}
		$.fn.callbackFun3=function (json)
		 {
		 	document.getElementById("prjName1").disabled = false;	
			//document.getElementById("versionNO1").disabled = false;
		  	$("#prjName1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					 $("#prjName1").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
				 document.getElementById("prjName1").focus();
				 document.getElementById("prjName1").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
	
		  
	</script>
    
 <body id="bodyid" leftmargin="0" topmargin="0" onLoad="searchPrj();">
<br>
 	<s:form name="reportInfoForm"  namespace="/pages/projectquality" action="projectqualityinfo!list.action" method="post" >
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
                 <td width="6%" align="center">日期:</td>
                <td width="20%" ><input name="querydate"  type="text" id="querydate"  class="MyInput"   issel="true" isdate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /></td>
               
                <td width="6%" align="center">项目类型:</td>
              <td width="20%" align="left"> 
              <input name="usernameHidden" type="hidden" id="usernameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getUsername() %>'/>
              <input name="groupNameHidden" type="hidden" id="groupNameHidden"  value='<%=((cn.grgbanking.feeltm.login.domain.UserModel)session.getAttribute("tm.loginUser")).getGroupName() %>'/>

              <s:select name="prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:140px;" onchange="searchType();"/></td>
 <td width="6%" align="center">项目名称:</td>
       
                <td width="20%" align="left"><select name="prjName1" id="prjName1"  style="width:165px;"></select></td>

                 <td width="14%"> </td>
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
<td><input type="checkbox" name="chkall" onClick="SelAll(this)"/>全选
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
			<td nowrap class="oracolumncenterheader" width="2%"></td>
	  <td nowrap width="6%" class="oracolumncenterheader"><div align="center">编号</div></td>
      <td nowrap width="12%" class="oracolumncenterheader"><div align="center">项目名称</div></td>	 
      <td nowrap width="10%" class="oracolumncenterheader"><div align="center">开始日期</div></td>
      <td nowrap width="10%" class="oracolumncenterheader"><div align="center">结束日期</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">质量得分</div></td>
	  <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷密度</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷解决率</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷Reopen率</div></td>
      <td nowrap width="5%" class="oracolumncenterheader"><div align="center">文档质量</div></td>
      <td nowrap width="5%" class="oracolumncenterheader"><div align="center">代码质量</div></td>
      <td nowrap width="5%" class="oracolumncenterheader"><div align="center">性能质量</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">组件复用数</div></td>
     <td nowrap width="5%" class="oracolumncenterheader"><div align="center">版本频次</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">中止测试数</div></td>
      <td nowrap width="6%" class="oracolumncenterheader"><div align="center">工程投诉数</div></td>
	  </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="behaviorList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			
			<td nowrap align="center"><input type="checkbox" name="chkList" value="<s:property value='id'/>"/></td>

			<td align="center"><div align="center">
					<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:property value="prjQno"/></font>
			  		</a>
			  	</div></td>
		 	<td align="center"><s:property value="prjName"/></td>
			<td align="center"><s:date name="prjStart" format="yyyy-MM-dd"/></td>
            <td align="center"><s:date name="prjEnd" format="yyyy-MM-dd"/></td>
            <td align="center"><s:property value="qualityMark"/></td>
            <td align="center"><s:property value="bugDensity"/></td>
		 	<td align="center"><s:property value="bugResolverate"/></td>
            <td align="center"><s:property value="bugReopenrate"/></td>
            <td align="center"><s:property value="documentQualitymark"/></td>
            <td align="center"><s:property value="codeQuality"/></td>
            <td align="center"><s:property value="performanceQuality"/></td>
            <td align="center"><s:property value="moduleUsed"/></td>
            <td align="center"><s:property value="testupNum"/></td>
            <td align="center"><s:property value="teststopNum"/></td>
            <td align="center"><s:property value="projectcomplainNum"/></td>
            
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 <script type="text/javascript">
 	document.getElementById("prjType1").value ='<%=((cn.grgbanking.feeltm.domain.testsys.ProjectDB)session.getAttribute("globalDB")).getPrjName()%>';
 </script>
</body>
</html>

