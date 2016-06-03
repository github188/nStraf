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
			document.getElementById("ok").disabled=true;
			reportInfoForm.submit();	
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

<body>
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/meeting/meetinginfo!updateByAudit.action"   method="post">
        <input type="hidden" name="meeting.id" value="<s:property value='meeting.id'/>">
        <input type="hidden" name="meeting.auditStatus" value="<s:property value='meeting.auditStatus'/>">

<table width="990" height="232" border="1">
  <tr>
    <td width="95" bgcolor="#999999"><div align="right">主题<font color="#FF0000">*</font></div></td>
    <td colspan="7">
      <label></label>   <input name="meeting.subject" type="text" id="subject" size="110" value="<s:property value='meeting.subject'/>"/></td>
  </tr>
 
  <tr>
    
    <td width="95" bgcolor="#999999"><div align="right" >会议时间<font color="#FF0000">*</font></div></td>
    <td width="190">
<label>
                 <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="25" maxlength="100" class="MyInput" issel="true" isdate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" value="<s:date name='meeting.currentDateTime' format='yyyy-MM-dd HH:mm:ss'/>">
   		</label>    </td>
    
    <td width="70" bgcolor="#999999"><div align="right" >会议时长<font color="#FF0000">*</font></div></td>
    <td width="90">
<label>
   		     <input name="meeting.hour" type="text" id="hour" size="10" value="<s:property value='meeting.hour'/>" />
   		</label>    </td>

    <td width="55" bgcolor="#999999"><div align="right">地点<font color="#FF0000">*</font></div></td>
    <td width="90">
<label>
        <input name="meeting.addr" type="text" id="addr" size="20"  value="<s:property value='meeting.addr'/>"/>
    </label>    </td>
    <td width="55" bgcolor="#999999"><div align="right">主持人<font color="#FF0000">*</font></div></td>
    <td width="90">
