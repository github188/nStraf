<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
	<head>
		<title>TmlInfo query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
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
	   		return ""
	   	if(document.getElementById("chkAll").checked)
	 		return idList.substring(5)//去掉"全选"
	 	return idList.substring(1)
	 }
	
	function SelAll(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox")
	    		em[i].checked=chkAll.checked;
		}
	}
	
	function SelectObj(termId) {
	    rValue = new Array(GetSelIds());
	    //alert(GetSelIds());
	    formsubmit();
	}
	function formsubmit(){
		  parent.returnValue = rValue;
		  parent.close();
	}
	function closeModal(){
	 	window.close();
	}
	</script>
 
 <body id="bodyid" leftmargin="0" topmargin="0">
 	<s:form name="tmlInfoForm"  namespace="/pages/tmlInfo" action="/pages/tmlInfo/tmlInfo!list.action" method="post" >
 		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
			<tr bgcolor="#FFFFFF">
			<td> 
				<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<td width="6%"> 
						<div align="left">
							<input type="checkbox" name="all"  id="chkAll"   value="all"  onClick="SelAll(this)">
							<label for=chkAll><s:text name="operInfo.checkall" /></label>
						</div> 
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr class="oracolumncenterheader"> 
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="page.infoTermid" /></td>
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="page.infoTermid" /></td>
				<td width="4%"><s:text name="label.select" /></td>
				<td width="9%" style="cursor:hand"><s:text name="page.infoTermid" /></td>
			</tr>
			 <tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
		  		<s:iterator  value="tmlInfoList" id="tmlInfo" status="rowstatus">
				<td align="center">
					<input type="checkbox" name="chkList" value='<s:property value="termid" />' />
				</td>
				<td>
					<s:property value="termid" />
				</td>
		  		<s:if test="#rowstatus.count%3 == 0">
		  			<s:if test="(#rowstatus.count/3)%2 == 0">
		  				</tr><tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
		  			</s:if><s:else>
		  				</tr><tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
		  			</s:else>
		  		</s:if>
		</s:iterator> 
		<s:if test="#listSize%3 == 1">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</s:if><s:elseif test="#listSize%3 == 2">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</s:elseif>
		</tr>
		
 		</table> 
 		<table width="100%">
		<tr>
			<td align="center"> 
 			<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' onclick="SelectObj(tmlInfoForm.chkList)" class="MyButton" image="../../images/share/yes1.gif"> 
			<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
		</tr>
		</table>
 </s:form>
</body>
</html>

