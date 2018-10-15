package com.znjf.app.model.zy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;
import com.tziba.model.zy.ProjectBasicInfo;
import com.znjf.app.model.zy.dao.ProjectBasicInfoDao;
import com.znjf.app.model.zy.service.ProjectBasicInfoService;

@Service
public class ProjectBasicInfoServiceImpl extends GenericServiceImpl<ProjectBasicInfo, String> implements
		ProjectBasicInfoService {

	@Autowired
	ProjectBasicInfoDao projectBasicInfoDao;

	
	@Override
	public GenericDao<ProjectBasicInfo, String> getDao() {
		return projectBasicInfoDao;
	}


	@Override
	public BigDecimal selectTotalTrade() {
		return projectBasicInfoDao.selectTotalTrade();
	}


	@Override
	public List<ProjectBasicInfo> selectRecommendProjects(Page<ProjectBasicInfo> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		return projectBasicInfoDao.selectRecommendProjects();
	}

	
	

}