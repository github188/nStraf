<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="cn.grgbanking.feeltm.testrecord.domain.TestQuestion"%>
<script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>certification query</title>
<style type="text/css">
<!--
.STYLE8 {
	font-size: small
}
-->
</style>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {
		document.getElementById("ok").disabled = true;
		window.returnValue = true;
		reportInfoForm.submit();

	}

	function closeModal() {
		if (!confirm('您确认关闭此页面吗？')) {
			return;
		}
		window.close();
	}

	function check() {
		if (document.getElementById("certificationNo").value.trim() == "") {
			alert("请输入认定编号");
			$("#certificationNo").focus();
			return;
		}
		var url = "cerinfo!check.action";
		var params = {
			deviceNo : $("#certificationNo").val()
		};
		jQuery.post(url, params, $(document).callbackFun1, 'json');
	}

	function selDate(id) {
		var obj = document.getElementById(id);
		ShowDate(obj);
	}

	function numVali(itemName) {
		var ret = 0, tmp = 0, WorkLoad = 0, TesterSum = 0;
		if (!Validate(itemName.value, "Double")) {
			itemName.value = 0;
			alert("请输入数字");
		}
		WorkLoad = parseFloat(document.getElementById("testRecord.WorkLoad").value);
		TesterSum = parseFloat(document.getElementById("testRecord.TesterSum").value);
		if (WorkLoad > 0 && TesterSum > 0) {
			ret = WorkLoad / TesterSum;
			document.getElementById("testRecord.TestTimeSum").value = ret;
			document.getElementById("testRecord.TestTimeSum").value = document
					.getElementById("testRecord.TestTimeSum").value.replace(
					/(\.\d)\d+/ig, "$1");
		} else {
			document.getElementById("testRecord.TestTimeSum").value = 0;
		}

	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/TestRecord/TestRecordinfo!update.action"
		method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
						<legend>
							<s:text name="软件产品测试情况记录" />
						</legend>
						<table width="95%" align="center" border="0" cellspacing="1"
							cellpadding="1" bgcolor="#583F70">
							<br />
							<tr>
								<td align="center" width="8%" bgcolor="#999999"><div
										align="center">编号</div></td>
								<td bgcolor="#FFFFFF" colspan="5">
									<input name="question.id" type="hidden" id="id" size="100" maxlength="100" value='<s:property value="question.id"/>'>
									<input name="question.serialNo" type="text" id="no" size="100" maxlength="100" value='<s:property value="question.serialNo"/>'>
								</td>
							</tr>
							<tr>
								<td align="center" width="8%" bgcolor="#999999"><div
										align="center">项目名称</div></td>
								<td bgcolor="#FFFFFF" colspan="5" width="35%"><input
									name="question.ProjectName" type="text" id="ProjectName"
									size="100" maxlength="100" value='运通信息作战系统 '></td>
							</tr>
							<tr>
								<td align="center" width="8%" bgcolor="#999999"><div
										align="center">标题</div></td>
								<td bgcolor="#FFFFFF" colspan="5"><input
									name="question.title" type="text" id="title" size="100"
									maxlength="100" value='<s:property value="question.title"/>'></td>
							</tr>
							<tr>
								<td align="center" width="8%" bgcolor="#999999"><div
										align="center">模块</div></td>
								<td bgcolor="#FFFFFF" nowrap="nowrap" colspan="5"><input
									name="question.modualName" type="text" id="modualName"
									size="100" maxlength="100"
									value='<s:property value="question.modualName"/>'></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#999999"><div align="center">描述</div></td>
								<td colspan="5" bgcolor="#FFFFFF"><textarea cols="85"
										rows="7" name="question.questionDesc" id="questionDesc"><s:property
											value="question.questionDesc" /></textarea></td>
							</tr>
							<tr>
								<td align="center" bgcolor="#999999"><div align="center">提出人</div></td>
								<td bgcolor="#FFFFFF" nowrap="nowrap"><select
									name="question.findMan" id="findMan" style="width: 150px">
										<option value="">---- 请选择 ----</option>
										<option value="陈天水">陈天水</option>
										<option value="卢海燕">卢海燕</option>
										<option value="涂玲">涂玲</option>
										<option value="曾志慧">曾志慧</option>
										<option value="李平">李平</option>
										<option value="汪腾蛟">汪腾蛟</option>
										<option value="徐文山">徐文山</option>
										<option value="吴杰">吴杰</option>
										<option value="郑权盛">郑权盛</option>
										<option value="张王府">张王府</option>
										<option value="曾迪">曾迪</option>
								</select>
								<script>
									$("#findMan").find("option").each(function(){
										if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getFindMan()%>'){
											$(this).attr('selected','selected');
										}else{
											$(this).removeAttr('selected');
										}
									});
								</script>
								</td>
								<td align="center" bgcolor="#999999"><div align="center">提出日期</div></td>
								<td bgcolor="#FFFFFF"><input name="question.findDate"
									type="text" id="findDate"
									value='<s:date  name="question.findDate" format="yyyy-MM-dd" />' class="MyInput"
									issel="true" isdate="true" onFocus="ShowDate(this)"
									dofun="ShowDate(this)"></td>
								<td align="center" width="8%" bgcolor="#999999"><div
										align="center">状态</div></td>
								<td bgcolor="#FFFFFF">
									<select
									name="question.questionStatus" id="questionStatus"
									style="width: 150px">
										<option value="">---- 请选择 ----</option>
										<option value="新建">新建</option>
										<option value="已指派">已指派</option>
										<option value="已解决">已解决</option>
										<option value="已拒绝">已拒绝</option>
										<option value="测试通过">测试通过</option>
										<option value="测试未通过">测试未通过</option>
										<option value="已关闭">已关闭</option>
								</select>
								
								<script>
									$("#questionStatus").find("option").each(function(){
										if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getQuestionStatus()%>'){
											$(this).attr('selected','selected');
										}else{
											$(this).removeAttr('selected');
										}
									});
								</script>
								</td>
							</tr>
							
							<tr>
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">更新人</div></td>
								<td bgcolor="#FFFFFF" width="23%">
									<select
										name="question.updateMan" id="updateMan" style="width: 150px">
											<option value="">---- 请选择 ----</option>
											<option value="陈天水">陈天水</option>
											<option value="卢海燕">卢海燕</option>
											<option value="涂玲" >涂玲</option>
											<option value="曾志慧">曾志慧</option>
											<option value="李平">李平</option>
											<option value="汪腾蛟" >汪腾蛟</option>
											<option value="徐文山" >徐文山</option>
											<option value="吴杰">吴杰</option>
											<option value="郑权盛">郑权盛</option>
											<option value="张王府">张王府</option>
											<option value="曾迪">曾迪</option>
									</select>
									<script>
										$("#updateMan").find("option").each(function(){
											if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getUpdateMan()%>'){
												$(this).attr('selected','selected');
											}else{
												$(this).removeAttr('selected');
											}
										});
									</script>
								</td>
								
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">处理日期</div></td>
								<td bgcolor="#FFFFFF" width="23%"><input
									name="question.soloveDate" type="text" id="soloveDate"
									value='<s:date  name="question.soloveDate" format="yyyy-MM-dd" />'
									class="MyInput" issel="true" isdate="true"
									onFocus="ShowDate(this)" dofun="ShowDate(this)"></td>
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">完成度</div></td>
								<td bgcolor="#FFFFFF" width="23%"><select name="question.finishRate" id="finishRate" id style='width:150px'>
										<option value="" selected="true">---- 请选择 ----</option>
										<option value="100%">100%</option>
			                            <option value="95%">95%</option>
			                            <option value="90%">90%</option>
			                            <option value="85%">85%</option>
			                            <option value="80%">80%</option>
			                            <option value="75%">75%</option>
			                            <option value="70%">70%</option>
			                            <option value="65%">65%</option>
			                            <option value="60%">60%</option>
			                            <option value="55%">55%</option>
			                            <option value="50%">50%</option>
			                            <option value="45%">45%</option>
			                            <option value="40%">40%</option>
			                            <option value="35%">35%</option>
			                            <option value="30%">30%</option>
			                            <option value="25%">25%</option>
			                            <option value="20%">20%</option>
			                            <option value="15%">15%</option>
			                            <option value="10%">10%</option>
			                            <option value="5%">5%</option>
			                            <option value="0%">0%</option>
								</select>
								<script>
									$("#finishRate").find("option").each(function(){
										if($(this).val()=='<s:property value="question.finishRate"/>'){
											$(this).attr('selected','selected');
										}else{
											$(this).removeAttr('selected');
										}
									});
								</script>
								</td>
							</tr>
							<tr>
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">指派给</div></td>
								<td bgcolor="#FFFFFF" width="23%">
									<select
										name="question.soloveMan" id="soloveMan" style="width: 150px">
											<option value="">---- 请选择 ----</option>
											<option value="陈天水">陈天水</option>
											<option value="卢海燕">卢海燕</option>
											<option value="涂玲" >涂玲</option>
											<option value="曾志慧">曾志慧</option>
											<option value="李平">李平</option>
											<option value="汪腾蛟" >汪腾蛟</option>
											<option value="徐文山" >徐文山</option>
											<option value="吴杰">吴杰</option>
											<option value="郑权盛">郑权盛</option>
											<option value="姚炜">姚炜</option>
											<option value="张王府">张王府</option>
											<option value="曾迪">曾迪</option>
									</select>
									<script>
										$("#soloveMan").find("option").each(function(){
											if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getSoloveMan()%>'){
												$(this).attr('selected','selected');
											}else{
												$(this).removeAttr('selected');
											}
										});
									</script>
								</td>
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">部署情况</div></td>
								<td bgcolor="#FFFFFF" width="23%">
									<select
										name="question.deployStatus" id="deployStatus" style="width: 150px">
											<option value="">---- 请选择 ----</option>
											<option value="未部署">未部署</option>
											<option value="已部署">已部署</option>
									</select>
									<script>
										$("#deployStatus").find("option").each(function(){
											if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getDeployStatus()%>'){
												$(this).attr('selected','selected');
											}else{
												$(this).removeAttr('selected');
											}
										});
									</script>
								</td>
								<td align="center" width="10%" bgcolor="#999999"><div
										align="center">紧急度</div></td>
								<td bgcolor="#FFFFFF" width="23%">
									<select name="question.urge" id="urge" style="width:150px">
										<option value="">----请选择 ----</option>
										<option value="低">低</option>
										<option value="中">中</option>
										<option value="高">高</option>
										<option value="紧急">紧急</option>
									</select>
									<script>
									$("#urge").find("option").each(function(){
										if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getUrge()%>'){
											$(this).attr('selected','selected');
										}else{
											$(this).removeAttr('selected');
										}
									});
								</script>
								</td>
								</tr>
							<tr>
								<td align="center" bgcolor="#999999">缺陷等级:</td>
								<td colspan="5" bgcolor="#FFFFFF">
									<select name="question.buglevel" id="buglevel" style='width: 150px'>
										<option value="" >----请选择 ----</option>
										<option value="致命">致命</option>
										<option value="严重">严重</option>
										<option value="一般">一般</option>
										<option value="警告">警告</option>
										<option value="建议">建议</option>
									</select>
									<script>
									$("#buglevel").find("option").each(function(){
										if($(this).val()=='<%=((TestQuestion)request.getAttribute("question")).getBuglevel()%>'){
											$(this).attr('selected','selected');
										}else{
											$(this).removeAttr('selected');
										}
									});
								</script>
								</td>
							</tr>
							<tr>
								<td align="center" bgcolor="#999999"><div align="center">备注</div></td>
								<td colspan="5" bgcolor="#FFFFFF"><textarea cols="85"
										rows="10" name="question.note" id="note"><s:property
											value="question.note" /></textarea></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onclick="save();" image="../../images/share/yes1.gif"> <input
					type="button" name="return" id="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>
