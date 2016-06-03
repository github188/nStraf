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

<script language="javascript">
	/* function closeModal() {

		window.close();
	} */
</script>

<body id="bodyid" leftmargin="0" topmargin="0">

	<table width="90%" align="center" cellPadding="0" cellSpacing="0"
		height="100%">
		<input type="hidden" name="userId" id="userId"
			value="<s:property value='userId'/>" />
		<tr>
			<td>

				<table class="input_table">
					<tr>
						<td class="input_tablehead">查看客户详情信息</td>
					</tr>
					<s:iterator value="customList" id="customInfo">
						<tr>
							<td width="15%" class="input_label2">
								<div align="center">客户名称：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="client" /></td>
							<td width="15%" class="input_label2">
								<div align="center">联系人：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="mouthPiece" /></td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">联系电话：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="tel" /></td>
							<td width="10%" class="input_label2">
								<div align="center">联系地址：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="address" /></td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">创建时间：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:date name="creatDate" format="yyyy-MM-dd" /></td>
							<td width="10%" class="input_label2">更新日期：</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:date name="upDate" format="yyyy-MM-dd" /></td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">
								<div align="center">联系邮箱：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="mail" /></td>
							<td width="10%" class="input_label2">
								<div align="center">更新人：</div>
							</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left">
								<s:property value="updateMan" /></td>
						</tr>
						<tr>
							<td width="10%" class="input_label2">项目列表：</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left"
								colspan="3"><textarea cols="50" rows="6" readonly>
										<s:property value="prjList" />							
							</textarea></td>
						</tr>

						<tr>
							<td width="10%" class="input_label2">备注：</td>
							<td bgcolor="#FFFFFF" width="35%" height="20" align="left"
								colspan="3"><textarea cols="50" rows="6" readonly>
									<s:property value="note" />
								</textarea></td>
						</tr>
						<tr>
					</s:iterator>
				</table></td>
		</tr>
		<tr>
			<td align="center"><input type="button" name="return"
				value='<s:text  name="button.close"/>' class="MyButton"
				onclick="closeModal()" image="../../images/share/f_closed.gif">
			</td>
		</tr>
	</table>

</body>
</html>