package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="TEST_TOOLS")
public class TestTool implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="tool_name")
	private String toolName;
	@Column(name="visit_url")
	private String visitURL;
	@Column(name="tool_desc")
	private String toolDesc;
	private String source; 
	private String assort;
	private String property;
	@Column(name="version_no")
	private String versionNO;
	
	
	
	public TestTool(){
		
	}
	public TestTool(String id){
		this.id=id;
	}
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getVisitURL() {
		return visitURL;
	}
	public void setVisitURL(String visitURL) {
		this.visitURL = visitURL;
	}
	public String getToolDesc() {
		return toolDesc;
	}
	public void setToolDesc(String toolDesc) {
		this.toolDesc = toolDesc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TestTool other = (TestTool) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAssort() {
		return assort;
	}
	public void setAssort(String assort) {
		this.assort = assort;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(String versionNO) {
		this.versionNO = versionNO;
	}
	
}
