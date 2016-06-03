<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="cn.grgbanking.feeltm.util.Constants" %>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%
	UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
	String username = userModel.getUserid();
	String groupname = userModel.getGroupName();
	String deptname = userModel.getDeptName();
%>
<html>
<head></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript">
	function save() {
		var no=document.getElementById("no").value.trim();
		var type=document.getElementById("type").value.trim();
		var name = document.getElementById("name").value.trim();
		var status=document.getElementById("status").value;
		var use=document.getElementById("use").value;
		var indate=document.getElementById("indate").value;
		var inman=document.getElementById("inman").value;
		var useman=document.getElementById("useman").value;
		var usedate=document.getElementById("usedate").value;
		var expectdate=document.getElementById("expectdate").value;
		var factdate=document.getElementById("factdate").value;
		var usereason=document.getElementById("usereason").value;
		if (no == "") {
			alert('资产编号不能为空');
			return;
		}
		if (type == "") {
			alert('资产类型不能为空');
			return;
		}
		if(name==""){
			alert("资产名称不能为空");
			return;
		}
		if(status==""){
			alert("状态不能为空");
			return;
		}
		if (use == "") {
			alert('用途不能为空');
			return;
		}
		if(inman==""){
			alert("资产管理员不能为空");
			return;
		}
		if(status=="1"){
			var flag = false;
			if(useman==""){
				alert("领用人不能为空");
				flag = true;
			}else if(usedate==""){
				alert("领用日期不能为空");
				flag = true;
			}else if(expectdate==""){
				alert("预计归还日期不能为空");
				flag = true;
			}else if(usereason==""){
				alert("领用原因不能为空");
				flag = true;
			}
			if(flag){
				return;
			}
		}
		/*if(status=="2"){
			var flag=false;
			if(factdate==""){
				alert("归还日期不能为空");
				flag=true;
			}
			if(flag){
				return;
			}
		}*/
		var selectid="";
		var url = "<%=request.getContextPath()%>/pages/fixasset/fixassetinfo!isValid.action";
		$.ajax({
			url:url,
			data:{
				no:no,
				id:selectid
			},
			success:function(data){
				if(data.returnMsg=="true"){
					alert("已经存在该固定资产编号");
				}else{
					var selectid="";
					var url = "<%=request.getContextPath()%>/pages/fixasset/fixassetinfo!flagDateCompare.action";
					$.ajax({
						url:url,
						data:{
							status:status,
							id:selectid,
							indate:indate,
							usedate:usedate,
							factdate:factdate,
							expectdate:expectdate
						},
						success:function(data){
							if(data.returnMsg!=""){
								alert(data.returnMsg);
							}else{
								window.returnValue = true;
								reportInfoForm.submit();
							}
						}
					});
					
				}
			}
		});
		
	}
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}

	function getKey(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode != 27 || keycode != 9) {
			for (var i = 0; i < document.getElementsByTagName("textarea").length; i++) {
				var tmpStr = document.getElementsByTagName("textarea")[i].value
						.trim();
				if (tmpStr.length > 250) {
					alert("你输入的字数超过250个了");
					document.getElementsByTagName("textarea")[i].value = tmpStr
							.substr(0, 250);
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
	function init(){
		
	}
	
	function selectMainPeople(see,hidden){
		var strUrl="/pages/fixasset/fixassetinfo!select.action?see="+see+"&hidden="+hidden;
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
		 	document.getElementById(see).value = name.substring(0, name.indexOf(",", 0));
		 	document.getElementById(hidden).value = id.substring(0, id.indexOf(",", 0));
	 	}
	}
	var useman="";
	var usedate="";
	var expectdate="";
	var usereason="";
	function c_status(){
		document.getElementById("span1").innerHTML="";
		document.getElementById("span2").innerHTML="";
		document.getElementById("span3").innerHTML="";
		document.getElementById("span4").innerHTML="";
		document.getElementById("span5").innerHTML="";
		document.getElementById("fixuse").disabled=false;
		document.getElementById("usedate").disabled=false;
		document.getElementById("expectdate").disabled=false;
		document.getElementById("usereason").disabled=false;
		document.getElementById("factdate").disabled=false;
		useman = useman==""?document.getElementById("useman").value:useman;
		usedate = usedate==""?document.getElementById("usedate").value:usedate;
		expectdate = expectdate==""?document.getElementById("expectdate").value:expectdate;
		usereason = usereason==""?document.getElementById("usereason").value:usereason;
		var status=document.getElementById("status").value;
		if(status=="0"){
			document.getElementById("useman").value="";
			document.getElementById("usedate").value="";
			document.getElementById("expectdate").value="";
			document.getElementById("factdate").value="";
			document.getElementById("usereason").value="";
			document.getElementById("fixuse").disabled=true;
			document.getElementById("usedate").disabled=true;
			document.getElementById("expectdate").disabled=true;
			document.getElementById("usereason").disabled=true;
			document.getElementById("factdate").disabled=true;
		}
		if(status=="1"){
			document.getElementById("useman").value=useman;
			document.getElementById("usedate").value=usedate;
			document.getElementById("expectdate").value=expectdate;
			document.getElementById("factdate").value="";
			document.getElementById("usereason").value=usereason;
			document.getElementById("factdate").disabled=true;
			document.getElementById("span1").innerHTML="<font color='#FF0000'>*</font>";
			document.getElementById("span2").innerHTML="<font color='#FF0000'>*</font>";
			document.getElementById("span3").innerHTML="<font color='#FF0000'>*</font>";
			document.getElementById("span5").innerHTML="<font color='#FF0000'>*</font>";
		}
		if(status=="2"){
			document.getElementById("span1").innerHTML="";
			document.getElementById("span2").innerHTML="";
			document.getElementById("span3").innerHTML="";
			document.getElementById("span4").innerHTML="";
			document.getElementById("span5").innerHTML="";
			document.getElementById("useman").value="";
			document.getElementById("usedate").value="";
			document.getElementById("expectdate").value="";
			document.getElementById("factdate").value="";
			document.getElementById("usereason").value="";
			document.getElementById("fixuse").disabled=true;
			document.getElementById("usedate").disabled=true;
			document.getElementById("expectdate").disabled=true;
			document.getElementById("usereason").disabled=true;
			document.getElementById("factdate").disabled=true;
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
		 	document.getElementById(see).value = name.substring(0, name.indexOf(",", 0));
		 	document.getElementById(hidden).value = id.substring(0, id.indexOf(",", 0));
	 	}
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10" onload="init()">
	<form name="reportInfoForm"
		action="<%=request.getContextPath()%>/pages/fixasset/fixassetinfo!save.action"
		method="post">
		<table width="90%" align="center" cellPadding="1" cellSpacing="1">
			<tr>
				<td>
						
						<table width="90%" class="input_table">
							<tr>
								<td class="input_tablehead"> 固定资产登记 </td>
							</tr>
							<tr>
								<td width="15%" class="input_label2"><font
									color="#000000">资产编号</font><font color="#FF0000">*</font></td>
								<td bgcolor="#ffffff" width="35%"><input
									name="fixAsset.no" type="text" id="no"
									style="width: 100%;" /></td>
								<td  width="15%" class="input_label2"><font
									color="#000000">资产类型</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<select name="fixAsset.type" id="type" style="width:100%">
										<option value=""></option>
										<option value="0">PC</option>
										<option value="1">笔记本</option>
										<option value="2">服务器</option>
										<option value="3">路由</option>
										<option value="4">键盘</option>
										<option value="5">鼠标</option>
									</select>
								</td>
							</tr>
							<tr>
								<td  width="15%"class="input_label2"><font
									color="#000000">资产名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%"><input
									name="fixAsset.name" type="text" id="name"
									style="width: 100%;" /></td>
								<td class="input_label2" width="15%"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<select name="fixAsset.status" id="status" style="width:100%" onchange="c_status();">
										<option value=""></option>
										<option value="0">闲置</option>
										<option value="1">领用</option>
										<option value="2">无效</option>
									</select>
								</td>
							</tr>
							<tr>
								<td height="44"  width="15%" class="input_label2">用途</font><font
									color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.use" type="text" id="use" rows="3" 
										cols="80"></textarea></td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">入库日期</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.indate" type="text" id="indate" readonly=true
											size="22" class="MyInput" />
									</div>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">资产管理员</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<input name="fixAsset.inman" type="text" id="inman" style="width: 75%;" readonly="readonly"/>
									<input name="inmanid" type="hidden" id="inmanid" style="width: 75%;" />
									<input type="button" value="选择" id="fixadmin" onclick="selectMainPeople('inman','inmanid')"/>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">领用人</font><span id="span1"></span></td>
								<td bgcolor="#FFFFFF" width="35%">
									 <input name="fixAsset.useman" type="text" id="useman" style="width: 75%;" readonly="readonly"/>
									 <input name="fixAsset.usemanid" type="hidden" id="usemanid" style="width: 75%;" />
									<input type="button" value="选择" id="fixuse" onclick="selectMainPeople('useman','usemanid')"/>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">领用日期</font><span id="span2"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.usedate" type="text" id="usedate" readonly="readonly"
											size="22" class="MyInput" />
									</div>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">预计归还日期</font><span id="span3"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.expectdate" type="text" id="expectdate" readonly="readonly"
											size="22" class="MyInput" />
									</div>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">归还日期</font><span id="span4"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.factdate" type="text" id="factdate" readonly="readonly"
											size="22" class="MyInput" />
									</div>
								</td>
							</tr>
							<tr>
								<td height="44" class="input_label2">领用原因</font><span id="span5"></span></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.usereason" type="text" id="usereason" rows="3"
										cols="80"></textarea></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
		<br />
		<div style="color: #FF0000; margin-left: 20px" id="showInfo"></div>
		<table width="90%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="okButton"
					value='<s:text name="grpInfo.ok" />' class="MyButton"
					onclick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	</form>
</body>
</html>
