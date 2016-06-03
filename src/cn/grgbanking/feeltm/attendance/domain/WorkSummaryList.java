package cn.grgbanking.feeltm.attendance.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="oa_worksummary")
public class WorkSummaryList {
	private String id;
	private String employeeid;
	private String employeename;
	private String dept;
	private Date begindate;
	private Date enddate;
	private double shouldattendhours;
	private double reallyattendhours;
	private double absencedays;
	private double enterdays;
	private double latehours;
	private double leaveearlyhours;
	private double absenteeismhours;
	private double casualleavedays;
	private double workleavedays;
	private double sickleavedays;
	private double yearleavedays;
	private double holidaydays;
	private double weddingleavedays;
	private double funeralleavedays;
	private double paternityleavedays;
	private double prenatalexaminationleavedays;
	private double maternityleavedays;
	private double familyplanningleavedays;
	private double breaestfeedingdays;
	private double medicaltreatmentperioddays;
	private double paidvacationdays;
	private double newyearleave;
	private double monthleavedays;
	private double businesstrip;
	private double outing;
	private double normalovertimehours;
	private double weekendovertimehours;
	private double legalholidayovertimehours;
	private double residualnormalovertimehours;
	private double residualweekendovertimehours;
	private double offsetot1;
	private double offsetot2;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="employeeid")
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	@Column(name="employeename")
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	@Column(name="dept")
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	@Column(name="begindate")
	public Date getBegindate() {
		return begindate;
	}
	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}
	@Column(name="enddate")
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	@Column(name="shouldattendhours")
	public double getShouldattendhours() {
		return shouldattendhours;
	}
	public void setShouldattendhours(double shouldattendhours) {
		this.shouldattendhours = shouldattendhours;
	}
	@Column(name="reallyattendhours")
	public double getReallyattendhours() {
		return reallyattendhours;
	}
	public void setReallyattendhours(double reallyattendhours) {
		this.reallyattendhours = reallyattendhours;
	}
	@Column(name="absencedays")
	public double getAbsencedays() {
		return absencedays;
	}
	public void setAbsencedays(double absencedays) {
		this.absencedays = absencedays;
	}
	@Column(name="enterdays")
	public double getEnterdays() {
		return enterdays;
	}
	public void setEnterdays(double enterdays) {
		this.enterdays = enterdays;
	}
	@Column(name="latehours")
	public double getLatehours() {
		return latehours;
	}
	public void setLatehours(double latehours) {
		this.latehours = latehours;
	}
	@Column(name="leaveearlyhours")
	public double getLeaveearlyhours() {
		return leaveearlyhours;
	}
	public void setLeaveearlyhours(double leaveearlyhours) {
		this.leaveearlyhours = leaveearlyhours;
	}
	@Column(name="absenteeismhours")
	public double getAbsenteeismhours() {
		return absenteeismhours;
	}
	public void setAbsenteeismhours(double absenteeismhours) {
		this.absenteeismhours = absenteeismhours;
	}
	@Column(name="casualleavedays")
	public double getCasualleavedays() {
		return casualleavedays;
	}
	public void setCasualleavedays(double casualleavedays) {
		this.casualleavedays = casualleavedays;
	}
	@Column(name="workleavedays")
	public double getWorkleavedays() {
		return workleavedays;
	}
	public void setWorkleavedays(double workleavedays) {
		this.workleavedays = workleavedays;
	}
	@Column(name="sickleavedays")
	public double getSickleavedays() {
		return sickleavedays;
	}
	public void setSickleavedays(double sickleavedays) {
		this.sickleavedays = sickleavedays;
	}
	@Column(name="yearleavedays")
	public double getYearleavedays() {
		return yearleavedays;
	}
	public void setYearleavedays(double yearleavedays) {
		this.yearleavedays = yearleavedays;
	}
	@Column(name="holidaydays")
	public double getHolidaydays() {
		return holidaydays;
	}
	public void setHolidaydays(double holidaydays) {
		this.holidaydays = holidaydays;
	}
	@Column(name="weddingleavedays")
	public double getWeddingleavedays() {
		return weddingleavedays;
	}
	public void setWeddingleavedays(double weddingleavedays) {
		this.weddingleavedays = weddingleavedays;
	}
	@Column(name="funeralleavedays")
	public double getFuneralleavedays() {
		return funeralleavedays;
	}
	public void setFuneralleavedays(double funeralleavedays) {
		this.funeralleavedays = funeralleavedays;
	}
	@Column(name="paternityleavedays")
	public double getPaternityleavedays() {
		return paternityleavedays;
	}
	public void setPaternityleavedays(double paternityleavedays) {
		this.paternityleavedays = paternityleavedays;
	}
	@Column(name="prenatalexaminationleavedays")
	public double getPrenatalexaminationleavedays() {
		return prenatalexaminationleavedays;
	}
	public void setPrenatalexaminationleavedays(double prenatalexaminationleavedays) {
		this.prenatalexaminationleavedays = prenatalexaminationleavedays;
	}
	@Column(name="maternityleavedays")
	public double getMaternityleavedays() {
		return maternityleavedays;
	}
	public void setMaternityleavedays(double maternityleavedays) {
		this.maternityleavedays = maternityleavedays;
	}
	@Column(name="familyplanningleavedays")
	public double getFamilyplanningleavedays() {
		return familyplanningleavedays;
	}
	public void setFamilyplanningleavedays(double familyplanningleavedays) {
		this.familyplanningleavedays = familyplanningleavedays;
	}
	@Column(name="breaestfeedingdays")
	public double getBreaestfeedingdays() {
		return breaestfeedingdays;
	}
	public void setBreaestfeedingdays(double breaestfeedingdays) {
		this.breaestfeedingdays = breaestfeedingdays;
	}
	@Column(name="medicaltreatmentperioddays")
	public double getMedicaltreatmentperioddays() {
		return medicaltreatmentperioddays;
	}
	public void setMedicaltreatmentperioddays(double medicaltreatmentperioddays) {
		this.medicaltreatmentperioddays = medicaltreatmentperioddays;
	}
	@Column(name="paidvacationdays")
	public double getPaidvacationdays() {
		return paidvacationdays;
	}
	public void setPaidvacationdays(double paidvacationdays) {
		this.paidvacationdays = paidvacationdays;
	}
	@Column(name="newyearleave")
	public double getNewyearleave() {
		return newyearleave;
	}
	public void setNewyearleave(double newyearleave) {
		this.newyearleave = newyearleave;
	}
	@Column(name="monthleavedays")
	public double getMonthleavedays() {
		return monthleavedays;
	}
	public void setMonthleavedays(double monthleavedays) {
		this.monthleavedays = monthleavedays;
	}
	@Column(name="businesstrip")
	public double getBusinesstrip() {
		return businesstrip;
	}
	public void setBusinesstrip(double businesstrip) {
		this.businesstrip = businesstrip;
	}
	@Column(name="outing")
	public double getOuting() {
		return outing;
	}
	public void setOuting(double outing) {
		this.outing = outing;
	}
	@Column(name="normalovertimehours")
	public double getNormalovertimehours() {
		return normalovertimehours;
	}
	public void setNormalovertimehours(double normalovertimehours) {
		this.normalovertimehours = normalovertimehours;
	}
	@Column(name="weekendovertimehours")
	public double getWeekendovertimehours() {
		return weekendovertimehours;
	}
	public void setWeekendovertimehours(double weekendovertimehours) {
		this.weekendovertimehours = weekendovertimehours;
	}
	@Column(name="legalholidayovertimehours")
	public double getLegalholidayovertimehours() {
		return legalholidayovertimehours;
	}
	public void setLegalholidayovertimehours(double legalholidayovertimehours) {
		this.legalholidayovertimehours = legalholidayovertimehours;
	}
	@Column(name="residualnormalovertimehours")
	public double getResidualnormalovertimehours() {
		return residualnormalovertimehours;
	}
	public void setResidualnormalovertimehours(double residualnormalovertimehours) {
		this.residualnormalovertimehours = residualnormalovertimehours;
	}
	@Column(name="residualweekendovertimehours")
	public double getResidualweekendovertimehours() {
		return residualweekendovertimehours;
	}
	public void setResidualweekendovertimehours(double residualweekendovertimehours) {
		this.residualweekendovertimehours = residualweekendovertimehours;
	}
	@Column(name="offsetot1")
	public double getOffsetot1() {
		return offsetot1;
	}
	public void setOffsetot1(double offsetot1) {
		this.offsetot1 = offsetot1;
	}
	@Column(name="offsetot2")
	public double getOffsetot2() {
		return offsetot2;
	}
	public void setOffsetot2(double offsetot2) {
		this.offsetot2 = offsetot2;
	}
}
