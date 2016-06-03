package cn.grgbanking.feeltm.client.webapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.client.domain.ClientUpload;
import cn.grgbanking.feeltm.client.service.ClientUploadService;
import cn.grgbanking.feeltm.common.util.NStrafFileUtils;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

@SuppressWarnings("serial")
public class ClientUploadAction extends BaseAction{
	
	@Autowired
	private ClientUploadService service;
	
	private File upload;
	
	private String uploadFileName;
	
	private String uploadContentType;

	
	public String toUpload(){
		String nextVersion=getNextVersion(null);
		request.setAttribute("nextVersion", nextVersion);
		return "toUpload";
	}
	
	/**
	 * 上传文件
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public String upload(){
		String status = request.getParameter("status");
		String version=request.getParameter("version");
		String note=request.getParameter("note");
		MsgBox msgBox;
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			if(upload!=null){
				Date date=new Date();
				String dir = NStrafFileUtils.getClientUploadDir();
				String fileWholeName="";
				if(uploadFileName.endsWith(".apk")||uploadFileName.endsWith(".ipa")){
					String tmp1=uploadFileName.substring(0,uploadFileName.lastIndexOf("."));
					String tmp2=uploadFileName.substring(uploadFileName.lastIndexOf("."),uploadFileName.length());
					fileWholeName=tmp1+"("+version+")"+tmp2;
				}else{
					fileWholeName=UUID.randomUUID().toString();
				}
				String filePath=dir+fileWholeName;
				
				File destFile = new File(filePath);
				FileUtils.copyFile(upload, destFile);
				ClientUpload clientUpload = new ClientUpload();
				if(uploadFileName.endsWith(".apk")){
					clientUpload.setType("0");
				}
				else if(uploadFileName.endsWith(".ipa")){
					clientUpload.setType("1");
				}else{
					clientUpload.setType("2");//其它
				}
				clientUpload.setFileName(fileWholeName);
				clientUpload.setUploadTime(date);
				clientUpload.setUrl(filePath);
				clientUpload.setUsername(userModel.getUsername());
				clientUpload.setStatus(status);
				String nextVersion=getNextVersion(version);
				clientUpload.setVersion(nextVersion);
				clientUpload.setVersionNum(getVersionNum(nextVersion));
				clientUpload.setNote(note);
				service.save(clientUpload);
				msgBox = new MsgBox(request, getText("上传成功"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}else {
				msgBox = new MsgBox(request, getText("上传失败,未选择文件"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("上传失败"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**根据版本获取版本号
	 * wtjiao 2014年8月6日 下午3:33:14
	 */
	private long getVersionNum(String nextVersion) {
		long sum=0;
		String[] arr=nextVersion.split("\\.");
		sum+=Integer.parseInt(arr[0])*100*1000*10000+Integer.parseInt(arr[1])*1000*10000+Integer.parseInt(arr[2])*10000+Integer.parseInt(arr[3]);
		return sum;
	}

	private String getNextVersion(String uploadVersion) {
		if(StringUtils.isNotBlank(uploadVersion)){
			return uploadVersion;
		}else{
			String curVer=service.getNewVersion();
			String curVerMinCodePre=curVer.substring(0,curVer.lastIndexOf('.')+1);
			String curVerMinCode=curVer.substring(curVer.lastIndexOf('.')+1);
			int curVerMinCodeInt=Integer.parseInt(curVerMinCode)+1;
			String newVer=curVerMinCodePre+curVerMinCodeInt;
			return newVer;
		}
	}

	public void refresh(){
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 列表页面
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		MsgBox msgBox;
		try {
			int pageNum = 1;
			int pageSize = 20;
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String form = request.getParameter("form");
			Page page = service.getPage(pageNum, pageSize,startTime,endTime);
			request.setAttribute("currPage", page);
			List<Object> list = page.getQueryResult();
			if (form != null && form.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil util = new JSONUtil(
						"cn.grgbanking.feeltm.client.domain.ClientUpload");
				JSONArray array = util.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", array);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}
			request.setAttribute("clients", list);
			return "list";
		} catch (Exception e) {
			SysLog.error("error in (ClientUploadAction.java-list())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	public String downloadFile(){
		try{
			String id=request.getParameter("id");
			ClientUpload cu=service.getObject(id);
			File file=new File(cu==null?"":cu.getUrl());
	        if(file.exists()){
	            InputStream ins=new FileInputStream(file);//构造一个读取文件的IO流对象
	            BufferedInputStream bins=new BufferedInputStream(ins);//放到缓冲流里面
	            OutputStream outs=response.getOutputStream();//获取文件输出IO流
	            BufferedOutputStream bouts=new BufferedOutputStream(outs);
	            response.setContentType("application/x-download");//设置response内容的类型
	            response.setHeader("Content-disposition","attachment;filename="+cu.getFileName());//设置头部信息
	            int bytesRead = 0;
	            byte[] buffer = new byte[8192];
	            //开始向网络传输文件流
	            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
	                bouts.write(buffer, 0, bytesRead);
	            }
	            bouts.flush();//这里一定要调用flush()方法
	            ins.close();
	            bins.close();
	            outs.close();
	            bouts.close();
	        }else{
	            System.out.println("下载的文件不存在");
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	
	

}
