package cn.grgbanking.feeltm.util;

import java.security.*;

import cn.grgbanking.feeltm.log.SysLog;

public class HashUtil
{
  private static MessageDigest digest = null;

  public HashUtil()
  {
  }
//------------------------------------------------------------------------------

 public  synchronized static final String hash(String data)
 {
      if (digest == null)
    	  
      {
        try
        {
          digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae)
        {
        	SysLog.error("error in (HashUtil.java-hash())");
          System.err.println("Failed to load the MD5 MessageDigest. ");
          nsae.printStackTrace();
        }
      }
      digest.update(data.getBytes());
      return encodeHex(digest.digest());
 }
//------------------------------------------------------------------------------

   public static final String encodeHex(byte[] bytes)
   {
       StringBuffer buf = new StringBuffer(bytes.length * 2);
       for (int i = 0; i < bytes.length; i++)
       {
           if (((int) bytes[i] & 0xff) < 0x10)
           {
                   buf.append("0");
           }
           buf.append(Long.toString((int) bytes[i] & 0xff, 16));
       }
       return buf.toString();
    }
//------------------------------------------------------------------------------
}