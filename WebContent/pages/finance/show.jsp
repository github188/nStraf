<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
		function closeModal(){
	 		
		 	window.close();
		}
		function save(){
			if(document.getElementById("activityDate").value.trim()=="")
			{
				alert("请输入日期");
				return;
			}
			if(document.getElementById("responsible").value.trim()=="")
			{
				alert("请输入经手人");
				return;
			}
			if(document.getElementById("activity").value.trim()=="")
			{
				alert("请输入项目/活动");
				return;
			}
			
			window.returnValue=true;
			reportInfoForm.submit();
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/finance/financeinfo!save.action"   method="post">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="财务管理" /></legend>
              <table width="84%" align="center" border="0" cellspacing="1" cellpadding="1" >
                    <br/>
                        <tr>
                          <td align="center" width="10%" bgcolor="#999999"><div align="center" >日期</div></td>
                          <td width="29%" bgcolor="#FFFFFF">
                          <input name="finance.activityDate" type="text" id="activityDate" size="22"  value='<s:date name="finance.activityDate" format="yyyy-MM-dd"/>'   readonly> </td>
                            
                          <td width="7%" align="center" bgcolor="#999999"><div align="center" >支出</div></td>
                          <td width="25%" bgcolor="#FFFFFF"><input name="finance.pay" type="text" id="raise_man2"  value="<s:property value='finance.pay'/>" size="10" maxlength="10" readonly></td>  
                          
                          <td width="11%" align="center" bgcolor="#999999"><div align="center" >收入</div></td>
                          <td width="18%" bgcolor="#FFFFFF"><input name="finance.income" type="text" id="raise_man" maxlength="10" size="10"  value="<s:property value='finance.income'/>" readonly></td> 
                </tr> 
                      
                      <tr>
                        <td align="center" bgcolor="#999999"><div align="center">项目/活动</div></td>
                        <td colspan="3" bgcolor="#FFFFFF"><input name="finance.activity" type="text" id="activity" size="60" maxlength="60" value="<s:property value='finance.activity'/>" readonly>
                        &nbsp;</td> 
                        <td width="11%" align="center" bgcolor="#999999"><div align="center" >经手人</div></td>
                        <td bgcolor="#FFFFFF"><input name="finance.responsible" type="text" id="responsible" maxlength="23" size="10"  value='<s:property value='finance.responsible'/>' readonly></td>
                    </tr>
                      
                      <tr>
                      		<td align="center" bgcolor="#999999"><div align="center">详情描述</div></td>
                            <td colspan="5" bgcolor="#FFFFFF"><div >
                              <textarea cols="65" rows="5" name="finance.desc" id="desc" readonly><s:property value='finance.desc'/></textarea>
                            </div>                        </td> 
               		  </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif">			</td> 
  		</tr>  
 	</table> 
			
</form>
    
</body>  
</html> 