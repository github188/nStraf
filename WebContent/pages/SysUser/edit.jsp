<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.domain.SysUser"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>certification query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" >
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
		 if(document.getElementById("username").value.trim()=="")
			{
				alert("请输入姓名");
				return;
			}	
			document.getElementById("ok").disabled = true;
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
			  	var thisDate = reportInfoForm.birthdate.value.trim();
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

	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init();">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/SysUser/SysUserinfo!update.action"  method="post">
	<input type="hidden" id="id2" name="id2" value='<s:property value="sysuser.id"/>' >
					<input type="hidden" id="id" name="sysuser.id"
				value='<s:property value="sysuser.id"/>'>
		 <table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="员工基本资料" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
					  <tr bgcolor="#FFFFFF" >
					  <td bgcolor="#999999" width="8%" rowspan="3" align="center" ><div align="center">基本信息<font color="#FF0000">
					  </font></div> </td>
					  <td bgcolor="#999999"  width="6%" >姓名</td>
					  <td width="14%" ><input name="sysuser.username"   disabled="true" type="text" id="username"  class="MyInput" value='<s:property value="sysuser.username"/>'>					  </td>
					  <td bgcolor="#999999"  width="6%" >性别</td>
					  <td width="14%" ><select name="sysuser.sex"  id="sex" >		
                      <option value='男'>男</option>
                      <option value='女'>女</option>
                      </select></td>
					  <td bgcolor="#999999"  width="6%" >出生日期</td>
					  <td  width="18%" ><input   name="sysuser.birthdate" type="text" id="birthdate"  class="MyInput" value='<s:date name="sysuser.birthdate" format="yyyy-MM-dd"/>'></td>
					  <td width="6%" bgcolor="#999999">民族</td>
					  <td width="14%" ><select name="sysuser.national"  id="national" >		
                      <option value='汉族'>汉族</option>
                      <option value='满族'>满族</option>
					  <option value='藏族'>藏族</option>
					  <option value='维吾尔族'>维吾尔族</option>
					  <option value='蒙古族'>蒙古族</option>
                      </select></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF" >

					  <td bgcolor="#999999">政治面貌</td>
					  <td ><select name="sysuser.politicalstatus"  id="politicalstatus"  >		
                      <option value='团员'>团员</option>
                      <option value='党员'>党员</option>
					  <option value='群众'>群众</option>
                      </select></td>
					  <td bgcolor="#999999">籍贯</td>
					  <td ><select name="sysuser.birthplace"  id="birthplace" >		
                      <option value='广东'>广东</option>
                      <option value='广西'>广西</option>
					  <option value='江西'>江西</option>
					  <option value='湖南'>湖南</option>
                      <option value='湖北'>湖北</option>
					  <option value='安徽'>安徽</option>
                      </select></td>
					  <td bgcolor="#999999">外语水平</td>
					  <td ><select name="sysuser.englishskill" id="englishskill" >		
                      <option value='一般'>一般</option>
                      <option value='CET4'>CET4</option>
					  <option value='CET6'>CET6</option>
					  <option value='CET8'>CET8</option>
                      </select></td>
					  <td bgcolor="#999999">技术职称</td>
					  <td><input name="sysuser.technicaltitle" type="text" id="technicaltitle"  class="MyInput" value='<s:property value="sysuser.technicaltitle"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF" >
					  <td bgcolor="#999999">学历</td>
					  <td ><select name="sysuser.education" tabindex="4" id="education" >		
                      <option value='初中'>初中</option>
                      <option value='高中/中专'>高中/中专</option>
					  <option value='大专'>大专</option>
					  <option value='本科'>本科</option>
					  <option value='研究生'>研究生</option>
					  <option value='硕士'>硕士</option>
                      </select></td>
					  <td bgcolor="#999999">毕业院校</td>
					  <td colspan="3"><input name="sysuser.graduateschool"  size="55" type="text" id="graduateschool"  class="MyInput" isSel="true" value='<s:property value="sysuser.graduateschool"/>'>                      </td>
                      
					  <td bgcolor="#999999">所学专业</td>
					  <td ><input name="sysuser.major" type="text" id="major"  class="MyInput"  value='<s:property value="sysuser.major"/>'></td>
				      </tr>
					   
					
				<input name="stat" type="hidden" id="stat"  value='<s:property value="sysuser.sex"/>' />
                <input name="stat1" type="hidden" id="stat1"  value='<s:property value="sysuser.national"/>' />
				<input name="stat2" type="hidden" id="stat2"  value='<s:property value="sysuser.politicalstatus"/>' />
				<input name="stat3" type="hidden" id="stat2"  value='<s:property value="sysuser.birthplace"/>' />
				<input name="stat4" type="hidden" id="stat2"  value='<s:property value="sysuser.englishskill"/>' />
				<input name="stat5" type="hidden" id="stat2"  value='<s:property value="sysuser.education"/>' />
    </table>
<br/>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" id="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		  </td> 
  		</tr>  
 	</table> 
			
 	</form>
	<script type="text/javascript">
	
		var szSex = document.getElementsByName("stat")[0].value;
		szSex = decodeURI(szSex);
		document.getElementsByName("sex")[0].value = (szSex.trim() == "" ? "男" : szSex.trim());
		var szNational = document.getElementsByName("stat1")[0].value;
		szNational = decodeURI(szNational);
		document.getElementsByName("national")[0].value = (szNational.trim() == "" ? "汉族" : szNational.trim());
		
		var szPoliticalstatus = document.getElementsByName("stat2")[0].value;
		szPoliticalstatus=decodeURI(szPoliticalstatus);
		document.getElementsByName("politicalstatus")[0].value = (szPoliticalstatus.trim() == "" ? "党员" : szPoliticalstatus.trim());
		
		var szBirthplace = document.getElementsByName("stat3")[0].value;
		szBirthplace=decodeURI(szBirthplace);
		document.getElementsByName("birthplace")[0].value = (szBirthplace.trim() == "" ? "广东" : szBirthplace.trim());
		var szEnglishskill = document.getElementsByName("stat4")[0].value;
		szEnglishskill=decodeURI(szEnglishskill);
		document.getElementsByName("englishskill")[0].value = (szEnglishskill.trim() == "" ? "一般" : szEnglishskill.trim());
		var szEducation = document.getElementsByName("stat5")[0].value;
		szEducation=decodeURI(szEducation);
		document.getElementsByName("education")[0].value = (szEducation.trim() == "" ? "本科" : szEducation.trim());
</script>
</body>  
</html> 