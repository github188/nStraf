<!--20100107 14:00-->
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
<head><title>tool query</title></head>
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
		var ufirst = true;
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
			var pageNum = document.getElementById("pageNum").value;
			var prjName = document.getElementById("prjName1").value;
			var versionNO = document.getElementById("versionNO1").value;
			var prjType = document.getElementById("prjType1").value;
			var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
			if(prjName.trim() == '全选')
			{
				prjName = "";
			}
			if(versionNO.trim() == '全选')
			{
				versionNO = "";
			}
			var actionUrl = "<%=request.getContextPath()%>/pages/prjparams/prjparamsinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&prjName="+prjName;
			actionUrl += "&versionNO="+versionNO;
			actionUrl += "&prjType1="+prjType;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
				var html = '';
				var cao = entryInfo["versionNO"];
				//var id=entryInfo["id"];
				if(cao.trim() == "fqst")
				{
					cao = "";
				}
				var str = "javascript:show(\""+entryInfo["id"]+"\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" align="center" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td align="center">';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#336699">' +entryInfo["prjName"] + '</font></a></td>';
			html += '<td>' +cao+ '</td>';
			html += '<td>' +entryInfo["prjDefect"] + '</td>';
			html += '<td>' +entryInfo["codeLine"] + '</td>';
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
			var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('确认删除吗？')) {
			 	var strUrl="/pages/prjparams/prjparamsinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/prjparams/prjparamsinfo!edit.action?ids="+itemId;
			  	var features="800,350,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/prjparams/prjparamsinfo!add.action','800,350,tmlInfo.addTmlTitle,tmlInfo');
	  		query();
	}
		
				
		function show(id) {
			var strUrl="/pages/prjparams/prjparamsinfo!show.action?ids="+id;
			var features="800,350,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
		}
		function search()
		{
			var url="prjparamsinfo!queryNames.action";
			var params={prjName:$("#prjName1").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
			document.getElementById("versionNO1").disabled = true;
		}
		$.fn.callbackFun=function (json)
		 {	
		 	var nval = "";
		 	document.getElementById("versionNO1").disabled = false;	
		  	$("#versionNO1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					nval = json[i];
					if(nval.trim() == "fqst")
					{
						nval = "";
					}
					 $("#versionNO1").append("<option value='"+json[i]+"'>"+nval+"</option>");
				 }
				 document.getElementById("versionNO1").focus();
				 document.getElementById("versionNO1").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
		 
		 function searchPrj()
		{
			var url="prjparamsinfo!queryPrjs.action";
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
			if(document.getElementById("prjType1").value.trim() != ""){
				var url="../prjdetail/prjdetailinfo!selectDB.action";
				var params={prjType1:$("#prjType1").val()};;
				jQuery.post(url, params, $(document).callbackFun3, 'json');
				document.getElementById("prjName1").disabled = true;
				document.getElementById("versionNO1").disabled = true;
			}else
			{
				document.getElementById("prjName1").value = "";
				document.getElementById("versionNO1").value = "";
				document.getElementById("prjName1").disabled = true;
				document.getElementById("versionNO1").disabled = true;
			}
		}
		$.fn.callbackFun3=function (json)
		 {
		 	document.getElementById("prjName1").disabled = false;	
			document.getElementById("versionNO1").disabled = false;
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
			if(ufirst)
			{
				ufirst = false;
				search();
			}
		 }
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/prjparams" action="prjparamsinfo!list.action" method="post" >
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
                <td align="center">项目类型：</td>
                <td>
                 <s:select name="prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:100px;" onchange="searchType();"></s:select>
                <!--<select name="prjType1" id="prjType1" onChange="searchType()" style="width:100px">
                <option value="" selected>全选</option>
                <option value="ATMC">ATMC</option>
                <option value="DevDll">DevDll</option>
                <option value="SP">SP</option>           
                <option value="View">FEEL View</option>
                <option value="Sith">FEEL Switch</option>
                <option value="SECOne">SECOne</option>
                <option value="LiquidDate">F@ST LiquidDate</option>
                <option value="TellerMaster">TellerMaster</option>
                <option value="dongbao">东保押运综合业务系统</option>
                </select>--></td>
				<td align="center">项目名称：</td>
				<td><select name="prjName1" id="prjName1" onChange="search()" style="width:173px;"></select></td>
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
			<td nowrap width="40%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目版本</div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center">工程反馈缺陷数</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">代码行数</div></td>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="prjparamsList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center" align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'/></td>
			<td><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'>
					<font color="#336699"><s:property value="prjName"/></font>
		  		</a>
		  	</div></td>
		 	<td align="center"><s:property value='versionNO=="fqst"?"":versionNO'/></td>
		 	<td align="center"><s:property value="prjDefect"/></a></td>
            <td align="center"><s:property value="codeLine"/></a></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 <script type="text/javascript">
 /*document.getElementById("prjType1").value ='<%=((cn.grgbanking.feeltm.domain.testsys.ProjectDB)session.getAttribute("globalDB")).getPrjName()%>';*/
 document.getElementById("prjType1").value = "";
 searchType();
 </script>
</body>
</html>

