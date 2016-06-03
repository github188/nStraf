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
		/* function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		} */
		function save(){
             /*         
			if(document.getElementById("createdate").value.trim()=="")
			{
				alert("请输入提出日期");
				return;
			}
			if(document.getElementById("createman").value.trim()=="")
			{
				alert("请输入提出者");
				return;
			}
			if(document.getElementById("summary").value.trim()=="")
			{
				alert("请输入概要");
				return;
			}
			if(document.getElementById("riskdesc").value.trim()=="")
			{
				alert("请输入描述");
				return;
			}
			if(document.getElementById("suggest").value.trim()=="")
			{
				alert("请输入建议措施");
				return;
			}
			if(document.getElementById("type").value.trim()=="")
			{
				alert("请选择类别");
				return;
			}
			*/
			document.getElementById("ok").disabled=true;
			window.returnValue=true;
			reportInfoForm.submit();
			
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
    function selDate(id)
    {
    	var obj=document.getElementById(id);
    	ShowDate(obj);
    } 

	String.prototype.trim = function()
	{
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
	</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!update.action"
		method="post">
		<input type="hidden" name="prjRisk.id"
			value='<s:property value="prjRisk.id"/>'> <input
			name="usernameHidden" type="hidden" id="usernameHidden"
			value="<%=((cn.grgbanking.feeltm.login.domain.UserModel) session
					.getAttribute("tm.loginUser")).getUsername()%>" />
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">项目风险</td>
						</tr>
						<tr>
							<td class="input_label2" width="13%">
								<div >
									提出日期 ：
								</div></td>
							<td bgcolor="#FFFFFF" width="27%"><s:date
									name="prjRisk.createdate" format="yyyy-MM-dd" /></td>
							<td width="9%" class="input_label2">
								<div >
									提出者：
								</div></td>
							<td bgcolor="#FFFFFF" width="26%"><s:property
									value="prjRisk.createman" />
							</td>
							<td class="input_label2" width="11%"><div >
									状态：
								</div>
							</td>
							<td width="14%" bgcolor="#FFFFFF"><s:property
									value="prjRisk.status" /></td>
						</tr>

						<tr>
							<td class="input_label2">
								<div >
									项目名称 ：
								</div></td>
							<td colspan="3" nowrap bgcolor="#FFFFFF"><s:property
									value="prjRisk.prjname" />
							</td>
							<td class="input_label2"><div >
									类别：
								</div>
							</td>
							<td bgcolor="#FFFFFF"><s:property value="prjRisk.type" />
							</td>


						</tr>
						<tr>
							<td class="input_label2">
								<div >严重性：</div></td>
							<td bgcolor="#FFFFFF" colspan="3"><s:property
									value="prjRisk.pond" /></td>

							<td width="11%" class="input_label2"><div >
									紧急程度：</div>
							</td>
							<td bgcolor="#FFFFFF"><s:property value="prjRisk.urgent" />
							</td>

						</tr>
						<tr>
							<td  class="input_label2">
								<div >
									概要：
								</div></td>
							<td colspan="5" bgcolor="#FFFFFF"><s:property
									value="prjRisk.summary" /></td>
						</tr>
						<tr>
							<td  class="input_label2">
								<div >
									风险描述：
								</div></td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<textarea cols="74" rows="3" name="prjRisk.riskdesc"
										id="riskdesc" readonly>
										<s:property value="prjRisk.riskdesc" />
									</textarea>
								</div></td>
						</tr>

						<tr>
							<td  class="input_label2">建议方案 ：</td>
							<td bgcolor="#FFFFFF" colspan="5"><textarea cols="74"
									rows="3" name="prjRisk.suggest" id="suggest" readonly>
									<s:property value="prjRisk.suggest" />
								</textarea></td>
						</tr>
						<tr>
							<td width="13%" class="input_label2">行动计划：</td>
							<td bgcolor="#FFFFFF" colspan="5"><textarea
									name="prjRisk.plan" cols="74" rows="3" id="plan" readonly>
									<s:property value="prjRisk.plan" />
								</textarea></td>
						</tr>


						<tr>
							<td width="13%" class="input_label2">
								<div >计划完成日期：</div></td>
							<td width="27%" bgcolor="#FFFFFF"><s:date
									name="prjRisk.handleterm" format="yyyy-MM-dd" /></td>
							<td width="13%" class="input_label2">
								<div >实际完成日期：</div></td>
							<td bgcolor="#FFFFFF"><s:date name="prjRisk.factdate"
									format="yyyy-MM-dd" />
							</td>
							<td class="input_label2" width="9%">
								<div >处理者：</div></td>
							<td width="26%" bgcolor="#FFFFFF"><s:property
									value="prjRisk.handleman" /></td>
						</tr>

						<tr>
							<td class="input_label2">解决情况：</td>
							<td bgcolor="#FFFFFF" colspan="5"><textarea
									name="prjRisk.fruit" cols="74" rows="3" id="fruit" readonly>
									<s:property value="prjRisk.fruit" />
								</textarea></td>
						</tr>


					</table></td>
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
	</form>

</body>
</html>
