package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 *  create table device_config(
 ID          VARCHAR2(32) primary key,
 atm_core     VARCHAR2(128),
 read_card     VARCHAR2(128),
 key           VARCHAR2(128),
 running_printer  VARCHAR2(128),
 proof_printer    VARCHAR2(128),   --凭条打印机
 core_door         VARCHAR2(128),   --机芯门
 backdoor_maintance VARCHAR2(128),--后台维护
 dvr           VARCHAR2(40),
 dispatcher   VARCHAR2(128),   --分配器
 moxa_card    VARCHAR2(32),   --MOXA卡
 touch_screen VARCHAR2(32)        --触摸屏
 )
 */
@Entity
@Table(name = "device_config")
public class DeviceConfig {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "atm_core")
	private String atmCore;
	private String  core_trans_type ;
	private String  core_pc_sata ;
	private String  core_port ;  

	
	@Column(name = "read_card")
	private String readCard;
	private String  read_trans_type ;
	private String  read_pc_sata    ;
	private String read_port     ;

	private String key;
	private String  key_trans_type  ;
	private String key_pc_sata    ;
	private String key_port     ;

	@Column(name = "running_printer")
	private String runningPrinter;
	private String running_trans_type  ;
	private String  running_pc_sata    ;
	private String  running_port     ;

	@Column(name = "proof_printer")
	private String proofPrinter;
	private String  proof_trans_type  ;
	private String  proof_pc_sata   ;
	private String proof_port      ;

	@Column(name = "core_door")
	private String coreDoor;
	private String door_trans_type  ;
	private String door_pc_sata    ;
	private String door_port      ;


	@Column(name = "backdoor_maintance")
	private String backdoor;
	private String back_trans_type ;
	private String back_pc_sata     ;
	private String back_port      ;


	private String dvr;

	private String dispatcher;
	private String  dispatcher_trans_type  ;
	private String  dispatcher_pc_sata   ;
	private String dispatcher_port      ;

	@Column(name = "moxa_card")
	private String moxaCard;

	@Column(name = "touch_screen")
	private String touchScreen;

	public DeviceConfig(){
		
	}
	public DeviceConfig(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAtmCore() {
		return atmCore;
	}

	public void setAtmCore(String atmCore) {
		this.atmCore = atmCore;
	}

	public String getReadCard() {
		return readCard;
	}

	public void setReadCard(String readCard) {
		this.readCard = readCard;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getCoreDoor() {
		return coreDoor;
	}

	public void setCoreDoor(String coreDoor) {
		this.coreDoor = coreDoor;
	}

	public String getBackdoor() {
		return backdoor;
	}

	public void setBackdoor(String backdoor) {
		this.backdoor = backdoor;
	}

	public String getDvr() {
		return dvr;
	}

	public void setDvr(String dvr) {
		this.dvr = dvr;
	}

	public String getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(String dispatcher) {
		this.dispatcher = dispatcher;
	}

	public String getMoxaCard() {
		return moxaCard;
	}

	public void setMoxaCard(String moxaCard) {
		this.moxaCard = moxaCard;
	}

	public String getTouchScreen() {
		return touchScreen;
	}

	public void setTouchScreen(String touchScreen) {
		this.touchScreen = touchScreen;
	}
	

	public String getCore_trans_type() {
		return core_trans_type;
	}
	public void setCore_trans_type(String coreTransType) {
		core_trans_type = coreTransType;
	}
	public String getCore_pc_sata() {
		return core_pc_sata;
	}
	public void setCore_pc_sata(String corePcSata) {
		core_pc_sata = corePcSata;
	}
	public String getCore_port() {
		return core_port;
	}
	public void setCore_port(String corePort) {
		core_port = corePort;
	}
	public String getRead_trans_type() {
		return read_trans_type;
	}
	public void setRead_trans_type(String readTransType) {
		read_trans_type = readTransType;
	}
	public String getRead_pc_sata() {
		return read_pc_sata;
	}
	public void setRead_pc_sata(String readPcSata) {
		read_pc_sata = readPcSata;
	}
	public String getRead_port() {
		return read_port;
	}
	public void setRead_port(String readPort) {
		read_port = readPort;
	}
	public String getKey_trans_type() {
		return key_trans_type;
	}
	public void setKey_trans_type(String keyTransType) {
		key_trans_type = keyTransType;
	}
	public String getKey_pc_sata() {
		return key_pc_sata;
	}
	public void setKey_pc_sata(String keyPcSata) {
		key_pc_sata = keyPcSata;
	}
	public String getKey_port() {
		return key_port;
	}
	public void setKey_port(String keyPort) {
		key_port = keyPort;
	}
	public String getRunning_trans_type() {
		return running_trans_type;
	}
	public void setRunning_trans_type(String runningTransType) {
		running_trans_type = runningTransType;
	}
	public String getRunning_pc_sata() {
		return running_pc_sata;
	}
	public void setRunning_pc_sata(String runningPcSata) {
		running_pc_sata = runningPcSata;
	}
	public String getRunning_port() {
		return running_port;
	}
	public void setRunning_port(String runningPort) {
		running_port = runningPort;
	}
	public String getProof_trans_type() {
		return proof_trans_type;
	}
	public void setProof_trans_type(String proofTransType) {
		proof_trans_type = proofTransType;
	}
	public String getProof_pc_sata() {
		return proof_pc_sata;
	}
	public void setProof_pc_sata(String proofPcSata) {
		proof_pc_sata = proofPcSata;
	}
	public String getProof_port() {
		return proof_port;
	}
	public void setProof_port(String proofPort) {
		proof_port = proofPort;
	}
	public String getDoor_trans_type() {
		return door_trans_type;
	}
	public void setDoor_trans_type(String doorTransType) {
		door_trans_type = doorTransType;
	}
	public String getDoor_pc_sata() {
		return door_pc_sata;
	}
	public void setDoor_pc_sata(String doorPcSata) {
		door_pc_sata = doorPcSata;
	}
	public String getDoor_port() {
		return door_port;
	}
	public void setDoor_port(String doorPort) {
		door_port = doorPort;
	}
	public String getBack_trans_type() {
		return back_trans_type;
	}
	public void setBack_trans_type(String backTransType) {
		back_trans_type = backTransType;
	}
	public String getBack_pc_sata() {
		return back_pc_sata;
	}
	public void setBack_pc_sata(String backPcSata) {
		back_pc_sata = backPcSata;
	}
	public String getBack_port() {
		return back_port;
	}
	public void setBack_port(String backPort) {
		back_port = backPort;
	}
	public String getDispatcher_trans_type() {
		return dispatcher_trans_type;
	}
	public void setDispatcher_trans_type(String dispatcherTransType) {
		dispatcher_trans_type = dispatcherTransType;
	}
	public String getDispatcher_pc_sata() {
		return dispatcher_pc_sata;
	}
	public void setDispatcher_pc_sata(String dispatcherPcSata) {
		dispatcher_pc_sata = dispatcherPcSata;
	}
	public String getDispatcher_port() {
		return dispatcher_port;
	}
	public void setDispatcher_port(String dispatcherPort) {
		dispatcher_port = dispatcherPort;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
