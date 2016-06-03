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


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" >
	function save(){
			if(document.getElementById("PersonReauditstatus").value=="待审核")
			{
				alert("请选择审核状态");
				return;
			}
			 if(document.getElementById("PersonReauditstatus").value=="审核不通过")
			{
				if(document.getElementById("suggest").value=="")
				{
					alert("审核不通过，必须输入审核意见");
					return;
				}
			}
			if(document.getElementById("subject").value=="")
			{
				alert("请输入会议纪要主题");
				return;
			}
			if(document.getElementById("currentDateTime").value=="")
			{
				alert("请输入会议时间");
				return;
			}
			if(document.getElementById("hour").value=="")
			{
				alert("请输入会议时长");
				return;
			}
			if(document.getElementById("addr").value=="")
			{
				alert("请输入会议地点");
				return;
			}
			if(document.getElementById("compere").value=="")
			{
				alert("请输入会议主持人");
				return;
			}
			if(document.getElementById("attendPersons").value=="")
			{
				alert("请输入会议出席人员");
				return;
			}
			if(document.getElementById("content").value=="")
			{
				alert("请输入会议内容");
				return;
			}
			if(document.getElementById("main").value=="")
			{
				alert("请输入会议纪要邮件主送人员");
				return;
			}
			if(document.getElementById("copy").value=="")
			{
				alert("请输入会议纪要邮件抄送人员");
				return;
			}
			if(document.getElementById("writer").value=="")
			{
				alert("请输入会议纪要记录人员");
				return;
			}
			if(document.getElementById("reAudit").value=="")
			{
				alert("请输入会议纪要复核人员");
				return;
			}
			if(document.getElementById("sign").value=="")
			{
				alert("请输入会议纪要签发人员");
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	function tihs()
		{
			var url="meetinginfo!close.action";
			var params={idFlag:$("#riid").val()};
			//jQuery.post(url, params, $(document).callbackFun{return;}, 'json');
			jQuery.post(url, params, '', 'json');
		}
		
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	</script>
<title>新增页面</title>
</head>

<body id="bodyid"   onUnload="tihs()">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/meeting/meetinginfo!updateByreAudit.action"   method="post">
    
    <input name="meeting.id" type="hidden" id="riid"  value='<s:property value="meeting.id"/>'>
<table width="990" height="232" border="1">
  <tr>
    <td width="95" bgcolor="#999999"><div align="right">主题</div></td>
    <td colspan="7">
      <label></label>   <input name="meeting.subject" type="text" id="subject" size="110" value="<s:property value='meeting.subject'/>"/></td>
  </tr>
 
  <tr>
    
    <td width="95" bgcolor="#999999"><div align="right" >会议时间</div></td>
    <td width="200">
<label>
                 <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="25"  value="<s:date name='meeting.currentDateTime' format='yyyy-MM-dd HH:mm:ss'/>" readonly/>
   		</label> </td>
    
    <td width="70" bgcolor="#999999"><div align="right" >会议时长</div></td>
    <td width="116">
<label>
   		     <input name="meeting.hour" type="text" id="hour" size="10" value="<s:property value='meeting.hour'/>"  readonly/>
   		</label>    </td>
  
   
    
    <td width="55" bgcolor="#999999"><div align="right">地点</div></td>
    <td width="90">
<label>
        <input name="meeting.addr" type="text" id="addr" size="20"  value="<s:property value='meeting.addr'/>"  readonly/>
    </label>    </td>
    <td width="55" bgcolor="#999999"><div align="right">主持人</div></td>
    <td width="94">
<label>
       <input name="meeting.compere" type="text" id="compere" size="12" value="<s:property value='meeting.compere'/>" readonly/>
    </label>    </td>
  </tr>

  
  <tr>
    <td bgcolor="#999999"><div align="right">出席人员</div></td>
    <td colspan="7">
      <textarea name="meeting.attendPersons" id="attendPersons" cols="109" rows="2"  readonly><s:property value='meeting.attendPersons'/></textarea>    </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">缺席人员</div></td>
    <td colspan="7"><input name="meeting.absentPersons" type="text" id="absentPersons" value="<s:property value='meeting.absentPersons'/>" size="146"  readonly/></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">会议内容</div></td>
    <td colspan="7"><textarea name="meeting.content" id="content" cols="109" rows="30"  readonly><s:property value='meeting.content'/></textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">主送</div></td>
    <td colspan="7"><textarea name="meeting.main" id="main" cols="109" rows="2"  readonly><s:property value='meeting.main'/></textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">抄送</div></td>
    <td colspan="7"><input name="meeting.copy" type="text" id="copy" value="<s:property value='meeting.copy'/>" size="146"   readonly/></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">记录</div></td>
    <td><label>
      <input name="meeting.writer" type="text" id="writer" size="14"  value="<s:property value='meeting.writer'/>" readonly/>
      </label>    </td>
    
    <td bgcolor="#999999"><div align="right">复核</div></td>
    <td colspan="3">
       <textarea name="meeting.reAudit" id="reAudit" cols="40" rows="1"  readonly><s:property value='meeting.reAudit'/></textarea>
         </td>
    
    <td  bgcolor="#999999">签发</td>
    <td > <input name="meeting.sign" type="text" id="sign" size="12"  value="<s:property value='meeting.sign'/>" readonly/></td>
    
   
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">审核状态<font color="#FF0000">*</font></div></td>
    <td colspan="7"><input name="meeting.reauditPersonstatus" type="hidden" id="reauditPersonstatus" size="20"  value="<s:property value='meeting.reauditPersonstatus'/>"/>
         	<select  name="PersonReauditstatus" style="width:163px;" id="PersonReauditstatus"   >
								<option value="待审核" >待审核</option>
                				<option value="审核通过">审核通过</option>
										<option  value="审核不通过">审核不通过</option>
              		</select > </td>  
                    <input type='hidden' id="st" name="st" value="<s:property value='PersonReauditstatus'/>"/>
                                  <s:if test="PersonReauditstatus!=null&&!PersonReauditstatus.equals('')">
											<script language="javascript">
												var status = document.getElementById("st").value;
						            		    status = decodeURI(status);
												document.getElementsByName("PersonReauditstatus")[0].value = status;
						            		</script>
									</s:if>
  </tr>
    </tr>
      <tr>
    <td bgcolor="#999999"><div align="right">审核意见</div></td>
    <td colspan="7"> <textarea name="suggest" id="suggest" cols="109" rows="7" ></textarea>
   	</td>
   
   
  </tr>
      <tr>
    <td bgcolor="#999999"><div align="right">审核记录</div></td>
    <td colspan="7"> <textarea name="meeting.reauditPersonsuggest" id="reauditPersonsuggest" cols="109" rows="7"  class="bgg" readonly><s:property value='meeting.reauditPersonsuggest'/></textarea>
   	</td>
   
   
  </tr>
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				 <input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
    </form>

</body>
</html>
