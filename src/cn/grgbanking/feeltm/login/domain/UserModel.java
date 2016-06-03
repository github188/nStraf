package cn.grgbanking.feeltm.login.domain;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import cn.grgbanking.feeltm.util.Constants;

@SuppressWarnings("unchecked")
public class UserModel {
	private String userid;
	private String username;

	/**
	 * ��¼������l��id
	 */
	private String loginid;
	// ��������id
	private String orgid;
	// ��������id
	private String areaid;
	// ��������ļ���
	private int areaLevel;
	// �Ƿ�����ܼ���
	private String orgLevel;
	private HashMap role;
	private String orgfloor;
	private Integer level;
	private String deptName;

	/**
	 * 
	 * �û�Ȩ�޼���
	 */
	private HashMap operHash;
	/**
	 * ��¼�û������Ⱥ��
	 */
	private String[] groupids;
	
	private String groupName;

	/**
	 * @return Returns the groupids.
	 */
	public String[] getGroupids() {
		return groupids;
	}

	/**
	 * @param groupids
	 *            The groupids to set.
	 */
	public void setGroupids(String[] groupids) {
		this.groupids = groupids;
	}

	/**
	 * @return Returns the loginid.
	 */
	public String getLoginid() {
		return loginid;
	}

	/**
	 * @param loginid
	 *            The loginid to set.
	 */
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	/**
	 * @return Returns the operHash.
	 */
	public HashMap getOperHash() {
		return operHash;
	}

	/**
	 * @param operHash
	 *            The operHash to set.
	 */
	public void setOperHash(HashMap operHash) {
		this.operHash = operHash;
	}

	/**
	 * @return Returns the userid.
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            The userid to set.
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	// ------------------------------------------------------------------------------
	/**
	 * ���ص�¼�û�����
	 * 
	 * @param request
	 * @return
	 */
	public static final UserModel getLoginUser(HttpServletRequest request) {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		return loginUser;
	}

	// ------------------------------------------------------------------------------
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}

	public HashMap getRole() {
		return role;
	}

	public void setRole(HashMap role) {
		this.role = role;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
