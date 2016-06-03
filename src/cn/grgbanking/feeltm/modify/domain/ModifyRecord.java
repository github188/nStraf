package cn.grgbanking.feeltm.modify.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
	
/**
 * 修改记录
 * @author lhyan3
 * 2014年7月21日
 */
@Entity
@Table(name="OA_MODIFYRECORD")
public class ModifyRecord {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;//主键id
	
	@Column(name="C_MODIFYID")
	private String modifyId;//修改记录的id
	
	@Column(name="C_USERNAME")
	private String username;//修改人姓名
	
	@Column(name="C_OPERATE")
	private String operate;//操作(修改还是删除)
	
	@Column(name="C_RECORD")
	private String recorde;//修改前的内容
	
	@Column(name="D_MODIFYDATE")
	private Date modifyDate;//修改时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRecorde() {
		return recorde;
	}

	public void setRecorde(String recorde) {
		this.recorde = recorde;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getOperate() {
		return operate;
	}
	
	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	public String getModifyId() {
		return modifyId;
	}
	
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	

}
