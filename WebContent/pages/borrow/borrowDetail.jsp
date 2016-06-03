<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
	$(function(){
		var getsum=$("#amount").val();
		var showsum=fmoney(getsum,2);
		if(getsum!=""){
			$("#showamount").html(showsum);
		}
		var getsum=$("#approveSum").val();
		var showsum=fmoney(getsum,2);
		if(getsum!=""){
			$("#showapproveSum").html(showsum);
		}
	});
	
	//金额千分号分隔
	function fmoney(s, n){   
	   n = n > 0 && n <= 20 ? n : 2;   
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
	   var l = s.split(".")[0].split("").reverse(),   
	   r = s.split(".")[1];   
	   t = "";   
	   for(i = 0; i < l.length; i ++ )   
	   {   
	      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
	   }   
	   return t.split("").reverse().join("") + "." + r;   
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="reportInfoForm" action="<%=request.getContextPath()%>/pages/borrow/borrowinfo!update.action" method="post">
		<table width="95%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>	
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">查看借款信息</td>
							<input type="hidden" id="id" name="borrow.id" value='<s:property value="borrow.id"/>' />
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="6">
								姓名：<s:property value="borrow.userman"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								部门：<s:property value="borrow.detname"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								借款状态：
								<s:if test='#request.borrow.status=="0"'>财务审核通过，已发款，代还款</s:if>
								<s:elseif test='#request.borrow.status=="1"'>财务审核未通过</s:elseif>
								<s:elseif test='#request.borrow.status=="2"'>待审核</s:elseif>
								<s:elseif test='#request.borrow.status=="3"'>新增</s:elseif>
								<s:elseif test='#request.borrow.status=="4"'>还款结束</s:elseif>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								借款类型：
								<s:if test='#request.borrow.type=="0"'>出差</s:if>
								<s:elseif test='#request.borrow.type=="1"'>备用金</s:elseif>
								<input type="hidden" id="borrowId" name="borrowId" value="<s:property value="borrow.id"/>" />
								<input type="hidden" id="borrow.id" name="id" value="<s:property value="borrow.id"/>" />
							</td>
						</tr>
						<tr>
							<td class="input_label2">申请借款金额:</td>
							<td bgcolor="#FFFFFF" style="width:18%;"">
								<input name="amount" type="hidden" id="amount" value='<s:property value="borrow.amount"/>'/>
								<span id="showamount"></span>元
							</td>
						   	<td class="input_label2">出差地:</td>
							<td bgcolor="#FFFFFF" style="width:19%;">
								<s:property value="borrow.tripcity"/>
							</td>
							<td class="input_label2">借款日期:</td>
			                <td bgcolor="#ffffff" style="width:18%;">
			                	<s:date name="borrow.borrowdate" format="yyyy-MM-dd"/>
						   	</td>
						</tr>					
						<tr>
							<td class="input_label2">核准借款金额:</td>
							<td bgcolor="#FFFFFF">
								<input name="approveSum" type="hidden" id="approveSum" value='<s:property value="borrow.approveSum"/>'/>
								<span id="showapproveSum"></span>元
							</td>
							<td class="input_label2">财务审核人员:</td>
			                <td bgcolor="#ffffff">
			                	<s:property value="borrow.updatemanid"/>
						   	</td>
							<td class="input_label2">预计还款日期:</td>
			                <td bgcolor="#ffffff">
			                	<s:date name="borrow.expectedRepaydate" format="yyyy-MM-dd"/>
						   	</td>
						</tr>
						<tr>
							<td class="input_label2">借款原因：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="borrow.borrowReason" id="borrowReason" cols="77" rows="3" style="width:100%" readonly="readonly"><s:property value="borrow.borrowReason"/></textarea>
							</td>
						</tr>
						<tr>
							<td class="input_label2">财务审核：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="finrecord" id="finrecord" cols="77" rows="3" style="width:100%" readonly="readonly"><s:property value="#request.finRecord"/></textarea>
							</td>
						</tr>
						<tr>
							<td class="input_label2">审核记录：</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<textarea name="record" cols="73" rows="4" id="record" style="width:100%" readonly="readonly"><s:property value="#request.record"/></textarea>
							</td>
						</tr>
					</table> 
				</td>
			</tr>
		</table>
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
