<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<html>
<head>
<title>运通信息作战系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="SHORTCUT ICON" href="">
</head>
<frameset frameSpacing="0" rows="107,*" frameBorder="no" cols="*"  noresize="noresize" scrolling="no">
  <!-- 顶部内容页 -->
  <frame id=topFrame name=topFrame src="login!headerMenu.action"  noresize="noresize" scrolling=no />
  <frameset cols="0,0,*" name="left" id="left" frameSpacing=0 frameBorder=no id="left">
  	<!-- 左边菜单项内容页 -->
    <frame src="login!leftMenu.action" id=leftFrame name=leftFrame scrolling="No" noresize="noresize" />
    <!-- 中间分割块 -->
	<frame src="mid.htm" name="mid" id="mid"  noresize="noresize" scrolling="no" />
	<!-- 主内容页 -->
    <frame id="mainframe" name="mainframe" src="login!mainpage.action" scrolling="yes" />
  </frameset>
</frameset>
<noframes><body>
</body>
</noframes>
</html>

