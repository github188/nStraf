package cn.grgbanking.feeltm.borrow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.borrow.dao.BorrowDao;
import cn.grgbanking.feeltm.borrow.domain.Borrow;
import cn.grgbanking.feeltm.borrow.domain.BorrowDetail;
import cn.grgbanking.feeltm.borrow.domain.BorrowRemind;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.util.Page;

@Service("borrowService")
@Transactional
public class BorrowService {
	@Autowired
	private BorrowDao borrowDao;
	
	@Transactional(readOnly = true)
	public Page getPage(Borrow borrow, int pageNum, int pageSize, String borrowTime1, String borrowTime2,UserModel userModel){
		return borrowDao.getPage(borrow, pageNum, pageSize, borrowTime1, borrowTime2, userModel);
	}

	public Borrow getById(String id) {
		return (Borrow) borrowDao.getById(id);
	}

	public void save(Borrow borrow) {
		borrowDao.addObject(borrow);
	}

	public void update(Borrow borrow) {
		borrowDao.updateObject(borrow);
	}

	public void delete(String id) {
		borrowDao.removeObject(Borrow.class, id);
	}
	
	public List<BorrowDetail> getBorrowDetailByBorrowId(String id) {
		return borrowDao.getBorrowDetailByBorrowId(id);
	}
	
	public void saveDetail(BorrowDetail detail) {
		borrowDao.addObject(detail);
	}
	
	public void saveRemind(BorrowRemind remind) {
		borrowDao.addObject(remind);
	}
	
	public void updateRemind(BorrowRemind remind) {
		borrowDao.updateObject(remind);
	}
	
	public BorrowRemind getRemindById() {
		return (BorrowRemind) borrowDao.getRemindById();
	}
	
	public List getRepayInfoById(String currentdate) {
		return borrowDao.getRepayInfoById(currentdate);
	}
}
