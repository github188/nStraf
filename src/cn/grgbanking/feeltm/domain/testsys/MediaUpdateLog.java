package cn.grgbanking.feeltm.domain.testsys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "media_version_log")
public class MediaUpdateLog {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name = "media")
	private String media; // 介质名称的值,如a1,a2
	@Column(name = "update_date")
	@Temporal(TemporalType.DATE)
	private Date update_date; // 更新日期
	@Column(name = "update_man")
	private String update_man; // 更新人
	@Column(name = "update_version")
	private String update_version; // 更新版本
	@Column(name = "atm_id")
	private String atmId;

//	private int version; // 用于标识版本号的数字，当有新版本时，自动加1

	public MediaUpdateLog() {

	}

	public MediaUpdateLog(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getUpdate_man() {
		return update_man;
	}

	public void setUpdate_man(String updateMan) {
		update_man = updateMan;
	}

	public String getUpdate_version() {
		return update_version;
	}

	public void setUpdate_version(String updateVersion) {
		update_version = updateVersion;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date updateDate) {
		update_date = updateDate;
	}

	public String getAtmId() {
		return atmId;
	}

	public void setAtmId(String atmId) {
		this.atmId = atmId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
