package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 *   --机器介质版本
 create table media_version(
 ID VARCHAR2(32) primary key,
 running_printer_version  VARCHAR2(32),   --流水打印机	
 proof_printer_version VARCHAR2(32),--凭条打印机	
 read_card_version  VARCHAR2(32),--读卡器	
 door_version VARCHAR2(32),--闸门			
 key_version  VARCHAR2(32),--用户键盘	
 backdoor_version VARCHAR2(32),--后台维护终端	
 atm_core_version VARCHAR2(32)，--机芯			
 other  VARCHAR2(32)--其它		

 )
 */
@Entity
@Table(name = "media_version")
public class MediaVersion {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Column(name = "running_printer")
	private String runningPrinter;
	@Column(name = "running_printer_version")
	private String runningPrinterVersion;

	@Column(name = "proof_printer")
	private String proofPrinter;
	@Column(name = "proof_printer_version")
	private String proofPrinterVersion;

	@Column(name = "read_card")
	private String readCard;
	@Column(name = "read_card_version")
	private String readCardVersion;
	
	private String door;
	@Column(name = "door_version")
	private String doorVersion;

	private String key;
	@Column(name = "key_version")
	private String keyVersion;
	
	@Column(name = "backdoor")
	private String backDoor;
	@Column(name = "backdoor_version")
	private String backDoorVersion; 

	@Column(name = "atm_core")
	private String core;
	@Column(name = "atm_core_version")
	private String coreVersion;

	private String other;
	@Column(name = "other_version")
	private String otherVersion;

	public MediaVersion() {
	}
	
	public MediaVersion(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRunningPrinterVersion() {
		return runningPrinterVersion;
	}

	public void setRunningPrinterVersion(String runningPrinterVersion) {
		this.runningPrinterVersion = runningPrinterVersion;
	}

	public String getProofPrinterVersion() {
		return proofPrinterVersion;
	}

	public void setProofPrinterVersion(String proofPrinterVersion) {
		this.proofPrinterVersion = proofPrinterVersion;
	}

	public String getReadCardVersion() {
		return readCardVersion;
	}

	public void setReadCardVersion(String readCardVersion) {
		this.readCardVersion = readCardVersion;
	}

	public String getDoorVersion() {
		return doorVersion;
	}

	public void setDoorVersion(String doorVersion) {
		this.doorVersion = doorVersion;
	}

	public String getKeyVersion() {
		return keyVersion;
	}

	public void setKeyVersion(String keyVersion) {
		this.keyVersion = keyVersion;
	}

	public String getBackDoorVersion() {
		return backDoorVersion;
	}

	public void setBackDoorVersion(String backDoorVersion) {
		this.backDoorVersion = backDoorVersion;
	}

	public String getCoreVersion() {
		return coreVersion;
	}

	public void setCoreVersion(String coreVersion) {
		this.coreVersion = coreVersion;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	
	public String getRunningPrinter() {
		return runningPrinter;
	}

	public void setRunningPrinter(String runningPrinter) {
		this.runningPrinter = runningPrinter;
	}

	public String getProofPrinter() {
		return proofPrinter;
	}

	public void setProofPrinter(String proofPrinter) {
		this.proofPrinter = proofPrinter;
	}

	public String getReadCard() {
		return readCard;
	}

	public void setReadCard(String readCard) {
		this.readCard = readCard;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBackDoor() {
		return backDoor;
	}

	public void setBackDoor(String backDoor) {
		this.backDoor = backDoor;
	}

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public String getOtherVersion() {
		return otherVersion;
	}

	public void setOtherVersion(String otherVersion) {
		this.otherVersion = otherVersion;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
