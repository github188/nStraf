<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 250) {
					alert("你输入的字数超过250个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 250);
				}
			}
		}
	}

	function listenKey() {
		if (document.addEventListener) {
			document.addEventListener("keyup", getKey, false);
		} else if (document.attachEvent) {
			document.attachEvent("onkeyup", getKey);
		} else {
			document.onkeyup = getKey;
		}
	}
	listenKey();

	
	function init(){
		var type='<s:property value="fixAsset.type"/>';
		var status='<s:property value="fixAsset.status"/>';
		document.getElementById("type").value=type;
		document.getElementById("status").value=status;
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10" onload="init()">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/behavior/behaviorinfo!save.action"
		method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
						
						<table width="90%" class="input_table">
							<tr>
								<td class="input_tablehead"> 固定资产登记 </td>
							</tr>
							<tr>
								<td width="15%" class="input_label2"><font
									color="#000000">资产编号：</font></td>
								<td bgcolor="#ffffff" width="35%"><input
									name="fixAsset.no" type="text" id="no"
									style="width: 100%;" readonly value='<s:property value="fixAsset.no"/>'/></td>
								<td  width="15%" class="input_label2"><font
									color="#000000">资产类型：</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<select name="fixAsset.type" id="type" style="width:100%" disabled>
										<option value="">全部</option>
										<option value="0">PC</option>
										<option value="1">笔记本</option>
										<option value="2">服务器</option>
										<option value="3">路由</option>
										<option value="4">键盘</option>
										<option value="5">鼠标</option>
									</select>
								</td>
							</tr>
							<tr>
								<td  width="15%" class="input_label2"><font
									color="#000000">资产名称：</font></td>
								<td bgcolor="#FFFFFF" width="35%"><input
									name="fixAsset.name" type="text" id="name"
									style="width: 100%;" readonly value='<s:property value="fixAsset.name"/>'/></td>
								<td class="input_label2" width="15%"><font
									color="#000000">状态：</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<select name="fixAsset.status" id="status" style="width:100%" disabled>
										<option value="">全部</option>
										<option value="0">闲置</option>
										<option value="1">领用</option>
										<option value="2">无效</option>
									</select>
								</td>
							</tr>
							<tr>
								<td height="44" class="input_label2" width="15%">用途：</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea rows="3" 
										cols="80" name="fixAsset.use" type="text" id="use" wrap="virtual"
										readonly><s:property value="fixAsset.use"/></textarea></td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">入库日期：</font></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.indate" type="text" id="indate" readonly=true
											style="width:100%"
											value='<s:date name="fixAsset.indate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">资产管理员：</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<input name="fixAsset.inman" type="text" id="inman" readonly value='<s:property value="fixAsset.inman"/>' style="width: 100%;" />
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">领用人：</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									 <input name="fixAsset.useman" type="text" id="useman" readonly value='<s:property value="fixAsset.useman"/>' style="width: 100%;" />
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">领用日期：</font></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.usedate" type="text" id="usedate" readonly=true
											style="width:100%;"
											value='<s:date name="fixAsset.usedate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">预计归还日期：</font></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.expectdate" type="text" id="expectdate" readonly=true
											style="width:100%;"
											value='<s:date name="fixAsset.expectdate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">归还日期：</font></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.factdate" type="text" id="factdate" readonly=true
											style="width:100%;"
											value='<s:date name="fixAsset.factdate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
							</tr>
							<tr>
								<td height="44" class="input_label2">领用原因：</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.usereason" type="text" id="usereason" rows="3"
										cols="80" readonly><s:property value='fixAsset.usereason'/></textarea></td>
							</tr>
							<tr>
								<td height="44" class="input_label2">领用记录：</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.uselog" type="text" id="uselog" rows="6"
										cols="80" readonly="readonly"><s:property value='#request.record'/></textarea></td>
								<input type="hidden" id="id" name="fixAsset.id" value="<s:property value='fixAsset.id'/>"
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
	
</body>
</html>
