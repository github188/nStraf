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
	$(function(){
		var modifyRecord="<%=request.getAttribute("modifyRecord")%>";
		if(CheckExprole()){
			modifyRecord = modifyRecord.replace(/<br\/>/g, "\n");
		}
		$("#modifyRecord").html(modifyRecord);
		var record="<%=request.getAttribute("record")%>";
		if(CheckExprole()){
			record = record.replace(/<br\/>/g, "\n");
		}
		$("#record").html(record);
		var approveSum=$("#approveSum").val();
		if(approveSum!=""){
			var showsum=fmoney(approveSum,2);
			$("#approveSumid").html(showsum);
		}
		var sum=$("#sum").val();
		if(sum!=""){
			var showsum=fmoney(sum,2);
			$("#sumid").html(showsum);
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
	
	function printDetail(){
		var expenseId=$("#expenseId").val();
		if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
			window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId);
		}else{
			OpenModal('/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
		}
	}
	function printCost(){
		var expenseId=$("#expenseId").val();
		if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
			window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId);
		}else{
			OpenModal('/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
		}
	}
	//给一个空回调函数防止js报错
	function query(){
	}
	//判断浏览器的类型
	function CheckExprole(){
		if(navigator.userAgent.indexOf("Opera") != -1){
			return true;
		}else if(navigator.userAgent.indexOf("MSIE") != -1){
			return false;
		}else if(navigator.userAgent.indexOf("Firefox") != -1){
		   	 return true;
		}else if(navigator.userAgent.indexOf("Netscape") != -1){
			return true;
		}else if(navigator.userAgent.indexOf("Safari") != -1){
			return true;
		}else{
		　 return true;
		} 
	}
</script>


<body id="bodyid"  leftmargin="0" topmargin="10">
 	 
<form action="/pages/staff/staffInfo!modifyPage.action" method="post"  name="sysUserForm">	
			<input type="hidden"  name="userId" id="userId" value="<s:property value='userId'/>"/>

<body id="bodyid" leftmargin="0" topmargin="10">
	<form action="/pages/staff/staffInfo!modifyPage.action" method="post"
		name="sysUserForm">
		<table width="98%" align="center" cellPadding="0" cellSpacing="0" height="100%">
			<input type="hidden" name="userId" id="userId" value="<s:property value='userId'/>" />
			<tr>
				<td height="500">
					<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" class="input_table">
						<tr>
							<td class="input_tablehead">查看报销信息</td>
						</tr>
					</table>
					<table width="95%" class="input_table">
						<s:iterator value="#request.expenseInfoList" id="expenseInfo">
						<input name="expenseId" id="expenseId" type="hidden" value='<s:property value="id"/>'>
						<tr>
							<td colspan="2">
								<s:hidden name="expenseAccount.userId"></s:hidden>
								姓名：<s:property value="userName" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								部门：<s:property value="detName" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								报销状态：
									<s:if test='status=="0"'>新增</s:if>
									<s:elseif test='status=="1"'>行政审核中</s:elseif>
									<s:elseif test='status=="2"'>财务审核中</s:elseif>
									<s:elseif test='status=="3"'>财务审核通过，已发款</s:elseif>
									<s:elseif test='status=="4"'>行政审核不通过</s:elseif>
									<s:elseif test='status=="5"'>财务审核不通过</s:elseif>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								报销类型：出差费用报销&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">申请报销金额</td>
							<td bgcolor="#FFFFFF" >
								<input type="hidden" name="sum" id="sum" value='<s:property value="sum" />'/>
								<span id="sumid">元</span>
							</td>
						</tr>
						<tr>
							<td class="input_label2">核准报销金额</td>
							<td bgcolor="#FFFFFF">
								<input type="hidden" name="approveSum" id="approveSum" value='<s:property value="approveSum" />'/>
								<span id="approveSumid">元</span>
							</td>
						</tr>
						<tr>
							<td class="input_label2">备注</td>
							<td bgcolor="#FFFFFF">
								<textarea  cols="73" rows="4" id="note" style="width:100%" readonly="readonly"><s:property value="note" /></textarea>
							</td>
						</tr>
						</s:iterator>
					</table>  
					<table width="100%" cellpadding="0" cellspacing="0"
						bgcolor="#FFFFFF" id=downloadList>
						<tr>
							<td class="input_tablehead2" colspan="14">出差报销明细</td>
						</tr>
						<tr>
							<td nowrap width="8%" class="oracolumncenterheader"><div
									align="center">日期</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">地点</div>
							</td>
							<td nowrap width="22%" class="oracolumncenterheader"><div
									align="center">项目名称</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">出差任务</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">往返车船飞机费</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">出租车</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">公共汽车</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">住宿费</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">通讯费</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">业务费用</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">其它</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">补助</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">小计</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">公交路线</div>
							</td>
						</tr>
						<tbody name="li" id="li" name="formlist" id="formlist"
							align="center">
							<s:iterator value="#request.expenselist" id="costDetail"
								status="row">
								<tr style="border: 1">
									<td align="center"><s:property value="dateTime" /></td>
									<td align="center"><s:property value="place" /></td>
									<td align="center" title='<s:property value="prjname" />'><s:property value="prjname" /></td>
									<td align="center"><s:property value="task" /></td>
									<td align="center"><s:property value="fly" /></td>
									<td align="center"><s:property value="taxi" /></td>
									<td align="center"><s:property value="bus" /></td>
									<td align="center"><s:property value="living" /></td>
									<td align="center"><s:property value="contact" /></td>
									<td align="center"><s:property value="business" /></td>
									<td align="center"><s:property value="other" /></td>
									<td align="center"><s:property value="buzhu" /></td>
									<td align="center"><s:property value="account" /></td>
									<td align="center" title='<s:property value="path" />'><s:property value="path" /></td>
								</tr>
							</s:iterator>
							<tr id="costSumTr">
								<td align="center"><s:property
										value="#session.costSum.dateTime" /></td>
								<td align="center"><s:property
										value="#session.costSum.place" /></td>
								<td align="center"><s:property
										value="#session.costSum.prjname" /></td>
								<td align="center"><s:property
										value="#session.costSum.task" /></td>
								<td align="center"><s:property value="#session.costSum.fly" /></td>
								<td align="center"><s:property
										value="#session.costSum.taxi" /></td>
								<td align="center"><s:property value="#session.costSum.bus" /></td>
								<td align="center"><s:property
										value="#session.costSum.living" /></td>
								<td align="center"><s:property
										value="#session.costSum.contact" /></td>
								<td align="center"><s:property
										value="#session.costSum.business" /></td>
								<td align="center"><s:property
										value="#session.costSum.other" /></td>
								<td align="center"><s:property
										value="#session.costSum.buzhu" /></td>
								<td align="center"><s:property
										value="#session.costSum.account" /></td>
								<td align="center"><s:property
										value="#session.costSum.path" /></td>
							</tr>
						</tbody>
					</table>
					<table width="100%" border="0" cellpadding="1" cellspacing="1"
						bgcolor="#FFFFFF" id=downloadList>
						<tr>
							<td class="input_tablehead2" colspan="7">费用报销明细表</td>
						</tr>
						<tr>
							<td nowrap width="8%" class="oracolumncenterheader"><div
									align="center">日期</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">地点</div>
							</td>
							<td nowrap width="37%" class="oracolumncenterheader"><div
									align="center">项目名称</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">业务费用</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">市内交通</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">其他费用</div>
							</td>
							<td nowrap width="25%" class="oracolumncenterheader"><div
									align="center">业务及其他费用说明</div>
							</td>
						</tr>
						<tbody name="li" id="li" align="center">
							<s:iterator value="#request.travelDetailList" id="travelDetail"
								status="row">
								<tr>
									<td align="center"><s:property value="ddTime" /></td>
									<td align="center"><s:property value="didian" /></td>
									<td align="center"><s:property value="prjname" /></td>
									<td align="center"><s:property value="yewu" /></td>
									<td align="center"><s:property value="traffic" /></td>
									<td align="center"><s:property value="another" /></td>
									<td align="center"><s:property value="note" /></td>
										
								</tr>
							</s:iterator>
							<tr id="travelSumTr">
								<td align="center"><s:property
										value="#session.travelSum.ddTime" /></td>
								<td align="center"><s:property
										value="#session.travelSum.didian" /></td>
								<td align="center"><s:property
										value="#session.travelSum.prjname" /></td>
								<td align="center"><s:property
										value="#session.travelSum.yewu" /></td>
								<td align="center"><s:property
										value="#session.travelSum.traffic" /></td>
								<td align="center"><s:property
										value="#session.travelSum.another" /></td>
								<td align="center"><s:property
										value="#session.travelSum.note" /></td>
							</tr>
						</tbody>
					</table> 
					<table width="100%" border="1" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" id=prjAndSumList>
						<tr>
							<td colspan="5" class="input_tablehead2">项目报销金额</td>
							<input type="hidden" id="maxsize" name="maxsize" value="<s:property value='#session.prjSumSize' />"/>
						</tr>
						<tbody name="pslist" id="pslist" align="center">
							<s:iterator value="#session.savePrjSumList" id="prjsumlist" status="row">
								<tr id="prjsumtr<s:property value='#row.index'/>">
								<td align="right" width="10%;">项目名称</td>
								<td align="left" width="40%;">
									<select name="prjname<s:property value='#row.index'/>" id="prjname<s:property value='#row.index'/>" style="width:100%">
										<s:iterator value="#session.projectSelect" id="project">
											    <option value="<s:property value='name'/>" <s:if test='name==prjname'>selected</s:if> ><s:property value='name'/></option>
										</s:iterator>
									</select>
								</td>
								<td align="right" width="10%;">报销金额</td>
								<td align="left" width="40%;">
									<input type="text" id="sum<s:property value='#row.index'/>" name="sum<s:property value='#row.index'/>" value='<s:property value="sum" />'>元
									<input type="hidden" id="id<s:property value='#row.index'/>" name="id<s:property value='#row.index'/>" value='<s:property value="id" />'/>
								</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead2" colspan="2">审批记录</td>
						</tr>
						<tr>
							<td class="input_label2" width="8">审批记录</td>
							<td bgcolor="#FFFFFF">
								<textarea name="record" cols="73" rows="4" id="record" style="width:100%" readonly="readonly"></textarea>
							</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead2" colspan="2">修改记录</td>
						</tr>
						<tr>
							<td class="input_label2">修改记录</td>
							<td bgcolor="#FFFFFF">
								<textarea name="modifyRecord" cols="73" rows="4" id="modifyRecord" style="width:100%" readonly="readonly"></textarea>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><br> 
					<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal()" image="../../images/share/f_closed.gif">
					<input type="button" name="return"
					value='打印报销明细单' class="MyButton"
					onclick="printDetail()" image="../../images/share/f_closed.gif">
					<input type="button" name="return"
					value='打印报销费用单' class="MyButton"
					onclick="printCost()" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>