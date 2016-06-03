package cn.grgbanking.feeltm.dboaBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import cn.grgbanking.feeltm.log.SysLog;


public class FtpDownloadDboa{

	public void  dboaBackUp() {
		String host = PropertiesUtil.get("FtpHost");//ftp  ip地址
		int port = Integer.parseInt(PropertiesUtil.get("FtpPort"));//ftp端口号
		String userName = PropertiesUtil.get("FtpUserName"); //ftp用户名
		String password = PropertiesUtil.get("Ftppasswd");//密码
		String remotePath = PropertiesUtil.get("dboaDownloadPath");//文件的下载路径
		String localPath = PropertiesUtil.get("dboaSavaPath");//下载文件的本地保存路径
		
		FTPClient ftpClient = new FTPClient();
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());  
		ftpClient.setControlEncoding("UTF-8");  //设置字符编码
		ftpClient.configure(ftpClientConfig);
		
		try {
			ftpClient.connect(host, port);
			boolean login = ftpClient.login(userName, password);
			if( login){ 
				ftpClient.enterLocalPassiveMode();  
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
				SysLog.info("用户"+userName+"登录成功!");
				downFile(ftpClient,host, remotePath,localPath) ;
			}else{
				SysLog.info("用户"+userName+"登录失败!");
			}
			
			ftpClient.logout();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				ftpClient.disconnect();// 关闭FTP服务器的连接  
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	/**
	* Description: 从FTP服务器下载文件
	* @param url FTP服务器hostname
	* @param port FTP服务器端口
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param remotePath FTP服务器上的相对路径
	* @param fileName 要下载的文件名
	* @param localPath 下载后保存到本地的路径
	 * @param localPath 
	* @return
	*/  
	public static boolean downFile(FTPClient ftpClient,String url, String remotePath, String localPath) { 
		Date today = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2= new SimpleDateFormat("yyyyMMdd");
		String todayStr = sdf1.format(today);
		String dirNameStr = sdf2.format(today);
		SysLog.info(todayStr +"================开始下载数据库的备份文件");
		 boolean isSuccessed = false;  
		 
		try {
			boolean isChange =ftpClient.changeWorkingDirectory("/dboaBackUp/"+dirNameStr+"/");//将ftp上的文件目录切换到   我们要下载文件的目录
			if(isChange){
				FTPFile[] files = ftpClient.listFiles();  //获取 切换后的目录下的所有文件
				 if( files != null && files.length>0){
					 for( int i=0;i<files.length;i++){//将所有的备份文件下载到dmz
						 
						 String fileName = files[i].getName();
						//下载文件要存放的本地路径 C:\oraclebackup\20150831\文件名
						 File file = new File(localPath +"/"+dirNameStr+ "/"+fileName); 
						 File parent = file.getParentFile();
						 if(parent!=null&&!parent.exists()){ //
							 parent.mkdirs();
						 }
						file.createNewFile();
						FileOutputStream fos = new FileOutputStream(file);
						isSuccessed =  ftpClient.retrieveFile(fileName, fos)  ;   //文件下载 
						fos.close();
						file = null;
						if (!isSuccessed) {
							SysLog.info("文件" + fileName + "下载失败.");
						}else{
							SysLog.info(todayStr + fileName+"================成功下载数据库的备份文件！");
						}
					 }
				 }
			}else{
				SysLog.info(todayStr +"================要下载的文件不存在！");
				return isSuccessed;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccessed;

	}
	
}
