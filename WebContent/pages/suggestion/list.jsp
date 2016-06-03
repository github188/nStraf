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
			var raiseDate = document.getElementById("raiseDate").value;
			var raiseMan = document.getElementById("raiseMan").value;
			var status = document.getElementById("status").value;
			var category = document.getElementById("category").value;
			var pageNum = document.getElementById("pageNum").value;
			var resloveMan=document.getElementById("resloveMan").value;
			var planFinishDate=document.getElementById("planFinishDate").value;
			var summary=document.getElementById("summary").value;
			var description=document.getElementById("description").value;
			var raiseEndDate=document.getElementById("raiseEndDate").value;
			var pn=document.getElementById("pn").value;
		    var showdiv = document.getElementById("showdiv");
           	showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/suggestion/suggestioninfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&raiseMan="+raiseMan;
            actionUrl += "&status="+status;
            actionUrl += "&category="+category;
            actionUrl += "&riseDate="+raiseDate;
			 actionUrl += "&resloveMan="+resloveMan;
			 actionUrl += "&summary="+summary;
			 actionUrl += "&description="+description;
			 actionUrl += "&planFinishDate="+planFinishDate;
			 actionUrl += "&pn="+pn;
			  actionUrl += "&raiseEndDate="+raiseEndDate;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			var status=entryInfo["status"];
			var finishdate=entryInfo["finishing_dateString"];
			var color='';
			var colortmp = '';
			if(status=='打开'){
			    if(document.getElementById("nowdate").value > finishdate)
				{
					colortmp='red';
				}
				else
				{
					colortmp='#3366FF';
				}
			}
			else
			{
				colortmp='#3366FF';
			}
			if(status=='不解决'){
				color='#666666';
			}else if(status=='已解决'){
				color='blue';
			}else if(status=='新建'){
				color='red';
			}
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'>' +'<font color=\''+colortmp+'\'>'+entryInfo["pno"] +'</font>' + '</a></td>';
			html += '<td>' +entryInfo["raiseDateString"] + '</td>';
			html += '<td>' +entryInfo["raise_man"] + '</td>';
			html += '<td align="left">' +entryInfo["summary"] + '</td>';
			html += '<td>' +entryInfo["category"] + '</td>';
			html += '<td>' +'<font color=\''+color+'\'>'+entryInfo["status"] +'</font>' +'</td>';
			html += '<td>' +entryInfo["resolve_man"] + '</td>';
			html += '<td align="center">' +entryInfo["finishing_dateString"] + '</td>';
			html += '<td>' +entryInfo["pratical_dateString"] + '</td>';
			html += '<td align="center">' +entryInfo["price_score"] + '</td>';
			html += '<td align="center">' +entryInfo["effect_score"] + '</td>';
			html += '<td align="center">' +entryInfo["update_man"] + '</td>';
			html += '<td align="center">' +entryInfo["updateDateString"] + '</td>';
			
	  		html += '</tr>';
	 		<% k++;%>;
			showdiv.style.display = "none";
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
			 	var strUrl="/pages/suggestion/suggestioninfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/suggestion/suggestioninfo!edit.action?ids="+itemId;
			  	var features="900,600,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
	function add(){
      
    	var resultvalue = OpenModal('/pages/suggestion/suggestioninfo!add.action','900,600,tmlInfo.addTmlTitle,tmlInfo');
		  if(resultvalue!=null)
	  		 query();
	}
		
				
		function show(id) {
			var strUrl="/pages/suggestion/suggestioninfo!show.action?ids="+id;
			var features="900,600,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
	}
	</script>
    
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/suggestion" action="suggestioninfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
    <input type="hidden" name="nowdate" id="nowdate"  value='<s:property value="nowdate"/>'>
 	<table width="100%" cellSpacing="0" cellPadding="0" > 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%" height="80" class="selectTableBackground">
			  <tr>
                    <td width="8%" align="right">负责处理者:</td>
                    <td width="17%"><input name="resloveMan" type="text" id="resloveMan"  class="MyInput" size="23" maxlength="23"></td>
                    
                    <td width="6%" align="right">计划完成日期:</td>
                    <td width="14%"><input name="planFinishDate" type="text" id="planFinishDate"  class="MyInput" /></td>
                    
                    <td width="7%" align="right">状态:</td>
                    <td width="12%" ><select name="status" id="status" align="left" style="width:100px">
                      <option value='' selected="true">全部</option>
                      <option value='新建'>新建</option>
                      <option value='打开'>打开</option>
                      <option value='已解决'>已解决</option>
                      <option value='不解决'>不解决</option>
                    </select></td>
                    
                    <td width="5%" align="right">类别:</td>
            <td width="8%" align="left"><select name="category" id="category"   align="left" style="width:100px" >
                      <option value='' selected="true">全部</option>
                      <option value='制度'> 制度 </option>
                      <option value='标准'> 标准 </option>
                      <option value='流程'> 流程 </option>
                      <option value='报告'> 报告 </option>
                      <option value='方案'> 方案 </option>
                      <option value='文档'> 文档 </option>
                      <option value='用例'> 用例 </option>
                      <option value='环境'> 环境 </option>
                      <option value='工具'> 工具 </option>
                      <option value='QC'> QC </option>
                      <option value='平台'> 平台 </option>
                      <option value='技术'> 技术 </option>
                      <option value='管理'> 管理 </option>
                      <option value='培训'> 培训 </option>
                      <option value='其他'> 其他 </option>
                    </select></td>
               </tr>
                 
               <tr>
                 <td width="8%" align="right">提出开始日期:</td> 
                <td><input name="raiseDate" type="text" id="raiseDate"  class="MyInput" /></td>
                
               
               
                <td align="right">提出结束日期:</td>
                <td><input name="raiseEndDate" type="text" id="raiseEndDate"  class="MyInput" /></td>
                <td width="6%" align="right" >提出者:</td>
			     <td width="6%" ><input name="raiseMan" align="center" type="text" id="raiseMan"  class="MyInput" size="15" maxlength="15"></td>
                    
                 <td align="right"> 问题编号:</td>
                <td width="11%" align="left"> 
                 <input name="pn" type="text" id="pn" align="center" class="MyInput" size="15" maxlength="15"></td>
			  </tr>
              
              <tr>
              <td width="6%" align="right" >概要:</td>
			     <td width="6%" align="left"><input name="summary" type="text" id="summary"  class="MyInput" size="23" maxlength="23"></td>
                    
                 <td align="right"> 描述:</td>
                <td width="11%" align="left"> 
                 <input name="description" type="text" id="description"  class="MyInput" size="23" maxlength="23"></td>
                <td></td>
                <td></td>
                <td></td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<tm:button site="1"></tm:button></td>
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
			<td nowrap class="oracolumncenterheader" width="2%"></td>
	  <td nowrap width="4%" class="oracolumncenterheader"><div align="center">问题编号</div></td>
	  <td nowrap width="7%" class="oracolumncenterheader"><div align="center">提出日期</div></td>
	  <td nowrap width="5%" class="oracolumncenterheader"><div align="center">提出者</div></td>
	  <td nowrap width="23%" class="oracolumncenterheader"><div align="center">概要</div></td>
	  <td nowrap width="5%" class="oracolumncenterheader"><div align="center">类别</div></td>
	  <td nowrap width="5%" class="oracolumncenterheader"><div align="center">状态</div></td>
	  <td nowrap width="5%" class="oracolumncenterheader"><div align="center">负责处理者</div></td>
	  <td nowrap width="7%" class="oracolumncenterheader"><div align="center">计划完成日期</div></td>
	  <td nowrap width="7%" class="oracolumncenterheader"><div align="center">实际完成日期</div></td>
	  <td nowrap width="3%" class="oracolumncenterheader"><div align="center">价值分</div></td>
	  <td nowrap width="3%" class="oracolumncenterheader"><div align="center">效果分</div></td>
	  <td nowrap width="4%" class="oracolumncenterheader"><div align="center">最近更新者</div></td>
      <td nowrap width="13%" class="oracolumncenterheader"><div align="center">最近更新日期</div></td>
	  </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="behaviorList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center"><input type="checkbox" name="chkList" value="<s:property value='id'/>"/></td>
			<td align="center">             

				<div align="center">

					<a href='javascript:show("<s:property value="id"/>")'>
                <s:if test="status.equals('打开')"> 
               		
                     <s:if test="nowdate > finishing_dateString"> 
             			<font color='#FF0000'>
                    </s:if>
                    <s:else>
             		<font color="#3366FF">
             		</s:else>
             	</s:if>
                <s:else>
             		<font color="#3366FF">
             	</s:else>
               
				<s:property value="pno"/>
                 </font>
			  		</a>
                   
			  	</div>
			</td>
			<td align="center">				
						<s:property value="raiseDateString"/>
		  	</td>
		 	<td><s:property value="raise_man"/></td>
		 	<td align="left"><s:property value="summary"/></td>
		 	<td align="center"><s:property value="category"/></td>
             <td>
             	<s:if test="status.equals('不解决')"> 
             		<font color='#666666'>
             	</s:if>
             	<s:elseif test="status.equals('已解决')">
             		<font color='blue'>
             	</s:elseif>
             	<s:elseif test="status.equals('新建')">
             		<font color='red'>
             	</s:elseif>
             	<s:else>
             		<font color=''>
             	</s:else>
             	<s:property value="status"/>
             	</font>
             </td>  
            <td align="center"><s:property value="resolve_man"/></td>
		 	<td align="center"><s:property value="finishing_dateString"/></td>
			<td align="center"><s:property value="pratical_dateString"/></td>
		 	<td align="center"><s:property value="price_score"/></td>
			<td align="center"><s:property value="effect_score"/></td>
		 	<td align="center"><s:property value="update_man"/></td>
            <td align="center"><s:property value="updateDateString"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
</body>
</html>

