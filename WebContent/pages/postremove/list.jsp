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
	<script type="text/javascript">

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
				var removedate = document.getElementById("removedate").value;
				var name = document.getElementById("name").value;
				
				var pageNum = document.getElementById("pageNum").value;
				var actionUrl = "<%=request.getContextPath()%>/pages/postremove/postremoveinfo!refresh.action?from=refresh&pageNum="+pageNum;
				actionUrl += "&removedate="+removedate;
				actionUrl += "&name="+name;
				
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
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["name"] + '</font></a></td>';
				
				html += '<td align="center">' +entryInfo["advencedate"] + '</td>';
			    html += '<td align="center">' +entryInfo["groupname"]+ '</td>';
				html += '<td align="center">' +entryInfo["domain"]+ '</td>';
				html += '<td align="center">' +entryInfo["plantotaltime"]+'个月</td>';
				html += '<td align="center">' +entryInfo["planstartdate"] + '</td>';
				html += '<td align="center">' +entryInfo["planfinishdate"] + '</td>';
				html += '<td align="center">' +entryInfo["updateman"] + '</td>';
				html += '<td align="left">' +entryInfo["updatedate"] + '</td>';
		  		html += '</tr>';
		 		<% k++;%>;     
				return html;
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
			if(confirm('确认删除吗?')){
			 	var strUrl="/pages/postremove/postremoveinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/postremove/postremoveinfo!edit.action?ids="+itemId;
			  	var features="1000,750,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
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
			  	var strUrl="/pages/postremove/postremoveinfo!UpAuditing.action?ids="+itemId;
			  	var features="1000,800,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/postremove/postremoveinfo!add.action','1000,750,tmlInfo.addTmlTitle,tmlInfo');
	 	 if(resultvalue!=null)
	  		query();
	}
	
	function show(id) {
			var strUrl="/pages/postremove/postremoveinfo!show.action?ids="+id;
			var features="1000,750,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		  
		function openURL(url){
			window.open(url);
	      }
	
	</script>
    <body id="bodyid"  centermargin="0" topmargin="10">
	<s:form name="certificationInfoForm"  namespace="/pages/postremove" action="postremoveinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td height="83">
	    <fieldset  width="100%">
			<legend></legend>
			<table width="100%">
			  <tr>
			    <td height="27" align="center">姓名:</td>
			    <td><input name="name" type="text" id="name"  class="MyInput"></td>

			    <td align="center">轮岗日期:</td>
			    <td align="left">
               	  <input name="removedate" type="text" id="removedate"  size="18" class="MyInput" isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)">
                  <td width="10%" align="right"> </td>
 				<td width="17%" align="right"> <tm:button site="1"/></td>
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
 <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066"  id=downloadList>
    <tr>
             <td nowrap width="2%" class="oracolumncenterheader"></td>
      <td width="5%" nowrap class="oracolumncenterheader">姓名</td>
      <td width="10%" nowrap class="oracolumncenterheader">提出日期</td>
      <td nowrap width="7%" class="oracolumncenterheader">轮岗部门</td>
      <td nowrap width="12%" class="oracolumncenterheader">轮岗领域</td>
       <td nowrap width="8%" class="oracolumncenterheader">轮岗计划时长</td>
	  <td width="9%" nowrap class="oracolumncenterheader">计划开始日期</td>
	  <td width="9%" nowrap class="oracolumncenterheader">计划结束日期</td>
      <td width="5%" nowrap class="oracolumncenterheader">最近更新者</td>
      <td width="12%" nowrap class="oracolumncenterheader">最近更新日期</td>
    </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator value="behaviorList" id="tranInfo" status="row">
            <s:if test="#row.odd == true"> 
       	   	 <tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>            </s:if>
            <s:else>
       	    <tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>            </s:else> 
			<td nowrap align="center" >
            	<input type="checkbox" name="chkList" value='<s:property value="id"/>'/>   
            </td>
			<td align="center" >
            	<div align="center">
                    <a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
                        <s:property value="name"/></font>
                    </a>		  	   
                 </div>         
            </td>
		 	<td align="center"><s:date name="advencedate" format="yyyy-MM-dd"/></td>
             <td align="center"><s:property value="groupname"/></td>
             <td align="center"><s:property value="domain"/></td>
             <td align="center"><s:property value="plantotaltime"/>个月</td>
             <td align="center"><s:date name="planstartdate" format="yyyy-MM-dd"/></td>
             <td align="center"><s:date name="planfinishdate" format="yyyy-MM-dd"/></td>
             <td align="center" ><s:property value="updateman" /></td>
             <td align="left" ><s:property value="updatedate"/></td>
        </tr>
	</s:iterator>   
 		</tbody> 
 	</table> 
 </s:form>       
</body>  
</html> 