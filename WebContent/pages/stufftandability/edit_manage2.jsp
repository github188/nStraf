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
            <style type="text/css">
<!--
.STYLE1 {
	color: #0000FF;
	font-weight: bold;
}
.STYLE2 {
	color: #FF0000;
	font-weight: bold;
}
-->
        </style>
</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){      
		
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
		
			function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		   var Currency = /^\d+(\.\d+)?$/;
		   var Number= /^\d+$/;
		   var Zip = /^[1-9]\d{5}$/;
		   var QQ = /^[1-9]\d{4,8}$/;
		   var Integer = /^[-\+]?\d+$/;
		   var integer = /^[+]?\d+$/;
		   var Double= /^[-\+]?\d+(\.\d+)?$/;
		   var double = /^[+]?\d+(\.\d+)?$/;
		   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		   var Chinese = /^[\u0391-\uFFE5]+$/;
		   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		
			//var itemNameValue=document.getElementsByName(itemName)[0].value
			var itemNameValue=itemName;
			
				var flag;
			switch(pattern){ 
			 case "Require":
				 flag = Require.test(itemNameValue);
				  break;
			 case "Email":
				 flag = Email.test(itemNameValue);
				  break;
			 case "Phone":
				 flag = Phone.test(itemNameValue);
				  break;
			 case "Mobile":
				 flag = Mobile.test(itemNameValue);
				  break;
			 case "Url":
				 flag = Url.test(itemNameValue);
				  break;
			 case "IdCard":
				 flag = IdCard.test(itemNameValue);
				  break;
			 case "Currency":
				 flag = Currency.test(itemNameValue);
				  break;
			 case "Number":
				 flag = Number.test(itemNameValue);
					  break;
			 case "Zip":
				 flag = Zip.test(itemNameValue);
				  break;
			 case "QQ":
				 flag = QQ.test(itemNameValue);
				  break;
			 case "integer":
				 flag = integer.test(itemNameValue);
				  break;		  
			 case "Integer":
			// if (itemNameValue.length>0)
				 flag = Integer.test(itemNameValue);
			//else 
			//	flag=true
				  break;
			 case "Double":
				 flag = Double.test(itemNameValue);
				  break;
			 case "double":
					 flag = double.test(itemNameValue);
					  break;
			 case "English":
				 flag = English.test(itemNameValue);
				  break;
			 case "Chinese":
				 flag = Chinese.test(itemNameValue);
				  break;
			 case "BankCard":
				 flag = BankCard.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag
		
		}
		
		function numClear(itemName){
			itemName.select();
		}
		
			function numVali() {
			var ret = 0, ret1 = 0, ret2 = 0,ret3 = 0, ret4 = 0, tmp = 0;

			document.getElementById("stufftandability.subtotal_m").value = 0;
			document.getElementById("stufftandability.subtotal_s").value = 0;
			ret1 = (parseFloat(document.getElementById("stufftandability.subtotal_m_1").value)*30/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_2").value)*30/10+ parseFloat(document.getElementById("stufftandability.subtotal_m_3").value)*20/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_4").value)*20/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_5").value) + parseFloat(document.getElementById("stufftandability.subtotal_m_6").value)*30/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_7").value)*20/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_8").value)*20/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_9").value)*20/10 + parseFloat(document.getElementById("stufftandability.subtotal_m_10").value)*20/10)*10/10;
			ret2 = (parseFloat(document.getElementById("stufftandability.subtotal_m_11").value) + parseFloat(document.getElementById("stufftandability.subtotal_m_12").value)+ parseFloat(document.getElementById("stufftandability.subtotal_m_13").value) + parseFloat(document.getElementById("stufftandability.subtotal_m_14").value))*10/10;
			document.getElementById("stufftandability.subtotal_m").value = (ret1+ ret2).toFixed(1);	
			document.getElementById("stufftandability.subtotal_m").value =document.getElementById("stufftandability.subtotal_m").value.replace(/(\.\d)\d+/ig,"$1");
			
			ret3 = (parseFloat(document.getElementById("stufftandability.subtotal_s_1").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_2").value)+ parseFloat(document.getElementById("stufftandability.subtotal_s_3").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_4").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_5").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_6").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_7").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_8").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_9").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_10").value))*10/10;
			ret4 = (parseFloat(document.getElementById("stufftandability.subtotal_s_11").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_12").value)+ parseFloat(document.getElementById("stufftandability.subtotal_s_13").value) + parseFloat(document.getElementById("stufftandability.subtotal_s_14").value))*10/10;
			document.getElementById("stufftandability.subtotal_s").value = (ret3+ ret4).toFixed(1);	
			document.getElementById("stufftandability.subtotal_s").value =document.getElementById("stufftandability.subtotal_s").value.replace(/(\.\d)\d+/ig,"$1");
		}
		
				function numVali_1(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_1").value)*30/10;
			
			 document.getElementById("stufftandability.subtotal_s_1").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_1").value = document.getElementById("stufftandability.subtotal_s_1").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_2(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_2").value)*30/10;
			
			 document.getElementById("stufftandability.subtotal_s_2").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_2").value = document.getElementById("stufftandability.subtotal_s_2").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_3(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_3").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_3").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_3").value = document.getElementById("stufftandability.subtotal_s_3").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_4(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_4").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_4").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_4").value = document.getElementById("stufftandability.subtotal_s_4").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_5(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_5").value);
			
			 document.getElementById("stufftandability.subtotal_s_5").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_5").value = document.getElementById("stufftandability.subtotal_s_5").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_6(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_6").value)*30/10;
			
			 document.getElementById("stufftandability.subtotal_s_6").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_6").value = document.getElementById("stufftandability.subtotal_s_6").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_7(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_7").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_7").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_7").value = document.getElementById("stufftandability.subtotal_s_7").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_8(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_8").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_8").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_8").value = document.getElementById("stufftandability.subtotal_s_8").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_9(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_9").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_9").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_9").value = document.getElementById("stufftandability.subtotal_s_9").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_10(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_10").value)*20/10;
			
			 document.getElementById("stufftandability.subtotal_s_10").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_10").value = document.getElementById("stufftandability.subtotal_s_10").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_11(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_11").value);
			
			 document.getElementById("stufftandability.subtotal_s_11").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_11").value = document.getElementById("stufftandability.subtotal_s_11").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_12(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_12").value);
			
			 document.getElementById("stufftandability.subtotal_s_12").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_12").value = document.getElementById("stufftandability.subtotal_s_12").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_13(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_13").value);
			
			 document.getElementById("stufftandability.subtotal_s_13").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_13").value = document.getElementById("stufftandability.subtotal_s_13").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_14(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
			else{
				tmp = parseFloat(itemName.value);
				if(tmp>5){
					itemName.value = 0;
					alert("请输入5以内的数字");
				}			
			}
            itemName.value = itemName.value.replace(/(\.\d)\d+/ig,"$1");
			ret = parseFloat(document.getElementById("stufftandability.subtotal_m_14").value);
			
			 document.getElementById("stufftandability.subtotal_s_14").value = ret.toFixed(1);
			 document.getElementById("stufftandability.subtotal_s_14").value = document.getElementById("stufftandability.subtotal_s_14").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/stufftandability/stufftandabilityinfo!update.action"
			method="post">
           
              <input type="hidden" name="stufftandability.id" value='<s:property value="stufftandability.id"/>'>
               <table width="99%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
			    <fieldset class="jui_fieldset" width="99%">
							<legend>
								测试人员素质与能力评价表
							</legend>
        <table width="98%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
        <br/>
                <td align="center" width="10%" bgcolor="#999999"><div align="center">月份</div></td>
                <td colspan="3" bgcolor="#FFFFFF"><input name="month_date" type="text" id="month_date" value='<s:property value="stufftandability.month_date"/>' style="border:0" size="12" readonly="readonly"></td>
                <td width="10%" align="center" bgcolor="#999999"><div align="center"> 考核人 </div></td>
                <td colspan="5"  bgcolor="#FFFFFF"><input name="user_id" type="text"  id="user_id" value='<s:property value="stufftandability.user_id"/>'  style="border:0" size="12"  maxlength="20" readonly></td>
              </tr>
              <tr>
                <td width="10%" bgcolor="#999999"><div align="center">序号</div></td>
                <td width="12%"  bgcolor="#999999"><div align="center">评价指标</div></td>
                <td width="37%"  bgcolor="#999999"><div align="center">典型行为或事件举例（参照标准）</div></td>
                <td width="10%"  bgcolor="#999999"><div align="center">系数</div></td>
                <td width="10%"  bgcolor="#999999"><div align="center">自评得分</div></td>
                <td width="20%"  bgcolor="#999999"><div align="center">自评说明</div></td>
                <td width="10%"  bgcolor="#999999"><div align="center">上级评分</div></td>
                <td width="20%"  bgcolor="#999999"><div align="center">上级说明</div></td>
                <td width="10%"  bgcolor="#999999"><div align="center">上上级评分</div></td>
                <td width="7%"  bgcolor="#999999"><div align="center">综合得分</div></td>
          </tr>
              
              <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">测试能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）没有测试经验，测试理论知识欠缺； </div>
                    <div align="left">（1分）具备一定的测试理论知识，但测试经验欠缺； </div>
                    <div align="left">（2分）熟练掌握黑盒测试，且熟悉用例和报告的编写，并通过黑盒考试；</div>
                    <div align="left">（3分）熟练掌握白盒测试、自动化测试和性能测试中一种，并通过其中一门考试；</div> 
                    <div align="left">（4分）熟练掌握白盒测试、自动化测试和性能测试中两种，并通过其中两门考试；</div>
                    <div align="left">（5分）对白盒测试、自动化测试和性能测试都熟练掌握，且通过所有科目考试。</div></td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                  <input name="stufftandability.subtotal_z_1" type="text" id="subtotal_z_1"  size="8" maxlength="11" class="bgg"  value='<s:property value="stufftandability.subtotal_z_1"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center">
                           <textarea cols="16" rows="12" name="stufftandability.remark_z_1" class="bgg"
												id="remark_z_1" readonly><s:property value="stufftandability.remark_z_1"/></textarea>
                         </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                  <input name="stufftandability.subtotal_d_1" type="text" id="subtotal_d_1"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_1"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="12" class="bgg" name="stufftandability.remark_d_1"
												id="remark_d_1" readonly><s:property value="stufftandability.remark_d_1"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_1" type="text" id="subtotal_m_1"  size="8" maxlength="10" onChange="numVali_1(this)" onBlur="numVali_1(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_1"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_1" type="text" id="subtotal_s_1" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_1"/>' readonly="readonly">
                      </div></td>
              </tr>    
                 <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">业务能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）对公司产品都不了解； </div>
                    <div align="left">（1分）基本了解公司产品中的任一种，且在指导下能开展测试；</div>
                    <div align="left">（2分）掌握公司产品中的任一种，能独立开展测试，且通过该业务的考试； </div>
                    <div align="left">（3分）掌握公司产品中的任两种，能独立开展测试，且通过该两项业务的考试； </div> 
                    <div align="left">（4分）掌握公司产品中的任三种，能独立开展测试，且通过该三项业务的考试；</div>
                    <div align="left">（5分）掌握公司产品中的三种以上，能独立开展测试，且通过这些业务的考试。</div>       
                    <div align="left">产品包括如下：C、P、V、SP、DLL、清分、VTM、SECOne、TellerMaster</div>         </td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_2" type="text" id="subtotal_z_2"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_z_2"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="13" class="bgg" name="stufftandability.remark_z_2"
												id="remark_z_2" readonly><s:property value="stufftandability.remark_z_2"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_2" type="text" id="subtotal_d_2"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_d_2"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="13" class="bgg"  name="stufftandability.remark_d_2"
												id="remark_d_2" readonly><s:property value="stufftandability.remark_d_2"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_2" type="text" id="subtotal_m_2" size="8" maxlength="10"  onChange="numVali_2(this)" onBlur="numVali_2(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_2"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_2" type="text" id="subtotal_s_2" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_2"/>' readonly="readonly">
                      </div></td>
              </tr> 
              
              <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">问题分析能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）不能发现问题，或对发现的问题束手无策；</div> 
					<div align="left">（1分）能够发现问题，但不会对问题进行分析；</div> 
					<div align="left">（2分）能够发现问题，能够对问题进行初步分析，但不够深入；</div> 
					<div align="left">（3分）能够发现问题，能够对问题分析清晰，并能给开发提出指导意见；</div>
					<div align="left">（4分）善于发现问题，且对问题分析和定位清晰，并能协助开发人员进行修改；</div>
					<div align="left">（5分）能够找出问题的根源，并通过编写规范或其他形式从源头规避问题再发生。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                  <input name="stufftandability.subtotal_z_3" type="text" id="subtotal_z_3"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_z_3"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center">
                           <textarea cols="16" rows="12" class="bgg" name="stufftandability.remark_z_3"
												id="stufftandability.remark_z_3" readonly><s:property value="stufftandability.remark_z_3"/></textarea>
                         </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_3" type="text" id="subtotal_d_3"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_3"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12"  name="stufftandability.remark_d_3"
												id="stufftandability.remark_d_3" class="bgg" readonly><s:property value="stufftandability.remark_d_3"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_3" type="text" id="subtotal_m_3"  size="8" maxlength="10"  onChange="numVali_3(this)" onBlur="numVali_3(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_3"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_3" type="text" id="subtotal_s_3" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_3"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
                 <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">4</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">沟通能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）沟通存在障碍，容易与对方争吵；</div> 
					<div align="left">（1分）基本上能表述清楚要沟通的内容；</div> 
					<div align="left">（2分）部门内部沟通无障碍，组间沟通顺畅；</div>
					<div align="left">（3分）跨部门沟通能力强，有说服力，语言表达能力较好；</div> 
					<div align="left">（4分）与客户沟通时能注意方法和技巧，善于表达；</div>
					<div align="left">（5分）善于运用沟通的各种技巧和手段，能控制住现场。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_4" type="text" id="subtotal_z_4"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_z_4"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="10" class="bgg" name="stufftandability.remark_z_4"
												id="stufftandability.remark_z_4" readonly><s:property value="stufftandability.remark_z_4"/></textarea>
                      </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_4" type="text" id="subtotal_d_4" size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_4"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="10" class="bgg" name="stufftandability.remark_d_4"
												id="stufftandability.remark_d_4" readonly><s:property value="stufftandability.remark_d_4"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_4" type="text" id="subtotal_m_4"  size="8" maxlength="10"  onChange="numVali_4(this)" onBlur="numVali_4(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_4"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_4" type="text" id="subtotal_s_4" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_4"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">5</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">开发能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）对开发语言都不熟悉；</div>  
					<div align="left">（1分）基本了解开发语言中的任一种；</div> 
					<div align="left">（2分）能够使用开发语言中的任一种进行开发；</div>  
					<div align="left">（3分）能够使用开发语言中的两种以上进行开发；</div>  
					<div align="left">（4分）熟练掌握C/C++、JAVA和C#语言中的任一种，且能独立进行程序开发；</div> 
					<div align="left">（5分）精通C/C++、JAVA和C#语言中的任两种或熟练掌握两种以上。</div>      
                     <div align="left">语言包括如下：C/C++、JAVA、C#、JavaScript、VBScript、HTML、Flex</div>  
                              </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_5" type="text" id="subtotal_z_5"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_z_5"/>'  readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg"  name="stufftandability.remark_z_5"
												id="stufftandability.remark_z_5"readonly><s:property value="stufftandability.remark_z_5"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_5" type="text" id="subtotal_d_5" size="8" maxlength="10"  class="bgg" value='<s:property value="stufftandability.subtotal_d_5"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg" name="stufftandability.remark_d_5"
							id="stufftandability.remark_d_5" readonly><s:property value="stufftandability.remark_d_5"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_5" type="text" id="subtotal_m_5"  size="8" maxlength="10" onChange="numVali_5(this)" onBlur="numVali_5(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="stufftandability.subtotal_m_5"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_5" type="text" id="subtotal_s_5" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_5"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">6</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">测试工具</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）不熟悉任何一款测试工具；</div>   
				<div align="left">（1分）能够使用测试工具中的任两种，并在测试中实际应用；</div>  
				<div align="left">（2分）能够熟练使用测试工具中的任三种，且在测试中实际应用；</div>   
				<div align="left">（3分）能够熟练使用测试工具中的任四种，且在测试中实际应用；</div>   
				<div align="left">（4分）能够熟练使用测试工具中的任五种，且在测试中实际应用；</div>  
				<div align="left">（5分）能够熟练使用测试工具中的五种以上，且在测试中实际应用。</div>    
                   <div align="left"> 测试工具包括如下：TrueBack、TestTools、SPTestTools、报文解析工具、模拟主机、Insure++、PCLint、Logiscope、QTP、LoadRunner、QC/TD、CheckStyle </div>
                              </td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_6" type="text" id="subtotal_z_6" class="bgg"  size="8" maxlength="10"  value='<s:property value="stufftandability.subtotal_z_6"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="15" class="bgg"  name="stufftandability.remark_z_6"
												id="stufftandability.remark_z_6" readonly><s:property value="stufftandability.remark_z_6"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_6" type="text" id="subtotal_d_6"  size="8" maxlength="10" class="bgg"   value='<s:property value="stufftandability.subtotal_d_6"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="15"  name="stufftandability.remark_d_6" class="bgg" 
												id="stufftandability.remark_d_6" readonly><s:property value="stufftandability.remark_d_6"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_6" type="text" id="subtotal_m_6"  size="8" maxlength="10"  onChange="numVali_6(this)" onBlur="numVali_6(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_6"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_6" type="text" id="subtotal_s_6" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_6"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
   
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">7</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">数据库掌握</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）对数据库都不了解；</div>  
				<div align="left">（1分）基本了解数据库中的任一种；</div>  
				<div align="left">（2分）基本掌握数据库中的任一种；</div>  
				<div align="left">（3分）熟练掌握数据库中的任一种；</div>  
				<div align="left">（4分）精通SQL SEVER、ORACLE、SYBASE和DB2中的任一种或熟练掌握两种；</div> 
				<div align="left">（5分）精通SQL SEVER、ORACLE、SYBASE和DB2中的任两种或熟练掌握两种以上。</div>    
                <div align="left">数据库包括如下：SQL SERVER、ORACLE、SYBASE、ACCESS、DB2、Informix、MySQL</div> 
                           </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_7" type="text" id="subtotal_z_7" class="bgg" size="8" maxlength="10"  value='<s:property value="stufftandability.subtotal_z_7"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg" name="stufftandability.remark_z_7"
												id="stufftandability.remark_z_7" readonly><s:property value="stufftandability.remark_z_7"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_7" type="text" id="subtotal_d_7"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_7"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg"  name="stufftandability.remark_d_7"
												id="stufftandability.remark_d_7" readonly><s:property value="stufftandability.remark_d_7"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_7" type="text" id="subtotal_m_7"  size="8" maxlength="10"  onChange="numVali_7(this)" onBlur="numVali_7(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_7"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_7" type="text" id="subtotal_s_7" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_7"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">8</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">文档编写能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）不会编写文档；</div>  
				<div align="left">（1分）文档编写质量较差；</div>  
				<div align="left">（2分）文档编写质量一般，语言表述不够精简；</div>  
				<div align="left">（3分）文档编写格式规范，思路清晰；</div>  
				<div align="left">（4分）文档编写质量良好，且排版美观；</div> 
				<div align="left">（5分）善于文档编写，思路清晰，排版美观，且有一定的创意。</div>   
                    <div align="left">文档包括如下：含报告、缺陷描述、方案、PPT、其他文档等</div>    
                                </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_8" type="text" id="subtotal_z_8" class="bgg" size="8" maxlength="10" value='<s:property value="stufftandability.subtotal_z_8"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="8" class="bgg" name="stufftandability.remark_z_8"
												id="stufftandability.remark_z_8" readonly><s:property value="stufftandability.remark_z_8"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_8" type="text" id="subtotal_d_8"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_d_8"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="8"  name="stufftandability.remark_d_8" class="bgg"
												id="stufftandability.remark_d_8" readonly><s:property value="stufftandability.remark_d_8"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_8" type="text" id="subtotal_m_8" size="8" maxlength="10"  onChange="numVali_8(this)" onBlur="numVali_8(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_8"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_8" type="text" id="subtotal_s_8" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_8"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">9</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">标准掌握</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）对标准和规范都不了解；</div>  
				 <div align="left">（1分）基本了解标准和规范中的任一种；</div>  
 				<div align="left">（2分）基本掌握标准和规范中的任一种；</div>  
  				<div align="left">（3分）熟练掌握标准和规范中的任一种；</div>  
				 <div align="left">（4分）精通标准和规范中的任一种或熟练掌握两种；</div> 
 				<div align="left">（5分）精通标准和规范中的任两种或熟练掌握两种以上。</div>  
                <div align="left">协议标准包括如下:ISO8583、PBOC/EMV、EPP、NDC、XFS、公司的CPV规范</div>       
                             </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_9" type="text" id="subtotal_z_9"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_z_9"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="9" class="bgg" name="stufftandability.remark_z_9"
												id="stufftandability.remark_z_9" readonly><s:property value="stufftandability.remark_z_9"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_9" type="text" id="subtotal_d_9"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_d_9"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="9"  name="stufftandability.remark_d_9" class="bgg"
												id="stufftandability.remark_d_9" readonly><s:property value="stufftandability.remark_d_9"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_9" type="text" id="subtotal_m_9"  size="8" maxlength="10" onChange="numVali_9(this)" onBlur="numVali_9(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_9"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_9" type="text" id="subtotal_s_9" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_9"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
                  <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">10</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">时间管理能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）时间管理混乱，分不清任务的轻重缓急，没有计划性；</div>  
				<div align="left">（1分）有一定计划性，任务基本上都能按时完成；</div>  
				<div align="left">（2分）做事有计划，临时任务能用标签或工具记录下来，以作备忘；</div>  
				<div align="left">（3分）时间安排得井井有条，任务的轻重缓急都很明确，都能按计划完成任务；</div>  
				<div align="left">（4分）善于时间管理，做事效率高，任务都能超额完成；</div> 
				<div align="left">（5分）做事效率高，计划任务能提前完成，且新增任务也能按时完成。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_10" type="text" id="subtotal_z_10"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_z_10"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="13" class="bgg" name="stufftandability.remark_z_10"
												id="stufftandability.remark_z_10" readonly><s:property value="stufftandability.remark_z_10"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_10" type="text" id="subtotal_d_10"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_10"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="13"  name="stufftandability.remark_d_10" class="bgg"
												id="stufftandability.remark_d_10" readonly><s:property value="stufftandability.remark_d_10"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_10" type="text" id="subtotal_m_10"  size="8" maxlength="10"  onChange="numVali_10(this)" onBlur="numVali_10(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_10"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_10" type="text" id="subtotal_s_10" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_10"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">11</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">培训能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）未组织过任何培训，也未开发过课件；</div> 
				<div align="left">（1分）组织过一次以上培训或开发过一篇以上课件；</div> 
				<div align="left">（2分）组织过三次以上内部培训或一次以上跨部门培训或开发过两篇以上课件；</div> 
				<div align="left">（3分）组织过五次以上内部培训或三次以上跨部门培训或开发过四篇以上课件；</div> 
				<div align="left">（4分）组织过八次以上内部培训或五次以上跨部门培训或开发过五篇以上课件；</div>
				<div align="left">（5分）组织过十次以上内部培训或八次以上跨部门培训或开发过八篇以上课件或被评为公司优秀讲师。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_11" type="text" id="subtotal_z_11" class="bgg" size="8" maxlength="15" value='<s:property value="stufftandability.subtotal_z_11"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="13" class="bgg" name="stufftandability.remark_z_11"
												id="stufftandability.remark_z_11" readonly><s:property value="stufftandability.remark_z_11"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_11" type="text" id="subtotal_d_11"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_11"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="13" name="stufftandability.remark_d_11" class="bgg"
												id="stufftandability.remark_d_11" readonly><s:property value="stufftandability.remark_d_11"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_11" type="text" id="subtotal_m_11"  size="8" maxlength="10" onChange="numVali_11(this)" onBlur="numVali_11(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_11"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_11" type="text" id="subtotal_s_11" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_11"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
                     <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">12</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">风险控制能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）不会发现和预测风险，对发生的风险也无法应对；</div>    
				<div align="left">（1分）风险发生时，能及时反馈给相关人员；</div>    
				<div align="left">（2分）风险发生时，能及时反馈，并能较好的控制和预防；</div>    
				<div align="left">（3分）能够预测到风险，并及时反馈给相关人员；</div>    
				<div align="left">（4分）能准确预测到风险，并能较好的控制和预防；</div>   
				<div align="left">（5分）预测到较大风险，并及时处理和预防，给公司或组织消除了较大的隐患。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_12" type="text" id="subtotal_z_12"  size="8" maxlength="15" class="bgg"  value='<s:property value="stufftandability.subtotal_z_12"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12" class="bgg" name="stufftandability.remark_z_12"
												id="stufftandability.remark_z_12" readonly><s:property value="stufftandability.remark_z_12"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_12" type="text" id="subtotal_d_12"  size="8" maxlength="10" class="bgg"    value='<s:property value="stufftandability.subtotal_d_12"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12"  name="stufftandability.remark_d_12" class="bgg" 
												id="stufftandability.remark_d_12" readonly><s:property value="stufftandability.remark_d_12"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_12" type="text" id="subtotal_m_12" size="8" maxlength="10" onChange="numVali_12(this)" onBlur="numVali_12(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_12"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_12" type="text" id="subtotal_s_12" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_12"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
                     <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">13</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">创新能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）工作中不会主动思考，无创新意识；</div>   
					<div align="left">（1分）工作中主动思考，能提出有效的改进意见并被采纳；</div>   
					<div align="left">（2分）工作中主动使用或编写工具来辅助测试；</div>   
					<div align="left">（3分）引入新工具或新方法，并在部门内部推广应用；</div>   
					<div align="left">（4分）引入新工具或新方法，并在软件体系内部推广应用；</div>  
					<div align="left">（5分）引入新工具或新方法，并在公司内部推广应用；或完成一份专利的编写。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_13" type="text" id="subtotal_z_13"  size="8" maxlength="15" class="bgg"  value='<s:property value="stufftandability.subtotal_z_13"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="11" class="bgg" name="stufftandability.remark_z_13"
												id="stufftandability.remark_z_13" readonly><s:property value="stufftandability.remark_z_13"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_13" type="text" id="subtotal_d_13"  size="8" maxlength="10" class="bgg"  value='<s:property value="stufftandability.subtotal_d_13"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="11"  name="stufftandability.remark_d_13" class="bgg"
												id="stufftandability.remark_d_13" readonly><s:property value="stufftandability.remark_d_13"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_13" type="text" id="subtotal_m_13"  size="8" maxlength="10"  onChange="numVali_13(this)" onBlur="numVali_13(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="stufftandability.subtotal_m_13"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_13" type="text" id="subtotal_s_13" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_13"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
                 <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">14</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">组织管理能力</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）无组织管理能力；</div>   
				<div align="left">（1分）有一定的组织管理能力，但团队成员有部分人有消极情绪；</div>   
				<div align="left">（2分）能较好的对团队进行管理，且协调工作和监督工作做的较好；</div>   
				<div align="left">（3分）能带领团队较好的完成项目，且受到开发或客户的表扬；</div>   
				<div align="left">（4分）能带领团队高效完成工作，团队成员能主动加班加点，毫无怨言；</div>  
				<div align="left">（5分）带领的团队气氛活跃，团队成员积极向上、凝聚力强、战斗力强。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_z_14" type="text" id="subtotal_z_14"  size="8" maxlength="15" class="bgg" value='<s:property value="stufftandability.subtotal_z_14"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12" class="bgg" name="stufftandability.remark_z_14"
												id="stufftandability.remark_z_14" readonly><s:property value="stufftandability.remark_z_14"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_d_14" type="text" id="subtotal_d_14"  size="8" maxlength="10" class="bgg" value='<s:property value="stufftandability.subtotal_d_14"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12"  name="stufftandability.remark_d_14" class="bgg"
												id="stufftandability.remark_d_14" readonly><s:property value="stufftandability.remark_d_14"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m_14" type="text" id="subtotal_m_14"  size="8" maxlength="10"  onChange="numVali_14(this)" onBlur="numVali_14(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="stufftandability.subtotal_m_14"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s_14" type="text" id="subtotal_s_14" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s_14"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
        <tr>
                <td bgcolor="#CCFFFF"><div align="center">合计</div></td>
          <td colspan="3"  bgcolor="#CCFFFF"><div align="left"></div>                  <div align="center"></div>                  <div align="center"></div></td>
   <td  bgcolor="#CCFFFF"><div align="center">
                     <input name="stufftandability.subtotal_z" type="text" id="subtotal_z" class="bgg" size="8" maxlength="10" value='<s:property value="stufftandability.subtotal_z"/>' format="NNN" readonly="readonly">
                   </div></td>
                   <td  bgcolor="#CCFFFF"></td>
   <td  bgcolor="#CCFFFF"><div align="center">
     <input name="stufftandability.subtotal_d" type="text" id="subtotal_d" class="bgg" size="8" maxlength="10" value='<s:property value="stufftandability.subtotal_d"/>' format="NNN" readonly="readonly">
   </div></td>
                   <td  bgcolor="#CCFFFF"></td>
<td  bgcolor="#CCFFFF"><div align="center">
                        <input name="stufftandability.subtotal_m" type="text" id="subtotal_m"  class="bgg" size="8" maxlength="10" value='<s:property value="stufftandability.subtotal_m"/>' readonly="readonly">
                      </div></td>
                      
                     
<td   bgcolor="#CCFFFF"><div align="center">
                        <input name="stufftandability.subtotal_s" type="text" id="subtotal_s" class="bgg" size="8" maxlength="12" value='<s:property value="stufftandability.subtotal_s"/>' readonly="readonly">
          </div></td>
        </tr>
          </table>

					  </fieldset>
					</td>
				</tr>
		
			</table>
	    <br />
			<br />
			<table width="80%" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="center">
						<input type="button" name="ok" id="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onClick="save()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>
	</form>
	</body>
</html>
