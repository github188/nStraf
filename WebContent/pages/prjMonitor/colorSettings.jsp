<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.*,java.util.HashMap,java.util.Map,java.util.Map.Entry"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store" />
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<link href="../../css/chart.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery.simple-color.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript">

$(function(){
	$(".monFlag").each(function(){
		if($(this).val()==1){
			$(this).attr("checked",true);
		}else{
			$(this).attr("checked",false);			
		}
	})

	$('.simple_color').simpleColor({
		livePreview:true,
		displayColorCode:true,
		columns:11,
		cellWidth:30,
		cellHeight:30,
		colors:['008899',
		        '00A381',
		        '01562E',
		        '1E50A2',
		        '2CA9E1',
		        '333631',
		        '3EB370',
		        '43676B',
		        '44617B',
		        '4D4398',
		        '513743',
		        '544A47',
		        '583823',
		        '5A79BA',
		        '698338',
		        '742414',
		        '74325C',
		        '767C6B',
		        '7B4171',
		        '7B6C3E',
		        '80ABA9',
		        '89C3EB',
		        '8B968D',
		        '8D654A',
		        '93CA76',
		        '97524E',
		        '9D896C',
		        '9E8B8E',
		        'A59432',
		        'A6A5C4',
		        'B44C97',
		        'B79A5A',
		        'BB5548',
		        'BCE2E8',
		        'BF794E',
		        'C4A3BF',
		        'C97586',
		        'D0576B',
		        'DBCB24',
		        'DBD0E6',
		        'E5B422',
		        'E83A29',
		        'E95295',
		        'EA5506',
		        'EA6924',
		        'ED817C',
		        'EDBBCB',
		        'EFAB93',
		        'F6B977',
		        'FED900']
	});
})

function swapFlag(ele){
	if(ele.checked){
		$(ele).parent().find("input[type=hidden]").val("1");
	}else{
		$(ele).parent().find("input[type=hidden]").val("0");
	}
}

function submit(){
	colorForm.submit();
}
</script>
<body id="bodyid" leftmargin="10px" topmargin="0" onload="init()">
<form name="colorForm"
		action="<%=request.getContextPath()%>/pages/prjMonitor/colorSettings!saveColorSettings.action"
		method="post">
<table class="deptColorTable">
	<thead>
		<tr>
			<th>部门</th>
			<th>颜色</th>
			<th>是否监控</th>
		</tr>
	</thead>
	<s:iterator value="deptColorList" var="deptColorBean" status="dst">
		<tr>
			<td align="center" >
				<input style="text-align: center;" readonly="true"  value='<s:property value="#deptColorBean.deptName"/>'  />
				<input type="hidden" name="deptId" value='<s:property value="#deptColorBean.deptId"/>'   />
			</td>
			<td align="center">
				<input type="hidden" name="deptColorId" value='<s:property value="#deptColorBean.deptColorId"/>' />
				<input class='simple_color' name="deptColorVal" value='#<s:property value='#deptColorBean.deptColorVal'/>' />
			</td>
			<td align="center">
				<input type="hidden" name="monFlag" value="<s:property value='#deptColorBean.monFlag' />" />
				<input class="monFlag" type="checkbox"  onclick="swapFlag(this)" value="<s:property value='#deptColorBean.monFlag' />" />
			</td>
		</tr>
	</s:iterator>
	<tr><td><br /></td></tr>
	<tr><td colspan="4" align="center"><input type="button" value="保存" onclick="submit()" /></td></tr>
</table>
</form>
</body>
</html>