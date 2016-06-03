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
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" >
	function save(){
	
		 if(document.getElementById("courseName").value.trim()=="")
			{
				alert("请输入课程名称");
				return;
			}	
			 if(document.getElementById("category").value.trim()=="")
			{
				alert("请输入类别");
				return;
			}	
			 if(document.getElementById("teacher").value.trim()=="")
			{
				alert("请输入讲师");
				return;
			}	
			 if(document.getElementById("trainDate").value.trim()=="")
			{
				alert("请输入培训日期");
				return;
			}	
			 if(document.getElementById("trainHour").value.trim()=="")
			{
				alert("请输入培训时长");
				return;
			}	
			 if(document.getElementById("addr").value.trim()=="")
			{
				alert("请输入地点");
				return;
			}	
			 if(document.getElementById("student").value.trim()=="")
			{
				alert("请输入参训人员");
				return;
			}	
			 
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	
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
		   function sChange(obj){

  		if(obj.value=="按年"){
   			hideAll();
           document.all.sendMonth.style.display="inline";  
		   document.all.sendDay.style.display="inline";
		}  
        if(obj.value=="按月"){
   			hideAll();
           document.all.sendDay.style.display="inline";
		
        }
        if(obj.value=="按周"){
           hideAll();  
  		    document.all.sendWeek.style.display="inline";	
		
        }
        if(obj.value=="按日"){
           hideAll();  
		
        }
   }
   function hideAll(){
	document.all.sendMonth.style.display="none"
	document.all.sendWeek.style.display="none"
	document.all.sendDay.style.display="none"
   }
	
	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/autoOa/autoOainfo!save.action"   method="post">
    	<input type="hidden" name="autoOas.id"  value="<s:property value='autoOas.id'/>">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	</table>
<br/>
<table width="90%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="培训课程" /></legend>
                    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
                    <br/>
                    
      
    
                  <tr>
                      <td width="10%" align="center"  bgcolor="#999999"><div align="center">起始时间<font color="#FF0000">*</font></div></td>
    <td width="26%" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
<input name="autoOas.effectStart" type="text" id="effectStart" size="25" maxlength="100" class="MyInput" value='<s:date name="autoOas.effectStart" format="yyyy-MM-dd"/>' readonly="readonly" />
</label>    </td>
        
                    <td width="10%" bgcolor="#999999"><div align="center" >结束时间</div></td>
    <td width="55%" bgcolor="#FFFFFF">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
  <input name="autoOas.effectend" type="text" id="effectend" size="25" maxlength="100" class="MyInput" value='<s:date name="autoOas.effectend" format="yyyy-MM-dd"/>' readonly="readonly" />
    	</label>
为空则为没有结束时间</td>
                  </tr> 

                  <tr> 
                 
                     <td align="center"  bgcolor="#999999"><div align="center">提醒类型<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF">
                         <!--<select name="autoOas.sendCycle" id="sendCycle" align="left" style="width:101px" onChange="sChange(this)">
                        			 <option value='按日' style="width:105px">按日</option>
                                     <option value='按周'>按周</option>
                                     <option value='按月'>按月</option>
                                     <option value='按年'>按年</option>
       					</select>-->
                        <s:select list="{'按日','按周','按月','按年'}" name="autoOas.sendCycle"  theme="simple"   cssStyle="width:101px;"   id="sendCycle" onChange="sChange(this)"></s:select></td>
<td align="center" bgcolor="#999999">消息发送时间</td>
                  <td bgcolor="#FFFFFF">
                 <s:select list="{'1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月',''}" name="autoOas.sendMonth"  theme="simple"   cssStyle="width:101px;"  style="display:none"  id="sendMonth" ></s:select><s:select list="{'星期一','星期二','星期三','星期四','星期五','星期六','星期天',''}" name="autoOas.sendWeek"  theme="simple"   cssStyle="width:101px;"  style="display:none"  id="sendWeek" ></s:select> <s:select list="{'1号','2号','3号','4号','5号','6号','7号','8号','9号','10号','11号','12号','13号','14号','15号','16号','17号','18号','19号','20号','21号','22号','23号','24号','25号','26号','27号','28号','29号','30号','31号',''}" name="autoOas.sendDay"  theme="simple"   cssStyle="width:101px;" style="display:none"  id="sendDay" ></s:select><input name="autoOas.sendDate" type="text" id="sendDate" value='<s:property value="autoOas.sendDate"/>' size="10" maxlength="20"  class="MyInput"  issel="true" isdate="true" onFocus="ShowTime(this)" dofun="ShowTime(this)" ><input type="hidden" name="weekDay" id="weekDay" value='<s:property value="weekDay"/>'>
                 为空则为当前时间 </td>
                 <script type="text/javascript">
				 
		if(document.getElementById("sendCycle").value=="按年"){
   			hideAll();
           document.all.sendMonth.style.display="inline";  
		   document.all.sendDay.style.display="inline";
		}  
        if(document.getElementById("sendCycle").value=="按月"){
   			hideAll();
           document.all.sendDay.style.display="inline";
		
        }
        if(document.getElementById("sendCycle").value=="按周"){
          	 hideAll();  
  		    document.all.sendWeek.style.display="inline";	
		
        }
        if(document.getElementById("sendCycle").value=="按日"){
           hideAll();  
		
        }
		</script>
                  </tr>
                  <tr>
                    
                    <td bgcolor="#999999"><div align="center">接收人员<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" colspan="3">
                    
                    	<input type="text" name="autoOas.embracerMan" size="100" id="embracerMan" value='<s:property value="autoOas.embracerMan"/>'>
                                      </td>
                  </tr>
                  <tr>
                  	  <td align="center"  bgcolor="#999999"><div align="center">提醒内容<font color="#FF0000">*</font></div></td> 
					<td colspan="3" bgcolor="#FFFFFF">
                  	  <textarea name="autoOas.description" id="description" cols="90" rows="5" ><s:property value="autoOas.description"/></textarea>                    </td>
                  </tr>
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 