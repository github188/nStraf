<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
</head>
<body id="bodyid" leftmargin="0" topmargin="10" style="text-align: center">
	<font color="red">错误信息:<s:property value="#request.downfileMessage"/></font>
</body>
</html>