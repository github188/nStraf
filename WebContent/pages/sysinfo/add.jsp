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
	<script type="text/javascript">
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
            var type=document.getElementsByName("identity");
           for(i=0;i<type.length;i++){ 
       	       if(type[i].checked) 
      	       {
                   document.getElementById("calendertype").value=type[i].value;
      	       } 
          	}           
			if(document.getElementById("education").value.trim()=="")
			{
				alert("请填写学历");
				return;
			}
			if(document.getElementById("birthdate").value.trim()=="")
			{
				alert("请填写出生日期");
				return;
			}
			if(document.getElementById("national").value.trim()=="")
			{
				alert("请填写民族");
				return;
			}
			if(document.getElementById("politicalstatus").value.trim()=="")
			{
				alert("请填写政治面貌");
				return;
			}
			if(document.getElementById("birthplace").value.trim()=="")
			{
				alert("请填写籍贯");
				return;
			}
			if(document.getElementById("englishskill").value.trim()=="")
			{
				alert("请填写外语水平");
				return;
			}
			if(document.getElementById("technicaltitle").value.trim()=="")
			{
				alert("请填写技术职称");
				return;
			}
			if(document.getElementById("startworddtae").value.trim()=="")
			{
				alert("请填写参加工作日期");
				return;
			}
			if(document.getElementById("mobile").value.trim()=="")
			{
				alert("请填写手机号码");
				return;
			}
			if(document.getElementById("hukoukind").value.trim()=="")
			{
				alert("请填写户口性质");
				return;
			}
			if(document.getElementById("marrystatus").value.trim()=="")
			{
				alert("请填写婚姻状况");
				return;
			}
			if(document.getElementById("graduateschool").value.trim()=="")
			{
				alert("请填写毕业院校");
				return;
			}
			if(document.getElementById("graduatedate").value.trim()=="")
			{
				alert("请填写毕业日期");
				return;
			}
			if(document.getElementById("studysubject").value.trim()=="")
			{
				alert("请填写所学专业");
				return;
			}
			if(document.getElementById("workingdate").value.trim()=="")
			{
				alert("请填写入职日期");
				return;
			}
			if(document.getElementById("tel").value.trim()=="")
			{
				alert("请填写办公电话");
				return;
			}
			if(document.getElementById("status").value.trim()=="离职")
			{
				if(document.getElementById("leavedate").value.trim()=="")
				{
					alert("已离职人员请填写离职日期");
					return;
				}
				if(document.getElementById("leavereason").value.trim()=="")
				{
					alert("已离职人员请填写离职原因");
					return;
				}
			}
			if(document.getElementById("status").value.trim()=="在职")
			{
				if(document.getElementById("leavedate").value.trim()!="")
				{
				    
					alert("在职人员请不要填写离职日期");
					return;
				}
				if(document.getElementById("leavereason").value.trim()!="")
				{
				   
					alert("在职人员请不要填写离职原因");
					return;
				}
			}
			if(document.getElementById("email").value.trim()=="")
			{
				alert("请填写外部邮箱地址");
				return;
			}
			if(document.getElementById("qq").value.trim()=="")
			{
				alert("请填写QQ号码");
				return;
			}
			if(document.getElementById("like").value.trim()=="")
			{
				alert("请填写个人爱好");
				return;
			}
			if(document.getElementById("hukouadress").value.trim()=="")
			{
				alert("请填写户口所在地址");
				return;
			}
			if(document.getElementById("hukounum").value.trim()=="")
			{
				alert("请填写户口邮箱编码");
				return;
			}
			if(document.getElementById("address").value.trim()=="")
			{
				alert("请填写家庭地址");
				return;
			}
			if(document.getElementById("addressnum").value.trim()=="")
			{
				alert("请填写家庭住址邮箱编码");
				return;
			}
			if(document.getElementById("contactsman").value.trim()=="")
			{
				alert("请填写紧急联系人");
				return;
			}
			if(document.getElementById("relation").value.trim()=="")
			{
				alert("请填写关系");
				return;
			}
			if(document.getElementById("contactscallnum").value.trim()=="")
			{
				alert("请填写紧急联系人手机号码");
				return;
			}
			if(document.getElementById("contactstelnum").value.trim()=="")
			{
				alert("请填写紧急联系人家庭电话");
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
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/sysinfo/sysinfoinfo!save.action"
			method="post">
             <input type="hidden" name="sysInfo.calendertype" id="calendertype" value='<s:property value="sysInfo.calendertype"/>'>
			<table width="95%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
					  <fieldset class="jui_fieldset" width="95%">
							<legend>
								员工档案
							</legend>
						  <table width="95%" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" style="border-bottom: none">
			      <br/>
								
                                  <tr bgcolor="#FFFFFF" >
					  <td bgcolor="#999999" width="6%" rowspan="4" align="center" ><div align="center">基本信息</div> </td>
					  <td bgcolor="#999999" width="8%" align="right">姓名<font color="#FF0000">*</font></td>
					  <td width="7%"  bgcolor="#FFFFFF"><input name="sysInfo.username"  type="text" id="username" size="16" class='bgg'
											maxlength="16" value='<s:property value="modify_man"/>' readonly="readonly"> </td>
					  <td bgcolor="#999999" width="4%" align="right">学历<font color="#FF0000">*</font></td>
					  <td width="7%"><select name="sysInfo.education" tabindex="4" id="education" style="width:100px">		
                      <option value='初中'>初中</option>
                      <option value='高中/中专'>高中/中专</option>
					  <option value='大专'>大专</option>
					  <option value='本科'>本科</option>
					  <option value='研究生'>研究生</option>
                      <option value='博士'>博士</option>
                      </select></td>
					  <td bgcolor="#999999"  width="5%" align="right">出生日期<font color="#FF0000">*</font></td>
					  <td  width="7%" >
					    <input name="sysInfo.birthdate" type="text" id="birthdate"  class="MyInput" />											  
                        <input type="radio" name="identity" value="公历" checked="checked" />公历 
                    	<input type="radio" name="identity" value="农历" />农历
						</td>
					  <td width="4%" bgcolor="#999999" align="right">民族<font color="#FF0000">*</font></td>
					  <td width="7%" ><input name="sysInfo.national" type="text" id="national" size="16"  maxlength="16" value='汉族'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF" >

					  <td bgcolor="#999999" align="right">政治面貌<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.politicalstatus"  id="politicalstatus"  style="width:90px">		
                      <option value='团员'>团员</option>
                      <option value='党员'>党员</option>
					  <option value='群众'>群众</option>
                      </select></td>
					  <td bgcolor="#999999" align="right">籍贯<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.birthplace" type="text" id="birthplace" size="16"
											maxlength="16"
									 value='广东省广州市'></td>
					  <td bgcolor="#999999" align="right">外语水平<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.englishskill" id="englishskill" style="width:115px">
                        <option value='一般'>一般</option>
                        <option value='CET4'>CET4</option>
                        <option value='CET6'>CET6</option>
                      </select></td>
					  <td bgcolor="#999999" align="right">技术职称<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.technicaltitle" id="technicaltitle" style="width:90px">
                        <option value='无'>无</option>
                        <option value='助理'>助理</option>
                        <option value='初级'>初级</option>
                          <option value='中级'>中级</option>
                          <option value='高级'>高级</option>
                      </select></td>
					  </tr>
					    <tr bgcolor="#FFFFFF" >

					  <td bgcolor="#999999" align="right">参加工作日期<font color="#FF0000">*</font></td>
					  <td ><input   name="sysInfo.startworddtae" type="text" id="startworddtae"  class="MyInput" value='<s:date name="sysInfo.startworddtae" format="yyyy-MM-dd"/>'></td>
					  <td bgcolor="#999999" align="right">手机号码<font color="#FF0000">*</font></td>
					  <td ><input name="sysInfo.mobile"  size="16" type="text" id="mobile"   value='<s:property value="sysInfo.mobile"/>'></td>
					  <td bgcolor="#999999" align="right">户口性质<font color="#FF0000">*</font></td>
					  <td ><select name="sysInfo.hukoukind" id="hukoukind" style="width:115px">		
                      <option value='农村'>农村</option>
                      <option value='城镇'>城镇</option>
                      </select></td>
					  <td bgcolor="#999999" align="right">婚姻状况<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.marrystatus" id="marrystatus" style="width:90px">		
                      <option value='未婚'>未婚</option>
                      <option value='已婚'>已婚</option>
                      </select></td>
					  </tr>
					  <tr bgcolor="#FFFFFF" >
					 
					  <td bgcolor="#999999" align="right">毕业院校<font color="#FF0000">*</font></td>
					  <td colspan="3"><input name="sysInfo.graduateschool"  size="50" type="text" id="graduateschool"  isSel="true" value='<s:property value="sysInfo.graduateschool"/>'>                      </td>
                      <td bgcolor="#999999" align="right">毕业日期<font color="#FF0000">*</font></td>
					  <td ><input   name="sysInfo.graduatedate" type="text" id="graduatedate" size="12"
											maxlength="12"  class="MyInput" value='<s:date name="sysInfo.graduatedate" format="yyyy-MM-dd"/>'></td>
					  <td bgcolor="#999999" align="right">所学专业<font color="#FF0000">*</font></td>
					  <td ><input name="sysInfo.studysubject" type="text" id="studysubject" size="16"
											maxlength="16"  value='<s:property value="sysInfo.studysubject"/>'></td>
				      </tr>
                        <tr bgcolor="#FFFFFF">
					  <td  rowspan="3"  align="center" bgcolor="#999999"><div align="center">在职信息<font color="#FF0000">
					  </font></div> </td>
					  <td  bgcolor="#999999" align="right">入职日期<font color="#FF0000">*</font></td>
					  <td ><input name="sysInfo.workingdate" type="text" id="workingdate" size="12"
											maxlength="12" class="MyInput" value='<s:date name="sysInfo.workingdate" format="yyyy-MM-dd"/>'></td>
					  <td  bgcolor="#999999" align="right">组别<font color="#FF0000">*</font></td>
					  <td ><select name="sysInfo.groupname"  id="groupname" style="width:115px">		
                			<option value='质量管理组'>质量管理组</option>
             				 <option value='基础软件测试组'>基础软件测试组</option>
             				 <option value='应用软件测试组'>应用软件测试组</option>
             				 <option value='技术支持组'>技术支持组</option>
            				</select></td>
			   
                      <td  bgcolor="#999999" align="right">岗位<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.workstation" id="workstation" style="width:115px">
                       <option value='助理软件评测师'>助理软件评测师</option>
                        <option value='初级软件评测师'>初级软件评测师</option>
                          <option value='中级软件评测师'>中级软件评测师</option>
                          <option value='高级软件评测师'>高级软件评测师</option>
                          <option value='监理师/主任'>监理师/主任</option>
                          <option value='资深监理师/经理'>资深监理师/经理</option>
                      </select></td>
					   <td  bgcolor="#999999" align="right"> 办公电话<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.tel"  size="16" type="text" id="tel"   value='<s:property value="sysInfo.tel"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999" align="right">状态<font color="#FF0000">*</font></td>
					  <td><select name="sysInfo.status"  id="status"  style="width:90px">		
                      <option value='在职'>在职</option>
                      <option value='离职'>离职</option>
                      </select></td>
					  <td bgcolor="#999999" align="right">离职日期</td>
					  <td><input name="sysInfo.leavedate" type="text" id="leavedate" size="12"
											maxlength="12"  class="MyInput"
					  value='<s:date name="sysInfo.leavedate" format="yyyy-MM-dd"/>'></td>
					  <td bgcolor="#999999" align="right">离职原因</td>
					  <td  colspan="3"><input name="sysInfo.leavereason"  size="50" type="text" id="leavereason"   value='<s:property value="sysInfo.leavereason"/>'>
					  </td>
                      </tr>
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">备注<font color="#FF0000">*</font></td>
					  <td  colspan="7">
					  <textarea cols="88"  name="sysInfo.remark" id="remark" ><s:property value="sysInfo.remark"/></textarea>					 </td>
					  </tr>
                       <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" rowspan="4" align="center"  ><div align="center">其它信息<font color="#FF0000">
					  </font></div> </td>
                     

					  <td bgcolor="#999999" align="right">外部邮箱<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.email"  size="16" type="text" id="email"   value='<s:property value="sysInfo.email"/>'></td>
					  <td bgcolor="#999999" align="right">QQ<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.qq"  size="16" type="text" id="qq"   value='<s:property value="sysInfo.qq"/>'></td>
					  <td bgcolor="#999999" align="right">个人爱好<font color="#FF0000">*</font></td>
					  <td  colspan="3"><input name="sysInfo.like"  size="50" type="text" id="like"   value='<s:property value="sysInfo.like"/>'></td>
                      </tr>
                       <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">户口所在地<font color="#FF0000">*</font></td>
					  <td colspan="5"><input name="sysInfo.hukouadress" size="84" type="text" id="hukouadress"  value='<s:property value="sysInfo.hukouadress"/>'></td>
					  <td bgcolor="#999999" align="right"> 邮政编码<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.hukounum" type="text" id="hukounum" size="16"
											maxlength="16" value='<s:property value="sysInfo.hukounum"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">家庭地址<font color="#FF0000">*</font></td>
					  <td colspan="5"><input name="sysInfo.address" size="84" type="text" id="address"   value='<s:property value="sysInfo.address"/>'></td>
					  <td bgcolor="#999999" align="right"> 邮政编码<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.addressnum" type="text" id="addressnum" size="16"
											maxlength="16" value='<s:property value="sysInfo.addressnum"/>'></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999" align="right">紧急联系人<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.contactsman"  type="text" id="contactsman" size="16"
											maxlength="16"  value='<s:property value="sysInfo.contactsman"/>'></td>
					  <td bgcolor="#999999" align="right">关系<font color="#FF0000">*</font></td>
					  <td><input name="sysInfo.relation" type="text" id="relation" size="10"
											maxlength="10" value='<s:property value="sysInfo.relation"/>'></td>
					  <td bgcolor="#999999" align="right">联系人电话<font color="#FF0000">*</font></td>
					  <td ><input  name="sysInfo.contactscallnum" type="text" id="contactscallnum" size="16"
											maxlength="16" value='<s:property value="sysInfo.contactscallnum"/>'></td>
                      <td bgcolor="#999999" align="right">家庭电话<font color="#FF0000">*</font></td>
					  <td ><input  name="sysInfo.contactstelnum" type="text" id="contactstelnum"  size="16"
											maxlength="16" value='<s:property value="sysInfo.contactstelnum"/>'></td>
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
