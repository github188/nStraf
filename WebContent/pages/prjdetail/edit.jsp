<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
			window.returnValue=false;
		 	window.close();
		}
		
		function update(){
			if(document.getElementById("processQuality").value.trim()=="")
			{
				alert("请选择相应的过程质量情况");
				return;
			}
			if(document.getElementById("progress").value.trim()=="")
			{
				alert("请选择相应的进度情况");
				return;
			}
			if(! Validate(document.getElementById("unfitNumber").value, "Number"))
			{
				alert("请输入正确的不符合测试准入次数");
				return;
			}
			if(! Validate(document.getElementById("submitVersionNumer").value, "Number"))
			{
				alert("请输入正确的提交版本数");
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
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function numVali(itemName) {
			var ret = 0, tmp = 0;
			if(! Validate(itemName.value, "Url"))
			{
				alert("请输入访问地址，如http://ip:port或ftp://ip:port");
			}
		}
		
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}
		function calculate(){
			var projectQuality= document.getElementById("projectQuality").value;
			var submitVersionNumer=document.getElementById("submitVersionNumer").value;
			var bugResolveRate=document.getElementById("bugResolveRate").value;
			var unfitNumber=document.getElementById("unfitNumber").value;
			var processQuality=document.getElementById("processQuality").value;
			var progress=document.getElementById("progress").value;
			
			if(document.getElementById("processQuality").value.trim()=="")
			{
				alert("请选择相应的过程质量情况");
				return;
			}
			if(document.getElementById("progress").value.trim()=="")
			{
				alert("请选择相应的进度情况");
				return;
			}
			if(! Validate(document.getElementById("unfitNumber").value, "Number"))
			{
				alert("请输入正确的不符合测试准入次数");
				return;
			}
			if(! Validate(document.getElementById("submitVersionNumer").value, "Number"))
			{
				alert("请输入正确的提交版本数");
				return;
			}
			var qualityPoint
			
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/prjdetail/prjdetailinfo!update.action"   method="post">
		<input type="hidden" name="part.id" value='<s:property value="part.id"/>'/>
		<input type="hidden" name="part.prjName" value='<s:property value="part.prjName"/>'/>
		<input type="hidden" name="part.staticMonth" value='<s:property value="part.staticMonth"/>'/>
		<input type="hidden" name="part.projectQuality" value='<s:property value="part.projectQuality"/>'/>
		<input type="hidden" name="part.bugResolveRate" value='<s:property value="part.bugResolveRate"/>'/>
		<input type="hidden" name="part.start" value='<s:property value="part.start"/>'/>
		
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="项目详细信息" /></legend>
                    <table width="100%" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                        <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">项目名称：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="part.prjName"/></td> 
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">统计月份：<font color="#FF0000">*</font>&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="part.staticMonth"/></td> 
                      </tr> 
                      
                      <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">质量得分：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF" id="qq"> 
                           <s:if test="part.qualityPoint!=0">
								<s:property value="part.qualityPoint"/>
							</s:if>
						</td>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">总体评价：<font color="#FF0000">*</font>&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="part.totalEvaluate"/></td> 
                      </tr>
                      
                      <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">项目质量：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="part.projectQualityStr"/></td> 
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">缺陷解决率：<font color="#FF0000">*</font></div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="part.bugResolveRate"/></td> 
                      </tr>
                      
                      <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">提交版本数：<font color="#FF0000">*</font>&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="part.submitVersionNumer" type="text" id="submitVersionNumer" size="30" maxlength="30" value='<s:property value="part.submitVersionNumer"/>'>&nbsp;</td> 
                         
                          <td align="center" bgcolor="#999999"><div align="center">不符合测试准入次数：&nbsp;&nbsp;<font color="#FF0000">*</font>&nbsp;</div></td>
                            <td colspan="3" bgcolor="#FFFFFF"><input name="part.unfitNumber" type="text" id="unfitNumber" size="30" maxlength="30" value='<s:property value="part.unfitNumber"/>'></td> 
                         
                      </tr>
                      
                      
                      <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">过程质量：<font color="#FF0000">*</font>&nbsp;&nbsp;&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF">
                          	<select name="part.processQuality" id="processQuality" style="width: 199px">
                                 <option value="" >请选择</option>
                                 <option value="5">优</option>
                                 <option value="4">良</option>
                                 <option value="3">中</option>
                                 <option value="2">较差</option>
                                 <option value="1">差</option>
                            </select>
                           </td> 
                          <s:if test="part.processQuality!=null&&!part.processQuality.equals('')">
											<script language="javascript">
						            		$("#processQuality").val('<s:property value='part.processQuality'/>');
						            		</script>
									</s:if>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center">进度：&nbsp;<font color="#FF0000">*</font>&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF">
                          <select name="part.progress" id="progress" style="width: 199px" >
                            <option value="" >请选择</option>
                                <option value="5">提前两周</option>
                                 <option value="4">提前一周</option>
                                 <option value="3">正常</option>
                                 <option value="2">延迟一周</option>
                                 <option value="1">延迟二周</option>
                           </select>
                           </td> 
                           <s:if test="part.progress!=null&&!part.progress.equals('')">
											<script language="javascript">
						            		$("#progress").val('<s:property value='part.progress'/>');
						            		</script>
									</s:if>
                      </tr> 
                      <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center">PM：&nbsp;</div></td>  
                          <td bgcolor="#FFFFFF"><input name="part.PM" type="text" id="PM" size="30" maxlength="30" value='<s:property value="part.PM"/>'>&nbsp;</td> 
                          
                            
                      </tr>
                     
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="update()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 