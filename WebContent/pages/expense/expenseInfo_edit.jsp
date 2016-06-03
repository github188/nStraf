<%@page import="cn.grgbanking.feeltm.domain.SysOperLog"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ include file="/inc/pagination.inc"%>
<%
String username = ((cn.grgbanking.feeltm.login.domain.UserModel) session.getAttribute("tm.loginUser")).getUsername();
String curdate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
%>

<html>
<head>
<title>certification query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">
		var curdate='<%=curdate%>';
		var username='<%=username%>';
		var maxsize = 0;
		function savea(){
			/* var status = $("#status").find("option:selected").val();
			var reason = document.getElementById("reason").value;
			if(status=="打回"){
				if(reason==""){
					alert("请输入打回原因");
					return ;
				}
			} */
			/* if(document.getElementById("sum").value==""){
				alert("请输入报销金额");
				document.getElementById("sum").focus();
				return ;
			} */
	/*		if(document.getElementById("feedBack").value.length>16){
				alert("修改反馈字数限制在16字以内，您输入的字数为"+document.getElementById("feedBack").value.length+";请重新输入！");
				document.getElementById("feedBack").focus();
				return;
			}
			if(document.getElementById("note").value.length>200){
				alert("备注字数限制在200字以内，您输入的字数为"+document.getElementById("note").value.length+";请重新输入！");
				document.getElementById("note").focus();
				return;
			}	*/
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
				if(!confirm("申报报销金额不等核准报销金额，确定保存")){
					flag="true";
				}
			}
			if(flag=="true"){
				return;
			}
			if(curstatus!="2"){
				$("#approveSum").val("0");
			}
			document.getElementById("ok").disabled=true;
			window.returnValue=true;
			expenseInfoForm.submit();
		}
		function fromExport(){
			var expenseId = document.getElementById("expenseId").value;
			var resultvalue = OpenModal('/pages/expense/expenseInfo_import.jsp?expenseId='+expenseId,'1000,500,expenseInfo.importExpenseTitle,staffManager');
		    refreshList();
		}
		function query(){
			var expenseId = document.getElementById("expenseId").value;
			var actionUrl = "<%=request.getContextPath()%>/pages/expense/expenseInfo!showCostDetail.action?expenseId="+expenseId;
			window.location = actionUrl;
		}

		$(function(){
			var status = "<%=request.getAttribute("status")%>";
			if(status=="" || status==null ||　status=="null"){
				status = "<%=session.getAttribute("status")%>";
			}
			$("#curstatus").val(status);
			if(status=="1"){
				$("#approveTd").html("财务审核人员");
				$("#printDetail").show();
				$("#printCost").show();
			}else if(status=="2"){
				$("#approveTr").hide();
				$("#approvesumTr").show();
			}else{
				$("#approveTd").html("行政审核人员");
			}
			
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
			var getsum=$("#sum").val();
			if(getsum!=""){
				var showsum=fmoney(getsum,2);
				$("#showsum").val(showsum);
			}
			var costSumValue = new Array();
			var travelSumValue = new Array();
			var recordinfo="";
			//出差删除
			$(".removeTr").click(function(){
				//费用合计  用于合计总金额
				var travelSumTds = $("#travelSumTr").find("td");
				var travelSum = Number($(travelSumTds[3]).html())+Number($(travelSumTds[4]).html())+Number($(travelSumTds[5]).html());
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var costSumTd = $("#costSumTr").find("td");
				for(var i=4;i<13;i++){
					$(costSumTd[i]).html((Number($(costSumTd[i]).html())-Number($(tds[i]).html())).toFixed(2));
				}
				$("#sum").val((Number(travelSum)+Number($(costSumTd[12]).html())).toFixed(2));
				var sum = $("#sum").val();
				if(sum!=""){
					var showsum=fmoney(sum,2);
					$("#showsum").val(showsum);
				}
				var url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!deleteCostDetail.action";
				$.ajax({
					url:url,
					data:{
						detailId:detailId,
						sum:sum
					},
					success:function(){}
				});
				var dt = new Date();
				var h=dt.getHours();
				var m=dt.getMinutes();
				var s=dt.getSeconds();
				var currentdate=curdate+" "+h+":"+m+":"+s;
				recordinfo = "往返车船飞机票:"+$(tds[4]).html()　+",出租车:"+$(tds[5]).html()+",公共汽车:"+$(tds[6]).html()+",住宿费:"+$(tds[6]).html()
					+",通讯费:"+$(tds[8]).html()+",业务费用:"+$(tds[9]).html()+",其它:"+$(tds[10]).html()+",补助:"+$(tds[11]).html();
				var info=username+"("+currentdate+")删除了"+$(tds[0]).html()+"的记录,操作前的数据为："+recordinfo;
				var modifyrecord = $("#modifyRecord").html();
				if(modifyrecord!=""){
					if(CheckExprole()){
						info=modifyrecord.trim()+"\n"+info;
					}else{
						info=modifyrecord.trim()+"<br/>"+info;
					}
				}
				$("#modifyRecord").html(info);
				$(this).parent("td").parent("tr").remove();
			});
			//出差修改
			$(".modifyTr").click(function(){
				//费用合计  用于合计总金额
				var travelSumTds = $("#travelSumTr").find("td");
				var travelSum = Number($(travelSumTds[3]).html())+Number($(travelSumTds[4]).html())+Number($(travelSumTds[5]).html());
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var oldValue="";
				var btnValue = $(this).val();
				var costSumTd = $("#costSumTr").find("td");
				if(btnValue=="修改"){
					recordinfo = "往返车船飞机票:"+$(tds[4]).html()　+",出租车:"+$(tds[5]).html()+",公共汽车:"+$(tds[6]).html()+",住宿费:"+$(tds[6]).html()
						+",通讯费:"+$(tds[8]).html()+",业务费用:"+$(tds[9]).html()+",其它:"+$(tds[10]).html()+",补助:"+$(tds[11]).html();
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
					var xiaoji = 0;
					for(var i=4;i<12;i++){
						var inputValue = $(tds[i]).find("input").val();
						$(tds[i]).html(inputValue);
						xiaoji = (Number(xiaoji) + Number(inputValue)).toFixed(2);
						$(costSumTd[i]).html((Number(costSumValue[i])+Number(inputValue)).toFixed(2));
						subValue += ","+inputValue;
					}
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
					var url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!modifyCostDetail.action";
					$.ajax({
						url:url,
						data:{
							detailId:detailId,
							subValue:subValue,
							sum:sum
						},
						success:function(){
							
						}
					});
					var dt = new Date();
					var h=dt.getHours();
					var m=dt.getMinutes();
					var s=dt.getSeconds();
					var currentdate=curdate+" "+h+":"+m+":"+s;
					var info=username+"("+currentdate+")修改了"+$(tds[0]).html()+"的记录,操作前的数据为："+recordinfo;
					var modifyrecord = $("#modifyRecord").html();
					if(modifyrecord!=""){
						if(CheckExprole()){
							info=modifyrecord.trim()+"\n"+info;
						}else{
							info=modifyrecord.trim()+"<br/>"+info;
						}
					}
					$("#modifyRecord").html(info);
				}
			});
			//费用删除
			$(".removeTr1").click(function(){
				//出差合计  用于合计总金额
				var costSumTds = $("#costSumTr").find("td");
				var costSum = Number($(costSumTds[12]).html()).toFixed(2);
				var detailId = $(this).parent().find("input[name='detailId']:first").val();
				var tds = $(this).parent("td").parent("tr").find("td");
				var travelSumTd = $("#travelSumTr").find("td");
				for(var i=3;i<6;i++){
					$(travelSumTd[i]).html((Number($(travelSumTd[i]).html())-Number($(tds[i]).html())).toFixed(2));
				}
				$("#sum").val((Number(costSum)+Number($(travelSumTd[3]).html())+Number($(travelSumTd[4]).html())+Number($(travelSumTd[5]).html())).toFixed(2));
				var sum = $("#sum").val();
				if(sum!=""){
					var showsum=fmoney(sum,2);
					$("#showsum").val(showsum);
				}
				var url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!deleteTravelDetail.action";
				$.ajax({
					url:url,
					data:{
						detailId:detailId,
						sum:sum
					},
					success:function(){}
				});
				var dt = new Date();
				var h=dt.getHours();
				var m=dt.getMinutes();
				var s=dt.getSeconds();
				var currentdate=curdate+" "+h+":"+m+":"+s;
				recordinfo = "业务费用:"+$(tds[3]).html()　+",市内交通:"+$(tds[4]).html()+",其它费用:"+$(tds[5]).html();
				var info=username+"("+currentdate+")删除了"+$(tds[0]).html()+"的记录,操作前的数据为："+recordinfo;
				var modifyrecord = $("#modifyRecord").html();
				if(modifyrecord!=""){
					if(CheckExprole()){
						info=modifyrecord.trim()+"\n"+info;
					}else{
						info=modifyrecord.trim()+"<br/>"+info;
					}
				}
				$("#modifyRecord").html(info);
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
				if(btnValue=="修改"){
					recordinfo = "业务费用:"+$(tds[3]).html()　+",市内交通:"+$(tds[4]).html()+",其它费用:"+$(tds[5]).html();
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
					for(var i=3;i<6;i++){
						var inputValue = $(tds[i]).find("input").val();
						$(tds[i]).html(inputValue);
						$(travelSumTd[i]).html((Number(travelSumValue[i])+Number(inputValue)).toFixed(2));
						subValue += ","+inputValue;
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
					var url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!modifyTravelDetail.action";
					$.ajax({
						url:url,
						data:{
							detailId:detailId,
							subValue:subValue,
							sum:sum
						},
						success:function(){
							
						}
					});
					var dt = new Date();
					var h=dt.getHours();
					var m=dt.getMinutes();
					var s=dt.getSeconds();
					var currentdate=curdate+" "+h+":"+m+":"+s;
					var info=username+"("+currentdate+")修改了"+$(tds[0]).html()+"的记录,操作前的数据为："+recordinfo;
					var modifyrecord = $("#modifyRecord").html();
					if(modifyrecord!=""){
						if(CheckExprole()){
							info=modifyrecord.trim()+"\n"+info;
						}else{
							info=modifyrecord.trim()+"<br/>"+info;
						}
					}
					$("#modifyRecord").html(info);
				}
			});
			var updateman=$("#updateman").val();
			$("#updatemanid").find("option").each(function(){
       	 		if($(this).val()==updateman){
       	 			$(this).attr("selected","selected");
       	 		}else{
       	 			$(this).removeAttr("selected");
       	 		}       	 			
   	 		});
		});
		
		function limitLen(maxlen,element){
			var message = $(element).val();
		    var byteCount=0;  
		    var strValue=message;  
		    var strLength=message.length;  
		    var maxValue=maxlen;  
	        for(var i=0;i<strLength;i++){  
	            byteCount=(strValue.charCodeAt(i)<=256)?byteCount+1:byteCount+2;  
	            if(byteCount>(maxValue*2)){  
	                message=strValue.substring(0,i);  
	                $(element).val(message);
	                alert("内容最多不能超过"+maxValue+"个字！");  
	                byteCount=maxValue;
	                $(element).focus();
	                break;  
	            }  
	        }  
		}
		
		function addprjsum(){
			var html =getPrjSumTr();
			$("#pslist").append(html);
			
		}
		
		function getPrjSumTr(){
			maxsize = $("#maxsize").val();
			var insertSize = parseInt(maxsize)+1;
			$("#maxsize").val(insertSize);
			var html='<tr id="prjsumtr'+maxsize+'">'+
			'<td align="center" width="5%;"><input type="radio" name="chkList" value="'+maxsize+'"/></td>'+
			'<td align="right" width="10%;">项目名称</td>'+
			'<td align="left" width="40%;">'+
			'	<select name="prjname'+maxsize+'" id="prjname'+maxsize+'" style="width:100%">';
			<s:iterator value="#session.projectSelect" id="project">
				    html+='<option value="<s:property value='name'/>"><s:property value='name'/></option>';
			</s:iterator>
			html+='	</select>'+
			'</td>'+
			'<td align="right" width="10%;">报销金额</td>'+
			'<td align="left" width="27%;"><input type="text" id="sum'+maxsize+'" name="sum'+maxsize+'" value="" onkeyup="this.value=this.value.replace(/[^0-9\.]+/,\'\')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,\'\')">元'+
			'</td>'+
			'<td>'+
			'<input type="button" id="enterPrjSum" name="enterPrjSum" value="确认" onClick="enterprjsum('+maxsize+')" />'+
			'<input type="hidden" id="id'+maxsize+'" name="'+maxsize+'" value=""/>'+
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
			var detailId = $("#id"+ids).val();
			if(detailId==""){
				$("#prjsumtr"+ids).remove();
				return;
			}
			var url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!deleteProjectSumDetail.action";
			$.ajax({
				url:url,
				data:{
					detailId:detailId
				},
				success:function(){}
			});
			var dt = new Date();
			var h=dt.getHours();
			var m=dt.getMinutes();
			var s=dt.getSeconds();
			var currentdate=curdate+" "+h+":"+m+":"+s;
			var record = "项目名称:"+ $("#prjname"+ids).val()　+",项目金额:"+ $("#sum"+ids).val();
			var info=username+"("+currentdate+")删除了"+$("#prjname"+ids).val()+"的记录,操作前的数据为："+record;
			var modifyrecord = $("#modifyRecord").html();
			if(modifyrecord!=""){
				if(CheckExprole()){
					info=modifyrecord.trim()+"\n"+info;
				}else{
					info=modifyrecord.trim()+"<br/>"+info;
				}
			}
			$("#modifyRecord").html(info);
			$("#prjsumtr"+ids).remove();
		}
		
		//获取所选复选框
		function  GetSelIds(){
		    var idList="";
		 	var  em= document.getElementsByName("chkList");
		 	for(var i=0;i<em.length;i++){
		 	   if(em[i].type=="radio"){
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
		 		var ids="";
		 		var sumTotal=0;
		 		var upprjname="";
		 		var uppprjsum="";
		 		var noSavePrjname="";
		 		var noSavePrjsum="";
		 		var noUpdatePrjname="";
		 		var noUpdatePrjsum="";
		 		var noUpdateId="";
		 		idList=idList.substring(1);
		 		for(var i=0;i<idList.split(",").length;i++){
		 			var index = idList.split(",")[i];
		 			if($("#sum"+index).val()==""){
		 				alert("请填写项目报销金额");
		 				flag="true";
		 				break;
		 			}else if(Number($("#sum"+index).val())==0){
		 				alert("项目报销金额不能为0");
		 				flag="true";
		 				break;
		 			}
		 			if($("#id"+index).val()==""){
		 				noSavePrjname+=","+$("#prjname"+index).val();
		 				noSavePrjsum+=","+$("#sum"+index).val();
		 			}
		 			if($("#id"+index).parent().parent().find("input[name='enterPrjSum']").attr("disabled")==undefined && $("#id"+index).val()!=""){
		 				noUpdatePrjname+=","+$("#prjname"+index).val();
		 				noUpdatePrjsum+=","+$("#sum"+index).val();
		 				noUpdateId+=","+$("#id"+index).val();
		 			}
		 			/*if($("#id"+index).val()==""){
			 			prjname+=","+$("#prjname"+index).val();
			 			prjsum+=","+$("#sum"+index).val();
		 			}else{
		 				upprjname+=","+$("#prjname"+index).val();
		 				uppprjsum+=","+$("#sum"+index).val();
		 				ids+=","+$("#id"+index).val();
		 			}*/
		 			sumTotal=Number(sumTotal)+Number($("#sum"+index).val());
		 		}
		 		if(noSavePrjname!=""){
		 			noSavePrjname=noSavePrjname.substring(1);
		 			noSavePrjsum=noSavePrjsum.substring(1);
		 			$("#noSavePrjname").val(noSavePrjname);
		 			$("#noSavePrjsum").val(noSavePrjsum);
		 		}
		 		if(noUpdatePrjname!=""){
		 			noUpdatePrjname=noUpdatePrjname.substring(1);
		 			noUpdatePrjsum=noUpdatePrjsum.substring(1);
		 			noUpdateId=noUpdateId.substring(1);
		 			$("#noUpdatePrjname").val(noUpdatePrjname);
		 			$("#noUpdatePrjsum").val(noUpdatePrjsum);
		 			$("#noUpdateId").val(noUpdateId);
		 		}
		 		/*if(prjname!=""){
		 			prjname = prjname.substring(1);
		 			prjsum = prjsum.substring(1);
		 			var total=$("#sum").val();
		 			if(parseFloat(total)!=sumTotal){
		 				alert("项目报销金额之和需等于申请报销金额");
		 				flag="true";
		 			}
		 			$("#savePrjdata").val(prjname);
		 			$("#saveSumdata").val(prjsum);
		 		}
		 		if(upprjname!="" && flag!="true"){
		 			upprjname = upprjname.substring(1);
		 			uppprjsum = uppprjsum.substring(1);
		 			ids = ids.substring(1);
		 			var total=$("#sum").val();
		 			if(parseFloat(total)!=sumTotal){
		 				alert("项目报销金额之和需等于申请报销金额");
		 				flag="true";
		 			}
		 			$("#uppPrjdata").val(upprjname);
		 			$("#uppSumdata").val(uppprjsum);
		 			$("#ids").val(ids);
		 		}*/
		 		var total=$("#sum").val();
	 			if(parseFloat(total)!=sumTotal){
	 				alert("项目报销金额之和需等于申请报销金额");
	 				flag="true";
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
			var expenseId=$("#expenseId").val();
			if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
				window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId);
			}else{
				OpenModal('/pages/expense/expenseInfo!toPrintDetailPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
			}
		}
		function printCosta(){
			var expenseId=$("#expenseId").val();
			if(navigator.userAgent.indexOf("MSIE") != -1){//Ie浏览器
				window.open('<%=request.getContextPath()%>/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId);
			}else{
				OpenModal('/pages/expense/expenseInfo!toPrintCostPage.action?expenseId='+expenseId,'1200,600,tmlInfo.addTmlTitle,tmlInfo');
			}
		}
		
		function enterprjsum(i){
			if($("#sum"+i).val()==""){
 				alert("请填写项目报销金额");
 				return;
 			}else if(Number($("#sum"+i).val())==0){
 				alert("项目报销金额不能为0");
 				return;
 			}
			var new_prjname = $("#prjname"+i).val();
			var new_sum = $("#sum"+i).val();
			if(new_prjname==old_prjname &&　new_sum==old_sum){
				
			}else{
				var detailId = $("#id"+i).val();
				var prjname = $("#prjname"+i).val();
				var prjsum = $("#sum"+i).val();
				var url = "";
				var expenseId = $("#expenseId").val();
				if(detailId==""){
					url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!addProjectSumDetail.action";
				}else{
					url = "<%=request.getContextPath()%>/pages/expense/expenseInfo!updateProjectSumDetail.action";
				}
				$.ajax({
					url:url,
					data:{
						detailId:detailId,
						prjname:prjname,
						prjsum:prjsum,
						expenseId:expenseId
					},
					success:function(data){
						if(data.uuid==undefined || data.uuid=="undefined"){
							
						}else{
							$("#id"+i).val(data.uuid);
						}
					}
				});
				var dt = new Date();
				var h=dt.getHours();
				var m=dt.getMinutes();
				var s=dt.getSeconds();
				var currentdate=curdate+" "+h+":"+m+":"+s;
				var record="";
				var info="";
				if(detailId==""){
					record = "项目名称:"+ new_prjname　+",项目金额:"+ new_sum;
					info=username+"("+currentdate+")新增了"+new_prjname+"的记录";
					
				}else{
					record = "项目名称:"+ old_prjname　+",项目金额:"+ old_sum;
					info=username+"("+currentdate+")修改了"+new_prjname+"的记录,操作前的数据为："+record;
				}
				var modifyrecord = $("#modifyRecord").html();
				if(modifyrecord!=""){
					if(CheckExprole()){
						info=modifyrecord.trim()+"\n"+info;
					}else{
						info=modifyrecord.trim()+"<br/>"+info;
					}
				}
				$("#modifyRecord").html(info);
			}
			$("#prjname"+i).attr("disabled","disabled");
			$("#sum"+i).attr("readonly","disabled");
			$("#prjname"+i).parent().parent().find("input[name='enterPrjSum']").attr("disabled","disabled");
		}
		
		var old_prjname = "";
		var old_sum = "";
		function modifyprjsum(){
			var idList = GetSelIds();
			if(idList==","){
				alert("请选择一条记录");
				return;
			}
			if(idList.split(",").length>1){
				alert("一次只能修改一条记录");
				return;
			}
			var ids = idList.split(',');
			old_prjname = $("#prjname"+ids).val();
			old_sum = $("#sum"+ids).val();
			$("#prjname"+ids).removeAttr("disabled");
			$("#sum"+ids).removeAttr("readonly");
			$("#prjname"+ids).parent().parent().find("input[name='enterPrjSum']").removeAttr("disabled");
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
		
		String.prototype.trim = function() {
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
	</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="expenseInfoForm" id="expenseInfoForm"
		action="<%=request.getContextPath()%>/pages/expense/expenseInfo!update.action"
		method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" /> 
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="98%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" class="input_table">
						<input name="expenseId" id="expenseId" type="hidden" value='<s:property value="#request.expenseAccount.id"/>'>
						<tr>
							<td class="input_tablehead">编辑报销信息</td>
						</tr>
					</table>
					<table width="100%" class="input_table">
						<tr>
							<td colspan="2">
								<s:hidden name="expenseAccount.userId"></s:hidden>
								姓名：<s:property value="#request.expenseAccount.userName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="#request.expenseAccount.userName" id="userName" readonly="true" type="hidden" value='<s:property value="#request.expenseAccount.userName"/>'>
								部门：<s:property value="#request.expenseAccount.DetName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.detName" readonly="true" id="userName" type="hidden" value='<s:property value="#request.expenseAccount.DetName"/>'>
								报销状态：
								<s:if test='#request.expenseAccount.status=="0"'>新增</s:if>
								<s:elseif test='#request.expenseAccount.status=="1"'>行政审核中</s:elseif>
								<s:elseif test='#request.expenseAccount.status=="2"'>财务审核中</s:elseif>
								<s:elseif test='#request.expenseAccount.status=="3"'>财务审核通过，已发款</s:elseif>
								<s:elseif test='#request.expenseAccount.status=="4"'>行政审核不通过</s:elseif>
								<s:elseif test='#request.expenseAccount.status=="5"'>财务审核不通过</s:elseif>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.status" id="status" readonly="true" type="hidden" value='<s:property value="#request.expenseAccount.status"/>'>
								报销类型：出差费用报销&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="expenseAccount.type" id="type" readonly="true" type="hidden" value='出差费用报销'>
								<input name="expenseAccount.prjName" id="prjName" readonly="true" type="hidden" value='<s:property value="#request.expenseAccount.prjName"/>'>
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">申请报销金额</td>
							<td bgcolor="#FFFFFF">
								<!--<s:property value='#session.expenseSum'/>
								<input type="hidden" name="expenseAccount.sum" id="sum" readonly="readonly" value="">-->
								<input type="text" name="showsum" id="showsum" readonly="readonly" value='<s:property value="#request.expenseAccount.sum"/>' style="border:0px;"/>
								<input type="hidden" name="expenseAccount.sum" id="sum" readonly="readonly" value='<s:property value="#request.expenseAccount.sum"/>' style="border:0px;"/>元
							</td>
						</tr>
						<tr id="approvesumTr" style="display:none;">
							<td class="input_label2" width="8%">核准报销金额</td>
							<td bgcolor="#FFFFFF">
								<input type="hidden" name="curstatus" id="curstatus" value=""/>
								<input type="text" name="expenseAccount.approveSum" id="approveSum"  value='<s:property value="#request.expenseAccount.approveSum"/>' onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')"/>元
							</td>
						</tr>
						<tr id="approveTr">
							<td class="input_label2" id="approveTd">行政审核人员</td>
							<td bgcolor="#FFFFFF">
								<input name="updateman" id="updateman" readonly="true" type="hidden" value='<s:property value="#request.expenseAccount.updatemanid"/>'>
								<s:select name="expenseAccount.updatemanid" id="updatemanid"  list="#session.approvePerson" listKey="userid" listValue="username"></s:select>
							</td>
						</tr>
						<tr>
							<td class="input_label2">备注</td>
							<td bgcolor="#FFFFFF">
								<textarea name="expenseAccount.note" cols="73" rows="4" id="note" style="width:100%"><s:property value="#request.expenseAccount.note"/></textarea>
							</td>
						</tr>
					</table>  
					<table width="100%" cellpadding="0" cellspacing="0"
						bgcolor="#FFFFFF" id=downloadList>
						<tr>
							<td class="input_tablehead2" colspan="15">出差报销明细</td>
						</tr>
						<tr>
							<td nowrap width="8%" class="oracolumncenterheader"><div
									align="center">日期</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
									align="center">地点</div>
							</td>
							<td nowrap width="12%" class="oracolumncenterheader"><div
									align="center">项目名称</div>
							</td>
							<td nowrap width="10%" class="oracolumncenterheader"><div
									align="center">出差任务</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">往返车船飞机费</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
									align="center">出租车</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
									align="center">公共汽车</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
									align="center">住宿费</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
									align="center">通讯费</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">业务费用</div>
							</td>
							<td nowrap width="4%" class="oracolumncenterheader"><div
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
									align="center">操作</div></td>
						</tr>
						<tbody name="li" id="li" name="formlist" id="formlist"
							align="center">
							<s:iterator value="#session.expenselist" id="costDetail"
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
									<td align="center"><input name="detailId" type="hidden"
										value='<s:property value="id"/>' /> <input class="modifyTr"
										type="button" value="修改" style="width:36px;"/> <input class="removeTr"
										type="button" value="删除" style="width:36px;margin-left:-5px;"/></td>
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
								<td></td>
							</tr>
						</tbody>
					</table>
					<table width="100%" border="0" cellpadding="1" cellspacing="1"
						bgcolor="#FFFFFF" id=downloadList>
						<tr>
							<td class="input_tablehead2" colspan="8">费用报销明细表</td>
						</tr>
						<tr>
							<td nowrap width="8%" class="oracolumncenterheader"><div
									align="center">日期</div>
							</td>
							<td nowrap width="5%" class="oracolumncenterheader"><div
									align="center">地点</div>
							</td>
							<td nowrap width="27%" class="oracolumncenterheader"><div
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
							<td nowrap width="20%" class="oracolumncenterheader"><div
									align="center">业务及其他费用说明</div>
							</td>
							<td nowrap width="15%" class="oracolumncenterheader"><div
									align="center">操作</div></td>
						</tr>
						<tbody name="li" id="li" align="center">
							<s:iterator value="#session.travelDetailList" id="travelDetail"
								status="row">
								<tr>
									<td align="center"><s:property value="ddTime" /></td>
									<td align="center"><s:property value="didian" /></td>
									<td align="center"><s:property value="prjname" /></td>
									<td align="center"><s:property value="yewu" /></td>
									<td align="center"><s:property value="traffic" /></td>
									<td align="center"><s:property value="another" /></td>
									<td align="center"><s:property value="note" /></td>
									<td align="center"><input name="detailId" type="hidden"
										value='<s:property value="id"/>' /> <input class="modifyTr1"
										type="button" value="修改" /> <input class="removeTr1"
										type="button" value="删除" /></td>
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
								<td></td>
							</tr>
						</tbody>
					</table> 
					<table width="100%" border="1" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" id=prjAndSumList>
						<tr>
							<td colspan="6" class="input_tablehead2">项目报销金额<font color="red">(单击修改按钮进行数据的修改，数据修改后请及时点击“确认”按钮，进行数据的保存)</font></td>
							<input type="hidden" id="maxsize" name="maxsize" value="<s:property value='#session.prjSumSize' />"/>
						</tr>
						<tbody name="pslist" id="pslist" align="center">
							<s:iterator value="#session.savePrjSumList" id="prjsumlist" status="row">
								<tr id="prjsumtr<s:property value='#row.index'/>">
									<td align="center" width="5%;"><input type="radio" name="chkList" value="<s:property value='#row.index'/>"/></td>
									<td align="right" width="10%;">项目名称</td>
									<td align="left" width="40%;">
										<select name="prjname<s:property value='#row.index'/>" id="prjname<s:property value='#row.index'/>" style="width:100%" disabled="disabled">
											<s:iterator value="#session.projectSelect" id="project">
												    <option value="<s:property value='name'/>" <s:if test='name==prjname'>selected</s:if> ><s:property value='name'/></option>
											</s:iterator>
										</select>
									</td>
									<td align="right" width="10%;">报销金额</td>
									<td align="left" width="30%;">
										<input type="text" readonly="readonly" id="sum<s:property value='#row.index'/>" name="sum<s:property value='#row.index'/>" value='<s:property value="sum" />'  onkeyup="this.value=this.value.replace(/[^0-9\.]+/,'')" onKeyDown="this.value=this.value.replace(/[^0-9\.]+/,'')">元
										<input type="hidden" id="id<s:property value='#row.index'/>" name="id<s:property value='#row.index'/>" value='<s:property value="id" />'/>
									</td>
									<td width="5%;">
										<input type="button" id="enterPrjSum" name="enterPrjSum" value="确认" onClick="enterprjsum(<s:property value='#row.index'/>)" disabled="disabled" />
									</td>
								</tr>
							</s:iterator>
						</tbody>
						<tr>
							<td colspan="6">
								<div style="text-align:right;">
									<input type="button" id="addPrjSum" name="addPrjSum" value="新增" onClick="addprjsum()" />
									<input type="button" id="modifyPrjSum" name="modifyPrjSum" value="修改" onClick="modifyprjsum()" />
									<input type="button" id="delPrjSum" name="delPrjSum" value="删除" onClick="delprjsum()" />
									<input type="hidden" id="savePrjdata" name="savePrjdata" value=""/>
									<input type="hidden" id="saveSumdata" name="saveSumdata" value=""/>
									<input type="hidden" id="uppPrjdata" name="uppPrjdata" value=""/>
									<input type="hidden" id="uppSumdata" name="uppSumdata" value=""/>
									<input type="hidden" id="noSavePrjname" name="noSavePrjname" value="" />
									<input type="hidden" id="noSavePrjsum" name="noSavePrjsum" value="" />
									<input type="hidden" id="noUpdatePrjname" name="noUpdatePrjname" value="" />
									<input type="hidden" id="noUpdatePrjsum" name="noUpdatePrjsum" value="" />
									<input type="hidden" id="noUpdateId" name="noUpdateId" value="" />
									<input type="hidden" id="ids" name="ids" value=""/>
								</div>
							</td>
						</tr>
					</table>
					<table width="95%" class="input_table">
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
					<table width="95%" class="input_table">
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
			<table width="80%" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="center">
						<input type="button" value='确定' class="MyButton" onclick="savea()" image="../../images/share/f_closed.gif" id="ok" name="ok">
						<input type="button" value='打印报销明细单' class="MyButton" onclick="printDetaila()" image="../../images/share/f_closed.gif" id="printDetail" style="display:none;">
						<input type="button" value='打印报销费用单' class="MyButton" onclick="printCosta()" image="../../images/share/f_closed.gif" id="printCost" style="display:none;">
						<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton" onclick="closeModal(true);" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
			</form>
</body>
</html>
