package cn.grgbanking.feeltm.dayLog.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OA_DAYLOG_VOTE")
public class DayLogVote {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;
	
	@Column(name = "C_DAYLOGID")
	private String daylogId;
	
	@Column(name = "C_CONTENT")
	private String content;
	
	@Column(name = "C_MAN")
	private String voter;
	
	@Column(name = "D_DATE")
	private Date voteTime;
	
	@Column(name="C_MANID")
	private String voterId;
	
	@Column(name="C_MANDEPT")
	private String voterDept;
	
	@Column(name="C_MANGROUP")
	private String voterGroupName;
	
	@Column(name="C_PARENETID")
	private String parentId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getDaylogId() {
		return daylogId;
	}

	public void setDaylogId(String daylogId) {
		this.daylogId = daylogId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVoter() {
		return voter;
	}

	public void setVoter(String voter) {
		this.voter = voter;
	}

	public Date getVoteTime() {
		return voteTime;
	}

	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}

	public String getVoterDept() {
		return voterDept;
	}

	public void setVoterDept(String voterDept) {
		this.voterDept = voterDept;
	}

	public String getVoterGroupName() {
		return voterGroupName;
	}

	public void setVoterGroupName(String voterGroupName) {
		this.voterGroupName = voterGroupName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getVoterId() {
		return voterId;
	}
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}
	
	
}
