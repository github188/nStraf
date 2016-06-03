<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html> 
	<head></head>
	<script type="text/javascript">
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10">
<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/moduleDistribute/moduleDistributeInfo!save.action" method="post">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" style="border:solid 1px #cccccc;">
<tr>
	<td align="center">
        <table style="width:95%;" border="0" cellPadding="1" cellSpacing="1">
        	<tr height="30px;">
        		<td style="text-align:left;background-color:#eeeeee;">
        			<span style="margin-left:5px;">收集设置</span>
        		</td>
            </tr>
        	<tr>
        		<td style="text-align:left;">
        			<span style="margin-left:5px;">从
        			<input name="distributeRecord.sdate" type="text" id="sdate" readonly=true size="22" class="dateTimeInput" value='<s:date name="" format="yyyy-MM-dd HH:mm:ss"/>'>
					至
					<input name="distributeRecord.edate" type="text" id="edate" readonly=true size="22" class="dateTimeInput" value='<s:date name="" format="yyyy-MM-dd HH:mm:ss"/>'></span>
        		</td>
            </tr>
        </table>
        <div style="width:95%;text-align:center;margin-bottom:10px;margin-top:10px;">
        	<input type="button" value="下一步：选择发放方式" onclick="selectDistributeType()"/>
        </div>
        <table style="width:95%;" border="0" cellPadding="0" cellSpacing="0" style="text-align:center;" id="othertableinfo" name="othertalbeinfo">
        	<tr height="30px;">
        		<td colspan="2" style="text-align:left;background-color:#eeeeee;">
        			<span style="margin-left:5px;">选择发放问卷方式</span>
        		</td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td colspan="2" style="text-align:left;">手机评估</td></tr>
            <tr>
        		<td style="width:30%;text-align:left;">
        			<img alt="" src="" style="width:150px;height:200px;">
        		</td>
        		<td style="width:70%;text-align:left;">
        			让学员扫一扫，手机填写反馈问卷：
        			<br/>• 投影到幕布上扫 (PPT 图片)
        			<br/>• 打印到教材上扫；
        			<br/>• 打印到铭牌上扫。使用微信、UC浏览器等可以扫二维码的工具皆可。
        		</td>
            </tr>
            <tr><td colspan="2" style="text-align:left;">下载二维码</td></tr>
            <tr height="60px;"><td colspan="2" style="text-align:left;">电脑评估网址</td></tr>
            <tr><td colspan="2" style="text-align:left;"><span>http://www.91pxb.com/feedback/sc-991191-27896-14119</span></td></tr>
            <tr height="60px;"><td colspan="2" style="text-align:left;">书面评估</td></tr>
            <tr><td colspan="2" style="text-align:left;"><span>打印给不能手机评估的学员，只是辛苦您手工录入这些问卷了</span></td></tr>
            <tr><td colspan="2" style="text-align:left;">打印预览</td></tr>
        </table>
        <br/>
        <div style="width:95%;text-align:left;margin-bottom:10px;margin-left:2px;"><input type="button" value="新增" onclick="addOther()"/></div>
        <div style="width:95%;text-align:center;margin-bottom:10px;margin-left:2px;">
        	<!-- <input type="button" value="保存" onclick="saveModule()"/> -->
        	<input type="button" value="关闭" onclick="closeModal();"/>
        	<input type="hidden" id="questionCount" name="questionCount" />
        </div>
	</td>
</tr>
</table>
</form>
</body>  
</html> 

