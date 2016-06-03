package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "testrecord")
public class TestRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="SUMBITPLANDATE")
	private Date SumbitPlanDate;

	@Temporal(TemporalType.DATE)
	@Column(name="ACTUALSUMBITDATE")
	private Date ActualSumbitDate;

	@Column(name = "PROJECTNAME")
	private String ProjectName;

	@Column(name = "PROJECTTYPE")
	private String ProjectType;

	@Column(name = "PROJECTMANAGER")
	private String ProjectManager;

	@Column(name = "TESTVER")
	private String TestVer;

	@Column(name = "VERTYPE")
	private String VerType;

	@Column(name = "TESTSTATUS")
	private String TestStatus;
	
	@Column(name = "SUMBITPROCESS")
	private String SumbitProcess;
	
    @Temporal(TemporalType.DATE)
	@Column(name="TESTFINISHDATE")
	private Date TestFinishDate;
    
    @Temporal(TemporalType.DATE)
	@Column(name="TESTSTARTDATE")
	private Date TestStartDate;

	@Column(name="FINDBUGSUM")
	private Integer FindBugSum;

    @Column(name="TESTERSUM")
	private Integer TesterSum;

    @Column(name="TESTTIMESUM")
	private Double TestTimeSum;

    @Column(name="WORKLOAD")
	private Double WorkLoad;

    @Column(name = "TESTER")
	private String Tester;
        
    @Column(name = "REMARK")
    private String Remark;
    
    @Column(name = "REMARK_2")
    private String Remark2;
    
    @Column(name = "OANUM")
    private String OaNum;

	@Column(name = "update_man")
	private String updateMan;

	@Column(name = "update_date")
	private String updateDate;
	
	@Column(name = "create_date")
	private String createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String ProjectName) {
		this.ProjectName = ProjectName;
	}

	public String getProjectType() {
		return ProjectType;
	}

	public void setProjectType(String ProjectType) {
		this.ProjectType = ProjectType;
	}

	public String getProjectManager() {
		return ProjectManager;
	}

	public void setProjectManager(String ProjectManager) {
		this.ProjectManager = ProjectManager;
	}

	public String getTestVer() {
		return TestVer;
	}

	public void setTestVer(String TestVer) {
		this.TestVer = TestVer;
	}

	public String getVerType() {
		return VerType;
	}

	public void setVerType(String VerType) {
		this.VerType = VerType;
	}

	public String getTestStatus() {
		return TestStatus;
	}

	public void setTestStatus(String TestStatus) {
		this.TestStatus = TestStatus;
	}

	public Date getSumbitPlanDate() {
		return SumbitPlanDate;
	}

	public void setSumbitPlanDate(Date SumbitPlanDate) {
		this.SumbitPlanDate = SumbitPlanDate;
	}

        public Date getActualSumbitDate() {
		return ActualSumbitDate;
	}

	public void setActualSumbitDate(Date ActualSumbitDate) {
		this.ActualSumbitDate = ActualSumbitDate;
	}

	public String getSumbitProcess() {
		return SumbitProcess;
	}

	public void setSumbitProcess(String SumbitProcess) {
		this.SumbitProcess = SumbitProcess;
	}

        public Date getTestFinishDate() {
		return TestFinishDate;
	}

	public void setTestFinishDate(Date TestFinishDate) {
		this.TestFinishDate = TestFinishDate;
	}




        public Double getTestTimeSum() {
		return TestTimeSum;
	}

	public void setTestTimeSum(Double TestTimeSum) {
		this.TestTimeSum = TestTimeSum;
	}

        public Double getWorkLoad() {
		return WorkLoad;
	}

	public void setWorkLoad(Double WorkLoad) {
		this.WorkLoad = WorkLoad;
	}

        public String getTester() {
		return Tester;
	}

	public void setTester(String Tester) {
		this.Tester = Tester;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getFindBugSum() {
		return FindBugSum;
	}

	public void setFindBugSum(Integer findBugSum) {
		FindBugSum = findBugSum;
	}

	public Integer getTesterSum() {
		return TesterSum;
	}

	public void setTesterSum(Integer testerSum) {
		TesterSum = testerSum;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getOaNum() {
		return OaNum;
	}

	public void setOaNum(String oaNum) {
		OaNum = oaNum;
	}

	public String getRemark2() {
		return Remark2;
	}

	public void setRemark2(String remark2) {
		Remark2 = remark2;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getTestStartDate() {
		return TestStartDate;
	}

	public void setTestStartDate(Date testStartDate) {
		TestStartDate = testStartDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
