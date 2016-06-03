<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 	<head>
 		<title><s:text name="rule.title.info" /></title>
 	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
	function closeModal(){
		window.close();
	}
	</script>
	
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form action="/pages/rule/repeatRegulation!show.action"   method="post">
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	<tr>
	<td>
		<fieldset class="jui_fieldset" width="100%">
		<legend><s:text name="rule.title.info" /></legend>
		  <s:iterator value="repeatRegulation" id="repeatRegulation">
		  	<table width="100%" align="center" class="popnewdialog1">
				<tr>
					<td><s:text name="rule.applyId" /><s:text name="label.colon" /></td>
          			<td><s:property value="applyId"/></td>
          			
          			<td><s:text name="repeat.info.num" /><s:text name="label.colon" /></td>
          			<td><s:property value="repeatnum" /></td>
          		</tr> 
				<tr>
					<td><s:text name="repeat.info.dealMode" /><s:text name="label.colon" /></td>
          			<td>
          				<tm:dataDir beanName="repeatRegulation" property="dealwithMode" path="ruleMgr.processModel" />
          			</td>
          			
          			<td><s:text name="repeat.info.screenMode" /><s:text name="label.colon" /></td>
          			<td>
          				<tm:dataDir beanName="repeatRegulation" property="creenMode" path="ruleMgr.greenModel" />
          			</td>
          		</tr> 
				<tr>
					<td><s:text name="repeat.info.accountMode" /><s:text name="label.colon" /></td>
          			<td>
          				<tm:dataDir beanName="repeatRegulation" property="enterAccountMode" path="ruleMgr.accountModel" />
          			</td>
          			
          			<td><s:text name="repeat.info.logMode" /><s:text name="label.colon" /></td>
          			<td>
          				<tm:dataDir beanName="repeatRegulation" property="logMode" path="ruleMgr.logModel" />
          			</td>
          		</tr> 
				<tr>
					<td><s:text name="rule.list.date" /><s:text name="label.colon" /></td>
          			<td><s:date name="createDate" format="yyyy-MM-dd HH:mm:ss" /></td>
          			
					<td><s:text name="rule.list.creater" /><s:text name="label.colon" /></td>
          			<td><s:property value="createName"/></td>
          		</tr> 
				<tr>
					<td><s:text name="rule.list.reversion" /><s:text name="label.colon" /></td>
          			<td><s:date name="reversionDate" format="yyyy-MM-dd HH:mm:ss" /></td>
          			
          			<td><s:text name="rule.list.termNum" /><s:text name="label.colon" /></td>
          			<td><s:property value="termNum" /></td>
          		</tr> 
			</table> 
		  </s:iterator>
		</fieldset>
	</td> 
	</tr>
	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<fieldset class="jui_fieldset" width="100%">
				<LEGEND><s:text name="label.detail.term" /></LEGEND>
				<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
					<tr class="oracolumncenterheader">
						<td nowrap width="12%"><div align="center"><s:text name="fixplan.termid"/></div></td>
						<td nowrap width="12%"><div align="center"><s:text name="rule.list.status"/></div></td>
						<td nowrap width="12%"><div align="center"><s:text name="fixplan.termid"/></div></td>
						<td nowrap width="12%"><div align="center"><s:text name="rule.list.status"/></div></td>
						<td nowrap width="12%"><div align="center"><s:text name="fixplan.termid"/></div></td>
						<td nowrap width="12%"><div align="center"><s:text name="rule.list.status"/></div></td>
					</tr>
					<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					<s:iterator value="regulationDeliverList" id="regulationDeliver" status="status">
						<td nowrap align="center">
							<s:property value="termid" />
						</td>
						<td nowrap align="center">
							<tm:dataDir beanName="regulationDeliver" property="status" path="ruleMgr.reguStatus" />
						</td>
						<s:if test="#status.count%3 == 0">
							<s:if test="(#status.count/3)%2 == 0">
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
			</fieldset>
		</td>
	</tr>
	 
	<tr>
		<td align="center"> 
			<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		</td> 
	</tr>  
</table> 
 </form>
  
</body>  
</html> 