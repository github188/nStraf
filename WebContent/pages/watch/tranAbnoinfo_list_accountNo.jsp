<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head><title>TranInfo query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
		function  SelAll(chkAll){
			var   em=document.all.tags("input");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}
				
		function show(id) {
			var strUrl="/pages/transmgr/TransMgr!show.action?ids="+id;
			var features="650,700,transmgr.traninfo,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
		}
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="tranInfoForm"  namespace="/pages/TransMgr" action="TransMgr!list.action" method="post" >
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.trans_code" /></div></td>
			<td nowrap width="23%" class="oracolumncenterheader"><div align="center"><s:text name="watch.account_no" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.trans_result" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.note_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.blacklist_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.repeat_count" /></div></td>
			<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="watch.callback_count" /></div></td>
			<td nowrap width="17%" class="oracolumncenterheader">
				<div align="center"><s:text name="watch.trans_time" /></div>
			</td>
		</tr>
		<tbody name="formlist" id="formlist">
			<s:iterator  value="tranAbnoinfoList" id="tranAbnoinfo" status="row">
			<s:if test="#row.odd == true"> 
	 		<tr id="tr" height="20" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:if><s:else>
	 		<tr id="tr" height="20" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:else>
				<td nowrap width="10%"><div align="center">
					<a href='javascript:show("<s:property value="tranId"/>")'> 
						<s:property value="transCode"/>
			  		</a>
			  	</div></td>
			 	<td nowrap width="23%"><div align="center"><s:property value="accountNo"/></div></td>
			 	<td nowrap width="10%"><div align="center">
			 		<tm:dataDir beanName="tranAbnoinfo" property="transResult" path="transMgr.transResult" scope="request"/>
				</div></td>
			  	<td nowrap width="10%"><div align="center"><s:property value="transNotenum"/></div></td>
			    <td nowrap width="10%"><div align="center"><s:property value="blackNotenum" /></div></td>
			    <td nowrap width="10%"><div align="center"><s:property value="repeatNotenum" /></div></td>
			    <td nowrap width="10%"><div align="center"><s:property value="callbackNotenum" /></div></td>
			    <td nowrap width="17%"><div align="center"><s:property value="transTime" /></div></td>
		    </tr>
			</s:iterator>
 		</tbody>
 	</table> 
 </s:form>
</body>
</html>

