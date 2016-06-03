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
			action="<%=request.getContextPath()%>/pages/sysinfo/sysinfoinfo!update.action"
			method="post">
            <input type="hidden" name="sysInfo.id" value='<s:property value="sysInfo.id"/>'>
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
					  <td bgcolor="#999999" width="5%" rowspan="4" align="center" ><div align="center">基本信息<font color="#FF0000">
					  </font></div> </td>
					  <td bgcolor="#999999" width="6%" align="right">姓名</td>
					  <td width="7%"  bgcolor="#FFFFFF"><input name="sysInfo.username"  type="text" id="username" size="16"
											maxlength="16" value='<s:property value="sysInfo.username"/>' readonly="readonly"> </td>
					  <td bgcolor="#999999" width="5%" align="right">学历</td>
					  <td width="7%"><input name="sysInfo.education"  type="text" id="education" size="16"
											maxlength="16" value='<s:property value="sysInfo.education"/>' readonly="readonly"></td>
					  <td bgcolor="#999999"  width="5%" align="right">出生日期</td>
					  <td  width="7%" ><input name="sysInfo.birthdate" type="text" id="birthdate"  size="16"
											maxlength="16" value='<s:date name="sysInfo.birthdate" format="yyyy-MM-dd"/>' readonly="readonly"/>
                                            <input type="radio" name="identity" value="公历" readonly="readonly"/>公历 
                    						  <input type="radio" name="identity" value="农历" readonly="readonly"/>农历</td>
                                              <script language="javascript">
                                                  var calendertype=document.getElementById("calendertype");
												  var identity=document.getElementsByName("identity");
												  var i = 0;
                                                  for(i=0;i<identity.length;i++){ 
       	                                          if(identity[i].value == calendertype.value ) 
      	                                          {
                                                       identity[i].checked = true; 
      	                                          } 
                                                 }    
                                                </script>
					  <td width="5%" bgcolor="#999999" align="right">民族</td>
					  <td width="7%" ><input name="sysInfo.national" type="text" id="national" size="16"  maxlength="16" value='<s:property value="sysInfo.national"/>' readonly="readonly"></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF" >

					  <td bgcolor="#999999" align="right">政治面貌</td>
					  <td><input name="sysInfo.politicalstatus" type="text" id="politicalstatus" size="16"  maxlength="16" value='<s:property value="sysInfo.politicalstatus"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">籍贯</td>
					  <td><input name="sysInfo.birthplace" type="text" id="birthplace" size="16"
											maxlength="16"
									 value='<s:property value="sysInfo.birthplace"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">外语水平</td>
					  <td><input name="sysInfo.englishskill" type="text" id="englishskill" size="16"
											maxlength="16"
									 value='<s:property value="sysInfo.englishskill"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">技术职称</td>
					  <td><input name="sysInfo.technicaltitle" type="text" id="technicaltitle" size="16"
											maxlength="16"
									 value='<s:property value="sysInfo.technicaltitle"/>' readonly="readonly"></td>
					  </tr>
					    <tr bgcolor="#FFFFFF" >

					  <td bgcolor="#999999" align="right">参加工作日期</td>
					  <td ><input   name="sysInfo.startworddtae" type="text" id="startworddtae"   size="16"
											maxlength="16"  value='<s:date name="sysInfo.startworddtae" format="yyyy-MM-dd"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">手机号码</td>
					  <td ><input name="sysInfo.mobile"  size="16" type="text" id="mobile"   value='<s:property value="sysInfo.mobile"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">户口性质</td>
					  <td ><input name="sysInfo.hukoukind"  size="16" type="text" id="hukoukind"   value='<s:property value="sysInfo.hukoukind"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">婚姻状况</td>
					  <td><input name="sysInfo.marrystatus"  size="16" type="text" id="marrystatus"   value='<s:property value="sysInfo.marrystatus"/>' readonly="readonly"></td>
					  </tr>
					  <tr bgcolor="#FFFFFF" >
					 
					  <td bgcolor="#999999" align="right">毕业院校</td>
					  <td colspan="3"><input name="sysInfo.graduateschool"  size="50" type="text" id="graduateschool"  isSel="true" value='<s:property value="sysInfo.graduateschool"/>' readonly="readonly">                      </td>
                      <td bgcolor="#999999" align="right">毕业日期</td>
					  <td ><input   name="sysInfo.graduatedate" type="text" id="graduatedate" size="16"
											maxlength="16"   value='<s:date name="sysInfo.graduatedate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">所学专业</td>
					  <td ><input name="sysInfo.studysubject" type="text" id="studysubject" size="16"
											maxlength="16"  value='<s:property value="sysInfo.studysubject"/>' readonly="readonly"></td>
				      </tr>
                        <tr bgcolor="#FFFFFF">
					  <td  rowspan="3"  align="center" bgcolor="#999999"><div align="center">在职信息<font color="#FF0000">
					  </font></div> </td>
					  <td  bgcolor="#999999" align="right">入职日期</td>
					  <td ><input name="sysInfo.workingdate" type="text" id="workingdate" size="16"
											maxlength="16"  value='<s:date name="sysInfo.workingdate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
					  <td  bgcolor="#999999" align="right">组别</td>
					  <td ><input name="sysInfo.groupname" type="text" id="groupname" size="16"
											maxlength="16"  value='<s:property value="sysInfo.groupname"/>' readonly="readonly"></td>
			  
                      <td  bgcolor="#999999" align="right">岗位</td>
					  <td><input name="sysInfo.workstation" type="text" id="workstation" size="16"
											maxlength="16"  value='<s:property value="sysInfo.workstation"/>' readonly="readonly"></td>
					   <td  bgcolor="#999999" align="right"> 办公电话</td>
					  <td><input name="sysInfo.tel"  size="16" type="text" id="tel"   value='<s:property value="sysInfo.tel"/>' readonly="readonly"></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999" align="right">状态</td>
					  <td><input name="sysInfo.status"  size="16" type="text" id="status"   value='<s:property value="sysInfo.status"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">离职日期</td>
					  <td><input name="sysInfo.leavedate" type="text" id="leavedate" size="16"
											maxlength="16" 
					  value='<s:date name="sysInfo.leavedate" format="yyyy-MM-dd"/>' readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">离职原因</td>
					  <td  colspan="3"><input name="sysInfo.leavereason"  size="50" type="text" id="leavereason"   value='<s:property value="sysInfo.leavereason"/>' readonly="readonly">
					  </td>
                      </tr>
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">备注</td>
					  <td  colspan="7">
					  <textarea cols="88"  name="sysInfo.remark" id="remark" readonly><s:property value="sysInfo.remark"/></textarea>					 </td>
					  </tr>
                       <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" rowspan="4" align="center"  ><div align="center">其它信息<font color="#FF0000">
					  </font></div> </td>
                     

					  <td bgcolor="#999999" align="right">外部邮箱</td>
					  <td><input name="sysInfo.email"  size="16" type="text" id="email"   value='<s:property value="sysInfo.email"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">QQ</td>
					  <td><input name="sysInfo.qq"  size="16" type="text" id="qq"   value='<s:property value="sysInfo.qq"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">个人爱好</td>
					  <td  colspan="3"><input name="sysInfo.like"  size="50" type="text" id="like"   value='<s:property value="sysInfo.like"/>'  readonly="readonly"></td>
                      </tr>
                       <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">户口所在地</td>
					  <td colspan="5"><input name="sysInfo.hukouadress" size="84" type="text" id="hukouadress"  value='<s:property value="sysInfo.hukouadress"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right"> 邮政编码</td>
					  <td><input name="sysInfo.hukounum" type="text" id="hukounum" size="16"
											maxlength="16" value='<s:property value="sysInfo.hukounum"/>'  readonly="readonly"></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">
					  <td bgcolor="#999999" align="right">家庭地址</td>
					  <td colspan="5"><input name="sysInfo.address" size="84" type="text" id="address"   value='<s:property value="sysInfo.address"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right"> 邮政编码</td>
					  <td><input name="sysInfo.addressnum" type="text" id="addressnum" size="16"
											maxlength="16" value='<s:property value="sysInfo.addressnum"/>'  readonly="readonly"></td>
					  </tr>
					  
					  <tr bgcolor="#FFFFFF">

					  <td bgcolor="#999999" align="right">紧急联系人</td>
					  <td><input name="sysInfo.contactsman"  type="text" id="contactsman" size="16"
											maxlength="16"  value='<s:property value="sysInfo.contactsman"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">关系</td>
					  <td><input name="sysInfo.relation" type="text" id="relation" size="16"
											maxlength="16" value='<s:property value="sysInfo.relation"/>'  readonly="readonly"></td>
					  <td bgcolor="#999999" align="right">联系人电话</td>
					  <td ><input  name="sysInfo.contactscallnum" type="text" id="contactscallnum" size="16"
											maxlength="16" value='<s:property value="sysInfo.contactscallnum"/>'  readonly="readonly"></td>
                      <td bgcolor="#999999" align="right">家庭电话</td>
					  <td ><input  name="sysInfo.contactstelnum" type="text" id="contactstelnum"  size="16"
											maxlength="16" value='<s:property value="sysInfo.contactstelnum"/>'  readonly="readonly"></td>
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
