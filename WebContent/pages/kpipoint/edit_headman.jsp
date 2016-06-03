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

		   return flag
		
		}
		
		function numClear(itemName){
			itemName.select();
		}
		
	function numVali() {
			var ret = 0, ret1 = 0, ret2 = 0, ret3 = 0, tmp = 0;
			document.getElementById("kpipoint.subtotal_s").value = 0;
			ret1 = parseFloat(document.getElementById("kpipoint.subtotal_1").value) + parseFloat(document.getElementById("kpipoint.subtotal_2").value)+ parseFloat(document.getElementById("kpipoint.subtotal_3").value) + parseFloat(document.getElementById("kpipoint.subtotal_4").value) + parseFloat(document.getElementById("kpipoint.subtotal_5").value) + parseFloat(document.getElementById("kpipoint.subtotal_6").value) + parseFloat(document.getElementById("kpipoint.subtotal_7").value) + parseFloat(document.getElementById("kpipoint.subtotal_8").value) + parseFloat(document.getElementById("kpipoint.subtotal_9").value) + parseFloat(document.getElementById("kpipoint.subtotal_10").value);
			ret2 = parseFloat(document.getElementById("kpipoint.subtotal_11").value) + parseFloat(document.getElementById("kpipoint.subtotal_12").value)+ parseFloat(document.getElementById("kpipoint.subtotal_13").value) + parseFloat(document.getElementById("kpipoint.subtotal_14").value) + parseFloat(document.getElementById("kpipoint.subtotal_15").value) + parseFloat(document.getElementById("kpipoint.subtotal_16").value) + parseFloat(document.getElementById("kpipoint.subtotal_17").value) + parseFloat(document.getElementById("kpipoint.subtotal_18").value) + parseFloat(document.getElementById("kpipoint.subtotal_19").value) + parseFloat(document.getElementById("kpipoint.subtotal_20").value) + parseFloat(document.getElementById("kpipoint.subtotal_31").value) + parseFloat(document.getElementById("kpipoint.subtotal_33").value) + parseFloat(document.getElementById("kpipoint.subtotal_34").value);
			ret3 = parseFloat(document.getElementById("kpipoint.subtotal_21").value) + parseFloat(document.getElementById("kpipoint.subtotal_22").value)+ parseFloat(document.getElementById("kpipoint.subtotal_23").value) - parseFloat(document.getElementById("kpipoint.subtotal_24").value) - parseFloat(document.getElementById("kpipoint.subtotal_25").value) - parseFloat(document.getElementById("kpipoint.subtotal_26").value) - parseFloat(document.getElementById("kpipoint.subtotal_27").value) - parseFloat(document.getElementById("kpipoint.subtotal_28").value) - parseFloat(document.getElementById("kpipoint.subtotal_29").value) - parseFloat(document.getElementById("kpipoint.subtotal_30").value)- parseFloat(document.getElementById("kpipoint.subtotal_32").value)-parseFloat(document.getElementById("kpipoint.subtotal_35").value);			
			document.getElementById("kpipoint.subtotal_s").value = ret1+ ret2 + ret3;
			document.getElementById("kpipoint.subtotal_s").value = document.getElementById("kpipoint.subtotal_s").value.replace(/(\.\d)\d+/ig,"$1");
		}
		
		function numVali_1(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			 ret = parseFloat(document.getElementById("kpipoint.number_1").value)/parseFloat(document.getElementById("kpipoint.coeff_1").value);
			
			 document.getElementById("kpipoint.subtotal_1").value = ret;
			 document.getElementById("kpipoint.subtotal_1").value = document.getElementById("kpipoint.subtotal_1").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		
		function numVali_2(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_2").value = parseFloat(document.getElementById("kpipoint.number_2").value)/parseFloat(document.getElementById("kpipoint.coeff_2").value);
			document.getElementById("kpipoint.subtotal_2").value = document.getElementById("kpipoint.subtotal_2").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_3(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_3").value = parseFloat(document.getElementById("kpipoint.number_3").value)/parseFloat(document.getElementById("kpipoint.coeff_3").value);
			document.getElementById("kpipoint.subtotal_3").value = document.getElementById("kpipoint.subtotal_3").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_3").value)>20)
			{
				document.getElementById("kpipoint.subtotal_3").value = 20;
			}
			numVali();
		}
		
				function numVali_4(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_4").value = parseFloat(document.getElementById("kpipoint.number_4").value)/parseFloat(document.getElementById("kpipoint.coeff_4").value);
			document.getElementById("kpipoint.subtotal_4").value = document.getElementById("kpipoint.subtotal_4").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_5(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_5").value = parseFloat(document.getElementById("kpipoint.number_5").value)/parseFloat(document.getElementById("kpipoint.coeff_5").value);
			document.getElementById("kpipoint.subtotal_5").value = document.getElementById("kpipoint.subtotal_5").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_6(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_6").value = parseFloat(document.getElementById("kpipoint.number_6").value)/parseFloat(document.getElementById("kpipoint.coeff_6").value);
			document.getElementById("kpipoint.subtotal_6").value = document.getElementById("kpipoint.subtotal_6").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_7(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_7").value = (parseFloat(document.getElementById("kpipoint.number_7").value)/20) * parseFloat(document.getElementById("kpipoint.coeff_7").value);
			document.getElementById("kpipoint.subtotal_7").value = document.getElementById("kpipoint.subtotal_7").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_7").value)>20)
			{
				document.getElementById("kpipoint.subtotal_7").value = 20;
			}
			numVali();
		}
		
		function numVali_8(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_8").value = (parseFloat(document.getElementById("kpipoint.number_8").value)/100) * parseFloat(document.getElementById("kpipoint.coeff_8").value);
			document.getElementById("kpipoint.subtotal_8").value = document.getElementById("kpipoint.subtotal_8").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_8").value)>20)
			{
				document.getElementById("kpipoint.subtotal_8").value = 20;
			}
			numVali();
		}
		
		function numVali_9(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_9").value = (parseFloat(document.getElementById("kpipoint.number_9").value)/50) * parseFloat(document.getElementById("kpipoint.coeff_9").value);
			document.getElementById("kpipoint.subtotal_9").value = document.getElementById("kpipoint.subtotal_9").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_9").value)>20)
			{
				document.getElementById("kpipoint.subtotal_9").value = 20;
			}
			numVali();
		}
		
		function numVali_10(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_10").value = (parseFloat(document.getElementById("kpipoint.number_10").value)/100) * parseFloat(document.getElementById("kpipoint.coeff_10").value);
			document.getElementById("kpipoint.subtotal_10").value = document.getElementById("kpipoint.subtotal_10").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_10").value)>20)
			{
				document.getElementById("kpipoint.subtotal_10").value = 20;
			}
			numVali();
		}
		
		function numVali_11(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_11").value = parseFloat(document.getElementById("kpipoint.number_11").value) * parseFloat(document.getElementById("kpipoint.coeff_11").value);
			document.getElementById("kpipoint.subtotal_11").value = document.getElementById("kpipoint.subtotal_11").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_11").value)>10)
			{
				document.getElementById("kpipoint.subtotal_11").value = 10;
			}
			numVali();
		}
		
		function numVali_13(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_13").value = parseFloat(document.getElementById("kpipoint.number_13").value) * parseFloat(document.getElementById("kpipoint.coeff_13").value);
			document.getElementById("kpipoint.subtotal_13").value = document.getElementById("kpipoint.subtotal_13").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_14(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_14").value = parseFloat(document.getElementById("kpipoint.number_14").value) * parseFloat(document.getElementById("kpipoint.coeff_14").value);
			document.getElementById("kpipoint.subtotal_14").value = document.getElementById("kpipoint.subtotal_14").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_14").value)>10)
			{
				document.getElementById("kpipoint.subtotal_14").value = 10;
			}
			numVali();
		}
		
		function numVali_15(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_15").value = parseFloat(document.getElementById("kpipoint.number_15").value) * parseFloat(document.getElementById("kpipoint.coeff_15").value);
			document.getElementById("kpipoint.subtotal_15").value = document.getElementById("kpipoint.subtotal_15").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_16(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_16").value = parseFloat(document.getElementById("kpipoint.number_16").value) * parseFloat(document.getElementById("kpipoint.coeff_16").value);
			document.getElementById("kpipoint.subtotal_16").value = document.getElementById("kpipoint.subtotal_16").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_16").value)>10)
			{
				document.getElementById("kpipoint.subtotal_16").value = 10;
			}
			numVali();
		}
		
		function numVali_17(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_17").value = parseFloat(document.getElementById("kpipoint.number_17").value) * parseFloat(document.getElementById("kpipoint.coeff_17").value);
			document.getElementById("kpipoint.subtotal_17").value = document.getElementById("kpipoint.subtotal_17").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_17").value)>10)
			{
				document.getElementById("kpipoint.subtotal_17").value = 10;
			}
			numVali();
		}
		
		function numVali_18(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_18").value = parseFloat(document.getElementById("kpipoint.number_18").value) * parseFloat(document.getElementById("kpipoint.coeff_18").value);
			document.getElementById("kpipoint.subtotal_18").value = document.getElementById("kpipoint.subtotal_18").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_18").value)>10)
			{
				document.getElementById("kpipoint.subtotal_18").value = 10;
			}
			numVali();
		}
		
		function numVali_20(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_20").value = parseFloat(document.getElementById("kpipoint.number_20").value) * parseFloat(document.getElementById("kpipoint.coeff_20").value);
			document.getElementById("kpipoint.subtotal_20").value = document.getElementById("kpipoint.subtotal_20").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_21(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_21").value = parseFloat(document.getElementById("kpipoint.number_21").value) / parseFloat(document.getElementById("kpipoint.coeff_21").value);
			document.getElementById("kpipoint.subtotal_21").value = document.getElementById("kpipoint.subtotal_21").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_22(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_22").value = parseFloat(document.getElementById("kpipoint.number_22").value) * parseFloat(document.getElementById("kpipoint.coeff_22").value);
			document.getElementById("kpipoint.subtotal_22").value = document.getElementById("kpipoint.subtotal_22").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
				function numVali_23(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_23").value = parseFloat(document.getElementById("kpipoint.number_23").value) * parseFloat(document.getElementById("kpipoint.coeff_23").value);
			document.getElementById("kpipoint.subtotal_23").value = document.getElementById("kpipoint.subtotal_23").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
				function numVali_24(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_24").value = parseFloat(document.getElementById("kpipoint.number_24").value) * parseFloat(document.getElementById("kpipoint.coeff_24").value);
			document.getElementById("kpipoint.subtotal_24").value = document.getElementById("kpipoint.subtotal_24").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
				function numVali_25(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_25").value = parseFloat(document.getElementById("kpipoint.number_25").value) * parseFloat(document.getElementById("kpipoint.coeff_25").value);
			document.getElementById("kpipoint.subtotal_25").value = document.getElementById("kpipoint.subtotal_25").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_26(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_26").value = parseFloat(document.getElementById("kpipoint.number_26").value) * parseFloat(document.getElementById("kpipoint.coeff_26").value);
			document.getElementById("kpipoint.subtotal_26").value = document.getElementById("kpipoint.subtotal_26").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
			}
		
			function numVali_27(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_27").value = parseFloat(document.getElementById("kpipoint.number_27").value) * parseFloat(document.getElementById("kpipoint.coeff_27").value);
			document.getElementById("kpipoint.subtotal_27").value = document.getElementById("kpipoint.subtotal_27").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
			function numVali_28(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_28").value = parseFloat(document.getElementById("kpipoint.number_28").value) * parseFloat(document.getElementById("kpipoint.coeff_28").value);
			document.getElementById("kpipoint.subtotal_28").value = document.getElementById("kpipoint.subtotal_28").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		
		function numVali_29(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
            document.getElementById("kpipoint.subtotal_29").value = parseFloat(document.getElementById("kpipoint.number_29").value) * parseFloat(document.getElementById("kpipoint.coeff_29").value);
			document.getElementById("kpipoint.subtotal_29").value = document.getElementById("kpipoint.subtotal_29").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}

		function numVali_30(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
            document.getElementById("kpipoint.subtotal_30").value = parseFloat(document.getElementById("kpipoint.number_30").value) * parseFloat(document.getElementById("kpipoint.coeff_30").value);
			document.getElementById("kpipoint.subtotal_30").value = document.getElementById("kpipoint.subtotal_30").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
			function numVali_31(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}

			document.getElementById("kpipoint.subtotal_31").value = parseFloat(document.getElementById("kpipoint.number_31").value)/parseFloat(document.getElementById("kpipoint.coeff_31").value);
			document.getElementById("kpipoint.subtotal_31").value = document.getElementById("kpipoint.subtotal_31").value.replace(/(\.\d)\d+/ig,"$1");
			if(parseFloat(document.getElementById("kpipoint.subtotal_31").value)>20)
			{
				document.getElementById("kpipoint.subtotal_31").value = 20;
			}
			numVali();
		}
		
			function numVali_32(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
            document.getElementById("kpipoint.subtotal_32").value = parseFloat(document.getElementById("kpipoint.number_32").value) * parseFloat(document.getElementById("kpipoint.coeff_32").value);
			document.getElementById("kpipoint.subtotal_32").value = document.getElementById("kpipoint.subtotal_32").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		function numVali_33(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
            document.getElementById("kpipoint.subtotal_33").value = parseFloat(document.getElementById("kpipoint.number_33").value) * parseFloat(document.getElementById("kpipoint.coeff_33").value);
			document.getElementById("kpipoint.subtotal_33").value = document.getElementById("kpipoint.subtotal_33").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
		function numVali_34(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Double"))
			{
				itemName.value = 0;
				alert("请输入数字");
			}
            document.getElementById("kpipoint.subtotal_34").value = parseFloat(document.getElementById("kpipoint.number_34").value) * parseFloat(document.getElementById("kpipoint.coeff_34").value);
			document.getElementById("kpipoint.subtotal_34").value = document.getElementById("kpipoint.subtotal_34").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/kpipoint/kpipointinfo!update.action"
			method="post">
            <input type="hidden" name="kpipoint.id" value='<s:property value="kpipoint.id"/>'>
      <input type="hidden" id="firstedit" name="kpipoint.firstedit" value='<s:property value="kpipoint.firstedit"/>'>
              <input type="hidden" name="UserEffect_score" id="UserEffect_score" value='<s:property value="UserEffect_score"/>'>
              <input type="hidden" name="UserPrice_score" id="UserPrice_score" value='<s:property value="UserPrice_score"/>'>
              <input type="hidden" name="UserEffect_num" id="UserEffect_num" value='<s:property value="UserEffect_num"/>'>
              <input type="hidden" name="UserPrice_num" id="UserPrice_num" value='<s:property value="UserPrice_num"/>'>
          <input type="hidden" name="UserheadmanAddValue" id="UserheadmanAddValue" value='<s:property value="UserheadmanAddValue"/>'>
         <input type="hidden" name="UserfellowValue" id="UserfellowValue" value='<s:property value="UserfellowValue"/>'>
               <input type="hidden" name="UserUnwriteNum" id="UserUnwriteNum" value='<s:property value="UserUnwriteNum"/>'>
              <input type="hidden" name="UserTrain_num" id="UserTrain_num" value='<s:property value="UserTrain_num"/>'>
                               <input type="hidden" name="Userbugtotal" id="Userbugtotal" value='<s:property value="Userbugtotal"/>'>
                 <input type="hidden" name="UserbugYanZhengtotal" id="UserbugYanZhengtotal" value='<s:property value="UserbugYanZhengtotal"/>'>
                    <input type="hidden" name="UserModifybugtotal" id="UserModifybugtotal" value='<s:property value="UserModifybugtotal"/>'>
                       <input type="hidden" name="UserCaseExec" id="UserCaseExec" value='<s:property value="UserCaseExec"/>'>
                          <input type="hidden" name="UserCaseNewModify" id="UserCaseNewModify" value='<s:property value="UserCaseNewModify"/>'>
                 <input type="hidden" name="SuggestionDay" id="SuggestionDay" value='<s:property value="SuggestionDay"/>'>
                          <input type="hidden" name="SuggestionRemark" id="SuggestionRemark" value='<s:property value="SuggestionRemark"/>'>
               <table width="99%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
			    <fieldset class="jui_fieldset" width="99%">
							<legend>
								测试人员月度KPI评价
							</legend>
        <table width="98%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none"><tr><td align="left" bgcolor="#FFFFFF"><table width="98%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
          <br/>
  <td align="center" width="7%" bgcolor="#999999"><div align="center">月份</div></td>
      <td colspan="2" bgcolor="#FFFFFF"><input name="month_date" type="text" id="month_date" value='<s:property value="kpipoint.month_date"/>' style="border:0" size="10" readonly="readonly"></td>
    <td width="7%" align="center" bgcolor="#999999"><div align="center"> 考核人 </div></td>
    <td colspan="5"  bgcolor="#FFFFFF"><input name="user_id" type="text"  id="user_id" value='<s:property value="kpipoint.user_id"/>'  style="border:0" size="10"  maxlength="20" readonly></td>
  </tr>
  <tr>
    <td width="7%" bgcolor="#999999"><div align="center">序号</div></td>
    <td width="7%"  bgcolor="#999999"><div align="center">分类</div></td>
    <td width="12%"  bgcolor="#999999"><div align="center">评价指标</div></td>
    <td width="7%"  bgcolor="#999999"><div align="center">数目</div></td>
    <td width="7%"  bgcolor="#999999"><div align="center">分值</div></td>
    <td width="7%"  bgcolor="#999999"><div align="center">系数</div></td>
    <td width="24%" bgcolor="#999999"><div align="center"> 备注</div></td>
    <td width="26%" bgcolor="#999999"><div align="center">说明</div></td>
  </tr>
  <tr>
    <td width="6%" bgcolor="#FFFFFF"><div align="center">1</div></td>
    <td width="7%" rowspan="26"  bgcolor="#FFFFFF"><div align="center">加分项</div>
        <div align="center"></div></td>
    <td  bgcolor="#FFFFFF"><div align="left">出勤天数</div></td>
    <td   bgcolor="#FFFFFF"><input name="kpipoint.number_1" type="text" id="number_1"  size="10" maxlength="10" onChange="numVali_1(this)" onBlur="numVali_1(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_1"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_1" type="text" id="subtotal_1" class="bgg" size="10" maxlength="10" value='<s:property value="kpipoint.subtotal_1"/>' readonly="readonly">
    </div></td>
        <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_1" type="text" id="coeff_1"  size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.coeff_1"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_1" type="text" id="remark_1" size="44" maxlength="44" value='<s:property value="kpipoint.remark_1"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">出勤天数/4，从考勤记录和日报获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">2</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">加班时长</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_2" type="text" id="number_2"  size="10" maxlength="10"  onChange="numVali_2(this)" onBlur="numVali_2(this)" onFocus="numClear(this)" value='<s:property value="kpipoint.number_2"/>'></td>
    <td  bgcolor="#FFFFFF" align="center"><input name="kpipoint.subtotal_2" type="text" id="subtotal_2" size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_2"/>' readonly="readonly"></td>
      <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_2" type="text" id="coeff_2"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_2"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_2" type="text" id="remark_2" size="44" maxlength="44" value='<s:property value="kpipoint.remark_2"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">加班时长/6，从考勤记录和日报获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">3</div></td>
    <td  bgcolor="#FFFFFF"><div align="left">提交BUG总值</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_3" type="text" id="number_3"  size="10" maxlength="10"  onChange="numVali_3(this)" onBlur="numVali_3(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_3"/>'></td>
    <td  bgcolor="#FFFFFF" align="center"><input name="kpipoint.subtotal_3" type="text" id="subtotal_3" size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_3"/>' readonly="readonly"></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_3" type="text" id="coeff_3"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_3"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_3" type="text" id="remark_3" size="44" maxlength="44" value='<s:property value="kpipoint.remark_3"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">缺陷总值/10，不超过20分；缺陷总值 = 致命*20 + 严重*10 + 一般*5 + 警告*3 + 建议，统计当月提交的不含Rejected的缺陷总数，从QC获取</td>
  </tr>
  
   <tr>
    <td bgcolor="#FFFFFF"><div align="center">4</div></td>
    <td  bgcolor="#FFFFFF"><div align="left">验证缺陷数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_31" type="text" id="number_31"  size="10" maxlength="10"  onChange="numVali_31(this)" onBlur="numVali_31(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_31"/>'></td>
    <td  bgcolor="#FFFFFF" align="center"><input name="kpipoint.subtotal_31" type="text" id="subtotal_31" size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_31"/>' readonly="readonly"></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_31" type="text" id="coeff_31"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_31"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_31" type="text" id="remark_31" size="44" maxlength="44" value='<s:property value="kpipoint.remark_31"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">(Closed+Reopen缺陷总数)/10，从QC获取，不超过20分</td>
  </tr>
  
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">5</div></td>
    <td   bgcolor="#FFFFFF">修改BUG总值</td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_4" type="text" id="number_4"  size="10" maxlength="10"  onChange="numVali_4(this)" onBlur="numVali_4(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_4"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_4" type="text" id="subtotal_4"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.subtotal_4"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_4" type="text" id="coeff_4"  size="10" maxlength="10"  class="bgg"  value='<s:property value="kpipoint.coeff_4"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_4" type="text" id="remark_4" size="44" maxlength="44" value='<s:property value="kpipoint.remark_4"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">缺陷总值/50，统计当月修改的不含Rejected的缺陷总数，从QC获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">6</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">提出问题建议数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_5" type="text" id="number_5"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.number_5"/>' readonly="readonly"></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_5" type="text" id="subtotal_5" size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_5"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_5" type="text" id="coeff_5"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_5"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_5" type="text" id="remark_5" size="44" maxlength="44" value='<s:property value="kpipoint.remark_5"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">价值分合计/3，从部门管理平台获取数据，不包含公司合理化建议</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">7</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">处理问题建议数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_6" type="text" id="number_6"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.number_6"/>' readonly="readonly"></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_6" type="text" id="subtotal_6"  size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_6"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_6" type="text" id="coeff_6"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_6"/>'  readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_6" type="text" id="remark_6" size="44" maxlength="44" value='<s:property value="kpipoint.remark_6"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">效果分合计/3，从部门管理平台获取数据</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">8</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">新增/修改用例数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_7" type="text" id="number_7"  size="10" maxlength="10"  onChange="numVali_7(this)" onBlur="numVali_7(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_7"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_7" type="text" id="subtotal_7"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_7"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_7" type="text" id="coeff_7"  size="10" maxlength="10"  onChange="numVali_7(this)" onBlur="numVali_7(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.coeff_7"/>' >
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_7" type="text" id="remark_7" size="44" maxlength="44" value='<s:property value="kpipoint.remark_7"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">（新增或修改用例数/20）*项目系数（默认为1），通过主任审核，主任可以根据项目的复杂性和难度来决定项目系数，不超过20分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">9</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">执行用例数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_8" type="text" id="number_8"  size="10" maxlength="10"  onChange="numVali_8(this)" onBlur="numVali_8(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_8"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_8" type="text" id="subtotal_8"  size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_8"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_8" type="text" id="coeff_8"  size="10" maxlength="10"  onChange="numVali_8(this)" onBlur="numVali_8(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.coeff_8"/>' >
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_8" type="text" id="remark_8" size="44" maxlength="44" value='<s:property value="kpipoint.remark_8"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">（执行用例数/100）*项目系数（默认为1），通过主任审核，主任可以根据项目的复杂性和难度来决定项目系数，不超过20分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">10</div></td>
    <td   bgcolor="#FFFFFF">新增/修改脚本数</td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_9" type="text" id="number_9"  size="10" maxlength="10"  onChange="numVali_9(this)" onBlur="numVali_9(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_9"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_9" type="text" id="subtotal_9"  size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_9"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_9" type="text" id="coeff_9"  size="10" maxlength="10"  onChange="numVali_9(this)" onBlur="numVali_9(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.coeff_9"/>' >
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_9" type="text" id="remark_9" size="44" maxlength="44" value='<s:property value="kpipoint.remark_9"/>'>
    </div></td>
    <td  align="left" bgcolor="#FFFFFF">（新增或修改脚本数/50）*项目系数（默认为1），通过主任审核，主任可以根据项目的复杂性和难度来决定项目系数，不超过20分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">11</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">新增/修改代码行数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_10" type="text" id="number_10"  size="10" maxlength="10"  onChange="numVali_10(this)" onBlur="numVali_10(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_10"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_10" type="text" id="subtotal_10"  size="10" class="bgg" maxlength="10" value='<s:property value="kpipoint.subtotal_10"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_10" type="text" id="coeff_10"  size="10" maxlength="10"  onChange="numVali_10(this)" onBlur="numVali_10(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.coeff_10"/>' >
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_10" type="text" id="remark_10" size="44" maxlength="44" value='<s:property value="kpipoint.remark_10"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">（新增或修改代码行数/100）*项目系数（默认为1），通过主任审核，主任可以根据项目的复杂性和难度来决定项目系数，不超过20分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">12</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">评审时提出有效建议数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_11" type="text" id="number_11"  size="10" maxlength="10"  onChange="numVali_11(this)" onBlur="numVali_11(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_11"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_11" type="text" id="subtotal_11"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_11"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_11" type="text" id="coeff_11"  size="10" maxlength="10"  class="bgg"  format="NN"  value='<s:property value="kpipoint.coeff_11"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_11" type="text" id="remark_11" size="44" maxlength="44" value='<s:property value="kpipoint.remark_11"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每条*2分，通过评审记录表获取，不超过10分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">13</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">组织培训次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_12" type="text" id="number_12" class="bgg" size="10" maxlength="10"  value='<s:property value="kpipoint.number_12"/>' readonly="readonly"></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_12" type="text" id="subtotal_12"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_12"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_12" type="text" id="coeff_12" size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_12"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_12" type="text" id="remark_12" size="44" maxlength="44" value='<s:property value="kpipoint.remark_12"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每次部门内部3分，跨部门5分，从部门管理平台获取数据</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">14</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">开发培训课件数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_13" type="text" id="number_13"  size="10" maxlength="10" onChange="numVali_13(this)" onBlur="numVali_13(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_13"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_13" type="text" id="subtotal_13"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_13"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_13" type="text" id="coeff_13"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_13"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_13" type="text" id="remark_13" size="44" maxlength="44" value='<s:property value="kpipoint.remark_13"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*5分，要求SVN归档，部门管理平台登记</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">15</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">串讲次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_14" type="text" id="number_14"  size="10" maxlength="10"  onChange="numVali_14(this)" onBlur="numVali_14(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_14"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_14" type="text" id="subtotal_14" size="10" class="bgg" maxlength="10"   value='<s:property value="kpipoint.subtotal_14"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_14" type="text" id="coeff_14"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_14"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_14" type="text" id="remark_14" size="44" maxlength="44" value='<s:property value="kpipoint.remark_14"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*2分，要有会议纪要，不超过10分</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">16</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">发表博客数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_15" type="text" id="number_15"  size="10" maxlength="10"  onChange="numVali_15(this)" onBlur="numVali_15(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_15"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_15" type="text" id="subtotal_15"  size="10" class="bgg"  maxlength="10" value='<s:property value="kpipoint.subtotal_15"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_15" type="text" id="coeff_15"  size="10" maxlength="10" class="bgg"  value='<s:property value="kpipoint.coeff_15"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_15" type="text" id="remark_15" size="44" maxlength="44" value='<s:property value="kpipoint.remark_15"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*2分，从研究院知识管理中心获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">17</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">分享文件数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_16" type="text" id="number_16"  size="10" maxlength="10"  onChange="numVali_16(this)" onBlur="numVali_16(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_16"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_16" type="text" id="subtotal_16" size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_16"/>' readonly="readonly">
    </div></td>
         <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_16" type="text" id="coeff_16"  size="10" class="bgg" maxlength="10"  onChange="numVali_16(this)" onBlur="numVali_16(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.coeff_16"/>' readonly="readonly">
    </div></td>
    <td bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_16" type="text" id="remark_16" size="44" maxlength="44" value='<s:property value="kpipoint.remark_16"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*2分，重复不算，含技术资料、竞争对手资料、银行客户资料等，通过主任和经理审核，不超过10分，从研究院知识管理中心获取，SVN归档</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">18</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">原创经验总结文档数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_17" type="text" id="number_17"  size="10" maxlength="10"  onChange="numVali_17(this)" onBlur="numVali_17(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_17"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_17" type="text" id="subtotal_17"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_17"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_17" type="text" id="coeff_17"  size="10" maxlength="10"  class="bgg" value='<s:property value="kpipoint.coeff_17"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_17" type="text" id="remark_17" size="44" maxlength="44" value='<s:property value="kpipoint.remark_17"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*3，通过审核，不超过10分，从研究院知识管理中心获取，SVN归档</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">19</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">分享案例数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_18" type="text" id="number_18"  size="10" maxlength="10"  onChange="numVali_18(this)" onBlur="numVali_18(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_18"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_18" type="text" id="subtotal_18"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_18"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_18" type="text" id="coeff_18"  size="10" maxlength="10"  class="bgg" value='<s:property value="kpipoint.coeff_18"/>' readonly="readonly">
    </div></td>
    <td bgcolor="#FFFFFF"><input name="kpipoint.remark_18" type="text" id="remark_18" size="44" maxlength="44" value='<s:property value="kpipoint.remark_18"/>'></td>
    <td align="left" bgcolor="#FFFFFF">次数*3分，要有案例分析和改进措施，通过审核，不超过10分，从部门管理平台获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">20</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">在平台被表扬/赞赏次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_19" type="text" id="number_19" class="bgg" size="10" maxlength="10"  value='<s:property value="kpipoint.number_19"/>' readonly="readonly"></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_19" type="text" id="subtotal_19"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_19"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
            <input name="kpipoint.coeff_19" type="text" id="coeff_19"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_19"/>'  readonly="readonly">
          </div></td>
    <td bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_19" type="text" id="remark_19" size="44" maxlength="44" value='<s:property value="kpipoint.remark_19"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每次部门同事互相赞赏或表扬0.5分，主任或经理表扬2分，从部门管理平台获取</td>
  </tr>
  
   <tr>
    <td bgcolor="#FFFFFF"><div align="center">21</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">跨部门表扬</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_33" type="text" id="number_33"  size="10" maxlength="10"  onChange="numVali_33(this)" onBlur="numVali_33(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_33"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_33" type="text" id="subtotal_33"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_33"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
            <input name="kpipoint.coeff_33" type="text" id="coeff_33"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_33"/>'  readonly="readonly">
          </div></td>
    <td bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_33" type="text" id="remark_33" size="44" maxlength="44" value='<s:property value="kpipoint.remark_33"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*3分（要有表扬信或邮件，口头不算）</td>
  </tr>
  
   <tr>
    <td bgcolor="#FFFFFF"><div align="center">22</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">客户表扬次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_34" type="text" id="number_34"  size="10" maxlength="10"  onChange="numVali_34(this)" onBlur="numVali_34(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_34"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_34" type="text" id="subtotal_34"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_34"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
            <input name="kpipoint.coeff_34" type="text" id="coeff_34"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_34"/>'  readonly="readonly">
          </div></td>
    <td bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_34" type="text" id="remark_34" size="44" maxlength="44" value='<s:property value="kpipoint.remark_34"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*5分（要有表扬信或邮件，口头不算）</td>
  </tr>
  
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">23</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">组织部门活动次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_20" type="text" id="number_20"  size="10" maxlength="10"  onChange="numVali_20(this)" onBlur="numVali_20(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_20"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_20" type="text" id="subtotal_20"  size="10" class="bgg" maxlength="10"   value='<s:property value="kpipoint.subtotal_20"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_20" type="text" id="coeff_20"  size="10" maxlength="10"  class="bgg" value='<s:property value="kpipoint.coeff_20"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_20" type="text" id="remark_20" size="44" maxlength="44" value='<s:property value="kpipoint.remark_20"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">3分/次</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">24</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">出差天数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_21" type="text" id="number_21"  size="10" maxlength="10" onChange="numVali_21(this)" onBlur="numVali_21(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_21"/>' ></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_21" type="text" id="subtotal_21"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_21"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_21" type="text" id="coeff_21"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_21"/>'  readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_21" type="text" id="remark_21" size="44" maxlength="44" value='<s:property value="kpipoint.remark_21"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">出差天数/10，从部门管理平台获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">25</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">发布论文数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_22" type="text" id="number_22"  size="10" maxlength="10"  onChange="numVali_22(this)" onBlur="numVali_22(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_22"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_22" type="text" id="subtotal_22"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_22"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_22" type="text" id="coeff_22"  size="10" maxlength="10" class="bgg"  value='<s:property value="kpipoint.coeff_22"/>' readonly="readonly">
    </div></td>
    <td bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_22" type="text" id="remark_22" size="44" maxlength="44" value='<s:property value="kpipoint.remark_22"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*10分，在外部刊物上刊登</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">26</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">提交专利数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_23" type="text" id="number_23"  size="10" maxlength="10"  onChange="numVali_23(this)" onBlur="numVali_23(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_23"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.subtotal_23" type="text" id="subtotal_23" size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_23"/>' readonly="readonly">
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_23" type="text" id="coeff_23"  size="10" maxlength="10"  class="bgg" value='<s:property value="kpipoint.coeff_23"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_23" type="text" id="remark_23" size="44" maxlength="44" value='<s:property value="kpipoint.remark_23"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">每篇*10分，需通过专利部审核</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">27</div></td>
    <td width="7%" rowspan="9"  bgcolor="#FFFFFF"><div align="center">扣分项</div>
        <div align="center"></div></td>
    <td   bgcolor="#FFFFFF"><div align="left">迟到/早退次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_24" type="text" id="number_24"  size="10" maxlength="10" onChange="numVali_24(this)" onBlur="numVali_24(this)" onFocus="numClear(this)"  format="NN"   value='<s:property value="kpipoint.number_24"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_24" type="text" id="subtotal_24"  size="10" class="bgg" maxlength="10"   value='<s:property value="kpipoint.subtotal_24"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_24" type="text" id="coeff_24" size="10" maxlength="10" class="bgg"  value='<s:property value="kpipoint.coeff_24"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_24" type="text" id="remark_24" size="44" maxlength="44" value='<s:property value="kpipoint.remark_24"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*3分（>20分钟），事先通知和请假的除外</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">28</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">未按时提交周报次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_25" type="text" id="number_25"  size="10" maxlength="10"  onChange="numVali_25(this)" onBlur="numVali_25(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_25"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_25" type="text" id="subtotal_25"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_25"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_25" type="text" id="coeff_25"  size="10" maxlength="10"  class="bgg"  value='<s:property value="kpipoint.coeff_25"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_25" type="text" id="remark_25" size="44" maxlength="44" value='<s:property value="kpipoint.remark_25"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*1分，从邮件获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">29</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">未按时提交日报次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_26" type="text" id="number_26"  size="10" maxlength="10" onChange="numVali_26(this)" onBlur="numVali_26(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_26"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_26" type="text" id="subtotal_26"size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_26"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_26" type="text" id="coeff_26"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_26"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_26" type="text" id="remark_26" size="44" maxlength="44" value='<s:property value="kpipoint.remark_26"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*0.5分，从考勤记录和日报获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">30</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">旷工次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_27" type="text" id="number_27"  size="10" maxlength="10"  onChange="numVali_27(this)" onBlur="numVali_27(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_27"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_27" type="text" id="subtotal_27"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_27"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_27" type="text" id="coeff_27" size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_27"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_27" type="text" id="remark_27" size="44" maxlength="44" value='<s:property value="kpipoint.remark_27"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*10分，事先通知和请假的除外</td>
  </tr>
  
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">31</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">培训缺席次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_32" type="text" id="number_32"  size="10" maxlength="10"  onChange="numVali_32(this)" onBlur="numVali_32(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_32"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_32" type="text" id="subtotal_32"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_32"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_32" type="text" id="coeff_32" size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_32"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_32" type="text" id="remark_32" size="44" maxlength="44" value='<s:property value="kpipoint.remark_32"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*1分，事先通知和请假的除外</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">32</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">被通报批评次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_28" type="text" id="number_28"  size="10" maxlength="10"  onChange="numVali_28(this)" onBlur="numVali_28(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="kpipoint.number_28"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_28" type="text" id="subtotal_28"  size="10" class="bgg" maxlength="10"  value='<s:property value="kpipoint.subtotal_28"/>' readonly="readonly"></font>
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_28" type="text" id="coeff_28"  size="10" maxlength="10" class="bgg"  value='<s:property value="kpipoint.coeff_28"/>' readonly="readonly">
    </div></td>
    <td   bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_28" type="text" id="remark_28" size="44" maxlength="44" value='<s:property value="kpipoint.remark_28"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*5分，从OA或邮件获取</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF"><div align="center">33</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">被部门投诉次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_29" type="text" id="number_29"  size="10" maxlength="10" onChange="numVali_29(this)" onBlur="numVali_29(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_29"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_29" type="text" id="subtotal_29" size="10"  maxlength="10" class="bgg"  value='<s:property value="kpipoint.subtotal_29"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_29" type="text" id="coeff_29"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_29"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_29" type="text" id="remark_29" size="44" maxlength="44" value='<s:property value="kpipoint.remark_29"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*2分</td>
  </tr>
    <tr>
    <td bgcolor="#FFFFFF"><div align="center">34</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">被客户投诉次数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_30" type="text" id="number_30"  size="10" maxlength="10" onChange="numVali_30(this)" onBlur="numVali_30(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="kpipoint.number_30"/>'></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_30" type="text" id="subtotal_30" size="10"  maxlength="10" class="bgg"  value='<s:property value="kpipoint.subtotal_30"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_30" type="text" id="coeff_30"  size="10" maxlength="10" class="bgg" value='<s:property value="kpipoint.coeff_30"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_30" type="text" id="remark_30" size="44" maxlength="44" value='<s:property value="kpipoint.remark_30"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">次数*5分</td>
  </tr>
     <tr>
    <td bgcolor="#FFFFFF"><div align="center">35</div></td>
    <td   bgcolor="#FFFFFF"><div align="left">平台问题建议未处理天数</div></td>
    <td  bgcolor="#FFFFFF"><input name="kpipoint.number_35" type="text" id="number_35"  size="10" maxlength="10" class="bgg"  value='<s:property value="kpipoint.number_35"/>' readonly="readonly"></td>
    <td  bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">
      <input name="kpipoint.subtotal_35" type="text" id="subtotal_35" size="10"  maxlength="10" class="bgg" value='<s:property value="kpipoint.subtotal_35"/>' readonly="readonly"></font>
    </div></td>
          <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.coeff_35" type="text" id="coeff_35" class="bgg" size="10" maxlength="10"  value='<s:property value="kpipoint.coeff_35"/>' readonly="readonly">
    </div></td>
    <td  bgcolor="#FFFFFF"><div align="center">
      <input name="kpipoint.remark_35" type="text" id="remark_35" size="44" maxlength="44" value='<s:property value="kpipoint.remark_35"/>'>
    </div></td>
    <td align="left" bgcolor="#FFFFFF">天数/10*2分</td>
  </tr>
              <tr>
    <td bgcolor="#FFFFFF"><div align="center">备注</div></td>
   
    <td colspan="7"  bgcolor="#FFFFFF"><div align="left">
      <input name="kpipoint.remark" type="text" class="bgg" id="remark" size="172" maxlength="200" value='<s:property value="kpipoint.remark"/>' readonly="readonly">
    </div>

			</td>
 		 </tr>
  <tr>
    <td bgcolor="#CCFFFF"><div align="center">总分</div></td>
    <td colspan="3"  bgcolor="#CCFFFF"><div align="left"></div>
        <div align="center"></div>
      <div align="center"></div></td>
 
    <td  bgcolor="#CCFFFF"><div align="center">
<input name="kpipoint.subtotal_s" type="text" id="subtotal_s" class="bgg" size="10" maxlength="10" value='<s:property value="kpipoint.subtotal_s"/>' readonly="readonly"></div></td>
    <td colspan="3"  bgcolor="#CCFFFF"><div align="center">
    </div>
    <script type="text/javascript">
							if(document.getElementById("firstedit").value == "初次修改")
								{
										if(document.getElementById("UserPrice_score").value == "")
										{
											document.getElementById("remark_5").value = "本月所提出问题数为0条，价值分为：0";
											document.getElementById("subtotal_5").value = "0.0";
											document.getElementById("number_5").value = "0.0";
										}
										else
										{
											document.getElementById("remark_5").value = "本月所提出问题数为"+document.getElementById("UserPrice_num").value+"条,价值分为：" + document.getElementById("UserPrice_score").value;
											document.getElementById("subtotal_5").value = parseFloat(document.getElementById("UserPrice_score").value)/3;
											document.getElementById("subtotal_5").value = document.getElementById("subtotal_5").value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
											document.getElementById("number_5").value = document.getElementById("UserPrice_num").value;
										}
										if(document.getElementById("UserEffect_score").value == "")
										{
											document.getElementById("remark_6").value = "本月所处理问题数为0条，效果分为：0";
											document.getElementById("subtotal_6").value = "0.0";
											document.getElementById("number_6").value = "0.0";
										}
										else
										{
											document.getElementById("remark_6").value = "本月所处理问题为"+document.getElementById("UserEffect_num").value+"条，效果分为：" + document.getElementById("UserEffect_score").value;
											document.getElementById("subtotal_6").value = parseFloat(document.getElementById("UserEffect_score").value)/3;
											document.getElementById("subtotal_6").value = document.getElementById("subtotal_6").value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
											document.getElementById("number_6").value = document.getElementById("UserEffect_num").value;
										}
										
										if(document.getElementById("UserheadmanAddValue").value != "" && document.getElementById("UserfellowValue").value != "")
								        {
										   document.getElementById("number_19").value = parseFloat(document.getElementById("UserfellowValue").value) + parseFloat(document.getElementById("UserheadmanAddValue").value);
										   document.getElementById("subtotal_19").value = (parseFloat(document.getElementById("UserfellowValue").value) *0.5) + (parseFloat(document.getElementById("UserheadmanAddValue").value)*2);		
										   document.getElementById("remark_19").value = "本月主任或经理赞赏次数为：" + document.getElementById("UserheadmanAddValue").value + "，同事赞赏次数为:" +	document.getElementById("UserfellowValue").value;				
										}
										else if(document.getElementById("UserfellowValue").value != "" && document.getElementById("UserheadmanAddValue").value == "")
								        {
										   document.getElementById("number_19").value = parseFloat(document.getElementById("UserfellowValue").value);
										   document.getElementById("subtotal_19").value = parseFloat(document.getElementById("UserfellowValue").value)*0.5;									   document.getElementById("remark_19").value = "本月同事赞赏次数为:" +	document.getElementById("UserfellowValue").value;
										}
										else if(document.getElementById("UserfellowValue").value == "" && document.getElementById("UserheadmanAddValue").value != "")
								        {
										   document.getElementById("number_19").value = parseFloat(document.getElementById("UserheadmanAddValue").value);
										   document.getElementById("subtotal_19").value = parseFloat(document.getElementById("UserheadmanAddValue").value)*2;									
										   document.getElementById("remark_19").value = "本月主任或经理赞赏次数为：" + document.getElementById("UserheadmanAddValue").value;
										}
										if(document.getElementById("SuggestionRemark").value!="")
										{
											document.getElementById("number_35").value = parseFloat(document.getElementById("SuggestionDay").value);
											document.getElementById("subtotal_35").value = (parseFloat(document.getElementById("SuggestionDay").value)/10)*2;
											document.getElementById("subtotal_35").value = document.getElementById("subtotal_35").value.replace(/(\.\d)\d+/ig,"$1");
											document.getElementById("remark_35").value = document.getElementById("SuggestionRemark").value;
										}
										document.getElementById("remark").value = "提交BUG总值:"+document.getElementById("Userbugtotal").value + "，修改BUG总值:"+document.getElementById("UserModifybugtotal").value + "，执行用例数:"+document.getElementById("UserCaseExec").value + "，新增和修改用例数:" + document.getElementById("UserCaseNewModify").value;
										document.getElementById("remark_3").value = "提交BUG总值:"+document.getElementById("Userbugtotal").value;
										document.getElementById("remark_4").value = "修改BUG总值:"+document.getElementById("UserModifybugtotal").value;
										document.getElementById("remark_8").value = "执行用例数:"+document.getElementById("UserCaseExec").value;
										document.getElementById("remark_7").value = "新增和修改用例数:"+document.getElementById("UserCaseNewModify").value;
										document.getElementById("remark_31").value = "验证缺陷总值:"+document.getElementById("UserbugYanZhengtotal").value;
								}
								numVali();
									</script>			</td>
 		 </tr>
        </table>		
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
