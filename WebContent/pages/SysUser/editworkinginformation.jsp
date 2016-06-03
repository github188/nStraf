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
     <body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init()">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/SysUser/SysUserinfo!updateworkinginformaton.action"   method="post">
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
                  
					  <tr bgcolor="#FFFFFF">
					  <td width="5%"  rowspan="3"  align="center" bgcolor="#999999"><div align="center">在职信息<font color="#FF0000">
					  </font></div> </td>
					  <td width="7%"  bgcolor="#999999">入职日期</td>
					  <td width="12%" ><input name="sysuser.hiredate" type="text" id="hiredate"  class="MyInput" value='<s:date name="sysuser.hiredate" format="yyyy-MM-dd"/>'></td>
					  <td width="4%"  bgcolor="#999999">岗位</td>
					  <td width="12%" ><input name="sysuser.position" type="text" id="position"  class="MyInput" value='<s:property value="sysuser.position"/>'></td>
					  <td width="4%"  bgcolor="#999999">组别</td>
					  <td width="10%" ><select name="sysuser.groupName"  id="groupName" >		
                <option value='质量管理组'>质量管理组</option>
              <option value='基础软件测试组'>基础软件测试组</option>
              <option value='应用软件测试组'>应用软件测试组</option>
              <option value='技术支持组'>技术支持组</option>
              </select></td>
			    <input type='hidden' id="st" name="st" value="<s:property value='sysuser.groupName'/>"/>
                                  <s:if test="sysuser.groupName!=null&&!sysuser.groupName.equals('')">
											<script language="javascript">
												var status = document.getElementById("st").value;
						            		    status = decodeURI(status);
												document.getElementsByName("groupName")[0].value = status;
						            		</script>
									</s:if>
					   <td width="5%" bgcolor="#999999"> 手机号码</td>
					  <td width="8%" ><input name="sysuser.mobile"  size="14" "type="text" id="mobile"  class="MyInput" value='<s:property value="sysuser.mobile"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999">状态</td>
					  <td><select name="sysuser.status"  id="status"  value='<s:property value="sysuser.status"/>'>		
                      <option value='在职'>在职</option>
                      <option value='离职'>离职</option>
                      </select></td>
					  <td bgcolor="#999999">离职日期</td>
					  <td><input name="sysuser.leavedate" type="text" id="leavedate"  class="MyInput" 
					  value='<s:date name="sysuser.leavedate" format="yyyy-MM-dd"/>'></td>
					  <td bgcolor="#999999">离职原因</td>
					  <td  colspan="3"><textarea  cols="34" name="sysuser.leavereason" type="text" id="leavereason" ><s:property value="sysuser.leavereason"/>
					  </textarea></td>
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999">备注</td>
					  <td  colspan="7">
					  <textarea cols="86"  name="sysuser.remark" id="remark" ><s:property value="sysuser.remark"/></textarea>
					 </td>
					  </tr>
					  <input name="stat" type="hidden" id="stat"  value='<s:property value="sysuser.groupname"/>' />
                <input name="stat1" type="hidden" id="stat1"  value='<s:property value="sysuser.status"/>' />
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
	
	//	var szGroupname = document.getElementsByName("stat")[0].value;
	//	szGroupname = decodeURI(szGroupname);
	//	document.getElementsByName("groupname")[0].value = (szGroupname.trim() == "" ? "白盒测试组" : szGroupname.trim());
		var szStatus = document.getElementsByName("stat1")[0].value;
		szStatus = decodeURI(szStatus);
		document.getElementsByName("status")[0].value = (szStatus.trim() == "" ? "在职" : szStatus.trim());
		
</script>
</body>  
</html> 