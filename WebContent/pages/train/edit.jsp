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
	var addressName=new Array("沈阳","北京","大连","抚顺","天津","上海");
	var addressHelpCode=new Array("sy","bj","dl","fs","tj","sh");
	var getSelectHelpCode=document.getElementsByName("train.courseName");

	function init(){
		var yy=false;
		with(getSelectHelpCode[0]){
			length=addressName.length;
			for(var i=0;i<length;i++){
			if(options[i].value == document.getElementById("CnTmp").value){		
				yy = true;	
			}}
		}
		
		if(yy = false){
		 $("#train.courseName").append("<option value='").append(document.getElementById("CnTmp").value).append("'>").append(document.getElementById("CnTmp").value).append("</option>");

		}
	}

	function writeSelect(obj){
		obj.options[obj.selectedIndex].text=obj.options[obj.selectedIndex].text + String.fromCharCode(event.keyCode);
		obj.options[obj.selectedIndex].value = obj.options[obj.selectedIndex].text;
		event.returnValue=false;
	}
	
	function catch_keydown(sel)
	{
		switch(event.keyCode)
		{
			case 46:
				//delete
				sel.options[sel.selectedIndex].text ="";
				sel.options[sel.selectedIndex].value = sel.options[sel.selectedIndex].text;
				event.returnValue = false;
				break;
			case 8:
				//Back Space;
				var s = sel.options[sel.selectedIndex].text;
				sel.options[sel.selectedIndex].text = s.substr(0,s.length-1);
				sel.options[sel.selectedIndex].value = sel.options[sel.selectedIndex].text;
				event.returnValue = false;
				break;
			case 13:
				event.returnValue = false;
				break;
		}
		
	}

	function vertiyt(obj){
	    var yy=false;
		var jj=-1;
		with(obj){
			for(var i=0;i<addressName.length;i++){
				var selectedValue=options[selectedIndex].value;
				var selectedText=options[selectedIndex].text;
				if(addressName[i]==selectedText||addressHelpCode[i]==selectedText){
					yy=true;
					jj=i;
				}
			}
		}
		if(yy==true){
			
			obj.options[jj].selected=true;
		}else{

		}
	}
	
	


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
			if(document.getElementById("start").value.trim()=="")
			{
				alert("请输入培训时间");
				return;
			}	
			if(document.getElementById("end").value.trim()=="")
			{
				alert("请输入培训时间");
				return;
			}
			 
			window.returnValue=true;
			reportInfoForm.submit();

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
	function selectPeople(see,hidden){
		var studentids=document.getElementById("studentids").value;
		var strUrl="/pages/train/traininfo!select.action?studentids="+studentids+"&see="+see+"&hidden="+hidden;
		var feature="520,380,notidy.addTitle,notify";
	 	var returnValue=OpenModal(strUrl,feature);
	 	if(returnValue!=null && returnValue!=""){
		 	var values = returnValue.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name;
		 	document.getElementById(hidden).value = id; 
	 	}
	}
	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 500) {
					alert("你输入的字数超过500个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 500);
				}
			}
		}
	}

	function listenKey() {
		if (document.addEventListener) {
			document.addEventListener("keyup", getKey, false);
		} else if (document.attachEvent) {
			document.attachEvent("onkeyup", getKey);
		} else {
			document.onkeyup = getKey;
		}
	}
	listenKey();
	
	function setSelectPeopleValue(idList,see,hidden){
		if(idList!=null && idList!=""){
		 	var values = idList.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name;
		 	document.getElementById(hidden).value = id;
	 	}
	}
	</script>
      <body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/train/traininfo!update.action"   method="post">
    	<input type="hidden" name="train.id"  value="<s:property value='train.id'/>">
<table width="90%" align="center" cellPadding="0" cellSpacing="0">
	</table>
