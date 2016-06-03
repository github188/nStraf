package cn.grgbanking.feeltm.util;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
  
/**   
  *  
  *   void getKey(String strKey)��strKey���������һ��Key   
  *     
  *   String getEncString(String strMing)��strMing���м���,����String����   
  *   String getDesString(String strMi)��strMin���н���,����String����   
  *     
  *   byte[] getEncCode(byte[] byteS)byte[]�͵ļ���   
  *   byte[] getDesCode(byte[] byteD)byte[]�͵Ľ���   
  */   
  
public class DesEncrypt   
{   
	Key key;   
	   private String charset = "GB2312";
  
  /**   
    *   ��ݲ������KEY   
    *   @param   strKey   
    */   
  public void getKey(String strKey)   
  {   
    try{   
      KeyGenerator _generator = KeyGenerator.getInstance("DES");   
        _generator.init(new SecureRandom(strKey.getBytes()));   
        this.key = _generator.generateKey();   
        _generator=null;   
      }catch(Exception e){   
      e.printStackTrace();   
    }   
  }   
  /**  
   * ��ָ���ַ������Կ����Կ������ֽ����鳤��Ϊ8λ ����8λʱ���油0������8λֻȡǰ8λ  
   *   
   * @param arrBTmp  
   *            ���ɸ��ַ���ֽ�����  
   * @return ��ɵ���Կ  
   * @throws java.lang.Exception  
   */  
  public void getKey(byte[] arrBTmp) {   
   // ����һ��յ�8λ�ֽ����飨Ĭ��ֵΪ0��   
   byte[] arrB = new byte[8];   
   
   // ��ԭʼ�ֽ�����ת��Ϊ8λ   
   for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {   
    arrB[i] = arrBTmp[i];   
   }   
   
   // �����Կ   
   Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");   
   
   this.key = key;   
  }  

  /**   
    *   ����String��������,String�������   
    *   @param   strMing   
    *   @return   
    */   
  public String getEncString(String strMing)   
  {   
    byte[] byteMi = null;   
    byte[] byteMing = null;   
    String strMi = "";   
    BASE64Encoder base64en = new BASE64Encoder();   
    try   
    {   
      byteMing = strMing.getBytes(charset);   
      byteMi = this.getEncCode(byteMing);   
      strMi = base64en.encode(byteMi);   
    }   
    catch(Exception e)   
    {   
      e.printStackTrace();   
    }   
    finally   
    {   
      base64en = null;   
      byteMing = null;   
      byteMi = null;   
    }   
    return strMi;   
  }   

  public byte[] getEncbyte(String strMing)   
  {   
    byte[] byteMi = null;   
    byte[] byteMing = null;   
    try   
    {   
      byteMing = strMing.getBytes(charset);   
      byteMi = this.getEncCode(byteMing);   
    }   
    catch(Exception e)   
    {   
      e.printStackTrace();   
    }   
    finally   
    {   
      byteMing = null;  
    }   
    return byteMi;   
  }   
  
  /**   
    *   ����   ��String��������,String�������   
    *   @param strMi   
    *   @return   
    */   
  public byte[] getDesString(String strMi)   
  {   
    BASE64Decoder base64De = new BASE64Decoder();   
    byte[] byteMing = null;   
    byte[] byteMi = null;   
    try   
    {   
      byteMi = base64De.decodeBuffer(strMi);   
      byteMing = this.getDesCode(byteMi);   
    }   
    catch(Exception e)   
    {   
      e.printStackTrace();   
    }   
    finally   
    {   
      base64De = null;   
      byteMing = null;   
      byteMi = null;   
    }   
    return byteMing;   
  }   
  
  /**   
    *   ������byte[]��������,byte[]�������   
    *   @param byteS   
    *   @return   
    */   
  public byte[] getEncCode(byte[] byteS)   
  {   
    byte[] byteFina = null;   
    Cipher cipher;   
    try   
    {   
      cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");   
      cipher.init(Cipher.ENCRYPT_MODE, key);   
      byteFina = cipher.doFinal(byteS);   
    }   
    catch(Exception e)   
    {   
      e.printStackTrace();   
    }   
    finally   
    {   
      cipher = null;   
    }   
    return byteFina;   
  }
     
  /**   
    *   ������byte[]��������,��byte[]�������   
    *   @param byteD   
    *   @return   
    */   
  public byte[] getDesCode(byte[] byteD)   
  {   
    Cipher cipher;   
    byte[] byteFina=null;   
    try{   
      cipher = Cipher.getInstance("DES/ECB/NoPadding");   
      cipher.init(Cipher.DECRYPT_MODE, key);   
      byteFina = cipher.doFinal(byteD);   
    }catch(Exception e){   
      e.printStackTrace();   
    }finally{   
      cipher=null;   
    }   
    return byteFina;      
  }   
    
  public static void main(String[] args){   
    
    System.out.println("hello");   
    DesEncrypt des=new DesEncrypt();//ʵ��һ�����   
    des.getKey("aadd");//����ܳ�   
      
    byte[] strEnc = des.getEncbyte("�Ӻ���");//�����ַ�,����String������   
    //System.out.println(strEnc);   
//
//    BASE64Encoder base64en = new BASE64Encoder();  
//    String encStr = base64en.encode(strEnc);
//    
//    BASE64Decoder base64De = new BASE64Decoder();   
    try {
//		byte []antiBase64 = base64De.decodeBuffer( encStr );
		
		byte[] strDes = des.getDesCode(strEnc);//��String���͵����Ľ���   
		String newStr = new String( strDes,"GB2312" );
		System.out.println("newStr="+newStr);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   
    
  }

	public Key getKey() {
		return key;
	}
  
}   

