<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.util.UUID"%>
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
		//报销明细单参数
		var updateCostRow = new Array();
		var updateCostData = new Array();
		var delCostRow = new Array();
		var updateCostIndex = 0;
		var delCostIndex = 0;
		var costSumData = "";
		
		//报销费用单参数
		var updateTravelRow = new Array();
		var updateTravelData = new Array();
		var delTravelRow = new Array();
		var updateTravelIndex = 0;
		var delTravelIndex = 0;
		var travelSumData = "";
		
		var maxsize = 0;
		function save(){
			if(document.getElementById("sum").value==""){
				alert("请导入报销明细");
				//document.getElementById("sum").focus();
				return ;
			}
			if(Number(document.getElementById("sum").value)==0){
				alert("申请报销金额不能为0");
				//document.getElementById("sum").focus();
				return ;
			}
			if(getAllPrjAndSum()=="true"){
				return;
			}
			var flag="false";
			var curstatus=$("#curstatus").val();
			if(curstatus=="2"){
				var approvesum=$("#approveSum").val();
				if(approvesum==""){
					alert("请填写核准报销金额");
					flag="true";
				}else if(Number(approvesum)==0){
					alert("核准报销金额不能为0");
					flag="true";
				}
			}
			if(flag=="true"){
				return;
			}
			if(Number($("#approveSum").val())!=Number($("#sum").val()) && curstatus=="2"){
				if(!confirm("申请报销金额不等核准报销金额，确定保存")){
					flag="true";
				}
			}
			if(flag=="true"){
				return;
			}
			if(curstatus!="2"){
				$("#approveSum").val('');
			}
			
			$("#delCostRow").val(delCostRow);
			$("#updateCostRow").val(updateCostRow);
			$("#updateCostData").val(updateCostData);
			if(costSumData!=""){
				costSumData=costSumData.substring(1);
			}
			$("#costSumData").val(costSumData);
			
			$("#delTravelRow").val(delTravelRow);
			$("#updateTravelRow").val(updateTravelRow);
			$("#updateTravelData").val(updateTravelData);
			if(travelSumData!=""){
				travelSumData=travelSumData.substring(1);
			}
			$("#travelSumData").val(travelSumData);
			
			document.getElementById("ok").disabled=true;
			window.returnValue=true;
			expenseInfoForm.submit();
		}
		function fromExport(){
			var uuid = document.getElementById("uuid").value;
			var updatemanid = $("#updatemanid").val();
			var note = $("#note").val();
			var url='/pages/expense/add_import.jsp?uuid='+uuid+"&updatemanid="+updatemanid+"&note="+note;
			url = encodeURI(url,"UTF-8");
			var resultvalue = OpenModal(url,'1000,500,contactInfo.importContactTitle,contactInfo');
		    refreshList();
		}
		function query(){
		    var uuid = document.getElementById("uuid").value;
		    var actionUrl = "<%=request.getContextPath()%>/pages/expense/expenseInfo!showAddDetail.action?uuid="+uuid;
		    window.location = actionUrl;
		}
		
		String.prototype.trim = function() {
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function getKey(e) {
			e = e || window.event;
			var keycode = e.which ? e.which : e.keyCode;
			if (keycode != 27 || keycode != 9) {
				for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
					var tmpStr = document.getElementsByTagName("textarea")[i].value
							.trim();
					if (tmpStr.length > 250) {
						document.getElementsByTagName("textarea")[i].value = tmpStr
								.substr(0, 250);
						alert("你输入的字数超过250个了");
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
		$(function(){
			var getsum=$("#sum").val();
			if(getsum!=""){
				var showsum=fmoney(getsum,2);
				$("#showsum").val(showsum);
			}
			var status = "<%=request.getAttribute("status")%>";
			if(status=="" || status==null ||　status=="null"){
				status = "<%=session.getAttribute("status")%>";
			}
			$("#curstatus").val(status);
			if(status=="1"){
				$("#approveTd").html("财务审核人员");
				//$("#printDetail").show();
				//$("#printCost").show();
			}else if(status=="2"){
				//$("#printDetail").show();
				//$("#printCost").show();
				$("#approveTr").hide();
				$("#approvesumTr").show();
			}else{
				$("#approveTd").html("行政审核人员");
			}
			var updatemanid = "<%=session.getAttribute("updatemanid")%>";
			if(updatemanid==null || updatemanid=="null"){
				updatemanid="";
			}
			if(updatemanid!=""){
				$("#updatemanid").find("option").each(function(){
	      	 		if($(this).val()==updatemanid){
	      	 			$(this).attr("selected","selected");
	      	 		}else{
	      	 			$(this).removeAttr("selected");
	      	 		}       	 			
	  	 		});
			}
			var note = "<%=session.getAttribute("note")%>";
			if(note==null || note=="null"){
				note="";
			}
			$("#note").val(note);
			
			var costSumValue = new Array();
			var travelSumValue = new Array();
			
			
			$(".removeTr").click(function(){
				var rowid = $(this).parent().parent().find("input[name='expenserow']").val();
				delCostRow[delCostIndex] = rowid;
				delCostIndex++;
				//费用合计  用于合计总金额
				var travelSumTds = $("#travelSumTr").find("td");
				var travelSum = Number($(travelSumTds[3]).html())+Number($(travelSumTds[4]).html())+Number($(travelSumTds[5]).html());
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var costSumTd = $("#costSumTr").find("td");
				costSumData="";
				for(var i=4;i<13;i++){
					$(costSumTd[i]).html((Number($(costSumTd[i]).html())-Number($(tds[i]).html())).toFixed(2));
					costSumData+=","+$(costSumTd[i]).html();
				}
				$("#sum").val((Number(travelSum)+Number($(costSumTd[12]).html())).toFixed(2));
				var sum = $("#sum").val();
				if(sum!=""){
					var showsum=fmoney(sum,2);
					$("#showsum").val(showsum);
				}
				$(this).parent("td").parent("tr").remove();
			});
			//出差修改
			$(".modifyTr").click(function(){
				//费用合计  用于合计总金额
				var travelSumTds = $("#travelSumTr").find("td");
				var travelSum = Number($(travelSumTds[3]).html())+Number($(travelSumTds[4]).html())+Number($(travelSumTds[5]).html());
				var tds = $(this).parent("td").parent("tr").find("td");
				var oldValue="";
				var btnValue = $(this).val();
				var costSumTd = $("#costSumTr").find("td");
				var rowid = $(this).parent().parent().find("input[name='expenserow']").val();
				if(btnValue=="修改"){
					//修改
					 for(var i=4;i<12;i++){
						var tdValue = $(tds[i]).html();
						var newHtml = '<input type="text" size="10%" value="'+tdValue+'" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,\'\')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,\'\')">';
						$(tds[i]).html(newHtml);
						//合计
						costSumValue[i] = (Number($(costSumTd[i]).html())-Number(tdValue)).toFixed(2);
					} 
					costSumValue[i] = (Number($(costSumTd[i]).html())-Number($(tds[i]).html())).toFixed(2);
					$(this).val("确认");
				}
				if(btnValue=="确认"){
					//确认
					var subValue="";
					costSumData="";
					var xiaoji = 0;
					for(var i=4;i<12;i++){
						var inputValue = $(tds[i]).find("input").val();
						$(tds[i]).html(inputValue);
						xiaoji = (Number(xiaoji) + Number(inputValue)).toFixed(2);
						$(costSumTd[i]).html((Number(costSumValue[i])+Number(inputValue)).toFixed(2));
						subValue += ","+inputValue;
						costSumData+=","+$(costSumTd[i]).html();
					}
					costSumData+=","+$(costSumTd[12]).html();
					$(this).val("修改");
					$(tds[i]).html(xiaoji);
					subValue += ","+xiaoji;
					subValue = subValue.substring(1);
					$(costSumTd[i]).html((Number(costSumValue[i])+Number(xiaoji)).toFixed(2));
					$("#sum").val((Number(travelSum)+Number($(costSumTd[i]).html())).toFixed(2));
					var sum = $("#sum").val();
					if(sum!=""){
						var showsum=fmoney(sum,2);
						$("#showsum").val(showsum);
					}
					updateCostRow[updateCostIndex] = rowid;
					updateCostData[updateCostIndex] = subValue;
					updateCostIndex++;
				}
			});
			
			//费用删除
			$(".removeTr1").click(function(){
				var rowid = $(this).parent().parent().find("input[name='travelrow']").val();
				delTravelRow[delTravelIndex] = rowid;
				delTravelIndex++;
				//出差合计  用于合计总金额
				var costSumTds = $("#costSumTr").find("td");
				var costSum = Number($(costSumTds[12]).html()).toFixed(2);
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var travelSumTd = $("#travelSumTr").find("td");
				travelSumData="";
				for(var i=3;i<6;i++){
					$(travelSumTd[i]).html((Number($(travelSumTd[i]).html())-Number($(tds[i]).html())).toFixed(2));
					travelSumData+=","+$(travelSumTd[i]).html();
				}
				$("#sum").val((Number(costSum)+Number($(travelSumTd[3]).html())+Number($(travelSumTd[4]).html())+Number($(travelSumTd[5]).html())).toFixed(2));
				var sum = $("#sum").val();
				if(sum!=""){
					var showsum=fmoney(sum,2);
					$("#showsum").val(showsum);
				}
				$(this).parent("td").parent("tr").remove();
			});
			//费用修改
			$(".modifyTr1").click(function(){
				//出差合计  用于合计总金额
				var costSumTds = $("#costSumTr").find("td");
				var costSum = Number($(costSumTds[12]).html()).toFixed(2);
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var oldValue="";
				var btnValue = $(this).val();
				var travelSumTd = $("#travelSumTr").find("td");
				var rowid = $(this).parent().parent().find("input[name='travelrow']").val();
				if(btnValue=="修改"){
					//修改
					 for(var i=3;i<6;i++){
						var tdValue = $(tds[i]).html();
						var newHtml = '<input type="text" size="10%" value="'+tdValue+'" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,\'\')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,\'\')">';
						$(tds[i]).html(newHtml);
						//合计
						travelSumValue[i] = (Number($(travelSumTd[i]).html())-Number(tdValue)).toFixed(2);
					} 
					$(this).val("确认");
				}
				if(btnValue=="确认"){
					//确认
					var subValue="";
					travelSumData="";
					for(var i=3;i<6;i++){
						var inputValue = $(tds[i]).find("input").val();
						$(tds[i]).html(inputValue);
						$(travelSumTd[i]).html((Number(travelSumValue[i])+Number(inputValue)).toFixed(2));
						subValue += ","+inputValue;
						travelSumData+=","+$(travelSumTd[i]).html();
					}
					$(this).val("修改");
					subValue = subValue.substring(1);
					var travelSum = Number($(travelSumTd[3]).html())+Number($(travelSumTd[4]).html())+Number($(travelSumTd[5]).html());
					$("#sum").val((Number(costSum)+Number(travelSum)).toFixed(2));
					var sum = $("#sum").val();
					if(sum!=""){
						var showsum=fmoney(sum,2);
						$("#showsum").val(showsum);
					}
					updateTravelRow[updateTravelIndex] = rowid;
					updateTravelData[updateTravelIndex] = subValue;
					updateTravelIndex++;
				}
			});
		});
		
		function addprjsum(){
			if(document.getElementById("sum").value==""){
				alert("请导入报销明细");
				return ;
			}
			var html =getPrjSumTr();
			$("#pslist").append(html);
			
		}
		
		function getPrjSumTr(){
			maxsize = $("#maxsize").val();
			var insertSize = parseInt(maxsize)+1;
			$("#maxsize").val(insertSize);
			var html='<tr id="prjsumtr'+maxsize+'">'+
			'<td align="center" width="5%;"><input type="checkbox" name="chkList" value="'+maxsize+'"/></td>'+
			'<td align="right" width="10%;">项目名称</td>'+
			'<td align="left" width="40%;">'+
			'	<select name="prjname'+maxsize+'" id="prjname'+maxsize+'" style="width:100%">';
			<s:iterator value="#session.projectSelect" id="project">
				    html+='<option value="<s:property value='name'/>"><s:property value='name'/></option>';
			</s:iterator>
			html+='	</select>'+
			'</td>'+
			'<td align="right" width="10%;">报销金额</td>'+
			'<td align="left" width="35%;"><input type="text" id="sum'+maxsize+'" name="sum'+maxsize+'" value=""  onkeyup="this.value=this.value.replace(/[^0-9\.]+/,\'\')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,\'\')">元'+
			'</td>'+
			'</tr>';
			return html;
		}
		
		function delprjsum(){
			var idList = GetSelIds();
			if(idList=="," || idList==""){
				alert("请选择一条记录");
				return;
			}
			if(idList.split(",").length>1){
				alert("一次只能删除一条记录");
				return;
			}
			var ids = idList.split(',');
			$("#prjsumtr"+ids).remove();
		}
		
		//获取所选复选框
		function  GetSelIds(){
		    var idList="";
		 	var  em= document.getElementsByName("chkList");
		 	for(var i=0;i<em.length;i++){
		 	   if(em[i].type=="checkbox"){
		 		   if(em[i].checked){
		 			   idList+=","+em[i].value.split(",")[0];
		 		   }
		 	   }
		 	}
		 	if(idList=="")
		 	   return "";
		 	return idList.substring(1);
		}
		//存储所有项目金额数据
		function getAllPrjAndSum(){
			var idList="";
		 	var  em= document.getElementsByName("chkList");
		 	for(var i=0;i<em.length;i++){
		 		idList+=","+em[i].value.split(",")[0];
		 	}
		 	var flag="false";
		 	if(idList!=""){
		 		var prjname="";
		 		var prjsum="";
		 		var sumTotal=0;
		 		idList=idList.substring(1);
		 		for(var i=0;i<idList.split(",").length;i++){
		 			var index = idList.split(",")[i];
		 			if($("#sum"+index).val().trim()==""){
		 				alert("请填写项目报销金额");
		 				flag="true";
		 				break;
		 			}else if(Number($("#sum"+index).val().trim())==0){
		 				alert("报销金额不能为0");
		 				flag="true";
		 				break;
		 			}
		 			prjname+=","+$("#prjname"+index).val();
		 			prjsum+=","+$("#sum"+index).val();
		 			sumTotal=Number(sumTotal)+Number($("#sum"+index).val());
		 		}
		 		if(flag=="true"){
		 			return flag;
		 		}
		 		if(prjname!=""){
		 			prjname = prjname.substring(1);
		 			prjsum = prjsum.substring(1);
		 			var total=$("#sum").val();
		 			if(Number(total)!=Number(sumTotal)){
		 				alert("项目报销金额之和需等于申请报销金额");
		 				flag="true";
		 			}
		 			$("#savePrjdata").val(prjname);
		 			$("#saveSumdata").val(prjsum);
		 		}
		 	}
		 	return flag;
		}
		
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
		
		function printDetaila(){
			if(document.getElementById("sum").value==""){
				alert("请导入报销明细");
				return ;
			}
			var expenseId=$("#uuid").val();
			if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
				window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId);
			}else{
				OpenModal('/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
			}
		}
		function printCosta(){
			if(document.getElementById("sum").value==""){
				alert("请导入报销明细");
				return ;
			}
			var expenseId=$("#uuid").val();
			if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
				window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId);
			}else{
				OpenModal('/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
			}
		}
	</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="expenseInfoForm"
		action="<%=request.getContextPath()%>/pages/expense/expenseInfo!add.action"
		method="post">
		
		<input type="hidden" id="updateCostData" name="updateCostData" value="" />
		<input type="hidden" id="updateCostRow" name="updateCostRow" value="" />
		<input type="hidden" id="delCostRow" name="delCostRow" value="" />
		<input type="hidden" id="costSumData" name="costSumData" value="" />
		
		<input type="hidden" id="updateTravelData" name="updateTravelData" value="" />
		<input type="hidden" id="updateTravelRow" name="updateTravelRow" value="" />
		<input type="hidden" id="delTravelRow" name="delTravelRow" value="" />
		<input type="hidden" id="travelSumData" name="travelSumData" value="" />
		<table width="98%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<table width="100%" class="input_table">
						<tr>
							<td class="input_tablehead">编辑报销信息</td>
							<input name="uuid" id="uuid" type="hidden" value='<s:property value="#request.uuid"/>'>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="2">
								姓名：<s:property value="#request.userName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.userName" id="userName" readonly="true" type="hidden" value='<s:property value="#request.userName"/>'>
								部门：<s:property value="#request.DetName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.DetName" id="detName" readonly="true" type="hidden" value='<s:property value="#request.DetName"/>'>
								报销状态：新增&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.status" id="status" readonly="true" type="hidden" value='0'>
								报销类型：出差费用报销&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.type" id="type" readonly="true" type="hidden" value='出差费用报销'>
								<input name="expenseAccount.prjName" id="prjName" readonly="true" type="hidden" value='<s:property value="#request.prjName"/>'>
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">申请报销金额</td>
							<td bgcolor="#FFFFFF">
								<input type="text" name="showsum" id="showsum" readonly="readonly" value="<s:property value='#session.expenseSum'/>" style="border:0px;">
								<input type="hidden" name="expenseAccount.sum" id="sum" readonly="readonly" value="<s:property value='#session.expenseSum'/>">元
							</td>
						</tr>
						<tr id="approvesumTr" style="display:none;">
							<td class="input_label2" width="8%">核准报销金额</td>
							<td bgcolor="#FFFFFF">
								<input type="hidden" name="curstatus" id="curstatus" value=""/>
								<input type="text" name="expenseAccount.approveSum" id="approveSum" value="<s:property value='#session.expenseSum'/>" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')">元
							</td>
						</tr>
						<tr id="approveTr">
							<td class="input_label2" id="approveTd"></td>
							<td bgcolor="#FFFFFF">
								<s:select name="updatemanid" id="updatemanid"  list="#request.approvePerson" listKey="userid" listValue="username"></s:select>
							</td>
						</tr>
						<tr>
							<td class="input_label2">备注</td>
							<td bgcolor="#FFFFFF">
								<textarea name="expenseAccount.note" cols="73" rows="4" id="note" style="width:100%"></textarea>
							</td>
						</tr>
					</table>  
					
					<br />
					<table rules="rows" width="100%" border="0" cellpadding="0" cellspacing="0"
						bgcolor="#FFFFFF" id=downloadList>
						<tr>
							<td colspan="15" class="input_tablehead2">出差报销明细</td>
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
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">操作</div>
							</td>
						</tr>
						<tbody name="li" id="li" name="formlist" id="formlist" align="center">
							<s:iterator value="#session.expenseList" id="costDetail" status="row">
								<tr style="border: 1">
									<td align="center"><s:property value="id" /><s:property value="dateTime" />
									</td>
									<td align="center"><s:property value="place" />
									</td>
									<td align="center" title='<s:property value="prjname" />'><s:property value="prjname" />
									</td>
									<td align="center"><s:property value="task" />
									</td>
									<td align="center"><s:property value="fly" />
									</td>
									<td align="center"><s:property value="taxi" />
									</td>
									<td align="center"><s:property value="bus" />
									</td>
									<td align="center"><s:property value="living" />
									</td>
									<td align="center"><s:property value="contact" />
									</td>
									<td align="center"><s:property value="business" />
									</td>
									<td align="center"><s:property value="other" />
									</td>
									<td align="center"><s:property value="buzhu" />
									</td>
									<td align="center"><s:property value="account" />
									</td>
									<td align="center" title='<s:property value="path" />'><s:property value="path" />
									</td>
									<td align="center">
										<input type="hidden" id="expenserow" name="expenserow" value="<s:property value='#row.index'/>"/>
										<input type="hidden" name="detailId" value="<s:property value='uuid'/>"/>
										<input class="modifyTr" type="button" value="修改" style="width:36px;margin-left:-5px;"/>
										<input class="removeTr" type="button" value="删除" style="width:36px;margin-left:-5px;"/>
									</td>
							</tr>
							</s:iterator>
							<tr id="costSumTr">
								<td align="center"><s:property value="id" /><s:property value="#session.costSum.dateTime" />
								</td>
								<td align="center"><s:property value="#session.costSum.place" />
								</td>
								<td align="center"><s:property value="#session.costSum.prjname" />
								</td>
								<td align="center"><s:property value="#session.costSum.task" />
								</td>
								<td align="center"><s:property value="#session.costSum.fly" />
								</td>
								<td align="center"><s:property value="#session.costSum.taxi" />
								</td>
								<td align="center"><s:property value="#session.costSum.bus" />
								</td>
								<td align="center"><s:property value="#session.costSum.living" />
								</td>
								<td align="center"><s:property value="#session.costSum.contact" />
								</td>
								<td align="center"><s:property value="#session.costSum.business" />
								</td>
								<td align="center"><s:property value="#session.costSum.other" />
								</td>
								<td align="center"><s:property value="#session.costSum.buzhu" />
								</td>
								<td align="center"><s:property value="#session.costSum.account" />
								</td>
								<td align="center"><s:property value="#session.costSum.path" />
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>

					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						bgcolor="#FFFFFF" id=uploadList>
						<tr>
							<td colspan="8" class="input_tablehead2">费用报销明细表</td>
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
									align="center">业务费用</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">市内交通</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">其他费用</div>
							</td>
							<td nowrap width="23%" class="oracolumncenterheader"><div
									align="center">业务及其他费用说明</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">操作</div>
							</td>
						</tr>
						<tbody name="li" id="li" align="center">
							<s:iterator value="#session.travelDetailList" id="travelDetail" status="row">
								<td align="center"><s:property value="ddTime" />
								</td>
								<td align="center"><s:property value="didian" />
								</td>
								<td align="center"><s:property value="prjname" />
								</td>
								<td align="center"><s:property value="yewu" />
								</td>
								<td align="center"><s:property value="traffic" />
								</td>
								<td align="center"><s:property value="another" />
								</td>
								<td align="center"><s:property value="note" />
								</td>
								<td align="center">
									<input type="hidden" id="travelrow" name="travelrow" value="<s:property value='#row.index'/>"/>
									<input type="hidden" name="detailId" value="<s:property value='id'/>"/>
									<input class="modifyTr1" type="button" value="修改"/>
									<input class="removeTr1" type="button" value="删除"/>
								</td>
								</tr>
							</s:iterator>
							<tr id="travelSumTr">
								<input type="hidden" name="detailId" value="<s:property value='#session.travelSum.uuid'/>"/>
								<td align="center"><s:property value="#session.travelSum.ddTime" />
								</td>
								<td align="center"><s:property value="#session.travelSum.didian" />
								</td>
								<td align="center"><s:property value="#session.travelSum.prjname" />
								</td>
								<td align="center"><s:property value="#session.travelSum.yewu" />
								</td>
								<td align="center"><s:property value="#session.travelSum.traffic" />
								</td>
								<td align="center"><s:property value="#session.travelSum.another" />
								</td>
								<td align="center"><s:property value="#session.travelSum.note" />
								</td>
								<td></td>
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
								<td align="center" width="8%;"><input type="checkbox" name="chkList" value="<s:property value='#row.index'/>"/></td>
								<td align="right" width="10%;">项目名称</td>
								<td align="left" width="40%;">
									<select name="prjname<s:property value='#row.index'/>" id="prjname<s:property value='#row.index'/>" style="width:100%">
										<s:iterator value="#session.projectSelect" id="project">
											    <option value="<s:property value='name'/>" <s:if test='name==prjname'>selected</s:if> ><s:property value='name'/></option>
										</s:iterator>
									</select>
								</td>
								<td align="right" width="10%;">报销金额</td>
								<td align="left" width="32%;">
									<input type="text" id="sum<s:property value='#row.index'/>" name="sum<s:property value='#row.index' />" value='<s:property value="sum" />' onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')">元
								</td>
								</tr>
							</s:iterator>
						</tbody>
					</table></td>
			</tr>
			<tr>
				<td>
					<div style="text-align:right;">
						<input type="button" id="addPrjSum" name="addPrjSum" value="新增" onClick="addprjsum()" />
						<input type="button" id="delPrjSum" name="delPrjSum" value="删除" onClick="delprjsum()" />
						<input type="hidden" id="savePrjdata" name="savePrjdata" value=""/>
						<input type="hidden" id="saveSumdata" name="saveSumdata" value=""/>
					</div>
				</td>
			</tr>
		</table>
		<br /> <br />

		<table width="80%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
					<input type="button" name="import" id="import" value='<s:text name="导入报销明细表"/>' class="MyButton" onClick="fromExport()" image="../../images/share/import.gif">
					<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton" onClick="save()" image="../../images/share/yes1.gif"> 
					<input type="button" name="return" value='打印报销明细单' class="MyButton" onclick="printDetaila()" image="../../images/share/f_closed.gif" style="display:none;" id="printDetail">
					<input type="button" name="return" value='打印报销费用单' class="MyButton" onclick="printCosta()" image="../../images/share/f_closed.gif" style="display:none;" id="printCost">
					<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton" onclick="closeModal(true);" image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
