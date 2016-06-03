<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
    <script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var clickFlag = false;
		var oldStartTime, oldEndTime;
		var submitOK = true;
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
			var ret = false;
			$("input[name*='examScore']").each(function(i){
				var ind = i + 1;
				if(this.value.trim()=="")
				{
					alert("第"+ind+"行的考核分数不能为空");
					this.focus();
					ret = false;
					return false;
				}
				if(!numVali(this.value.trim()))
				{
					this.focus();
					ret = false;
					return false;
				}
				if(parseFloat(this.value.trim())>200)
				{
					alert("第"+ind+"行的考核分数不能大于200");
					this.focus();
					ret = false;
					return false;
				}
				ret = true;
			});
			if(ret)
			{
				window.returnValue=true;
				reportInfoForm.submit();
			}
		}
		
		function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^(http|ftp|svn):\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		   var Currency = /^\d+(\.\d+)?$/;
		   var Number= /^\d+$/;
		   var Number2= /^(-)?\d+(\.\d+)?$/;
		   var Zip = /^[1-9]\d{5}$/;
		   var QQ = /^[1-9]\d{4,8}$/;
		   var Integer = /^[-\+]?\d+$/;
		   var integer = /^[+]?\d+$/;
		   var Double= /^[-\+]?\d+(\.\d+)?$/;
		   var double = /^[+]?\d+(\.\d+)?$/;
		   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		   var Chinese = /^[\u0391-\uFFE5]+$/;
		   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		   var ip=/^(((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))$/;
		//var ip = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)((d|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
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
			 case "Number2":
				 flag = Number2.test(itemNameValue);
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
			case "ip":
				 flag = ip.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
  	var thisDate = document.getElementById("startDate").value.trim();
	var endDate = document.getElementById("endDate").value.trim();
	    if(thisDate.length==0)
		{
			alert('请输入开始日期!');
			return false;
		}
		if(endDate.length==0)
		{
			alert('请输入结束日期!');
			return false;
		}
		if(thisDate.length>0&&endDate.length>0){
		var v = re.test(endDate);
		var a = re1.test(thisDate);
		if(!v||!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 
		 if(!DateValidate('start','end'))
		 {
		 	alert('开始日期大于结束日期，请重新输入！');
		 	return false;
		 }
	 }else if(thisDate.length>0&&endDate.length==0){
	     
		  var a = re1.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }else if(thisDate.length==0&&endDate.length>0){
	      var v = re.test(endDate);
		if(!v){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }
	  return true;
	 }
	 
	 function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementById("startDate").value.trim();
    var end=document.getElementById("endDate").value.trim();
	var flag=true;

    /*if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;*/
	if(Require.test(begin) && Require.test(end))
	{
		var beginStr = begin.split("-");
		var endStr = end.split("-");
		if(parseInt(beginStr[0]) > parseInt(endStr[0]))
		{
			flag = false;
		}
		else if(parseInt(beginStr[0]) == parseInt(endStr[0]))
		{
			if(parseInt(beginStr[1]) > parseInt(endStr[1]))
			{
				flag = false;
			}
			else if(parseInt(beginStr[1]) == parseInt(endStr[1]))
			{
				if(parseInt(beginStr[2]) > parseInt(endStr[2]))
				{
					flag = false;
				}
			}
		}
	}
   return flag;

}
		
		function validateInputStart(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;    
  	var thisDate = document.getElementById("startDate").value.trim();
		if(thisDate.length>0)
		{
			var a = re.test(thisDate);
			if(!a)
			{
				alert('日期格式不正确,请使用日期选择!');
				return false;
			}
			return true;
		}
		else if(thisDate.length==0)
		{
		  	alert('不输入日期，还加班啊！');
		  	return false;
		}
	}
		 
		 function validateInputStartMonth(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g;    
  	var thisDate = document.getElementById("startDate").value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
		 
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				{
					var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
					if(tmpStr.length > 250)
					{
						alert("你输入的字数超过250个了");
						document.getElementsByTagName("textarea")[i].value = tmpStr.substr(0,250);
					}
				}
			} 
		} 
		
		function listenKey(){ 
			if (document.addEventListener) { 
				document.addEventListener("keyup",getKey,false); 
			} else if (document.attachEvent) { 
				document.attachEvent("onkeyup",getKey); 
			} else { 
				document.onkeyup = getKey; 
			} 
		} 
		listenKey();
		
		function search()
		{
			var url="overtimeinfo!queryNames.action";
			var params={groupName:$("#groupName").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {	
		 	$("#username option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				//for(var i=json.length-1;i>=0;i--)
				{
					    $("#username").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
			}
		 }
		 
		 function numClear(itemName){
			itemName.select();
		}
		
		function numVali(itemName) {
			if(! Validate(itemName, "Currency"))
			{
				alert("考核分数请输入正数值");
				return false;
			}
			//itemName.value = parseFloat(itemName.value);
			return true;
		}
		
		function numClear2(itemName){
			var val = Math.round(parseFloat(itemName.value)*100)/100;
			if(val<0)
			{
				val = -val;
			}
			itemName.value = val;
			itemName.select();
		}
		
		function numVali2(itemName) {
			if(! Validate(itemName.value, "Currency"))
			{
				itemName.value = 0;
				alert("请输入数字");
				return;
			}
		}
		
		function numVali2Blur(itemName)
		{
			var val = Math.round(parseFloat(itemName.value)*100)/100;
			if(val>0)
			{
				val = -val;
			}
			itemName.value = val;
			if(! Validate(itemName.value, "Number2"))
			{
				itemName.value = 0;
				alert("请输入数字");
				return;
			}
		}
		
		function scoreHdl(id){
			if(!numVali($("#examScore"+id).val()))
			{
				return ;
			}
			var score = parseFloat($("#examScore"+id).val());
			var rank = "B";
			if(!isNaN(score))
			{
				score = Math.abs(score);
				$("#examScore"+id).val(score);
				if(score>100 && score<=200)
				{
					rank = "A";
				}
				else if(score>90 && score<=100)
				{
					rank = "B";
				}
				else if(score>80 && score<=90)
				{
					rank = "C";
				}
				else if(score>80 && score<=90)
				{
					rank = "D";
				}
				else if(score>0 && score<=80)
				{
					rank = "E";
				}
				else
				{
					alert("请输入0-200分值");
					return;
				}
				$("#examRank"+id).val(rank);
			}
			else
			{
				alert("请输入0-200分值");
			}
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
    <s:form name="reportInfoForm"  namespace="/pages/monthMission" action="monthMissioninfo!updatebat.action" method="post" >
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="90%" style="margin-left:10px">
                    <legend>
                    月度任务
                    </legend>
                     <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
                        <tr> 
                            <td nowrap width="1%" class="oracolumncenterheader">序号</td>
                            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">姓名</div></td>
                            <td nowrap width="15%" class="oracolumncenterheader"><div align="center">组别</div></td>
                            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">月度考核分</div></td>
                            <td nowrap width="5%" class="oracolumncenterheader"><div align="center">考核等级</div></td>
                            <td nowrap width="40%" class="oracolumncenterheader"><div align="center">备注</div></td>
                        </tr>
                        <tbody name="formlist" id="formlist" align="center"> 
                        <s:iterator value="monthMissionModList" id="tranInfo" status="row">
                            <s:if test="#row.odd == true"> 
                            	<tr id="tr" class="trClass0" oriClass="" >
                            </s:if><s:else>
                            	<tr id="tr" class="trClass1" oriClass="" >
                            </s:else> 
                            <td nowrap align="center"><div><s:property value="#row.count"/></div><input type="hidden" name="id" value='<s:property value="id"/>'/><input type="hidden" name="monthDate" value='<s:property value="monthDate"/>'/></td>
                            <td><input name="currentName" type="text" id='currentName' readonly value='<s:property value="currentName"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none;color:#000000;text-align: center;vertical-align: middle;background-position: center center; width:50px;" /></td>
                            <td><input name="groupName" type="text" id='groupName' readonly value='<s:property value="groupName"/>' style="text-align: center;vertical-align: middle;background-position: center center;background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none;color:#000000;"/></td>
                            <td><div align="left"><input name="examScore" type="text" id='examScore<s:property value="#row.count"/>' value='<s:property value="examScore"/>' maxlength="5" onBlur="scoreHdl('<s:property value="#row.count"/>')" style="color:#FF0000;width:50px;"></div></td>
                            <td><div align="left"><input name="examRank" type="text" id='examRank<s:property value="#row.count"/>' readonly value='<s:property value="examRank"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000;width:20px;"></div></td>
                            <td id="tdReason" align="left"><textarea name="note" type="text" id="note" rows="3" cols="30" ><s:property value="note"/></textarea></td>
                        </tr>
                        </s:iterator>  
                        </tbody> 
                    </table> 
                 </fieldset>
                </td> 
            </tr> 
    </table>
 
<br/>
	<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>  
           <td>
                 <fieldset width="100%" >
                      <legend><s:text name="label.tips.title"/></legend>
                      <table width="100%" >
                          <tr>
                            <td><s:text name="label.admin.content"/></td>
                          </tr>
                      </table>
                  </fieldset>
      		<br/>
              </td>  
      	</tr>
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="okButton"  value='<s:text name="grpInfo.ok" />' class="MyButton"  onclick="save()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 	
</s:form>
</body>  
</html> 