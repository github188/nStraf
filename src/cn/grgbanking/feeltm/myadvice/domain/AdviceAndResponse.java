package cn.grgbanking.feeltm.myadvice.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
	

/*
 * Author:ljlian
 * 2014-12-5
 * 
 * */
@Entity
@Table(name="OA_ADVICEANDRESPONSE")
public class AdviceAndResponse {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;//主键id
	
	@Column(name="C_ADVICEMAN",nullable=false)
	private String adviceMan;//建议人
	
	@Column(name="C_CONTENT",nullable=false )
	private String content;//意见内容
	
	@Column(name="D_TIME")
	private Date time;//建议提出的时间
	
	@Column(name="C_TEL",nullable=false)
	private String tel;//联系方式
	
	@Column(name="C_EMAIL",nullable=false)
	private String email;//电子邮箱
	
	@Column(name="C_REPLY")
	private String reply;//回复（对所提意见的回复）

	@Column(name="C_STATUS" )
	private String status ="pending";//建议被处理的状态    默认是待处理  待处理  已解决   已关闭   已列入计划  此数据配在 系统的数据字典中
	
	@Column(name="D_PLANTIME" )
	private Date plantime;//计划执行的时间
	
	@Column(name="C_USERID")
	private String userId;//用户的id
	
	
	@Column(name="C_EXT1")
	private String ext1;
	
	@Column(name="C_EXT2")
	private String ext2;

	

	public AdviceAndResponse() {
		super();
	}



	public AdviceAndResponse(String id, String adviceMan, String content,
			Date time, String tel, String email, String reply, String status,
			Date plantime, String userId, String ext1, String ext2) {
		super();
		this.id = id;
		this.adviceMan = adviceMan;
		this.content = content;
		this.time = time;
		this.tel = tel;
		this.email = email;
		this.reply = reply;
		this.status = status;
		this.plantime = plantime;
		this.userId = userId;
		this.ext1 = ext1;
		this.ext2 = ext2;
	}

	
	


	@Override
	public String toString() {
		return "AdviceAndResponse [id=" + id + ", adviceMan=" + adviceMan
				+ ", content=" + content + ", time=" + time + ", tel=" + tel
				+ ", email=" + email + ", reply=" + reply + ", status="
				+ status + ", plantime=" + plantime + ", userId=" + userId
				+ ", ext1=" + ext1 + ", ext2=" + ext2 + "]";
	}

	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adviceMan == null) ? 0 : adviceMan.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((ext1 == null) ? 0 : ext1.hashCode());
		result = prime * result + ((ext2 == null) ? 0 : ext2.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((plantime == null) ? 0 : plantime.hashCode());
		result = prime * result + ((reply == null) ? 0 : reply.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdviceAndResponse other = (AdviceAndResponse) obj;
		if (adviceMan == null) {
			if (other.adviceMan != null)
				return false;
		} else if (!adviceMan.equals(other.adviceMan))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (ext1 == null) {
			if (other.ext1 != null)
				return false;
		} else if (!ext1.equals(other.ext1))
			return false;
		if (ext2 == null) {
			if (other.ext2 != null)
				return false;
		} else if (!ext2.equals(other.ext2))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (plantime == null) {
			if (other.plantime != null)
				return false;
		} else if (!plantime.equals(other.plantime))
			return false;
		if (reply == null) {
			if (other.reply != null)
				return false;
		} else if (!reply.equals(other.reply))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tel == null) {
			if (other.tel != null)
				return false;
		} else if (!tel.equals(other.tel))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getAdviceMan() {
		return adviceMan;
	}



	public void setAdviceMan(String adviceMan) {
		this.adviceMan = adviceMan;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public Date getTime() {
		return time;
	}



	public void setTime(Date time) {
		this.time = time;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getReply() {
		return reply;
	}



	public void setReply(String reply) {
		this.reply = reply;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Date getPlantime() {
		return plantime;
	}



	public void setPlantime(Date plantime) {
		this.plantime = plantime;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getExt1() {
		return ext1;
	}



	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}



	public String getExt2() {
		return ext2;
	}



	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}



	


}