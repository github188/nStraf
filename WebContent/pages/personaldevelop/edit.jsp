<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var oldBorderStyle, oldBgColor;
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
            
			if(document.getElementById("createyear").value.trim()=="")
			{
				alert("请输入年份");
				return;
			}
			if(document.getElementById("name").value.trim()=="")
			{
				alert("请输入姓名");
				return;
			}
			if(document.getElementById("groupname").value.trim()=="")
			{
				alert("请输入组别");
				return;
			}
			if(document.getElementById("analyse").value.trim()=="")
			{
				alert("请输入自我优势分析");
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}

		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
			function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^(http|ftp|svn):\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
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
		   var ip=/^(((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))$/;
		//var ip = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)((d|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
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
			case "ip":
				 flag = ip.test(itemNameValue);
				  break;
			case "ShareUrl":
				flag = ShareUrl.test(itemNameValue);	  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag;
		
		}
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}

		  function init()
	      {
	      	var id=$("#id").val();
	      	if(""!=id&&null!=id)
	      	{   
	      		parent.document.getElementById("id").value=id;
	      		if(parent.showLi)
	      			parent.showLi();
	      	}
	      	return false;
	      }   
	      
	       function selDate(id)
	      {
	      	var obj=document.getElementById(id);
	      	ShowDate(obj);
	      }  

	       
			
			
			function validateInputInfo1(){
				var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			  	var thisDate = reportInfoForm.BrowerDate.value.trim();
					if(thisDate.length>0){
					var a = re1.test(thisDate);
					if(!a){
						alert('日期格式不正确,请使用日期选择!');
						return false;
						}
					 }
					  return true;
				 }		
			function validateInputInfo2(){
				var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			  	var thisDate = reportInfoForm.returnDate.value.trim();
					if(thisDate.length>0){
					var a = re1.test(thisDate);
					if(!a){
						alert('日期格式不正确,请使用日期选择!');
						return false;
						}
					 }
					  return true;
				 }		
        function startMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("abilityanalyse.createyear").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
	
					var str = document.getElementById("abilityanalyse.createyear").value.split("-");
						document.getElementById("abilityanalyse.createyear").value = parseInt(str[0],10);

			
				checkMonthStartFlag = true;
				return true;
		}
		function sChangeheadmanupditing(obj){
        
  		if(document.getElementById("level").value != "1"){
 			obj.value = document.getElementById("headmanupditingtTmp").value;
		}  
		else
		{
			if(document.getElementById("status").value != "待上级审核")
			{
				obj.value = document.getElementById("headmanupditingtTmp").value;
			}
		}
   }
   
   		function sChangemanageupauditing(obj){

  		if(document.getElementById("level").value != "0"){
 			obj.value = document.getElementById("manageupauditingTmp").value;
		}  
		else
		{
			if(document.getElementById("status").value != "待上上级审核")
			{
				obj.value = document.getElementById("manageupauditingTmp").value;
			}
		}
		
   }
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10" onLoad="init();">
		<form name="reportInfoForm" method="post" action="<%=request.getContextPath() %>/pages/personaldevelop/personaldevelopinfo!update.action">
					<input type="hidden" id="id" name="id"
				value='<s:property value="id"/>'>
        <input type="hidden" id="log" name="abilitylog.log"
				value='<s:property value="abilitylog.log"/>'>
                        <input type="hidden" id="level" name="level"
				value='<s:property value="level"/>'>
                        <input type="hidden" id="editwirte" name="editwirte"
				value='<s:property value="editwirte"/>'>
                <input type="hidden" id="headmanupditingtTmp" name="headmanupditingtTmp"
				value='<s:property value="abilityanalyse.headmanupditing"/>'>
                <input type="hidden" id="manageupauditingTmp" name="manageupauditingTmp"
				value='<s:property value="abilityanalyse.manageupauditing"/>'>
			<table width="80%" align="center" cellPadding="0" cellSpacing="0">
				<tr>                                                  
					<td>
    <fieldset class="jui_fieldset" width="100%">                       
							<legend>
								<s:text name="能力状况分析" />
							</legend>
							<table width="570" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" height="153">
						    <br />
                  <tr>
                <td align="center" width="15%" bgcolor="#999999"><div align="center"> 年份<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.createyear" type="text" size="15" maxlength="40"
											id="createyear" value='<s:property value="abilityanalyse.createyear"/>' ></td>
                <td width="20%" align="center" bgcolor="#999999"><div align="center"> 姓名<font color="#FF0000">*</font></div></td>
                <td   bgcolor="#FFFFFF"><input name="abilityanalyse.name" type="text" class="bgg" size="15" maxlength="20"  id="name" value='<s:property value="abilityanalyse.name"/>' readonly="readonly"></td>
                <td width="15%" align="center" bgcolor="#999999"><div align="center"> 组别<font color="#FF0000">*</font> </div></td>
                <td   bgcolor="#FFFFFF"><input name="abilityanalyse.groupname" type="text" class="bgg" id="groupname" value='<s:property value="abilityanalyse.groupname"/>' size="15"  maxlength="20" readonly="readonly"></td>
              </tr>
							  <tr>
									<td align="center" width="20%" bgcolor="#999999">
						  <div align="center" style="width: 100px">
											自我优势分析
											<font color="#FF0000">*</font></div></td>
							  <td colspan="5" width="80%" bgcolor="#FFFFFF" ><textarea cols="75" rows="11" name="abilityanalyse.analyse" value='<s:property value="abilityanalyse.analyse"/>' id="analyse" ><s:property value="abilityanalyse.analyse"/></textarea></td>
							  </tr>
                              <tr>
                                <td align="center" width="15%" bgcolor="#999999"><div align="center"> 上级审核状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><s:select list="{'未审核','审核通过','审核不通过'}" name="abilityanalyse.headmanupditing"  theme="simple"   cssStyle="width:111px;" id="headmanupditing" onChange="sChangeheadmanupditing(this)"></s:select></td>
                              <td align="center" width="15%" bgcolor="#999999"><div align="center"> 上上级审核状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><s:select list="{'未审核','审核通过','审核不通过'}" name="abilityanalyse.manageupauditing" theme="simple"   cssStyle="width:111px;" id="manageupauditing" onChange="sChangemanageupauditing(this)"></s:select></td>
					    
                             <td align="center" width="15%" bgcolor="#999999"><div align="center"> 总状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.status" type="text" size="15" maxlength="40" class="bgg"
											id="status" value='<s:property value="abilityanalyse.status"/>' readonly="readonly"></td>
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
                    <tr>  
       <td>
             <fieldset width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
				<table width="100%" >
                      <tr>
                        <td><s:text name="label.admin.content"/></td>

                        <!--<td width="6%"><input type="button" value="全选" onClick="selAgain()"/></td>-->
                      </tr>
                  </table>
              </fieldset>
      </td>  
  </tr>
					<td align="center">
						<input type="button" name="ok" id="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onclick="save()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
                   <script language="javascript">
  							
								if(document.getElementById("level").value == "1"){
								        if(document.getElementById("status").value != "待上级审核")
										{
											document.getElementById("headmanupditing").style.backgroundColor='#999999';
											document.getElementById("ok").disabled = true;
										}
										if(document.getElementById("status").value == "未经审核通过" && document.getElementById("editwirte").value == document.getElementById("name").value)
										{
											document.getElementById("ok").disabled = false;
										}
 										document.getElementById("manageupauditing").style.backgroundColor='#999999';
								} 
								if(document.getElementById("level").value == "2"){
 										document.getElementById("headmanupditing").style.backgroundColor='#999999';
										document.getElementById("manageupauditing").style.backgroundColor='#999999';
										if(document.getElementById("status").value != "未经审核通过")
										{
											document.getElementById("ok").disabled = true;
										}
								}  
								if(document.getElementById("status").value != "未经审核通过")
								{							
										document.getElementById("createyear").readOnly = true;
										document.getElementById("analyse").readOnly = true;
										
								}
								if(document.getElementById("level").value == "0"){
								        if(document.getElementById("status").value != "待上上级审核")
										{
											document.getElementById("manageupauditing").style.backgroundColor='#999999';
										}
 										document.getElementById("headmanupditing").style.backgroundColor='#999999';
										document.getElementById("createyear").readOnly = false;
										document.getElementById("analyse").readOnly = false;
								}
						         </script>
				</tr>
			</table>

		</form>

	</body>
</html>
