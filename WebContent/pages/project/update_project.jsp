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
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>  --%><%-- 加载<fmt:formatNumber>标签,数字格式化   --%> 
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil,cn.grgbanking.feeltm.project.domain.Project"%>
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
		function toViewResourcePlan(){
			var strUrl='/pages/project/project!viewDistribution.action?projectId=<s:property value="#request.project.id"/>';
				var returnValue=OpenModal(strUrl,"1000,800,back.title.del,org"); 
		}
		$(function(){
			//省市级联控件
			new PCAS("project.province","project.city",'<%=((Project)request.getAttribute("project")).getProvince()%>','<%=((Project)request.getAttribute("project")).getCity()%>');
			
			
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
			
			$("select[name='projectRoleList']").each(function(){
				var role=$(this).prev().val();
				$(this).find("option").each(function(){
					if($(this).val()==role){
						$(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
			});
			
			$("select[name='projectDutyList']").each(function(){
				var role=$(this).prev().val();
				$(this).find("option").each(function(){
					if($(this).val()==role){
						$(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
			});
			
			//项目类型下拉列表的默认值设置
			$("#projectTypeSelect").find("option").each(function(){
				if($(this).val()=='<%=((Project)request.getAttribute("project")).getProjectType()%>'){
					$(this).attr("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
			//是否完结下拉列表的默认值设置
			$("#projectIsEndSelect").find("option").each(function(){
				if($(this).val()=='<%=((Project)request.getAttribute("project")).getIsEnd()%>'){
					$(this).attr("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
			//客户名称下拉列表的默认值设置
			$("#customerTypeSelect").find("option").each(function(){
				if($(this).val()=='<%=((Project)request.getAttribute("project")).getCustomer()%>'){
					$(this).attr("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
			//监控考勤类型下拉列表默认设置
			$("select[name='attendanceTypeList']").each(function(){
				var hiddenValue=$(this).prev().val();
				$(this).find("option").each(function(){
					if($(this).val()==hiddenValue){
						$(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
				
			})
			
			//查看地图
			$("input[name='viewMap']").click(function(){
				var address = $(this).parent().find("input[name='attendanceAddressList']").val();
				var url = "http://api.map.baidu.com/geocoder?address="+address+"&output=html&src=nStraf";
				window.open(url);
			});
			
			//setTimeout("changezuobiao()",30);
			//坐标保留两位小数
			changeCoordinate();
			 /* $("input[name='attendanceAddressList']").focus(function(){
				 var ele =this;
				    $(ele).css("background-color","#FFFFCC");
				  }); */
			//监听考勤地址输入框改变重新定位
			monitorAttendanceAddress();
			
		});
		
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
							var lat = data.latitude;
							var lng = data.longitude;
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
		
		
		
		
		//坐标保留两位小数
		function changeCoordinate(){
			for(var i = 0;i<$("label[name = 'latitude']").length;i++){
				var lat = $("label[name = 'latitude']")[i].innerHTML;
				lat = lat*1;//转数字类型
		        var lng = $("label[name = 'longitude']")[i].innerHTML;
		        lng = lng*1;//转数字类型
		        if(lng!=0&&lat!=0){
		        	$("label[name = 'latitude']")[i].innerHTML = lat.toFixed(2)+"&nbsp";
		        	$("label[name = 'longitude']")[i].innerHTML = lng.toFixed(2)+"&nbsp";
		           }
		        else{
		        	$("label[name = 'latitude']")[i].innerHTML = "0.0&nbsp";
		        	 $("label[name = 'longitude']")[i].innerHTML = "0.0&nbsp";
		        }
		        
			}
		}
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
		function selUser(ele) {		
			var indexvalue = $(ele).parent().parent().parent().parent().parent().parent().find('span[id="indexvalue"]').html();
			var userids=encodeURI($(ele).prev().prev().val());
			var usernames=encodeURI($(ele).prev().val());
			var projectId = $("#projectId").val();
			//alert(userids+"." +usernames +"." + projectId );
			var strUrl = "project!getAllUserByProject.action?projectId="+ projectId+"&userids="+userids+"&usernames="+usernames+"&indexvalue="+indexvalue;
			var feature = "520,380,grpInfo.updateTitle,um";
			var iTop = (window.screen.availHeight - 20 - 380) / 2;
		    var iLeft = (window.screen.availWidth - 10 - 520) / 2;
		    var returnValue=OpenModal(strUrl, feature);
	        //上面的OpenModal在IE中正常,但是Firefox中跳转到selectuser JSP中点击保存无效,所以用下面投机取巧方法,直接指定action,在struts配置文件中加了/命名空间
		 	//var returnValue = window.showModalDialog(strUrl,window,"dialogWidth="+520+"px;dialogHeight="+380+"px;dialogTop="+iTop+"px;dialogLeft="+iLeft+"px;resizable=yes;scroll=no;center=yes;help=no;status=no;toolbar=yes");
	        if (returnValue != null && returnValue != "") {
				var values = returnValue.split(":");
				userids="";
				usernames="";
				if(values.length>0){
					for(var j=0;j<values.length;j++){
						var idname = values[j].split(",");
						userids += ","+idname[0];
						usernames += ","+idname[1];
					}
				}
				userids= userids.substring(1);
				usernames = usernames.substring(1);
				$(ele).prev().val(usernames);
				$(ele).prev().prev().val(userids);
			}else if(returnValue==""){
				$(ele).prev().val("");
				$(ele).prev().prev().val("");
			}
		}
		/* function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？')){
				return;				
			}
		 	window.close();
		} */
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
			 var name=document.getElementsByName("useridList");
			 for(var i=0;i<name.length;i++) { 
				 if(name[i].value=="") {
					 alert("选择人员不能为空!");
					 return false;
				 }
			 }
			 var factStarttime=document.getElementsByName("factStartTimeList");
			 for(var i=0;i<factStarttime.length;i++) { 
				 if(factStarttime[i].value=="") {
					 alert("实际开始时间不能为空!");
					 return false;
				 }
			 }
			 var factEndtime=document.getElementsByName("factEndTimeList");
			 var count = 0;
			 for(var i=0;i<factEndtime.length;i++) { 
				 if(factEndtime[i].value=="") {
					 count++;
				 }
			 }
			 if(count>0){
				 alert("实际退出时间为空时，系统默认置为9999-12-31");
				 for(var i=0;i<factEndtime.length;i++) { 
					 if(factEndtime[i].value=="") {
						 factEndtime[i].value="9999-12-31";
					 }
				 }
			 }
		 	//document.getElementById("ok").disabled=true;
		 	var flag = checkAttendanceType();
			 if(flag){
			    	alert("移动签到类型,考勤地点不能为空或请输入正确地址!");
			    	return false;
			    }
			 
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
			
			if($("#attendanceContainer").children().size()<=0){
				$("#attendanceContainer").append(getSignPlaceTable());
			}else{
				$("#attendanceContainer").children("table:last").after(getSignPlaceTable());
			}
			
			
			//重新对checkbox的序号编号
			renameCheckboxSpan();
			listenCaldarInput($("#attendanceContainer").children("table:last"));
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
			html+='				<label>&nbsp;纬度:</label><label name = "latitude">0&nbsp;</label> <label>经度:</label><label name = "longitude">0&nbsp;</label>';
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
			
/**--------------------------------人员分配-------------------------------------------**/
 	function addAgain2(){
		if($("#attendanceContainer2").children("table").size()==0){
			$("#attendanceContaine2r").children("table:last").after(getPlanResourceTable());
		}else{
			$("#attendanceContainer2").children("table:last").after(getPlanResourceTable());
		}
		
		//重新对checkbox的序号编号
		renameCheckboxSpan2();
		listenCaldarInput($("#attendanceContainer2").children("table:last"));
	}
	function delAgain2(){
		//被选中的checkbox
		var checked=0;
		$("div[name='attendanceCheckBoxDiv2']").find("input[type='checkbox']").each(function(){
			 if($(this).attr('checked')==true||$(this).prop('checked')==true){
			 	checked++;
			 }
		});
		if(checked==0){
			return;
		}
		$("div[name='attendanceCheckBoxDiv2']").find("input[type='checkbox']").each(function(){
			 if($(this).attr('checked')==true||$(this).prop('checked')==true){
				 $(this).parent().parent().parent().parent().remove();
			 }
		});
		//重新对checkbox的序号编号
		renameCheckboxSpan2();
	}
	
	function getPlanResourceTable(){
		var html='<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">';
		html+='	<tr>';
		html+='		<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv2"><input type="checkbox" /><span id="indexvalue">1</span></div></td>';
		html+='		<td bgcolor="#E6E6E6" >';
		html+='			<table width="100%" align="center" cellPadding="1" cellSpacing="1">';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" style="width:12%" align="right">项目角色<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:22%">';
		html+='						<select name="projectRoleList" style="width:90%">';
		<s:iterator value="#request.projectRole">
		html+='<option value="<s:property value='key'/>"><s:property value="value" /></option>';
		</s:iterator>
		html+='						</select>';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">计划进入时间:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:18%">';
		html+='						<input type="text"  class="CalendarInput" name="planStartTimeList" style="width:90%" value="<s:date name="#request.project.projectResourcePlan[0].planStartTime" format="yyyy-MM-dd"/>" />';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">计划退出时间:</td>';
		html+='					<td bgcolor="#FFFFFF">';
		html+='						<input type="text" class="CalendarInput" name="planEndTimeList" style="width:74%" value="<s:date name="#request.project.projectResourcePlan[0].planEndTime" format="yyyy-MM-dd"/>"/>';
		html+='					</td>';
		html+='				</tr>';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" style="width:12%" align="right">项目职责<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:22%">';
		html+='						<select name="projectDutyList" style="width:90%">';
		<s:iterator value="#request.projectDuty" id="ele">
		html+='<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>';
		</s:iterator>
		html+='						</select>';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">实际进入时间<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:18%">';
		html+='						<input type="text" class="CalendarInput" name="factStartTimeList" style="width:90%" value="<s:date name="#request.project.projectResourcePlan[0].factStartTime" format="yyyy-MM-dd"/>" />';
		html+='					</td>';
		html+='					<td bgcolor="#FFFFFF" style="width:13%" align="right">实际退出时间:</td>';
		html+='					<td bgcolor="#FFFFFF">';
		html+='						<input type="text" class="CalendarInput" name="factEndTimeList" style="width:74%"  value="<s:date name="#request.project.projectResourcePlan[0].factEndTime" format="yyyy-MM-dd"/>"/>';
		html+='					</td>';
		html+='				</tr>';
		html+='				<tr>';
		html+='					<td bgcolor="#FFFFFF" align="right">选择人员<font color="red">*</font>:</td>';
		html+='					<td bgcolor="#FFFFFF" colspan="5">';
		html+='						<input type="hidden" name="useridList" value=""/>';
		html+='						<input type="text" readonly="readonly" value="" name="usernameList" style="width:40%"/>';
		html+='						<input class="selectuser" onclick="selUser(this)" type="button" value="选择">';
		html+='					</td>';
		html+='				</tr>';
		html+='			</table>';
		html+='		</td>';
		html+='	</tr>';
		html+='</table>';
		return html;
	}
	function renameCheckboxSpan2(){
		var i=0;
		$("div[name='attendanceCheckBoxDiv2']").find("span").each(function(){
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
	
	function setPrjSelectPeopleValue(returnValue,indexvalue){
		var i=0;
		$("div[name='attendanceCheckBoxDiv2']").find("span").each(function(){
			i++;
			if(indexvalue==i){
				if (returnValue != null && returnValue != "") {
					var values = returnValue.split(":");
					userids="";
					usernames="";
					if(values.length>0){
						for(var j=0;j<values.length;j++){
							var idname = values[j].split(",");
							userids += ","+idname[0];
							usernames += ","+idname[1];
						}
					}
					userids= userids.substring(1);
					usernames = usernames.substring(1);
					$(this).parent().parent().next().find('input[name="usernameList"]').val(usernames);
					$(this).parent().parent().next().find('input[name="useridList"]').val(userids);
				}else if(returnValue==""){
					$(this).parent().parent().next().find('input[name="usernameList"]').val("");
					$(this).parent().parent().next().find('input[name="useridList"]').val("");
				}
			}
		});
	}
	
	function test(_this){
	}
</script>
<body id="bodyid" leftmargin="0" topmargin="10">
<div style="height:650px;overflow-y:scroll;">
	<form name="projectForm"
		action="<%=request.getContextPath()%>/pages/project/project!modify.action"
		method="post">
		<table width="100%" align="center" cellPadding="0" cellSpacing="0">
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">项目基本信息</td>
							<input type="hidden" id="projectId" name="project.id" value='<s:property value="#request.project.id"/>'/>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目名称<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5">
								<input type="text" name="project.name" value='<s:property value="#request.project.name"/>' maxlength="100" size="100" id="names" />
							</td>
						</tr>
						<tr>
							<td class="input_label2" width="8%">
								<div align="center">
									项目所在地<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="3">
								<select style="width:30%" id="projectProvince" name="project.province"></select> &nbsp;&nbsp;<select style="width:30%" id="projectCity" name="project.city"></select>
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
							<td bgcolor="#FFFFFF" width="15%">
								<select id="projectTypeSelect" name="project.projectType" style="width:90%">
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
							<td bgcolor="#FFFFFF" width="15%" >
								<input type="hidden" value='<s:property value="#request.project.proManagerId"/>' name="project.proManagerId" id="proManagerId" />
								<input type="text" readonly="readonly" value='<s:property value="#request.project.proManager"/>' name="project.proManager" id="proManager" style="width:30%"/>
								<input id="selectManager" type="button" value="选择">
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									是否虚拟项目
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%" >
								<s:select style="width:90%" list="#{0:'否',1:'是'}" value="#request.project.isVisual" name="project.isVisual" ></s:select>
							</td>
						</tr>
						<tr>
							<td width="8%" class="input_label2">
								<div align="center">
									计划开始时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<input id="planStartTime" value='<s:date name="#request.project.planStartTime" format="yyyy-MM-dd"/>' class="CalendarInput" type="text" name="project.planStartTime" style="width:90%" />
							</td>
							<td class="input_label2" width="8%">
								<div align="center">
									计划结束时间<font color="#FF0000">*</font>
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<input id="planEndTime" value='<s:date name="#request.project.planEndTime" format="yyyy-MM-dd"/>' class="CalendarInput" type="text" name="project.planEndTime" style="width:90%"/>
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									项目编号
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<input type="text" value='<s:property value="#request.project.projectNo"/>' name="project.projectNo" style="width:90%" />
							</td>
						</tr>
						<tr>
							
							<td width="8%" class="input_label2">
								<div align="center">
									实际开始时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<input id="factStartTime" value='<s:date name="#request.project.factStartTime" format="yyyy-MM-dd"/>' class="CalendarInput" type="text" name="project.factStartTime"  style="width:90%">
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									实际结束时间
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<input id="factEndTime" value='<s:date name="#request.project.factEndTime" format="yyyy-MM-dd"/>' class="CalendarInput" type="text" name="project.factEndTime" style="width:90%" />
							</td>
							<td width="8%" class="input_label2">
								<div align="center">
									客户名称
								</div>
							</td>
							<td bgcolor="#FFFFFF" width="15%">
								<select id="customerTypeSelect" name="project.customer" style="width:90%">
									<s:iterator value="#request.customerType" var="customer">
										<option value="<s:property value='#customer.key'/>"><s:property value="#customer.value" /></option>
									</s:iterator>
								</select>
							</td>
						</tr>
						<tr>
							<td width="13%" class="input_label2">
								<div align="center">
									<br/>项目组成员<br/>
									<br/><br/>
								</div>
							</td>
							<td colspan="5" bgcolor="#FFFFFF" style="vertical-align: top;">
								<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" >
									<tr style="background-color:#E3E3E3;text-align:center" >
										<td width="13%"><b>姓名</b></td>
										<td width="13%"><b>项目角色</b></td>
										<td width="13%"><b>项目职责</b></td>
										<td width="13%"><b>所在部门</b></td>
										<td width="13%"><b>进入日期</b></td>
										<td width="13%"><b>退出日期</b></td>
										<td width="13%"><b>联系电话</b></td>
									</tr>
									<s:iterator value="#request.project.userProjects" status="row">
									<tr>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="username"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="projectRole"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="projectDuty"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="deptName"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:date name="entryTime" format="yyyy-MM-dd"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:date name="exitTime" format="yyyy-MM-dd"/>'/></td>
										<td><input type="text" style="border-color:transparent;width:99%;" readonly="readonly" value='<s:property value="phone"/>'/></td>
									</tr>
									</s:iterator>
								</table>
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
									<textarea name="project.description" style="width: 98%" id="description" rows="3" id="note"><s:property value="#request.project.description"/></textarea>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr><td><br /></td></tr>
			<tr>
				<td>
					<table class="input_table">
						<tr>
							<td class="input_tablehead">人员分配</td>
						</tr>
						<tr>
							<td id="attendanceContainer2">
								<s:iterator value="#request.project.projectResourcePlan" status="row">
								<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv2"><input type="checkbox" /><span id="indexvalue"><s:property value="#row.index+1" /></span></div></td>
										<td bgcolor="#E6E6E6" >
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目角色<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:22%">
														<input type="hidden" name="projectRole" value='<s:property value="projectRole"/>'>
														<select name="projectRoleList" style="width:90%">
															<s:iterator value="#request.projectRole" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">计划进入时间:</td>
													<td bgcolor="#FFFFFF" style="width:18%">
														<input type="text"  class="CalendarInput" name="planStartTimeList" style="width:90%" value='<s:date name="planStartTime" format="yyyy-MM-dd"/>' onclick="test(this)"/>
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">计划退出时间:</td>
													<td bgcolor="#FFFFFF">
														<input type="text" class="CalendarInput" name="planEndTimeList" style="width:74%" value='<s:date name="planEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" style="width:12%" align="right">项目职责<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:22%">
														<input type="hidden" name="projectDuty" value='<s:property value="projectDuty"/>'>
														<select name="projectDutyList" style="width:90%">
															<s:iterator value="#request.projectDuty" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">实际进入时间<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" style="width:18%">
														<input type="text" class="CalendarInput" name="factStartTimeList" style="width:90%" value='<s:date name="factStartTime" format="yyyy-MM-dd"/>' />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%" align="right">实际退出时间:</td>
													<td bgcolor="#FFFFFF">
														<input type="text" class="CalendarInput" name="factEndTimeList" style="width:74%"  value='<s:date name="factEndTime" format="yyyy-MM-dd"/>'/>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF" align="right">选择人员<font color="red">*</font>:</td>
													<td bgcolor="#FFFFFF" colspan="5">
														<input type="hidden" name="useridList" value='<s:property value="userid"/>'/>
														<input type="text" readonly="readonly" value='<s:property value="username"/>' name="usernameList" style="width:40%"/>
														<input class="selectuser" onclick="selUser(this)" type="button" value="选择">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
								</s:iterator>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" align="center" cellPadding="1" cellSpacing="1">
									<tr>
				                        <td></td>
				                        <td width="6%"><input type="button" value="添加" onClick="addAgain2()"/></td>
				                        <td width="6%"><input type="button" value="删除" onClick="delAgain2()"/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
				</td>
			</tr>
			<tr><td><br /></td></tr>
			<tr>
				<td>
					<table class="input_table" cellPadding="0" cellSpacing="0" >
						<tr>
							<td class="input_tablehead">考勤地点维护</td>
						</tr>
						<tr bgcolor="#E6E6E6">
							<td id="attendanceContainer">
								<s:iterator value="#request.project.attendances" status="row">
								<table  bgcolor="#FFFFFF" width="100%" align="center" border="1" cellPadding="1" cellSpacing="1">
									<tr>
										<td bgcolor="#FFFFFF" width="5%"><div name="attendanceCheckBoxDiv"><input type="checkbox" /><span><s:property value="#row.index+1" /></span></div></td>
										<td bgcolor="#E6E6E6" >
											<table width="100%" align="center" cellPadding="1" cellSpacing="1">
												<tr>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(进入):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" value='<s:date name="entryTime" format="HH:mm:ss"/>' name="attendanceEntryTimeList" style="width:90%" />
													</td>
													<td bgcolor="#FFFFFF" style="width:13%">考勤时间(离开):</td>
													<td bgcolor="#FFFFFF" style="width:21%">
														<input type="text" typeGroup="CalendarHourInput" class="CalendarHourInput" value='<s:date name="exitTime" format="HH:mm:ss"/>' name="attendanceExitTimeList" style="width:90%" />
													</td>
													<td bgcolor="#FFFFFF" style="width:8%">考勤类型:</td>
													<td bgcolor="#FFFFFF" bgcolor="#FFFFFF" style="width:23%">
														<input type="hidden" name="attendanceTypeHiddenValue" value='<s:property value="attendanceType"/>'>
														<select name="attendanceTypeList" style="width:90%" >
															<s:iterator value="#request.attendanceType" id="ele">
																<option value="<s:property value='#ele.key'/>"><s:property value="#ele.value" /></option>
															</s:iterator>
														</select>
													</td>
												</tr>
												<tr>
													<td bgcolor="#FFFFFF">考勤地点:</td>
													<td bgcolor="#FFFFFF" colspan="5">
														<input type="text" value='<s:property value="signPlace"/>' name="attendanceAddressList" maxlength="60" size="70%"/>
														<label>&nbsp;纬度:</label>
														<label name = "latitude">
													    <%-- <fmt:formatNumber value="${<s:property value='longitude'/>}" pattern="#0.00" /> --%>
														 <%-- <fmt:formatNumber value='${"longitude"}' pattern="#0.00" /> --%>
													    <%--  <fmt:formatNumber value="123456.7891" pattern="#,#00.0#"/>  --%>
												     	<%-- <s:set name="userName" value="#restStaffBean.userName"></s:set> --%>
												     	<s:property value="latitude"/>
														</label>
														 <label>经度:</label>
														<label name = "longitude">
														<s:property value="longitude"/>
														<%-- <fmt:formatNumber value="${<struts:property value='longitude'/>}" pattern="#0.00" /> --%>
														</label>
														<input name="viewMap" type="button" value="查看地图">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
								</s:iterator>
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
				<td align="center">
					<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> <input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif">
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>