<label>
         <s:select  list="unames" name="meeting.compere" theme="simple"  cssStyle="width:120px;" headerKey="" headerValue="----" id="compere" value="meeting.compere"></s:select>
    </label>    </td>
  </tr>

  
  <tr>
    <td bgcolor="#999999"><div align="right">出席人员<font color="#FF0000">*</font></div></td>
   <td colspan="7"><tm:dmselect name="attendPersons" dicId="umap" scope="request" separator="," width="875" ></tm:dmselect></td>
    <input type="hidden" name="t11" id="t11" value="<s:property value='attendPersons'/>">
                      	<script>
						var t11=document.getElementById("t11");
						document.getElementById("attendPersonsTxt").value=t11.value;
						document.getElementById("attendPersons").value=t11.value;
						var aa=t11.value.split(",");
						var bb=document.getElementsByName("attendPersonsChk");
						for(var j=1;j<bb.length;j++){
							for(var i=0;i<aa.length;i++){
								if(aa[i]==bb[j].value){
									bb[j].checked=true;
							    }
							}	
						}
						
                  	</script>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">缺席人员<font color="#FF0000">*</font></div></td>
  <td colspan="7"><tm:dmselect name="absentPersons" dicId="umaps" scope="request" separator="," width="875" ></tm:dmselect></td>
                <input type="hidden" name="t113" id="t113" value="<s:property value='absentPersons'/>">
                      	<script>
						var t113=document.getElementById("t113");
						document.getElementById("absentPersonsTxt").value=t113.value;
						document.getElementById("absentPersons").value=t113.value;
						var aa3=t113.value.split(",");
						var bb3=document.getElementsByName("absentPersonsChk");
						for(var j=1;j<bb3.length;j++){
							for(var i=0;i<aa3.length;i++){
								if(aa3[i]==bb3[j].value){
									bb3[j].checked=true;
							    }
							}	
						}
						
                  	</script>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">会议内容<font color="#FF0000">*</font></div></td>
    <td colspan="7"><textarea name="meeting.content" id="content" cols="109" rows="30"><s:property value='meeting.content'/></textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">主送<font color="#FF0000">*</font></div></td>
     <td colspan="7"><tm:dmselect name="main" dicId="umap" scope="request" separator="," width="875" ></tm:dmselect></td>
            <input type="hidden" name="t112" id="t112" value="<s:property value='main'/>">
                      	<script>
						var t112=document.getElementById("t112");
						document.getElementById("mainTxt").value=t112.value;
						document.getElementById("main").value=t112.value;
						var aa2=t112.value.split(",");
						var bb2=document.getElementsByName("mainChk");
						for(var j=1;j<bb2.length;j++){
							for(var i=0;i<aa2.length;i++){
								if(aa2[i]==bb2[j].value){
									bb2[j].checked=true;
							    }
							}	
						}
						
                  	</script>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">抄送<font color="#FF0000">*</font></div></td>
   <td colspan="7"><tm:dmselect name="copy" dicId="umap" scope="request" separator="," width="875" ></tm:dmselect></td>
        <input type="hidden" name="t111" id="t111" value="<s:property value='copy'/>">
                      	<script>
						var t111=document.getElementById("t111");
						document.getElementById("copyTxt").value=t111.value;
						document.getElementById("copy").value=t111.value;
						var aa1=t111.value.split(",");
						var bb1=document.getElementsByName("copyChk");
						for(var j=1;j<bb1.length;j++){
							for(var i=0;i<aa1.length;i++){
								if(aa1[i]==bb1[j].value){
									bb1[j].checked=true;
							    }
							}	
						}
						
                  	</script>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">记录<font color="#FF0000">*</font></div></td>
    <td><label>
     <s:select  list="unames" name="meeting.writer" theme="simple"  cssStyle="width:141px;" headerKey="" headerValue="----" id="writer" value="meeting.writer"></s:select>
      </label>    </td>
    
    <td bgcolor="#999999"><div align="right">复核<font color="#FF0000">*</font></div></td>
     <td colspan="3">
     <tm:dmselect name="reAudit" dicId="umap" scope="request" separator="," width="300" ></tm:dmselect></td>
        <input type="hidden" name="t114" id="t114" value="<s:property value='reAudit'/>">
                      	<script>
						var t114=document.getElementById("t114");
						document.getElementById("reAuditTxt").value=t114.value;
						document.getElementById("reAudit").value=t114.value;
						var aa4=t114.value.split(",");
						var bb4=document.getElementsByName("reAuditChk");
						for(var j=1;j<bb4.length;j++){
							for(var i=0;i<aa4.length;i++){
								if(aa4[i]==bb4[j].value){
									bb4[j].checked=true;
							    }
							}	
						}
						
                  	</script>
    
    <td  bgcolor="#999999">签发<font color="#FF0000">*</font></td>
    <td > <s:select  list="unames" name="meeting.sign" theme="simple"  cssStyle="width:120px;" headerKey="" headerValue="----" id="sign" value="meeting.sign"></s:select></td>
    
   
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">审核状态<font color="#FF0000">*</font></div></td>
    <td ><input name="meeting.reauditPersonstatus" type="hidden" id="reauditPersonstatus" size="20"  value="<s:property value='meeting.reauditPersonstatus'/>"/>
         	<select  name="PersonReauditstatus" style="width:163px;" id="PersonReauditstatus"   >
								<option value="待签发" >待签发</option>
                                <option value="待审核" >待审核</option>
                				<option value="审核通过">审核通过</option>
										<option  value="审核不通过">审核不通过</option>
              		</select > </td>  
                    <input type='hidden' id="st" name="st" value="<s:property value='meeting.auditStatus'/>"/>
                     <s:if test="meeting.auditStatus!=null&&!meeting.auditStatus.equals('')">
											<script language="javascript">
												var status = document.getElementById("st").value;
						            		    status = decodeURI(status);
												document.getElementsByName("PersonReauditstatus")[0].value = status;
						            		</script>				
                     </s:if>				
    <td colspan="2">
      <input type="checkbox" name="send_flag" id="send_flag" value="1" />
      发送邮件</td>
    <td colspan="4">&nbsp;</td>
   
  </tr>
   <tr>
    <td bgcolor="#999999"><div align="right">审核意见</div></td>
    <td colspan="7"> <textarea name="suggest" id="suggest" cols="109" rows="7" ></textarea>
   	</td>
   
   
  </tr>
      <tr>
    <td bgcolor="#999999"><div align="right">审核记录</div></td>
    <td colspan="7"> <textarea name="meeting.reauditPersonsuggest" class="bgg" id="reauditPersonsuggest" cols="109" rows="7" readonly><s:property value='meeting.reauditPersonsuggest'/></textarea>
   	</td>
   
   
  </tr>
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				 <input type="button" name="ok"  id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
    </form>
</body>
</html>
