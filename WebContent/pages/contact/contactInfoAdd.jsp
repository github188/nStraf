<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script language="javascript">

var deptname = "";
var username = "";
var groupname = "";
var userHeadKey ="";
var userHeadValue="";

function validateInfo(){
if (confirm('<s:text  name="confirm.modify.commit"/>')){
        validateUserid();
        var note = $("#note").val();
        if(note!=null){
        	if(note.length>100){
        		alert("备注超过长度100");
        	}
        }
        if($("#userId").val()=="全选"){
        	alert("请输入用户姓名");
        	return false;
        }
     return true;
	}
 }

	
function formsubmit(){
	if (validateInfo()){
        window.returnValue=true;
        var newAnOther=contactForm.newAnOther.checked;
        contactForm.action="<%=request.getContextPath() %>/pages/contact/contactAction!save.action?newAnOther="+newAnOther;
        contactForm.submit();
	}
}
	
function validateUserid(){
	contactForm.userId.value=contactForm.userId.value.trim();
	var user = contactForm.userId.value;
	if(user == userHeadValue){
		alert('<s:text  name="roleInfo.check.username"/>');
		   contactForm.userId.focus();
	}
	}
	
	 /* var listUsr ;
	
	function search()
	{
		var url="contactAction!queryNameId.action";
		var params={deptName:$("#deptName").val().trim(),groupName:$("#groupName").val().trim()};
		jQuery.post(url, params, $(document).callbackFun, 'json');
	}
	$.fn.callbackFun=function (json)
	 {		
		listUsr = json.jsonObj;
		$("#userId option").remove();
		  $.each(listUsr,function(entry,entryInfo){
				//entry 序列号相当于Id  ， entryInfo 相当于数组名
			  $("#userId").append("<option value='"+entryInfo["userid"]+"'>"+entryInfo["username"]+"</option>");
		  });
		  $("#userName").val(listUsr[listUsr.length-1].userid);
		  $("#conName").val(listUsr[listUsr.length-1].username);
	 }
	
	function selected(o){
		var s = "#userId option:eq("+o.selectedIndex+")";
		$("#userName").val($(s).val());
		$("#conName").val($(s).text());
	} */ 
	
	function searchByDept(deptId,userId,userKey,userValue,deptHeadvalue){
			deptname = deptId;
			username = userId;
			userHeadKey = userKey;
			userHeadValue= userValue;
			var url="contactAction!queryNameId.action";
			var params={deptName:$("#"+deptId).val().trim(),deptValue:deptHeadvalue};
			jQuery.post(url, params, $(document).callname, 'json');
			
		}
	
		$.fn.callname=function (json){
			$("#"+username+" option").remove();
		
			if(userHeadKey!="null"||userHeadKey!=undefined){
				$("#"+username+"").append("<option value='"+userHeadValue+"'>"+userHeadKey+"</option>");
			}
			
			$.each(json,function(entry,entryInfo){
				//entry 序列号相当于i  ， entryInfo 相当于数组名
			  $("#"+username).append("<option value='"+entryInfo["userid"]+"'>"+entryInfo["username"]+"("+entryInfo["userid"]+")</option>");
		  });
			document.getElementById(username).focus();
			document.getElementById(username).value = userHeadValue;
		} 
		
		function searchByName(userId,groupId){
			groupname = groupId;
			username = userId;
			var url="<%=request.getContextPath()%>/pages/um/sysUserGroup!getGroupNameByUserName.action";
			var params={userid:$("#"+userId).val().trim()};
			jQuery.post(url, params, $(document).callgroup, 'json');
			
		}
		$.fn.callgroup=function (json){
			var values = "";
			 if(json!=null&&json.length>0){
				for(var i=0;i<json.length;i++){
					values +=json[i].grpname+",";
				}
			} 
			 $("#"+groupname).val(values);
			 $("#"+groupname+"label").text(values);
			 $("#conName").val($("#"+username+" :selected").text());
		}
	
</script>

<body id="bodyid"  leftmargin="0" topmargin="10" >

