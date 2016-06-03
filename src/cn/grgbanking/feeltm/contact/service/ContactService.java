package cn.grgbanking.feeltm.contact.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.contact.dao.ContactDAO;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrContacts;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.BeanUtils;
import cn.grgbanking.framework.util.Page;

@Service("contactService")
@Transactional
public class ContactService extends BaseService {
	@Autowired
	private ContactDAO contactDao;
	
	@Transactional(readOnly=true)
	public Page getContactPage(int pageNum,int pageSize){
		return contactDao.getPage(pageNum, pageSize);
	}
	
	public ContactDAO getContactDao() {
		return contactDao;
	}

	public void setContactDao(ContactDAO contactDao) {
		this.contactDao = contactDao;
	}

	@Transactional(readOnly=true)
	public Object[] getContactInfoByUser(String userId) {
		List list = contactDao.getContactInfoByUser(userId);
		if(list!=null&&list.size()==1){
			return (Object[]) list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public UsrContacts getContactById(String ids) {
		return (UsrContacts) contactDao.getObject(UsrContacts.class, ids);
	}

	@Transactional(readOnly = true)
	public UsrContacts getContactByUserId(String userId) {
		return (UsrContacts) contactDao.findByUserName(userId);
	}
	
	@Transactional(readOnly = true)
	public Page getContactPageBycondition(UsrContacts usrContact, String orderField, String regulation,
			int pageNum, int pageSize) {

		return contactDao.getContactPageBycondition(usrContact,orderField,regulation,pageNum,pageSize);
	}

	@Transactional(readOnly = true)
	public List<Object> getNames(UsrContacts usrContact) {
		return contactDao.getNamesByDeptGroup(usrContact.getDeptName(),usrContact.getGroupName());
	}

	@Transactional(readOnly = true)
	public boolean isExitsContactInfo(UsrContacts usrcontact) {
		UsrContacts contact = contactDao.findByUserName(usrcontact.getUserId());
		if(contact !=null){
			return true;
		}
		return false;
	}

	public boolean addContactInfo(UsrContacts usrcontact) {
		boolean flag = false;
		try {
			contactDao.addObject(usrcontact);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	public boolean updateContactInfo(UsrContacts usrcontact) {
		boolean flag = false;
		try {
			contactDao.updateObject(usrcontact);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public List getNoContacts(UsrContacts usrContact, String deptValue) {
		
		return contactDao.getNoContacts(usrContact.getDeptName(),usrContact.getGroupName(),deptValue);
	}

	public List<UsrGroup> getALlusrgroup() {
		
		return contactDao.getAllUserGroup();
	}

	public boolean delete(UsrContacts temp) {
		try{
			contactDao.removeObject(temp.getClass(), temp.getId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return false;
	}

	public boolean isExitsUserInfo(UsrContacts usrContacts) {
		List contact = contactDao.getContactInfoByUser(usrContacts.getUserId());
		if(contact.size()>0){
			return true;
		}
		return false;
	}

	public boolean addContactListInfo(List<UsrContacts> contactSaveList) {
		boolean flag = false;
		try {
			for(UsrContacts usrcontact:contactSaveList)
				contactDao.addObject(usrcontact);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	public boolean updateContactListInfo(List<UsrContacts> contactUpdateList) {
		boolean flag = false;
		try {
			for(UsrContacts usrcontact:contactUpdateList){
				UsrContacts usr = contactDao.findByUserName(usrcontact.getUserId());
				usrcontact.setId(usr.getId());
				BeanUtils.copyProperties(usr, usrcontact);
				contactDao.updateObject(usr);
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	/**
	 * @param mobile
	 * @param tel
	 * @param userid
	 * lhyan3
	 * 2014年6月15日
	 */
	public void saveNewConstact(String name,
			SysUser staff) {
		UsrContacts usrContacts = new UsrContacts();
		usrContacts.setUserId(staff.getUserid());
		usrContacts.setConName(staff.getUsername());
		usrContacts.setConMobile(staff.getMobile());
		usrContacts.setConTel(staff.getTel());
		usrContacts.setUpdateManId(name);
		usrContacts.setUpdate(new Date());
		contactDao.addObject(usrContacts);
	}

	/**
	 * @param user
	 * lhyan3
	 * 2014年6月15日
	 */
	@SuppressWarnings("unchecked")
	public void updateContactByUser(SysUser user,UserModel userModel) {
			UsrContacts contacts = contactDao.findByUserName(user.getUserid());
			if(contacts!=null){
				contacts.setConEmail(user.getEmail());
				contacts.setConMobile(user.getMobile());
				contacts.setConTel(user.getTel());
				contacts.setUpdate(new Date());
				contacts.setUpdateManId(userModel.getUsername());
				this.updateContactInfo(contacts);
		}else{
			contacts = new UsrContacts();
			contacts.setConEmail(user.getEmail());
			contacts.setConMobile(user.getMobile());
			contacts.setConName(user.getUsername());
			contacts.setConTel(user.getTel());
			contacts.setDeptName(user.getDeptName());
			contacts.setGroupName(user.getGroupName());
			contacts.setNote("修改人员信息联系方式同步添加通讯录");
			contacts.setUpdate(new Date());
			contacts.setUpdateManId(userModel.getUsername());
			contacts.setUserId(user.getUserid());
			this.addContactInfo(contacts);
		}
	}

}
