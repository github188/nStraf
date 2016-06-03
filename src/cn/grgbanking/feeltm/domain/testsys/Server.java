package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SERVER_SOURCE")
public class Server implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name="device_no")
	private String deviceNo;
	@Column(name="net_ip")
	private String netIP;
	@Column(name="device_name")
	private String deviceName;
	@Column(name="visit_url")
	private String visitURL;  
	@Column(name="device_desc")
	private String deviceDesc; //��;
	private String responsor;
	@Column(name="page_url")
	private String pageURL;
	
	public Server(){
		
	}
	
	public Server(String id){
		this.id=id;
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
		Server other = (Server) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getNetIP() {
		return netIP;
	}
	public void setNetIP(String netIP) {
		this.netIP = netIP;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getVisitURL() {
		return visitURL;
	}
	public void setVisitURL(String visitURL) {
		this.visitURL = visitURL;
	}
	
	public String getDeviceDesc() {
		return deviceDesc;
	}
	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}
	public String getResponsor() {
		return responsor;
	}
	public void setResponsor(String responsor) {
		this.responsor = responsor;
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

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	
	
	
}
