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
	<!-- 	private String username;  //对应用户表中主键id
	private String start;
	private String end;
	private String prjName; -->
	<script type="text/javascript"> 
	
		function query(){
			var start = document.getElementById("start").value;
			var end = document.getElementById("end").value;
			var type = document.getElementById("type").value;
			var responsible = document.getElementById("responsible").value;
			var pageNum = document.getElementById("pageNum").value;
			 var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/finance/financeinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&start="+start+"&end="+end+"&type="+type+"&responsible="+responsible;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
	   		
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			//var strURL = "javascript:openURL(\""+entryInfo["visitURL"]+"\")";
				if(entryInfo["activity"] == "合计")
				{
					html += '<tr bgcolor="#6699FF" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				}
				else
				{
					html += '<tr class="trClass<%=k%2 %>" align="center" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				}
			html += '<td>';
			if(entryInfo["id"]!=null&&entryInfo["id"]!=''){
				html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			}
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' + entryInfo["activityDate"] + '</font></a></td>';
			html += '<td align="center">' +entryInfo["activity"] + '</td>';
			html += '<td align="center">' +entryInfo["pay"] + '</td>';
			html += '<td align="center">' +entryInfo["income"] + '</td>';
			html += '<td align="center">' +entryInfo["responsible"] + '</td>';
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
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
			 	var strUrl="/pages/finance/financeinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/finance/financeinfo!edit.action?ids="+itemId;
			  	var features="1000,400,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/finance/financeinfo!add.action','1000,400,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
				
		function show(id) {
			var strUrl="/pages/finance/financeinfo!show.action?ids="+id;
			var features="1000,400,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}

		function balance(){
			var start = document.getElementById("start").value;
			var end = document.getElementById("end").value;
			var strUrl="/pages/finance/financeinfo!getBalance.action?start="+start+"&end="+end;
			//var strUrl="/pages/prjdetail/prjdetailinfo!show.action?req="+encodeURI(prjName);
		//	strUrl = encodeURI(strUrl);
			var features="670,200,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
	
	</script>
    
 <body id="bodyid" leftmargin="0" topmargin="0"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/server" action="serverinfo!list.action" method="post" >
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
				<td width="7%" align="center">开始日期:</td>
				<td width="19%"><input name="start" type="text" id="start"  class="MyInput" /> </td>
                
                <td width="7%" align="center">结束日期:</td>
				<td width="18%"><input name="end" type="text" id="end"  class="MyInput" /> </td>
                
				<td width="7%" align="center"> 类型:</td>
				<td width="9%"><select name="type" id="type" align="center" style="width:80px">
                  <option selected value="">全部</option>
                  <option value="0">支出</option>
                  <option value="1">收入</option>
                </select></td>
                
                <td width="8%" align="center"> 经手人:</td>
				<td width="1%">&nbsp;</td>
				<td width="10%" align="right">    <input name="responsible"  type="text" id="responsible" size="20" maxlength="20" /></td>
				<td width="18%" align="right"> <tm:button site="1"/>
			 </td>
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
				  <td colspan="2"><input type="button"   value="余额查询" onClick="balance();" class="MyButton"  image="../../images/share/find.gif"/></td>
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
		  <td nowrap width="9%" class="oracolumncenterheader"><div align="center">日期</div></td>
		  <td nowrap width="48%" class="oracolumncenterheader"><div align="center">项目/活动</div></td>
		  <td nowrap width="9%" class="oracolumncenterheader"><div align="center">支出</div></td>
		  <td nowrap width="9%" class="oracolumncenterheader"><div align="center">收入</div></td>
          <td nowrap width="16%" class="oracolumncenterheader"><div align="center">经手人</div></td>
  
	  </tr>
		<tbody name="formlist" id="formlist" align="center"> 
  		<s:iterator  value="financesList" id="finance" status="row">
  		<s:if test="#row.odd == true && id==null"> 
 		<tr id="tr" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) bgcolor="#6699FF">
 		</s:if>
 		<s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)  >
 		</s:else> 
			<td nowrap align="center">
            	<s:if test="id!=null">
                <input type="checkbox" name="chkList" value='<s:property value="id"/>'/>
                </s:if>
            </td>
			<td align="center"><div align="center">
				<a href='javascript:show("<s:property value="id"/>")'><font color="#3366FF">
					<s:date name="activityDate" format="yyyy-MM-dd"/></font>
		  		</a>
		  	</div></td>
		 	<td><s:property value="activity"/></td>
		 	<td align="center"><s:property value="pay"/></td>
            <td><s:property value="income"/></td>
            <td align="center"><s:property value="responsible"/></td>
	    </tr>
		</s:iterator>  
		<!-- 
		<tr  bgcolor="green">
			<td colspan='2'>合计</td>
			<td></td>
			<td>9000</td>
			<td  align="center">11000</td>
			<td  align="center"></td>
		</tr> -->
 		</tbody> 
 	</table> 
 </s:form>
 
</body>
</html>

