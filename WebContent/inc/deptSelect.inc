<%@ page contentType="text/html; charset=UTF-8" %>
	<script language="javascript">
	 var deptname = "";
	 var username = "";
	 var groupname = "";
	 var userHeadKey ="";
	 var userHeadValue="";
	 //判断是否有级联的操作
	 var deptSelectFlag = false;
		function searchByDept(deptId,userId,userKey,userValue,deptHeadvalue){
			deptname = deptId;
			username = userId;
			userHeadKey = userKey;
			userHeadValue= userValue;
			//var url="<%=request.getContextPath()%>/pages/um/sysUserInfo!queryNames.action";
			var url="<%=request.getContextPath()%>/pages/staff/staffInfo!queryNames.action";
			var params={deptName:document.getElementById(deptId).value,deptValue:deptHeadvalue};
			if(params.deptName==params.deptValue){
				$("#"+username+"").show();
				$("#id"+username+"").hide();
				return ;
			}else{
				$("#"+username+"").hide();
				$("#id"+username+"").show();
			}
			jQuery.post(url, params, $(document).callname, 'json');
		}
		function searchByDept(deptId,userId,userKey,userValue,deptHeadvalue,groupId,isloadName){
			deptname = deptId;
			username = userId;
			userHeadKey = userKey;
			userHeadValue= userValue;
			groupname=groupId;
			var groupvalue = document.getElementById(groupId).value;
			if(isloadName!='true'){
				groupvalue="";
			}
			if(groupvalue=="全选"){
				groupvalue="";
			}
			//var url="<%=request.getContextPath()%>/pages/um/sysUserInfo!queryNames.action";
			var url="<%=request.getContextPath()%>/pages/staff/staffInfo!queryNames.action";
			var params={deptName:document.getElementById(deptId).value,deptValue:deptHeadvalue,groupId:groupvalue};
			if(params.deptName==params.deptValue){
				$("#"+username+"").show();
				$("#id"+username+"").hide();
				return ;
			}else{
				$("#"+username+"").hide();
				$("#id"+username+"").show();
			}
			jQuery.post(url, params, $(document).callname, 'json');
		}
		
		$.fn.callname=function (json){
			$("#id"+username+" option").remove();
			if(userHeadKey!="null"||userHeadKey!=undefined){
				$("#id"+username+"").append("<option value='"+userHeadValue+"'>"+userHeadKey+"</option>");
			}
			 if(json!=null&&json.length>0){
				for(var i=0;i<json.length;i++){
					$("#id"+username).append("<option value='"+json[i][1]+"' title='"+json[i][1]+"'>"+json[i][0]+"</option>");
				}
			} 
			document.getElementById("id"+username).focus();
			document.getElementById("id"+username).value = userHeadValue;
			deptSelectFlag = true;
		} 
		
		
		function searchByGroup(groupId,deptId,userId,userKey,userValue,deptHeadvalue,deptHeadvalue){
			groupname = groupId;
			username = userId;
			userHeadKey = userKey;
			userHeadValue= userValue;
			var url="<%=request.getContextPath()%>/pages/project/project!queryNames.action";
			var params={deptvalue:document.getElementById(deptId).value,grpvalue:document.getElementById(groupId).value,deptValue:deptHeadvalue,groupValue:deptHeadvalue};
			var isinput = (params.deptvalue==params.deptValue&&params.grpvalue==params.groupValue);
			if(!isinput){
				$("#"+username+"").hide();
				$("#id"+username+"").show();
			}else{
				$("#"+username+"").show();
				$("#id"+username+"").hide();
				return ;
			}
			jQuery.post(url, params, $(document).callnameByGroup, 'json');
			
		}
		$.fn.callnameByGroup=function (json){
			$("#id"+username+" option").remove();
			if(userHeadKey!="null"||userHeadKey!=undefined){
				$("#id"+username+"").append("<option value='"+userHeadValue+"'>"+userHeadKey+"</option>");
			}
			 if(json!=null&&json.length>0){
				for(var i=0;i<json.length;i++){
					$("#id"+username).append("<option value='"+json[i][1]+"' title='"+json[i][1]+"'>"+json[i][0]+"</option>");
				}
			} 
			document.getElementById("id"+username).focus();
			document.getElementById("id"+username).value = userHeadValue;
			deptSelectFlag = true;
		} 
		
		function searchByName(userId,groupId){
			groupname = groupId;
			var url="<%=request.getContextPath()%>/pages/project/project!getGroupNameByUserName.action";
			//var url="<%=request.getContextPath()%>/pages/um/sysUserGroup!getGroupNameByUserName.action";
			document.getElementById(userId).value = document.getElementById("id"+userId).value;
			var params={userid:document.getElementById(userId).value};
			jQuery.post(url, params, $(document).callgroup, 'json');
			
		}
		$.fn.callgroup=function (json){
			var values = "";
			 if(json!=null&&json.length>0){
				for(var i=0;i<json.length;i++){
					values +=json[i].name+",";
				}
			} 
			 $("#"+groupname).val(values);
			 $("#"+groupname+"label").text(values);
			 deptSelectFlag = true;
		}
		$(function(){
			$("#id" + username).hide();
			$("#" + username).show();
			$("input[type='reset']").click(function(){
				//判断是否做了级联的选择控件的操作
				if(deptSelectFlag){
					//将下拉列表控件进行隐藏，输入框显示
					$("#id" + username).hide();
					$("#" + username).show();
				}
			});
		})
		</script>
		
		
		