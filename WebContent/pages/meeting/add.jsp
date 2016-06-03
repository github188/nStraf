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
			
			 if(document.getElementById("meeting.currentDateTime").value=="")
			{
				alert("请输入会议时间");
				return;
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
			if(! Validate(document.getElementById("hour").value, "Double"))
			{
				document.getElementById("hour").value = 0;
				alert("请在会议时长处只输入数字");
				return
			}
			else{
				tmp = parseFloat(document.getElementById("hour").value);
				if(tmp<0 || tmp>30){
					document.getElementById("hour").value = 0;
					alert("会议时长输入有误，请重新输入！");
					return
				}			
			}
			document.getElementById("hour").value = document.getElementById("hour").value + "h";
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
			document.getElementById("ok").disabled = true;
			window.returnValue=true;
			reportInfoForm.submit();

		}
	
	function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		   var Currency = /^\d+(\.\d+)?$/;
		   var Number= /^\d+$/;
		   var Zip = /^[1-9]\d{5}$/;
		   var QQ = /^[1-9]\d{4,8}$/;
		   var Integer = /^[-\+]?\d+$/;
		   var integer = /^[+]?\d+$/;
		   var Double= /^[-\+]?\d+(\.\d+)?$/;
		   var double = /^[+]?\d+(\.\d+)?$/;
		   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		   var Chinese = /^[\u0391-\uFFE5]+$/;
		   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		
			//var itemNameValue=document.getElementsByName(itemName)[0].value
			var itemNameValue=itemName;
			
				var flag;
			switch(pattern){ 
			 case "Require":
				 flag = Require.test(itemNameValue);
				  break;
			 case "Email":
				 flag = Email.test(itemNameValue);
				  break;
			 case "Phone":
				 flag = Phone.test(itemNameValue);
				  break;
			 case "Mobile":
				 flag = Mobile.test(itemNameValue);
				  break;
			 case "Url":
				 flag = Url.test(itemNameValue);
				  break;
			 case "IdCard":
				 flag = IdCard.test(itemNameValue);
				  break;
			 case "Currency":
				 flag = Currency.test(itemNameValue);
				  break;
			 case "Number":
				 flag = Number.test(itemNameValue);
					  break;
			 case "Zip":
				 flag = Zip.test(itemNameValue);
				  break;
			 case "QQ":
				 flag = QQ.test(itemNameValue);
				  break;
			 case "integer":
				 flag = integer.test(itemNameValue);
				  break;		  
			 case "Integer":
			// if (itemNameValue.length>0)
				 flag = Integer.test(itemNameValue);
			//else 
			//	flag=true
				  break;
			 case "Double":
				 flag = Double.test(itemNameValue);
				  break;
			 case "double":
					 flag = double.test(itemNameValue);
					  break;
			 case "English":
				 flag = English.test(itemNameValue);
				  break;
			 case "Chinese":
				 flag = Chinese.test(itemNameValue);
				  break;
			 case "BankCard":
				 flag = BankCard.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag
		
		}
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
		function refu2(){
		   if(!confirm('您确认周报生成到会议纪要内容里吗？'))
		   {
				return;				
		   }
			document.getElementById("content").value =  document.getElementById("contenttmpjsp").value;
			document.getElementById("subject").value = "软件测试部周例会 ";
			document.getElementById("content").focus();
		    document.getElementById("bodyid").focus();
			return;	
	}
	</script>
<title>新增页面</title>
</head>

<body id="bodyid">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/meeting/meetinginfo!save.action"   method="post">
    <input type="hidden" name="contenttmpjsp" id="contenttmpjsp" value="<s:property value='content_tmp'/>">
<table width="990" height="232" border="1">
  <tr>
    <td width="95" bgcolor="#999999"><div align="right">主题<font color="#FF0000">*</font></div></td>
    <td colspan="7">
      <label></label>   <input name="meeting.subject" type="text" id="subject" size="110" /></td>
  </tr>
 
  <tr>
    
    <td width="95" bgcolor="#999999"><div align="right" >会议时间<font color="#FF0000">*</font></div></td>
    <td width="200">
<label>
<!--
   		     <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="15" />
   	-->
  <input name="meeting.currentDateTime" type="text" id="currentDateTime" size="25" maxlength="100" class="MyInput" issel="true" isdate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" >
    	</label>    </td>
    
    <td width="70" bgcolor="#999999"><div align="right" >会议时长<font color="#FF0000">*</font></div></td>
    <td width="116">
<label>
   		     <input name="meeting.hour" type="text" id="hour" size="10" />
   		</label>    
小时</td>
  
   
    
    <td width="55" bgcolor="#999999"><div align="right">地点<font color="#FF0000">*</font></div></td>
    <td width="90">
<label>
        <input name="meeting.addr" type="text" id="addr" size="20" />
    </label>    </td>
    <td width="55" bgcolor="#999999"><div align="right">主持人<font color="#FF0000">*</font></div></td>
    <td width="94">
<label>
       <!--
        <input name="meeting.compere" type="text" id="compere" size="12" />
        -->
        <s:select  list="unames" name="meeting.compere" theme="simple"  cssStyle="width:100px;" headerKey="" headerValue="----" id="compere"></s:select>
    </label>    </td>
  </tr>

  
  <tr>
    <td bgcolor="#999999"><div align="right">出席人员</div></td>
    <td colspan="7"><tm:dmselect name="attendPersons" dicId="umap" scope="request" separator="," width="890" ></tm:dmselect></td>
     <!-- <textarea name="meeting.attendPersons" id="attendPersons" cols="100" rows="2"></textarea> -->
   
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">缺席人员</div></td>
    <td colspan="7"><tm:dmselect name="absentPersons" dicId="umaps" scope="request" separator="," width="890" ></tm:dmselect></td>
    <!--<input name="meeting.absentPersons" type="text" id="absentPersons" size="110" />-->
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">会议内容</div></td>
    <td colspan="7"><textarea name="meeting.content" id="content" cols="110" rows="30"></textarea></td>
  </tr>
  
  	<!--<div id="d1"><s:property value="content_tmp"/></div>
    <script language="javascript">document.getElementById('content').appendChild(document.getElementById('d1'))</script>-->
  
  <tr>
    <td bgcolor="#999999"><div align="right">主送</div></td>
    <td colspan="7"><tm:dmselect name="main" dicId="umap" scope="request" separator="," width="890" ></tm:dmselect></td>
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
    <!--<textarea name="meeting.main" id="main" cols="100" rows="2"></textarea>-->
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right">抄送</div></td>
    <td colspan="7"><tm:dmselect name="copy" dicId="umap" scope="request" separator="," width="890" ></tm:dmselect></td>
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
    <td colspan="3"><label>
    <!--
      <input name="meeting.reAudit" type="text" id="reAudit" size="14" />
      -->
       <tm:dmselect name="reAudit" dicId="umap" scope="request" separator="," width="300" ></tm:dmselect>
      </label>    </td>
    
    <td  bgcolor="#999999">签发</td>
    <td >
    <s:select  list="unames" name="meeting.sign" theme="simple"  cssStyle="width:100px;" headerKey="" headerValue="----" id="sign" value="audit"></s:select>
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
        </label>    <input name="meeting.auditStatus" type="text" id="auditStatus" size="20"  value="未经审核通过" readonly/></td>
   
   
  </tr>
  
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
                <input type="button" name="refu" value="部门周例会" class="MyButton"  onclick="refu2();" image="../../images/share/refu.gif">
				</td> 
  		</tr>  
 	</table> 
    </form>
</body>
</html>
