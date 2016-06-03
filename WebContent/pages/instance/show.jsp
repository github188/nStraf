<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	function save() {

		if (document.getElementById("create_date").value.trim() == "") {
			alert("请输入提交日期");
			return;
		}
		if (document.getElementById("create_man").value.trim() == "") {
			alert("请输入提交者");
			return;
		}
		if (document.getElementById("summary").value.trim() == "") {
			alert("请输入案例名称");
			return;
		}
		window.returnValue = true;
		reportInfoForm.submit();
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function selDate(id) {
		var obj = document.getElementById(id);
		ShowDate(obj);
	}

	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	function checkHdl(val) {
		if (val.value.trim() == "" || val.value == null) {
			alert("必填项不能为空");
		}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0" style="word-wrap: break-word; word-break: break-all;">
			<tr>
				<td>
						<table class="input_table">
							<tr>
							<td class="input_tablehead">
								爱心小鱼
							</td>
							</tr>
							<tr>
								<td class="input_label2" width="17%" >
									<div >提出日期：</div>
								</td>
								<td colspan="2" bgcolor="#FFFFFF">
									<s:date name="instance.create_date" format="yyyy-MM-dd"/>
									</td>
								<td width="15%" class="input_label2">
									<div >提出者：</div>
								</td>
								<td bgcolor="#FFFFFF" width="25%"><s:property value="instance.create_man"/>
								</td>

							</tr>
							<tr>
								<td class="input_label2"><div >接受者：</div></td>
								<td colspan="2" bgcolor="#FFFFFF">
								<s:property value='instance.embracer_man'/></td>
								<td class="input_label2"><div >类别：</div></td>
								<td bgcolor="#FFFFFF">
								<s:property value='instance.category'/></td>

							</tr>
							<tr>
								<td class="input_label2">
									<div >概要：</div>
								</td>
								<td colspan="5" bgcolor="#FFFFFF">
								<s:property value="instance.summary"/>
								</td>
							</tr>


							<tr>
								<td class="input_label2">
									<div >事件描述：</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5">
									<div>
										<textarea cols="77" rows="6" name="instance.description"
											id="description" readonly style="width:100%"><s:property
												value="instance.description" /></textarea>
									</div>
								</td>
							</tr>


							<tr>
								<td width="17%" class="input_label2">建议/备注：</td>
								<td bgcolor="#FFFFFF" colspan="5"><textarea cols="77"
										rows="6" name="instance.solution" id="solution" readonly style="width:100%"><s:property
											value="instance.solution" /></textarea></td>
							</tr>
							<tr>
		                  	 <td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">确认说明</font></div></td>
		                     	<td colspan="7" nowrap bgcolor="#FFFFFF" >
				                     <div align="left">
				                     <!-- 部门经理级别以上可以编辑 -->
					                  	 <s:if test="#request.hasComfirmRight==true">
							             	<textarea name="instance.confirmDesc" type="text"  rows="6" style="width:99%"><s:property value='instance.confirmDesc'/></textarea>
					                  	 </s:if>
					                  	 <s:else>
							             	<textarea style="background-color: #E3E3E3" readonly="readonly" name="instance.confirmDesc" type="text"  rows="6" style="width:99%"><s:property value='instance.confirmDesc'/></textarea>
					                  	 </s:else>
				                     </div>
		                       </td>
	                  </tr>
						</table>
				</td>
			</tr>
		</table>
		<br /> <br />
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal();" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
</body>
</html>
