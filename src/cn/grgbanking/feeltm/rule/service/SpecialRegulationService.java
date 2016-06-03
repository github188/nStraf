package cn.grgbanking.feeltm.rule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SpecialRegulation;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.rule.dao.SpecialRegulationDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class SpecialRegulationService extends BaseService {
	@Autowired
	private SpecialRegulationDao specialRegulationDao;

	public boolean addSpecialRegulation(SpecialRegulation obj) {
		System.out.println("1");
		boolean flag = false;
		try {
			specialRegulationDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteSpecialRegulation(SpecialRegulation obj) {
		boolean flag = false;
		try {
			specialRegulationDao.removeObject(SpecialRegulation.class, obj
					.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateSpecialRegulation(SpecialRegulation obj) {
		boolean flag = false;
		try {
			specialRegulationDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getSpecialRegulationPage(Object[] obj, int pageNum, int pageSize) {
		String sql = "  from SpecialRegulation ";
		return specialRegulationDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getSpecialRegulationList(Object[] obj) {
		String sql = "  from SpecialRegulation ";
		return specialRegulationDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public SpecialRegulation getSpecialRegulationObject(String id) {
		return (SpecialRegulation) specialRegulationDao.getObject(
				SpecialRegulation.class, id);
	}

	@Transactional(readOnly = true)
	public Page getSpecialRegulationPage(SpecialRegulation specialRegulation,
			int pageNum, int pageSize) {
		String sql = " from SpecialRegulation s ";
		boolean flag = false;
		String source = specialRegulation.getSource();
		if (source != null && !"".equals(source)) {
			if (flag) {
				sql += " and s.source ='" + source + "' ";
			} else {
				sql += " where s.source='" + source + "' ";
			}
			flag = true;
		}
		sql += "order by s.createDate desc";
		return specialRegulationDao.getObjectPage(sql, pageNum, pageSize);
	}
}
