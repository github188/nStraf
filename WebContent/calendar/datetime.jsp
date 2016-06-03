<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
  response.setHeader("Cache-Control","no-store");
  response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
%>
<HTML>
<Head>
<title><s:text name="selectDate.title"/></title>
<script language="JavaScript" src="selectDate.js"></script>
<script language="javascript">months = new Array(<s:text name="calendar.months"/>);
weekDays = new Array(<s:text name="calendar.weekDays"/>);
yearLabel='<s:text name="calendar.year"/>';
hourLabel='<s:text name="calendar.hour"/>';
minuteLabel='<s:text name="calendar.minute"/>';
secondLabel='<s:text name="calendar.second"/>';
okLabel='<s:text name="calendar.okLabel"/>';
clearLabel='<s:text name="calendar.clearLabel"/>';
cancelLabel='<s:text name="calendar.cancelLabel"/>';
</script>
<STYLE type=text/css>A {
	COLOR: #000000; FONT-SIZE: 10pt; TEXT-DECORATION: none
}
.country {
	FONT-SIZE: 9pt; LETTER-SPACING: 2px; LINE-HEIGHT: 12pt
}
A:hover {
	COLOR: #367d89; TEXT-DECORATION: none
}
.style {
	FONT-SIZE: 9pt; LINE-HEIGHT: 12px
}
.font1 {
	FONT-SIZE: 10pt
}
</STYLE>
</HEAD>
<BODY onload=load()>
<SCRIPT>
function fkeydown()
{
	if(event.keyCode==27){
		event.returnValue = null;
		window.returnValue = null;
		window.close();
	}
}
document.onkeydown=fkeydown;
function load(){
toggleDatePicker('daysOfMonth',1);
}
</SCRIPT>
<div id='daysOfMonth' style="POSITION: absolute; left: 0px; top: 0px; width: 300px; height: 200px">
</BODY>
</HTML>
