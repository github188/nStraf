<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@page import="cn.grgbanking.feeltm.domain.*"%>
<html>
<head>
	<title>addOneTrainee</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link href="js/jquery.autocomplete.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="js/jquery.autocomplete.js"></script>

<script type="text/javascript">

</script>

<body id="bodyid"  leftmargin="0" topmargin="0">

<form name="addOneForm" action="traineemanage!addOne.action";
		namespace="/pagesTraining/traineemanage" method="post">
		
	<table width="90%" align="center" cellPadding="0" cellSpacing="0">
			<td>
					<table class="input_table">
			      		<tr>
							<td class="input_tablehead">
								添加单个人员
							</td>
			      		</tr>
			      		<tr>
							<td class="input_label2" width="13%">
		  						<div align="center">
									OA账号：<font color="#FF0000"></font>										
								</div>									
							</td>
							<td bgcolor="#FFFFFF" width="25%"><input  type="text"  name="trainingEnrollInfo.account" id="account" value="ljlian2"></td>
						</tr>
			      		<tr>
							<td class="input_label2" width="13%">
		  						<div align="center">
									工号：<font color="#FF0000"></font>										
								</div>									
							</td>
							<td bgcolor="#FFFFFF" width="25%" name="trainingEnrollInfo.workernum" id="workernum">1000046</td>
						</tr>
						
						<tr>
							<td class="input_label2">
								<div align="center">
									姓名：<font color="#FF0000"></font>					
								</div>							
							</td>
							<td  bgcolor="#FFFFFF"  name="trainingEnrollInfo.enrollusername" id="enrollusername">
							罗静莲
							</td>
						</tr>
						
						<tr>
						
             				<td class="input_label2">
								<div align="center">
									部门：<font color="#FF0000"></font>		
								</div>					
							</td>
							<td bgcolor="#FFFFFF" name="trainingEnrollInfo.department" id="department" >开发二部	</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center" >
									岗位：<font color="#FF0000"></font>		
								</div>					
							</td>
							<td bgcolor="#FFFFFF"  name="trainingEnrollInfo.positions" id="positions">							
									软件工程师																
							</td>
						</tr>
						<tr>
							<td class="input_label2">
								<div align="center">
									邮箱：<font color="#FF0000"></font>		
								</div>					
							</td>
							<td bgcolor="#FFFFFF" name="trainingEnrollInfo.email" id="email">
									ljlian@grgbanking.com									
							</td>
						</tr>
					</table>
				</td>

				</table>
	<br />
	<br />
	<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="center">
				<input type="button" name="ok" id="ok"
					value='<s:text  name="grpInfo.ok" />' class="MyButton"
					onClick="save()" image="../../images/share/yes1.gif"> 
					<input
					type="button" name="return" value='<s:text  name="button.close"/>'
					class="MyButton" onclick="closeModal(true);"
					image="../../images/share/f_closed.gif"></td>
			</tr>
		</table>
	
   
	</form>
</body>
</html>
