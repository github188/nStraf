package cn.grgbanking.feeltm.personDay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.project.service.ProjectService;

@Service
public class ProjectPersonDayStatisticService {
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private DayLogDao daylogDao;
}
