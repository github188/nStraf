<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议详情</title>
<style type="text/css">
<!--
.STYLE2 {
	font-family: "楷体_GB2312";
	font-weight: bold;
	font-size: 24px;
}
.STYLE3 {
	font-family: "新宋体";
	font-size: 14px;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<table  height="232" border="2" cellpadding="0" cellspacing="1" >
  <tr>
      <td colspan="6">
      <label>
<img src="images/main/GrgBack.jpg">
      </label>   </td>
      </tr>
      <tr>
     <td colspan="6">
     <div align="center" class="STYLE2">会议纪要</div>   </td>
</tr>
<tr>
    <td width="103"  bgcolor="#999999"><div align="right" class="STYLE3">主题</div></td>
    <td colspan="5">
      <label>
     ${subject}
      </label>   </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">时间</div></td>
    <td width="334" >
<label>
      ${currentDateTime}(${hour})
        </label>    </td>
    <td width="58"  bgcolor="#999999"><div align="right" class="STYLE3">地点</div></td>
    <td width="144" >
<label>
        ${addr}
        </label>    </td>
    <td width="86"  bgcolor="#999999"><div align="right" class="STYLE3">主持人</div></td>
    <td width="159" >
<label>
        ${compere}
        </label>    </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">出席人员</div></td>
    <td colspan="5">
      ${attendPersons}</td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">缺席人员</div></td>
    <td colspan="5">${absentPersons}</td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">会议内容</div></td>
    <td colspan="5"><textarea cols="135" rows="120" >${content}</textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">主送</div></td>
    <td colspan="5">${main}</td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">抄送</div></td>
    <td colspan="5">${copy}</td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right" class="STYLE3">记录</div></td>
    <td><label>
      ${writer}
      </label>    </td>
    <td bgcolor="#999999"><div align="right" class="STYLE3">复核</div></td>
    <td><label>
      ${reAudit}
      </label>    </td>
    <td bgcolor="#999999"><div align="right" class="STYLE3">签发</div></td>
    <td><label>
      ${sign}
      </label>    </td>
  </tr>
</table>
</body>
</html>
