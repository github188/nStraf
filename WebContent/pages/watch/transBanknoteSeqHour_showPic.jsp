<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %><html> 
 <head>
 	<title>交易序列号图片查看</title>
 	<STYLE type="text/css">
 		.noteSeq {
 			font-family: 幼圆, sans-serif;
			font-weight: bold;
			FONT-SIZE: 18pt; 
 		}
 	</STYLE> 
 </head>
<link href="../../css/css_v2.css" type="text/css" rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>

<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="transBanknoteSeqHourForm" action="<%=request.getContextPath() %>/pages/watch/transBanknoteSeqHour!save.action" method="post">
<fieldset class="jui_fieldset" width="100%">
<legend> <s:text name="wacth.checkPic"/> </legend>
<table>
	<tr>
		<td>
			<div align="left"><img src="../../images/noteType.gif" /></div>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
	<tr>
		<td nowrap width="20%" class="oracolumncenterheader"><s:text name="watch.seriaNo" /></td>
		<td nowrap width="20%" class="oracolumncenterheader"><s:text name="watch.note_pic" /></td>
		<td  nowrap width="8%" class="oracolumncenterheader"><s:text name="watch.checkResult" /></td>
		<td  nowrap width="8%" class="oracolumncenterheader"><s:text name="watch.note_type" /></td>
		<td nowrap width="8%" class="oracolumncenterheader"><s:text name="rule.list.moneyType" /></td>
		<td nowrap width="8%" class="oracolumncenterheader"><s:text name="rule.list.denomination" /></td>
	</tr>
	<s:iterator value="objList" id="transBanknoteSeqHour" status="rowstatus">
	<s:if test="#rowstatus.odd == true">
		<tr class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	</s:if><s:else>
		<tr class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	</s:else>
		<td class="noteSeq" align="center">
			<s:if test='noteType=="0"'>
				<font color="#00AA00">
			</s:if> <s:elseif test='noteType=="1"'>
				<font color="#FF0000">
			</s:elseif> <s:elseif test='noteType=="3"'>
				<font color="#FF9900">
			</s:elseif><s:else>
				<font color="#0000AA">
			</s:else>
					<s:property value="seriaNo" />
				</font>
		</td>
		<td align="center"><img src="<%=request.getContextPath() %>/upload/<s:property value='urlName'/>"/></td>
		<td align="center">
			<tm:dataDir beanName="transBanknoteSeqHour" property="checkResult" path="transMgr.checkResult" scope="request"/>
		</td>
		<td align="center">
			<tm:dataDir beanName="transBanknoteSeqHour" property="noteType" path="transMgr.noteType" />
		</td>
		<td align="center">
			<tm:dataDir beanName="transBanknoteSeqHour" property="currency" path="ruleMgr.moneyType" />
		</td>
		<td align="center">
			<tm:dataDir beanName="transBanknoteSeqHour" property="denomination" path="ruleMgr.moneyDenomination" />
		</td>
	</s:iterator>
	 </tr>
 </table>
 </fieldset>
</body>  
</html> 

