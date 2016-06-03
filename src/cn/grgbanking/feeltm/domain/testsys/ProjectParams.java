package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="prj_params")
public class ProjectParams implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name="prj_name")
	private String prjName;
	@Column(name="version_no")
	private String versionNO;
	@Column(name="prj_defect")
	private Integer prjDefect;
	@Column(name="code_line")
	private Long codeLine=0L;
	@Column(name="db_table")
	private String prjType1;
	
	private String note;

	public ProjectParams() {
		
	}
	
	public ProjectParams(String id){
		this.id=id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getVersionNO() {
		return versionNO;
	}

	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}

	public Integer getPrjDefect() {
		return prjDefect;
	}

	public void setPrjDefect(Integer prjDefect) {
		this.prjDefect = prjDefect;
	}

	public Long getCodeLine() {
		return codeLine;
	}

	public void setCodeLine(Long codeLine) {
		this.codeLine = codeLine;
	}
	
	
	
	

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
	


	public String getPrjType1() {
		return prjType1;
	}

	public void setPrjType1(String prjType1) {
		this.prjType1 = prjType1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectParams other = (ProjectParams) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
