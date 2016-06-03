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
                      
			if(document.getElementById("month_date").value.trim()=="")
			{
				alert("请输入月份");
				return;
			}
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
		
				
	 function validateInputStart(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 
		 function validateInputStartMonth(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 	
		var checkMonthStartFlag = true;
		
		
		function startMonthHdl()
		{
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("month_date").value.trim() == "")
			{
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("month_date").value.trim() != "")
			{
				if(validateInputStart("month_date"))
				{
					dayFlag = true;
				}
				else if(validateInputStartMonth("month_date"))
				{
					monthFlag = true;
				}
				else
				{
					//alert("开始月份格式不正确,请使用日期选择!");
					checkMonthStartFlag = false;
					return false;
				}
				if(dayFlag)
				{
					var str = document.getElementById("month_date").value.split("-");
					if(str[1]!="10" && str[1]!="11" && str[1]!="12")
					{
						document.getElementById("month_date").value = parseInt(str[0],10) + "-" + "0" + parseInt(str[1],10);
					}
					else
					{
						document.getElementById("month_date").value = parseInt(str[0],10) + "-" + parseInt(str[1],10);
					}
				}
				checkMonthStartFlag = true;
				return true;
			}
		}
	
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/performance/performanceinfo!uplock.action"
			method="post">
			<table width="90%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="90%">
							<legend>提交月度考核</legend>
					  <table width="88%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			      <br />
								
                                
								<tr>
									<td align="center" width="7%" bgcolor="#999999">
		  <div align="center">
											月份
								  <font color="#FF0000">*</font>										</div>									</td>
									<td bgcolor="#FFFFFF" width="80%"  onClick="startMonthHdl()"><input name="month_date" type="text"
											id="month_date" class="MyInput" issel="true" isdate="true"
											onFocus="ShowDate(this)" dofun="ShowDate(this)"
											value='<s:date name="new java.util.Date()" format="yyyy-MM"/>'></td>
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
