package cn.grgbanking.feeltm.domain.testsys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="LAB_ATM")
public class Atm {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	
	@Column(name = "machine_no")
	private String machineNo;
	
	@Column(name = "device_seq_no")
	private String deviceNo;
	
	@Column(name="ip")
	private String netIP;
	
	@Column(name="borrow_man")
	private String browerer;
	
	@Column(name="borrow_date")
	@Temporal(TemporalType.DATE)
	private Date browerDate;
	
	@Column(name="running_no")
	private String browerOA;  
	
	
	@Column(name="pc_type_no")
	private String controllerNo;
	
	private String cpu;
	private String memory;
	
	@Column(name="display_card")
	private String vga;   //显卡
	
	@Column(name="network_card")
	private String netCard;
	
	@Column(name="sound_card")
	private String soundCard;
	
	@Column(name="operation_system")
	private String os;
	
	@Column(name="patch_version")
	private String patch;
	
	@Column(name="driver_size")
	private String space;
	
	@Column(name="ie_version")
	private String ieVersion;
	
	@Column(name="config_status")
	private String configStatus;
	
	@Transient
	private String BrowerDateString;
	
	
	public Atm(){
		
	}
	public Atm(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
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
	public String getBrowerer() {
		return browerer;
	}
	public void setBrowerer(String browerer) {
		this.browerer = browerer;
	}
	public Date getBrowerDate() {
		return browerDate;
	}
	public void setBrowerDate(Date browerDate) {
		this.browerDate = browerDate;
	}
	public String getBrowerOA() {
		return browerOA;
	}
	public void setBrowerOA(String browerOA) {
		this.browerOA = browerOA;
	}
	public String getControllerNo() {
		return controllerNo;
	}
	public void setControllerNo(String controllerNo) {
		this.controllerNo = controllerNo;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getVga() {
		return vga;
	}
	public void setVga(String vga) {
		this.vga = vga;
	}
	public String getNetCard() {
		return netCard;
	}
	public void setNetCard(String netCard) {
		this.netCard = netCard;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getIeVersion() {
		return ieVersion;
	}
	public void setIeVersion(String ieVersion) {
		this.ieVersion = ieVersion;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public String getPatch() {
		return patch;
	}
	public void setPatch(String patch) {
		this.patch = patch;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String getSoundCard() {
		return soundCard;
	}
	public void setSoundCard(String soundCard) {
		this.soundCard = soundCard;
	}
	public String getBrowerDateString() {
		return BrowerDateString;
	}
	public void setBrowerDateString(String browerDateString) {
		BrowerDateString = browerDateString;
	}
	public String getConfigStatus() {
		return configStatus;
	}
	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}
	
	
}
