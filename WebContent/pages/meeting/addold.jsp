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
			
			 if(document.getElementById("currentDateTime").value.trim()=="")
			{
				alert("请输入会议时间");
				return;
			}
			/*
			if(document.getElementById("attendPersons").value.trim()=="")
			{
				alert("请输入出席人员");
				return;
			}
			if(document.getElementById("content").value.trim()=="")
			{
				alert("请输入会议内容");
				return;
			}
			if(document.getElementById("main").value.trim()=="")
			{
				alert("请输入邮件主送人员");
				return;
			}
			if(document.getElementById("copy").value.trim()=="")
			{
				alert("请输入邮件抄送人员");
				return;
			}
			*/
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
	</script>
<title>新增页面</title>
</head>

<body>
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/meeting/meetinginfo!save.action"   method="post">
<table width="950" height="232" border="1">
  <tr>
    <td width="111" bgcolor="#999999"><div align="right">主题</div></td>
    <td colspan="7">
      <label></label>   <input name="meeting.subject" type="text" id="subject" size="110" /></td>
  </tr>
 
  <tr>
    
    <td bgcolor="#999999"><div align="right" >会议时间</div></td>
    <td width="234">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
  <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="25" maxlength="100" class="MyInput" issel="true" isdate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" >
    	</label>    </td>
    
    <td width="85" bgcolor="#999999"><div align="right" >会议时长</div></td>
    <td width="128">
<label>
   		     <input name="meeting.hour" type="text" id="hour" size="10" />
   		</label>    </td>
  
   
    
    <td width="32" bgcolor="#999999"><div align="right">地点</div></td>
    <td width="146">
<label>
        <input name="meeting.addr" type="text" id="addr" size="20" />
    </label>    </td>
    <td width="68" bgcolor="#999999"><div align="right">主持人</div></td>
    <td width="94">
<label>
       <!--
        <input name="meeting.compere" type="text" id="compere" size="12" />
        -->
        <s:select  list="unames" name="meeting.compere" theme="simple"  cssStyle="width:141px;" headerKey="" headerValue="----" id="compere"></s:select>
    </label>    </td>
  </tr>

  
  <tr>
    <td bgcolor="#999999"><div align="right">出席人员</div></td>
    <td colspan="7"><tm:dmselect name="attendPersons" dicId="umap" scope="request" separator="," width="870" ></tm:dmselect></td>
     <!-- <textarea name="meeting.attendPersons" id="attendPersons" cols="100" rows="2"></textarea> -->
   
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">缺席人员</div></td>
    <td colspan="7">
    <!--<input name="meeting.absentPersons" type="text" id="absentPersons" size="141" value="<s:property value='meeting.absentPersons'/>"/>-->
    <tm:dmselect name="absentPersons" dicId="umaps" scope="request" separator="," width="870" ></tm:dmselect>
    </td>
    <!--<input name="meeting.absentPersons" type="text" id="absentPersons" size="110" />-->
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">会议内容</div></td>
    <td colspan="7"><textarea name="meeting.content" id="content" cols="106" rows="30"></textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">主送</div></td>
    <td colspan="7"><tm:dmselect name="main" dicId="umap" scope="request" separator="," width="870" ></tm:dmselect></td>
    <!--<textarea name="meeting.main" id="main" cols="100" rows="2"></textarea>-->
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">抄送</div></td>
    <td colspan="7"><tm:dmselect name="copy" dicId="umap" scope="request" separator="," width="870" ></tm:dmselect></td>
    <!--<input name="meeting.copy" type="text" id="copy" value="" size="110" />-->
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">记录</div></td>
    <td><label>
      <s:select  list="unames" name="meeting.writer" theme="simple"  cssStyle="width:141px;" headerKey="" headerValue="----" id="writer" value="writer"></s:select>
    <!--
      <input name="meeting.writer" type="text" id="writer" size="14" />
      -->
      </label>    </td>
    
    <td bgcolor="#999999"><div align="right">复核</div></td>
    <td><label>
    <!--
      <input name="meeting.reAudit" type="text" id="reAudit" size="14" />
      -->
       <s:select  list="unames" name="meeting.reAudit" theme="simple"  cssStyle="width:141px;" headerKey="" headerValue="----" id="reAudit" value="audit"></s:select>
      </label>    </td>
    
    <td  bgcolor="#999999">签发</td>
    <td colspan="3">
    <s:select  list="unames" name="meeting.sign" theme="simple"  cssStyle="width:141px;" headerKey="" headerValue="----" id="sign" value="audit"></s:select>
    <!--
    <input name="meeting.sign" type="text" id="sign" size="14" />
    -->
    </td>
    
   
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">审核状态</div></td>
    <td colspan="7">
      <label>
      
       <!--
        <select name="meeting.auditStatus" id="auditStatus">
          <option selected="selected">未审核</option>
          <option>审核通过</option>
          <option>审核不通过</option>
        </select>
        -->
        </label>    <input name="meeting.auditStatus" type="text" id="auditStatus" size="20"  value="未审核" readonly/></td>
   
   
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
