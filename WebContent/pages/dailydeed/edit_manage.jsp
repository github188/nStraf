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
			var ret = 0, ret1 = 0,ret3 = 0, tmp = 0;

			document.getElementById("dailydeed.subtotal_m").value = 0;
			document.getElementById("dailydeed.subtotal_s").value = 0;
			ret1 = (parseFloat(document.getElementById("dailydeed.subtotal_m_1").value)*40/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_2").value)*50/10+ parseFloat(document.getElementById("dailydeed.subtotal_m_3").value)*40/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_4").value)*30/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_5").value)*30/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_6").value)*30/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_7").value)*20/10 + parseFloat(document.getElementById("dailydeed.subtotal_m_8").value))*10/10;
			document.getElementById("dailydeed.subtotal_m").value = ret1.toFixed(1);	
			document.getElementById("dailydeed.subtotal_m").value =document.getElementById("dailydeed.subtotal_m").value.replace(/(\.\d)\d+/ig,"$1");
			
			ret3 = (parseFloat(document.getElementById("dailydeed.subtotal_s_1").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_2").value)+ parseFloat(document.getElementById("dailydeed.subtotal_s_3").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_4").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_5").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_6").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_7").value) + parseFloat(document.getElementById("dailydeed.subtotal_s_8").value))*10/10;
			document.getElementById("dailydeed.subtotal_s").value = ret3.toFixed(1);	
			document.getElementById("dailydeed.subtotal_s").value =document.getElementById("dailydeed.subtotal_s").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_1").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_1").value)*4/10)*40/10;
			
			 document.getElementById("dailydeed.subtotal_s_1").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_1").value = document.getElementById("dailydeed.subtotal_s_1").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_2").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_2").value)*4/10)*50/10;
			
			 document.getElementById("dailydeed.subtotal_s_2").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_2").value = document.getElementById("dailydeed.subtotal_s_2").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_3").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_3").value)*4/10)*40/10;

			 document.getElementById("dailydeed.subtotal_s_3").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_3").value = document.getElementById("dailydeed.subtotal_s_3").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (document.getElementById("dailydeed.subtotal_d_4").value*6/10+document.getElementById("dailydeed.subtotal_m_4").value*4/10)*3;
			//alert(ret.toFixed(1));
			 document.getElementById("dailydeed.subtotal_s_4").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_4").value = document.getElementById("dailydeed.subtotal_s_4").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_5").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_5").value)*4/10)*30/10;
			
			 document.getElementById("dailydeed.subtotal_s_5").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_5").value = document.getElementById("dailydeed.subtotal_s_5").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_6").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_6").value)*4/10)*30/10;
			
			 document.getElementById("dailydeed.subtotal_s_6").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_6").value = document.getElementById("dailydeed.subtotal_s_6").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_7").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_7").value)*4/10)*20/10;
			
			 document.getElementById("dailydeed.subtotal_s_7").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_7").value = document.getElementById("dailydeed.subtotal_s_7").value.replace(/(\.\d)\d+/ig,"$1");
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
			ret = (parseFloat(document.getElementById("dailydeed.subtotal_d_8").value)*6/10+parseFloat(document.getElementById("dailydeed.subtotal_m_8").value)*4/10)*10/10;
			
			 document.getElementById("dailydeed.subtotal_s_8").value = ret.toFixed(1);
			 document.getElementById("dailydeed.subtotal_s_8").value = document.getElementById("dailydeed.subtotal_s_8").value.replace(/(\.\d)\d+/ig,"$1");
			numVali();
		}
	
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/dailydeed/dailydeedinfo!update.action"
			method="post">
           
              <input type="hidden" name="dailydeed.id" value='<s:property value="dailydeed.id"/>'>
               <table width="99%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
			    <fieldset class="jui_fieldset" width="99%">
							<legend>
								测试人员日常行为评价表
							</legend>
        <table width="98%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
        <br/>
                <td align="center" width="10%" bgcolor="#999999"><div align="center">月份</div></td>
                <td colspan="3" bgcolor="#FFFFFF"><input name="month_date" type="text" id="month_date" value='<s:property value="dailydeed.month_date"/>' style="border:0" size="12" readonly="readonly"></td>
                <td width="10%" align="center" bgcolor="#999999"><div align="center"> 考核人 </div></td>
                <td colspan="5"  bgcolor="#FFFFFF"><input name="user_id" type="text"  id="user_id" value='<s:property value="dailydeed.user_id"/>'  style="border:0" size="12"  maxlength="20" readonly></td>
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
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">工作纪律</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）经常违反公司或部门制度，被指正时态度傲慢；</div> 
				<div align="left">（2分）纪律观念不强，偶尔违反公司或部门制度；</div> 
				<div align="left">（3分）偶有迟到，但上班后工作兢兢业业；</div> 
				<div align="left">（4分）能遵守公司规章制度，但需要有人督导；</div> 
				<div align="left">（5分）自觉遵守和维护公司各项规章制度。</div></td>
                <td bgcolor="#FFFFFF"><div align="center">4</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                  <input name="dailydeed.subtotal_z_1" type="text" id="subtotal_z_1"  size="8" maxlength="11" class="bgg"  value='<s:property value="dailydeed.subtotal_z_1"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center">
                           <textarea cols="16" rows="12" name="dailydeed.remark_z_1" class="bgg"
												id="remark_z_1" readonly><s:property value="dailydeed.remark_z_1"/></textarea>
                         </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                  <input name="dailydeed.subtotal_d_1" type="text" id="subtotal_d_1"  size="8" maxlength="10" class="bgg"  value='<s:property value="dailydeed.subtotal_d_1"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="12" class="bgg" name="dailydeed.remark_d_1"
												id="remark_d_1" readonly><s:property value="dailydeed.remark_d_1"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_1" type="text" id="subtotal_m_1"  size="8" maxlength="10" onChange="numVali_1(this)" onBlur="numVali_1(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_1"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_1" type="text" id="subtotal_s_1" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_1"/>' readonly="readonly">
                      </div></td>
              </tr>    
                 <tr>
              <td width="6%" bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">工作态度</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）工作态度较差，经常在底下抱怨；</div>  
					 <div align="left">（1分）工作态度一般，有时不服从领导安排或领导安排的任务未按时执行；</div>  
					 <div align="left">（2分）工作态度一般，需领导督查下才能完成；</div>  
					 <div align="left">（3分）工作态度良好，能服从领导工作安排；</div>  
					 <div align="left">（4分）工作态度积极，根据项目需要主动加班；</div>  
					 <div align="left">（5分）工作态度积极，且能带动团队气氛，影响到大家。</div>            
                    </td>
                <td bgcolor="#FFFFFF"><div align="center">5</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_2" type="text" id="subtotal_z_2"  size="8" maxlength="10" class="bgg" value='<s:property value="dailydeed.subtotal_z_2"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="13" class="bgg" name="dailydeed.remark_z_2"
												id="remark_z_2" readonly><s:property value="dailydeed.remark_z_2"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_2" type="text" id="subtotal_d_2"  size="8" maxlength="10" class="bgg" value='<s:property value="dailydeed.subtotal_d_2"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="13" class="bgg"  name="dailydeed.remark_d_2"
												id="remark_d_2" readonly><s:property value="dailydeed.remark_d_2"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_2" type="text" id="subtotal_m_2" size="8" maxlength="10"  onChange="numVali_2(this)" onBlur="numVali_2(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_2"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_2" type="text" id="subtotal_s_2" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_2"/>' readonly="readonly">
                      </div></td>
              </tr> 
              
              <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">流程执行度</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）抵触流程的执行，不按照流程执行工作；</div>
				<div align="left">（2分）偶尔按照流程执行工作，对流程执行不配合；</div>
				<div align="left">（3分）按照流程执行工作，不关心开发人员是否执行；</div>
				<div align="left">（4分）积极主动按照流程执行工作，并适时的对开发人员进行提醒；</div>
				<div align="left">（5分）严格按照流程执行工作，并推动开发人员遵守流程。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">4</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                  <input name="dailydeed.subtotal_z_3" type="text" id="subtotal_z_3"  size="8" maxlength="10" class="bgg" value='<s:property value="dailydeed.subtotal_z_3"/>' readonly="readonly">
                </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center">
                           <textarea cols="16" rows="12" class="bgg" name="dailydeed.remark_z_3"
												id="dailydeed.remark_z_3" readonly><s:property value="dailydeed.remark_z_3"/></textarea>
                         </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_3" type="text" id="subtotal_d_3"  size="8" maxlength="10" class="bgg"  value='<s:property value="dailydeed.subtotal_d_3"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="12"  name="dailydeed.remark_d_3"
												id="dailydeed.remark_d_3" class="bgg" readonly><s:property value="dailydeed.remark_d_3"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_3" type="text" id="subtotal_m_3"  size="8" maxlength="10"  onChange="numVali_3(this)" onBlur="numVali_3(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_3"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_3" type="text" id="subtotal_s_3" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_3"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
                 <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">4</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">严谨认真</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）由于不严格不认真，导致工作出现了疏漏，且没有及时补救；</div>  
					<div align="left">（2分）工作出现了问题，但能够积极补救，不推卸责任；</div>  
					<div align="left">（3分）按本岗位工作要求做，没有出现工作疏漏；</div>  
					<div align="left">（4分）发现他人的工作疏漏，告知对方，并协助其补救；</div>  
					<div align="left">（5分）严格认真地履行岗位职责，发现隐患，并预先采取措施，避免问题的发生。</div>               </td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_4" type="text" id="subtotal_z_4"  size="8" maxlength="10" class="bgg"  value='<s:property value="dailydeed.subtotal_z_4"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="10" class="bgg" name="dailydeed.remark_z_4"
												id="dailydeed.remark_z_4" readonly><s:property value="dailydeed.remark_z_4"/></textarea>
                      </div></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_4" type="text" id="subtotal_d_4" size="8" maxlength="10" class="bgg"  value='<s:property value="dailydeed.subtotal_d_4"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><div align="center"><textarea cols="16" rows="10" class="bgg" name="dailydeed.remark_d_4"
												id="dailydeed.remark_d_4" readonly><s:property value="dailydeed.remark_d_4"/></textarea>
                      </div></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_4" type="text" id="subtotal_m_4"  size="8" maxlength="10"  onChange="numVali_4(this)" onBlur="numVali_4(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_4"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_4" type="text" id="subtotal_s_4" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_4"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">5</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">团队协作</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）不与团队成员沟通，完全按照个人设想来工作；</div> 
					<div align="left">（2分）告知团队成员自己的设想，但不响应团队成员提出的建议和要求，固执己见；</div> 
					<div align="left">（3分）能够认真听取团队成员的意见，修正个人的工作设想；</div> 
					<div align="left">（4分）当他与团队成员意见发生分歧的时候，不仅能够听取对方的意见，而且能够提出有价值的建议；</div> 
					<div align="left">（5分）在协助对方获得成功，并达成团队整体目标的同时，实现个人目标。</div>          </td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_5" type="text" id="subtotal_z_5"  size="8" maxlength="10" class="bgg" value='<s:property value="dailydeed.subtotal_z_5"/>'  readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg"  name="dailydeed.remark_z_5"
												id="dailydeed.remark_z_5"readonly><s:property value="dailydeed.remark_z_5"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_5" type="text" id="subtotal_d_5" size="8" maxlength="10"  class="bgg" value='<s:property value="dailydeed.subtotal_d_5"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg" name="dailydeed.remark_d_5"
							id="dailydeed.remark_d_5" readonly><s:property value="dailydeed.remark_d_5"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_5" type="text" id="subtotal_m_5"  size="8" maxlength="10" onChange="numVali_5(this)" onBlur="numVali_5(this)" onFocus="numClear(this)"  format="NN"  value='<s:property value="dailydeed.subtotal_m_5"/>' >
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_5" type="text" id="subtotal_s_5" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_5"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
               <tr>
                 <td width="6%" bgcolor="#FFFFFF"><div align="center">6</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">工作总结</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）工作中不善于总结，且出现相同的失误；</div> 
				<div align="left">（2分）能够不出现相同的失误，但不能防范于未燃，且未形成经验文档；</div> 
				<div align="left">（3分）工作中善于总结，且能形成文档，但未主动与大家分享；</div> 
				<div align="left">（4分）工作中主动思考和总结，并形成文档，主动在部门内部与大家分享和交流；</div> 
				<div align="left">（5分）将自己有价值的工作经验分享给整个组织，并为其他人员提供了帮助且受到好评。</div></td>
                <td bgcolor="#FFFFFF"><div align="center">3</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_6" type="text" id="subtotal_z_6" class="bgg"  size="8" maxlength="10"  value='<s:property value="dailydeed.subtotal_z_6"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="15" class="bgg"  name="dailydeed.remark_z_6"
												id="dailydeed.remark_z_6" readonly><s:property value="dailydeed.remark_z_6"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_6" type="text" id="subtotal_d_6"  size="8" maxlength="10" class="bgg"   value='<s:property value="dailydeed.subtotal_d_6"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="15"  name="dailydeed.remark_d_6" class="bgg" 
												id="dailydeed.remark_d_6" readonly><s:property value="dailydeed.remark_d_6"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_6" type="text" id="subtotal_m_6"  size="8" maxlength="10"  onChange="numVali_6(this)" onBlur="numVali_6(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_6"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_6" type="text" id="subtotal_s_6" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_6"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
   
               <tr>
                <td width="6%" bgcolor="#FFFFFF"><div align="center">7</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">学习意识</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）几乎不参加培训，平时也不怎么学习；</div>    
					<div align="left">（1分）偶尔参加培训，学习积极性不高；</div>    
					<div align="left">（2分）会参加各种培训，闲时也会看一点书；</div>    
					<div align="left">（3分）积极主动参加各种培训，并总结培训所学，空闲时参加一些考试；</div>    
					<div align="left">（4分）积极主动学习并参加各种培训，并将所学知识总结下来与大家一些分享交流；</div>    
					<div align="left">（5分）主动要求接触和学习新项目或新业务，并做出一些成绩。</div>              </td>
                <td bgcolor="#FFFFFF"><div align="center">2</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_7" type="text" id="subtotal_z_7" class="bgg" size="8" maxlength="10"  value='<s:property value="dailydeed.subtotal_z_7"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg" name="dailydeed.remark_z_7"
												id="dailydeed.remark_z_7" readonly><s:property value="dailydeed.remark_z_7"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_7" type="text" id="subtotal_d_7"  size="8" maxlength="10" class="bgg"  value='<s:property value="dailydeed.subtotal_d_7"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="10" class="bgg"  name="dailydeed.remark_d_7"
												id="dailydeed.remark_d_7" readonly><s:property value="dailydeed.remark_d_7"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_7" type="text" id="subtotal_m_7"  size="8" maxlength="10"  onChange="numVali_7(this)" onBlur="numVali_7(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_7"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_7" type="text" id="subtotal_s_7" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_7"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
               <tr>
               <td width="6%" bgcolor="#FFFFFF"><div align="center">8</div></td>
                <td width="7%"   bgcolor="#FFFFFF"><div align="center">整洁卫生</div>
                    <div align="center"></div></td>
                <td  bgcolor="#FFFFFF">
                  <div align="left">（0分）个人办公桌面脏乱，实验室和机房卫生较差；</div>  
				<div align="left">（2分）个人办公桌面比较整洁，实验室和机房卫生良好；</div>  
				<div align="left">（3分）个人办公桌面十分整洁，书籍资料摆放有条理，实验室和机房整理得井井有条；</div>  
				<div align="left">（4分）个人办公桌面不仅整洁，还督促其他同事注意整洁卫生；</div>  
				<div align="left">（5分）个人办公桌面不仅整洁，还协助其他同事进行整理，且协助部门对部门卫生进行监督。</div>           </td>
                <td bgcolor="#FFFFFF"><div align="center">1</div></td>
                <td bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_z_8" type="text" id="subtotal_z_8" class="bgg" size="8" maxlength="10" value='<s:property value="dailydeed.subtotal_z_8"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="8" class="bgg" name="dailydeed.remark_z_8"
												id="dailydeed.remark_z_8" readonly><s:property value="dailydeed.remark_z_8"/></textarea></td>
                <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_d_8" type="text" id="subtotal_d_8"  size="8" maxlength="10" class="bgg" value='<s:property value="dailydeed.subtotal_d_8"/>' readonly="readonly">
                      </div></td>
                         <td  bgcolor="#FFFFFF"><textarea cols="16" rows="8"  name="dailydeed.remark_d_8" class="bgg"
												id="dailydeed.remark_d_8" readonly><s:property value="dailydeed.remark_d_8"/></textarea></td>
                      <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m_8" type="text" id="subtotal_m_8" size="8" maxlength="10"  onChange="numVali_8(this)" onBlur="numVali_8(this)" onFocus="numClear(this)"  format="NN" value='<s:property value="dailydeed.subtotal_m_8"/>'>
                      </div></td>
                       <td  bgcolor="#FFFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s_8" type="text" id="subtotal_s_8" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s_8"/>' readonly="readonly">
                      </div></td>                  
              </tr> 
              
              
        <tr>
                <td bgcolor="#CCFFFF"><div align="center">合计</div></td>
          <td colspan="3"  bgcolor="#CCFFFF"><div align="left"></div>                  <div align="center"></div>                  <div align="center"></div></td>
   <td  bgcolor="#CCFFFF"><div align="center">
                     <input name="dailydeed.subtotal_z" type="text" id="subtotal_z" class="bgg" size="8" maxlength="10" value='<s:property value="dailydeed.subtotal_z"/>' format="NNN" readonly="readonly">
                   </div></td>
                   <td  bgcolor="#CCFFFF"></td>
   <td  bgcolor="#CCFFFF"><div align="center">
     <input name="dailydeed.subtotal_d" type="text" id="subtotal_d" class="bgg" size="8" maxlength="10" value='<s:property value="dailydeed.subtotal_d"/>' format="NNN" readonly="readonly">
   </div></td>
                   <td  bgcolor="#CCFFFF"></td>
<td  bgcolor="#CCFFFF"><div align="center">
                        <input name="dailydeed.subtotal_m" type="text" id="subtotal_m"  class="bgg" size="8" maxlength="10" value='<s:property value="dailydeed.subtotal_m"/>' readonly="readonly">
                      </div></td>
                      
                     
<td   bgcolor="#CCFFFF"><div align="center">
                        <input name="dailydeed.subtotal_s" type="text" id="subtotal_s" class="bgg" size="8" maxlength="12" value='<s:property value="dailydeed.subtotal_s"/>' readonly="readonly">
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