<form name="contactForm" action="/pages/contact/contactAction!save.action" method="post">
	<input type="hidden" name="update" value="<%=DateUtil.to_char(new Date(),"yyyy-MM-dd")%>" id="update"/>
								
	<table width="90%" align="center" cellPadding="0" cellSpacing="0" class="popnewdialog1">
   	 <tr>
  		<td> 
    		<fieldset class="jui_fieldset" width="100%">
        	<!--  	<legend><s:text name="form.selectName"/></legend>  -->
        	<legend><s:text name="label.addContact"/></legend>
        		<table width="90%" align="center" border="0" cellspacing="1"
								cellpadding="1" style="border-bottom: none"  bgcolor="#583F70">
        		
        			<tr bgcolor="#FFFFFF"> 
        			<tm:deptSelect deptId="deptName" groupId="groupName" userId="userId" 
        				deptHeadKey='--请选择部门名--' userHeadKey='--请选择姓名--' deptHeadValue="--请选择部门名--" userHeadValue="全选"
        				isloadName="false" type="add"
        				labelUser="姓名<font color='#FF0000'>*</font>" labelDept="部门<font color='#FF0000'>*</font>"
        				deptLabelClass="align:center;" groupLabelClass="align:center;" userLabelClass="align:center;"
        				lableStyle="width:10%;background-color:#999999;align:center;" selectStyle="width:20%;background-color:#FFFFFF;"></tm:deptSelect>
            		</tr>
            			<s:hidden name="conName" id="conName"></s:hidden>
            			
            			<%-- <td width="30%" align="right"> 
            				<div align="right"><s:text name="queryCondition.org"/>所在部门</div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/>
            			</td>
            			<td width="65%" align="left">
           				  <s:select list="#request.deptMap" listKey="value" listValue="value" style="width:50%" 
           				  		headerKey="全选" headerValue="--请选择部门名--" onchange="search()"
           				  		 id="deptName" name="deptName"></s:select>
			 			</td>
          			</tr>
          			<tr> 
            			<td width="30%" align="right"> 
            				<div align="right">所属组别</div>
            			</td>
            			<td width="5%" align="left"><s:text name="label.colon"/>
            			</td>
            			<td width="65%" align="left">
			 				<s:select list="#request.groupList" listKey="grpname" listValue="grpname" style="width:50%" 
			 					headerKey="全选" headerValue="--请选择组别名--" onchange="search()"
			 					id="groupName" name="groupName"></s:select>
			 			</td>
          			</tr>
        			
          			 <tr> 
           				 <td width="30%" align="right"> 
           				 	<div align="right">姓    名</div>
           				 </td>
           				 <td width="5%"  align="left"><s:text name="label.colon"/></td>
           				 <td width="65%"  align="left"> 
           				<s:select list="#request.list"  listKey="userid" listValue="username" headerKey="" headerValue="-请选择姓名-"
           						name="userId" id="userId" style="width:50%"  onchange="selected(this)"></s:select>
             				<font color="#FF0000">*</font> 
              			 </td>
          			</tr>
        		    <tr> 
           				<td width="30%" align="right"> 
           					<div align="right">用户标识</div>
           				</td>
            			<td width="5%"  align="left"><s:text name="label.colon"/></td>
            			<td width="65%"  align="left"> 
            			<input name="userName" type="text" id="userName" size="30" maxlength="20" readonly="readonly">
            			<s:hidden name="conName" id="conName"></s:hidden>
              				<font color="#FF0000">*</font> 
              			</td>--%>
         		    <tr> 
            			<td style="width:10%;background-color:#999999;align:center;"> <div align="center"><s:text name="form.Tel" /><s:text name="label.colon"/></div></td>
            			<td bgcolor="#FFFFFF" width="20%"> 
            				<s:textfield name="conTel" id="conTel" size="30" maxlength="15" value=""  onkeyup="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"/> 
              			</td>
            			<td style="width:10%;background-color:#999999;align:center;"> <div align="center"><s:text name="form.Mobile" /><s:text name="label.colon"/></div></td>
            			<td  bgcolor="#FFFFFF" width="20%"> 
            				<s:textfield name="conMobile" id="conMobile" size="30" maxlength="11" value="" onkeyup="this.value=this.value.replace(/\D/g,'')" onKeyDown="this.value=this.value.replace(/\D/g,'')"/> 
              			</td>
              			<td  bgcolor="#FFFFFF" ></td><td bgcolor="#FFFFFF" ></td>
          			</tr>
          			<tr>
          				 <td style="width:10%;background-color:#999999;align:center;"> <div align="center"><s:text name="form.note" /><s:text name="label.colon"/></div></td>
            			<td  colspan="5" bgcolor="#FFFFFF"> 
            				<s:textarea name="note" id="note"   cols="60" rows="2" />
              			</td> 
              			
              		</tr>
        	</table>
           </fieldset>
       </td>
    </tr>
    </table>
    <br />
    <table width="90%" align="center" cellPadding="0" cellSpacing="0">
    <tr>  
       <td>
             <fieldset  width="100%" >
                  <legend><s:text name="label.tips.title"/></legend>
                  <table width="100%" >
                      <tr>
                          <td><s:text name="label.admin.content"/></td>
                      </tr>
                  </table>
              </fieldset>
          </td>  
  </tr>
  
  <tr>
     <td align="center"> <br> 
        <input type="button" name="ok"  value='<s:text name="grpInfo.ok"/>' class="MyButton"  onclick="formsubmit();" image="../../images/share/yes1.gif"> 
        <input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
         <input type="checkbox" name="newAnOther" id="newAnOther" value="1" >
        <label for="newAnOther"><s:text name="label.addAnOther"/></label>         
      </td>    
  </tr>
</table>
</form>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>
