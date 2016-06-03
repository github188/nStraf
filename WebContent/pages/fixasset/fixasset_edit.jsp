<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
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
    <script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
	<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var clickFlag = false;
		var oldStartTime, oldEndTime;
		var submitOK = true;
		
		function save(){
			/*document.getElementById("fixuse").disabled=false;
			document.getElementById("usedate").disabled=false;
			document.getElementById("expectdate").disabled=false;
			document.getElementById("usereason").disabled=false;
			document.getElementById("factdate").disabled=false;*/
			var no=document.getElementById("no").value.trim();
			var type=document.getElementById("type").value.trim();
			var name = document.getElementById("name").value.trim();
			var status=document.getElementById("status").value;
			var use=document.getElementById("use").value;
			var indate=document.getElementById("indate").value;
			var inman=document.getElementById("inman").value;
			var useman=document.getElementById("usemantext").value;
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
			var before_status='<s:property value="fixAsset.status"/>';
			if(status=="0" && before_status=="1"){
				var flag=false;
				if(factdate==""){
					alert("归还日期不能为空");
					flag=true;
				}
				if(flag){
					return;
				}
			}
			var selectid=document.getElementById("id").value;
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
		
		String.prototype.trim = function(){
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		 
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode != 27 || keycode!=9){ 
				//for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
				//{
					var tmpStr = document.getElementById("usereason").value.trim();
					if(tmpStr.length > 250)
					{
						alert("你输入的字数超过250个了");
						document.getElementById("usereason").value = tmpStr.substr(0,250);
					}
				//}
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
		
		function init(){
			var type='<s:property value="fixAsset.type"/>';
			var status='<s:property value="fixAsset.status"/>';
			document.getElementById("type").value=type;
			document.getElementById("status").value=status;
			/*
			document.getElementById("usemantext").value="";
			document.getElementById("usedate").value="";
			document.getElementById("expectdate").value="";
			document.getElementById("factdate").value="";
			document.getElementById("usereason").value="";*/
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
			 	//document.getElementById("usemantext").value=name.substring(0, name.indexOf(",", 0));
			 	//document.getElementById("usemanid").value=id.substring(0, id.indexOf(",", 0));
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
			useman = useman==""?document.getElementById("usemantext").value:useman;
			usedate = usedate==""?document.getElementById("usedate").value:usedate;
			expectdate = expectdate==""?document.getElementById("expectdate").value:expectdate;
			usereason = usereason==""?document.getElementById("usereason").value:usereason;
			var status=document.getElementById("status").value;
			var before_status='<s:property value="fixAsset.status"/>';
			if(status=="0" && before_status!="1"){
				document.getElementById("usemantext").value="";
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
			if(status=="0" && before_status=="1"){
				document.getElementById("usemantext").value="";
				document.getElementById("usedate").value="";
				document.getElementById("expectdate").value="";
				document.getElementById("factdate").value="";
				document.getElementById("usereason").value="";
				document.getElementById("fixuse").disabled=true;
				document.getElementById("usedate").disabled=true;
				document.getElementById("expectdate").disabled=true;
				document.getElementById("usereason").disabled=true;
				document.getElementById("span4").innerHTML="<font color='#FF0000'>*</font>";
			}
			if(status=="1"){
				document.getElementById("usemantext").value=useman;
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
				document.getElementById("usemantext").value="";
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
<body id="bodyid"  leftmargin="0" topmargin="10" onload="init();c_status();">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/fixasset/fixassetinfo!update.action" method="post">
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
									style="width: 100%;" value='<s:property value="fixAsset.no"/>'/></td>
								<td  width="15%" class="input_label2"><font
									color="#000000">资产类型</font><font color="#FF0000">*</font></td>
								<td class="input_label2" width="35%">
									<select name="fixAsset.type" id="type" style="width:100%">
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
								<td  width="15%" class="input_label2"><font
									color="#000000">资产名称</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%"><input
									name="fixAsset.name" type="text" id="name"
									style="width: 100%;" value='<s:property value="fixAsset.name"/>'/></td>
								<td class="input_label2" width="15%"><font
									color="#000000">状态</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<select name="fixAsset.status" id="status" style="width:100%" onchange="c_status();">
										<option value="0">闲置</option>
										<option value="1">领用</option>
										<option value="2">无效</option>
									</select>
								</td>
							</tr>
							<tr>
								<td height="44" class="input_label2" width="15%">用途</font><font
									color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.use" type="text" id="use" rows="3"
										cols="80"><s:property value="fixAsset.use"/></textarea></td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">入库日期</font><font color="#FF0000">*</font></td>
								<td  width="35%" class="input_label2"><div align="left">
										<input name="fixAsset.indate" type="text" id="indate" readonly=true
											size="22" class="MyInput" 
											value='<s:date name="fixAsset.indate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
								<td  width="15%" class="input_label2"><font
									color="#000000">资产管理员</font><font color="#FF0000">*</font></td>
								<td bgcolor="#FFFFFF" width="35%">
									<input name="fixAsset.inman" type="text" id="inman" value='<s:property value="fixAsset.inman"/>' style="width: 75%;" />
									<input name="inmanid" type="hidden" id="inmanid" style="width: 75%;" />
									<input type="button" value="选择" id="fixadmin" onclick="selectMainPeople('inman','inmanid')"/>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">领用人</font><span id="span1"></span></td>
								<td bgcolor="#FFFFFF" width="35%">
									 <input name="fixAsset.useman" type="text" id="usemantext" value='<s:property value="fixAsset.useman"/>' style="width: 75%;" readonly/>
									 <input name="fixAsset.usemanid" type="hidden" id="usemanid" style="width: 75%;" value='<s:property value="fixAsset.usemanid"/>'/>
									 <input type="button" value="选择" id="fixuse" onclick="selectMainPeople('usemantext','usemanid')"/>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">领用日期</font><span id="span2"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.usedate" type="text" id="usedate" readonly="readonly"
											size="22" class="MyInput" 
											value='<s:date name="fixAsset.usedate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
							</tr>
							<tr>
								<td class="input_label2" width="15%"><font
									color="#000000">预计归还日期</font><span id="span3"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.expectdate" type="text" id="expectdate" readonly="readonly"
											size="22" class="MyInput" 
											value='<s:date name="fixAsset.expectdate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
								<td class="input_label2" width="15%"><font
									color="#000000">归还日期</font><span id="span4"></span></td>
								<td bgcolor="#FFFFFF" width="35%"><div align="left">
										<input name="fixAsset.factdate" type="text" id="factdate" readonly="readonly"
											size="22" class="MyInput" 
											value='<s:date name="fixAsset.factdate" format="yyyy-MM-dd"/>'>
									</div>
								</td>
							</tr>
							<tr>
								<td height="44" class="input_label2">领用原因</font><span id="span5"></span></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="fixAsset.usereason" type="text" id="usereason" rows="3"
										cols="80"><s:property value='fixAsset.usereason'/></textarea></td>
							</tr>
							<tr>
								<td height="44" class="input_label2">领用记录</font></td>
								<td bgcolor="#FFFFFF" colspan="3"><textarea wrap="virtual"
										name="uselog" type="text" id="uselog" rows="6"
										cols="80" readonly="readonly"><s:property value='#request.record'/></textarea></td>
								<input type="hidden" id="id" name="fixAsset.id" value="<s:property value='fixAsset.id'/>"
							</tr>
						</table>
				</td>
			</tr>
		</table>
<br/>
<div style="color:#FF0000; margin-left:20px" id="showInfo"></div>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="okButton"  value='<s:text name="grpInfo.ok" />' class="MyButton"  onclick="save()"  image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
 	</form>
</body>  
</html> 