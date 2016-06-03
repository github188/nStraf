package cn.grgbanking.feeltm.costControl.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="OA_COSTCONTROL_DATEINCOME")
public class DateIncomeManage {
	
	  
	   @Id
		@GeneratedValue(generator="system-uuid")
		@GenericGenerator(name="system-uuid",strategy="uuid.hex")
		@Column(name="C_DATEINCOMEID ",unique=true,nullable=false)
	   private String dateIncomeId;//人日收入的id
	   
	   @Column(name="C_PORJECTID",nullable=false)
	   private String projectId;//项目名称的id
		
	   @Column(name="C_PRJGROUP",nullable=false)
		private String prjGroup;//项目组
	   
	   @Column(name="D_STARTTIME")
		private Date startTime;//起始时间
	   
	   @Column(name="D_ENDTIME")
		private Date endTime;//结束时间
	   
	   @Column(name="N_DATEINCOME",nullable=false)
		private Double dateIncome;//人日收入
	   
	   
	   @Column(name="C_ENTRYPEOPLE")
		private String  entryPeople;//录入人
	   
	   @Column(name="D_ENTRYTIME")
		private Date  entryTime;//录入时间
	   
	   @Column(name="C_EXIT1")
		private String  exit1;//备用属性1
	   
	   @Column(name="C_EXIT2")
		private String  exit2;//备用属性2
	   
	   @Column(name="C_EXIT3")
		private String  exit3;//备用属性3
	   
	   @Column(name="C_EXIT4")
		private String  exit4;//备用属性4

		public DateIncomeManage() {
			super();
		}

		public DateIncomeManage(String dateIncomeId, String projectId,
				String prjGroup, Date startTime, Date endTime,
				Double dateIncome, String entryPeople, Date entryTime,
				String exit1, String exit2, String exit3, String exit4) {
			super();
			this.dateIncomeId = dateIncomeId;
			this.projectId = projectId;
			this.prjGroup = prjGroup;
			this.startTime = startTime;
			this.endTime = endTime;
			this.dateIncome = dateIncome;
			this.entryPeople = entryPeople;
			this.entryTime = entryTime;
			this.exit1 = exit1;
			this.exit2 = exit2;
			this.exit3 = exit3;
			this.exit4 = exit4;
		}

		public String getDateIncomeId() {
			return dateIncomeId;
		}

		public void setDateIncomeId(String dateIncomeId) {
			this.dateIncomeId = dateIncomeId;
		}

		public String getProjectId() {
			return projectId;
		}

		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}

		public String getPrjGroup() {
			return prjGroup;
		}

		public void setPrjGroup(String prjGroup) {
			this.prjGroup = prjGroup;
		}

		public Date getStartTime() {
			return startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		public Double getDateIncome() {
			return dateIncome;
		}

		public void setDateIncome(Double dateIncome) {
			this.dateIncome = dateIncome;
		}

		public String getEntryPeople() {
			return entryPeople;
		}

		public void setEntryPeople(String entryPeople) {
			this.entryPeople = entryPeople;
		}

		public Date getEntryTime() {
			return entryTime;
		}

		public void setEntryTime(Date entryTime) {
			this.entryTime = entryTime;
		}

		public String getExit1() {
			return exit1;
		}

		public void setExit1(String exit1) {
			this.exit1 = exit1;
		}

		public String getExit2() {
			return exit2;
		}

		public void setExit2(String exit2) {
			this.exit2 = exit2;
		}

		public String getExit3() {
			return exit3;
		}

		public void setExit3(String exit3) {
			this.exit3 = exit3;
		}

		public String getExit4() {
			return exit4;
		}

		public void setExit4(String exit4) {
			this.exit4 = exit4;
		}

		
}
