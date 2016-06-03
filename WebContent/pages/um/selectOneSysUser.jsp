<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>operation info</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript"> 
	function SelectObj(userid) { 
	    rValue = new Array(userid);
	    formsubmit();
	}
	function formsubmit(){
		  parent.returnValue = rValue;
		  parent.close();
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
<form action="/pages/um/sysUserInfo!getUsrUsrgrp.action"   method="post">
  <table width="100%" align="center" cellPadding="0" cellSpacing="0">
  	<tr>
	<td>
		<fieldset class="jui_fieldset">
			<LEGEND><s:text name="label.audit.list" /></LEGEND>
			<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
					<tr class="oracolumncenterheader">
						<td nowrap width="20%"><s:text name="label.select" /></td>
						<td nowrap width="80%"><div align="center"><s:text name="queryCondition.operIden"/></div></td>
					</tr>
					<s:iterator value="usrList" id="usrUsrgrp" status="rowstatus">
						<s:if test="#rowstatus.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						</s:if><s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
						</s:else>
							<td nowrap align="center">
								<input type="radio" name="reversionName" onclick="SelectObj('<s:property value="userid" />')"/>
							</td>
							<td nowrap align="center">
								<s:property value="userid" />
							</td>
						</tr>
					</s:iterator>
				</table>
				</fieldset>
			</td>
		</tr>
	</table>
</form>
</body>
</html>