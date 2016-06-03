package cn.grgbanking.feeltm.client.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_CLIENTUPLOAD")
public class ClientUpload {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID")
	private String id;
	
	@Column(name="C_FILENAME")
	private String fileName;//文件名
	
	@Column(name="D_UPLOADTIME")
	private Date uploadTime;//上传时间 
	
	@Column(name="C_URL")
	private String url;//路径
	
	@Column(name="C_USERNAME")
	private String username;//上传人
	
	@Column(name="D_VERSION")
	private String version;//版本
	
	@Column(name="L_VERSIONNUM")
	private Long versionNum;//版本号
	
	@Column(name="C_TYPE")
	private String type;//安卓 0  IOS 1
	
	@Column(name="C_STATUS")
	private String status;//强制更新 1  自动更新 0
	
	@Column(name="C_NOTE")
	private String note;//更新提示

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Long versionNum) {
		this.versionNum = versionNum;
	}

	
	
}
