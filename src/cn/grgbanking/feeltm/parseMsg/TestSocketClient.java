package cn.grgbanking.feeltm.parseMsg;

import cn.grgbanking.feeltm.parseMsg.appMessage.R2002Msg;
import cn.grgbanking.feeltm.parseMsg.appMessage.S2002Msg;

public class TestSocketClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       try{
    	   S2002Msg smsg=new S2002Msg();
    	   smsg.setCheckcode("checkcode");
    	   smsg.setDevid("Z1000001");
    	   smsg.setMessageno("testmesageno");
    	   smsg.setOgcd("0000000");
    	   smsg.setPicturename("HD90123456789.jpg");
    	   byte[] returnpack = null;
		   byte[] sendpack = smsg.packMessage();
		 //报文发送，并等待获取返回报文
			Transfer trans = new Transfer();
			
			try
			{
				
				returnpack = trans.sendPack(sendpack);
			}
			catch (Exception ee) {			
				ee.printStackTrace();
			}
           if(returnpack == null){
				
				System.out.println("null exception ");		// 通讯超时
			}else
			{
			   R2002Msg rmsg=new R2002Msg();
			   rmsg.unpackMessage(returnpack);
			   System.out.println("******");
			}
    	   
       }catch(Exception e){
    	   e.printStackTrace();
       }
	}

}
