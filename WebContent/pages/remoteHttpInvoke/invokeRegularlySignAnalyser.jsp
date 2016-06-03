<%
	try{
		cn.grgbanking.feeltm.util.RemoteHttpMethodInvokeReceiver.invokeRegularlySignAnalyser(request,response);
	}catch(Exception e){
		e.printStackTrace();
	}
%>