<br/>
<table width="90%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    
                    <table width="95%" class="input_table">
                    <tr>
                    	<td class="input_tablehead"><s:text name="培训课程" /></td>
                    </tr>
                   <tr>
                    <td width="13%" class="input_label2"><div align="center">课程名称<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" nowrap="nowrap" width="28%">
                                     <select name="train.courseName" style="width:163px;" id="courseName" onBlur="vertiyt(this)" onkeydown="catch_keydown(this)" onkeypress="writeSelect(this)">
                    <option value=''>---</option> 
                    <s:iterator value="#request.courses" status="stuts" id="test">                         
                    <option value='<s:property value="#test.courseName"/>'><s:property value="#test.courseName"/></option>
                    </s:iterator>    
                     <option value='<s:property value="train.courseName"/>'><s:property value="train.courseName"/></option>
                     </select>
                     <input type="hidden" name="CnTmp" id="CnTmp" value="<s:property value='train.courseName'/>">
                     <script language="javascript">
	       			 document.getElementById("courseName").value=document.getElementById("CnTmp").value;
	       			 </script>
                     
                    </td>
                     <td  width="13%" class="input_label2"><div align="center">分类<font color="#FF0000">*</font></div></td>
                   <td width="16%" bgcolor="#FFFFFF">
					 <s:select list="{'内训','外训','外派'}" name="train.category" value="train.category" theme="simple"   cssStyle="width:90px;"
                         headerKey="" headerValue="----"  id="category"></s:select> </td> 
                     <td  width="10%" class="input_label2"><div align="center">讲师<font color="#FF0000">*</font></div></td>
                     <td width="23%" bgcolor="#FFFFFF"><input name="train.teacher" type="text" id="teacher" size="25" maxlength="20" value='<s:property value="train.teacher"/>'></td> 
                  </tr>
                  <tr> 
                   <td  width="13%" class="input_label2"><div align="center">培训日期<font color="#FF0000"></font><font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="train.trainDate" type="text" id="trainDate" size="18" maxlength="100" 
                    class="MyInput" value="<s:date name='train.trainDate' format='yyyy-MM-dd'/>">
                   </td>
                     <td  width="13%" class="input_label2"><div align="center">培训时长<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="train.trainHour" onkeyup="this.value=this.value.replace(/[^\d\.]/g,'')" onkeyDown="this.value=this.value.replace(/[^\d\.]/g,'')" type="text" id="trainHour" size="12" maxlength="3" value='<s:property value="train.trainHour"/>'>  <div align="center"></div></td>
                    <td class="input_label2"><div align="center">地点<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF"><input name="train.addr" type="text" id="addr" size="25" maxlength="150" value="<s:property value='train.addr'/>"></td>
                  </tr>
                  <tr>
                    <td  class="input_label2">培训时间<font color="#FF0000">*</font></td>
                  <td bgcolor="#FFFFFF">
                        <input name="train.start" type="text" id="start" size="8" maxlength="100"  
                        class="timeInput"  value='<s:property value="train.start"/>'>--<input name="train.end" type="text" id="end" size="8" 
                        maxlength="100"  class="timeInput"  value='<s:property value="train.end"/>'>
                    </td>
                    <td class="input_label2"><div align="center">参训人员<font color="#FF0000">*</font></div></td>
                    <td bgcolor="#FFFFFF" colspan="3"><textarea name="train.student" id="student" cols="35" rows="3" style="word-break:break-all;word-wrap:break-word;width:85%;"><s:property value='train.student'/></textarea>
    <input type="hidden" name="train.studentids" id="studentids" size="35" value="<s:property value='train.studentids'/>">
    	<input type="button" value="选择" id="zhusong" onClick="selectPeople('student','studentids')"/></td>
                  </tr>
                  	<input type="hidden" name="t11" id="t11" value="<s:property value='train.student'/>">
                  	<script>
						var t11=document.getElementById("t11");
						//document.getElementById("studentTxt").value=t11.value;
						document.getElementById("student").value=t11.value;
						var aa=t11.value.split(",");
						var bb=document.getElementsByName("studentChk");
						for(var j=1;j<bb.length;j++){
							for(var i=0;i<aa.length;i++){
								if(aa[i]==bb[j].value){
									bb[j].checked=true;
							    }
							}	
						}
						
                  	</script>
                  <tr>
                  	  <td  width="13%" class="input_label2"><div align="center">备注</div></td> 
					<td colspan="5" bgcolor="#FFFFFF">
                  	  <textarea name="train.note" cols="77" rows="3" style="word-break:break-all;word-wrap:break-word;width:100%;"><s:property value='train.note'/></textarea>                    </td>
                  	  
                  </tr>
                  <tr>
                  <td colspan="6" bgcolor="#FFFFFF">
                   <input type="checkbox" name="anon_flag" id="anon_flag" value="1"/>跨部门<input type='hidden' id="bound" name="train.bound" value="<s:property value='train.bound'/>"/></td>
                   </tr>
                   <script>
                   if(document.getElementById("bound").value == "跨部门")
						{
							document.getElementById("anon_flag").checked = true;
						}
						else
						{
							document.getElementById("anon_flag").checked = false;
						}
						</script>
                    </table>
                </td> 
            </tr> 
    </table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok"  value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
			
 	</form>
</body>  
</html> 