<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
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
	function save(){
	
		 if(document.getElementById("effectStart").value.trim()=="")
			{
				alert("请输入开始时间");
				return;
			}	
			 if(document.getElementById("sendCycle").value.trim()=="")
			{
				alert("请输入发送类型");
				return;
			}	
			 if(document.getElementById("description").value.trim()=="")
			{
				alert("请输入消息内容");
				return;
			}	
			document.getElementById("ok").disabled = true;
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	
	/* function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	} */
	
	function check()
	{
	   if(document.getElementById("certificationNo").value.trim()=="")
		{
			alert("请输入认定编号");
			$("#certificationNo").focus();
			return;
		}
		var url="cerinfo!check.action";
		var params={deviceNo:$("#certificationNo").val()};
		jQuery.post(url, params, $(document).callbackFun1, 'json');
	}
	$.fn.callbackFun1=function (json)
	 {	
		
	  	if(json!=null&&json==false)
		{	
		//	$("#deviceNo").focus();
			document.getElementById("ok").disabled = true;
			alert("认定编号与之前输入的相同，请重新输入");
			return;
		}else{
			document.getElementById("ok").disabled = false;
			return;
		}	
	 }
	
	   function sChange(obj){
	  	var myDate = new Date();
		var weekday = myDate.getDay();
		var monthday = myDate.getMonth();
		var today = myDate.getDate();
  		if(obj.value=="按年"){
   			hideAll();
           document.all.sendMonth.style.display="inline";  
		   document.all.sendDay.style.display="inline";
		   document.getElementById("sendDay").value = today + "号";
		   if(monthday == 0)
			{
				document.getElementById("sendMonth").value = "1月";
			}
			if(monthday == 1)
			{
				document.getElementById("sendMonth").value = "2月";
			}
			if(monthday == 2)
			{
				document.getElementById("sendMonth").value = "3月";
			}
			if(monthday == 3)
			{
				document.getElementById("sendMonth").value = "4月";
			}
			if(monthday == 4)
			{
				document.getElementById("sendMonth").value = "5月";
			}
			if(monthday == 5)
			{
				document.getElementById("sendMonth").value = "6月";
			}
			if(monthday == 6)
			{
				document.getElementById("sendMonth").value = "7月";
			}
			if(monthday == 7)
			{
				document.getElementById("sendMonth").value = "8月";
			}
			 if(monthday == 8)
			{
				document.getElementById("sendMonth").value = "9月";
			}
			if(monthday == 9)
			{
				document.getElementById("sendMonth").value = "10月";
			}
			if(monthday == 10)
			{
				document.getElementById("sendMonth").value = "11月";
			}
			if(monthday == 11)
			{
				document.getElementById("sendMonth").value = "12月";
			}
			document.getElementById("sendWeek").value = "";
		}  
        if(obj.value=="按月"){
   			hideAll();
           document.all.sendDay.style.display="inline";
		   document.getElementById("sendDay").value = today + "号";
		   document.getElementById("sendMonth").value = "";
		   document.getElementById("sendWeek").value = "";
        }
        if(obj.value=="按周"){
           hideAll();  
  		    document.all.sendWeek.style.display="inline";	
			if(weekday == 0)
			{
				document.getElementById("sendWeek").value = "星期天";
			}
			if(weekday == 1)
			{
				document.getElementById("sendWeek").value = "星期一";

			}
			if(weekday == 2)
			{
				document.getElementById("sendWeek").value = "星期二";
			}
			if(weekday == 3)
			{
				document.getElementById("sendWeek").value = "星期三";
			}
			if(weekday == 4)
			{
				document.getElementById("sendWeek").value = "星期四";
			}
			if(weekday == 5)
			{
				document.getElementById("sendWeek").value = "星期五";
			}
			if(weekday == 6)
			{
				document.getElementById("sendWeek").value = "星期六";
			}
			document.getElementById("sendMonth").value = "";
		  	document.getElementById("sendDay").value = "";
        }
        if(obj.value=="按日"){
           hideAll();  
		   document.getElementById("sendWeek").value = "";
		   document.getElementById("sendDay").value = "";
		   document.getElementById("sendMonth").value = "";
        }
   }
   function hideAll(){
	document.all.sendMonth.style.display="none"
	document.all.sendWeek.style.display="none"
	document.all.sendDay.style.display="none"
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
	
	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/autoOa/autoOainfo!save.action"   method="post">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	</table>
<br/>
<table width="90%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="消息设置" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                  
                  <tr>
                      <td width="7%" align="center"  bgcolor="#999999"><div align="center">起始时间<font color="#FF0000">*</font></div></td>
    <td width="25%" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="autoOas.effectStart" type="text" id="effectStart" size="25" maxlength="100" class="MyInput" readonly="readonly" />
</label>   <input name="autoOas.createDate" type="hidden"
											id="createDate" class="MyInput" readonly="readonly"
											value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'>	 </td>
        
                    <td width="15%" bgcolor="#999999"><div align="center" >结束时间</div></td>
    <td width="55%" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
  <input name="autoOas.effectend" type="text" id="effectend" size="25" maxlength="100" class="MyInput" readonly="readonly" />
    	</label>
为空则为没有结束时间</td>
                  </tr> 

                  <tr> 
                 
                     <td align="center"  width="13%" bgcolor="#999999"><div align="center">提醒类型<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF">
                         <select name="autoOas.sendCycle" id="sendCycle" align="left" style="width:101px" onChange="sChange(this)">
                        			 <option value='按日' style="width:105px">按日</option>
                                     <option value='按周'>按周</option>
                                     <option value='按月'>按月</option>
                                     <option value='按年'>按年</option>
       					</select></td>
<td align="center" bgcolor="#999999">消息发送时间</td>
                  <td bgcolor="#FFFFFF">
                 <s:select list="{'1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'}" name="autoOas.sendMonth" value="sendMonth" theme="simple"   cssStyle="width:101px;"  style="display:none" headerKey="" headerValue="----"  id="sendMonth" ></s:select><s:select list="{'星期一','星期二','星期三','星期四','星期五','星期六','星期天'}" name="autoOas.sendWeek" value="sendWeek" theme="simple"   cssStyle="width:101px;"  style="display:none" headerKey="" headerValue="----"  id="sendWeek" ></s:select> <s:select list="{'1号','2v','3号','4号','5号','6号','7号','8号','9号','10号','11号','12号','13号','14号','15号','16号','17号','18号','19号','20号','21号','22号','23号','24号','25号','26号','27号','28号','29号','30号','31号'}" name="autoOas.sendDay" value="sendDay" theme="simple"   cssStyle="width:101px;" style="display:none" headerKey="" headerValue="----"  id="sendDay" ></s:select><input name="autoOas.sendDate" type="text" id="sendDate" value='<s:date name="new java.util.Date()" format="HH:mm:ss"/>' size="10" maxlength="20"  class="MyInput"  issel="true" isdate="true" onFocus="ShowTime(this)" dofun="ShowTime(this)" ><input type="hidden" name="weekDay" id="weekDay" value='<s:property value="weekDay"/>'>
                 为空则为当前时间 </td>
                  </tr>
                  <tr>
                    
                    <td bgcolor="#999999"><div align="center">接收人员<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" colspan="3">
                    	<tm:dmselect name="embracer_manlist" dicId="embracerlist" scope="request" separator="," width="710" ></tm:dmselect>
                    	<!-- 
                    	<input type="text" name="train.student" size="62" id="student">
                     -->                    </td>
                  </tr>
                  <tr>
                  	  <td align="center"  width="13%" bgcolor="#999999"><div align="center">提醒内容<font color="#FF0000">*</font></div></td> 
					<td colspan="3" bgcolor="#FFFFFF">
                  	  <textarea name="autoOas.description" id="description" cols="88" rows="5" ></textarea>                    </td>
                  </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 