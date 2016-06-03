<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.lang.Integer"%>
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
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script language="javascript" src="../../plugin/pcasun/pcasunzip.js" charset="utf-8"></script>
<script type="text/javascript">
		$(function(){
			//省市级联控件
			new PCAS("project.province","project.city","广东省","广州市");
			
			listenCaldarInput();
			
			$("#selectManager").click(function(){
				var projectId="";
				var strUrl="/pages/project/project!findAllUser.action?projectId="+projectId;
				var feature="520,380,grpInfo.updateTitle,um";
			 	var returnValue=OpenModal(strUrl,feature);
			 	if(returnValue!=null && returnValue!=""){
				 	var values = returnValue.split(":");
				 	document.getElementById("proManagerId").value = values[0];
				 	document.getElementById("proManager").value = values[1]; 
			 	}
			});
			//监听考勤地址输入框改变重新定位
			monitorAttendanceAddress();
			//查看地图
			$("input[name='viewMap']").click(function(){
				var address = $(this).parent().find("input[name='attendanceAddressList']").val();
				var url = "http://api.map.baidu.com/geocoder?address="+address+"&output=html&src=nStraf";
				window.open(url);
			});
		});
		
		//监听JqueryUI的Calendar控件
		function listenCaldarInput(){
			
			$(".CalendarInput").datepicker({  
		        dateFormat:'yy-mm-dd',  //更改时间显示模式  
		        changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
		        changeYear:true        //是否显示年份的下拉菜单，默认为false  
		     });
			$("input[typeGroup='CalendarHourInput']").timepicker({
				currentText: '现在',
				closeText: '关闭',
				hour: 8,
				minute: 0,
				stepMinute: 10,
				timeFormat: 'HH:mm:ss',
				timeOnlyTitle: '时间选择',
				timeText: '时间',
				hourText: '时',
				minuteText: '钟',
				secondText:'秒'
		     });
		}
	
		/* function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？')){
				return;				
			}
		 	window.close();
		} */
		
		//监听考勤地址输入框改变重新定位
		function monitorAttendanceAddress(){
			 //当输入地址改变时
			 $("input[name='attendanceAddressList']").change(function(){
				 var ele = this;
				var address = $(ele).val();
				if($.trim(address)!=""){
					var actionUrl = "/nStraf/pages/project/project!getLatitudeAndLongitude.action?address="+address;
					actionUrl = encodeURI(actionUrl);
					$.ajax({
						url : actionUrl,
						data : {},
						type : 'POST',
						dataType : 'json',
						timeout : 30000,
						success : function(data) {
							var lng = data.longitude;
					        var lat = data.latitude;
					        if(lng!=0&&lat!=0){
					        	$(ele).next().next()[0].innerHTML = lat.toFixed(2)+"&nbsp";//纬度
					        	$(ele).next().next().next().next()[0].innerHTML = lng.toFixed(2)+"&nbsp";//经度
					           }
					        else{
					        	 alert('没有找到对应的经纬度，确认地址是否正确或联系管理员!');
							     $(ele).next().next()[0].innerHTML = "0.0&nbsp";
							     $(ele).next().next().next().next()[0].innerHTML = "0.0&nbsp";
					           }
						},
						error : function(){
							alert('获取经纬度失败,联系管理员！');
						}
					});
				}
				else{
					$(ele).next().next()[0].innerHTML = "0.0&nbsp";
					$(ele).next().next().next().next()[0].innerHTML = "0.0&nbsp";
				}
				
			});
		}
		
		function save(){
			var name = document.getElementById("names").value;
		    if(name==""){
				 alert("项目名称必填");
				 document.getElementById("names").focus();
				 return false;
			 }
			 if(document.getElementById("proManagerId").value==""){
				 alert("项目负责人必填");
				 return false;
			 }
			if(document.getElementById("planStartTime").value==""){
				 alert("计划开始时间必填");
				 return false;
			 }
			if(document.getElementById("planEndTime").value==""){
				 alert("计划结束必填");
				 return false;
			 } 
			// //如果选择是移动签到则签到地点不能为空
			var flag = checkAttendanceType();
			 if(flag){
			    	alert("移动签到类型,考勤地点不能为空或请输入正确地址!");
			    	return false;
			    }
			//document.getElementById("ok").disabled=true;
			window.returnValue=true;
			projectForm.submit();
		}	
		// //如果选择是移动签到则签到地点不能为空
		function checkAttendanceType(){
			var flag = false;
			var a = $("select[name = 'attendanceTypeList']");
		    $.each(a,function(index,data){
		    	if(data.value == "mobileSign" ){
		    		var str = $("input[name = 'attendanceAddressList']")[index].value
		    		str.replace(/\s+/g,"");//去空格
		    		if(navigator.userAgent.indexOf("MSIE 8.0")>0){//兼容IE8
			    		if(str =="" || ($("label[name = 'longitude']")[index].innerText*1 ==0 && $("label[name = 'latitude']")[index].innerText*1 == 0)){
			    			flag = true;
			    			return false;
			    		}
		    		}
		    		else{
		    			if(str =="" || ($("label[name = 'longitude']")[index].textContent*1 ==0 && $("label[name = 'latitude']")[index].textContent*1 == 0)){
			    			flag = true;
			    			return false;
			    		}
		    		}
		    	}
		    });
		    return flag;
		}
		
		function addAgain(){
			/* //拷贝第一个table加入最后面
			var attTable=$("#attendanceContainer").children("table").eq(0);
			var cloneTable=attTable.clone(); */
			
			$("#attendanceContainer").children("table:last").after(getSignPlaceTable());
			
			//重新对checkbox的序号编号
			renameCheckboxSpan();
			listenCaldarInput();
			$("input[name='attendanceAddressList']").unbind("change"); 
			monitorAttendanceAddress();//监听考勤地址输入框改变重新定位
		}
		function delAgain(){
			//被选中的checkbox
			var checked=0;
			var size=$("div[name='attendanceCheckBoxDiv']").find("input[type='checkbox']").size();
			$("div[name='attendanceCheckBoxDiv']").find("input[type='checkbox']").each(function(){
				 if($(this).attr('checked')==true||$(this).prop('checked')==true){
				 	checked++;
				 }
			});
			if(checked==0){
				return;
			}
			if(checked==size){
				alert("至少保留一个考勤地点");
				return;
			}
			$("div[name='attendanceCheckBoxDiv']").find("input[type='checkbox']").each(function(){
				 if($(this).attr('checked')==true||$(this).prop('checked')==true){
					 $(this).parent().parent().parent().parent().remove();
				 }
			});
			//重新对checkbox的序号编号
			renameCheckboxSpan();
			listenCaldarInput();

		}
		
		function getSignPlaceTable(){
			var html='<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">';
			html+='<tr>';
			html+='	<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv"><input type="checkbox" /><span>1</span></div></td>';
			html+='	<td bgcolor="#E6E6E6" >';
			html+='		<table width="100%" align="center" cellPadding="1" cellSpacing="1">';
			html+='			<tr>';
			html+='				<td bgcolor="#FFFFFF" style="width:13%">考勤时间(进入):</td>';
			html+='				<td bgcolor="#FFFFFF" style="width:21%">';
			html+='					<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" name="attendanceEntryTimeList" style="width:90%" value="08:00:00" />';
			html+='				</td>';
			html+='				<td bgcolor="#FFFFFF" style="width:13%">考勤时间(离开):</td>';
			html+='				<td bgcolor="#FFFFFF" style="width:21%">';
			html+='					<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" name="attendanceExitTimeList" style="width:90%"  value="17:00:00"/>';
			html+='				</td>';
			html+='				<td bgcolor="#FFFFFF" style="width:8%">考勤类型:</td>';
			html+='				<td bgcolor="#FFFFFF" bgcolor="#FFFFFF" style="width:23%">';
			html+='					<select name="attendanceTypeList" style="width:90%">';
			
			<s:iterator value="#request.attendanceType" id="ele">
			html+='<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>';
			</s:iterator>
			
			html+='					</select>';
			html+='				</td>';
			html+='			</tr>';
			html+='			<tr>';
			html+='				<td bgcolor="#FFFFFF">考勤地点:</td>';
			html+='				<td bgcolor="#FFFFFF" colspan="5">';
			html+='					<input type="text" name="attendanceAddressList" maxlength="60" size="70%"/>';
			html+='				    <label>&nbsp;纬度:</label><label name= "latitude">0&nbsp;</label> <label>经度:</label><label name = "longitude">0&nbsp;</label>';
			html+='					<input name="viewMap" type="button" value="查看地图">';
			html+='				</td>';
			html+='			</tr>';
			html+='		</table>';
			html+='	</td>';
			html+='</tr>';
			html+='</table>';
			return html;
		}
		function renameCheckboxSpan(){
			var i=0;
			$("div[name='attendanceCheckBoxDiv']").find("span").each(function(){
				i++;
				$(this).html(i);
			});
		}
		
		function setSelectPeopleValue(idList,see,hidden){
			if(idList!=null && idList!=""){
				var values = idList.split(":");
				document.getElementById("proManagerId").value = values[0];
			 	document.getElementById("proManager").value = values[1];
		 	}
		}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
	<form name="projectForm"
		action="<%=request.getContextPath()%>/pages/project/project!add.action"
		method="post">
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">项目基本信息</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目名称<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<input type="text" name="project.name" maxlength="100" size="100" id="names" />
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目所在地<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<select style="width:30%" name="project.province"></select> &nbsp;&nbsp;<select style="width:30%" name="project.city"></select>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									是否完结
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%" >
								<select id="projectIsEndSelect" name="project.isEnd" style="width:90%">
									<s:iterator value="#request.projectIsEndType" var="isEnd">
										<option value="<s:property value='#isEnd.key'/>"><s:property value="#isEnd.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目类型<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<select name="project.projectType" style="width:90%">
									<s:iterator value="#request.projectType" id="ele">
										<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
									</s:iterator>
								</select>
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									项目负责人<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%" colspan="1">
								<input type="hidden" name="project.proManagerId" id="proManagerId" />
								<input type="text" readonly="readonly" name="project.proManager" id="proManager" style="width:30%"/>
								<input id="selectManager" type="button" value="选择">
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									是否虚拟项目
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<select style="width:90%" name="project.isVisual">
									<option value="0">否</option>
									<option value="1">是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td width="8%" class="input_label2">
								<div align="center">
									计划开始时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input id="planStartTime" readonly="readonly" class="CalendarInput" type="text" name="project.planStartTime" style="width:90%" />
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									计划结束时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input id="planEndTime" readonly="readonly" class="CalendarInput" type="text" name="project.planEndTime" style="width:90%"/>
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									项目编号
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input type="text" name="project.projectNo" style="width:90%" />
							</td>
						</tr>
						<tr>
							
							<td width="8%" class="input_label2">
								<div align="center">
									实际开始时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input id="factStartTime" readonly="readonly" class="CalendarInput" type="text" name="project.factStartTime"  style="width:90%">
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									实际结束时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<input id="factEndTime" readonly="readonly" class="CalendarInput" type="text" name="project.factEndTime" style="width:90%" />
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									客户名称
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="25%">
								<select name="project.customer" style="width:90%">
									<s:iterator value="#request.customerType" var="customer">
										<option value="<s:property value='#customer.key'/>"><s:property value="#customer.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									备注<font color="#FF0000"></font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<div>
									<textarea name="project.description" style="width: 98%" id="description" rows="3" id="note"></textarea>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="input_table" cellPadding="0" cellSpacing="0" >
						<tr>
							<td class="input_tablehead">考勤地点维护</td>
						</tr>
						<tr bgcolor="#E6E6E6">
							<td id="attendanceContainer">
								<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv"><input type="checkbox" /><span>1</span></div></td>
										<td bgcolor="#E6E6E6" >
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(进入):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" name="attendanceEntryTimeList" style="width:90%" value="08:00:00" />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(离开):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" name="attendanceExitTimeList" style="width:90%"  value="17:00:00"/>
													</td>
													<td bgcolor="#FFFFFF" style="width:8%">考勤类型:</td>
													<td bgcolor="#FFFFFF" bgcolor="#FFFFFF" style="width:23%">
														<select name="attendanceTypeList" style="width:90%">
															<s:iterator value="#request.attendanceType" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF">考勤地点:</td>
													<td bgcolor="#FFFFFF" colspan="5">
														<input type="text" name="attendanceAddressList" maxlength="60" size="70%"/>
														<label >&nbsp;纬度:</label><label name = "latitude">0&nbsp;</label> <label>经度:</label><label name = "longitude">0&nbsp;</label>
														<input name="viewMap" type="button" value="查看地图">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" align="center" cellPadding="1" cellSpacing="1">
									<tr>
				                        <td></td>
				                        <td width="6%"><input type="button" value="新增" onClick="addAgain()"/></td>
				                        <td width="6%"><input type="button" value="删除" onClick="delAgain()"/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br /> <br />
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center"><input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
